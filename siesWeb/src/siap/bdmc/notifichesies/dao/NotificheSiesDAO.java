package siap.bdmc.notifichesies.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.notifichesies.model.NotificheSiesModel;
//import f3b.dao.TableOracleDAO;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: NotificheSiesDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella NotificheSies
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
public class NotificheSiesDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public NotificheSiesDAO(Connection con) {
		super(con);
		setTable("NOTIFICHE_SIES");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_NOTIFICHE_SIES", "SEQ_NOTIFICHE_SIES");

		// setField("ID_NOTIFICHE_SIES", BIG_DECIMAL);
		setField("ANNO_SIEP", BIG_DECIMAL);
		setField("PROG_SIEP", BIG_DECIMAL);
		setField("UFFICIO_SIEP", STRING);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("UFFICIO_FASC_BDMC", STRING);
		setField("NUMERO_FASC_BDMC", BIG_DECIMAL);
		setField("TIPO_NOTIFICA", STRING);
		setField("DATA_NOTIFICA", DATE);
		setField("STATO_TRASMISSIONE", STRING);
		setField("DATA_TRASMISSIONE", DATE);
		setField("ID_PREN", BIG_DECIMAL);
		setField("PROG_PERI_PRES", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("ID_EVENTO", BIG_DECIMAL);
		setField("ID_FASCICOLO_BDMC", BIG_DECIMAL);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdNotificheSies() throws DAOException {
		return getBigDecimal("ID_NOTIFICHE_SIES");
	}

	public BigDecimal getAnnoSiep() throws DAOException {
		return getBigDecimal("ANNO_SIEP");
	}

	public BigDecimal getProgSiep() throws DAOException {
		return getBigDecimal("PROG_SIEP");
	}

	public String getUfficioSiep() throws DAOException {
		return getString("UFFICIO_SIEP");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
	}

	public String getUfficioFascBdmc() throws DAOException {
		return getString("UFFICIO_FASC_BDMC");
	}

	public BigDecimal getNumeroFascBdmc() throws DAOException {
		return getBigDecimal("NUMERO_FASC_BDMC");
	}

	public String getTipoNotifica() throws DAOException {
		return getString("TIPO_NOTIFICA");
	}

	public Date getDataNotifica() throws DAOException {
		return getDate("DATA_NOTIFICA");
	}

	public String getStatoTrasmissione() throws DAOException {
		return getString("STATO_TRASMISSIONE");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public BigDecimal getProgPeriPres() throws DAOException {
		return getBigDecimal("PROG_PERI_PRES");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public BigDecimal getIdEvento() throws DAOException {
		return getBigDecimal("ID_EVENTO");
	}

	public BigDecimal getIdFascicoloBdmc() throws DAOException {
		return getBigDecimal("ID_FASCICOLO_BDMC");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdNotificheSies(BigDecimal aValore) {
		setBigDecimal("ID_NOTIFICHE_SIES", aValore);
	}

	public void setAnnoSiep(BigDecimal aValore) {
		setBigDecimal("ANNO_SIEP", aValore);
	}

	public void setProgSiep(BigDecimal aValore) {
		setBigDecimal("PROG_SIEP", aValore);
	}

	public void setUfficioSiep(String aValore) {
		setString("UFFICIO_SIEP", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setUfficioFascBdmc(String aValore) {
		setString("UFFICIO_FASC_BDMC", aValore);
	}

	public void setNumeroFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUMERO_FASC_BDMC", aValore);
	}

	public void setTipoNotifica(String aValore) {
		setString("TIPO_NOTIFICA", aValore);
	}

	public void setDataNotifica(Date aValore) {
		setDate("DATA_NOTIFICA", aValore);
	}

	public void setStatoTrasmissione(String aValore) {
		setString("STATO_TRASMISSIONE", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setProgPeriPres(BigDecimal aValore) {
		setBigDecimal("PROG_PERI_PRES", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setIdEvento(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO", aValore);
	}

	public void setIdFascicoloBdmc(BigDecimal aValore) {
		setBigDecimal("ID_FASCICOLO_BDMC", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new NotificheSiesModel(getIdNotificheSies(), getAnnoSiep(), getProgSiep(), getUfficioSiep(),
				getAnnoFascBdmc(), getUfficioFascBdmc(), getNumeroFascBdmc(), getTipoNotifica(),
				getDataNotifica(), getStatoTrasmissione(), getDataTrasmissione(), getIdPren(),
				getProgPeriPres(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), getIdEvento(), getIdFascicoloBdmc());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(NotificheSiesModel aModel) throws DAOException {
		setIdNotificheSies(aModel.getIdNotificheSies());
		setAnnoSiep(aModel.getAnnoSiep());
		setProgSiep(aModel.getProgSiep());
		setUfficioSiep(aModel.getUfficioSiep());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setUfficioFascBdmc(aModel.getUfficioFascBdmc());
		setNumeroFascBdmc(aModel.getNumeroFascBdmc());
		setTipoNotifica(aModel.getTipoNotifica());
		setDataNotifica(aModel.getDataNotifica());
		setStatoTrasmissione(aModel.getStatoTrasmissione());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setIdPren(aModel.getIdPren());
		setProgPeriPres(aModel.getProgPeriPres());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setIdEvento(aModel.getIdEvento());
		setIdFascicoloBdmc(aModel.getIdFascicoloBdmc());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(NotificheSiesModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdNotificheSies() != null) {
			lCondizioni += " and ID_NOTIFICHE_SIES = " + aModel.getIdNotificheSies() + "";
		}
		if (aModel.getAnnoSiep() != null) {
			lCondizioni += " and ANNO_SIEP = " + aModel.getAnnoSiep() + "";
		}
		if (aModel.getProgSiep() != null) {
			lCondizioni += " and PROG_SIEP = " + aModel.getProgSiep() + "";
		}
		if (aModel.getUfficioSiep() != null && aModel.getUfficioSiep().length() > 0) {
			lCondizioni += " and UFFICIO_SIEP = '" + aModel.getUfficioSiep() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getUfficioFascBdmc() != null && aModel.getUfficioFascBdmc().length() > 0) {
			lCondizioni += " and UFFICIO_FASC_BDMC = '" + aModel.getUfficioFascBdmc() + "' ";
		}
		if (aModel.getNumeroFascBdmc() != null) {
			lCondizioni += " and NUMERO_FASC_BDMC = " + aModel.getNumeroFascBdmc() + "";
		}
		if (aModel.getTipoNotifica() != null && aModel.getTipoNotifica().length() > 0) {
			lCondizioni += " and TIPO_NOTIFICA = '" + aModel.getTipoNotifica() + "' ";
		}
		if (aModel.getDataNotifica() != null) {
			lCondizioni += " and to_char(DATA_NOTIFICA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataNotifica(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getStatoTrasmissione() != null && aModel.getStatoTrasmissione().length() > 0) {
			lCondizioni += " and STATO_TRASMISSIONE = '" + aModel.getStatoTrasmissione() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getIdEvento() != null) {
			lCondizioni += " and ID_EVENTO = " + aModel.getIdEvento() + "";
		}
		if (aModel.getIdFascicoloBdmc() != null) {
			lCondizioni += " and ID_FASCICOLO_BDMC = " + aModel.getIdFascicoloBdmc() + "";
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
	public void selCondizioneUpdate(BigDecimal aIdNotificheSies) {
		String lCondizioni = new String();

		lCondizioni += " and ID_NOTIFICHE_SIES = " + aIdNotificheSies;
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