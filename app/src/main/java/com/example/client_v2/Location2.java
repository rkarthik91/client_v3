package com.example.client_v2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.HttpsConnection;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Location2 extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE=1;
    private static final String SERVER_ADDRESS="gynecoid-sunshine.000webhostapp.com";
    ImageView imagetoupload,imagetodownload;
    Button bUpload,bDownload;
    EditText uploadName,downloadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagetoupload=(ImageView)findViewById(R.id.imageToUpload);
        imagetodownload=(ImageView)findViewById(R.id.downloadImage);

        bUpload=(Button)findViewById(R.id.bUploaadImage);
        bDownload=(Button)findViewById(R.id.bDownloadImage);

        uploadName=(EditText)findViewById(R.id.etUploadName);
        downloadName=(EditText)findViewById(R.id.downloadName);

        imagetoupload.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bDownload.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageToUpload:
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploaadImage:
                Bitmap image=((BitmapDrawable) imagetoupload.getDrawable()).getBitmap();
                new UploadImage(image,uploadName.getText().toString()).execute();
                break;
            case R.id.bDownloadImage:

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Uri selectedImage=data.getData();
            imagetoupload.setImageURI(selectedImage);
        }
    }

    private class UploadImage extends AsyncTask<Void,Void,Void> {

        Bitmap image;
        String name;
        public UploadImage(Bitmap image , String name){
            this.image=image;
            this.name=name;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            String encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image",encodedImage));
            dataToSend.add(new BasicNameValuePair("name",name));

            HttpParams httpRequestParams=getHttpRequestParams();

            HttpClient client=new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Savepicture.php");
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image Upload",Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{

        String name;



        @Override
        protected Bitmap doInBackground(Void... voids) {
            //String URL
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,100*30);
        HttpConnectionParams.setSoTimeout(httpParams,1000*30);
        return httpParams;
    }

}
