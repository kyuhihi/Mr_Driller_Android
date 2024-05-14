package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.content.Context;
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
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.SheetSprite;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class Player extends SheetSprite implements IBoxCollidable {
    private static final String TAG =Player.class.getSimpleName();

    private static Map<String, ArrayList<Rect>> player_sheet_map; //first
    private static final float RADIUS = 0.8f;
    private static final float SPEED = 5.0f;
    private static final float TARGET_RADIUS = 0.5f;
    private float targetX,targetY;

    private RectF targetRect = new RectF() ;
    private final RectF collisionRect = new RectF();

    public enum State {
        idle, drill, walk, crush,angel, revive,jump, bo, COUNT
    }
    State state = State.idle;
    public enum Direction{
        Dir_Left,Dir_Right,Dir_Down,Dir_Up
    }

    Direction PlayerDir = Direction.Dir_Down;


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
                    int h = 40;//frameData.getInt("h");

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

        setPosition(Metrics.width / 2, Metrics.height /2, RADIUS);
        setTargetXY(x,y);

    }

    private void setTargetXY(float x,float y) {
        targetX = Math.max(RADIUS, Math.min(x, Metrics.width - RADIUS));
        targetY = Math.max(RADIUS, Math.min(y, Metrics.height - RADIUS));
        targetRect.set(
                targetX - TARGET_RADIUS, targetY - TARGET_RADIUS,
                targetX + TARGET_RADIUS, targetY + TARGET_RADIUS
        );
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
            dx = 0;
        }
        collisionRect.set(dstRect);
        if(dx == 0)
            setState(State.idle);
        else
            setState(State.walk);

        if (adjx != x) {
            setPosition(adjx, y, RADIUS);//,calculateHeight(RADIUS,srcRects.get(index)));
        }


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

    private String GetProperAnimation() {
        switch (this.state)
        {
            case idle:
                //return "idle";
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
                if(dx>0)
                    return "drillright";
                else if(dx < 0)
                    return "drillleft";
                else
                    return "drilldown";
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
        PlayerBlockIndex(this.x);
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
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
}
