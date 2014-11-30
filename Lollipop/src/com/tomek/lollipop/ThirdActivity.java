package com.tomek.lollipop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends Activity {

	Palette palette;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_main);
		ImageView imageRor = (ImageView) findViewById(R.id.ror);
		imageRor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View image2 = findViewById(R.id.ror);

				if (image2.getVisibility() == View.INVISIBLE) {
					int radius = Math.max(image2.getWidth(), image2.getHeight());
					Animator anim = ViewAnimationUtils.createCircularReveal(
							image2, image2.getLeft(), image2.getTop(), 0,
							radius);
					image2.setVisibility(View.VISIBLE);
					anim.start();
				} else {
					int initialRadius = image2.getWidth();
					Animator animation = ViewAnimationUtils
							.createCircularReveal(image2, image2.getRight()
									- image2.getLeft(), image2.getBottom()
									- image2.getTop(), initialRadius, 0);
					animation.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
							findViewById(R.id.ror)
									.setVisibility(View.INVISIBLE);
						}
					});
					animation.start();
				}
			}

		});
		ImageView image = (ImageView) findViewById(R.id.quarter);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animatable anim = ((Animatable) ((ImageView) v).getDrawable());
				if (anim.isRunning()) {
					anim.stop();
				} else {
					anim.start();
				}
			}
		});
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

	@Override
	public void onBackPressed() {
		finishAfterTransition();
		super.onBackPressed();
	}
}
