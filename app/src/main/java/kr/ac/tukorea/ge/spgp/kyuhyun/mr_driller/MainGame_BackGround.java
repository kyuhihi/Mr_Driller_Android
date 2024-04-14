package kr.ac.tukorea.ge.spgp.kyuhyun.mr_driller;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;


import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class MainGame_BackGround extends GameObject{
    public MainGame_BackGround(Bitmap bTargetBitmap) {
        m_Rect = new RectF();
        m_fSizeX = 3.25f;
        m_fSizeY = 1.25f;
        m_Rect.set(m_fPosX- m_fSizeX * 0.5f, m_fPosY, m_fPosX + m_fSizeX, m_fPosY + 2*m_fSizeY);


        m_fPosX = 0.0f;
        m_fPosY = 0.0f;

        m_fSizeX = 1.25f;
        m_fSizeY = 1.25f;

        m_Bitmap = bTargetBitmap;
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.drawBitmap(m_Bitmap, null, m_Rect, null);
        canvas.restore();
    }

}
