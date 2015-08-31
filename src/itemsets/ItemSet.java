package itemsets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


public class ItemSet  implements Serializable {

	private static final long serialVersionUID = -3616565558207879992L;
	String title;
	String type;
	String map;
	String mode;
	Boolean priority;
	Integer sortrank;
	Integer download;
	
	public ItemSet(String title, String type, String map, String mode, Boolean priority, Integer sortrank, Integer download) {
		super();
		this.title = title;
		this.type = type;
		this.map = map;
		this.mode = mode;
		this.priority = priority;
		this.sortrank = sortrank;
		this.download = download;
	}
	
	public void init() {
    	type = "custom";
    	map = "any";
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Boolean getPriority() {
		return priority;
	}
	public void setPriority(Boolean priority) {
		this.priority = priority;
	}
	public Integer getSortrank() {
		return sortrank;
	}
	public void setSortrank(Integer sortrank) {
		this.sortrank = sortrank;
	}

	public Integer getDownload() {
		return download;
	}

	public void setDownload(Integer download) {
		this.download = download;
	}
	
	
}
