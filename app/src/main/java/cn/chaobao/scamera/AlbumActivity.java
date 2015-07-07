package cn.chaobao.scamera;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chaoba.utils.NewFatherActivity;

import java.io.File;

public class AlbumActivity extends NewFatherActivity {
    public static final String PICTURE_PATH = "PATH";
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        setView(R.layout.album);
        mTitleBar.setVisibility(View.GONE);
        path = getIntent().getStringExtra(PICTURE_PATH);
        ImageView imageView = (ImageView) findViewById(R.id.album_img);
        Drawable drawable= Drawable.createFromPath(path);
        imageView.setImageDrawable(drawable);
        findViewById(R.id.album_cancel).setOnClickListener(mOnClickLister);
        findViewById(R.id.album_save).setOnClickListener(mOnClickLister);
    }

    View.OnClickListener mOnClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.album_save:
                    setResult(RESULT_OK);
                    Util.scanFile(path);
                    finish();
                    break;
                case R.id.album_cancel:
                    deletePicture();
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        deletePicture();
        super.onBackPressed();
    }

    private void deletePicture() {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }

}
