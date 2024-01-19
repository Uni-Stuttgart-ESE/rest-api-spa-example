package de.unistuttgart.iste.ese.api.cats;

import de.unistuttgart.iste.ese.api.ApiVersion1;
import de.unistuttgart.iste.ese.api.todo.TodoModel;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@ApiVersion1
public class CatController {
    private static final Log LOG = LogFactory.getLog(CatController.class);
    
    private final CatRepository catRepository;

    public CatController(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    // executed after start-up and dependency injection
    @PostConstruct
    public void init() {

        // check if DB is empty
        long numberOfCats = catRepository.count();
        if (numberOfCats == 0) {
            // adding sample data for demonstration purposes
            Cat octocat = new Cat("Octocat", 42, "https://avatars1.githubusercontent.com/u/583231");
            catRepository.save(octocat);

            Cat grumpyCat = new Cat("Grumpy Cat", 10,
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Grumpy_Cat_%2814556024763%29_%28cropped%29.jpg/220px-Grumpy_Cat_%2814556024763%29_%28cropped%29.jpg");
            catRepository.save(grumpyCat);
        }
    }

    // get all cats
    @GetMapping("/cats")
    public List<Cat> getCats() {
        return (List<Cat>) catRepository.findAll();
    }

    // get a prediction for a sample todo task
    @GetMapping("/classify")
    public String todoModel() {
        String sampleText = "Take care of my cat.";
        String modelPath = "model.pmml";

        TodoModel todoModel = new TodoModel(modelPath);

        String prediction = todoModel.predictClass(sampleText);

        LOG.info(sampleText + " | Predicted class: " + prediction);
        todoModel.unloadModel();

        return prediction;
    }

    // get a single cat
    @GetMapping("/cats/{id}")
    public Cat getCat(@PathVariable("id") long id) {

        Cat searchedCat = catRepository.findById(id);
        if (searchedCat != null) {
            return searchedCat;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }

    // create a cat
    @PostMapping("/cats")
    @ResponseStatus(HttpStatus.CREATED)
    public Cat createCat(@Valid @RequestBody Cat requestBody) {
        Cat cat = new Cat(requestBody.getName(), requestBody.getAgeInYears(),
                requestBody.getPicUrl());
        return catRepository.save(cat);
    }

    // update a cat
    @PutMapping("/cats/{id}")
    public Cat updateCat(@PathVariable("id") long id, @Valid @RequestBody Cat requestBody) {
        requestBody.setId(id);
        Cat catToUpdate = catRepository.findById(id);
        if (catToUpdate != null) {
            return catRepository.save(requestBody);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }

    // delete a cat
    @DeleteMapping("/cats/{id}")
    public Cat deleteCat(@PathVariable("id") long id) {

        Cat catToDelete = catRepository.findById(id);
        if (catToDelete != null) {
            catRepository.deleteById(id);
            return catToDelete;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }
}
