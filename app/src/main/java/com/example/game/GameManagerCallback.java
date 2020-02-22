package com.example.game;

import android.graphics.Rect;

import com.example.game.sprites.Obstacle;

import java.util.ArrayList;

public interface GameManagerCallback {

    void updatePosition(Rect birdPosition);
    // For each obstacle there are two rectangles and is possible to have more than one obstacle in the screen so we need the list
    void updatePosition(Obstacle obstacle, ArrayList<Rect> positions);
    void removeObstacle(Obstacle obstacle);
}
