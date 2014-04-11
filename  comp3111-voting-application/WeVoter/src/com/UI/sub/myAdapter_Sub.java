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
	
	static class ViewHolder{
		TextView title;
		TextView desc;
		TextView username;
		TextView dura;
		TextView opt[];
		TextView opt_num[];
		ImageView opt_image[];
		public ViewHolder(){
			opt = new TextView[5];
			opt_num = new TextView[5];
			opt_image = new ImageView[5];
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tab1_layout,
					 parent, false);
			
			holder = new ViewHolder();
			
			holder.title = (TextView)convertView.findViewById(R.id.tab_title);
			holder.desc = (TextView)convertView.findViewById(R.id.tab_desc);
			holder.username = (TextView)convertView.findViewById(R.id.tab_username);
			holder.dura = (TextView)convertView.findViewById(R.id.tab_duration);
			holder.opt[0] = (TextView)convertView.findViewById(R.id.tab_option_1);
			holder.opt[1] = (TextView)convertView.findViewById(R.id.tab_option_2);
			holder.opt[2] = (TextView)convertView.findViewById(R.id.tab_option_3);
			holder.opt[3] = (TextView)convertView.findViewById(R.id.tab_option_4);
			holder.opt[4] = (TextView)convertView.findViewById(R.id.tab_option_5);
			holder.opt_num[0] = (TextView)convertView.findViewById(R.id.tab_num_1);
			holder.opt_num[1] = (TextView)convertView.findViewById(R.id.tab_num_2);
			holder.opt_num[2] = (TextView)convertView.findViewById(R.id.tab_num_3);
			holder.opt_num[3] = (TextView)convertView.findViewById(R.id.tab_num_4);
			holder.opt_num[4] = (TextView)convertView.findViewById(R.id.tab_num_5);
			holder.opt_image[0] = (ImageView)convertView.findViewById(R.id.tab_image_1);
			holder.opt_image[1] = (ImageView)convertView.findViewById(R.id.tab_image_2);
			holder.opt_image[2] = (ImageView)convertView.findViewById(R.id.tab_image_3);
			holder.opt_image[3] = (ImageView)convertView.findViewById(R.id.tab_image_4);
			holder.opt_image[4] = (ImageView)convertView.findViewById(R.id.tab_image_5);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Vote_info v = (Vote_info)values.get(position);
		
		holder.title.setText(v.title);
		holder.desc.setText(v.description);
		holder.username.setText("From " + v.user_name + ",  ");
		holder.username.setTextColor(Color.GRAY);
		holder.dura.setText(v.start_time + " - " + v.end_time);
		holder.dura.setTextColor(Color.GRAY);
		int max_num = 1;
		for(int i = 0; i < v.option_number; i ++){
			if(v.option_num[i] > max_num)
				max_num = v.option_num[i];
		}
		int len[] = new int[5];
		for(int i = 0; i < v.option_number; i ++){
			holder.opt[i].setText(v.opt_align(i));
			holder.opt[i].setTextColor(Color.BLUE);
			len[i] = v.option_num[i] * 333 / max_num + 10;
			holder.opt_image[i].getLayoutParams().width = len[i];
			holder.opt_image[i].getLayoutParams().height = 68;
			holder.opt_num[i].setText(" " + v.option_num[i]);
		}
		for(int i = v.option_number; i < 5; i ++){
			holder.opt[i].setVisibility(View.GONE);
			holder.opt_image[i].setVisibility(View.GONE);
			holder.opt_num[i].setVisibility(View.GONE);
		}
		return convertView;
	}
}