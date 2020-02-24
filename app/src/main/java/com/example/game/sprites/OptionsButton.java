package com.example.game.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.game.GameManagerCallback;
import com.example.game.R;

public class OptionsButton implements Sprite{

    private Bitmap optionsButton;
    private int screenHeight, screenWidth;
    private GameManagerCallback callback;
    private boolean optionsClicked;

    private int buttonX, buttonY, buttonWidth, buttonHeight;

    public OptionsButton(Resources resources, int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        buttonX = (int) resources.getDimension(R.dimen.button_x);
        buttonY = (int) resources.getDimension(R.dimen.button_y);
        buttonWidth = (int) resources.getDimension(R.dimen.button_width);
        buttonHeight = (int) resources.getDimension(R.dimen.button_height);

        Bitmap buttonOptions = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
        optionsButton = Bitmap.createScaledBitmap(buttonOptions, buttonWidth, buttonHeight, false);

        optionsClicked = false;

    }

    @Override
    public void draw(Canvas canvas) {
        if (!optionsClicked) {
            canvas.drawBitmap(optionsButton, buttonX, buttonY, null);
        }
    }

    @Override
    public void update() {

    }

    public void optionsClicked () {
        optionsClicked = true;
    }

    public boolean isButtonClicked(int x, int y) {
        if (x >= buttonX && x < (buttonX + buttonWidth)
                && y >= buttonY && y < (buttonY + buttonHeight)) {
            //tada, if this is true, you've started your click inside your bitmap
            return true;
        }
        else {
            return false;
        }
    }



}
