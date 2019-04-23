package yk.edu.tzc.processmonitorandroid.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import yk.edu.tzc.processmonitorandroid.R;
import yk.edu.tzc.processmonitorandroid.view.AutoFitTextureView;
import yk.edu.tzc.processmonitorandroid.view.Camera2BasicFragment;

public class FaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        String account = "";
        Intent intent = getIntent();
        account = intent.getStringExtra("account");


        if (null == savedInstanceState) {

            Bundle bundle = new Bundle();
            bundle.putString("account",account);
            Camera2BasicFragment camera2BasicFragment = Camera2BasicFragment.newInstance();
            camera2BasicFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, camera2BasicFragment)
                    .commit();
        }
        
    }



}
