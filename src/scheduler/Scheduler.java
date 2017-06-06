package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.JOptionPane;

import algos.FCFS;
import algos.NPPrio;
import algos.Prio;
import algos.RR;
import algos.SJF;
import algos.SRTF;
import disk.*;
import objects.Resource;
import queues.Job;
import queues.Ready;
import view.GUI;
import objects.Process;

public class Scheduler implements Runnable{
	
	GUI gui;
	Bankers banker;
	public Job job[] = new Job[6];
	public Ready ready[] = new Ready[6];
	
	FCFS fcfs;
	NPPrio npPrio;
	Prio prio;
	SJF sjf;
	SRTF srtf;
	RR rr;
	
	Disk_CLOOK clook;
	Disk_SCAN scan;
	Disk_CSCAN cscan;
	Disk_FCFS dfcfs;
	Disk_LOOK look;
	Disk_SSTF sstf;
	
	public ArrayList<Process> processes;
	public ArrayList<Process>[] diskProcesses;
	public int avail[], cpuTime, timeArr[] = new int[6], fin;
	
	boolean running[] = new boolean[6], waiting[] = new boolean[6], onRun, stop;
	public boolean resume = false;
	public int diskRange;
	int k, alloc[], qTime, cTime, prioR, srtfR, b = 0;
	int wait[] = new int[6], turn[] = new int[6], prevAvail[];
	int waitTime[] = new int[6], turnTime[] = new int[6], index[] = new int[6];
	Process p[] = new Process[6];
	

	public Scheduler(GUI gui) {
		this.gui= gui;
		
		processes = new ArrayList<Process>();
		diskProcesses=new ArrayList[6];
		onRun = false;
		stop = false;
		prioR = 0;
		srtfR = 0;
		cpuTime = 0;
		fin = 0;
		for(k = 0; k < 6; k++){
			running[k] = false;
			timeArr[k] = 0;
			waitTime[k] = 0;
			turnTime[k] = 0;
			wait[k] = 0;
			turn[k] = 1;
			p[k] = null;
			diskProcesses[k] = new ArrayList<Process>();
		}
		//randomizing values of diskCylinder for each processes
		store();
	}

	private void store() {
		int row, col, row2;
		
		Process arrayP[] = new Process[gui.rows];

		for(row = 0; row < gui.rows; row++){
			
			int[] alloc = new int[gui.cols];
			int[] max = new int[gui.cols];
			int[] need = new int[gui.cols];
			
			for(col = 0; col < gui.cols; col++){
				alloc[col] = Integer.parseInt(gui.allocText[row][col].getText());
				max[col] = Integer.parseInt(gui.maxText[row][col].getText());
				need[col] = max[col] - alloc[col];
				
			}
			
			int id = row;
			ArrayList<Integer> ran = new ArrayList<Integer>();
			int a = Integer.parseInt(gui.procText[row][0].getText());
			int p = Integer.parseInt(gui.procText[row][1].getText());
			diskRange=2000;
			int dMin = (2000 / gui.rows) * row;
			int dMax = dMin + (2000 / gui.rows);
			
			for(k = 0; k <= alloc[0]; k++){
				int num = (int) (Math.random()*((dMax-1)-dMin)+dMin);
				if(!ran.isEmpty() || !ran.contains(num)){
					////system.out.println(id + ": " + num);
					ran.add(num);
				}
				else if(ran.contains(num))
					k--;
			}
			
			
			Process process = null;
			process = new Process(id, a, p, new Resource(alloc, max, need),ran);
				

			
			arrayP[row] = process;
			
		}
		alloc = new int[gui.rows];
		
		for(row = 0; row < gui.rows; row++){
			for(row2 = 0; row2 < gui.rows; row2++){
				if(arrayP[row].getArrival() < arrayP[row2].getArrival()){
					Process p = arrayP[row];
					arrayP[row] = arrayP[row2];
					arrayP[row2] = p;
				}
			}
			alloc[row] = Integer.parseInt(gui.allocText[row][0].getText());
			////system.out.println(alloc[row] + " heeeeeey");
		}
		
		avail = new int[gui.cols];
		prevAvail = new int[gui.cols];
		
		
		for(col = 0; col < gui.cols; col++){
			avail[col] = Integer.parseInt(gui.firstAvail[col].getText());
			prevAvail[col] = Integer.parseInt(gui.firstAvail[col].getText());
		}

		Collections.addAll(processes, arrayP);
			
		for(Process p: processes){
			gui.totalT += p.getResource().getMax()[0];
		}

		fcfs = new FCFS(gui, this);
		npPrio = new NPPrio(gui, this);
		prio = new Prio(gui, this);
		sjf = new SJF(gui, this);
		srtf = new SRTF(gui, this);
		rr = new RR(gui, this);
		
		for(int k = 0; k < 6; k++){
			job[k] = new Job();
			ready[k] = new Ready();
		}
		
		banker = new Bankers(this);
	}

	public void run(){
		boolean av, went;
		int a[], b[];
		
		if(gui.rr.isSelected()){
			String s = JOptionPane.showInputDialog("Input Quantum Time: ");
			qTime = Integer.parseInt(s);
			cTime = qTime;
		}
		
		try {
		
			do{
				
				onRun = false;
				for(k = 0; k < 6; k++)
					waiting[k] = true;
				
				for(Process p: processes){
					if(p.getArrival() <= cpuTime && !p.toJob()){
						if(gui.fcfs.isSelected())
							toJob(0, p);
						if(gui.npPrio.isSelected())
							toJob(1, p);
						if(gui.prio.isSelected())
							toJob(2, p);
						if(gui.sjf.isSelected())
							toJob(3, p);
						if(gui.srtf.isSelected())
							toJob(4, p);
						if(gui.rr.isSelected())
							toJob(5, p);
						p.setJob();
					}
				}
				
				
				for(int row = 0; row < 6; row++){
					////system.out.println("job " + row);
					if(!job[row].isEmpty()){
						banker.bank(row);
						////system.out.print(row + " ");
						//job[row].showQueue();
						if(job[row].peek().isReady())
							ready[row].enqueue(job[row].dequeue());
						//ready[row].showQueue();
					}
				}
				
				
				if(gui.fcfs.isSelected() && !running[0] && !ready[0].isEmpty()){
					fcfs.sort();
					select(0);
				}
				if(gui.npPrio.isSelected() && !running[1] && !ready[1].isEmpty()){
					npPrio.sort();
					select(1);
				}
				if(gui.prio.isSelected() && !running[2] && !ready[2].isEmpty()){
					prio.sort();
					select(2);
				}
				if(gui.sjf.isSelected() && !running[3] && !ready[3].isEmpty()){
					sjf.sort();
					select(3);
				}
				if(gui.srtf.isSelected() && !running[4] && !ready[4].isEmpty()){
					srtf.sort();
					select(4);
				}
				if(gui.rr.isSelected() && !running[5] && !ready[5].isEmpty()){
					select(5);
				}
			
				if(running[0]){
					runIt(0);
				}
				if(running[1]){
					runIt(1);
				}
				if(running[2]){
					runIt(2);
				}
				if(running[3]){
					runIt(3);
				}
				if(running[4]){
					runIt(4);
				}
				if(running[5]){
					runIt(5);
				}
				
				if(gui.fcfs.isSelected()){
					updateQueue(0);
					waitMate(0);
				}
				if(gui.npPrio.isSelected()){
					updateQueue(1);
					waitMate(1);
				}
				if(gui.prio.isSelected()){
					updateQueue(2);
					waitMate(2);
				}
				if(gui.sjf.isSelected()){
					updateQueue(3);
					waitMate(3);
				}
				if(gui.srtf.isSelected()){
					updateQueue(4);
					waitMate(4);
				}
				if(gui.rr.isSelected()){
					updateQueue(5);
					waitMate(5);
				}
				
				av = true;
				went = false;
				int all = 0;
				
				for(Process p: processes){
					////system.out.println(p.getID());
					av = true;
					if(p.toJob()){
						all++;
						if(!p.done()){
							went = true;
							a = p.getResource().getNeed();
							b = p.getResource().getAlloc();
							for(k = 0; k < gui.cols; k++){
								////system.out.println(prevAvail[k] + " <? " + a[k]);
								if(prevAvail[k] < a[k]){
									av = false;
									break;
								}
							}
							if(av == true){
								updateAvail(p.getID(), b);
								p.setDone();
								break;
							}
						}
					}
				}
				
				////system.out.println(p[0].getResource().getAlloc()[0] + " heeeeeey");
				
				if(went && !av && all == processes.size()){
					////system.out.println("DEAD");
					JOptionPane.showMessageDialog(gui, "DEADLOCK DETECTED", null, JOptionPane.ERROR_MESSAGE);
					break;
				}
				
				if(gui.step.isSelected() && resume == false)
					this.wait();
				else if(gui.step.isSelected() && resume == true){
					this.notify();
					resume = false;
				}
				else
					Thread.sleep(gui.sleepTime);
					cpuTime++;
					
				for(k = 0; k < 6; k++){
					if(gui.time[k] != null)
						gui.time[k].setText(String.valueOf(cpuTime));
				}
				
			
			
			}while(fin < gui.rows*gui.fin && !stop);
		
			for(k = 0; k < 6; k++){
				if(gui.stat[k] != null)
					gui.stat[k].setText("Done");
			}
			
		} catch (InterruptedException | ConcurrentModificationException e) {
		}

	}


	private void updateQueue(int i) {
		ArrayList<Process> j = job[i].getList();
		ArrayList<Process> r = ready[i].getList();
		
		for(k = 0; k < 20; k++){
			if(!j.isEmpty())
				gui.jobQ[i][k].setText("P" + String.valueOf(j.remove(0).getID()));
			else
				gui.jobQ[i][k].setText("");
			if(!r.isEmpty())
				gui.readyQ[i][k].setText("P" + String.valueOf(r.remove(0).getID()));
			else
				gui.readyQ[i][k].setText("");
		}
	}

	private void waitMate(int i) {
		if(waiting[i]){
			wait[i]++;
			waitTime[i]++;
			gui.waitT[i].setText(String.valueOf(waitTime[i]));
			gui.stat[i].setText("Waiting...");
		}
		else{
			turn[i]++;
			turnTime[i]++;
			gui.turnT[i].setText(String.valueOf(turnTime[i]));
			gui.stat[i].setText("Processing " + p[i].getID());
		}
		
	}

	private void select(int i) {
		p[i] = ready[i].dequeue();
		////system.out.println(p[i].getResource().getAlloc()[0] + " heeeeeey");
		
		if(wait[i] > 0){
			gui.addGantt(i, wait[i], true, "wait", p[i].getCylinder(), p[i].getID());
			//gui.updateS(i, wait[i]);
		}
		
		wait[i] = 0;
		running[i] = true;
	}

	private void runIt(int i) {
		onRun = true;
		waiting[i] = false;
		
		boolean in = false;
		p[i].getResource().getAlloc()[0]--;
		
		if(gui.rr.isSelected() && i == 5){
			cTime--;
			////system.out.println(cTime + " ... " + p[i].getResource().getAlloc()[0] + " sdas " + turn[i]);
		}
		
		if(gui.rr.isSelected() && cTime == 0 && p[i].getResource().getAlloc()[0] > 0 && i == 5){
			////system.out.println(cTime + " heeeeeey " + p[i].getResource().getAlloc()[0] + " sdas " + turn[i]);
			////system.out.println(running[i] + " heeeeeey ");
			running[i] = false;
			cTime = qTime;
			
			//alloc = p[i].getResource().getAlloc();
			//for(k = 0; k < alloc.length; k++)
			//	avail[k] += qTime;
			
			ready[5].enqueue(p[i]);
			in = true;
		}
		else if(gui.prio.isSelected() && !ready[2].isEmpty() && p[i].getResource().getAlloc()[0] > 0 && i == 2){
			prio.sort();
			if(p[i].getPrio() > ready[2].peek().getPrio()){
				ready[2].enqueue(p[i]);
				
			//	alloc = p[i].getResource().getAlloc();
			//	for(k = 0; k < alloc.length; k++)
			//		avail[k] += prioR;
				
				running[i] = false;
				prioR = 0;
				in = true;
			}
			
			prioR++;
		}
		else if(gui.srtf.isSelected() && !ready[4].isEmpty() && p[i].getResource().getAlloc()[0] > 0 && i == 4){
			srtf.sort();
			if(p[i].getResource().getAlloc()[0] > ready[4].peek().getResource().getAlloc()[0]){
				ready[4].enqueue(p[i]);
				
		//		alloc = p[i].getResource().getAlloc();
		//		for(k = 0; k < alloc.length; k++)
		//			avail[k] += srtfR;
				
				running[i] = false;
				srtfR = 0;
				in = true;
			}
			
			srtfR++;
		}
		else if(p[i].getResource().getAlloc()[0] <= 0){
			running[i] = false;
			fin++;
			
			//alloc = p[i].getResource().getAlloc();
			////system.out.println(p[i].getResource().getAlloc()[0] + " heeeeeey");
			for(k = 0; k < p[i].getResource().getSubAlloc().length; k++){
				////system.out.println(p[i].getID() + ": " + avail[k] + " ... " + p[i].getResource().getSubAlloc()[k]);
				avail[k] += p[i].getResource().getSubAlloc()[k];
			}
			
			//updateAvail(i);
			cTime = qTime;
			in = true;
		}
		
		if(turn[i] > 0 && in == true){
			////system.out.println(turn[i] + " heeeeeey");
			////system.out.println(p[i].getCylinder());
			ArrayList arr = new ArrayList<Process>();
			ArrayList sub = new ArrayList<>(p[i].getCylinder());
			int cur = p[i].getCur();
			////system.out.println("Start " + sub);
			for(k = 0; k <= turn[i]; k++){
				////system.out.println(cur + " " + sub.get(cur));
				arr.add(sub.get(cur++));
			}
			
			p[i].setCur(cur-1);
			if(gui.dSCAN.isSelected()){
				scan = new Disk_SCAN(gui.last[i], arr, gui.up[i]);
				arr = scan.getOrder();
			}
			if(gui.cSCAN.isSelected()){
				cscan = new Disk_CSCAN(gui.last[i], arr, gui.up[i]);
				arr = cscan.getOrder();
			}
			if(gui.dLOOK.isSelected()){
				look = new Disk_LOOK(gui.last[i], arr, gui.up[i]);
				arr = look.getOrder();
			}
			if(gui.cLOOK.isSelected()){
				clook = new Disk_CLOOK(gui.last[i], arr, gui.up[i]);
				arr = clook.getOrder();
			}
			if(gui.dSSTF.isSelected()){
				sstf = new Disk_SSTF(arr);
				arr = sstf.getOrder();
			}
			
			gui.addGantt(i, turn[i], false, "P" + String.valueOf(p[i].getID()), arr, p[i].getID());
			diskProcesses[i].add(p[i]);
			/*
			if(gui.dFCFS.isSelected()){
				dfcfs = new Disk_FCFS(p[i].getCylinder());
				p[i].setCylinder(dfcfs.getOrder());
			}
			if(gui.dSCAN.isSelected()){
				scan = new Disk_SCAN(this, p[i].getCylinder());
				p[i].setCylinder(scan.getOrder());
			}
			if(gui.cSCAN.isSelected()){
				cscan = new Disk_CSCAN(this, p[i].getCylinder());
				p[i].setCylinder(cscan.getOrder());
			}
			if(gui.dLOOK.isSelected()){
				look = new Disk_LOOK(p[i].getCylinder());
				p[i].setCylinder(look.getOrder());
			}
			if(gui.cLOOK.isSelected()){
				clook = new Disk_CLOOK(p[i].getCylinder());
				p[i].setCylinder(clook.getOrder());
			}
			if(gui.dSSTF.isSelected()){
				sstf = new Disk_SSTF(p[i].getCylinder());
				p[i].setCylinder(sstf.getOrder());
			}
			*/
			////system.out.println(p[i].getCylinder());
			
			//gui.addDataset(i, cpuTime, p[i].getCylinder(), p[i].getR(i), p[i].getID());
			
			
			turn[i] = 0;
		}
		
		//if(running[i] && in == false)
		////system.out.println(p[i].getResource().getAlloc()[i] + " heeeeeey");
		
	}
	
	private void toJob(int i, Process p2){
		////system.out.println(p2.getID() + " jobbed at " + i);
		int id = p2.getID();
		int a = p2.getArrival(), b = p2.getPrio();
		int alloc[] = new int[p2.getResource().getAlloc().length];
		int max[] = new int[p2.getResource().getAlloc().length];
		int need[] = new int[p2.getResource().getAlloc().length];
		
		for(k = 0; k < p2.getResource().getAlloc().length; k++){
			alloc[k] = p2.getResource().getAlloc()[k];
			max[k] = p2.getResource().getMax()[k];
			need[k] = p2.getResource().getNeed()[k];
			////system.out.println("   " + alloc[k]);
		}
		
		Resource r = new Resource(alloc, max, need);
		
		Process it = new Process(id, a, b, r,p2.getCylinder());
				
		job[i].enqueue(it);
	}
	
	public void updateAvail(int i, int[] b){
		for(k = 0; k < gui.cols; k++){
			////system.out.println(i + ": " + prevAvail[k] + " ... " + b[k]);
			prevAvail[k] += b[k];
			gui.availText[i][k].setText(String.valueOf(prevAvail[k]));
		}
	}

	public void kill() {
		stop = true;
	}
}