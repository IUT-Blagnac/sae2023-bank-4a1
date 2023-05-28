/**
 * Permet de gérer les prélèvements selon les boutons cliquer.
 * @author Julien Bernard
 */
package application.view;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.ComptesManagement;
import application.control.PrelevementManagement;
import application.tools.StageManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Employe;
import model.data.Prelevement;

public class PrelevementManagementController {

	private DailyBankState dailyBankState;
	private PrelevementManagement cmDialogController;
	private Stage primaryStage;
	private ObservableList<Prelevement> oListPrelevements;
	private CompteCourant compte;

	public void initContext(Stage _containingStage, PrelevementManagement _cm, DailyBankState _dbstate, CompteCourant compteConcerne) {
		this.cmDialogController = _cm;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.compte = compteConcerne;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.oListPrelevements = FXCollections.observableArrayList();
		this.lvPrelevements.setItems(this.oListPrelevements);
		this.lvPrelevements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvPrelevements.getFocusModel().focus(-1);
		this.lvPrelevements.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		this.loadList();
		this.validateComponentState();
	}

	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	@FXML
	private TextField txtIdPrel;
	@FXML
	private ListView<Prelevement> lvPrelevements;
	@FXML
	private Button btnSupprPrel;
	@FXML
	private Button btnModifPrel;

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	
	/**
	 * Recherche les prélèvements avec ou sans un id précisé
	 * @author Julien Bernard
	 */
	@FXML
	private void doRechercher() {
		int idPrel;
		System.out.println("PrelevementManagementController rechercher : " + this.compte.idNumCompte);
		try {
			String nc = this.txtIdPrel.getText();
			if (nc.equals("")) {
				idPrel = -1;
			} else {
				idPrel = Integer.parseInt(nc);
			}
		} catch (NumberFormatException nfe) {
			idPrel = -1;
		}

		ArrayList<Prelevement> listePrels;
		listePrels = this.cmDialogController.getlistePrelevements(idPrel);
		
		this.oListPrelevements.clear();
		this.oListPrelevements.addAll(listePrels);
		this.loadList();
		this.validateComponentState();
	}
	
	/**
	 * Modifie le prélèvement actuel en appelant la méthode et fenêtre nécessaire
	 * @author Julien Bernard
	 */
	@FXML
	private void doModifierPrelevement() {

		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Prelevement prelMod = this.oListPrelevements.get(selectedIndice);
			Prelevement result = this.cmDialogController.modifierPrelevement(prelMod);
			if (result != null) {
				this.oListPrelevements.set(selectedIndice, result);
			}
		}
		this.loadList();
		this.validateComponentState();
	}
	
	/**
	 * Supprime le prélèvement en cliquant sur le bouton
	 * @author Julien Bernard
	 */
	@FXML
	private void doSupprPrelevement() {
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Prelevement prel = this.oListPrelevements.get(selectedIndice);
			this.cmDialogController.supprimerPrelevement(prel.idPrelev);
		}
		this.loadList();
		this.validateComponentState();
	}
	
	/**
	 * Créer un nouveau prélèvement en appelant la méthode et fenêtre nécessaire
	 * @author Julien Bernard
	 */
	@FXML
	private void doNouveauPrelevement() {
		Prelevement prel = new Prelevement(0,0,0,"",this.compte.idNumCompte);
		prel = this.cmDialogController.nouveauPrelevement();
		if (prel != null) {
			this.oListPrelevements.add(prel);
		}
		this.loadList();
		this.validateComponentState();
	}

	private void loadList() {
		int idPrelev;
		try {
			idPrelev = Integer.parseInt(this.txtIdPrel.getText());
		} catch (Exception e) {
			idPrelev = -1;
		}

		ArrayList<Prelevement> listePrel;
		listePrel = this.cmDialogController.getlistePrelevements(idPrelev);
		this.oListPrelevements.clear();
		this.oListPrelevements.addAll(listePrel);
	}

	private void validateComponentState() {
		int selectedIndice = this.lvPrelevements.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifPrel.setDisable(false);
			this.btnSupprPrel.setDisable(false);
		} else {
			this.btnModifPrel.setDisable(true);
			this.btnSupprPrel.setDisable(true);
		}
	}
}
