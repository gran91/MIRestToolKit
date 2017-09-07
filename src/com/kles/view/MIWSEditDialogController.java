/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view;

import com.kles.MainApp;
import com.kles.fx.custom.InputConstraints;
import com.kles.fx.custom.TextFieldValidator;
import com.kles.model.AbstractDataModel;
import com.kles.model.MIWS;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jchau
 */
public class MIWSEditDialogController extends AbstractDataModelEditController {

    @FXML
    private TextField tname;

    @FXML
    private TextField thost;

    @FXML
    private TextField tport;

    @FXML
    private TextField tlogin;

    @FXML
    private PasswordField tpassword;

    protected BooleanBinding hostBoolean, portBoolean;

    @FXML
    public void initialize() {
        InputConstraints.numbersOnly(tport, 5);
    }

    @Override
    public void setMainApp(MainApp main) {
        super.setMainApp(main);
    }

    /**
     * Sets the environment to be edited in the dialog.
     *
     * @param miws
     */
    @Override
    public void setDataModel(AbstractDataModel miws) {
        datamodel = miws;
        tname.setText(((MIWS) miws).getName());
        thost.setText(((MIWS) miws).getHost());
        tport.setText("" + ((MIWS) miws).getPort());
        tlogin.setText(((MIWS) miws).getLogin());
        tpassword.setText(((MIWS) miws).getPassword());
    }

    @Override
    public void setBooleanMessage() {
        hostBoolean = TextFieldValidator.patternTextFieldBinding(thost, TextFieldValidator.hostnamePattern, mainApp.getResourceBundle().getString("message.host"), messages);
        portBoolean = TextFieldValidator.patternTextFieldBinding(tport, TextFieldValidator.allPortNumberPattern, mainApp.getResourceBundle().getString("message.port"), messages);
        BooleanBinding[] mandotariesBinding = new BooleanBinding[]{hostBoolean, portBoolean};
        BooleanBinding mandatoryBinding = TextFieldValidator.any(mandotariesBinding);
        LongBinding nbMandatoryBinding = TextFieldValidator.count(mandotariesBinding);
    }

    @Override
    public void saveData() {
        ((MIWS) datamodel).getNameProperty().set(tname.getText());
        ((MIWS) datamodel).getHostProperty().set(thost.getText());
        ((MIWS) datamodel).getPortProperty().set(Integer.parseInt(tport.getText()));
        ((MIWS) datamodel).getLoginProperty().set(tlogin.getText());
        ((MIWS) datamodel).getPasswordProperty().set(tpassword.getText());
    }

    @Override
    public boolean isInputValid() {
        errorMessage = "";
        return super.isInputValid();
    }
}
