/*
 * Vote information class
 */
package com.INFO;

import android.annotation.SuppressLint;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("SimpleDateFormat")
public class Vote_info {
		public static ArrayList<Vote_info> VoteInfo;
		public static ArrayList<Vote_info> GoingOn_Vote;
		public static ArrayList<Vote_info> Finished_Vote;
		public static ArrayList<Vote_info> my_Vote;
		static{
			VoteInfo = new ArrayList<Vote_info>();
			GoingOn_Vote = new ArrayList<Vote_info>();
			Finished_Vote = new ArrayList<Vote_info>();
			my_Vote = new ArrayList<Vote_info>();
		}
		public boolean voted;
		public int myOpt;
		public int vote_id;
		public String user_name;
		public String title;
		public String description;
		public String start_time;
		public String end_time;
		public Date start_date;
		public Date end_date;
		public ArrayList<Comment_info> vote_comment;
		public int option_number;
		public String option_des[];
		public int option_num[];
		public Vote_info(){
			voted = false;
			myOpt = -1;
			option_des = new String[5];
			option_num = new int[5];
			vote_comment = new ArrayList<Comment_info>();
		}
		/*
		 * align all the option description to the same length
		 */
		public String opt_align(int index){ 
			String res = this.option_des[index];
			for(int i = 0; i < 20 - this.option_des[index].length(); i ++)
				res += " ";
			return res;
		}
		public static String date_to_string(Date d){
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = formatter.format(d);
			return s;
		}
		public static Date string_to_date(String s){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date res = new Date();
			try {
				res = formatter.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return res;
		}
		/*
		 * Parse the data from a json array
		 */
		public static void parseJsonArray(JSONArray j_array) throws JSONException{
			 int num = j_array.length();
		        Vote_info.VoteInfo.clear();
		        for(int i = 0; i < num; i ++){
		        	JSONObject js = j_array.getJSONObject(i);
		        	Vote_info v = new Vote_info();
		        	v.parseJson(js);
		        	Vote_info.VoteInfo.add(v);
		        }
		        Vote_info.parseVote();
		}
		public void parseJson(JSONObject js){
        	try {
				this.description = js.getString("DESCRIPTION");
				this.end_time = js.getString("END_TIME");
	        	this.option_number = Integer.parseInt(js.getString("OPTION_NUMBER"));
	        	this.start_time = js.getString("START_TIME");
	        	this.title = js.getString("TITLE");
	        	this.user_name = js.getString("USER_NAME");
	        	this.vote_id = Integer.parseInt(js.getString("VOTE_ID"));
	        	String temp[] = {"ONE", "TWO", "THREE", "FOUR", "FIVE"};
	        	for(int i = 0; i < this.option_number; i ++){
	        		this.option_des[i] = js.getString("OP_" + temp[i]);
	        		this.option_num[i] = Integer.parseInt(js.getString("OP_" + temp[i] + "_NUM"));
	        	}
	        	this.start_date = string_to_date(this.start_time);
	        	this.end_date = string_to_date(this.end_time);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		public static void parseVote(){
			GoingOn_Vote.clear();
			Finished_Vote.clear();
			my_Vote.clear();
			Date now = new Date();
			for(Vote_info v : VoteInfo){
				if(v.end_date.after(now))
					GoingOn_Vote.add(v);
				if(v.end_date.before(now))
					Finished_Vote.add(v);
				if(v.user_name.equals(User_info.username))
					my_Vote.add(v);
			}
		}
}
