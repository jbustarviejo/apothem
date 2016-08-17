package telefonica.tiws.grtu.apothem;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class FragmentLocation extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;
    private static LatLngBounds latLngBounds;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        progressDialog=ProgressDialog.show(getActivity(), "Cargando", "Espera mientras se representa el mapa...");

        View rootView = inflater.inflate(R.layout.content_location, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                //Disable directions
                mMap.getUiSettings().setMapToolbarEnabled(false);

                //Get pointers
                DataBase dataBase = new DataBase();
                List<DataBase.LocationRecord> locationRecordList =  dataBase.getPositionsHistory(getActivity(), true);

                LatLng position = null;
                LatLng lastPosition = null;
                Marker previousMarker=null;

                PolylineOptions rectOptions = new PolylineOptions();
                //this is the color of route
                rectOptions.color(getActivity().getResources().getColor(R.color.telefonicaColorLight));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                int count=1;
                for(int i=0; i<locationRecordList.size(); i++){
                    DataBase.LocationRecord locationRecord = locationRecordList.get(i);
                    Log.d("Marker",locationRecord.toJSON());
                    lastPosition=position;
                    position = new LatLng(locationRecord.latitude, locationRecord.longitude);

                    if(position.equals(lastPosition) && count>1) {
                        Log.d("Marker","Skipped previous");
                        if(previousMarker!=null){
                            previousMarker.remove();
                            continue;
                        }
                    }

                    /*if(lastPosition!=null) {
                        //Calculate if are very near
                        Location loc1 = new Location("");
                        loc1.setLatitude(position.latitude);
                        loc1.setLongitude(position.longitude);

                        Location loc2 = new Location("");
                        loc2.setLatitude(lastPosition.latitude);
                        loc2.setLongitude(lastPosition.longitude);

                        float distanceInMeters = loc1.distanceTo(loc2);

                        if (distanceInMeters < 30 && count>1) {
                            Log.d("Marker", "Skipped previous for distance=" + distanceInMeters);
                        }else {
                            previousMarker = mMap.addMarker(new MarkerOptions().position(position).title(locationRecord.getDate()).snippet(locationRecord.stationInfo.type+locationRecord.stationInfo.power)));
                            builder.include(position);
                            rectOptions.add(position);
                        }
                    }*/

                    previousMarker = mMap.addMarker(new MarkerOptions().position(position).title(locationRecord.getDate()).snippet(locationRecord.stationInfo.type+": "+locationRecord.stationInfo.power+" ("+locationRecord.stationInfo.signal+"/4)"));
                    builder.include(position);
                    rectOptions.add(position);
                }

                if(position!=null){
                    try {
                        latLngBounds = builder.build();
                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 300));
                                progressDialog.hide();
                            }
                        });
                        mMap.addPolyline(rectOptions);
                    }catch (Exception e){
                        e.printStackTrace();
                        progressDialog.hide();
                    }
                }else{
                    progressDialog.hide();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Registro vacío")
                            .setMessage("El registro de datos está vacío")
                            .setPositiveButton("Aceptar", null)
                            .show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}