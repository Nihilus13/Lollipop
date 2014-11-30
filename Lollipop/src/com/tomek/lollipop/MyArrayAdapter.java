package com.tomek.lollipop;

import java.util.List;

import com.tomek.lollipop.TimeTicker.Mode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
	public MyArrayAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String item = getItem(position);

		holder.mTextView.setText(item);
		if (position == 3 && !holder.mTimeTicker.isStarted()) {
			holder.mTimeTicker.setBase(System.currentTimeMillis());
			holder.mTimeTicker.start();
			holder.mTimeTicker.setMode(Mode.COUNT_DOWN);
		}

		return view;
	}

	public static class ViewHolder {
		// each data item is just a string in this case
		public TextView mTextView;
		public TimeTicker mTimeTicker;

		public ViewHolder(View v) {
			mTextView = (TextView) v.findViewById(R.id.text);
			mTimeTicker = (TimeTicker) v.findViewById(R.id.ticker);
		}
	}
}