package com.kles;

import com.kles.mi.MIInputData;
import com.kles.model.MIWS;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.kles.view.RootLayoutController;
import com.kles.view.mi.MIRestTestToolsController;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import resources.ResourceApp;

public class MIRestTools extends MainApp {

    private BorderPane rootLayout;
    private RootLayoutController rootController;

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        prefs = Preferences.userRoot().node("3KLES_" + resources.ResourceApp.TITLE);
        initPrefs();
        initApp();
        resourceBundle = ResourceBundle.getBundle("resources/miresttools", Locale.getDefault());
        addToDataMap("MIWS", FXCollections.observableArrayList(), MIWS.class);
        addToDataMap("MIInputData", FXCollections.observableArrayList(), MIInputData.class);
        loadView();
    }

    @Override
    public void loadView() {
        super.title.unbind();
        super.title.bind(Bindings.concat(ResourceApp.TITLE).concat("\t").concat(super.clock.getTimeText()));
        this.primaryStage.titleProperty().unbind();
        this.primaryStage.titleProperty().bind(title);
        initRootLayout();
        showMIRestTools();
        primaryStage.show();
    }

    /**
     * Initializes the root layout and tries to load the last opened person
     * file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resourceBundle);
            loader.setLocation(MIRestTools.class.getResource("/com/kles/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
//            UndecoratorScene scene = new UndecoratorScene(primaryStage, rootLayout);
//            scene.setFadeInTransition();

//            primaryStage.setOnCloseRequest((WindowEvent we) -> {
//                we.consume();   // Do not hide yet
//                scene.setFadeOutTransition();
//            });
            primaryStage.setScene(scene);

            rootController = loader.getController();
            rootController.setMainApp(this);
            scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
                System.out.println("Width: " + newSceneWidth);
            });
            scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
                System.out.println("Height: " + newSceneHeight);
            });
//            Undecorator undecorator = scene.getUndecorator();
//            primaryStage.setMinWidth(undecorator.getMinWidth());
//            primaryStage.setMinHeight(undecorator.getMinHeight());

//            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = getDataDirectoryPath();
        if (file != null) {
            loadDataDirectory(file);
        }
    }

    public void showMIRestTools() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resourceBundle);
            loader.setLocation(MIRestTools.class.getResource("/com/kles/view/mi/MIRestTestTools.fxml"));
            rootLayout.setCenter(loader.load());
            MIRestTestToolsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setEnvironmentList(this.getDataMap().get("MIWS").getList());
            controller.setStage(primaryStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootLayoutController getRootController() {
        return rootController;
    }

    public void setRootController(RootLayoutController rootController) {
        this.rootController = rootController;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    static {
        loadLanguages();
    }
}
