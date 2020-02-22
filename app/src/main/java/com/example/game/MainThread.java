package com.example.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//Thread that is going to run in the background
public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameManager gameManager;
    private boolean running;
    private static Canvas canvas;
    private int targetFPS = 60;

    //surfaceHolder allows us to manipulate the canvas where we draw the elements on the screen
    public MainThread(SurfaceHolder surfaceHolder, GameManager gameManager) {
        this.surfaceHolder = surfaceHolder;
        this.gameManager = gameManager;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    //Run the thread in the background
    //While our thread is running we are going to update the game manager
    //Draw the objects that we have in our code
    //Track how long our thread/frame has taken (try to reach 60 frames)
    @Override
    public void run() {
        long startTime;
        //How long took the frame to run
        long timeMillis;
        //Amount of wait that we need to do so we can sync our targets to have 60fps
        long waitTime;
        long targetTime = 1000 / targetFPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;
            //Lock the canvas to start drawing on it
            try {
                canvas = surfaceHolder.lockCanvas();
                //We want to avoid calling two threads at the same time
                synchronized (surfaceHolder) {
                    gameManager.update();
                    gameManager.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //It's Nano seconds so we need to divide the time by 1000000
            timeMillis = (System.nanoTime() - startTime) /100000;
            waitTime = targetTime - timeMillis;

            //Thread sleep until the new frame start
            try {
                if(waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
