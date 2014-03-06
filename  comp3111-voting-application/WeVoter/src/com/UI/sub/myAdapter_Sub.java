package com.UI.sub;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.INFO.Vote_info;
import com.UI.R;

@SuppressWarnings("rawtypes")
class myArrayAdapter_Sub extends ArrayAdapter{
	private final Context context;
	private ArrayList<Vote_info> values;
 
	@SuppressWarnings("unchecked")
	public myArrayAdapter_Sub(Context context, ArrayList<Vote_info> v) {
		super(context, R.layout.tab1_layout, v);
		this.context = context;
		this.values = v;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Vote_info v = (Vote_info)values.get(position);
 
		View rowView = inflater.inflate(R.layout.tab1_layout, parent, false);
		
		TextView title = (TextView)rowView.findViewById(R.id.tab_title);
		title.setText(v.title);
		
		TextView desc = (TextView)rowView.findViewById(R.id.tab_desc);
		desc.setText(v.description);
		
		TextView username = (TextView)rowView.findViewById(R.id.tab_username);
		username.setText("From " + v.user_name + ",  ");
		username.setTextColor(Color.GRAY);
		
		TextView dura = (TextView)rowView.findViewById(R.id.tab_duration);
		dura.setText(v.start_time + " - " + v.end_time);
		dura.setTextColor(Color.GRAY);
		
		TextView opt[] = new TextView[5];
		opt[0] = (TextView)rowView.findViewById(R.id.tab_option_1);
		opt[1] = (TextView)rowView.findViewById(R.id.tab_option_2);
		opt[2] = (TextView)rowView.findViewById(R.id.tab_option_3);
		opt[3] = (TextView)rowView.findViewById(R.id.tab_option_4);
		opt[4] = (TextView)rowView.findViewById(R.id.tab_option_5);
		TextView opt_num[] = new TextView[5];
		opt_num[0] = (TextView)rowView.findViewById(R.id.tab_num_1);
		opt_num[1] = (TextView)rowView.findViewById(R.id.tab_num_2);
		opt_num[2] = (TextView)rowView.findViewById(R.id.tab_num_3);
		opt_num[3] = (TextView)rowView.findViewById(R.id.tab_num_4);
		opt_num[4] = (TextView)rowView.findViewById(R.id.tab_num_5);
		ImageView opt_image[] = new ImageView[5];
		opt_image[0] = (ImageView)rowView.findViewById(R.id.tab_image_1);
		opt_image[1] = (ImageView)rowView.findViewById(R.id.tab_image_2);
		opt_image[2] = (ImageView)rowView.findViewById(R.id.tab_image_3);
		opt_image[3] = (ImageView)rowView.findViewById(R.id.tab_image_4);
		opt_image[4] = (ImageView)rowView.findViewById(R.id.tab_image_5);
		int max_num = 1;
		for(int i = 0; i < v.option_number; i ++){
			if(v.option_num[i] > max_num)
				max_num = v.option_num[i];
		}
		int len[] = new int[5];
		for(int i = 0; i < v.option_number; i ++){
			opt[i].setText(v.opt_align(i));
			opt[i].setTextColor(Color.BLUE);
			len[i] = v.option_num[i] * 333 / max_num + 10;
			opt_image[i].getLayoutParams().width = len[i];
			opt_image[i].getLayoutParams().height = 68;
			opt_num[i].setText(" " + v.option_num[i]);
		}
		for(int i = v.option_number; i < 5; i ++){
			opt[i].setVisibility(View.GONE);
			opt_image[i].setVisibility(View.GONE);
			opt_num[i].setVisibility(View.GONE);
		}
		return rowView;
	}
}