package kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.res.BitmapPool;

public class AnimSprite extends Sprite{
    protected Rect srcRect = new Rect();
    private float fps;
    private int frameWidth, frameHeight;
    private int frameCount;
    private final long createdOn;

    public AnimSprite(int mipmapId, float fps) {
        super(mipmapId);
        setAnimationResource(0, fps, 0);
        createdOn = System.currentTimeMillis();
    }
    public void setAnimationResource(int mipmapId, float fps) {
        setAnimationResource(mipmapId, fps, 0);
    }
    public void setAnimationResource(int mipmapId, float fps, int frameCount) {
        if (mipmapId != 0) {
            bitmap = BitmapPool.get(mipmapId);
        }
        this.fps = fps;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        if (frameCount == 0) {
            this.frameWidth = imageHeight;
            this.frameHeight = imageHeight;
            this.frameCount = imageWidth / imageHeight;
        } else {
            this.frameWidth = imageWidth / frameCount;
            this.frameHeight = imageHeight;
            this.frameCount = frameCount;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        long now = System.currentTimeMillis();
        float time = (now - createdOn) / 1000.0f;
        int frameIndex = Math.round(time * fps) % frameCount;
        srcRect.set(frameIndex * frameWidth, 0, (frameIndex + 1) * frameWidth, frameHeight);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
