package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneController;
import application.view.ClientsManagementController;
import application.view.EmployeEditorPaneController;
import application.view.EmployesManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Employe;

/**
 * Classe qui gère le contrôleur de la page de gestion des clients(ajout et modification)
*/
public class EmployeEditorPane {

	private Stage primaryStage;
	private EmployeEditorPaneController eepcViewController;
	private DailyBankState dailyBankState;

	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployesManagementController.class.getResource("employeeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un employe");
			this.primaryStage.setResizable(false);

			this.eepcViewController = loader.getController();
			this.eepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Démarre la fonction de contrôleur de la page ajoutée ou modifiée des clients
	 * @param client : Client qui est modifié
	 * @param em : Indique le mode d'édition (ajouter, modifier, supprimer) 
	 * @return Si le client a changé 
	 */
	public Employe doEmployeEditorDialog(Employe employe, EditionMode em) {
		return this.eepcViewController.displayDialog(employe, em);
	}
}