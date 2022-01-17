package processScheduling;
import models.Process_;
public class HighestResponseRatioNext implements ProcessSchedulingInterface {
	@Override
	public int compare(Process_ o1, Process_ o2) {
		if(o1.getRp()>=o2.getRp()) return -1;
		return 1;
	}
	public String toString() {
		return "高响应比优先";
	}
	@Override
	public int getIndex() {
		return 7;
	};
}
