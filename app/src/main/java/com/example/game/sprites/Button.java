package com.example.game.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.game.GameManagerCallback;
import com.example.game.R;

public class Button implements Sprite{

    private Bitmap optionsButton;
    private int screenHeight, screenWidth;
    private GameManagerCallback callback;
    private boolean optionsClicked;

    private int buttonX, buttonY, buttonWidth, buttonHeight;

    public Button(Resources resources, int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        buttonX = (int) resources.getDimension(R.dimen.button_x);
        buttonWidth = (int) resources.getDimension(R.dimen.button_width);
        buttonHeight = (int) resources.getDimension(R.dimen.button_height);

        Bitmap buttonOptions = BitmapFactory.decodeResource(resources, R.drawable.dani);
        optionsButton = Bitmap.createScaledBitmap(buttonOptions, buttonWidth, buttonHeight, false);

        optionsClicked = false;

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(optionsButton, screenWidth/2 - optionsButton.getWidth()/2, screenHeight/8, null);
    }

    @Override
    public void update() {

    }

    public void optionsClicked () {
        optionsClicked = true;
    }

}
