package ar.com.stoller.stollermobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pablo on 10/31/14.
 */
public class DBConfiguracion extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Configuracion";

    public DBConfiguracion(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        addConfiguration();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_db = "CREATE TABLE CONFIG (host varchar(30), user varchar(30)," +
                " pass varchar(30))";
        db.execSQL(create_db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    private void addConfiguration(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("host", "10.0.0.107");
        values.put("user", "sa");
        values.put("pass", "asdf1234");
        db.insert("CONFIG", null, values);
    }

    public void updateConfiguracion(String host, String user, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE CONFIG SET host = '" + host + "', user = '" + user + "', pass = " +
                "'" + pass + "'";
        db.execSQL(update);
    }

    public String[] getConfiguracion(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM CONFIG";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String[] s = new String[3];
        s[0] = cursor.getString(0);
        s[1] = cursor.getString(1);
        s[2] = cursor.getString(2);
        return s;
    }
}
