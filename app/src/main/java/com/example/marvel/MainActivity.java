package com.example.marvel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.marvel.model.Result;
import com.example.marvel.model.Root;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.ArrayList;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;


public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    public void initView(){
        btnSearch = findViewById(R.id.btnSearch);
        txtName = findViewById(R.id.txtName);
    }

    public void initEvents(){
        btnSearch.setOnClickListener(view -> getInfoApi());
    }

    public void showAlert(String title, String text){

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
        txtName.setText("");
    }

    public void getInfoApi(){

        String name = txtName.getText().toString();
        URL url = null;
        URLComponents components = new URLComponents();

        components.setScheme("https");
        components.setHost("gateway.marvel.com");
        components.setPath("/v1/public/characters");
        components.setQueryItems( new URLQueryItem[]{
                new URLQueryItem( "ts", "12345"),
                new URLQueryItem("apikey", "747a1ba5a7f79efde5f366558f03859f"),
                new URLQueryItem("hash", "a8e395e768458c6a2fc375cba1405d30"),
                new URLQueryItem("name", name)
        });

        url = components.getURL();
        String strURL = url.toString();
        Log.d("Marvel", strURL);

        URLSession.getShared().dataTask(url, (data, response, error) -> {
            HTTPURLResponse resp = (HTTPURLResponse) response;
            if(error == null){
                if(resp.getStatusCode() == 200){
                    String text = data.toText();
                    //Log.d("Marvel", text);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson json = gsonBuilder.create();
                    Root root = json.fromJson(text, Root.class);
                    //Log.d("Marvel", root.getData().getResults().get(0).getName());

                    ArrayList<Result> results = root.getData().getResults();
                    if(results.size() > 0){
                        Result result = results.get(0);
                        runOnUiThread(()->{
                            Intent intent = new Intent(this, CharacterActivity.class);
                            intent.putExtra("result", result);
                            startActivity(intent);
                        });

                    } else {
                        runOnUiThread(() ->{
                            showAlert("Datos Erroneos", "Los datos ingresados estan erroneos o no existe el caracter");
                        });
                    }

                } else {
                    Log.d("Marvel", "Error: " + resp.getStatusCode());
                }
            } else {
                Log.d("Marvel", "Network error");
            }
        }).resume();
    }


}