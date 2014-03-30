package pvg.ky.wildlife;


import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;

public class SplashScreen extends Activity {
	protected int _splashTime = 2000; 

	private Thread splashTread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.splash1);

	    
	    final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
        splashImageView.setBackgroundResource(R.drawable.splash);
//        final AnimationDrawable frameAnimation = (AnimationDrawable)splashImageView.getBackground();
//        
//        splashImageView.post(new Runnable(){
//            @Override
//            public void run() {
//                frameAnimation.start();                
//            }            
//        }); 
	    
	    // thread for displaying the SplashScreen
	    splashTread = new Thread() {
	    
	        public void run() {
	        	
	            try {
	            	synchronized(this){
	            		
	            		//wait 5 sec
	            		wait(_splashTime);
	            	}

	            } catch(InterruptedException e) {}
	            finally {
	                finish();

	                //start a new activity
	               
	                Intent intent = new Intent(SplashScreen.this, PrincipalActivity.class);
	                startActivity(intent);
	        		

	                
	            }
	        }
	    };

	    splashTread.start();
	}

	//Function that will handle the touch
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}

}