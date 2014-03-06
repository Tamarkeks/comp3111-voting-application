package com.UI.sub;

import java.util.ArrayList;
import java.util.List;

import com.INFO.Vote_info;
import com.UI.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class tab4 extends Activity implements OnClickListener {
	Spinner spinner = null;
	EditText edittext = null;
	ImageButton search_btn = null;
	ListView search_list = null;
	ArrayList<Vote_info> search_res = null;
	myArrayAdapter2 adapter2 = null;
	public enum Area{
		ALL, NAME, TITLE, DESC
	}
	Area selectedItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab4_layout);
		
		initView();
	}
	private void initView(){
		spinner = (Spinner)findViewById(R.id.search_spinner);
		edittext = (EditText)findViewById(R.id.search_content);
		search_btn = (ImageButton)findViewById(R.id.search_btn);
		search_list = (ListView)findViewById(R.id.search_list);
		search_btn.setOnClickListener(this);
		search_res = new ArrayList<Vote_info>();
		adapter2  = new myArrayAdapter2(getBaseContext(), search_res);
		search_list.setAdapter(adapter2);
	}
	@Override
	public void onClick(View v) {
		String str = edittext.getText().toString();
		search_res.clear();
		getArea();
		
		boolean match_time, match_title, match_desc;
		for(Vote_info vote : Vote_info.VoteInfo){
			match_time = 
					((selectedItem == Area.ALL || selectedItem == Area.NAME) 
							&& str.equalsIgnoreCase(vote.user_name));
			match_title = 
					((selectedItem == Area.ALL || selectedItem == Area.TITLE) 
							&& vote.title.toLowerCase().contains(str.toLowerCase()));
			match_desc = 
					((selectedItem == Area.ALL || selectedItem == Area.DESC) 
							&& vote.description.toLowerCase().contains(str.toLowerCase()));
		
			if(match_time || match_title || match_desc)
				search_res.add(vote);
		}
		adapter2.notifyDataSetChanged();
	}
	private void getArea(){
		switch(spinner.getSelectedItemPosition()){
		case 0:
			selectedItem = Area.ALL;
			break;
		case 1:
			selectedItem = Area.TITLE;
			break;
		case 2:
			selectedItem = Area.DESC;
			break;
		case 3:
			selectedItem = Area.NAME;
			break;
		}
	}
}
@SuppressWarnings("rawtypes")
class myArrayAdapter2 extends ArrayAdapter{
	private final Context context;
	List<Vote_info> values;
 
	@SuppressWarnings("unchecked")
	public myArrayAdapter2(Context context, List<Vote_info> objects) {
		super(context, R.layout.tab4_list_item, objects);
		this.context = context;
		values = objects;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Vote_info v = (Vote_info)values.get(position);
		View itemView = inflater.inflate(R.layout.tab4_list_item, parent, false);
		
		TextView title = (TextView)itemView.findViewById(R.id.tab4_list_title);
		title.setText(v.title);
		TextView name = (TextView)itemView.findViewById(R.id.tab4_list_username);
		name.setText( "from " + v.user_name);
		TextView desc = (TextView)itemView.findViewById(R.id.tab4_list_desc);
		desc.setText(v.description);
		
		return itemView;
	}
}
