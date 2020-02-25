package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMBeneficioCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichPM_Beneficio_Cum</p>
* <p>Company: Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;

public class RichPMBeneficioCumDAO extends TableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public RichPMBeneficioCumDAO(Connection con) {
		super(con);
		setTable("RICHPM_BENEFICIO_CUM");

		// Tab di Conversione, NON esiste Sequence

		setField("RIC_ID_RICHIESTE_PM_IN_CUMULO", BIG_DECIMAL);
		setField("BEN_ID_BENEFICIO_CUM", BIG_DECIMAL);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getRichIdRichiestePMinCumulo() throws DAOException {
		return getBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO");
	}

	public BigDecimal getBenIdBeneficioCumulo() throws DAOException {
		return getBigDecimal("BEN_ID_BENEFICIO_CUM");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setRichIdRichiestePMinCumulo(BigDecimal aValore) {
		setBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO", aValore);
	}

	public void setBenIdBeneficioCumulo(BigDecimal aValore) {
		setBigDecimal("BEN_ID_BENEFICIO_CUM", aValore);
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

	public void ricercaRichPmBeneficioCumByRichIdRich(BigDecimal aIdRichiestePmInCumulo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByRichIdRich(aIdRichiestePmInCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "RIC_ID_RICHIESTE_PM_IN_CUMULO, " + "BEN_ID_BENEFICIO_CUM ";
		lStatement += " FROM RICHPM_BENEFICIO_CUM";

		return lStatement;
	}

	public String setCondizioniByRichIdRich(BigDecimal aIdRichiestePmInCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

}
