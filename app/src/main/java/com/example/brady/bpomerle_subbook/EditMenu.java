package com.example.brady.bpomerle_subbook;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.example.brady.bpomerle_subbook.MainActivity.RESULT_DELETE;

public class EditMenu extends AppCompatActivity {

    private EditText nameInput;
    private EditText amountInput;
    private EditText dateInput;
    private EditText commentInput;
    private Button addButton;
    private Button deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        nameInput = findViewById(R.id.name_input);
        amountInput = findViewById(R.id.amount_input);
        dateInput = findViewById(R.id.date_input);
        commentInput = findViewById(R.id.comment_input);

        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);

        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                try {
                    if (nameInput.getText().toString().equals(""))
                        throw new HorseWithNoNameException();
                    Float.valueOf(amountInput.getText().toString());
                    new SimpleDateFormat("yyyy-MM-dd").parse(dateInput.getText().toString());
                    if(commentInput.getText().toString().length() > 30
                        || nameInput.getText().toString().length() > 20)
                           throw new TooLongException();
                } catch (HorseWithNoNameException e) {
                    Snackbar.make(addButton, getResources().getString(R.string.nameError), 1000).show();
                    setResult(RESULT_CANCELED);
                    return;
                } catch (ParseException e) {
                    Snackbar.make(addButton, getResources().getString(R.string.dateError), 1000).show();
                    setResult(RESULT_CANCELED);
                    return;
                } catch (NumberFormatException e) {
                    Snackbar.make(addButton, getResources().getString(R.string.amountError), 1000).show();
                    setResult(RESULT_CANCELED);
                    return;
                } catch (TooLongException e) {
                    Snackbar.make(addButton, "Don't write a novel here!", 1000).show();
                    setResult(RESULT_CANCELED);
                }
                Intent returnIntent = new Intent(EditMenu.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",nameInput.getText().toString());
                bundle.putString("date",dateInput.getText().toString());
                bundle.putFloat("amount",Float.valueOf(amountInput.getText().toString()));
                bundle.putString("comment",commentInput.getText().toString());
                returnIntent.putExtras(bundle);
                setResult(RESULT_OK, returnIntent);
                finish();


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_DELETE);
                finish();
            }
        });
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState){
//        nameInput.setText(savedInstanceState.getString("name"));
//        amountInput.setText(savedInstanceState.getString("amount"));
//        dateInput.setText(savedInstanceState.getString("date"));
//        commentInput.setText(savedInstanceState.getString("comment"));
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState){
//        outState.putString("name",nameInput.getText().toString());
//        outState.putString("amount",amountInput.getText().toString());
//        outState.putString("date",dateInput.getText().toString());
//        outState.putString("comment",commentInput.getText().toString());
//
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onStart(){
        super.onStart();
        Intent receiveIntent = getIntent();
        Bundle bundle = receiveIntent.getExtras();
        if (bundle != null && !bundle.isEmpty()){
            nameInput.setText(bundle.getString("name"));
            amountInput.setText(String.format(Locale.CANADA, "%.2f", bundle.getFloat("amount")));
            dateInput.setText(bundle.getString("date"));
            commentInput.setText(bundle.getString("comment"));
        }
        if (receiveIntent.getIntExtra("requestCode", 0) == MainActivity.REQUEST_EDIT){
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setText("Save");
        }
    }
}
