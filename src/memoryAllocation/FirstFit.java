package memoryAllocation;
import java.util.Comparator;
import models.Memory_;
public class FirstFit implements Comparator<Memory_> {
	@Override
	public int compare(Memory_ o1, Memory_ o2) {
		return o1.getStart()-o2.getStart();
	}
	@Override
	public String toString() {
		return "首次适应算法";
	}
}
