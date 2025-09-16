package springboot.configurations;

import java.sql.Connection;
import java.util.Properties;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ValidationMode;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/* https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa */

@Configuration
@EnableJpaRepositories(basePackages = "springboot.repositories")
@EnableTransactionManagement
public class PersistenceJpaConfig
{
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
	    LocalContainerEntityManagerFactoryBean emfb 
	        = new LocalContainerEntityManagerFactoryBean();
	    emfb.setDataSource(dataSource());
	    String[] scanPackages = {"springboot.dto.response", "springboot.entities"};
	    emfb.setPackagesToScan(scanPackages);
	 
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setShowSql(true);
	    vendorAdapter.setGenerateDdl(true);
	    vendorAdapter.setPrepareConnection(true); // hibernate 5.1 or 5.2
	    emfb.setJpaProperties(additionalProperties());
	    emfb.setJpaVendorAdapter(vendorAdapter);
	    emfb.setValidationMode(ValidationMode.NONE); // Needed for H2 to work in Docker
	    emfb.setPersistenceUnitName("RegistrationMicroServicePU");
	    
	    return emfb;
	}	
	
	@Bean(name="pooledDataSource")
	public DataSource dataSource(){
 //   	String appName = ConfigServerReader.getAppName();
    	
		DataSource aDataSource = buildPooledDataSource();
//		System.out.println("in PersistenceJpaConfig AppName: " + appName + " env is: " + env.getActiveProfiles()[0]);
    	
	    return aDataSource;
	}

	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
	{
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	    
	    System.out.println(" in transactionManager Object is: " + emf.toString());
	 
	    return transactionManager;
	}
	
	@Bean	
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	private Properties additionalProperties()
	{
	    Properties properties = new Properties();
//	    properties.setProperty("hibernate.ddl-auto", "none");
//	    properties.setProperty("hibernate.ddl-auto", "create-drop");
	    properties.setProperty("hibernate.ddl-auto", "update");
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	    properties.setProperty("hibernate.current_session_context_class", "thread");
	    properties.setProperty("hibernate.format_sql", "true");
	    properties.setProperty("hibernate.show_sql", "true");
	    properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
	    properties.setProperty("hibernate.connection.release_mode", "after_transaction");
	    
	        
	    return properties;
	}
	
	public DataSource buildPooledDataSource()
	{
//		String envName =  anEnvironment.getActiveProfiles()[0].toUpperCase();
//		String connectString = getConfigSettingNoCache(appName, "DBCONN", anEnvironment);
//		String userName = getConfigSettingNoCache(appName, "DBCONN-UserName", anEnvironment);
//		String password = getConfigSettingNoCache(appName, "DBCONN-Password", anEnvironment);
//		String driverClass = getConfigSettingNoCache(appName, "DBCONN-DriverClass", anEnvironment);
		
		/* cheating for now in memory database */
		
		String connectString = "jdbc:h2:mem:testdb";
		String userName = "SA"; // also try SA, with no password, initial h2
		String password = ""; // initial h2
		String driverClass = "org.h2.Driver";
		
		// https://tomcat.apache.org/tomcat-9.0-doc/jdbc-pool.html#Plain_Ol'_Java

		PoolProperties p = new PoolProperties();
		
		p.setDefaultAutoCommit(false);
		p.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
        p.setUrl(connectString);
        p.setUsername(userName);
        p.setPassword(password);
        p.setDriverClassName(driverClass);
        
        p.setTestWhileIdle(false);
        p.setValidationQuery("SELECT 1");
        
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(20);
        p.setMaxIdle(20);
        p.setInitialSize(5);
        p.setMaxWait(30000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(1);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
		 
		DataSource aDataSource = null;
		
		try {
			aDataSource = new DataSource(p);
		}
		catch (Exception e)
		{
			System.out.println("Create Pool Failed!!!");
			aDataSource = null;
		}
		
		return aDataSource;
		
	}
	
}
