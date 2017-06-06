package disk;
import java.util.ArrayList;
import java.util.Collections;

import objects.Process;
import scheduler.Scheduler;
public class Disk_SCAN {
	ArrayList<Integer> order;
	ArrayList<Integer> toSort;
	int start, last;
	boolean up;
	public Disk_SCAN(int last, ArrayList<Integer> p, boolean up){
		order=new ArrayList<>();
		this.last = last;
		this.up = up;
		toSort=new ArrayList<Integer>(p);
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
		order.add(toSort.get(current));
		int right=current+1;
		while(right<toSort.size()-1){
			order.add(toSort.get(++current));
			right++;
		}
		//order.add(toSort.get(++current));
		order.add(2000);
		current=left-1;
		while(left>0){
			order.add(toSort.get(current--));
			left--;
		}
	}
}
