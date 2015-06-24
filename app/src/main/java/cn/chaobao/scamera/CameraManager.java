package cn.chaobao.scamera;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Liyanshun on 2015/6/24.
 */
public class CameraManager {
    private Camera mCamera;
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    public void setSurface(SurfaceView surface) {
        mSurface = surface;
        mHolder = mSurface.getHolder();
        mHolder.addCallback(mSurfaceHolderCallback);
        mHolder.setKeepScreenOn(true);
    }

    Camera.AutoFocusCallback mAutoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
                mCamera.autoFocus(mAutoFocusCB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(mCamera!=null){
                mCamera.release();
            }
        }
    };
}
