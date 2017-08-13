package hello;

public class TimeSeries {
    private String multiplier;
    private String name;
    private String date;

    public TimeSeries() {

    }
    

	public TimeSeries(String name, String date, String multiplier) {
		super();
		this.multiplier = multiplier;
		this.name = name;
		this.date = date;
	}
    
	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "TimeSeries [multiplier=" + multiplier + ", name=" + name + "]";
	}



}
