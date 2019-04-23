package yk.edu.tzc.processmonitorandroid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yk.edu.tzc.processmonitorandroid.R;

public class LoginActivity extends AppCompatActivity {

    EditText accountEdit;
    EditText passwordEdit;
    Button okButton;

    String ip = "http://39.108.95.162:8080/MonitorService/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        accountEdit = ((EditText) findViewById(R.id.account));
        passwordEdit = ((EditText) findViewById(R.id.password));
        okButton = ((Button) findViewById(R.id.ok));

        //存在历史登陆记录，免登陆
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String localAccount = sharedPreferences.getString("account","");
        if(localAccount != ""){
            //跳转到主页面
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("account",localAccount);
            startActivity(intent);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                login(account,password);
            }
        });
    }

    public void login(final String account, String password){
        Map<String,Object> params = new HashMap<>();
        params.put("account",account);
        params.put("password",password);
        final Gson gson = new Gson();
        String paramsInJson = gson.toJson(params);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),paramsInJson);
        final Request request = new Request.Builder()
                .post(requestBody)
                .url(ip+"login")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),"登陆失败,请检查网络设置",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                Type type = new TypeToken<HashMap<String,Object>>(){}.getType();
                Map<String,Object> resultData = gson.fromJson(responseData,type);
                Double code0 = ((Double) resultData.get("code"));
                final int code = code0.intValue();
                //System.out.println(code);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 0){
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                            //记录已登陆账号信息
                            SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("account",account);
                            editor.commit();

                            //跳转到主页面
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("account",account);
                            startActivity(intent);
                        }else if(code == 1){
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }else if(code == 2){
                            Toast.makeText(LoginActivity.this,"用户不存在，请先注册",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

}
