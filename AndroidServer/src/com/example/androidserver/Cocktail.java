package com.example.androidserver;

import android.graphics.Color;
import android.widget.TextView;

public class Cocktail {
	static final int layNum = 3;
	byte id = 0;
	byte isPoured = 0;
	byte isShaking = 0;
	byte r[] ;
	byte g[] ;
	byte b[] ;
	int tempLay = 0;
	byte endMark;
	int shakeFlag = 0;
	int step = 0;
	
	public Cocktail(byte _id){
		id = _id;
		isShaking = 0;
		r = new byte[layNum];
		g = new byte[layNum];
		b = new byte[layNum];
		tempLay = 0;
		shakeFlag = 0;
		step = 0;
		endMark = '#';
	};
	
	public void addColor( byte _r , byte _g , byte _b )
	{
		if( tempLay < layNum )
		{
			r[tempLay] = _r;
			g[tempLay] = _g;
			b[tempLay] = _b;
			
			tempLay++;
		}
	}
	
	public void setColor( int i , int _r , int _g , int _b )
	{
		if ( i >= layNum )
			return;
		r[i] = (byte)_r;
		g[i] = (byte)_g;
		b[i] = (byte)_b;
	}
	
	
	public void receive(byte[] in)
	{
		if ( in.length < 3 )
			return;
		if ( in[0] != id )
			return;
		if ( in[3] != '#' )
			return;
		isPoured = in[1];
		isShaking = in[2];
	}
	
	
	public void deal( TextView txt )
	{
		if ( isShaking == 1 && shakeFlag == 0 )
		{

			if ( step == 0 )
			{
				//mix color
				int color_r=0;
				int color_g=0;
				int color_b=0;
				
				for( int i = 0 ; i < layNum ; ++ i )
				{
					color_r += (int) r[i] & 0xFF ;
					color_g += (int) g[i] & 0xFF ;
					color_b += (int) b[i] & 0xFF ;	
				}
				color_r = (color_r / layNum ) & 0xFF;
				color_g = (color_g / layNum ) & 0xFF;
				color_b = (color_b / layNum ) & 0xFF;
				
				for ( int i = 0 ;i < layNum ; ++i  )
				{
					setColor(i, color_r, color_g, color_b);
				}
				
				step = 1;
			}else{
				for ( int i = 0 ;i < layNum ; ++i  )
				{
					setColor(i, 0, 0, 0);
				}
				tempLay = 0;
				step = 0;
			}
			shakeFlag = 1;
			endMark = '*';
		}else if ( isShaking == 0 ){
			shakeFlag = 0;
			endMark = '#';
		}
	}
	
	public byte[] getInfo()
	{
		byte[] res = new byte[layNum*3+1];
		for(int i = 0 ; i < layNum ; ++i )
		{
			res[i*3] = r[i];
			res[i*3+1] = g[i];
			res[i*3+2] = b[i];
		}
		
		res[layNum*3] = endMark;
		return res;
	}
	
}
