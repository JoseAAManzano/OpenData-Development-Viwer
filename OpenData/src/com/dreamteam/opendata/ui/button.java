package com.dreamteam.opendata.ui;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dreamteam.utils.ImageTool;

public class button {

	public int x, y,width,height;
	BufferedImage img;
	Applet app;
	String label;
	
	public button(int x,int y, Applet app, String label)
	{
		this.x = x;
		this.y = y;
		this.label = label;
		this.app = app;
		loadImage();
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public button(String file,int x,int y, Applet app)
	{
		this.x = x;
		this.y = y;
		this.label = "";
		this.app = app;
		img = ImageTool.toBufferedImage(app.getImage(app.getCodeBase(), file+".png"));
		width = img.getWidth();
		height = img.getHeight();
	}

	public void draw(Graphics g)
	{
		g.drawImage(img,x,y,null);
		g.drawString(label, x+(img.getWidth()/2-(g.getFontMetrics().getMaxAdvance()*label.length())/2), y+(img.getHeight()/2)-g.getFontMetrics().getHeight()/2);
	}
	
	private void loadImage() {
		img = ImageTool.toBufferedImage(app.getImage(app.getCodeBase(), "but.png"));
		
	}
}
