package com.example.androidserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.R.integer;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Button;
import android.widget.TextView;

public class MyDevice<T>{

	public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final byte end = (byte)'#';
	public String name;
	public byte id;
	public Button btn;
	public BluetoothDevice device;
	public BluetoothSocket socket;
	public InputStream in;
	public OutputStream out;
	public TextView text;
	public T data;
	
	public MyDevice(String _name , byte _id ){
		name = _name;
		id = _id;
	}
	
	public void init()
	{
		if ( text != null )
			text.setText("not device found");
		if ( btn != null )
		{
			btn.setText(name);
			btn.setEnabled(false);
		}
	}
	
	public void start()
	{
		if ( btn != null )
			btn.setEnabled(true);
	}
	
	public void select( BluetoothDevice _device )
	{
		if ( _device != null )
		{
			device = _device;
			connect();
		}
	}
	
	public void connect()
	{
		try{
		socket = device.createRfcommSocketToServiceRecord(uuid); 
		socket.connect(); 
		in = socket.getInputStream();
		out = socket.getOutputStream();
		text.setText( name + " connect to " + device.getName());
		}catch (Exception e){
			if ( text != null )
				text.setText("connect fail " + e.toString());
		}
	}
	public byte[] receive( byte[] get)
	{
		if ( device != null && socket != null && in != null && out != null )
		{
			try{
				if ( in.available() >= 1 )
				{
					if ( get == null )
						get = new byte[100];
					in.read(get);
					text.setText(name + " receive " + get[0] + get[1] + get[2]);
					return get;
				}
			}catch (Exception e){
				text.setText("receive fail " + e.toString());
			}
			return null;
		}
		return null;
	}
	
	public void send( byte[] info)
	{
		if ( device != null && socket != null && in != null && out != null )
		{
			try{
				out.write(info);
			}catch (Exception e){
				text.setText("send fail" + e.toString());
			}
		}
	}
}
