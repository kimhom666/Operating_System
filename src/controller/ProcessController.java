package controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;
import algorithm.Banker_;
import algorithm.GlobalOptions;
import models.Request_;
import models.Resourse_;
import processScheduling.ProcessSchedulingInterface;
import models.Process_;
public class ProcessController {
	public static LinkedList<Process_> ready_list=null;//就绪队列
	public static LinkedList<Process_> block_list=null;//阻塞队列
	public static ArrayList<Process_> run_list=null;//运行队列
	public static int auto_process_id;//进程号自增主键
	private static int degree;//允许调入内存的作业数
	private static int batch;//允许同时处理的作业数
	public static int step;//运行时间
	public static Vector<Object> name=null;
	public static int col=-1;
	public ProcessController(int degree,int batch) {
		ready_list=new LinkedList<>();
		block_list=new LinkedList<>();
		run_list=new ArrayList<>();
		auto_process_id=1;
		step=0;
		ProcessController.degree=degree;
		ProcessController.batch=batch;
		name=new Vector<>(Arrays.asList(new String[]{
				"进程号","进程名","状态","优先级","总时间","需要时间","运行时间","响应比"}));
	}
	/**
	 * 银行家算法检查中
	 */
	public static void run1(){
		Collections.sort(block_list,GlobalOptions.block_comparator);
		col=((ProcessSchedulingInterface)GlobalOptions.block_comparator).getIndex();
		int s=block_list.size();
		/*若阻塞队列不为空且就绪队列未满，从阻塞队列末尾移动到就绪队列*/
		for(int i=0;i<s&&ready_list.size()<degree;i++){
			Process_ p=block_list.getFirst();
			block_list.removeFirst();
			Resourse_ request=p.haveRequest();
			if(request!=null) {
				/*若满足银行家算法，*/
				if(Banker_.judge(p, request, ready_list)){
					p.getAllocation().add(request);
					p.getNeed().sub(request);
					DeviceController.resourse.sub(request);
					p.getRequest().removeFirst();
					p.ready();
				}
				else block_list.add(p);
			}
			else {
				p.ready();
			}
		}
	}
	/**
	 * 时间片运行中
	 */
	public static void run2(){
		Collections.sort(ready_list,GlobalOptions.ready_comparator);
		col=((ProcessSchedulingInterface)GlobalOptions.ready_comparator).getIndex();
		for(int i=0;i<batch&&i<ready_list.size();i++){
			Process_ p=ready_list.get(i);
			p.run();
			run_list.add(p);
		}
	}
	/**
	 * 等待运行下一时间片
	 */
	public static void run3(){
		col=-1;
		for(Process_ e:ready_list){
			e.setWait();
		}
		for(Process_ e:block_list){
			e.setWait();
		}
		step++;
		for(int i=run_list.size()-1;i>=0;i--){
			Process_ e=run_list.get(i);
			/*销毁进程*/
			if(e.getCost()==e.getPc()) {
				remove(e.getProcess_id());
				continue;
			}
			if(e.haveRequest()==null) e.ready();
			else e.block();
		}
		run_list.clear();
	}
	/**
	 * 禁用虚拟内存
	 * @param pri 优先级
	 * @param name 进程名
	 * @param cost 消耗时间片
	 * @param request 资源请求
	 * @param msize 需要内存
	 * @return
	 */
	public static boolean add(int pri,String name,int cost,LinkedList<Request_> request,int msize){
		Resourse_ need=new Resourse_();
		for(Request_ e:request){
			need.add(e.getRequest());
		}
		if(DeviceController.total.compareTo(need)==-1) return false;
		if(MemoryController.add(auto_process_id, msize)==false) return false;
		Process_ p=new Process_(auto_process_id, pri, name, cost,request,need, null);
		block_list.add(p);
		auto_process_id++;
		return true;
	}
	/**
	 * 启用虚拟内存
	 * @param pri 优先级
	 * @param name 进程名
	 * @param request 资源请求
	 * @param pagelist 页面请求
	 * @return
	 */
	public static boolean add(int pri,String name,LinkedList<Request_> request,ArrayList<Integer> pagelist){
		Resourse_ need=new Resourse_();
		for(Request_ e:request){
			need.add(e.getRequest());
		}
		if(DeviceController.total.compareTo(need)==-1) return false;
		if(MemoryController.add(auto_process_id)==false) return false;
		Process_ p=new Process_(auto_process_id, pri, name, pagelist.size(),request,need, pagelist);
		block_list.add(p);
		auto_process_id++;
		return true;
	}
	public static void remove(int process_id){
		for(Process_ e:ready_list){
			if(e.getProcess_id()==process_id) {
				DeviceController.resourse.add(e.getAllocation());
				ready_list.remove(e);
				MemoryController.remove(e.getProcess_id());
				return ;
			}
		}
	}
	public static Process_ get(int process_id){
		for(Process_ e:ready_list) if(e.getProcess_id()==process_id) return e;
		for(Process_ e:block_list) if(e.getProcess_id()==process_id) return e;
		return null;
	}
	public static Vector<Vector<Object>> gettable(){
		Vector<Vector<Object>> vector=new Vector<>();
		for(Process_ e:ready_list) vector.add(e.getRow());
		for(Process_ e:block_list) vector.add(e.getRow());
		return vector;
	}
}
