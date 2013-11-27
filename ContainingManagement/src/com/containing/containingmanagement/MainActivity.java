package com.containing.containingmanagement;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String ipAdress = "";
	static SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settings = getSharedPreferences("containingSettings", Activity.MODE_PRIVATE);
	    
		getIPAdres();
		 
	}

	private void getIPAdres()
	{
		String ip = settings.getString("ip", "192.168.0.1");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Geef ip van controller");
		alert.setMessage("bv 192.168.0.1");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this); 

		alert.setView(input);
		input.setText(ip);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
		  String ipAd = input.getText().toString();
		  SharedPreferences.Editor editor = settings.edit();
		  editor.putString("ip", ipAd);
		  editor.commit();
		  ipAdress = ipAd;
		  getData();
		  
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
			  finish();
		  }
		});

		alert.show();
		// see http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	      return true;
	    }

	public void getData()
	{
		new ServerListener(this,ipAdress);
	}
	
	public void messageRecieved(final Message decodedMessage) {
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "aantal containers:"+decodedMessage.getParameters()[0], Toast.LENGTH_LONG).show();

				
			}});
			}

}
