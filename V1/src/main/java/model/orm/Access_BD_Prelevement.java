/**
 * Modifie directement la BD en relation avec les prélèvements.
 * @author Julien Bernard
 */
package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.data.Prelevement;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

public class Access_BD_Prelevement {

	public Access_BD_Prelevement() {
	}

	/**
	 * @author Julien Bernard
	 * Créer et ajoute prélèvement dans la BD
	 * 
	 * prel.montant doit être positif obligatoirement, prel.dateRecurrente doit être en 1 et 28
	 * prel.beneficiaire doit être référencé, prel.idNumCompte est automatiquement ajouté.
	 * 
	 * @param prel 									Informations du prélèvements									
	 * @throws RowNotFoundOrTooManyRowsException	Le prélèvement existe déjà 
	 * @throws DataAccessException					Erreur d'accès aux données (requête
	 *                                     			mal formée ou autre)
	 * @throws DatabaseConnexionException			Erreur de connexion
	 */
	public void insertPrelevement(Prelevement prel)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "INSERT INTO PRELEVEMENTAUTOMATIQUE VALUES (" + "seq_id_prelevAuto.NEXTVAL" + ", " + "?" + ", " + "?" + ", "
					+ "?" + ", " + "?" + ")";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setDouble(1, prel.montant);
			pst.setInt(2, prel.dateRecurrente);
			pst.setString(3, prel.beneficiaire);
			pst.setInt(4, prel.idNumCompte);


			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}

			query = "SELECT seq_id_prelevAuto.CURRVAL from DUAL";

			System.err.println(query);
			PreparedStatement pst2 = con.prepareStatement(query);

			ResultSet rs = pst2.executeQuery();
			rs.next();
			int numPrelBase = rs.getInt(1);

			con.commit();
			rs.close();
			pst2.close();

			prel.idPrelev = numPrelBase;
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur accès", e);
		}
	}
	
	/**
	 * @author Julien Bernard
	 * Supprime le prélèvement de la BD
	 * 
	 * prel.montant doit être positif obligatoirement, prel.dateRecurrente doit être en 1 et 28
	 * prel.beneficiaire doit être référencé, prel.idNumCompte est automatiquement ajouté.
	 * 
	 * @param idPrelev								(clé primaire) doit exister seul
	 * @throws RowNotFoundOrTooManyRowsException	Le prélèvement n'existe pas
	 * @throws DataAccessException					Erreur d'accès aux données (requête
	 *                                     			mal formée ou autre)
	 * @throws DatabaseConnexionException			Erreur de connexion
	 */
	public void supprimerPrelevement(int idPrelev)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "DELETE FROM PRELEVEMENTAUTOMATIQUE WHERE idPrelev = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idPrelev);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}

			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.INSERT, "Erreur accès", e);
		}

	}
	
	/**
	 * @author Julien Bernard
	 * Met à jour le prélèvement dans la BD
	 * 
	 * prel.montant doit être positif obligatoirement, prel.dateRecurrente doit être en 1 et 28
	 * prel.beneficiaire doit être référencé, prel.idNumCompte est automatiquement ajouté.
	 * 
	 * @param prel 									Informations du prélèvements									
	 * @throws RowNotFoundOrTooManyRowsException	Le prélèvement existe déjà 
	 * @throws DataAccessException					Erreur d'accès aux données (requête
	 *                                     			mal formée ou autre)
	 * @throws DatabaseConnexionException			Erreur de connexion
	 */
	public void updatePrelevement(Prelevement prel)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE PRELEVEMENTAUTOMATIQUE SET " + "montant = " + "? , " + "dateRecurrente = " + "? , " + "beneficiaire = "
					+ "?" + "WHERE idPrelev= ? ";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setDouble(1, prel.montant);
			pst.setInt(2, prel.dateRecurrente);
			pst.setString(3, prel.beneficiaire);
			pst.setInt(4, prel.idPrelev);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.UPDATE,
						"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.UPDATE, "Erreur accès", e);
		}
	}

	public ArrayList<Prelevement> getPrelevements(int idPrelev, int idNumCompte)
			throws DataAccessException, DatabaseConnexionException {

		ArrayList<Prelevement> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();

			PreparedStatement pst;
			String query;
			if (idPrelev != -1) {
				query = "SELECT * FROM PRELEVEMENTAUTOMATIQUE where idPrelev = ?" + " AND idNumCompte = ?";
				pst = con.prepareStatement(query);
				pst.setInt(1, idPrelev);
				pst.setInt(2, idNumCompte);
			} else {
				query = "SELECT * FROM PRELEVEMENTAUTOMATIQUE WHERE idNumCompte = ?";
				pst = con.prepareStatement(query);
				pst.setInt(1, idNumCompte);
			}
			System.err.println(query + " idPrelev : " + idPrelev);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idPrelTR = rs.getInt("idPrelev");
				double montant = rs.getDouble("montant");
				String beneficiaire = rs.getString("beneficiaire");
				int date = rs.getInt("dateRecurrente");
				int idNumCompte2 = rs.getInt("idNumCompte");
				alResult.add(
						new Prelevement(idPrelTR, montant, date, beneficiaire, idNumCompte2));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}
}
