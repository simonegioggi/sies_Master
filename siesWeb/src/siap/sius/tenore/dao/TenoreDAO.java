package siap.sius.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

// STUB 04/11/2003 Rework x Dettaglio Motivo Provvedimento:
// Aggiunti riferimenti a "COD_DETTAGLIO_OGGETTO" e a "CodDettaglioOggetto"

/**
 * <p>
 * Title: TenoreDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Tenore
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
public class TenoreDAO extends SIAPTableDAO {

	public TenoreDAO(Connection con) {
		// Setto la Sequence e i campi chiave
		super(con);
		setTable("TENORE");

		setSequenceField("ID_TENORE", "TEN_SEQ");

		setFieldKey("ID_TENORE", BIG_DECIMAL);

		setField("COD_ESITO_TENORE", STRING);
		setField("DATA", DATE);
		setField("COD_MAGISTRATO", STRING);
		setField("NOTE", STRING);
		setField("COD_OGGETTO_TENORE", STRING);
		setField("PROGR_TENORE", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("DEP_OPID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
		setField("IMP_ID_IMPUGNAZIONE", BIG_DECIMAL);
		setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
		setField("COD_DETTAGLIO_OGGETTO", STRING);
		setField("DATA_FINE", DATE); // Luigi 9-12-2003
		setField("DEP_ID_DEPOSITO_SENTENZA", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdTenore() throws DAOException {
		return getBigDecimal("ID_TENORE");
	}

	public String getCodEsitoTenore() throws DAOException {
		return getString("COD_ESITO_TENORE");
	}

	public Date getData() throws DAOException {
		return getDate("DATA");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public String getCodOggettoTenore() throws DAOException {
		return getString("COD_OGGETTO_TENORE");
	}

	public BigDecimal getProgrTenore() throws DAOException {
		return getBigDecimal("PROGR_TENORE");
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

	public BigDecimal getDepOpidDepositoOrdinanzaPc() throws DAOException {
		return getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC");
	}

	public BigDecimal getImpIdImpugnazione() throws DAOException {
		return getBigDecimal("IMP_ID_IMPUGNAZIONE");
	}

	public BigDecimal getDepDecIdDepositoDecreto() throws DAOException {
		return getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO");
	}

	public String getCodDettaglioOggetto() throws DAOException {
		return getString("COD_DETTAGLIO_OGGETTO");
	}

	public Date getDataFine() throws DAOException {
		return getDate("DATA_FINE");
	}

	public BigDecimal getDepIdDepositoSentenza() throws DAOException {
		return getBigDecimal("DEP_ID_DEPOSITO_SENTENZA");
	}

	//
	// METODI SET()
	//

	public void setIdTenore(BigDecimal aValore) {
		setBigDecimal("ID_TENORE", aValore);
	}

	public void setCodEsitoTenore(String aValore) {
		setString("COD_ESITO_TENORE", aValore);
	}

	public void setData(Date aValore) {
		setDate("DATA", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setCodOggettoTenore(String aValore) {
		setString("COD_OGGETTO_TENORE", aValore);
	}

	public void setProgrTenore(BigDecimal aValore) {
		setBigDecimal("PROGR_TENORE", aValore);
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

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		setBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC", aValore);
	}

	public void setImpIdImpugnazione(BigDecimal aValore) {
		setBigDecimal("IMP_ID_IMPUGNAZIONE", aValore);
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		setBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO", aValore);
	}

	public void setCodDettaglioOggetto(String aValore) {
		setString("COD_DETTAGLIO_OGGETTO", aValore);
	}

	public void setDataFine(Date aValore) {
		setDate("DATA_FINE", aValore);
	}

	public void setDepIdDepositoSentenza(BigDecimal aValore) {
		setBigDecimal("DEP_ID_DEPOSITO_SENTENZA", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new TenoreModel(getIdTenore(), getCodEsitoTenore(), "", getData(), getCodMagistrato(), "",
				getNote(), getCodOggettoTenore(), "", getProgrTenore(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), "", getGenPridGeneraleProcedimento(),
				getDepOpidDepositoOrdinanzaPc(), getImpIdImpugnazione(), getDepDecIdDepositoDecreto(),
				getCodDettaglioOggetto(), "", this.getDataFine(), "", getDepIdDepositoSentenza());
	}

	public void setDAOFromModel(TenoreModel aModel) throws DAOException {
		setIdTenore(aModel.getIdTenore());
		setCodEsitoTenore(aModel.getCodEsitoTenore());
		setData(aModel.getData());
		setCodMagistrato(aModel.getCodMagistrato());
		setNote(aModel.getNote());
		setCodOggettoTenore(aModel.getCodOggettoTenore());
		setProgrTenore(aModel.getProgrTenore());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		setImpIdImpugnazione(aModel.getImpIdImpugnazione());
		setDepDecIdDepositoDecreto(aModel.getDepDecIdDepositoDecreto());
		setCodDettaglioOggetto(aModel.getCodDettaglioOggetto());
		this.setDataFine(aModel.getDataFine());
		setDepIdDepositoSentenza(aModel.getDepIdDepositoSentenza());
	}

	public void setDAOFromModelForUpdate(TenoreModel aModel) throws DAOException {
		setIdTenore(aModel.getIdTenore());
		setCodEsitoTenore(aModel.getCodEsitoTenore());
		setData(aModel.getData());
		setCodMagistrato(aModel.getCodMagistrato());
		setNote(aModel.getNote());
		setCodOggettoTenore(aModel.getCodOggettoTenore());
		setProgrTenore(aModel.getProgrTenore());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		setImpIdImpugnazione(aModel.getImpIdImpugnazione());
		setDepDecIdDepositoDecreto(aModel.getDepDecIdDepositoDecreto());
		setCodDettaglioOggetto(aModel.getCodDettaglioOggetto());
		setDataFine(aModel.getDataFine());
		setCondizioneUpdate(aModel.getIdTenore());
		setDepIdDepositoSentenza(aModel.getDepIdDepositoSentenza());
	}

	public void setDAOFromModelForUpdateDepOrd(TenoreModel aModel) throws DAOException {
		// setIdTenore( aModel.getIdTenore() );
		setCodEsitoTenore(aModel.getCodEsitoTenore());
		setData(aModel.getData());
		setCodMagistrato(aModel.getCodMagistrato());
		setNote(aModel.getNote());
		// setCodOggettoTenore( aModel.getCodOggettoTenore() );
		setProgrTenore(aModel.getProgrTenore());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		// setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		setImpIdImpugnazione(aModel.getImpIdImpugnazione());
		setDepDecIdDepositoDecreto(aModel.getDepDecIdDepositoDecreto());
		// setCodDettaglioOggetto( aModel.getCodDettaglioOggetto() );
		setDataFine(aModel.getDataFine());
		setDepIdDepositoSentenza(aModel.getDepIdDepositoSentenza());

		setCondizioneUpdate(aModel.getIdTenore());
	}

	// Vincenzo 05-01-2007
	public void setDAOFromModelForDeleteStralcio(TenoreModel aModel) throws DAOException {
		setCodEsitoTenore("-");
		setCodMagistrato("-");
		setData(null);
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setNote(" ");
		setDataFine(null);

		setCondizioneUpdate(aModel.getIdTenore());
	}

	public void setCondizione(TenoreModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	/**
	 * Imposta condizione di update per la chiave id_tenore.
	 * <p>
	 * 
	 * @param key
	 *            valore chiave id_tenore per update.
	 */
	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_TENORE = " + key);
	}

	/**
	 * Imposta condizione di delete per la chiave generale procedimento.
	 * <p>
	 * 
	 * @param key
	 *            valore chiave id_tenore per update.
	 */
	public void setCondizioneDelete(BigDecimal key) {
		setCondition(" GEN_PRID_GENERALE_PROCEDIMENTO = " + key);
	}

	/*
	 * Setta le condizioni di update per annullare il riferimento al record Deposito Ordinanza PC da
	 * cancellare
	 */

	public void setDAOForDeleteDepOrd(DepositoOrdinanzaPcModel aDepOrd) {
		this.setDepOpidDepositoOrdinanzaPc(null);
		this.setDataAggiornamento(aDepOrd.getDataAggiornamento());
		this.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
		this.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());

		// 30/01/2008 Bonifica Aggiornamento Tenori in casi di Cancellazione/modifica provvedimenti
		this.setCodEsitoTenore("-");
		this.setData(null);
		this.setCodMagistrato(null);

		setCondition(" DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aDepOrd.getIdDepositoOrdinanzaPc());
	}

	/*
	 * Setta le condizioni di update per annullare il riferimento al record Deposito Decreto da cancellare
	 */

	public void setDAOForDeleteDepDec(DepositoDecretoModel aDepDec) {
		this.setDepDecIdDepositoDecreto(null);
		this.setDataAggiornamento(aDepDec.getDataAggiornamento());
		this.setCodOperatoreAggiornamento(aDepDec.getCodOperatoreAggiornamento());
		this.setCodUfficioAggiornamento(aDepDec.getCodUfficioAggiornamento());

		// 30/01/2008 Bonifica Aggiornamento Tenori in casi di Cancellazione/modifica provvedimenti
		this.setCodEsitoTenore("-");
		this.setData(null);
		this.setCodMagistrato(null);

		setCondition(" DEP_DEC_ID_DEPOSITO_DECRETO = " + aDepDec.getIdDepositoDecreto());
	}

	/*
	 * Setta le condizioni di update per valorizzare data_fine per tutti i record collegati al generale
	 * procedimento
	 */

	public void setDAOFromModelForUpdateDataFine(TenoreModel aModel) {
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataFine(aModel.getDataFine());
		setCondition(" DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aModel.getGenPridGeneraleProcedimento());
	}

	/*
	 * Setta le condizioni di update per valorizzare data_fine per tutti i record collegati al generale
	 * procedimento esclusi i tenori della lista indicata
	 */

	public void setDAOFromModelForUpdateDataFine(TenoreModel aModel, String listaTenori) {
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataFine(aModel.getDataFine());

		String lCondizione = " DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aModel.getGenPridGeneraleProcedimento();
		lCondizione += " AND COD_OGGETTO_TENORE NOT IN ( " + listaTenori + " )";
		setCondition(lCondizione);

		// setCondition(" DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = " +
		// aModel.getGenPridGeneraleProcedimento() );
	}

	public void setDAOFromModelForUpdateSenzaDataFine(TenoreModel aModel) {
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDepOpidDepositoOrdinanzaPc(aModel.getDepOpidDepositoOrdinanzaPc());
		// MEV_39: gestione differente per codTenore = "2422"
		if ("2422".equals(aModel.getCodOggettoTenore()))
			setCodDettaglioOggetto(aModel.getCodDettaglioOggetto());
		setCondition(" DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aModel.getGenPridGeneraleProcedimento());
	}

	// Vincenzo 19-12-2006
	public void setDAOFromModelForUpdateStralcio(TenoreModel aModel) throws DAOException {
		setCodEsitoTenore(aModel.getCodEsitoTenore());
		setCodMagistrato(aModel.getCodMagistrato());
		setData(aModel.getData());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setNote(aModel.getNote());
		setDataFine(aModel.getDataFine());

		setCondizioneUpdate(aModel.getIdTenore());
	}

	// 20131130 - Paolo
	/**
	 * Preposto alla modifica del condice magistrato del tenore. ( decidere se gneralizzarlo oppure fissarlo
	 * per il deposito ord )
	 * <p>
	 * 
	 * @param aModel
	 */
	public void setDAOFromModelForUpdateMagistratoByOrdinanza(TenoreModel aModel) {
		setCodMagistrato(aModel.getCodMagistrato());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCondition(" DEP_OPID_DEPOSITO_ORDINANZA_PC=" + aModel.getDepOpidDepositoOrdinanzaPc());
	}

	// 20131201 - Paolo
	/**
	 * Preposto alla modifica del condice magistrato del tenore. ( decidere se gneralizzarlo oppure fissarlo
	 * per il deposito decreto )
	 * <p>
	 * 
	 * @param aModel
	 */
	public void setDAOFromModelForUpdateMagistratoByDecreto(TenoreModel aModel) {
		setCodMagistrato(aModel.getCodMagistrato());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCondition(" DEP_DEC_ID_DEPOSITO_DECRETO = " + aModel.getDepDecIdDepositoDecreto());
	}

	/*
	 * Setta le condizioni di update per annullare il riferimento al record Deposito Sentenza da cancellare
	 */

	public void setDAOForDeleteDepSen(DepositoSentenzaModel aDepSen) {
		this.setDepIdDepositoSentenza(null);
		this.setDataAggiornamento(aDepSen.getDataAggiornamento());
		this.setCodOperatoreAggiornamento(aDepSen.getCodOperatoreAggiornamento());
		this.setCodUfficioAggiornamento(aDepSen.getCodUfficioAggiornamento());

		// 30/01/2008 Bonifica Aggiornamento Tenori in casi di Cancellazione/modifica provvedimenti
		this.setCodEsitoTenore("-");
		this.setData(null);
		this.setCodMagistrato(null);

		setCondition(" DEP_ID_DEPOSITO_SENTENZA = " + aDepSen.getIdDepositoSentenza());
	}

	public void setDAOFromModelForUpdateMagistratoBySentenza(TenoreModel aModel) {
		setCodMagistrato(aModel.getCodMagistrato());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCondition(" DEP_ID_DEPOSITO_SENTENZA =" + aModel.getDepIdDepositoSentenza());
	}

}
