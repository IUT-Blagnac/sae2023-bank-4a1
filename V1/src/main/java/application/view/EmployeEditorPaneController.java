package application.view;

import application.DailyBankState;
import application.control.ExceptionDialog;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.Order;
import model.orm.exception.Table;

public class EmployeEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	private Employe employeEdite;
	private EditionMode editionMode;
	private Employe employeResultat;

	// Manipulation de la fenêtre

	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.editionMode = mode;
		if (employe == null) {
			this.employeEdite = new Employe(0, "", "", "", "", "", this.dailyBankState.getEmployeActuel().idAg);
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResultat = null;
		switch (mode) {
		case CREATION:
			this.txtIdEmp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtLogin.setDisable(false);
			this.txtMotDePasse.setDisable(false);
			this.rbChef.setSelected(true);
			this.rbPasChef.setSelected(false);
			if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
				this.rbChef.setDisable(false);
				this.rbPasChef.setDisable(false);
			} else {
				this.rbChef.setDisable(true);
				this.rbPasChef.setDisable(true);
			}
			this.lblMessage.setText("Informations sur le nouvel employé");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdEmp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtLogin.setDisable(false);
			this.txtMotDePasse.setDisable(false);
			this.rbChef.setSelected(true);
			this.rbPasChef.setSelected(false);
			
			if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
				this.rbChef.setDisable(false);
				this.rbPasChef.setDisable(false);
			} else {
				this.rbChef.setDisable(true);
				this.rbPasChef.setDisable(true);
			}
			this.lblMessage.setText("Informations employé");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			ApplicationException ae = new ApplicationException(Table.NONE, Order.OTHER, "SUPPRESSION EMPLOYE NON PREVUE", null);
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();

			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdEmp.setText("" + this.employeEdite.idEmploye);
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtLogin.setText(this.employeEdite.login);
		this.txtMotDePasse.setText(this.employeEdite.motPasse);

		if (ConstantesIHM.isAdmin(this.employeEdite)) {
			this.rbPasChef.setSelected(true);
			this.rbChef.setSelected(false);
		} else {
			this.rbPasChef.setSelected(false);
			this.rbChef.setSelected(true);
		}

		this.employeResultat = null;

		this.primaryStage.showAndWait();
		return this.employeResultat;
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdEmp;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtMotDePasse;
	@FXML
	private RadioButton rbChef;
	@FXML
	private RadioButton rbPasChef;
	@FXML
	private ToggleGroup chefPasChef;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	@FXML
	private void doCancel() {
		this.employeResultat = null;
		this.primaryStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case SUPPRESSION:
			this.employeResultat = this.employeEdite;
			this.primaryStage.close();
			break;
		}

	}

	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.login = this.txtLogin.getText().trim();
		this.employeEdite.motPasse = this.txtMotDePasse.getText().trim();
		if (this.rbChef.isSelected()) {
			this.employeEdite.droitsAccess = ConstantesIHM.AGENCE_CHEF;
		} else {
			this.employeEdite.droitsAccess = ConstantesIHM.AGENCE_GUICHETIER;
		}

		if (this.employeEdite.nom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
					AlertType.WARNING);
			this.txtNom.requestFocus();
			return false;
		}
		if (this.employeEdite.prenom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}
		return true;
	}
}