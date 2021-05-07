package com.example.gngapppro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



public class LoginActivity extends AppCompatActivity  {

    private EditText et_id, et_pw;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id=findViewById(R.id.et_id);
        et_pw=findViewById(R.id.et_pw);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);

        //회원가입 버튼을 클릭 시 수행 화면전환 로그인화면에서 회원가입화면으로
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어있는 값을 get해옴
                String userID=et_id.getText().toString();
                String PW=et_pw.getText().toString();

                //여기서 가져온 ID로 새로운 firebase 데이터베이스를 만들 수 있을까?

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("aa","aa");
                            //성공여부를 담는 변수
                            boolean success=jsonObject.getBoolean("success"); //이 success는 register.php안의 변수명
                            if(success)
                                Log.d("ss", "ss");
                            if(success){ //로그인에 성공한 경우

                                String userID=jsonObject.getString("userID");
                                String faceIDX=jsonObject.getString("faceIDX");
                                String PW=jsonObject.getString("PW"); //""안에 있는 이름은 php에 있는것과 동일
                                Log.e("user", faceIDX);
                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                                if(faceIDX.equals("-1")) { // 사진 찍어야 합니다.
                                    Intent intent = new Intent(LoginActivity.this, CameraActivity.class);//카메라로 넘어가야하지만 우선 메인으로 바꿔놓음!!!!!!!!!!!!!!!

                                    Toast.makeText(getApplicationContext(),"사진이 필요합니다..",Toast.LENGTH_SHORT).show();
                                    //현재로그인한 사람이 이사람임을 알려주기 위해 담아줌
                                    intent.putExtra("userID",userID);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);//쇼핑으로 넘어가야하지만 우선 메인으로 바꿔놓음!!!!!!!!!!!!!!!
                                    Toast.makeText(getApplicationContext(),"쇼핑 화면으로 전환..",Toast.LENGTH_SHORT).show();
                                    //현재로그인한 사람이 이사람임을 알려주기 위해 담아줌
                                    intent.putExtra("userID",userID);
                                    startActivity(intent);
                                }

                                //intent.putExtra("PW",PW);
                            }else{ //로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest=new LoginRequest(userID, PW ,responseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

                //cameraActivity로 넘어감!
                Intent intent = new Intent(
                        getApplicationContext(),
                        //LoginActivity.class);
                        MainActivity.class);//카메라로넘어가야하지만 우선 메인으로 바꿔놓음!!!!!!!!!!!!!!!
                startActivity(intent);
            }
        });
    }

}