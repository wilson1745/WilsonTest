package example.com.wilsontest;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

   final String tag = "MainActivity";

   //to make our alarm manager
   AlarmManager alarm_manager;
   TimePicker alarm_timepicker;
   TextView update_text;
   Context context;
   PendingIntent pending_intent;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      this.context = this;

      // initialize our alarm manager
      alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

      //initialize our timepicker
      alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

      //initialize our text update box
      update_text = (TextView) findViewById(R.id.update_text);

      // create an instance of a calendar
      final Calendar calendar = Calendar.getInstance();

      // create an intent to the Alarm Receiver class
      final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

      // initialize start button
      Button alarm_on = (Button) findViewById(R.id.alarm_on);

      // create an onClick listener to start the alarm
      alarm_on.setOnClickListener(new View.OnClickListener() {
         @TargetApi(Build.VERSION_CODES.M)
         @Override
         public void onClick(View v) {

            // setting calendar instance with the hour and minute that we picked on the time picker
            calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
            calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

            // get the int values of the hour and minute
            int hour = alarm_timepicker.getHour();
            int minute = alarm_timepicker.getMinute();

            // convert the int values to strings
            String hour_string = String.valueOf(hour);
            String minute_string = String.valueOf(minute);

            // convert 24-hour time to 12-hour time
            if (hour > 12) {
               hour_string = String.valueOf(hour - 12);
            }

            if (minute < 10) {
               //10:7 --> 10:07
               minute_string = "0" + String.valueOf(minute);
            }

            // method that changes the update text Textbox
            set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

            // put in extra string into my_intent tells the clock that you pressed the "alarm on" button
            my_intent.putExtra("extra", "alarm on");

            // put in an extra int into my_intent tells the clock that you want a certain value from the drop-down menu/spinner
            my_intent.putExtra("music", "yes");

            // create a pending intent that delays the intent until the specified calendar time
            pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // set the alarm manager
            alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
         }

      });

      // initialize the stop button
      Button alarm_off = (Button) findViewById(R.id.alarm_off);
      // create an onClick listener to stop the alarm or undo an alarm set

      alarm_off.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            // method that changes the update text Textbox
            set_alarm_text("Alarm off!");

            // cancel the alarm
            alarm_manager.cancel(pending_intent);

            // put extra string into my_intent tells the clock that you pressed the "alarm off" button
            my_intent.putExtra("extra", "alarm off");
            // also put an extra int into the alarm off section to prevent crashes in a Null Pointer Exception
            my_intent.putExtra("music", "no");

            // stop the ringtone
            sendBroadcast(my_intent);


         }
      });

   }

   private void set_alarm_text(String output) {
      update_text.setText(output);
   }
}
