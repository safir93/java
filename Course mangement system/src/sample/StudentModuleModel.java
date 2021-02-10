package sample;

import javafx.beans.property.SimpleStringProperty;

public class StudentModuleModel {
    private SimpleStringProperty name,module;

    public StudentModuleModel(String  name, String  module) {
        this.name = new SimpleStringProperty(name);
        this.module = new SimpleStringProperty(module);
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
}
