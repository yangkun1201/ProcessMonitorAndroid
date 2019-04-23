package yk.edu.tzc.processmonitorandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import yk.edu.tzc.processmonitorandroid.R;


public class MainActivity extends AppCompatActivity {

    Button scanBtn;
    Button faceBtn;
    String account;

    String ip = "http://39.108.95.162:8080/MonitorService/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = ((Button) findViewById(R.id.scan));
        faceBtn = ((Button) findViewById(R.id.upload_face_id));
        Intent intent = getIntent();
        account = intent.getStringExtra("account");

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ScanActivity.class);
                startActivityForResult(intent,1);
            }
        });

        faceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,FaceActivity.class);
                intent1.putExtra("account",account);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode ==1){
            String uuid = data.getStringExtra("uuid");
            //向服务端绑定account和uuid
            bindAccounAndUuid(account,uuid);
        }
    }

    public void bindAccounAndUuid(String account,String uuid){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(ip+"loginByQrCode?account="+account+"&uuid="+uuid)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"扫码失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"扫码成功",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
