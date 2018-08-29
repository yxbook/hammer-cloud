package com.fmkj.common.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description: MB代码生成类
 **/
public class GeneratorUtil {
    // Service模板路径
    private static String service_vm = "/template/Service.vm";
    // ServiceImpl模板路径
    private static String serviceImpl_vm = "/template/ServiceImpl.vm";

    /**
     * 根据模板生成generatorConfig.xml文件
     *
     * @param jdbcDriver   驱动路径
     * @param jdbcUrl      链接
     * @param jdbcUsername 帐号
     * @param jdbcPassword 密码
     * @param module       项目模块
     * @param database     数据库
     * @param tabelName    表前缀
     * @param packageName  包名
     */
    public static void generator(
            String jdbcDriver,
            String jdbcUrl,
            String jdbcUsername,
            String jdbcPassword,
            String module,
            String database,
            String tabelName,
            String packageName,
            Map<String, String> lastInsertIdTables,
            String daoName,
            String serviceName) throws Exception {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            service_vm = GeneratorUtil.class.getResource(service_vm).getPath().replaceFirst("/", "");
            serviceImpl_vm = GeneratorUtil.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");
        } else {
            service_vm = GeneratorUtil.class.getResource(service_vm).getPath();
            serviceImpl_vm = GeneratorUtil.class.getResource(serviceImpl_vm).getPath();
        }

        String targetProject = module + "/" + daoName;
        String basePath = GeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "").replace(targetProject, "").replaceFirst("/", "");
        //String generatorConfigXml = MybatisGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "") + "/src/main/resources/generatorConfig.xml";
        targetProject = basePath + targetProject;
        String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "' AND table_name = '" + tabelName + "';";

        System.out.println("========== 开始生成generatorConfig.xml文件 ==========");
        List<Map<String, Object>> tables = new ArrayList<>();
        List<String> tablesName = new ArrayList<>();
        try {
            VelocityContext context = new VelocityContext();
            Map<String, Object> table;

            // 查询定制前缀项目的所有表
            JdbcUtil jdbcUtil = new JdbcUtil(jdbcDriver, jdbcUrl, jdbcUsername, "root");
            List<Map> result = jdbcUtil.selectByParams(sql, null);
            for (Map map : result) {
                System.out.println(map.get("TABLE_NAME"));
                table = new HashMap<>(2);
                table.put("table_name", map.get("TABLE_NAME"));
                tablesName.add((String) table.get("table_name"));
                table.put("model_name", StringUtils.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
                tables.add(table);
            }
            jdbcUtil.release();
            // 删除旧代码
            //deleteDir(new File(targetProject + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/domain"));
            //deleteDir(new File(targetProject + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/mapper"));
            //deleteDir(new File(targetProjectSqlMap + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/mapper"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("========== 结束生成generatorConfig.xml文件 ==========");

        System.out.println("========== 开始运行MybatisGenerator ==========");

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(targetProject + "/src/main/java/");
        gc.setFileOverride(true);
        gc.setOpen(false);
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("youxun");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName(jdbcDriver);
        dsc.setUsername(jdbcUsername);
        dsc.setPassword(AESUtil.aesDecode(jdbcPassword));
        dsc.setUrl(jdbcUrl);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { tablePrefix });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setInclude((String[]) tablesName.toArray(new String[0])); // 需要生成的表
        //strategy.setInclude(new String[] { "permission" }); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName + ".dao");
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);

        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
        System.out.println("========== 结束运行MybatisGenerator ==========");

        System.out.println("========== 开始生成Service ==========");
        String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());
        String servicePath = basePath + module + "/" + serviceName + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/server/service";
        String serviceImplPath = basePath + module + "/" + serviceName + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/server/service/impl";
        // 删除旧代码
        //deleteDir(new File(servicePath));
        //deleteDir(new File(serviceImplPath));
        for (int i = 0; i < tables.size(); i++) {
            String model = StringUtils.lineToHump(ObjectUtils.toString(tables.get(i).get("table_name")));
            String service = servicePath + "/" + model + "Service.java";
            //String serviceMock = servicePath + "/" + model + "ServiceMock.java";
            String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
            // 生成service
            File serviceFile = new File(service);
            if (!serviceFile.exists()) {
                VelocityContext context = new VelocityContext();
                context.put("package_name", packageName);
                context.put("model", model);
                context.put("ctime", ctime);
                VelocityUtil.generate(service_vm, service, context);
                System.out.println(service);
            }
            // 生成serviceImpl
            File serviceImplFile = new File(serviceImpl);
            if (!serviceImplFile.exists()) {
                VelocityContext context = new VelocityContext();
                context.put("package_name", packageName);
                context.put("model", model);
                context.put("mapper", StringUtils.toLowerCaseFirstOne(model));
                context.put("ctime", ctime);
                VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
                System.out.println(serviceImpl);
            }
        }
        System.out.println("========== 结束生成Service ==========");
    }

    // 递归删除非空文件夹
    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        dir.delete();
    }
}
