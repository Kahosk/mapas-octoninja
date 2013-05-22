package map.ambimetrics.ambiguay_android;

import map.ambimetrics.comunicacion.RequestMethod;
import map.ambimetrics.comunicacion.RestClient;
import map.ambimetrics.database.UsuarioTable;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Activity which displays a Registrar screen to the user, offering registration as
 * well.
 */
public class RegActivity extends Activity {
	private static final String URL = "http://10.0.0.202/Agenda/";
	private String Respuesta = null;
	
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the Registrar task to ensure we can cancel it if requested.
	 */
	private UserRegistrarTask mAuthTask = null;

	// Values for email and password at the time of the Registrar attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	
	private EditText mNombreView;
	private EditText mApellidosView;
	private EditText mTelefonoView;
	
	private EditText mEmailView;
	private EditText mPasswordView;
	
	private View mRegistrarFormView;
	private View mRegistrarStatusView;
	private TextView mRegistrarStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reg);

		// Set up the Registrar form.
		mNombreView = (EditText) findViewById(R.id.nombre);
		mApellidosView = (EditText) findViewById(R.id.apellidos);
		mTelefonoView = (EditText) findViewById(R.id.telefono);
		
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		//mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.registrar || id == EditorInfo.IME_NULL) {
							attemptRegistrar();
							return true;
						}
						return false;
					}
				});

		mRegistrarFormView = findViewById(R.id.registrar_form);
		mRegistrarStatusView = findViewById(R.id.registrar_status);
		mRegistrarStatusMessageView = (TextView) findViewById(R.id.registrar_status_message);
		


		
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptRegistrar();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the Registrar form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual Registrar attempt is made.
	 */
	public void attemptRegistrar() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the Registrar attempt.
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
			// There was an error; don't attempt Registrar and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user Registrar attempt.
			mRegistrarStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserRegistrarTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the Registrar form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegistrarStatusView.setVisibility(View.VISIBLE);
			mRegistrarStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegistrarStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegistrarFormView.setVisibility(View.VISIBLE);
			mRegistrarFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegistrarFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegistrarStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegistrarFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous Registrar/registration task used to authenticate
	 * the user.
	 */
	public class UserRegistrarTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	private void sendJSON(){
	    String nombre = mNombreView.getText().toString();
	    String apellidos = mApellidosView.getText().toString();
	    String telefono = mTelefonoView.getText().toString();
	    String email = mEmailView.getText().toString();
	    String password = mPasswordView.getText().toString();
	    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
    	
		try { 
    	  		cadena.put("tag", "registro");
    		    cadena.put(UsuarioTable.COLUMN_NOMBRE, nombre);
    		    cadena.put(UsuarioTable.COLUMN_APELLIDOS, apellidos);
    		    cadena.put(UsuarioTable.COLUMN_TELEFONO, telefono);
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
		
		
		
		
		/*
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                		obj, Toast.LENGTH_LONG);
     
            toast1.show();
		*/
	
	}
}
