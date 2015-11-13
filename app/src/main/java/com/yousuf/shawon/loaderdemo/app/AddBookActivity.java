package com.yousuf.shawon.loaderdemo.app;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yousuf.shawon.loaderdemo.app.db.DbHelper;
import com.yousuf.shawon.loaderdemo.app.model.Book;

import java.nio.DoubleBuffer;
import java.util.Date;


public class AddBookActivity extends ActionBarActivity {

    EditText etTitle, etAuthorName;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        iniUI();


    }



    private void iniUI(){

        etTitle = (EditText) findViewById(R.id.et_add_book_title);
        etAuthorName = (EditText) findViewById(R.id.et_add_book_author_name);
        btnSubmit = (Button) findViewById(R.id.btn_add_book_submit);
        iniAction();
    }



    private void iniAction(){

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String authorName = etAuthorName.getText().toString().trim();
                if( title.length() < 1 || authorName.length() < 1  ){
                    showToast("Please fill title and author");
                }else{
                    Book book = new Book(title, authorName, new Date(System.currentTimeMillis()));

                    ContentValues values = new ContentValues();
                    values.put(DbHelper.COLUMN_BOOK_TITLE, book.getTitle());
                    values.put(DbHelper.COLUMN_BOOK_AUTHOR_NAME, book.getAuthorName());
                    values.put(DbHelper.COLUMN_PUBLISH_DATE, "" + book.getPublishDate());
                    values.put(DbHelper.COLUMN_UPDATED_AT, "" + System.currentTimeMillis());
                    values.put(DbHelper.COLUMN_CREATED_AT, "" + System.currentTimeMillis());

                    DbHelper.getInstance(getApplicationContext()).insert(DbHelper.TABLE_BOOK, values);

                    etTitle.setText("");
                    etAuthorName.setText("");
                    finish();
                }
            }
        });
    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
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
