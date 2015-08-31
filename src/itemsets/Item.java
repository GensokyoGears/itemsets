package itemsets;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
String name;
String id;
String image;
String description;
String plaintext;
Integer count;



public Integer getCount() {
	return count;
}
public void setCount(Integer count) {
	this.count = count;
}
public Item(String name, String id, String image, String description, String plaintext, Integer count) {
	super();
	this.name = name;
	this.id = id;
	this.image = image;
	this.description = description;
	this.plaintext = plaintext;
	this.count = count;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getPlaintext() {
	return plaintext;
}
public void setPlaintext(String plaintext) {
	this.plaintext = plaintext;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}


}
