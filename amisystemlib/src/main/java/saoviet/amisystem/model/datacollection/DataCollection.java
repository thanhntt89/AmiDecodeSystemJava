/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: DataCollection.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 * Tạo list cấu trúc chứa dữ liệu bản tin
 */

package saoviet.amisystem.model.datacollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import saoviet.amisystem.model.DataField;
import saoviet.amisystem.ulti.StringUlti;

public class DataCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DataField> dataList = Collections.synchronizedList(new ArrayList<DataField>());

	public void setDataList(List<DataField> dataList) {
		this.dataList = new ArrayList<DataField>(dataList);
	}

	public List<DataField> getdataList() {
		return new ArrayList<DataField>(dataList);
	}

	/**
	 * Đưa dữ liệu vào list theo từng trường
	 * 
	 * @param tagId
	 * @param fieldCode
	 * @param name
	 * @param length
	 * @param data
	 * @param value
	 * @param scale
	 * @param type
	 */
	public void add(String tagId, String fieldCode, String name, int length, String data, BigDecimal scaledefault,
			BigDecimal scale, String type, List<String> stringList) {
		DataField item = new DataField();

		item.setTagId(tagId);
		item.setFieldCode(fieldCode);
		item.setName(name);
		item.setLength(length);
		item.setData(data);
		item.setDefaultScale(scaledefault);
		item.setScale(scale);
		item.setType(type);
		item.setValueList(stringList);
		this.add(item);
		item = null;
		Runtime.getRuntime().gc();
	}

	public void add(String fieldCode, String data) {
		DataField item = new DataField();
		item.setFieldCode(fieldCode);
		item.setData(data);
		this.add(item);
	}

	public void add(String name, BigDecimal scale) {
		DataField item = new DataField();
		item.setName(name);
		item.setScale(scale);
		this.add(item);
		item = null;
		Runtime.getRuntime().gc();
	}

	/**
	 * Đữ dữ liệu vào list kiểu cấu trúc
	 * 
	 * @param data
	 */
	public void add(DataField data) {
		try {
			this.dataList.add(data);
		} catch (Exception ex) {
		}
	}

	/**
	 * Cập nhật trường data cho bản tin trường hợp obis trùng nhau sử dụng thêm
	 * điều kiện obiTag
	 * 
	 * @param tagId
	 * @param obisCode
	 * @param data
	 */
	public void updateDataByObisCodeAndTagId(String tagId, String obisCode, String data) {
		try {
			int index = this.dataList.indexOf(new DataField(tagId, obisCode));
			if (index < 0)// Index <0 item not exist in list
				return;
			this.dataList.get(index).setData(data);
		} catch (Exception ex) {
		}
	}

	/**
	 * Cập nhật trường data cho bản tin trường hợp obis không trùng nhau
	 * 
	 * @param obisCode
	 * @param data
	 */
	public void updateDataByObisCode(String obisCode, String data) {
		try {
			int index = this.dataList.indexOf(new DataField(obisCode));
			if (index < 0)// Index <0 item not exist in list
				return;
			this.dataList.get(index).setData(data);
		} catch (Exception ex) {
		}
	}

	public void updateScaleByObisCode(String obisCode, BigDecimal scale) {
		try {
			int index = this.dataList.indexOf(new DataField(obisCode));
			if (index < 0)// Index <0 item not exist in list
				return;
			this.dataList.get(index).setScale(scale);
		} catch (Exception ex) {
		}
	}

	/**
	 * Cập nhật List các trường theo tagid và obiscode
	 * 
	 * @param tagId
	 * @param obisCode
	 * @param data
	 */
	public void updateListDataByObisCodeAndTagId(String tagId, String obisCode, List<String> stringList,
			List<DataField> obisValueFullList) {
		try {
			int index = this.dataList.indexOf(new DataField(tagId, obisCode));
			if (index < 0)// Index <0 item not exist in list
				return;
			this.dataList.get(index).setValueList(stringList);
			this.dataList.get(index).setObisValueFullList(obisValueFullList);
		} catch (Exception ex) {
		}
	}

	public void updateValueListByObisCodeAndTagId(String tagId, String obisCode, List<String> listdata) {
		try {
			int index = this.dataList.indexOf(new DataField(tagId, obisCode));
			if (index < 0)// Index <0 item not exist in list
				return;
			this.dataList.get(index).setValueList(listdata);
		} catch (Exception ex) {
		}
	}

	/**
	 * Lấy về giá trị của trường dữ liệu trong bản tin
	 * 
	 * @param fieldCode
	 * @return
	 */
	public DataField getDataByFieldCode(String fieldCode) {
		DataField value = new DataField();
		try {
			int index = this.dataList.indexOf(new DataField(fieldCode));
			if (index > -1) {
				value = this.dataList.get(index);
			}
			return value;
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Thêm list sự kiện cho obis sự kiện
	 * 
	 * @param obisCode
	 * @param tagId
	 * @param data
	 */
	public void addEvent(String obisCode, String tagId, String data) {
		DataField exist = this.getDataByFieldCode(obisCode);

		if (exist == null)
			return;
		try {
			DataField item = new DataField();
			item.setName(exist.getName());
			item.setTagId(tagId);
			item.setData(data);
			item.setFieldCode(StringUlti.PadLeft(String.valueOf(exist.getEventList().size()), 1, '0'));
			exist.getEventList().add(item);
			item = null;
			exist = null;
		} catch (Exception ex) {
		}
	}

	/**
	 * Lấy phần tử trong list
	 * 
	 * @param tagId
	 * @param obisCode
	 * @return
	 */
	public DataField getDataByTagId(String tagId, String obisCode) {
		try {
			DataField item = new DataField();
			int index = this.dataList.indexOf(new DataField(tagId, obisCode));
			if (index < 0)// Index <0 item not exist in list
				item = null;
			else
				item = this.dataList.get(index);
			return item;
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Get item with the same tagid
	 * 
	 * @param tagId
	 * @return
	 */
	public DataCollection getDataCollectionByTagId(String tagId) {
		DataCollection dataTemp = new DataCollection();
		for (DataField item : this.dataList) {
			if (item.getTagId().equals(tagId))
				dataTemp.add(item);
		}
		return dataTemp;
	}

	/**
	 * Create list copy without referent
	 * 
	 * @return
	 */
	public DataCollection copy() {
		DataCollection objDest = null;
		byte[] byteData = null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bais = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			bos.close();
			byteData = bos.toByteArray();
			bais = new ByteArrayInputStream(byteData);
			try {
				objDest = (DataCollection) new ObjectInputStream(bais).readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			byteData = null;
			bos = null;
			oos = null;
			Runtime.getRuntime().gc();
		}
		return objDest;
	}

	/**
	 * Đưa vào obisname lấy ra giá trị data
	 * 
	 * @param obisname
	 * @return
	 */
	public String getDatabyName(String obisname) {
		try {
			DataField value = new DataField();
			DataField search = new DataField();
			search.setName(obisname);
			int index = this.dataList.indexOf(search);
			if (index > -1) {
				value = this.dataList.get(index);
			}
			return value.getData();
		} catch (Exception ex) {
		}
		return null;
	}

	public List<String> getValueListbyName(String obisname) {
		try {
			DataField value = new DataField();
			DataField search = new DataField();
			search.setName(obisname);
			int index = this.dataList.indexOf(search);
			if (index > -1) {
				value = this.dataList.get(index);
			}

			search = null;
			
			return value.getValueList();
		} catch (Exception ex) {
		}
		return null;
	}

	public BigDecimal getScalebyName(String obisname) {
		try {
			DataField value = new DataField();
			DataField search = new DataField();
			search.setName(obisname);
			int index = this.dataList.indexOf(search);
			if (index > -1) {
				value = this.dataList.get(index);
			}
			search = null;
			return value.getScale();
		} catch (Exception ex) {
		}
		return null;
	}

	public DataField getDataFieldByObisName(String obisName) {
		try {
			DataField dataTemp = new DataField();
			for (DataField item : this.dataList) {
				if (item.getName().equals(obisName)) {
					dataTemp = item;
					return dataTemp;
				}
			}
			return dataTemp;
		} catch (Exception ex) {
		}
		return null;
	}
}
