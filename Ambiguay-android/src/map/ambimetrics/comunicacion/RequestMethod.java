package map.ambimetrics.comunicacion;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RequestMethod {

	public static int GET = 0;
	public static int POST = 1;
	public static String URL = "http://192.168.0.153/Ambiway/";
	
	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
		if (ni.getTypeName().equalsIgnoreCase("wifi"))
		if (ni.isConnected())
		hasConnectedWifi = true;
		if (ni.getTypeName().equalsIgnoreCase("mobile"))
		if (ni.isConnected())
		hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
		}
	
}
