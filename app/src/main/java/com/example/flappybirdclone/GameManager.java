package com.example.flappybirdclone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.flappybirdclone.sprites.Background;
import com.example.flappybirdclone.sprites.Bird;
import com.example.flappybirdclone.sprites.Obstacle;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback {

    public MainThread thread;

    private Bird bird;
    private Background background;
    // Used to get the screen size dimensions
    private DisplayMetrics dm;

    public GameManager(Context context, AttributeSet attributeSet) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        dm = new DisplayMetrics();
        // This context comes from main activity. Main activity initiates the view and  the view opens the game manager
        // We need to convert the context into an Activity so we can get the window manager
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        initGame();
    }

    //Initiate the game and create the bird
    private void initGame() {
        bird = new Bird(getResources());
        background = new Background(getResources(), dm.heightPixels);
    }

    //Start the thread
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(thread.getState() == Thread.State.TERMINATED) {
            thread = new MainThread(holder, this);
        }
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //We can't longer update our UI we stop the background thread from running
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update(){
        bird.update();
    }

    //Draw the bird inside the canvas
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRGB(150,255,255);
        background.draw(canvas);
        bird.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bird.onTouchEvent();
        return super.onTouchEvent(event);
    }
}
