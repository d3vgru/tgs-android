/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tudelft.triblerdroid.first;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import android.content.pm.*;

import com.tudelft.triblerdroid.first.R;
import com.googlecode.android_scripting.Constants;
import com.googlecode.android_scripting.facade.ActivityResultFacade;
import com.googlecode.android_scripting.jsonrpc.RpcReceiverManager;

import java.io.File;

/**
 * @author Alexey Reznichenko (alexey.reznichenko@gmail.com)
 */
public class ScriptActivity extends Activity {

	/*
	 * Arno: From Riccardo's original SwiftBeta
	 */
	NativeLib nativelib = null;
//	protected TextView _text;
    protected SwiftMainThread _swiftMainThread;
    protected StatsTask _statsTask;
	private VideoView mVideoView = null;
	protected Button _b1;
	protected Button _b2;
	protected Button _b3;
	protected Button _b4;
    protected ProgressDialog _dialog;
    protected Integer _seqCompInt;

    String hash; 
	String tracker;
	String destination;
	boolean inmainloop = false;
	
	
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	
	  super.onCreate(savedInstanceState);
      setTheme(android.R.style.Theme_Light);
      setContentView(R.layout.main);
    	
      ScriptApplication application = (ScriptApplication) getApplication();
      if (application.readyToStart()) {
        startService(new Intent(this, ScriptService.class));
      }
      // Arno, 2012-02-15: Hack to keep this activity alive.
      // finish();
      
      try
      {
    	  SwiftInitalize();
      }
      catch(Exception e)
      {
    	  e.printStackTrace();
      }
  }

  /*
   *  Arno: From Riccardo's original SwiftBeta
   */
  
  protected void SwiftInitalize()
  {
	  // create dir for swift
	  String swiftFolder = "/swift";
	  String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	  File mySwiftFolder = new File(extStorageDirectory + swiftFolder);
	  mySwiftFolder.mkdir();
	  
	  Bundle extras = getIntent().getExtras();
	  hash = extras.getString("hash");//"280244b5e0f22b167f96c08605ee879b0274ce22"
	  tracker = extras.getString("tracker");//"tracker3.p2p-next.org:20024"
	  destination = extras.getString("destination");//"/sdcard/swift/p1-s1e6-clip2-base.ts"
	  SwiftStartDownload();
  }
	
  
	//starts the download thread
	protected void SwiftStartDownload() {
		if (hash == null || destination == null || tracker == null) {
//			_text.setText("Swarm params are incorrect!!");
		}
		else {
			// Start the background process
	      _swiftMainThread = new SwiftMainThread();
	      _swiftMainThread.start();    	
	      // start the progress bar
	      SwiftCreateProgress();
	      _statsTask = new StatsTask();
	      _statsTask.execute( hash, tracker, destination );
		}
	}
	
	// creates the progress dialog
	protected void SwiftCreateProgress() {
		_dialog = new ProgressDialog(ScriptActivity.this);
	  _dialog.setCancelable(true);
	  _dialog.setMessage("Downloading...");
	  // set the progress to be horizontal
	  _dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	  // reset the bar to the default value of 0
	  _dialog.setProgress(0);
	  
	  //stop the engine if the procress scree is cancelled
	  _dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
//				_text.setText("TODO HTTPGW engine stopped!");
				// Arno, 2012-01-30: TODO tell HTTPGW to stop serving data
				//nativelib.stop();
			}
		});
	
	  // display the progressbar
	  _dialog.show();
	  
	}
	
	
	//starts the video playback
	private void SwiftStartPlayer() {
		//_dialog.dismiss();
		if (destination == null || destination.length() == 0) {
			Toast.makeText(ScriptActivity.this, "File URL/path is empty",
					Toast.LENGTH_LONG).show();
		}
		else {
			runOnUiThread(new Runnable(){
				public void run() {
					getWindow().setFormat(PixelFormat.TRANSLUCENT);
//					_text.setText("Play " + destination);
		    		mVideoView = (VideoView) findViewById(R.id.surface_view);
	
		    		// Arno, 2012-01-30: Download *and* play, using HTTPGW
		    		//String filename = "/sdcard/swift/" + destination;
		    		//mVideoView.setVideoPath(destination);
		    		String urlstr = "http://127.0.0.1:8082/"+hash;
		    		//String urlstr = "file:"+destination;
		    		mVideoView.setVideoURI(Uri.parse(urlstr));
		    		
		    		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared (MediaPlayer mp) {
//							_text.setText("Player75 prepared!");
							_dialog.dismiss();
						}
					});
		    		
		    		
		    		MediaController mediaController = new MediaController(ScriptActivity.this);
		    		mediaController.setAnchorView(mVideoView);
		            mVideoView.setMediaController(mediaController);
					mVideoView.start();
					mVideoView.requestFocus();
		    		//mediaController.show(0); // keep visible
				}
				
			});
			
		}
	}
	
    private class SwiftMainThread extends Thread
    {
        public void run() 
        {
    		try 
    		{
    			NativeLib nativelib =  new NativeLib();
    			String ret = nativelib.start(hash, tracker, destination);
    			
				SwiftStartPlayer();
				
				// Arno: Never returns, calls libevent2 mainloop
				if (!inmainloop) 
				{
					inmainloop = true;
					Log.w("Swift","Entering libevent2 mainloop");
					
					int progr = nativelib.progress();
					
					Log.w("Swift","LEFT MAINLOOP!");
    			}
    		}
        	catch (Exception e ) 
        	{
        			e.printStackTrace();
        	}
        }
    }
	
    
	/**
	* sub-class of AsyncTask. Retrieves stats from Swift via JNI and
	* updates the progress dialog.
	*/
	private class StatsTask extends AsyncTask<String, Integer, String> {
		
	  protected String doInBackground(String... args) {
	  	
	  	String ret = "hello";
	  	if (args.length != 3) {
	  		ret = "Received wrong number of parameters during initialization!";
	  	}
	  	else {
	  		try {
	
	  			NativeLib nativelib =  new NativeLib();
	  			mVideoView = (VideoView) findViewById(R.id.surface_view);
	  			boolean play = false, pause=false;
	  			while(true) {
	  				String progstr = nativelib.hello();
	  				String[] elems = progstr.split("/");
	  				long seqcomp = Long.parseLong(elems[0]);
	  				long asize = Long.parseLong(elems[1]);
	
	  				if (asize == 0)
	  					_dialog.setMax(1024);
	  				else
	  					_dialog.setMax((int)(asize/1024));
	  				
	  				_seqCompInt = new Integer((int)(seqcomp/1024));
	  				
	  				Log.w("SwiftStats", "SeqComp   " + seqcomp );
	  				
	  	    		runOnUiThread(new Runnable(){
	  	    			public void run() {
	          				_dialog.setProgress(_seqCompInt.intValue() );
	
	  	    			}
	  	    			
	  	    		});
	
	  				if (asize > 0 && seqcomp == asize)
	  				{
	  					Log.w("SwiftStats", "*** COMPLETE, STOP MONITOR ***");
	  					break;
	  				}
	  	    		
						Thread.sleep( 1000 );
	  			}
	  		}
	  		catch (Exception e ) {
	  			//System.out.println("Stacktrace "+e.toString());
	  			e.printStackTrace();
	  			ret = "error occurred during initialization!";
	  		}
	  	}
	      return ret;
	  }
	}

	

}
