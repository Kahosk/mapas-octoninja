package map.ambimetrics.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsuarioTable {

  // Database table
  public static final String TABLE_USUARIO = "usuario";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NOMBRE = "nombre";
  public static final String COLUMN_APELLIDOS = "apellidos";
  public static final String COLUMN_TELEFONO = "telefono";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_PASSWORD = "password";
  public static final String COLUMN_SEXO = "sexo";
  
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_USUARIO
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_NOMBRE + " text not null, " 
      + COLUMN_APELLIDOS + " text not null," 
      + COLUMN_TELEFONO + " text," 
      + COLUMN_EMAIL + " text not null,"
      + COLUMN_PASSWORD + " text not null,"
      + COLUMN_SEXO + " integer not null,"
      + ");";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(UsuarioTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
    onCreate(database);
  }
} 