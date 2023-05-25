package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.AssuranceEmpruntController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AssuranceEmprunt {

	
	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private AssuranceEmpruntController AssuranceEmpruntController;

	public AssuranceEmprunt (Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(AssuranceEmpruntController.class.getResource("assuranceemprunt.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Informations assurance emprunt");
			this.primaryStage.setResizable(false);

			this.AssuranceEmpruntController = loader.getController();
			this.AssuranceEmpruntController.initContext(this.primaryStage, _dbstate);
			this.primaryStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}
