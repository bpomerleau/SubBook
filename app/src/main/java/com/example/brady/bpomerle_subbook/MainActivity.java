package com.example.brady.bpomerle_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Manage and display user input Subscription information
 * @author Brady Pomerleau
 * @see EditMenu
 * @see Subscription
 */
public class MainActivity extends AppCompatActivity {
    protected static final int REQUEST_ADD = 10;
    protected static final int REQUEST_EDIT = 11;
    protected static final int RESULT_DELETE = 12;
    private static final String FILENAME = ".listdata.sav";
    private ListView sublist;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
    private TextView totalView;

    private ArrayList<Subscription> accountBook;
    private ArrayAdapter<Subscription> adapter;

    private int arraySelection; //this value describes the index of the subscription last clicked

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sublist = findViewById(R.id.sublist);
        addButton = findViewById(R.id.addSub);
        editButton = findViewById(R.id.editSub);
        totalView = findViewById(R.id.totalview);

        sublist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                arraySelection = position;
                editButton.setVisibility(View.VISIBLE);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(new Intent(MainActivity.this, EditMenu.class),
                        REQUEST_ADD);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent sendIntent = new Intent(getApplicationContext(),EditMenu.class);
                Bundle bundle = new Bundle();
                Subscription s = accountBook.get(arraySelection);
                bundle.putString("name",s.getName());
                bundle.putString("date", String.format(Locale.CANADA,"%1$tY-%<tm-%<td", s.getDate()));
                bundle.putFloat("amount", s.getAmount());
                bundle.putString("comment", s.getComment());
                sendIntent.putExtras(bundle);
                startActivityForResult(sendIntent, REQUEST_EDIT);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        loadFromFile();

        adapter = new ArrayAdapter<>(this, R.layout.list_item, accountBook);
        sublist.setAdapter(adapter);
        float total = 0;
        for (Subscription s: accountBook){
            total += s.getAmount();
        }
        totalView.setText("$ ".concat(String.format(Locale.CANADA,"%.2f",total)));

    }

    /**
     * Overridden startActivityForResult so that requestCode is always stored in passed Intent
     * @param intent data package
     * @param requestCode integer code describing type of request
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * Overridden onActivityResult - depending on type of request as well as type of return,
     *      create, edit, or delete
     * @param requestCode request code of Intent starting activity
     * @param resultCode result code returned by activity
     * @param data Intent containing user input subscription information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.e("mainactivity","returned");
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_ADD) {
                Bundle bundle = data.getExtras();
                if (bundle != null && !bundle.isEmpty()) {
                    try {
                        accountBook.add(new Subscription(bundle.getString("name"),
                                new SimpleDateFormat("yyyy-MM-dd").parse(bundle.getString("date")),
                                bundle.getFloat("amount"), bundle.getString("comment")));
                        adapter.notifyDataSetChanged();
                        saveInFile();

                    } catch (ParseException e){
                        Log.e("createSubscription","MainActivity: 90: should never get here");
                    }
                }
            } else if (requestCode == REQUEST_EDIT){
                Bundle bundle = data.getExtras();
                if (bundle != null && !bundle.isEmpty()){
                    Subscription s = accountBook.get(arraySelection);
                    s.setName(bundle.getString("name"));
                    s.setAmount(bundle.getFloat("amount"));
                    try {
                        s.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(bundle.getString("date")));
                    } catch (ParseException e) {/* I've already checked for this!*/}
                    s.setComment(bundle.getString("comment"));
                    adapter.notifyDataSetChanged();
                    saveInFile();
                    editButton.setVisibility(View.GONE);
                }
            }
        } else if (resultCode == RESULT_DELETE){
            accountBook.remove(arraySelection);
            adapter.notifyDataSetChanged();
            saveInFile();
            editButton.setVisibility(View.GONE);
        }
    }

    /**
     * load ArrayList info from file
     * adapted from lonelyTwitter class demo 2018-02-03
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //taken from https://stackoverflow.com/...
            //2018-01-23
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            accountBook = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            accountBook = new ArrayList<>();
        }
    }

    /**
     * save ArrayList info to file
     * adapted from lonelyTwitter class demo 2018-02-03
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(accountBook, out);
            out.flush();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
