package scheduler;

import java.util.ArrayList;

import objects.Process;

public class Bankers{
	
	Scheduler sched;

	public Bankers(Scheduler sched){
		this.sched = sched;
	}

	public void bank(int i) {
		boolean valid;
		int need[], j;
		
		ArrayList<Process> ready = new ArrayList<Process>();
		ArrayList<Process> notReady = new ArrayList<Process>();
		//sched.job[i].clear();
		
		while(!sched.job[i].isEmpty()){
			Process p = sched.job[i].dequeue();
			need = p.getResource().getNeed();
			j = 0;
			valid = true;
			while(j < need.length){
				//System.out.println("P" + p.getID() + " : " + need[j] + " >? " + sched.avail[j]);
				if(need[j] > sched.avail[j])
					valid = false;
				j++;
			}
			if(valid == true){
				p.setReady();
				//sched.updateAvail(i);
				//sched.job[i].enqueue(p);
				//processes.remove(processes.indexOf(p));
				//break;
				//System.out.println("P" + p.getID() + " is ready");
				ready.add(p);
			}
			else
				notReady.add(p);
		}
		
		sched.job[i].clear();
		
		for(Process p: ready)
			sched.job[i].enqueue(p);
		for(Process p: notReady)
			sched.job[i].enqueue(p);
	}

}
