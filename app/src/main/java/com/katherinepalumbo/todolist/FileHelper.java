package com.katherinepalumbo.todolist;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

//Objective:
//  create method to store list data
//  create method to read data from file


public class FileHelper {
    public static final String FILENAME = "listinfo.dat";   //list items saved to device's memory

    //method to write data to file
    //will take in the item and the context in which it will be used
    public static void writeData(ArrayList<String> item, Context context)
    {
        //openFileOutput needs try/catch block
        try {
            //Steps: create a file in device memory, open file, write in file, close file
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);  //PRIVATE so other apps can't access this file
            ObjectOutputStream oas = new ObjectOutputStream(fos);   //need catch IOException
            oas.writeObject(item);
            oas.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    //method to read to file
    public static ArrayList<String> readData(Context context)
    {
        ArrayList<String> itemList = null;
        //openFileInput needs try/catch block
        try {
            //Steps: open file, read data, add read data to ArrayList
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //itemList = ois.readObject();
            itemList = (ArrayList<String>) ois.readObject();    //this will read data as an object, so typecast to string (ArrayList is string type)
        } catch (FileNotFoundException e) {
            itemList = new ArrayList<>();     //necessary for when app opens for first time
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return itemList;
    }

}
