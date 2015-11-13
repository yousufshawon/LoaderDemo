package com.yousuf.shawon.loaderdemo.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    Button btnAll, btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniUI();

    }


    private void iniUI(){
        btnAll = (Button) findViewById(R.id.btn_book_list);
        btnNew = (Button) findViewById(R.id.btn_add_new);

        iniAction();
    }



    private void iniAction(){
        if( btnAll != null ){
            btnAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent booksIntent = new Intent(MainActivity.this, AllBooksActivity.class);
                    startActivity(booksIntent);
                }
            });
        }

        if( btnNew != null ){
            btnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent addBookIntent = new Intent(MainActivity.this, AddBookActivity.class);
                    startActivity(addBookIntent);
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
