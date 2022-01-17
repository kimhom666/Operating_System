package processScheduling;
import models.Process_;
public class FirstComeFirstServed implements ProcessSchedulingInterface {
	@Override
	public int compare(Process_ o1, Process_ o2) {
		return o1.getProcess_id()-o2.getProcess_id();
	}
	public String toString() {
		return "先来先服务";
	}
	@Override
	public int getIndex() {
		return 0;
	};
}
