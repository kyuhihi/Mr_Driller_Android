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

    private void Initialize(MainScene targetMainScene)
    {
        Map<String, List<JSONObject>> player_sheet_map = JsonLoader.readAndCategorizeFrames(this, "player_sheet.json");
        targetMainScene.setPlayerSheet(player_sheet_map);
        targetMainScene.Writetxt(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainScene newMainScene =  new MainScene();
        newMainScene.push();

        Initialize(newMainScene);
    }
}