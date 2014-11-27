package com.tomek.lollipop;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.tomek.lollipop.TimeTicker.Mode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
	private String[] mDataset;
	private static final SimpleDateFormat DATE_FORMAT_TIME_START = new SimpleDateFormat(
			"HH:mm:ss", Locale.getDefault());

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView mTextView;
		public TimeTicker mTimeTicker;

		public ViewHolder(View v) {
			super(v);
			mTextView = (TextView) v.findViewById(R.id.text);
			mTimeTicker = (TimeTicker) v.findViewById(R.id.ticker);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyAdapter(String[] myDataset) {
		mDataset = myDataset;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		holder.mTextView.setText(mDataset[position]);
		if (position == 3 && !holder.mTimeTicker.isStarted()) {
			holder.mTimeTicker.setBase(System.currentTimeMillis());
			holder.mTimeTicker.start();
			holder.mTimeTicker.setMode(Mode.COUNT_DOWN);
		}

	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.length;
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		// TODO Auto-generated method stub
		super.onViewRecycled(holder);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item, parent, false);
		// set the view's size, margins, paddings and layout parameters

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}
}