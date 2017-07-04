package com.example.builehieu.hat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AbsRuntimePermission {

    ListView listSong;

    String[] item;


    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            requestAppPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS},
                    R.string.msg, REQUEST_PERMISSION);



        listSong = (ListView) findViewById(R.id.listSong);



        final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
        item = new String[mySong.size()];

        for (int i = 0; i < mySong.size(); i++) {
          item[i] = mySong.get(i).getName().toString().replace(".mp3","");
           // Toast.makeText(getApplicationContext(),""+item.toString(),Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, item);
        listSong.setAdapter(stringArrayAdapter);
        listSong.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(),PlayActivity.class).putExtra("pos",position).putExtra("song",mySong));


            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();

    }

    private ArrayList<File> findSong(File root) {
        ArrayList<File> al = new ArrayList<>();
        File[] files = root.listFiles();

        for (File singe : files) {
            if (singe.isDirectory()) {

               al.addAll(findSong(singe));

            } else {
                if (singe.getName().endsWith(".mp3")) {
                    al.add(singe);

                }

            }
        }
        return al;

    }

}