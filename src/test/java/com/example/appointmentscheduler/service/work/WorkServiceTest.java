package com.example.appointmentscheduler.service.work;

import com.example.appointmentscheduler.dao.WorkRepository;
import com.example.appointmentscheduler.entity.Work;
import com.example.appointmentscheduler.service.impl.WorkServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class WorkServiceTest {

    // Khai báo đối tượng mock cho WorkRepository
    @Mock
    private WorkRepository workRepository;

    // Tự động inject các mock vào đối tượng cần kiếm thử (WorkServiceImpl)
    @InjectMocks
    private WorkServiceImpl workService;

    // Khai báo các đối tượng cần kiểm thử
    private Work work;
    private Optional<Work> workOptional;
    private List<Work> works;

    // Phương thức được thực hiện trước mỗi phương thức để khởi tạo
    @Before
    public void initObjects() {
        work = new Work();
        workOptional = Optional.of(work);
    }

    // Phương thức kieemr thử chức năng lưu mới công việc
    @Test
    public void shouldSaveWork() {
        workService.createNewWork(work);
        verify(workRepository, times(1)).save(work);
    }

    // Phương thức kiểm thử chức năng tìm kiếm công việc theo id
    @Test
    public void shouldFindWorkById() {
        when(workRepository.findById(1)).thenReturn(workOptional);
        Assert.assertEquals(workOptional.get(), workService.getWorkById(1));
        verify(workRepository, times(1)).findById(1);
    }

    // Phương thức kiểm thử chức năng tìm kiếm tất cả công việc
    @Test
    public void shouldFindAllWorks() {
        when(workRepository.findAll()).thenReturn(works);
        assertEquals(works, workService.getAllWorks());
        verify(workRepository, times(1)).findAll();
    }

    // Phương thức kiểm thử chức năng xóa công việc theo id
    @Test
    public void shouldDeleteWorkById() {
        workService.deleteWorkById(1);
        verify(workRepository).deleteById(1);
    }
}
