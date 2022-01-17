package controller;
import java.util.Arrays;
import java.util.Vector;
import java.util.Map.Entry;
import models.Resourse_;
public class DeviceController {
	public static Resourse_ resourse=null;
	public static Resourse_ total=null;
	public static Vector<String> Dname=null;
	public static Vector<String> PDname=null;
	public DeviceController() {
		resourse=new Resourse_();
		total=new Resourse_();
		Dname=new Vector<>(Arrays.asList(new String[]{"设备名","总数量","剩余数量","分配数量"}));
		PDname=new Vector<>(Arrays.asList(new String[]{"设备名","Need","Allocation"}));
	}
	public static boolean add(String name,int count){
		if(count+resourse.getOrDefault(name,0)>=0) {
			resourse.add(name,count);
			total.add(name,count);
			return true;
		}
		return false;
	}
	public static int get(String name){
		return resourse.get(name);
	}
	public static Vector<Vector<Object>> gettable(){
		Vector<Vector<Object>> vector=new Vector<>();
		for(Entry<String, Integer> e:total.entrySet()){
			Vector<Object> objects=new Vector<>();
			String key=e.getKey();
			int t=e.getValue();
			int t2=resourse.get(key);
			objects.add(key);
			objects.add(t);
			objects.add(t2);
			objects.add(t-t2);
			vector.add(objects);
		}
		return vector;
	}
}
