package map.ambimetrics.ambiguay_android;

import map.ambimetrics.comunicacion.RequestMethod;
import map.ambimetrics.comunicacion.RestClient;
import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.AmigosTable;
import map.ambimetrics.database.UsuarioTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapListActivity extends FragmentActivity implements LocationListener  {
	TabHost tHost;
	
	private static final String URL = RequestMethod.URL;
	private String Respuesta = null;
	private String EmailU = null;
	private String TokenU = null;
	
	private double dLatitude = 200;
    private double dLongitude = 200;
	
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
					mapa.clear();
					extrasMapa();
					usuarioToken();
					ft.attach(mapFragment);						

				}else{	/** If current tab is amigos */
					mapa.clear();
					extrasMapa();
					usuarioToken();
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
        
        //addAmigo();
        //extrasMapa();
        mapa.clear();
		extrasMapa();
        	
		marcarAmigos();

     }


    public void extrasMapa(){
    	
    	mapa.setMyLocationEnabled(true);
		
		UiSettings UI =mapa.getUiSettings();
		
		UI.setMyLocationButtonEnabled(mapa.isMyLocationEnabled());
		
		Location myLocation  = mapa.getMyLocation();

	    if(myLocation!=null)
	    {
	        dLatitude = myLocation.getLatitude();
	        dLongitude = myLocation.getLongitude();
	    }else{
	    	/*
	    	Toast toast1 = Toast.makeText(getApplicationContext(),
					"No es posible actualizar", Toast.LENGTH_LONG);

			toast1.show();
			*/	
	    }
    }
    
	public void mostrarMarcador(double lat, double lng, String titulo, int sexo, int mostrar)
	{
		

		float color = BitmapDescriptorFactory.HUE_YELLOW;
		if (sexo == 1){
			color = BitmapDescriptorFactory.HUE_AZURE;
		}else if(sexo == 2){
			color = BitmapDescriptorFactory.HUE_VIOLET;
		}
		if (mostrar == 0){
			color = BitmapDescriptorFactory.HUE_RED;
		}
		mapa.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))
        .title(titulo).icon(BitmapDescriptorFactory.defaultMarker(color)));
	}

	public void marcarAmigos(){
		
		Cursor cursor = null;
		String[] projection = { AmigosTable.COLUMN_ID, AmigosTable.COLUMN_NOMBRE, AmigosTable.COLUMN_APELLIDOS, AmigosTable.COLUMN_LAT, AmigosTable.COLUMN_LONG, AmigosTable.COLUMN_SEXO, AmigosTable.COLUMN_MOSTRAR };
		cursor = getContentResolver().query(MyAmigosContentProvider.CONTENT_URI1, projection, null, null,
		        null);   
		    if (cursor!=null) {
		    	if (cursor.moveToFirst()) {
			    	String lat = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_LAT));
			    	String longitud = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_LONG));
			    	String nombre = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_NOMBRE));
			    	String sexo = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_SEXO));
			    	String mostrar = cursor.getString(cursor
			                .getColumnIndexOrThrow(AmigosTable.COLUMN_MOSTRAR));
			    	
			    	
			    	mostrarMarcador(Double.valueOf(lat).doubleValue(),Double.valueOf(longitud).doubleValue(),nombre, Integer.parseInt(sexo), Integer.parseInt(mostrar));
			    	while(cursor.moveToNext()){
			    		lat = cursor.getString(cursor
				                .getColumnIndexOrThrow(AmigosTable.COLUMN_LAT));
				    	longitud = cursor.getString(cursor
				                .getColumnIndexOrThrow(AmigosTable.COLUMN_LONG));
				    	nombre = cursor.getString(cursor
				                .getColumnIndexOrThrow(AmigosTable.COLUMN_NOMBRE));
				    	sexo = cursor.getString(cursor
				                .getColumnIndexOrThrow(AmigosTable.COLUMN_SEXO));
				    	mostrar = cursor.getString(cursor
				                .getColumnIndexOrThrow(AmigosTable.COLUMN_MOSTRAR));
				    	
				    	mostrarMarcador(Double.valueOf(lat).doubleValue(),Double.valueOf(longitud).doubleValue(),nombre, Integer.parseInt(sexo), Integer.parseInt(mostrar));
			    	}
		    	}
		    }
		
	}

	public void addAmigo(String nombre, String apellidos, String telefono,
	String email, String sexo, String lat, String longitud, String mostrar) {
		

			/*
			String nombre = a.getString(AmigosTable.COLUMN_NOMBRE);
			String apellidos = a.getString(AmigosTable.COLUMN_APELLIDOS);
			String telefono = a.getString(AmigosTable.COLUMN_TELEFONO);
			String email = a.getString(AmigosTable.COLUMN_EMAIL);
			String sexo = a.getString(AmigosTable.COLUMN_SEXO);
			String lat = a.getString(AmigosTable.COLUMN_LAT);
			String longitud = a.getString(AmigosTable.COLUMN_LONG);
			String mostrar = "1";
			*/

			ContentValues values = new ContentValues();
		    values.put(AmigosTable.COLUMN_NOMBRE, nombre);
		    values.put(AmigosTable.COLUMN_APELLIDOS, apellidos);
		    values.put(AmigosTable.COLUMN_TELEFONO, telefono);
		    values.put(AmigosTable.COLUMN_EMAIL, email);
		    values.put(AmigosTable.COLUMN_SEXO, sexo);
		    if (lat.equals("null") || longitud.equals("null")){
		    	values.put(AmigosTable.COLUMN_LAT, "90");
			    values.put(AmigosTable.COLUMN_LONG, "90");
		    }else{
		    values.put(AmigosTable.COLUMN_LAT, lat);
		    values.put(AmigosTable.COLUMN_LONG, longitud);
		    }
		    values.put(AmigosTable.COLUMN_MOSTRAR, mostrar);
			 
		 
		    Uri amigo = getContentResolver().insert(MyAmigosContentProvider.CONTENT_URI1, values);
		    
		
	}
	
	public void usuarioToken(){
		//Actualizar
		//1 Buscar token
		Cursor cursor = null;
		String[] projection = { UsuarioTable.COLUMN_ID, UsuarioTable.COLUMN_EMAIL, UsuarioTable.COLUMN_TOKEN};
		cursor = getContentResolver().query(MyAmigosContentProvider.CONTENT_URI2, projection, null, null,
		        null);
		if (cursor!=null) {
			if (cursor.moveToFirst()) {
		    	TokenU = cursor.getString(cursor
		                .getColumnIndexOrThrow(UsuarioTable.COLUMN_TOKEN));
		    	EmailU = cursor.getString(cursor
	                .getColumnIndexOrThrow(UsuarioTable.COLUMN_EMAIL));
			}
		}
		//2 Petición actualizar
		if ((TokenU!=null && EmailU!=null)&&(dLongitude!=90 && dLatitude!=180)){
						
			new DownloadDataTask().execute();
			
		}
		
	}
	private void sendJSON(){
	    
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
	  		cadena.put("tag", "actualizar");
	  		cadena.put("email", EmailU);
	  		cadena.put("token", TokenU);
		    cadena.put("latitud",  dLatitude);
		    cadena.put("longitud", dLongitude);
		   
	  		//Le asignamos los datos que necesitemos
		}catch (JSONException e) {
        	e.printStackTrace();
        }
		

		
		RestClient client = new RestClient(URL);
		client.AddParam("JSON", cadena.toString());
		Respuesta = client.getResponse();


		try {
		    client.Execute(RequestMethod.POST);
					    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		Respuesta = client.getResponse();
	
		
	}
	
	private int respuestaJSON(JSONObject cadena){
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        int resp = 1;
        try {
        	String error = cadena.getString("error");
        	resp = Integer.parseInt(error);
        } catch (JSONException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return resp;
	
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
	
	 private class DownloadDataTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... params) {

	    	 sendJSON();
			return true;
	     }
	     @Override
	     protected void onPostExecute(final Boolean success) {
	    	 try {
		 		if (success){
		 			
		
	    	 	JSONObject datos = new JSONObject(Respuesta);
	    	 	Intent intent = null;
				int error = respuestaJSON(datos);
				switch (error) {
					case 0:
						//Eliminar
						getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI1, null, null);
						
						//Insertar
						JSONArray amigos = datos.getJSONArray("listaAmigos");

			    		for(int i = 0; i < amigos.length(); i++){
			    			JSONObject a = amigos.getJSONObject(i);
			    			a.toString();

			    			String nombre = a.getString(AmigosTable.COLUMN_NOMBRE);
			    			String apellidos = a.getString(AmigosTable.COLUMN_APELLIDOS);
			    			String telefono = a.getString(AmigosTable.COLUMN_TELEFONO);
			    			String email = a.getString(AmigosTable.COLUMN_EMAIL);
			    			//String email = "no";
			    			String sexo = a.getString(AmigosTable.COLUMN_SEXO);
			    			String lat = a.getString(AmigosTable.COLUMN_LAT);
			    			String longitud = a.getString(AmigosTable.COLUMN_LONG);
			    			String mostrar = a.getString("actualizado");;

			    			addAmigo(nombre,apellidos,telefono,email,sexo,lat,longitud,mostrar);
			    		}
			    		marcarAmigos();
						break;
					case 1:
						Toast ToastFallo = Toast.makeText(getApplicationContext(),
								"Fallo del servidor", Toast.LENGTH_LONG);
						ToastFallo.show();
						intent = new Intent(MapListActivity.this, LogActivity.class);
				    	startActivity(intent);
				    	finish();
				    	break;
					case 4:
						Toast ToastToken = Toast.makeText(getApplicationContext(),
								"Error de sesión", Toast.LENGTH_LONG);
						ToastToken.show();
					default:
						
						intent = new Intent(MapListActivity.this, LogActivity.class);
				    	startActivity(intent);
				    	finish();
						break;
				
				
				
				}
				
				
		 		}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	     }
	 }
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.desconectar:
			getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI1, null, null);
			getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI2, null, null);
			

		    Toast toastDesconectar = Toast.makeText(getApplicationContext(),
						"Desconectando..." , Toast.LENGTH_SHORT);
			toastDesconectar.show();

			Intent intent = new Intent(MapListActivity.this, LogActivity.class);
			startActivity(intent);

			
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    
}	