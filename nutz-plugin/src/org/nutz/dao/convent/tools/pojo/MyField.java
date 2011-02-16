package org.nutz.dao.convent.tools.pojo;

public class MyField {
	/**
	 * �ֶ���
	 */
	protected String fieldName;
	/**
	 * �ֶ�����
	 */
	protected Class fieldType;
	/**
	 * �ֶγ���
	 */
	protected int fieldLength;
	/**
	 * �Ƿ�����Ϊ��
	 */
	protected boolean allowNull;
	/**
	 * ��Ӧjava.sql.Types�е�����
	 */
	protected int dataType;
	/**
	 * �����ݿ�������
	 */
	protected String dbFieldType;
	/**
	 * �Ƿ�������
	 */
	protected boolean key;
	/**
	 * Ĭ��ֵ
	 */
	protected Object defaultValue;
	/**
	 * �Ƿ������
	 */
	protected boolean foreignKey;
	/**
	 * ����(С��λ)
	 */
	protected int scale;
	/**
	 * С����λ��,��������oracle��Ƶ��ֶ�
	 */
	protected int precision;
	//protected String chineseName;//������
	/**
	 * ��ע��
	 */
	protected String remarks;
	
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDbFieldType() {
		return dbFieldType;
	}

	public void setDbFieldType(String dbFieldType) {
		this.dbFieldType = dbFieldType;
	}

	public MyField() {
		// TODO Auto-generated constructor stub
	}
	
	public MyField(String fieldName, Class fieldType, String chineseName) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
//		this.chineseName = chineseName;
	}

	public MyField(String fieldName, Class fieldType, int fieldLength, boolean isNull,String chineseName) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLength = fieldLength;
		this.allowNull = isNull;
//		this.chineseName=chineseName;
	}
	public int getFieldLength() {
		return fieldLength;
	}
	public MyField setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
		return this;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public MyField setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
		return this;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


//	public String getChineseName() {
//		return chineseName;
//	}
//	public void setChineseName(String chineseName) {
//		this.chineseName = chineseName;
//	}
}
