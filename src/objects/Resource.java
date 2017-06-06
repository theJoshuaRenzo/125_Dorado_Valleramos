package objects;

public class Resource {
	private int[] alloc, max, need, subAlloc;
	
	public Resource(int[] alloc, int[] max, int[] need){
		this.alloc = alloc;
		this.max = max;
		this.need = need;
		subAlloc = new int[alloc.length];
		
		for(int k = 0; k < alloc.length; k++){
			int a = alloc[k];
			subAlloc[k] = a;
		}
	}
	
	public int[] getAlloc(){
		return alloc;
	}
	
	public int[] getMax(){
		return max;
	}
	
	public int[] getNeed(){
		return need;
	}
	
	public int[] getSubAlloc(){
		return subAlloc;
	}
	
}
