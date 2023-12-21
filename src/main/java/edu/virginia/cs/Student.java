package edu.virginia.cs;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    @Column(name = "Username", nullable = false, unique = true)
    private String username;
    @Column(name = "Password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "student")
    List<Review> reviewList;  // not sure if this is needed; delete this field and @ManyToOne tag in Review.java if not

    public Student() {}

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void addReviewToStudentReviews(Review review) {
        this.reviewList.add(review);
    }

    public int getNumberOfStudentReviews() {
        return reviewList.size();
    }
}
