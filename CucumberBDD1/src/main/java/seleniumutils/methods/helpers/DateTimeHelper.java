package seleniumutils.methods.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    private String currentDate;
    private String currentTime;
    private String timeStamp;
    private LocalDateTime now;
    private LocalDateTime time;

    public DateTimeHelper() {
        now = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HHmmss");
        currentDate = date.format(now);
        currentTime = time.format(now);
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    public String getTimeStamp() {
        this.timeStamp=currentDate+"_"+currentTime;
        return timeStamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
    
    public LocalDateTime getNow() {
        return now;
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
