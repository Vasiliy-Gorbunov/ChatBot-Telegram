package teamwork.chatbottelegrem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamwork.chatbottelegrem.Model.ReportMessage;
import teamwork.chatbottelegrem.service.ReportMessageService;

import java.util.Collection;
/**
 * Контролеер класса отправки уведомлений
 */
@RestController
@RequestMapping("animalReports")
public class ReportMessageController {
    private final ReportMessageService reportMessageService;
    private final String fileType = "image/jpeg";
    public  ReportMessageController( ReportMessageService reportMessageService) {
        this.reportMessageService = reportMessageService;
    }
    @GetMapping("/{id}")
    public ReportMessage downloadReport(@Parameter(description = "report id") @PathVariable Long id) {
        return this.reportMessageService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void remove(@Parameter (description = "report id") @PathVariable Long id) {
        this.reportMessageService.remove(id);
    }
    @GetMapping("/getAll")
    public ResponseEntity<Collection<ReportMessage>> getAll() {
        return ResponseEntity.ok(this.reportMessageService.getAll());
    }
    @GetMapping("/{id}/downloadPhotoFromDb")
    public ResponseEntity<byte[]> downloadPhotoFromDB(@Parameter (description = "report id") @PathVariable Long id) {
        ReportMessage reportData = this.reportMessageService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(reportData.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(reportData.getData());
    }
}

