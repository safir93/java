package sample;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class AddCourse extends Application {
    Stage window;
    TableView tab;
    Scene scene1, scene2;
    Button back,deleteBtn, add;
    TextField courseText;
    ArrayList<String> courses = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("addCourse.fxml"));
        primaryStage.setTitle("Add New Course");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        tab = (TableView) thisScene.lookup("#coursesTable");
        TableColumn courseC = new TableColumn("Course");
        courseC.setCellValueFactory(new PropertyValueFactory<>("course"));
        tab.getColumns().addAll(courseC);

        readFromFile();

        back = (Button) thisScene.lookup("#btnBack");
        deleteBtn = (Button) thisScene.lookup("#btnDeleteCourse");
        back.setOnAction(actionEvent -> {
            Admin main = new Admin();
            try {
                main.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        courseText  = (TextField) thisScene.lookup("#textCourseName");
        add = (Button) thisScene.lookup("#btnAddCourse");
        add.setOnAction(actionEvent -> {
            String course = courseText.getText().trim();
            if (add.getText().equals("Edit Course")){
                if (course.isEmpty()) {
                    displayError("Enter Course Name");
                }
                else {
                    courses.remove(Holder.id);
                    courses.add(Holder.id,course);
                    writeToFile(null,courses);
                    removeAll();
                }

            }
            else {
                //get course
                if (course.isEmpty()) {
                    displayError("Enter Course Name");
                }
                else{
                    //do stuff
                    try {
                        File courses = new File("courses.txt");
                        if (courses.createNewFile()) {
                            System.out.println("File created: " + courses.getName());
                        } else {
                            System.out.println("File already exists.");
                        }
                        writeToFile(course,null);
                        CourseModel newCourse = new CourseModel(course);
                        tab.getItems().add(newCourse);
                        courseText.setText("");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        final ObservableList<TablePosition> selectedCells = tab.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> change) {
                for (TablePosition pos : selectedCells) {
                    String value = (String) pos.getTableColumn().getCellObservableValue(tab.getItems().get(pos.getRow())).getValue();
                    if (value!=null){
                        courseText.setText(value);
                        Holder.id = pos.getRow();
                        add.setText("Edit Course");
                        deleteBtn.setVisible(true);
                    }
                }
            }
        });

        Button clearBtn = (Button) thisScene.lookup("#btnClearCourse");
        clearBtn.setOnAction(actionEvent -> {
            clearAll();
        });

        deleteBtn.setOnAction(actionEvent -> {
            for (int i=0; i<courses.size();i++){
                String text = courseText.getText().trim();
                if (text.equals(courses.get(i))){
                    courses.remove(Holder.id);
                    writeToFile(null,courses);
                    removeAll();
                    clearAll();
                }

            }
        });

    }

    public void removeAll(){
        tab.getItems().clear();
        for (int i=0; i<courses.size();i++){
            CourseModel newCourse = new CourseModel(courses.get(i));
            tab.getItems().add(newCourse);
        }
    }

    public void clearAll(){
        add.setText("Add Course");
        courseText.setText("");
        deleteBtn.setVisible(false);
    }

    public void writeToFile(String course, ArrayList<String> list){
        if (list==null){
            try {
                FileWriter myWriter = new FileWriter("courses.txt",true);
                myWriter.write(course+"\n");
                myWriter.close();
                courses.add(course);
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else {
            try {
                FileWriter myWriter = new FileWriter("courses.txt");
                for(int i=0; i<list.size();i++){
                    myWriter.write(list.get(i)+"\n");
                }
                myWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromFile(){
        try {
            File myObj = new File("courses.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data!=null){
                    courses.add(data);
                    CourseModel m = new CourseModel(data);
                    tab.getItems().add(m);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void displayError(String error){
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
