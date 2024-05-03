package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
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
    private static Map<String, ArrayList<Rect>> player_sheet_map; //first
    private static final float RADIUS = 0.5f;
    private static final float SPEED = 5.0f;
    private static final float TARGET_RADIUS = 0.5f;
    private float targetX,targetY;
    private RectF targetRect = new RectF() ;
    private final RectF collisionRect = new RectF();

    public enum State {
        idle, drill, walk, crush,angel, revive,jump, bo, COUNT
    }
    State state = State.idle;
    // 파일에서 읽어온 문자열을 저장할 이중 배열
    ArrayList<String>[] stateStrings = new ArrayList[State.COUNT.ordinal()];



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

        // 각 상태에 대한 ArrayList 초기화
        for (int i = 0; i < State.COUNT.ordinal(); i++) {
            stateStrings[i] = new ArrayList<>();
        }

        // 파일에서 읽어와서 분류
        try (BufferedReader reader = new BufferedReader(new FileReader("keys.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 파일에서 읽은 각 문자열을 '_'를 기준으로 상태와 이름으로 분리
                String[] parts = line.split("_");
                if (parts.length == 2) {
                    String stateStr = parts[1];
                    try {
                        // 문자열을 해당하는 상태로 변환
                        State state = State.valueOf(stateStr);
                        // 해당 상태에 문자열 추가
                        stateStrings[state.ordinal()].add(line);
                    } catch (IllegalArgumentException e) {
                        // State.valueOf()에서 IllegalArgumentException이 발생하면 해당 상태가 없는 것이므로 무시
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 결과 출력
        for (int i = 0; i < State.COUNT.ordinal(); i++) {
            State state = State.values()[i];
            System.out.println(state + ": " + stateStrings[i]);
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
            setPosition(adjx, y, RADIUS);
            dx = 0;
        }
    }
    private void setState(State state) {
        this.state = state;
        //srcRects = srcRectsArray[state.ordinal()];
    }
    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetXY(pts[0],pts[1]);
                return true;
        }
        return false;
    }
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
}
