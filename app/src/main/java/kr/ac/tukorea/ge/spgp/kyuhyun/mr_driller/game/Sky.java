package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;

public class Sky extends Sprite {
    private final float height;
    public Sky(int bitmapResId, float speed) {
        super(bitmapResId);
        this.height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        setPosition(Metrics.width / 2, Metrics.height / 2, Metrics.width, height);
    }
    @Override
    public void update(float elapsedSeconds) {
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        dstRect.set(0, 0, Metrics.width, Metrics.height);
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }
}
