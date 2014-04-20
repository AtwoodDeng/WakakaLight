package com.example.androidserver;

import android.graphics.Color;

public class Cocktail {
	byte id;
	byte isShaking;
	Color[] colors;
	
	public Cocktail(){
		id = 0;
		isShaking = 0;
		colors = new Color[3];
	};
	
}
