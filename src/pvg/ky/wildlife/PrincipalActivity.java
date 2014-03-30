package pvg.ky.wildlife;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.client.ResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.wildlife.util.MenuEventController;
import com.wildlife.util.MenuLazyAdapter;
import com.wildlife.util.OnSwipeTouchListener;

/**
 * Android activity with menu and layout to add elements of your application
 * @author Leonardo Salles
 */
public class PrincipalActivity extends SherlockActivity implements ActionBar.TabListener,OnTouchListener,OnClickListener{
	public static final String ID = "id";
    public static final String ICON = "icon";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    final static private int NEW_PICTURE = 1;
    private static final int CAMERA_PIC_REQUEST = 1111;
    Bitmap bm;
    File file = new File(Environment.getExternalStorageDirectory()+File.separator +"/Wildlife/"+ System.currentTimeMillis()+".jpg");
  
   
	private RelativeLayout layout,overlay,layoutgallery,faunaover,galleryover,mainview;
	private LinearLayout layoutsanctuary,layoutimage,getsocial,fauna;
	
	
	private MenuLazyAdapter menuAdapter;
	private boolean open = false;
    
	private final Context context = this;
	
	
	TextView paragraph;
	LinearLayout content;
	LinearLayout.LayoutParams contentParams;
	TranslateAnimation slide;
	int marginX, animateFromX, animateToX = 0;
	boolean menuOpen = false;
	ImageAdapter adapter;
    private TextView mSelected,head;
    private ImageView image;

String[] tabs;
String path;
	private ListView listMenu;
	private TextView appName;
	Button dodont,rules,history,factsheet,future,visit,facebook,twitter,blog,forum,pic;

	 private static final String TAG = "Touch";
	    @SuppressWarnings("unused")
	    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

	    // These matrices will be used to scale points of the image
	    Matrix matrix = new Matrix();
	    Matrix savedMatrix = new Matrix();

	    // The 3 states (events) which the user is trying to perform
	    static final int NONE = 0;
	    static final int DRAG = 1;
	    static final int ZOOM = 2;
	    int mode = NONE;

	    // these PointF objects are used to record the point(s) the user is touching
	    PointF start = new PointF();
	    PointF mid = new PointF();
	    float oldDist = 1f;
	   
	    
	    
//gallery def end
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Internetcheck internet = new Internetcheck(getApplicationContext());
        final Boolean isInternetPresent = internet.isConnectingToInternet();
      
        setContentView(R.layout.principal);
        
	    final ImageView principalImageView = (ImageView) findViewById(R.id.menuButton);
	    principalImageView.setBackgroundResource(R.drawable.menuanim);
        final AnimationDrawable menuanimation = (AnimationDrawable)principalImageView.getBackground();
        principalImageView.post(new Runnable(){
            @Override
            public void run() {
                menuanimation.start();                
            }            
        }); 
        
        Integer[] images = { R.drawable.gallery, R.drawable.gallery2,
                R.drawable.gallery3, R.drawable.gallery4, R.drawable.gallery5,
                R.drawable.gallery6 };
        
       adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();
        
        getSupportActionBar().hide();
        this.listMenu = (ListView) findViewById(R.id.listMenu);
        this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
       // this.overlay = (RelativeLayout) findViewById(R.id.top_layout);
        this.faunaover = (RelativeLayout) findViewById(R.id.faunaover);
        faunaover.setVisibility(View.GONE);
        this.galleryover = (RelativeLayout) findViewById(R.id.galleryover);
        galleryover.setVisibility(View.GONE);
        this.layoutsanctuary = (LinearLayout) findViewById(R.id.layout);
        this.layoutgallery = (RelativeLayout) findViewById(R.id.layoutgallery);
        this.fauna = (LinearLayout) findViewById(R.id.fauna);
        this.layoutimage = (LinearLayout) findViewById(R.id.layoutimage);
        this.getsocial = (LinearLayout) findViewById(R.id.getsocial);
        this.mainview = (RelativeLayout) findViewById(R.id.mainview);
        this.appName = (TextView) findViewById(R.id.appName);
        
//        final ImageView hand = (ImageView) findViewById(R.id.ivInstruction);
//        hand.setBackgroundResource(R.drawable.hand);
//        final AnimationDrawable frameAnimation = (AnimationDrawable)hand.getBackground();
//    	hand.post(new Runnable(){
//            @Override
//            public void run() {
//                frameAnimation.start();                
//            }            
//        }); 
//        
//    	
        
        
        
        
        
      WebView editText2 = (WebView) findViewById(R.id.editText2);
      editText2.setVerticalScrollBarEnabled(true);
      editText2.getSettings().setPluginState(WebSettings.PluginState.ON);
      editText2.getSettings().setPluginsEnabled(true);
      editText2.getSettings().setAllowFileAccess(true);
      editText2.getSettings().setJavaScriptEnabled(true);
      editText2.getSettings().setLoadWithOverviewMode(true);
      editText2.getSettings().setUseWideViewPort(true);
      editText2.getSettings().setLoadWithOverviewMode(true);
      editText2.getSettings().setUseWideViewPort(true);
      editText2.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
      editText2.setScrollbarFadingEnabled(true);
     editText2.loadUrl("file:///android_asset/page.html");
 
        
        appName.setOnClickListener(this);
        
       
        this.menuAdapter = new MenuLazyAdapter(this, MenuEventController.menuArray.size() == 0 ? MenuEventController.getMenuDefault(this) : MenuEventController.menuArray);
        this.listMenu.setAdapter(menuAdapter);
     
        
    
      if (isFirstTime()) {
       //	overlay.setVisibility(View.VISIBLE);
       	faunaover.setVisibility(View.GONE);
        galleryover.setVisibility(View.GONE);
        
       
       	
      }
      faunaover.setVisibility(View.GONE);
      galleryover.setVisibility(View.GONE);
              fauna.setVisibility(View.GONE);
        layoutsanctuary.setVisibility(View.GONE);
		layoutgallery.setVisibility(View.GONE);
		layoutimage.setVisibility(View.GONE);
		getsocial.setVisibility(View.GONE);
		mainview.setVisibility(View.VISIBLE);
		
		
		
        this.layout.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeRight() {
                if(!open){
                	open = true;
                	MenuEventController.open(context, layout, appName);
                	MenuEventController.closeKeyboard(context, getCurrentFocus());
                	
                }
            }
            public void onSwipeLeft() {
            	if(open){
            		open = false;
            		MenuEventController.close(context, layout, appName);
            		MenuEventController.closeKeyboard(context, getCurrentFocus());
            		
            	}
            }
            
         
          
        });
        
        this.listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Your intent object is null, you need set a intent to this object, 
				//like in 0 position
				
				if(position == 0){
					
					
					
//					TextView faunatext = (TextView) findViewById(R.id.faunatext);
//			        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/simple.ttf");
//			        faunatext.setTypeface(face);
					 faunaover.setVisibility(View.VISIBLE);
					 galleryover.setVisibility(View.GONE);
					 final ImageView faunaover = (ImageView) findViewById(R.id.faunaimage);
					 faunaover.setBackgroundResource(R.drawable.faunaover);
				        final AnimationDrawable frameAnimation1 = (AnimationDrawable)faunaover.getBackground();
				    	faunaover.post(new Runnable(){
				            @Override
				            public void run() {
				                frameAnimation1.start();                
				            }            
				        }); 
			         faunaover.setOnTouchListener(new View.OnTouchListener(){

			      		@Override
			      		public boolean onTouch(View v, MotionEvent event) {
			      			faunaover.setVisibility(View.GONE);
			      		return false;
			      		}

			      		            });
					getSupportActionBar().show();
					mainview.setVisibility(View.GONE);
					fauna.setVisibility(View.VISIBLE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.GONE);
					layoutimage.setVisibility(View.GONE);
					getsocial.setVisibility(View.GONE);
					content = (LinearLayout)findViewById(R.id.content);
					contentParams = (LinearLayout.LayoutParams)content.getLayoutParams();
					contentParams.width = getWindowManager().getDefaultDisplay().getWidth();
					// Ensures constant width of content during menu sliding
					content.setLayoutParams(contentParams);
				   mSelected = (TextView)findViewById(R.id.text);
				   mSelected.setTypeface(null,Typeface.ITALIC);
				   head = (TextView)findViewById(R.id.texthead);
				   head.setTypeface(null,Typeface.BOLD_ITALIC);
				   image = (ImageView)findViewById(R.id.image);
				   tabs = getResources().getStringArray(R.array.birds);
				   
				TypedArray imgs = getResources().obtainTypedArray(R.array.birdimg);
					getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				  for (int i = 0; i <= 21; i++) {
					        ActionBar.Tab tab = getSupportActionBar().newTab();
					        String name = tabs[i];
					        int d = imgs.getResourceId(i, -1);
					        tab.setText(name);
					        tab.setIcon(d);
					        
					        tab.setTabListener(PrincipalActivity.this);
					        getSupportActionBar().addTab(tab);
					        
					    }
					
			  			
				
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
				 	
				} else if(position == 1){
					galleryover.setVisibility(View.VISIBLE);
					final ImageView galleryover = (ImageView) findViewById(R.id.galleryimage);
					 galleryover.setBackgroundResource(R.drawable.galleryover);
				        final AnimationDrawable frameAnimation2 = (AnimationDrawable)galleryover.getBackground();
				    	galleryover.post(new Runnable(){
				            @Override
				            public void run() {
				                frameAnimation2.start();                
				            }            
				        }); 
				    	galleryover.setOnTouchListener(new View.OnTouchListener(){

			      		@Override
			      		public boolean onTouch(View v, MotionEvent event) {
			      			galleryover.setVisibility(View.GONE);
			      		return false;
			      		}

			      		            });
					

				        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
				        galleryFlow.setAdapter(adapter);
					  
					getSupportActionBar().hide();
					faunaover.setVisibility(View.GONE);
					mainview.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.VISIBLE);
					layoutimage.setVisibility(View.GONE);
					getsocial.setVisibility(View.GONE);
					
					
				     
				        
					 
				      
				        Button seemore = (Button)findViewById(R.id.seemore);
				        

						if(open){
							open = false;
							MenuEventController.close(context, layout, appName);
							MenuEventController.closeKeyboard(context, view);
		            			}
						seemore.setOnClickListener(new OnClickListener() {

							 
							@Override
							public void onClick(View arg0) {
								if (isInternetPresent) {
									
								
								AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);

					        alert.setTitle("Wildlife - Gallery");
					        WebView wv = new WebView(PrincipalActivity.this);

					        wv.loadUrl("http://www.wildlife.ae//en/stills/gallery.php");

					        wv.setWebViewClient(new WebViewClient()
					        {
					            
					            public boolean shouldOverrideUrlLoading(WebView view, String url)
					            {
					                view.loadUrl(url);

					                return true;
					            }
					        });

					        alert.setView(wv);
					        alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
					        {
					            @Override
					            public void onClick(DialogInterface dialog, int id)
					            {
					            }
					        });
					        alert.show();}
								else {
				                    // Internet connection is not present
				                    // Ask user to connect to Internet
				                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
				                }						
								
								}
							
							
							
						
						});
						
						
				        
					
				} else if(position == 2){
									
					getSupportActionBar().hide();
					//if activity is this just close menu before verify if menu is open
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
					mainview.setVisibility(View.GONE);
					faunaover.setVisibility(View.GONE);
					galleryover.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.VISIBLE);
					layoutgallery.setVisibility(View.GONE);
					layoutimage.setVisibility(View.GONE);
					getsocial.setVisibility(View.GONE);
					
					dodont=(Button)findViewById(R.id.button1);
					  rules=(Button)findViewById(R.id.button2);
					    history=(Button)findViewById(R.id.button3);
					  factsheet=(Button)findViewById(R.id.button4);
					   future=(Button)findViewById(R.id.button5);
					    visit=(Button)findViewById(R.id.button6);
					   clicking();
				
					
				} else if(position == 3){
					getSupportActionBar().hide();
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
					galleryover.setVisibility(View.GONE);
					mainview.setVisibility(View.GONE);
					faunaover.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.GONE);
					layoutimage.setVisibility(View.VISIBLE);
					getsocial.setVisibility(View.GONE);
										
					 ImageView view2 = (ImageView) findViewById(R.id.imageshow);
				     view2.setImageResource(R.drawable.terrain);
				     view2.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {

						        ImageView view2 = (ImageView) v;
						        view2.setScaleType(ImageView.ScaleType.MATRIX);
						        float scale;

						        dumpEvent(event);
						        // Handle touch events here...

						        switch (event.getAction() & MotionEvent.ACTION_MASK) 
						        {
						            case MotionEvent.ACTION_DOWN:   // first finger down only
						                                                savedMatrix.set(matrix);
						                                                start.set(event.getX(), event.getY());
						                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
						                                                mode = DRAG;
						                                                break;

						            case MotionEvent.ACTION_UP: // first finger lifted

						            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

						                                                mode = NONE;
						                                                Log.d(TAG, "mode=NONE");
						                                                break;

						            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

						                                                oldDist = spacing(event);
						                                                Log.d(TAG, "oldDist=" + oldDist);
						                                                if (oldDist > 5f) {
						                                                    savedMatrix.set(matrix);
						                                                    midPoint(mid, event);
						                                                    mode = ZOOM;
						                                                    Log.d(TAG, "mode=ZOOM");
						                                                }
						                                                break;

						            case MotionEvent.ACTION_MOVE:

						                                                if (mode == DRAG) 
						                                                { 
						                                                    matrix.set(savedMatrix);
						                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
						                                                } 
						                                                else if (mode == ZOOM) 
						                                                { 
						                                                    // pinch zooming
						                                                    float newDist = spacing(event);
						                                                    Log.d(TAG, "newDist=" + newDist);
						                                                    if (newDist > 5f) 
						                                                    {
						                                                        matrix.set(savedMatrix);
						                                                        scale = newDist / oldDist; // setting the scaling of the
						                                                                                    // matrix...if scale > 1 means
						                                                                                    // zoom in...if scale < 1 means
						                                                                                    // zoom out
						                                                        matrix.postScale(scale, scale, mid.x, mid.y);
						                                                    }
						                                                }
						                                                break;
						        }

						        view2.setImageMatrix(matrix); // display the transformation on screen

						        return true; // indicate event was handled
						    
							}
					    	
					  
					    
					    });
				    
				} else if(position == 4){
					getSupportActionBar().hide();
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
					galleryover.setVisibility(View.GONE);
					mainview.setVisibility(View.GONE);
					faunaover.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.GONE);
					getsocial.setVisibility(View.GONE);
					layoutimage.setVisibility(View.VISIBLE);
					
					ImageView view2 = (ImageView) findViewById(R.id.imageshow);
				     view2.setImageResource(R.drawable.hides);
				     view2.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {

						        ImageView view2 = (ImageView) v;
						        view2.setScaleType(ImageView.ScaleType.MATRIX);
						        float scale;

						        dumpEvent(event);
						        // Handle touch events here...

						        switch (event.getAction() & MotionEvent.ACTION_MASK) 
						        {
						            case MotionEvent.ACTION_DOWN:   // first finger down only
						                                                savedMatrix.set(matrix);
						                                                start.set(event.getX(), event.getY());
						                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
						                                                mode = DRAG;
						                                                break;

						            case MotionEvent.ACTION_UP: // first finger lifted

						            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

						                                                mode = NONE;
						                                                Log.d(TAG, "mode=NONE");
						                                                break;

						            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

						                                                oldDist = spacing(event);
						                                                Log.d(TAG, "oldDist=" + oldDist);
						                                                if (oldDist > 5f) {
						                                                    savedMatrix.set(matrix);
						                                                    midPoint(mid, event);
						                                                    mode = ZOOM;
						                                                    Log.d(TAG, "mode=ZOOM");
						                                                }
						                                                break;

						            case MotionEvent.ACTION_MOVE:

						                                                if (mode == DRAG) 
						                                                { 
						                                                    matrix.set(savedMatrix);
						                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
						                                                } 
						                                                else if (mode == ZOOM) 
						                                                { 
						                                                    // pinch zooming
						                                                    float newDist = spacing(event);
						                                                    Log.d(TAG, "newDist=" + newDist);
						                                                    if (newDist > 5f) 
						                                                    {
						                                                        matrix.set(savedMatrix);
						                                                        scale = newDist / oldDist; // setting the scaling of the
						                                                                                    // matrix...if scale > 1 means
						                                                                                    // zoom in...if scale < 1 means
						                                                                                    // zoom out
						                                                        matrix.postScale(scale, scale, mid.x, mid.y);
						                                                    }
						                                                }
						                                                break;
						        }

						        view2.setImageMatrix(matrix); // display the transformation on screen

						        return true; // indicate event was handled
						    
							}
					    	
					  
					    
					    });
					
				} else if(position == 5){
					getSupportActionBar().hide();
					galleryover.setVisibility(View.GONE);
					mainview.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					faunaover.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.GONE);
					layoutimage.setVisibility(View.GONE);
					getsocial.setVisibility(View.VISIBLE);
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
					facebook=(Button)findViewById(R.id.facebook);
					  twitter=(Button)findViewById(R.id.twitter);
					    blog=(Button)findViewById(R.id.blog);
					  forum=(Button)findViewById(R.id.forum);
					  pic=(Button)findViewById(R.id.takeapic);
					  getsocial();
					  
				} 
				else if(position == 6){
					getSupportActionBar().hide();
					galleryover.setVisibility(View.GONE);
					mainview.setVisibility(View.GONE);
					faunaover.setVisibility(View.GONE);
					fauna.setVisibility(View.GONE);
					layoutsanctuary.setVisibility(View.GONE);
					layoutgallery.setVisibility(View.GONE);
					layoutimage.setVisibility(View.GONE);
					getsocial.setVisibility(View.GONE);
					if(open){
						open = false;
						MenuEventController.close(context, layout, appName);
						MenuEventController.closeKeyboard(context, view);
	            			}
					
					// custom dialog
					final Dialog dialog = new Dialog(PrincipalActivity.this);
					
									dialog.setContentView(R.layout.dialog);
									
			
					dialog.setTitle("Contact & Credit");
					dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
					ImageView imaged = (ImageView) dialog.findViewById(R.id.imageView1);
					imaged.setImageResource(R.drawable.dm);
		 
					// set the custom dialog components - text, image and button
					WebView text = (WebView) dialog.findViewById(R.id.text);
					text.loadData(getString(R.string.contact), "text/html", "utf-8");
					text.setBackgroundColor(0);
					text.setBackgroundResource(R.drawable.dialogback);
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					
					
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
							Intent intent= new Intent(context, PrincipalActivity.class);
							startActivity(intent);
							
							
						}
					});
		 
					dialog.show();
					
					
					
				} 
				
							}

			private void getsocial() {

				facebook.setOnClickListener(new OnClickListener() {

					 
					@Override
					public void onClick(View arg0) {
						if (isInternetPresent) {
							
						
						AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);

			        alert.setTitle("Wildlife - FaceBook");
			        WebView wv = new WebView(PrincipalActivity.this);

			        wv.loadUrl("https://www.facebook.com/wildlife.ae");

			        wv.setWebViewClient(new WebViewClient()
			        {
			            
			            public boolean shouldOverrideUrlLoading(WebView view, String url)
			            {
			                view.loadUrl(url);

			                return true;
			            }
			        });

			        alert.setView(wv);
			        alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int id)
			            {
			            }
			        });
			        alert.show();}
						else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
		                }						
						
						}
					
					
					
				
				});
				
				
				
				twitter.setOnClickListener(new OnClickListener() {

					 
					@Override
					public void onClick(View arg0) {
						if (isInternetPresent) {
							
						
						AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);

			        alert.setTitle("Wildlife - Twitter");
			        WebView wv = new WebView(PrincipalActivity.this);

			        wv.loadUrl("https://twitter.com/DMunicipality");

			        wv.setWebViewClient(new WebViewClient()
			        {
			            
			            public boolean shouldOverrideUrlLoading(WebView view, String url)
			            {
			                view.loadUrl(url);

			                return true;
			            }
			        });

			        alert.setView(wv);
			        alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int id)
			            {
			            }
			        });
			        alert.show();}
						else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
		                }						
						
						}
					
					
					
				
				});
			
				
				blog.setOnClickListener(new OnClickListener() {

					 
					@Override
					public void onClick(View arg0) {
						if (isInternetPresent) {
							
						
						AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);

			        alert.setTitle("Wildlife - Blog");
			        WebView wv = new WebView(PrincipalActivity.this);

			        wv.loadUrl("http://www.wildlife.ae/new/get-social/blog");

			        wv.setWebViewClient(new WebViewClient()
			        {
			            
			            public boolean shouldOverrideUrlLoading(WebView view, String url)
			            {
			                view.loadUrl(url);

			                return true;
			            }
			        });

			        alert.setView(wv);
			        alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int id)
			            {
			            }
			        });
			        alert.show();}
						else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
		                }						
						
						}
					
					
					
				
				});
				forum.setOnClickListener(new OnClickListener() {

					 
					@Override
					public void onClick(View arg0) {
						if (isInternetPresent) {
							
						
						AlertDialog.Builder alert = new AlertDialog.Builder(PrincipalActivity.this);

			        alert.setTitle("Wildlife - Forum");
			        WebView wv = new WebView(PrincipalActivity.this);

			        wv.loadUrl("http://www.wildlife.ae/new/get-social/forum/");

			        wv.setWebViewClient(new WebViewClient()
			        {
			            
			            public boolean shouldOverrideUrlLoading(WebView view, String url)
			            {
			                view.loadUrl(url);

			                return true;
			            }
			        });

			        alert.setView(wv);
			        alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int id)
			            {
			            }
			        });
			        alert.show();}
						else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
		                }						
						
						}
					
					
					
				
				});
				
				pic.setOnClickListener(new OnClickListener() {

					 
					@Override
					public void onClick(View arg0) {
						
						
						
						
						if (isInternetPresent) {
					
							Intent intent1 = new Intent(PrincipalActivity.this, Record.class);
				    	  	
				    	  	startActivity(intent1);
				    	  	
				    	  	finish();
				        }
						else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(PrincipalActivity.this, "No Internet Connection", "you need internet connection to view this!", false);
		                }						
						
						}
					
					
					
				
				});
				
			}

			
			
			
			private void clicking() {
				dodont.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
				
					dialog.setTitle("Do's & Don't");
						
				
						
						
			 
						// set the custom dialog components - text, image and button
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.dosdont), "text/html", "utf-8");
				
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
			 
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();
						
						
									}
					
				});
				

				rules.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										
				
						dialog.setTitle("Rules & Regulations");
						dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
						
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.rules), "text/html", "utf-8");
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();
						
						
									}
					
				});
				
			
				history.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										
				
						dialog.setTitle("History & Management");
						dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
						
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.history), "text/html", "utf-8");
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();
						
						
									}
					
				});
				factsheet.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										
				
						dialog.setTitle("Sanctuary Factsheet");
						dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.factsheet), "text/html", "utf-8");
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();
						
						
									}
					
				});
				future.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										
				
						dialog.setTitle("Future Of Sanctuary");
						
						dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
			 
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.future), "text/html", "utf-8");
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();
						
						
									}
					
				});
				visit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
						// custom dialog
						final Dialog dialog = new Dialog(PrincipalActivity.this);
						
										dialog.setContentView(R.layout.dialog);
										
				
						dialog.setTitle("Plan a visit");
						dialog.getWindow().setBackgroundDrawableResource(R.color.icsdarkblue);
						WebView text = (WebView) dialog.findViewById(R.id.text);
						text.loadData(getString(R.string.plan), "text/html", "utf-8");
						text.setBackgroundColor(0);
						text.setBackgroundResource(R.drawable.dialogback);
						Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			 
						dialog.show();						
						
									}					
					
				});
			
			}
			 
		});
    }

    public void openCloseMenu(View view){
    	if(!this.open){
    		this.open = true;
    		MenuEventController.open(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
    		
    	} else {
    		this.open = false;
    		MenuEventController.close(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
    	}
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		

		 if(tab.getPosition()  == 0)   {
			
			 mSelected.setText("Order:\t\t CHARADRIIFORMES\nFamily:\t\t Sandpipers (Scolopacidae)\nLength:\t\t 18 cm\nWingspan:\t 40 cm\nWeight:\t\t M/F: 48 g\nWorld Distribution:\t BREEDS: c&n Europe, Siberia & n North America, WINTERS: south to s Asia, Africa & Mexico\nHabitat:\t\t Tundra, moor, heath, on migration estuaries & coasts");
		     image.setImageResource(R.drawable.dunlino);
		     head.setText("Dunlin Calidris alpina (Linnaeus, 1758)");
	       	
	          }else if(tab.getPosition() == 1){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order:\t\t FALCONIFORMES\nFamily: Kites, Eagles and Hawks (Accipitridae)\nLength: 56 cm\nWingspan: 158 cm\nWeight: M/F: 1.5 kg\nWorld Distribution: Cosmopolitan, Northern populations generally winter further south\nHabitat: Lakes, rivers, seacoasts");
	     	     image.setImageResource(R.drawable.ospreyo);
	     	     head.setText("Osprey Pandion haliaetus (Linnaeus, 1758)");
		     
	          }else if(tab.getPosition() == 2){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order:\t\t CHARADRIIFORMES\nFamily: Laridae\nLength: 38 cm\nWingspan: 100 cm\nWeight: M/F: 250 g\nWorld Distribution: BREEDS: Europe, s North America & n South America, WINTERS: south to s Africa, India & se South America\nHabitat: Sandy seacoasts, in winter estuaries");
	      	     image.setImageResource(R.drawable.sandwicho);
	      	     head.setText("Sandwich Tern Sterna sandvicensis (Latham, 1787)");
	          }else if(tab.getPosition() == 3){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order: CICONIIFORMES\nFamily: Bitterns, Herons and Egrets (Ardeidae)\nLength: 60 cm\nWingspan: 92 cm\nWeight: M/F: 450 g\nWorld Distribution: Local throughout s,c Europe, Asia Australasia and Africa\nHabitat: Lakes, marsh, flooded fields & estuary");
	       	     image.setImageResource(R.drawable.littleegreto);
	       	     head.setText("Little Egret Egretta garzetta (Linnaeus, 1766)");
	       }
	          else if(tab.getPosition() == 4){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order: CHARADRIIFORMES\nFamily: Sandpipers (Scolopacidae)\nLength: 16 cm\nWingspan: 38 cm\nWeight: M/F: 40 g\nWorld Distribution: BREEDS: n Scandinavia, Siberia, WINTERS: s Asia to Australia\nHabitat: Tundra, on migration mudflats, marshes");
	       	     image.setImageResource(R.drawable.broadbilledo);
	       	     head.setText("Broad-billed Sandpiper Limicola falcinellus (Pontoppidan, 1763)");
	       }
	          else if(tab.getPosition() == 5){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order: CHARADRIIFORMES\nFamily: Laridae\nLength: 36 cm\nWingspan: 105 cm\nWeight: M: 330 g F: 250 g\nWorld Distribution: BREEDS: Europe, n&c Asia, ne North America, WINTERS: south to c Africa, s Asia, e US\nHabitat: Lakes, rivers, moors, grassland, coasts");
	       	     image.setImageResource(R.drawable.blackheado);
	       	     head.setText("Black-headed Gull Larus ridibundus (Linnaeus, 1766)");
	       }else if(tab.getPosition() == 6){
	    	   faunaover.setVisibility(View.INVISIBLE);
	     	  mSelected.setText("Order: Pelecaniformes\nFamily: Phoenicopteridae\nLength: 135 cm\nWingspan: 150 cm\nWeight: M: 3.6 kg F: 2.7 kg\nWorld Distribution: Local in s Europe & Asia, e&s Africa\nHabitat: Salt lakes & brackish shallow lagoons");
	    	     image.setImageResource(R.drawable.greaterflamingoo);
	    	     head.setText("Greater Flamingo Phoenicopterus ruber (Linnaeus, 1758)");
	    }else if(tab.getPosition() == 7){
	    	faunaover.setVisibility(View.INVISIBLE);
	  	  mSelected.setText("Order: Ciconiiformes\nFamily: Threskiornithidae\nLength: 85 cm\nWingspan: 122 cm\nWeight: 450 g\nWorld Distribution: BREEDS: local in s Europe, c&s Asia & n Africa, WINTERS: south to Mediterranean, s Asia & c Africa\nHabitat: Lakes, marshes, swamps & mudflats");
		     image.setImageResource(R.drawable.spoonbillo);
		     head.setText("Spoonbill Platalea leucorodia (Linnaeus, 1758)");
	}else if(tab.getPosition() == 8){
		faunaover.setVisibility(View.INVISIBLE);
		  mSelected.setText("Order: Charadriiformes\nFamily: Recurvirostridae\nLength: 38 cm\nWingspan: 75 cm\nWeight: M/F: 180\nWorld Distribution: c & se Europe, c & s Asia, Africa\nHabitat: Marsh, weedy lakes, flooded fields\n");
	     image.setImageResource(R.drawable.blackwingedslito);
	     head.setText("Black-winged Stilt Himantopus himantopus (Linnaeus, 1758)");
	}else if(tab.getPosition() == 9){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Charadriiformes\nFamily: Scolopacidae\nLength: 28 cm\nWingspan: 62 cm\nWeight: M: 110 g F: 130 g\nWorld Distribution: BREEDS: Europe, n&c Asia, WINTERS: to s Africa & Indonesia\nHabitat: Rivers, wet grassland, moors & estuaries");
	  image.setImageResource(R.drawable.redshanko);
	     head.setText("(Common) Redshank Tringa totanus (Linnaeus, 1758)");
	}else if(tab.getPosition() == 10){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Charadriiformes\nFamily: Scolopacidae\nLength: 32 cm\nWingspan: 69 cm\nWeight: M: 190 g\nWorld Distribution: BREEDS: n Eurasia, WINTERS: s Europe, s Asia, Africa & Australia\nHabitat: Marsh, wet grassland, moor, on migration mudflats");
	     image.setImageResource(R.drawable.greenshanko);
	     head.setText("(Common) Greenshank Tringa nebularia (Gunnerus, 1767)");
	}else if(tab.getPosition() == 11){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Ciconiiformes\nFamily: Ardeidae\nLength: 50 cm\nWingspan: 93 cm\nWeight: M/F: 350 g\nWorld Distribution: s Europe, Africa, s,e Asia, Australasia, Americas\nHabitat: Wet fields, marsh and pasture, often with grazing animals");
	     image.setImageResource(R.drawable.cattleegreto);
	     head.setText("Cattle Egret Bubulcus ibis (Linnaeus, 1758)");
	}else if(tab.getPosition() == 12){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Charadriiformes\nFamily: Scolopacidae\nLength: 55 cm\nWingspan: 90cm\nWeight: M: 770 g F: 1000 g\nWorld Distribution: BREEDS: n&c Eurasia, WINTERS: S Europe, S Asia, Africa & Indonesia\nHabitat: Marsh, grassland, on migration mudflats");
	     image.setImageResource(R.drawable.curlewo);
	     head.setText("(Eurasian) Curlew Numenius arquata (Linnaeus, 1758)");
	}else if(tab.getPosition() == 13){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Podicipediformes\nFamily: Podicipedidae\nLength: 31 cm\nWingspan: 58cm\nWeight: M: 360 g F: 260 g\nWorld Distribution: BREEDS: local in n Europe, n&c Asia, e&s Africa, w North America, WINTERS: south to s Europe, India & n Central America\nHabitat: Large reedy lakes, Winter also coastal");
	     image.setImageResource(R.drawable.blackneckedgrebeo);
	     head.setText("Black-necked Grebe Podiceps nigricollis (CL Brehm, 1831)");
	}else if(tab.getPosition() == 14){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Ciconiiformes\nFamily: Ardeidae\nLength: 94 cm\nWingspan: 155 cm\nWeight: M/F: 870 g\nWorld Distribution: From e Europe through s&c Asia to Australasia, e,s Africa and Americas from c US south to s South America\nHabitat: Marshes, reed-bed, lakes & rivers");
	     image.setImageResource(R.drawable.greatwhiteegreto);
	     head.setText("Great (White) Egret Ardea alba (Linnaeus, 1758)");
	}else if(tab.getPosition() == 15){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: PASSERIFORMES\nFamily: PYCNONOTIDAE\nLength: 18 cm\nWingspan: 20-25 cm\nWeight: 23-60 g\nWorld Distribution: Widespread and common, almost everywhere in Africa and south of 20oN, except in dry southwest and the Cape\nHabitat: Any wooded or bushy habitat, especially near water");
	     image.setImageResource(R.drawable.plovero);
	     head.setText("Plover Pycnonotus barbatus (Desfontaine, 1789)");
	}else if(tab.getPosition() == 16){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Leptocoma zeylonica\nFamily: Passeriformes\nLength: 10 cm\nWingspan: 20-25 cm\nWeight: 7-11 g\nWorld Distribution: Purple-rumped Sunbird is a common resident breeder in tropical southern Asia in India, Srilanka, Bangladesh and Myanmar\nHabitat: Deciduous forest, thorn-scrub, farmland and gardens up to 7,875 ft (2,400 m) in hills.");
	     image.setImageResource(R.drawable.purplesunbirdo);
	     head.setText("Purple sunbird Cinnyris asiaticus (Latham, 1790)");
	}else if(tab.getPosition() == 17){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Dromadidae\nFamily: Charadriiformes\nLength: 40 cm\nWingspan: -\nWeight: 230-330 g\nWorld Distribution: Andaman Islands, Srilanka, Tanzania, Madagascar, Persian Gulf, Red Sea and Somalia\nHabitat: Coastlines, Lagoons, Reefs, Mudflats");
	     image.setImageResource(R.drawable.crabplovero);
	     head.setText("Crab Plover Dromas ardeola (Paykull, 1805)");
	}else if(tab.getPosition() == 18){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Charadriidae\nFamily: Charadriiformes\nLength: 35 cm\nWingspan: -\nWeight: -\nWorld Distribution: Iraq eastwards across tropical Asia\nHabitat: Marshes and similar freshwater wetland habitats.\n");
	     image.setImageResource(R.drawable.redwattledo);
	     head.setText("Red-wattled Lapwing Vanellus indicus (Boddaert, 1783)");
	}else if(tab.getPosition() == 19){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Ciconiiformes\nFamily: Threskiornithidae\nLength: 60 cm\nWingspan: 80 cm\nWeight: M/F: 630 g\nWorld Distribution: Local in s Eurasia, Africa, Australia, s US & c America\nHabitat: Lakes, marshes & swamps");
	     image.setImageResource(R.drawable.glossyibiso);
	     head.setText("Glossy lbis Plegadis falcinellus (Linnaeus, 1766)");
	}else if(tab.getPosition() == 20){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Charadriiformes\nFamily: Scolopacidae\nLength: 42 cm\nWingspan: 72 cm\nWeight: M: 280 g   F: 340 g\nWorld Distribution: BREEDS: n Europe & n Asia WINTERS: w & s Europe, s Asia south to s Africa & Australia\nHabitat: Marshy grassland & steppe, on migration mudflats");
	     image.setImageResource(R.drawable.blacktailedo);
	     head.setText("Black-tailed Godwit Limosa limosa (Linnaeus, 1758)");
	}else if(tab.getPosition() == 21){
		faunaover.setVisibility(View.INVISIBLE);
	  mSelected.setText("Order: Phalacrocoracidae\nFamily: Scolopacidae\nLength: 80 cm\nWingspan: 102-110 cm\nWeight: 2500 g\nWorld Distribution: Islands off the Persian Gulf coasts of Bahrain, United Arab Emirates (UAE), Saudi Arabia, Qatar & Iran\nHabitat: Shaalo seas and rocky shores");
	     image.setImageResource(R.drawable.socotrao);
	     head.setText("Socotra Cormorant Phalacrocorax nigrogularis (Ogilvie-Grant and Forbes, 1899)");
	}

		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		
	}

	  /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) 
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) 
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) 
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) 
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
	 private boolean isFirstTime()
 	{
     SharedPreferences preferences = getPreferences(MODE_PRIVATE);
     boolean ranBefore = preferences.getBoolean("RanBefore", false);
// if (!ranBefore) {

         SharedPreferences.Editor editor = preferences.edit();
         editor.putBoolean("RanBefore", true);
         editor.commit();
//         overlay.setVisibility(View.VISIBLE);
//         overlay.setOnTouchListener(new View.OnTouchListener(){
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//		overlay.setVisibility(View.INVISIBLE);
//		return false;
//		}
//
//		            });
         faunaover.setOnTouchListener(new View.OnTouchListener(){

     		@Override
     		public boolean onTouch(View v, MotionEvent event) {
     		faunaover.setVisibility(View.INVISIBLE);
     		return false;
     		}

     		            });
         galleryover.setOnTouchListener(new View.OnTouchListener(){

     		@Override
     		public boolean onTouch(View v, MotionEvent event) {
     		galleryover.setVisibility(View.INVISIBLE);
     		return false;
     		}

     		            });


//    }
		return ranBefore;

		}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onClick(View paramView) {
		
		Intent intent= new Intent(context, PrincipalActivity.class);
		startActivity(intent);
		
		
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
        	String filepath;
            //2
          Bitmap thumbnail = (Bitmap) data.getExtras().get("data");  
           // mImage.setImageBitmap(thumbnail);
            //3
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
           thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //4
           
           
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

          
            doFileUpload();
          

           String path = file.toString();
            Intent intent1 = new Intent(PrincipalActivity.this, MailSenderActivity.class);
    	  	intent1.putExtra("pvg.twopi.ky.Record", path);
    	  	startActivity(intent1);
    	  	this.finishActivity(1);
    	  	finish();
        }
       
	  	
    }
	private void doFileUpload(){
		

		  // TODO Auto-generated method stub
			 boolean tempStatus = false;
	          String desFileName = "";
	          FileInputStream srcFileStream = null;
	          String get ; 
	       
	          
	          FTPClient con = null;

//	          try
//	          {
//	              con = new FTPClient();
//	              con.connect("198.38.82.37");
//
//	              if (con.login("ky@photint.com", "kyphotint"))
//	              {
//	                  con.enterLocalPassiveMode(); // important!
//	                  con.setFileType(FTP.BINARY_FILE_TYPE);
//	                  String data = file.toString();
//
//	                  FileInputStream in = new FileInputStream(new File(data));
//	                  boolean result = con.storeFile("/wildlife/"+ System.currentTimeMillis()+".jpg" , in);
//	                  in.close();
//	                  if (result) Log.v("upload result", "succeeded");
//	                  con.logout();
//	                  con.disconnect();
//	              }
//	          }
//	          catch (Exception e)
//	          {
//	              e.printStackTrace();
//	          }
	                      
		 
		 

       
	}
	

}
