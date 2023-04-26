package teamwork.chatbottelegrem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamwork.chatbottelegrem.Model.DogUsers;
@Repository
public interface DogUsersRepository extends JpaRepository<DogUsers, Long> {
    public DogUsers getDogUsersById(long id);
}
