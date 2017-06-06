package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import scheduler.Scheduler;

public class GUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	Font font = new Font("Arial", Font.BOLD, 10);
	Font font2 = new Font("Arial", Font.BOLD, 10);
	
	public int rows, cols, sleepTime, fin = 0, left[] = new int[6], x[] = new int[6], totalT = 0, tex = 0;
	public boolean pause = false, pausing = false;
	
	public JPanel menuPanel = new JPanel();
	
	JTextField pText = new JTextField("5");
	JTextField rText = new JTextField("1");
	
	public JCheckBox fcfs = new JCheckBox("FCFS");
	public JCheckBox npPrio = new JCheckBox("NP-Prio");
	public JCheckBox prio = new JCheckBox("Prio");
	public JCheckBox sjf = new JCheckBox("SJF");
	public JCheckBox srtf = new JCheckBox("SRTF");
	public JCheckBox rr = new JCheckBox("RR");
	
	public JRadioButton step = new JRadioButton("STEP");
	JRadioButton slow = new JRadioButton("SLOW");
	JRadioButton fast = new JRadioButton("FAST");
	
	ButtonGroup speed = new ButtonGroup();
	
	JButton newButt = new JButton("New");
	JButton addButt = new JButton("Add");
	JButton runButt = new JButton("Run");
	JButton stopButt = new JButton("Stop");
	public JButton nextButt = new JButton("Next");

	public JPanel procPanel, allocPanel, maxPanel, availPanel;
	
	public JScrollPane procScroll, allocScroll, maxScroll, availScroll;
	
	JButton arrivButt = new JButton("Arrival");
	JButton prioButt = new JButton("Priority");
	JButton allocButt = new JButton("Allocation");
	JButton maxButt = new JButton("Maximum");
	JButton availButt = new JButton("Available");
	
	public JTextField procText[][] = new JTextField[20][2];
	public JTextField allocText[][] = new JTextField[20][20];
	public JTextField maxText[][] = new JTextField[20][20];
	public JTextField availText[][] = new JTextField[20][20];
	public JTextField firstAvail[] = new JTextField[20];
	
	public JPanel outPanel, fcfsPanel, npPrioPanel, prioPanel, sjfPanel, srtfPanel, rrPanel;
	public JScrollPane outScroll;
	
	JLabel j[] = new JLabel[6];
	JLabel r[] = new JLabel[6];
	JLabel g[] = new JLabel[6];
	JLabel t[] = new JLabel[6];
	JLabel wT[] = new JLabel[6];
	JLabel tT[] = new JLabel[6];
	
	public JTextField jobQ[][] = new JTextField[6][20];
	public JTextField readyQ[][] = new JTextField[6][20];
	public JTextField turnT[] = new JTextField[6];
	public JTextField waitT[] = new JTextField[6];
	public JTextField time[] = new JTextField[6];
	public JTextField stat[] = new JTextField[6];
	JTextField text[] = new JTextField[100];
	
	public JRadioButton dFCFS = new JRadioButton("FCFS");
	public JRadioButton dSSTF = new JRadioButton("SSTF");
	public JRadioButton dSCAN = new JRadioButton("SCAN");
	public JRadioButton cSCAN = new JRadioButton("C-SCAN");
	public JRadioButton dLOOK = new JRadioButton("LOOK");
	public JRadioButton cLOOK = new JRadioButton("C-LOOK");
	
	JTextField dText = new JTextField();
	
	ButtonGroup dAlgo = new ButtonGroup();
	
	JButton diskButt = new JButton("Start");
	
	JPanel diskPanel[] = new JPanel[6];
	
	JFreeChart chart[] = new JFreeChart[6];
	
	XYSeries series[] = new XYSeries[6];
	int s0 = 0, s1 = 0, s2 = 0, s3 = 0, s4 = 0, s5 = 0, s6 = 0;
	int r0[], r1[], r2[], r3[], r4[], r5[], r6[];
	int y0 = 0, y1 = 0, y2 = 0, y3 = 0, y4 = 0, y5 = 0;
	public int last[] = new int[6];
	public boolean up[] = new boolean[6];
	XYSeriesCollection dataset;
	
	Random rn;
	
	Scheduler sched;
	Thread schedT;
	
	public GUI(){
		
		super("Dorado, Valleramos");
		
		setLayout(null);
		setSize(1330, 650);
		setVisible(true);
		setLocationRelativeTo(null);
   		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		
   		r0 = new int[6];
		r1 = new int[6];
		r2 = new int[6];
		r3 = new int[6];
		r4 = new int[6];
		r5 = new int[6];
   		
   		for(int k = 0; k < 6; k++){
   			left[k] = 0;
   			x[k] = 100;
   			
   			r0[k] = 0;
   			r1[k] = 0;
   			r2[k] = 0;
   			r3[k] = 0;
   			r4[k] = 0;
   			r5[k] = 0;
   			//diskPanel[k] = new JPanel();
   		}
   		
		addMenuPanel();
		this.revalidate();
	}

	private void addMenuPanel() {
		
		menuPanel.setLayout(null);
		menuPanel.setBounds(5, 5, 200, 640);
		menuPanel.setBackground(Color.LIGHT_GRAY);
		add(menuPanel);
		
		JLabel mLabel = new JLabel("CPU Scheduler");
		mLabel.setBounds(40, 2, 200, 30);
		menuPanel.add(mLabel);
		
		JLabel pLabel = new JLabel("No. of Process: ");
		pLabel.setFont(font);
		pLabel.setBounds(20, 30, 100, 30);
		menuPanel.add(pLabel);
		
		pText.setBounds(135, 35, 30, 20);
		pText.setFont(font);
		pText.setEnabled(false);
		menuPanel.add(pText);
		
		JLabel rLabel = new JLabel("No. of Resource: ");
		rLabel.setFont(font);
		rLabel.setBounds(20, 50, 110, 30);
		menuPanel.add(rLabel);
		
		rText.setBounds(135, 55, 30, 20);
		rText.setFont(font);
		rText.setEnabled(false);
		menuPanel.add(rText);
		
		fcfs.setBounds(35, 80, 60, 30);
		fcfs.setFont(font);
		fcfs.setBackground(Color.LIGHT_GRAY);
		fcfs.addActionListener(this);
		menuPanel.add(fcfs);
		
		npPrio.setBounds(95, 80, 80, 30);
		npPrio.setFont(font);
		npPrio.setBackground(Color.LIGHT_GRAY);
		npPrio.addActionListener(this);
		menuPanel.add(npPrio);
		
		prio.setBounds(35, 105, 60, 30);
		prio.setFont(font);
		prio.setBackground(Color.LIGHT_GRAY);
		prio.addActionListener(this);
		menuPanel.add(prio);
		
		sjf.setBounds(95, 105, 80, 30);
		sjf.setFont(font);
		sjf.setBackground(Color.LIGHT_GRAY);
		sjf.addActionListener(this);
		menuPanel.add(sjf);
		
		srtf.setBounds(35, 130, 60, 30);
		srtf.setFont(font);
		srtf.setBackground(Color.LIGHT_GRAY);
		srtf.addActionListener(this);
		menuPanel.add(srtf);
		
		rr.setBounds(95, 130, 80, 30);
		rr.setFont(font);
		rr.setBackground(Color.LIGHT_GRAY);
		rr.addActionListener(this);
		menuPanel.add(rr);
		
		step.setBounds(5, 160, 65, 30);
		step.setFont(font);
		step.setBackground(Color.LIGHT_GRAY);
		step.setEnabled(false);
		step.addActionListener(this);
		menuPanel.add(step);
		
		slow.setBounds(65, 160, 65, 30);
		slow.setFont(font);
		slow.setBackground(Color.LIGHT_GRAY);
		slow.addActionListener(this);
		menuPanel.add(slow);
		
		fast.setBounds(130, 160, 65, 30);
		fast.setFont(font);
		fast.setBackground(Color.LIGHT_GRAY);
		fast.addActionListener(this);
		menuPanel.add(fast);
		
		speed.add(step);
		speed.add(slow);
		speed.add(fast);
		
		JLabel dLabel = new JLabel("DISK Scheduler");
		dLabel.setBounds(40, 190, 200, 30);
		menuPanel.add(dLabel);
		
		dFCFS.setBounds(35, 220, 65, 30);
		dFCFS.setFont(font);
		dFCFS.setBackground(Color.LIGHT_GRAY);
		dFCFS.addActionListener(this);
		menuPanel.add(dFCFS);
		
		dSSTF.setBounds(95, 220, 65, 30);
		dSSTF.setFont(font);
		dSSTF.setBackground(Color.LIGHT_GRAY);
		dSSTF.addActionListener(this);
		menuPanel.add(dSSTF);
		
		dSCAN.setBounds(35, 250, 65, 30);
		dSCAN.setFont(font);
		dSCAN.setBackground(Color.LIGHT_GRAY);
		dSCAN.addActionListener(this);
		menuPanel.add(dSCAN);
		
		cSCAN.setBounds(95, 250, 70, 30);
		cSCAN.setFont(font);
		cSCAN.setBackground(Color.LIGHT_GRAY);
		cSCAN.addActionListener(this);
		menuPanel.add(cSCAN);
		
		dLOOK.setBounds(35, 280, 65, 30);
		dLOOK.setFont(font);
		dLOOK.setBackground(Color.LIGHT_GRAY);
		dLOOK.addActionListener(this);
		menuPanel.add(dLOOK);
		
		cLOOK.setBounds(95, 280, 65, 30);
		cLOOK.setFont(font);
		cLOOK.setBackground(Color.LIGHT_GRAY);
		cLOOK.addActionListener(this);
		menuPanel.add(cLOOK);
		
		dAlgo.add(dFCFS);
		dAlgo.add(dSSTF);
		dAlgo.add(dSCAN);
		dAlgo.add(cSCAN);
		dAlgo.add(cLOOK);
		dAlgo.add(dLOOK);
		
		newButt.setBounds(50, 310, 100, 25);
		newButt.addActionListener(this);
		menuPanel.add(newButt);
		
		addButt.setBounds(50, 340, 100, 25);
		addButt.addActionListener(this);
		addButt.setEnabled(false);
		menuPanel.add(addButt);
		
		runButt.setBounds(50, 370, 100, 25);
		runButt.addActionListener(this);
		runButt.setEnabled(false);
		menuPanel.add(runButt);
		
		stopButt.setBounds(50, 400, 100, 25);
		stopButt.addActionListener(this);
		stopButt.setEnabled(false);
		menuPanel.add(stopButt);
		
		nextButt.setBounds(50, 430, 100, 25);
		nextButt.addActionListener(this);
		nextButt.setEnabled(false);
		menuPanel.add(nextButt);
		
	}
	
	
	private void random(int sel) {
		String rand;
		int rowTemp=rows;
		if(sel == 3)
			rowTemp = cols;
		for(int row = 0; row < rowTemp; row++){
			if(sel < 3){
				for(int col = 0; col < cols; col++){
					rn = new Random();
					if(sel == 1){
						rand = Integer.toString((int) (Math.random()*(19-1)+1));
						allocText[row][col].setText(rand);
					}else if(sel == 2){
						int temp = Integer.parseInt(allocText[row][col].getText());
						rand = Integer.toString((int) (Math.random()*(20-(temp+1))+(temp+1)));
						maxText[row][col].setText(rand);
					}
				}
			}else{
				rn = new Random();
				rand = Integer.toString(rn.nextInt(30+1)+0);
				if(sel == 3)
					firstAvail[row].setText(rand);
				if(sel == 4)
					procText[row][0].setText(rand);
				if(sel == 5)
					procText[row][1].setText(rand);
			}
		}
	}
	
		
	private void addPanels() {
		rows = Integer.parseInt(pText.getText());
		cols = Integer.parseInt(rText.getText());
		
		procPanel = new JPanel(new GridLayout(rows+1, 1, 0, 0));
		procPanel.setBackground(Color.LIGHT_GRAY);
		
		for(int row = 0; row <= rows ; row++){
			for(int col = 0; col < 2 ; col++){
				if(row == 0){
					if(col == 0){
						arrivButt.addActionListener(this);
						procPanel.add(arrivButt);
					}else if(col == 1){
						prioButt.addActionListener(this);
						procPanel.add(prioButt);
					}
				}else{
					procText[row-1][col]=new JTextField("0");
					procPanel.add(procText[row-1][col]);
				}
			}
		}
		
		procScroll = new JScrollPane(procPanel);
		procScroll.setBounds(210, 5, 200, 150);
		add(procScroll);
		
		allocPanel = new JPanel(new GridLayout(rows+1, cols+1, 0, 0));
		allocPanel.setBackground(Color.LIGHT_GRAY);
		
		maxPanel = new JPanel(new GridLayout(rows+1, cols+1, 0, 0));
		maxPanel.setBackground(Color.LIGHT_GRAY);
		
		availPanel = new JPanel(new GridLayout(rows+1, cols+1, 0, 0));
		availPanel.setBackground(Color.LIGHT_GRAY);
		
		for(int row = 0; row <= rows ; row++){
			for(int col = 0; col <= cols ; col++){
				if(row == 0){
					if(col == 0){					
						allocButt.addActionListener(this);
						allocPanel.add(allocButt);
						
						maxButt.addActionListener(this);
						maxPanel.add(maxButt);
						
						availButt.addActionListener(this);
						availPanel.add(availButt);
					}
					else{
						JLabel label = new JLabel("Resource " + (col-1));
						allocPanel.add(label);
						
						JLabel label2 = new JLabel("Resource " + (col-1));
						maxPanel.add(label2);
						
						////system.out.println("hey");
						firstAvail[col-1] = new JTextField();
						availPanel.add(firstAvail[col-1]);
					}
					
				}
				else if(col == 0 && row > 0){
					JLabel label = new JLabel("Process " + (row-1));
					allocPanel.add(label);
					
					JLabel label2 = new JLabel("Process " + (row-1));
					maxPanel.add(label2);
					
					JLabel label3 = new JLabel("Process " + (row-1));
					availPanel.add(label3);
				}
				else{
					allocText[row-1][col-1] = new JTextField("0");
					allocPanel.add(allocText[row-1][col-1]);
					
					maxText[row-1][col-1] = new JTextField("0");
					maxPanel.add(maxText[row-1][col-1]);
					
					availText[row-1][col-1] = new JTextField("-");
					availText[row-1][col-1].setEditable(false);
					availPanel.add(availText[row-1][col-1]);
				}
			}
		}
		
		allocScroll = new JScrollPane(allocPanel);
		allocScroll.setBounds(415, 5, 300, 150);
		add(allocScroll);
		
		maxScroll = new JScrollPane(maxPanel);
		maxScroll.setBounds(720, 5, 300, 150);
		add(maxScroll);
		
		availScroll = new JScrollPane(availPanel);
		availScroll.setBounds(1025, 5, 300, 150);
		add(availScroll);
		
		revalidate();
	}

	
	private void addPPanel(int s) {
		int h = 500, w = 3000;
		outPanel = new JPanel(new GridLayout(s, 0, 3, 3));
		outPanel.setVisible(true);
		
			if(fcfs.isSelected()){
				fin++;
				fcfsPanel = new JPanel();
				fcfsPanel.setLayout(null);
				fcfsPanel.setBackground(Color.LIGHT_GRAY);
				fcfsPanel.setPreferredSize(new Dimension(w, h));
				
				JLabel l = new JLabel("FCFS");
				l.setBounds(5, 5, 50, 20);
				
				addContent(0);
				
				fcfsPanel.add(l);
				fcfsPanel.add(stat[0]);
				fcfsPanel.add(j[0]);
				fcfsPanel.add(r[0]);
				fcfsPanel.add(g[0]);
				fcfsPanel.add(t[0]);
				fcfsPanel.add(time[0]);
				fcfsPanel.add(tT[0]);
				fcfsPanel.add(turnT[0]);
				fcfsPanel.add(wT[0]);
				fcfsPanel.add(waitT[0]);
				
				addTextQ(0);
				addDisk(0);
				outPanel.add(fcfsPanel);
			}
			if(npPrio.isSelected()){
				fin++;
				npPrioPanel = new JPanel();
				npPrioPanel.setLayout(null);
				npPrioPanel.setBackground(Color.LIGHT_GRAY);
				npPrioPanel.setPreferredSize(new Dimension(w, h));

				JLabel l = new JLabel("NPPrio");
				l.setBounds(5, 5, 50, 20);
				
				addContent(1);
				
				npPrioPanel.add(l);
				npPrioPanel.add(stat[1]);
				npPrioPanel.add(j[1]);
				npPrioPanel.add(r[1]);
				npPrioPanel.add(g[1]);
				npPrioPanel.add(t[1]);
				npPrioPanel.add(time[1]);
				npPrioPanel.add(tT[1]);
				npPrioPanel.add(turnT[1]);
				npPrioPanel.add(wT[1]);
				npPrioPanel.add(waitT[1]);
				
				addTextQ(1);
				addDisk(1);
				outPanel.add(npPrioPanel);
			}
			if(prio.isSelected()){
				fin++;
				prioPanel = new JPanel();
				prioPanel.setLayout(null);
				prioPanel.setBackground(Color.LIGHT_GRAY);
				prioPanel.setPreferredSize(new Dimension(w, h));
				
				JLabel l = new JLabel("Prio");
				l.setBounds(5, 5, 50, 20);
				
				addContent(2);
				
				prioPanel.add(l);
				prioPanel.add(stat[2]);
				prioPanel.add(j[2]);
				prioPanel.add(r[2]);
				prioPanel.add(g[2]);
				prioPanel.add(t[2]);
				prioPanel.add(time[2]);
				prioPanel.add(tT[2]);
				prioPanel.add(turnT[2]);
				prioPanel.add(wT[2]);
				prioPanel.add(waitT[2]);

				addTextQ(2);
				addDisk(2);
				outPanel.add(prioPanel);
			}
			if(sjf.isSelected()){
				fin++;
				sjfPanel = new JPanel();
				sjfPanel.setLayout(null);
				sjfPanel.setBackground(Color.LIGHT_GRAY);
				sjfPanel.setPreferredSize(new Dimension(w, h));
				
				JLabel l = new JLabel("SJF");
				l.setBounds(5, 5, 50, 20);
				
				addContent(3);
				
				sjfPanel.add(l);
				sjfPanel.add(stat[3]);
				sjfPanel.add(j[3]);
				sjfPanel.add(r[3]);
				sjfPanel.add(g[3]);
				sjfPanel.add(t[3]);
				sjfPanel.add(time[3]);
				sjfPanel.add(tT[3]);
				sjfPanel.add(turnT[3]);
				sjfPanel.add(wT[3]);
				sjfPanel.add(waitT[3]);
				
				addTextQ(3);
				addDisk(3);
				outPanel.add(sjfPanel);
			}
			if(srtf.isSelected()){
				fin++;
				srtfPanel = new JPanel();
				srtfPanel.setLayout(null);
				srtfPanel.setBackground(Color.LIGHT_GRAY);
				srtfPanel.setPreferredSize(new Dimension(w, h));
				
				JLabel l = new JLabel("SRTF");
				l.setBounds(5, 5, 50, 20);
				
				addContent(4);
				
				srtfPanel.add(l);
				srtfPanel.add(stat[4]);
				srtfPanel.add(j[4]);
				srtfPanel.add(r[4]);
				srtfPanel.add(g[4]);
				srtfPanel.add(t[4]);
				srtfPanel.add(time[4]);
				srtfPanel.add(tT[4]);
				srtfPanel.add(turnT[4]);
				srtfPanel.add(wT[4]);
				srtfPanel.add(waitT[4]);
				
				addTextQ(4);
				addDisk(4);
				outPanel.add(srtfPanel);
			}
			if(rr.isSelected()){
				fin++;
				rrPanel = new JPanel();
				rrPanel.setLayout(null);
				rrPanel.setBackground(Color.LIGHT_GRAY);
				rrPanel.setPreferredSize(new Dimension(w, h));
				
				JLabel l = new JLabel("RR");
				l.setBounds(5, 5, 50, 20);
				
				addContent(5);
				
				rrPanel.add(l);
				rrPanel.add(stat[5]);
				rrPanel.add(j[5]);
				rrPanel.add(r[5]);
				rrPanel.add(g[5]);
				rrPanel.add(t[5]);
				rrPanel.add(time[5]);
				rrPanel.add(tT[5]);
				rrPanel.add(turnT[5]);
				rrPanel.add(wT[5]);
				rrPanel.add(waitT[5]);
				
				addTextQ(5);
				addDisk(5);
				outPanel.add(rrPanel);
			}
			
		outScroll = new JScrollPane(outPanel);
		outScroll.setBounds(210, 155, 1120, 490);
		outScroll.setVisible(true);
		add(outScroll);
		requestFocusInWindow();
		revalidate();
	}

	private void addDisk(int i) {
		diskPanel[i] = createChartPanel(i);
		diskPanel[i].setBounds(100, 120, 900, 330);
		
		if(i == 0)
			fcfsPanel.add(diskPanel[i]);
		if(i == 1)
			npPrioPanel.add(diskPanel[i]);
		if(i == 2)
			prioPanel.add(diskPanel[i]);
		if(i == 3)
			sjfPanel.add(diskPanel[i]);
		if(i == 4)
			srtfPanel.add(diskPanel[i]);
		if(i == 5)
			rrPanel.add(diskPanel[i]);
		
	}

	
	private JPanel createChartPanel(int i) {
		String chartTitle = "Disk Schedule";
	    String xAxisLabel = "Time";
	    String yAxisLabel = "Cyllinder";
	    boolean showLegend = false;
	    boolean createURL = false;
	    boolean createTooltip = false;
	    
	    XYDataset dataset = createDataset(i);
	     
	    chart[i] = ChartFactory.createXYLineChart(chartTitle,
	            xAxisLabel, yAxisLabel, dataset,
	            PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

	    return new ChartPanel(chart[i]);
	}
	
	private XYDataset createDataset(int i) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		    series[i]= new XYSeries("");
		 
		    //series[i].add(0, 0);
		 
		    dataset.addSeries(series[i]);
		    
		    //system.out.println(i);
		return dataset;
	}

	public void addDataset(int i, int left, int right, ArrayList<Integer> y, int id, boolean wait) {
		////system.out.println(i + " " + left + " " + right + " " + wait + " ");
		int k = 0, pivot = y.get(0);
		////system.out.println(y);
		if(y.get(y.size() - 1) > y.get(y.size() - 2))
			up[i] = true;
		else
			up[i] = false;
		
		if(wait){
			//series[i].add(left, last[lastID[i]]);
			series[i].add(right, last[i]);
			////system.out.println(left + " " + last[lastID[i]]);
			//system.out.println(right + " " + last[i]);
			chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
		}
			
		
		else if(i == 0){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				//system.out.println(left + " " + y.get(k));
				

				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);

				k++;
			}
		}
		else if(i == 1){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				////system.out.println(left + " " + y.get(r0[id]));
				

				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);

				k++;
			}
		}
		else if(i == 2){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				////system.out.println(left + " " + y.get(r0[id]));
				

				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);

				
				k++;
			}
		}
		else if(i == 3){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				////system.out.println(left + " " + y.get(r0[id]));
				

				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);
				
				k++;
			}
		}
		else if(i == 4){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				////system.out.println(left + " " + y.get(r0[id]));
				

				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);

				k++;
			}
		}
		else if(i == 5){
			for(; left <= right; left++){
				
				//y0 = y.get(r0[id]);
				series[i].add(left, y.get(k));
				////system.out.println(left + " " + y.get(k));
				
				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
				last[i] =  y.get(k);

				
				k++;
			}
		}
	    /*
	    if(dSCAN.isSelected()){
	    	if(up[i]){
	    		series[i].add(--left, 2000);
				//system.out.println("dscan up");
				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
	    	}
	    	else{
	    		series[i].add(--left, 0);
	    		//system.out.println("dscan down");
				chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
	    	}
	    }
	    else if(cSCAN.isSelected()){
    		series[i].add(--left, 0);
    		//system.out.println("cscan up");
			chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
	    }
	    */
	}


	private void addSeries(int i, int left, boolean b) {
		if(cSCAN.isSelected()){
			series[i].add(left, 0);
			////system.out.println(left + " " + y.get(k));
			
			chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
		}
		else if(dSCAN.isSelected() && b){
			series[i].add(left, 2000);
			////system.out.println(left + " " + y.get(k));
			
			chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
		}
		else if(dSCAN.isSelected() && !b){
			series[i].add(left, 0);
			////system.out.println(left + " " + y.get(k));
			
			chart[i].getXYPlot().setDataset(chart[i].getXYPlot().getDataset());
		}
	}

	private void addContent(int i) {
		
		j[i] = new JLabel("Job Queue:");
		r[i] = new JLabel("Ready Queue:");
		g[i] = new JLabel("Gantt Chart:");
		t[i] = new JLabel("Time:");
		tT[i] = new JLabel("Turn Time:");
		wT[i] = new JLabel("Wait Time:");
		stat[i] = new JTextField();
		stat[i].setEditable(false);
		stat[i].setBounds(100, 5, 200, 20);
		time[i] = new JTextField();
		time[i].setEditable(false);
		time[i].setBounds(840, 25, 30, 23);
		turnT[i] = new JTextField();
		turnT[i].setEditable(false);
		turnT[i].setBounds(970, 25, 30, 23);
		waitT[i] = new JTextField();
		waitT[i].setEditable(false);
		waitT[i].setBounds(970, 50, 30, 23);
		j[i].setFont(font);
		r[i].setFont(font);
		g[i].setFont(font);
		t[i].setFont(font);
		tT[i].setFont(font);
		wT[i].setFont(font);
		
		j[i].setBounds(5, 25, 90, 20);
		r[i].setBounds(5, 50, 90, 20);
		g[i].setBounds(5, 75, 90, 20);
		t[i].setBounds(800, 25, 50, 20);
		tT[i].setBounds(900, 25, 90, 20);
		wT[i].setBounds(900, 50, 90, 20);
		
	}
	
	

	private void addTextQ(int i) {
		int x = 100;
		
		for(int k = 0; k < 20; k++){
			jobQ[i][k] = new JTextField();
			jobQ[i][k].setBounds(x, 28, 30, 20);
			jobQ[i][k].setEditable(false);
			
			readyQ[i][k] = new JTextField();
			readyQ[i][k].setBounds(x, 53, 30, 20);
			readyQ[i][k].setEditable(false);
			
			if(i == 0){
				fcfsPanel.add(jobQ[i][k]);
				fcfsPanel.add(readyQ[i][k]);
			}
			else if(i == 1){
				npPrioPanel.add(jobQ[i][k]);
				npPrioPanel.add(readyQ[i][k]);
			}
			else if(i == 2){
				prioPanel.add(jobQ[i][k]);
				prioPanel.add(readyQ[i][k]);
			}
			else if(i == 3){
				sjfPanel.add(jobQ[i][k]);
				sjfPanel.add(readyQ[i][k]);
			}
			else if(i == 4){
				srtfPanel.add(jobQ[i][k]);
				srtfPanel.add(readyQ[i][k]);
			}
			else if(i == 5){
				rrPanel.add(jobQ[i][k]);
				rrPanel.add(readyQ[i][k]);
			}
			
			x+=33;
		}
		
	}
	
	
	
	public void addGantt(int i, int j, boolean wait, String p, ArrayList<Integer> arrayList, int id){
		
		////system.out.println(j + " " + totalT);
		
		text[tex] = new JTextField();
		text[tex].setBounds(x[i], 78, 100, 20);
		text[tex].setText(left[i] + " - " + p + " - " + (left[i]+j));
		text[tex].setFont(font2);
		text[tex].setVisible(true);
		text[tex].setEditable(false);
		
		////system.out.println(j + " " + wait);
		
		if(i == 0)
			fcfsPanel.add(text[tex]);
		else if(i == 1)
			npPrioPanel.add(text[tex]);
		else if(i == 2)
			prioPanel.add(text[tex]);
		else if(i == 3)
			sjfPanel.add(text[tex]);
		else if(i == 4)
			srtfPanel.add(text[tex]);
		else if(i == 5)
			rrPanel.add(text[tex]);
		
		if(wait){
			addDataset(i, left[i], left[i]+j, arrayList, id, wait);
		}
		else{
			addDataset(i, left[i], left[i]+j, arrayList, id, wait);
		}
		
		tex++;
		left[i] += j;
		x[i] += 100;
		outPanel.revalidate();
		outScroll.revalidate();
		this.revalidate();
	}
	
	

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == newButt){
			pText.setEnabled(true);
			rText.setEnabled(true);
			newButt.setEnabled(false);
			addButt.setEnabled(true);
		}
		else if(e.getSource() == addButt){
			pText.setEnabled(false);
			rText.setEnabled(false);
			addButt.setEnabled(false);
			runButt.setEnabled(true);
			
			addPanels();
		}
		
		
		else if(e.getSource() == runButt){
			
			if(speed.getSelection() == null || !(fcfs.isSelected() || npPrio.isSelected() || prio.isSelected()
					|| sjf.isSelected() || srtf.isSelected() || rr.isSelected()))
				JOptionPane.showMessageDialog(this, "Select Execution Type/Speed First");
			else{
				runButt.setEnabled(false);
				stopButt.setEnabled(true);
				diskButt.setEnabled(true);
				
				s0 = 0;
				s1 = 0;
				s2 = 0;
				s3 = 0;
				s4 = 0;
				s5 = 0;
				
				y0 = 0;
				y1 = 0;
				y2 = 0;
				y3 = 0;
				y4 = 0;
				y5 = 0;
				
				int s = 0;
				if(fcfs.isSelected())
					s++;
				if(npPrio.isSelected())
					s++;
				if(prio.isSelected())
					s++;
				if(sjf.isSelected())
					s++;
				if(srtf.isSelected())
					s++;
				if(rr.isSelected())
					s++;
				
				if(step.isSelected())
					nextButt.setEnabled(true);
				
				addPPanel(s--);
				sched = new Scheduler(this);
				schedT = new Thread(sched);
				
				schedT.start();
				
			}
		}
		else if(e.getSource() == stopButt){
			newButt.setEnabled(true);
			stopButt.setEnabled(false);
			nextButt.setEnabled(false);
			diskButt.setEnabled(false);
			
			schedT.stop();
			
			procScroll.remove(procPanel);
			allocScroll.remove(allocPanel);
			maxScroll.remove(maxPanel);
			availScroll.remove(availPanel);
			outScroll.remove(outPanel);
			
			remove(procScroll);
			remove(allocScroll);
			remove(maxScroll);
			remove(availScroll);
			remove(outScroll);
			
			s0 = 0;
			s1 = 0;
			s2 = 0;
			s3 = 0;
			s4 = 0;
			s5 = 0;
			s6 = 0;
			
			y0 = 0;
			y1 = 0;
			y2 = 0;
			y3 = 0;
			y4 = 0;
			y5 = 0;
			
			initialize();
			sched.kill();
			revalidate();
		}
		else if(e.getSource() == nextButt){
			sched.resume = true;
		}
		if(e.getSource() == arrivButt)
			random(4);
		if(e.getSource() == prioButt)
			random(5);
		if(e.getSource() == allocButt)
			random(1);
		if(e.getSource() == maxButt)
			random(2);
		if(e.getSource() == availButt)
			random(3);
		if(e.getSource() == diskButt){
			//addDiskPanel();
		}
		if(e.getSource() == slow){
			sleepTime = 1000;
			nextButt.setEnabled(false);
		}
		if(e.getSource() == fast){
			sleepTime = 250;
			nextButt.setEnabled(false);
		}
		if(e.getSource() == step)
			pause = true;
	}

	
	private void initialize() {
		
		fin = 0;
		totalT = 0;
		tex = 0;
		
		for(int k = 0; k < 6; k++){
   			left[k] = 0;
   			x[k] = 100;
   		}
	}

}
