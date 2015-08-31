package itemsets;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import local.GlobalConfig;

public class Archive implements Serializable {

	private static final long serialVersionUID = 2235798874128912332L;

	ArrayList<Champion> championList;

	public ArrayList<Champion> getChampionList() {
		return championList;
	}

	public void setChampionList(ArrayList<Champion> championList) {
		this.championList = championList;
	}

	@PostConstruct
	public void init() {
		championList = new ArrayList<Champion>();

		try {
			// create our mysql database connection
			Class.forName(GlobalConfig.DB_DRIVER);
			Connection conn = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.DB_USERNAME, GlobalConfig.DB_PASSWORD);
			String query = "SELECT * FROM champions ORDER BY NAME";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next()) {

				Champion currChamp = new Champion();
				currChamp.setId(rs.getInt("ID"));
				String sid = rs.getString("ID");
				currChamp.setName(rs.getString("NAME"));
				currChamp.setTitle(rs.getString("TITLE"));
				String image = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + rs.getString("KEYID") + ".png";
				currChamp.setImage(image);

				championList.add(currChamp);

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
