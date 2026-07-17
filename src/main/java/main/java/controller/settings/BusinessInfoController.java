package main.java.main.java.controller.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import main.java.main.java.hibernate.entities.CompanyDetails;
import main.java.main.java.hibernate.service.service.CompanyService;
import main.java.main.java.hibernate.service.serviceImpl.CompanyServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class BusinessInfoController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtContact;
    @FXML private TextField txtAltContact;
    @FXML private TextField txtEmail;
    @FXML private TextField txtGst;
    @FXML private TextField txtPan;
    @FXML private TextArea  txtAddress;
    @FXML private TextField txtCity;
    @FXML private TextField txtPin;
    @FXML private TextField txtTaluka;
    @FXML private TextField txtDistrict;
    @FXML private Label     lblStatus;

    private CompanyService companyService;
    private int companyId = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyService = new CompanyServiceImpl();
        loadCompanyDetails();
    }

    private void loadCompanyDetails() {
        CompanyDetails company = companyService.getCompanyDetails(1);
        if (company != null) {
            companyId = company.getId();
            txtName.setText(safe(company.getName()));
            txtContact.setText(safe(company.getContact()));
            txtAltContact.setText(safe(company.getAltercontact()));
            txtEmail.setText(safe(company.getEmail()));
            txtGst.setText(safe(company.getGst()));
            txtPan.setText(safe(company.getPanNo()));
            txtAddress.setText(safe(company.getAddress()));
            txtCity.setText(safe(company.getCity()));
            txtPin.setText(safe(company.getPin()));
            txtTaluka.setText(safe(company.getTaluka()));
            txtDistrict.setText(safe(company.getDistrict()));
            setStatus("Record loaded. Edit fields and click SAVE DETAILS to update.", "#546E7A");
        } else {
            setStatus("No record found. Fill in the details and save.", "#1565C0");
        }
    }

    @FXML
    void saveAction(ActionEvent event) {
        try {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Business name is required.");
                txtName.requestFocus();
                return;
            }

            CompanyDetails company = new CompanyDetails();
            company.setId(companyId);
            company.setName(name);
            company.setContact(txtContact.getText().trim());
            company.setAltercontact(txtAltContact.getText().trim());
            company.setEmail(txtEmail.getText().trim());
            company.setGst(txtGst.getText().trim());
            company.setPanNo(txtPan.getText().trim());
            company.setAddress(txtAddress.getText().trim());
            company.setCity(txtCity.getText().trim());
            company.setPin(txtPin.getText().trim());
            company.setTaluka(txtTaluka.getText().trim());
            company.setDistrict(txtDistrict.getText().trim());

            int result = companyService.saveCompany(company);
            if (result == 1) {
                companyId = 1;
                setStatus("Saved successfully.", "#43A047");
                showAlert(Alert.AlertType.INFORMATION, "Saved", "Business information saved successfully.");
            } else if (result == 2) {
                setStatus("Updated successfully.", "#43A047");
                showAlert(Alert.AlertType.INFORMATION, "Updated", "Business information updated successfully.");
            } else {
                setStatus("Save failed. Check the console for details.", "#D32F2F");
                showAlert(Alert.AlertType.ERROR, "Save Failed", "Could not save business information.\nPlease check your database connection and try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Error: " + e.getMessage(), "#D32F2F");
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred:\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void clearAction(ActionEvent event) {
        txtName.clear();
        txtContact.clear();
        txtAltContact.clear();
        txtEmail.clear();
        txtGst.clear();
        txtPan.clear();
        txtAddress.clear();
        txtCity.clear();
        txtPin.clear();
        txtTaluka.clear();
        txtDistrict.clear();
        setStatus("Fields cleared.", "#546E7A");
    }

    private String safe(String val) {
        return val == null ? "" : val;
    }

    private void setStatus(String message, String colorHex) {
        lblStatus.setTextFill(Color.web(colorHex));
        lblStatus.setText(message);
    }
}
