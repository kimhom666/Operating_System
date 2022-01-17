package processScheduling;
import models.Process_;
public class PrioritySchedulingAlgorithm implements ProcessSchedulingInterface{
	@Override
	public int compare(Process_ o1, Process_ o2) {
		return o1.getPriority()-o2.getPriority();
	}
	public String toString() {
		return "优先级调度";
	}
	@Override
	public int getIndex() {
		return 3;
	};
}
