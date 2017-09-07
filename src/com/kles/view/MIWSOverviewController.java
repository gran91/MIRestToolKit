package com.kles.view;

import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import com.kles.model.MIWS;

public class MIWSOverviewController extends ModelManagerTableViewController implements IModelManagerView {

    @FXML
    private TableColumn<MIWS, String> nameColumn;
    @FXML
    private TableColumn<MIWS, String> miwsIpColumn;
    @FXML
    private TableColumn<MIWS, Number> miwsPortColumn;
    @FXML
    private TableColumn<MIWS, String> miwsLoginColumn;
    @FXML
    private TableColumn<MIWS, String> miwsPwdColumn;

    public MIWSOverviewController() {
        super("MIWS");
    }

    public MIWSOverviewController(String dataname) {
        super("MIWS");
    }

    @Override
    public void loadColumnTable() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        miwsIpColumn.setCellValueFactory(cellData -> cellData.getValue().getHostProperty());
        miwsIpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        miwsPortColumn.setCellValueFactory(cellData -> cellData.getValue().getPortProperty());
        miwsPortColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {

            @Override
            public String toString(Number object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        }));
        miwsLoginColumn.setCellValueFactory(cellData -> cellData.getValue().getLoginProperty());
        miwsLoginColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        miwsPwdColumn.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
        miwsPwdColumn.setCellFactory((TableColumn<MIWS, String> cell) -> new PasswordLabelCell());

//        TableViewUtils.addHeaderFilter(table);
    }

    /*@FXML
    @Override
    public void handleNew() {
        datamodel = new Environment();
        super.handleNew();
    }*/
    @Override
    public void newInstance() {
        datamodel = new MIWS();
    }

    class PasswordLabelCell extends TableCell<MIWS, String> {

        private final Label label;

        public PasswordLabelCell() {
            label = new Label();
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic(null);
        }

        private String genDotString(int len) {
            String dots = "";

            for (int i = 0; i < len; i++) {
                dots += "\u2022";
            }

            return dots;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                label.setText(genDotString(item.length()));
                setGraphic(label);
            } else {
                setGraphic(null);
            }
        }
    }
}
