package kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

public class SheetSprite extends AnimSprite {
    protected ArrayList<Rect> srcRects = new ArrayList<Rect>();
    public SheetSprite(int mipmapResId, float fps)
    {
        super(mipmapResId, fps, 1);
    }
    protected int index = 0;
    @Override
    public void draw(Canvas canvas) {
        if (srcRects ==null || srcRects.isEmpty())
            return;
        long now = System.currentTimeMillis();
        float time = (now - createdOn) / 1000.0f;
        index = Math.round(time * fps) % srcRects.size();
        canvas.drawBitmap(bitmap, srcRects.get(index), dstRect, null);
    }
}
