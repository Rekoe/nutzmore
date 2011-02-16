/**
 * 
 */
package org.nutz.dao.convent.tools.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Title:��������
 * Description:��������
 * </pre>
 * @author liaohongliu liaohl@yuchengtech.com
 * @version 1.0   2009-7-29
 * 
 * <pre>
 * �޸ļ�¼
 * 	  �޸ĺ�汾:      �޸���:     �޸�ʱ��:       �޸�����
 * </pre>
 */
public class MyTable {
	private String tableChineseName;//���������
	private String tableName;//����
	private MyField[] fields;//���Ӧ���ֶ�
	private String insertSql;//����һ����¼��sql
	private String updateSql;//������Ϊ����������sql
	private String deleteSql;//������Ϊɾ��������sql
	private String selectSql;//������Ϊ�����Ĳ�ѯsql

	public String getSelectSql() {
		return selectSql;
	}
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}
	public MyField[] getFields() {
		return fields;
	}
	public List<MyField> getFieldList() {
		if(fields==null||fields.length==0){
			throw new RuntimeException("����ֶζ�������Ϊ��,��������ȷ��!");
		}
		List<MyField> fieldList=new ArrayList<MyField>();
		for(int i=0;i<fields.length;i++){
			fieldList.add(fields[i]);
		}
		return fieldList;
	}
	public boolean containField(String fieldName){
		List<MyField> fields=this.getFieldList();
		for (MyField field : fields) {
			if(field.getFieldName().equals(fieldName)){
				return true;
			}
		}
		return false;
	}
	public MyField getField(String fieldName){
		List<MyField> fields=this.getFieldList();
		for (MyField field : fields) {
			if(field.getFieldName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	public List<MyField> getPkFields(){
		List<MyField> pkFields=new ArrayList<MyField>();
		List<MyField> fields=this.getFieldList();
		for (MyField field : fields) {
			if(field.isKey()){
				pkFields.add(field);
			}
		}
		return pkFields;
	}
	public void setFields(MyField[] fields) {
		this.fields = fields;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public MyTable() {
		// TODO �Զ����ɹ��캯�����
	}
	public MyTable(String tableId, String tableChineseName) {
		super();
//		this.tableId = tableId;
		this.tableChineseName = tableChineseName;
	}
	public String getTableChineseName() {
		return tableChineseName;
	}
	public MyTable setTableChineseName(String tableChineseName) {
		this.tableChineseName = tableChineseName;
		return this;
	}
	public String getDeleteSql() {
		return deleteSql;
	}
	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
}
