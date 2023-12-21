package edu.virginia.cs;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    @ManyToOne
    @JoinColumn(name = "StudentID", referencedColumnName = "ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "CourseID", referencedColumnName = "ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;
    @Column(name = "Comment", nullable = false)
    private String comment;
    @Column(name = "Rating", nullable = false)
    @Check(constraints = "Rating >= 1 AND Rating <= 5")
    private int rating;

    public Review() {}

    public Review(Student student, Course course, String comment, int rating) {
        this.student = student;
        this.course = course;
        this.comment = comment;
        this.rating = rating;
    }

    public Review(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
