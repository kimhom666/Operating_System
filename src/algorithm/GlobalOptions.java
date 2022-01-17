package algorithm;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import memoryAllocation.BestFit;
import memoryAllocation.FirstFit;
import memoryAllocation.WorstFit;
import models.Memory_;
import models.Process_;
import pageReplacement.FirstInputFirstOutput;
import pageReplacement.LeastRecentlyUsed;
import pageReplacement.PageReplacementInterface;
import processScheduling.FirstComeFirstServed;
import processScheduling.HighestResponseRatioNext;
import processScheduling.PrioritySchedulingAlgorithm;
import processScheduling.ShortestJobFirst;
public class GlobalOptions {
	public static Object[] memoryComparators=new Object[]{
			new FirstFit(),
			new BestFit(),
			new WorstFit()
	};
	public static Comparator<Memory_> memory_comparator=null;
	public static Object[] processComparators=new Object[]{
			new PrioritySchedulingAlgorithm(),
			new FirstComeFirstServed(),
			new ShortestJobFirst(),
			new HighestResponseRatioNext()
	};
	public static Comparator<Process_> ready_comparator=null;
	public static Comparator<Process_> block_comparator=null;
	public static Comparator<Memory_> firstfit(){
		return (Comparator<Memory_>) memoryComparators[0];
	}
	public static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	public static String createtime(){
		Date now =new Date();
		return ft.format(now);
	}
	public static Object[] pageReplacements=new Object[]{
			new LeastRecentlyUsed(),
			new FirstInputFirstOutput()
	};
	public static PageReplacementInterface pageReplacement=null;
}
