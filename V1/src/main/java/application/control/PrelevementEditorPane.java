/**
 * Cette classe permet d'appeler le fichier FXML afin d'afficher la fenêtre.
 * Elle gère l'affichage de la fenêtre et appelle une méthode pour gérer les textes
 * @author Julien Bernard
 */
package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.CompteEditorPaneController;
import application.view.PrelevementEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Prelevement;

public class PrelevementEditorPane {
	
	private Stage primaryStage;
	private PrelevementEditorPaneController pepcViewController;
	private DailyBankState dailyBankState;

	public PrelevementEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(CompteEditorPaneController.class.getResource("prelevementeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un prélèvement");
			this.primaryStage.setResizable(false);

			this.pepcViewController = loader.getController();
			this.pepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Appelle la méthode displayDialog pour gérer l'affichage de la fenêtre
	 */
	public Prelevement doPrelevementEditorDialog(Prelevement p, EditionMode em) {
		return this.pepcViewController.displayDialog(p, em);
	}
}
