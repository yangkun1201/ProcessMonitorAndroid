package yk.edu.tzc.processmonitorandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mei_husky.library.view.QRCodeScannerView;

public class ScanActivity extends AppCompatActivity {

    QRCodeScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mScannerView = ((QRCodeScannerView) findViewById(R.id.scanner_view));

        scan();

    }

    public void scan(){
        //自动聚焦间隔2s
        mScannerView.setAutofocusInterval(2000L);
        //闪光灯
        mScannerView.setTorchEnabled(true);
        //扫描结果监听处理
        mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {
                Log.d("tag", "扫描结果 ： " + text);
                Intent intent = new Intent();
                intent.putExtra("uuid",text);
                ScanActivity.this.setResult(1,intent);
                ScanActivity.this.finish();
            }
        });
        //相机权限监听
        mScannerView.setOnCheckCameraPermissionListener(new QRCodeScannerView.OnCheckCameraPermissionListener() {
            @Override
            public boolean onCheckCameraPermission() {
                if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return false;
                }
            }
        });
        //开启后置摄像头
        mScannerView.setBackCamera();
    }

}
