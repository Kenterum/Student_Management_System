package az.edu.ada.assignment1.controller;

import az.edu.ada.assignment1.model.Course;
import az.edu.ada.assignment1.model.Student;
import az.edu.ada.assignment1.service.CourseService;
import az.edu.ada.assignment1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/index")
    public String getAllStudents(Model model) {
        List<Student> students = studentService.findAllStudents();
        model.addAttribute("students", students);
        return "student/index";
    }

    @GetMapping("/detail/{id}")
    public String getStudentById(@PathVariable Long id, Model model) {
        Optional<Student> studentOpt = studentService.findStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("student", student);
            model.addAttribute("courses", student.getCourses());
        } else {
            return "redirect:/students/index"; // or handle student not found appropriately
        }
        return "student/detail";
    }
    @GetMapping("/create")
    public String showStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("allCourses", courseService.findAllCourses());
        return "student/create";
    }

//    @PostMapping("/create")
//    public String saveOrUpdateStudent(@ModelAttribute Student student,
//                                       @RequestParam(required = false) List<Long> courseIds,
//                                      RedirectAttributes redirectAttributes) {
//        Set<Course> selectedCourses = new HashSet<>();
//        if (courseIds != null) {
//            for (Long courseId : courseIds) {
//                Course course = courseService.findCourseById(courseId)
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + courseId));
//                selectedCourses.add(course);
//            }
//        }
//        student.setCourses(selectedCourses);
//        studentService.saveOrUpdateStudent(student);
//        redirectAttributes.addFlashAttribute("message", "Student saved successfully");
//        return "redirect:/students/index";
//    }


    @PostMapping("/create")
    public String saveStudent(@ModelAttribute Student student,
                              @RequestParam(required = false) List<Long> courseIds,
                              RedirectAttributes redirectAttributes) {
        processCourses(student, courseIds);
        studentService.saveOrUpdateStudent(student);
        redirectAttributes.addFlashAttribute("message", "Student saved successfully.");
        return "redirect:/students/index";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute Student student,
                                @RequestParam(required = false) List<Long> courseIds,
                                RedirectAttributes redirectAttributes) {
        Student existingStudent = studentService.findStudentById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student Id: " + id));
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDateOfBirth(student.getDateOfBirth());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setMajor(student.getMajor());
        processCourses(existingStudent, courseIds);
        studentService.saveOrUpdateStudent(existingStudent);
        redirectAttributes.addFlashAttribute("message", "Student updated successfully.");
        return "redirect:/students/index";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Student student = studentService.findStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        List<Course> allCourses = courseService.findAllCourses();
        model.addAttribute("student", student);
        model.addAttribute("allCourses", allCourses);
        return "student/update";
    }

    private void processCourses(Student student, List<Long> courseIds) {
        Set<Course> selectedCourses = new HashSet<>();
        if (courseIds != null && !courseIds.isEmpty()) {
            courseIds.forEach(courseId -> {
                Course course = courseService.findCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Course ID not found: " + courseId));
                selectedCourses.add(course);
            });
        }
        student.setCourses(selectedCourses);
    }

//    @PostMapping("/update/{id}")
//    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student, @RequestParam(value = "courseIds", required = false) List<Long> courseIds, RedirectAttributes redirectAttributes) {
//        student.setId(id);
//        Set<Course> selectedCourses = new HashSet<>();
//        if (courseIds != null) {
//            courseIds.forEach(courseId -> selectedCourses.add(courseService.findCourseById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId))));
//        }
//        student.setCourses(selectedCourses);
//        studentService.saveOrUpdateStudent(student);
//        redirectAttributes.addFlashAttribute("message", "Student updated successfully");
//        return "redirect:/students/index";
//    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("message", "Student deleted successfully");
        return "redirect:/students/index"; // Corrected the redirect issue to navigate properly after deletion.
    }
}
