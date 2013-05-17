package map.ambimetrics.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import map.ambimetrics.database.AmigosDatabaseHelper;
import map.ambimetrics.database.AmigosTable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class MyAmigosContentProvider extends ContentProvider {

  // database
  private AmigosDatabaseHelper database;

  // Used for the UriMacher
  private static final int AMIGOS = 10;
  private static final int AMIGO_ID = 20;
  //private static final int USUARIO = 30;
  //private static final int USUARIO_ID = 40;
  
  private static final String AUTHORITY = "map.ambimetrics.contentprovider";

  private static final String BASE_PATH1 = "amigo";
  //private static final String BASE_PATH2 = "usuario";
  public static final Uri CONTENT_URI1 = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH1);
  //public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH2);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/amigos";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/amigo";
  //public static final String CONTENT_ITEM_TYPE2 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/usuario";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH1, AMIGOS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH1 + "/#", AMIGO_ID);
    //sURIMatcher.addURI(AUTHORITY, BASE_PATH2, USUARIO);
    //sURIMatcher.addURI(AUTHORITY, BASE_PATH2 + "/#", USUARIO_ID);
  }

  @Override
  public boolean onCreate() {
    database = new AmigosDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    //queryBuilder.setTables(AmigosTable.TABLE_AMIGO);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case AMIGO_ID:
        // Adding the ID to the original query
        queryBuilder.appendWhere(AmigosTable.COLUMN_ID + "="
            + uri.getLastPathSegment());
    case AMIGOS:
    	queryBuilder.setTables(AmigosTable.TABLE_AMIGOS);
    	break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    //int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    //No tiene sentido...
    case AMIGOS:
      id = sqlDB.insert(AmigosTable.TABLE_AMIGOS, null, values);
      getContext().getContentResolver().notifyChange(uri, null);
      return Uri.parse(BASE_PATH1 + "/" + id);
	default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case AMIGOS:
      rowsDeleted = sqlDB.delete(AmigosTable.TABLE_AMIGOS, selection,
          selectionArgs);
      break;
    case AMIGO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(AmigosTable.TABLE_AMIGOS,
            AmigosTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(AmigosTable.TABLE_AMIGOS,
            AmigosTable.COLUMN_ID + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;

    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case AMIGOS:
      rowsUpdated = sqlDB.update(AmigosTable.TABLE_AMIGOS, 
          values, 
          selection,
          selectionArgs);
      break;
    case AMIGO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(AmigosTable.TABLE_AMIGOS, 
            values,
            AmigosTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(AmigosTable.TABLE_AMIGOS, 
            values,
            AmigosTable.COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
      
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = { AmigosTable.COLUMN_NOMBRE,
        AmigosTable.COLUMN_APELLIDOS, AmigosTable.COLUMN_TELEFONO,AmigosTable.COLUMN_EMAIL,
        AmigosTable.COLUMN_SEXO,AmigosTable.COLUMN_LAT, AmigosTable.COLUMN_LONG , AmigosTable.COLUMN_ID };
    
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }

} 