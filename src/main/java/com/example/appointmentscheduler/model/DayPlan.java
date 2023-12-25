package com.example.appointmentscheduler.model;

import java.util.List;
import java.util.ArrayList;

public class DayPlan {
    private TimePeroid workingHours;
    private List<TimePeroid> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }


    //DONE: getters and setters
    public TimePeroid getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(TimePeroid workingHours) {
        this.workingHours = workingHours;
    }

    public List<TimePeroid> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<TimePeroid> breaks) {
        this.breaks = breaks;
    }

    //TODO: other methods

    public void removeBreak(TimePeroid breakToRemove) {
        breaks.remove(breakToRemove);
    }

    public void addBreak(TimePeroid breakToAdd) {
        breaks.add(breakToAdd);
    }

    //TODO: Phương thức trả về danh sách các khoảng thời gian trong ngày mà không tính các thời gian nghỉ
}
