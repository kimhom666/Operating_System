package memoryAllocation;
import java.util.Comparator;
import models.Memory_;
public class WorstFit implements Comparator<Memory_> {
	@Override
	public int compare(Memory_ o1, Memory_ o2) {
		return o2.getMemorysize()-o1.getMemorysize();
	}
	@Override
	public String toString() {
		return "最坏适应算法";
	}
}
