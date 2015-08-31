package itemsets;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.DragDropEvent;

import local.GlobalConfig;
import itemsets.ItemSetDisplay;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChampionDetail implements Serializable {

	private static final long serialVersionUID = 7414763884453808726L;

	Champion curr;
	String champId;
	String gameMode;
	List<ItemSetDisplay> itemSetData;

	public List<ItemSetDisplay> getItemSetData() {

		itemSetData = new ArrayList<ItemSetDisplay>();
		try {
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME, GlobalConfig.DB_PASSWORD);

			// get ItemSets
			String query = "SELECT * FROM itemsets WHERE champion=?";
			if (!gameMode.equals("custom")) {
				query += " AND map=?";
			}
			query += " ORDER BY DOWNLOAD DESC";
			
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, champId);
			if (!gameMode.equals("custom")) {
				st.setString(2, gameMode);
			}
			
			ResultSet rs = st.executeQuery();

			// iterate through the java resultset
			while (rs.next()) {
				ItemSetDisplay is = new ItemSetDisplay();
				is.setId(rs.getInt("id"));
				//
				
				String mapString = rs.getString("map");
				if(mapString.equals("SR")){
					is.setMap("Summoner's Rift");
				}
				if(mapString.equals("HA")){
					is.setMap("Howling Abyss");
				}
				if(mapString.equals("TT")){
					is.setMap("Twisted Treeline");
				}
				if(mapString.equals("CS")){
					is.setMap("Crystal Scar");
				}
				if(mapString.equals("any")){
					is.setMap("Any");
				}
				
				is.setName(rs.getString("name"));
				is.setUsername(rs.getString("username"));
				is.setDownload(rs.getString("download"));
				itemSetData.add(is);
			}
			
			st.close();
			rs.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemSetData;
	}

	public void setItemSetData(List<ItemSetDisplay> itemSetData) {
		this.itemSetData = itemSetData;
	}

	public Champion getCurr() {
		return curr;
	}

	public void setCurr(Champion curr) {
		this.curr = curr;
	}

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	@PostConstruct
	public void init() {
		itemSetData = new ArrayList<ItemSetDisplay>();
		curr = null;

		try {
			champId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mode") != null) {
				gameMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mode");
			} else {
				gameMode = "custom";
			}

			// create our mysql database connection
			Class.forName(GlobalConfig.DB_DRIVER);
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME, GlobalConfig.DB_PASSWORD);

			String query = "SELECT * FROM champions WHERE ID=?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, champId);
			ResultSet rs = st.executeQuery();

			// iterate through the java resultset

			while (rs.next()) {
				curr = new Champion();
				curr.setId(rs.getInt("ID"));
				curr.setName(rs.getString("NAME"));
				curr.setTitle(rs.getString("TITLE"));
				String image = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + rs.getString("KEYID")
						+ ".png";
				curr.setImage(image);
				String splash = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + rs.getString("KEYID")
						+ "_0.jpg";
				curr.setSplash(splash);
				String loading = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/" + rs.getString("KEYID")
						+ "_0.jpg";
				curr.setLoading(loading);
			}
			
			rs.close();
			st.close();
			conn.close();

		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}

}
