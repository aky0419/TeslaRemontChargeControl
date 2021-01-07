package com.example.teslaremontchargecontrol.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.teslaremontchargecontrol.MainActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerFragment";

    public interface OnTimeSelected{
        void onTimeSelected(TimePicker view, int hourOfDay, int minute);
    }

    OnTimeSelected mOnTimeSelected;

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            mOnTimeSelected = (OnTimeSelected) getActivity();
        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: " + e.getMessage());

        }

        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        mOnTimeSelected.onTimeSelected(view,hourOfDay,minute);



    }


}
