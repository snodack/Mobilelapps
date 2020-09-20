package com.gameball1.game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Ball {
    public Ball(float _x, float _y){
        x = _x;
        y = _y;
    }
    float x;
    float y;
    float velocity_x = 0;
    float velocity_y = 0;
    Vector2 acceleration;

}
