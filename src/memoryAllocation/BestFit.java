package memoryAllocation;
import java.util.Comparator;
import models.Memory_;
public class BestFit implements Comparator<Memory_> {
	@Override
	public int compare(Memory_ o1, Memory_ o2) {
		return o1.getMemorysize()-o2.getMemorysize();
	}
	@Override
	public String toString() {
		return "最佳适应算法";
	}
}
