package com.dev.swapftrz;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.resource.SPFZResourceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AndroidLauncher extends AndroidApplication implements AndroidInterfaceLIBGDX
{
  AndroidApplicationConfiguration config;

  //layout necessary for full game view
  RelativeLayout layout;
  RelativeLayout.LayoutParams adParams;
  View gameView;
  Display display;
  final AndroidLauncher context = this;
  Handler h = new Handler();
  int bright;

  static final String FILE_NAME = "spfzfile";
  static final String FILE_NAME2 = "spfzstory";

  //AD Initializations
  //private InterstitialAd SPFZ_INT;
  //private AdView SPFZ_BAN;
  //private AdRequest adRequest;
  String orientation;
  SPFZResourceManager resourceManager;
  VideoView videoview;
  boolean load;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //MobileAds.initialize(this, OnInitializationCompleteListener);
    resourceManager = new SPFZResourceManager();
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.introvideoview);

    layout = new RelativeLayout(this);

    //configure ads
    /*SPFZ_BAN = new AdView(this);
    SPFZ_BAN.setAdListener(new AdListener()
    {
      @Override
      public void onAdClosed() {
        //SPFZ_BAN.setVisibility(View.INVISIBLE);
        layout.removeView(SPFZ_BAN);
        super.onAdClosed();
      }
    });*/

    /*SPFZ_BAN.setAdSize(AdSize.SMART_BANNER);
    SPFZ_BAN.setAdUnitId(getString(R.string.TEST_BANNER_ID));
    //adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
    SPFZ_INT = new InterstitialAd(this);
    SPFZ_INT.setAdUnitId(getString(R.string.TEST_INTER_ID));*/


    config = new AndroidApplicationConfiguration();
    //rm = new SPFZResourceManager();
    //config.numSamples = 2;
    //loadresources();
    //rm.initAllResources();
    display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

    //videoview = (VideoView) findViewById(R.id.videoView1);
    // videoview.setVideoPath("/video/testsplash.mp4");

    //videoview.start();
    //initialize(new spfzTrial(this), config);

    /*RelativeLayout.LayoutParams adParams =
      new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT);
    adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);*/
    //adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    config.useAccelerometer = false;
    config.useCompass = false;
    gameView = initializeForView(new SwapFyterzMain(this, resourceManager), config);
    //SPFZ_BAN.loadAd(adRequest);

    /*SPFZ_INT.loadAd(new AdRequest.Builder().build());
    SPFZ_INT.setAdListener(new AdListener()
    {
      @Override
      public void onAdClosed() {
        // Load the next interstitial.
        SPFZ_INT.loadAd(new AdRequest.Builder().build());
      }

    });*/

    //add views to the layout but allow banner to overlap the gameView
    layout.addView(gameView);
    //layout.addView(SPFZ_BAN, adParams);

    setContentView(layout);
    hideAD();
    //SPFZ_BAN.setVisibility(View.VISIBLE);

  }

  public void loadresources() {
    /*AsyncTask.execute(new Runnable()
    {
      @Override
      public void run() {
        //rm.initAllResources();
        rm.initGame();
      }
    });*/
  }

  public void hideAD() {
    /*if (SPFZ_BAN.getVisibility() == View.VISIBLE)
    {
      this.runOnUiThread(new Runnable()
      {
        @Override
        public void run() {
          SPFZ_BAN.setVisibility(View.GONE);
          layout.removeViewInLayout(SPFZ_BAN);
        }
      });
    }*/
  }

  public void loaded() {

    /*this.runOnUiThread(new Runnable()
    {
      @Override
      public void run() {
        load = SPFZ_INT.isLoaded();
      }
    });

    return load;*/
  }

  public void NEW_SPFZ_AD(String ADTYPE) {

    /*switch (ADTYPE)
    {

      case "banner":
        if (SPFZ_BAN.getVisibility() == View.GONE || SPFZ_BAN.getVisibility() == View.INVISIBLE)
        {
          this.runOnUiThread(new Runnable()
          {
            @Override
            public void run() {

              // AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
              AdRequest adRequest = new AdRequest.Builder().addTestDevice("3ED981949537812063CB3B441385DC45").build();
              RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                  RelativeLayout.LayoutParams.WRAP_CONTENT);
              adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
              layout.addView(SPFZ_BAN, adParams);
              SPFZ_BAN.loadAd(adRequest);
              SPFZ_BAN.setVisibility(View.VISIBLE);
              SPFZ_BAN.setAlpha(1f);

              SPFZ_BAN.setAdListener(new AdListener()
              {

                @Override
                public void onAdClosed() {
                  SPFZ_BAN.setVisibility(View.INVISIBLE);
                  SPFZ_BAN.setAlpha(0f);
                  layout.removeView(SPFZ_BAN);
                  super.onAdClosed();
                }

                @Override
                public void onAdClicked() {
                  SPFZ_BAN.setVisibility(View.INVISIBLE);
                  SPFZ_BAN.setAlpha(0f);
                  layout.removeView(SPFZ_BAN);
                  super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                  super.onAdImpression();
                }
              });
            }
          });
        }

        break;
      case "inter":
        //SPFZ_INT.loadAd(adRequest);
        //SPFZ_INT.loadAd(new AdRequest.Builder().build());
        this.runOnUiThread(new Runnable()
        {
          @Override
          public void run() {

            SPFZ_INT.show();
          }
        });

        //toast("interstitial ad displayed");
        break;
      default:
        break;
    }*/
  }

  public void checkINT() {
    /*if (SPFZ_INT.isLoaded())
    {
      SPFZ_INT.show();
    }*/
  }

  public void videocall() {
    handler.post(new Runnable()
    {

      @Override
      public void run() {
        display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        videoview = (VideoView) findViewById(R.id.videoView1);
        //videoview.setVideoPath("/video/testsplash.mp4");
        videoview.requestFocus();
        // videoview.start();

      }
    });
  }

  public void initgame(final AndroidInterfaceLIBGDX android) {
    h.postDelayed(new Runnable()
    {

      @Override
      public void run() {


      }
    }, 1000);
  }

  @Override
  public void toast(final String toast) {
    handler.post(new Runnable()
    {

      @Override
      public void run() {

        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

      }

    });

  }

  public int getADattr() {
    return 0;
    //return SPFZ_BAN.getVisibility();
  }

  public int visible() {
    return View.VISIBLE;
  }

  @Override
  public void lockOrientation(final boolean lock, final String orientation) {
    handler.post(new Runnable()
    {

      @Override
      public void run() {
        if (lock && orientation == "portrait")
        {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        else if (lock && orientation == "landscape")
        {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else
        {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
      }

    });

  }

  @Override
  public void adjustBrightness(final float brightness) {
    bright = (int) brightness;
    handler.post(new Runnable()
    {

      @Override
      public void run() {

        android.provider.Settings.System.putInt(context.getContentResolver(),
          android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
          android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        android.provider.Settings.System.putInt(context.getContentResolver(),
          android.provider.Settings.System.SCREEN_BRIGHTNESS, bright);
      }

    });

  }

  public void getrotation() {
    switch (display.getRotation())
    {
      case Surface.ROTATION_0:
        orientation = "portrait";
        break;
      case Surface.ROTATION_90:
        orientation = "landscape";
        break;
      case Surface.ROTATION_180:
        orientation = "portrait";
        break;
      case Surface.ROTATION_270:
        orientation = "landscape";
        break;

    }

  }

  public String getorientation() {
    return orientation;
  }

  public void setorientation(String orient) {
    orientation = orient;
  }

  @Override
  public int getBrightness() {

    handler.post(new Runnable()
    {

      @Override
      public void run() {

        android.provider.Settings.System.putInt(context.getContentResolver(),
          android.provider.Settings.System.SCREEN_BRIGHTNESS, bright);
      }

    });

    return bright;
  }

  public void writeFile(final String values, boolean append) {
    try
    {
      // String message;
      // message = values;
      if (values.length() == 1)
      {
        /*
         * initialization of application. This is needed in order to check to
         * see if the file exists. So we do not override any possible values
         * that will exists for the in game settings. values = "null";
         *
         */

        //toast("file initialized successfully");
      }
      else
      {

        if (append)
        {

          FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
          fileOutputStream.write(values.getBytes());
          fileOutputStream.close();
        }
        else
        {
          FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
          fileOutputStream.write(values.getBytes());
          fileOutputStream.close();
        }


      }
    }

    catch (FileNotFoundException e)
    {
      //toast("file created successfully");

      e.printStackTrace();
    }
    catch (IOException e)
    {

      e.printStackTrace();
    }

  }

  public void writeFile2(final String values, boolean append) {
    try
    {
      // String message;
      // message = values;
      if (values.length() == 1)
      {
        /*
         * On initialization. Populate the text file within all story text.
         *
         */

        //toast("file initialized successfully");
      }
      else
      {

        if (append)
        {

          FileOutputStream fileOutputStream = openFileOutput(FILE_NAME2, MODE_APPEND);
          fileOutputStream.write(values.getBytes());
          fileOutputStream.close();
        }
        else
        {
          FileOutputStream fileOutputStream = openFileOutput(FILE_NAME2, MODE_PRIVATE);
          fileOutputStream.write(values.getBytes());
          fileOutputStream.close();
        }


      }
    }

    catch (FileNotFoundException e)
    {
      //toast("file created successfully");

      e.printStackTrace();
    }
    catch (IOException e)
    {

      e.printStackTrace();
    }

  }

  public String readFile(final String file) {
    FileInputStream fileInputStream;
    String message = null;
    String values = "";
    try
    {


      fileInputStream = openFileInput(file);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuffer stringBuffer = new StringBuffer();

      while ((message = bufferedReader.readLine()) != null)
      {
        stringBuffer.append(message + "\n");
        values = values + message + "\n";
        //toast(message);
      }

      if (values.length() == 1)
      {
        values = null;
      }

      //toast("file read successfully");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return values;
  }
}