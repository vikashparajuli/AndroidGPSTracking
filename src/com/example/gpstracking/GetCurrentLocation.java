package com.example.gpstracking;

import java.io.IOException;  
import java.text.BreakIterator;
import java.util.List;  
import java.util.Locale;  

import com.example.db.SqlHandler;

import android.app.Activity;  
import android.app.AlertDialog;  
import android.content.ContentResolver;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;  
import android.content.pm.ActivityInfo;  
import android.location.Address;  
import android.location.Geocoder;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.provider.Settings;  
import android.util.Log;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.ProgressBar;  
  
public class GetCurrentLocation extends Activity implements OnClickListener {  
   
 private LocationManager locationMangaer=null;  
 private LocationListener locationListener=null;   
   
 private Button btnGetLocation = null;  
 private Button btnEnd = null;
 private EditText editLocation = null;   
 private ProgressBar pb =null;  
   
 private static final String TAG = "Debug";  
 private Boolean flag = false;  
 
 SqlHandler sqlHandler;
  
 @Override  
 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);  
	 setContentView(R.layout.main);  
    
    
	 //if you want to lock screen for always Portrait mode    
	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
	 
	 pb = (ProgressBar) findViewById(R.id.progressBar1);  
	 pb.setVisibility(View.INVISIBLE);  
    
	 editLocation = (EditText) findViewById(R.id.editTextLocation);   
  
	 btnGetLocation = (Button) findViewById(R.id.btnLocation);  
	 btnEnd = (Button)findViewById(R.id.btnEnd);
  
	 btnGetLocation.setOnClickListener(this);
	 btnEnd.setOnClickListener(this);
    
	 locationMangaer = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	 }  
  
 	@Override  
 	public void onClick(View v) {
 		
 		switch(v.getId()){
 			case R.id.btnLocation:
	 
 			flag = displayGpsStatus();  
 			if (flag) {  
     
 				Log.v(TAG, "onClick");    
 				editLocation.setText("Please!! move your device to see the changes in coordinates."+"\nWait..");  
 				pb.setVisibility(View.VISIBLE);  
 				locationListener = new MyLocationListener();  
 				locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,locationListener);  
 			} else {  
 				alertbox("Gps Status!!", "Your GPS is: OFF");  
 			}  
	
 			break;
 			case R.id.btnEnd:
 			
 				Intent i = new Intent("android.intent.action.RESULT");
 				startActivity(i);
 				break;
 				
 				default:
 					break;
		 
 		}
 	}  
  
 /*----Method to Check GPS is enable or disable ----- */  
 private Boolean displayGpsStatus() {  
  ContentResolver contentResolver = getBaseContext().getContentResolver();  
  boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,LocationManager.GPS_PROVIDER);  
  if (gpsStatus) {  
   return true;  
  
  } else {  
   return false;  
  }  
 }  
  
 /*----------Method to create an AlertBox ------------- */  
 protected void alertbox(String title, String mymessage) {  
  AlertDialog.Builder builder = new AlertDialog.Builder(this);  
  builder.setMessage("Your Device's GPS is Disable")  
  .setCancelable(false)  
  .setTitle("** Gps Status **")  
  .setPositiveButton("Gps On",  
   new DialogInterface.OnClickListener() {  
   public void onClick(DialogInterface dialog, int id) {  
   // finish the current activity  
   // AlertBoxAdvance.this.finish();  
   Intent myIntent = new Intent(  
   Settings.ACTION_SETTINGS);  
   startActivity(myIntent);  
      dialog.cancel();  
   }  
   })  
   .setNegativeButton("Cancel",  
   new DialogInterface.OnClickListener() {  
   public void onClick(DialogInterface dialog, int id) {  
    // cancel the dialog box  
    dialog.cancel();  
    }  
   });  
  AlertDialog alert = builder.create();  
  alert.show();  
 }  
   
 /*----------Listener class to get coordinates ------------- */  
 private class MyLocationListener implements LocationListener {  
        @Override  
        public void onLocationChanged(Location loc) {  
            
            editLocation.setText("");  
            pb.setVisibility(View.INVISIBLE);  
            String longitude = ""+loc.getLongitude();    
            Log.v(TAG, longitude);  
            String latitude = ""+loc.getLatitude();  
            Log.v(TAG, latitude);
            String altitude = ""+loc.getAltitude();
            Log.v(TAG, altitude);
            
//            Toast.makeText(getApplicationContext(), "Your Location:\nLatitude: "+latitude+"\nLongitude: "+longitude, Toast.LENGTH_LONG).show();
            
    /*----------to get City-Name from coordinates ------------- */  
      String cityName=null;                
      Geocoder gcd = new Geocoder(getBaseContext(),Locale.getDefault());               
      List<Address>  addresses;    
      try {    
      addresses = gcd.getFromLocation(loc.getLatitude(), loc  
   .getLongitude(), 1);    
      if (addresses.size() > 0)    
         System.out.println(addresses.get(0).getLocality());    
         cityName=addresses.get(0).getLocality();    
        } catch (IOException e) {              
        e.printStackTrace();    
      }   
            
      String s = latitude +longitude+"\n"
    		  +"\n\nMy Currrent City is: "+cityName;  
      			editLocation.setText("Latitude: "+latitude+"\nLongitude: "+longitude+"\nAltitude: "+altitude);
      			
      			try {
        			sqlHandler = new SqlHandler(getApplicationContext());
            	  	String query = "INSERT INTO moveon(altitude) values ('"+ altitude+ "')";
						
            	  	sqlHandler.executeQuery(query);
						Log.d("SQLView","============== Altitude inserted in Sqlite database ==============");
        		  	
                    } catch (Exception ex) {
                    // TODO Auto-generated catch block
                    System.out.println("_____________Error_________________"+ ex);
                    String err = ex.toString();
                    Log.e("SQLView",err);
                    }
      			
        }  
  
        @Override  
        public void onProviderDisabled(String provider) {  
            // TODO Auto-generated method stub           
        }  
  
        @Override  
        public void onProviderEnabled(String provider) {  
            // TODO Auto-generated method stub           
        }  
  
        @Override  
        public void onStatusChanged(String provider,   
  int status, Bundle extras) {  
            // TODO Auto-generated method stub           
        }  
    }  
}