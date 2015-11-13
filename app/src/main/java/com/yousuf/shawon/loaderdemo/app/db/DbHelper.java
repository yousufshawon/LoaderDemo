package com.yousuf.shawon.loaderdemo.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.yousuf.shawon.loaderdemo.app.utility.Utility;

/**
 * Created by user on 11/9/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    // private static variable to store singleton object of the class
    private static DbHelper dbHelper = null;

    // private static  SQLiteDatabase variable for all database related operation
    private static  SQLiteDatabase db = null;

    // All static variable
    //Database Version
    public static final int DATABASE_VERSION = 1;

    //Database Name
    public static final String DATABASE_NAME = "Book_Db.db";

    //commons fields
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    // field type
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String TEXT_TYPE = " TEXT";
    public static final String REAL_TYPE = " REAL";
    public static final String DATETIME_TYPE = " DATETIME";
    public static final String COMMA_SEP = ", ";

    // Book Table name
    public static final String TABLE_BOOK = "Book";

    //Book Tables column
    public static final String COLUMN_BOOK_ID = COLUMN_ID;
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_AUTHOR_NAME = "author_name";
    public static final String COLUMN_PUBLISH_DATE = "publish_date";

    private String TAG = getClass().getSimpleName();


    /**
     * private constructor and only called from getInstance method
     * @param context
     */
    private DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbHelper = this;
    }

    /**
     * return a singleton object of this class
     * @param context
     * @return a singleton object
     */
    public static synchronized DbHelper getInstance(Context context){
        if( dbHelper == null ){
            dbHelper = new DbHelper(context.getApplicationContext());
            openConnection();
        }

        return dbHelper;
    }

    /**
     *  open Database connection if SQLiteDatabase variable is null or closed
     *  called when singleton is created
     */
    private static void openConnection(){
        if( db == null || !db.isOpen() ) {
            Utility.showLog("DbHelper", "Opening database connection");
            db = dbHelper.getWritableDatabase();
        }
    }

    public synchronized void closeConnection(){
        if( dbHelper != null ){
            if( db != null ) db.close();
            dbHelper.close();

            db = null;
            dbHelper = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createBookTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
       //Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_BOOK);

        // crate table again
        onCreate(sqLiteDatabase);
    }


    private void createBookTable( SQLiteDatabase sqLiteDatabase ){
        String queryString = "CREATE TABLE " + TABLE_BOOK+ " ("
                + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                + COLUMN_BOOK_TITLE + TEXT_TYPE + COMMA_SEP
                + COLUMN_BOOK_AUTHOR_NAME + TEXT_TYPE + COMMA_SEP
                + COLUMN_PUBLISH_DATE + INTEGER_TYPE + COMMA_SEP
                + COLUMN_UPDATED_AT + INTEGER_TYPE + COMMA_SEP
                + COLUMN_CREATED_AT + INTEGER_TYPE
                + " )";

        sqLiteDatabase.execSQL(queryString);

        Utility.showLog(TAG, "database created");
    }


    /**
     *
     * @param tableName table name
     * @param values  content values
     * @return insert id
     */
    public long insert(String tableName, ContentValues values){

        // open database connection if it is closed
        openConnection();

        long insertId = db.insert(tableName, null, values);
        Utility.showLog(TAG, "1 row is inserted in " + tableName);

        return insertId;
    }

    /**
     *
     * @param tableName table name
     * @param values  content values
     * @return insert id
     */
    public long insert(String tableName, ContentValues values, Loader<Cursor> loader){

        // open database connection if it is closed
        openConnection();

        long insertId = db.insert(tableName, null, values);
        Utility.showLog(TAG, "1 row is inserted in " + tableName);

        if( loader != null){
            Utility.showLog(TAG, " loader.onContentChanged()" );
            loader.onContentChanged();
        }

        return insertId;
    }



    /**
     *
     * @param tableName         table name
     * @param values            content values
     * @param selection         where clause
     * @param selectionArgs     the value of where clause
     * @return
     */
    public int update(String tableName, ContentValues values, String selection, String[] selectionArgs ){

        // open database connection if it is closed
        openConnection();

        int count = db.update(tableName, values, selection, selectionArgs);
        Utility.showLog(TAG, count + " row is update in " + tableName);
        return count;
    }



    /**
     *
     * @param tableName         table name
     * @param values            content values
     * @param selection         where clause
     * @param selectionArgs     the value of where clause
     * @return
     */
    public int update(String tableName, ContentValues values, String selection, String[] selectionArgs, Loader<Cursor> loader ){

        // open database connection if it is closed
        openConnection();

        int count = db.update(tableName, values, selection, selectionArgs);
        Utility.showLog(TAG, count + " row is update in " + tableName);

        if( loader != null){
            Utility.showLog(TAG, " loader.onContentChanged()" );
            loader.onContentChanged();
        }

        return count;
    }



    /**
     *
     * @param tableName     table name
     * @param columns       the columns to return
     * @param selection     where clause
     * @param selectionArgs the value of where clause
     * @param groupBy       Group by
     * @param having        Having
     * @param orderBy       Order by
     * @return boolean
     */
    public boolean isExist(String tableName, String[] columns, String selection,  String[] selectionArgs,String groupBy, String having, String orderBy  ){

        // open database connection if it is closed
        openConnection();

        Cursor cursor = db.query(
                tableName,                        //Table name
                columns,                         // the columns to return
                selection,                       // where clause
                selectionArgs,                   // The value of where clause
                groupBy,                         // Group by
                having,                          // Having
                orderBy                          // Order by
        );

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }


    public Cursor getEntry( String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return getEntry( tableName,  columns, selection, selectionArgs,  groupBy,  having,  orderBy, null);
    }


    public Cursor getEntry( String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy,
                            String having, String orderBy, String limit){

        // open database connection if it is closed
        openConnection();

        Cursor cursor = db.query(
                tableName,                        //Table name
                columns,                        // the columns to return
                selection,                       // where clause
                selectionArgs,                   // The value of where clause
                groupBy,                            // Group by
                having,                            // Having
                orderBy,                             // Order by
                limit
        );
        // close cursor when the cursor instance is not needed anymore
        return cursor;
    }




    /**
     *
     * @param tableName     database table name
     * @param whereClause   where clause
     * @param whereArgs     value of the where clause
     * @return int          the number of row deleted
     */
    public int delete( String tableName, String whereClause, String[] whereArgs ){

        // open database connection if it is closed
        openConnection();

        int count = db.delete(tableName, whereClause, whereArgs);
        Utility.showLog(TAG, count + " row deleted from " + tableName);

        return count;
    }

    public int delete( String tableName, String whereClause, String[] whereArgs, Loader<Cursor> loader ){

        // open database connection if it is closed
        openConnection();

        int count = db.delete(tableName, whereClause, whereArgs);
        Utility.showLog(TAG, count + " row deleted from " + tableName);

        if( loader != null){
            Utility.showLog(TAG, " loader.onContentChanged()" );
            loader.onContentChanged();
        }
        return count;
    }







}
