package com.kitesoft.naverlogin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RetrofitService {

    @GET("/v1/nid/me")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String authorization);
}
