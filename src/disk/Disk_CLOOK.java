package disk;
import java.util.ArrayList;
import java.util.Collections;

import objects.Process;
import scheduler.Scheduler;
public class Disk_CLOOK {
	ArrayList<Integer> order;
	ArrayList<Integer> toSort;
	int start, last;
	boolean up;
	public Disk_CLOOK(int last, ArrayList<Integer> p, boolean up){
		order=new ArrayList<>();
		this.last = last;
		this.up = up;
		toSort=p;
		start=toSort.get(0);
		Collections.sort(toSort);
		calcDistance();
	}
	public ArrayList<Integer> getOrder(){
		return this.order;
	}
	public void calcDistance(){
		int current=Collections.binarySearch(toSort, start);
		int left=current;
		while(current < toSort.size()){
			order.add(toSort.get(current++));
		}
		current=0;
		while(current < left){
			order.add(toSort.get(current++));
		}
	}	
}
