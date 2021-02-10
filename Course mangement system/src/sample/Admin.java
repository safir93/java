package sample;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class Admin extends Application {
    Stage window;
    Scene scene1, scene2;
    TableView tab;
    Label label;
    ArrayList<MarkModel> marks = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        primaryStage.setTitle("Admin Management");
        Scene thisScene = new Scene(root, 750, 500);
        primaryStage.setScene(thisScene);
        primaryStage.show();

        tab = (TableView) thisScene.lookup("#adminMarksTable");
        label = (Label) thisScene.lookup("#labelResult");
        TableColumn sName = new TableColumn("Student");
        TableColumn sModule = new TableColumn("Module");
        TableColumn sMarks = new TableColumn("Marks");
        sName.setCellValueFactory(new PropertyValueFactory<>("name"));
        sModule.setCellValueFactory(new PropertyValueFactory<>("module"));
        sMarks.setCellValueFactory(new PropertyValueFactory<>("marks"));
        sName.setMinWidth(120);
        sModule.setMinWidth(120);
        sMarks.setMinWidth(100);
        tab.getColumns().addAll(sName,sModule,sMarks);

        final ObservableList<TablePosition> selectedCells2 = tab.getSelectionModel().getSelectedCells();
        selectedCells2.addListener((ListChangeListener<TablePosition>) change -> {
            if (tab.getSelectionModel().getSelectedItems()!=null){
                MarkModel tModel = (MarkModel) tab.getSelectionModel().getSelectedItem();
                if (tModel!=null){
                    int mark = Integer.parseInt(tModel.getMarks());
                    String result = "Failed";
                    if (mark>40){
                        result = "Passed";
                    }
                    label.setText(result);
                }
            }
        });

        readMarks();


        Button back = (Button) thisScene.lookup("#btnBack");
        back.setOnAction(actionEvent -> {
            Main main = new Main();
            try {
                main.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button btnViewCourses = (Button) thisScene.lookup("#btnViewCourses");
        btnViewCourses.setOnAction(actionEvent -> {
            AddCourse ac = new AddCourse();
            try {
                ac.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button btnAddIns = (Button) thisScene.lookup("#btnAddIns");
        btnAddIns.setOnAction(actionEvent -> {
            AddInstructor ac = new AddInstructor();
            try {
                ac.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button report = (Button) thisScene.lookup("#btnReport");
        report.setOnAction(actionEvent -> {
            tab.getItems().clear();
            label.setText("");
            for (int i=0; i<marks.size();i++){
                tab.getItems().add(marks.get(i));
            }
        });

        Button btnAddModule = (Button) thisScene.lookup("#btnAddModule");
        btnAddModule.setOnAction(actionEvent -> {
            AddModule mo = new AddModule();
            try {
                mo.start(primaryStage);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void readMarks() {
        try {
            File ins = new File("marks.txt");
            Scanner iReader = new Scanner(ins);
            while (iReader.hasNextLine()) {
                String data = iReader.nextLine();
                if (data!=null){
                    String [] values = data.split(",");
                    MarkModel model = new MarkModel(values[0],values[1],values[2]);
                    marks.add(model);
                }
            }
//            comboBoxInstructor.getSelectionModel().selectFirst();
            iReader.close();
        }
        catch (IOException e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
