package com.shenjianli.fly.app.engine.map;

public interface UpdateMapResultListener {
	public int LOCATION_ADDRESS = 0x2015;
	public int SHOW_LABEL = LOCATION_ADDRESS +1;
	public int RESULT_RECOMMAND_DATA = SHOW_LABEL +1;
	public int RESULT_GET_LOT_LOG_DATA_FAIL= RESULT_RECOMMAND_DATA +1;
	
	public void updateMapResult(MapResultData data);

}
