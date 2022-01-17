package pageReplacement;
import java.util.ArrayList;
import java.util.LinkedList;
import models.Process_;
public class LeastRecentlyUsed implements PageReplacementInterface {
	@Override
	public boolean run(Process_ p) {
		ArrayList<Integer> pagelist=p.pagelist;
		LinkedList<Integer> havelist=p.havelist;
		boolean flag=true;
		int page=pagelist.get(p.getPc());
		if(!havelist.contains(page)) {
			flag=false;
			if(havelist.size()==p.getMaxpage()) havelist.removeFirst();
		}
		else havelist.remove(havelist.indexOf(page));
		havelist.add(page);
		return flag;
	}
	@Override
	public String toString() {
		return "最近最久未使用";
	}
}
