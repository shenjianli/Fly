package com.shenjianli.fly.map;

public class MapResultData {
	private boolean isSuccess = false;
	private int mWhat;
	private Object mResult = ""; 

	public MapResultData(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Object getResult() {
		return mResult;
	}

	public void setResult(Object result) {
		this.mResult = result;
	}

	public int getWhat() {
		return mWhat;
	}

	public void setWhat(int mWhat) {
		this.mWhat = mWhat;
	}
	
	@Override
	public String toString() {
		return isSuccess + ":" + mWhat + ":" + mResult;
	}

}
