package models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;
import algorithm.GlobalOptions;
import algorithm.RequestComparator;
import controller.MemoryController;
import controller.ProcessController;
public class Process_ {
	private int process_id;
	private String name;
	private int priority;
	private int status=0;//阻塞、就绪、运行
	private int cost;
	private int pc=0;
	private int spend=0;
	private LinkedList<Request_> request;
	private Process_Resourse resourse;
	public LinkedList<Integer> havelist;
	public ArrayList<Integer> pagelist;
	private int maxpage;//最大页数
	private boolean flag=false;//是否缺页
	/**
	 * 创建进程
	 * @param process_id 进程号
	 * @param priority 优先级
	 * @param name 进程名
	 * @param cost 总时间片
	 * @param request 请求
	 * @param need 时间片需求
	 * @param pagelist 页面需求
	 * @param maxpage 页容量
	 */
	public Process_(int process_id,int priority,String name,int cost,LinkedList<Request_> request,Resourse_ need,ArrayList<Integer> pagelist) {
		this.process_id=process_id;
		this.priority=priority;
		this.cost=cost;
		this.name=name;
		resourse=new Process_Resourse(need, new Resourse_());
		this.request=request;
		Collections.sort(request, RequestComparator.comparator);
		this.pagelist=pagelist;
		this.maxpage=MemoryController.defaultpage;
		havelist=new LinkedList<>();
	}
	public void block(){
		if(status!=0) {
			ProcessController.block_list.add(this);
			ProcessController.ready_list.remove(this);
		}
		status=0;
	}
	public void ready(){
		if(status==0) {
			ProcessController.ready_list.add(this);
			ProcessController.block_list.remove(this);
		}
		status=1;
	}
	public int getSpend(){
		return spend;
	}
	public int getPc(){
		return pc;
	}
	public int getCost() {
		return cost;
	}
	public int getMaxpage(){
		return maxpage;
	}
	public void run(){
		if(MemoryController.virtualMemory){
			flag=GlobalOptions.pageReplacement.run(this);
		}
		status=2;
		pc++;
	}
	public int getPriority() {
		return priority;
	}
	public String getName() {
		return name;
	}
	public int getProcess_id() {
		return process_id;
	}
	public void setWait() {
		spend++;
	}
	public double getRp(){
		return (spend)/1.0/cost+1;
	}
	public static final String sta_name[]=new String[]{"阻塞","就绪","运行"};
	public String getStatus() {
		return sta_name[status];
	}
	public LinkedList<Request_> getRequest() {
		return request;
	}
	public void setRequest(LinkedList<Request_> request) {
		this.request = request;
	}
	public Resourse_ haveRequest(){
		if(!request.isEmpty()){
			Request_ r=request.getFirst();
			if(r.getPc()==pc) return r.getRequest();
		}
		return null;
	}
	public Resourse_ getNeed(){
		return resourse.need;
	}
	public Resourse_ getAllocation(){
		return resourse.allocation;
	}
	public Vector<Object> getRow(){
		Vector<Object> objects=new Vector<>();
		objects.add(Integer.toString(process_id));
		objects.add(name);
		objects.add(getStatus());
		objects.add(Integer.toString(priority));
		objects.add(Integer.toString(spend));
		objects.add(Integer.toString(cost));
		objects.add(Integer.toString(pc));
		objects.add(String.format("%.3f", getRp()));
		return objects;
	}
	public Vector<Object> getmRow(){
		Vector<Object> objects=new Vector<>();
		objects.add(Integer.toString(process_id));
		String hString="";
		for(Integer e:havelist) hString+=e.toString()+" ";
		objects.add(hString);
		if(flag) objects.add("不缺页");
		else objects.add("缺页");
		return objects;
	} 
}
