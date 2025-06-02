package com.example.defter.Admin;

import com.example.defter.Model.Kreditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class KreditorSechimiDialog {
    private List<Kreditor> kreditorList= new ArrayList<>();
    private ObservableList<Kreditor> kreditorObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<Kreditor> kreditorTableView;
    @FXML
    private TableColumn<Kreditor,String> id;
    @FXML
    private TableColumn<Kreditor,String> ad;
    @FXML
    private TableColumn<Kreditor,String> borc;
    @FXML
    private TextField searchBar;
    private AdminClient client;

    public void init(AdminClient client){
        this.client=client;
        kreditorList.clear();
        kreditorObservableList.clear();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        borc.setCellValueFactory(new PropertyValueFactory<>("borc"));
        this.client.sendMessage("GETKREDITORLIST");
        kreditorList= (List<Kreditor>) this.client.objectReader();
        kreditorObservableList.addAll(kreditorList);
        kreditorTableView.setItems(kreditorObservableList);

    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(kreditorObservableList,name);
        }
    }
    private void Search(ObservableList<Kreditor> kreditorObservableList, String name) {
        kreditorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < kreditorList.size(); i++) {
            if (kreditorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                kreditorObservableList.add(kreditorList.get(i));
            }
        }
    }

    public Kreditor getSelectedItem(){
        return kreditorTableView.getSelectionModel().getSelectedItem();
    }
}
