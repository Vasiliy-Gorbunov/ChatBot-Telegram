package teamwork.chatbottelegrem.service;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teamwork.chatbottelegrem.Model.DogReport;
import teamwork.chatbottelegrem.repository.DogReportRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

    @ExtendWith(MockitoExtension.class)
    public class  DogReportServiceTest {


        @Mock
        DogReportRepository dogReportRepository;

        @Mock
        TelegramBot telegramBot;

        @InjectMocks
        DogReportService dogReportService;


        DogReportServiceTest() throws URISyntaxException {

        }

        @Test
        public void dogReportSaveTest () throws URISyntaxException, IOException {
            String json = Files.readString(Path.of(CatReportServiceTest.class.getResource("update.json").toURI()));
            Update update = BotUtils.fromJson(json, Update.class);
            GetFileResponse sendResponse = BotUtils.fromJson("""
            {
            "ok": true
            }
            """, GetFileResponse.class);

            when(telegramBot.execute(any())).thenReturn(sendResponse);
            DogReport dogReport = dogReportService.dogReportFromUpdate(update);
            dogReportService.save(update);
            verify(dogReportRepository.save((dogReport)));
            assertEquals(dogReport.getFileId(), "15");
        }
    }