package com.dreamteam.opendata;

import java.util.Vector;

public class Province {
	
	
	Vector<dato> Data;
	public int Color,
			   Color2Draw;
	public double gradient;
	public String Name;
	public double progress;
	
	public Province(int color, String name) {
		super();
		Color = getWithAlpha(color);
		Name = name;
		Data = new Vector<dato>();
	}
	
	private int getWithAlpha(int color) {
		
		int c = color;
		int a = 255;
	    int r = (c >> 16) & 0xFF;
	    int g = (c >>  8) & 0xFF;
	    int b = (c >>  0) & 0xFF;
	     
		return ((a<<24) | (r << 16) | (g << 8) | b);
	}

	public void calcProgress()
	{
		double p=0;
		for(int i=0;i<Data.size();i++)
			p+=Data.elementAt(i).value;
		progress = p/Data.size();
	}

	public void setColor2Draw() 
	{
		int c = 0xFF0000FF;
		int a = (c >> 24) & 0xFF;
	    int r = (c >> 16) & 0xFF;
	    int g = (c >>  8) & 0xFF;
	    int b = (c >>  0) & 0xFF;
	    int minus = (int) (255*(gradient/100.f));
	    r += 255-minus;
	    g += 255-minus;
	    Color2Draw = ((a<<24) | (r << 16) | (g << 8) | b); 
	    
	}

	
	
//	public void Draw()
//	{
//		Map.DrawProvince(this, getDrawColor(Map.DataSet,this.Data));
//	}
//	
}


