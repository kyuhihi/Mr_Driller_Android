package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.SheetSprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Player extends SheetSprite implements IBoxCollidable {
    private static final String TAG =Player.class.getSimpleName();

    private static Map<String, ArrayList<Rect>> player_sheet_map; //first
    private static final float RADIUS = 0.8f;
    private static final float SPEED = 5.0f;
    private static final float TARGET_RADIUS = 0.5f;
    private float targetX,targetY;
    private float jumpSpeed = 0.f;
    private static final float JUMP_POWER = 9.0f;
    private static final float GRAVITY = 17.0f;
    private RectF targetRect = new RectF() ;
    private final RectF collisionRect = new RectF();

    public enum State {
        idle, drill, walk, crush,angel, revive,jump,fall, bo, COUNT
    }
    State state = State.fall;
    public enum Direction{
        Dir_Left,Dir_Right,Dir_Down,Dir_Up
    }

    Direction PlayerDir = Direction.Dir_Down;

    BlockGenerator BlockMgr;
    public void SetBlockGenerator(BlockGenerator pGenerator)
    {
        BlockMgr = pGenerator;
    }
    public void MakeSrcRectsArray(Map<String, List<JSONObject>> pMap)
    {
        player_sheet_map = new HashMap<>();

        for (Map.Entry<String, List<JSONObject>> entry : pMap.entrySet()) {
            String category = entry.getKey();
            List<JSONObject> frames = entry.getValue();

            ArrayList<Rect> rectList = new ArrayList<>();
            for (JSONObject frame : frames) {
                try {
                    JSONObject frameData = frame.getJSONObject("frame");
                    int x = frameData.getInt("x");
                    int y = frameData.getInt("y");
                    int w = frameData.getInt("w");
                    int h = frameData.getInt("h");

                    Rect rect = new Rect(x, y, x + w, y + h);
                    rectList.add(rect);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            player_sheet_map.put(category, rectList);
        }


    }
    public static void writeKeysToFile(Context context, String filename) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            FileWriter writer = new FileWriter(fos.getFD());
            for (String key : player_sheet_map.keySet()) {
                writer.write(key + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write Finish
    }

    public Player(){
        super(R.mipmap.player_sheet,8);

        setPosition(Metrics.width / 2, 0.f/*Metrics.height /2*/, RADIUS);
        setTargetXY(x,y);

    }
    public void jump() {
        if (state == State.walk || state == State.idle) {
            jumpSpeed = -JUMP_POWER;
            setState(State.jump);
        }
    }
    private void setTargetXY(float x,float y) {
        targetX = Math.max(RADIUS, Math.min(x, Metrics.width - RADIUS));
        targetY = Math.max(RADIUS, Math.min(y, Metrics.height - RADIUS));
        targetRect.set(
                targetX - TARGET_RADIUS, targetY - TARGET_RADIUS,
                targetX + TARGET_RADIUS, targetY + TARGET_RADIUS
        );
    }

    private void Walk_IdleTick(float elapsedSeconds){
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
            dx = 0;
        }

        if(dx == 0)
            setState(State.idle);
        else
            setState(State.walk);


        if (adjx != x) {
            setPosition(adjx, y, RADIUS);
        }
    }

    private void Jump_FallTick(float elapsedSeconds){
        float dy = jumpSpeed * elapsedSeconds;
        jumpSpeed += GRAVITY * elapsedSeconds;
        if (jumpSpeed >= 0) { // 낙하하고 있다면 발밑에 땅이 있는지 확인한다
            float foot = collisionRect.bottom;
            float floor = findNearestPlatformTop(foot);
            if (foot + dy >= floor) {
                dy = floor - foot;
                setState(State.idle);
            }
            else
            {
                setState(State.fall);
            }
        }
        y += dy;
        dstRect.offset(0, dy);
    }
    public void update(float elapsedSeconds) {
        switch (this.state)
        {
            case idle:
            case walk:
                Walk_IdleTick(elapsedSeconds);
                break;
            case drill:
                break;
            case jump:
            case fall:
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
                    dx = 0;
                }
                Jump_FallTick(elapsedSeconds);
                break;
            case crush:
                break;
            case angel:
                break;
            case revive:
                break;
        }
        collisionRect.set(dstRect);


    }
    // Radius가 주어졌을 때, height 값을 계산하는 메서드
    public static float calculateHeight(float radius, Rect rect) {
        float width = rect.width();
        float height = rect.height();

        float ratio = height / width;

        float newHeight = radius * ratio;

        return newHeight;
    }
    private void setState(State state) {
        this.state = state;
        String retVal = GetProperAnimation();
        if(retVal == null)
            return;

        srcRects = player_sheet_map.get(retVal);
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

    private String GetProperAnimation() {
        switch (this.state)
        {
            case idle:
                //return "fall";
                switch (PlayerDir)
                {
                    case Dir_Left:
                        return "idleleft";
                    case Dir_Right:
                        return "idleright";
                    case Dir_Down:
                        return "idle";
                }

            case drill:{
                switch(PlayerDir)
                {
                    case Dir_Left:
                        return "drillleft";
                    case Dir_Right:
                        return "drillright";
                    case Dir_Down:
                        return "drilldown";
                    case Dir_Up:
                        return "drillup";
                }
            }
            case walk: {
                if(dx > 0)
                    return "walkright";
                else
                    return "walkleft";
            }
            case crush:
                break;
            case angel:
                break;
            case revive:
                break;
            case jump:
               break;
            case fall:
                return "fall";
            case bo:
            case COUNT:
                break;
        }
        return null;
    }
    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetXY(pts[0],pts[1]);
                //Log.d(TAG, "targetX : "+ targetX);
                SetDirection();
                return true;
        }
        return false;
    }

    public void Execute_Drill()
    {
        if(State.idle == state || State.walk == state)
            setState(State.drill);
    }

    public int PlayerBlockIndex(float targetXPos){
        float playerPosition = targetXPos; // 플레이어의 위치
        float blockSize = 9.0f / 7.0f; // 블록의 x 사이즈

        int blockIndex = (int) (playerPosition / blockSize);
        return blockIndex;
    }
    private void SetDirection(){
        int iClickIndex = PlayerBlockIndex(this.targetX);
        int iPlayerIndex = PlayerBlockIndex(this.x);

        if(iPlayerIndex < iClickIndex)
            PlayerDir = Direction.Dir_Right;
        else if(iPlayerIndex > iClickIndex)
            PlayerDir = Direction.Dir_Left;
        else
            PlayerDir = Direction.Dir_Down;
    }
    private void EndOfFrame()
    {
        if(this.state == State.drill)
        {
            //Log.d(TAG, "PlayerIn : "+ PlayerBlockIndex(this.x));
            if(PlayerDir == Direction.Dir_Down) {
                float foot = collisionRect.bottom;
                Block pBlock = findNearestPlatform(foot);
                BlockMgr.DrillThisBlock(pBlock);

                setState(State.fall);
            }
            else
                setState(State.idle);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(srcRects.size() - 1 == srcRectsIndex)
        {
            EndOfFrame();
        }
    }
}
