package com.ling.smsrcvzhc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {	

	Button myButton;
	public static final String PREF_NAME = "MESSAGE_RECEIVER_ZHC";
	int type = 0;
	String MSG_TYPE = "MESSAGE_TYPE";
	RadioGroup mRadio;
	RadioButton showToast,showDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		restorePreferences();
		
		myButton = (Button) findViewById(R.id.button1);
		mRadio = (RadioGroup)findViewById(R.id.radioGroup1);
		showToast = (RadioButton)findViewById(R.id.toastShow);
		showDialog = (RadioButton)findViewById(R.id.dialogShow);
		
		if(type == 0){
			showToast.setChecked(true);
		}else{
			showDialog.setChecked(true);
		}
		
		mRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == showToast.getId()){
					type = 0;
					Log.i("LICH","Toast Show");
				}else if (arg1 == showDialog.getId()){
					type = 1;
					Log.i("LICH","Dialog show");
				}
			}
		});
		
        myButton.setOnClickListener(
    		new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					savePreferences();
					System.exit(0);
				}
    		}
        );
	}
	
    private void savePreferences() {
        
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    	
        
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putInt(MSG_TYPE, type);
        editor.commit();
    }
    
    private void restorePreferences() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        type = prefs.getInt(MSG_TYPE, 0);
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
