package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.game;

import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp.kyuhyun.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp.kyuhyun.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class MainScene extends Scene {
    private final Player Player;

    public MainScene(){

        this.Player = new Player();
        add(Player);
    }

    public boolean onTouch(MotionEvent event) {
        return Player.onTouch(event);
    }
}
