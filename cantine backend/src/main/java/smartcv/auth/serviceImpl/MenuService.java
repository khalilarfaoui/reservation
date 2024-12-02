package smartcv.auth.serviceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import smartcv.auth.menu.Menu;
import smartcv.auth.menu.MenuRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class MenuService {

    @Autowired
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // Method to get all menus
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu updateMenu(int id, Menu menuDetails) {
        return menuRepository.findById(id).map(menu -> {
            menu.setEntree(menuDetails.getEntree());
            menu.setMainCourse(menuDetails.getMainCourse());
            System.out.println(menuDetails.getGarnish());
            menu.setGarnish(menuDetails.getGarnish());
            System.out.println(menu.getGarnish());
            menu.setDessert(menuDetails.getDessert());
            menu.setSandwiches(menuDetails.getSandwiches());
            // If needed, you can update the user or other fields
            return menuRepository.save(menu);
        }).orElseThrow(() -> new NoSuchElementException("Menu not found with id " + id));
    }


    // Save the menu row to the database
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);  // Save the new menu row
    }

    @Transactional
    public boolean deleteMenu(int menuId) {
        if (menuRepository.existsById(menuId)) {
            menuRepository.deleteById(menuId);
            return true;
        } else {
            return false;
        }
    }

    private JdbcTemplate jdbcTemplate;

    public void saveTable(Map<String, Object> tableData) {
        List<String> columns = (List<String>) tableData.get("columns");
        List<Map<String, Object>> data = (List<Map<String, Object>>) tableData.get("data");

        // Iterate through each row (ligne)
        for (Map<String, Object> row : data) {
            StringBuilder query = new StringBuilder("INSERT INTO menu_table (");
            StringBuilder values = new StringBuilder(" VALUES (");

            for (String column : columns) {
                query.append(column).append(",");
                values.append("'").append(row.get(column)).append("',");
            }

            // Remove trailing commas
            query.setLength(query.length() - 1);
            values.setLength(values.length() - 1);

            query.append(")").append(values).append(")");
            jdbcTemplate.execute(query.toString());
        }
    }
}
