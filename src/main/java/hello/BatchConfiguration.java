package hello;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<TimeSeries> reader() {
        FlatFileItemReader<TimeSeries> reader = new FlatFileItemReader<TimeSeries>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<TimeSeries>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name", "date", "multiplier" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<TimeSeries>() {{
                setTargetType(TimeSeries.class);
            }});
        }});
        return reader;
    }

    @Bean
    public TimeSeriesItemProcessor splitData() {
        return new TimeSeriesItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<TimeSeries> writer() {
        JdbcBatchItemWriter<TimeSeries> writer = new JdbcBatchItemWriter<TimeSeries>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<TimeSeries>());
        writer.setSql("INSERT INTO INSTRUMENT_PRICE_MODIFIER (name, multiplier) VALUES (:name, :multiplier)");
        writer.setDataSource(dataSource);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job calculateInstrument1(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("calculateInstrument1")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(instrument1())
                .end()
                .build();
    }
    
    @Bean
    public Job calculateInstrument2(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJobSecond")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(instrument2())
                .end()
                .build();
    }

    @Bean
    public Step instrument1() {
        return stepBuilderFactory.get("instrument1")
                .<TimeSeries, TimeSeries> chunk(10)
                .reader(reader())
                .processor(splitData())
               // .writer(writer())
                .build();
    }
    
    @Bean
    public Step instrument2() {
        return stepBuilderFactory.get("instrument2")
                .<TimeSeries, TimeSeries> chunk(10)
                .reader(reader())
                .processor(splitData())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
