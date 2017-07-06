package com.extralarge.fujitsu.xl.ReporterSection;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;

import com.extralarge.fujitsu.xl.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{

 //   MarshmallowPermission permission;
    boolean init;
    FrameLayout takePhotoBtn;
    String fileName="";
    public static Camera.CameraInfo info;
    AppCompatButton btnCamType;

    int camType=1;
    private Camera mCamera;
    private CameraPreview mPreview;
    AlertDialog.Builder alert;
    private static final String TAG = "MyActivity";
    private static byte[] CompressedImageByteArray;
    private static Bitmap CompressedImage;
    FrameLayout preview;
 //   private static final int PICK_CROPIMAGE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        UIClass.setStatusBarColor(this);


        takePhotoBtn=(FrameLayout)findViewById(R.id.takePhotoBtn);
        btnCamType=(AppCompatButton)findViewById(R.id.btnCamType);
        preview = (FrameLayout) findViewById(R.id.camera_preview);

        takePhotoBtn.setOnClickListener(this);
        btnCamType.setOnClickListener(this);

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), GlobalVariables.FOLDER_NAME);
        if(!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

              init = false;
                if (!init) initializeCamera(camType);
                    super.onResume();
        }


    private void initializeCamera(int camType) {
        init = true;

        mCamera = getCameraInstance(camType);
        Camera.Parameters param = mCamera.getParameters();
        List<Camera.Size> sizes = param.getSupportedPictureSizes();
        int iTarget = 0;
        for (int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);
            if (size.width < 1000) {
                iTarget = i;
                break;
            }

        }
        param.setJpegQuality(50);
        param.setPictureSize(sizes.get(iTarget).width,
                sizes.get(iTarget).height);
        mCamera.setParameters(param);
        alert = new AlertDialog.Builder(this);
        Display getOrient = getWindowManager().getDefaultDisplay();

        int rotation = getOrient.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                mCamera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                break;
            case Surface.ROTATION_180:
                break;
            case Surface.ROTATION_270:
                mCamera.setDisplayOrientation(90);
                break;
            default:
                break;
        }

        try {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);

        } catch (Exception e) {
            finish();
        }
    }



    public static Camera getCameraInstance(int camType) {
        // Camera c = null;
        try {

            int numberOfCameras = Camera.getNumberOfCameras();
            int cameraId = 0;
            for (int i = 0; i < numberOfCameras; i++) {
                info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == camType) {
                    // Log.d(DEBUG_TAG, "Camera found");
                    cameraId = i;
                    break;
                }
            }
            return Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            return null;
        }
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {

                Log.e("pic callback", "Yes");
                Log.d(TAG, "Start");
                Bitmap bmp = BitmapFactory
                        .decodeByteArray(data, 0, data.length);

                Matrix mat = new Matrix();
                mat.postRotate(90);
                Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0,
                        bmp.getWidth(), bmp.getHeight(), mat, true);

                setCameraImage(UtilityClass
                        .GenerateThumbnail(bMapRotate, 500, 700));

            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
    };

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.d(TAG, "onShutter'd");
        }
    };
    /** Handles data for raw picture */
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "onPictureTaken - raw");
        }
    };

    public void captureImage() {
        // System.gc();
        if(mCamera!=null)
            mCamera.takePicture(shutterCallback, rawCallback, mPicture);
    }

    private void setCameraImage(Bitmap bitmap) {

        Display getOrient = getWindowManager().getDefaultDisplay();
        int rotation = getOrient.getRotation();
        Matrix matrix = new Matrix();
        switch (rotation) {

            case Surface.ROTATION_0:
                break;
            case Surface.ROTATION_270:
                matrix.postRotate(90);
                break;
            case Surface.ROTATION_90:
                matrix.postRotate(-90);
                break;
            case Surface.ROTATION_180:
                matrix.postRotate(-180);
                break;
            default:
                break;
        }
        if(Camera.CameraInfo.CAMERA_FACING_FRONT==info.facing) matrix.postRotate(180);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        String fname=getIntent().getStringExtra(GlobalVariables.FILENAME);
        if(!fname.equals(""))
        {
            fileName=fname;
        }
        else
        {
            fileName=String.valueOf(System.currentTimeMillis())+".png";
        }

        try {
            saveImage(bitmap,fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean checktr = true;
        Intent cropIntent=new Intent(this, CropImage.class);
        cropIntent.putExtra(GlobalVariables.CAMERA_SOURCE,true);
        cropIntent.putExtra("camerajiboolean",checktr);
        startActivity(cropIntent);


        bitmap.recycle();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(GlobalVariables.FILENAME,fileName);
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    public  void saveImage(Bitmap image,String imagename) throws FileNotFoundException {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), GlobalVariables.FOLDER_NAME);
        String fileName=imagename;
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory");
            }
        }
        else {

            File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + fileName);
            FileOutputStream fo = new FileOutputStream(file);
            if (image != null) {
                image.compress(Bitmap.CompressFormat.PNG, 100, fo);
            }
        }
    }

    public static byte[] getCompressedImage() {
        return CompressedImageByteArray;
    }

    public static void setCompressedImage(byte[] compressedImageByteArray) {
        CompressedImageByteArray = compressedImageByteArray;
    }

    public static Bitmap getCompressedBitmap() {
        return CompressedImage;
    }

    public static void setCompressedBitmap(Bitmap compressedImage) {
        CompressedImage = compressedImage;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.takePhotoBtn:
                captureImage();
                break;

            case R.id.btnCamType:

                changeCamera();

                break;

        }
    }


    private void changeCamera() {
        if(mCamera!=null){
            mCamera.stopPreview();
        }
        if(camType==Camera.CameraInfo.CAMERA_FACING_BACK)
        {
            camType=Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else
        {
            camType=Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        preview.removeAllViews();
        initializeCamera(camType);


    }


}
