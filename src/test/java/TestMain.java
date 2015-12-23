import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vko.gencode.mysql.Table;
import vko.gencode.util.FileHelper;
import vko.gencode.util.MysqlUtil;
import vko.gencode.util.FileHelper.FileEnum;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("../applicationContext.xml") 
public class TestMain {
	@Autowired
	private DataSource ds;
	@Test
	public void testTbName(){
		List<Table> list=null;
		try {
			list = MysqlUtil.getTbsInfo(ds);
			System.out.println(list);
			FileHelper fh=new FileHelper("d:/sssss/",list);
			fh.genXmlFile("mapping","dc.mapper", "dc.entity");
			fh.genJavaFile(FileEnum.Mapper, "mapper", "dc.mapper");
			fh.genJavaFile(FileEnum.Entity, "entity", "dc.entity");
			fh.genJavaFile(FileEnum.EntityBase, "entitybase", "dc.entity.base");
			fh.genJavaFile(FileEnum.Service, "service", "dc.service");
			fh.genJavaFile(FileEnum.ServiceImpl, "serviceimpl", "dc.service.impl");
			fh.genJavaFile(FileEnum.Controller, "controller", "dc.controller");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
