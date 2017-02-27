package com.ambeyindustry.iss;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Predictions {
    String date;
    Double duration;

    public Predictions(String duration, String date) {
        long milliSeconds = Long.parseLong(date + "000");
        this.duration = Double.parseDouble(duration);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy-hh:mm:ss a");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            this.date = formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public Double getDuration() {
        return duration;
    }
}
