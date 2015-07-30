package com.larvalabs.androidify.wallpaper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * The Wallpaper service class.
 *
 * @author Larva Labs, LLC
 */
public class LiveWallpaper extends WallpaperService {

    // Random number generator
    private static final Random RANDOM = new Random();

    /**
     * The frame rate we will attempt to achieve with the wallpaper
     */
    public static final int FRAME_RATE = 40;

    /**
     * The width of the wallpaper, as a percent of the height of the phone.
     */
    public static final int SCENE_WIDTH = 150;

    /**
     * Duration of animation to slide between screens (in milliseconds).
     */
    public static final long SLIDE_TIME = 100;

    /**
     * The set of background colors
     */
    private static final int[] COLORS =
            {
                    0x59c0ce,
                    0xe684a9,
                    0xfef48b,
                    0x9dcb7a,
                    0xd65143,
            };

    /**
     * Min scale size of android, where 1 means the width of the android's head just fits on the screen (width-wise)
     */
    public static final float MIN_SCALE = 2.0f;

    /**
     * Max scale size of android, where 1 means the width of the android's head just fits on the screen (width-wise)
     */
    public static final float MAX_SCALE = 2.25f;

    /**
     * Min off-centering offset (in units of the diameter of the android's eye)
     */
    public static final float MIN_OFFSET = -1.5f;

    /**
     * Max off-centering offset (in units of the diameter of the android's eye)
     */
    public static final float MAX_OFFSET = 1.5f;

    /**
     * Min rotation angle (degrees)
     */
    public static final int MIN_ROTATION = 5;

    /**
     * Max rotation angle (degrees)
     */
    public static final int MAX_ROTATION = 15;

    /**
     * Drift time in millis.
     */
    public static final long DRIFT_TIME = 15000L;

    /**
     * Drift amount (in units of the diameter of the android's eye).
     */
    public static final float DRIFT_AMOUNT = 5f;

    /**
     * Amount of time before the next scene starts to fades in (make it plus the transition time less than the drift time), in millis.
     */
    public static final long SCENE_TIME = 13000L;

    /**
     * Amount of time fading between scene transitions, in millis.
     */
    public static final long SCENE_TRANSITION = 1500L;

    @Override
    public Engine onCreateEngine() {
        return new AndroidifyEngine();
    }

    /**
     * The engine responsible for rendering the wallpaper.
     */
    private class AndroidifyEngine extends Engine {

        private final Handler mHandler = new Handler();
        private boolean mVisible;
        private AndroidDrawer android, nextAndroid;
        private AssetDatabase assetDatabase;
        private float xOffset = Float.MIN_VALUE, yOffset;
        private int width, height, maxWidth;
        private int lastIndex = -1;
        private int lastColorIndex = -1;
        private long sceneTime = 0L;
        private boolean transitionMode = false;

        private float targetSlideX;
        private long lastSlideTime;

        // Gesture detector to detect single taps.
        private android.view.GestureDetector.OnGestureListener gestureListener = new android.view.GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doTap(e);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        };

        GestureDetector mGestureDetector = new GestureDetector(gestureListener);

        private final Runnable drawer = new Runnable() {
            public void run() {
                drawFrame();
            }
        };

        /**
         * Gets a randomly-generated android.
         */
        private AndroidConfig getNextConfig() {
            // Return a random config
            return assetDatabase.getRandomConfig();
        }

        /**
         * Gets a random background color, ensuring it isn't the same as the last color.
         */
        private int getNextColor() {
            int index;
            do {
                index = RANDOM.nextInt(COLORS.length);
            } while (index == lastColorIndex);
            lastColorIndex = index;
            return COLORS[index];
        }

        private ZoomInfo createRandomZoomInfo() {
            int angle = MIN_ROTATION + RANDOM.nextInt(MAX_ROTATION - MIN_ROTATION);
            if (RANDOM.nextBoolean()) {
                angle = -angle;
            }
            final float offsetX = MIN_OFFSET + RANDOM.nextFloat() * (MAX_OFFSET - MIN_OFFSET);
            final float offsetY = MIN_OFFSET + RANDOM.nextFloat() * (MAX_OFFSET - MIN_OFFSET);
            int driftAngle = (int) (Math.atan2(offsetY, offsetX) * 180 / Math.PI);
            return new ZoomInfo(
                    MIN_SCALE + RANDOM.nextFloat() * (MAX_SCALE - MIN_SCALE),
                    offsetX,
                    offsetY,
                    angle,
                    RANDOM.nextBoolean(),
                    DRIFT_AMOUNT,
                    DRIFT_TIME,
                    driftAngle
            );
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            assetDatabase = new AssetDatabase(getAssets(), getResources());
            android = new AndroidDrawer(assetDatabase);
            nextAndroid = new AndroidDrawer(assetDatabase);
            // First android
            android.setAndroidConfig(getNextConfig(), assetDatabase);
            android.setBackgroundColor(getNextColor());
            // Set initial zoom
            android.setZoom(createRandomZoomInfo());
            sceneTime = System.currentTimeMillis();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(drawer);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                postDraw();
            } else {
                mHandler.removeCallbacks(drawer);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            maxWidth = Math.max(width, height) * SCENE_WIDTH / 100;
            this.width = width;
            this.height = height;
            android.setScreenWidth(width);
            nextAndroid.setScreenWidth(width);
            android.setDimensions(maxWidth, height);
            nextAndroid.setDimensions(maxWidth, height);
            postDraw();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(drawer);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            this.yOffset = 0;
            targetSlideX = xOffset * (width - maxWidth);
            if (this.xOffset == Float.MIN_VALUE) {
                this.xOffset = targetSlideX;
            }
            lastSlideTime = System.currentTimeMillis();
            //Log.d("!", "xOffset: " + xOffset + ", yOffset: " + yOffset);
            //Log.d("!", "xStep: " + xStep + ", yStep: " + yStep);
            //Log.d("!", "xPixels: " + xPixels + ", yPixels: " + yPixels);
            postDraw();
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
        }

        private void doTap(MotionEvent e) {
            Log.d("ANDROIDIFY WALLPAPER", "Screen tapped.");
            android.addRandomAnimation(false);
        }

        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        private void drawFrame() {
            // Figure out if we are in the middle of a scene change
            long time = System.currentTimeMillis() - sceneTime;
            int alpha = 0;
            if (time > SCENE_TIME) {
                long changeTime = time - SCENE_TIME;
                if (changeTime > SCENE_TRANSITION) {
                    AndroidDrawer tmp = android;
                    android = nextAndroid;
                    nextAndroid = tmp;
                    // Reset if there has been a large gap (more than one scene has passed)
                    if (time - SCENE_TIME > SCENE_TIME) {
                        // Reset scene time to now
                        sceneTime = time + sceneTime;
                        // Reset animations too
                        android.setZoom(createRandomZoomInfo());
                        android.rescale();
                    } else {
                        // Advance scene time
                        sceneTime += SCENE_TIME;
                    }
                    transitionMode = false;
                } else {
                    if (!transitionMode) {
                        nextAndroid.setAndroidConfig(getNextConfig(), assetDatabase);
                        nextAndroid.setBackgroundColor(getNextColor());
                        // todo - randomize
                        nextAndroid.setZoom(createRandomZoomInfo());
                        nextAndroid.rescale();
                        transitionMode = true;
                    }
                    alpha = (int) (255 * changeTime / SCENE_TRANSITION);

                }
            }
            final SurfaceHolder holder = getSurfaceHolder();
            final Rect frame = holder.getSurfaceFrame();
//            final int width = frame.width();
//            final int height = frame.height();
            android.stepAnimations();
            if (xOffset != targetSlideX) {
                long slideTime = System.currentTimeMillis() - lastSlideTime;
                if (slideTime > SLIDE_TIME) {
                    xOffset = targetSlideX;
                } else {
                    xOffset = xOffset + (targetSlideX - xOffset) * slideTime / SLIDE_TIME;
                }
            }
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    c.save();
                    c.translate(xOffset, yOffset);
                    // Draw nextdroid if in transition mode
                    android.draw(c);
                    if (transitionMode) {
                        c.saveLayerAlpha(0, 0, maxWidth, maxWidth, alpha, Canvas.ALL_SAVE_FLAG);
                        nextAndroid.draw(c);
                        c.restore();
                    }
                    c.restore();
                    //sketch.setCanvas(null);
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }

            postDraw();

        }

        /**
         * Posts a draw event to the handler.
         */
        private void postDraw() {
            mHandler.removeCallbacks(drawer);
            if (mVisible) {
                mHandler.postDelayed(drawer, 1000 / FRAME_RATE);
            }
        }

    }
}
