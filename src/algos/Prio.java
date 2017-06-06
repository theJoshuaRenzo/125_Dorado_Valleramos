package algos;

import java.util.ArrayList;

import objects.Process;
import scheduler.Scheduler;
import view.GUI;

public class Prio{
	GUI gui;
	Scheduler sched;
	Process array[];
	
	public Prio(GUI gui, Scheduler sched){
		this.gui = gui;
		this.sched = sched;
	}

	public void sort() {
		ArrayList<Process> processes = new ArrayList<Process>(sched.ready[2].getList());
		array = processes.toArray(new Process[processes.size()]);
		sched.ready[2].clear();
		
		quickSort(0, array.length-1);
		
		for(Process p: array){
			sched.ready[2].enqueue(p);
		}
	}
	
	private void quickSort(int left, int right){
        int l = left, r = right;
        int pivot = array[left + (right-left)/2].getPrio();
        
        while (l <= r){

            while (array[l].getPrio() < pivot)
            	l++;
            while (array[r].getPrio() > pivot)
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
