package com.davefer.repasot2.modelos;

import android.content.Context;

import com.davefer.repasot2.Clases.Localizacion;
import com.davefer.repasot2.Interfaces.Persistencia;
import com.davefer.repasot2.SQLite.SQLManager;

import java.util.ArrayList;
import java.util.List;

public class GestionLocalidadesSQLite implements Persistencia {

    private Context context;

    public GestionLocalidadesSQLite(Context context) {
        this.context = context;
    }

    @Override
    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        SQLManager sQLManager = new SQLManager(context);
        localizaciones = new ArrayList<Localizacion>();
        localizaciones = (ArrayList<Localizacion>) sQLManager.selectAll();
        return localizaciones;
    }

}
