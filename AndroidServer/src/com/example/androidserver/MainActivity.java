package com.example.androidserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {


	UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	//basic
	private static final int REQUEST_ENABLE_BT = 2;  
    TextView txt;  
    BluetoothAdapter mBluetoothAdapter;  
    ArrayAdapter mArrayAdapter;  
    Button btnSwitch; 
    Button btnScan;  
    ListView list;  
    CountDownTimer scan_timer;
    CountDownTimer main_timer;
	ArrayList<BluetoothDevice> bluetoothArray = new ArrayList<BluetoothDevice>();

	//cock
	Button btnCock;
	BluetoothDevice cockDevice;
	BluetoothSocket cockSocket;
	InputStream cockIn;
	OutputStream cockOut;
	TextView cockText;
	Cock cockData;
	
	//CockTail
	MyDevice<Cocktail> cocktailDevice = new MyDevice<Cocktail>("cocktail", (byte)1);
	
	
	public enum MyState
	{
		Normal,
		SelectCock,
		SelectCocktail,
		SelectTail,
	};
	public MyState state = MyState.Normal;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
		
        Init();
    }
    
    public void onButtonStart( View view)
    {
        btnSwitch.setEnabled(true);
    	btnCock.setEnabled(true);
    	cocktailDevice.start();
    	
        if (mBluetoothAdapter.isEnabled())
        {
        	btnSwitch.setText("ON");
        	btnScan.setEnabled(true);
        }
        main_timer.start();
    }
    
    public void Init()
    {
    	//blink
        txt = (TextView)findViewById(R.id.mainText); 
        txt.setText("__Wakaka__");
        cockText = (TextView)findViewById(R.id.textCock);
        cockText.setText( "cock device not found");
        cocktailDevice.text = (TextView)findViewById(R.id.textCocktail);
        list = (ListView) findViewById(R.id.listView);    
        
        //blind buttons
        btnSwitch = (Button)findViewById(R.id.buttonControl);
        btnSwitch.setEnabled(false);
        btnScan = (Button)findViewById(R.id.buttonSearch);  
        btnScan.setText("扫描:OFF");
        btnScan.setEnabled(false);
        btnCock = (Button)findViewById(R.id.buttonCock);
        btnCock.setEnabled(false);
        cocktailDevice.btn = (Button)findViewById(R.id.buttonCocktail);
        
        //init data
        cockData = new Cock();
        cocktailDevice.data = new Cocktail();
        cocktailDevice.init();
        
        //list view
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
        list.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)
        	{
        		setMessage("the " + arg2 + " device is " + bluetoothArray.get(arg2).getName()
        				+ " " + bluetoothArray.get(arg2).getAddress());
        		selectDevice(bluetoothArray.get(arg2));
        	}
		});
           
        //blue tooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if ( mBluetoothAdapter ==null )
	       {
	       	setMessage("fail");
	       	MainActivity.this.finish();
	       	
	       	return;
	       }
        
        
        //timmer
        scan_timer = new CountDownTimer(12000,1000)   
        {  
            @Override  
            public void onTick( long millisUntilFinished)   
            {  
            	setMessage( "剩余扫描时间" + millisUntilFinished/1000 + "秒");  
            }

			@Override
			public void onFinish() {
				//判断蓝牙是否已经被打开   
                if (mBluetoothAdapter.isEnabled())  
                {  
                    btnScan.setEnabled(true);  
                    //关闭扫描   
                    mBluetoothAdapter.cancelDiscovery();  
                    btnScan.setText("扫描:OFF");  
                    setMessage( "停止扫描");  
                }   
			}
        };
        
        main_timer = new CountDownTimer(1200000,500)   
        {  
            @Override  
            public void onTick( long millisUntilFinished)   
            {  
            	update();
            }

			@Override
			public void onFinish() { 
				
			}
        };
        
    }
    
    public void update()
    {
    	receive();
    	deal();
    	send();
    }
    
    static int time = 0;
    public void receive()
    {
    	time ++;
    	try{
    		//receive cock
	    	if ( cockDevice != null && cockSocket != null && cockIn != null && cockOut != null )
	    	{
	    		if ( cockIn.available() >= 4 )
	    		{
	    			byte[] in = new byte[100];
	    			cockIn.read(in);
	    			cockIn.reset();
	    			
	    			if ( in[0] != '0' )
	    			{
	    				setMessage("wrong cock id " + in[0] );
	    				return;
	    			}
	    			if ( in[3] != '#')
	    			{
	    				setMessage("wrong protile tail " + in[3]);
	    				return;
	    			}
	    			
	    			cockData.colorID = (byte)(in[1] - 48);
	    			cockData.isPouring = (byte)(in[2] -48);
	    			setMessage("get " + time + " " + cockData.colorID + " " + cockData.isPouring );
	    		}
	    		
	    		byte[] cocktailRec = cocktailDevice.receive(15);
	    	}
    	}catch( Exception e){
    		setMessage("Fail receive " + e.toString());
    	}
    	
    }
    
    public void deal()
    {
    	if ( cockDevice != null && cockSocket != null && cockIn != null && cockOut != null  )
    	{
    		if ( cockData.colorID == 1 )
    		{
    			cockData.setColor(0, 255, 0);
    		}
    		else if ( cockData.colorID == 2 )
    		{
    			cockData.setColor(0, 0, 255);
    		}
    		else
    		{
    			cockData.setColor(0, 0, 0);
    		}
    	}

    }
    public void send()
    {
    	try{
	    	if ( cockDevice != null && cockSocket != null && cockIn != null && cockOut != null  )
	    	{
	    		byte[] out = new byte[4];
	    		out[0] = cockData.r;
	    		out[1] = cockData.g;
	    		out[2] = cockData.b;
	    		out[3] = (byte)'#';
	    		cockOut.write(out);
	    	}
	    	{

	    		byte[] out = new byte[13];
	    		cocktailDevice.send(out);
	    	}
    	}catch( Exception e){
    		setMessage("Fail receive " + e.toString());
    	}
    }
    
    public void selectDevice( BluetoothDevice device)
    {
    	switch(state){
    	case SelectCock:
    		cockDevice = device;
			setMessage("Begin connect to " + device.getName());
    		try {
    			cockSocket = cockDevice.createRfcommSocketToServiceRecord(uuid); 
    			cockSocket.connect(); 
    			cockIn = cockSocket.getInputStream();
    			cockOut = cockSocket.getOutputStream();
    			setMessage("Cock connection success!");
        		cockText.setText("cock connect to " + device.getName());
    			send();
    		} catch (Exception e) {
    			setMessage("Fail selectDevice " + e.toString());
    		}
    		break;
    	case SelectCocktail:
    		cocktailDevice.select(device);
    		send();
			setMessage("Cocktail connection success!");
    	default:
    		break;
    	}
    }
	
	public void onButtonCock(View view){
		state = MyState.SelectCock;
		setMessage("select a cock device.");
	}
	public void onButtonCocktail( View view ){
		state = MyState.SelectCocktail;
		setMessage("select a cock tail device.");
	}
    
    public void setMessage( String str )
    {
    	if ( txt != null )
    		txt.setText(str);
    }
    
    public void onButtonControl(View view){
    	String str = btnSwitch.getText().toString();
		if ( "OFF".equals(str))
		{
			if ( !mBluetoothAdapter.isEnabled())
			{
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				txt.setText("s1");
				btnScan.setText("扫描:OFF");
				btnScan.setEnabled(true);
			}
		}else {

            //关闭蓝牙   
            mBluetoothAdapter.disable();  
            btnSwitch.setText("OFF");  
            mArrayAdapter.clear();  
            list.setAdapter(mArrayAdapter);    
            btnScan.setEnabled(false);   
		}
    }
    
	public void onButtonSearch(View view){
		 String str = btnScan.getText().toString();  
         if (str == "扫描:OFF")  
         {  
             txt.setText("s5");  
             if (mBluetoothAdapter.isEnabled())   
             {  
                 //开始扫描   
                 mBluetoothAdapter.startDiscovery();  
                 txt.setText("s6");  
                 btnScan.setText("扫描:ON");  
                   
                 // Create a BroadcastReceiver for ACTION_FOUND   
                 final BroadcastReceiver mReceiver = new BroadcastReceiver()   
                 {  
                     @Override  
                     public void onReceive(Context context, Intent intent)   
                     {     
                         String action = intent.getAction();  
                         // When discovery finds a device   
                         mArrayAdapter.clear();  
                         if (BluetoothDevice.ACTION_FOUND.equals(action))   
                         {  
                             // Get the BluetoothDevice object from the Intent   
                             BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                             // Add the name and address to an array adapter to show in a ListView   
                             mArrayAdapter.add(device.getName() + ":" + device.getAddress());  
                             
                         }  
                         list.setAdapter(mArrayAdapter);  
                     }  
                 };  
                 // Register the BroadcastReceiver   
                 IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);  
                 registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy   
                   
                 scan_timer.start();  
             }  
         }  
         else  
         {  
             //关闭扫描   
             mBluetoothAdapter.cancelDiscovery();  
             btnScan.setText("扫描:OFF");  
             scan_timer.cancel();  
             setMessage( "停止扫描");  
         }   
	    	
	    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)   
    {    
		
        switch (requestCode)   
        {    
        case REQUEST_ENABLE_BT:    
            // When the request to enable Bluetooth returns     
            if (resultCode == Activity.RESULT_OK)   
            {    
                // Bluetooth is now enabled, so set up a chat session     
                btnSwitch.setText("ON");  
                txt.setText("s4");  
                  
                //获取蓝牙列表   
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();  
                mArrayAdapter.clear();  
                // If there are paired devices   
                if (pairedDevices.size() > 0)   
                { 
                	
                	txt.setText("s3");   
                      
                    // Loop through paired devices   
                    for (BluetoothDevice device : pairedDevices)   
                    {  
                        // Add the name and address to an array adapter to show in a ListView   
                        mArrayAdapter.add(device.getName() + ":" + device.getAddress());  
                        bluetoothArray.add(device);
                    }  
                    list.setAdapter(mArrayAdapter);  
                 }  
            } else   
            {    
                finish();    
            }    
        }    
    } 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
