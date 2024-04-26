package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.RecycleBin;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Block extends Sprite implements IBoxCollidable, IRecyclable {
    private static final float RADIUS = 9 / 14.f;
    private final Rect srcRect = new Rect();

    public enum BLOCK_TYPE {
        BLOCK_BLUE, BLOCK_GREEN, BLOCK_RED, BLOCK_YELLOW, END
    }
    private static final int[] resIds = {
            R.mipmap.block_0, R.mipmap.block_1, R.mipmap.block_2, R.mipmap.block_3
    };
    protected RectF collisionRect = new RectF();
    private BLOCK_TYPE blockType;

    private Block(BLOCK_TYPE eblockType, int index) {
        super(resIds[eblockType.ordinal()]);
        this.blockType = eblockType;
        Initialize(this,index);
    }
    public static Block get(BLOCK_TYPE eblockType, int index) {
        Block block = (Block) RecycleBin.get(Block.class);
        if (block != null) {
            block.blockType = eblockType;
            block.Initialize(block,index);
            return block;
        }
        return new Block(eblockType, index);
    }

    public void Initialize(Block block, int index)
    {
        srcRect.set(0, 0, 48, 40);
        block.setPosition(Metrics.width/14 * (2 * index +1), Metrics.height/2 + (RADIUS * 2), RADIUS);
    }
    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        if (dstRect.top < 0) {
            Scene.top().remove(MainScene.Layer.block, this);
        }
        collisionRect.set(dstRect);
    }
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }

    @Override
    public void onRecycle() {

    }
    @Override
    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }


}
