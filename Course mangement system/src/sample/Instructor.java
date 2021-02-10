package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Instructor extends Application {
    Stage window;
    Scene scene1, scene2;
    Button back, add;
    Pane pane;
    ComboBox comboBoxInstructor;
    TableView moduleTable, studentTable;
    TextField marks;
    ArrayList<InstructorModel> instructors = new ArrayList<>();
    ArrayList<ModuleModel> modules = new ArrayList<>();
    ArrayList<StudentModuleModel> studentModules = new ArrayList<>();
    ArrayList<StudentModel> students = new ArrayList<>();
    ArrayList<String> availableStds = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("instructor.fxml"));
        primaryStage.setTitle("Instructor Panel");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        back = (Button) thisScene.lookup("#btnBack");
        moduleTable = (TableView) thisScene.lookup("#insModuleTable");
        studentTable = (TableView) thisScene.lookup("#insStudentTable");
        comboBoxInstructor = (ComboBox) thisScene.lookup("#comboBoxIns");
        pane = (Pane) thisScene.lookup("#insPane");
        marks = (TextField) thisScene.lookup("#textFieldMarks");
        add = (Button) thisScene.lookup("#btnAddIns");

        TableColumn inName = new TableColumn("Module Name");
        inName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inName.setMinWidth(120);
        moduleTable.getColumns().addAll(inName);

        TableColumn sName = new TableColumn("Student");
        TableColumn sPhone = new TableColumn("Phone");
        sName.setCellValueFactory(new PropertyValueFactory<>("name"));
        sPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        sName.setMinWidth(120);
        sPhone.setMinWidth(120);
        studentTable.getColumns().addAll(sName,sPhone);

        final ObservableList<TablePosition> selectedCells2 = studentTable.getSelectionModel().getSelectedCells();
        selectedCells2.addListener((ListChangeListener<TablePosition>) change -> {
            if (studentTable.getSelectionModel().getSelectedItems()!=null){
                StudentModel tModel = (StudentModel) studentTable.getSelectionModel().getSelectedItem();
                if (tModel!=null){
                    Holder.tempStudent = tModel.getName();
                    pane.setVisible(true);
                }
            }
        });

        final ObservableList<TablePosition> selectedCells = moduleTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<TablePosition>) change -> {
            if (moduleTable.getSelectionModel().getSelectedItems()!=null){
                ModuleModel tModel = (ModuleModel) moduleTable.getSelectionModel().getSelectedItem();
                if (tModel!=null){
                    Holder.tempModule = tModel.getName();
                    Holder.id=moduleTable.getSelectionModel().getFocusedIndex();
                    for (int i=0; i<studentModules.size();i++){
                        if (studentModules.get(i).getModule().equals(tModel.getName())){
                            if (!availableStds.contains(studentModules.get(i).getName())){
                                availableStds.add(studentModules.get(i).getName());
                            }
                        }
                    }
                    showStudentTable();
//                    add.setText("Edit Module");
//                    deleteBtn.setVisible(true);
                }
            }
        });

        add.setOnAction(actionEvent -> {
            String mark = marks.getText().trim();
            if (mark.isEmpty()){
                displayError("Enter All Data");
            }
            else {
                try {
                    File ins = new File("marks.txt");
                    if (ins.createNewFile()) {
                        System.out.println("File created: " + ins.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                    writeToFile(Holder.tempStudent+","+Holder.tempModule+","+mark);
                    clear();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        readFromFile();

        comboBoxInstructor.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if (t1!=null){
                moduleTable.getItems().clear();
                studentTable.getItems().clear();
                String instName = t1.toString();
                for (int i=0; i<modules.size();i++){
                    if (modules.get(i).getInstructor().equals(instName)){
                        moduleTable.getItems().add(modules.get(i));
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

    private void clear() {
        pane.setVisible(false);
        marks.setText("");
    }

    private void writeToFile(String s) {
        try{
            FileWriter myWriter = new FileWriter("marks.txt",true);
            myWriter.write(s+"\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showStudentTable(){
        studentTable.setVisible(true);
        studentTable.getItems().clear();
        for (int i=0;i<students.size();i++){
            for (int j=0; j<availableStds.size();j++){
                if (students.get(i).getName().equals(availableStds.get(j))){
                    studentTable.getItems().add(students.get(i));
                }
            }
        }
    }

    private void readFromFile() {
        try {
            File ins = new File("instructors.txt");
            Scanner iReader = new Scanner(ins);
            while (iReader.hasNextLine()) {
                String data = iReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    InstructorModel model = new InstructorModel(values[0],values[1],values[2]);
                    instructors.add(model);
                    comboBoxInstructor.getItems().add(model.getName());
                }
            }
//            comboBoxInstructor.getSelectionModel().selectFirst();
            iReader.close();

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

            File mou = new File("modules.txt");
            Scanner mReader = new Scanner(mou);
            while (mReader.hasNextLine()) {
                String data = mReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    ModuleModel model = new ModuleModel(values[0],values[1],values[2],values[3]);
                    modules.add(model);
                }
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
                }
            }
//            studentComboBox.getSelectionModel().selectFirst();
            myReader.close();

        }
        catch (IOException e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
