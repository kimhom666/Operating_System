package models;
import java.util.Vector;
import algorithm.GlobalOptions;
public class File_  {
	private String name;
	private String time;
	private int size;
	private String path;
	/**
	 * 若该值为-1，说明文件创建失败
	 */
	private int fcb;
	public File_(String name,int size){
		this.name = name;
		time=GlobalOptions.createtime();
		this.size = size;
		fcb=Storage_.allocate(size);
	}
	public File_(File_ o){
		name=o.name;
		time=GlobalOptions.createtime();
		size=o.size;
		fcb=Storage_.allocate(size);
	}
	public boolean exists(){
		return fcb!=Storage_.fatUnused;
	}
	public void delete(){
		if(!exists()) return;
		Storage_.delete(fcb);
		fcb=-1;
	}
	public void SetFile(String name){
		this.name=name;
	}
	public boolean resize(int size){
		int fcb=Storage_.allocate(size);
		if(fcb==-1) return false;
		delete();
		this.fcb=fcb;
		this.size=size;
		time=GlobalOptions.createtime();
		return true;
	}
	public String getName(){
		int dotIndex=name.lastIndexOf(".");
		if(dotIndex==-1){
			return name;
		}
		else{
			return name.substring(0,dotIndex);
		}
	}
	public String getType() {
		int dotIndex=name.lastIndexOf(".");
		if(dotIndex==-1){
			return "";
		}
		else{
			return name.substring(dotIndex+1);
		}
	}
	public String getTime() {
		return time;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return name;
	}
	public Vector<Object> getRow(){
		Vector<Object> objects=new Vector<>();
		objects.add(getName());
		objects.add(getType());
		objects.add(Integer.toString(size));
		objects.add(time);
		objects.add(Integer.toString(fcb));
		return objects;
	}
	public int getSize(){
		return size;
	}
}
