package az.edu.ada.assignment1.repository;

import az.edu.ada.assignment1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // This query supports the findStudentWithCoursesById service method
    @Query("SELECT s FROM Student s JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> fetchWithCoursesById(@Param("id") Long id);


    // This query is for finding students by their major, potentially used in additional service methods
    @Query(value = "SELECT * FROM STUDENTS WHERE major = :major", nativeQuery = true)
    List<Student> findStudentsByMajor(@Param("major") String major);

    // Overriding deleteById to be transactional, supporting the deleteStudent service method
    @Transactional
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = :id")
    void deleteById(@Param("id") Long id);
}
