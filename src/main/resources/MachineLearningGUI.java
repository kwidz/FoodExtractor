package GUI;


import MachineLearning.IngredientReader;
import RecipeExtractor.RicardoCrawler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MachineLearningGUI extends Application {

    ListView listComponents;
    private TextField typefield, inputVegetable, inputMeat, inputMod, inputForbidden;
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private Button validateVegetable, validateMeat, validateMod, validateForbidden;

    String meatFishCheese = "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt";
    String modifiers = "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt";
    String vegetables = "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt";
    String forbidden = "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt" ;



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Learning components types helper");

        initRootLayout();
        }

    private void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MachineLearningGUI.class.getResource("MachineLearningGUI.fxml"));
            rootLayout = (AnchorPane) loader.load();


        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        listComponents = (ListView) scene.lookup("#listComponents");
        typefield = (TextField) scene.lookup("#type");
        //TODO
        //Add all fields
        validateVegetable = (Button) scene.lookup("#validateVegetable");
        validateMeat =(Button) scene.lookup("#validateMeat");
        validateMod = (Button) scene.lookup("#validateMod");
        validateForbidden = (Button) scene.lookup("#validateForbidden");

        inputVegetable = (TextField) scene.lookup("#inputVegetable");
        inputMeat = (TextField) scene.lookup("#inputMeat");
        inputMod = (TextField) scene.lookup("#inputMod");
        inputForbidden = (TextField) scene.lookup("#inputForbidden");;
        IngredientReader components = new IngredientReader("/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Meat.txt","/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Modifiers.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Vegetables.txt", "/home/kwidz/Cours/Memoire Maitrise/ExtracteurIngrédients/src/MachineLearning/Forbiden.txt");
        ArrayList<String> allIngredients = new RicardoCrawler().getAllIngredients();

        for (String s: allIngredients
             ) {

            //if(components.classify(s).equals("unclassified"))
              //  listComponents.getItems().add(s);
        }
        System.out.println(allIngredients.size());
        listComponents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                //String type = components.classify(newValue);
                //typefield.setText(type);
                //System.out.println("Selected item: " + newValue+ " "+type);
            }
        });

        validateForbidden.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                writeInaFile(forbidden, inputForbidden.getText());
                components.refreshTypes(meatFishCheese,modifiers,vegetables,forbidden);
            }
        });
        validateMod.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                writeInaFile(modifiers, inputMod.getText());
                components.refreshTypes(meatFishCheese,modifiers,vegetables,forbidden);
            }
        });
        validateMeat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                writeInaFile(meatFishCheese, inputMeat.getText());
                components.refreshTypes(meatFishCheese,modifiers,vegetables,forbidden);
            }
        });
        validateVegetable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                writeInaFile(vegetables, inputVegetable.getText());
                components.refreshTypes(meatFishCheese,modifiers,vegetables,forbidden);
            }
        });

            primaryStage.show();
    }

    private void writeInaFile(String file, String newType){
        try(FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(newType);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
