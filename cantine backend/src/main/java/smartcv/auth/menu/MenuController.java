package smartcv.auth.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import smartcv.auth.serviceImpl.MenuService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Method to get all menus
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();

        List<Map<String, Object>> response = menus.stream().map(menu -> {
            Map<String, Object> menuData = new HashMap<>();
            menuData.put("id", menu.getId());
            menuData.put("entree", menu.getEntree());
            menuData.put("mainCourse", menu.getMainCourse());
            menuData.put("garnish", menu.getGarnish());
            menuData.put("dessert", menu.getDessert());
            menuData.put("sandwiches", menu.getSandwiches());

            return menuData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Menu> addMenu(@RequestBody Menu menu) {
        Menu addedMenu = menuService.addMenu(menu);
        return ResponseEntity.ok(addedMenu);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTable(@RequestBody Map<String, Object> tableData) {
        try {
            menuService.saveTable(tableData);
            return ResponseEntity.ok("Table saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving table");
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable int id, @RequestBody Menu menuDetails) {
        System.out.println(menuDetails.getGarnish());
        Menu updatedMenu = menuService.updateMenu(id, menuDetails);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable int id) {
        boolean isDeleted = menuService.deleteMenu(id);

        if (isDeleted) {
            return ResponseEntity.ok("Menu deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found");
        }
    }
}
