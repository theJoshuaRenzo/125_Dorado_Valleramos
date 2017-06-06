package algos;

import java.util.ArrayList;

import objects.Process;
import scheduler.Scheduler;
import view.GUI;

public class FCFS {
	GUI gui;
	Scheduler sched;
	Process array[];
	
	public FCFS(GUI gui, Scheduler sched){
		this.gui = gui;
		this.sched = sched;
		
	}

	public void sort() {
		ArrayList<Process> processes = new ArrayList<Process>(sched.ready[0].getList());
		array = processes.toArray(new Process[processes.size()]);
		sched.ready[0].clear();
		
		quickSort(0, array.length-1);
		
		//System.out.println("FCFS");
		for(Process p: array){
			//System.out.println(p.getResource().getAlloc()[0] + " heeeeeey");
			//System.out.println(p.getID() + " " + p.getArrival());
			sched.ready[0].enqueue(p);
		}
	}
	
	private void quickSort(int left, int right){
        int l = left, r = right;
        int pivot = array[left + (right-left)/2].getArrival();
        
        while (l <= r){

            while (array[l].getArrival() < pivot)
            	l++;
            while (array[r].getArrival() > pivot)
            	r--;

            if (l <= r){
            	exchange(l, r);
            	l++;
            	r--;
            }
        }

        if (left < r)
        	quickSort(left, r);
        if (l < right)
        	quickSort(l, right);
	}

	private void exchange(int l, int r){
		Process temp = array[l];
		
		array[l] = array[r];
		array[r] = temp;
	}

}
