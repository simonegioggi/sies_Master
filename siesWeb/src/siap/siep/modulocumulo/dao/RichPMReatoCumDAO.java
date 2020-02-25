package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMReatoCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichPM_Reato_Cum</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;

public class RichPMReatoCumDAO extends TableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public RichPMReatoCumDAO(Connection con) {
		super(con);
		setTable("RICHPM_REATO_CUM");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		// setSequenceField("ID_TITOLO_CUMULATO","TITOLO_CUMULATO_SEQ");

		// setField("ID_TITOLO_CUMULATO", BIG_DECIMAL);
		setField("RIC_ID_RICHIESTE_PM_IN_CUMULO", BIG_DECIMAL);
		setField("REA_ID_REATO_CUMULO", BIG_DECIMAL);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getRichIdRichiestePMinCumulo() throws DAOException {
		return getBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO");
	}

	public BigDecimal getReaIdReatoCumulo() throws DAOException {
		return getBigDecimal("REA_ID_REATO_CUMULO");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setRichIdRichiestePMinCumulo(BigDecimal aValore) {
		setBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO", aValore);
	}

	public void setReaIdReatoCumulo(BigDecimal aValore) {
		setBigDecimal("REA_ID_REATO_CUMULO", aValore);
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

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	// public GenericModel getModel() throws DAOException {
	// return new TitoloCumulatoModel(
	// getIdTitoloCumulato() ,
	// getAnnoRegePm()
	// );
	// }

}
