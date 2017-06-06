package objects;

import java.util.ArrayList;

public class Process {
	Resource resource;
	int ID;
	int arrival, prio, remaining[] = new int[6], current;
	ArrayList<Integer> cylind;
	boolean isReady, job = false, done = false;
	
	public Process(int ID, int arrival, int prio, Resource resource,ArrayList<Integer> cylind){
		this.cylind=cylind;
		this.ID = ID;
		this.arrival = arrival;
		this.prio = prio;
		this.resource = resource;
		for(int k = 0; k < 6; k++){
			this.remaining[k] = resource.getAlloc()[0];
		}
		isReady = false;
		current = 0;
	}
	
	public int getR(int i){
		return this.remaining[i];
	}
	
	public int getCur(){
		return current;
	}
	
	public void setCur(int current){
		this.current = current;
	}
	
	
	public void setCylinder(ArrayList<Integer> cyc){
		this.cylind=cyc;
	}
	public ArrayList<Integer> getCylinder(){
		return this.cylind;
	}
	public void setDone(){
		done = true;
	}
	
	public boolean done(){
		return done;
	}
	
	public void setJob(){
		job = true;
	}
	
	public boolean toJob(){
		return job;
	}

	public int getID(){
		return ID;
	}
	
	public int getArrival(){
		return arrival;
	}
	
	public int getPrio(){
		return prio;
	}
	
	public Resource getResource(){
		return resource;
	}
	
	public void setResource(Resource resource){
		this.resource = resource;
	}
	
	public void setReady(){
		isReady = true;
	}
	
	public boolean isReady(){
		return isReady;
	}
}