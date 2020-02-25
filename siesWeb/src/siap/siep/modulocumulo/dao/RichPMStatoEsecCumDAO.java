package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMPenAccCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichPM_Stato_Esec_Cum</p>
* <p>Company: Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;

public class RichPMStatoEsecCumDAO extends TableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public RichPMStatoEsecCumDAO(Connection con) {
		super(con);
		setTable("RICHPM_STATO_ESEC_CUM");

		// setField("ID_TITOLO_CUMULATO", BIG_DECIMAL);
		setField("RIC_ID_RICHIESTE_PM_IN_CUMULO", BIG_DECIMAL);
		setField("STAT_ID_STATO_ESEC_TITOLO_CUM", BIG_DECIMAL);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getRichIdRichiestePMinCumulo() throws DAOException {
		return getBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO");
	}

	public BigDecimal getStatoIdStatoEsecTitCumulo() throws DAOException {
		return getBigDecimal("STAT_ID_STATO_ESEC_TITOLO_CUM");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setRichIdRichiestePMinCumulo(BigDecimal aValore) {
		setBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO", aValore);
	}

	public void setStatoIdStatoEsecTitCumulo(BigDecimal aValore) {
		setBigDecimal("STAT_ID_STATO_ESEC_TITOLO_CUM", aValore);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di Delete o update puntuale
	 * 
	 * @param key_Richiesta_PM_in_Cumulo
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aIdRichiestePmInCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

}
