package disk;
import java.util.ArrayList;
import java.util.Collections;

import objects.Process;
import scheduler.Scheduler;
public class Disk_LOOK {
	ArrayList<Integer> order;
	ArrayList<Integer> toSort;
	int start, last;
	boolean up;
	public Disk_LOOK(int last, ArrayList<Integer> p, boolean up){
		this.last = last;
		this.up = up;
		order=new ArrayList<>();
		//System.out.println("P = " + p);
		toSort=p;
		start=toSort.get(0);
		Collections.sort(toSort);
		calcDistance();
	}
	public ArrayList<Integer> getOrder(){
		return this.order;
	}
	public void calcDistance(){
		int current = Collections.binarySearch(toSort, start);
		int left = current;
		
		while(current < toSort.size()){
			order.add(toSort.get(current++));
		}
		//order.add(toSort.get(++current));
		current=left-1;
		while(current >= 0){
			order.add(toSort.get(current--));
		}
		//System.out.println("order = " + order);
	}
}
