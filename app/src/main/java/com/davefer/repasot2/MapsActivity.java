package com.davefer.repasot2;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.davefer.repasot2.Clases.Localizacion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Toolbar tb1;
    Drawer mDrawer;
    Marker locAE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tb1 = (Toolbar)findViewById(R.id.toolbar);

        new DrawerBuilder().withActivity(this).build();
        //builder de la cabecera del materialdrawer
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.md_blue_100)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("AppRepaso 2T")
                                .withEmail("davefer033@gmail.com")
                                .withIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                )
                .build();

        //Elementos del Material Drawer
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(tb1)
                .withActionBarDrawerToggle(true)
                .withDrawerGravity(Gravity.START)
                .withSliderBackgroundColor(getResources().getColor(android.R.color.white))

                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Ver mi localización"),

                        new SecondaryDrawerItem()
                                .withIdentifier(2)
                                .withName("Ocultar mi localización"),

                        new DividerDrawerItem(),

                        new PrimaryDrawerItem()
                            .withIdentifier(3)
                            .withName("Mostrar Ayuntamiento"),

                        new SecondaryDrawerItem()
                            .withIdentifier(4)
                            .withName("Ocultar Ayuntamiento"),

                        new DividerDrawerItem(),

                        new PrimaryDrawerItem()
                                .withIdentifier(5)
                                .withName("Mostrar Puertos"),

                        new PrimaryDrawerItem()
                                .withIdentifier(6)
                                .withName("Mostrar Aeropuertos"),

                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName("Ocultar todo"),

                        new DividerDrawerItem(),

                        new SecondaryDrawerItem()
                                .withIdentifier(8)
                                .withName("Cerrar Menu"),

                        new SecondaryDrawerItem()
                                .withIdentifier(9)
                                .withName("Salir App")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {


                            case 3: {
                                mostarAyuntamiento(true);
                                break;
                            }

                            case 4:{
                                mostarAyuntamiento(false);
                                break;
                            }

                            case 5:{
                                verOtros("puerto");
                                break;
                            }

                            case 6:{
                                verOtros("aeropuerto");
                                break;
                            }

                            case 7:{
                                verOtros("nada");
                                break;
                            }

                            case 8:{
                                mDrawer.closeDrawer();
                                break;
                            }
                            case 9:{
                                finish();
                                break;
                            }
                        }
                        return false;
                    }
                }).build();

    }

    private void mostarAyuntamiento(Boolean condition) {
        if(condition == true){ locAE.setVisible(true); }
        if(condition == false){ locAE.setVisible(false); }
    }

    private void verOtros(String eleccion) {
        List<Localizacion> localizaciones;
        localizaciones = leerXML();

        if(eleccion.equals("nada")){
            mMap.clear();

        }else{

            for(Localizacion localizacion:localizaciones){

                if(eleccion.equals("puerto")){
                    if(localizacion.getEtiqueta().equals("puerto")){
                        LatLng posAE = new LatLng(localizacion.getLatitud(),localizacion.getLongitud());
                         locAE = mMap.addMarker(new MarkerOptions().position(posAE)
                                .title(localizacion.getTitulo())
                                .snippet(localizacion.getFragmento())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ayuntamiento)));

                        locAE.setTag(localizacion.getEtiqueta());
                    }
                }else if (eleccion.equals("aeropuerto")){
                    if(localizacion.getEtiqueta().equals("aeropuerto")){
                        LatLng posAE = new LatLng(localizacion.getLatitud(),localizacion.getLongitud());
                         locAE = mMap.addMarker(new MarkerOptions().position(posAE)
                                .title(localizacion.getTitulo())
                                .snippet(localizacion.getFragmento())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ayuntamiento)));

                        locAE.setTag(localizacion.getEtiqueta());
                    }
                }

            }

        }

    }

    private List<Localizacion> leerXML(){
        List<Localizacion> localizaciones = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getResources().openRawResource(R.raw.localizaciones_propias));
            Element raiz = doc.getDocumentElement();
            NodeList items = raiz.getElementsByTagName("localizacion");

            for (int i = 0; i < items.getLength(); i++) {
                Node nodoLocalizacion = items.item(i);
                Localizacion localizacion = new Localizacion();

                for (int j = 0; j < nodoLocalizacion.getChildNodes().getLength() - 1; j++) {
                    Node nodoActual = nodoLocalizacion.getChildNodes().item(j);
                    if (nodoActual.getNodeType() == Node.ELEMENT_NODE) {
                        if (nodoActual.getNodeName().equalsIgnoreCase("titulo")) {
                            localizacion.setTitulo(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("fragmento")) {
                            localizacion.setFragmento(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("etiqueta")) {
                            localizacion.setEtiqueta(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("latitud")) {
                            String latitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLatitud(Double.parseDouble(latitud));
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("longitud")) {
                            String longitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLongitud(Double.parseDouble(longitud));
                        }
                    }
                }
                localizaciones.add(localizacion);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localizaciones;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // -- Cambiar el estilo del mapa, indicando en un .json
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        // -------------------------Colocar Marcador automaticamente en Torrent --------------------------- //

        // -- Latitud del marcador
        LatLng torrent = new LatLng(39.431853268538596, -0.4728578637526629);
        // -- Colocar el marcador en la latitud Indicada  - .Tittle() para poner un titulo
        mMap.addMarker(new MarkerOptions().position(torrent).title("Torrent"));
        // -- Mover la camara para situar el marcador en el centro de esta
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(torrent));

        // -- Mover la camara para situar el marcador en el centro de esta CON ZOOM
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(torrent,12f));
        // ----------------------------------------------------------------------------------------------- //


        // ----------------------------------- MARCADOR CON ICONO PERSONALIZADO Y SNIPET ------------------------//
        LatLng posAE = new LatLng(39.436763732082454, -0.46602458865181545);
        locAE = mMap.addMarker(new MarkerOptions().position(posAE)
                .title("Ayuntamiento de Torrent")
                .snippet("610774203")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ayuntamiento)));

        locAE.setTag("Ayuntamiento");
        locAE.setVisible(false);

        // ------------------------------------------------------------------------------------------------------//

    }
}