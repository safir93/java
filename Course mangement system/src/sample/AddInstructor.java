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


public class AddInstructor extends Application {
    Stage window;
    Scene scene1, scene2;
    TableView tab;
    Button back,deleteBtn, add,clearBtn;
    ArrayList<InstructorModel> instructors = new ArrayList<>();
    TextField nameTextField, phoneTextFiled, addressTextField;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("addInstructor.fxml"));
        primaryStage.setTitle("Add Instructor");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        nameTextField = (TextField) thisScene.lookup("#insName");
        phoneTextFiled = (TextField) thisScene.lookup("#insPhone");
        addressTextField = (TextField) thisScene.lookup("#insAddress");
        deleteBtn = (Button) thisScene.lookup("#btnDeleteIns");
        clearBtn = (Button) thisScene.lookup("#btnClearIns");

        tab = (TableView) thisScene.lookup("#tableInstructors");
        TableColumn inName = new TableColumn("Name");
        TableColumn inAddress = new TableColumn("Address");
        TableColumn inPhone = new TableColumn("Phone");
        inName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        inPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tab.getColumns().addAll(inName,inAddress,inPhone);

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

        add = (Button) thisScene.lookup("#btnAddInstructor");
        add.setOnAction(actionEvent -> {
            String name = nameTextField.getText().trim();
            String phone = phoneTextFiled.getText().trim();
            String address = addressTextField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()){
                displayError("TextFields are empty");
            }
            else {
                if (add.getText()=="Edit Instructor"){
                    instructors.remove(Holder.id);
                    InstructorModel im = new InstructorModel(name,address,phone);
                    instructors.add(Holder.id,im);
                    writeToFile(null,instructors,im);
                    removeAll();
                    clear();
                }
                else {
                    try {
                        File ins = new File("instructors.txt");
                        if (ins.createNewFile()) {
                            System.out.println("File created: " + ins.getName());
                        } else {
                            System.out.println("File already exists.");
                        }
                        InstructorModel instructorModel = new InstructorModel(name,address,phone);
                        writeToFile(name+","+phone+","+address,null,instructorModel);
                        tab.getItems().add(instructorModel);
                        clear();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        final ObservableList<TablePosition> selectedCells = tab.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<TablePosition>) change -> {
            if (tab.getSelectionModel().getSelectedItems()!=null){
                InstructorModel tModel = (InstructorModel) tab.getSelectionModel().getSelectedItem();
                if (tModel!=null){
                    nameTextField.setText(tModel.getName());
                    addressTextField.setText(tModel.getAddress());
                    phoneTextFiled.setText(tModel.getPhone());
                    Holder.id=tab.getSelectionModel().getFocusedIndex();
                    add.setText("Edit Instructor");
                    deleteBtn.setVisible(true);
                }
            }
        });

        clearBtn.setOnAction(actionEvent -> {
            clear();
        });

        deleteBtn.setOnAction(actionEvent -> {
            for (int i=0; i<instructors.size();i++){
                String name = nameTextField.getText().trim();
                if (name.equals(instructors.get(i).getName())){
                    instructors.remove(Holder.id);
                    writeToFile(null,instructors,null);
                    removeAll();
                    clear();
                }

            }
        });

    }

    public void removeAll(){
        tab.getItems().clear();
        for (int i=0; i<instructors.size();i++){
            tab.getItems().add(instructors.get(i));
        }
    }

    private void clear() {
        add.setText("Add Instructor");
        nameTextField.setText("");
        addressTextField.setText("");
        phoneTextFiled.setText("");
        deleteBtn.setVisible(false);
    }

    private void writeToFile(String s, ArrayList<InstructorModel> list, InstructorModel model) {
        if (list==null){
            try{
                FileWriter myWriter = new FileWriter("instructors.txt",true);
                myWriter.write(s+"\n");
                myWriter.close();
                instructors.add(model);
                System.out.println("Successfully wrote to the file.");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                FileWriter myWriter = new FileWriter("instructors.txt");
                for(int i=0; i<list.size();i++){
                    String temp = list.get(i).getName()+","+list.get(i).getPhone()+","+list.get(i).getAddress();
                    myWriter.write(temp+"\n");
                }
                myWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
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

    public void readFromFile(){
        try {
            File myObj = new File("instructors.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    InstructorModel model = new InstructorModel(values[0],values[1],values[2]);
                    instructors.add(model);
                    tab.getItems().add(model);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
