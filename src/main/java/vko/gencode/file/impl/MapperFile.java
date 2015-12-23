package vko.gencode.file.impl;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Table;

public class MapperFile extends GenFileAbs{
	
	
	public MapperFile(Table table, String namespace, String outDir) {
		super(table,namespace,outDir);
	}
	
	@Override
	protected String fileName(){
		return nu.getIMapper();
	}


	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		sb.append("public interface ").append(nu.getIMapper());
		sb.append(" extends IBaseMapper<").append(nu.getEntity()).append(">").append("{");
		sb.append("\n\n\n\n");
		sb.append("}");
		return sb.toString();
	}

}
