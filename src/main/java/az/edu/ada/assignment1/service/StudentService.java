package az.edu.ada.assignment1.service;

import az.edu.ada.assignment1.model.Student;
import az.edu.ada.assignment1.repository.StudentRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create or Update a Student
    public Student saveOrUpdateStudent(Student student) {
        if (student.getId() == null) {
            // If there's no ID, it's a new student
            return studentRepository.save(student); // Create operation
        } else {
            // Existing ID indicates an update
            return studentRepository.findById(student.getId())
                    .map(existingStudent -> {
                        // Update existing student details
                        existingStudent.setName(student.getName());
                        existingStudent.setEmail(student.getEmail());
                        existingStudent.setDateOfBirth(student.getDateOfBirth());
                        existingStudent.setAddress(student.getAddress());
                        existingStudent.setMajor(student.getMajor());
                        // Assume courses are managed separately
                        return studentRepository.save(existingStudent);
                    })
                    .orElseGet(() -> {
                        // If the student doesn't exist, create as new
                        return studentRepository.save(student);
                    });
        }
    }

    // Retrieve all students
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    // Retrieve a single student by ID
    @Transactional(readOnly = true)
    public Optional<Student> findStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(student -> Hibernate.initialize(student.getCourses()));
        return studentOpt;
    }

    // Delete a student by ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Additional method to fetch a student with courses
    public Optional<Student> findStudentWithCoursesById(Long id) {
        return studentRepository.fetchWithCoursesById(id);
    }

    public List<Student> findStudentsByMajor(String major) {
        return studentRepository.findStudentsByMajor(major);
    }
}
