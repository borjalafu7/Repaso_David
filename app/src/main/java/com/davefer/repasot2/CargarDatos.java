package com.davefer.repasot2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.davefer.repasot2.Interfaces.Persistencia;
import com.davefer.repasot2.SQLite.SQLManager;
import com.davefer.repasot2.Clases.Localizacion;
import com.davefer.repasot2.modelos.GestionLocalidadesFichero;
import com.davefer.repasot2.modelos.GestionLocalidadesSQLite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CargarDatos extends AppCompatActivity {

    private android.content.SharedPreferences.OnSharedPreferenceChangeListener listener;
    String preferencia;

    private Persistencia gestionLocalidades;
    private List<Localizacion> mLocalizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_datos);

        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,?> preferencias = sharedPreferences.getAll();
        preferencia = sharedPreferences.getString("list_preference_1","");

        Log.i("tst","Preferencia "+ preferencia);

        //if(preferencia.equals("FICHERO")){
        // rellenarFichero();
        //}

        //if(preferencia.equals("SQL")){
        //   rellenarSQL();
        //}

        recogerInfo();

    }

    private void rellenarFichero() {
        Log.i("tst","Rellenando Fichero");
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        Localizacion l1 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Restaurante Asador Azorín", "+34961574702", "restaurante", 39.43162415426558, -0.4764209532649998);
        Localizacion l2 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Folder Papelerías", "http://www.folder.es/", "comercio", 39.43314605170049, -0.47139780673038345);
        localizaciones.add(l1);
        localizaciones.add(l2);

        File appFilesDirectory = this.getFilesDir();
        try {
            FileOutputStream fos = new FileOutputStream(new File(appFilesDirectory, "misLocalizaciones.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(localizaciones);
            oos.close();
        } catch (IOException ex) {
            Log.i("info","Fallo al guardar el fichero");
            Log.i("info", String.valueOf(ex));
        }

    }

    private void rellenarSQL() {
        Log.i("tst","Rellenando SQL");
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        Localizacion l1 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Little Thai", "+34960261040", "restaurante", 39.43254841415737, -0.4708583178118206);
        Localizacion l2 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Foto Ya", "https://www.google.com/url?sa=t&source=web&rct=j&url=https://m.facebook.com/fotoyatorrent/&ved=2ahUKEwiV8ajujPbtAhUSuRoKHQxLBgoQFjANegQIIhAC&usg=AOvVaw1fF4l8eg8hEAPMz8Udv9u6", "comercio", 39.433283496754726, -0.4705630989702177);
        localizaciones.add(l1);
        localizaciones.add(l2);

        SQLManager sQLManager = new SQLManager(this);
        for(Localizacion localizacion: localizaciones){
            sQLManager.insert(localizacion);
        }

    }

    private void recogerInfo(){
        if(preferencia.equals("SQL")){
            gestionLocalidades = new GestionLocalidadesSQLite(this);
        }else if(preferencia.equals("FICHERO")){
            gestionLocalidades = new GestionLocalidadesFichero(this);
        }

        mLocalizaciones = gestionLocalidades.leerLocalizaciones();

        sacarInfo();
    }

    private void sacarInfo() {
        for(int a=0;a<mLocalizaciones.size();a++){
            Log.i("tst","Localizacion en "+preferencia+" "+ mLocalizaciones.get(a).toString());
            Toast t = Toast.makeText(this,mLocalizaciones.get(a).toString() ,Toast.LENGTH_SHORT);
            t.show();
        }
    }

}