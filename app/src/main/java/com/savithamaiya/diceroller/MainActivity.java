package com.savithamaiya.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import android.preference.PreferenceManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    TextView display,history;
    Button rollOnce,rollTwice,add;
    EditText editText;

    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "DieRollerPrefs";
    private static final String SHARED_PREF_KEY = "diceList";

    List<String> diceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.display);
        rollOnce = (Button) findViewById(R.id.rollOnce);
        rollTwice = (Button) findViewById(R.id.rollTwice);
        add = (Button) findViewById(R.id.add);
        editText = (EditText) findViewById(R.id.editText);
        history = (TextView) findViewById(R.id.history);

        spinner = (Spinner) findViewById(R.id.spinner);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);



        String storedDiceList = sharedPreferences.getString(SHARED_PREF_KEY,"");

        if(storedDiceList.isEmpty()){
            diceList.add("4");
            diceList.add("6");
            diceList.add("8");
            diceList.add("10");
            diceList.add("12");
            diceList.add("20");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREF_KEY,getDiceListAsString());
            editor.commit();
        }else{

            convertStringToDiceList(storedDiceList);
        }

        ArrayAdapter<String> scoreSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, diceList);
        scoreSpinner.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        spinner.setAdapter(scoreSpinner);

        display.setText("");
        history.setText("History: ");

        rollOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noOfSides = Integer.parseInt(diceList.get(spinner.getSelectedItemPosition()));
                rollOneDie(noOfSides);
            }
        });

        rollTwice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noOfSides = Integer.parseInt(diceList.get(spinner.getSelectedItemPosition()));
                rollTwoDice(noOfSides);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noOfSides = editText.getText().toString();
                addNewDice(noOfSides);
            }
        });
    }

    private void rollOneDie(Integer noOfSides){
        String rollHistory = history.getText().toString();
        String newRoll = String.valueOf((int)(Math.random() * noOfSides) + 1);
        display.setText(newRoll);
        if(rollHistory.length() == 9) {
            history.setText(rollHistory + newRoll);
        }
        else {
            history.setText(rollHistory + "," + newRoll);
        }


    }

    private void rollTwoDice(Integer noOfSides){
        String rollHistory = history.getText().toString();
        int dice1Roll = (int)(Math.random() * noOfSides) + 1;
        int dice2Roll = (int)(Math.random() * noOfSides) + 1;
        String newRoll = dice1Roll +" | "+ dice2Roll;
        display.setText(newRoll);
        history.setText(rollHistory + "," + newRoll);
    }

    private void addNewDice(String noOfSides){
        if(null!=noOfSides && !noOfSides.trim().isEmpty() && !diceList.contains(noOfSides)){
            diceList.add(noOfSides);
            Collections.sort(diceList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return Integer.parseInt(s1)-Integer.parseInt(s2);
                }
            });
            editText.setText("");
            spinner.setSelection(diceList.indexOf(noOfSides));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREF_KEY,getDiceListAsString());
            editor.commit();
        }
    }

    private String getDiceListAsString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<diceList.size();i++){
            sb.append(diceList.get(i));
            if(i<diceList.size()-1)
                sb.append(",");
        }
        return sb.toString();
    }

    private void convertStringToDiceList(String data){
        String[] dataArr = data.split(",");
        diceList.clear();
        for(int i=0;i< dataArr.length;i++){
            diceList.add(dataArr[i]);
        }
    }


}