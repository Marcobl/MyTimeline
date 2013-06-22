package com.marcobl.android.mytimeline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TweetListAdapter extends BaseAdapter {
	
	private JSONArray tweetArray;
	private LayoutInflater inflater;
	
	
	public TweetListAdapter(JSONArray arg1, LayoutInflater inflater) {
		
		this.inflater = inflater;
		this.tweetArray = arg1;
	}

	@Override
	public int getCount() {
		
		return  tweetArray.length();
	}

	@Override
	public JSONObject getItem(int arg0) {
		
		try {
			return tweetArray.getJSONObject(arg0);
		} catch (JSONException e) {
			
			e.printStackTrace();
			return new JSONObject();
		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		
		view = this.inflater.inflate(R.layout.tweet_layout, null);
		TextView lbDate = (TextView) view.findViewById(R.id.label_date);
		TextView lbTweet = (TextView) view.findViewById(R.id.label_tweet);
		
		try {
			lbDate.setText(tweetArray.getJSONObject(position).getString("created_at"));
			lbTweet.setText(tweetArray.getJSONObject(position).getString("text"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return view;
		
		
	}

}
