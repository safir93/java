package sample;

import javafx.beans.property.SimpleStringProperty;

public class ModuleModel {
    private SimpleStringProperty name,code,instructor,course;

    public ModuleModel(String name, String code, String instructor, String course) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
        this.instructor = new SimpleStringProperty(instructor);
        this.course = new SimpleStringProperty(course);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getInstructor() {
        return instructor.get();
    }

    public SimpleStringProperty instructorProperty() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor.set(instructor);
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
