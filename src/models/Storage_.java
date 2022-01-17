package models;
import java.util.ArrayList;
import java.util.Vector;
public class Storage_ {
	public static int fat[];
	public static final int bitUsed=1,bitUnused=0,fatUnused=-1,fatEnd=-2;
	private static int dbsize;//每个盘块的大小
	public static Vector<Object> name=null;
	public static int bitmap[][];
	public static int row;
	public static int col;
	public static Vector<Object> name_bit=null;
	public Storage_(int row,int col,int dbsize){//盘块个数和盘块大小
		Storage_.row=row;
		Storage_.col=col;
		int dblength=row*col;
		fat=new int[dblength];
		for(int i=0;i<dblength;i++) fat[i]=fatUnused;
		bitmap=new int[row][col];
		Storage_.dbsize=dbsize;
		name=new Vector<>();
		name.add("盘块号");
		name.add("下一盘块号");
		name_bit=new Vector<>();
		name_bit.add("");
		for(int i=0;i<col;i++) name_bit.add(i);
	}
	/**
	 * 根据数组更新为fat表和位图
	 * @param res
	 */
	private static void updatefat(ArrayList<Integer> res) {
		int count=res.size();
		int temp;
		res.add(-2);
		for(int i=0;i<count;i++){
			temp=res.get(i);
			fat[temp]=res.get(i+1);
			bitmap[temp/col][temp%col]=bitUsed;
		}
	}
	/**
	 * 为文件分配空间
	 * @param size 需要的文件大小
	 * @return 若分配成功返回fat表头，若失败返回-1
	 */
	public static int allocate(int size){
		int count=(size-1)/dbsize+1;
		ArrayList<Integer> res=findEmpty(count);
		if(res.size()==count){
			updatefat(res);
			return res.get(0);
		}
		else return -1;
	}
	private static ArrayList<Integer> findEmpty(int count){
		ArrayList<Integer> res=new ArrayList<Integer>();
		for(int i=0;i<fat.length&&count!=0;i++){
			if(fat[i]==fatUnused){
				--count;
				res.add(i);
			}
		}
		return res;
	}
	public static void delete(int fcb) {
		int i;
		while(fcb!=fatEnd){
			i=fcb;
			fcb=fat[i];
			fat[i]=fatUnused;
			bitmap[i/col][i%col]=bitUnused;
		}
	}
	public static Vector<Vector<Object>> gettable_fat(){
		Vector<Vector<Object>> vectors=new Vector<>(fat.length);
		for(int i=0;i<fat.length;i++){
			Vector<Object> objects=new Vector<>();
			objects.add(Integer.toString(i));
			objects.add(Integer.toString(fat[i]));
			vectors.add(objects);
		}
		return vectors;
	}
	public static Vector<Vector<Object>> gettable_bit(){
		Vector<Vector<Object>> vectors=new Vector<>(row);
		for(int i=0;i<col;i++){
			Vector<Object> objects=new Vector<>();
			objects.add(Integer.toString(i));
			for(int j=0;j<col;j++){
				objects.add(Integer.toString(bitmap[i][j]));
			}
			vectors.add(objects);
		}
		return vectors;
	}
}
