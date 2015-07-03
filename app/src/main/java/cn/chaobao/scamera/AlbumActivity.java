package cn.chaobao.scamera;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

public class AlbumActivity extends Activity {
    public static final String PICTURE_PATH ="PATH";
    public static final String RESULT_SVAE="SAVE";
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.album);
        path=getIntent().getStringExtra(PICTURE_PATH);
        ImageView imageView= (ImageView) findViewById(R.id.album_img);
        imageView.setImageURI(Uri.parse(path));
        findViewById(R.id.album_cancel).setOnClickListener(mOnClickLister);
        findViewById(R.id.album_save).setOnClickListener(mOnClickLister);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    View.OnClickListener mOnClickLister =new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.album_save:
                    setResult(RESULT_OK);
                    MainApplication.scanFile(path);
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
        File f=new File(path);
        if(f.exists()){
            f.delete();
        }
        setResult(RESULT_CANCELED);
    }

}
