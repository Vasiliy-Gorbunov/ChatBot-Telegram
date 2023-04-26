package teamwork.chatbottelegrem.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.service.DogUsersService;

@RestController
@RequestMapping("/dogUsers")
public class DogUsersController {
    private final DogUsersService dogUsersService;


    public DogUsersController(DogUsersService dogUsersService) {
        this.dogUsersService = dogUsersService;
    }

    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public DogUsers getById(@PathVariable Long id) {
        return dogUsersService.getById(id);
    }

    @Operation(summary = "Получение пользователя по chat id")
    @GetMapping("/{chatId}")
    public DogUsers getByChatId(@PathVariable Long chatId) {
        return dogUsersService.getById(chatId);
    }
    @Operation(summary = "Создание пользователя")
    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody DogUsers dogUsers) {
        dogUsersService.save(dogUsers);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Изменение данных пользователя")
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody DogUsers dogUsers) {
        dogUsersService.save(dogUsers);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Удаление пользователей по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        dogUsersService.delete(id);
        return ResponseEntity.ok().build();
    }
}
