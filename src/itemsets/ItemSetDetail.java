package itemsets;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.context.RequestContext;

import local.GlobalConfig;
import itemsets.ItemSetDisplay;

public class ItemSetDetail implements Serializable {

	private static final long serialVersionUID = 6501079101373926031L;
	JSONObject jsonObj;
	ItemSetDisplay is;
	Champion champObj;
	String jsonString;

	public ItemSetDisplay getIs() {
		return is;
	}

	public void setIs(ItemSetDisplay is) {
		this.is = is;
	}

	public Champion getChampObj() {
		return champObj;
	}

	public void setChampObj(Champion champObj) {
		this.champObj = champObj;
	}

	public JSONObject getJsonObj() {

		try {
			String itemsetId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("id");
			Class.forName(GlobalConfig.DB_DRIVER);
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME,
					GlobalConfig.DB_PASSWORD);
			String query = "SELECT * FROM itemsets WHERE ID=?";

			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, itemsetId);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				// Get data to parse
				jsonString = rs.getString("json");

				// Get itemset data
				is = new ItemSetDisplay();
				is.setId(rs.getInt("id"));
				is.setMap(rs.getString("map"));
				is.setName(rs.getString("name"));
				is.setJson(jsonString);
				is.setUsername(rs.getString("username"));
				is.setChampion(rs.getString("champion"));
				is.setDownload(rs.getString("download"));
			}

			rs.close();
			st.close();

			// Get champion data
			query = "SELECT * FROM champions WHERE ID=?";
			st = conn.prepareStatement(query);
			st.setString(1, is.getChampion());
			rs = st.executeQuery();

			if (rs.next()) {
				champObj = new Champion();
				champObj.setId(rs.getInt("ID"));
				champObj.setName(rs.getString("NAME"));
				champObj.setTitle(rs.getString("TITLE"));
				String image = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + rs.getString("KEYID")
						+ ".png";
				champObj.setImage(image);
				String splash = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + rs.getString("KEYID")
						+ "_0.jpg";
				champObj.setSplash(splash);
				String loading = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/" + rs.getString("KEYID")
						+ "_0.jpg";
				champObj.setLoading(loading);
			}

			st.close();
			rs.close();
			conn.close();

			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(jsonString);
			return obj;
		} catch (Exception e) {
			System.out.println("Error while loading item set!!");
			e.printStackTrace();
			return null;
		}
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	//
	public Item doGetItemObject(String inputID) {

		try {
			Class.forName(GlobalConfig.DB_DRIVER);
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME,
					GlobalConfig.DB_PASSWORD);
			String query = "SELECT * FROM items WHERE id=?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, inputID);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Integer id = rs.getInt("ID");
				String sid = id.toString();
				String iname = rs.getString("NAME");
				String idescription = rs.getString("DESCRIPTION");
				String iplaintext = rs.getString("PLAINTEXT");
				String image = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/" + sid + ".png";
				Integer count = 1;
				Item item = new Item(iname, sid, image, idescription, iplaintext, count);

				st.close();
				rs.close();
				conn.close();
				return item;
			}
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		return null;
	}

	public void download() {
		String js = "var a = window.document.createElement('a'); a.href = window.URL.createObjectURL(new Blob(['"
				+ is.getJson() + "'], {type: 'text/plain'})); a.download = '" + champObj.getName() + "_" + is.getName()
				+ ".json'; document.body.appendChild(a); a.click(); document.body.removeChild(a);";
		RequestContext.getCurrentInstance().execute(js);
		// Add 1 to downloads
		try {
			Class.forName(GlobalConfig.DB_DRIVER);
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME,
					GlobalConfig.DB_PASSWORD);
			String query = "SELECT * FROM itemsets WHERE id=?";

			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, is.getId());
			ResultSet rs = st.executeQuery();

			Integer counter = 0;
			if (rs.next()) {
				counter = rs.getInt("download");
				counter++;
			}

			st.close();
			rs.close();

			query = "UPDATE itemsets SET DOWNLOAD=? WHERE id=?";
			st = conn.prepareStatement(query);
			st.setInt(1, counter);
			st.setInt(2, is.getId());
			st.executeUpdate();
			st.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String doGetBlockHeader(String status, String jungleStatus) {
		String result = "";
		if (status != null) {
			result += status;
		}

		if (jungleStatus != null) {
			if(jungleStatus.equals("SummonerSmite")){
				result += " (jungle only)";
			}
		}
		
		return result;
	}
}