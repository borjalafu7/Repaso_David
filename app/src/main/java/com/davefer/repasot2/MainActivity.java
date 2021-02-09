package com.davefer.repasot2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davefer.repasot2.Clases.Localizacion;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    //---- NOTIFICACIONES ----- //
    private static final String CHANNEL_ID = "NOTIFICACION";
    public static final int NOTIFICACION_ID = 0;
    PendingIntent pendingIntent;
    PendingIntent siPending, noPending;
    private static final int PENDING_REQUEST = 5;
    //---------------------------------------------//


    //---------------------------------- SHARED PREFERENCES ----------------------//
    private android.content.SharedPreferences.OnSharedPreferenceChangeListener listener;
    String preferencia;
    //----------------------------------------------------------------------------//

    //---------------------------BROADCAST RECEIVER--------------------------- //
    //El receptor ha de esperar por esta acción (coincidir el String)
    public static final String ACTION_RECEIVER = MainActivity.class.getCanonicalName() + ".ACTION_RECEIVER"; //com.catata.transmisionsender.MainActivity.ACTION_RECEIVER
    public static final String EXTRA_DATA = MainActivity.class.getCanonicalName() + ".EXTRA_DATA";
    public static final int WAITING_TIME = 5000;

    BroadcastReceiver br;
    IntentFilter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);

        br = new MyBroadcastReceiver();

        filter  = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ACTION_RECEIVER);



        //-------------Recoger preferencias al inicio ----------------//
        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,?> preferencias = sharedPreferences.getAll();
        preferencia = sharedPreferences.getString("list_preference_1","");

        Log.i("tst","Preferencia "+ preferencia);
        //----------------------------------------------------------------------//

        //--------------------- Detectar cambios en las preferencias ------------//
        listener = new android.content.SharedPreferences.OnSharedPreferenceChangeListener(){
            @Override
            public void onSharedPreferenceChanged(android.content.SharedPreferences sharedPreferences, String key) {

                Map<String,?> preferencias = sharedPreferences.getAll();
                preferencia = preferencias.get("list_preference_1").toString();
                Log.i("tst","Preferencia "+ preferencia);
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        //-----------------------------------------------------------------------//

    }

    //----------------------------- MENU CONTEXTUAL --------------------------- //
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_c, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cambio1) {
            startActivity(new Intent(this,SharedPreferences.class));
        }
        return true;
    }
    //--------------------------------------------------------------------------//


    //----------------------- MAPAS -----------------------------------//
    public void mapas(View view) {
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }
    //-----------------------------------------------------------------//

    //------------------- HTTP REQUEST -------------------------//
    public void httprequest(View view) {
        Intent i = new Intent(this,httprequest.class);
        startActivity(i);
    }
    //----------------------------------------------------------//


    //------------------------------------------------NOTIFICACIONES------------------------------------------------//

    public void sendNotificacion(View view) {
        setNoPendingIntent();
        setSiPendingIntent();
        setPendingIntent();
        createNotificacionChannel();
        crearNotificaciones();
    }

    //Versiones posteriores a Oreo
    private void createNotificacionChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Características del Canal
            CharSequence name="Notificacion Normal";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //notificationChannel.setAllowBubbles(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //Inferiores a Oreo API 26 Android 8.0
    private void crearNotificaciones(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.common_full_open_on_phone);
        builder.setContentTitle("Nombre Notificacion");
        builder.setContentText("Texto Notificación");
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Texto largo para que no cabe en una única línea"));
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA,1000,1000);
        builder.setVibrate(new long[]{1000,1000,1000, 1000});
        //builder.setSound()
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(7);
        builder.addAction(R.drawable.ic_launcher_background,"Sí", siPending);
        builder.addAction(R.drawable.ic_launcher_background,"No", noPending);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }

    private void setNoPendingIntent() {
        Intent intent = new Intent(this, notificacion_no.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        noPending = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setSiPendingIntent() {
        Intent intent = new Intent(this, notificacion_si.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        siPending = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setPendingIntent() {
        Intent intent = new Intent(this, notificacion.class);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        pendingIntent = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);


    }
//---------------------------------------------------------------------------------------------------------------//

    //------------------------------------------- HILOS ----------------------------------------------------//
    public void ejecutarHilo(View view) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                try{
                    Log.i("tst","INICIANDO HILO");

                    Thread.sleep(5000);

                }catch (Exception e){

                }

                Log.i("tst","FINALIZANDO HILO");
                tv.setText("Final 1");


                //NI IDEA PARA QUE SIRVE XD
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("tst","FINALIZANDO HILO RUNOITHREAD");
                        tv.setText("Final 2");
                    }
                });

            }
        };
        Thread hilo = new Thread(r);
        hilo.start();

    }



    //---------------------------------------------------------------------------------------------------//

    public void cargarDatos(View view) {
        Intent i = new Intent(this, CargarDatos.class);
        startActivity(i);
    }


    //-------------------------------------CUSTOM BROADCAST RECEIVER---------------------------------------//

    public void broadcastReceiver(View view) {

        ArrayList<Localizacion> listLocal= new ArrayList<Localizacion>();
        Localizacion l1 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Little Thai", "+34960261040", "restaurante", 39.43254841415737, -0.4708583178118206);
        Localizacion l2 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Foto Ya", "https://www.google.com/url?sa=t&source=web&rct=j&url=https://m.facebook.com/fotoyatorrent/&ved=2ahUKEwiV8ajujPbtAhUSuRoKHQxLBgoQFjANegQIIhAC&usg=AOvVaw1fF4l8eg8hEAPMz8Udv9u6", "comercio", 39.433283496754726, -0.4705630989702177);
        listLocal.add(l1);
        listLocal.add(l2);

        Intent i = new Intent(ACTION_RECEIVER);
        //Metemos el Array de productos como extra en el intent
        i.putParcelableArrayListExtra(EXTRA_DATA,listLocal);

        //Hacemos que no lo envíe de inmediato, esperamos 5 segundos para poder cambiar de aplicación y verlo
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBroadcast(i);
            }
        },  WAITING_TIME);

    }

    protected void onResume() {
        super.onResume();
        registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
    //-------------------------------------------------------------------------------------------------------------------//


    //-------------------------------------------- TAREAS ASINCRONAS -------------------------------------//
    public void tareaAsincrona(View view) {
        MiTareaAsincrona miTareaAsincrona = new MiTareaAsincrona();
        miTareaAsincrona.execute(21);
    }


}

class MiTareaAsincrona extends AsyncTask<Integer,String,String>{

    @Override
    protected String doInBackground(Integer... numeros) {

        for (int a=1;a<numeros[0];a++){
            publishProgress(""+a);

            try{
                Thread.sleep(1000);
            }catch(Exception e){}

        }
        Log.i("tst","SE ACABÓ LA ASINCRONIZACIÓN");
        return "fin";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       Log.i("tst","COMIENZA LA ASINCRONIZACIÓN");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("tst","DEJO DE ASINCRONAR");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.i("tst","SIGO ASINCRONANDO "+values[0]);
    }


}
//-----------------------------------------------------------------------------------------------------//

 class MyBroadcastReceiver extends BroadcastReceiver {

     private static final String TAG = "MyBroadcastReceiver";

     @Override
     public void onReceive(Context context, Intent intent) {
         boolean isMyReceiver = MainActivity.ACTION_RECEIVER.equals(intent.getAction());
         Log.i("tst",""+isMyReceiver);
         if (isMyReceiver) {
             Toast.makeText(context, "has enviado " + intent.getParcelableArrayListExtra(MainActivity.EXTRA_DATA), Toast.LENGTH_LONG).show();
         } else {
             StringBuilder sb = new StringBuilder();
             sb.append("Action: " + intent.getAction() + "\n");
             sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
             String log = sb.toString();
             Log.d(TAG, log);
             Toast.makeText(context, log, Toast.LENGTH_LONG).show();
         }

     }

 }