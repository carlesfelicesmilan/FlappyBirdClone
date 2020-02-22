package com.example.game.sprites;

import android.graphics.Canvas;

public interface Sprite {
    //draw our sprite in the canvas
    void draw(Canvas canvas);
    void update();

}
