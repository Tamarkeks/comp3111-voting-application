/*
 * Comment information class
 */
package com.INFO;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment_info {
	public static ArrayList<Comment_info> CommentInfo;
	static{
		CommentInfo = new ArrayList<Comment_info>();
	}
	public int vote_id;
	public String from_name;
	public String to_name;
	public String comment_time;
	public String comment;
	public Comment_info(){
		this.to_name = "";
	}
	public void parseJson(JSONObject js){
		try {
			this.comment = js.getString("CONTENT");
			this.comment_time = js.getString("TIME");
			this.from_name = js.getString("FROM_NAME");
			this.to_name = js.getString("TO_NAME");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void addFirstRow(int vote_id){
		Vote_info v = Vote_info.VoteInfo.get(vote_id);
		this.comment = "How do you think about my vote? Feel free to make comments";
		this.comment_time = v.start_time;
		this.from_name = v.user_name;
		this.to_name = "ALL";
	}
}
