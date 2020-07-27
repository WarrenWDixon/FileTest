package android.bignerdranch.filetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_textview);

        // first try to write file using FileWriter - this fails with IOException - Read Only File System
        // note that AndroidManifest file has permission to write to external storage
       try {
            FileWriter fileWriter = new FileWriter("testfile.txt");
            fileWriter.write("this is a test string");
            fileWriter.close();
            mTextView.setText("file write worked");
            Log.d("WWD", "first file write worked");
        }
        catch (IOException e) {
            mTextView.setText("file write failed");
            Log.d("WWD", "first file write failed");
        }

        // next try to use File class, the file.canRead() and file.canWrite() both come back false
        String filePath = this.getFilesDir().getPath().toString() + "/fileName.txt";
        File file = new File(filePath);
        //File file = new File("myfile.txt");

        if (file.canRead()) {
            Log.d("WWD", "file is readable");
        } else {
            Log.d("WWD", "file is not readable");
        }
        if (file.canWrite()) {
            Log.d("WWD", "file is writeable");
        } else {
            Log.d("WWD", "file is not writeable");
        }


        // next try to write file using FileOutputStream, this WORKS but no file is visible in folder structure
        // and FileOutputStream is for binary data, not text data
        String FILENAME = "hello_file";  // this works with no path
        //String FILENAME = "/Users/warrendixon/Android/work/filetest/hello_file";  // having path crashes app
        String string = "hello world!";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAME, this.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
            Log.d("WWD", "using FileOutputStream worked");
        } catch (IOException e) {
            Log.d("WWD", "got exception on using FileOutputStream ");
        }

        // next try to read data from file created above using FileOutputStream

        try {
            InputStream inputStream = this.openFileInput(FILENAME);
            if (inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String apiKey = "";
                StringBuilder stringBuilder  = new StringBuilder();
                apiKey = bufferedReader.readLine();
                Log.d("WWD", "apiKey is " + apiKey);
                if ((apiKey != null)  && (apiKey != "")) {
                    mTextView.setText(apiKey);
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.d("WWD", "file not found on trying to read FILENAME created by FileOutputStream");
            mTextView.setText("file not found");
        }
        catch (IOException e) {
            Log.d("WWD", " can not read file");
            mTextView.setText("unable to read file IOException");
        }

    }
}

