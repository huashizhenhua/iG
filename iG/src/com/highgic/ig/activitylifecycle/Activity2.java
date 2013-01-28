package com.highgic.ig.activitylifecycle;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.highgic.ig.R;

public class Activity2 extends Activity {
	
	private static String TAG="HighgicAndroidStudy Lifecycle Activity2";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        Log.i(TAG, "---------Activity2 ONCREATE--------");
        
        setContentView(R.layout.activity_result);
        
        AlertDialog.Builder alertDialog=new Builder(Activity2.this);
        alertDialog.setTitle("DIALOG");
        alertDialog.setIcon(android.R.drawable.ic_menu_info_details);
        alertDialog.setMessage("This Dialog show a Activity's Lifecycle.");
        alertDialog.setNegativeButton("ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "---------Activity2 ONCLICK--------");
				startActivity(new Intent(Activity2.this, Activity1.class));
				Activity2.this.finish();
			}
		});
        
        alertDialog.show();
        Log.i(TAG, "---------Activity2 DIALOG SHOW--------");
        
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.i(TAG, "---------Activity2 ONSTART--------");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
       	Log.i(TAG, "---------Activity2 ONRESUME--------");
    }
    
    
    @Override
    protected void onPause() {
    	super.onPause();
       	Log.i(TAG, "---------Activity2 ONPAUSE--------");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
       	Log.i(TAG, "---------Activity2 ONSDOP--------");
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
       	Log.i(TAG, "---------Activity2 ONRESTART--------");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i(TAG, "---------Activity2 ONDESTROY--------");
    }
    
}