package com.dreamteam.opendata;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.sql.DriverManager;
import java.util.Vector;
import com.dreamteam.opendata.ui.button;
import com.dreamteam.utils.ImageTool;


public class MyApplet extends Applet implements MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 4198914155412395609L;
	public static Vector<Province> Provinces;
	button buts[] = new button[5]; 
	Province moused;
	int X,Y;
	Compute compute;
	BufferedImage mapa;
	BufferedImage mapa2;
	private boolean onceLoaded;

	
	public void init()
	{
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		}
		catch(Exception e)
		{
		
		}
		onceLoaded = false;
		int w = 795,h=555;
		setSize(w, h);
		addMouseListener(this);
		addMouseMotionListener(this);
		Provinces = new Vector<Province>();
		compute = new Compute();

		loadbuts();
		initMap();
		readData();
		setData();
		ColorMap();
		
	}
	
	
	private void loadbuts() {
		String f = "but";
		for(int i=0;i<buts.length;i++)
			buts[i] = new button(f+(i+1),i*200+2,getHeight()-70,this);
		buts[4].x = getWidth() - 225;
		buts[4].y = 30;
	}


	private void ColorMap() {
		for(int i =0;i<Provinces.size();i++)
		{
			Province p = Provinces.elementAt(i);
			System.out.println(p.Name+ " "+p.Color + " "+p.Color2Draw+ " "+p.gradient);
			ImageTool.forceColor(mapa2, p.Color, p.Color2Draw);
		}
	}


	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(mapa2, 0, 0, Color.WHITE, null);
		drawbuts(g);
		if(moused!=null)
			drawInfo(g);
	}
	
	
	private void drawInfo(Graphics g) {
//		int pixels[] = new int[1000];
//				for(int i=0;i<pixels.length;i++)
//					pixels[i] = 0xBB000000;
//		
		int size = 175;
		g.setColor(new Color(0xAA000000, true));
		g.fillRect(X, Y-25, size+5, (moused.Data.size()*(g.getFontMetrics().getHeight()*2))+15);
		for(int i=0;i<moused.Data.size();i++)
		{
			g.setColor(new Color(0x0DAA0B,false));
			Font temp = g.getFont();
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString(moused.Name, X+5, Y-10);
			g.setFont(new Font("serif",Font.BOLD,temp.getSize()));
			g.drawString(moused.Data.elementAt(i).type+": "+moused.Data.elementAt(i).value+"%", X+5, Y+(i+1)*g.getFontMetrics().getHeight());
		}
		
	}


	private void drawbuts(Graphics g) {
		for(int i =0;i<buts.length;i++)
			buts[i].draw(g);
	}


	private void setData() 
	{
		if(!onceLoaded)
		{
			compute.generateArrays();
			onceLoaded=true;
		}
		for(int i =0;i<Provinces.size();i++)
			{
			Province p = Provinces.elementAt(i);
			p.Data.clear();
			p.Data.addElement(new dato("Access to Electricity",compute.getElect()[i]));
			p.Data.addElement(new dato("Garbage Pollution",compute.getGarba()[i]));
			p.Data.addElement(new dato("Literacy",compute.getLiter()[i]));
			p.Data.addElement(new dato("OverCrowding",compute.getOverC()[i]));
			p.calcProgress();
			}
		setColors2Draw();
	}


	private void setColors2Draw() 
	{
		double MAX=0;
		for(int i =0;i<Provinces.size();i++)
		{
			Province p = Provinces.elementAt(i);
			MAX = p.progress > MAX? p.progress: MAX ;
		}
		
		for(int i =0;i<Provinces.size();i++)
		{
			Province p = Provinces.elementAt(i);
			p.gradient = (p.progress*100)/MAX;
			p.setColor2Draw();
		}
		
		
		
	}


	private void readData()
	{
	         String s = "Pedernales		0x6600ff \nIndependencia		0x336699 \nBarahona		0x3000ff \nBahoruco		0x33ccff \nSanJuandelaMaguana	0x66cc33 \nElíasPiña		0x66cc99 \nAzua			0xccff00 \nValverde		0xcc99ff \nSantiagoRodriguez	0x99ff99 \nMonteCristi		0x669933 \nDajabón			0xffff99 \nSantiago		0xff9999 \nPuertoPlata		0x333366 \nEspaillat		0x99cc99 \nMonseñorNouel		0xcc6633 \nSanchezRamírez		0x999933 \nLaVega			0xcc3333 \nSamana			0xff9933 \nSalcedo			0x003366 \nMariaTrinidadSanchez	0x996633 \nDuarte			0x33ff00 \nHatoMayor		0x00ffff \nSanPedro		0xcc00ff \nLaRomana		0xff0000 \nHigüey			0xffff00 \nElSeibo		        0xff9900 \nSanJosédeOcoa	        0xcc3399 \nMontePlata		0x99cc00 \nSanCristobal		0xff0099 \nPeravia			0xffcc00 \nSantoDomingo 		0x0099ff \nDistritoNacional	0x66ff00\n"; 		 
	        		 loadProvinces(s);
	 		 System.out.println("Provinces Loaded");
	}
	
	private void loadProvinces(String s) {
		String sti ="", //Linea
			   stp=""; //Color
		int k=0;
		boolean flag = false;
		 
		for(int i = 0 ;i<s.length();i++)
		{
		  if(s.charAt(i)!='\n')	
		  {
			  
			if(s.charAt(i)!=' ' && s.charAt(i)!='0' && !flag)
			{
				sti+=s.charAt(i);
			}
			else if(s.charAt(i)=='0' && s.charAt(i+1)=='x')
			{   flag = true;
				stp = s.substring(i, i+8);//asigno un Substring
				
			}
		  }
		  else
		  {
			Province pti = new Province(Integer.decode(stp.replace("\t", "")),sti.replace("\t", ""));//province to insert
			Provinces.insertElementAt(pti, k);//insert the province in a "k" index
			System.out.println(pti.Name + " "+pti.Color);
			k++;
			sti ="";
			stp="";
			flag = false;
		  }
		}
	}


	private void initMap() {
		try
		{
				Image m = getImage(getCodeBase(),"MAPA2.png"),
					 m1 = getImage(getCodeBase(),"MAPA2.png");
				mapa = ImageTool.toBufferedImage(m);
				mapa2 = ImageTool.toBufferedImage(m1);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void destroy()
	{
	
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX(),
			y = e.getY();
		System.out.println("X: "+x+"Y: "+y);
		for(int i = 0;i<buts.length;i++)
		{
			button b = buts[i];
			if(x > b.x && x< b.x+b.width && y>b.y && y<b.y+b.height)
			{
				System.out.println("clicked");
				if(i==0)
					graph("Literacy",compute.Liter);
				else if(i==1)
					graph("Access to Electricity",compute.Elect);
				else if(i==2)
					graph("Garbage Pollution",compute.Garba);
				else if(i==3)
					graph("Overcrowding",compute.OverC);
				else regraph();
			}
		}
					int color= mapa.getRGB(x, y);
			Province p = null;
			for(int i=0;i<Provinces.size();i++)
			{
				if(Provinces.elementAt(i).Color==color)
				{
					p=Provinces.elementAt(i);
					i=Provinces.size()-1;
					moused = p;
				}
				else moused = null;
				repaint();
			}
			X = x;
			Y = y;
		
		
	}


	private void graph(String s,double[] array) {
		for(int i =0;i<Provinces.size();i++)
		{
		Province p = Provinces.elementAt(i);
		p.Data.clear();
		p.Data.addElement(new dato(s,array[i]));
		p.calcProgress();
		}
	setColors2Draw();
	mapa2 = ImageTool.deepCopy(mapa);
	ColorMap();
	repaint();
	invalidate();
	repaint();
	paint(getGraphics());
	}
	
	private void regraph() {
		for(int i =0;i<Provinces.size();i++)
		{
			Province p = Provinces.elementAt(i);
			p.Data.clear();
			p.Data.addElement(new dato("Access to Electricity",compute.getElect()[i]));
			p.Data.addElement(new dato("Garbage Pollution",compute.getGarba()[i]));
			p.Data.addElement(new dato("Literacy",compute.getLiter()[i]));
			p.Data.addElement(new dato("OverCrowding",compute.getOverC()[i]));
			p.calcProgress();
		}
	setColors2Draw();
	mapa2 = ImageTool.deepCopy(mapa);
	ColorMap();
	repaint();
	invalidate();
	repaint();
	paint(getGraphics());
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}
		

}

