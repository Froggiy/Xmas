package io.github.some_example_name;

import static io.github.some_example_name.Main.SCR_HEIGHT;
import static io.github.some_example_name.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Flake {
    float x, y;
    float xStep, yStep;
    boolean isHit = false;
    boolean hasHitten = false;
    Texture image;

    public Flake(Texture img,float x, float y) {
        this.image = img;
        this.x = x;
        xStep = MathUtils.random(-0.5f, 0.5f);
        this.y = y;
        yStep = -1;
    }

    public void fly(){
        if (x < -50) x = SCR_WIDTH;
        else if (x > SCR_WIDTH) x = -50;
        if (y < -50) y = SCR_HEIGHT+100;
        x += xStep;
        y += yStep;

    }
    public boolean hit(float tx,float ty){
        return x<tx && tx<x+50 && y<ty && ty<y+50;
    }
}
