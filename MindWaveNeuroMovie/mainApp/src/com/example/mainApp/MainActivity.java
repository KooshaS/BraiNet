package com.example.mainApp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mainApp.R;
import com.neurosky.thinkgear.*;

public class MainActivity extends Activity {
	BluetoothAdapter bluetoothAdapter;
	
	
	private volatile static int flag1 = 0;
	private volatile static int flag2 = 0;
	private volatile static int flag3 = 0;
	TextView tv;
	Button b;
	
	TGDevice tgDevice;
	final boolean rawEnabled = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        tv = (TextView)findViewById(R.id.textView);
        tv.setText("");
        tv.append("Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n" );
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
        	// Alert user that Bluetooth is not available
        	Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }else {
        	/* create the TGDevice */
        	tgDevice = new TGDevice(bluetoothAdapter, handler);
        }
        	
        	doStuff(null);
        	start();
        	          
    }
    
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MULTIMEDIA
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void start() {
    	
    	final VideoView vid = (VideoView) findViewById(R.id.videoView);
		
		//** Setup paths
				final String video[] = new String[20];
				video[0] = "/sdcard/Movies/o1.mp4";
				video[1] = "/sdcard/Movies/b1.mp4";
				video[2] = "/sdcard/Movies/o2.mp4";
				video[3] = "/sdcard/Movies/b2.mp4";
				video[4] = "/sdcard/Movies/o3.mp4";
				video[5] = "/sdcard/Movies/b3.mp4";
				video[6] = "/sdcard/Movies/o4.mp4";
				video[7] = "/sdcard/Movies/b4.mp4";
				video[8] = "/sdcard/Movies/o5.mp4";
				video[9] = "/sdcard/Movies/b5.mp4";
				video[10] = "/sdcard/Movies/o6.mp4";
				video[11] = "/sdcard/Movies/b6.mp4";
				video[12] = "/sdcard/Movies/o7.mp4";
				video[13] = "/sdcard/Movies/b7.mp4";
				video[14] = "/sdcard/Movies/o8.mp4";
				video[15] = "/sdcard/Movies/b8.mp4";
				video[16] = "/sdcard/Movies/o9.mp4";
				video[17] = "/sdcard/Movies/b9.mp4";
				video[18] = "/sdcard/Movies/o10.mp4";
				video[19] = "/sdcard/Movies/b10.mp4";
				
				vPlayer(vid, video);
						
	}
    
    
    
	private void vPlayer(final VideoView vid, final String[] video) {
			
			
			// flag = 0 + (int)(Math.random() * ((2 - 0) + 1));
			vid.setVideoPath(video[flag1]);
			vid.start();
        	vid.requestFocus();

        	
		        	
		vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
            	// flag = MentalState.getValue();
            	
            	flag3++;
            	
            	if (flag2 == 0) {
            		flag1 = (2 * flag3 + 1) % 20;
            	} else {
            		flag1 = (2 * flag3) % 20;
            	}
            	
        		vPlayer(vid, video);
        		
                }
          });
		
		
		}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//DATA ACQUISITION
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public void onDestroy() {
    	tgDevice.close();
    	flag1 = flag2 = flag3 = 0;
        super.onDestroy();
    }
    /**
     * Handles messages from TGDevice
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:

                switch (msg.arg1) {
	                case TGDevice.STATE_IDLE:
	                    break;
	                case TGDevice.STATE_CONNECTING:		                	
	                	tv.append("Connecting...\n");
	                	break;		                    
	                case TGDevice.STATE_CONNECTED:
	                	tv.append("Connected.\n");
	                	tgDevice.start();
	                    break;
	                case TGDevice.STATE_NOT_FOUND:
	                	tv.append("Can't find\n");
	                	break;
	                case TGDevice.STATE_NOT_PAIRED:
	                	tv.append("not paired\n");
	                	break;
	                case TGDevice.STATE_DISCONNECTED:
	                	tv.append("Disconnected mang\n");
                }

                break;
            
       
                
            case TGDevice.MSG_MEDITATION:        		
            	
            	if (msg.arg1 <= 75) {
            		flag2 = 0;
            		tv.setText("Nervous " + "(" + msg.arg1 + ")");
            	} else {
            		flag2 = 1;
            		tv.setText("Neutral " + "(" + msg.arg1 + ")");
            	}            		
            	
            	
                    	
            	// tv.setText(Integer.toString(msg.arg1));            	
            	break;
                        
            default:
            	break;
        }
        }
    };
    
            
    
    public void doStuff(View view) {
    	if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
    		tgDevice.connect(rawEnabled);   
    	//tgDevice.ena
    }
}
