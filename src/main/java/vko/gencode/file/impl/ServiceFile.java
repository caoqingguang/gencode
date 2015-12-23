package vko.gencode.file.impl;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Table;

public class ServiceFile extends GenFileAbs{

	
	public ServiceFile(Table table, String namespace, String outDir) {
		super(table, namespace, outDir);
	}

	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		sb.append("public interface ").append(nu.getIService());
		sb.append(" extends IBaseService<").append(nu.getEntity()).append(">");
		sb.append("{\n\n\n\n").append("}");
		return sb.toString();
	}

	@Override
	protected String fileName() {
		return nu.getIService();
	}

}
