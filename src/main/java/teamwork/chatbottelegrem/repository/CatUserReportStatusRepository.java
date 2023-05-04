package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.Model.CatUserReportStatus;

@Repository
public interface CatUserReportStatusRepository extends JpaRepository<CatUserReportStatus, Long> {
    boolean existsCatUserReportStatusByCatUsers_ChatId (Long chatId);
    CatUserReportStatus getReferenceByCatUsers_ChatId(Long chatId);
}