package ru.crg.reglaments.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.crg.reglaments.entity.ReglamentLink;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<ReglamentLink> reader() {
        return new JdbcCursorItemReaderBuilder<ReglamentLink>()
                .dataSource(dataSource)
                .name("reglamentLinkJDBC_Reader")
                .sql("select reglament from fiz_test")
                .rowMapper(new ReglamentLinkRowMapper())
                .build();
    }

    @Bean
    public ReglamentLinkProcessor handler() {
        return new ReglamentLinkProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<ReglamentLink> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ReglamentLink>()
                .itemPreparedStatementSetter((rLink, ps) -> {
                    ps.setString(1, rLink.getUrl().toString());
                    ps.setString(2, rLink.getText());
                })
                .sql("INSERT INTO reglaments_result (url, text) VALUES (?, ?)")
                .dataSource(dataSource)
                .build();
    }



    @Bean
    public Job checkReglamentJob(JobCompletionNotificationListener listener, Step firstStep) {
        return jobBuilderFactory.get("checkReglamentsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(firstStep)
                .end()
                .build();
    }

    @Bean
    public Step firstStep(JdbcBatchItemWriter<ReglamentLink> writer) {
        return stepBuilderFactory.get("step1")
                .<ReglamentLink, ReglamentLink> chunk(100)
                .reader(reader())
                .processor(handler())
                .writer(writer)
                .build();
    }

}
