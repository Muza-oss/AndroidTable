package com.muza.mytabel.Utils;
import android.content.*;

public class Utils
{
	Context mContext;
	public Utils(Context context){
		mContext = context;
	}
	
	public int getMin(float x){
		
		String tmp   = Float.toString(x).split("\\.")[1];
        int m = Integer.parseInt(tmp);
        if (tmp.length() == 1)
            m = Integer.parseInt(tmp) * 10;
		
		return m;
	}
}
