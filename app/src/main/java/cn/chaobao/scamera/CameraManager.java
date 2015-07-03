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
    public static final String PIC_DIR = "PIC_DIR";
    private Camera mCamera;
    private SurfaceView mSurface;
    private SurfaceHolder mHolder;
    private TakePictureListener mTakePictureListener;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    public void setSurface( SurfaceView surface, TakePictureListener listener) {
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
                if (mTakePictureListener != null) {
                    mTakePictureListener.onError("save picture failed!");
                }
            }
        }
    };



    public void saveToSDCard(byte[] data) throws IOException {
        if (data.length > MainApplication.getAvailableExternalMemorySize()) {
            if (mTakePictureListener != null) {
                mTakePictureListener.onError("no enough memory");
            }
            return;
        }
//        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//        //生成缩略图
//        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
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
//        MainApplication.scanFile(jpgFile.getPath());
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
                if (mTakePictureListener != null) {
                    mTakePictureListener.onError("open camera failed");
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mCamera != null) {
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

    private void setCameraParameters() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizeList;
//        sizeList = parameters.getSupportedPictureSizes();
//        if (sizeList.size() > 0) {
//            Camera.Size cameraSize = null;// sizeList.get(0);
//            for (Camera.Size size : sizeList) {
//                if (size.width * size.height < 100000) {
//                    cameraSize = size;
//                    break;
//                }
//            }
//            if (cameraSize != null)
//                parameters.setPictureSize(cameraSize.width, cameraSize.height);
//        }
        parameters.setPictureSize(TakePictureActivity.width, TakePictureActivity.height);
        parameters.setPictureSize(1280, 720);
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);
        parameters.setJpegThumbnailQuality(100);
        parameters.setRotation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);

    }

    public String getFlashMode() {
        if (mCamera != null) {
            return mCamera.getParameters().getFlashMode();
        } else {
            return "";
        }
    }

    public void setFlashMode(String mode) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(mode);
            mCamera.setParameters(parameters);
        }
    }

    public interface TakePictureListener {
        void onPictureTake(String path);

        void onError(String error);
    }

    public  void tackPicture() {
        if (!MainApplication.externalMemoryAvailable()) {
            if (mTakePictureListener != null) {
                mTakePictureListener.onError("no sdcard!");
            }
            return;
        }
        if (mCamera != null) {
            mCamera.takePicture(null, null, null, mPictureCallback);
        }
    }

}
