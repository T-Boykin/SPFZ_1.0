package com.dev.swapftrz;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity
{
	VideoView videoview;
	String urlpath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introvideoview);
		videoview = (VideoView) findViewById(R.id.videoView1);
		urlpath  = "android.resource://" + getPackageName() + "/" + R.raw.testsplash;
		videoview.setVideoURI(Uri.parse(urlpath));
		
		
    
		videoview.start();
	
		videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
		{
			
			@Override
			public void onCompletion(MediaPlayer mp)
			{
				try
				{
					
					Intent intent = new Intent("com.spfz.alpha.ANDROIDLAUNCHER");
					startActivity(intent);
				}
				finally
				{
					finish();
				}
				
			}
		});
	}

}
