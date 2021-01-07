package com.example.teslaremontchargecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

import com.example.teslaremontchargecontrol.interfaces.TeslaApi;
import com.example.teslaremontchargecontrol.models.ChargeResponseList;
import com.example.teslaremontchargecontrol.models.TokenRequest;
import com.example.teslaremontchargecontrol.models.TokenResponse;
import com.example.teslaremontchargecontrol.models.VehicleInfoList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String accessToken = intent.getStringExtra("accessToken");
        String vehicleId = intent.getStringExtra("vehicleId");
        Log.d(TAG, "onReceive: " + accessToken + " " + vehicleId);
        if (intent.getIntExtra("pendingIntent", 0) == 1) {
            //post request to tesla to start charging
            Log.d(TAG, "onReceive: start charging" );
            TeslaApi startChargeService = RetrofitClient.getClient("https://owner-api.teslamotors.com/").create(TeslaApi.class);
            Call<ChargeResponseList> chargeResponse = startChargeService.startChargeResponse(vehicleId, "Bearer " + accessToken);
            chargeResponse.enqueue(new Callback<ChargeResponseList>() {
                @Override
                public void onResponse(Call<ChargeResponseList> call, Response<ChargeResponseList> response) {
                    int responseCode = response.code();
                    Boolean result = response.body().getResponse().getResult();
                    String reason = response.body().getResponse().getReason();
                    Log.d(TAG, "onStartChargeResponse: " + responseCode + " " + result + reason);


                }

                @Override
                public void onFailure(Call<ChargeResponseList> call, Throwable t) {

                }
            });


        } else if (intent.getIntExtra("pendingIntent", 0) == 2) {
            Log.d(TAG, "onReceive: stop charging" );
            //post request to tesla to stop charging
            TeslaApi stopChargeService = RetrofitClient.getClient("https://owner-api.teslamotors.com/").create(TeslaApi.class);
            Call<ChargeResponseList> chargeResponse = stopChargeService.stopChargeResponse(vehicleId, "Bearer " + accessToken);
            chargeResponse.enqueue(new Callback<ChargeResponseList>() {
                @Override
                public void onResponse(Call<ChargeResponseList> call, Response<ChargeResponseList> response) {
                    int responseCode = response.code();
                    Boolean result = response.body().getResponse().getResult();
                    String reason = response.body().getResponse().getReason();
                    Log.d(TAG, "onStopChargeResponse: " + responseCode + " " + result + reason);
                }

                @Override
                public void onFailure(Call<ChargeResponseList> call, Throwable t) {

                }
            });


        }


    }
}
