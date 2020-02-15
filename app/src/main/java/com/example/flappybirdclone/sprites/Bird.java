package com.example.flappybirdclone.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flappybirdclone.R;

public class Bird implements Sprite {

    private Bitmap bird_down;
    private Bitmap bird_up;
    private int birdX, birdY, birdWidth, birdHeight;
    private float gravity;
    private float currentFallingSpeed;
    private float flappyBoost;

    // Setup the initial position of the bird (birdX) based on dimens.xml
    // We load our image and scale it to our desired dimensions based on the device size (birdWith and birdHeight)
    public Bird(Resources resources) {
        birdX = (int) resources.getDimension(R.dimen.bird_x);
        birdWidth = (int) resources.getDimension(R.dimen.bird_width);
        birdHeight = (int) resources.getDimension(R.dimen.bird_height);
        gravity = resources.getDimension(R.dimen.gravity);
        flappyBoost = (int) resources.getDimension(R.dimen.flappy_boost);

        Bitmap birdBmpDown = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
        bird_down = Bitmap.createScaledBitmap(birdBmpDown, birdWidth, birdHeight, false);

        Bitmap birdBmpUp = BitmapFactory.decodeResource(resources, R.drawable.bird_up);
        bird_up = Bitmap.createScaledBitmap(birdBmpUp, birdWidth, birdHeight, false);
    }

    @Override
    // Draw the position of the bird in the canvas depending if the bird is falling or going up
    // if Y axis is negative (birdY) means that our bird is going up, we know that by the currentFallingSpeed
    public void draw(Canvas canvas) {
        if (currentFallingSpeed < 0) {
            canvas.drawBitmap(bird_up, birdX, birdY, null);
        } else {
            canvas.drawBitmap(bird_down, birdX, birdY, null);
        }
    }

    @Override
    // Update the Y position with the falling speed
    // Update the current forward speed with the gravity
    public void update() {
        birdY += currentFallingSpeed;
        currentFallingSpeed += gravity;
    }

    // When we touch the screen we add the flappyBoost to our Falling speed
    public void onTouchEvent() {
        currentFallingSpeed = flappyBoost;
    }
}
