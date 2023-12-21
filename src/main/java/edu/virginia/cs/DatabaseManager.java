package edu.virginia.cs;

import org.hibernate.*;
import javax.persistence.*;
import java.util.*;

public class DatabaseManager {
    private Session session;

    public DatabaseManager(Session session) {
        this.session = session;
    }

    public void startTransaction() {
        if (!session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        session.beginTransaction();
    }

    public void endTransaction() {
        session.close();
    }

    public void addStudent(Student student) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        session.persist(student);
        session.getTransaction().commit();
    }

    public boolean studentExists(Student student) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String hqlStr = "SELECT e FROM Student e WHERE e.username = :username";
        TypedQuery<Student> matchingStudentQuery = session.createQuery(hqlStr, Student.class);
        matchingStudentQuery.setParameter("username", student.getUsername());
        List<Student> matchStudentList = matchingStudentQuery.getResultList();
        return matchStudentList.size() != 0;
    }

    public boolean studentHasPasswordMismatch(Student student) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String hqlStr = "SELECT e FROM Student e WHERE e.username = :username AND e.password = :password";
        TypedQuery<Student> matchingStudentQuery = session.createQuery(hqlStr, Student.class);
        matchingStudentQuery.setParameter("username", student.getUsername());
        matchingStudentQuery.setParameter("password", student.getPassword());
        List<Student> matchStudentList = matchingStudentQuery.getResultList();
        return matchStudentList.size() == 0;
    }

    public int getStudentID(String username) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String IdStr = "SELECT e FROM Student e WHERE e.username = :username";
        TypedQuery<Student> IdQuery = session.createQuery(IdStr, Student.class);
        IdQuery.setParameter("username", username);
        return IdQuery.getSingleResult().getId();
    }

    public void addCourse(Course course) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        session.persist(course);
        session.getTransaction().commit();
    }

    public boolean courseExists(Course course) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String hqlStr = "SELECT e FROM Course e WHERE e.department = :department AND e.catalogNumber = :catalogNumber";
        TypedQuery<Course> matchingCourseQuery = session.createQuery(hqlStr, Course.class);
        matchingCourseQuery.setParameter("department", course.getDepartment());
        matchingCourseQuery.setParameter("catalogNumber", course.getCatalogNumber());
        List<Course> matchCourseList = matchingCourseQuery.getResultList();
        return matchCourseList.size() != 0;
    }

    public int getCourseID(Course course) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String hqlStr = "SELECT e FROM Course e WHERE e.department = :department AND e.catalogNumber = :catalogNumber";
        TypedQuery<Course> matchingCourseQuery = session.createQuery(hqlStr, Course.class);
        matchingCourseQuery.setParameter("department", course.getDepartment());
        matchingCourseQuery.setParameter("catalogNumber", course.getCatalogNumber());
        return matchingCourseQuery.getSingleResult().getId();
    }

    public void addReviewForCourse(Review review) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        session.persist(review);
        session.getTransaction().commit();
    }

    public boolean studentAlreadyReviewedCourse(Student student, Course course) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }
        String hqlStr = "SELECT e FROM Review e WHERE e.student = :student AND e.course = :course";
        TypedQuery<Review> matchingReviewQuery = session.createQuery(hqlStr, Review.class);
        matchingReviewQuery.setParameter("student", student);
        matchingReviewQuery.setParameter("course", course);
        List<Review> matchReviewList = matchingReviewQuery.getResultList();
        return matchReviewList.size() != 0;
    }

    public List<Review> getCourseReviews(Course course) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("ERROR: The database manager session is not open.");
        }

        List<Review> reviewList = new ArrayList<>();
        if (!courseExists(course)) {
            return reviewList;
        }

        course.setId(getCourseID(course));

        String reviewStr = "SELECT e FROM Review e WHERE e.course = :course";
        TypedQuery<Review> courseReviewQuery = session.createQuery(reviewStr, Review.class);
        courseReviewQuery.setParameter("course", course);
        reviewList = courseReviewQuery.getResultList();
        return reviewList;
    }
}
