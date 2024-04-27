package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.AbstractList;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Player extends Sprite {
    private static final float RADIUS = 0.5f;
    private static final float SPEED = 5.0f;
    private static final float TARGET_RADIUS = 0.5f;
    private final Bitmap targetBmp;
    private float targetX,targetY;
    private RectF targetRect = new RectF() ;

    public Player(){
        super(R.mipmap.player_test);
        setPosition(Metrics.width / 2, Metrics.height /2, RADIUS);
        setTargetXY(x,y);
        targetBmp = BitmapPool.get(R.mipmap.fighter_target);

    }

    private void setTargetXY(float x,float y) {
        targetX = Math.max(RADIUS, Math.min(x, Metrics.width - RADIUS));
        targetY = Math.max(RADIUS, Math.min(y, Metrics.height - RADIUS));
        targetRect.set(
                targetX - TARGET_RADIUS, targetY - TARGET_RADIUS,
                targetX + TARGET_RADIUS, targetY + TARGET_RADIUS
        );
    }
    public void draw(Canvas canvas) {
        if (dx != 0) {
            canvas.drawBitmap(targetBmp, null, targetRect, null);
        }
        super.draw(canvas);
    }
    public void update(float elapsedSeconds) {
        if (targetX < x) {
            dx = -SPEED;
        } else if (x < targetX) {
            dx = SPEED;
        } else {
            dx = 0;
        }
        super.update(elapsedSeconds);
        float adjx = x;
        if ((dx < 0 && x < targetX) || (dx > 0 && x > targetX)) {
            adjx = targetX;
        } else {
            adjx = Math.max(RADIUS, Math.min(x, Metrics.width - RADIUS));
        }
        if (adjx != x) {
            setPosition(adjx, y, RADIUS);
            dx = 0;
        }
    }

    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetXY(pts[0],pts[1]);
                return true;
        }
        return false;
    }
}
