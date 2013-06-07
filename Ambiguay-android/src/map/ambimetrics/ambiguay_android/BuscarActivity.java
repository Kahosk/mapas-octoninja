package map.ambimetrics.ambiguay_android;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class BuscarActivity extends Activity {
	private String bEmail;
	private String bNombre;
	private String bApellidos;
	
	private EditText bEmailView;
	private EditText bNombreView;
	private EditText bApellidosView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscar);
		setupActionBar();
		bEmailView = (EditText) findViewById(R.id.bemail);
		bNombreView = (EditText) findViewById(R.id.bnombre);
		bApellidosView = (EditText) findViewById(R.id.bapellidos);
		
		findViewById(R.id.buscar_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						buscarAmigo();
					}
				});
	}
	private void buscarAmigo() {
		bEmail = bEmailView.getText().toString();
		bNombre = bNombreView.getText().toString();
		bApellidos = bApellidosView.getText().toString();
		View focusView = null;
		bEmailView.setError(null);
		
		int vacio = 0;
		if (TextUtils.isEmpty(bEmail)) {
			bEmail = "-1";
			vacio= vacio +1;
		}
		if (TextUtils.isEmpty(bNombre)) {
			bNombre = "-1";
			vacio = vacio+1;
		}
		if (TextUtils.isEmpty(bApellidos)) {
			bApellidos = "-1";
			vacio = vacio +1;
		}

		
		if (vacio<3){
			String buscar[]= new String[]{bEmail, bNombre, bApellidos};
			//Intent i = new Intent(this, ListBusquedaActivity.class);
			//i.putExtra("datos", buscar);
			//startActivity(i);
			
			
		}else{
			bEmailView.setError(getString(R.string.error_required));
			focusView = bEmailView;
			focusView.requestFocus();
		}
		
		
	}
	

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buscar, menu);
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
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
