/*
 * This class is a package to access database.
 * All methods are static, be invoked in activity, but not in main thread.
 * After get data, send message to UI handler to handle the result, like displaying the data.
 */
package com.DB;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;

import com.INFO.Comment_info;
import com.INFO.User_info;
import com.INFO.Vote_info;
import com.UI.Comment;
import com.UI.LoginPage;
import com.UI.sub.tab1;
import com.UI.sub.tab2;
import com.UI.sub.tab3;

public class access_DB {
	/************host of server***************/
	private static String host = "143.89.212.189";
	
	/*
	 * To access the database, 
	 * data passed to server in json, and returned  in jsonArray.
	 * This method is packaged for data access.
	 */
	private static JSONArray access_db(JSONObject json){
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://" + host + ":8080/comp3111_project/db_connect/myOp_DB.php");
        try{
        	 httppost.setHeader("json", json.toString());
             StringEntity se = new StringEntity(json.toString());
             se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                     "application/json"));
             httppost.setEntity(se);
             HttpResponse response = httpclient.execute(httppost);
             
             HttpEntity entity = response.getEntity();
             InputStream is = entity.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
             StringBuilder  sb = new StringBuilder();
		     sb.append(reader.readLine() + "\n");

		     String line="0";
		     while ((line = reader.readLine()) != null) {
	              sb.append(line + "\n");
		      }
		      is.close();
		      String result=sb.toString();
		      JSONArray jArray = new JSONArray(result);
		      return jArray;
        }catch(Exception e){
        	return null;
        }
        
	}
	/*
	 * Vertify if the username and password match
	 */
	public static void Account_Vertify(String _name, String _pwd) throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("user_name", _name);
        json.put("instruction", "Account_Vertify");
        
        JSONArray res = access_db(json);
        
        Message m = new Message();
        if(res == null)
        	m.what = 2;
        else{
        	JSONObject json_data = res.getJSONObject(0);
        	String real_pwd = json_data.getString("USER_PWD");
        	if(_pwd.equals(real_pwd))
        		m.what = 0;                    //password is right
        	else
        		m.what = 1;                       //wrong password
        }
		/*}catch(JSONException e){
		    return 2;                         //no username found
		}*/
		LoginPage.myHandler.sendMessage(m);
	}
	
	/*
	 * Add a new vote to database.
	 */
	public static boolean Add_New_Vote(Vote_info v) throws JSONException{ 
        JSONObject json = new JSONObject();
        
        json.put("instruction", "Add_New_Vote");
        json.put("user_name", v.user_name);
        json.put("Title", v.title);
        json.put("Description", v.description);
        json.put("opt_num", v.option_number);
        json.put("start_time", v.start_time);
        json.put("end_time", v.end_time);
        json.put("opt_1", v.option_des[0]);
        json.put("opt_2", v.option_des[1]);
        json.put("opt_3", v.option_des[2]);
        json.put("opt_4", v.option_des[3]);
        json.put("opt_5", v.option_des[4]);
        
        JSONArray res = access_db(json);
        JSONObject json_data = res.getJSONObject(0);
        if(json_data.getString("YESorNO").equals("YES"))
        	return true;
        else
        	return false;
    }
	public static void Change_Password(String new_pwd) throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("user_name", User_info.username);
        json.put("new_pwd", new_pwd);
        json.put("instruction", "Change_Password");
        
        JSONArray res = access_db(json);
        JSONObject json_data = res.getJSONObject(0);
        
        Message m = new Message();
        if(json_data.getString("YESorNO").equals("YES"))
        	m.what = 0;
        else
        	m.what = 1;
        com.UI.MainActivity.myHandler.sendMessage(m);
	}
	public static void Get_Vote(int from) throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("instruction", "Get_Vote");
        
        JSONArray res = access_db(json);
        Vote_info.parseJsonArray(res);
        Get_myVote();
        Message m = new Message();
        switch(from){
        case 0:
            m.what = 3;
            LoginPage.myHandler.sendMessage(m);
            break;
        case 1:
        	m.what = 0;
        	if(tab1.isVisible)
        		tab1.myHandler_tab1.sendMessage(m);
        	if(tab2.isVisible)
        		tab2.myHandler_tab2.sendMessage(m);
        	if(tab3.isVisible)
        		tab3.myHandler_tab3.sendMessage(m);
        	break;
        }
	}
	/*
	 * Get all the votes I have participated in.
	 */
	public static void Get_myVote() throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("instruction", "Get_myVote");
        json.put("user_name", User_info.username);
        
        JSONArray res = access_db(json);
       if(res == null)
    	   return;
        int num = res.length();
        for(int i = 0; i < num; i ++){
        	JSONObject js = res.getJSONObject(i);
        	int v_id = js.getInt("VOTE_ID");
        	int opt_num = js.getInt("OPTION_NUM");
        	Vote_info.VoteInfo.get(v_id - 1).voted = true;
        	Vote_info.VoteInfo.get(v_id - 1).myOpt = opt_num;
        }
        return;
	}
	/*
	 * Get all the comments to one specific vote.
	 */
	public static void getComment(int vote_id) throws JSONException{
		Comment_info.CommentInfo.clear();
		
		JSONObject json = new JSONObject();
        json.put("instruction", "Get_Comment");
        json.put("vote_id", (vote_id + 1) + "");
        
        JSONArray res = access_db(json);
       
        Comment_info fir = new Comment_info();
        fir.addFirstRow(vote_id);
        Comment_info.CommentInfo.add(fir);
        if(res != null){
        	int num = res.length();
        	for(int i = 0; i < num; i ++){
        		JSONObject js = res.getJSONObject(i);
        		Comment_info c = new Comment_info();
        		c.parseJson(js);
        		Comment_info.CommentInfo.add(c);
        	}
        }
        Message m = new Message();
        m.what = 0;
        Comment.myHandler.sendMessage(m);
	}
	public static void Do_Vote(int vote_id, int opt_num) throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("instruction", "Do_Vote");
        json.put("vote_id", (vote_id + 1) + "");
        json.put("option", (opt_num + 1) + "");
        json.put("user_name", User_info.username);
        
        access_db(json);
       
	}
	public static void Add_Comment(Comment_info c) throws JSONException{
		JSONObject json = new JSONObject();
        
        json.put("instruction", "Add_Comment");
        json.put("from_name", c.from_name);
        json.put("to_name", c.to_name);
        json.put("time", c.comment_time);
        json.put("comment", c.comment);
        json.put("vote_id", c.vote_id + 1);
        
        access_db(json);
        return;
	}
}
