package io.mosip.admin.config;

import io.mosip.admin.bulkdataupload.batch.JobResultListener;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import org.digibooster.spring.batch.listener.JobExecutionListenerContextSupport;
import org.digibooster.spring.batch.security.listener.JobExecutionSecurityContextListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Spring batch configuration
 * 
 * @author dhanendra
 *
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	ApplicationContext applicationContext;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuditUtil auditUtil;

    @Bean
    public JobResultListener jobResultListener() {
        return new JobResultListener(dataSource, auditUtil, new JobExecutionSecurityContextListener());
    }

    @Bean(name = "customStepBuilderFactory")
    public StepBuilderFactory customStepBuilderFactory() {
        return  new StepBuilderFactory(jobRepository, platformTransactionManager);
    }


    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

}
