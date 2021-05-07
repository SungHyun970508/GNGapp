package com.example.gngapppro;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    //서버 URL 설정 (php 파일 연동)
    final static private String URL = "http://10.10.10.23/Login.php";
    private Map<String, String> map;

    public LoginRequest(String userID, String PW, Response.Listener<String> listener){
        //POST는 서버 전송? 방식 중 하나
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();
        map.put("userID",userID);
        map.put("PW",PW);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
