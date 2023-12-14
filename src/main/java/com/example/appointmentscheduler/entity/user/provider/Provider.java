package com.example.appointmentscheduler.entity.user.provider;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.entity.WorkingPlan;
import com.example.appointmentscheduler.entity.user.Role;
import com.example.appointmentscheduler.entity.user.User;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "providers")
@PrimaryKeyJoinColumn(name = "id_provider")
public class Provider extends User {

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(name = "works_providers", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_work"))
    private List<Work> works;

    @OneToOne(mappedBy = "provider", cascade = {CascadeType.ALL})
    private WorkingPlan workingPlan;

    public Provider() {
    }

    //TODO: add constructor

    //TODO: add update method

    //DONE: add getters and setters

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public WorkingPlan getWorkingPlan() {
        return workingPlan;
    }

    public void setWorkingPlan(WorkingPlan workingPlan) {
        this.workingPlan = workingPlan;
    }

    public List<Work> getCorporateWorks() {
        List<Work> corporateWorks = new ArrayList<>();
        for (Work w : works) {
            if (w.getTargetCustomer().equals("corporate")) {
                corporateWorks.add(w);
            }
        }
        return corporateWorks;
    }

    public List<Work> getRetailWorks() {
        List<Work> retailWorks = new ArrayList<>();
        for (Work w : works) {
            if (w.getTargetCustomer().equals("retail")) {
                retailWorks.add(w);
            }
        }
        return retailWorks;
    }

    //TODO: add other methods
}
