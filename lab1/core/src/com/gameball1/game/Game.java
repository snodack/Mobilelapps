package com.gameball1.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Field field;
	World world;
	Box2DDebugRenderer renderer;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		Box2D.init();
		shapeRenderer = new ShapeRenderer();
		world = new World(new Vector2(0,0), true);
		Gdx.input.setInputProcessor(new GameInputProcessor(this));
		set_borders();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1/60f,6, 2);
		Array<Body> arr = new Array<Body>();
		world.getBodies(arr);
		//world.setGravity(new Vector2(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX()));
		world.setGravity(new Vector2(0, -10));
		for (Body bd:arr) {
			if (bd.getType() == BodyDef.BodyType.StaticBody) continue;
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.circle(bd.getPosition().x, bd.getPosition().y, 100f);
			shapeRenderer.end();
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.circle(bd.getPosition().x, bd.getPosition().y, 100f);
			shapeRenderer.end();

		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void new_ball(int x, int y){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, Gdx.graphics.getHeight() - y);
		Body body = world.createBody(bodyDef);
		CircleShape circle = new CircleShape();
		circle.setRadius(100f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 1f;
		Fixture fixture = body.createFixture(fixtureDef);
		MassData ms = new MassData();
		ms = body.getMassData();
		ms.mass = 68000f;
		circle.dispose();
	}
	public void set_borders(){
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(Gdx.graphics.getWidth() / 2, -5);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(Gdx.graphics.getWidth() / 2, 5f);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()+5);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(Gdx.graphics.getWidth() / 2, 5f);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(-5, Gdx.graphics.getHeight()/2);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(5f, Gdx.graphics.getHeight()/2);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
		{
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.position.set(Gdx.graphics.getWidth() + 5, Gdx.graphics.getHeight()/2);
			Body groundBody = world.createBody(groundBodyDef);
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(5f, Gdx.graphics.getHeight()/2);
			groundBody.createFixture(groundBox, 0.0f);
			groundBox.dispose();
		}
	}

}
