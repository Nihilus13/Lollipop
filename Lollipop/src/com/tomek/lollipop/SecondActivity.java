package com.tomek.lollipop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.graphics.Palette.Swatch;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

	Palette palette;
	int drawableres;
	boolean selected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_main);
		ImageView imageView = (ImageView) findViewById(R.id.image);
		drawableres = R.drawable.rajwajmen;
		Bitmap bitmap = drawableToBitmap(imageView.getDrawable());
		bitmap.setHasAlpha(true);
		Palette.generateAsync(bitmap, listener);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (drawableres == R.drawable.rajwajmen) {
					((ImageView) v).setImageResource(R.drawable.rajwajmen2);
					drawableres = R.drawable.rajwajmen2;
				} else {
					((ImageView) v).setImageResource(R.drawable.rajwajmen);
					drawableres = R.drawable.rajwajmen;
				}

				Bitmap bitmap = drawableToBitmap(((ImageView) v).getDrawable());
				bitmap.setHasAlpha(true);
				Palette.generateAsync(bitmap, listener);

			}
		});
		imageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(SecondActivity.this,
						ThirdActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}

	PaletteAsyncListener listener = new PaletteAsyncListener() {

		@Override
		public void onGenerated(Palette arg0) {
			SecondActivity.this.palette = arg0;

			LinearLayout swatches = (LinearLayout) findViewById(R.id.swatches);
			swatches.removeAllViews();
			for (Swatch swatch : palette.getSwatches()) {
				ImageView image = new ImageView(SecondActivity.this);
				image.setBackgroundColor(swatch.getRgb());
				image.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
				image.setOnClickListener(onClickListener);
				swatches.addView(image);
			}
			((TextView) findViewById(R.id.test)).setTextColor(palette
					.getLightVibrantSwatch().getTitleTextColor());

		}
	};

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!selected) {
				View image2 = findViewById(R.id.image2);
				int radius = Math.max(image2.getWidth(), image2.getHeight());
				Animator anim = ViewAnimationUtils.createCircularReveal(image2,
						image2.getLeft(), image2.getTop(), 0, radius);
				image2.setVisibility(View.VISIBLE);
				anim.start();
				selected = true;
			} else {
				final View image2 = findViewById(R.id.image2);
				int initialRadius = image2.getWidth();
				Animator animation = ViewAnimationUtils.createCircularReveal(
						image2, image2.getRight() - image2.getLeft(),
						image2.getBottom() - image2.getTop(), initialRadius, 0);
				animation.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						image2.setVisibility(View.INVISIBLE);
					}
				});
				animation.start();
				selected = false;
			}
		}
	};

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

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
