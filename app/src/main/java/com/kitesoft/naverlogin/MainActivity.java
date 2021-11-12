package com.kitesoft.naverlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    OAuthLogin oAuthLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oAuthLogin= OAuthLogin.getInstance();
        oAuthLogin.init(this, "HGe4RgZIAuf7XBx6QOmq","X0tjqkfDuR","당근장");
        //OAUTH_CLIENT_ID: 애플리케이션 등록 후 발급받은 클라이언트 아이디
        //OAUTH_CLIENT_SECRET: 애플리케이션 등록 후 발급받은 클라이언트 시크릿
        //OAUTH_CLIENT_NAME: 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름. 모바일 웹의 로그인 화면을 사용할 때는 서버에 저장된 애플리케이션 이름이 표시됩니다.
    }

    public void clickBtn(View view) {
        oAuthLogin.startOauthLoginActivity(this, new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                //Toast.makeText(MainActivity.this, ""+success, Toast.LENGTH_SHORT).show();
                if(success){
                    String accessToken= oAuthLogin.getAccessToken(MainActivity.this);
                    Log.i("token", accessToken+"");
                    //new AlertDialog.Builder(MainActivity.this).setMessage(accessToken+"").show();

                    Retrofit retrofit= new Retrofit.Builder().baseUrl("https://openapi.naver.com").addConverterFactory(GsonConverterFactory.create()).build();
                    retrofit.create(RetrofitService.class).getUserInfo("Bearer "+accessToken).enqueue(new retrofit2.Callback<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            UserInfoResponse userInfo= response.body();
                            StringBuffer buffer= new StringBuffer();
                            buffer.append(userInfo.response.nickname+"\n");
                            buffer.append(userInfo.response.profile_image+"\n");
                            buffer.append(userInfo.response.email+"\n");
                            new AlertDialog.Builder(MainActivity.this).setMessage(buffer.toString()).show();
                        }

                        @Override
                        public void onFailure(Call<UserInfoResponse> call, Throwable t) {

                        }
                    });

                }
                //new AlertDialog.Builder(MainActivity.this).setMessage(oAuthLogin.getLastErrorDesc(MainActivity.this)).show();
            }
        });
    }
}