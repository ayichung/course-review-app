package edu.virginia.cs;

import org.hibernate.*;
import java.util.*;
import java.util.logging.*;

public class DatabaseService {
    private DatabaseManager manager;

    public void initDatabaseManager() {
        if (manager == null) {
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            Session session = HibernateUtil.getSessionFactory().openSession();
            manager = new DatabaseManager(session);
        }
    }
    public void addStudentToDatabase(Student student) {
        manager.startTransaction();
        manager.addStudent(student);
        manager.endTransaction();
    }

    public boolean studentExistsInDatabase(Student student) {
        manager.startTransaction();
        boolean studentExists = manager.studentExists(student);
        manager.endTransaction();
        return studentExists;
    }

    public boolean studentHasPasswordMismatchInDatabase(Student student) {
        manager.startTransaction();
        boolean passwordMismatch = manager.studentHasPasswordMismatch(student);
        manager.endTransaction();
        return passwordMismatch;
    }

    public int getStudentIDFromDatabase(Student student) {
        manager.startTransaction();
        int id = manager.getStudentID(student.getUsername());
        manager.endTransaction();
        return id;
    }

    public void addCourseToDatabase(Course course) {
        manager.startTransaction();
        manager.addCourse(course);
        manager.endTransaction();
    }

    public boolean courseExistsInDatabase(Course course) {
        manager.startTransaction();
        boolean courseExists = manager.courseExists(course);
        manager.endTransaction();
        return courseExists;
    }

    public int getCourseIDFromDatabase(Course course) {
        manager.startTransaction();
        int id = manager.getCourseID(course);
        manager.endTransaction();
        return id;
    }

    public void addReviewToDatabase(Review review) {
        manager.startTransaction();
        manager.addReviewForCourse(review);
        manager.endTransaction();
    }

    public boolean studentAlreadyReviewedCourseInDatabase(Student student, Course course) {
        manager.startTransaction();
        boolean studentReviewedCourse = manager.studentAlreadyReviewedCourse(student, course);
        manager.endTransaction();
        return studentReviewedCourse;
    }

    public List<Review> getCourseReviewsInDatabase(Course course) {
        manager.startTransaction();
        List<Review> reviewList = manager.getCourseReviews(course);
        manager.endTransaction();
        return reviewList;
    }
}
