package sample;

import javafx.beans.property.SimpleStringProperty;

public class InstructorModel {
    private SimpleStringProperty name,address,phone;

    public InstructorModel(String name, String address, String phone) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);

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

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
}
