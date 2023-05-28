package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.EmpruntController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Prescilla Estrade
 * Classe gérant l'affichage de la fenêtre d'informations d'un emprunt
 */
public class Emprunt {

	
	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmpruntController EmpruntController;

	public Emprunt (Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmpruntController.class.getResource("emprunt.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Informations emprunt");
			this.primaryStage.setResizable(false);

			this.EmpruntController = loader.getController();
			this.EmpruntController.initContext(this.primaryStage, _dbstate);
			this.primaryStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}
