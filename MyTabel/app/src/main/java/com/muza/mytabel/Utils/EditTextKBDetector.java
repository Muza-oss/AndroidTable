package com.muza.mytabel.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mirkopinna on 26/09/15.
 */
@SuppressLint("AppCompatCustomView")
public class EditTextKBDetector extends EditText {

    public static final String KEYBOARD_HIDE_EVENT = "keyboard.hidden.event";
    public static final String KEYBOARD_SHOW_EVENT = "keyboard.showing.event";
	
	private Context mContext;
    public EditTextKBDetector(Context context) {
        super(context);
		mContext = context;
        init();
    }

    public EditTextKBDetector(Context context, AttributeSet attrs) {
        super(context, attrs);
		mContext = context;
        init();
    }

    public EditTextKBDetector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		mContext = context;
        init();
    }

    private void init() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean b) {
					if (b) {
						//Has focus keyboard is showing
						fireKBShownEvent();
					} else {
						//Doesn't have focus, keyboard is not showing
						fireKBHiddenEvent();
					}
				}
			});

        setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
					if (i == EditorInfo.IME_ACTION_DONE) {
						//Keyboard closed with done button
						clearFocus();
                        Toast.makeText(mContext,"keycode_enter",Toast.LENGTH_SHORT).show();
						//fireKBHiddenEvent();
					}
					return false;
				}
			});
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            //Keyboard closed with back button
            clearFocus();
            Toast.makeText(mContext,"keycode_back",Toast.LENGTH_SHORT).show();
            //fireKBHiddenEvent();
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //Keyboard closed with back button

            Toast.makeText(mContext,"keycode_enter",Toast.LENGTH_SHORT).show();
            //fireKBHiddenEvent();

        }
        return super.dispatchKeyEvent(event);
    }

    private void fireKBShownEvent() {
		Toast.makeText(mContext,"shown",Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(KEYBOARD_SHOW_EVENT));
    }

    private void fireKBHiddenEvent() {
		Toast.makeText(mContext,"hide",Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(KEYBOARD_HIDE_EVENT));
    }

}
