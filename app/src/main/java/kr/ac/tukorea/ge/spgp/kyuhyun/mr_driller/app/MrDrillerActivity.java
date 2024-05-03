package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.app;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game.JsonLoader;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game.MainScene;

public class MrDrillerActivity extends GameActivity {
    private void Initialize()
    {
        Map<String, List<JSONObject>> categorizedFrames = JsonLoader.readAndCategorizeFrames(this, "player_sheet.json");

        // 카테고리별로 프레임 출력
        for (Map.Entry<String, List<JSONObject>> entry : categorizedFrames.entrySet()) {
            String category = entry.getKey();
            List<JSONObject> frames = entry.getValue();

            System.out.println("Category: " + category);
            for (JSONObject frame : frames) {
                System.out.println("Frame: " + frame.toString());
            }
            System.out.println();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MainScene().push();
        Initialize();
    }
}