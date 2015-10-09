package com.training.android.dle.instagramphoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "50cfa56c8a8d4259a1578c1c3c3f5689";
    private List<InstagramPhoto> photos;
    private ListView lvPhotos;
    private InstagramPhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photos = new ArrayList<InstagramPhoto>();
        photoAdapter = new InstagramPhotoAdapter(this, photos);
        lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photoAdapter);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++){
                        JSONObject photoJson = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.profileUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.createdTime = photoJson.getJSONObject("caption").getLong("created_time");
                        photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likeCount = photoJson.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
