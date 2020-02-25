package siap.bdmc.statoprenotazionibdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: StatoPrenotazioniBdmcDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella StatoPrenotazioniBdmc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class StatoPrenotazioniBdmcDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public StatoPrenotazioniBdmcDAO(Connection con) {
		super(con);
		setTable("STATO_PRENOTAZIONI_BDMC");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("STATO_PRENOTAZIONI_BDMC", "SEQ_STATO_PRENOTAZIONI_BDMC");

		// setField("STATO_PRENOTAZIONI_BDMC", BIG_DECIMAL);
		setField("ID_MISURA_CAUTELARE_BDMC", BIG_DECIMAL);
		setField("DATA_TRASMISSIONE", DATE);
		setField("ESITO_ID", BIG_DECIMAL);
		setField("ESITO_MSG", STRING);
		setField("ID_PRENOTAZIONE", BIG_DECIMAL);
		setField("PROG_PERI_PRES", BIG_DECIMAL);
		setField("TIPO_TRASMISSIONE", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getStatoPrenotazioniBdmc() throws DAOException {
		return getBigDecimal("STATO_PRENOTAZIONI_BDMC");
	}

	public BigDecimal getIdMisuraCautelareBdmc() throws DAOException {
		return getBigDecimal("ID_MISURA_CAUTELARE_BDMC");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public BigDecimal getEsitoId() throws DAOException {
		return getBigDecimal("ESITO_ID");
	}

	public String getEsitoMsg() throws DAOException {
		return getString("ESITO_MSG");
	}

	public BigDecimal getIdPrenotazione() throws DAOException {
		return getBigDecimal("ID_PRENOTAZIONE");
	}

	public BigDecimal getProgPeriPres() throws DAOException {
		return getBigDecimal("PROG_PERI_PRES");
	}

	public String getTipoTrasmissione() throws DAOException {
		return getString("TIPO_TRASMISSIONE");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setStatoPrenotazioniBdmc(BigDecimal aValore) {
		setBigDecimal("STATO_PRENOTAZIONI_BDMC", aValore);
	}

	public void setIdMisuraCautelareBdmc(BigDecimal aValore) {
		setBigDecimal("ID_MISURA_CAUTELARE_BDMC", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setEsitoId(BigDecimal aValore) {
		setBigDecimal("ESITO_ID", aValore);
	}

	public void setEsitoMsg(String aValore) {
		setString("ESITO_MSG", aValore);
	}

	public void setIdPrenotazione(BigDecimal aValore) {
		setBigDecimal("ID_PRENOTAZIONE", aValore);
	}

	public void setProgPeriPres(BigDecimal aValore) {
		setBigDecimal("PROG_PERI_PRES", aValore);
	}

	public void setTipoTrasmissione(String aValore) {
		setString("TIPO_TRASMISSIONE", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new StatoPrenotazioniBdmcModel(getStatoPrenotazioniBdmc(), getIdMisuraCautelareBdmc(),
				getDataTrasmissione(), getEsitoId(), getEsitoMsg(), getIdPrenotazione(), getProgPeriPres(),
				getTipoTrasmissione());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(StatoPrenotazioniBdmcModel aModel) throws DAOException {
		setStatoPrenotazioniBdmc(aModel.getStatoPrenotazioniBdmc());
		setIdMisuraCautelareBdmc(aModel.getIdMisuraCautelareBdmc());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setEsitoId(aModel.getEsitoId());
		setEsitoMsg(aModel.getEsitoMsg());
		setIdPrenotazione(aModel.getIdPrenotazione());
		setProgPeriPres(aModel.getProgPeriPres());
		setTipoTrasmissione(aModel.getTipoTrasmissione());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(StatoPrenotazioniBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getStatoPrenotazioniBdmc() != null) {
			lCondizioni += " and STATO_PRENOTAZIONI_BDMC = " + aModel.getStatoPrenotazioniBdmc() + "";
		}
		if (aModel.getIdMisuraCautelareBdmc() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aModel.getIdMisuraCautelareBdmc() + "";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getEsitoId() != null && aModel.getEsitoId().compareTo(new BigDecimal(0)) == 0) {
			lCondizioni += " and ESITO_ID = " + aModel.getEsitoId() + "";
		}
		if (aModel.getEsitoId() != null && aModel.getEsitoId().compareTo(new BigDecimal(0)) != 0) {
			lCondizioni += " and ESITO_ID <> 0 "; // + aModel.getEsitoId() + "";
		}
		if (aModel.getEsitoMsg() != null && aModel.getEsitoMsg().length() > 0) {
			lCondizioni += " and ESITO_MSG = '" + aModel.getEsitoMsg() + "' ";
		}
		if (aModel.getIdPrenotazione() != null) {
			lCondizioni += " and ID_PRENOTAZIONE = " + aModel.getIdPrenotazione() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getTipoTrasmissione() != null && aModel.getTipoTrasmissione().length() > 0) {
			lCondizioni += " and TIPO_TRASMISSIONE = '" + aModel.getTipoTrasmissione() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aStatoPrenotazioniBdmc) {
		String lCondizioni = new String();

		lCondizioni += " and STATO_PRENOTAZIONI_BDMC = " + aStatoPrenotazioniBdmc;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di order by per la ricerca
	 * 
	 *****************************************************************************/
	public void setOrderBy() {
		String orderBy = "";
		// =======================================================================
		// Lasciare orderBy="" se non si vuole scegliere un ordinamento,
		// altrimenti elencare i campi separati da virgola
		// n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente
		// =======================================================================

		setOrder(orderBy);
	}

}