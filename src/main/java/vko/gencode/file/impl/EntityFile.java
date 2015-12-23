package vko.gencode.file.impl;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Table;

public class EntityFile extends GenFileAbs{

	public EntityFile(Table table, String namespace, String outDir) {
		super(table, namespace, outDir);
	}

	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		sb.append("\n@SuppressWarnings(\"serial\")\n");
		sb.append("public class ").append(nu.getEntity());
		sb.append(" extends ").append(nu.getEntityBase());
		sb.append("{\n\n\n\n}");
		return sb.toString();
	}

	@Override
	protected String fileName() {
		return nu.getEntity();
	}

}
