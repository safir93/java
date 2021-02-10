package sample;

import javafx.beans.property.SimpleStringProperty;

public class MarkModel {
    private SimpleStringProperty name,module,marks;

    public MarkModel(String  name, String module, String marks) {
        this.name = new SimpleStringProperty(name);
        this.module = new SimpleStringProperty(module);
        this.marks = new SimpleStringProperty(marks);
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

    public String getModule() {
        return module.get();
    }

    public SimpleStringProperty moduleProperty() {
        return module;
    }

    public void setModule(String module) {
        this.module.set(module);
    }

    public String getMarks() {
        return marks.get();
    }

    public SimpleStringProperty marksProperty() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks.set(marks);
    }
}
