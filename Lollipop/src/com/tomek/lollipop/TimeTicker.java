package com.tomek.lollipop;

import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeTicker extends TextView {

	private static final int TICK_WHAT = 2;

	public enum Mode {
		COUNT_UP, COUNT_DOWN, COUNT_DOWN_NEGATIVE
	}

	public enum Phase {
		DISABLED, ON_SCHEDULE, WARNING, OVERTIME
	}

	public interface OnTickListener {
		void onTick(TimeTicker ticker);
	}

	private long mBase;
	private boolean mVisible;
	private volatile boolean mStarted;
	private boolean mRunning;
	private static boolean mMoving = false;
	private boolean isShort = false;
	private Mode mMode;
	private OnTickListener mOnTickListener;

	public TimeTicker(Context context) {
		this(context, null, 0);
	}

	public TimeTicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeTicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mBase = System.currentTimeMillis();
		mMode = Mode.COUNT_UP;
		updateText(mBase);
	}

	public void setMode(Mode mode) {
		this.mMode = mode;
	}

	public Mode getMode() {
		return mMode;
	}

	public void setBase(long base) {

		mBase = base;
		dispatchChronometerTick();
		updateText(System.currentTimeMillis());

	}

	public long getBase() {
		return mBase;
	}

	public Phase getPhase() {

		if (mMode == Mode.COUNT_UP) {
			return Phase.ON_SCHEDULE;
		} else {
			long secondsRemaining = (mBase - System.currentTimeMillis()) / 1000;
			if (secondsRemaining > 15 * 60) {
				return Phase.ON_SCHEDULE;
			} else if (secondsRemaining > 0) {
				return Phase.WARNING;
			} else {
				return Phase.OVERTIME;
			}
		}
	}

	public boolean isStarted() {
		return mStarted;
	}

	public void setOnTickListener(OnTickListener listener) {
		mOnTickListener = listener;
	}

	public OnTickListener getOnTickListener() {
		return mOnTickListener;
	}

	public void start() {
		mStarted = true;
		updateRunning();
	}

	public void stop() {
		mStarted = false;
		mRunning = false;
		updateRunning();
	}

	public void setStarted(boolean started) {
		mStarted = started;
		updateRunning();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mVisible = false;
		updateRunning();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == VISIBLE;
		updateRunning();
	}

	public synchronized void updateText(long now) {
		switch (mMode) {

		case COUNT_DOWN: {
			long seconds = (mBase - now) / 1000;
			if (seconds < 0) {
				seconds *= -1;
			}
			if (isShort) {
				setText(formatElapsedTimeShort(seconds));
			} else {
				setText(formatElapsedTime(seconds));
			}
			break;
		}
		case COUNT_DOWN_NEGATIVE: {
			long seconds = (mBase - now) / 1000;
			if (seconds < 0) {
				// overtime, should be displayed as positive
				if (isShort) {
					setText(formatElapsedTimeShort(seconds * -1));
				} else {
					setText(formatElapsedTime(seconds * -1));
				}

			} else {
				// on schedule, display as negative
				if (isShort) {
					setText("-" + formatElapsedTimeShort(seconds));
				} else {
					setText("-" + formatElapsedTime(seconds));
				}

			}
			break;
		}
		case COUNT_UP:
		default: {
			long seconds = (now - mBase) / 1000;
			if (seconds < 0) {
				seconds = 0;
			}
			if (isShort) {
				setText(formatElapsedTimeShort(seconds));
			} else {
				setText(formatElapsedTime(seconds));
			}
			break;
		}
		}
	}

	public synchronized void updateText(long now, long mBase) {
		if (mMode == Mode.COUNT_UP) {

			long seconds = (now - mBase) / 1000;
			if (seconds < 0) {
				seconds = 0;
			}
			this.mBase = mBase;

			setText(formatElapsedTime(seconds));

		}

	}

	private void updateRunning() {
		boolean running = mVisible && mStarted;
		if (running != mRunning) {
			if (running) {
				if (!ismMoving()) {
					updateText(System.currentTimeMillis());
					dispatchChronometerTick();
				}
				mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
			} else {
				mHandler.removeMessages(TICK_WHAT);
			}
			mRunning = running;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			if (mRunning && !mMoving) {
				updateText(System.currentTimeMillis());
				dispatchChronometerTick();

			}
			sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
		}
	};

	void dispatchChronometerTick() {
		if (mOnTickListener != null) {
			mOnTickListener.onTick(this);
		}
	}

	public static String formatElapsedTime(long elapsedSeconds) {
		if (elapsedSeconds < 0) {
			elapsedSeconds *= -1;
		}
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		if (elapsedSeconds >= 3600) {
			hours = elapsedSeconds / 3600;
			elapsedSeconds -= hours * 3600;
		}
		if (elapsedSeconds >= 60) {
			minutes = elapsedSeconds / 60;
			elapsedSeconds -= minutes * 60;
		}
		seconds = elapsedSeconds;
		return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
		// if (hours > 0) {
		// return String.format("%d:%02d:%02d", hours, minutes, seconds);
		// } else {
		// return String.format("%02d:%02d", minutes, seconds);
		// }
	}

	public static String formatElapsedTimeShort(long elapsedSeconds) {
		if (elapsedSeconds < 0) {
			elapsedSeconds *= -1;
		}
		long hours = 0;
		long minutes = 0;

		if (elapsedSeconds >= 3600) {
			hours = elapsedSeconds / 3600;
			elapsedSeconds -= hours * 3600;
		}
		if (elapsedSeconds >= 60) {
			minutes = elapsedSeconds / 60;
			elapsedSeconds -= minutes * 60;
		}

		return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
		// if (hours > 0) {
		// return String.format("%d:%02d:%02d", hours, minutes, seconds);
		// } else {
		// return String.format("%02d:%02d", minutes, seconds);
		// }
	}

	public static boolean ismMoving() {
		return mMoving;
	}

	public static void setmMoving(boolean mMoving) {
		TimeTicker.mMoving = mMoving;
	}

	public boolean isShort() {
		return isShort;
	}

	public void setShort(boolean isShort) {
		this.isShort = isShort;
	}

}
