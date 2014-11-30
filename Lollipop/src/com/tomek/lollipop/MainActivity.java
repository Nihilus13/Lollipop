package com.tomek.lollipop;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button mButton;
	String[] myDataset = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
			"22", "23", "24", "25", "26", "27", "28", "29", "30" };

	private RecyclerView recycler;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mButton = (Button) findViewById(R.id.button);
		recycler = (RecyclerView) findViewById(R.id.recycler);
		list = (ListView) findViewById(R.id.list);
		MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, 0, myDataset);
		list.setAdapter(myArrayAdapter);
		recycler.setHasFixedSize(true);

		// use a linear layout manager
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recycler.setLayoutManager(mLayoutManager);

		MyAdapter adapter = new MyAdapter(myDataset);
		recycler.setAdapter(adapter);

		mButton.setOutlineProvider(new ViewOutlineProvider() {

			@Override
			public void getOutline(View view, Outline outline) {
				outline.setOval(0, 0, view.getWidth(), view.getHeight());

			}
		});
		recycler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "?", Toast.LENGTH_SHORT)
						.show();

			}
		});
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);
				startActivity(intent, ActivityOptions
						.makeSceneTransitionAnimation(MainActivity.this)
						.toBundle());
			}
		});
		getWindow().setExitTransition(new Explode());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
