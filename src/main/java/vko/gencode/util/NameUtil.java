package vko.gencode.util;

import vko.gencode.mysql.Table;

public class NameUtil {
	private Table table;
	private NameUtil(Table table){
		this.table=table;
	}
	
	public static NameUtil newUtil(Table table){
		return new NameUtil(table);
	}
	
	public String getEntity(){
		return this.table.getName2();
	}
	public String getEntityBase(){
		return this.table.getName2()+"Base";
	}
	public String getIMapper(){
		return "I"+this.table.getName2()+"Mapper";
	}
	public String getMapping(){
		return this.table.getName2()+"Mapper";
	}
	public String getIService(){
		return "I"+this.table.getName2()+"Service";
	}
	public String getIserviceRemote(){
		return "I"+this.table.getName2()+"RemoteService";
	}
	public String getServiceImpl(){
		return this.table.getName2()+"ServiceImpl";
	}
	public String getController(){
		return this.table.getName2()+"Controller";
	}

}
