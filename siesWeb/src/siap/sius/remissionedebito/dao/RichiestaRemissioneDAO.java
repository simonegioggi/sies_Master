package siap.sius.remissionedebito.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: RichiestaRemissioneDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella RichiestaRemissione
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
public class RichiestaRemissioneDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public RichiestaRemissioneDAO(Connection con) {
		super(con);
		setTable("RICHIESTA_REMISSIONE");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_RICHIESTA_REMISSIONE", "RIC_REM_SEQ");

		// setField("ID_RICHIESTA_REMISSIONE", BIG_DECIMAL);
		setField("ANNO_PARTITA", BIG_DECIMAL);
		setField("NUM_PARTITA", BIG_DECIMAL);
		setField("NUM_EX_CAMPIONE", STRING);
		setField("PROT_CIRCOSRIZIONE_DOGANALE", STRING);
		setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
		setField("COD_LUOGO_EMITTENTE", STRING);
		setField("COD_TIPO_PROVVEDIMENTO", STRING);
		setField("DATA_EMISSIONE", DATE);
		setField("COD_AUTORITA_EMITTENTE_PROVV", STRING);
		setField("COD_LUOGO_EMITTENTE_PROVV", STRING);
		setField("FLAG_SPESE_CARCERE", STRING);
		setField("IMPORTO_SPESE_CARCERE", BIG_DECIMAL);
		setField("FLAG_SPESE_PROCEDIMENTO", STRING);
		setField("IMPORTO_SPESE_PROCEDIMENTO", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("NOTE", STRING);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdRichiestaRemissione() throws DAOException {
		return getBigDecimal("ID_RICHIESTA_REMISSIONE");
	}

	public BigDecimal getAnnoPartita() throws DAOException {
		return getBigDecimal("ANNO_PARTITA");
	}

	public BigDecimal getNumPartita() throws DAOException {
		return getBigDecimal("NUM_PARTITA");
	}

	public String getNumExCampione() throws DAOException {
		return getString("NUM_EX_CAMPIONE");
	}

	public String getProtCircosrizioneDoganale() throws DAOException {
		return getString("PROT_CIRCOSRIZIONE_DOGANALE");
	}

	public String getCodTipoAutoritaEmittente() throws DAOException {
		return getString("COD_TIPO_AUTORITA_EMITTENTE");
	}

	public String getCodLuogoEmittente() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE");
	}

	public String getCodTipoProvvedimento() throws DAOException {
		return getString("COD_TIPO_PROVVEDIMENTO");
	}

	public Date getDataEmissione() throws DAOException {
		return getDate("DATA_EMISSIONE");
	}

	public String getCodAutoritaEmittenteProvv() throws DAOException {
		return getString("COD_AUTORITA_EMITTENTE_PROVV");
	}

	public String getCodLuogoEmittenteProvv() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE_PROVV");
	}

	public String getFlagSpeseCarcere() throws DAOException {
		return getString("FLAG_SPESE_CARCERE");
	}

	public BigDecimal getImportoSpeseCarcere() throws DAOException {
		return getBigDecimal("IMPORTO_SPESE_CARCERE");
	}

	public String getFlagSpeseProcedimento() throws DAOException {
		return getString("FLAG_SPESE_PROCEDIMENTO");
	}

	public BigDecimal getImportoSpeseProcedimento() throws DAOException {
		return getBigDecimal("IMPORTO_SPESE_PROCEDIMENTO");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
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

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdRichiestaRemissione(BigDecimal aValore) {
		setBigDecimal("ID_RICHIESTA_REMISSIONE", aValore);
	}

	public void setAnnoPartita(BigDecimal aValore) {
		setBigDecimal("ANNO_PARTITA", aValore);
	}

	public void setNumPartita(BigDecimal aValore) {
		setBigDecimal("NUM_PARTITA", aValore);
	}

	public void setNumExCampione(String aValore) {
		setString("NUM_EX_CAMPIONE", aValore);
	}

	public void setProtCircosrizioneDoganale(String aValore) {
		setString("PROT_CIRCOSRIZIONE_DOGANALE", aValore);
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		setString("COD_TIPO_AUTORITA_EMITTENTE", aValore);
	}

	public void setCodLuogoEmittente(String aValore) {
		setString("COD_LUOGO_EMITTENTE", aValore);
	}

	public void setCodTipoProvvedimento(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO", aValore);
	}

	public void setDataEmissione(Date aValore) {
		setDate("DATA_EMISSIONE", aValore);
	}

	public void setCodAutoritaEmittenteProvv(String aValore) {
		setString("COD_AUTORITA_EMITTENTE_PROVV", aValore);
	}

	public void setCodLuogoEmittenteProvv(String aValore) {
		setString("COD_LUOGO_EMITTENTE_PROVV", aValore);
	}

	public void setFlagSpeseCarcere(String aValore) {
		setString("FLAG_SPESE_CARCERE", aValore);
	}

	public void setImportoSpeseCarcere(BigDecimal aValore) {
		setBigDecimal("IMPORTO_SPESE_CARCERE", aValore);
	}

	public void setFlagSpeseProcedimento(String aValore) {
		setString("FLAG_SPESE_PROCEDIMENTO", aValore);
	}

	public void setImportoSpeseProcedimento(BigDecimal aValore) {
		setBigDecimal("IMPORTO_SPESE_PROCEDIMENTO", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new RichiestaRemissioneModel(getIdRichiestaRemissione(), getAnnoPartita(), getNumPartita(),
				getNumExCampione(), getProtCircosrizioneDoganale(), getCodTipoAutoritaEmittente(), "",
				getCodLuogoEmittente(), "", getCodTipoProvvedimento(), "", getDataEmissione(),
				getCodAutoritaEmittenteProvv(), "", getCodLuogoEmittenteProvv(), "", getFlagSpeseCarcere(),
				getImportoSpeseCarcere(), getFlagSpeseProcedimento(), getImportoSpeseProcedimento(),
				getFasSieIdFascicoloSiep(), getEveIdEvento(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), getFasSiuIdFascicoloSius(), getNote());
	}

	/**
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModel(RichiestaRemissioneModel aModel) throws DAOException {
		setIdRichiestaRemissione(aModel.getIdRichiestaRemissione());
		setAnnoPartita(aModel.getAnnoPartita());
		setNumPartita(aModel.getNumPartita());
		setNumExCampione(aModel.getNumExCampione());
		setProtCircosrizioneDoganale(aModel.getProtCircosrizioneDoganale());
		setCodTipoAutoritaEmittente(aModel.getCodTipoAutoritaEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setCodTipoProvvedimento(aModel.getCodTipoProvvedimento());
		setDataEmissione(aModel.getDataEmissione());
		setCodAutoritaEmittenteProvv(aModel.getCodAutoritaEmittenteProvv());
		setCodLuogoEmittenteProvv(aModel.getCodLuogoEmittenteProvv());
		setFlagSpeseCarcere(aModel.getFlagSpeseCarcere());
		setImportoSpeseCarcere(aModel.getImportoSpeseCarcere());
		setFlagSpeseProcedimento(aModel.getFlagSpeseProcedimento());
		setImportoSpeseProcedimento(aModel.getImportoSpeseProcedimento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setEveIdEvento(aModel.getEveIdEvento());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setNote(aModel.getNote());

	}

	/**
	 * Metodo che imposta i campi delle operazioni di Aggiornamento x Emissione Ordinanza
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModelForUpdate(RichiestaRemissioneModel aModel) throws DAOException {
		if (aModel.getIdRichiestaRemissione() != null)
			setIdRichiestaRemissione(aModel.getIdRichiestaRemissione());
		if (aModel.getEveIdEvento() != null)
			setEveIdEvento(aModel.getEveIdEvento());
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 1)
			setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		if (aModel.getDataAggiornamento() != null)
			setDataAggiornamento(aModel.getDataAggiornamento());
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 1)
			setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		/*
		 * Da Verificare gli altri campi per la SET for Update
		 */
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(RichiestaRemissioneModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdRichiestaRemissione() != null) {
			lCondizioni += " and ID_RICHIESTA_REMISSIONE = " + aModel.getIdRichiestaRemissione() + "";
		}
		if (aModel.getAnnoPartita() != null) {
			lCondizioni += " and ANNO_PARTITA = " + aModel.getAnnoPartita() + "";
		}
		if (aModel.getNumPartita() != null) {
			lCondizioni += " and NUM_PARTITA = " + aModel.getNumPartita() + "";
		}
		if (aModel.getNumExCampione() != null && aModel.getNumExCampione().length() > 0) {
			lCondizioni += " and NUM_EX_CAMPIONE = '" + aModel.getNumExCampione() + "' ";
		}
		if (aModel.getProtCircosrizioneDoganale() != null
				&& aModel.getProtCircosrizioneDoganale().length() > 0) {
			lCondizioni += " and PROT_CIRCOSRIZIONE_DOGANALE = '" + aModel.getProtCircosrizioneDoganale()
					+ "' ";
		}
		if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) {
			lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente()
					+ "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}

		if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
		}
		if (aModel.getDataEmissione() != null) {
			lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodAutoritaEmittenteProvv() != null
				&& aModel.getCodAutoritaEmittenteProvv().length() > 0) {
			lCondizioni += " and COD_AUTORITA_EMITTENTE_PROVV = '" + aModel.getCodAutoritaEmittenteProvv()
					+ "' ";
		}
		if (aModel.getCodLuogoEmittenteProvv() != null && aModel.getCodLuogoEmittenteProvv().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE_PROVV = '" + aModel.getCodLuogoEmittenteProvv() + "' ";
		}
		if (aModel.getFlagSpeseCarcere() != null && aModel.getFlagSpeseCarcere().length() > 0) {
			lCondizioni += " and FLAG_SPESE_CARCERE = '" + aModel.getFlagSpeseCarcere() + "' ";
		}
		if (aModel.getImportoSpeseCarcere() != null) {
			lCondizioni += " and IMPORTO_SPESE_CARCERE = " + aModel.getImportoSpeseCarcere() + "";
		}
		if (aModel.getFlagSpeseProcedimento() != null && aModel.getFlagSpeseProcedimento().length() > 0) {
			lCondizioni += " and FLAG_SPESE_PROCEDIMENTO = '" + aModel.getFlagSpeseProcedimento() + "' ";
		}
		if (aModel.getImportoSpeseProcedimento() != null) {
			lCondizioni += " and IMPORTO_SPESE_PROCEDIMENTO = " + aModel.getImportoSpeseProcedimento() + "";
		}

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
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
		if (aModel.getFasSiuIdFascicoloSius() != null) {
			lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius() + "";
		}

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 */
	public void selCondizioneUpdate(BigDecimal aIdRichiestaRemissione) {
		String lCondizioni = new String();

		lCondizioni += " and ID_RICHIESTA_REMISSIONE = " + aIdRichiestaRemissione;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}
		setCondition(lCondizioni);
	}

	/**
	 * Imposta la condizione where per FAS_SIU_ID_FASCICOLO_SIUS.
	 * 
	 * @param aIdFasSius
	 */
	public void selCondizioneByIdFasSius(BigDecimal aIdFasSius) {
		String lCondizioni = new String();

		lCondizioni += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSius;

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la condizione where per FAS_SIU_ID_FASCICOLO_SIUS ed EVE_ID_EVENTO.
	 * 
	 * @param aIdFasSius
	 * @param aIdEvento
	 */
	public void selCondizioneByIdFasSiusIdEvento(BigDecimal aIdFasSius, BigDecimal aIdEvento) {
		String lCondizioni = new String();

		lCondizioni += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSius;
		lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la condizione where per EVE_ID_EVENTO.
	 * 
	 * @param aIdFasSius
	 */
	public void selCondizioneByIdEvento(BigDecimal aIdEvento) {
		String lCondizioni = new String();

		lCondizioni += " EVE_ID_EVENTO = " + aIdEvento;

		setCondition(lCondizioni);
	}

	/**
	 * Il metodo prepara l'Update da effettuare a seguito di Cancellazione Ordinanza di Remissione Debito.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	/*
	 * public void setDAOFromModelForCancOrdinanzaCPP(RichiestaRemissioneModel aModel) throws DAOException {
	 * setDataAggiornamento( aModel.getDataAggiornamento() ); setCodUfficioAggiornamento(
	 * aModel.getCodUfficioAggiornamento() ); setCodOperatoreAggiornamento(
	 * aModel.getCodOperatoreAggiornamento() ); setEveIdEvento(null); setDurataEsitoAnni(null);
	 * setDurataEsitoMesi(null); setDurataEsitoGiorni(null); setNumeroRate(null); setValoreRata(null);
	 * setValoreUltimaRata(null); setDataAnnullamento(null); setCodTipoSanzione("-"); setDataDeposito(null);
	 * setDataInizioPagamento(null); setNumGGInizioPagamento(null);
	 * 
	 * }
	 */
	/**
	 * Il metodo prepara l'Update da effettuare a seguito di Deposito Ordinanza di Remissione Debito.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	/*
	 * public void setDAOFromModelForDepOrdinanzaCPP(RichiestaRemissioneModel aModel) throws DAOException {
	 * setDataAggiornamento( aModel.getDataAggiornamento() ); setCodUfficioAggiornamento(
	 * aModel.getCodUfficioAggiornamento() ); setCodOperatoreAggiornamento(
	 * aModel.getCodOperatoreAggiornamento() ); setDataDeposito(aModel.getDataDeposito());
	 * 
	 * }
	 */
	/**
	 * Imposta la condizione di order by per la ricerca
	 * 
	 */
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