package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.Score;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class MainScene extends Scene {
    private static final String TAG =MainScene.class.getSimpleName();
    private final Player Player;

    Score score;

    public enum Layer {
        bg, block, player, effect, ui, controller, END
    }
    public MainScene(){
        initLayers(Layer.END);

        add(Layer.controller, new BlockGenerator());
        add(Layer.controller, new CollisionChecker(this));

        add(Layer.bg, new Sky(R.mipmap.blue,0.f));
        add(Layer.bg, new Cloud(R.mipmap.clouds,0.4f));

        this.Player = new Player();
        add(Layer.player,this.Player);

    }
    public void addScore(int amount) {
        score.add(amount);
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
    }
    public boolean onTouch(MotionEvent event) {
        return Player.onTouch(event);
    }
}
