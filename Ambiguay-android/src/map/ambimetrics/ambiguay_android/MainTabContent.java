package map.ambimetrics.ambiguay_android;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class MainTabContent implements TabContentFactory{
	private Context mContext;
	
	public MainTabContent(Context context){
		mContext = context;
	}
			

	@Override
	public View createTabContent(String tag) {
		View v = new View(mContext);
		return v;
	}
	

}
