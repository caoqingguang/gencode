package vko.gencode.mysql;

public class Column {
	private String name;
	private Class<?> javaType;
	private String jdbcType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getJavaType() {
		return javaType;
	}
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public String getName2(){
		return this.getName();
	}
	@Override
	public String toString() {
		return "Column [name=" + name + ", javaType=" + javaType
				+ ", jdbcType=" + jdbcType + "]";
	}
	
}
