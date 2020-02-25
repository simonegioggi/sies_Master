package siap.sige.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.model.TenoreSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: TenoreSigeDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella TenoreSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class TenoreSigeDAO extends TableDAO {

	public TenoreSigeDAO(Connection con) {

		super(con);
		setTable("TENORE_SIGE");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_TENORE_SIGE", "TEN_SIGE_SEQ");
		setFieldKey("ID_TENORE_SIGE", BIG_DECIMAL);

		setField("ID_TENORE_SIGE", BIG_DECIMAL);
		setField("COD_OGGETTO_SIGE", STRING);
		setField("COD_ESITO_SIGE", STRING);
		setField("DATA", DATE);
		setField("DATA_FINE", DATE);
		setField("RIC_SIG_ID_RICHIESTA_SIGE", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("PROV_ID_PROVVEDIMENTO_SIGE", BIG_DECIMAL);
		setField("FAS_ID_FASCICOLO_SIGE", BIG_DECIMAL);
		setField("NOTE", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdTenoreSige() throws DAOException {
		return getBigDecimal("ID_TENORE_SIGE");
	}

	public String getCodOggettoSige() throws DAOException {
		return getString("COD_OGGETTO_SIGE");
	}

	public String getCodEsitoSige() throws DAOException {
		return getString("COD_ESITO_SIGE");
	}

	public Date getData() throws DAOException {
		return getDate("DATA");
	}

	public Date getDataFine() throws DAOException {
		return getDate("DATA_FINE");
	}

	public BigDecimal getRicSigIdRichiestaSige() throws DAOException {
		return getBigDecimal("RIC_SIG_ID_RICHIESTA_SIGE");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public BigDecimal getProvIdProvvedimentoSige() throws DAOException {
		return getBigDecimal("PROV_ID_PROVVEDIMENTO_SIGE");
	}

	public BigDecimal getFasIdFascicoloSige() throws DAOException {
		return getBigDecimal("FAS_ID_FASCICOLO_SIGE");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	//
	// METODI SET()
	//
	public void setIdTenoreSige(BigDecimal aValore) {
		setBigDecimal("ID_TENORE_SIGE", aValore);
	}

	public void setCodOggettoSige(String aValore) {
		setString("COD_OGGETTO_SIGE", aValore);
	}

	public void setCodEsitoSige(String aValore) {
		setString("COD_ESITO_SIGE", aValore);
	}

	public void setData(Date aValore) {
		setDate("DATA", aValore);
	}

	public void setDataFine(Date aValore) {
		setDate("DATA_FINE", aValore);
	}

	public void setRicSigIdRichiestaSige(BigDecimal aValore) {
		setBigDecimal("RIC_SIG_ID_RICHIESTA_SIGE", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setProvIdProvvedimentoSige(BigDecimal aValore) {
		setBigDecimal("PROV_ID_PROVVEDIMENTO_SIGE", aValore);
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		setBigDecimal("FAS_ID_FASCICOLO_SIGE", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new TenoreSigeModel(getIdTenoreSige(), getCodOggettoSige(), "", getCodEsitoSige(), "",
				getData(), getDataFine(), getRicSigIdRichiestaSige(), getCodOperatoreInserimento(),
				getCodUfficioInserimento(), "", getDataInserimento(), getCodOperatoreAggiornamento(),
				getCodUfficioAggiornamento(), "", getDataAggiornamento(), null, null,
				getProvIdProvvedimentoSige(), getFasIdFascicoloSige(), null, getNote(), "", "", "", "");
	}

	public void setDAOFromModel(TenoreSigeModel aModel) throws DAOException {

		setIdTenoreSige(aModel.getIdTenoreSige());
		setCodOggettoSige(aModel.getCodOggettoSige());
		setCodEsitoSige(aModel.getCodEsitoSige());
		setData(aModel.getData());
		setDataFine(aModel.getDataFine());
		setRicSigIdRichiestaSige(aModel.getRicSigIdRichiestaSige());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setProvIdProvvedimentoSige(aModel.getProvIdProvvedimentoSige());
		setFasIdFascicoloSige(aModel.getFasIdFascicoloSige());
		setNote(aModel.getNote());
	}

	public void setDAOFromModelForUpdateEsito(TenoreSigeModel aModel) throws DAOException {

		setCodEsitoSige(aModel.getCodEsitoSige());
		setData(aModel.getData());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setNote(aModel.getNote());
		selCondizioneDelete(aModel.getIdTenoreSige());
	}

	/**
	 * Valorizzazione dei campi in tabella necessari all'update per la chiusura/storicizzazione di un tenore.
	 * Si noti che i dati dell'operatore di aggiornamento vengono presi da quello dell'inserimento e la data
	 * fine dalla data nel model.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOPerStoricizzare(TenoreSigeModel aModel) throws DAOException {

		setDataFine(aModel.getData());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreInserimento());
		setCodUfficioAggiornamento(aModel.getCodUfficioInserimento());
		setDataAggiornamento(aModel.getDataInserimento());
	}

	public void selCondizioneFasSige(BigDecimal aIdFasSige) {

		setCondition(" FAS_ID_FASCICOLO_SIGE = " + aIdFasSige);
	}

	public void setCondizioneFasSigePerStoricizzazione(BigDecimal aIdFasSige) {

		String lCondition = new String();
		lCondition = " FAS_ID_FASCICOLO_SIGE = " + aIdFasSige + " AND DATA_FINE IS NULL";
		setCondition(lCondition);
	}

	public void selCondizione(TenoreSigeModel aModel) {

		String lCondizioni = new String();
		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void selCondizioneIdProvvedimento(BigDecimal aIdProvSige) {

		setCondition(" PROV_ID_PROVVEDIMENTO_SIGE = " + aIdProvSige);
	}

	public void selCondizioneIdRichiestaSige(BigDecimal aIdRichiestaSige) {

		setCondition(" RIC_SIG_ID_RICHIESTA_SIGE = " + aIdRichiestaSige);
	}

	/**
	 * Predispone la condizione di filtro per cancellare il record individuato dalla chiave primaria
	 * ID_TENORE_SIGE.
	 */
	public void selCondizioneDelete(BigDecimal aIdTenoreSige) {

		setCondition(" ID_TENORE_SIGE = " + aIdTenoreSige);
	}

	/**
	 * Prepara l'update sulla tabella per la cancellazione di un Provvedimento.
	 * 
	 * @param aProvModel
	 * @throws DAOException
	 */
	public void setDAOUpdateTenoriForDeleteProvvedimento(ProvvedimentoSigeModel aProvModel)
			throws DAOException {

		if (aProvModel.getIdProvvedimentoSige() == null)
			throw new DAOException("ID Provvedimento SIGE assente!");

		String aStatement = " FAS_ID_FASCICOLO_SIGE = " + aProvModel.getFasIdFascicoloSige();
		aStatement += " AND ((PROV_ID_PROVVEDIMENTO_SIGE is not null AND PROV_ID_PROVVEDIMENTO_SIGE <> "
				+ aProvModel.getIdProvvedimentoSige() + " )";
		aStatement += "  OR  (PROV_ID_PROVVEDIMENTO_SIGE is null AND RIC_SIG_ID_RICHIESTA_SIGE is not null ))";
		aStatement += " AND TO_CHAR(DATA_AGGIORNAMENTO, 'yyyyMMddHH24miss') = '"
				+ DateUtils.getDateToString(aProvModel.getDataInserimento(), "yyyyMMddHHmmss") + "'";
		setCondition(aStatement);
	}

	/*
	 * Setta le condizioni di update per annullare il riferimento al record Provvedimento da cancellare
	 */
	public void setDAOForDeleteProvvedimento(ProvvedimentoSigeModel aProvvedimento) {

		setProvIdProvvedimentoSige(null);
		setDataAggiornamento(aProvvedimento.getDataAggiornamento());
		setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());

		// 20190508 [SG]: eliminato impostazioni proprieta'
		// setCodEsitoSige("-");
		// setData(null);
		// Aggiornamento Tenori in casi di Cancellazione/modifica provvedimenti
		setCodEsitoSige("-");
		setData(null);

		setCondition(" PROV_ID_PROVVEDIMENTO_SIGE = " + aProvvedimento.getIdProvvedimentoSige());
	}

}