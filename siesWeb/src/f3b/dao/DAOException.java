package f3b.dao;

import java.sql.SQLException;

/**
 * <p>
 * Title: DAOException
 * </p>
 * <p>
 * Description: Gestione errori di eccezioni degli oggetti DAO, eredita <code>SQLException</code>
 * </p>
 * <p>
 * Company: BULL Italia S.p.A.
 * </p>
 */
public class DAOException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3304670331086723391L;

	/**
	 * Membro di classe <code>public</code>, per default impostato a <code>false</code>. Indica con un valore
	 * <code>boolean</code> il verificarsi dell'omonimo errore.
	 */
	public boolean UNIQUE_CONSTRAINT_VIOLATED = false;

	/**
	 * Membro di classe <code>public</code>, per default impostato a <code>false</code>. Indica con un valore
	 * <code>boolean</code> il verificarsi dell'omonimo errore.
	 */
	public boolean INTEGRITY_CONSTRAINT_VIOLATED = false;

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public DAOException(String aMessage) {
		super(aMessage);
	}

	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 * @param aCode
	 *            codice di errore.
	 */
	public DAOException(String aMessage, int aCode) {
		super(aMessage, "", aCode);
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aExc
	 *            errore <code>SQLException</code>.
	 */
	public DAOException(SQLException aExc) {
		super(aExc.getMessage(), aExc.getSQLState(), aExc.getErrorCode());

		switch (this.getErrorCode()) {
		case 1: // Codice errore SQL.
			UNIQUE_CONSTRAINT_VIOLATED = true;
			break;

		case 2291: // STUB 19/04/2005 Codice errore SQL.
			INTEGRITY_CONSTRAINT_VIOLATED = true;
			break;

		case 2292: // Codice errore SQL.
			INTEGRITY_CONSTRAINT_VIOLATED = true;
			break;

		default:
		}
	}

}