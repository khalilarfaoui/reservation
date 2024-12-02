package smartcv.auth.menu;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Récupérer toutes les catégories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable int id) {
        return categoryRepository.findById(id);
    }

    // Ajouter une nouvelle catégorie
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PostMapping("/all")
    public  List<Category>  createCategoryAll(@RequestBody List<Category> category) {
        return categoryRepository.saveAll(category);
    }

    // Mettre à jour une catégorie
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setValeur(categoryDetails.getValeur());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
        return "Category with id " + id + " has been deleted.";
    }
}