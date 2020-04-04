package com.example.calendartest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static java.time.temporal.ChronoUnit.DAYS;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        Calendar cal = Calendar.getInstance();
        Calendar tomorrow = (Calendar) cal.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        LocalDate today = LocalDate.now();
        String daysSinceEpoch = Long.toString(today.toEpochDay());

        String daysToMax = Long.toString(today.until(LocalDate.MAX, DAYS));

        LocalDate epoch = LocalDate.ofEpochDay(0);
        LocalDate epoch100 = epoch.plusYears(100);

        String hundredYears = Long.toString(epoch.until(epoch100, DAYS));

        //36525

        textView.setText(epoch100.toString());
    }


}
