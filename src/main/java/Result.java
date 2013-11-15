import java.net.URL;
import java.util.HashMap;


class Result {
private Result(){
	result = new HashMap<String, String[]>();
	description = "default";
	size = 0;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getSize() {
	return size;
}
public void setSize(int size) {
	this.size = size;
}
public static Result get (){
	return r1;
}
public void add(String u, String[] s){
	this.result.put(u, s);
}
public HashMap<String, String[]> getResult(){
	return result;
}
public String toString(String u){
	HashMap <String, String[]> hash  = this.getResult();
	System.out.print(u);
	String[] array = hash.get(u);
	String s=this.getDescription()+"["+this.getSize()+"]<br/>";
	int i=1;
	for(String iArray: array){
		s+=i+". "+iArray+"<br/>";
		i++;
	}
	return s;
}
private static Result r1= new Result();
private HashMap<String, String[]> result;
private String description;
private int size;


public static void main (String[] args){
	Result rTemp = Result.get();
	String key = "http://ukr.net";
	String[] arrayS={"one", "two", "three"};
	rTemp.add(key, arrayS);
	//map.put("FirsElement", arrayS);
	System.out.print(rTemp.toString(key));
}
}
