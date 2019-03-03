package com.example.client_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Location1 extends AppCompatActivity {

    ProgressDialog prgDialog;
    String encodedString;
    //RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap, lesimg;
    private static int RESULT_LOAD_IMG = 1;
    private static int REQUEST_IMAGE_CAPTURE = 1;
    private static String TIME_STAMP="null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Intent intent=  getIntent();
        String message=intent.getStringExtra("EXTRA_MESSAGE");
        TextView view=findViewById(R.id.location);
        view.setText(message);
        prgDialog = new ProgressDialog(this);

        prgDialog.setCancelable(false);

    }
    public void loadImagefromGallery(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imgView = (ImageView) findViewById(R.id.imgView);
            imgView.setImageBitmap(imageBitmap);
            lesimg=imageBitmap;
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            String hjkl=currentDateTimeString.replaceAll(" ", "_");
            String hiop=hjkl.replaceAll(":", "-");
            TIME_STAMP=hiop;
            fileName=TIME_STAMP+".jpeg";
            //params.put("filename", fileName);
            // Save image to gallery



        }
    }


    public void uploadImage(View v) {
        encodeImagetoString();

    }


    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;

                bitmap=lesimg;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();

                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                prgDialog.show();

                //params.put("image", encodedString);

                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    public void makeHTTPCall() {
        prgDialog.setMessage("parsing image");
        prgDialog.show();

        prgDialog.setMessage("image upload in progress");
        prgDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = null;
        client.post("http://localhost:5000/upload",
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "string",
                                Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        prgDialog.hide();

                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }

                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }

                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }

                    }



                });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

}
