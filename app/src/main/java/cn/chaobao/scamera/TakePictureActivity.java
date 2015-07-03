package cn.chaobao.scamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class TakePictureActivity extends Activity {
    RadioGroup mSplash;
    Button mTakePicture;
    ImageView mAnimView, mThubmnailView, mSplashImg;
    final CameraManager mCameraManager = new CameraManager();
    private Animation animation;
    public static int width, height;
    private final int REQUEST_CODE = 0;
    private ArrayList<String> mPicturePaths = new ArrayList<>();
    private Bitmap mThubmBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.take_picture);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);

        mCameraManager.setSurface(surfaceView, mLister);
        mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mSplash = (RadioGroup) findViewById(R.id.splash_group);
        mAnimView = (ImageView) findViewById(R.id.anim_view);
        mThubmnailView = (ImageView) findViewById(R.id.thumbnail);
        mSplashImg = (ImageView) findViewById(R.id.splash_img);
        mSplashImg.setSelected(true);
        mTakePicture = (Button) findViewById(R.id.take_pieture);

        mSplash.setOnCheckedChangeListener(mSplashCheckListener);
        mThubmnailView.setOnClickListener(mOnClickLister);
        mTakePicture.setOnClickListener(mOnClickLister);
        findViewById(R.id.close).setOnClickListener(mOnClickLister);

        animation = AnimationUtils.loadAnimation(TakePictureActivity.this, R.anim.tempview_show);
        animation.setAnimationListener(mAnimLister);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    View.OnClickListener mOnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.thumbnail:
                    if (mPicturePaths.size()> 0) {
//                        Intent intent = new Intent();
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivity(intent);
                    }
                    break;
                case R.id.take_pieture:
                    mTakePicture.setEnabled(false);
                    mCameraManager.tackPicture();
                    break;
                case R.id.close:
                    finish();
                    break;
            }
        }
    };


    Animation.AnimationListener mAnimLister = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mAnimView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mAnimView.setVisibility(View.GONE);
            if (mThubmBitmap != null) {
                mThubmnailView.setImageBitmap(mThubmBitmap);
            }
            Intent i = new Intent(TakePictureActivity.this, AlbumActivity.class);
            i.putExtra(AlbumActivity.PICTURE_PATH, mPicturePaths.get(mPicturePaths.size() - 1));
            startActivityForResult(i, REQUEST_CODE);
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
                    mSplashImg.setSelected(true);
                    break;
                case R.id.splash_on:
                    mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    mSplashImg.setSelected(true);
                    break;
                case R.id.splash_off:
                    mCameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mSplashImg.setSelected(false);
                    break;
            }
        }
    };


    CameraManager.TakePictureListener mLister = new CameraManager.TakePictureListener() {
        @Override
        public void onPictureTake(String path) {
            mTakePicture.setEnabled(true);

            if (animation == null) {
                animation = AnimationUtils.loadAnimation(TakePictureActivity.this, R.anim.tempview_show);
                animation.setAnimationListener(mAnimLister);
            }
            mAnimView.startAnimation(animation);
            mThubmBitmap = createImageThumbnail(path);
            if (mThubmBitmap != null)
                mAnimView.setImageBitmap(mThubmBitmap);
            mPicturePaths.add(path);
        }

        @Override
        public void onError(String error) {
            mTakePicture.setEnabled(true);
            Toast.makeText(TakePictureActivity.this, error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                mPicturePaths.remove(mPicturePaths.size() - 1);

                mThubmBitmap = createImageThumbnail(mPicturePaths.get(mPicturePaths.size() - 1));
                Log.d("test","mThubmBitmap:"+(mThubmBitmap!=null));
                if (mThubmBitmap != null)
                    mThubmnailView.setImageBitmap(mThubmBitmap);
            }
        }
    }

    public static Bitmap createImageThumbnail(String filePath) {
        Log.d("test","create thumbnail:"+filePath);
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int targetSize = 96;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, targetSize, targetSize);
        return thumbnail;
    }
}
