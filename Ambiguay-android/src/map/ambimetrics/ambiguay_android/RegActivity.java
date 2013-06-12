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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class RegActivity extends Activity {
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
	private UserRegisterTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	
	private String mEmail;
	private String mPassword;
	private String mNombre;
	private String mApellidos;
	private String mTelefono;
	private int mSexo;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mNombreView;
	private EditText mApellidosView;
	private EditText mTelefonoView;
	private RadioGroup mSexoView;
	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;
	
	private Uri usuarioUri = null;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setupActionBar();
		setContentView(R.layout.activity_reg);
		usuarioPass();
		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email_r);

		mPasswordView = (EditText) findViewById(R.id.password_r);
		mNombreView = (EditText) findViewById(R.id.nombre_r);
		mApellidosView = (EditText) findViewById(R.id.apellidos_r);
		mTelefonoView = (EditText) findViewById(R.id.telefono_r);
		mSexoView = (RadioGroup) findViewById(R.id.sexo_r);
		
		
		mEmailView.setText(EmailU);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});

		mRegisterFormView = findViewById(R.id.register_form);
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);
		
	
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptRegister();
					}
				});
	}
	


	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mNombreView.setError(null);
		mApellidosView.setError(null);
		
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mNombre = mNombreView.getText().toString();
		mApellidos = mApellidosView.getText().toString();
		mTelefono = mTelefonoView.getText().toString();
		mSexo = mSexoView.getCheckedRadioButtonId();
		
		boolean cancel = false;
		View focusView = null;
		
		if (mSexo == R.id.hombre_r){
			mSexo = 1;
		}else if(mSexo == R.id.mujer_r){
			mSexo = 2;
		}else{
			mSexo = 0;
		}
		
		
		
		
		if (TextUtils.isEmpty(mTelefono)) {
			mTelefono = "-1";
		}
		// Check for a valid lastname.
		if (TextUtils.isEmpty(mApellidos)) {
			mApellidosView.setError(getString(R.string.error_field_required));
			focusView = mApellidosView;
			cancel = true;
		}
		// Check for a valid name.
		if (TextUtils.isEmpty(mNombre)) {
			mNombreView.setError(getString(R.string.error_field_required));
			focusView = mNombreView;
			cancel = true;
		}
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 3) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
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
			mRegisterStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserRegisterTask();
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

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
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
			    	
			    	String token = datos.getString(UsuarioTable.COLUMN_TOKEN);
			    	Toast toast1 = Toast.makeText(getApplicationContext(),
	    					"Bienvenido "+mNombre, Toast.LENGTH_LONG);
	     
	    			toast1.show();			    	

			    	SaveUsuario(token);
			    	
			    	intent = new Intent(RegActivity.this, MapListActivity.class);
			    	startActivity(intent);
			    	finish();
					break;
			    case 1: //incorrecto
			    	mEmailView
					.setError("Error del servidor");
			    	mEmailView.requestFocus();
					break;
			    case 2: //usuario que ya existe
					mEmailView
					.setError(getString(R.string.error_user_registered));
					mEmailView.requestFocus();
					break;
				default:
					mEmailView
					.setError("Error desconocido.");
					mEmailView.requestFocus();
				}

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

	  	    /*
		mEmail
		mPassword
		mNombre
		mApellidos
		mTelefono
		mSexo 
	    */
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
    	  		cadena.put("tag", "registro");
    		    cadena.put(UsuarioTable.COLUMN_EMAIL, mEmail);
    		    cadena.put(UsuarioTable.COLUMN_PASSWORD, mPassword);//Le asignamos los datos que necesitemos
    		    cadena.put(UsuarioTable.COLUMN_NOMBRE, mNombre);
    		    cadena.put(UsuarioTable.COLUMN_APELLIDOS, mApellidos);
    		    cadena.put(UsuarioTable.COLUMN_TELEFONO, mTelefono);
    		    cadena.put(UsuarioTable.COLUMN_SEXO, mSexo);
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
	private void SaveUsuario(String token){
    	
			/*
			datos.toString(); //Para obtener la cadena de texto de tipo JSON
	    	String token = datos.getString("token");
	    	String nombre = datos.getString("nombre");
	    	String apellidos = datos.getString("apellidos");
	    	String telefono = datos.getString("telefono");
	    	*/
    		ContentValues values = new ContentValues();
    	    values.put(UsuarioTable.COLUMN_NOMBRE, mNombre);
    	    values.put(UsuarioTable.COLUMN_APELLIDOS, mApellidos);
    	    values.put(UsuarioTable.COLUMN_TELEFONO, mTelefono);
    	    values.put(UsuarioTable.COLUMN_TOKEN, token);
    	    values.put(UsuarioTable.COLUMN_EMAIL, mEmail);
    	    values.put(UsuarioTable.COLUMN_PASSWORD, mPassword);
    	    
    	    Uri usuario = getContentResolver().insert(MyAmigosContentProvider.CONTENT_URI2, values);

	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
			Intent intent = new Intent(RegActivity.this, LogActivity.class);
	    	startActivity(intent);
	    	finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		Bundle extras = getIntent().getExtras();
		EmailU = extras.getString("email");
	}
}
