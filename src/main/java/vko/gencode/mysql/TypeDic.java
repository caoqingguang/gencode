package vko.gencode.mysql;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeDic {
	private static Map<String,Class<?>> javaMap;
	private static Map<String,String> jdbcMap;
	
	static {
		javaMap=new HashMap<String,Class<?>>();
		jdbcMap=new HashMap<String,String>();
		addRelation("int",Integer.class,"INTEGER");
		addRelation("bigint",Long.class,"BIGINT");
		addRelation("varchar",String.class,"VARCHAR");
		addRelation("date",Date.class,"DATE");	
	}
	
	private static void addRelation(String type,Class<?> javaType,String jdbcType){
		type=typeMerge(type);
		javaMap.put(type, javaType);
		jdbcMap.put(type, jdbcType);
	}
	
	private static String typeMerge(String type){
		int index=type.indexOf('(');
		if(index>0){
			return type.substring(0, index);
		}
		return type;
	}
	public static Class<?> getJavaType(String type){
		type=typeMerge(type);
		return javaMap.get(type);
	}
	public static String getJdbcType(String type){
		type=typeMerge(type);
		return jdbcMap.get(type);
	}
	
}
