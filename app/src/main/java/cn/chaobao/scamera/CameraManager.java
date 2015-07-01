package cn.chaobao.scamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
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
    Context mContext;

    public void setSurface(Context c, SurfaceView surface, TakePictureListener listener) {
        mContext = c;
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

    public void tackPicture() {
        if (!externalMemoryAvailable()) {
            if (mTakePictureListener != null) {
                mTakePictureListener.onError("no sdcard!");
            }
            return;
        }
        if (mCamera != null) {
            mCamera.takePicture(null, null, null, mPictureCallback);
        }
    }

    public void saveToSDCard(byte[] data) throws IOException {
        if (data.length > getAvailableExternalMemorySize()) {
            if (mTakePictureListener != null) {
                mTakePictureListener.onError("no enough memory");
            }
            return;
        }
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
        //生成缩略图
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
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
        scanFile(jpgFile.getPath());
        if (mTakePictureListener != null) {
            mTakePictureListener.onPictureTake(thumbnail, jpgFile.getPath());
        }
    }

    private void scanFile(String path) {
        MediaScannerConnection.scanFile(mContext,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
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
//            Camera.Size cameraSize = sizeList.get(0);
//            parameters.setPictureSize(cameraSize.width, cameraSize.height);
//        }
        parameters.setPictureSize(800, 480);
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
        void onPictureTake(Bitmap thumbnail, String path);

        void onError(String error);
    }

    public boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }
}
