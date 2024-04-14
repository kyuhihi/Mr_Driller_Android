package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import android.util.AttributeSet;

import java.util.ArrayList;

public class GameView extends View implements Choreographer.FrameCallback{
    private static final String TAG = GameView.class.getSimpleName();
    private Activity activity;

    private final ArrayList<GameObject> m_GameObjects = new ArrayList<>();
    public GameView(Context context) {
        super(context);
        if(context instanceof Activity) {
            this.activity = (Activity) context;
        }

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(0.3f);
        borderPaint.setColor(Color.WHITE);

        Resources res = getResources();
        Bitmap BackGroundBitmap = BitmapFactory.decodeResource(res,R.mipmap.name_korea);
        GameObject pGameObjet = new MainGame_BackGround(BackGroundBitmap);

        m_GameObjects.add(pGameObjet);
        setFullScreen();

        scheduleUpdate();
    }

    private void scheduleUpdate() {
        Choreographer.getInstance().postFrameCallback(this);
    }



    public void setFullScreen() {
        int flags =  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        setSystemUiVisibility(flags);
    }
    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private static final float SCREEN_WIDTH = 9.0f;
    private static final float SCREEN_HEIGHT = 16.0f;
    private final Matrix transformMatrix = new Matrix();
    private final RectF borderRect = new RectF(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    private final Paint borderPaint;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float view_ratio = (float)w / (float)h;
        float game_ratio = SCREEN_WIDTH / SCREEN_HEIGHT;


        if (view_ratio > game_ratio) {
            float scale = h / SCREEN_HEIGHT;
            transformMatrix.setTranslate((w - h * game_ratio) / 2, 0);
            transformMatrix.preScale(scale, scale);
        } else {
            float scale = w / SCREEN_WIDTH;
            transformMatrix.setTranslate(0, (h - w / game_ratio) / 2);
            transformMatrix.preScale(scale, scale);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.argb(255,61,157,217));

        canvas.save();
        canvas.concat(transformMatrix);
        canvas.drawRect(borderRect, borderPaint);
        for(GameObject GameObj : m_GameObjects)
        {
            GameObj.draw(canvas);
        }

        canvas.restore();
    }

    private long previousNanos = 0;
    private float elapsedSeconds;
    @Override
    public void doFrame(long nanos) {
        long elapsedNanos = nanos - previousNanos;
        elapsedSeconds = elapsedNanos / 1_000_000_000f;
        if (previousNanos != 0) {
            update();
        }
        invalidate();
        if (isShown()) {
            scheduleUpdate();
        }
        previousNanos = nanos;
    }

    private void update() {
        Log.d(TAG, "updateCall" + elapsedSeconds);
        for(GameObject pGameObject:m_GameObjects)
        {
            pGameObject.update(elapsedSeconds);
        }

    }
}
