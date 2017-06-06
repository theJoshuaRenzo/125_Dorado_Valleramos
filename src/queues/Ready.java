package queues;

import java.util.ArrayList;

import objects.Process;
import objects.Resource;

public class Ready {
	private ArrayList<Process> processes;
	
	public Ready(){
		processes = new ArrayList<Process>();
	}
	
	public void enqueue(Process process){
		processes.add(process);
	}
	
	public Process dequeue(){
		return processes.remove(0);
	}
	
	public Process peek(){
		return processes.get(0);
	}
	
	public boolean isEmpty(){
		if(processes.isEmpty())
			return true;
		return false;
	}
	
	public ArrayList<Process> getList(){
		ArrayList<Process> list = new ArrayList<Process>();
		for(Process p: processes){
			int id = p.getID();
			int a = p.getArrival(), b = p.getPrio();
			int alloc[] = new int[p.getResource().getAlloc().length];
			int max[] = new int[p.getResource().getAlloc().length];
			int need[] = new int[p.getResource().getAlloc().length];
			
			for(int k = 0; k < p.getResource().getAlloc().length; k++){
				alloc[k] = p.getResource().getAlloc()[k];
				max[k] = p.getResource().getMax()[k];
				need[k] = p.getResource().getNeed()[k];
			}
			
			Resource r = new Resource(alloc, max, need);
			
			Process it = new Process(id, a, b, r, p.getCylinder());
			list.add(it);
		}
		
		return list;
	}
	
	public void clear(){
		processes.clear();
	}
	
	public void showQueue(){
		
		System.out.println("Ready Queue");
		for(Process p: processes)
			System.out.println("     ID: " + p.getID());
		
	}
}
