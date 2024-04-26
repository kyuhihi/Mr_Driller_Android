package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;

public class Background extends Sprite {
    private final float speed;
    private final float height;
    public Background(int bitmapResId, float speed) {
        super(bitmapResId);
        this.height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        setPosition(Metrics.width / 2, Metrics.height / 2, Metrics.width, height);
        this.speed = speed;
    }
    @Override
    public void update(float elapsedSeconds) {
        this.y += speed * elapsedSeconds; // y 값을 스크롤된 양으로 사용한다
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        float curr = y % height;
        if (curr > 0) curr -= height;
        while (curr < Metrics.height) {
            dstRect.set(0, curr, Metrics.width, curr + height);
            canvas.drawBitmap(bitmap, null, dstRect, null);
            curr += height;
        }
    }
}
