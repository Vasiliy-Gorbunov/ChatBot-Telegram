package teamwork.chatbottelegrem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import teamwork.chatbottelegrem.model.ReportMessage;
import teamwork.chatbottelegrem.service.ReportMessageService;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportMessageControllerTest {
    private final String fileType = "image/jpeg";
    private final ReportMessage REPORT_MESSAGE_EXAMPLE = new ReportMessage(
            123123L, 321321L, "Шарик", "Мясо 2 раза в день",
            "Здоровый", "Спокойный", "/s/d/s/f/f/ggfsfs",
            new Date(2023, Calendar.MAY, 10), new byte[]{1, 5, 7, 6, 1});
    @Mock
    ReportMessageService service;
    @InjectMocks
    ReportMessageController controller;

    @BeforeEach
    private void initMock() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void downloadReport() {
        when(service.findById(REPORT_MESSAGE_EXAMPLE.getId())).thenReturn(REPORT_MESSAGE_EXAMPLE);
        assertEquals(REPORT_MESSAGE_EXAMPLE, controller.downloadReport(REPORT_MESSAGE_EXAMPLE.getId()));
    }

    @Test
    void remove() {
        controller.remove(REPORT_MESSAGE_EXAMPLE.getId());
        verify(service).remove(REPORT_MESSAGE_EXAMPLE.getId());

    }

    @Test
    void getAll() {
        controller.getAll();
        verify(service).getAll();

    }

    @Test
    void downloadPhotoFromDB() {
        when(service.findById(REPORT_MESSAGE_EXAMPLE.getId())).thenReturn(REPORT_MESSAGE_EXAMPLE);
        REPORT_MESSAGE_EXAMPLE.setData(new byte[]{5, 8, 7, 9, 7, 1,});
        controller.downloadPhotoFromDB(REPORT_MESSAGE_EXAMPLE.getId());
        verify(service).findById(REPORT_MESSAGE_EXAMPLE.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(REPORT_MESSAGE_EXAMPLE.getData().length);
        assertEquals(headers, controller.downloadPhotoFromDB(REPORT_MESSAGE_EXAMPLE.getId()).getHeaders());
        assertEquals(REPORT_MESSAGE_EXAMPLE.getData(), controller.downloadPhotoFromDB(REPORT_MESSAGE_EXAMPLE.getId()).getBody());
    }
}