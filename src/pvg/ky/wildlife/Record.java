package pvg.ky.wildlife;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
import android.widget.VideoView;

public class Record extends Activity {
	
	


final static int REQUEST_VIDEO_CAPTURED = 1;
private static final int CAPTURE_PICTURE_INTENT = 0;
Uri mCapturedImageURI ;
VideoView videoviewPlay;
Intent intent;
Toast toast;
String email;
String filename ;

/** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
           
   Context context = getApplicationContext();
      CharSequence text = "Please Press the Record button in your Device to Start Recording!!!";
      int duration = Toast.LENGTH_SHORT;
    //Get the bundle


      toast = Toast.makeText(context, text, duration);
      toast.show();

    
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
 
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  
      
      startActivityForResult(intent, CAPTURE_PICTURE_INTENT);
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     
  }
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
     	 Intent intent = new Intent(Record.this, PrincipalActivity.class);
     	 
       startActivity(intent);
    	
          return true;
      }
      return super.onKeyDown(keyCode, event);
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	  super.onActivityResult(requestCode, resultCode, intent);
	  toast.cancel();
	  if (resultCode != RESULT_OK) return;
	  
	  	  try {
	    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(intent.getData(), "r");
	    FileInputStream fis = videoAsset.createInputStream();
	  
	  
	
	 filename = "/photint/2pi/" + String.valueOf("+ Photint +" + System.currentTimeMillis()) +".jpg";
    File tmpFile = new File(Environment.getExternalStorageDirectory(), filename); 
	    FileOutputStream fos = new FileOutputStream(tmpFile);
	
		
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = fis.read(buf)) > 0) {
	        fos.write(buf, 0, len);
	    }       
	    fis.close();
	    fos.close();
	         	
	  } catch (IOException io_e) {
	    // TODO: handle error
	  }
	  	
	  	Intent intent1 = new Intent(Record.this, MailSenderActivity.class);
	  	intent1.putExtra("pvg.twopi.ky.Record", filename);
	  	startActivity(intent1);
	  	this.finishActivity(1);
	  	finish();
	  	
	  
	  	
  }
}

