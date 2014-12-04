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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Context context;

    boolean audioOn = false;
    MediaPlayer mp;

    Camera camera;
    boolean cameraOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Button buttonAudio = (Button) findViewById(R.id.buttonAudio);
        Button buttonCamera = (Button) findViewById(R.id.buttonCamera);
        Button buttonCPU = (Button) findViewById(R.id.buttonCPU);
        Button buttonGPS = (Button) findViewById(R.id.buttonGPS);
        Button buttonNetwork = (Button) findViewById(R.id.buttonNetwork);
        Button buttonVibrator = (Button) findViewById(R.id.buttonVibrator);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioOn) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.rickroll);
                    mp.setLooping(true);
                    mp.start();
                    audioOn = true;
                }
                else {
                    mp.stop();
                    audioOn = false;
                }
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cameraOn) {
                    try {
                        camera = Camera.open();
                        Camera.Parameters params = camera.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(params);
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        cameraOn = true;
                    } catch (Exception e) {
                        System.out.println("Camera failed!");
                    }
                }
                else {
                    camera.release();
                    camera = null;
                    cameraOn = false;
                }
            }
        });

        buttonCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new CPURunnable())).start();
                (new Thread(new CPURunnable())).start();
                (new Thread(new CPURunnable())).start();
                (new Thread(new CPURunnable())).start();
            }
        });

        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

            }
        });

        buttonNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute("https://www.google.fi/");
                    try {
                        Thread.sleep(1);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Network failed!");
                }
            }
        });

        buttonVibrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new VibratorRunnable(context))).start();
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
