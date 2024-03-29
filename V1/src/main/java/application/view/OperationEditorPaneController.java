package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.SimulationEmprunt;
import application.tools.AlertUtilities;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.Access_BD_CompteCourant;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class OperationEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private CategorieOperation categorieOperation;
	private CompteCourant compteEdite;
	private Operation operationResultat;
	private ArrayList<CompteCourant> listComptesClient;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	public Operation displayDialog(CompteCourant cpte, CategorieOperation mode) {
		this.categorieOperation = mode;
		this.compteEdite = cpte;

		switch (mode) {
		/**
		* @author Prescilla Estrade 
	 	* Case Débit compte
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
	 	* Modifie le texte des boutons btnOk et btnCancel par "Effectuer Débit" et "Annuler débit"
	 	*/
		case DEBIT:
			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			this.btnOk.setText("Effectuer Débit");
			this.btnCancel.setText("Annuler débit");

			ObservableList<String> listTypesOpesPossibles = FXCollections.observableArrayList();
			listTypesOpesPossibles.addAll(ConstantesIHM.OPERATIONS_DEBIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
			
		/**
		* @author Prescilla Estrade 
	 	* Case Débit exceptionnel compte		 	
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
		* Modifie le texte des boutons btnOk et btnCancel par "Effectuer Débit exceptionnel " et "Annuler débit exceptionnel"
	 	*/	
		case DEBITEXCEPTIONNEL:
			String info1 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info1);

			this.btnOk.setText("Effectuer Débit exceptionnel");
			this.btnCancel.setText("Annuler débit exceptionnel");

			ObservableList<String> listTypesOpesPossibles1 = FXCollections.observableArrayList();
			listTypesOpesPossibles1.addAll(ConstantesIHM.OPERATIONS_DEBITEXCEPTIONNEL_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles1);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
			
		/**
		* @author Prescilla Estrade 
	 	* Case Crédit compte		 			 	
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
		* Modifie le texte des boutons btnOk et btnCancel par "Effectuer Crédit " et "Annuler crédit"
	 	*/			
		case CREDIT:
			String info2 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info2);

			this.btnOk.setText("Effectuer Crédit");
			this.btnCancel.setText("Annuler crédit");

			ObservableList<String> listTypesOpesPossibles2 = FXCollections.observableArrayList();
			listTypesOpesPossibles2.addAll(ConstantesIHM.OPERATIONS_CREDIT_GUICHET);

			this.cbTypeOpe.setItems(listTypesOpesPossibles2);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;			

		case VIREMENT:

			String info3 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info3);
			this.btnOk.setText("Effectuer Virement");
			this.btnCancel.setText("Annuler Virement");
			operationTypeLabel.setText("Comptes Clients");

			ObservableList<String> listTypesOpesPossibles3 = FXCollections.observableArrayList();

			Access_BD_CompteCourant cptAll = new Access_BD_CompteCourant();
			try {
				this.listComptesClient = cptAll.getCompteCourants(this.compteEdite.idNumCli);
				for (int i = 0; i < this.listComptesClient.size(); i++) {
					if (this.listComptesClient.get(i).idNumCompte != this.compteEdite.idNumCompte
							&& !this.listComptesClient.get(i).estCloture.equals("O")) {
						listTypesOpesPossibles3.add(this.listComptesClient.get(i).toString());
					}
				}
			} catch (DataAccessException | DatabaseConnexionException e) {
				e.printStackTrace();
			}

			this.cbTypeOpe.setItems(listTypesOpesPossibles3);
			this.cbTypeOpe.getSelectionModel().select(0);
			break;
		}

		this.primaryStage.showAndWait();
		return this.operationResultat;
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
	private Label lblMontant;
	@FXML
	private ComboBox<String> cbTypeOpe;
	@FXML
	private TextField txtMontant;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;
	@FXML
	private Label operationTypeLabel;

	@FXML
	private void doCancel() {
		this.operationResultat = null;
		this.primaryStage.close();
	}

	@FXML
	private void doAjouter() {
		switch (this.categorieOperation) {
		/**
		* @author Prescilla Estrade 
	 	* Case Débit compte		 	
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
	 	*/	
		case DEBIT:
			// règles de validation d'un débit :
			// - le montant doit être un nombre valide (supérieur à 0)
			// - et si l'utilisateur n'est pas chef d'agence,
			// - le débit ne doit pas amener le compte en dessous de son découvert autorisé
			double montant;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info);

			try {
				montant = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			if (this.compteEdite.solde - montant < this.compteEdite.debitAutorise) {
				info = "Dépassement du découvert ! - Cpt. : " + this.compteEdite.idNumCompte + "  "
						+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
						+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
				this.lblMessage.setText(info);
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.lblMessage.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				
				Alert alert = new Alert (AlertType.INFORMATION);
				alert.setTitle("Erreur débit");
				alert.setContentText("Impossible d'effectuer le débit, le montant est supérieur au découvert autorisé !");
				alert.showAndWait();
				
				return;
			}
			
			String typeOp = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montant, null, null, this.compteEdite.idNumCli, typeOp);
			this.primaryStage.close();
			break;
			
		/**
		* @author Prescilla Estrade 
	 	* Case Débit exceptionnel compte		 	
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
		*/	
		case DEBITEXCEPTIONNEL:
			// règles de validation d'un débit :
			// - le montant doit être un nombre valide (supérieur à 0)
			// - et si l'utilisateur n'est pas chef d'agence,
			// - le débit ne doit pas amener le compte en dessous de son découvert autorisé,
			double montant1;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info1 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info1);

			try {
				montant1 = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant1 <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			String typeOp1 = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montant1, null, null, this.compteEdite.idNumCli, typeOp1);
			this.primaryStage.close();
			break;

		/**
		* @author Prescilla Estrade 
	 	* Case Crédit compte		 	
	 	* Affiche les informations du compte (numéro de compte, solde et débit autorisé) dans le label lblMessage
		*/	
		case CREDIT:
			// règles de validation d'un crédit :
			// - le montant doit être un nombre valide (supérieur à 0)

			double montant2;

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info2 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info2);

			try {
				montant2 = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant2 <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				
				Alert alert = new Alert (AlertType.INFORMATION);
				alert.setTitle("Erreur crédit");
				alert.setContentText("Impossible d'effectuer le crédit, le montant est inférieur à 0 !");
				alert.showAndWait();
				
				return;
			}
			String typeOp2 = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montant2, null, null, this.compteEdite.idNumCli, typeOp2);
			this.primaryStage.close();
			break;

		case VIREMENT:

			this.txtMontant.getStyleClass().remove("borderred");
			this.lblMontant.getStyleClass().remove("borderred");
			this.lblMessage.getStyleClass().remove("borderred");
			String info3 = "Cpt. : " + this.compteEdite.idNumCompte + "  "
					+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
					+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
			this.lblMessage.setText(info3);

			try {
				montant1 = Double.parseDouble(this.txtMontant.getText().trim());
				if (montant1 <= 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			if (this.compteEdite.solde - montant1 < this.compteEdite.debitAutorise) {
				info1 = "Dépassement du découvert ! - Cpt. : " + this.compteEdite.idNumCompte + "  "
						+ String.format(Locale.ENGLISH, "%12.02f", this.compteEdite.solde) + "  /  "
						+ String.format(Locale.ENGLISH, "%8d", this.compteEdite.debitAutorise);
				this.lblMessage.setText(info1);
				this.txtMontant.getStyleClass().add("borderred");
				this.lblMontant.getStyleClass().add("borderred");
				this.lblMessage.getStyleClass().add("borderred");
				this.txtMontant.requestFocus();
				return;
			}
			CompteCourant compte = null;
			for (int i = 0; i < this.listComptesClient.size(); i++) {
				if (this.listComptesClient.get(i).toString()
						.equals(this.cbTypeOpe.getSelectionModel().getSelectedItem())) {
					compte = this.listComptesClient.get(i);
				}
			}

			String typeOp3 = this.cbTypeOpe.getValue();
			this.operationResultat = new Operation(-1, montant1, null, null, this.compteEdite.idNumCli, typeOp3);
			this.primaryStage.close();
			break;

		}
	}
}
