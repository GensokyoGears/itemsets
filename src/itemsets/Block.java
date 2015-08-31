package itemsets;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


public class Block implements Serializable {
	
	private static final long serialVersionUID = -188557234136140659L;
	String type;
    Boolean recMath;
    Integer minSummonerLevel;
    Integer maxSummonerLevel;
    String showIfSummonerSpell;
    String hideIfSummonerSpell;
    List<Item> items;
  
	
	public Block(String type, Boolean recMath, Integer minSummonerLevel, Integer maxSummonerLevel,
			String showIfSummonerSpell, String hideIfSummonerSpell, List<Item> items) {
		super();
		this.type = type;
		this.recMath = recMath;
		this.minSummonerLevel = minSummonerLevel;
		this.maxSummonerLevel = maxSummonerLevel;
		this.showIfSummonerSpell = showIfSummonerSpell;
		this.hideIfSummonerSpell = hideIfSummonerSpell;
		this.items = items;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getRecMath() {
		return recMath;
	}
	public void setRecMath(Boolean recMath) {
		this.recMath = recMath;
	}
	public Integer getMinSummonerLevel() {
		return minSummonerLevel;
	}
	public void setMinSummonerLevel(Integer minSummonerLevel) {
		this.minSummonerLevel = minSummonerLevel;
	}
	public Integer getMaxSummonerLevel() {
		return maxSummonerLevel;
	}
	public void setMaxSummonerLevel(Integer maxSummonerLevel) {
		this.maxSummonerLevel = maxSummonerLevel;
	}
	public String getShowIfSummonerSpell() {
		return showIfSummonerSpell;
	}
	public void setShowIfSummonerSpell(String showIfSummonerSpell) {
		this.showIfSummonerSpell = showIfSummonerSpell;
	}
	public String getHideIfSummonerSpell() {
		return hideIfSummonerSpell;
	}
	public void setHideIfSummonerSpell(String hideIfSummonerSpell) {
		this.hideIfSummonerSpell = hideIfSummonerSpell;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	 
}
