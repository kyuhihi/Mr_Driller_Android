package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;

public class BlockGenerator implements IGameObject {
    private static final String TAG = BlockGenerator.class.getSimpleName();
    private final Random random = new Random();
    private float TempTime = 0;
    boolean bTest = false;

    @Override
    public void update(float elapsedSeconds) {
        if(bTest == false) {
            generate();
            bTest =true;
            return;
        }
        else
            return;
        //TempTime -= elapsedSeconds;
        //if (TempTime < 0) {
        //    generate();
        //    TempTime = 5.0f;
        //}
    }

    private void generate() {
        Scene scene = Scene.top();
        if (scene == null) return;
        for(int i =0; i< 7; i++)
        {
            scene.add(MainScene.Layer.block,Block.get(Block.BLOCK_TYPE.BLOCK_BLUE,i));
        }

    }

    @Override
    public void draw(Canvas canvas) {

    }
}
