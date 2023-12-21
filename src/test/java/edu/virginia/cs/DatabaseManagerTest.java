package edu.virginia.cs;

import org.hibernate.*;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

class DatabaseManagerTest {
    DatabaseManager testManager;
    Session mockSession;
    Transaction mockTransaction;
    Student mockStudent;
    Course mockCourse;
    Review mockReview;
    Query<Student> mockStudentQuery;
    Query<Course> mockCourseQuery;
    Query<Review> mockReviewQuery;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        mockSession = mock(Session.class);
        mockTransaction = mock(Transaction.class);
        mockStudent = mock(Student.class);
        mockCourse = mock(Course.class);
        mockReview = mock(Review.class);
        mockStudentQuery = mock(Query.class);
        mockCourseQuery = mock(Query.class);
        mockReviewQuery = mock(Query.class);
        testManager = new DatabaseManager(mockSession);
        when(mockSession.isOpen()).thenReturn(true); //for session checks
        when(mockSession.getTransaction()).thenReturn(mockTransaction); //for commits
        testManager.startTransaction();
    }

    @AfterEach
    public void tearDown() {
        testManager.endTransaction();
    }

    @Test
    public void testAddStudent() {
        testManager.addStudent(mockStudent);
        verify(mockSession).persist(mockStudent);
        verify(mockTransaction).commit();
    }

    //CITATION: https://www.digitalocean.com/community/tutorials/mockito-argument-matchers-any-eq
    //DESCRIPTION: Must use eq() if you're using an argument matcher (i.e. any(), etc.) alongside a specific value (i.e. Student.class)
    @Test
    public void testStudentExists() {
        when(mockSession.createQuery(anyString(), eq(Student.class))).thenReturn(mockStudentQuery);
        when(mockStudentQuery.setParameter(anyString(), any())).thenReturn(mockStudentQuery);
        when(mockStudent.getUsername()).thenReturn("kvc6dv"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudentQuery.getResultList()).thenReturn(List.of(mockStudent));

        assertTrue(testManager.studentExists(mockStudent));
        verify(mockSession).createQuery(anyString(), eq(Student.class));
        verify(mockStudentQuery).setParameter(anyString(), any());
        verify(mockStudent).getUsername();
        verify(mockStudentQuery).getResultList();
    }

    @Test
    public void testStudentExists_False() {
        when(mockSession.createQuery(anyString(), eq(Student.class))).thenReturn(mockStudentQuery);
        when(mockStudentQuery.setParameter(anyString(), any())).thenReturn(mockStudentQuery);
        when(mockStudent.getUsername()).thenReturn("kvc6dv"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudentQuery.getResultList()).thenReturn(List.of());

        assertFalse(testManager.studentExists(mockStudent));
        verify(mockSession).createQuery(anyString(), eq(Student.class));
        verify(mockStudentQuery).setParameter(anyString(), any());
        verify(mockStudent).getUsername();
        verify(mockStudentQuery).getResultList();
    }

    @Test
    public void testStudentHasPasswordMismatch() {
        when(mockSession.createQuery(anyString(), eq(Student.class))).thenReturn(mockStudentQuery);
        when(mockStudentQuery.setParameter(anyString(), any())).thenReturn(mockStudentQuery);
        when(mockStudent.getUsername()).thenReturn("kvc6dv"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudent.getPassword()).thenReturn("123"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudentQuery.getResultList()).thenReturn(new ArrayList<>());

        assertTrue(testManager.studentHasPasswordMismatch(mockStudent));
        verify(mockSession).createQuery(anyString(), eq(Student.class));
        verify(mockStudentQuery).setParameter(anyString(), eq("kvc6dv"));
        verify(mockStudentQuery).setParameter(anyString(), eq("123"));
        verify(mockStudent).getUsername();
        verify(mockStudent).getPassword();
        verify(mockStudentQuery).getResultList();
    }

    @Test
    public void testStudentHasPasswordMismatch_False() {
        when(mockSession.createQuery(anyString(), eq(Student.class))).thenReturn(mockStudentQuery);
        when(mockStudentQuery.setParameter(anyString(), any())).thenReturn(mockStudentQuery);
        when(mockStudent.getUsername()).thenReturn("kvc6dv"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudent.getPassword()).thenReturn("123"); //Must be assigned an actual value since it's used in setParameter() in place of any()
        when(mockStudentQuery.getResultList()).thenReturn(List.of(mockStudent));

        assertFalse(testManager.studentHasPasswordMismatch(mockStudent));
        verify(mockSession).createQuery(anyString(), eq(Student.class));
        verify(mockStudentQuery).setParameter(anyString(), eq("kvc6dv"));
        verify(mockStudentQuery).setParameter(anyString(), eq("123"));
        verify(mockStudent).getUsername();
        verify(mockStudent).getPassword();
        verify(mockStudentQuery).getResultList();
    }

    @Test
    public void testGetStudentID() {
        when(mockSession.createQuery(anyString(), eq(Student.class))).thenReturn(mockStudentQuery);
        when(mockStudentQuery.setParameter(anyString(), any())).thenReturn(mockStudentQuery);
        when(mockStudentQuery.getSingleResult()).thenReturn(mockStudent);
        when(mockStudent.getId()).thenReturn(11);

        assertEquals(11, testManager.getStudentID("jmc7rn"));
        verify(mockSession).createQuery(anyString(), eq(Student.class));
        verify(mockStudentQuery).setParameter(anyString(), any());
        verify(mockStudentQuery).getSingleResult();
        verify(mockStudent).getId();
    }

    @Test
    public void testAddCourse() {
        testManager.addCourse(mockCourse);
        verify(mockSession).persist(mockCourse);
        verify(mockTransaction).commit();
    }

    @Test
    public void testCourseExists() {
        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getResultList()).thenReturn(List.of(mockCourse));

        assertTrue(testManager.courseExists(mockCourse));
        verify(mockSession).createQuery(anyString(), eq(Course.class));
        verify(mockCourseQuery).setParameter(anyString(), eq("CS"));
        verify(mockCourseQuery).setParameter(anyString(), eq(3140));
        verify(mockCourse).getDepartment();
        verify(mockCourse).getCatalogNumber();
        verify(mockCourseQuery).getResultList();
    }

    @Test
    public void testCourseExists_False() {
        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getResultList()).thenReturn(new ArrayList<>());

        assertFalse(testManager.courseExists(mockCourse));
        verify(mockSession).createQuery(anyString(), eq(Course.class));
        verify(mockCourseQuery).setParameter(anyString(), eq("CS"));
        verify(mockCourseQuery).setParameter(anyString(), eq(3140));
        verify(mockCourse).getDepartment();
        verify(mockCourse).getCatalogNumber();
        verify(mockCourseQuery).getResultList();
    }

    @Test
    public void testGetCourseID() {
        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getSingleResult()).thenReturn(mockCourse);
        when(mockCourse.getId()).thenReturn(1);

        assertEquals(1, testManager.getCourseID(mockCourse));
        verify(mockSession).createQuery(anyString(), eq(Course.class));
        verify(mockCourseQuery).setParameter(anyString(), eq("CS"));
        verify(mockCourseQuery).setParameter(anyString(), eq(3140));
        verify(mockCourse).getDepartment();
        verify(mockCourse).getCatalogNumber();
        verify(mockCourseQuery).getSingleResult();
        verify(mockCourse).getId();
    }

    @Test
    public void testAddReviewForCourse() {
        testManager.addReviewForCourse(mockReview);
        verify(mockSession).persist(mockReview);
        verify(mockTransaction).commit();
    }

    @Test
    public void testStudentAlreadyReviewedCourse() {
        when(mockSession.createQuery(anyString(), eq(Review.class))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.setParameter(anyString(), eq(mockStudent))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.setParameter(anyString(), eq(mockCourse))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.getResultList()).thenReturn(List.of(mockReview));

        assertTrue(testManager.studentAlreadyReviewedCourse(mockStudent, mockCourse));
        verify(mockSession).createQuery(anyString(), eq(Review.class));
        verify(mockReviewQuery).setParameter(anyString(), eq(mockStudent));
        verify(mockReviewQuery).setParameter(anyString(), eq(mockCourse));
        verify(mockReviewQuery).getResultList();
    }

    @Test
    public void testStudentAlreadyReviewedCourse_False() {
        when(mockSession.createQuery(anyString(), eq(Review.class))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.setParameter(anyString(), eq(mockStudent))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.setParameter(anyString(), eq(mockCourse))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.getResultList()).thenReturn(new ArrayList<>());

        assertFalse(testManager.studentAlreadyReviewedCourse(mockStudent, mockCourse));
        verify(mockSession).createQuery(anyString(), eq(Review.class));
        verify(mockReviewQuery).setParameter(anyString(), eq(mockStudent));
        verify(mockReviewQuery).setParameter(anyString(), eq(mockCourse));
        verify(mockReviewQuery).getResultList();
    }

    @Test
    public void testGetCourseReviews() {
        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getResultList()).thenReturn(List.of(mockCourse));

        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getSingleResult()).thenReturn(mockCourse);
        when(mockCourse.getId()).thenReturn(1);

        when(mockSession.createQuery(anyString(), eq(Review.class))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.setParameter(anyString(), eq(mockCourse))).thenReturn(mockReviewQuery);
        when(mockReviewQuery.getResultList()).thenReturn(List.of(mockReview));

        assertEquals(1, testManager.getCourseReviews(mockCourse).size());
        verify(mockSession, atLeast(1)).createQuery(anyString(), eq(Course.class));
        verify(mockSession, atLeast(1)).createQuery(anyString(), eq(Review.class));
        verify(mockReviewQuery).setParameter(anyString(), eq(mockCourse));
        verify(mockReviewQuery, atLeast(1)).getResultList();
    }

    @Test
    public void testGetCourseReviews_NoCourseReviews() {
        when(mockSession.createQuery(anyString(), eq(Course.class))).thenReturn(mockCourseQuery);
        when(mockCourseQuery.setParameter(anyString(), any())).thenReturn(mockCourseQuery);
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(mockCourseQuery.getResultList()).thenReturn(new ArrayList<>());

        assertEquals(0, testManager.getCourseReviews(mockCourse).size());
        verify(mockSession, atLeast(1)).createQuery(anyString(), eq(Course.class));
        verify(mockCourseQuery, atLeast(1)).getResultList();
    }

}