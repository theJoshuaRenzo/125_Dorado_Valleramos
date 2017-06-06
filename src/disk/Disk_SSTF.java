package disk;
import java.util.ArrayList;
import java.util.Collections;

import objects.Process;
import scheduler.Scheduler;
public class Disk_SSTF {
	ArrayList<Integer> order;
	ArrayList<Integer> toSort;
	int start;
	public Disk_SSTF(ArrayList<Integer> p){
		order=new ArrayList<>();
		toSort= new ArrayList<Integer>(p);	
		start= toSort.get(0);
		Collections.sort(toSort);
		calcDistance();
	}
	public ArrayList<Integer> getOrder(){
		return this.order;
	}
	public void calcDistance(){
		int current=Collections.binarySearch(toSort, start);
		order.add(toSort.get(current));
		int right,left;
		do{
			right=-1;
			left=-1;
			if(current>0)
				left=toSort.get(current)-toSort.get(current-1);
			if(current<toSort.size()-1)
				right=toSort.get(current+1)-toSort.get(current);
			toSort.remove(toSort.get(current));
			if(left!=-1&&right!=-1){
				if(left<right)
					current-=1;
			}
			else if(right==-1&&left==-1)
				break;
			else{
				if(left!=-1&&right==-1)
					current-=1;
			}
			order.add(toSort.get(current));
		}while(true);
	}
}
