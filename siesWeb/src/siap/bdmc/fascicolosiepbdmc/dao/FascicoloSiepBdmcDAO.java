package siap.bdmc.fascicolosiepbdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: FascicoloSiepBdmcDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella FascicoloSiepBdmc
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
public class FascicoloSiepBdmcDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public FascicoloSiepBdmcDAO(Connection con) {
		super(con);
		setTable("FASCICOLO_SIEP_BDMC");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_FASCICOLO_BDMC", "SEQ_FASC_BDMC");

		setField("ID_FASCICOLO_BDMC", BIG_DECIMAL);
		setField("CHIAVE_ANNO_BDMC", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO_BDMC", STRING);
		setField("CHIAVE_PROGR_BDMC", BIG_DECIMAL);
		setField("CHIAVE_ANNO_SIEP", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO_SIEP", STRING);
		setField("CHIAVE_PROGR_SIEP", BIG_DECIMAL);
		setField("FLAG_TRASMISSIONE", STRING);
		setField("DATA_TRASMISSIONE", DATE);
		setField("DATA_DISATTIVAZIONE", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("TIPO_MISURA", STRING);

		setField("ID_EVENTO", BIG_DECIMAL);
		setField("FLAG_ORDINE_ESECUZIONE", STRING);
		setField("ISTITUTO_DETENZIONE", STRING);
		setField("COD_COMUNE", STRING);
		setField("ALTRO_LUOGO", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdFascicoloBdmc() throws DAOException {
		return getBigDecimal("ID_FASCICOLO_BDMC");
	}

	public BigDecimal getChiaveAnnoBdmc() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO_BDMC");
	}

	public String getChiaveUfficioBdmc() throws DAOException {
		return getString("CHIAVE_UFFICIO_BDMC");
	}

	public BigDecimal getChiaveProgrBdmc() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR_BDMC");
	}

	public BigDecimal getChiaveAnnoSiep() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO_SIEP");
	}

	public String getChiaveUfficioSiep() throws DAOException {
		return getString("CHIAVE_UFFICIO_SIEP");
	}

	public BigDecimal getChiaveProgrSiep() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR_SIEP");
	}

	public String getFlagTrasmissione() throws DAOException {
		return getString("FLAG_TRASMISSIONE");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public Date getDataDisattivazione() throws DAOException {
		return getDate("DATA_DISATTIVAZIONE");
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

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public String getTipoMisura() throws DAOException {
		return getString("TIPO_MISURA");
	}

	public BigDecimal getIdEvento() throws DAOException {
		return getBigDecimal("ID_EVENTO");
	}

	public String getCodComune() throws DAOException {
		return getString("COD_COMUNE");
	}

	public String getAltroLuogo() throws DAOException {
		return getString("ALTRO_LUOGO");
	}

	public String getFlagOrdineEsecuzione() throws DAOException {
		return getString("FLAG_ORDINE_ESECUZIONE");
	}

	public String getIstitutoDetenzione() throws DAOException {
		return getString("ISTITUTO_DETENZIONE");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdFascicoloBdmc(BigDecimal aValore) {
		setBigDecimal("ID_FASCICOLO_BDMC", aValore);
	}

	public void setChiaveAnnoBdmc(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO_BDMC", aValore);
	}

	public void setChiaveUfficioBdmc(String aValore) {
		setString("CHIAVE_UFFICIO_BDMC", aValore);
	}

	public void setChiaveProgrBdmc(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR_BDMC", aValore);
	}

	public void setChiaveAnnoSiep(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO_SIEP", aValore);
	}

	public void setChiaveUfficioSiep(String aValore) {
		setString("CHIAVE_UFFICIO_SIEP", aValore);
	}

	public void setChiaveProgrSiep(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR_SIEP", aValore);
	}

	public void setFlagTrasmissione(String aValore) {
		setString("FLAG_TRASMISSIONE", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setDataDisattivazione(Date aValore) {
		setDate("DATA_DISATTIVAZIONE", aValore);
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

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setTipoMisura(String aValore) {
		setString("TIPO_MISURA", aValore);
	}

	public void setIdEvento(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO", aValore);
	}

	public void setCodComune(String aValore) {
		setString("COD_COMUNE", aValore);
	}

	public void setAltroLuogo(String aValore) {
		setString("ALTRO_LUOGO", aValore);
	}

	public void setFlagOrdineEsecuzione(String aValore) {
		setString("FLAG_ORDINE_ESECUZIONE", aValore);
	}

	public void setIstitutoDetenzione(String aValore) {
		setString("ISTITUTO_DETENZIONE", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new FascicoloSiepBdmcModel(getIdFascicoloBdmc(), getChiaveAnnoBdmc(), getChiaveUfficioBdmc(),
				getChiaveProgrBdmc(), getChiaveAnnoSiep(), getChiaveUfficioSiep(), getChiaveProgrSiep(),
				getFlagTrasmissione(), getDataTrasmissione(), getDataDisattivazione(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(),
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(),
				getTipoMisura(), getIdEvento(), getCodComune(), getAltroLuogo(), getFlagOrdineEsecuzione(),
				getIstitutoDetenzione()
		// getDescrUfficioAggiornamento()
		);
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(FascicoloSiepBdmcModel aModel) throws DAOException {
		setIdFascicoloBdmc(aModel.getIdFascicoloBdmc());
		setChiaveAnnoBdmc(aModel.getChiaveAnnoBdmc());
		setChiaveUfficioBdmc(aModel.getChiaveUfficioBdmc());
		setChiaveProgrBdmc(aModel.getChiaveProgrBdmc());
		setChiaveAnnoSiep(aModel.getChiaveAnnoSiep());
		setChiaveUfficioSiep(aModel.getChiaveUfficioSiep());
		setChiaveProgrSiep(aModel.getChiaveProgrSiep());
		setFlagTrasmissione(aModel.getFlagTrasmissione());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setDataDisattivazione(aModel.getDataDisattivazione());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setTipoMisura(aModel.getTipoMisura());
		setIdEvento(aModel.getIdEvento());
		setCodComune(aModel.getCodComune());
		setAltroLuogo(aModel.getAltroLuogo());
		setFlagOrdineEsecuzione(aModel.getFlagOrdineEsecuzione());
		setIstitutoDetenzione(aModel.getIstitutoDetenzione());

	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(FascicoloSiepBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdFascicoloBdmc() != null) {
			lCondizioni += " and ID_FASCICOLO_BDMC = " + aModel.getIdFascicoloBdmc() + "";
		}
		if (aModel.getChiaveAnnoBdmc() != null) {
			lCondizioni += " and CHIAVE_ANNO_BDMC = " + aModel.getChiaveAnnoBdmc() + "";
		}
		if (aModel.getChiaveUfficioBdmc() != null && aModel.getChiaveUfficioBdmc().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO_BDMC = '" + aModel.getChiaveUfficioBdmc() + "' ";
		}
		if (aModel.getChiaveProgrBdmc() != null) {
			lCondizioni += " and CHIAVE_PROGR_BDMC = " + aModel.getChiaveProgrBdmc() + "";
		}
		if (aModel.getChiaveAnnoSiep() != null) {
			lCondizioni += " and CHIAVE_ANNO_SIEP = " + aModel.getChiaveAnnoSiep() + "";
		}
		if (aModel.getChiaveUfficioSiep() != null && aModel.getChiaveUfficioSiep().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO_SIEP = '" + aModel.getChiaveUfficioSiep() + "' ";
		}
		if (aModel.getChiaveProgrSiep() != null) {
			lCondizioni += " and CHIAVE_PROGR_SIEP = " + aModel.getChiaveProgrSiep() + "";
		}
		if (aModel.getFlagTrasmissione() != null && aModel.getFlagTrasmissione().length() > 0) {
			lCondizioni += " and FLAG_TRASMISSIONE = '" + aModel.getFlagTrasmissione() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataDisattivazione() != null) {
			lCondizioni += " and to_char(DATA_DISATTIVAZIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataDisattivazione(), "dd/MM/yyyy") + "' ";
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
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getTipoMisura() != null && aModel.getTipoMisura().length() > 0) {
			lCondizioni += " and TIPO_MISURA = '" + aModel.getTipoMisura() + "' ";
		}
		if (aModel.getIdEvento() != null) {
			lCondizioni += " and ID_EVENTO = " + aModel.getIdEvento() + "";
		}
		if (aModel.getCodComune() != null && aModel.getCodComune().length() > 0) {
			lCondizioni += " and COD_COMUNE = '" + aModel.getCodComune() + "' ";
		}
		if (aModel.getAltroLuogo() != null && aModel.getAltroLuogo().length() > 0) {
			lCondizioni += " and ALTRO_LUOGO = '" + aModel.getAltroLuogo() + "' ";
		}
		if (aModel.getIstitutoDetenzione() != null && aModel.getIstitutoDetenzione().length() > 0) {
			lCondizioni += " and ISTITUTO_DETENZIONE = '" + aModel.getIstitutoDetenzione() + "' ";
		}
		if (aModel.getFlagOrdineEsecuzione() != null && aModel.getFlagOrdineEsecuzione().length() > 0) {
			lCondizioni += " and FLAG_ORDINE_ESECUZIONE = '" + aModel.getFlagOrdineEsecuzione() + "' ";
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
	public void selCondizioneUpdate(BigDecimal aIdFascicoloBdmc) {
		String lCondizioni = new String();

		lCondizioni += "and ID_FASCICOLO_BDMC = " + aIdFascicoloBdmc;
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