package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmployesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;


public class EmployesManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmployesManagementController emcViewController;
        /**
         * Constructeur de la classe EmployesManagement
         * 
         * @param _parentStage IN : Stage parent
         * @param _dbstate     IN : Etat de l'application
         * 
         * @return void
         */
	public EmployesManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementController.class.getResource("employesmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des employes");
			this.primaryStage.setResizable(false);

			this.emcViewController = loader.getController();
		//	this.emcViewController.initContext(this.primaryStage, this, _dbstate);

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
	 * @param e : IN L'employé à modifier
	 * @return l'employé modifié
	 */
	public Employe modifierEmploye(Employe emp) {
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		Employe result = cep.doEmployeEditorDialog(emp, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();
				ac.updateEmploye(result);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}
	/**
	 * Affiche la fenêtre de gestion des employes
	 * 
	 * @return le nouveau employe
	 */
	public Employe nouveauEmploye() {
		Employe employe;
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		employe = cep.doEmployeEditorDialog(null, EditionMode.CREATION);
		if (employe != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();

				ac.insertEmploye(employe);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				employe = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				employe = null;
			}
		}
		return employe;
	}
	
	/**
	 * Affiche la fenêtre de gestion des employes
	 * 
	 * @param _numCompte   IN : numéro d'employé
	 * @param _debutNom    IN : prénom de l'employé
	 * @param _debutPrenom IN : nom de l'employé     
	 * 
	 * @return une liste d'employes
	 */
	public ArrayList<Employe> getlisteEmployes() {
		ArrayList<Employe> listeEmp = new ArrayList<>();
		try {

			Access_BD_Employe ac = new Access_BD_Employe();
			listeEmp = ac.getEmployes(this.dailyBankState.getEmployeActuel().idAg);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeEmp = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeEmp = new ArrayList<>();
		}
		return listeEmp;
	}
}
