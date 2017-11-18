package com.example.navin.alarmfull;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navin.alarmfull.mapscomp.GetDirectionsData;
import com.example.navin.alarmfull.mapscomp.GetNearbyPlacesData;
import com.example.navin.alarmfull.weathercomp.data.JSONWeatherParser;
import com.example.navin.alarmfull.weathercomp.data.WeatherHttpClient;
import com.example.navin.alarmfull.weathercomp.model.BadWeather;
import com.example.navin.alarmfull.weathercomp.model.Weather;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Alarm >>>>>>>>>>>>>>>>>>>
    AlarmManager alarm_manager;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    int alarm_tracks;
    DateFormat df;
    Date time1, time2;
    long delay = 900000;
    String timeNow;
    String destAddress;
    String parsedata="Colombo,LK";
    String am_pm = "AM";

    int hour = 0;
    int minute = 0;

    //Weather >>>>>>>>>>>>>>>>>>>>>>>
    private boolean weatherCondition;
    private String myAppId = "dcb6553bfccc040683d9917eedd6cfbe";
    Weather weather = new Weather();
    BadWeather badWeather = new BadWeather();

    //Maps >>>>>>>>>>>>>>>>>>>>>>>>>>
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng dest, current;
    private int distance;
    double myLat, myLng;
    double destLat, destLng;
    int durationHours=0, durationMins=0;
    int readyDurationMins=0;
    GoogleMap mMap;
    int distA_B;
    int[] durr;
    public static String durationToDest="";
    public static String distanceToDest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        update_text = (TextView)findViewById(R.id.update_alarm);
        final Calendar calendar = Calendar.getInstance(); //Create an instance of the calendar
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);//Create an intent to the alarm receiver class

        //country=(EditText)findViewById(R.id.txt_Country);
        //city=(EditText)findViewById(R.id.txt_City);


        //WEATHER >>>>>>>>>>>>>>>>>>>>>
        renderWeatherData("Colombo,LK");
        //ERROR
        //Colombo,LK
        //Spokane,US

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarms_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        //turn_on
        Button alarm_on = (Button)findViewById(R.id.alarm_on);

        //Create an onclick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(hour == 0 && minute == 0 && dest == null)) {

                    Log.d("MainActivity:SetAlarm"," Got through 1:YAYY");

                    OtherMethods otherMethods = new OtherMethods();

                    /*//COMMENTED - fixing duration and dest
                    Location locationA = new Location("Point A");
                    Location locationB = new Location("Point B");*/

                    //Have to get our location
                    myLat = 6.837140;
                    myLng = 79.929397;

                    destLat = dest.latitude;
                    destLng = dest.longitude;

                    Log.d("MainActivity:SetAlarm"," Got through 2:YAYY");

                    /*//COMMENTED - fixing duration and dest
                    locationA.setLatitude(myLat);
                    locationA.setLongitude(myLng);

                    locationB.setLatitude(destLat);
                    locationB.setLongitude(destLng);

                    float distanceA_B = locationA.distanceTo(locationB);

                    int distA_B = (int) distanceA_B;*/

                    distanceFromAtoB(myLat,myLng,destLat,destLng);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                OtherMethods exe = new OtherMethods();
                                                distA_B = exe.extractDistance(distanceToDest);
                                                durr = exe.extractDuration(durationToDest);

                                                Log.d("MainActivity:SetAlarm","Distance = "+distanceToDest);
                                                Log.d("MainActivity:SetAlarm","Duration = "+durationToDest);

                                                Log.d("MainActivity:SetAlarm","Got through 3:YAYY");

                                                if(durr.length == 1){
                                                    durationMins = durr[0];
                                                }
                                                else if (durr.length == 2){
                                                    durationHours = durr[0];
                                                    durationMins = durr[1];
                                                }
                                                else {
                                                    Log.e("MainActivity:SetAlarm","ERROR: Array durr = "+ Arrays.toString(durr));
                                                }

                                                Log.d("MainActivity:SetAlarm", "Distance is = "+Integer.toString(distA_B));
                                                //String duration = otherMethods.extractDuration(GetDirectionsData.duration);
//                    String duration="25";
//                    String[] parts = duration.split(" "); COMMENTED - fixing duration and dest

                    /*//COMMENTED - fixing duration and dest
                    if (parts.length == 1)
                    {
                        String timeMinutes = parts[0];
                        durationMins = Integer.parseInt(timeMinutes);
                    }
                    else if (parts.length == 2)
                    {
                        String timeHours = parts[0];
                        String timeMinutes = parts[1];

                        durationHours = Integer.parseInt(timeHours);
                        durationMins = Integer.parseInt(timeMinutes);
                    }
                    else {
                        Log.e("ERROR Duration ", "WRONG array");
                    }*/

                                                renderWeatherData(parsedata);

//                int hour = alarm_time_picker.getHour();
//                int minute = alarm_time_picker.getMinute();

                                                //ADD TIME
                                                hour = MyTimePicker.hoursTP;
                                                minute = MyTimePicker.minsTP;

                                                hour = hour - durationHours;
                                                minute = minute - durationMins;

                                                Log.i("MainActivity(SetAlarm)", "Hours PASS = "+Integer.toString(hour));
                                                Log.i("MainActivity(SetAlarm)", "Mins PASS = "+Integer.toString(minute));

                                                //weatherCondition=false;
                                                if (weatherCondition) {
                                                    Log.i("MainActivity(SetAlarm)","Weather data PASS = "+Boolean.toString(weatherCondition));
                                                    distA_B = distA_B * 1;
                                                    minute = minute - distA_B;
                                                    if (minute < 0) {
                                                        minute = 60 + minute;
                                                        hour = hour - 1;
                                                    }
                                                }


                                                calendar.set(Calendar.HOUR_OF_DAY, hour);//set calendar instance with hours and minutes on the time picker
                                                calendar.set(Calendar.MINUTE, minute);

                                                String hour_string = String.valueOf(hour);
                                                String minute_string = String.valueOf(minute);

                                                if (hour > 12) {

                                                    hour_string = String.valueOf(hour - 12);
                                                    am_pm = "PM";
                                                }

                                                if (minute < 10) {

                                                    minute_string = "0" + String.valueOf(minute);

                                                }

                                                set_alarm_text(hour_string + ":" + minute_string + am_pm);//changes the text in the update text box

                                                my_intent.putExtra("extra", "alarm on");//tells the clock that the alarm on button is pressed, putting extra string to my_intent
                                                my_intent.putExtra("alarm tone", alarm_tracks);//tell the app that you want a certain value from the spinner

                                                Log.e("The alarm id is", String.valueOf(alarm_tracks));

                                                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                                                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);//Create a pending intent

                                                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                                            }
                                        },4000);

//                    int distA_B = otherMethods.extractDistance(GetDirectionsData.distanceToDest);
//                    int[] durr = otherMethods.extractDuration(durationToDest);



                }

                else{
                    //Alert the user to enter the time or the Destination
                    Log.e("","Alarm set FAIL");
                    Log.e("","FAILED hour = "+Integer.toString(hour));
                    Log.e("","FAILED mins = "+Integer.toString(minute));
                    Log.e("","FAILED LatLng = "+dest.toString());
                }
            }
        });

        //turn_off
        Button alarm_off = (Button)findViewById(R.id.alarm_off);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Alarm Off");//changes the text in the update text box

                alarm_manager.cancel(pending_intent);

                my_intent.putExtra("extra", "alarm off");//tells the clock that the alarm off button is pressed, putting extra string to my_intent

                my_intent.putExtra("alarm tone",alarm_tracks);//prevent crashes in a null point exception

                sendBroadcast(my_intent);//Stops the ringtone

            }
        });


        //renderWeatherData("Spokane,US"); //ERROR
        //Colombo,LK
        //Spokane,US

    }

    public String extractCity(String fullAddress){
        if(!(fullAddress == null)){
            String[] parts = fullAddress.split(", ");
            String extracted = parts[1];
            Log.i("MainActivity(extCity)", "Extracted City = "+extracted);
            return extracted;
        }
        else{
            Log.e("MainActivity(extCity)", "Can't extract city cos the String provided is NULL");
            return null;
        }
    }

    public LatLng findNearestStation(double lat, double lng){

        LatLng nearestTrainLatLng;

        OtherMethods otherMethods = new OtherMethods();
        String keyType = "train_station";
        String trainUrl = otherMethods.getNearbyTrainUrl(lat, lng, keyType);

        Log.d("MainAct(findStation)", "TRAIN URL = "+trainUrl);

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = trainUrl;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        nearestTrainLatLng = getNearbyPlacesData.getLatLng(); // ADD CODE - GETLATLNG FROM getNearbyPlacesData

        Log.d("MainAct(findStation)","Train LatLng = "+nearestTrainLatLng.toString());

        return nearestTrainLatLng;
    }

    public void distanceFromAtoB(double A_lat, double A_lng, double B_lat, double B_lng){

        OtherMethods otherMethods = new OtherMethods();
        Object dataTransfer[] = new Object[3];
        String distanceUrl = otherMethods.getDirectionUrl(A_lat, A_lng, B_lat, B_lng);
        GetDirectionsData getDirectionsData = new GetDirectionsData();

        dataTransfer[0] = mMap;
        dataTransfer[1] = distanceUrl;
        dataTransfer[2] = new LatLng(B_lat,B_lng);

        getDirectionsData.execute(dataTransfer); // EXTRACT

        /*//COMMENT - fixing
        distance = otherMethods.extractDistance(GetDirectionsData.distanceToDest);*/

//        return distance;

    }

    /*public String durationFromAtoB(double A_lat, double A_lng, double B_lat, double B_lng){

        OtherMethods otherMethods = new OtherMethods();
        Object dataTransfer[] = new Object[3];
        String distanceUrl = otherMethods.getDirectionUrl(A_lat, A_lng, B_lat, B_lng);
        GetDirectionsData getDirectionsData = new GetDirectionsData();

        dataTransfer[0] = mMap;
        dataTransfer[1] = distanceUrl;
        dataTransfer[2] = new LatLng(B_lat,B_lng);

        getDirectionsData.execute(dataTransfer); // EXTRACT

        return GetDirectionsData.durationToDest;

    }*/

    public void renderWeatherData(String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city+"&appid="+myAppId}); //FIX if needed
        //weatherTask.execute(new String[]{city+"&appid=dcb6553bfccc040683d9917eedd6cfbe"});

    }

    /*
    FRAGMENT
     */

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_location:
            {
                // AUTOCOMPLETE FRAGMENT
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("LK")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(typeFilter)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
            break;

            case R.id.btn_time:
            {
                DialogFragment newFragment = new MyTimePicker();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                dest = place.getLatLng();
                destAddress = place.getAddress().toString();
                Log.i("Destination", "Place: " + place.getName());
                Log.i("City", extractCity(place.getAddress().toString()));
                Log.i("Address", place.getAddress().toString());
                Log.i("LatLng",dest.toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Destination", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /*
    FRAGMENT
     */

    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @Override
        protected Weather doInBackground(String... strings) {
            //data hold the whole StringBuffer that we returned from WeatherHttpClient class
            String data = ((new WeatherHttpClient()).getWeatherData(strings[0]));
            weather = JSONWeatherParser.getWeather(data);

            //Log.v("Data : ",weather.place.getCity());
            //Log.v("Data : ",weather.currentCondition.getDescription());
            String weatherSample = weather.currentCondition.getDescription();

            if ((badWeather.isCloudy(weatherSample)) || (badWeather.isRaining(weatherSample))){
                weatherCondition = true;
                Log.v("Good or Bad : BAD ", String.valueOf(weatherCondition));
            }
            else{
                weatherCondition = false;
                Log.v("Good or Bad : GOOD ", String.valueOf(weatherCondition));
            }

            Log.v("Good or Bad : ", weather.currentCondition.getDescription());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

       /* long newId = id;
        ++newId;*/

        Toast.makeText(parent.getContext(), "spinner item is "+id, Toast.LENGTH_SHORT).show();
        alarm_tracks = (int)id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // Another interface callback
    }

    private void set_alarm_text(String output) {

        update_text.setText(output);

    }

}
