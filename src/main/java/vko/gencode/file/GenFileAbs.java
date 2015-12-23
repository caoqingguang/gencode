package vko.gencode.file;

import java.io.FileWriter;
import java.io.IOException;

import vko.gencode.mysql.Table;
import vko.gencode.util.NameUtil;


public abstract class GenFileAbs implements IGenFile{
	protected final Table table;
	protected final String namespace;
	protected final String outDir;
	protected final NameUtil nu;
	private StringBuilder sb;
	public GenFileAbs(Table table, String namespace, String outDir) {
		super();
		this.table = table;
		this.namespace = namespace;
		this.outDir = outDir;
		nu=NameUtil.newUtil(this.table);
		sb=new StringBuilder();
	}
	private String initTitle(){
		return "package "+this.namespace+";\n\n";
	}
	protected abstract String initBody();
	protected abstract String fileName();
	private void saveFile(){
		try {
			FileWriter fw=new FileWriter(this.outDir+fileName()+".java");
			fw.append(sb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void genFileTemplate() {
		sb.append(initTitle());
		sb.append(initBody());
		this.saveFile();
	}
	
	

}
