package disk;
import java.util.ArrayList;
import objects.Process;
import scheduler.Scheduler;
public class Disk_FCFS {
	ArrayList<Integer> order;
	public Disk_FCFS(ArrayList<Integer> p){
		order=p;
	}
	public ArrayList<Integer> getOrder(){
		return this.order;
	}
	
}
