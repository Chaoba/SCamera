package cn.chaobao.scamera;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class AlbumActivity extends AppCompatActivity {
    public static final String PICTURE_PATH ="PATH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        String path=getIntent().getStringExtra(PICTURE_PATH);
        ImageView imageView= (ImageView) findViewById(R.id.album_img);
        imageView.setImageURI(Uri.parse(path));
    }


}
