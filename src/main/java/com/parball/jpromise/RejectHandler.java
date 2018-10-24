package com.parball.jpromise;

public interface RejectHandler extends Handler {
	public void onRejected(Exception e);
}
