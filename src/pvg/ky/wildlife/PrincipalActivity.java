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
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
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
  
   
	private RelativeLayout layout,overlay,layoutgallery,faunaover,galleryover;
	private LinearLayout layoutsanctuary,layoutimage,getsocial,fauna,mainview;
	
	
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
        
        Integer[] images = { R.drawable.gallery, R.drawable.gallery2,
                R.drawable.gallery3, R.drawable.gallery4, R.drawable.gallery5,
                R.drawable.gallery6 };
        
       adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();
        
        getSupportActionBar().hide();
        this.listMenu = (ListView) findViewById(R.id.listMenu);
        this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
        this.overlay = (RelativeLayout) findViewById(R.id.top_layout);
        this.faunaover = (RelativeLayout) findViewById(R.id.faunaover);
        faunaover.setVisibility(View.GONE);
        this.galleryover = (RelativeLayout) findViewById(R.id.galleryover);
        galleryover.setVisibility(View.GONE);
        this.layoutsanctuary = (LinearLayout) findViewById(R.id.layout);
        this.layoutgallery = (RelativeLayout) findViewById(R.id.layoutgallery);
        this.fauna = (LinearLayout) findViewById(R.id.fauna);
        this.layoutimage = (LinearLayout) findViewById(R.id.layoutimage);
        this.getsocial = (LinearLayout) findViewById(R.id.getsocial);
        this.mainview = (LinearLayout) findViewById(R.id.mainview);
        this.appName = (TextView) findViewById(R.id.appName);
        
        final ImageView hand = (ImageView) findViewById(R.id.ivInstruction);
        hand.setBackgroundResource(R.drawable.hand);
        final AnimationDrawable frameAnimation = (AnimationDrawable)hand.getBackground();
    	hand.post(new Runnable(){
            @Override
            public void run() {
                frameAnimation.start();                
            }            
        }); 
        
    	
        TextView editText2 = (TextView) findViewById(R.id.editText2);
        editText2.setMovementMethod(new ScrollingMovementMethod());
        editText2.setLineSpacing(1, 2);
              
        String text = "<font color=#625D5D>Wildlife.ae is the official applicaiton of the Ras Al Khor Wildlife Sanctuary or RAKWS.\nRAKWS is one of the few urban protected areas in the world, holding approximately more than 450 species of fauna and 47 species of flora.\nThe sanctuary also boasts various ecosystems from mangroves, mudflats, lagoons and sabkhas to reed beds and shrub lands.\n</font>" +
        		 "<font color=#0099CC><br>UAE joining Ramsar Convention</font>" +
        		"<font color=#625D5D><br>The United Arab Emirates (UAE) has ratified the Ramsar Convention when the UNESCO, the Ramsar Convention's legal depositary, announced that its partnership to the Convention will enter into force on 29 December 2007.</font>" +
        		"<font color=#0099CC><br>What is a Ramsar site?</font>" + 
        		"<font color=#625D5D><br>Ramsar sites are wetlands of international importance designated under the Ramsar Convention. The convention was developed and adopted by contracting nations at a meeting in Ramsar, Iran on February 2, 1971 and came into force on December 21, 1975. In January 2011, The Ramsar List of Wetlands of International Importance included 160 contracting parties covering over 1,900 sites (known as Ramsar sites) with a total area of over 187,984,550 km², up from 1,021 sites.For the latest updates check Ramsar website: www.ramsar.org</font>" +
        		"<font color=#0099CC><br>UAE's first Ramsar site</font>" +
        		"<font color=#625D5D><br>With The UAE's accession, Ras Al Khor Wildlife Sanctuary (RAKWS) has become the Nation's first Ramsar wetland site. RAKWS is located at the head of the 14Km long watercourse known as Dubai Creek, and covers an area of 620 hectares featuring sabkhas saline flats, intertidal mudflats and mangroves, small lagoons and pools, and a few tiny islands which lies at the interface between the Persian Gulf and the Al Awir Desert.During winter, RAKWS supports more than 20,000 water birds of 67 species and acts as a critical staging ground for the wintering birds of the East African-West Asian Flyway. The site hosts more than 500 species of flora and fauna and is one of the best-managed arid zone wetlands in the region. Located within Dubai city, it is an important eco-tourism destination and receives increasing numbers of local and international visitors.The importance of RAKWS is beyond its aesthetic value. It serves as a beacon on the significance of biodiversity as a life supporting system for humanity's continual existence and its being an integral part of the cultural and traditional heritage of the UAE.</font>";
        
        
        editText2.setText(Html.fromHtml(text));
        appName.setOnClickListener(this);
        
       
        this.menuAdapter = new MenuLazyAdapter(this, MenuEventController.menuArray.size() == 0 ? MenuEventController.getMenuDefault(this) : MenuEventController.menuArray);
        this.listMenu.setAdapter(menuAdapter);
     
        
    
      if (isFirstTime()) {
       	overlay.setVisibility(View.VISIBLE);
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
				   head = (TextView)findViewById(R.id.texthead);
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
					
					ImageView imaged = (ImageView) dialog.findViewById(R.id.imageView1);
					imaged.setImageResource(R.drawable.dm);
		 
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.text);
					text.setMovementMethod(new ScrollingMovementMethod());
					text.setText("Visitor entry process:\n\nVisitors must apply to :\nThe Marine Environment & Wildlife Section, \nEnvironment Department, \nDubai Municipality,\nPO Box 67, \nDubai.\n\nTel  +971-4-6066822 / +971-4-6066826\nFax +971-4-7033532\n\nRequest for Permit to Visit Wildlife Sanctuary: Apply only three days prior to visit.\nAt least two working days are required to process the permits.\n\nCREDITS : Photint Venture Group");
					
//					ImageView image = (ImageView) dialog.findViewById(R.id.image);
//					image.setImageResource(R.drawable.dosdonts);
		 
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
						if (isInternetPresent) {Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				        startActivityForResult(intent, CAMERA_PIC_REQUEST);
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
										
				
						dialog.setTitle("Do's & Don't");
						
						
			 
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						
						text.setText("The following practices are prohibited and may result in heavy fines and legal action: \n \n1) Entering the sanctuary, outside the hides, without a permit from Environment Department, Dubai Municipality \n\n2) Parking outside the designated areas\n\n3) Introducing pets or foreign species into the sanctuary\n\n4) Entering beyond the track area\n\n5) Polluting the soil, water, or air of the sanctuary\n\n6) Approaching or harming wildlife\n\n7) Damaging vegetation or geological formations\n\n8) Collecting or transporting plants, animals or parts thereof\n\n9) Producing noise or using noise producing tools/equipment\n\n10) Using firearms, hunting or trapping of any kind");
						
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.dosdonts);
			 
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
						
						
			 
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						text.setText("Local Law No. 11 of 2003 on the ESTABLISHMENT of PROTECTED AREAS in the EMIRATE of DUBAI specifies:\n\n Any work, acts, activities or procedures which may destroy or damage wildlife, marine flora and fauna, and affect the aesthetic standard in protected areas, shall be prohibited; particularly the following:\n\n 1)Hunting, transporting, killing or disturbing marine or other wildlife; or undertake any acts which lead to their destruction.\n2)Hunting, removal or transporting of any creatures or organic materials such as mollusks, coral reefs, rocks or soil for any purpose.\n3)Destroying or transporting plants from the protected areas.\n4)Damaging or disfiguring geological or geographical formations of areas considered to be the habitat of animal or plant species or their proliferation.\n4)Introducing non-indigenous species to the protected areas.\n5)Polluting the soil, water or air of the protected area by any means.\n6)Constructing buildings, structures, roads; using motorized vehicles or practicing any agriculture, industrial or commercial activities in the protected areas, or practice any activity, acts or works in areas surrounding the conservation area unless by a permit from the competent authority in accordance with approved conditions and rules\n\nViolators of rules and regulations can be prosecuted under the Local Law No. 11 of 2003, and local order No. 61 of 1991.");
						
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.dosdonts);
			 
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
						
						
			 
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						text.setText("RAKWS was established in 1985 and was officially declared as a protected area on March 1, 1998 by Local Order No. (2) of 1998. In December 2003, His Highness, the Ruler of Dubai has promulgated Law No. 11 of 2003 on the Establishment of Protected Areas in the Emirate of Dubai giving RAKWS full protection from Dubai Municipality.\nOn August 29, 2007, RAKWS was declared as the UAE's first RAMSAR Site. It was officially accepted on December 29, 2007 where the UAE become the 156th member of the RAMSAR Convention. It is also identified as a globally Important Bird Area (IBA) by Birdlife International and considered an exceptional wetland within UAE.\nThe specific role of Dubai Municipality in environmental conservation is to:\n1)Prepare conservation plans and legislation\n2)Conduct research and studies\n3)Conserve and restore the biodiversity of the Emirate of Dubai\n4)Ensure the protection of plants and animal stocks of actual or potential value to mankind\n5)Help the preservation of species and ecological processes that underline rural productivity and the restoration of ecosystems degraded by unwise land use\n6)Ensure protection of conservation areas in accordance with Law No. 11 of 2003 on protected areas\n7)Enforce laws on the violators\n\nThe main objectives of the RAKWS management are to:\n1)Conserve and improve the biological diversity of coastal and terrestrial ecosystems typical of the intertidal area along the shores of Ras Al Khor\n2)Maintain the essential ecological processes within these systems\n3)Manage their renewable resources sustainably\n4)Undertake studies and research to generate data for better management of the Sanctuary\n5)Protect and restore the faunal and floral diversity, as well as natural abundance of individual species through habitat conservation, management and restoration\n6)Educate the community about natural heritage, general principles of conservation and the sustainable use of biological resources\n7)Develop and implement an ecotourism programme in the wilderness area");
						
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.dosdonts);
			 
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
						
						
			 
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						text.setText("Country/Territory: United Arab Emirates\nAdministrative region(s): Dubai\nCentral coordinates: 55o 20' East 25o 12' North Map\nArea: 620 ha\nAltitude: 0 - 20m\nCriteria: A4iii, B1i\n\nSite description:\nThe head of a 10-km-long tidal creek which penetrates 7 km inland from the Gulf through Dubai city, containing tidal mudflats (maximum low-water extent c.150 ha) and a lagoon with maximum depth of 2 m and tidal range of 1.0-1.5 m. Flat sabkhah surrounds the intertidal area; there is some salt-tolerant scrub above the high-tide line, and some Tamarix in disturbed areas. The intertidal zone supports an abundant invertebrate fauna, but in late 1993 most of the 50-cm-deep top layer of mud was scoured off and a network of channels created, after which most of the area was densely planted with mangrove saplings. There is nutrient enrichment from irrigation run-off and treated sewage effluent.  Seawards of the mudflats the creek has been dredged for shipping, and much is bounded by commercial development. The land is under the authority of the Coastguard, but is owned by the Government of Dubai/His Highness Sheikh Mohammed.\n\nLand-use and percentage cover:\nnature conservation and research: 100%\n\nBirds:See box for key species. The most important mudflat area in the UAE, supporting a more varied assemblage of waterbird species at much higher densities than any other sites in the coastal zone, especially in winter and during passage periods. Important for Broad-billed Sandpiper (Limicola falcinellus); no other significant concentrations have been found elsewhere in the country. Furthermore, the site is well-known for its population of Flamingoes (Phoenicopterus ruber), which is present all year but is largest in winter (January av. max. 1,400, 1989-1992; 2,300 in February 1990, peak 3,100 in January 2011 ); another notable winter visitor is Pacific Golden Plover (Pluvialis fulva) (max. 40). Large numbers of Common Black-headed Gull (Larus ridibundus) also roost in winter (see box).\n\nSpecies	Season	Year	Min	Max	Quality	Criteria\nGrey Plover (Pluvialis squatarola)	breeding	2007-2011	400	1000	good	B1i\nGrey Plover (Pluvialis squatarola)	passage	1992	727	0	good	B1i\nKentish Plover (Charadrius alexandrinus)	breeding	2007-2011	300	1500	good	B1i\nLesser Sand Plover (Charadrius mongolus)	breeding	2007-2011	500	2000	good	B1i\nBar-tailed Godwit (Limosa lapponica)	non-breeding	1990	700	1100	good	B1i\nEurasian Curlew (Numenius arquata)	non-breeding	2007-2011	100	500	good	B1i\nCommon Redshank (Tringa totanus)	breeding	2007-2011	450	1000	good	B1i\nDunlin (Calidris alpina)	non-breeding	2007-2011	400	2400	good	B1i\nBroad-billed Sandpiper (Limicola falcinellus)	non-breeding	2007-2011	400	2500	good	B1i\nLittle Stint (Calidris minuta)	breeding	2007-2011	4000	6000	good	B1i\nBlack-headed Gull (Larus ridibundus)	non-breeding	2007-2011	500	4500	good	B1i\n\nConservation approaches:\nThe Government of Dubai/His Highness Sheikh Mohammed declared the area a Wildlife Sanctuary in 1985, giving protection from interference (only) at his discretion. Thousands of mangrove, Avicennia  sp. saplings were planted in  1991 to 1994. The mangrove forest is steadily flourishing since then. A Mangrove Management Plan is currently being developed to maximize the advantages of the expanding forest which is currently showing signs of encroachment to the mudflats.  Moreover, researches on the biodiversity of the sanctuary are continuously being undertaken.  Lectures are also being conducted (upon request) regarding the sanctuary to schools or any groups interested in the activities being done in the sanctuary.\n\nCitation:\nBirdLife International 2007 BirdLife's online World Bird Database: the site for bird conservation. Version 2.1. Cambridge, UK: BirdLife International. Available: http://www.birdlife.org (accessed 4/7/2007)");
						
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.dosdonts);
			 
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
						
						
			 
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						text.setText("The Ras Al Khor Sanctuary has great plans for the future. A state-of-the-art Visitor's Centre will shortly be launched. The Centre will be complete with a reception and interpretation lounge, wildlife showcase that displays the unique features of the Sanctuary, mangrove boardwalk, live CCTV observation, merchandise and souvenir store, multimedia library and laboratory facilities, administration office and a cafeteria. On completion, this technologically advanced Centre will easily allow visitors to access information and get a closer understanding of the Biological diversity of the Sanctuary. The interpretative services will be a boost to the education, recreation and awareness of the protected area experience. It could also become the Centre for Communication Education and Public Awareness (CEPA) of the Ramsar Convention.");
						
//						ImageView image = (ImageView) dialog.findViewById(R.id.image);
//						image.setImageResource(R.drawable.dosdonts);
			 
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
						
						TextView text = (TextView) dialog.findViewById(R.id.text);
						text.setMovementMethod(new ScrollingMovementMethod());
						text.setText("Visitor entry process:\n\n1) Individuals and families can go directly to the sanctuary and register on the visitors’ logbook.\n2) Group tours, media organizations or affiliates, academic institutions and companies must apply and get an entry permit thru the internet:\n\nFirst , you have to register on the website http://www.dm.gov.ae.\na) If you still do not have a user name and password, follow the steps enumerated below:\nClick on “Register ”\nClick “Company and Admin. User Registration”, then follow procedure of registration;\nAfter registration, print the application form and let the CEO/Owner and Administrative Manager sign the document then submit it to the Information Technology Department, Dubai Municipality;\nAfterwards username and password will be send to the email address written on the application form.\nb) If company already have username and password just “Log-in”.\n\nPermits will be issued electronically and must be printed. A copy of the permit should be given to hide staffs in the sanctuary.\nApply only one week prior to visit. At least two working days are required to process the permits .\nFor any technical assistance in getting permits, comments and complaints please e -mail   to:jcaguhob @dm.gov.ae  mahussain@dm.gov.ae or call: 6066826 ; 6066822");			 
					
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
			
			 mSelected.setText("Order: CHARADRIIFORMES\nFamily: Sandpipers (Scolopacidae)\nLength: 18 cm\nWingspan: 40 cm\nWeight: M/F: 48 g\nWorld Distribution: BREEDS: c&n Europe, Siberia & n North America, WINTERS: south to s Asia, Africa & Mexico\nHabitat: Tundra, moor, heath, on migration estuaries & coasts");
		     image.setImageResource(R.drawable.dunlino);
		     head.setText("Dunlin Calidris alpina (Linnaeus, 1758)");
	       	
	          }else if(tab.getPosition() == 1){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order: FALCONIFORMES\nFamily: Kites, Eagles and Hawks (Accipitridae)\nLength: 56 cm\nWingspan: 158 cm\nWeight: M/F: 1.5 kg\nWorld Distribution: Cosmopolitan, Northern populations generally winter further south\nHabitat: Lakes, rivers, seacoasts");
	     	     image.setImageResource(R.drawable.ospreyo);
	     	     head.setText("Osprey Pandion haliaetus (Linnaeus, 1758)");
		     
	          }else if(tab.getPosition() == 2){
	        	  faunaover.setVisibility(View.INVISIBLE);
	        	  mSelected.setText("Order: CHARADRIIFORMES\nFamily: Laridae\nLength: 38 cm\nWingspan: 100 cm\nWeight: M/F: 250 g\nWorld Distribution: BREEDS: Europe, s North America & n South America, WINTERS: south to s Africa, India & se South America\nHabitat: Sandy seacoasts, in winter estuaries");
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
         overlay.setVisibility(View.VISIBLE);
         overlay.setOnTouchListener(new View.OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		overlay.setVisibility(View.INVISIBLE);
		return false;
		}

		            });
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
