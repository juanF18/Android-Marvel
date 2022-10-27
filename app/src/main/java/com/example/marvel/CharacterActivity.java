package com.example.marvel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.marvel.model.Result;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import cafsoft.foundation.Data;
import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLRequest;
import cafsoft.foundation.URLSession;

public class CharacterActivity extends AppCompatActivity {
    private TextView labName;
    private TextView labDesc;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_activity);
        Result result =  (Result) getIntent().getSerializableExtra("result");
        //Log.d("Marvel", result.getName());

        initView();
        labName.setText(result.getName());
        labDesc.setText(result.getDescription());
        //URLComponents img = new URLComponents(result.getThumbnail().getPath()+"."+result);
        String path = result.getThumbnail().getPath();
        String ext = result.getThumbnail().getExtension();
        String strURL = (path+"."+ext).replace("http:","https:");
        URL url = null;
        try {
            url = new URL(strURL);
            downloadImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.d("Marvel", strURL);

    }

    public void initView(){
        labName = findViewById(R.id.labName);
        labDesc = findViewById(R.id.labDesc);
        imageView = findViewById(R.id.imageView);
    }

    public Bitmap dataToBitmap(Data data){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(data.toBytes(), 0, data.length());
        return bitmap;
    }

    public void downloadImage(URL url){
        URLRequest request = new URLRequest(url);
        URLSession.getShared().dataTask(request, (data, response,error) ->{
            if (error == null){
                HTTPURLResponse resp = (HTTPURLResponse) response;
                if(resp.getStatusCode() == 200){
                    Bitmap img = dataToBitmap(data);

                    runOnUiThread(() ->{
                        imageView.setImageBitmap(img);
                    });
                } else{
                    Log.d("Marvel", "Error: " + resp.getStatusCode());
                }
            } else {
                Log.d("Marvel", "Network error");
            }
        }).resume();
    }
}