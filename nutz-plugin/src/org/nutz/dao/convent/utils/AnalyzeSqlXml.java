package org.nutz.dao.convent.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.nutz.dao.convent.tools.pojo.SqlXmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class AnalyzeSqlXml {
	private static Map<String, SqlXmlObject> sqlMap=new LinkedHashMap<String, SqlXmlObject>();
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder builder;
	public static final String BASESQLFILE="sql.xml";
	private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	static{
		initSql();
	}
	public static void initSql(){
		try {
			sqlMap=new LinkedHashMap<String, SqlXmlObject>();
			builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId,String systemId) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
			URL url=classLoader.getResource(BASESQLFILE);
			Document doc=builder.parse(url.toString());
			getSqlXmlObjects(doc);
			// ��include�е�Ҳ�ӽ���
			Document[] docs=getOtherSqlDocs(doc);
			for(int i=0;i<docs.length;i++){
				if(docs[i].getElementsByTagName("sqlMap").getLength()!=0){
					getSqlXmlObjectsForIbatis(docs[i]);
				}else{
					getSqlXmlObjects(docs[i]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("����sql�ļ������쳣!",e);
		}
	}
	public static void getSqlXmlObjectsForIbatis(Document doc){
		NodeList nodeList=doc.getElementsByTagName("sql");//�õ����б�ǩΪsql��node
		buildeXmlObject(nodeList);
		nodeList=doc.getElementsByTagName("select");//�õ����б�ǩΪselect��node
		buildeXmlObject(nodeList);
		nodeList=doc.getElementsByTagName("insert");//�õ����б�ǩΪinsert��node
		buildeXmlObject(nodeList);
		nodeList=doc.getElementsByTagName("update");//�õ����б�ǩΪupdate��node
		buildeXmlObject(nodeList);
	}
	private static void buildeXmlObject(NodeList nodeList) {
		for(int i=0;i<nodeList.getLength();i++){//��������sqlnode
			SqlXmlObject sqlObject = new SqlXmlObject();//����һ������
			Node node=nodeList.item(i);//�õ����е�һ��
			NamedNodeMap nodeMap=node.getAttributes();
			String sqlId=nodeMap.getNamedItem("id").getNodeValue();//�õ�id����
			
			NodeList list=node.getChildNodes();//�õ�sql node���ӽڵ�
			String sqlContent=list.item(0).getNodeValue();//�õ����е�sql
			if(sqlContent==null||"".equals(sqlContent.trim())){
				if(list.getLength()>0){
					sqlContent=list.item(1).getNodeValue();
				}
				if(sqlContent==null||"".equals(sqlContent.trim())){
					throw new RuntimeException("��sqlIdΪ"+sqlId+"��û���ҵ���Ч��sql���!");
				}
			}
			String[] sqls=sqlContent.trim().split("\n");//�����н��зָ�
			StringBuffer newSql=new StringBuffer();//����һ���µ��ַ������洢�޸ĺ��sql���
			for(int j=0;j<sqls.length;j++){
				newSql.append(fiterSql(sqls[j]));
			}
			sqlObject.setSqlId(sqlId);
			sqlObject.setSqlContent(newSql.toString().trim());
			//���ж��Ƿ��Ѿ�����,����о��׳��쳣
			Object sqlObj=sqlMap.get(sqlId);
			if(sqlObj!=null){
				throw new RuntimeException("�Ѿ����ڱ��Ϊ"+sqlId+"��sql���!");
			}
			sqlMap.put(sqlId, sqlObject);//��ӵ�map��
		}
	}
	public static Map getSqlXmlObjects(Document doc){
		NodeList nodeList=doc.getElementsByTagName("sql");//�õ����б�ǩΪsql��node
		buildeXmlObject(nodeList);
		return sqlMap;
	}
	private static Document[] getOtherSqlDocs(Document doc){
		NodeList nodeList=doc.getElementsByTagName("include");//�õ����б�ǩΪsql��node
		//Document[] docs=new Document[nodeList.getLength()];
		List<Document> docs=new ArrayList<Document>();
		for(int i=0;i<nodeList.getLength();i++){
			Node node=nodeList.item(i);//�õ����е�һ��
			NamedNodeMap nodeMap=node.getAttributes();
			String filePath=nodeMap.getNamedItem("file").getNodeValue();//�õ�file����
			try {
				builder=factory.newDocumentBuilder();
				builder.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(String publicId,String systemId) throws SAXException, IOException {
						return new InputSource(new StringReader(""));
					}
				});
				if(filePath.indexOf("*")==-1){
					URL url=classLoader.getResource(filePath);
					docs.add(builder.parse(url.toString()));
				}else{
					String baseFilePath=classLoader.getResource("").getPath();
					
					if(filePath.indexOf("/")!=-1){
						baseFilePath=baseFilePath+filePath.substring(0,filePath.lastIndexOf("/"));
					}
					baseFilePath=java.net.URLDecoder.decode(baseFilePath, "utf-8");
					File baseFile=new File(baseFilePath);
					if(!baseFile.isDirectory()){
						throw new IllegalArgumentException(baseFilePath+"����һ����Ч��Ŀ¼!");
					}
					String fileLikeName=filePath.substring(filePath.lastIndexOf("/")+1);
					
					File[] files=baseFile.listFiles();
					for(int j=0;j<files.length;j++){
						if(BASESQLFILE.equals(files[j].getName())){
							continue;
						}
						if(files[j].getName().indexOf(fileLikeName.replaceAll("\\*", ""))!=-1){
							docs.add(builder.parse(files[j]));
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("�ڽ����ļ�:"+filePath+"�����쳣",e);
			}
		}
		Document[] documents=new Document[docs.size()];
		return  docs.toArray(documents);
	}
	/**
	 * ����sql���,��Ҫ��ȥ��ע��
	 * @param sql
	 * @return
	 */
	public static String fiterSql(String sql){
		String newSql=sql;
		if(sql.indexOf("--")!=-1){
			newSql=sql.substring(0,sql.indexOf("--"));
		}
		if(newSql.indexOf("/*")!=-1){
			newSql=newSql.substring(0,newSql.indexOf("/*"));
		}
		return newSql;
	}
	public static Map getSqlMap(){
		return sqlMap;
	}
	/**
	 * ���������õ�sql���Ὣibatis��#name#��ʽ��Ϊ?
	 * @param sqlId sql���ı��
	 * @return sql���
	 */
	public static String getSql(String sqlId){
		SqlXmlObject sqlObj= (SqlXmlObject) sqlMap.get(sqlId);
		if(sqlObj==null){
			throw new RuntimeException("û���ҵ�sql���Ϊ:"+sqlId+"��sql���");
		}
		return sqlObj.getSqlContent().replaceAll("#[a-zA-Z]+[1-9]?#", "?");
	}
	/**
	 * ���ԭʼ��sql���
	 * @param sqlId sql���ı��
	 * @return sql���
	 */
	public static String getFormerSql(String sqlId){
		SqlXmlObject sqlObj= (SqlXmlObject) sqlMap.get(sqlId);
		if(sqlObj==null){
			throw new RuntimeException("û���ҵ�sql���Ϊ:"+sqlId+"��sql���");
		}
		return sqlObj.getSqlContent();
	}
	public static void main(String[] args) {
		String sql=AnalyzeSqlXml.getFormerSql("saveUser");
		//AnalyzeSqlXml.initSql();
		//sql="select * from user/**/";
		System.out.println(sql);
		System.out.println(fiterSql(sql));
		System.out.println("insert into user(user_name,age,gender,birthday,update_dt) values(#userName#,#age#,#gender#,#birthday#,now())".replaceAll("#[a-zA-Z]+[1-9]?#", "?"));
		
	}
	
}
