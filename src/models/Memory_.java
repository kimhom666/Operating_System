package models;
import java.util.Vector;
public class Memory_ {
	private int process_id;
	private int memorysize;
	private int start;
	/**
	 * 禁用虚拟内存，创建进程内存
	 * @param process_id 进程号
	 * @param start 起始地址
	 * @param memorysize 内存大小
	 */
	public Memory_(int process_id,int start,int memorysize) {
		setProcess_id(process_id);
		setMemorysize(memorysize);
		setStart(start);
	}
	public int getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}
	public void show(){
		System.out.println(process_id+"\t"+start+"\t"+getEnd()+"\t"+memorysize);
	}
	public int getProcess_id() {
		return process_id;
	}
	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd(){
		return start+memorysize-1;
	}
	public Vector<Object> getRow(){
		Vector<Object> objects=new Vector<>();
		objects.add(Integer.toString(process_id));
		objects.add(Integer.toString(start));
		objects.add(Integer.toString(getEnd()));
		objects.add(Integer.toString(memorysize));
		return objects;
	}
}
