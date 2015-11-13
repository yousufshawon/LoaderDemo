package com.yousuf.shawon.loaderdemo.app;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.yousuf.shawon.loaderdemo.app.db.DbHelper;
import com.yousuf.shawon.loaderdemo.app.loader.DemoLoader;
import com.yousuf.shawon.loaderdemo.app.model.Book;
import com.yousuf.shawon.loaderdemo.app.utility.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AllBooksActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView listViewBooks;
    List<Book> bookList;

    ArrayAdapter<Book> bookArrayAdapter;

    String mAllBookColumns[] = new String[]{DbHelper.COLUMN_BOOK_ID, DbHelper.COLUMN_BOOK_TITLE, DbHelper.COLUMN_BOOK_AUTHOR_NAME,
    DbHelper.COLUMN_PUBLISH_DATE, DbHelper.COLUMN_UPDATED_AT, DbHelper.COLUMN_CREATED_AT};

    private int BOOK_LOADER = 1;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        bookList = new ArrayList<Book>();
        bookArrayAdapter  = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, bookList);


        iniUI();

        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }


    @Override
    protected void onResume() {

      //  loadAllBooks();

        super.onResume();


    }

    private void iniUI(){
        listViewBooks = (ListView) findViewById(R.id.list_view_books);

        iniAction();
    }




    private void iniAction(){
      //  bookArrayAdapter  = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, bookList);
        listViewBooks.setAdapter(bookArrayAdapter);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog mDialog = new AlertDialog.Builder(AllBooksActivity.this).create();

                mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Book book = bookArrayAdapter.getItem(position);

                        updateBooks(book);

                    }
                });


                mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Book book = bookArrayAdapter.getItem(position);

                        Loader<Cursor> loader = getLoaderManager().getLoader(BOOK_LOADER);

                        DbHelper.getInstance(getApplicationContext()).delete(DbHelper.TABLE_BOOK, DbHelper.COLUMN_BOOK_ID + " =?",
                                new String[]{String.valueOf(book.getId())}, loader);


                        dialogInterface.dismiss();
                    }
                });

                mDialog.show();


            }
        });
    }


    private void loadAllBooks(){
        bookList.clear();
        Cursor cursor = DbHelper.getInstance(getApplicationContext()).getEntry(DbHelper.TABLE_BOOK, mAllBookColumns, null, null, null, null, null);
        Utility.showLog(TAG, "Total row in cursor:" + cursor.getCount());
        bookList = getListFromCursor(cursor, bookList);

        bookArrayAdapter.notifyDataSetChanged();
    }


    private void updateBooks(final Book book){

         final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllBooksActivity.this);

        View inflateView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_add_book, null);

         mBuilder.setView(inflateView);
        final  AlertDialog mDialog = mBuilder.create();

        Button btnSubmit = (Button) inflateView.findViewById(R.id.btn_add_book_submit);
        final EditText etTitle = (EditText) inflateView.findViewById(R.id.et_add_book_title);
        final EditText etAuthorName = (EditText) inflateView.findViewById(R.id.et_add_book_author_name);

        etTitle.setText(book.getTitle());
        etAuthorName.setText(book.getAuthorName());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String authorName = etAuthorName.getText().toString().trim();
                if( title.length() < 1 || authorName.length() < 1  ){
                    showToast("Please fill title and author");
                }else{
                    book.setTitle(title);
                    book.setAuthorName(authorName);

                    ContentValues values = new ContentValues();
                    values.put(DbHelper.COLUMN_BOOK_TITLE, book.getTitle());
                    values.put(DbHelper.COLUMN_BOOK_AUTHOR_NAME, book.getAuthorName());
                    values.put(DbHelper.COLUMN_UPDATED_AT, "" + System.currentTimeMillis());

                    Loader<Cursor> loader = getLoaderManager().getLoader(BOOK_LOADER);

                    DbHelper.getInstance(getApplicationContext()).update(DbHelper.TABLE_BOOK, values, DbHelper.COLUMN_BOOK_ID + "=?",
                            new String[]{String.valueOf(book.getId())}, loader );

                    mDialog.dismiss();

                //  restartLoader();

                }
            }
        });

        mBuilder.setPositiveButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        mDialog.show();

    }


    private void restartLoader(){
        getLoaderManager().restartLoader(BOOK_LOADER, null, this);
    }


    private List<Book> getListFromCursor(Cursor cursor, List<Book> bList ){

        if( bList.size() > 0 ) bList.clear();

        if( cursor != null && cursor.moveToFirst() ){
            do {
                Book book = new Book();
                book.setId(cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_BOOK_ID)) );
                book.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_BOOK_TITLE)));
                book.setAuthorName(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_BOOK_AUTHOR_NAME)));
                book.setPublishDate(new Date( cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_PUBLISH_DATE)) ));
                bList.add(book);
            }while (cursor.moveToNext());
        }
        if( !cursor.isClosed() )
            cursor.close();
        return  bList;
    }



    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Utility.showLog(TAG, "onCreateLoader");
        return new DemoLoader(getApplicationContext(), DbHelper.TABLE_BOOK, mAllBookColumns, null, null, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Utility.showLog(TAG, "onLoadFinished");

        bookList = getListFromCursor(cursor, bookList);
        bookArrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Utility.showLog(TAG, "onLoaderReset");
        bookList.clear();
        bookArrayAdapter.notifyDataSetChanged();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
