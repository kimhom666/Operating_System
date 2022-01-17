package algorithm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import controller.DeviceController;
import controller.MemoryController;
import controller.ProcessController;
import front.MainFile;
import models.File_;
import models.Package_;
import models.Request_;
import models.Resourse_;
public class TestJson {
	public static void run() throws JSONException, IOException {
		File file=new File("test.json");
		if(!file.exists()) return;
		InputStream inputStream=new FileInputStream(file);
		Scanner scanner=new Scanner(inputStream,"utf-8");
		scanner.nextLine();
		StringBuilder stringBuilder=new StringBuilder();
		while(scanner.hasNextLine()){
			stringBuilder.append(scanner.nextLine());
			
		}
		JSONObject jsonObject=new JSONObject(stringBuilder.toString());
		JSONArray devices=jsonObject.getJSONArray("devices");
		addDevices(devices);
		if(MemoryController.virtualMemory){
			addVir(jsonObject.getJSONArray("vir"));
		}
		else {
			addNotVir(jsonObject.getJSONArray("notvir"));
		}
		addFile(jsonObject.getJSONObject("root"), MainFile.root);
		scanner.close();
	}
	public static void addDevices(JSONArray devices) throws JSONException{
		for(int i=0;i<devices.length();++i){
			JSONObject device=devices.getJSONObject(i);
			DeviceController.add(device.getString("name"), device.getInt("count"));
		}
	}
	public static void addVir(JSONArray processes) throws JSONException{
		for(int i=0;i<processes.length();++i){
			JSONObject process=processes.getJSONObject(i);
			int priority=process.getInt("priority");
			String name=process.getString("name");
			LinkedList<Request_> requests=addRequests(process.getJSONArray("requests"));
			ArrayList<Integer> pagelist=new ArrayList<>();
			JSONArray pagelistjson=process.getJSONArray("pagelist");
			for(int j=0;j<pagelistjson.length();++j){
				pagelist.add(pagelistjson.getInt(j));
			}
			if(!ProcessController.add(priority, name, requests, pagelist))
				JOptionPane.showMessageDialog(null,"添加进程"+name+"失败", null, JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void addNotVir(JSONArray processes) throws JSONException{
		for(int i=0;i<processes.length();++i){
			JSONObject process=processes.getJSONObject(i);
			int priority=process.getInt("priority");
			int cost=process.getInt("cost");
			int msize=process.getInt("msize");
			String name=process.getString("name");
			LinkedList<Request_> requests=addRequests(process.getJSONArray("requests"));
			if(!ProcessController.add(priority, name, cost, requests, msize))
				JOptionPane.showMessageDialog(null,"添加进程"+name+"失败", null, JOptionPane.ERROR_MESSAGE);
		}
	}
	public static LinkedList<Request_> addRequests(JSONArray request0) throws JSONException{
		LinkedList<Request_> list=new LinkedList<Request_>();
		for(int i=0;i<request0.length();++i){
			JSONObject request1=request0.getJSONObject(i);
			int pc=request1.getInt("pc");
			Resourse_ r=new Resourse_();
			JSONArray request2=request1.getJSONArray("request");
			for(int j=0;j<request2.length();++j){
				JSONObject request3=request2.getJSONObject(j);
				r.add(request3.getString("name"), request3.getInt("count"));
			}
			list.add(new Request_(pc, r));
		}
		return list;
	}
	public static void addFile(JSONObject object,Package_ parent) throws JSONException{
		JSONArray filelistjson=object.getJSONArray("filelist");
		for(int i=0;i<filelistjson.length();++i){
			JSONObject filejson=filelistjson.getJSONObject(i);
			File_ file_=new File_(filejson.getString("name"), filejson.getInt("size"));
			if(file_.exists()){
				parent.add(file_);
			}
			else {
				JOptionPane.showMessageDialog(null,"文件"+file_.toString()+"过大", null, JOptionPane.ERROR_MESSAGE);
			}
		}
		JSONArray packagelistjson=object.getJSONArray("packagelist");
		for(int i=0;i<packagelistjson.length();++i){
			JSONObject packagejson=packagelistjson.getJSONObject(i);
			Package_ p=new Package_(packagejson.getString("name"));
			addFile(packagejson,p);
			parent.add(p);
		}
	}
}
