package com.containing.containingmanagement;


import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String ipAdress = "";
	static SharedPreferences settings;
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN };  
	  
	private static double[] VALUES = new double[] { 10, 11, 12, 13 };  
	  
	private static String[] NAME_LIST = new String[] { "Transport", "Buffer", "Crane", "Agv" };  
	  
	private CategorySeries mSeries = new CategorySeries("");  
	  
	private DefaultRenderer mRenderer = new DefaultRenderer();  
	  
	private GraphicalView mChartView; 
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
		  Timer	myTimer = new Timer();
		   myTimer.scheduleAtFixedRate(new TimerTask() {          
		        @Override
		        public void run() {
		            getData();
		        }

		    }, 0, 1000);
		  
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
				VALUES = new double[]{(Integer)decodedMessage.getParameters()[1],
				(Integer)decodedMessage.getParameters()[2],
				(Integer)decodedMessage.getParameters()[3],
				(Integer)decodedMessage.getParameters()[4]};
				mSeries.clear();
				mRenderer.removeAllRenderers();
				for (int i = 0; i < VALUES.length; i++) {  
					mSeries.add(NAME_LIST[i]+ " - " +(int)Math.round(VALUES[i])+ " ", VALUES[i]);  
					SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();  
					renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);  
					mRenderer.addSeriesRenderer(renderer);  
				
					} 
				if(mChartView == null)
				{
		
				mRenderer.setApplyBackgroundColor(true);  
				mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));  
				mRenderer.setChartTitleTextSize(20);  
				mRenderer.setLabelsTextSize(15);  
				mRenderer.setLegendTextSize(15);  
				//mRenderer.setLabelsColor(Color.WHITE);
				mRenderer.setMargins(new int[] { 20, 30, 15, 0 });  
				mRenderer.setZoomButtonsVisible(true);  
				mRenderer.setStartAngle(90);  
				
				
				
			
					LinearLayout layout = (LinearLayout) findViewById(R.id.chart);  
				mChartView = ChartFactory.getPieChartView(MainActivity.this, mSeries, mRenderer);  
				layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT ));
				}
				else
				{
					
					mChartView.repaint(); 
				}
				
				
						
				}
			
				
			});
			}

}
