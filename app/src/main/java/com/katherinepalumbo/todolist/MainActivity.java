package com.katherinepalumbo.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText item;              //to add new task item
    Button add;                 //adds new task to list
    ListView listView;          //for list of items
    ArrayList<String> itemList = new ArrayList<>();     //stores tasks in list
    ArrayAdapter<String> arrayAdapter;      //connects ArrayList and ListView
                                            //can't add items directly to the adapter b/c you need to save the items on the device memory
                                            //to do this, you need to save the items on a file on the device
                                            //if added directly to the adapter, items would be deleted after app is closed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link main activity object to UI component
        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        listView = findViewById(R.id.list);

        //1. Want to be able to retrieve existing data, if previously stored
        //check if file to store data already exists
        //if file already exists, data will need to be saved to ArrayList
        //then, ListView can take this data from the ArrayList
        itemList = FileHelper.readData(this);       //read data from existing file
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemList); //Parameters: context, default layout for listview, default for basic listview, name of the ArrayList
        listView.setAdapter(arrayAdapter);  //assign adapter to ListView

        //2. Want to be able to add new items to ListView with Add button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Steps: data for new item will be saved to as a String type, data will be added to ArrayList,
                // EditText will be cleared, data will be added to file, tell adapter data has changed
                String itemName = item.getText().toString();
                itemList.add(itemName);
                item.setText("");   //this will clear the EditText
                FileHelper.writeData(itemList, getApplicationContext());
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //3. Want to be able to delete items from list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Steps: When item from list is clicked, AlertDialog will ask use if they want to delete the item
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete this item from the list?");
                alert.setCancelable(false);     //if use clicks elsewhere on screen, dialog box will not close
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {   //'which' refers to 'yes' or 'no' options here
                        dialogInterface.cancel();   //if user select 'No', dialog box will close
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {   //'which' refers to 'yes' or 'no' options here
                        //if user selects 'yes', item will be removed from ArrayList
                        itemList.remove(position); //position is item position on ArrayList
                        arrayAdapter.notifyDataSetChanged();    //ListView will need to be updated by notifying the adapter
                        FileHelper.writeData(itemList, getApplicationContext());    //write updated list to file
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }


}