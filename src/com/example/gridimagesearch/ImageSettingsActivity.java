package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ImageSettingsActivity extends Activity implements OnItemSelectedListener, OnFocusChangeListener {

	EditText etSiteFilter; 
	Spinner imgTypeSpinner;
	Spinner colorSpinner;
	Spinner szSpinner;
    ImageSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_settings);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		imgTypeSpinner = (Spinner) findViewById(R.id.imgTypeSpinner);
		colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
		szSpinner = (Spinner) findViewById(R.id.szSpinner);
		settings = new ImageSettings();
		imgTypeSpinner.setOnItemSelectedListener(this);
		colorSpinner.setOnItemSelectedListener(this);
		szSpinner.setOnItemSelectedListener(this);
		etSiteFilter.setOnFocusChangeListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_settings, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long id) {
		// TODO Auto-generated method stub
		switch(parent.getId()) {
		case R.id.imgTypeSpinner:
			settings.imgType=(String) parent.getItemAtPosition(pos);
			break;
		case R.id.colorSpinner:
			settings.imgColor=(String)parent.getItemAtPosition(pos);
			break;
		case R.id.szSpinner:
			settings.imgSz = (String)parent.getItemAtPosition(pos);
			break;
		}
		
		Toast.makeText(this, settings.imgType + " " + settings.imgColor + " " + settings.imgSz , Toast.LENGTH_LONG).show();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	public void onSelectSettings(View v) {
		settings.filterText = etSiteFilter.getText().toString();
		returnResp(settings);		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OnFocusChange:" + v.getId(), Toast.LENGTH_LONG).show();
		//settings.filterText = etSiteFilter.getText().toString();
		//returnResp(settings);
	}

	private void returnResp(ImageSettings settings) {
		Intent data = new Intent();
		data.putExtra("settingsRes", settings);
		setResult(RESULT_OK, data);
		finish();
	}



}
