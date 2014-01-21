package com.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	EditText etQuery;
	Button btnSearch;
	GridView gvResults;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	private final int REQUEST_CODE=20;
	ImageSettings img_settings; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		img_settings = new ImageSettings("", "", "", "");
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		Log.d("mytag", "in SearchActivityonCreate called");
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
			   Log.d("mytag", "onItemClickListener called");

               Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);				
               ImageResult imageResult = imageResults.get(position);
               //i.putExtra("url", imageResult.getFullUrl());
               i.putExtra("result", imageResult);
               startActivity(i);
			}
			
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				//Toast.makeText(, "onLoadMore ", Toast.LENGTH_LONG).show();
				Log.d("DEBUG", "onLoadMore called page=" + page + " totalItms=" + totalItemsCount);
                customLoadMoreDataFromApi(page);				
			}
			
		});
	}

	public void customLoadMoreDataFromApi(int offset) {
		String query = etQuery.getText().toString();
		AsyncHttpClient client = new AsyncHttpClient();
		Toast.makeText(this, "Query= " + query + " offset=" + offset, Toast.LENGTH_LONG).show();
        //commenting out for now
		
		client.get(genQuery(offset, query), 
		           new JsonHttpResponseHandler() {
			       @Override
			       public void onSuccess(JSONObject response) {
			    	   JSONArray imageJsonResults = null;
			    	   try {
			    		   imageJsonResults= response.getJSONObject("responseData").getJSONArray("results");
			    		   imageResults.clear();
			    		   // we are having the model populate result for us?
			    		   //imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
			    		   //Use adapter to load data into array and actual adapter...easier way
			    		   imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
			    		   Log.d("DEBUG", imageResults.toString());
			    	   } catch(JSONException e) {
			    		   e.printStackTrace();
			    	   }
			    	   
			       }
			 
		});
		
		
	}

	public void setupViews() {
		etQuery = (EditText)findViewById(R.id.etQuery);
	    btnSearch = (Button)findViewById(R.id.btnSearch);
	    gvResults = (GridView)findViewById(R.id.gvResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void onSettings(MenuItem mi) {
		Toast.makeText(this, "Settings clicked",  Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), ImageSettingsActivity.class);				
        startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			img_settings = (ImageSettings) data.getExtras().get("settingsRes");
			Toast.makeText(this, "Got back res as " + img_settings.imgColor + " " +
			                img_settings.imgSz + " " + img_settings.imgType + " " +
					        img_settings.filterText, Toast.LENGTH_LONG).show();
			
		}
	}
	
	private String genQuery(int offset, String query) {
		String res =  "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
		           "start=" + offset + "&v=1.0&q=" + Uri.encode(query);
		if (!TextUtils.isEmpty(img_settings.imgSz))  {
			res = res + "&imgsz=" +img_settings.imgSz;
		}
		
		if (!TextUtils.isEmpty(img_settings.imgType)) {
			res = res + "&imgtype=" + img_settings.imgType;
		}
		
		if (!TextUtils.isEmpty(img_settings.imgColor)) {
			res = res + "&imgcolor=" + img_settings.imgColor;
		}
		
		if (!TextUtils.isEmpty(img_settings.filterText)) {
			res = res + "&as_sitesearch=" + img_settings.filterText;
		}
		Toast.makeText(this, res, Toast.LENGTH_LONG).show();
		return res;
	}

	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
		AsyncHttpClient client = new AsyncHttpClient();
		//client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
		//           "start=" + 0 + "&v=1.0&q=" + Uri.encode(query), 
		client.get(genQuery(0, query), 
		           new JsonHttpResponseHandler() {
			       @Override
			       public void onSuccess(JSONObject response) {
			    	   JSONArray imageJsonResults = null;
			    	   try {
			    		   imageJsonResults= response.getJSONObject("responseData").getJSONArray("results");
			    		   imageResults.clear();
			    		   // we are having the model populate result for us?
			    		   //imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
			    		   //Use adapter to load data into array and actual adapter...easier way
			    		   imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
			    		   Log.d("DEBUG", imageResults.toString());
			    	   } catch(JSONException e) {
			    		   e.printStackTrace();
			    	   }
			    	   
			       }
			 
		});
	}
	
}
