package com.ling.smsrcvzhc;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ReceiveActivity extends Activity {

	TextView Body, Tel;
	Button close, call, reply;
	String telephone, msgbody;
	String tel, body, telname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.receive_activity);

		Body = (TextView) findViewById(R.id.textView1);
		Tel = (TextView) findViewById(R.id.textView2);

		close = (Button) findViewById(R.id.button1);
		call = (Button) findViewById(R.id.button2);
		reply = (Button) findViewById(R.id.button3);

		Intent mIntent = getIntent();
		tel = mIntent.getStringExtra("TEL");
		body = mIntent.getStringExtra("BODY");

		StringBuilder telcont = new StringBuilder();
		try {
			telname = searchContactNameByNumber(tel);

			if (telname != null) {
				telcont.append(this.getString(R.string.receive_from));
				telcont.append(telname);
			} else {
				telcont.append(this.getString(R.string.receive_from));
				telcont.append(tel);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			telcont.append(this.getString(R.string.receive_from));
			telcont.append(tel);
		}

		StringBuilder bodycont = new StringBuilder();
		bodycont.append(this.getString(R.string.content));
		bodycont.append(body);

		telephone = telcont.toString();
		Tel.setText(telephone);

		msgbody = bodycont.toString();
		Body.setText(msgbody);

		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/*
		 * read.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub tts.speak(telephone, TextToSpeech.QUEUE_FLUSH, null);
		 * tts.speak("   ", TextToSpeech.QUEUE_ADD, null); tts.speak(msgbody,
		 * TextToSpeech.QUEUE_ADD, null);
		 * 
		 * } });
		 */

		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		/*
		 * reply.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub setContentView(R.layout.reply);
		 * setTitle(getResources().getString(R.string.reply_msg)); TextView text
		 * = (TextView) findViewById(R.id.reply_num); final EditText edit =
		 * (EditText) findViewById(R.id.reply_body); text.setText(tel); Button
		 * send = (Button) findViewById(R.id.send); Button cancel = (Button)
		 * findViewById(R.id.cancel);
		 * 
		 * cancel.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub finish(); } });
		 * 
		 * send.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub String phone_number = tel; String sms_content =
		 * edit.getText().toString();
		 * 
		 * SmsManager smsManager = SmsManager.getDefault();
		 * 
		 * // SmsManager smsm = SmsManager.getDefault(); //
		 * smsm.sendTextMessage("000000000", null, "body", null, // null); if
		 * (sms_content.length() > 70) { List<String> contents =
		 * smsManager.divideMessage(sms_content); for (String sms : contents) {
		 * smsManager.sendTextMessage(phone_number, null, sms, pi, null); } }
		 * else { smsManager.sendTextMessage(phone_number, null, sms_content,
		 * pi, null); }
		 * 
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub try
		 * { Thread.sleep(1000); Toast.makeText(ReceiveActivity.this,
		 * R.string.send_finish, Toast.LENGTH_SHORT).show(); finish(); } catch
		 * (Exception e) { finish(); } }
		 * 
		 * }).start(); // finish();
		 * 
		 * } });
		 * 
		 * } });
		 */

		reply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) { // TODO Auto-generated
				sendSMS(tel);
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	public String searchContactNameByNumber(String number) throws Exception {
		String name = null;
		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "display_name" }, null, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
			// Log.i(TAG, name);
		}
		cursor.close();

		return name;
	}

	private void sendSMS(String smsNum) {

		StringBuilder telnum = new StringBuilder();
		telnum.append("smsto:");
		telnum.append(smsNum);

		Uri smsToUri = Uri.parse(telnum.toString());

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		startActivity(intent);

	}

}
