/**
 * 
 */
package org.nutz.dao.convent.utils;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author user
 *
 */
public class CommonUtils {

	public static final SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat dateTimeFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat tableFormatter=new SimpleDateFormat("yyMMdd");
	/**
	 * ��Strng���͵�ֵת��Ϊ��Ӧ������
	 * @param value String���͵�ֵ
	 * @param toType ת��������
	 * @return ת�����ͺ��ֵ
	 */
	public static Object getValueFromString(String value,Class toType){
		if(toType==Double.class){
			return new Double(value);
		}else if(toType==Integer.class){
			return new Integer(value);
		}else if(toType==Long.class){
			return new Long(value);
		}else if(toType==java.util.Date.class){
			return java.sql.Date.valueOf(value);
		}else{
			return value;
		}
	}
	/**
	 * �ж��ַ�������Ч��
	 * @param str Ҫ�жϵ��ַ���
	 * @return �Ƿ���Ч,true:��Ч
	 */
	public static boolean isValidString(String str){
		return str!=null&&str.trim().length()>0;
	}
	public static boolean isValidNum(String str){
		if(str==null || "".equals(str.trim())){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str.trim());
		if(isNum.matches()){
			return true;
		}else{
			return false;
		} 
	}
	/**
	 * �õ����С�����ʽ���ַ��������һ��
	 * @param name �ַ���
	 * @return ��ȡ����ַ���
	 */
	public static String getSimpleName(String name){
		if(name.lastIndexOf(".")!=-1){
			String temp=name.substring(name.lastIndexOf(".")+1);
			return temp;
		}else{
			return name;
		}
	}
	/**
	 * �õ����С�����ʽ���ַ������ʼһ��
	 * @param name �ַ���
	 * @return ��ȡ����ַ���
	 */
	public static String getFirstName(String name){
		if(name.indexOf(".")!=-1){
			String temp=name.substring(0,name.indexOf("."));
			return temp;
		}else{
			return name;
		}
	}
	/**
	 * �����ֶ���������ֶ�,֧������
	 * @param clazz ָ��Ҫ����ֶε���
	 * @param name �ֶ���,�༶�õ�Ÿ���
	 * @return �ҵ����ֶ�,���û�о�Ϊ��
	 */
	public static Field getFieldByString(Class clazz,String name){
		try {
			if(name.indexOf(".")==-1){
				return clazz.getDeclaredField(name);
			}
			Field field=clazz.getDeclaredField(name.substring(0,name.indexOf(".")));
			Class cla=field.getType();
			return cla.getDeclaredField(name.substring(name.indexOf(".")+1));
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * ������ĸ��д
	 * @param str Ҫ��д�ĵ���
	 * @return ����ĸ��д��ĵ���
	 */
	public static String getFirstUpper(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	public static void closeStream(Closeable steam){
		try {
			if(steam!=null){
				steam.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getYear(String inputDate){
		String[] datas=inputDate.split("-");
		return datas[0];
	}
	public static String getMonth(String inputDate){
		String[] datas=inputDate.split("-");
		return datas[1];
	}
	public static String getDay(String inputDate){
		String[] datas=inputDate.split("-");
		return datas[2];
	}
	public static Date getDayFromStr(String inputDate){
		return java.sql.Date.valueOf(inputDate);
	}
	public static List<File> getFilesBySuffix(String dir, String... suffixs) {
		File file = new File(dir);
		List<File> resultFile = new ArrayList<File>();
		if (!file.isDirectory()) {
			//throw new IllegalArgumentException(file + "����һ����Ч��Ŀ¼");
			return resultFile;
		}
		if (suffixs == null) {
			throw new IllegalArgumentException("��ָ���ļ���׺");
		}
		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				for (int j = 0; j < suffixs.length; j++) {
					if (f.getPath().endsWith(suffixs[j])) {
						resultFile.add(f);
						break;
					}
				}
			}
		}
		return resultFile;
	}
	public static List<File> getFilesByFolder(String dir) {
		File file = new File(dir);
		List<File> resultFile = new ArrayList<File>();
		if (!file.isDirectory()) {
			//throw new IllegalArgumentException(file + "����һ����Ч��Ŀ¼");
			return resultFile;
		}
		
		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				resultFile.add(f);
			}
		}
		return resultFile;
	}
	public static List<File> getFoldersByFolder(String dir) {
		File file = new File(dir);
		List<File> resultFile = new ArrayList<File>();
		if (!file.isDirectory()) {
			//throw new IllegalArgumentException(file + "����һ����Ч��Ŀ¼");
			return resultFile;
		}
		
		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				resultFile.add(f);
			}
		}
		return resultFile;
	}
	public static String getUTF8Str(String str){
		if(!isValidString(str)){
			return "";
		}
		try {
			return new String(str.getBytes("iso-8859-1"),"utf-8");
		} catch (Exception e) {
			throw new RuntimeException("��֧�ֵ��ַ���?",e);
		}
	}
	public static String getCurrencyDateStr(){
		Date now=new Date();
		return formatter.format(now);
	}
	/**
	 * ���صĸ�ʽΪ yyyy-MM-dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static String getDateTime(long time){
		Date date=new Date(time);
		return dateTimeFormatter.format(date);
	}
	/**
	 * ����ĸ�ʽΪ yyyy-MM-dd HH:mm:ss
	 * @param dateStr
	 * @return
	 */
	public static Date getDateTime(String dateStr){
		try {
			return dateTimeFormatter.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException("ת��ʱ���쳣!");
		}
	}
	/**
	 * �õ�һ��ʱ�������е�����(�ַ�����ʽ yyyy-mm-dd)
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateStrList(String startTime,String endTime){
		return getDateStrList(startTime, endTime, formatter);
	}
	/**
	 * �õ�һ��ʱ�������е�����
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateStrList(String startTime,String endTime,SimpleDateFormat formatter){
		List<String> dateStrList=new ArrayList<String>();
		
		Calendar cal1=Calendar.getInstance();
		java.util.Date date1=java.sql.Date.valueOf(startTime);
		java.util.Date date2=java.sql.Date.valueOf(endTime);
		if(!date1.before(date2)&&!date1.equals(date2)){
			throw new RuntimeException("����ʱ�������ڿ�ʼʱ��!");
		}
		cal1.setTime(date1);
		long betweenDay=(date2.getTime()-date1.getTime())/1000/60/60/24;
		dateStrList.add(startTime);
		for(int i=0;i<betweenDay;i++){
			cal1.add(Calendar.DAY_OF_MONTH, 1);
			String dateStr=formatter.format(cal1.getTime());
			dateStrList.add(dateStr);
		}
		return dateStrList;
	}
	/**
	 * ���ص����ڸ�ʽΪyyyy-MM-dd
	 * @param time
	 * @return
	 */
	public static String getDateStr(long time){
		Date date=new Date(time);
		return formatter.format(date);
	}
	public static Connection getConn(String url,String user,String password){
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new RuntimeException("��ȡ���ݿ������쳣",e);
		}
	}
	public static List<Object> arrayToList(Object[] array){
		List<Object> list=new ArrayList<Object>();
		for(int i=0;i<array.length;i++){
			list.add(array[i]);
		}
		return list;
	}
	
	public static boolean isWindows(){
		return "\\".equals(File.separator);
	}
	public static Field[] getDeclaredFields(Class clazz){
		try {
			Field[] fields=clazz.getDeclaredFields();
			return fields;
		} catch (Exception e) {
			throw new RuntimeException("��ȡ��Ķ������Գ����쳣!");
		}
	}
	public static void setProperty(Object obj,String fieldName,Object value){
		try {
			Field field=obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException("ͨ�����������ֵʧ��",e);
		} 
	}
	public static Object invokeMethod(Object obj,String methodName,Class[] parameterTypes,Object[] args){
		try {
			Method method=obj.getClass().getDeclaredMethod(methodName, parameterTypes);
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException("ͨ����������÷���ʧ��",e);
		}
	}
	public static Object invokeMethod(Method method,Object obj,Object[] args){
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException("ͨ����������÷���ʧ��",e);
		} 
	}
	public static Object getProperty(Object obj,String fieldName){
		try {
			Class clazz=obj.getClass();
			Field field=clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new RuntimeException("ͨ���������ȡ����ֵʧ��",e);
		}
	}
}
