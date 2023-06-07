package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmployesManagement;
import application.control.EmployesManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;
import model.data.Employe;

public class EmployesManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à EmployesManagementController
	private EmployesManagement emDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Employe employe;
	private ObservableList<Employe> oListEmploye;

	// Manipulation de la fenêtre

	public void initContext(Stage _containingStage, EmployesManagement _em, DailyBankState _dbstate) {
		this.emDialogController = _em;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		System.out.println(_dbstate.getEmployeActuel().motPasse);
		this.configure();
	}

	private void configure() {
		String info;

		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListEmploye = FXCollections.observableArrayList();
		this.lvEmployes.setItems(this.oListEmploye);
		this.lvEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmployes.getFocusModel().focus(-1);
		this.lvEmployes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.doRechercher();
		this.validateComponentState();
	}

	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private TextField txtNum;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnDesactEmploye;
	@FXML
	private Button btnModifEmploye;

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doRechercher() {
		int numCompte;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				numCompte = -1;
			} else {
				numCompte = Integer.parseInt(nc);
				if (numCompte < 0) {
					this.txtNum.setText("");
					numCompte = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			numCompte = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (numCompte != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		} else {
			if (debutNom.equals("") && !debutPrenom.equals("")) {
				this.txtPrenom.setText("");
			}
		}

		// Recherche des Employes en BD. cf. AccessEmploye > getEmployes(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les Employes
		ArrayList<Employe> listeCli;
		listeCli = this.emDialogController.getlisteEmployes(numCompte, debutNom, debutPrenom);

		this.oListEmploye.clear();
		this.oListEmploye.addAll(listeCli);
		this.validateComponentState();
	}

	@FXML
	private void doModifierEmploye() {

		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = this.oListEmploye.get(selectedIndice);
			Employe result = this.emDialogController.modifierEmploye(empMod);
			if (result != null) {
				this.oListEmploye.set(selectedIndice, result);
			}
		}
	}

	@FXML
	private void doDesactiverEmploye() {
	}

	@FXML
	private void doNouveauEmploye() {
		Employe Employe;
		Employe = this.emDialogController.nouveauEmploye();
		if (Employe != null) {
			this.oListEmploye.add(Employe);
		}
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactEmploye.setDisable(true);
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifEmploye.setDisable(false);
		} else {
			this.btnModifEmploye.setDisable(true);
		}
	}
	
	
	//______________________________________________________________________________________________________________________
	
	/*
	@FXML
	private TextField txtNum;
	@FXML
	private Label lblInfosEmploye;
	@FXML
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnVoirOpes;
	@FXML
	private Button btnModifierEmploye;
	@FXML
	private Button btnSupprEmploye;

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doVoirEmployes() {
		this.loadList();
		this.validateComponentState();
	}

	@FXML
	private void doModifierEmploye() {
		this.emDialogController.modifierEmploye(employe);
	}

	@FXML
	private void doSupprimerEmploye() {
		//this.emDialogController.supprimerEmploye(employe);
	}

	@FXML
	private void doNouveauEmploye() {
		Employe employe;
		employe = this.emDialogController.nouveauEmploye();
		if (employe != null) {
			this.oListEmploye.add(employe);
		}
	}

	private void loadList() {
		ArrayList<Employe> listeEmp;
		listeEmp = this.emDialogController.getlisteEmployes(Integer.parseInt(txtNum.getText())); //-1 ou "" si pas spécifié
		this.oListEmploye.clear();
		this.oListEmploye.addAll(listeEmp);
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnModifierEmploye.setDisable(true);
		this.btnSupprEmploye.setDisable(true);
	}
	*/
}
