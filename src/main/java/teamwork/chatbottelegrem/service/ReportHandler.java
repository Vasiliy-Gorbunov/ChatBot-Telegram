package teamwork.chatbottelegrem.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;
import teamwork.chatbottelegrem.botInterface.KeyBoard;
import teamwork.chatbottelegrem.exception.ReportDataNotFoundException;
import teamwork.chatbottelegrem.model.CatReport;
import teamwork.chatbottelegrem.model.DogReport;
import teamwork.chatbottelegrem.repository.CatReportRepository;
import teamwork.chatbottelegrem.repository.DogReportRepository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class ReportHandler {
    private final Logger logger = LoggerFactory.getLogger(ReportHandler.class);
    private final TelegramBot telegramBot;
    private final CatReportRepository catReportRepository;
    private final DogReportRepository dogReportRepository;


    public ReportHandler(TelegramBot telegramBot, CatReportRepository catReportRepository, DogReportRepository dogReportRepository) {
        this.telegramBot = telegramBot;
        this.catReportRepository = catReportRepository;
        this.dogReportRepository = dogReportRepository;

    }

    /**
     * Метод проверки полноты отчета
     */


    public boolean checkReport(Update update, String catDogUser) {
        try {
            Message message = update.message();
            Long chatId = update.message().chat().id();
            String caption = update.message().caption();
            String text = update.message().text();
            if (update.message().photo() != null) {
                PhotoSize photo = update.message().photo()[update.message().photo().length - 1];
            }
        } catch (ReportDataNotFoundException e) {
            logger.error(e.getMessage(), e);
        } finally {
            String caption = update.message().caption();
            Message message = update.message();
            String text = update.message().text();
            Long chatId = update.message().chat().id();
            PhotoSize[] photos = update.message().photo();

            boolean missedCaption = caption == null || caption.isEmpty() || caption.isBlank();

            if (message != null && missedCaption && text == null) {
                if (catDogUser.equals("catUsers")) {
                    CatReport lastReport = lastCatReport(chatId);
                    if (!(LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() != null)) {
                        sendMessage(chatId,"Пожалуйста, направьте текстовый отчет о питомце");
                    }
                } else if (catDogUser.equals("dogUsers")){
                    DogReport lastReport = lastDogReport(chatId);
                    if (!(LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() != null)) {
                        sendMessage(chatId,"Пожалуйста, направьте текстовый отчет о питомце");
                    }
                }
            } else if (text != null || caption != null) {
                if (caption != null) {
                    text = caption;
                }
                if (catDogUser.equals("catUsers")) {
                    CatReport lastReport = lastCatReport(chatId);
                    if (LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() == null) {
                        lastReport.setTextReport(text);
                        catReportRepository.save(lastReport);
                    } else if (LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() != null) {
                        lastReport.setTextReport(lastReport.getTextReport() + " " + text);
                        catReportRepository.save(lastReport);
                    } else if (!LocalDate.now().equals(lastReport.getDate())){
                        CatReport newReport = new CatReport();
                        newReport.setChatId(chatId);
                        newReport.setDate(LocalDate.now());
                        newReport.setTextReport(text);
                        catReportRepository.save(newReport);
                    }
                    sendMessage(chatId, "Мы получили ваш отчёт, спасибо!");

                } else if (catDogUser.equals("dogUsers")){
                    DogReport lastReport = lastDogReport(chatId);
                    if (LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() == null) {
                        lastReport.setTextReport(text);
                        dogReportRepository.save(lastReport);
                    } else if (LocalDate.now().equals(lastReport.getDate()) && lastReport.getTextReport() != null) {
                        lastReport.setTextReport(lastReport.getTextReport() + " " + text);
                        dogReportRepository.save(lastReport);
                    } else if (!LocalDate.now().equals(lastReport.getDate())){
                        DogReport newReport = new DogReport();
                        newReport.setChatId(chatId);
                        newReport.setDate(LocalDate.now());
                        newReport.setTextReport(text);
                        dogReportRepository.save(newReport);
                    }
                    sendMessage(chatId, "Мы получили ваш отчёт, спасибо!");

                }
            }

            if (message != null && photos == null) {
                if (catDogUser.equals("catUsers")) {
                    CatReport lastReport = lastCatReport(chatId);
                    if (!(LocalDate.now().equals(lastReport.getDate()) && lastReport.getFileId() != null)) {
                        sendMessage(chatId, "Пожалуйста, направьте фото питомца");
                    }
                } else if (catDogUser.equals("dogUsers")) {
                    DogReport lastReport = lastDogReport(chatId);
                    if (!(LocalDate.now().equals(lastReport.getDate()) && lastReport.getFileId() != null)) {
                        sendMessage(chatId,"Пожалуйста, направьте фото питомца");
                    }
                }
            } else if (message != null) {
                GetFileResponse getFileResponse = telegramBot.execute(
                        new GetFile(photos[photos.length - 1].fileId()));
                if (getFileResponse.isOk()) {
                    try {
                        String extension = StringUtils.getFilenameExtension(
                                getFileResponse.file().filePath());
                        byte[] image = telegramBot.getFileContent(getFileResponse.file());
                        Path path = Path.of("src/main/resources/reports/" + catDogUser + "/" + chatId);
                        Files.createDirectories(path);
                        Files.write(Paths.get(path + "/" + LocalDate.now() + "."+ extension), image);
//                                (DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "." + extension), image);
                        if (catDogUser.equals("catUsers")) {
                            CatReport lastReport = lastCatReport(chatId);
                            if (LocalDate.now().equals(lastReport.getDate())) {
                                lastReport.setFileId(photos[photos.length - 1].fileId());
                                catReportRepository.save(lastReport);
                            } else {
                                CatReport report = new CatReport();
                                report.setDate(LocalDate.now());
                                report.setChatId(chatId);
                                report.setFileId(photos[photos.length - 1].fileId());
                                catReportRepository.save(report);
                            }
                        } else if (catDogUser.equals("dogUsers")) {
                            DogReport lastReport = lastDogReport(chatId);
                            if (LocalDate.now().equals(lastReport.getDate())) {
                                lastReport.setFileId(photos[photos.length - 1].fileId());
                                dogReportRepository.save(lastReport);
                            } else {
                                DogReport report = new DogReport();
                                report.setDate(LocalDate.now());
                                report.setChatId(chatId);
                                report.setFileId(photos[photos.length - 1].fileId());
                                dogReportRepository.save(report);
                            }
                        }
                        sendMessage(chatId, "Мы получили ваше фото, спасибо!");
                        KeyBoard keyBoard = new KeyBoard(telegramBot);
                        keyBoard.shelterMainMenu(chatId);
                            return true;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return false;
    }

    private CatReport lastCatReport(Long chatId) {
        List<CatReport> reports = catReportRepository.findAllByChatId(chatId);
        if (reports.size() > 0) {
            return reports.get(reports.size() - 1);
        } else return new CatReport();
    }
    private DogReport lastDogReport(Long chatId) {
        List<DogReport> reports = dogReportRepository.findAllByChatId(chatId);
        if (reports.size() > 0) {
            return reports.get(reports.size() - 1);
        } else return new DogReport();
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}


