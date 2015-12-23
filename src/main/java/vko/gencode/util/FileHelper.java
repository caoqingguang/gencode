package vko.gencode.util;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vko.gencode.file.IGenFile;
import vko.gencode.file.impl.ControllerFile;
import vko.gencode.file.impl.EntityBaseFile;
import vko.gencode.file.impl.EntityFile;
import vko.gencode.file.impl.MapperFile;
import vko.gencode.file.impl.MappingFile;
import vko.gencode.file.impl.ServiceFile;
import vko.gencode.file.impl.ServiceImplFile;
import vko.gencode.mysql.Table;


public class FileHelper {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private final String baseDir;
	private final List<Table> tables;
	public FileHelper(String baseDir,List<Table> tables){
		if(!baseDir.endsWith("/")){
			baseDir+="/";
		}
		this.baseDir=baseDir;
		this.tables=tables;
		this.logger.debug("完成文件构造初始化,跟路径:"+baseDir+",表数量:"+tables.size());
	}
	public void genMapping(String path,String namespace){
		String path2=getPath("mapping");
		this.logger.debug("开始构建mapping文件,存放位置:"+path2);
		genPath(path2);
		for(Table table:tables){
			System.out.println(table);
			MappingFile mf=new MappingFile(table, namespace+".I"+table.getName2()+"Mapper", "cn.vko.entity."+table.getName2(), path2);
			mf.genFileTemplate();
		}
	}
	public void genXmlFile(String path,String namespace,String entityNamesapce){
		FileEnum fileType=FileEnum.Mapping;
		String path2=getPath("mapping");
		this.logger.debug("开始构建"+fileType.getInfo()+"文件,存放位置:"+path2);
		genPath(path2);
		for(Table table:tables){
			System.out.println(table);
			NameUtil nu=NameUtil.newUtil(table);
			String ns1=namespace+"."+nu.getIMapper();
			String ns2=entityNamesapce+"."+nu.getEntity();
			IGenFile mf=this.getIGenFile(fileType, table, ns1, path2, ns2);
			mf.genFileTemplate();
		}
	}
	public void genJavaFile(FileEnum fileType,String path,String namespace){
		String path2=getPath(path);
		this.logger.debug("开始构建"+fileType.getInfo()+"文件,存放位置:"+path2);
		genPath(path2);
		for(Table table:tables){
			IGenFile mf=this.getIGenFile(fileType, table, namespace, path2, null);
			mf.genFileTemplate();
		}
	}
	
	private IGenFile getIGenFile(FileEnum type,Table table,String namespace,String path,String entity){
		switch (type) {
		case Entity:
			return new EntityFile(table, namespace, path);
		case EntityBase:
			return new EntityBaseFile(table, namespace, path);
		case Mapper:
			return new MapperFile(table, namespace, path);
		case Mapping:
			return new MappingFile(table, namespace, entity, path);
		case Service:
			return new ServiceFile(table, namespace, path);
		case ServiceImpl:
			return new ServiceImplFile(table, namespace, path);
		case Controller:
			return new ControllerFile(table, namespace, path);
		default:
			return null;
		}
	}
	
	private String getPath(String path){
		if(!path.endsWith("/")){
			path+="/";
		}
		return this.baseDir+path;
	}
	private void genPath(String path){
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public static enum FileEnum{
		Entity("entity"),
		EntityBase("entity base"),
		Mapper("mapper"),
		Mapping("mapping xml"),
		Service("service "),
		ServiceImpl("service impl"),
		Controller("controller ");
		
		private final String info;

		private FileEnum(String info) {
			this.info = info;
		}

		public String getInfo() {
			return info;
		}
		
	}
}
