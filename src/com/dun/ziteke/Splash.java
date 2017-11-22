/**
 * 
 */
package com.dun.ziteke;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;


/**
 * @author Duncan
 *
 */
public class Splash extends Activity {
	
	String TAG = "SPLASH FORM";
    protected boolean _active = true;
    protected int _splashTime = 5000;
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {                	
                
                    
                    startActivity(new Intent("com.dun.ziteke.Splash.Login"));
                    Splash.this.finish();
					// stop();
                }
            }
        };
        splashTread.start();
    }


}
