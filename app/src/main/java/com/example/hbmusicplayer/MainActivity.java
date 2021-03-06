package com.example.hbmusicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView myListViewForSongs;
    String[] iteams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("HB Music Player");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        myListViewForSongs=(ListView) findViewById(R.id.mySongListView);
        runtimePermission();
    }

    public void runtimePermission()
    {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        display();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {
                       // PermissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File> findSongs(File file){

        ArrayList<File> arrayList=new ArrayList<>();

        File[] files=file.listFiles();

        for(File singleFile: files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){

                arrayList.addAll(findSongs(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") ||
                singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    void display(){

        final ArrayList<File>mySongs=findSongs(Environment.getExternalStorageDirectory());
        iteams=new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){

            iteams[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace("Wav","");
        }

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,iteams);
        myListViewForSongs.setAdapter(myAdapter);


        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 //changeedonemanually int position replaced by int i
                String songName=myListViewForSongs.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                        .putExtra("songs",mySongs)  .putExtra("songname",songName).putExtra("pos",position));

            }
        });

    }
}