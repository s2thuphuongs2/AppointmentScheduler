package com.example.appointmentscheduler.model;

import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.entity.AppointmentStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZoneOffset;

/**
 * Lớp tùy chỉnh để chuyển đổi đối tượng Appointment thành định dạng JSON.
 */
public class AppointmentSerializer extends StdSerializer<Appointment> {

        /**
         * Constructor mặc định.
         */
        public AppointmentSerializer() {
                this(null);
        }

        /**
         * Constructor với tham số để chỉ định kiểu được xử lý.
         *
         * @param t Kiểu được xử lý bởi serializer này.
         */
        public AppointmentSerializer(Class<Appointment> t) {
                super(t);
        }

        /**
         * Phương thức để chuyển đối tượng Appointment thành định dạng JSON.
         *
         * @param appointment Đối tượng Appointment cần chuyển đổi.
         * @param gen          JsonGenerator được sử dụng để viết nội dung JSON.
         * @param provider     SerializerProvider để truy cập các chức năng chuyển đổi bổ sung.
         * @throws IOException Nếu có lỗi I/O xảy ra trong quá trình chuyển đổi.
         */
        @Override
        public void serialize(Appointment appointment, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Bắt đầu viết đối tượng JSON
                gen.writeStartObject();

                // Viết các thuộc tính của Appointment vào các trường JSON
                gen.writeNumberField("id", appointment.getId());
                gen.writeStringField("title", appointment.getWork().getName());
                gen.writeNumberField("start", appointment.getStart().toInstant(ZoneOffset.UTC).toEpochMilli());
                gen.writeNumberField("end", appointment.getEnd().toInstant(ZoneOffset.UTC).toEpochMilli());
                gen.writeStringField("url", "/appointments/" + appointment.getId());
                gen.writeStringField("color", appointment.getStatus().equals(AppointmentStatus.SCHEDULED) ? "#28a745" : "grey");

                // Kết thúc đối tượng JSON
                gen.writeEndObject();
        }
}
