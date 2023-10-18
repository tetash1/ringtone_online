package com.islamialib.islamic_ringtone.serverside;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.islamialib.islamic_ringtone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapterServer videoAdapter;
    private List<ConstructorsGeterSeter> dataList;

    LinearLayout loading_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Title");
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_200));

        recyclerView = findViewById(R.id.recyclerView);
        loading_progress = findViewById(R.id.loading_progress);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dataList = new ArrayList<>();
        videoAdapter = new VideoAdapterServer(dataList, this);
        recyclerView.setAdapter(videoAdapter);

        recyclerView.setVisibility(View.GONE);
        loading_progress.setVisibility(View.VISIBLE);




        // Make a Volley request to retrieve data from your PHP script and update dataList
        fetchDataFromServer();
    }
    private void fetchDataFromServer() {
        // Make a Volley GET request to your PHP API endpoint
        // Parse the JSON response and update dataList
        // Example code for making a GET request with Volley:
        String url = "domain.com/getData.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                recyclerView.setVisibility(View.VISIBLE);
                                loading_progress.setVisibility(View.GONE);
                                JSONObject jsonObject = response.getJSONObject(i);
                                ConstructorsGeterSeter data = new ConstructorsGeterSeter();
                                data.setPlaylistName(jsonObject.getString("playlist_name"));
                                data.setPlaylistLink(jsonObject.getString("playlist_link"));
                                data.setUsername(jsonObject.getString("username"));
                                data.setImageUrl(jsonObject.getString("image_url"));
                                dataList.add(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        videoAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }
        );

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

}