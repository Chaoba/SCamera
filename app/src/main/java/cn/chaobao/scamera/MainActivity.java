package cn.chaobao.scamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SurfaceView surfaceView= (SurfaceView) findViewById(R.id.surface);
        final CameraManager mCameraManager=new CameraManager();
        mCameraManager.setSurface(surfaceView,mLister);
        findViewById(R.id.take_pieture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraManager.tackPicture();
            }
        });

    }
    CameraManager.TakePictureListener mLister=new CameraManager.TakePictureListener(){
        @Override
        public void onPictureTake(String path) {

        }
    };


}
