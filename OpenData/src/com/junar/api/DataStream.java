package com.junar.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dreamteam.opendata.OpenSQL;

public class DataStream {
    private String app_key="2cbb5dab8b13b6b6606a8760c1f3ebcaab49cbcb";
    private String guid;
    private String base_uri="http://apisandbox.junar.com";
    
    public static void main(String args[]) {
    	
    	DataStream ds = new DataStream("SCHOL-LEVEL-SELEC-A-REGIO");
    	decodeJson("");
        Stack<String> adsf = getStrStack(new File("utils/codes2.txt"));
        for(int i =0;i<32;i++){
        	String[] p_params = {adsf.pop()};
        	 FileSave(ds.invoke(p_params,"csv"),ds.getGUID(),i);
        }
    }
    
    public static Stack<String> getStrStack(File file) {
		Stack<String> s = new Stack<String>();
		try {
			File old = file;
			BufferedReader br = new BufferedReader(new FileReader(old));
			for(int i =0;i<32;i++) s.push(br.readLine());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
    
    private static void FileSave(String csvData, String string, int i) {
    	
    	
    	
    	OpenSQL sql = new OpenSQL("opendata", "opendata");
    	csvData = csvData.replace("", "").replace("%", "porcent");
//    	System.out.println(csvData);
		sql.createTable(csvData, string.replace("-", ""));
		sql.closeConn();
//    	try{
//    		
//
//    		OutputStream os = new FileOutputStream(new File("csv/"+string+i+".csv"));
//    		while (csvData.length()>0)
//    		{
//    			if(csvData.length()==1)
//    			{
//    				os.write(csvData.charAt(0));
//    				os.close();
//    				return;
//    			}
//    			else os.write(csvData.charAt(0));
//    			
//    			csvData = csvData.length()!=1 ? csvData.substring(1, csvData.length()): csvData.charAt(0)+"";
//    		}
//    		os.close();
//    	}
//    	catch(Exception e)
//    	{
//    		System.out.println("Error saving the \'"+string+"\' csv file");
//    		System.out.println(e.getMessage());
//    	}
    	
	}

	private static String decodeJson(String invoke) 
    {
    	String t = "";
    	boolean openc = false;
    	int tabs = 0;
    	for(int i =0;i<invoke.length();i++)
    	{
    		char c = invoke.charAt(i);
    		if(c!='{' && c!='}')
   				t+=c;
    		if(c==',')
    		{
    			if(!openc)
    			t+= "\n"+getTabs(tabs);
    		}
    		else if(c=='"')
    			openc= openc? false:true;
    		else if(c=='{')
    		{
    			t+="\n"+getTabs(tabs++)+"{\n"+getTabs(tabs);
    		}
    		else if(c=='}')
    		{
    			t+="\n"+getTabs(--tabs)+"}\n"+getTabs(tabs);
    		}
    	}
		return t;
	}
    
    private static String getTabs(int i)
    {
    	String t = "";
    	while(i-->0)
    	{
    		t+="\t";
    	}
    	return t;
    }

	public DataStream(String p_guid) {
        this.setGUID(p_guid);
    }
    
    public String getGUID() {
        return guid;
    }
    
    public String getAuthKey() {
        return this.app_key;
    }
    public void setGUID(String p_guid) {
        guid = p_guid;
    }
    
    public String invoke(String[] p_params,String output)  {
    	
    	String url=null;
    	try
    	{
	        url = "/datastreams/invoke/" + getGUID() + "?";
	        url += "auth_key=" + app_key;
	        
	        for (int i=0; i < p_params.length; i++) {
	            url += "&pArgument" + i + "=" + p_params[i];
	        }
	        url += "&output="+output;
	        return callURI(url);
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    	return url;
        
    }
    
  

	public String invoke(String[] p_params) throws ClientProtocolException, IOException {
        String url = "/datastreams/invoke/" + getGUID() + "?";
        url += "auth_key=" + app_key;
        
        for (int i=0; i < p_params.length; i++) {
            url += "&pArgument" + i + "=" + p_params[i];
        }
        
        return callURI(url);
    }
    
    public String info() throws ClientProtocolException, IOException {
        String url = "/datastreams/" + getGUID() + "?auth_key=" + getAuthKey();
        return callURI(url);
    }
    
    private String callURI(String p_url) throws ClientProtocolException, IOException {
        HttpClient http_client = new DefaultHttpClient();
        String response_body = "";
        String final_url = this.base_uri + p_url;
        System.out.println("URL: " + final_url);
        try {
            HttpGet httpget = new HttpGet(final_url);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response_body = http_client.execute(httpget, responseHandler);
        } finally {
            http_client.getConnectionManager().shutdown();
        }
        
        return response_body;
    }
}
