package com.model2.mvc.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {
	
	private static RequestMapping requestMapping;
	private Map<String, Action> map;
	private Properties properties;
	
	private RequestMapping(String resources) { //properties�� �ִ� ��
		map = new HashMap<String, Action>();
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);
			//in = new FileinputStream(sources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties ���� �ε� ���� :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	public synchronized static RequestMapping getInstance(String resources){ //Mapping �� �ϳ��� ������ �ȴ�~
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	public Action getAction(String path){ //properties �����´�!!
		Action action = map.get(path);
		if(action == null){ // ������ ����� 
			String className = properties.getProperty(path);
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim(); //trim ����� ����
			try{
				Class c = Class.forName(className); //��Ʈ������ �Ǿ��ִ°��� �ν��Ͻ� ����, jdbc������ ����̹� �Ŵ����� ��� 
				Object obj = c.newInstance(); //�޾Ƽ� ����
				if(obj instanceof Action){ //action ���� ��ȿ�� üũ
					map.put(path, (Action)obj); 
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class����ȯ�� ���� �߻�  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action������ ���ϴ� ���� ���� �߻� : " + ex);
			}
		}//������ �����´�
		return action;
	}
}