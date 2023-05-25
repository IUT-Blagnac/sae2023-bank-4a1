package application.view;

import application.DailyBankState;
import application.control.SimulationEmprunt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EmpruntController { 

	private Stage primaryStage;
	private DailyBankState dbs;
	
	public void initContext(Stage _primaryStage, DailyBankState _dbstate) {
		this.primaryStage=_primaryStage;
		this.dbs=_dbstate;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}
	
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}
	
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	
	@FXML
	private Button butCancel;
	@FXML
	private TextField montantEmp;
	@FXML
	private TextField dureeEmp;
	@FXML
	private TextField tauxEmp;
	@FXML
	private TextField tauxAss;
	
	@FXML
	public void actionSimulerEmprunt () {
		
		try {
			double capitalEmprunt=Double.parseDouble(this.montantEmp.getText());
			int dureeEmprunt=Integer.parseInt(this.dureeEmp.getText());
			double tauxEmprunt=Double.parseDouble(this.tauxEmp.getText());
			double tauxAssurance=Double.parseDouble(this.tauxAss.getText());
			
			if(capitalEmprunt<=0 || dureeEmprunt<=0 || tauxEmprunt<=0 || tauxEmprunt>100 || tauxAssurance<0 || tauxAssurance>100){
				throw new NumberFormatException();
			}else{
				new SimulationEmprunt (primaryStage, dbs, capitalEmprunt, dureeEmprunt, tauxEmprunt, tauxAssurance);
			}
			
		}catch (NumberFormatException nfe){
			Alert alert = new Alert (AlertType.INFORMATION);
			alert.setTitle("Erreur valeurs");
			alert.setContentText("Les valeurs doivent être supérieures à 0 et au maximum égales à 100\n Excepté le taux d'assurance compris entre 0 et 100");
			alert.showAndWait();
		}
	}
	
}