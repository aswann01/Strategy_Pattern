package com.company;

import java.util.*;
// This is an example of the strategy pattern for CSC3250
class Course {
    private String _cnum;
    private int _credits;

    public Course() {
    }

    public Course(String num, int cred) {
        _cnum = num;
        _credits = cred;
    }

    public void setNumber(String num) {
        _cnum = num;
    }

    public void setCredits(int cred) {
        _credits = cred;
    }

    public String getNumber() {
        return _cnum;
    }

    public int getCredits() {
        return _credits;
    }

    public String toString() {
        return _cnum + " " + _credits;
    }
}

class Student {
    private String _sid;

    public Student() {
    }

    public Student(String d) {
        _sid = d;
    }

    public void setID(String id) {
        _sid = id;
    }

    public String getID() {
        return _sid;
    }

    public String toString() {
        return _sid;
    }
}

interface SearchBehavior<T, S> {
    // T is the object, S is the value
    boolean search(T obj, S v); // doesnt matter public or private in interface
}

class StudentSearch implements SearchBehavior<Student, String> {
    @Override
    public boolean search(Student obj, String v) {
        return obj.getID().equals(v);
    }
}

class CourseSearch implements SearchBehavior<Course, String> {
    @Override
    public boolean search(Course obj, String v) {
        return obj.getNumber().equals(v);
    }
}

class CnumSearch implements SearchBehavior<String, String>{
    @Override
    public boolean search(String obj, String v) {
        return obj.equals(v);         // replaces the "if" in a search
    }
}

class AllItems<T> {
    private ArrayList<T> _items;
    public AllItems() {
        _items = new ArrayList<T>();
    }
    public AllItems(int i){_items = new ArrayList<T>(i);}
    public void addItem(T t) {
        _items.add(t);
    }
    public <S> int findItem(S v, SearchBehavior<T, S> sb) {
        for (int i =0; i< _items.size(); i++){
            if (sb.search(_items.get(i), v)) {
                return i;
            }
        }
        return -1;
    }
    public void removeItem(int i){
        if (i>= 0 && i < _items.size())
            _items.remove(i);
    }
    public int size() {
        return _items.size();
    }
    public T getItem(int i) {
        return _items.get(i);
    }

}

class AllStudents {
    private AllItems<Student> _students;

    public AllStudents() {
        _students = new AllItems<Student>();
    }
    public AllStudents(int i){_students = new AllItems<Student>(i);}
    public void addStudent(String id) {
        _students.addItem(new Student(id));
    }
    public boolean isStudent(String id) {
        if (_students.findItem(id, new StudentSearch()) == -1)
            return false;
        else
            return true;
    }
    public int findStudent(String id) {
        return _students.findItem(id, new StudentSearch());
    }
    public void removeStudent(String id) {
        int i = findStudent(id);
        _students.removeItem(i);
    }
    public boolean modifyStudentID(String oldID, String newID){
        int i = findStudent(oldID);
        if (i < 0)
            return false;
        else{
            _students.getItem(i).setID(newID);
            return true;
        }
    }
    public int size(){return _students.size();}
    public String toString() {
        String s = "Students:\n";
        for (int i = 0; i < _students.size(); i++)
            s += (_students.getItem(i).toString() + "\n");
        return s;
    }
}

class AllCourses {
    private AllItems<Course> _courses;

    public AllCourses() {
        _courses = new AllItems<Course>();
    }
    public AllCourses(int i){_courses = new AllItems<Course>(i);}
    public void addCourse(String cnum, int c) {
        _courses.addItem(new Course(cnum, c));
    }
    public boolean isCourse(String cnum) {
        if (_courses.findItem(cnum, new CourseSearch()) == -1)
            return false;
        else
            return true;
    }
    public int findCourse(String cnum) {
        return _courses.findItem(cnum, new CourseSearch());
    }
    public void removeCourse(String cnum) {
        int i = _courses.findItem(cnum, new CourseSearch());
        _courses.removeItem(i);
    }
    public boolean modifyCourseNum(String oldNum, String newNum){
        int i = findCourse(oldNum);
        if (i < 0)
            return false;
        else{
            _courses.getItem(i).setNumber(newNum);
            return true;
        }
    }
    public int size(){return _courses.size();}
    public String toString() {
        String s = "Courses:\n";
        for (int i = 0; i < _courses.size(); i++)
            s += (_courses.getItem(i).toString() + "\n");
        return s;
    }
}

class Enrollment{
    private HashMap<String, AllItems<String>> _enroll;
    public Enrollment(){
        _enroll = new HashMap<String, AllItems<String>>();
    }
    public boolean dropStudentFromCourse(String id, String cnum){
        // drops the course from the student's set of courses
        // if no other courses exist for student, drop student from hashmap
        AllItems<String> t = _enroll.get(id);
        int i = t.findItem(cnum, new CnumSearch());
        if (i == -1)
            return false;
        t.removeItem(i);
        if (t.size() == 0)
            _enroll.remove(id);
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        AllStudents as = new AllStudents();
        AllCourses ac = new AllCourses();
        as.addStudent("100");
        as.addStudent("200");
        as.addStudent("300");
        ac.addCourse("CSC3250", 4);
        ac.addCourse("CSC1700", 4);
        ac.addCourse("MTH3270", 4);
        System.out.println(as);
        System.out.println(ac);
        System.out.println("Is Student 300: " + as.isStudent("300"));
        System.out.println("Find Student 300: " + as.findStudent("300"));
        System.out.println("Is Course CSC3250: " + ac.isCourse("CSC3250"));
        System.out.println("Find Course CSC3250: " + ac.findCourse("CSC3250"));
        as.removeStudent("300");
        System.out.println("\nAfter removing student 300");
        as.removeStudent("300");
        System.out.println(as);
        System.out.println("Is Student 300: " + as.isStudent("300"));
        System.out.println("Find Student 300: " + as.findStudent("300"));
        System.out.println("\nAfter removing course 3250");
        ac.removeCourse("CSC3250");
        System.out.println(ac);
        System.out.println("Is Course CSC3250: " + ac.isCourse("CSC3250"));
        System.out.println("Find Course CSC3250: " + ac.findCourse("CSC3250"));
    }
}
