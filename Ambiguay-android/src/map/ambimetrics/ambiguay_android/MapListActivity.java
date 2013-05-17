package map.ambimetrics.ambiguay_android;

import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.AmigosTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapListActivity extends FragmentActivity implements LocationListener  {
	TabHost tHost;
	
	private GoogleMap mapa = null;

	
	public Uri amigoUri = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();
        
        
  
        
        
        /** Defining Tab Change Listener event. This is invoked when tab is changed */
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
				//MapaFragment mapFragment = (MapaFragment) fm.findFragmentByTag("map");
				
				com.google.android.gms.maps.SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.mapa);
				mapa = mapFragment.getMap();
				
				extrasMapa();
				
			
				AmigosFragment amigosFragment = (AmigosFragment) fm.findFragmentByTag("amigos");
				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
				
								
				/** Detaches the mapfragment if exists */
				if(mapFragment!=null)
					ft.detach(mapFragment);
				
				/** Detaches the amigosfragment if exists */
				if(amigosFragment!=null)
					ft.detach(amigosFragment);
				
				/** If current tab is map */
				if(tabId.equalsIgnoreCase("map")){

						ft.attach(mapFragment);						

				}else{	/** If current tab is amigos */
					if(amigosFragment==null){
						/** Create AmigosFragment and adding to fragment transaction */
						ft.add(R.id.realtabcontent2,new AmigosFragment(), "amigos");						
					}else{
						/** Bring to the front, if already exists in the fragment transaction */
						ft.attach(amigosFragment);						
					}
				}
				
				
				ft.commit();				
			}
			
		};
			
		
		/** Setting tabchangelistener for the tab */
		tHost.setOnTabChangedListener(tabChangeListener);
                
		/** Defining tab builder for Andriod tab */
		
        TabHost.TabSpec tSpecMap = tHost.newTabSpec("map");
        tSpecMap.setIndicator("Map");        
        tSpecMap.setContent(R.id.tab1);
        tHost.addTab(tSpecMap);
        
        
        /** Defining tab builder for Amigos tab */
        TabHost.TabSpec tSpecAmigos = tHost.newTabSpec("amigos");
        tSpecAmigos.setIndicator("Amigos");        
        tSpecAmigos.setContent(R.id.tab2);
        tHost.addTab(tSpecAmigos);

        marcarAmigos();
     }


    public void extrasMapa(){
    	
    	mapa.setMyLocationEnabled(true);
		
		UiSettings UI =mapa.getUiSettings();
		
		UI.setMyLocationButtonEnabled(mapa.isMyLocationEnabled());

    	
    	
    }
    
	public void mostrarMarcador(double lat, double lng, String titulo, int sexo)
	{
		float color = BitmapDescriptorFactory.HUE_YELLOW;
		if (sexo == 1){
			color = BitmapDescriptorFactory.HUE_AZURE;
		}else if(sexo == 2){
			color = BitmapDescriptorFactory.HUE_MAGENTA;
		}
			
		
		
		mapa.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))
        .title(titulo).icon(BitmapDescriptorFactory.defaultMarker(color)));
	}
    
	public void marcarAmigos(){
		
		
		String[] projection = { AmigosTable.COLUMN_ID, AmigosTable.COLUMN_NOMBRE, AmigosTable.COLUMN_APELLIDOS, AmigosTable.COLUMN_LAT, AmigosTable.COLUMN_LONG, AmigosTable.COLUMN_SEXO };
		Cursor cursor = getContentResolver().query(MyAmigosContentProvider.CONTENT_URI1, projection, null, null,
		        null);   
		    if (cursor != null) {
		    	cursor.moveToFirst();
		    	String lat = cursor.getString(cursor
		                .getColumnIndexOrThrow(AmigosTable.COLUMN_LAT));
		    	String longitud = cursor.getString(cursor
		                .getColumnIndexOrThrow(AmigosTable.COLUMN_LONG));
		    	String nombre = cursor.getString(cursor
		                .getColumnIndexOrThrow(AmigosTable.COLUMN_NOMBRE));
		    	String sexo = cursor.getString(cursor
		                .getColumnIndexOrThrow(AmigosTable.COLUMN_SEXO));
		    	mostrarMarcador(Double.valueOf(lat).doubleValue(),Double.valueOf(longitud).doubleValue(),nombre, Integer.parseInt(sexo));
		    	while(cursor.moveToNext()){
		    		lat = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_LAT));
			    	longitud = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_LONG));
			    	nombre = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_NOMBRE));
			    	sexo = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_SEXO));
			    	mostrarMarcador(Double.valueOf(lat).doubleValue(),Double.valueOf(longitud).doubleValue(),nombre, Integer.parseInt(sexo));
		    	}
		    	
		    }
		
	}
   

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}   
    

 	
}

