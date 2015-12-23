package vko.gencode.file.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import vko.gencode.file.IGenFile;
import vko.gencode.mysql.Column;
import vko.gencode.mysql.Table;
import vko.gencode.mysql.TypeDic;



public class MappingFile implements IGenFile{
	private final static String BASECOL_ID="BASECOL";
	private final static String BASEMAP_ID="BASEMAP";
	private final static String INSERTObj_ID="insertObj";
	private final static String UPDATEObjById_ID="updateObjById";
	private final static String DELETEObjById_ID="deleteObjById";
	private final static String SELECTObjById_ID="selectObjById";
	private final static String SELECTObjList_ID="selectObjList";
	
	private final Table table;
	private final String namespace;
	private final String entity;
	private final String outDir;
	private Document doc;
	private Element root;
	public MappingFile(Table table,String ns,String entity,String outDir){
		this.table=table;
		this.namespace=ns;
		this.entity=entity;
		this.outDir=outDir;
		doc = new Document();
		DocType docType=new DocType("mapper","-//mybatis.org//DTD Mapper 3.0//EN","http://mybatis.org/dtd/mybatis-3-mapper.dtd");
		doc.addContent(docType);
	}
	

	
	
	private void initRoot(){
		root=new Element("mapper");
		root.setAttribute("namespace", this.namespace);
		doc.setRootElement(root);
	}
	
	private Content initBaseCol(){
		Element basecol=new Element("sql");
		basecol.setAttribute("id",BASECOL_ID);
		StringBuilder sb=new StringBuilder();
		for(Column col:this.table.getColList()){
			sb.append(col.getName()).append(",");
		}
		String bc=sb.toString();
		int end=bc.lastIndexOf(',');
		Text txt=new Text(bc.substring(0,end));
		basecol.addContent(txt);
		return basecol;
	}
	
	private Content initBaseMap(){
		Element basemap=new Element("resultMap");
		basemap.setAttribute("id", BASEMAP_ID);
		basemap.setAttribute("type", this.entity);
		for(Column col:this.table.getColList()){
			Element coltmp=null;
			if("id".equals(col.getName())){
				coltmp=new Element("id");
			} else {
				coltmp=new Element("result");
			}
			coltmp.setAttribute("column", col.getName());
			coltmp.setAttribute("property", col.getName2());
			coltmp.setAttribute("jdbcType", col.getJdbcType());
			basemap.addContent(coltmp);
		}
		return basemap;
	}
	
	private Content initInsertObj(){
		Element insert=new Element("insert");
		insert.setAttribute("id", INSERTObj_ID);
		Text txt=new Text("insert into "+this.table.getName());
		insert.addContent(txt);
		Element trim=new Element("trim");
		trim.setAttribute("prefix", "(");
		trim.setAttribute("suffix", ")");
		trim.setAttribute("suffixOverrides", ",");
		for(Column col:this.table.getColList()){
			if("id".equals(col.getName2())){
				Text id=new Text("id,");
				trim.addContent(id);
				continue;
			}
			Element other=new Element("if");
			other.setAttribute("test", col.getName2() + " !=null");
			other.addContent(col.getName()+",");
			trim.addContent(other);
		}
		insert.addContent(trim);
		Element values=new Element("trim");
		values.setAttribute("prefix", "values (");
		values.setAttribute("suffix", ")");
		values.setAttribute("suffixOverrides", ",");
		for(Column col:this.table.getColList()){
			if("id".equals(col.getName2())){
				Text id=new Text("#{id,jdbcType="+col.getJdbcType()+"},");
				values.addContent(id);
				continue;
			}
			Element other=new Element("if");
			other.setAttribute("test", col.getName2() + " !=null");
			other.addContent("#{"+col.getName()+",jdbcType="+col.getJdbcType()+"},");
			values.addContent(other);
		}
		insert.addContent(values);
		
		
		return insert;
		
	}
/*	<update id="updateByPrimaryKeySelective" parameterType="cn.vko.cms.model.ClassRoom" >
    update classroom
    <set >
      <if test="name != null" >
        name = #{name,jdbcType=VARCHAR},
      </if>
      <if test="status != null" >
        status = #{status,jdbcType=TINYINT},
      </if>
    </set>
    where id = #{id,jdbcType=BIGINT}
  </update>*/
	private Content initUpdateObjById(){
		Element update=new Element("update");
		update.setAttribute("id",UPDATEObjById_ID);
		update.setAttribute("parameterType", this.entity);
		Text txt=new Text("update "+this.table.getName());
		update.addContent(txt);
		Element set=new Element("set");
		for(Column col:this.table.getColList()){
			if(!"id".equals(col.getName())){
				Element tmp=new Element("if");
				tmp.setAttribute("test", col.getName2()+ " !=null");
				tmp.addContent(col.getName()+"=#{"+col.getName2()+",jdbcType="+col.getJdbcType()+"},");
				set.addContent(tmp);
			}
		}
		update.addContent(set);
		Text txt2=new Text("where id=#{id,jdbcType=BIGINT}");
		update.addContent(txt2);
		return update;
	}
	
	
	private Content initDeleteObjById(){
		Element delete=new Element("delete");
		delete.setAttribute("id", DELETEObjById_ID);
		Text txt=new Text("delete from "+ this.table.getName() + " where id=#{id,jdbcType=BIGINT}");
		delete.addContent(txt);
		return delete;
	}
/*	 <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Long" >
	    select 
	    <include refid="Base_Column_List" />
	    from classroom
	    where id = #{id,jdbcType=BIGINT}
	  </select>*/
	private Content initSelectObjById(){
		Element select=new Element("select");
		select.setAttribute("id", SELECTObjById_ID);
		select.setAttribute("resultMap",BASEMAP_ID);
		select.setAttribute("parameterType", "long");
		select.addContent("select");
		Element ref=new Element("include");
		ref.setAttribute("refid",BASECOL_ID);
		select.addContent(ref);
		select.addContent("from "+ this.table.getName());
		select.addContent("  where id=#{id,jdbcType=BIGINT}");
		return select;
	}
	
	private Content initSelectObjList(){
		Element selectList=new Element("select");
		selectList.setAttribute("id", SELECTObjList_ID);
		selectList.setAttribute("resultMap",BASEMAP_ID);
		selectList.setAttribute("parameterType", "long");
		selectList.addContent("select");
		Element ref=new Element("include");
		ref.setAttribute("refid",BASECOL_ID);
		selectList.addContent(ref);
		selectList.addContent("from "+ this.table.getName());
		selectList.addContent("  where 1=1 ");
		Element obj=new Element("if");
		obj.setAttribute("test", "obj !=null");
		for(Column col:this.table.getColList()){
			Element tmp=new Element("if");
			tmp.setAttribute("test", "obj."+col.getName2() + " !=null");
			tmp.addContent("and " +col.getName() +" = #{obj."+col.getName2()+",jdbcType="+col.getJdbcType()+"}");
			obj.addContent(tmp);
		}
		selectList.addContent(obj);
		Element other=new Element("if");
		other.setAttribute("test", "other !=null");
		Comment otherinfo=new Comment("其它查询条件");
		other.addContent(otherinfo);
		selectList.addContent(other);
		return selectList;
	}
	private void saveFile(){
		Format format = Format.getPrettyFormat();
		format.setEncoding("utf-8");//设置编码
		format.setIndent("    ");//设置缩进
		XMLOutputter xmlOut=new XMLOutputter(format);
		try {
			FileWriter fileOut=new FileWriter(this.outDir+this.table.getName2()+"Mapper.xml");
			xmlOut.output(doc, fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void genFileTemplate() {
		
		this.initRoot();
		
		Content basecol=this.initBaseCol();
		this.root.addContent(basecol);
		
		Content basemap=this.initBaseMap();
		this.root.addContent(basemap);
		
		Content insert=this.initInsertObj();
		this.root.addContent(insert);
		
		Content update=this.initUpdateObjById();
		this.root.addContent(update);
		
		Content delete=this.initDeleteObjById();
		this.root.addContent(delete);
		
		Content select=this.initSelectObjById();
		this.root.addContent(select);
		
		Content selectList=this.initSelectObjList();
		this.root.addContent(selectList);
		saveFile();
	}
	
	
	
	
	
	public static void main(String[] args) {
		Table table=genTable();
		MappingFile mapper=new MappingFile(table, "cn.vko.TeacherMapper", "cn.entity.Teacher", "d:/");
		mapper.genFileTemplate();
	}
	public static Table genTable(){
		List<Column> list=new ArrayList<Column>();
		Column id=new Column();
		id.setName("id");
		id.setJavaType(TypeDic.getJavaType("bigint"));
		id.setJdbcType(TypeDic.getJdbcType("bigint"));
		list.add(id);
		Column name=new Column();
		name.setName("name");
		name.setJavaType(TypeDic.getJavaType("varchar"));
		name.setJdbcType(TypeDic.getJdbcType("varchar"));
		list.add(name);
		Column email=new Column();
		email.setName("email");
		email.setJavaType(TypeDic.getJavaType("varchar"));
		email.setJdbcType(TypeDic.getJdbcType("varchar"));
		list.add(email);
		return new Table("test_abc",list);
	}
	
}
