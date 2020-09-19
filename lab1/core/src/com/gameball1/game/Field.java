package com.gameball1.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
public class Field {
    ShapeRenderer renderer = new ShapeRenderer();
    Ball hero = new Ball(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    public void update(){
        hero.velocity_y = -(float) (Gdx.input.getAccelerometerX());
        hero.velocity_x = Gdx.input.getAccelerometerY();
        hero.x += hero.velocity_x;
        hero.y += hero.velocity_y;
        if (hero.x < 0) hero.x = 0;
        if (hero.y < 0) hero.y = 0;
        if (hero.y > Gdx.graphics.getHeight()) hero.y = Gdx.graphics.getHeight();
        if (hero.x > Gdx.graphics.getWidth()) hero.x = Gdx.graphics.getWidth();

    }
    public void render(){
        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeType.Filled);
        renderer.circle(hero.x, hero.y, 20);
        renderer.end();
    }
}
