package com.example.teslaremontchargecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.teslaremontchargecontrol.DialogFragment.DatePickerFragment;
import com.example.teslaremontchargecontrol.DialogFragment.TimePickerFragment;
import com.example.teslaremontchargecontrol.interfaces.TeslaApi;
import com.example.teslaremontchargecontrol.models.VehicleInfo;
import com.example.teslaremontchargecontrol.models.VehicleInfoList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSelected {
    private static final String TAG = "MainActivity";

    String accessToken;
    List<String> vehicleIds = new ArrayList<>();
    SharedPreferences shp;
    Button pickStartTimeBt, pickStopTimeBt;

    SimpleDateFormat sdf;
    Calendar c;
    Boolean isStartCharging = false;

    TextView startChargeTv, stopChargeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickStartTimeBt = findViewById(R.id.startTimeButton);
        pickStopTimeBt = findViewById(R.id.stopTimeButton);
        startChargeTv = findViewById(R.id.startCharge);
        stopChargeTv = findViewById(R.id.stopCharge);

        pickStartTimeBt.setOnClickListener(v -> {

            isStartCharging = true;
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");




//                DatePickerFragment datePickerFragment = new DatePickerFragment();
//                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });

        pickStopTimeBt.setOnClickListener(v -> {

            isStartCharging = false;
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), "timePicker");


//                DatePickerFragment datePickerFragment = new DatePickerFragment();
//                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });



        shp = getSharedPreferences("accessToken", MODE_PRIVATE);
        accessToken = shp.getString("token", null);

        Log.d(TAG, "onCreate: " + accessToken);

        if (accessToken != null) {

            TeslaApi vehicleInfoService = RetrofitClient.getClient("https://owner-api.teslamotors.com/").create(TeslaApi.class);

            Call<VehicleInfoList> vehicleInfo = vehicleInfoService.getVehicleInfo("Bearer " + accessToken);
            vehicleInfo.enqueue(new Callback<VehicleInfoList>() {
                @Override
                public void onResponse(Call<VehicleInfoList> call, Response<VehicleInfoList> response) {
                    int statusCode = response.code();
                    List<VehicleInfo> responses = response.body().getResponse();

                    for (int i = 0; i < responses.size(); i++) {
                        vehicleIds.add(responses.get(i).getId());
                    }


                    Log.d(TAG, "onResponse: " + statusCode + vehicleIds);
                }

                @Override
                public void onFailure(Call<VehicleInfoList> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                }
            });


        }



    }


    @Override
    public void onTimeSelected(TimePicker view, int hourOfDay, int minute) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("vehicleId", vehicleIds.get(0));
        intent.putExtra("accessToken", accessToken);

            if (isStartCharging) {
                startChargeTv.setText("Start Charge at " + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
                intent.putExtra("pendingIntent", 1);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 1, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

                //post request to Tesla to start charging
            } else {
                stopChargeTv.setText("Stop Charge at " + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
                intent.putExtra("pendingIntent", 2);
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 2, intent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
                //post stop charge to Tesla
            }



        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }







    }
}
