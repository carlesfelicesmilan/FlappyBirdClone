package com.example.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.game.sprites.Background;
import com.example.game.sprites.Bird;
import com.example.game.sprites.GameMessage;
import com.example.game.sprites.GameOver;
import com.example.game.sprites.Obstacle;
import com.example.game.sprites.ObstacleManager;
import com.example.game.sprites.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, GameManagerCallback {

    private static final String APP_NAME = "Flappy bird clone";
    public MainThread thread;
    private GameState gameState = GameState.INITIAL;

    private Bird bird;
    private Background background;
    // Used to get the screen size dimensions
    private DisplayMetrics dm;
    private ObstacleManager obstacleManager;
    private GameOver gameOver;
    private GameMessage gameMessage;
    private Score scoreSprite;
    // Actual score
    private int score;
    private Rect birdPosition;
    // Map of obstacle and each obstacle have two rectangles
    private HashMap<Obstacle, List<Rect>> obstaclePositions = new HashMap<>();

    // mpPint: Sound for the point (obstacle removed)
    // mpSwoosh: Sound for the beggining of the game
    private MediaPlayer mpPoint;
    private MediaPlayer mpSwoosh;
    private MediaPlayer mpDie;
    private MediaPlayer mpHit;
    private MediaPlayer mpWing;

    public GameManager(Context context, AttributeSet attributeSet) {
        super(context);
        initSounds();
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        dm = new DisplayMetrics();
        // This context comes from main activity. Main activity initiates the view and the view opens the game manager
        // We need to convert the context into an Activity so we can get the window manager
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        initGame();
    }

    // Initiate the game and create the bird
    // We need to restart bird position and obstacle position everytime that we start a new game
    private void initGame() {
        score = 0;
        birdPosition = new Rect();
        obstaclePositions = new HashMap<>();
        bird = new Bird(getResources(), dm.heightPixels, this);
        background = new Background(getResources(), dm.heightPixels);
        obstacleManager = new ObstacleManager(getResources(), dm.heightPixels, dm.widthPixels, this);
        gameOver = new GameOver(getResources(), dm.heightPixels, dm.widthPixels);
        gameMessage = new GameMessage(getResources(), dm.heightPixels, dm.widthPixels);
        scoreSprite = new Score(getResources(), dm.heightPixels, dm.widthPixels);
    }

    private void initSounds() {
        mpPoint = MediaPlayer.create(getContext(), R.raw.point);
        mpSwoosh = MediaPlayer.create(getContext(), R.raw.swoosh);
        mpDie = MediaPlayer.create(getContext(), R.raw.die);
        mpHit = MediaPlayer.create(getContext(), R.raw.hit);
        mpWing = MediaPlayer.create(getContext(), R.raw.wing);
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
        switch (gameState) {
            case PLAYING:
                bird.update();
                obstacleManager.update();
                break;
            case GAME_OVER:
                // We update the bird because it needs to fall but not the obstacles
                bird.update();
                break;
        }
    }

    //Draw the bird inside the canvas
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawRGB(150,255,255);
            background.draw(canvas);
            // REFACTOR
            switch (gameState) {
                case INITIAL:
                    bird.draw(canvas);
                    gameMessage.draw(canvas);
                    break;
                case PLAYING:
                    bird.draw(canvas);
                    obstacleManager.draw(canvas);
                    scoreSprite.draw(canvas);
                    calculateCollision();
                    break;
                case GAME_OVER:
                    bird.draw(canvas);
                    obstacleManager.draw(canvas);
                    gameOver.draw(canvas);
                    scoreSprite.draw(canvas);
                    break;
            }
        }

    }

    // In initial event we want the bird start flying when user clicks on the screen so we call onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case INITIAL:
                bird.onTouchEvent();
                mpWing.start();
                gameState = GameState.PLAYING;
                mpSwoosh.start();
                break;
            case PLAYING:
                bird.onTouchEvent();
                mpWing.start();
                break;
            case GAME_OVER:
                initGame();
                gameState = GameState.INITIAL;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void updatePosition(Rect birdPosition) {
        this.birdPosition = birdPosition;
    }

    @Override
    public void updatePosition(Obstacle obstacle, ArrayList<Rect> positions) {
        if(obstaclePositions.containsKey(obstacle)) {
            obstaclePositions.remove(obstacle);
        }
        obstaclePositions.put(obstacle, positions);
    }

    // When we remove an obstacle of the screen it means that the bird has passed so we increase the score
    @Override
    public void removeObstacle(Obstacle obstacle) {
        obstaclePositions.remove(obstacle);
        score++;
        scoreSprite.updateScore(score);
        mpPoint.start();
    }

    // Calculate if a collision has occurred
    // First we check if the bird is at the bottom of the screen
    // If not, we check for each obstacle and, for each one, we have two rectangles
    public void calculateCollision() {
        boolean collision = false;
        if (birdPosition.bottom > dm.heightPixels) {
            collision = true;
        } else {
            for (Obstacle obstacle : obstaclePositions.keySet()) {
                Rect bottomRectangle = obstaclePositions.get(obstacle).get(0);
                Rect topRectangle = obstaclePositions.get(obstacle).get(1);
                if (birdPosition.right > bottomRectangle.left && birdPosition.left < bottomRectangle.right && birdPosition.bottom > bottomRectangle.top) {
                    collision = true;
                } else if (birdPosition.right > topRectangle.left && birdPosition.left < topRectangle.right && birdPosition.top < topRectangle.bottom) {
                    collision = true;
                }
            }
        }

        if (collision) {
            // Implement game over
            // setOnCompletionListener: We let the Hit sound finish until we execute Die sound
            gameState = GameState.GAME_OVER;
            bird.collision();
            scoreSprite.collision(getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));
            mpHit.start();
            mpHit.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mpDie.start();
                }
            });
        }
    }


}
