package com.example.appointmentscheduler.service.qrcode;

import com.example.appointmentscheduler.dao.InvoiceRepository;
import com.example.appointmentscheduler.entity.Appointment;
import com.example.appointmentscheduler.service.InvoiceService;
import com.example.appointmentscheduler.service.QRCodeService;
import com.example.appointmentscheduler.service.impl.QRCodeServiceImpl;
import com.google.zxing.WriterException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QRCodeServiceTest {
    @Mock
    private QRCodeService qrCodeService;

    @InjectMocks
    private QRCodeServiceImpl qrCodeServiceImpl;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateQRCodeImage() throws WriterException, IOException {
        String invoiceData = "123456789";


        // Thiết lập hành vi giả lập
        when(qrCodeService.generateQRCodeImage(invoiceData)).thenReturn(new byte[]{/* Mocked byte array */});

        byte[] qrCodeImage = qrCodeServiceImpl.generateQRCodeImage(invoiceData);
        assertNotNull("QrCode is null",qrCodeImage);

    }


}
