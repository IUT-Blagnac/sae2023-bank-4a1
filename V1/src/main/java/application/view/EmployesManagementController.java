package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmployesManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
	public void initContext(Stage _containingStage, EmployesManagement _em, DailyBankState _dbstate, Employe employe) {
		this.emDialogController = _em;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.employe = employe;
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

		info = this.employe.nom + "  " + this.employe.prenom + "  (id : "
				+ this.employe.idEmploye + ")";
		this.lblInfosEmploye.setText(info);

		this.loadList();
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
	}

	@FXML
	private void doSupprimerEmploye() {
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
		listeEmp = this.emDialogController.getlisteEmployes();
		this.oListEmploye.clear();
		this.oListEmploye.addAll(listeEmp);
	}

	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnModifierEmploye.setDisable(true);
		this.btnSupprEmploye.setDisable(true);
	}
}
