package com.avidas.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotoGridActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_grid);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	// Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_grid, menu);
		return true;
	}

}
