package siap.sius.depositodecreto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;

/**
 * DepositoDecretoDAO - Classe DAO che rappresenta la tabella DepositoDecreto
 *
 * @version 1.0
 */
public class DepositoDecretoDAO extends SIAPTableDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruttore di classe, con parametro-
	 * <p>
	 *
	 * @param aConn
	 *            connessione come parametro.
	 */
	public DepositoDecretoDAO(Connection aConn) {

		super(aConn);
		setTable("DEPOSITO_DECRETO");

		setSequenceField("ID_DEPOSITO_DECRETO", "DEP_DEC_SEQ");

		setField("ID_DEPOSITO_DECRETO", BIG_DECIMAL);
		setField("ANNO_S72", BIG_DECIMAL);
		setField("NUM_S72", BIG_DECIMAL);
		setField("COD_TIPO_DECRETO", STRING);
		setField("DATA_EMISSIONE", DATE);
		setField("DATA_DEPOSITO", DATE);
		setField("COD_MAGISTRATO", STRING);
		setField("ALTRI_DESTINATARI", STRING);
		setField("DATA_PARERE_PG", DATE);
		setField("COD_TIPO_PARERE_PG", STRING);
		setField("DATA_RICORSO_IMPUGNAZIONE", DATE);
		setField("DATA_INVIO_ATTI_IMPUGNAZIONE", DATE);
		setField("DATA_SENTENZA_IMPUGNAZIONE", DATE);
		setField("TENORE_SENTENZA_IMPUGNAZIONE", STRING);
		setField("NOTE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("SENTENZE_RIFERIMENTO", STRING);
		setField("ID_EVENTO_GENERATO", BIG_DECIMAL);
		// nuovi campi Luigi 17-11-2003
		setField("COD_UFFICIO_COMP", STRING);
		setField("COD_PROCURA_ESECUZIONE", STRING);
		setField("LUOGO_SVOLGIMENTO_PROVA", STRING);
		// nuovi campi Luigi 27-01-2004
		setField("COD_TDS_COMP", STRING);
		setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
		setField("STATUS_PERSONA", STRING);
		setField("TOT_ORE_RAGGIUNGIMENTO", STRING);
		setField("ANNO_PROC_REVOCATO", STRING);
		setField("PROGR_PROC_REVOCATO", STRING);
		setField("UFFICIO_PROC_REVOCATO", STRING);
		setField("DATA_COMP_FOGLIO_COMPLEMENTARE", DATE);
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setField("DATA_SOSPENSIONE_SS", DATE);
		setField("GIORNI_RECUPERO_SS", BIG_DECIMAL);
		setField("FLAG_RECUPERO_SS", STRING);
		setField("DATA_SCADENZA_SOSPENSIONE_SS", DATE);
		setField("SOSPENSIONE_GG", BIG_DECIMAL);
		setField("SOSPENSIONE_MM", BIG_DECIMAL);
		setField("SOSPENSIONE_AA", BIG_DECIMAL);
		setField("FLAG_NOMINA_COMM_ACTA", STRING);
		setField("DESCR_COMM_ACTA", STRING);
		setField("TIPO_CONTROLLO_ESECUZIONE", STRING);
		// 07/2014
		setField("NUM_GIORNI_REVOCA_LA", BIG_DECIMAL);
		// DL 92 2014 Violazione CEDU
		setField("NUM_GIORNI_RIDUZIONE_PENA", BIG_DECIMAL);
		setField("SOMMA_RISARC_DANNI", BIG_DECIMAL);
		// 02/2015 Mis. Sic.
		setField("FLAG_ELABORATO", STRING);
		// MEV_9 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		setField("DATA_TERMINE_EMISSIONE", DATE);
		setField("NUM_GIORNI_TERMINE_EMISSIONE", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDepositoDecreto() throws DAOException {
		return getBigDecimal("ID_DEPOSITO_DECRETO");
	}

	public BigDecimal getAnnoS72() throws DAOException {
		return getBigDecimal("ANNO_S72");
	}

	public BigDecimal getNumS72() throws DAOException {
		return getBigDecimal("NUM_S72");
	}

	public String getCodTipoDecreto() throws DAOException {
		return getString("COD_TIPO_DECRETO");
	}

	public Date getDataEmissione() throws DAOException {
		return getDate("DATA_EMISSIONE");
	}

	public Date getDataDeposito() throws DAOException {
		return getDate("DATA_DEPOSITO");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public String getAltriDestinatari() throws DAOException {
		return getString("ALTRI_DESTINATARI");
	}

	public Date getDataParerePg() throws DAOException {
		return getDate("DATA_PARERE_PG");
	}

	public String getCodTipoParerePg() throws DAOException {
		return getString("COD_TIPO_PARERE_PG");
	}

	public Date getDataRicorsoImpugnazione() throws DAOException {
		return getDate("DATA_RICORSO_IMPUGNAZIONE");
	}

	public Date getDataInvioAttiImpugnazione() throws DAOException {
		return getDate("DATA_INVIO_ATTI_IMPUGNAZIONE");
	}

	public Date getDataSentenzaImpugnazione() throws DAOException {
		return getDate("DATA_SENTENZA_IMPUGNAZIONE");
	}

	public String getTenoreSentenzaImpugnazione() throws DAOException {
		return getString("TENORE_SENTENZA_IMPUGNAZIONE");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
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

	public BigDecimal getGenPridGeneraleProcedimento() throws DAOException {
		return getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO");
	}

	public String getSentenzeRiferimento() throws DAOException {
		return getString("SENTENZE_RIFERIMENTO");
	}

	public BigDecimal getIdEventoGenerato() throws DAOException {
		return getBigDecimal("ID_EVENTO_GENERATO");
	}

	// nuovi campi Luigi 17-11-2003
	public String getCodUfficioComp() throws DAOException {
		return getString("COD_UFFICIO_COMP");
	}

	public String getCodProcuraEsecuzione() throws DAOException {
		return getString("COD_PROCURA_ESECUZIONE");
	}

	public String getLuogoSvolgimentoProva() throws DAOException {
		return getString("LUOGO_SVOLGIMENTO_PROVA");
	}

	// nuovi campi Luigi 27-01-2004
	public String getCodTdsComp() throws DAOException {
		return getString("COD_TDS_COMP");
	}

	public String getIstDetIdIstitutoDetenzione() throws DAOException {
		return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
	}

	public String getStatusPersona() throws DAOException {
		return getString("STATUS_PERSONA");
	}

	public String getTotOreRaggiungimento() throws DAOException {
		return getString("TOT_ORE_RAGGIUNGIMENTO");
	}

	public String getAnnoProcRevocato() throws DAOException {
		return getString("ANNO_PROC_REVOCATO");
	}

	public String getProgrProcRevocato() throws DAOException {
		return getString("PROGR_PROC_REVOCATO");
	}

	public String getUfficioProcRevocato() throws DAOException {
		return getString("UFFICIO_PROC_REVOCATO");
	}

	public Date getDataCompFoglioComplementare() throws DAOException {
		return getDate("DATA_COMP_FOGLIO_COMPLEMENTARE");
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public Date getDataSospensioneSS() throws DAOException {
		return getDate("DATA_SOSPENSIONE_SS");
	}

	public BigDecimal getGiorniRecuperoSS() throws DAOException {
		return getBigDecimal("GIORNI_RECUPERO_SS");
	}

	public String getFlagRecuperoSS() throws DAOException {
		return getString("FLAG_RECUPERO_SS");
	}

	public Date getDataScadenzaSospensioneSS() throws DAOException {
		return getDate("DATA_SCADENZA_SOSPENSIONE_SS");
	}

	public BigDecimal getSospensioneGGSS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_GG");
	}

	public BigDecimal getSospensioneMMSS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_MM");
	}

	public BigDecimal getSospensioneAASS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_AA");
	}

	public String getFlagNominaComActa() throws DAOException {
		return getString("FLAG_NOMINA_COMM_ACTA");
	}

	public String getDescrCommActa() throws DAOException {
		return getString("DESCR_COMM_ACTA");
	}

	public String getCodTipoControlloEsecuzione() throws DAOException {
		return getString("TIPO_CONTROLLO_ESECUZIONE");
	}

	// 07/2014
	public BigDecimal getNumeroGiorniRevocaLA() throws DAOException {
		return getBigDecimal("NUM_GIORNI_REVOCA_LA");
	}

	// DL 92 2014 Violazione CEDU
	public BigDecimal getNumeroGiorniRiduzionePena() throws DAOException {
		return getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA");
	}

	public BigDecimal getSommaRisarcimentoDanni() throws DAOException {
		return getBigDecimal("SOMMA_RISARC_DANNI");
	}

	// 02/2015 Mis. Sic.
	public String getFlagElaborato() throws DAOException {
		return getString("FLAG_ELABORATO");
	}

	// MEV_9 aggiunto campo DATA_TERMINE_EMISSIONE
	public Date getDataTermineEmissione() throws DAOException {
		return getDate("DATA_TERMINE_EMISSIONE");
	}

	// MEV_9 aggiunto campo NUM_GIORNI_TERMINE_EMISSIONE
	public BigDecimal getNumGiorniTermineEmissione() throws DAOException {
		return getBigDecimal("NUM_GIORNI_TERMINE_EMISSIONE");
	}

	//
	// METODI SET()
	//
	public void setIdDepositoDecreto(BigDecimal aValore) {
		setBigDecimal("ID_DEPOSITO_DECRETO", aValore);
	}

	public void setAnnoS72(BigDecimal aValore) {
		setBigDecimal("ANNO_S72", aValore);
	}

	public void setNumS72(BigDecimal aValore) {
		setBigDecimal("NUM_S72", aValore);
	}

	public void setCodTipoDecreto(String aValore) {
		setString("COD_TIPO_DECRETO", aValore);
	}

	public void setDataEmissione(Date aValore) {
		setDate("DATA_EMISSIONE", aValore);
	}

	public void setDataDeposito(Date aValore) {
		setDate("DATA_DEPOSITO", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setAltriDestinatari(String aValore) {
		setString("ALTRI_DESTINATARI", aValore);
	}

	public void setDataParerePg(Date aValore) {
		setDate("DATA_PARERE_PG", aValore);
	}

	public void setCodTipoParerePg(String aValore) {
		setString("COD_TIPO_PARERE_PG", aValore);
	}

	public void setDataRicorsoImpugnazione(Date aValore) {
		setDate("DATA_RICORSO_IMPUGNAZIONE", aValore);
	}

	public void setDataInvioAttiImpugnazione(Date aValore) {
		setDate("DATA_INVIO_ATTI_IMPUGNAZIONE", aValore);
	}

	public void setDataSentenzaImpugnazione(Date aValore) {
		setDate("DATA_SENTENZA_IMPUGNAZIONE", aValore);
	}

	public void setTenoreSentenzaImpugnazione(String aValore) {
		setString("TENORE_SENTENZA_IMPUGNAZIONE", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
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

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore);
	}

	public void setSentenzeRiferimento(String aValore) {
		setString("SENTENZE_RIFERIMENTO", aValore);
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO_GENERATO", aValore);
	}

	// nuovi campi Luigi 17-11-2003
	public void setCodUfficioComp(String aValore) {
		setString("COD_UFFICIO_COMP", aValore);
	}

	public void setCodProcuraEsecuzione(String aValore) {
		setString("COD_PROCURA_ESECUZIONE", aValore);
	}

	public void setLuogoSvolgimentoProva(String aValore) {
		setString("LUOGO_SVOLGIMENTO_PROVA", aValore);
	}

	// nuovi campi Luigi 27-01-2004
	public void setCodTdsComp(String aValore) {
		setString("COD_TDS_COMP", aValore);
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
	}

	public void setStatusPersona(String aValore) {
		setString("STATUS_PERSONA", aValore);
	}

	public void setTotOreRaggiungimento(String aValore) {
		setString("TOT_ORE_RAGGIUNGIMENTO", aValore);
	}

	public void setAnnoProcRevocato(String aValore) {
		setString("ANNO_PROC_REVOCATO", aValore);
	}

	public void setProgrProcRevocato(String aValore) {
		setString("PROGR_PROC_REVOCATO", aValore);
	}

	public void setUfficioProcRevocato(String aValore) {
		setString("UFFICIO_PROC_REVOCATO", aValore);
	}

	public void setDataCompFoglioComplementare(Date aValore) {
		setDate("DATA_COMP_FOGLIO_COMPLEMENTARE", aValore);
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public void setDataSospensioneSS(Date aValore) {
		setDate("DATA_SOSPENSIONE_SS", aValore);
	}

	public void setGiorniRecuperoSS(BigDecimal aValore) {
		setBigDecimal("GIORNI_RECUPERO_SS", aValore);
	}

	public void setFlagRecuperoSS(String aValore) {
		setString("FLAG_RECUPERO_SS", aValore);
	}

	public void setDataScadenzaSospensioneSS(Date aValore) {
		setDate("DATA_SCADENZA_SOSPENSIONE_SS", aValore);
	}

	public void setSospensioneGGSS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_GG", aValore);
	}

	public void setSospensioneMMSS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_MM", aValore);
	}

	public void setSospensioneAASS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_AA", aValore);
	}

	public void setFlagNominaComActa(String aValore) {
		setString("FLAG_NOMINA_COMM_ACTA", aValore);
	}

	public void setDescrCommActa(String aValore) {
		setString("DESCR_COMM_ACTA", aValore);
	}

	public void setCodTipoControlloEsecuzione(String aValore) {
		setString("TIPO_CONTROLLO_ESECUZIONE", aValore);
	}

	// 07/2014
	public void setNumeroGiorniRevocaLA(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_REVOCA_LA", aValore);
	}

	// DL 92 2014 Violazione CEDU
	public void setNumeroGiorniRiduzionePena(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_RIDUZIONE_PENA", aValore);
	}

	public void setSommaRisarcimentoDanni(BigDecimal aValore) {
		setBigDecimal("SOMMA_RISARC_DANNI", aValore);
	}

	// 02/2015 Mis. Sic.
	public void setFlagElaborato(String aValore) {
		setString("FLAG_ELABORATO", aValore);
	}

	// MEV_9 aggiunto campo DATA_TERMINE_EMISSIONE
	public void setDataTermineEmissione(Date aValore) {
		setDate("DATA_TERMINE_EMISSIONE", aValore);
	}

	// MEV_9 aggiunto campo NUM_GIORNI_TERMINE_EMISSIONE
	public void setNumGiorniTermineEmissione(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_TERMINE_EMISSIONE", aValore);
	}

	/**
	 * Ritorna il model popolato con i dati del record
	 *
	 * @return model popolato
	 * @throws DAOException propaga errore di eccezione
	 */
	public GenericModel getModel() throws DAOException {

		return new DepositoDecretoModel(getIdDepositoDecreto(), getAnnoS72(), getNumS72(),
				getCodTipoDecreto(), "", getDataEmissione(), getDataDeposito(), getCodMagistrato(), "",
				getAltriDestinatari(), getDataParerePg(), getCodTipoParerePg(), "",
				getDataRicorsoImpugnazione(), getDataInvioAttiImpugnazione(), getDataSentenzaImpugnazione(),
				getTenoreSentenzaImpugnazione(), getNote(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), "", getGenPridGeneraleProcedimento(),
				getSentenzeRiferimento(), getIdEventoGenerato(),
				// nuovi campi Luigi 17-11-2003
				getCodUfficioComp(), "", getCodProcuraEsecuzione(), "", getLuogoSvolgimentoProva(),
				// nuovi campi Luigi 27-01-2004
				getCodTdsComp(), "", getIstDetIdIstitutoDetenzione(), getStatusPersona(),
				getTotOreRaggiungimento(), getAnnoProcRevocato(), getProgrProcRevocato(),
				getUfficioProcRevocato(), "", getDataCompFoglioComplementare(),
				// Nuovi campi per Sospensione Sanzioni Sostitutive
				getDataSospensioneSS(), getGiorniRecuperoSS(), getFlagRecuperoSS(),
				getDataScadenzaSospensioneSS(), getSospensioneGGSS(), getSospensioneMMSS(),
				getSospensioneAASS(), getFlagNominaComActa(), getDescrCommActa(),
				getCodTipoControlloEsecuzione(), "", getNumeroGiorniRevocaLA(),
				// DL 92 2014 Violazione CEDU
				getNumeroGiorniRiduzionePena(), getSommaRisarcimentoDanni(),
				// 02/2015 Mis.Sic.
				getFlagElaborato(),
				// MEV_9 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
				getDataTermineEmissione(), getNumGiorniTermineEmissione());
	}

	/**
	 * Imposta il DAO con i dati del model passato come argomento.
	 * <p>
	 *
	 * @param aModel
	 *            model dei dati da impostare nel DAO.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void setDAOFromModel(DepositoDecretoModel aModel) throws DAOException {

		setIdDepositoDecreto(aModel.getIdDepositoDecreto());
		setAnnoS72(aModel.getAnnoS72());
		setNumS72(aModel.getNumS72());
		setCodTipoDecreto(aModel.getCodTipoDecreto());
		setDataEmissione(aModel.getDataEmissione());
		setDataDeposito(aModel.getDataDeposito());
		setCodMagistrato(aModel.getCodMagistrato());
		setAltriDestinatari(aModel.getAltriDestinatari());
		setDataParerePg(aModel.getDataParerePg());
		setCodTipoParerePg(aModel.getCodTipoParerePg());
		setDataRicorsoImpugnazione(aModel.getDataRicorsoImpugnazione());
		setDataInvioAttiImpugnazione(aModel.getDataInvioAttiImpugnazione());
		setDataSentenzaImpugnazione(aModel.getDataSentenzaImpugnazione());
		setTenoreSentenzaImpugnazione(aModel.getTenoreSentenzaImpugnazione());
		setNote(aModel.getNote());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setSentenzeRiferimento(aModel.getSentenzeRiferimento());
		setIdEventoGenerato(aModel.getIdEventoGenerato());
		// nuovi campi Luigi 17-11-2003
		setCodUfficioComp(aModel.getCodUfficioCompetente());
		setCodProcuraEsecuzione(aModel.getCodProcuraEsecuzione());
		setLuogoSvolgimentoProva(aModel.getLuogoSvolgimentoProva());
		// nuovi campi Luigi 27-01-2004
		setCodTdsComp(aModel.getCodTdsComp());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setStatusPersona(aModel.getStatusPersona());
		setTotOreRaggiungimento(aModel.getTotOreRaggiungimento());
		setAnnoProcRevocato(aModel.getAnnoProcRevocato());
		setProgrProcRevocato(aModel.getProgrProcRevocato());
		setUfficioProcRevocato(aModel.getCodProcuraRevocato());
		setDataCompFoglioComplementare(aModel.getDataCompFoglioComplementare());
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setDataSospensioneSS(aModel.getDataSospensioneSS());
		setGiorniRecuperoSS(aModel.getGiorniRecuperoSS());
		setFlagRecuperoSS(aModel.getFlagRecuperoSS());
		setDataScadenzaSospensioneSS(aModel.getDataScadenzaSospensioneSS());
		setSospensioneGGSS(aModel.getSospensioneGGSS());
		setSospensioneMMSS(aModel.getSospensioneMMSS());
		setSospensioneAASS(aModel.getSospensioneAASS());
		setFlagNominaComActa(aModel.getFlagNominaComActa());
		setDescrCommActa(aModel.getDescrCommActa());
		setCodTipoControlloEsecuzione(aModel.getCodTipoControlloEsecuzione());
		// 07/2014
		setNumeroGiorniRevocaLA(aModel.getNumeroGiorniRevocaLA());
		// DL 92 2014 Violazione CEDU
		setNumeroGiorniRiduzionePena(aModel.getNumGiorniRiduzionePena());
		setSommaRisarcimentoDanni(aModel.getSommaRisarcimentoDanni());
		// 02/2015 Mis.Sic.
		setFlagElaborato(aModel.getFlagElaborato());
		// MEV_9 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		setDataTermineEmissione(aModel.getDataTermineEmissione());
		setNumGiorniTermineEmissione(aModel.getNumGiorniTermineEmissione());
	}

	/**
	 * Imposta il DAO con i dati del model passato come argomento, per la fase di update
	 *
	 * @param aModel model dei dati da impostare nel DAO
	 * @throws DAOException propaga errore di eccezione
	 */
	public void setDAOFromModelForUpdate(DepositoDecretoModel aModel) throws DAOException {

		setIdDepositoDecreto(aModel.getIdDepositoDecreto());
		setAnnoS72(aModel.getAnnoS72());
		setNumS72(aModel.getNumS72());
		setCodTipoDecreto(aModel.getCodTipoDecreto());
		setDataEmissione(aModel.getDataEmissione());
		setDataDeposito(aModel.getDataDeposito());
		setCodMagistrato(aModel.getCodMagistrato());
		setAltriDestinatari(aModel.getAltriDestinatari());
		setDataParerePg(aModel.getDataParerePg());
		setCodTipoParerePg(aModel.getCodTipoParerePg());
		setDataRicorsoImpugnazione(aModel.getDataRicorsoImpugnazione());
		setDataInvioAttiImpugnazione(aModel.getDataInvioAttiImpugnazione());
		setDataSentenzaImpugnazione(aModel.getDataSentenzaImpugnazione());
		setTenoreSentenzaImpugnazione(aModel.getTenoreSentenzaImpugnazione());
		setNote(aModel.getNote());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setSentenzeRiferimento(aModel.getSentenzeRiferimento());
		setIdEventoGenerato(aModel.getIdEventoGenerato());
		// nuovi campi Luigi 17-11-2003
		setCodUfficioComp(aModel.getCodUfficioCompetente());
		setCodProcuraEsecuzione(aModel.getCodProcuraEsecuzione());
		setLuogoSvolgimentoProva(aModel.getLuogoSvolgimentoProva());
		// nuovi campi Luigi 27-01-2004
		setCodTdsComp(aModel.getCodTdsComp());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setStatusPersona(aModel.getStatusPersona());
		setTotOreRaggiungimento(aModel.getTotOreRaggiungimento());
		setAnnoProcRevocato(aModel.getAnnoProcRevocato());
		setProgrProcRevocato(aModel.getProgrProcRevocato());
		setUfficioProcRevocato(aModel.getCodProcuraRevocato());
		setDataCompFoglioComplementare(aModel.getDataCompFoglioComplementare());
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setDataSospensioneSS(aModel.getDataSospensioneSS());
		setGiorniRecuperoSS(aModel.getGiorniRecuperoSS());
		setFlagRecuperoSS(aModel.getFlagRecuperoSS());
		setDataScadenzaSospensioneSS(aModel.getDataScadenzaSospensioneSS());
		setSospensioneGGSS(aModel.getSospensioneGGSS());
		setSospensioneMMSS(aModel.getSospensioneMMSS());
		setSospensioneAASS(aModel.getSospensioneAASS());
		setFlagNominaComActa(aModel.getFlagNominaComActa());
		setDescrCommActa(aModel.getDescrCommActa());
		// 07/2014
		setNumeroGiorniRevocaLA(aModel.getNumeroGiorniRevocaLA());
		// DL 92 2014 Violazione CEDU
		setNumeroGiorniRiduzionePena(aModel.getNumGiorniRiduzionePena());
		setSommaRisarcimentoDanni(aModel.getSommaRisarcimentoDanni());
		// 02/2015
		setFlagElaborato(aModel.getFlagElaborato());
		// MEV_9 aggiunti campi DATA_TERMINE_EMISSIONE e NUM_GIORNI_TERMINE_EMISSIONE
		setDataTermineEmissione(aModel.getDataTermineEmissione());
		setNumGiorniTermineEmissione(aModel.getNumGiorniTermineEmissione());

		setCondizioneUpdate(aModel.getIdDepositoDecreto());
	}

	/**
	 * Imposta le condizioni per Update e Delete
	 *
	 * @param key id del record
	 */
	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_DEPOSITO_DECRETO = " + key);
	}

	/**
	 * Valorizza le condizioni di filtro in base al contenuto del model DepositoDecretoModel passato.
	 *
	 * @param aModel
	 */
	public void setCondizione(DepositoDecretoModel aModel) {

		String lCondizioni = new String("");
		String lAppoggio = new String("");
		lInserito = false;

		if (aModel != null) {
			if (aModel.getAnnoS72() != null)
				lAppoggio = " ANNO_S72 = " + aModel.getAnnoS72();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getNumS72() != null)
				lAppoggio = " NUM_S72 = " + aModel.getNumS72();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getCodUfficioInserimento() != null
					&& aModel.getCodUfficioInserimento().trim().length() > 0)
				lAppoggio = " COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Condizione -> " + lCondizioni);

			setCondition(lCondizioni);
		}
	}

	boolean lInserito = false;

	private String setAND(String aCondizioni) {

		if (lInserito)
			aCondizioni = " AND " + aCondizioni;

		lInserito = true;

		return aCondizioni;
	}

}