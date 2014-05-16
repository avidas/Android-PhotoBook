package com.avidas.photoviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
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
		new FlickrTask().execute();
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
	
	class FlickrTask extends AsyncTask<String, Integer, List> {
		private static final String FLICKR_API_KEY = null;
		private static final String FLICKR_FORMAT = null;
		private ProgressDialog progressDialog;
		private Integer totalCount, currentIndex;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PhotoGridActivity.this);
			progressDialog.setMessage("Loading images from Flickr. Please wait...");
			progressDialog.show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values){
			super.onProgressUpdate(values);
			progressDialog.setMessage(String.format("Loading images from Flickr %s%s. Please wait...", values[0], values[1]));
		}
		
		@Override
		protected List doInBackground(String... params) {
			Flickr flickr = new Flickr(FLICKR_API_KEY, FLICKR_FORMAT);
			List photos = flickr.getPhotoSets().getPhotos(PHOTOSET_ID);
			List result = new ArrayList();
			totalCount = photos.size();
			currentIndex = 0;
			for (Photo photo : photos) {
				currentIndex++;
				List sizes = flickr.getPhotos().getSizes(photo.getId());
				String thumbnailUrl = sizes.get(0).getSource();
				String mediumUrl = sizes.get(4).getSource();
				InputStream inputSteamThumbnail = null, inputStreamMedium=null;
				try {
					 inputStreamThumbnail = new URL(thumbnailUrl).openStream();
		             inputStreamMedium = new URL(mediumUrl).openStream();
				} catch (IOException e) {
	                e.printStackTrace();
	            }
				Bitmap bitmapThumbnail = BitmapFactory.decodeStream(inputStreamThumbnail);
	            Bitmap bitmapMedium = BitmapFactory.decodeStream(inputStreamMedium);
	            result.add(new ImageInfo(photo.getTitle(),bitmapThumbnail ,bitmapMedium ));
	            publishProgress(currentIndex, totalCount);
				
			}
			currentAppData.setImageInfos(result);
	        return result;
		}
		@Override
		protected void onPostExecute(List s) {
			progressDialog.dismiss();
			imageGridViewAdapter = new ImageGridViewAdapter(MainActivity.this);
	        gridView.setAdapter(imageGridViewAdapter);
	        super.onPostExecute(s);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_grid, menu);
		return true;
	}

}
