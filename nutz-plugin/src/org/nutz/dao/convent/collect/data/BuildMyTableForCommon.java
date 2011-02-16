package org.nutz.dao.convent.collect.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.convent.tools.pojo.MyField;
import org.nutz.dao.convent.tools.pojo.MyTable;
import org.nutz.dao.convent.utils.DataBaseHelper;


public class BuildMyTableForCommon extends BaseBuildMyTable {

	private Connection conn;
	private boolean autoClose=true;
	public boolean isAutoClose() {
		return autoClose;
	}
	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}
	public BuildMyTableForCommon(Connection conn) {
		this.conn=conn;
	}
	@SuppressWarnings("unchecked")
	public Map getForeignKeys() {
		Map foreignMap=new HashMap();
		ResultSet rs=null;
		try {
			DatabaseMetaData metaData=conn.getMetaData();
			rs=metaData.getTables(conn.getCatalog(), metaData.getUserName(), null, new String[]{"TABLE"});
			while(rs.next()){
				foreignMap.putAll(this.getForeignKey(rs.getString("TABLE_NAME")));
			}
		} catch (SQLException e) {
			throw new RuntimeException("��ȡ���б�������Ϣ�쳣!",e);
		} finally{
			DataBaseHelper.closeRs(rs);
			if(this.autoClose){
				DataBaseHelper.closeConn(conn);
			}
		}
		return foreignMap; 
	}
	@SuppressWarnings("unchecked")
	public Map getForeignKey(String tableName) {
		
		Map foreignMap=new HashMap();
		ResultSet rs=null;
		try {
			DatabaseMetaData metaData=conn.getMetaData();
			rs=metaData.getImportedKeys(conn.getCatalog(), metaData.getUserName(), tableName);
			
			while(rs.next()){
				String pkTableName=rs.getString("PKTABLE_NAME");//������
				String pkColumnName=rs.getString("PKCOLUMN_NAME");//������
				String fkTableName=rs.getString("FKTABLE_NAME");//�����
				String fkColumnName=rs.getString("FKCOLUMN_NAME");//�����
				foreignMap.put((fkTableName+"."+fkColumnName).toUpperCase(), (pkTableName+"."+pkColumnName).toUpperCase());
			}
			
		} catch (SQLException e) {
			//������ֵ��쳣��ô����?
			e.printStackTrace();
		} finally{
			DataBaseHelper.closeRs(rs);
			if(this.autoClose){
				DataBaseHelper.closeConn(conn);
			}
		}
		return foreignMap; 
	}
	public MyTable getMyTable(String tableName) {
		MyTable table=new MyTable();
		table.setTableName(tableName);//���ñ���
		MyField[] fields=this.getTableField(tableName);//�õ������������
		table.setFields(fields);
		table.setInsertSql(this.buildInsertSql(tableName,fields));//���ò������(����ο�MyTable��)
		table.setUpdateSql(this.buildUpdateSql(tableName, fields));//���ø������
		table.setDeleteSql(this.buildDeleteSql(tableName, fields));//����ɾ�����
		table.setSelectSql(this.buildSelectSql(tableName, fields));//����ѡ�����
		return table;
	}

	@SuppressWarnings("unchecked")
	public MyTable[] getMyTables() {
		ResultSet rs=null;
		List tables=new ArrayList();
		try {
			DatabaseMetaData metaData=conn.getMetaData();
			rs=metaData.getTables(conn.getCatalog(), metaData.getUserName(), null, new String[]{"TABLE"});
			while(rs.next()){
				MyTable table=this.getMyTable(rs.getString("TABLE_NAME"));
				tables.add(table);
			}
		} catch (Exception e) {
			throw new RuntimeException("�����ݿ�ı�ת��Ϊ�����ʱ�����",e);
		} finally{
			DataBaseHelper.closeRs(rs);
			if(this.autoClose){
				DataBaseHelper.closeConn(conn);
			}
		}
		MyTable[] tablesArray=new MyTable[tables.size()];
		tables.toArray(tablesArray);
		return tablesArray;
	}

	@SuppressWarnings("unchecked")
	public MyField[] getTableField(String tableName) {
		List fields=new ArrayList();
		ResultSet rs=null;
		ResultSet rs2=null;
		try {
			DatabaseMetaData metaData=conn.getMetaData();
			rs=metaData.getColumns(conn.getCatalog(), metaData.getUserName(), tableName, null);
			while(rs.next()){
				String columnName=rs.getString("COLUMN_NAME");
				int dataType=rs.getInt("DATA_TYPE");
				String typeName=rs.getString("TYPE_NAME");
				int columnSize=rs.getInt("COLUMN_SIZE");
				int decimalDigits=rs.getInt("DECIMAL_DIGITS");
//				int numPrecRadix=rs.getInt("NUM_PREC_RADIX");
				int nullable=rs.getInt("NULLABLE");
				String remarks=rs.getString("REMARKS");
				String columnDef=rs.getString("COLUMN_DEF");
				MyField field=new MyField();
				field.setFieldName(columnName);
				field.setDataType(dataType);
				field.setDbFieldType(typeName);
				field.setFieldLength(columnSize);
				field.setScale(decimalDigits);
				field.setAllowNull(nullable==java.sql.DatabaseMetaData.columnNoNulls?false:true);
				field.setDefaultValue(columnDef);
				field.setRemarks(remarks);
				fields.add(field);
			}
			//��ѯ������Ϣ
			rs2=metaData.getPrimaryKeys(conn.getCatalog(), metaData.getUserName(), tableName);
			List keys=new ArrayList();
			while(rs2.next()){
				keys.add(rs2.getString("COLUMN_NAME"));
			}
			for(int i=0;i<fields.size();i++){
				MyField f=(MyField) fields.get(i);
				if(keys.contains(f.getFieldName())){
					f.setKey(true);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("����"+tableName+"����ת��ΪField�����ʱ�����",e);
		} finally{
			DataBaseHelper.closeRs(rs);
			DataBaseHelper.closeRs(rs2);
			if(this.autoClose){
				DataBaseHelper.closeConn(conn);
			}
		}
		MyField[] fieldArray=new MyField[fields.size()];
		fields.toArray(fieldArray);
		return fieldArray;
	}
}
