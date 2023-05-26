package teamwork.chatbottelegrem.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import teamwork.chatbottelegrem.model.CatUsers;
import teamwork.chatbottelegrem.service.CatUsersService;

import java.util.Collection;
/**
 * Контроллер класа владельца кота
 */
@RestController
@RequestMapping("catUsers")
public class CatUsersController {
    private final CatUsersService service;
    public CatUsersController(CatUsersService service) {
        this.service = service;
    }
    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public CatUsers getById(@PathVariable Long id) {
        return this.service.getById(id);
    }
    @Operation(summary = "Создание пользователя")
    @PostMapping()
    public CatUsers save(@RequestBody CatUsers catUsers) {
        return this.service.create(catUsers);
    }
    @Operation(summary = "Изменение данных пользователя")
    @PutMapping
    public CatUsers update(@RequestBody CatUsers catUsers) {
        return this.service.update(catUsers);
    }
    @Operation(summary = "Удаление пользователей по id")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        this.service.removeById(id);
    }
    @Operation(summary = "Просмотр всех пользователей")
    @GetMapping("/all")
    public Collection<CatUsers> getAll(@RequestParam(required = false) Long chatId) {
        if (chatId != null) {
            return this.service.getByChatId(chatId);
        }
        return this.service.getAll();
    }

}
