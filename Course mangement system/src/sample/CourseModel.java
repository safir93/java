package sample;

import javafx.beans.property.SimpleStringProperty;

public class CourseModel {

    private SimpleStringProperty course;

    public CourseModel(String course) {
        this.course = new SimpleStringProperty(course);
    }

    public String getCourse() {
        return course.get();
    }

    public SimpleStringProperty courseProperty() {
        return course;
    }

    public void setCourse(String course) {
        this.course.set(course);
    }
}
