package com.ling.smsrcvzhc;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MySMSReceiver extends BroadcastReceiver {
	Intent mIntent;
	String tel;
	String body;
	String longbody;
	StringBuilder bodyBuilder;

	public static final String PREF_NAME = "MESSAGE_RECEIVER_ZHC";
	int type = 0;
	String MSG_TYPE = "MESSAGE_TYPE";
	
	String telname;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {

				restorePreferences(context);
				Log.i("LICH","TYPE:"+type);

				bodyBuilder = new StringBuilder();

				Object[] myObject = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[myObject.length];
				for (int i = 0; i < myObject.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myObject[i]);
				}

				int length = messages.length;
				Log.i("LICH", "msg length" + length);

				for (SmsMessage tempSmsMessage : messages) {
					tel = tempSmsMessage.getDisplayOriginatingAddress();
					body = tempSmsMessage.getDisplayMessageBody();

					bodyBuilder.append(body);
				}

				longbody = bodyBuilder.toString();

				if (type == 0) {
					StringBuilder telcont = new StringBuilder();
					try {
						telname = searchContactNameByNumber(context,tel);

						if (telname != null) {
							telcont.append(context.getString(R.string.receive_from));
							telcont.append(telname);
						} else {
							telcont.append(context.getString(R.string.receive_from));
							telcont.append(tel);
						}

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
						telcont.append(context.getString(R.string.receive_from));
						telcont.append(tel);
					}

					StringBuilder bodycont = new StringBuilder();
					bodycont.append(telcont.toString());
					bodycont.append("\n");
					bodycont.append(context.getString(R.string.content));
					bodycont.append(longbody);
					
					Toast.makeText(context, bodycont.toString(), Toast.LENGTH_LONG).show();
				} else {

					mIntent = new Intent(context, ReceiveActivity.class);
					mIntent.putExtra("TEL", tel);
					mIntent.putExtra("BODY", longbody);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					context.startActivity(mIntent);
				}
			}

		}
	}

	private void restorePreferences(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		type = prefs.getInt(MSG_TYPE, 0);
	}
	
	public String searchContactNameByNumber(Context context,String number) throws Exception {
		String name = null;
		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "display_name" }, null, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
			// Log.i(TAG, name);
		}
		cursor.close();

		return name;
	}

}
