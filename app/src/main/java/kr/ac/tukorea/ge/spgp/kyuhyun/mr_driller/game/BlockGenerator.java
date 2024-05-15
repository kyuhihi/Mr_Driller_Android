package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;

public class BlockGenerator implements IGameObject {
    private static final String TAG = BlockGenerator.class.getSimpleName();
    private final Random random = new Random();
    private boolean bInitialize = false;
    private int m_iCurBlockLayer = 0;
    private ArrayList<Block> pLastBlocks = new ArrayList<Block>();
    private void Initialize() {//6개의 블럭층을 생성.
        Scene scene = Scene.top();
        if (scene == null) return;
        for (int j = 0; j < 100; j++)
        {
            for (int i = 0; i < 7; i++) {
                //scene.add(MainScene.Layer.block, Block.get(Block.BLOCK_TYPE.BLOCK_BLUE, i));
                Block tTempBlock = Block.get(Block.BLOCK_TYPE.toBlockType(random.nextInt(Block.BLOCK_TYPE.END.ordinal())), i);
                tTempBlock.SetInitY(j);
                tTempBlock.SetIndex(i,m_iCurBlockLayer);
                scene.add(MainScene.Layer.block, tTempBlock);
                if(j == 5)
                    pLastBlocks.add(tTempBlock);
            }
            ++m_iCurBlockLayer;
        }
    }
    @Override
    public void update(float elapsedSeconds) {
        if(bInitialize == false) {
            Initialize();
            bInitialize =true;
            return;
        }
        for(Block block : pLastBlocks)
        {
            float BlockBottomY = block.getCollisionRect().bottom;
            if(BlockBottomY < 16.f) {
                generate();
                return;
            }
        }
    }

    public void DrillThisBlock(Block pTargetBlock)
    {
        Scene scene = Scene.top();
        if (scene == null) return;
        int iTargetIndexX =0 ,iTargetIndexY=0;
        iTargetIndexX = pTargetBlock.GetIndexX();
        iTargetIndexY = pTargetBlock.GetIndexY();
        ArrayList<IGameObject> blocks = scene.objectsAt(MainScene.Layer.block);
        for(IGameObject obj :blocks){
            Block block = (Block)obj;
            block.SetState(Block.BLOCK_STATE.STATE_FALL);

            int iForIndexX = 0, iForIndexY = 0;
            if((iTargetIndexX == block.GetIndexX()) && (iTargetIndexY ==block.GetIndexY()))
            {
                block.SetState(Block.BLOCK_STATE.STATE_END);
            }
        }
    }

    private void generate() {
        Scene scene = Scene.top();
        if (scene == null) return;
        pLastBlocks.clear();
        for(int i =0; i< 7; i++)
        {
            Block tTempBlock = Block.get(Block.BLOCK_TYPE.toBlockType(random.nextInt(Block.BLOCK_TYPE.END.ordinal())), i);
            tTempBlock.SetInitY(5);
            tTempBlock.SetIndex(i,m_iCurBlockLayer);
            scene.add(MainScene.Layer.block,tTempBlock);
        }
        ++m_iCurBlockLayer;

    }

    @Override
    public void draw(Canvas canvas) {
    }
}
