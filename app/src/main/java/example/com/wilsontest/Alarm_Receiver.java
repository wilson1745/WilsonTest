package example.com.wilsontest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Alarm_Receiver extends BroadcastReceiver {

   final String tag = "Alarm_Receiver";

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.e(tag, "We are in the receiver!");

      Calendar c = Calendar.getInstance();
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int minute = c.get(Calendar.MINUTE);

      // fetch extra strings from the intent tells the app whether the user pressed the alarm on button or the alarm off button
      String alarmState = intent.getExtras().getString("extra");
      String musicState = intent.getExtras().getString("music");
      Log.e(tag,"alarmState: " + alarmState);
      Log.e(tag,"musicState: " + musicState);
      Log.e(tag,"time: " + hour + " : " + minute);

      // create an intent to the ringtone service
      Intent service_intent = new Intent(context, RingtonePlayingService.class);

      service_intent.putExtra("extra", alarmState);
      service_intent.putExtra("music", musicState);

      // start the ringtone service
      context.startService(service_intent);
   }
}
