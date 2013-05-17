package map.ambimetrics.ambiguay_android;




import map.ambimetrics.contentprovider.MyAmigosContentProvider;
import map.ambimetrics.database.AmigosTable;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;


//http://developer.android.com/intl/es/reference/android/app/LoaderManager.html
public class AmigosFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	 private SimpleCursorAdapter adapter;
	 SearchView mSearchView;

	 
	 private Uri direccion;
	
    /** An array of items to display in ArrayList */
	 String apple_versions[] = new String[]{
             "Mountain Lion",
             "Lion",
             "Snow Leopard",
             "Leopard",
             "Tiger",
             "Panther",
             "Jaguar",
             "Puma"
     };


    	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		fillData();
		//Comunication
		//http://android-er.blogspot.com.es/2012/06/communication-between-fragments-in.html
		
		
		this.setHasOptionsMenu(true);
		
		return super.onCreateView(inflater, container, savedInstanceState);
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
    	AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

    	// 2. Chain together various setter methods to set the dialog characteristics
    	builder.setMessage("Hola k ase?");

    	builder.setPositiveButton("Si!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            	
            	dialog.cancel();
            }
        });
    	builder.setNegativeButton("HNO!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            	dialog.cancel();
            }
        });   	
    	
    	
    	// 3. Get the AlertDialog from create()
    	AlertDialog dialog = builder.create();
    	dialog.show();
	    
	}
	
	@Override
    public void onStart() {
            super.onStart();

            /** Setting the multiselect choice mode for the listview */
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

	

	private void fillData() {
		    // Fields from the database (projection)
		    // Must include the _id column for the adapter to work
		 /*
		    String[] from = new String[] {AmigosTable.COLUMN_NOMBRE, AmigosTable.COLUMN_APELLIDOS};

		    // Fields on the UI to which we map
		    int[] to = new int[] { R.id.label, R.id.label2};

		    getLoaderManager().initLoader(0, null, (LoaderCallbacks<Cursor>) this);
		    adapter = new SimpleCursorAdapter(this.getActivity(), R.layout.row_contacto, null, from,
		        to, 0);
		    
		    
			MapListActivity activity =  ((MapListActivity) this.getActivity());
			String[] projection = { AmigosTable.COLUMN_APELLIDOS, AmigosTable.COLUMN_NOMBRE };
		    Cursor cursor = activity.getContentResolver().query(MyAmigosContentProvider.CONTENT_URI1, projection, null, null,
		        null);
		  	*/
        	adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] { AmigosTable.COLUMN_NOMBRE, AmigosTable.COLUMN_APELLIDOS },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);

			
			/** Creating array adapter to set data in listview */
		    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, apple_versions);

		    /** Setting the array adapter to the listview */
		    setListAdapter(adapter);

		    getActivity().getLoaderManager().initLoader(0, null, this);

		  }
	

    public static class MySearchView extends SearchView {
        public MySearchView(Context context) {
            super(context);
        }

        // The normal SearchView doesn't clear its search text when
        // collapsed, so we will do this for it.
        @Override
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }

	


	// Create the menu based on the XML defintion
	  @Override
	  public void onCreateOptionsMenu(Menu menu,  MenuInflater inflater) {
	    inflater.inflate(R.menu.listmenu, menu);
	    
	    
	    
	  }

	  // Reaction to the menu selection
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.insert:
	      addAmigo();
	      return true;
	    }
	    return super.onOptionsItemSelected(item);
	  }
	  
	  public void addAmigo(){
		  
		  
	  }
	  // Creates a new loader after the initLoader () call
	  @Override
	  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    String[] projection = { AmigosTable.COLUMN_ID, AmigosTable.COLUMN_NOMBRE, AmigosTable.COLUMN_APELLIDOS };
	    CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
	            MyAmigosContentProvider.CONTENT_URI1, projection, null, null, AmigosTable.COLUMN_NOMBRE+", "+AmigosTable.COLUMN_APELLIDOS);
	    return cursorLoader;
	    
	  }
	  @Override
	  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	    adapter.swapCursor(data);
        // The list should now be shown.
	  }

	  @Override
	  public void onLoaderReset(Loader<Cursor> loader) {
	    // data is not available anymore, delete reference
	    adapter.swapCursor(null);
	  }
	  


	
}
