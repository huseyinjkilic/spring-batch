package hello;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class TimeSeriesItemProcessor implements ItemProcessor<TimeSeries, TimeSeries> {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesItemProcessor.class);

    @Override
    public TimeSeries process(final TimeSeries timeSeries) throws Exception {
    	
        final String name = timeSeries.getName();
        final String multiplier = timeSeries.getMultiplier();
        final String date = timeSeries.getDate();
        
        
        TimeSeries transformedTimeSeries = null;
    	
    	if(isWeekend(date)) {
    		log.info("It is weekend so lets skip this value");
    	} else {
    		if(name.equalsIgnoreCase("Instrument1")) {
    			
    		}
    		
    		transformedTimeSeries  = new TimeSeries(name, multiplier, "");
            log.info("For value  (" + timeSeries + ") into (" + transformedTimeSeries + ")");
    	}

        return transformedTimeSeries;
    }
    
    public boolean isWeekend(String date) throws ParseException {
    	DateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);    	
    	Date d = df.parse(date);
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(d);
    	int wday = cal.get(Calendar.DAY_OF_WEEK);
    	return (wday == Calendar.SATURDAY || wday == Calendar.SUNDAY);
    }

}
