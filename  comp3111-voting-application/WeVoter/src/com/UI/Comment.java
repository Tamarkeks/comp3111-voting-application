/*
 * The page where user could view the vote information, do voting and comment.
 */
package com.UI;
import org.json.JSONException;

import com.DB.access_DB;
import com.INFO.Comment_info;
import com.INFO.User_info;
import com.INFO.Vote_info;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Comment extends Activity implements OnTouchListener, OnItemClickListener{
	public static Handler myHandler;
	private myArrayAdapter3 adapter3;
	private int vote_index;
	private ListView list;
	private TextView back_txt;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_layout);
		Bundle extras = getIntent().getExtras();
		vote_index = Integer.parseInt(extras.getString("position")) - 1;
		initView(vote_index);
		initHandler();
		adapter3  = new myArrayAdapter3(getBaseContext(), Comment_info.CommentInfo);
		list.setAdapter(adapter3);
	}
	
	private void initHandler(){
		myHandler = new Handler(){
			public void handleMessage(Message msg) {
				int res = msg.what;
				if(res == 0){
					adapter3.notifyDataSetChanged();
				}
			}
		};
	}
	
	private void initView(int index){
		initBar();
		list = (ListView)findViewById(R.id.comment_list);
		Vote_info v = (Vote_info)Vote_info.VoteInfo.get(index);
		
		TextView title = (TextView)findViewById(R.id.comment_title);
		title.setText(v.title);
		
		TextView desc = (TextView)findViewById(R.id.comment_desc);
		desc.setText(v.description);
		
		TextView username = (TextView)findViewById(R.id.comment_username);
		username.setText("From " + v.user_name + ",  ");
		username.setTextColor(Color.GRAY);
		
		TextView dura = (TextView)findViewById(R.id.comment_duration);
		dura.setText(v.start_time + " - " + v.end_time);
		dura.setTextColor(Color.GRAY);
		
		TextView opt[] = new TextView[5];
		opt[0] = (TextView)findViewById(R.id.comment_option_1);
		opt[1] = (TextView)findViewById(R.id.comment_option_2);
		opt[2] = (TextView)findViewById(R.id.comment_option_3);
		opt[3] = (TextView)findViewById(R.id.comment_option_4);
		opt[4] = (TextView)findViewById(R.id.comment_option_5);
		for(int i = 0; i < 5; i ++)
			opt[i].setOnTouchListener(this);
		TextView opt_num[] = new TextView[5];
		opt_num[0] = (TextView)findViewById(R.id.comment_num_1);
		opt_num[1] = (TextView)findViewById(R.id.comment_num_2);
		opt_num[2] = (TextView)findViewById(R.id.comment_num_3);
		opt_num[3] = (TextView)findViewById(R.id.comment_num_4);
		opt_num[4] = (TextView)findViewById(R.id.comment_num_5);
		ImageView opt_image[] = new ImageView[5];
		opt_image[0] = (ImageView)findViewById(R.id.comment_image_1);
		opt_image[1] = (ImageView)findViewById(R.id.comment_image_2);
		opt_image[2] = (ImageView)findViewById(R.id.comment_image_3);
		opt_image[3] = (ImageView)findViewById(R.id.comment_image_4);
		opt_image[4] = (ImageView)findViewById(R.id.comment_image_5);
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
		TextView com = (TextView)findViewById(R.id.comment_sig);
		com.setTextColor(Color.RED);
		
		initList(index);
	}
	
	private void initList(final int index){
		list.setOnItemClickListener(this);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					access_DB.getComment(index);
				} catch (JSONException e) {
					Toast.makeText(getBaseContext(), "Unexpected exception in initList()!" ,
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final Comment_info cc = Comment_info.CommentInfo.get(arg2);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle("My Comment");
    	alert.setMessage("@" + cc.from_name + ":");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this);
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		final Comment_info para = new Comment_info();
    		para.from_name = User_info.username;
    		para.to_name = cc.from_name;
    		para.comment = input.getText().toString();
    		para.comment_time = Vote_info.date_to_string(new Date());
    		para.vote_id = vote_index;
    		Comment_info.CommentInfo.add(para);
    		adapter3.notifyDataSetChanged();
    		new Thread(new Runnable(){
    			@Override
    			public void run() {
    				try {
    					access_DB.Add_Comment(para);
    				} catch (JSONException e) {
    					Toast.makeText(getBaseContext(), "Unexpected exception in Comment.onItemClick()!" ,
    							Toast.LENGTH_LONG).show();
    					e.printStackTrace();
    				}	
    			}
    		}).start();
    	}
    	});
    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			// Canceled.
    		}
    	});
    	alert.show();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) 
			return true;
		if(v == back_txt){
			finish();
			return true;
		}
		if(Vote_info.VoteInfo.get(vote_index).end_date.before(new Date())){
			Toast.makeText(this, "This vote has finished!!!" ,
					Toast.LENGTH_LONG).show();
			return true;
		}
		if(Vote_info.VoteInfo.get(vote_index).voted){
			Toast.makeText(this, "You have already voted!!!" ,
					Toast.LENGTH_LONG).show();
			return true;
		}
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final int res;
		switch(v.getId()){
		case R.id.comment_option_1:
			res = 0; break;
		case R.id.comment_option_2:
			res = 1; break;
		case R.id.comment_option_3:
			res = 2; break;
		case R.id.comment_option_4:
			res = 3; break;
		case R.id.comment_option_5:
			res = 4; break;
		default:
			res = -1;
		}
    	alert.setTitle(User_info.username);
    	alert.setMessage("Sure to vote for option:\n\"" + Vote_info.VoteInfo.get(vote_index).option_des[res] + "\"?");
    	alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		Vote_info.VoteInfo.get(vote_index).voted = true;
    		Vote_info.VoteInfo.get(vote_index).myOpt = res;
    		new Thread(new Runnable(){
    			@Override
    			public void run() {
    				try {
    					access_DB.Do_Vote(vote_index, res);
    				} catch (JSONException e) {
    					Toast.makeText(getBaseContext(), "Unexpected exception in myTouch()!" ,
    							Toast.LENGTH_LONG).show();
    					e.printStackTrace();
    				}	
    			}
    		}).start();
    	}
    	});

    	alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			// Canceled.
    		}
    	});

    	alert.show();
    	return true;
	}
	private void initBar(){
    	ActionBar action=getActionBar();
    	action.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
		action.setDisplayShowCustomEnabled(true);
		action.setDisplayShowTitleEnabled(false);
		back_txt =new TextView(getApplicationContext());
		back_txt.setText("BACK");
		back_txt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		back_txt.setClickable(true);
		back_txt.setOnTouchListener(this);
		action.setCustomView(back_txt);
    }
}
@SuppressWarnings("rawtypes")
class myArrayAdapter3 extends ArrayAdapter{
	private final Context context;
 
	@SuppressWarnings("unchecked")
	public myArrayAdapter3(Context context, List<Comment_info> objects) {
		super(context, R.layout.list_item, objects);
		this.context = context;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Comment_info c = (Comment_info)Comment_info.CommentInfo.get(position);
		View itemView = inflater.inflate(R.layout.list_item, parent, false);
		
		TextView name = (TextView)itemView.findViewById(R.id.item_name);
		name.setText(c.from_name);
		TextView time = (TextView)itemView.findViewById(R.id.item_time);
		time.setText(c.comment_time);
		TextView content = (TextView)itemView.findViewById(R.id.item_content);
		String con = "@" + c.to_name + ": " + c.comment;
		content.setText(con);
		
		return itemView;
	}
}