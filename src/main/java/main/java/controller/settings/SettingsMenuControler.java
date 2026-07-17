package main.java.main.java.controller.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsMenuControler implements Initializable {

	@FXML private AnchorPane settingsMenuPanel;

	private BorderPane pane;
	private ViewUtil viewUtil;
	private Pane current;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewUtil = new ViewUtil();
	}

	@FXML
	void openUploadStamp(ActionEvent event) {
		pane = (BorderPane) settingsMenuPanel.getParent();
		if (current != null) current.setVisible(false);
		current = viewUtil.getPage("settings/UploadStamp");
		pane.setCenter(current);
		current.setVisible(true);
	}

	@FXML
	void openBusinessInfo(ActionEvent event) {
		pane = (BorderPane) settingsMenuPanel.getParent();
		if (current != null) current.setVisible(false);
		current = viewUtil.getPage("settings/BusinessInfo");
		pane.setCenter(current);
		current.setVisible(true);
	}
}
