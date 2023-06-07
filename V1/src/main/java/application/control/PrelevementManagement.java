/**
 * Cette classe permet d'utiliser les méthodes nécessaires selon l'action sur les prélèvements.
 * @author Julien Bernard
 */
package application.control;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import com.itextpdf.text.DocumentException;
import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import application.tools.PairsOfValue;
import application.tools.StageManagement;
import application.view.ComptesManagementController;
import application.view.OperationsManagementController;
import application.view.PrelevementManagementController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Employe;
import model.data.Operation;
import model.data.Prelevement;
import model.orm.Access_BD_CompteCourant;
import model.orm.Access_BD_Employe;
import model.orm.Access_BD_Operation;
import model.orm.Access_BD_Prelevement;
import model.orm.exception.ApplicationException;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;
import oracle.sql.DATE;
import java.lang.Object;
import com.itextpdf.text.pdf.PdfPTable;

public class PrelevementManagement {

	private Stage primaryStage;
	private PrelevementManagementController pmcViewController;
	private DailyBankState dailyBankState;
	private CompteCourant compteCourant;

	public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate, CompteCourant compte) {

		this.compteCourant = compte;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(PrelevementManagementController.class.getResource("prelevementmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des prélèvements");
			this.primaryStage.setResizable(false);

			this.pmcViewController = loader.getController();
			this.pmcViewController.initContext(this.primaryStage, this, _dbstate, this.compteCourant);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPrelevementManagementDialog() {
		this.pmcViewController.displayDialog();
	}
	
	/**
	 * Affiche le texte pour la création d'un prélèvement
	 * Appelle la méthode insertPrelevement afin de l'ajouter à la BD
	 * @author Julien Bernard
	 */
	public Prelevement nouveauPrelevement() {
		Prelevement prelevement;
		prelevement = new Prelevement(0, 0.0, 0, "", this.compteCourant.idNumCompte);
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dailyBankState);
		prelevement = pep.doPrelevementEditorDialog(prelevement, EditionMode.CREATION);
		if (prelevement != null) {
			try {
				Access_BD_Prelevement ac = new Access_BD_Prelevement();
				ac.insertPrelevement(prelevement);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				prelevement = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				prelevement = null;
			}
		}
		return prelevement;
	}
	
	/**
	 * Affiche le texte pour modifier un prélèvement
	 * Appelle la méthode updatePrelevement afin d'ajouter les modifications à la BD
	 * @author Julien Bernard
	 */
	public Prelevement modifierPrelevement(Prelevement prelevementModif) {
		PrelevementEditorPane cep = new PrelevementEditorPane(this.primaryStage, this.dailyBankState);
		Prelevement result = cep.doPrelevementEditorDialog(prelevementModif, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Prelevement ac = new Access_BD_Prelevement();
				ac.updatePrelevement(result);
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
	 * Supprime en un clique le prélèvement de la BD en appelant la méthode supprimerPrelevement
	 * @author Julien Bernard
	 */
	public void supprimerPrelevement(int idPrelevement) {
			try {
				Access_BD_Prelevement acp = new Access_BD_Prelevement();
	          	acp.supprimerPrelevement(idPrelevement); 
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
			}
	}
	
	public ArrayList<Prelevement> getlistePrelevements(int idPrelevement) {
		ArrayList<Prelevement> listePrels = new ArrayList<>();
		try {
			int idNumCompte = this.compteCourant.idNumCompte;
			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			listePrels = ac.getPrelevements(idPrelevement, idNumCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listePrels = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listePrels = new ArrayList<>();
		}
		return listePrels;
	}
}
