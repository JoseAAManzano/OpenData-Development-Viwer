package com.dreamteam.opendata;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Stack;

import com.junar.api.DataStream;

public class Compute {
	
	@SuppressWarnings("unchecked")
	static Stack<String>[] stack = new Stack[5];
	
	OpenSQL sqlcon = new OpenSQL("opendata","opendata");
	public double[] OverC = new double[32];
	public double[] Elect = new double[32];
	public double[] Liter = new double[32];
	public double[] Garba = new double[32];

	public static void main(String[] args){
		
//		OpenSQL asdf = new OpenSQL("opendata","opendata");
//		DataStream ds[] = new DataStream[5];
//		loadStacks(ds);
//		initDataStreams(ds); //We initialize the data Streams
//		getCsvData(ds,asdf);
//		asdf.closeConn();
	}
	
	private int[] collectDataOverC(){
		int[] result = new int[32];
		for(int i=0;i<32;i++)
		{
			result[i]=sqlcon.processQuery("SELECT * FROM opendata.overcselecaregio"
					+i
					+" WHERE opendata.overcselecaregio"
					+i
					+".Categoras = \"Nohacinado\"", 3);
		}
		System.out.println("Cargado OverC");
		return result;
	}
	
	private int[] collectDataElectAcc(){
		int[] result = new int[32];
		for(int i=0;i<32;i++){
			result[i]=sqlcon.processQuery("SELECT * FROM opendata.electaccesselecaregio"
					+i
					+" WHERE opendata.electaccesselecaregio"
					+i
					+".Categoras = \"Total\"", 3);
		}
		System.out.println("Cargado Elect");
		return result;
	}
	
	private int[] collectDataLiterSel(){
		int[] result = new int[32];
		for(int i=0;i<32;i++){
			result[i]=sqlcon.processQuery("SELECT * FROM opendata.literselecaregio"
					+i
					+" WHERE opendata.literselecaregio"
					+i
					+".Categoras = \"Si\"", 3);
		}
		System.out.println("Cargado Liter");
		return result;
	}
	
	private int[] collectDataGarbPol(){
		int[] result = new int[32];
		for(int i=0;i<32;i++){
			result[i]=sqlcon.processQuery("SELECT * FROM opendata.garbapolluselecaregio"
					+i
					+" WHERE opendata.garbapolluselecaregio"
					+i
					+".Categoras = \"No\"", 3);
		}
		System.out.println("Cargado Garb");
		return result;
	}
	
	public void generateArrays(){
		int[] overC;
		int[] elect;
		int[] liter;
		int[] garba;
		overC = collectDataOverC();
		elect = collectDataElectAcc();
		liter = collectDataLiterSel();
		garba = collectDataGarbPol();
		int sumO = sum(overC);
		int sumE = sum(elect);
		int sumL = sum(liter);
		int sumG = sum(garba);
		for(int i =0;i<32;i++){
			this.OverC[i] = round((double)(((double)overC[i]/sumO)*100));
			this.Elect[i] = round((double)(((double)elect[i]/sumE)*100));
			this.Liter[i] = round((double)(((double)liter[i]/sumL)*100));
			this.Garba[i] = round((double)(((double)garba[i]/sumG)*100));
		}
	}
	
	private double round(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
    return Double.valueOf(twoDForm.format(d));
	}
	
	public double[] getOverC() {
		return OverC;
	}

	public double[] getElect() {
		return Elect;
	}

	public double[] getLiter() {
		return Liter;
	}

	public double[] getGarba() {
		return Garba;
	}

	private int sum(int[] array){
		int result=0;
		for(int i = 0; i < 32;i++){
			result+=array[i];
		}
		return result;
	}

	public void print(){
		for(int i =0; i<32;i++){
			System.out.print(this.OverC[i]+" ");
		}
		System.out.println();
		for(int i =0; i<32;i++){
			System.out.print(this.Elect[i]+" ");
		}
		System.out.println();
		for(int i =0; i<32;i++){
			System.out.print(this.Liter[i]+" ");
		}
		System.out.println();
		for(int i =0; i<32;i++){
			System.out.print(this.Garba[i]+" ");
		}
		System.out.println();
	}
	

	@SuppressWarnings("unused")
	private static void loadStacks(DataStream[] ds) {
		for(int j=0;j<ds.length;j++)
		{
		stack[j] = j==0? DataStream.getStrStack(new File("utils/codes.txt")) : DataStream.getStrStack(new File("utils/codes2.txt"));
		}
	}

	@SuppressWarnings("unused")
	private static void getCsvData(DataStream[] ds, OpenSQL asdf) 
	{
		for(int j=0;j<ds.length;j++)
		{
			for(int i =0;i<32;i++)
			{
	        	String[] p_params = {stack[j].pop()};
	        	String ss = ds[j].invoke(p_params,"csv");
	    		ss = ss.replace("", "").replace("%", "porcent");
	    		asdf.createTable(ss, ds[j].getGUID().replace("-", "")+i+"");
	        }
		}
        	 
	}

	@SuppressWarnings("unused")
	private static void initDataStreams(DataStream[] ds) 
	{
		int i=0;
		ds[i++] = new DataStream("ENHOG-2005-REPUB-DOMIN-SANEA");
		ds[i++] = new DataStream("GARBA-PULLU-SELEC-A-REGIO");
		ds[i++] = new DataStream("LITER-SELEC-A-REGIO");
		ds[i++] = new DataStream("OVERC-SELEC-A-REGIO");
		ds[i] = new DataStream("ELECT-ACCES-SELEC-A-REGIO");
	}
}
