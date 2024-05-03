package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.BuildConfig;
import kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if(BuildConfig.DEBUG)
        {
            startActivity(new Intent(this,MrDrillerActivity.class));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(this,MrDrillerActivity.class));
        }
        return super.onTouchEvent(event);
    }

}