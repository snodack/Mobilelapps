package com.gameball1.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.*;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	Box2DDebugRenderer renderer;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	int WIDTH = 0;
	int HEIGHT = 0;
	@Override
	public void create () {
		Box2D.init();
		shapeRenderer = new ShapeRenderer();
		world = new World(new Vector2(0,0), false);
		Gdx.input.setInputProcessor(new GameInputProcessor(this));
		WIDTH = Gdx.graphics.getWidth() / 64;
		HEIGHT = Gdx.graphics.getHeight() / 64;
		set_borders();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1/60f,6, 2);
		Array<Body> arr = new Array<Body>();
		world.getBodies(arr);
		world.setGravity(new Vector2(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX()));
		//world.setGravity(new Vector2(0, -10));
		for (Body bd : arr) {
			if (bd.getType() == BodyDef.BodyType.StaticBody) continue;
			Ball ball = (Ball)bd.getUserData();
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.circle(bd.getPosition().x * Gdx.graphics.getWidth()/(float)WIDTH, bd.getPosition().y* Gdx.graphics.getHeight()/(float)HEIGHT, ball.Radius * Gdx.graphics.getHeight()/(float)HEIGHT/2);
			shapeRenderer.end();
			shapeRenderer.setColor(ball.Color);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.circle(bd.getPosition().x * Gdx.graphics.getWidth()/(float)WIDTH, bd.getPosition().y*Gdx.graphics.getHeight()/(float)HEIGHT, ball.Radius*1.9f* Gdx.graphics.getHeight()/(float)HEIGHT/2);
			shapeRenderer.end();
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void new_ball(int x, int y){
		x = (int)(x * WIDTH/(float)Gdx.graphics.getWidth());
		y = (int)(y * HEIGHT/(float)Gdx.graphics.getHeight());
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, HEIGHT - y);

		Body body = world.createBody(bodyDef);
		Ball b = new Ball();
		b.Color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
		b.Radius = (float)(MathUtils.random(1.0f, 3.0f));
		body.setUserData(b);
		CircleShape circle = new CircleShape();
		circle.setRadius(b.Radius);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.8f;
		Fixture fixture = body.createFixture(fixtureDef);
		MassData ms = body.getMassData();
		ms.mass = 68000f;
		circle.dispose();
	}

	public void set_borders(){
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(WIDTH / 2, -5);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(WIDTH / 2, 5f);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(WIDTH / 2, HEIGHT+5);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(WIDTH/ 2, 5f);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(-5, HEIGHT/2);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(5f, HEIGHT/2);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(WIDTH + 5, HEIGHT/2);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(5f, HEIGHT/2);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
	}

}
