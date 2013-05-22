package map.ambimetrics.ambiguay_android;

import map.ambimetrics.comunicacion.RequestMethod;
import map.ambimetrics.comunicacion.RestClient;
import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.UsuarioTable;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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
	private static final String URL = "http://192.168.0.153/Ambiway/";

	private String Respuesta = null;
	
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	//private static final String[] DUMMY_CREDENTIALS = new String[] {
	//		"foo@example.com:hello", "bar@example.com:world" };

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

		setContentView(R.layout.activity_log);

		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		//mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
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
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
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
				int error = respuestaJSON(new JSONObject(Respuesta));
				switch (error) {
			    case 0: //correcto
			    	Intent intent = new Intent(LogActivity.this, MapListActivity.class);
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
					.setError("Password incorrecto");
					mPasswordView.requestFocus();
					break;
			    case 3: //usuario no registrado
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
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
}
