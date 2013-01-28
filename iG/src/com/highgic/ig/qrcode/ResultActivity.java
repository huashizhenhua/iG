package com.highgic.ig.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highgic.ig.R;

public class ResultActivity extends Activity {

	
	private TextView resultString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	setContentView(R.layout.activity_result);
	resultString =(TextView) findViewById(R.id.resultString);
	
	Bundle bundle= new Bundle();
	bundle=getIntent().getExtras();
	 if(bundle!=null){
	resultString.setText(bundle.getString("s1"));
	
	 }
	 
	 
	
		
	}
	
	
	
	
	
}
