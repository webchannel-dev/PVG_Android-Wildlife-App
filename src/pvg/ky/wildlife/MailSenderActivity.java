package pvg.ky.wildlife;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Pattern;



import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import javax.net.ssl.SSLContext;

public class MailSenderActivity extends Activity {
	String vidname,html ;
	
	public class load extends AsyncTask<Void, Void, Void> {
//		Dialog progress;
		 @Override
		 protected void onPostExecute(Void result) {
		  // TODO Auto-generated method stub
			  super.onPostExecute(result);
//		        progress.dismiss();
			  Toast.makeText(MailSenderActivity.this, "Image uploaded,successfully", Toast.LENGTH_LONG).show();
			
		 }

		 @Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
		    
//			 progress = ProgressDialog.show(MailSenderActivity.this, 
//		                "Loading Data", "Please wait while the Email is getting configured ....");
//		        super.onPreExecute();
		    
		  
		        
		 }

		 @Override
		 protected void onProgressUpdate(Void... values) {
		  // TODO Auto-generated method stub
		  //super.onProgressUpdate(values);
		 }

		 @Override
		 protected Void doInBackground(Void... arg0) {
		  // TODO Auto-generated method stub
			 boolean tempStatus = false;
	          String desFileName = "";
	          FileInputStream srcFileStream = null;
	          String get ; 
	        get =  getIntent().getExtras().getString("pvg.twopi.ky.Record");
	          
	          FTPClient con = null;

	          try
	          {
	              con = new FTPClient();
	              con.connect("198.38.82.37");

	              if (con.login("ky@photint.com", "kyphotint"))
	              {
	                  con.enterLocalPassiveMode(); // important!
	                  con.setFileType(FTP.BINARY_FILE_TYPE);
	                  String data = get;

	                  FileInputStream in = new FileInputStream(new File(data));
	                  boolean result = con.storeFile("/wildlife/"+System.currentTimeMillis(), in);
	                  in.close();
	                  Toast.makeText(MailSenderActivity.this, "Image uploaded,successfully", Toast.LENGTH_LONG).show();
	                  if (result) Log.v("upload result", "succeeded");
	                  con.logout();
	                  con.disconnect();
	              }
	          }
	          catch (Exception e)
	          {
	              e.printStackTrace();
	          }
	                      
		  return null;
		 }

	}
	
	

    /** Called when the activity is first created. */
		 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new load().execute();
        
       
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (!mWifi.isConnected()) {
        	AlertDialog alertDialog = new AlertDialog.Builder(MailSenderActivity.this).create();

	// Setting Dialog Title
	alertDialog.setTitle("Wildlife");

	// Setting Dialog Message
	alertDialog.setMessage("Please,connect to Internet for using this Application");

	// Setting Icon to Dialog
	alertDialog.setIcon(R.drawable.fail);

	// Setting OK Button
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// Write your code here to execute after dialog closed
			finish();
			}
	});

	// Showing Alert Message
	alertDialog.show();
        	
        }
        finish();
        Intent intent1 = new Intent(this, PrincipalActivity.class);
       
	  	startActivity(intent1);
	  	
            }         
     
        
      
                      
     
  }
   

