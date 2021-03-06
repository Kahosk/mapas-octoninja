package map.ambimetrics.ambiguay_android;

import map.ambimetrics.comunicacion.RequestMethod;
import map.ambimetrics.comunicacion.RestClient;
import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.AmigosTable;
import map.ambimetrics.database.UsuarioTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LogActivity extends Activity {
	private static final String URL = RequestMethod.URL;

	private String Respuesta = null;
	private String PassU = null;
	private String EmailU = null;
	
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */

	/**
	 * The default email to populate the email field with.
	 */
	//public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private Uri usuarioUri = null;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (RequestMethod.hasInternet(this)){
		setContentView(R.layout.activity_log);

		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		usuarioPass();
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mEmailView.setText(EmailU);
		mPasswordView.setText(PassU);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
	    Bundle extras = getIntent().getExtras();

	    // Check from the saved Instance
	    usuarioUri = (bundle == null) ? null : (Uri) bundle
	        .getParcelable(MyAmigosContentProvider.CONTENT_ITEM_TYPE2);

	    // Or passed from the other activity
	    if (extras != null) {
	    	usuarioUri = extras
	          .getParcelable(MyAmigosContentProvider.CONTENT_ITEM_TYPE2);
	    }
	    if(usuarioUri != null)
	    	fillData(usuarioUri);

		
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		}
		
	}
	
	  private void fillData(Uri uri) {
		  
		    String[] projection = { UsuarioTable.COLUMN_NOMBRE, UsuarioTable.COLUMN_APELLIDOS,
		        UsuarioTable.COLUMN_TELEFONO,UsuarioTable.COLUMN_EMAIL, 
		        UsuarioTable.COLUMN_PASSWORD};
		    Cursor cursor = getContentResolver().query(uri, projection, null, null,
		        null);
		    if (cursor != null) {
		      if(cursor.moveToFirst()){
			      mEmailView.setText(cursor.getString(cursor
			              .getColumnIndexOrThrow(UsuarioTable.COLUMN_EMAIL)));
			      mPasswordView.setText(cursor.getString(cursor
			              .getColumnIndexOrThrow(UsuarioTable.COLUMN_PASSWORD)));
			      
			      // Always close the cursor
			      cursor.close();
		      }
		    }
		  }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.


		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			registrar(0);
			focusView = mEmailView;
			cancel = true;

		} else if (TextUtils.isEmpty(mPassword)) {
			registrar(0);
			focusView = mEmailView;
			cancel = true;

		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			sendJSON();
			
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			try {
			if (success) {
				JSONObject datos = new JSONObject(Respuesta);

				int error = respuestaJSON(datos);
				Intent intent = null;
				switch (error) {
			    case 0: //correcto
			    	
			    	getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI1, null, null);
					getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI2, null, null);

			    	
			    	String token = datos.getString(UsuarioTable.COLUMN_TOKEN);
			    	JSONObject usuario = datos.getJSONObject(UsuarioTable.TABLE_USUARIO);
			    	String nombreU = usuario.getString(UsuarioTable.COLUMN_NOMBRE);
			    	String apellidosU = usuario.getString(UsuarioTable.COLUMN_APELLIDOS);
			    	String telefonoU = usuario.getString(UsuarioTable.COLUMN_TELEFONO);
	    			Toast toast1 = Toast.makeText(getApplicationContext(),
	    					"Bienvenido "+nombreU, Toast.LENGTH_LONG);
	     
	    			toast1.show();			    	

			    	SaveUsuario(nombreU, apellidosU, telefonoU, token);
			    	
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
		    			String mostrar = a.getString("actualizado");

		    			addAmigo(nombre,apellidos,telefono,email,sexo,lat,longitud,mostrar);
		    		}
		    		

			    	intent = new Intent(LogActivity.this, MapListActivity.class);
			    	startActivity(intent);
			    	finish();
					break;
			    case 1: //incorrecto
			    	mPasswordView
					.setError("No responde");
			    	mPasswordView.requestFocus();
					break;
			    case 2: //password incorrecto
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
					break;
			    case 3: //usuario no registrado
					//intent 
			    	registrar(1);
			    	

					break;
				default:
					mPasswordView
					.setError("default");
			mPasswordView.requestFocus();
				}

			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	private void sendJSON(){

	    String email = mEmailView.getText().toString();
	    String password = mPasswordView.getText().toString();
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
    	  		cadena.put("tag", "login");
    		    cadena.put(UsuarioTable.COLUMN_EMAIL, email);
    		    cadena.put(UsuarioTable.COLUMN_PASSWORD, password);//Le asignamos los datos que necesitemos
		}catch (JSONException e) {
        	e.printStackTrace();
        }		
		
		RestClient client = new RestClient(URL);
		client.AddParam("JSON", cadena.toString());
		

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
	private void SaveUsuario(String nombre, String apellidos, String telefono, String token){
    	
			/*
			datos.toString(); //Para obtener la cadena de texto de tipo JSON
	    	String token = datos.getString("token");
	    	String nombre = datos.getString("nombre");
	    	String apellidos = datos.getString("apellidos");
	    	String telefono = datos.getString("telefono");
	    	*/
    		ContentValues values = new ContentValues();
    	    values.put(UsuarioTable.COLUMN_NOMBRE, nombre);
    	    values.put(UsuarioTable.COLUMN_APELLIDOS, apellidos);
    	    values.put(UsuarioTable.COLUMN_TELEFONO, telefono);
    	    values.put(UsuarioTable.COLUMN_TOKEN, token);
    	    values.put(UsuarioTable.COLUMN_EMAIL, mEmail);
    	    values.put(UsuarioTable.COLUMN_PASSWORD, mPassword);
    	    
    	    Uri usuario = getContentResolver().insert(MyAmigosContentProvider.CONTENT_URI2, values);

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
		    	values.put(AmigosTable.COLUMN_LAT, "0");
			    values.put(AmigosTable.COLUMN_LONG, "0");
		    }else{
		    values.put(AmigosTable.COLUMN_LAT, lat);
		    values.put(AmigosTable.COLUMN_LONG, longitud);
		    }
		    values.put(AmigosTable.COLUMN_MOSTRAR, mostrar);
			 
		 
		    Uri amigo = getContentResolver().insert(MyAmigosContentProvider.CONTENT_URI1, values);
		    
		
	}
	public void usuarioPass(){
		//Actualizar
		//1 Buscar token
		Cursor cursor = null;
		String[] projection = { UsuarioTable.COLUMN_ID, UsuarioTable.COLUMN_EMAIL, UsuarioTable.COLUMN_PASSWORD};
		cursor = getContentResolver().query(MyAmigosContentProvider.CONTENT_URI2, projection, null, null,
		        null);
		if (cursor!=null) {
			if (cursor.moveToFirst()) {
		    	PassU = cursor.getString(cursor
		                .getColumnIndexOrThrow(UsuarioTable.COLUMN_PASSWORD));
		    	EmailU = cursor.getString(cursor
	                .getColumnIndexOrThrow(UsuarioTable.COLUMN_EMAIL));
			}
		}
	}
	
	
	public void registrar(int registrado){
		getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI1, null, null);
		getContentResolver().delete(MyAmigosContentProvider.CONTENT_URI2, null, null);
		
		if (registrado==1){
    	Toast toastNoUsuario = Toast.makeText(getApplicationContext(),
				"Usuario no registrado" , Toast.LENGTH_SHORT);
				toastNoUsuario.show();
		}
		Intent intent = new Intent(LogActivity.this, RegActivity.class);
		
		intent.putExtra("email", mEmail);
    	
    	startActivity(intent);
    	finish();
	}
	
}
