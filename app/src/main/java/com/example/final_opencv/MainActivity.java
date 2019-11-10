package com.example.final_opencv;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView javaCameraView;
    Mat mGRB,mGRAY,mHSV,mCANNY;
    enum mSTATUS{grb,gray,hsv,canndy};
    mSTATUS status= mSTATUS.grb;
    Button btGRB,btGRAY,btHSV,btCANNY;
    BaseLoaderCallback baseLoaderCallback= new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };
   private static String TAG = "BBB";
    static
    {
        if(OpenCVLoader.initDebug())
        {
            Log.d(TAG, "static initializer: OK");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btGRB= findViewById(R.id.btRGB);
        btGRAY= findViewById(R.id.btGRAY);
        btHSV= findViewById(R.id.btHSV);
        btCANNY= findViewById(R.id.btCANNY);
        btGRB.setOnClickListener(onClickListener);
        btGRAY.setOnClickListener(onClickListener);
        btCANNY.setOnClickListener(onClickListener);
        btHSV.setOnClickListener(onClickListener);
        javaCameraView=findViewById(R.id.camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

    }

    private View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(v==btGRB)
           {
               status= mSTATUS.grb;
           }
           if(v==btGRAY)
           {
               status= mSTATUS.gray;
           }
           if(v==btCANNY)
           {
               status= mSTATUS.canndy;
           }
           if(v==btHSV)
           {
               status= mSTATUS.hsv;
           }
        }
    };
    @Override
    protected void onPause()
    {
        super.onPause();
        if(javaCameraView!=null)
        {
            javaCameraView.disableView();
        }

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (javaCameraView!=null)
        {
            javaCameraView.disableView();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
            Log.i(TAG, "onResume: Successfully");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.i(TAG, "onResume: Opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGRB= new Mat(height,width, CvType.CV_8UC4);
        mGRAY= new Mat(height,width, CvType.CV_8UC4);
        mCANNY= new Mat(height,width, CvType.CV_8UC4);
        mHSV= new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
     mGRB.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mGRB = inputFrame.rgba();
        Imgproc.putText(mGRB, "Tuan Ngoc ", new Point(10, 50), 3, 2, new Scalar(255, 0, 0, 255), 2);

        if(status==mSTATUS.grb) {
            return mGRB;
        }
        if(status==mSTATUS.gray) {
            Imgproc.cvtColor(mGRB,mGRAY,Imgproc.COLOR_RGB2GRAY);
            return mGRAY;
        }
        if(status==mSTATUS.canndy) {
            Imgproc.cvtColor(mGRB,mGRAY,Imgproc.COLOR_RGB2GRAY);
            Imgproc.Canny(mGRAY,mCANNY,50,150);
            return mCANNY;
        }
        if(status==mSTATUS.hsv) {
            Imgproc.cvtColor(mGRB,mHSV,Imgproc.COLOR_RGB2HSV);
            return mHSV;
        }
        return null;
    }
}
