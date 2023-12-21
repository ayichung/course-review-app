package edu.virginia.cs;

import java.util.*;

public class CommandLineUI {
    private Scanner in;
    private Student user;
    private final DatabaseService service = new DatabaseService();
    private boolean loggedIn = false;

    public static void main(String[] args) {
        CommandLineUI curSession = new CommandLineUI();
        for (;;) {
            curSession.login();
            curSession.mainMenu();
        }
    }

    private void login() {
        initScanner();
        service.initDatabaseManager();
        while (!loggedIn) {
            System.out.println("""
                Welcome to the Course Review Application. Please enter a number to make a selection.
                (1) Login to existing user
                (2) Create new user""");
            String select = in.nextLine();
            while (!select.equals("1") && !select.equals("2")) {
                System.out.println("ERROR: You have made an invalid selection. Please enter a valid selection number (1) or (2).");
                select = in.nextLine();
            }
            if (select.equals("1")) {
                promptExistingUser();
            }
            else {
                promptNewUser();
            }
        }
    }

    private void mainMenu() {
        while (loggedIn) {
            System.out.println("""
                Welcome to the Main Menu. Please enter a number to make a selection.
                (1) Submit a review for a course
                (2) See reviews for a course
                (3) Log out""");
            String select = in.nextLine();
            while (!select.equals("1") && !select.equals("2") && !select.equals("3")) {
                System.out.println("ERROR: You have made an invalid selection. Please enter a valid selection number (1), (2), or (3).");
                select = in.nextLine();
            }
            if (select.equals("1")) {
                promptSubmitReview();
            } else if (select.equals("2")) {
                promptSeeReviews();
            } else {
                logOut();
            }
        }
    }

    private void initScanner() {
        in = new Scanner(System.in);
    }

    private void promptExistingUser() {
        System.out.print("Enter username: ");
        String username = in.nextLine();
        System.out.print("Enter password: ");
        String password = in.nextLine();
        Student supposedUser = new Student(username, password);
        if (!service.studentExistsInDatabase(supposedUser) || service.studentHasPasswordMismatchInDatabase(supposedUser)) {
            System.out.println("ERROR: You have provided an invalid username and password combination. You have been returned to Login.");
        } else {
            user = supposedUser;
            setUserId();
            loggedIn = true;
            System.out.println("You have been logged in!");
        }
    }

    private void promptNewUser() {
        System.out.print("Enter username: ");
        String username = in.nextLine();
        System.out.print("Enter password: ");
        String password = in.nextLine();
        System.out.print("Confirm password: ");
        String passwordConfirm = in.nextLine();
        if (!password.equals(passwordConfirm)) {
            System.out.println("ERROR: The provided passwords do not match. You have been returned to Login.");
        }
        else {
            user = new Student(username, password);
            createUser();
        }
    }

    private void createUser() {  // creates the full Student object with ID
        if (service.studentExistsInDatabase(user)) {
            System.out.println("ERROR: The given username is already in use. You have been returned to Login.");
        } else {
            service.addStudentToDatabase(user);
            setUserId();
            loggedIn = true;
            System.out.println("An account has been made for you, and you have been logged in!");
        }
    }

    private void setUserId() {
        int userId = service.getStudentIDFromDatabase(user);
        user.setId(userId);
    }

    private void promptSubmitReview() {
        String[] courseNameArray = promptCourseNameAsArray();
        String department = courseNameArray[0];
        String catalogNumber = courseNameArray[1];

        if (isInvalidCourseName(department, catalogNumber)) {
            System.out.println("ERROR: You have provided an invalid course name. Departments must be 2-4 capital letters, and catalog numbers must be 4 digits. You have been returned to the Main Menu.");
        }
        else {
            Course courseBeingReviewed = new Course(department, Integer.parseInt(catalogNumber));
            if (!service.courseExistsInDatabase(courseBeingReviewed)) {
                service.addCourseToDatabase(courseBeingReviewed);
            }
            int courseId = service.getCourseIDFromDatabase(courseBeingReviewed);
            courseBeingReviewed.setId(courseId);

            if (service.studentAlreadyReviewedCourseInDatabase(user, courseBeingReviewed)) {
                System.out.printf("""
                        ERROR: You have already added a review for %s %d. You can only submit one review per course.
                        You have been returned to the Main Menu.%n""", courseBeingReviewed.getDepartment(), courseBeingReviewed.getCatalogNumber());
            } else {
                Review userReview = promptAndGetReview(courseBeingReviewed);
                service.addReviewToDatabase(userReview);
                System.out.printf("""
                        You have successfully added a review for %s %d. You will now be returned to the Main Menu.
                        %n""", courseBeingReviewed.getDepartment(), courseBeingReviewed.getCatalogNumber());
            }
        }
    }

    private void promptSeeReviews() {
        String[] courseNameArray = promptCourseNameAsArray();
        String department = courseNameArray[0];
        String catalogNumber = courseNameArray[1];

        if (isInvalidCourseName(department, catalogNumber)) {
            System.out.println("ERROR: You have provided an invalid course name. Departments must be 2-4 capital letters, and catalog numbers must be 4 digits. You have been returned to the Main Menu.");
        }
        else {
            Course selectedCourse = new Course(department, Integer.parseInt(catalogNumber));
            List<Review> reviewList = service.getCourseReviewsInDatabase(selectedCourse);
            if (reviewList.isEmpty()) {
                System.out.printf("ERROR: The course %s %d has no reviews. You have been returned to the Main Menu.%n",
                        selectedCourse.getDepartment(), selectedCourse.getCatalogNumber());
            }
            else {
                double sumRating = 0;
                System.out.printf("Reviews for %s %d:%n", selectedCourse.getDepartment(), selectedCourse.getCatalogNumber());
                for (Review review : reviewList) {
                    System.out.printf("""
                            - "%s"
                            """, review.getComment());
                    sumRating += review.getRating();
                }
                double avgRating = sumRating / reviewList.size();
                System.out.printf("""
                    Course average: %.2f/5
                    All reviews have been displayed for %s %d. You will now be returned to the Main Menu.%n""",
                        avgRating, selectedCourse.getDepartment(), selectedCourse.getCatalogNumber());
            }
        }
    }

    private String[] promptCourseNameAsArray() {
        System.out.print("Enter course name: ");
        String courseName = in.nextLine();
        return courseName.split(" ");
    }

    private boolean isInvalidCourseName(String department, String catalogNumber) {
        return ((department.length() < 2 || department.length() > 4) || !department.matches(department.toUpperCase())) || !catalogNumber.matches("\\d{4}");
    }

    private Review promptAndGetReview(Course course) {
        System.out.print("Enter a comment regarding the course: ");
        String comment = in.nextLine();

        boolean validRating = false;
        Review userReview = null;
        while (!validRating) {
            System.out.print("Enter a rating from 1-5 for the course: ");
            try {
                int rating = Integer.parseInt(in.nextLine());
                while (rating < 1 || rating > 5) {
                    System.out.println("ERROR: You have provided an invalid rating. Please enter a valid rating from 1-5 for the course.");
                    rating = Integer.parseInt(in.nextLine());
                }
                validRating = true;
                userReview = new Review(user, course, comment, rating);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: You have provided a non-integer value for the rating (i.e. string, float, etc). Please enter a valid rating from 1-5 for the course.");
            }
        }
        return userReview;
    }

    private void logOut() {
        System.out.println("You have logged out. You will now be returned to Login.");
        loggedIn = false;
    }

}
