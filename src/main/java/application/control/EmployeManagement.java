package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import application.view.EmployesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Employe;
import model.orm.Access_BD_Client;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;


public class EmployeManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmployesManagementController emcViewController;
        /**
         * Constructeur de la classe ClientsManagement
         * 
         * @param _parentStage IN : Stage parent
         * @param _dbstate     IN : Etat de l'application
         * 
         * @return void
         */
	public EmployeManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementController.class.getResource("employesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des clients");
			this.primaryStage.setResizable(false);

			this.emcViewController = loader.getController();
			this.emcViewController.initContext(this.primaryStage, this, _dbstate, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        /**
	 * Affiche la fenêtre de gestion des employes
	 */
	public void doEmployeManagementDialog() {
		this.emcViewController.displayDialog();
	}
        /**
	 * Affiche la fenêtre de gestion des employes
	 * 
	 * @param c : IN Le client à modifier
	 * @return le client modifié
	 */ 
	public Employe modifierEmployes(Employe e) {
		EmployeEditorPane eep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		Employe result = eep.doEmployeEditorDialog(e, EditionMode.MODIFICATION);
		if (result != null) {
			Access_BD_Employe ae = new Access_BD_Employe();
			ae.updateEmploye(result);
		}
		return result;
	}
	/**
	 * Affiche la fenêtre de gestion des clients
	 * 
	 * @return le nouveau client
	 */
	public Client nouveauClient() {
		Client client;
		ClientEditorPane cep = new ClientEditorPane(this.primaryStage, this.dailyBankState);
		client = cep.doClientEditorDialog(null, EditionMode.CREATION);
		if (client != null) {
			try {
				Access_BD_Client ac = new Access_BD_Client();

				ac.insertClient(client);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				client = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				client = null;
			}
		}
		return client;
	}
	
	/**
	 * Affiche la fenêtre de gestion des comptes d'un client
	 * 
	 * @param c IN : client à gérer
	 */
	public void gererComptesClient(Client c) {
		ComptesManagement cm = new ComptesManagement(this.primaryStage, this.dailyBankState, c);
		cm.doComptesManagementDialog();
	}
	
	/**
	 * Affiche la fenêtre de gestion des comptes d'un client
	 * 
	 * @param _numCompte   IN : numéro de compte
	 * @param _debutNom    IN : prénom du client
	 * @param _debutPrenom IN : nom du client
	 * 
	 * @return une liste de client
	 */
	public ArrayList<Client> getlisteComptes(int _numCompte, String _debutNom, String _debutPrenom) {
		ArrayList<Client> listeCli = new ArrayList<>();
		try {
			// Recherche des clients en BD. cf. AccessClient > getClients(.)
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 et debutNom non vide => recherche nom/prenom
			// numCompte == -1 et debutNom vide => recherche tous les clients

			Access_BD_Client ac = new Access_BD_Client();
			listeCli = ac.getClients(this.dailyBankState.getEmployeActuel().idAg, _numCompte, _debutNom, _debutPrenom);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeCli = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCli = new ArrayList<>();
		}
		return listeCli;
	}
}
