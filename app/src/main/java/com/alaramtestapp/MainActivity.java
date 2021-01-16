package com.alaramtestapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "MainActivity";

    private EditText etDate, etTime;
    private Button btnSaveAlarm;

    private Calendar calendar = Calendar.getInstance();

    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = (EditText) findViewById(R.id.et_date);
        etTime = (EditText) findViewById(R.id.et_time);
        btnSaveAlarm = (Button) findViewById(R.id.btn_set_alarm);

        etTime.setOnClickListener(v -> _selectTime());

        etDate.setOnClickListener(v -> _selectDate());

        btnSaveAlarm.setOnClickListener(v -> _setAlarm());
    }

    private void _selectTime() {
        DialogFragment timePicker = new TimePickerDialogFragment();
        timePicker.show(getSupportFragmentManager(), "TIME");
    }

    private void _selectDate() {
        DialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.show(getSupportFragmentManager(), "DATE");
    }

    private void _setAlarm() {

        if (!isDateSelected) {
            Toast.makeText(MainActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isTimeSelected) {
            Toast.makeText(MainActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 145, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

//        AlarmManagerCompat.setExact(alarmManager, AlarmManager.RTC, System.currentTimeMillis() + (10 * 1000), pendingIntent);
        AlarmManagerCompat.setExact(alarmManager, AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(MainActivity.this, "Alarm Set...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        etTime.setText(time);
        isTimeSelected = true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        etDate.setText(date);
        isDateSelected = true;
    }
}
