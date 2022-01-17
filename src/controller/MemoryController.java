package controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;
import algorithm.GlobalOptions;
import models.Memory_;
import models.Process_;
public class MemoryController{
	public static LinkedList<Memory_> list=null;
	public static boolean virtualMemory;//是否使用虚拟内存
	public static int sum;//总页数或最大内存
	public static int defaultpage;//默认分配页数
	/**
	 * 当list中有0时，说明可添加进程
	 */
	public static ArrayList<Integer> vList=null;
	public static Vector<String> name=null;
	/**
	 * 禁用虚拟内存初始化方法
	 * @param sum 总内存
	 */
	public static void initialize(int sum){
		virtualMemory=false;
		list=new LinkedList<>();
		list.add(new Memory_(0,0,sum));
		name=new Vector<>(Arrays.asList(new String[]{"进程号","内存起始地址","内存终止地址","占用内存大小"}));
	}
	/**
	 * 启用虚拟内存初始化方法
	 * @param sum 总页数
	 * @param defaultpage 默认给每个进程分配的页数
	 */
	public static void initialize(int sum,int defaultpage) {
		virtualMemory=true;
		MemoryController.defaultpage=defaultpage;
		vList=new ArrayList<Integer>(Collections.nCopies(sum/defaultpage, 0));
		name=new Vector<>(Arrays.asList(new String[]{"进程号","当前页","是否缺页"}));
	}
	public static boolean add(int process_id){
		int index=vList.indexOf(0);
		if(index!=-1) {
			vList.set(index, process_id);
			return true;
		}
		return false;
	}
	public static boolean add(int process_id,int memorysize){
		Collections.sort(list,GlobalOptions.memory_comparator);
		for(Memory_ e:list){
			if(e.getProcess_id()!=0||e.getMemorysize()<memorysize) continue;
			Memory_ m1=new Memory_(process_id,e.getStart(), memorysize);
			Memory_ m2=null;
			if(e.getMemorysize()>memorysize){
				m2=new Memory_(0, e.getStart()+memorysize,e.getMemorysize()-memorysize);
			}
			list.remove(e);
			list.add(m1);
			if(m2!=null) list.add(m2);
			return true;
		}
		return false;
	}
	public static void remove(int process_id){
		if(virtualMemory){
			vList.set(vList.indexOf(process_id), 0);
		}
		else{
			Collections.sort(list,GlobalOptions.firstfit());
			for(int i=0;i<list.size();i++){
				Memory_ m=list.get(i);
				if(process_id==m.getProcess_id()){
					Memory_ m1=null;
					Memory_ m2=null;
					if(i>0) m1=list.get(i-1);
					if(i+1<list.size()) m2=list.get(i+1);
					if(m1!=null&&m1.getProcess_id()==0) {
						m=connect(m1, m);
						if(m2!=null&&m2.getProcess_id()==0) m=connect(m, m2);
					}
					else if(m2!=null&&m2.getProcess_id()==0) m=connect(m, m2);
					else {
						m.setProcess_id(0);
						return ;
					}
					list.add(m);
					return;
				}
			}
		}
	}
	public static Memory_ connect(Memory_ m1,Memory_ m2){
		Memory_ p=new Memory_(0, m1.getStart(),m1.getMemorysize()+m2.getMemorysize());
		list.remove(m1);list.remove(m2);
		return p;
	}
	public static Vector<Vector<Object>> gettable(){
		Vector<Vector<Object>> vector=new Vector<>();
		if(virtualMemory){
			for(Process_ e:ProcessController.ready_list) vector.add(e.getmRow());
			for(Process_ e:ProcessController.block_list) vector.add(e.getmRow());
		}
		else{
			Collections.sort(list,GlobalOptions.firstfit());
			for(Memory_ e:list) vector.add(e.getRow());
		}
		return vector;
	}
}
