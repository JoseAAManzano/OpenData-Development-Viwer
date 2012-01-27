package com.dreamteam.opendata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class OpenSQL {
	/*Creates new database connections, all from the same scheme.
	 * Connections must be closed after used.
	 * Hopefully working :)*/
	private Statement stmt;
	private String url = "jdbc:mysql://caasd.gov.do:3306/mysql";
	private Connection conn;
	private ResultSet rs;
	
	//Creates new instance of a Database Connection
	public OpenSQL(String user, String pw){
		try {
			conn = DriverManager.getConnection(url,user,pw);
			stmt = conn.createStatement();
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
		}
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	//Executes a query and prints the result.
	// TODO return result depending on type
	public void execQuery(String query, String[] p_params){
		try {
			rs = stmt.executeQuery(query);

			while(rs.next())
			{
				for(int i = 0; i < p_params.length; i++){
					System.out.print(rs.getString(p_params[i])+" ");
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Returns int located at index column from the query, must be a single value.
	public int processQuery(String query, int index){
		int result = 0;
		try{
			rs = stmt.executeQuery(query);
			rs.next();
			result = rs.getInt(index);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public void createTable(String csvData, String table_name){
		
		boolean tableColSet = false;
		int idx =0;
		String columns = "";
		Vector<String> Data = new Vector<String>();
		try{
			stmt.executeUpdate("DROP TABLE IF EXISTS opendata." + table_name);
			for(int i = 0; i < csvData.length();)
			{
				String s="";
				while(csvData.charAt(i)!='\n')
				{
					s+=csvData.charAt(i++);
				}
				for(int k=1;k<s.split('"'+"").length-1;k+=2)
				{	
					String a = s.split('"'+"")[k].trim().replace((13)+"", "").replace(" ", ""); 
					Data.addElement(a);
				}
				s="";
				
				if(!tableColSet)
				{
					tableColSet = true;
					String sql = "CREATE TABLE opendata." + table_name+" (id INT UNSIGNED NOT NULL AUTO_INCREMENT, PRIMARY KEY (id)," +getElementString(Data, Data.size())+")";
					stmt.executeUpdate(sql);
					columns = getValues(Data, Data.size());
				}
				else
				{
					String sql = "INSERT INTO opendata." + table_name + " VALUES (NULL,"+ getElementCols(Data, Data.size())+" )";
					stmt.executeUpdate(sql);
				}
				Data.clear();
				i++;
			}
			
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private String getValues(Vector<String> data,int x) {
		String s = "";
		for(int i = 0; i < x; i++){
			if(i != x-1){
				s += data.elementAt(i) + ",";
			} else s += data.elementAt(i).replace("\n", "");
		}
		return s;
	}

	private String getElementCols(Vector<String> data, int x) {
		String s = "";
		for(int i = 0; i < x; i++){
			if(i != x-1){
				s += "\""+data.elementAt(i) + "\", ";
			} else s += "\""+data.elementAt(i).replace("\n", "")+"\"";
		}
		return s;
	}

	private String getElementString(Vector<String> data, int x) {
		String s = "";
		for(int i = 0; i < x; i++){
			if(i != x-1){
				s += data.elementAt(i).toString() + " TEXT, ";
			} else s += data.elementAt(i).toString().replace("\n", "") + " TEXT";
		}
		return s;
	}

	//Closes connection after all queries were executed.
	public void closeConn(){
		try {
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
