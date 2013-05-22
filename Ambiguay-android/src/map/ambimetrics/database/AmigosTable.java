package map.ambimetrics.database;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AmigosTable {


	  // Database table
	  public static final String TABLE_AMIGOS = "amigos";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NOMBRE = "nombre";
	  public static final String COLUMN_APELLIDOS = "apellidos";
	  public static final String COLUMN_TELEFONO = "telefono";
	  public static final String COLUMN_EMAIL = "email";
	  public static final String COLUMN_SEXO = "sexo";
	  public static final String COLUMN_LAT = "latitud";
	  public static final String COLUMN_LONG = "longitud";
	  public static final String COLUMN_MOSTRAR = "mostrar";

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "create table " 
	      + TABLE_AMIGOS
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_NOMBRE + " text not null, " 
	      + COLUMN_APELLIDOS + " text not null," 
	      + COLUMN_TELEFONO + " text," 
	      + COLUMN_EMAIL + " text not null,"
	      + COLUMN_SEXO + " integer not null,"
	      + COLUMN_LAT + " double,"
	      + COLUMN_LONG + " double,"
	      + COLUMN_MOSTRAR + " integer not null" 
	      + ");";

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(AmigosTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_AMIGOS);
	    onCreate(database);
	  }
	
}
