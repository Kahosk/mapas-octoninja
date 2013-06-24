package map.ambimetrics.ambiguay_android;



import map.ambimetrics.comunicacion.RequestMethod;
import map.ambimetrics.comunicacion.RestClient;
import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.AmigosTable;
import map.ambimetrics.database.UsuarioTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListBusquedaActivity extends ListActivity implements
LoaderManager.LoaderCallbacks<Cursor>
{
	
	private static final String URL = RequestMethod.URL;
	private String Respuesta = null;
	private JSONArray Gente = null;
	private String EmailU = null;
	private String TokenU = null;
	private String emailAmigo = null;
	private String nombreAmigo = null;
	private String apellidosAmigo = null;
	private String[] emailsAdd = null;
	private String emailAdd = null;
	private int posicion = 0;
	
	
    String android_versions[] = new String[]{
            "Jelly Bean",
            "IceCream Sandwich",
            "HoneyComb",
            "Ginger Bread",
            "Froyo"
    };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_busqueda);
		this.getListView();
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
		    return;
		    }
		// Get data via the key
		emailAmigo = extras.getString("email");
		nombreAmigo = extras.getString("nombre");
		apellidosAmigo = extras.getString("apellidos");
		
		usuarioToken();

	    registerForContextMenu(getListView());
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_busqueda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    //Intent i = new Intent(this, VerContactoActivity.class);
	    //Uri contactoUri = Uri.parse(MyAmigosContentProvider.CONTENT_URI1 + "/" + id);
	    //i.putExtra(MyAmigosContentProvider.CONTENT_ITEM_TYPE, contactoUri);

	    //startActivity(i);
	    
	    //((MapListActivity) this.getActivity()).mostrarMarcador(40.5,-3.5,getListView().getItemAtPosition(position).toString());
	    
    	// 1. Instantiate an AlertDialog.Builder with its constructor
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	// 2. Chain together various setter methods to set the dialog characteristics
    	builder.setMessage(R.string.dialog_add);

    	builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            	/*
	 			Toast toast2 = Toast.makeText(getApplicationContext(),
	 					emailsAdd[posicion], Toast.LENGTH_LONG);
	 			toast2.show();
            	*/	
	 			new AddFriendTask().execute();
            }
        });
    	builder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            	dialog.cancel();
            }
        });   	
    	
    	
    	// 3. Get the AlertDialog from create()
    	posicion = position;
    	AlertDialog dialog = builder.create();
    	dialog.show();


	}	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void fillData(String[] amigos) {

		/** Creating array adapter to set data in listview */

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, amigos);

        /** Setting the array adapter to the listview */
        setListAdapter(adapter);
	}	

	public void nuevoAmigo(JSONObject a){
	//JSONObject a = amigos.getJSONObject(i);
	a.toString();
	/*
	String nombre = a.getString(AmigosTable.COLUMN_NOMBRE);
	String apellidos = a.getString(AmigosTable.COLUMN_APELLIDOS);
	String telefono = a.getString(AmigosTable.COLUMN_TELEFONO);
	//String email = a.getString(AmigosTable.COLUMN_EMAIL);
	String email = "no";
	String sexo = a.getString(AmigosTable.COLUMN_SEXO);
	String lat = a.getString(AmigosTable.COLUMN_LAT);
	String longitud = a.getString(AmigosTable.COLUMN_LONG);
	String mostrar = "1";

	*/
	//addAmigo(nombre,apellidos,telefono,email,sexo,lat,longitud,mostrar);
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
	
	//Comunicacion
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
		if ((TokenU!=null && EmailU!=null)){
			/*
			Toast toast1 = Toast.makeText(getApplicationContext(),
					"En proceso..." , Toast.LENGTH_SHORT);

			toast1.show();
			*/
			new SearchDataTask().execute();
			
		}
		
	}
	private void sendJSONbuscar(){
	    
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
	  		cadena.put("tag", "buscar");
	  		cadena.put("email", EmailU);
	  		cadena.put("token", TokenU);
	  		cadena.put("emailAmigo", emailAmigo);
	  		cadena.put("nombreAmigo", nombreAmigo);
	  		cadena.put("apellidosAmigo", apellidosAmigo);
		
		   
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
	
	private void sendJSONadd(){
	    
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
	  		cadena.put("tag", "add");
	  		cadena.put("email", EmailU);
	  		cadena.put("token", TokenU);
	  		cadena.put("emailAmigo", emailsAdd[posicion]);
	
		   
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
	 private class SearchDataTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... params) {

	    	 sendJSONbuscar();
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

						//Insertar
						JSONArray amigos = datos.getJSONArray("listaUsuarios");
						int t = amigos.length();
						String[] listaUsuarios = new String[t];
						emailsAdd = new String[t];
			    		for(int i = 0; i < amigos.length(); i++){
			    			JSONObject a = amigos.getJSONObject(i);
			    			a.toString();
			    			
			    			String nombre = a.getString("nombreAmigo");
			    			String apellidos = a.getString("apellidosAmigo");
				    	    String email = a.getString("emailAmigo");
			    			listaUsuarios[i] = nombre+" "+apellidos;
			    			emailsAdd[i] = email;
			    			
			    		}

			    		fillData(listaUsuarios);
			    	    registerForContextMenu(getListView());
			    		// Show the Up button in the action bar.
			    		setupActionBar();
			    		
						break;
					case 1:
						Toast ToastFallo = Toast.makeText(getApplicationContext(),
								"Fallo del servidor", Toast.LENGTH_LONG);
						ToastFallo.show();
						intent = new Intent(ListBusquedaActivity.this, LogActivity.class);
				    	startActivity(intent);
				    	finish();
				    	break;
					case 2:
						//Devolver error
						intent = new Intent(ListBusquedaActivity.this, BuscarActivity.class);
						intent.putExtra("error", "No se han encontrado amigos");
						startActivity(intent);
				    	finish();
				    	break;
					default:
						
						intent = new Intent(ListBusquedaActivity.this, LogActivity.class);
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
	 private class AddFriendTask extends AsyncTask<Void, Void, Boolean> {
	     protected Boolean doInBackground(Void... params) {

	    	 sendJSONadd();
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
						Toast ToastAdd = Toast.makeText(getApplicationContext(),
								"Agregado", Toast.LENGTH_LONG);
						ToastAdd.show();
						intent = new Intent(ListBusquedaActivity.this, BuscarActivity.class);
				    	startActivity(intent);
				    	finish();
						break;
					default:
						Toast ToastFallo = Toast.makeText(getApplicationContext(),
								"Fallo", Toast.LENGTH_LONG);
						ToastFallo.show();
						intent = new Intent(ListBusquedaActivity.this, LogActivity.class);
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
	 	 
	 
    
}
