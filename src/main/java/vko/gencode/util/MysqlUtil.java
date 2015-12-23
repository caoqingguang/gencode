package vko.gencode.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import vko.gencode.mysql.Column;
import vko.gencode.mysql.Table;
import vko.gencode.mysql.TypeDic;


public class MysqlUtil {

	public static Set<String> getTbsName(DataSource ds){
		Set<String> tbs=new HashSet<String>();
		try {
			Connection con=ds.getConnection();
			Statement stat=con.createStatement();
			ResultSet rs=stat.executeQuery("show tables");
			while(rs.next()){
				String str=rs.getString(1);
				tbs.add(str);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tbs;
	}
	
	public static Table getTbInfo(DataSource ds,String tb) throws SQLException{
		Connection con=ds.getConnection();
		Statement stat=con.createStatement();
		ResultSet rs=stat.executeQuery("show columns from "+tb);
		List<Column> colList=new ArrayList<Column>();
		while(rs.next()){
			String colName=rs.getString(1);
			String colType=rs.getString(2);
			Column col=new Column();
			col.setName(colName);
			Class<?> javaType=TypeDic.getJavaType(colType);
			String jdbcType=TypeDic.getJdbcType(colType);
			if(javaType==null||jdbcType==null){
				throw new RuntimeException("类型不存在对应关系"+colType);
			}
			col.setJavaType(javaType);
			col.setJdbcType(jdbcType);
			colList.add(col);
		}
		return new Table(tb,colList);
	}
	
	public static List<Table> getTbsInfo(DataSource ds) throws SQLException{
		List<Table> tableList=new ArrayList<Table>();
		for(String tbname:getTbsName(ds)){
			tableList.add(getTbInfo(ds,tbname));
		}
		return tableList;
	}
}
