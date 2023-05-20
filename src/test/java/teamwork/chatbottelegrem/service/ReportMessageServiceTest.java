package teamwork.chatbottelegrem.service;

import liquibase.pro.packaged.R;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import teamwork.chatbottelegrem.Model.ReportMessage;
import teamwork.chatbottelegrem.repository.ReportMessageRepository;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportMessageServiceTest {

    private final ReportMessage REPORT_MESSAGE_EXAMPLE = new ReportMessage(123123L, 321321L, "Шарик", "Мясо 2 раза в день",
            "Здоровый", "Спокойный", "/s/d/s/f/f/ggfsfs", new Date(2023, Calendar.MAY, 10), new byte[]{1,5,7,6,1});
    @Mock
    ReportMessageRepository repository;

    @InjectMocks
    ReportMessageService service;

    @BeforeEach
    public void iniMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadReportMessage() throws IOException {
        REPORT_MESSAGE_EXAMPLE.setId(0);
        service.uploadReportMessage(REPORT_MESSAGE_EXAMPLE.getChatId(), REPORT_MESSAGE_EXAMPLE.getName(),
                REPORT_MESSAGE_EXAMPLE.getData(), REPORT_MESSAGE_EXAMPLE.getRation(),
                REPORT_MESSAGE_EXAMPLE.getHealth(), REPORT_MESSAGE_EXAMPLE.getBehaviour(),
                REPORT_MESSAGE_EXAMPLE.getFilePath(), REPORT_MESSAGE_EXAMPLE.getLastMessage());
        verify(repository).save(REPORT_MESSAGE_EXAMPLE);
    }

    @Test
    void findById() {
        when(repository.findById(REPORT_MESSAGE_EXAMPLE.getId())).thenReturn(Optional.of(REPORT_MESSAGE_EXAMPLE));
        assertEquals(REPORT_MESSAGE_EXAMPLE,service.findById(REPORT_MESSAGE_EXAMPLE.getId()));
    }

    @Test
    void findByChatId() {
        when(repository.findByChatId(REPORT_MESSAGE_EXAMPLE.getChatId())).thenReturn(REPORT_MESSAGE_EXAMPLE);
        assertEquals(REPORT_MESSAGE_EXAMPLE,service.findByChatId(REPORT_MESSAGE_EXAMPLE.getChatId()));
    }

    @Test
    void findListByChatId() {
        Set<ReportMessage> reportMessages = new HashSet<>();
        reportMessages.add(REPORT_MESSAGE_EXAMPLE);
        when(repository.findListByChatId(REPORT_MESSAGE_EXAMPLE.getId())).thenReturn(reportMessages);
        assertEquals(reportMessages, service.findListByChatId(REPORT_MESSAGE_EXAMPLE.getId()));
    }

    @Test
    void save() {
        service.save(REPORT_MESSAGE_EXAMPLE);
        verify(repository).save(REPORT_MESSAGE_EXAMPLE);

    }

    @Test
    void remove() {
        service.remove(REPORT_MESSAGE_EXAMPLE.getId());
        verify(repository).deleteById(REPORT_MESSAGE_EXAMPLE.getId());
    }

    @Test
    void getAll() {
        List<ReportMessage> reportMessages = new ArrayList<>();
        reportMessages.add(REPORT_MESSAGE_EXAMPLE);
        when(repository.findAll()).thenReturn(reportMessages);
        assertEquals(reportMessages, service.getAll());
    }
}