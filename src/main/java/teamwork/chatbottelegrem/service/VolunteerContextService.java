package teamwork.chatbottelegrem.service;

import org.springframework.stereotype.Service;
import teamwork.chatbottelegrem.model.VolunteerContext;
import teamwork.chatbottelegrem.repository.VolunteerContextRepository;

import java.util.Optional;

@Service
public class VolunteerContextService {
    private final VolunteerContextRepository volunteerContextRepository;

    public VolunteerContextService(VolunteerContextRepository volunteerContextRepository) {
        this.volunteerContextRepository = volunteerContextRepository;
    }

    public Optional<VolunteerContext> getVolunteerContext(long volunteerChatId) {
        return volunteerContextRepository.findById(volunteerChatId);
    }
    public void deleteVolunteerContext(long volunteerChatId) {
        volunteerContextRepository.deleteById(volunteerChatId);
    }
    public void saveVolunteerContext(VolunteerContext volunteerContext) {
        volunteerContextRepository.save(volunteerContext);
    }
}
