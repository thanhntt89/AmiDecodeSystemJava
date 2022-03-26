/*----------------------------------------------------------------
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: ThreePhaseDecode.java
 * Author: Dong Phung
 * Email: phungdong92@gmail.com or dongpv@saovietgroup.com.vn
 * Mobile: 0983 643 739
 * Time: 2016-05-17 11:47:06
 *----------------------------------------------------------------*/

package saoviet.amisystem.model.meter.threephase.decode;

import saoviet.amisystem.business.IMT3Business;
import saoviet.amisystem.business.MT3Business;
import saoviet.amisystem.event.SystemEventCallback;

public class ThreePhaseDecode implements IThreePhaseDecode {

	private ElsterMessageDecode elsterDecode;

	private GelexMessageDecode gelexMessageDecode;

	private LandisMessageDecode landisMessageDecode;

	private StarMessageDecode starMessageDecode;

	private IMT3Business iMT3Business;

	private Thread threadAutoInsert;

	public ThreePhaseDecode() {
		// this.iMT3Business = new MT3Business();
		this.threadAutoInsert = new Thread(this.iMT3Business = new MT3Business());
		
		// TODO Auto-generated constructor stub
		this.elsterDecode = new ElsterMessageDecode(this.iMT3Business);
		this.gelexMessageDecode = new GelexMessageDecode(this.iMT3Business);
		this.landisMessageDecode = new LandisMessageDecode(this.iMT3Business);
		this.starMessageDecode = new StarMessageDecode(this.iMT3Business);
	}

	public void  StartThreadAutoInsertDatabase()
	{
		this.threadAutoInsert.start();
	}
	
	public boolean StopThreadAutoInsertDatabase() {
		return this.iMT3Business.StopThreadAutoInsert();
	}

	@Override
	public IThreePhaseMessageDecode getIElsterDecode() {
		// TODO Auto-generated method stub
		return this.elsterDecode;
	}

	@Override
	public IThreePhaseMessageDecode getILandisDecode() {
		// TODO Auto-generated method stub
		return this.landisMessageDecode;
	}

	@Override
	public IThreePhaseMessageDecode getIGelexDecode() {
		// TODO Auto-generated method stub
		return this.gelexMessageDecode;
	}

	@Override
	public IThreePhaseMessageDecode getIStarDecode() {
		// TODO Auto-generated method stub
		return this.starMessageDecode;
	}

	@Override
	public void setSystemEventCallBack(SystemEventCallback systemEventCallback) {
		// TODO Auto-generated method stub
		this.elsterDecode.setSystemEventCallBack(systemEventCallback);
		this.landisMessageDecode.setSystemEventCallBack(systemEventCallback);
		this.gelexMessageDecode.setSystemEventCallBack(systemEventCallback);
		this.starMessageDecode.setSystemEventCallBack(systemEventCallback);
	}
}
