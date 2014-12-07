package com.example.patrick.batterydrainer;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Patrick on 4.12.2014.
 */
public class VibratorRunnable implements Runnable {
    private Context context;
    public Vibrator vibrator;

    public VibratorRunnable(Context _context) {
        this.context = _context;
    }
    public void run() {
        // Starts an endless loop of 1ms vibrations
        while(true){
            vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        }
    }
}
