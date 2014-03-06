package com.UI.sub;

import com.INFO.Vote_info;
import com.UI.Comment;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class tab3 extends ListActivity {
	public static Handler myHandler_tab3;
	private myArrayAdapter_Sub myAdapter;
	public static boolean isVisible;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myAdapter = new myArrayAdapter_Sub(getBaseContext(), Vote_info.my_Vote);
		setListAdapter(myAdapter);
		myHandler_tab3 = new Handler(){
			public void handleMessage(Message m){
				if(m.what == 0)
					myAdapter.notifyDataSetChanged();
			}
		};
	}
	@Override
	protected void onResume() {
	  super.onResume();
	  myAdapter.notifyDataSetChanged();
	  isVisible = true;
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  isVisible = false;
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//get selected items
		Intent i = new Intent(this, Comment.class);
		i.putExtra("position", Vote_info.my_Vote.get(position).vote_id + "");
		startActivity(i);
	}
}