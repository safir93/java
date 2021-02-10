package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Student extends Application {
    Stage window;
    Scene scene1, scene2;
    TextField nameTextField, phoneTextField, addressTextField, seventhTextField, eighthTextField;
    Button back, add, addModule;
    Pane pane;
    TableView tab;
    ArrayList<StudentModel> students = new ArrayList<>();
    ArrayList<CourseModel> cCourses = new ArrayList<>();
    ArrayList<ModuleModel> mModules = new ArrayList<>();
    ArrayList<StudentModuleModel> studentModules = new ArrayList<>();
    ComboBox levelComboBox, studentComboBox, courseComboBox, comboBoxModule1,comboBoxModule2,comboBoxModule3,comboBoxModule4,comboBoxModule5,comboBoxModule6,comboBoxModule7,comboBoxModule8;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("student.fxml"));
        primaryStage.setTitle("Student");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        nameTextField = (TextField) thisScene.lookup("#textStudentName");
        phoneTextField = (TextField) thisScene.lookup("#textStudentPhone");
        addressTextField = (TextField) thisScene.lookup("#textStudentAddress");
        studentComboBox = (ComboBox) thisScene.lookup("#comboBoxStudents");
        levelComboBox = (ComboBox) thisScene.lookup("#comboBoxStudentLevel");
        courseComboBox = (ComboBox) thisScene.lookup("#comboBoxCourse");
        comboBoxModule1 = (ComboBox) thisScene.lookup("#comboBoxModule1");
        comboBoxModule2 = (ComboBox) thisScene.lookup("#comboBoxModule2");
        comboBoxModule3 = (ComboBox) thisScene.lookup("#comboBoxModule3");
        comboBoxModule4 = (ComboBox) thisScene.lookup("#comboBoxModule4");
        comboBoxModule5 = (ComboBox) thisScene.lookup("#comboBoxModule5");
        comboBoxModule6 = (ComboBox) thisScene.lookup("#comboBoxModule6");
        comboBoxModule7 = (ComboBox) thisScene.lookup("#comboBoxModule7");
        tab = (TableView) thisScene.lookup("#tableStudentModules");
        comboBoxModule8 = (ComboBox) thisScene.lookup("#comboBoxModule8");
        seventhTextField = (TextField) thisScene.lookup("#textSeventhModule");
        eighthTextField = (TextField) thisScene.lookup("#textEighthModule");

        TableColumn inName = new TableColumn("Module Name");
        TableColumn inCode = new TableColumn("Code");
        TableColumn inInstructor = new TableColumn("Instructor");
        TableColumn inCourse = new TableColumn("Course");
        inName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        inInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        inCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        tab.getColumns().addAll(inName,inCode,inInstructor,inCourse);

        pane = (Pane) thisScene.lookup("#pane");
        levelComboBox.getItems().addAll("4","5","6");
        levelComboBox.getSelectionModel().selectFirst();
//        deleteBtn = (Button) thisScene.lookup("#btnDeleteModule");
//        clearBtn = (Button) thisScene.lookup("#btnClearModule");
        add = (Button) thisScene.lookup("#btnEnroll");
        addModule = (Button) thisScene.lookup("#btnAdd");
        back = (Button) thisScene.lookup("#btnBack");

        readFromFile();


        addModule.setOnAction(actionEvent -> {
            String studentName = studentComboBox.getValue().toString();
            String module1 = comboBoxModule1.getValue().toString();
            String module2 = comboBoxModule2.getValue().toString();
            String module3 = comboBoxModule3.getValue().toString();
            String module4 = comboBoxModule4.getValue().toString();
            String module5 = comboBoxModule5.getValue().toString();
            String module6 = comboBoxModule6.getValue().toString();
            String module7="";
            String module8="";
            if (Holder.level.equals("6")){
                module7 = seventhTextField.getText().trim();
                module8 = eighthTextField.getText().trim();
            }
            else {
                module7 = comboBoxModule7.getValue().toString();
                module8 = comboBoxModule8.getValue().toString();
            }

            try {
                File ins = new File("studentModules.txt");
                if (ins.createNewFile()) {
                    System.out.println("File created: " + ins.getName());
                } else {
                    System.out.println("File already exists.");
                }
                StudentModuleModel model = new StudentModuleModel(studentName,module1);
                writeToSFile(studentName+","+module1,model);
                StudentModuleModel model2 = new StudentModuleModel(studentName,module2);
                writeToSFile(studentName+","+module2,model);
                StudentModuleModel model3 = new StudentModuleModel(studentName,module3);
                writeToSFile(studentName+","+module3,model);
                StudentModuleModel model4 = new StudentModuleModel(studentName,module4);
                writeToSFile(studentName+","+module4,model);
                StudentModuleModel model5 = new StudentModuleModel(studentName,module5);
                writeToSFile(studentName+","+module5,model);
                StudentModuleModel model6 = new StudentModuleModel(studentName,module6);
                writeToSFile(studentName+","+module6,model);
                StudentModuleModel model7 = new StudentModuleModel(studentName,module7);
                writeToSFile(studentName+","+module7,model);
                StudentModuleModel model8 = new StudentModuleModel(studentName,module8);
                writeToSFile(studentName+","+module8,model);
                clear();
                tab.getItems().clear();


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        studentComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if (t1!=null){
                String studentName = t1.toString();
                String level = "";
                for (int i=0; i<students.size();i++){
                    if (students.get(i).getName().equals(studentName)){
                        level = students.get(i).getLevel();
                        Holder.level = level;
                        break;
                    }
                }
                boolean contains = false;
                for (int i=0;i<studentModules.size();i++){
                    if (studentModules.get(i).getName().equals(studentName)){
                        System.out.println(studentModules.get(i).getModule());
                        for (int j=0;j<mModules.size();j++){
                            System.out.println(mModules.get(j).getName());
                            if (mModules.get(j).getName().equals(studentModules.get(i).getModule())){
                                contains = true;
                            }
                        }
                    }
                }
                if (contains){
                    tab.setVisible(true);
                    addInTable(studentName);
                    pane.setVisible(false);
                }
                else {
                    pane.setVisible(true);
                    tab.setVisible(false);
                    tab.getItems().clear();
                    if (level.equals("6")){
                        //change
                        comboBoxModule7.setVisible(false);
                        comboBoxModule8.setVisible(false);
                        seventhTextField.setVisible(true);
                        eighthTextField.setVisible(true);
                    }
                    else {
                        comboBoxModule7.setVisible(true);
                        comboBoxModule8.setVisible(true);
                        seventhTextField.setVisible(false);
                        eighthTextField.setVisible(false);
                    }
                }

            }
        });

        back.setOnAction(actionEvent -> {
            Main main = new Main();
            try {
                main.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        add.setOnAction(actionEvent -> {
            String name = nameTextField.getText().trim();
            String address = addressTextField.getText().trim();
            String phone = phoneTextField.getText().trim();
            String level = levelComboBox.getValue().toString();
            String course = courseComboBox.getValue().toString();
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()){
                displayError("Enter All Data");
            }
            else {
                try {
                    File ins = new File("students.txt");
                    if (ins.createNewFile()) {
                        System.out.println("File created: " + ins.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                    StudentModel model = new StudentModel(name,address,phone,level,course);
                    writeToFile(name+","+address+","+phone+","+level+","+course,model);
                    studentComboBox.getItems().add(model.getName());
                    clear();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void writeToSFile(String s, StudentModuleModel model) {
        try{
            FileWriter myWriter = new FileWriter("studentModules.txt",true);
            myWriter.write(s+"\n");
            myWriter.close();
            studentModules.add(model);
            System.out.println("Successfully wrote to the file.");
            addInTable(model.getName());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addInTable(String thisStudent) {
        tab.getItems().clear();
        for (int i=0; i<studentModules.size();i++){
            if (studentModules.get(i).getName().equals(thisStudent)){
                for (int j=0; j<mModules.size();j++){
                    if (mModules.get(j).getName().equals(studentModules.get(i).getModule())){
                        tab.getItems().add(mModules.get(j));
                    }
                }
            }
        }
    }

    private void readFromFile() {
        try {
            File sm = new File("studentModules.txt");
            Scanner smReader = new Scanner(sm);
            while (smReader.hasNextLine()) {
                String data = smReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    StudentModuleModel model = new StudentModuleModel(values[0],values[1]);
                    studentModules.add(model);
                }
            }
            smReader.close();

            File cou = new File("courses.txt");
            Scanner cReader = new Scanner(cou);
            while (cReader.hasNextLine()) {
                String data = cReader.nextLine();
                if (data!=null){
                    CourseModel model = new CourseModel(data);
                    cCourses.add(model);
                    courseComboBox.getItems().add(data);
                }
            }
            courseComboBox.getSelectionModel().selectFirst();
            cReader.close();

            File mou = new File("modules.txt");
            Scanner mReader = new Scanner(mou);
            while (mReader.hasNextLine()) {
                String data = mReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    ModuleModel model = new ModuleModel(values[0],values[1],values[2],values[3]);
                    mModules.add(model);
                    comboBoxModule1.getItems().add(model.getName());
                    comboBoxModule2.getItems().add(model.getName());
                    comboBoxModule3.getItems().add(model.getName());
                    comboBoxModule4.getItems().add(model.getName());
                    comboBoxModule5.getItems().add(model.getName());
                    comboBoxModule6.getItems().add(model.getName());
                    comboBoxModule7.getItems().add(model.getName());
                    comboBoxModule8.getItems().add(model.getName());
                }
                comboBoxModule1.getSelectionModel().selectFirst();
                comboBoxModule2.getSelectionModel().selectFirst();
                comboBoxModule3.getSelectionModel().selectFirst();
                comboBoxModule4.getSelectionModel().selectFirst();
                comboBoxModule5.getSelectionModel().selectFirst();
                comboBoxModule6.getSelectionModel().selectFirst();
                comboBoxModule7.getSelectionModel().selectFirst();
                comboBoxModule8.getSelectionModel().selectFirst();
            }
            mReader.close();

            File myObj = new File("students.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    StudentModel model = new StudentModel(values[0],values[1],values[2],values[3],values[4]);
                    students.add(model);
                    studentComboBox.getItems().add(model.getName());
                }
            }
//            studentComboBox.getSelectionModel().selectFirst();
            myReader.close();



        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void clear() {
        nameTextField.setText("");
        addressTextField.setText("");
        phoneTextField.setText("");
        levelComboBox.getSelectionModel().selectFirst();
        courseComboBox.getSelectionModel().selectFirst();
        comboBoxModule1.getSelectionModel().selectFirst();
        comboBoxModule2.getSelectionModel().selectFirst();
        comboBoxModule3.getSelectionModel().selectFirst();
        comboBoxModule4.getSelectionModel().selectFirst();
        comboBoxModule5.getSelectionModel().selectFirst();
        comboBoxModule6.getSelectionModel().selectFirst();
        comboBoxModule7.getSelectionModel().selectFirst();
        comboBoxModule8.getSelectionModel().selectFirst();
        seventhTextField.setText("");
        eighthTextField.setText("");
    }

    private void writeToFile(String s, StudentModel model) {
        try{
            FileWriter myWriter = new FileWriter("students.txt",true);
            myWriter.write(s+"\n");
            myWriter.close();
            students.add(model);
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void displayError(String error) {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("Error");
        Label l3 = new Label(error);

        VBox layout2 = new VBox(20);
        layout2.getChildren().addAll(l3);
        layout2.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(layout2,150,75);
        newStage.setScene(newScene);
        newStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
