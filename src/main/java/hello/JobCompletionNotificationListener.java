package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			log.info("Job process time: " + jobExecution.getEndTime());

			List<TimeSeries> results = jdbcTemplate.query("SELECT name, multiplier FROM INSTRUMENT_PRICE_MODIFIER", new RowMapper<TimeSeries>() {
				@Override
				public TimeSeries mapRow(ResultSet rs, int row) throws SQLException {
					return new TimeSeries(rs.getString(1), rs.getString(2), "");
				}
			});

			for (TimeSeries timeSeries : results) {
				log.info("Found <" + timeSeries + "> in the database.");
			}

		}
	}
}
