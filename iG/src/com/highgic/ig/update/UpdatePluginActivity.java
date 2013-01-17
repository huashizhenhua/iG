package com.highgic.ig.update;

import com.highgic.ig.R;

import android.app.Activity;
import android.os.Bundle;

public class UpdatePluginActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_update_plugin);
        
        DRobotDialog dialog = new DRobotDialog(this);
        dialog.show();
        
    }
    
}
