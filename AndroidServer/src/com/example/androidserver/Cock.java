package com.example.androidserver;

public class Cock {
	public byte id = 0;
	public byte colorID = 0;
	public byte isPouring = 0;
	public byte r = 0;
	public byte g = 0;
	public byte b = 0;
	
	public void setColor( int _r , int _g , int _b )
	{
		r = (byte)_r;
		g = (byte)_g;
		b = (byte)_b;
	}
}
