package vko.gencode.file.impl;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Table;

public class ServiceImplFile extends GenFileAbs{

	public ServiceImplFile(Table table, String namespace, String outDir) {
		super(table, namespace, outDir);
	}

	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		sb.append("\n@Component\n");
		sb.append("public class ").append(nu.getServiceImpl());
		sb.append(" extends BaseServiceImpl<").append(nu.getEntity()).append(">");
		sb.append(" implements ").append(nu.getIService());
		sb.append("{\n\n");
		sb.append("\n\t@Autowired");
		sb.append("\n\tprivate ").append(nu.getIMapper()).append(" mapper;");
		sb.append("\n\n\t@Override");
		sb.append("\n\tprotected IBaseMapper<").append(nu.getEntity()).append("> getMapper() {");
		sb.append("\n\t\treturn this.mapper;");
		sb.append("\n\t}");
		sb.append("\n\n}");
		return sb.toString();
	}

	@Override
	protected String fileName() {
		return nu.getServiceImpl();
	}

}
