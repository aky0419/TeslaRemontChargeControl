package com.example.teslaremontchargecontrol.interfaces;

import com.example.teslaremontchargecontrol.models.ChargeResponse;
import com.example.teslaremontchargecontrol.models.ChargeResponseList;
import com.example.teslaremontchargecontrol.models.TokenRequest;
import com.example.teslaremontchargecontrol.models.TokenResponse;
import com.example.teslaremontchargecontrol.models.VehicleInfo;
import com.example.teslaremontchargecontrol.models.VehicleInfoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TeslaApi {

    @POST("oauth/token")
    Call<TokenResponse> getTokenAccess(@Body TokenRequest tokenRequest);

    @GET("api/1/vehicles")
    Call<VehicleInfoList> getVehicleInfo(@Header("Authorization") String accessToken);

    @POST("api/1/vehicles/{id}/command/charge_start")
    Call<ChargeResponseList> startChargeResponse(@Path("id") String id, @Header("Authorization") String accessToken);

    @POST("api/1/vehicles/{id}/command/charge_stop")
    Call<ChargeResponseList> stopChargeResponse(@Path("id") String id, @Header("Authorization") String accessToken);

}
