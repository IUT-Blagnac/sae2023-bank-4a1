package application.tools;

/**
 * Catégories possibles de opérations dans l'application.
 *
 * Simplifie et limite les accès à la BD. Implicite en BD : un type opération
 * est un cas particulier de DEBIT, DEBITEXCEPTIONNEL, CREDIT ou VIREMENT.
 *@author Julien Bernard, Prescilla Estrade
 */
public enum CategorieOperation {
	DEBIT, DEBITEXCEPTIONNEL, CREDIT, VIREMENT
}
