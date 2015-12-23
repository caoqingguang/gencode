package vko.gencode.file.impl;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Column;
import vko.gencode.mysql.Table;

public class EntityBaseFile extends GenFileAbs{

	public EntityBaseFile(Table table, String namespace, String outDir) {
		super(table, namespace, outDir);
	}

	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		sb.append("\n@SuppressWarnings(\"serial\")\n");
		sb.append("public class ").append(nu.getEntityBase());
		sb.append(" extends ").append("SuperBaseEntity");
		sb.append("{\n");
		for(Column col:this.table.getColList()){
			sb.append("\tprivate ").append(col.getJavaType().getSimpleName()).append(" ").append(col.getName2());
			sb.append(";\n");
		}
		sb.append("\n}");
		return sb.toString();
	}

	@Override
	protected String fileName() {
		return nu.getEntityBase();
	}

	

}
