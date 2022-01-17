package models;
import java.util.ArrayList;
import java.util.Vector;
import algorithm.GlobalOptions;
public class Package_  {
	private ArrayList<File_> filelist = null;
	private ArrayList<Package_> packagelist = null;
	private String name;
	private String path;
	private String time;
	public Package_(String name){
		path="";
		time=GlobalOptions.createtime();
		this.name = name;
		filelist = new ArrayList<File_>();
		packagelist = new ArrayList<Package_>();
	}
	public Package_(Package_ o){
		time=GlobalOptions.createtime();
		name=o.name;
		filelist=new ArrayList<File_>();
		packagelist=new ArrayList<Package_>();
		for(File_ f:o.filelist){
			File_ file_=new File_(f);
			if(file_.exists())
				filelist.add(file_);
		}
		for(Package_ p:o.packagelist){
			packagelist.add(new Package_(p));
		}
	}
	public ArrayList<File_> getFilelist() {
		return filelist;
	}
	public ArrayList<Package_> getPackagelist() {
		return packagelist;
	}
	public int getSize() {
		int size=0;
		for(File_ e:filelist) size+=e.getSize();
		for(Package_ e:packagelist) size+=e.getSize();
		return size;
	}
	public String getPath() {
		return path+name+"/";
	}
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 判断是否用同名文件夹和同名文件
	 * @param name
	 * @return 若无同名返回true
	 */
	public boolean check(String name){
		for(File_ f:filelist) 
			if(f.toString().equals(name)) return false;
	    for(Package_ p:packagelist) 
	    	if(p.toString().equals(name)) return false;
    	return true;
	}
	public void add(Package_ p){
		packagelist.add(p);
		setFilePath(getPath());
	}
	public void add(File_ f){
		filelist.add(f);
		setFilePath(getPath());
	}
	public void remove(File_ f){
		f.delete();
		filelist.remove(f);
	}
	public void remove(Package_ p){
		for(File_ f:p.filelist) f.delete();
		for(Package_ e:p.packagelist) remove(e);
		packagelist.remove(p);
	}
	public void setFilePath(String childpath){
		for(File_ f:filelist){
			f.setPath(childpath+f.toString());
		}
		for(Package_ e:packagelist){
			e.setPath(childpath);
			e.setFilePath(e.getPath());
		}
	}
	public void setName(String name){
		this.name=name;
	}
	public void find(String name,ArrayList<Object> finded){
		for(File_ f:filelist) {
			if(f.toString().contains(name)||f.getPath().equals(name)) {
				finded.add(f);
			}
		}
		for(Package_ p:packagelist) {
			if(p.toString().contains(name)||p.getPath().equals(name)) {
				finded.add(p);
			}
			p.find(name, finded);
		}
	}
	@Override
	public String toString() {
		return name;
	}
	public Vector<Object> getRow(){
		Vector<Object> objects=new Vector<>();
		objects.add(toString());
		objects.add("文件夹");
		objects.add(Integer.toString(getSize()));
		objects.add(time);
		objects.add("-");
		return objects;
	}
}
