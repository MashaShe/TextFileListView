package com.example.textfilelistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 100;
    private static String FILE_NAME = "longText.txt";
    private static String HEADER = "header";
    private static String COUNT = "count";
    private List<MyData> values;
    private List<MyData> toadapter;
    private ListView list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        ListView list = findViewById(R.id.listView);

        int permissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            LoadText();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            try {
                String text = readFile();
                values = prepareContent(text);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
           // Intent intent = new Intent(this, MainActivity.class);
           // startActivity(intent);
        }

        adapter = new MyAdapter(this, toadapter);
        list.setAdapter(adapter);
        Button addBut = findViewById(R.id.button);
        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (values.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No new items left", Toast.LENGTH_SHORT);
                } else {
                    adapter.addItem(values.get(0));
                    adapter.notifyDataSetChanged();
                    values.remove(0);
                }
            }
        });
    }


    //Load text from string to file
    private void LoadText() {
        if (isExternalStorageWritable()) {
            File file = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS),
                    FILE_NAME);
            String myText = getString(R.string.large_text);

            if (file.length() == 0) {  //if file is empty we upload text from strings again. if not empty we use data we've changed last time
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(myText);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            File filelog = new File(getExternalFilesDir(null),
                    "log.txt");
            try (FileWriter writer = new FileWriter(filelog, true)) {
                writer.write("app started");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //prepare List for adapter
    private List<MyData> prepareContent(String text) throws FileNotFoundException {
        String[] largeText = text.split("\n\n");
        List<MyData> content = new ArrayList<>();
        MyData oneItem;
        if (largeText.length > 3) {
            for (int i = 0; i < largeText.length - 3; i += 3) {
                oneItem = new MyData(largeText[i], largeText[i + 1], largeText[i + 2]);
                content.add(oneItem);

            }
        } else
            Toast.makeText(MainActivity.this, "File is too short to create an item", Toast.LENGTH_LONG).show();

        return content;
    }

    //read String from file
    private String readFile() throws IOException {
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        if (isExternalStorageReadable()) {
            File file = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS),
                    FILE_NAME);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            return stringBuilder.toString();
        } else {
            Toast.makeText(MainActivity.this, "file is unavailable", Toast.LENGTH_LONG).show();
        }
        return stringBuilder.toString();
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadText();
                } else {
                    // Toast
                    finish();
                }

        }
    }

}