package cn.chaobao.scamera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TakePictureActivity extends AppCompatActivity {
    RadioGroup mSplash;
    Button mTakePicture;
    ImageView mAnimView, mThubmnail;
    final CameraManager mCameraManager = new CameraManager();
    private Animation animation;
    private String mLastPicturePath;
    private Bitmap mThubmBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);

        mCameraManager.setSurface(this, surfaceView, mLister);
        mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mSplash = (RadioGroup) findViewById(R.id.splash_group);
        mAnimView = (ImageView) findViewById(R.id.anim_view);
        mThubmnail = (ImageView) findViewById(R.id.thumbnail);

        mSplash.setOnCheckedChangeListener(mSplashCheckListener);
        mTakePicture = (Button) findViewById(R.id.take_pieture);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePicture.setEnabled(false);
                mCameraManager.tackPicture();
            }
        });

        animation = AnimationUtils.loadAnimation(TakePictureActivity.this, R.anim.tempview_show);
        animation.setAnimationListener(mAnimLister);
        mAnimView.setAnimation(animation);
    }

    Animation.AnimationListener mAnimLister = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mAnimView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimView.setVisibility(View.GONE);
            if (mThubmBitmap != null) {
                mThubmnail.setImageBitmap(mThubmBitmap);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    RadioGroup.OnCheckedChangeListener mSplashCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.splash_auto:
                    mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    break;
                case R.id.splash_on:
                    mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    break;
                case R.id.splash_off:
                    mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    break;
            }
        }
    };


    CameraManager.TakePictureListener mLister = new CameraManager.TakePictureListener() {
        @Override
        public void onPictureTake(Bitmap thumbnail, String path) {
            mTakePicture.setEnabled(true);
            mAnimView.setImageBitmap(thumbnail);
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(TakePictureActivity.this, R.anim.tempview_show);
                animation.setAnimationListener(mAnimLister);
            }
            mAnimView.startAnimation(animation);
            mLastPicturePath = path;
            mThubmBitmap = thumbnail;
//            Intent i = new Intent(TakePictureActivity.this, AlbumActivity.class);
//            i.putExtra(AlbumActivity.PICTURE_PATH, path);
//            startActivity(i);
        }

        @Override
        public void onError(String error) {
            mTakePicture.setEnabled(true);
            Toast.makeText(TakePictureActivity.this, error, Toast.LENGTH_LONG).show();
        }
    };


}
