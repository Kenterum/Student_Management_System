package az.edu.ada.assignment1.repository;

import az.edu.ada.assignment1.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {

    // Finds courses taught by a specific instructor
    @Query("SELECT c FROM Course c WHERE c.instructor = :instructorName")
    List<Course> findInstructorByName(@Param("instructorName") String name);

    // Retrieves all courses a given student is enrolled in
    @Query("SELECT c FROM Course c INNER JOIN c.students s WHERE s.id = :studId")
    List<Course> findCoursesByStudentId(@Param("studId") Long studentId);

    // Custom method to remove a course by its ID
    @Transactional
    @Modifying
    @Query("DELETE FROM Course c WHERE c.id = :courseId")
    void deleteCoursesByCourseId(@Param("courseId") Long courseId);


    Optional<Course> findByCourseCode(String courseCode);
}
