package com.julia.topic9_database_i;

import android.annotation.SuppressLint;
import android.content.Context;


    import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private List<String> cartList = new ArrayList<String>();;

        // creating a constant variables for our database. below variable is for our database name.
        private static final String DB_NAME = "coursedb";

        // below int is our database version
        private static final int DB_VERSION = 1;

        // below variable is for our table name.
        private static final String TABLE_NAME = "tabla";
        private static final String ID_COL = "id";
        private static final String TITULO_COL = "titulo";
        private static final String COMENTARIO_COL = "comentario";
        private Context context;

        // creating a constructor for our database handler.
        public DBHandler(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        // below method is for creating a database by running a sqlite query
        @Override
        public void onCreate(SQLiteDatabase db) {
            // on below line we are creating an sqlite query and we are setting our column names along with their data types.
            String query = "CREATE TABLE " + TABLE_NAME + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TITULO_COL + " TEXT,"
                    + COMENTARIO_COL + " TEXT)";

            db.execSQL(query);
        }

        // this method is use to add new course to our sqlite database.
        public void addNewCourse(String courseName, String courseDuration) {
            // on below line we are creating a variable for
            // our sqlite database and calling writable method as we are writing data in our database.
            SQLiteDatabase db = this.getWritableDatabase();

            // on below line we are creating a variable for content values.
            ContentValues values = new ContentValues();

            // on below line we are passing all values
            // along with its key and value pair.
            values.put(TITULO_COL, courseName);
            values.put(COMENTARIO_COL, courseDuration);

            // after adding all values we are passing
            // content values to our table.
            db.insert(TABLE_NAME, null, values);

            // at last we are closing our database after adding database.
           db.close();
        }

    public List<String> getProducts() {
        cartList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    int index = cursor.getColumnIndex(TITULO_COL);
                    //Toast.makeText(context, "eL INDEX ES "+index, Toast.LENGTH_SHORT).show();
                  cartList.add(cursor.getString(index));
                } while (cursor.moveToNext());
            }
        }
       cursor.close();
        db.close();
        return cartList;
    }

    public String buscarComentario(String comentarioTitulo) {
        String texto = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { comentarioTitulo };
        Cursor cursor = db.rawQuery("select * from 'tabla' where titulo=?", args, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    int index = cursor.getColumnIndex(COMENTARIO_COL);
                    Toast.makeText(context, "eL INDEX ES "+index, Toast.LENGTH_SHORT).show();
                     texto = (cursor.getString(index));
                    Toast.makeText(context, "eL texto ES "+texto, Toast.LENGTH_SHORT).show();
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return texto;
    }

    public void borrarComentario(String comentarioTitulo) {
            try{
                SQLiteDatabase db = this.getWritableDatabase();
                String[] args = { comentarioTitulo };
                db.execSQL("delete  from 'tabla' where titulo=?", args);
                db.close();
            } catch (Exception e){
                e.printStackTrace();
            }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // this method is called to check if the table exists already.
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
}
