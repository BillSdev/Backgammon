import java.util.ArrayList;

public class HelperMethods {

	
	public static int maxIntArrayList(ArrayList<Integer> list) {
		if(list.isEmpty())
			return -1;
		int max = list.get(0);
		for(int i = 0 ; i < list.size() ; i++) {
			if(list.get(i) > max)
				max = list.get(i);
		}
		return max;
	}
	
	public static void printArrayList(ArrayList<Integer> list) {
		System.out.print("      ");
		for(int i = 0 ; i < list.size() ; i++) {
			System.out.print(list.get(i) + ", ");
		}
		System.out.println();
	}
}
