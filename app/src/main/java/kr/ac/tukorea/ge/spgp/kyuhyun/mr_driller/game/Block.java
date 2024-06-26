package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.RecycleBin;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Block extends Sprite implements IBoxCollidable, IRecyclable {
    private static final float RADIUS = 9 / 14.f;
    private final Rect srcRect = new Rect();
    public enum BLOCK_STATE {
        STATE_IDLE,STATE_FALL,STATE_END;
    }
    public enum BLOCK_TYPE {
        BLOCK_BLUE, BLOCK_GREEN, BLOCK_RED, BLOCK_YELLOW, END;

        public static BLOCK_TYPE toBlockType(int x) {
            switch(x) {
                case 0:
                    return BLOCK_BLUE;
                case 1:
                    return BLOCK_GREEN;
                case 2:
                    return BLOCK_RED;
                case 3:
                    return BLOCK_YELLOW;
            }
            return null;
        }
    }
    private static final int[] resIds = {
            R.mipmap.block_0, R.mipmap.block_1, R.mipmap.block_2, R.mipmap.block_3
    };
    public static  float fPlayerScrollY = 0.f;
    protected RectF collisionRect = new RectF();
    private BLOCK_TYPE blockType;
    public static BLOCK_TYPE NeedToDeleteColor = BLOCK_TYPE.END;
    public BLOCK_TYPE GetBlockType() {return blockType;}
    private BLOCK_STATE blockState  = BLOCK_STATE.STATE_IDLE;

    private float jumpSpeed = 0.f;
    private static final float JUMP_POWER = 9.0f;
    private static final float GRAVITY = 17.0f;

    private int m_iBlockLevelIndexY =0;
    private int m_iBlockLevelIndexX =0;

    private Block(BLOCK_TYPE eblockType, int index) {
        super(resIds[0]);
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

    private void ChangeRandomBlockType()
    {
        bitmap = BitmapPool.get(resIds[blockType.ordinal()]);
    }

    public void SetIndex(int X, int Y)
    {
        m_iBlockLevelIndexY = Y;
        m_iBlockLevelIndexX = X;
    }

    public int GetIndexX()
    {
        return m_iBlockLevelIndexX;
    }
    public int GetIndexY()
    {
        return m_iBlockLevelIndexY;
    }

    public void Initialize(Block blockInstance, int index)
    {
        blockInstance.ChangeRandomBlockType();
        srcRect.set(0, 0, 48, 40);
        blockInstance.setPosition(Metrics.width/14 * (2 * index +1), Metrics.height/2 + (RADIUS * 2), RADIUS);
    }

    public void SetInitY(int iIndex)
    {
        y=y + (RADIUS * 2 *iIndex);
        dstRect.set(dstRect.left, y - RADIUS, dstRect.right, y + RADIUS);
    }

    private float findNearestPlatformTop(float foot) {
        Block platform = findNearestPlatform(foot);
        if (platform == null) return Metrics.height;
        return platform.getCollisionRect().top;
    }
    private Block findNearestPlatform(float foot) {
        Block nearest = null;
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return null;
        ArrayList<IGameObject> blocks = scene.objectsAt(MainScene.Layer.block);
        float top = Metrics.height;
        for (IGameObject obj: blocks) {
            Block block = (Block) obj;
            if(block.GetBlockType() == BLOCK_TYPE.END)
                continue;
            RectF rect = block.getCollisionRect();
            if (rect.left > x || x > rect.right) {
                continue;
            }
            if (rect.top < foot) {
                continue;
            }
            if (top > rect.top) {
                top = rect.top;
                nearest = block;
            }
        }
        return nearest;
    }
    public void SetState(BLOCK_STATE eState)
    {
        if(blockState == BLOCK_STATE.STATE_END|| blockType != NeedToDeleteColor)
        {
            return;
        }

        blockState = eState;
        if(eState == BLOCK_STATE.STATE_END)
        {
            MainScene scene = (MainScene) Scene.top();
            ArrayList<IGameObject> blocks = scene.objectsAt(MainScene.Layer.block);
            for(IGameObject obj :blocks)
            {
                Block TargetBlock = (Block) obj;
                if(Math.abs(x - TargetBlock.x) < RADIUS + 0.05F)
                {
                    continue;
                }


                RectF tTargetRect= this.getCollisionRect();

               /* float inset = 0.000f;
                RectF tTargetRect_Left = new RectF();

                tTargetRect_Left.set(tTargetRect.left - inset, tTargetRect.top,tTargetRect.right - inset,tTargetRect.bottom);
                if(CollisionHelper.collides(tTargetRect_Left,TargetBlock.getCollisionRect()))
                {
                    TargetBlock.SetState(BLOCK_STATE.STATE_END);
                }

                RectF tTargetRect_Right = new RectF();
                tTargetRect_Right.set(tTargetRect.left + inset, tTargetRect.top,tTargetRect.right + inset,tTargetRect.bottom);

                if(CollisionHelper.collides(tTargetRect_Right,TargetBlock.getCollisionRect()))
                {
                    TargetBlock.SetState(BLOCK_STATE.STATE_END);
                }

                RectF tTargetRect_Top = new RectF();
                tTargetRect_Top.set(tTargetRect.left , tTargetRect.top- inset,tTargetRect.right ,tTargetRect.bottom- inset);

                if(CollisionHelper.collides(tTargetRect_Top,TargetBlock.getCollisionRect()))
                {
                    TargetBlock.SetState(BLOCK_STATE.STATE_END);
                }

                RectF tTargetRect_Bottom = new RectF();
                tTargetRect_Bottom.set(tTargetRect.left , tTargetRect.top+ inset,tTargetRect.right ,tTargetRect.bottom+ inset);

                if(CollisionHelper.collides(tTargetRect_Bottom,TargetBlock.getCollisionRect()))
                {
                    TargetBlock.SetState(BLOCK_STATE.STATE_END);
                }
*/
            }

               /* if (CollisionHelper.collides(enemy, bullet)) {
                    Log.d(TAG, "Collision !!");
                    scene.remove(MainScene.Layer.bullet, bullet);
                    scene.remove(MainScene.Layer.enemy, enemy);
                    //scene.addScore(enemy.getScore());
                    break;
                }   */
        }
    }


    @Override
    public void update(float elapsedSeconds) {
        switch (blockState)
        {
            case STATE_IDLE:
            case STATE_END:
                break;
//            case STATE_FALL: {
//                float dy = jumpSpeed * elapsedSeconds;
//                jumpSpeed += GRAVITY * elapsedSeconds;
//                float foot = collisionRect.bottom;
//                float floor = findNearestPlatformTop(foot);
//                if (foot + dy >= floor) {
//                    dy = floor - foot;
//                    blockState = BLOCK_STATE.STATE_IDLE;
//                }
//                y += dy;
//                dstRect.offset(0, dy);
//                break;
//            }
        }

        y -= fPlayerScrollY;
        setPosition(x,y,RADIUS);
        super.update(elapsedSeconds);
        if (dstRect.top < -16.f || blockState == BLOCK_STATE.STATE_END) {
            Scene.top().remove(MainScene.Layer.block, this);//이펙트 여기서 낼것.
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
