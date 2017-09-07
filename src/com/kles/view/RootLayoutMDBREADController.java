package com.kles.view;

import com.kles.MDBREADTools;
import com.kles.fx.custom.FxUtil;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.kles.MainApp;
import com.kles.view.mi.MDBREADToolsController;
import com.kles.view.mi.MIRestTestToolsController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import resources.ResourceApp;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Jérémy Chaut
 */
public class RootLayoutMDBREADController {

    @FXML
    private RadioMenuItem menuFR;
    @FXML
    private RadioMenuItem menuEN;
    @FXML
    private Menu langmenu, skinmenu;
    private MDBREADTools mainApp;
    //private Stage customerStage, developerStage, environmentStage, serverStage;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = (MDBREADTools) mainApp;
        this.mainApp.getListSkin().entrySet().stream().forEach((nameSkin) -> {
            final CheckMenuItem menuItemSkin = new CheckMenuItem(nameSkin.getKey());
            menuItemSkin.setSelected(this.mainApp.prefs.get(MainApp.SKIN, MainApp.STYLESHEET_CASPIAN).equals(nameSkin.getKey()));
            menuItemSkin.setOnAction(e -> {
                Application.setUserAgentStylesheet(nameSkin.getValue());
                this.mainApp.prefs.put(MDBREADTools.SKIN, nameSkin.getKey());
                skinmenu.getItems().stream().forEach(m -> {
                    ((CheckMenuItem) m).setSelected(m.getText().equals(nameSkin.getKey()));
                });
            });
            skinmenu.getItems().add(menuItemSkin);
        });
        langmenu.getItems().stream().forEach(m -> {
            ((RadioMenuItem) m).setSelected(this.mainApp.prefs.get(MDBREADTools.LANGUAGE, Locale.getDefault().getLanguage()).equals(m.getId()));
            m.setOnAction(e -> {
                Locale.setDefault(new Locale(m.getId()));
                this.mainApp.prefs.put(MDBREADTools.LANGUAGE, m.getId());
                langmenu.getItems().stream().forEach(m1 -> {
                    ((RadioMenuItem) m).setSelected(m1.getId().equals(this.mainApp.prefs.get(MDBREADTools.LANGUAGE, Locale.getDefault().getLanguage())));
                });
                try {
                    mainApp.getPrimaryStage().close();
                } catch (Exception ex) {
                    Logger.getLogger(RootLayoutMDBREADController.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MDBREADTools().start(new Stage());
            });
        });
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        mainApp.clearData();
        mainApp.setRegistryFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        // Show save file dialog
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadDataDirectory(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    public void handleSave() {
        File directory = mainApp.getDataDirectoryPath();
        if (directory != null) {
            try {
                mainApp.saveDataToFile(directory);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RootLayoutMDBREADController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        DirectoryChooser fileChooser = new DirectoryChooser();

        // Set extension filter
        /*FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
         "XML files (*.xml)", "*.xml");
         fileChooser.getExtensionFilters().add(extFilter);
         */
        // Show save file dialog
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());

        if (file != null) {
            try {
                /*// Make sure it has the correct extension
                 if (!file.getPath().endsWith(".xml")) {
                 file = new File(file.getPath() + ".xml");
                 }*/
                mainApp.saveDataToFile(file);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RootLayoutMDBREADController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        FxUtil.showAlert(Alert.AlertType.INFORMATION, mainApp.getResourceBundle().getString("about.title"), String.format(mainApp.getResourceBundle().getString("about.header"), ResourceApp.VERSION), String.format(mainApp.getResourceBundle().getString("about.text"), ResourceApp.VERSION));
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExit() {
        handleSave();
        System.exit(0);
    }

    @FXML
    public void showMIWSOverview() {
        mainApp.showModelManagerTableView("MIWS", mainApp.getResourceBundle());
    }

    @FXML
    public void showMIRestTest() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(mainApp.getResourceBundle());
            loader.setLocation(MDBREADTools.class.getResource("/com/kles/view/mi/MIRestTestTools.fxml"));
            Pane miRestTest = loader.load();

            Stage stage = new Stage();
            stage.setTitle("MIRestTestTools");
            stage.initModality(Modality.NONE);
            stage.initOwner(mainApp.getPrimaryStage());
            stage.getIcons().add(ResourceApp.LOGO_ICON_32);
            Scene scene = new Scene(miRestTest);
            stage.setScene(scene);

            MIRestTestToolsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setEnvironmentList(mainApp.getDataMap().get("MIWS").getList());
            controller.setStage(stage);

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutMDBREADController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void showMDBREAD() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(mainApp.getResourceBundle());
            loader.setLocation(MDBREADTools.class.getResource("/com/kles/view/mi/MDBREADTools.fxml"));
            Pane miRestTest = loader.load();

            Stage stage = new Stage();
            stage.setTitle("MDBREAD");
            stage.initModality(Modality.NONE);
            stage.initOwner(mainApp.getPrimaryStage());
            stage.getIcons().add(ResourceApp.LOGO_ICON_32);
            Scene scene = new Scene(miRestTest);
            stage.setScene(scene);

            MDBREADToolsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setEnvironmentList(mainApp.getDataMap().get("MIWS").getList());
            controller.setStage(stage);

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RootLayoutMDBREADController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
