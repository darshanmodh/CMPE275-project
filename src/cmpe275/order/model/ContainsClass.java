package cmpe275.order.model;

import java.util.Collection;
import java.util.List;

public class ContainsClass {
	public static boolean contains(Collection<?> col, Object o) {
		//System.out.println(coll+" ,,");
	    return col.contains(o);
	  }

	public static double getValue(List<AverageRating> col, Object o) {
		for (AverageRating avg: col) {
			if (avg.getMenuId() == (int)o) {
				return avg.getAvgRating();
			}
		}
		return 0;
	}
}
