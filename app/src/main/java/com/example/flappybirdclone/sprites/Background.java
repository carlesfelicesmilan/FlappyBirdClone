package com.example.flappybirdclone.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flappybirdclone.R;

public class Background implements Sprite {

    public int screenHeight;
    private Bitmap top, bottom;
    private  int topHeight, bottomHeight;

    public Background(Resources resources, int screenHeight) {
        this.screenHeight = screenHeight;
        topHeight = (int) resources.getDimension(R.dimen.bkg_top_height);
        bottomHeight = (int) resources.getDimension(R.dimen.bkg_bottom_height);

        Bitmap bkgTop = BitmapFactory.decodeResource(resources,R.drawable.sky);
        Bitmap bkgBottom = BitmapFactory.decodeResource(resources,R.drawable.ground);

        top = Bitmap.createScaledBitmap(bkgTop, bkgTop.getWidth(), topHeight, false);
        bottom = Bitmap.createScaledBitmap(bkgBottom, bkgBottom.getWidth(), bottomHeight, false);

    }

    @Override
    // Draw the background map only once
    // The second draw is for tablets
    // Ground needs to be set at the end of the screen minus the height of the actual midline
    public void draw(Canvas canvas) {
        canvas.drawBitmap(top, 0, 0, null);
        canvas.drawBitmap(top, top.getWidth(), 0, null);

        canvas.drawBitmap(bottom, 0, screenHeight - bottom.getHeight(), null);
        canvas.drawBitmap(bottom, bottom.getWidth(), screenHeight - bottom.getHeight(), null);
    }

    @Override
    public void update() {

    }
}
