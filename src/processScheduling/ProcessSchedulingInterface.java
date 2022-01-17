package processScheduling;
import java.util.Comparator;
import models.Process_;
public interface ProcessSchedulingInterface extends Comparator<Process_> {
	/**
	 * 获取进程管理器表格中要强调的列下标
	 * @return
	 */
	public int getIndex();
}
