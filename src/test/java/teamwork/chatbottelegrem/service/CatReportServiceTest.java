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
import teamwork.chatbottelegrem.Listener.TelegramBotUpdatesListenerTest;
import teamwork.chatbottelegrem.Model.CatReport;
import teamwork.chatbottelegrem.repository.CatReportRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CatReportServiceTest {

    @Mock
    CatReportRepository catReportRepository;

    @Mock
    TelegramBot telegramBot;

    @InjectMocks
    CatReportService catReportService;


    CatReportServiceTest() throws URISyntaxException {

    }

    @Test
    public void catReportSaveTest () throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(CatReportServiceTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json, Update.class);
        byte[] testPhoto = Files.readAllBytes(Path.of(TelegramBotUpdatesListenerTest.class.getResource("foto.jpeg").toURI()));


        GetFileResponse getFileResponse = BotUtils.fromJson("""
                {
                    "result":
                    {
                        "file_id": "001",
                        "file_unique_id": "002",
                        "file_size": 157170,
                        "file_path": "photo.jpeg"
                    },
                    "ok": true
                }
                """, GetFileResponse.class);

        when(telegramBot.execute(any())).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(testPhoto);


        catReportService.save(update);
        CatReport catReport = catReportService.catReportFromUpdate(update);
        verify(catReportRepository).save(catReport);
        assertEquals(catReport.getFileId(), "15");
    }
}

