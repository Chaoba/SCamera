package cn.chaobao.scamera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Liyanshun on 2015/6/24.
 */
public class CameraManager {
    private static final String PIC_DIR = "PIC_DIR";
    private Camera mCamera;
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    private TakePictureListener mTakePictureListener;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    public void setSurface(SurfaceView surface, TakePictureListener listener) {
        mSurface = surface;
        mHolder = mSurface.getHolder();
        mHolder.addCallback(mSurfaceHolderCallback);
        mHolder.setKeepScreenOn(true);
        mTakePictureListener = listener;
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                saveToSDCard(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void tackPicture() {
        if (mCamera != null) {
            mCamera.takePicture(null, null, null, mPictureCallback);
        }
    }

    public void saveToSDCard(byte[] data) throws IOException {
        Date date = new Date();
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory()
                + File.separator + PIC_DIR + File.separator);
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile);
        outputStream.write(data);
        outputStream.close();
        if (mTakePictureListener != null) {
            mTakePictureListener.onPictureTake(jpgFile.getPath());
        }
    }

    Camera.AutoFocusCallback mAutoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(90);
                setCameraParameters();
                mCamera.startPreview();
                mCamera.autoFocus(mAutoFocusCB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(mCamera!=null) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(width, height);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    };
    private void setCameraParameters(){
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizeList;
        sizeList = parameters.getSupportedPictureSizes();
        if (sizeList.size()>0) {
            Camera.Size cameraSize=sizeList.get(0);
            parameters.setPictureSize(cameraSize.width, cameraSize.height);
        }
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);
        parameters.setJpegThumbnailQuality(100);
        parameters.setRotation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
//        //设置闪光灯模式。此处主要是用于在相机摧毁后又重建，保持之前的状态
//        setFlashMode(mFlashMode);
//        //设置缩放级别
//        setZoom(mZoom);
//        //开启屏幕朝向监听
//        startOrientationChangeListener();
    }
    public interface TakePictureListener {
        public void onPictureTake(String path);
    }

}
