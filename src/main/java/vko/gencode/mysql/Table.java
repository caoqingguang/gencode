package vko.gencode.mysql;

import java.util.List;

public class Table {
	private final String name;
	private final List<Column> colList;
	public Table(String name, List<Column> colList) {
		super();
		this.name = name;
		this.colList = colList;
	}
	public String getName() {
		return name;
	}
	public String getName2(){
		String[] strArr=this.name.split("_");
		StringBuilder sb=new StringBuilder();
		for(String str:strArr){
			sb.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
		}
		return sb.toString();
	}
	public List<Column> getColList() {
		return colList;
	}
	
	@Override
	public String toString() {
		return "Table [name=" + name + ", colList=" + colList + ", getName2()="
				+ getName2() + "]";
	}

	
	
}
