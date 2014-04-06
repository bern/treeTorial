package creator.end.api.copy;

import java.lang.Math;

public class ApiUtils {

	/**
	 * Returns a unique id to identify the bean. TODO for Clay!
	 * @return
	 */
	public static int getUID(){
		int id = (int)(Math.random()*10000);
		return id;
	}
}
