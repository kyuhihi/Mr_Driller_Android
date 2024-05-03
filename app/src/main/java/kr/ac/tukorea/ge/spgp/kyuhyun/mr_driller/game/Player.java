package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.AbstractList;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.SheetSprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Player extends SheetSprite implements IBoxCollidable {
    private static final float RADIUS = 0.5f;
    private static final float SPEED = 5.0f;
    private static final float TARGET_RADIUS = 0.5f;
    private final Bitmap targetBmp;
    private float targetX,targetY;
    private RectF targetRect = new RectF() ;
    private final RectF collisionRect = new RectF();

    public enum State {
        idle, walk, jump, crush, revive,angel ,COUNT
    }
    State state = State.idle;

    protected static Rect[][] srcRectsArray = {
            makeRects(100, 101, 102, 103), // State.running
            makeRects(7, 8),               // State.jump
            makeRects(1, 2, 3, 4),         // State.doubleJump
            makeRects(0),                  // State.falling
    };
    protected static Rect[] makeRects(int... indices) {
        Rect[] rects = new Rect[indices.length];
        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];
            int l = 72 + (idx % 100) * 272;
            int t = 132 + (idx / 100) * 272;
            rects[i] = new Rect(l, t, l + 140, t + 140);
        }
        return rects;
    }
    public Player(){
        super(R.mipmap.player_sheet,8);

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
    private void setState(State state) {
        this.state = state;
        srcRects = srcRectsArray[state.ordinal()];
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
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
}
