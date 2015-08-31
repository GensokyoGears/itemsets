package local;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataExtractor {

	static String API_KEY = "";
	static String SQL_FILEPATH = "";
	
	public static void main(String[] args) throws Exception {
		StringBuffer sqlData;

		sqlData = initializeSqlData();
		initializeSSLtrust();
		sqlData = processChampionsData(sqlData);
		sqlData = processItemsData(sqlData);
		
		saveData(sqlData);
	}

	private static void saveData(StringBuffer sqlData) throws Exception {
		 BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(SQL_FILEPATH)));
         bwr.write(sqlData.toString());
        
         //flush the stream
         bwr.flush();
        
         //close the stream
         bwr.close();
	}

	private static StringBuffer initializeSqlData() {
		StringBuffer data = new StringBuffer();
		data.append("create table champions (" + getNewLineString());
		data.append("ID integer(5) primary key," + getNewLineString());
		data.append("NAME  varchar(40)," + getNewLineString());
		data.append("TITLE varchar(100)," + getNewLineString());
		data.append("KEYID varchar(40));" + getNewLineString());
		data.append(getNewLineString());

		data.append("create table items (" + getNewLineString());
		data.append("ID integer(5) primary key," + getNewLineString());
		data.append("GOLDTOTAL integer(5)," + getNewLineString());
		data.append("NAME  varchar(40)," + getNewLineString());
		data.append("DESCRIPTION  varchar(2000)," + getNewLineString());
		data.append("PLAINTEXT varchar(2000));" + getNewLineString());
		data.append(getNewLineString());
		
		data.append("CREATE TABLE itemsets ("+ getNewLineString());
		data.append("ID int(11) NOT NULL AUTO_INCREMENT,"+ getNewLineString());
		data.append("NAME varchar(100) DEFAULT NULL,"+ getNewLineString());
		data.append("JSON varchar(10000) DEFAULT NULL,"+ getNewLineString());
		data.append("CHAMPION varchar(45) DEFAULT NULL,"+ getNewLineString());
		data.append("MAP varchar(10) DEFAULT NULL,"+ getNewLineString());
		data.append("USERNAME varchar(45) DEFAULT NULL,"+ getNewLineString());
		data.append("DOWNLOAD int(11) DEFAULT NULL,"+ getNewLineString());
		data.append("PRIMARY KEY (ID));"+ getNewLineString());
		data.append(getNewLineString());
		
		
		return data;
	}

	private static String getNewLineString() {
		return System.getProperty("line.separator");
	}

	private static StringBuffer processChampionsData(StringBuffer sqlData) throws Exception {

		String dataURL = "https://eune.api.pvp.net/api/lol/static-data/eune/v1.2/champion?champData=image&api_key=" + API_KEY;
		JSONObject data = getLOLAPIdata(dataURL);
		JSONObject championsData = (JSONObject) data.get("data");
		for (Object obj : championsData.values()) {
			JSONObject champion = (JSONObject) obj;
			String name = (String) champion.get("name");
			Long id = (Long) champion.get("id");
			String title = (String) champion.get("title");
			String key = (String) champion.get("key");
			String query = "INSERT INTO champions VALUES(" + id + ",\"" + name + "\",\"" + title + "\",\"" + key + "\");"
					+ getNewLineString();
			sqlData.append(query);
		}
		sqlData.append(getNewLineString());
		return sqlData;
	}

	private static StringBuffer processItemsData(StringBuffer sqlData) throws Exception {
		
		String dataURL = "https://global.api.pvp.net/api/lol/static-data/eune/v1.2/item?itemListData=gold&api_key=" + API_KEY;
		JSONObject data = getLOLAPIdata(dataURL);
		JSONObject championsData = (JSONObject) data.get("data");
		for (Object obj : championsData.values()) {
			JSONObject item = (JSONObject) obj;
			String name = (String) item.get("name");
			String description = (String) item.get("description");
			String plaintext = (String) item.get("plaintext");
			if(plaintext==null){
				plaintext = "";
			}
			JSONObject gold = (JSONObject) item.get("gold");
			Long goldTotal = (Long)gold.get("total");
			Long id = (Long) item.get("id");
			
			String query = "INSERT INTO items VALUES(" + id  + "," + goldTotal + ",\"" + name + "\"" + 
			",\"" + description + "\"" +
			",\"" + plaintext + "\"" +
			");" + getNewLineString();
			sqlData.append(query);
			
		}
		
		return sqlData;
	}

	private static JSONObject getLOLAPIdata(String httpsURL) throws Exception {
		StringBuffer result = new StringBuffer();
		URL myurl = new URL(httpsURL);
		HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
		InputStream ins = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(ins);
		BufferedReader in = new BufferedReader(isr);

		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			result.append(inputLine);
		}

		in.close();

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(result.toString());
		return json;
	}

	private static void initializeSSLtrust() {
		SSLContext ctx = null;
		TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		try {
			ctx = SSLContext.getInstance("SSL");
			ctx.init(null, trustAllCerts, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			System.out.println("Error loading ssl context {}" + e.getMessage());
		}

		SSLContext.setDefault(ctx);
	}
}