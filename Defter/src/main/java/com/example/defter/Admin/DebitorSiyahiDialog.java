package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
import com.example.defter.Model.Kreditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class DebitorSiyahiDialog {
    private AdminClient client;
    private Debitor debitor;
    @FXML
    private TableView<Debitor>debitorTableView;
    @FXML
    private TableColumn<Debitor,String> id;
    @FXML
    private TableColumn<Debitor,String> ad;
    @FXML
    private TableColumn<Debitor,String> borc;
    @FXML
    private TextField searchBar;
    private List<Debitor> debitorList = new ArrayList<>();
    private ObservableList<Debitor> debitorObservableList = FXCollections.observableArrayList();
    public void init(AdminClient client){
        this.client=client;
        debitorList.clear();
        debitorObservableList.clear();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        borc.setCellValueFactory(new PropertyValueFactory<>("borc"));
        this.client.sendMessage("GETDEBITORLIST");
        debitorList= (List<Debitor>) this.client.objectReader();
        debitorObservableList.addAll(debitorList);
        debitorTableView.setItems(debitorObservableList);
    }

    public Debitor getSelectedItem(){
        return debitorTableView.getSelectionModel().getSelectedItem();
    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(debitorObservableList,name);
        }
    }
    private void Search(ObservableList<Debitor> debitorObservableList, String name) {
        debitorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < debitorList.size(); i++) {
            if (debitorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                debitorObservableList.add(debitorList.get(i));
            }
        }
    }

}
