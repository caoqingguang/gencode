package vko.gencode.file.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import vko.gencode.file.GenFileAbs;
import vko.gencode.mysql.Table;

public class ControllerFile extends GenFileAbs{
	
	private String cname;
	private String sname;
	private String ename;
	public ControllerFile(Table table, String namespace, String outDir) {
		super(table, namespace, outDir);
		cname=this.getStrFirstLower(nu.getEntity());
		sname=this.getStrFirstLower(nu.getIService().substring(1));
		ename=this.getStrFirstLower(nu.getEntity());
	}
	
	
	private String readModel(){
		StringBuilder sb=new StringBuilder();
		try {
			String file=ControllerFile.class.getResource("/controller-model.txt").getFile();
			 InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式
			 BufferedReader bufferedReader = new BufferedReader(read);
			 String lineTxt = null;
			 while((lineTxt = bufferedReader.readLine()) != null){
			     sb.append(lineTxt).append("\n");
			 }
			 read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	@Override
	protected String initBody() {
		StringBuilder sb=new StringBuilder();
		String model=readModel();
		model=model.replace("cname", this.cname);
		model=model.replaceAll("ename", this.ename);
		model=model.replace("sname", this.sname);
		model=model.replace("cclass", nu.getController());
		model=model.replace("sclass", nu.getIService());
		model=model.replace("eclass", nu.getEntity());
		sb.append(model);
		return sb.toString();
	}

	@Override
	protected String fileName() {
		return nu.getController();
	}
	
	private String getStrFirstLower(String str){
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}
	

}
