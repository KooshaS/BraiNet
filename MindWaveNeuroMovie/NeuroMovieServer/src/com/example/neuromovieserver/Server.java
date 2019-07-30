package com.example.neuromovieserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;
 
public class Server extends Activity {
 
	volatile static int flag1 = 0;
	volatile static int flag2 = 0;
	volatile static int flag3 = 0;
	
	volatile static int flag21 = 0;
	volatile static int flag22 = 0;
	
	TextView tv;
	
	private ServerSocket serverSocket1;
	private ServerSocket serverSocket2;
	
    Handler updateConversationHandler; 
    Thread serverThread = null;
    
    public static final int SERVERPORT1 = 6000;
    public static final int SERVERPORT2 = 7000;
  
	                 
    @Override
    public void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        tv = (TextView) findViewById(R.id.textView);
  
        tv.setText("");
        // tv.append("Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n" );
 
        
 
        updateConversationHandler = new Handler();
 
        this.serverThread = new Thread(new ServerThread1());
        this.serverThread.start();
        
        this.serverThread = new Thread(new ServerThread2());
        this.serverThread.start();
        
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        tv.setText("IP: " + ip);
                
        start();
 
    }
    
    public String intToIp(int i) {

 	   return (i & 0xFF) + "." +
 	               ((i >> 8 ) & 0xFF) + "." +
 	               ((i >> 16 ) & 0xFF) + "." +
 	               ((i >> 24 ) & 0xFF); 	   
 	}
        
    
    
    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket1.close();
            serverSocket2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        flag1 = flag2 = flag3 = flag21 = flag22 = 0;
    }
 
    class ServerThread1 implements Runnable {
 
        public void run() {
            Socket socket1 = null;
            
            try {
                
            	serverSocket1 = new ServerSocket(SERVERPORT1);
            	            
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
 
                try {
 
                    socket1 = serverSocket1.accept();
                     
                    CommunicationThread1 commThread1 = new CommunicationThread1(socket1);
                    new Thread(commThread1).start();
                                                            
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 
    class CommunicationThread1 implements Runnable {
 
        private Socket clientSocket;
        private BufferedReader input;
 
        public CommunicationThread1(Socket clientSocket) {
 
            this.clientSocket = clientSocket;
 
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        public void run() {
 
            while (!Thread.currentThread().isInterrupted()) {
 
                try { 
                    String read = input.readLine(); 
                    updateConversationHandler.post(new updateUIThread1(read));
                                                          
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
    }
 
    class updateUIThread1 implements Runnable {
        private String msg;
 
        public updateUIThread1(String str) {
            this.msg = str;
                       
        }
 
        @Override
        public void run() {
        	
            // tv.append(" " + msg + "\n");
            
            if (msg.equals("0")) {            	
            	flag21 = 0;
            	Log.e("Nervous1", msg);
            	            	            	            	            		
            } else if (msg.equals("1")) {
            	flag21 = 1;
            	Log.e("Neutral1", msg);

//            	tv.post(new Runnable() {
//                    public void run() {
//                        // this code is executed on the UI thread.
//                        tv.setText("Neutral");
//                    }
//                });
            }
            
            tv.post(new Runnable() {
                public void run() {
                    // this code is executed on the UI thread.
                    
                	if (flag21 + flag22 == 0) {
                		tv.setText("Nervous");
                	} else {
                		tv.setText("Neutral");
                	}                   	                 	                   	                   	
                }
            });
            
        }        
    }
    
    class ServerThread2 implements Runnable {
    	 
        public void run() {           
            Socket socket2 = null;
            
            try {
                            	
            	serverSocket2 = new ServerSocket(SERVERPORT2);
            
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
 
                try {
                     
                    socket2 = serverSocket2.accept();                 
                    
                    CommunicationThread2 commThread2 = new CommunicationThread2(socket2);
                    new Thread(commThread2).start();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class CommunicationThread2 implements Runnable {
    	 
        private Socket clientSocket;
        private BufferedReader input;
 
        public CommunicationThread2(Socket clientSocket) {
 
            this.clientSocket = clientSocket;
 
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        public void run() {
 
            while (!Thread.currentThread().isInterrupted()) {
 
                try { 
                    String read = input.readLine(); 
                    updateConversationHandler.post(new updateUIThread2(read));
                                                          
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
    }
 
    class updateUIThread2 implements Runnable {
        private String msg;
 
        public updateUIThread2(String str) {
            this.msg = str;
                       
        }
 
        @Override
        public void run() {
        	
            // tv.append(" " + msg + "\n");
            
            if (msg.equals("0")) {            	
            	flag22 = 0;
            	Log.e("Nervous2", msg);
            	
//            	tv.post(new Runnable() {
//                    public void run() {
//                        // this code is executed on the UI thread.
//                        tv.setText("Nervous");
//                    }
//                });
            	            	            		
            } else if (msg.equals("1")) {
            	flag22 = 1;
            	Log.e("Neutral2", msg);

//            	tv.post(new Runnable() {
//                    public void run() {
//                        // this code is executed on the UI thread.
//                        tv.setText("Neutral");
//                    }
//                });
            
            }            
        }        
    }
    
    public void onResume() {
        super.onResume();                
    }

    public void onPause() {
        super.onPause();       
    }
    
    private void start() {
    	
    	final VideoView vid = (VideoView) findViewById(R.id.videoView);
		
		//** Setup paths
				final String video[] = new String[26];
				
				video[0] = "/sdcard/Movies/start.mp4";
				video[1] = "/sdcard/Movies/start.mp4";
				video[2] = "/sdcard/Movies/iceage.mp4";
				video[3] = "/sdcard/Movies/iceage.mp4";
				video[4] = "/sdcard/Movies/start.mp4";
				video[5] = "/sdcard/Movies/start.mp4";				
				video[6] = "/sdcard/Movies/o1.mp4";
				video[7] = "/sdcard/Movies/o1.mp4";
				video[8] = "/sdcard/Movies/o2.mp4";
				video[9] = "/sdcard/Movies/b2.mp4";
				video[10] = "/sdcard/Movies/o3.mp4";
				video[11] = "/sdcard/Movies/b3.mp4";
				video[12] = "/sdcard/Movies/o4.mp4";
				video[13] = "/sdcard/Movies/b4.mp4";
				video[14] = "/sdcard/Movies/o5.mp4";
				video[15] = "/sdcard/Movies/b5.mp4";
				video[16] = "/sdcard/Movies/o6.mp4";
				video[17] = "/sdcard/Movies/b6.mp4";
				video[18] = "/sdcard/Movies/o7.mp4";
				video[19] = "/sdcard/Movies/b7.mp4";
				video[20] = "/sdcard/Movies/o8.mp4";
				video[21] = "/sdcard/Movies/b8.mp4";
				video[22] = "/sdcard/Movies/o9.mp4";
				video[23] = "/sdcard/Movies/b9.mp4";
				video[24] = "/sdcard/Movies/o10.mp4";
				video[25] = "/sdcard/Movies/b10.mp4";
				
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
            	
            	if (flag21 + flag22 == 0) {
            		flag1 = (2 * flag3 + 1) % 26;
            	} else {
            		flag1 = (2 * flag3) % 26;
            	}
            	
        		vPlayer(vid, video);
        		
                }
          });
		
		
		}
    
    
  
}

