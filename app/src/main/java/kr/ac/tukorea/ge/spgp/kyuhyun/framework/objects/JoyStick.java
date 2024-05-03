package kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;

public class JoyStick implements IGameObject {
    private static final String TAG = JoyStick.class.getSimpleName();
    private final Bitmap bgBitmap;


    private float centerX = 2.0f;
    private float centerY = 14.0f;
    private float bgRadius = 2.0f;


    private final RectF bgRect = new RectF();


    public JoyStick(int bgBmpId) {
        bgBitmap = BitmapPool.get(bgBmpId);

        setRects(centerX, centerY, bgRadius);
    }

    public void setRects(float cx, float cy, float bgRadius) {
        this.centerX = cx;
        this.centerY = cy;
        this.bgRadius = bgRadius;
        bgRect.set(centerX - this.bgRadius, centerY - this.bgRadius, centerX + this.bgRadius, centerY + this.bgRadius);
    }

    @Override
    public void update(float elapsedSeconds) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, null, bgRect, null);
    }


}
