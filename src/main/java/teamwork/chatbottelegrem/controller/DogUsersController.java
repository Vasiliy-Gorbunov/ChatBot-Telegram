package teamwork.chatbottelegrem.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamwork.chatbottelegrem.Model.CatUsers;
import teamwork.chatbottelegrem.Model.DogUsers;
import teamwork.chatbottelegrem.service.CatUsersService;
import teamwork.chatbottelegrem.service.DogUsersService;

import java.util.Collection;

/**
 * Контроллер класса владельца собаки
 */
@RestController
@RequestMapping("/dogUsers")
public class DogUsersController {
    private final DogUsersService service;
    public DogUsersController(DogUsersService service) {
        this.service = service;
    }
    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public DogUsers getById(@PathVariable Long id) {
        return this.service.getById(id);
    }
    @Operation(summary = "Создание пользователя")
    @PostMapping()
    public DogUsers save(@RequestBody DogUsers dogUsers) {
        return this.service.create(dogUsers);
    }
    @Operation(summary = "Изменение данных пользователя")
    @PutMapping
    public DogUsers update(@RequestBody DogUsers dogUsers) {
        return this.service.update(dogUsers);
    }
    @Operation(summary = "Удаление пользователей по id")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        this.service.removeById(id);
    }
    @Operation(summary = "Просмотр всех пользователей")
    @GetMapping("/all")
    public Collection<DogUsers> getAll(@RequestParam(required = false) Long chatId) {
        if (chatId != null) {
            return this.service.getByChatId(chatId);
        }
        return this.service.getAll();
    }

}
