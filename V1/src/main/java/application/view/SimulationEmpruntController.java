package application.view;
import application.DailyBankState;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SimulationEmpruntController {

	private Stage primaryStage;
	private DailyBankState dbs;

	public void initContext(Stage _primaryStage, DailyBankState _dbstate) {
		this.primaryStage=_primaryStage;
		this.dbs=_dbstate;
	}
	
	public Text[] doEmprunt(double capitalEmp, double tauxEmp, int dureeEmp, double tauxAss) {
		
		Text tab [] = new Text [8];
		
		double capitalDeb=capitalEmp;
		double capitalFin=capitalEmp;
		double montantAss=(tauxEmp*capitalEmp)/12;
		double mensualite=(capitalEmp*tauxEmp*Math.pow(1+tauxEmp, dureeEmp)/(Math.pow(1+tauxEmp, dureeEmp)-1));
		
		String[] text = {"Mois\n\n\n", "Capital restant du\nen début de mois\n\n", "Montant des intérêts\n\n\n", "Montant du principal\n\n\n",
		"Montant à rembourser \nmensualité sans assurance\n\n", "Coût assurance\n\n\n", "Montant à rembourser \nmensualité avec assurance\n\n",
		"Capital restant du\nen fin de mois\n\n"
		};
		
		for (int mois=1; mois<dureeEmp; mois++) {

			if (capitalDeb!=capitalFin)
				capitalDeb=capitalFin;
			
			double montantInterets=capitalDeb*tauxEmp;
			double montantPrincipal=mensualite-montantInterets;
			
			capitalFin-=montantPrincipal;
			
			text[0]+=mois+"\n";
			text[1]+=(double)Math.round(capitalDeb*100)/100+"\n";
			text[2]+=(double)Math.round(montantInterets*100)/100+"\n";
			text[3]+=(double)Math.round(montantPrincipal*100)/100+"\n";
			text[4]+=(double)Math.round(mensualite*100)/100+"\n";
			text[5]+=(double)Math.round(montantAss*100)/100+"\n";
			text[6]+=(double)Math.round ((mensualite+montantAss)*100)/100+"\n";
			text[7]+=(double)Math.round(capitalFin*100)/100+"\n";	
		}
			
		for (int i=0; i<tab.length; i++) {
	        Text text1 = new Text();
	        text1.setText(text[i]);
	        tab[i]=text1;
	    }
		return tab;
	}	
}