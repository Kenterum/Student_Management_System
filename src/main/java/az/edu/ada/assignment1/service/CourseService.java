package az.edu.ada.assignment1.service;

import az.edu.ada.assignment1.model.Course;
import az.edu.ada.assignment1.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<Course> findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }
    // Create or Update a Course
    public Course saveOrUpdateCourse(Course course) {
        if (course.getId() == null) {
            // No ID means it's a new course
            return courseRepository.save(course); // Create operation
        } else {
            // An existing ID indicates update operation
            return courseRepository.findById(course.getId())
                    .map(existingCourse -> {
                        // Update the existing course with new details
                        existingCourse.setName(course.getName());
                        existingCourse.setInstructor(course.getInstructor());
                        existingCourse.setCourseCode(course.getCourseCode());
                        existingCourse.setDescription(course.getDescription());
                        existingCourse.setCreditHours(course.getCreditHours());
                        return courseRepository.save(existingCourse);
                    })
                    .orElseGet(() -> {
                        // If the course doesn't exist, create a new one
                        return courseRepository.save(course);
                    });
        }
    }


    // Retrieve all courses
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    // Retrieve a single course by ID
    public Optional<Course> findCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Delete a course by ID
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
