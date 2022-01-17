package pageReplacement;
import models.Process_;
public interface PageReplacementInterface {
	/**
	 * 执行页面置换算法
	 * @param p 进程对象
	 * @return 是否缺页
	 */
	public boolean run(Process_ p);
}
