package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Button;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Score;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class MainScene extends Scene {
    private static final String TAG =MainScene.class.getSimpleName();
    private final Player Player;
    private final Score score;


    public enum Layer {
        bg, block, player, effect, ui, touch, controller, END
    }
    public MainScene(){
        initLayers(Layer.END);
        BlockGenerator pBlockMgr = new BlockGenerator();
        add(Layer.controller, pBlockMgr);
        add(Layer.controller, new CollisionChecker(this));

        add(Layer.bg, new Sky(R.mipmap.blue,0.f));
        add(Layer.bg, new Cloud(R.mipmap.clouds,0.4f));

        //add(Layer.ui, new JoyStick(R.mipmap.joystick_thumb));
        this.Player = new Player();
        add(Layer.player,this.Player);

        this.Player.SetBlockGenerator(pBlockMgr);
        add(Layer.touch, new Button(R.mipmap.drill_btn, 1.0f, 14.0f, 2.0f, 2.f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                Player.Execute_Drill();
                return true;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.jump_btn, 8.f, 14.0f, 3.0f, 3.f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                Player.jump();
                return true;
            }
        }));

        this.score = new Score(R.mipmap.number_24x32, Metrics.width - 0.5f, 0.5f, 0.6f);
        score.setScore(0);
        add(Layer.ui, score);


    }

    @Override
    public boolean RestOfTouchEvent(MotionEvent event)
    {
        this.Player.onTouch(event);
        return true;
    }
    public void setPlayerSheet(Map<String, List<JSONObject>> pMap)
    {
        this.Player.MakeSrcRectsArray(pMap);

    }
    public void Writetxt(Context context)
    {
        //this.Player.writeKeysToFile(context, "player_map_keys.txt");
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        this.score.setScore((int)this.Player.PlayerY);
    }
   /* public boolean onTouch(MotionEvent event) {
        return Player.onTouch(event);
    }*/
}
