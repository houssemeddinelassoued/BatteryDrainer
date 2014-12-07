package com.example.patrick.batterydrainer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Context context;

    // The boolean variables (function + On) are for preventing the user to launch certain function multiple times
    // For audio and camera it allows to identify if the user is stopping or starting the function
    boolean audioOn = false;
    MediaPlayer mp;

    Camera camera;
    boolean cameraOn = false;

    boolean CPUOn = false;

    boolean GPSOn = false;

    boolean networkOn = false;

    boolean vibratorOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // Set buttons
        Button buttonAudio = (Button) findViewById(R.id.buttonAudio);
        Button buttonCamera = (Button) findViewById(R.id.buttonCamera);
        Button buttonCPU = (Button) findViewById(R.id.buttonCPU);
        Button buttonGPS = (Button) findViewById(R.id.buttonGPS);
        Button buttonNetwork = (Button) findViewById(R.id.buttonNetwork);
        Button buttonVibrator = (Button) findViewById(R.id.buttonVibrator);

        // Set surface view for the camera
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // Audio button
        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioOn) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.rickroll);
                    mp.setLooping(true); // Set the song on loop
                    mp.start();
                    audioOn = true;
                }
                else {
                    mp.stop();
                    audioOn = false;
                }
            }
        });

        // Camera button
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cameraOn) {
                    try {
                        camera = Camera.open();
                        Camera.Parameters parameters = camera.getParameters();
                        // Turn on LED flash
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        cameraOn = true;
                    } catch (Exception e) {
                        System.out.println("Camera failed!");
                    }
                }
                // Stop camera if it's running and the camera button is pressed
                else {
                    camera.release();
                    camera = null;
                    cameraOn = false;
                }
            }
        });

        // CPU button
        buttonCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CPUOn) {
                    // Starts four empty while loops in different threads
                    (new Thread(new CPURunnable())).start();
                    (new Thread(new CPURunnable())).start();
                    (new Thread(new CPURunnable())).start();
                    (new Thread(new CPURunnable())).start();
                    CPUOn = true;
                }

            }
        });

        // GPS button
        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GPSOn) {
                    LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                    LocationListener locListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    // Updates location. Minimum time and distance between location updates are set to 0
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                    GPSOn = true;
                }
            }
        });

        // Network button
        buttonNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkOn) {
                    ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    // Check if network is available
                    NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // The webpage to download is google.fi
                        new DownloadWebpageTask().execute("https://www.google.fi/");
                        networkOn = true;
                    } else {
                        System.out.println("Network failed!");
                    }
                }
            }
        });

        // Vibrator button
        buttonVibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vibratorOn) {
                    // Starts a new thread where the vibrations are handled
                    (new Thread(new VibratorRunnable(context))).start();
                    vibratorOn = true;
                }

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
