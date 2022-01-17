package processScheduling;
import models.Process_;
public class ShortestJobFirst implements ProcessSchedulingInterface {
	@Override
	public int compare(Process_ o1, Process_ o2) {
		return o1.getCost()-o2.getCost();
	}
	public String toString() {
		return "短作业优先";
	}
	@Override
	public int getIndex() {
		return 5;
	};
}
