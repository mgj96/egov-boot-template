package egovframework.example.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;


@Slf4j
public class QuartzConfig {

    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Value("${globals.isQuartz}")
    private boolean isQaurtz = false;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) throws SchedulerException {

        if(!isQaurtz) return new SchedulerFactoryBean();

        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);

        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);

        //jobDetail
//        factory.setJobDetails(..., ...);
        //trigger
//        factory.setTriggers(..., ...);

        factory.setDataSource(dataSource);

        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.scheduler.instanceName", "scheduler");
        quartzProperties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        quartzProperties.setProperty("org.quartz.scheduler.isClustered", "true");
        quartzProperties.setProperty("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");

        // Configuration of ThreadPool
        quartzProperties.setProperty("org.quartz.threadPool.threadCount", "5");
        quartzProperties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        // JMX 설정 -(Java Management Extensions) Java 응용 프로그램의 모니터링과 관리기능
//        quartzProperties.setProperty("org.quartz.scheduler.jmx.export", "true");
//        quartzProperties.setProperty("org.quartz.scheduler.jmx.objectName", "quartz:type=QuartzScheduler,name=MultiViewQuartzScheduler");
        // Configure Clustered
        quartzProperties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        quartzProperties.setProperty("org.quartz.jobStore.isClustered", "true");
        quartzProperties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");
        // Configure JDBC
        quartzProperties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        quartzProperties.setProperty("org.quartz.jobStore.useProperties", "false");
        quartzProperties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");

        factory.setQuartzProperties(quartzProperties);

        return factory;
    }
}
