package az.edu.ada.assignment1.controller;

import az.edu.ada.assignment1.model.Course;
import az.edu.ada.assignment1.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Display all courses
    @GetMapping("/index")
    public String listAllCourses(Model model) {
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("courses", courses);
        return "course/index";
    }

    // Show form to create a new course
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/create";
    }

    // Process the form for creating a new course
    @PostMapping("/create")
    public String createCourse(@ModelAttribute Course course, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            courseService.saveOrUpdateCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course created successfully.");
            return "redirect:/courses/index";
        } catch (DataIntegrityViolationException e) {
            if (result.hasErrors()) {
                return "course/create";
            }
            model.addAttribute("errorMessage", "A course with the given code already exists. Please use a different code.");
            model.addAttribute("course", course);
            return "course/create";
        }
    }

    // Process the form for updating a course
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        Optional<Course> existingCourse = courseService.findByCourseCode(course.getCourseCode());
        if (existingCourse.isPresent() && !existingCourse.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "A course with the given course code already exists.");
            return "redirect:/courses/update/" + id;
        }
        course.setId(id);
        courseService.saveOrUpdateCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully.");
        return "redirect:/courses/index";
    }

    @GetMapping("/detail/{id}")
    public String getCourseDetail(@PathVariable Long id, Model model) {
        Course course = courseService.findCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "course/detail"; // Ensure you have a detail.html template under src/main/resources/templates/course/
    }


    // Show form to update an existing course
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Course course = courseService.findCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "course/update";
    }


    // Delete a course
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("message", "Course successfully deleted.");
        return "redirect:/courses/index";
    }
}
