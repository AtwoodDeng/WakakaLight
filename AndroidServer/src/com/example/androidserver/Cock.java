package com.example.androidserver;

public class Cock {
	public byte id = 0;
	public byte colorID = 0;
	public byte isPouring = 0;
	public byte r = 0;
	public byte g = 0;
	public byte b = 0;
	
	public Cock(byte _id){
		id = _id;
		setColor(50, 50, 50);
	}
	
	public void setColor( int _r , int _g , int _b )
	{
		r = (byte)_r;
		g = (byte)_g;
		b = (byte)_b;
	}
	public void receive(byte[] in)
	{
		if ( in.length < 3 )
			return;
		if ( in[0] != id )
			return;
		if ( in[3] != '#' )
			return;
		colorID = in[1];
		isPouring = in[2];
		
	}
	
	public void deal()
	{
		switch (colorID) {
		case 1:
			setColor(0, 255, 0);
			break;
		case 2:
			setColor(0, 0, 255);
			break;
		default:
			setColor(0, 0, 0);
			break;
		}
		
	}
	
	public byte[] getInfo()
	{
		byte[] res = new byte[4];
		res[0] = r;
		res[1] = g;
		res[2] = b;
		res[3] = (byte)'#';
		return res;
	}
}
