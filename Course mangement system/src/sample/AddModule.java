package sample;

import javafx.application.Application;
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


public class AddModule extends Application {
    Stage window;
    Scene scene1, scene2;
    TableView tab;
    Button back,deleteBtn, add,clearBtn;
    ArrayList<ModuleModel> modules = new ArrayList<>();
    ArrayList<CourseModel> cCourses = new ArrayList<>();
    ArrayList<InstructorModel> cInstructors = new ArrayList<>();
    TextField nameTextField, codeTextField;
    ComboBox instructorComboBox, courseComboBox;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("module.fxml"));
        primaryStage.setTitle("Add Module");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        nameTextField = (TextField) thisScene.lookup("#textModuleName");
        codeTextField = (TextField) thisScene.lookup("#textModuleCode");
        courseComboBox = (ComboBox) thisScene.lookup("#comboBoxCourse");
        instructorComboBox = (ComboBox) thisScene.lookup("#comboBoxInstructor");
        deleteBtn = (Button) thisScene.lookup("#btnDeleteModule");
        clearBtn = (Button) thisScene.lookup("#btnClearModule");
        add = (Button) thisScene.lookup("#btnAddModule");


        tab = (TableView) thisScene.lookup("#moduleTable");
        TableColumn inName = new TableColumn("Module Name");
        TableColumn inCode = new TableColumn("Code");
        TableColumn inInstructor = new TableColumn("Instructor");
        TableColumn inCourse = new TableColumn("Course");
        inName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        inInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        inCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        tab.getColumns().addAll(inName,inCode,inInstructor,inCourse);

        readFromFile();

        back = (Button) thisScene.lookup("#btnBack");
        back.setOnAction(actionEvent -> {
            Admin main = new Admin();
            try {
                main.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        add.setOnAction(actionEvent -> {
            String name = nameTextField.getText().trim();
            String code = codeTextField.getText().trim();
            if (name.isEmpty() || courseComboBox.getValue()==null || instructorComboBox.getValue()==null || code.isEmpty()){
                displayError("Enter All Data");
            }
            else {
                String course = courseComboBox.getValue().toString();
                String instructor = instructorComboBox.getValue().toString();
                if (add.getText()=="Edit Module"){
                    modules.remove(Holder.id);
                    ModuleModel im = new ModuleModel(name,code,instructor,course);
                    modules.add(Holder.id,im);
                    writeToFile(null,modules,im);
                    removeAll();
                    clear();
                }
                else {
                    try {
                        File ins = new File("modules.txt");
                        if (ins.createNewFile()) {
                            System.out.println("File created: " + ins.getName());
                        } else {
                            System.out.println("File already exists.");
                        }
                        ModuleModel moduleModel = new ModuleModel(name,code,instructor,course);
                        writeToFile(name+","+code+","+instructor+","+course,null,moduleModel);
                        tab.getItems().add(moduleModel);
                        clear();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        clearBtn.setOnAction(actionEvent -> {
            clear();
        });

        deleteBtn.setOnAction(actionEvent -> {
            for (int i=0; i<modules.size();i++){
                String code = codeTextField.getText().trim();
                if (code.equals(modules.get(i).getCode())){
                    modules.remove(Holder.id);
                    writeToFile(null,modules,null);
                    removeAll();
                    clear();
                }

            }
        });

        final ObservableList<TablePosition> selectedCells = tab.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<TablePosition>) change -> {
            if (tab.getSelectionModel().getSelectedItems()!=null){
                ModuleModel tModel = (ModuleModel) tab.getSelectionModel().getSelectedItem();
                if (tModel!=null){
                    nameTextField.setText(tModel.getName());
                    codeTextField.setText(tModel.getCode());
                    Holder.id=tab.getSelectionModel().getFocusedIndex();
                    add.setText("Edit Module");
                    deleteBtn.setVisible(true);
                }
            }
        });

    }

    private void removeAll() {
        tab.getItems().clear();
        for (int i=0; i<modules.size();i++){
            tab.getItems().add(modules.get(i));
        }
    }

    private void clear() {
        add.setText("Add Module");
        nameTextField.setText("");
        codeTextField.setText("");
        courseComboBox.getSelectionModel().selectFirst();
        instructorComboBox.getSelectionModel().selectFirst();
        deleteBtn.setVisible(false);
    }

    private void writeToFile(String s, ArrayList<ModuleModel> list, ModuleModel model) {
        if (list==null){
            try{
                FileWriter myWriter = new FileWriter("modules.txt",true);
                myWriter.write(s+"\n");
                myWriter.close();
                modules.add(model);
                System.out.println("Successfully wrote to the file.");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                FileWriter myWriter = new FileWriter("modules.txt");
                for(int i=0; i<list.size();i++){
                    String temp = list.get(i).getName()+","+list.get(i).getCode()+","+list.get(i).getInstructor()+","+list.get(i).getCourse();
                    myWriter.write(temp+"\n");
                }
                myWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void readFromFile() {
        try {
            File ins = new File("instructors.txt");
            Scanner iReader = new Scanner(ins);
            while (iReader.hasNextLine()) {
                String data = iReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    InstructorModel model = new InstructorModel(values[0],values[1],values[2]);
                    cInstructors.add(model);
                    instructorComboBox.getItems().add(model.getName());
                }
            }
            instructorComboBox.getSelectionModel().selectFirst();
            iReader.close();

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

            File myObj = new File("modules.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    ModuleModel model = new ModuleModel(values[0],values[1],values[2],values[3]);
                    modules.add(model);
                    tab.getItems().add(model);
                }
            }
            myReader.close();


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
