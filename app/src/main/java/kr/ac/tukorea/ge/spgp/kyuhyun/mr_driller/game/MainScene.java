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
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class MainScene extends Scene {
    private static final String TAG =MainScene.class.getSimpleName();
    private final Player Player;

    Score score;

    public enum Layer {
        bg, block, player, effect, ui, touch, controller, END
    }
    public MainScene(){
        initLayers(Layer.END);

        add(Layer.controller, new BlockGenerator());
        add(Layer.controller, new CollisionChecker(this));

        add(Layer.bg, new Sky(R.mipmap.blue,0.f));
        add(Layer.bg, new Cloud(R.mipmap.clouds,0.4f));

        //add(Layer.ui, new JoyStick(R.mipmap.joystick_thumb));

        add(Layer.touch, new Button(R.mipmap.drill_btn, 1.0f, 14.0f, 2.0f, 2.f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                Log.d(TAG, "Button: Slide " + action);
                //player.slide(action == Button.Action.pressed);
                return true;
            }
        }));

        this.Player = new Player();
        add(Layer.player,this.Player);

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
    public void addScore(int amount) {
        score.add(amount);
    }
    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
    }
   /* public boolean onTouch(MotionEvent event) {
        return Player.onTouch(event);
    }*/
}
