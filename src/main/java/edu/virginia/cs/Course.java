package edu.virginia.cs;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    @Column(name = "Department", nullable = false, length = 4)
    private String department;
    @Column(name = "CatalogNumber", nullable = false, length = 4)
    private int catalogNumber;
    @OneToMany(mappedBy = "course")
    private List<Review> reviewList;

    public Course() {}

    public Course(String department, int catalogNumber, List<Review> reviewList) {
        this.department = department;
        this.catalogNumber = catalogNumber;
        this.reviewList = reviewList;
    }

    public Course(String department, int catalogNumber) {
        this(department, catalogNumber, new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void addReviewToCourse(Review review) {
        this.reviewList.add(review);
    }

    public int getNumberOfCourseReviews() {
        return reviewList.size();
    }
}
