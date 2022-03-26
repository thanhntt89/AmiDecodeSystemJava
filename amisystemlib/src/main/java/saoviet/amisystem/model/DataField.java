/*
 * Copyright (C) 2016 by Sao Viet Joint Stock Company
 * Class Name: DataField.java
 * Author: Nguyen Tat Thanh
 * Email: thanhntt89@yahoo.com or thanhnt@saovietgroup.com.vn
 * Mobile: 098 664 8910
 * Time: 2016-04-05 15:47:06
 */

package saoviet.amisystem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataField implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getTagId() {
		return this.tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getFieldCode() {
		return this.fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getScale() {
		return this.scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DataField> getEventList() {
		return this.eventList;
	}

	public void setEventList(List<DataField> eventList) {
		this.eventList = eventList;
	}

	private String tagId;

	private String fieldCode;

	private String name;

	private int length;

	private String data;

	private BigDecimal value;

	private BigDecimal scale;

	private BigDecimal defaultScale;
	
	private List<String> valueList;

	public List<String> getValueList() {
		return valueList;
	}

	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}

	public BigDecimal getDefaultScale() {
		return this.defaultScale;
	}

	public void setDefaultScale(BigDecimal defaultScale) {
		this.defaultScale = defaultScale;
	}

	// MeterType
	private String type;

	private List<DataField> eventList = new ArrayList<DataField>();
	
	private List<DataField> obisValueFullList = new ArrayList<DataField>();
	public DataField() {

	}

	// Tim kiem trong truong hop chi co mot truong
	public DataField(String fieldCode) {
		this.setFieldCode(fieldCode);
	}

	public DataField(String tagId, String fieldCode) {
		this.setFieldCode(fieldCode);
		this.setTagId(tagId);
	}

	public DataField(String tagId, String fieldCode, String name, int length, String data, BigDecimal value,
			BigDecimal scale, String type) {
		this.setData(data);
		this.setFieldCode(fieldCode);
		this.setLength(length);
		this.setName(name);
		this.setScale(scale);
		this.setTagId(tagId);
		this.setType(type);
		this.setValue(value);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		DataField other = (DataField) o;
		if (this.fieldCode != null && !this.fieldCode.equals(other.fieldCode))
			return false;
		if (this.name != null && !this.name.equals(other.name))
			return false;
		if (other != null && other.tagId != null && other.tagId != "" && this.tagId != null)
			if (!this.tagId.equals(other.tagId))
				return false;

		return true;
	}

	public List<DataField> getObisValueFullList() {
		return obisValueFullList;
	}

	public void setObisValueFullList(List<DataField> obisValueFullList) {
		this.obisValueFullList = obisValueFullList;
	}
}
