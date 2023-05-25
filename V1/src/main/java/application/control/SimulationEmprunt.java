package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.SimulationEmpruntController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimulationEmprunt {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private SimulationEmpruntController simulation_emprunt;
		
	public SimulationEmprunt (Stage _parentStage, DailyBankState _dbstate, double capitalEmp, int dureeEmp, double tauxEmp, double tauxAss) {

		try {
			FXMLLoader loader = new FXMLLoader(SimulationEmpruntController.class.getResource("simulationemprunt.fxml"));
			ScrollPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth()+90, root.getPrefHeight()+60);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Simulation emprunt");
			this.primaryStage.setResizable(false);
			
			HBox hb = new HBox ();
			
			this.simulation_emprunt = loader.getController();
			this.simulation_emprunt.initContext(this.primaryStage, _dbstate);
			
			Text [] tab = this.simulation_emprunt.doEmprunt(capitalEmp, tauxEmp /12 / 100, dureeEmp, tauxAss);

			for (Text text : tab) {
				hb.getChildren().add(text);
			}

			root.setContent(hb);
			this.primaryStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
