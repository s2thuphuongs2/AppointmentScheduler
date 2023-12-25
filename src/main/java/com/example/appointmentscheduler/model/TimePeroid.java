package com.example.appointmentscheduler.model;

import java.time.LocalTime;
import java.util.Objects;
public class TimePeroid implements Comparable<TimePeroid> {

        private LocalTime start;
        private LocalTime end;

        public TimePeroid() {

        }

        public TimePeroid(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalTime getStart() {
            return start;
        }

        public void setStart(LocalTime start) {
            this.start = start;
        }
        public LocalTime getEnd() {
            return end;
        }
        public void setEnd(LocalTime end) {
            this.end = end;
        }
        @Override
        public int compareTo(TimePeroid o) {
            return this.getStart().compareTo(o.getStart());
        }
        //note: Phương thức kiểm tra xem hai đối tượng có bằng nhau không
        @Override
        public boolean equals(Object o) {
            // Kiểm tra xem đối tượng hiện tại có bằng với chính nó không
            if (this == o) return true;

            // Kiểm tra xem đối tượng o có là một đối tượng của lớp TimePeroid không
            if (o == null || getClass() != o.getClass()) return false;

            // Ép kiểu đối tượng o về lớp TimePeroid
            TimePeroid peroid = (TimePeroid) o;

            // So sánh thời điểm bắt đầu và kết thúc của hai khoảng thời gian
            return this.start.equals(peroid.getStart()) &&
                    this.end.equals(peroid.getEnd());
        }

    @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
        @Override
        public String toString() {
            return "TimePeroid{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
}
