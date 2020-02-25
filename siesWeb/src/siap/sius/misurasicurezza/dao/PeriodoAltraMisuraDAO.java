package siap.sius.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: PeriodoAltraMisuraDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella PeriodoAltraMisura
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
public class PeriodoAltraMisuraDAO extends TableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public PeriodoAltraMisuraDAO(Connection con) {
		super(con);
		setTable("PERIODO_ALTRA_MISURA");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_PERIODO_ALTRA_MISURA", "per_mis_seq");

		// setField("ID_PERIODO_ALTRA_MISURA", BIG_DECIMAL);
		setField("DATA_INIZIO_ESECUZIONE", DATE);
		setField("DATA_SCADENZA", DATE);
		setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
		setField("MOTIVAZIONE", STRING);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("FLAG_MOTIVO", STRING);
		setField("FLAG_VALIDA", STRING);
		setField("COD_TIPO_AUTORITA", STRING);
		setField("COD_LUOGO_AUTORITA", STRING);
		setField("SOSPENSIONE_GG", BIG_DECIMAL);
		setField("SOSPENSIONE_MM", BIG_DECIMAL);
		setField("SOSPENSIONE_AA", BIG_DECIMAL);
		// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
		setField("DA_RECUPERARE", STRING);
		setField("DA_RECUPERARE_GG", BIG_DECIMAL);
		setField("DA_RECUPERARE_MM", BIG_DECIMAL);
		setField("DA_RECUPERARE_AA", BIG_DECIMAL);
		// setField("NUMERO_GIORNI" , BIG_DECIMAL);
		setField("COD_TIPO_UFFICIO_SOSP", STRING);
		setField("ESPIATA_GG", BIG_DECIMAL);
		setField("ESPIATA_MM", BIG_DECIMAL);
		setField("ESPIATA_AA", BIG_DECIMAL);
		setField("RESIDUA_GG", BIG_DECIMAL);
		setField("RESIDUA_MM", BIG_DECIMAL);
		setField("RESIDUA_AA", BIG_DECIMAL);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdPeriodoAltraMisura() throws DAOException {
		return getBigDecimal("ID_PERIODO_ALTRA_MISURA");
	}

	public Date getDataInizioEsecuzione() throws DAOException {
		return getDate("DATA_INIZIO_ESECUZIONE");
	}

	public Date getDataScadenza() throws DAOException {
		return getDate("DATA_SCADENZA");
	}

	public String getIstDetIdIstitutoDetenzione() throws DAOException {
		return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
	}

	public String getMotivazione() throws DAOException {
		return getString("MOTIVAZIONE");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
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

	public String getFlagMotivo() throws DAOException {
		return getString("FLAG_MOTIVO");
	}

	public String getFlagValida() throws DAOException {
		return getString("FLAG_VALIDA");
	}

	public String getCodTipoAutorita() throws DAOException {
		return getString("COD_TIPO_AUTORITA");
	}

	public String getCodLuogoAutorita() throws DAOException {
		return getString("COD_LUOGO_AUTORITA");
	}

	public BigDecimal getSospensioneGG() throws DAOException {
		return getBigDecimal("SOSPENSIONE_GG");
	}

	public BigDecimal getSospensioneMM() throws DAOException {
		return getBigDecimal("SOSPENSIONE_GG");
	}

	public BigDecimal getSospensioneAA() throws DAOException {
		return getBigDecimal("SOSPENSIONE_GG");
	}

	public String getDaRecuperare() throws DAOException {
		return getString("DA_RECUPERARE");
	}

	public BigDecimal getDaRecuperareGG() throws DAOException {
		return getBigDecimal("DA_RECUPERARE_GG");
	}

	public BigDecimal getDaRecuperareMM() throws DAOException {
		return getBigDecimal("DA_RECUPERARE_MM");
	}

	public BigDecimal getDaRecuperareAA() throws DAOException {
		return getBigDecimal("DA_RECUPERARE_AA");
	}

	// 12/05/2008 public BigDecimal getNumeroGiorni() throws DAOException { return getBigDecimal
	// ("NUMERO_GIORNI" ); }
	public String getCodTipoUfficioSosp() throws DAOException {
		return getString("COD_TIPO_UFFICIO_SOSP");
	}

	public BigDecimal getEspiataGG() throws DAOException {
		return getBigDecimal("ESPIATA_GG");
	}

	public BigDecimal getEspiataMM() throws DAOException {
		return getBigDecimal("ESPIATA_GG");
	}

	public BigDecimal getEspiataAA() throws DAOException {
		return getBigDecimal("ESPIATA_GG");
	}

	public BigDecimal getResiduaGG() throws DAOException {
		return getBigDecimal("RESIDUA_GG");
	}

	public BigDecimal getResiduaMM() throws DAOException {
		return getBigDecimal("RESIDUA_GG");
	}

	public BigDecimal getResiduaAA() throws DAOException {
		return getBigDecimal("RESIDUA_GG");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdPeriodoAltraMisura(BigDecimal aValore) {
		setBigDecimal("ID_PERIODO_ALTRA_MISURA", aValore);
	}

	public void setDataInizioEsecuzione(Date aValore) {
		setDate("DATA_INIZIO_ESECUZIONE", aValore);
	}

	public void setDataScadenza(Date aValore) {
		setDate("DATA_SCADENZA", aValore);
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
	}

	public void setMotivazione(String aValore) {
		setString("MOTIVAZIONE", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
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

	public void setFlagMotivo(String aValore) {
		setString("FLAG_MOTIVO", aValore);
	}

	public void setFlagValida(String aValore) {
		setString("FLAG_VALIDA", aValore);
	}

	public void setCodTipoAutorita(String aValore) {
		setString("COD_TIPO_AUTORITA", aValore);
	}

	public void setCodLuogoAutorita(String aValore) {
		setString("COD_LUOGO_AUTORITA", aValore);
	}

	public void setSospensioneGG(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_GG", aValore);
	}

	public void setSospensioneMM(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_MM", aValore);
	}

	public void setSospensioneAA(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_AA", aValore);
	}

	public void setDaRecuperare(String aValore) {
		setString("DA_RECUPERARE", aValore);
	}

	public void setDaRecuperareGG(BigDecimal aValore) {
		setBigDecimal("DA_RECUPERARE_GG", aValore);
	}

	public void setDaRecuperareMM(BigDecimal aValore) {
		setBigDecimal("DA_RECUPERARE_MM", aValore);
	}

	public void setDaRecuperareAA(BigDecimal aValore) {
		setBigDecimal("DA_RECUPERARE_AA", aValore);
	}

	// 12/05/2008 public void setNumeroGiorni (BigDecimal aValore ) { setBigDecimal ("NUMERO_GIORNI" ,
	// aValore); }
	public void setCodTipoUfficioSosp(String aValore) {
		setString("COD_TIPO_UFFICIO_SOSP", aValore);
	}

	public void setEspiataGG(BigDecimal aValore) {
		setBigDecimal("ESPIATA_GG", aValore);
	}

	public void setEspiataMM(BigDecimal aValore) {
		setBigDecimal("ESPIATA_MM", aValore);
	}

	public void setEspiataAA(BigDecimal aValore) {
		setBigDecimal("ESPIATA_AA", aValore);
	}

	public void setResiduaGG(BigDecimal aValore) {
		setBigDecimal("RESIDUA_GG", aValore);
	}

	public void setResiduaMM(BigDecimal aValore) {
		setBigDecimal("RESIDUA_MM", aValore);
	}

	public void setResiduaAA(BigDecimal aValore) {
		setBigDecimal("RESIDUA_AA", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new PeriodoAltraMisuraModel(getIdPeriodoAltraMisura(), getDataInizioEsecuzione(),
				getDataScadenza(), getIstDetIdIstitutoDetenzione(), getMotivazione(), getEveIdEvento(),
				getFasSieIdFascicoloSiep(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getFasSiuIdFascicoloSius(), getFlagMotivo(), "",
				getFlagValida(), getCodTipoAutorita(), getCodLuogoAutorita(), "", "", getSospensioneGG(),
				getSospensioneMM(), getSospensioneAA(), getDaRecuperare(), getDaRecuperareGG(),
				getDaRecuperareMM(), getDaRecuperareAA(),
				// 12/05/2008 getNumeroGiorni(),
				getCodTipoUfficioSosp(), "", getEspiataGG(), getEspiataMM(), getEspiataAA(), getResiduaGG(),
				getResiduaMM(), getResiduaAA());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(PeriodoAltraMisuraModel aModel) throws DAOException {
		setIdPeriodoAltraMisura(aModel.getIdPeriodoAltraMisura());
		setDataInizioEsecuzione(aModel.getDataInizioEsecuzione());
		setDataScadenza(aModel.getDataScadenza());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setMotivazione(aModel.getMotivazione());
		setEveIdEvento(aModel.getEveIdEvento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setFlagMotivo(aModel.getFlagMotivo());
		setFlagValida(aModel.getFlagValida());
		setCodTipoAutorita(aModel.getCodTipoAutorita());
		setCodLuogoAutorita(aModel.getCodLuogoAutorita());
		setSospensioneGG(aModel.getSospensioneGG());
		setSospensioneMM(aModel.getSospensioneMM());
		setSospensioneAA(aModel.getSospensioneAA());
		setDaRecuperare(aModel.getDaRecuperare());
		setDaRecuperareGG(aModel.getDaRecuperareGG());
		setDaRecuperareMM(aModel.getDaRecuperareMM());
		setDaRecuperareAA(aModel.getDaRecuperareAA());
		// 12/05/2008 setNumeroGiorni ( aModel.getNumeroGiorni() );
		setCodTipoUfficioSosp(aModel.getCodTipoUfficioSosp());
		setEspiataGG(aModel.getEspiataGG());
		setEspiataMM(aModel.getEspiataMM());
		setEspiataAA(aModel.getEspiataAA());
		setResiduaGG(aModel.getResiduaGG());
		setResiduaMM(aModel.getResiduaMM());
		setResiduaAA(aModel.getResiduaAA());
	}

	public void setDAOFromModelforUpdate(PeriodoAltraMisuraModel aModel) throws DAOException {
		setDataInizioEsecuzione(aModel.getDataInizioEsecuzione());
		setDataScadenza(aModel.getDataScadenza());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setMotivazione(aModel.getMotivazione());
		setEveIdEvento(aModel.getEveIdEvento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setFlagMotivo(aModel.getFlagMotivo());
		setFlagValida(aModel.getFlagValida());
		setCodTipoAutorita(aModel.getCodTipoAutorita());
		setCodLuogoAutorita(aModel.getCodLuogoAutorita());
		setSospensioneGG(aModel.getSospensioneGG());
		setSospensioneMM(aModel.getSospensioneMM());
		setSospensioneAA(aModel.getSospensioneAA());
		setDaRecuperare(aModel.getDaRecuperare());
		setDaRecuperareGG(aModel.getDaRecuperareGG());
		setDaRecuperareMM(aModel.getDaRecuperareMM());
		setDaRecuperareAA(aModel.getDaRecuperareAA());
		// 12/05/2008 setNumeroGiorni ( aModel.getNumeroGiorni() );
		setCodTipoUfficioSosp(aModel.getCodTipoUfficioSosp());
		setEspiataGG(aModel.getEspiataGG());
		setEspiataMM(aModel.getEspiataMM());
		setEspiataAA(aModel.getEspiataAA());
		setResiduaGG(aModel.getResiduaGG());
		setResiduaMM(aModel.getResiduaMM());
		setResiduaAA(aModel.getResiduaAA());

		setCondizioneUpdate(aModel.getIdPeriodoAltraMisura());
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_PERIODO_ALTRA_MISURA = " + key);
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(PeriodoAltraMisuraModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdPeriodoAltraMisura() != null) {
			lCondizioni += " and ID_PERIODO_ALTRA_MISURA = " + aModel.getIdPeriodoAltraMisura() + "";
		}
		if (aModel.getDataInizioEsecuzione() != null) {
			lCondizioni += " and to_char(DATA_INIZIO_ESECUZIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizioEsecuzione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataScadenza() != null) {
			lCondizioni += " and to_char(DATA_SCADENZA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataScadenza(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIstDetIdIstitutoDetenzione() != null
				&& aModel.getIstDetIdIstitutoDetenzione().length() > 0) {
			lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione()
					+ "' ";
		}
		if (aModel.getMotivazione() != null && aModel.getMotivazione().length() > 0) {
			lCondizioni += " and MOTIVAZIONE = '" + aModel.getMotivazione() + "' ";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
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
			lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = '" + aModel.getFasSiuIdFascicoloSius() + "' ";
		}
		if (aModel.getFlagMotivo() != null && aModel.getFlagMotivo().length() > 0) {
			lCondizioni += " and FLAG_MOTIVO = '" + aModel.getFlagMotivo() + "'";
		}
		if (aModel.getFlagValida() != null && aModel.getFlagValida().length() > 0) {
			lCondizioni += " and FLAG_VALIDA = '" + aModel.getFlagValida() + "'";
		}
		if (aModel.getCodTipoAutorita() != null && aModel.getCodTipoAutorita().length() > 0) {
			lCondizioni += " and COD_TIPO_AUTORITA = '" + aModel.getCodTipoAutorita() + "'";
		}
		if (aModel.getCodLuogoAutorita() != null && aModel.getCodLuogoAutorita().length() > 0) {
			lCondizioni += " and COD_LUOGO_AUTORITA = '" + aModel.getCodLuogoAutorita() + "'";
		}
		if (aModel.getSospensioneGG() != null) {
			lCondizioni += " and SOSPENSIONE_GG = " + aModel.getSospensioneGG() + "";
		}
		if (aModel.getSospensioneMM() != null) {
			lCondizioni += " and SOSPENSIONE_MM = " + aModel.getSospensioneMM() + "";
		}
		if (aModel.getSospensioneAA() != null) {
			lCondizioni += " and SOSPENSIONE_AA = " + aModel.getSospensioneAA() + "";
		}
		if (aModel.getDaRecuperare() != null && aModel.getDaRecuperare().length() > 0) {
			lCondizioni += " and DA_RECUPERARE = '" + aModel.getDaRecuperare() + "'";
		}
		if (aModel.getDaRecuperareGG() != null) {
			lCondizioni += " and DA_RECUPERARE_GG = '" + aModel.getDaRecuperareGG() + "'";
		}
		if (aModel.getDaRecuperareMM() != null) {
			lCondizioni += " and DA_RECUPERARE_MM = '" + aModel.getDaRecuperareMM() + "'";
		}
		if (aModel.getDaRecuperareAA() != null) {
			lCondizioni += " and DA_RECUPERARE_AA = '" + aModel.getDaRecuperareAA() + "'";
		}
		// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
		// if (aModel.getNumeroGiorni() != null ) {
		// lCondizioni += " and NUMERO_GIORNI = " + aModel.getNumeroGiorni() + "";
		// }
		if (aModel.getCodTipoUfficioSosp() != null && aModel.getCodTipoUfficioSosp().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_SOSP = '" + aModel.getCodTipoUfficioSosp() + "'";
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
	public void selCondizioneUpdate(BigDecimal aIdPeriodoAltraMisura) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PERIODO_ALTRA_MISURA = " + aIdPeriodoAltraMisura;
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

		orderBy = " DATA_SCADENZA ASC ";
		setOrder(orderBy);
	}

	/**
	 * Imposta la condizione per l'id dell'EVENTO.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave del dell'evento.
	 */
	public void setCondizioneByEveIdEvento(BigDecimal aKey) {
		setCondition(" EVE_ID_EVENTO = " + aKey);
	}

}