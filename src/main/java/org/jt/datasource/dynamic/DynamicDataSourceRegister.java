package org.jt.datasource.dynamic;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


public class DynamicDataSourceRegister implements
		ImportBeanDefinitionRegistrar, EnvironmentAware {
	
    //如配置文件中未指定数据源类型，使用该默认值

    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    private ConversionService conversionService = new DefaultConversionService();
    private PropertyValues dataSourcePropertyValues;

    // 默认数据源

    private DataSource defaultDataSource;

    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

    @Override

    public void setEnvironment(Environment environment) {
       System.out.println("DynamicDataSourceRegister.setEnvironment()");
       initDefaultDataSource(environment);
      initCustomDataSources(environment);
      // initCustomeDS(defaultDataSource,environment);
    }

    private void initCustomeDS(DataSource dataSource,Environment env){
    	/*JdbcTemplate template = new JdbcTemplate(dataSource);
    	String sql="select a.source_code,a.source_name,b.id,b.host,b.post,b.username,b.password,b.database_name,"
    	          +"b.driver_class,c.source_type_name,d.source_style_name from t_core_source a,t_core_source_db b,"
    			  +"t_core_source_type c,t_core_source_interact_style d "
    	          +" where a.id=b.id and a.source_type_id=c.id and a.source_style_id=d.id "
    	          +" and a.is_delete=b.is_delete and a.is_delete=c.is_delete and a.is_delete=d.is_delete"
    	          +" and a.is_delete="+GlobalVariable.no_delete;
    	List list=template.queryForList(sql);
    	for(int i=0;i<list.size();i++){
    		Map map=(Map) list.get(i);
    		String dsPrefix = "ds"+map.get("source_code");
    		String driverClassName = ""+map.get("driver_class");
    		String url  = "jdbc:"+map.get("datasource_type_code")+"://"+map.get("host")+":"+map.get("post")+"/"+map.get("database_name");
    		String username  = ""+map.get("user");
    		String password  = ""+map.get("password");
    		Map<String, Object> dsMap =new HashMap();
    		dsMap.put("driverClassName", driverClassName);
    		dsMap.put("url", url);
    		dsMap.put("username", username);
    		dsMap.put("password", password);
    		DataSource ds = buildDataSource(dsMap);
            dataBinder(ds, env);
            customDataSources.put(dsPrefix, ds);
    	}*/
    }
   
    private void initDefaultDataSource(Environment env){
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
		Map<String, Object> dsMap = new HashMap<String, Object>();
		dsMap.put("type", propertyResolver.getProperty("type"));
		dsMap.put("driverClassName", propertyResolver.getProperty("driverClassName"));
		dsMap.put("url", propertyResolver.getProperty("url"));
		dsMap.put("username", propertyResolver.getProperty("username"));
		dsMap.put("password", propertyResolver.getProperty("password"));
        //创建数据源;

        defaultDataSource = buildDataSource(dsMap);
        dataBinder(defaultDataSource, env);

    }


    private void initCustomDataSources(Environment env) {

        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源

        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "custom.datasource.");
        String dsPrefixs = propertyResolver.getProperty("names");
        if(dsPrefixs!=null){
	        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
	            Map<String, Object> dsMap = propertyResolver.getSubProperties(dsPrefix + ".");
	            DataSource ds = buildDataSource(dsMap);
	            dataBinder(ds, env);
	            customDataSources.put(dsPrefix, ds);
	        }
        }
        /*
    	List list=dynamicDataDao.getSources();
    	for(int i=0;i<list.size();i++){
    		Map map=(Map) list.get(i);
    		String dsPrefix = "ds"+map.get("source_id");
    		String driverClassName = ""+map.get("driver_class");
    		String url  = "jdbc:"+map.get("datasource_type_code")+":"+map.get("host")+"/"+map.get("post")+"/"+map.get("database_name");
    		String username  = ""+map.get("user");
    		String password  = ""+map.get("password");
    		Map<String, Object> dsMap =new HashMap();
    		dsMap.put("driverClassName", driverClassName);
    		dsMap.put("url", url);
    		dsMap.put("username", username);
    		dsMap.put("password", password);
    		DataSource ds = buildDataSource(dsMap);
            dataBinder(ds, env);
            customDataSources.put(dsPrefix, ds);
    	}
        */
    }

   

    /**

     * 创建datasource.

     * @param dsMap

     * @return

     */

    @SuppressWarnings("unchecked")
    public DataSource buildDataSource(Map<String, Object> dsMap) {

       Object type = dsMap.get("type");
        if (type == null){
            type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
        }
        Class<? extends DataSource> dataSourceType;
       try {
           dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
           String driverClassName = dsMap.get("driverClassName").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username")!=null?dsMap.get("username").toString():null;
            String password= dsMap.get("password")!=null?dsMap.get("password").toString():null;
            DataSourceBuilder factory = null;
            if(username!=null && password!=null){
                factory =   DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            }else if(username!=null){
            	factory =   DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).type(dataSourceType);
            }else{
                factory =   DataSourceBuilder.create().driverClassName(driverClassName).url(url).type(dataSourceType);
            }
            return factory.build();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }

       return null;

    }

   

    /**

     * 为DataSource绑定更多数据

     * @param dataSource

     * @param env

     */

    private void dataBinder(DataSource dataSource, Environment env){

       RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
       dataBinder.setConversionService(conversionService);
       dataBinder.setIgnoreNestedProperties(false);//false
       dataBinder.setIgnoreInvalidFields(false);//false
       dataBinder.setIgnoreUnknownFields(true);//true
        if(dataSourcePropertyValues == null){
            Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
            Map<String, Object> values = new HashMap<>(rpr);
            // 排除已经设置的属性
            values.remove("type");
            values.remove("driverClassName");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);

    }

   

 

    @Override

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

       System.out.println("DynamicDataSourceRegister.registerBeanDefinitions()");
       Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
       // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //添加属性：AbstractRoutingDataSource.defaultTargetDataSource
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

    }

}
