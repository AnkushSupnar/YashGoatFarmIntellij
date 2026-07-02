package main.java.main.java.controller.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.main.java.hibernate.util.AppSettings;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UploadStampControler implements Initializable {

	@FXML private AnchorPane mainPane;
	@FXML private TextField txtPath;
	@FXML private TextField txtWidth;
	@FXML private TextField txtHeight;
	@FXML private ImageView imgPreview;
	@FXML private Label lblStatus;
	@FXML private Button btnBrowse;
	@FXML private Button btnSave;
	@FXML private Button btnClear;

	private File selectedFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AppSettings.StampConfig cfg = AppSettings.loadStampConfig();
		if (cfg != null && cfg.isUsable()) {
			txtPath.setText(cfg.imagePath);
			txtWidth.setText(stripZero(cfg.widthPt));
			txtHeight.setText(stripZero(cfg.heightPt));
			selectedFile = new File(cfg.imagePath);
			loadPreview(selectedFile);
			lblStatus.setText("Current stamp loaded.");
		} else {
			txtWidth.setText(stripZero(AppSettings.DEFAULT_STAMP_WIDTH));
			txtHeight.setText(stripZero(AppSettings.DEFAULT_STAMP_HEIGHT));
			lblStatus.setText("No stamp configured yet.");
		}
	}

	@FXML
	void onBrowse(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose stamp / signature image");
		chooser.getExtensionFilters().add(new ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));
		File f = chooser.showOpenDialog(mainPane.getScene().getWindow());
		if (f == null) return;
		selectedFile = f;
		txtPath.setText(f.getAbsolutePath());
		loadPreview(f);
		lblStatus.setText("Image selected. Click Save to apply.");
	}

	@FXML
	void onSave(ActionEvent event) {
		if (selectedFile == null || !selectedFile.exists()) {
			alert(Alert.AlertType.ERROR, "Choose a stamp image first.");
			return;
		}
		float w = parseOr(txtWidth.getText(), AppSettings.DEFAULT_STAMP_WIDTH);
		float h = parseOr(txtHeight.getText(), AppSettings.DEFAULT_STAMP_HEIGHT);
		if (w <= 0 || h <= 0) {
			alert(Alert.AlertType.ERROR, "Width and height must be greater than 0.");
			return;
		}
		String saved = AppSettings.saveStampConfig(selectedFile, w, h);
		if (saved == null) {
			alert(Alert.AlertType.ERROR, "Failed to save stamp. See logs.");
			return;
		}
		txtPath.setText(saved);
		selectedFile = new File(saved);
		alert(Alert.AlertType.INFORMATION, "Stamp saved. New quotations will include it.");
		lblStatus.setText("Saved: " + saved);
	}

	@FXML
	void onClear(ActionEvent event) {
		selectedFile = null;
		txtPath.setText("");
		imgPreview.setImage(null);
		txtWidth.setText(stripZero(AppSettings.DEFAULT_STAMP_WIDTH));
		txtHeight.setText(stripZero(AppSettings.DEFAULT_STAMP_HEIGHT));
		lblStatus.setText("Cleared. Select a new image and Save.");
	}

	private void loadPreview(File f) {
		try {
			imgPreview.setImage(new Image(f.toURI().toString()));
		} catch (Exception e) {
			imgPreview.setImage(null);
		}
	}

	private void alert(Alert.AlertType type, String msg) {
		new Alert(type, msg).showAndWait();
	}

	private float parseOr(String s, float fallback) {
		try { return Float.parseFloat(s.trim()); } catch (Exception e) { return fallback; }
	}

	private String stripZero(float v) {
		if (v == Math.floor(v)) return Integer.toString((int) v);
		return Float.toString(v);
	}
}
