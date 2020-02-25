package siap.siep.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.scadenzario.model.ScadenzarioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ScadenzarioDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Scadenzario
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
public class ScadenzarioDAO extends TableDAO {

	public ScadenzarioDAO(Connection con) {

		super(con);

		setTable("SCADENZARIO_SIEP");

		setSequenceField("ID_SCADENZARIO_SIEP", "SCA_SIE_SEQ");
		setFieldKey("ID_SCADENZARIO_SIEP", BIG_DECIMAL);

		setField("ID_SCADENZARIO_SIEP", BIG_DECIMAL);
		setField("COD_TIPO_SCADENZARIO", STRING);
		setField("DATA_INIZIO_SCADENZA", DATE);
		setField("DATA_FINE_SCADENZA", DATE);
		setField("FLAG_VISTO", STRING);
		setField("DATA_VISTO", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("NOT_ID_NOTIFICA", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("COD_STATO_NOTIFICA", STRING);

		// MEV_39 (aggiunto un nuovo campo sulla tabella SCADENZARIO_SIEP)
		setField("RIF_FASC_SIEP_ORIG", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdScadenzario() throws DAOException {
		return getBigDecimal("ID_SCADENZARIO_SIEP");
	}

	public String getCodTipoScadenzario() throws DAOException {
		return getString("COD_TIPO_SCADENZARIO");
	}

	public Date getDataInizioScadenza() throws DAOException {
		return getDate("DATA_INIZIO_SCADENZA");
	}

	public Date getDataFineScadenza() throws DAOException {
		return getDate("DATA_FINE_SCADENZA");
	}

	public String getFlagVisto() throws DAOException {
		return getString("FLAG_VISTO");
	}

	public Date getDataVisto() throws DAOException {
		return getDate("DATA_VISTO");
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

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getNotIdNotifica() throws DAOException {
		return getBigDecimal("NOT_ID_NOTIFICA");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public String getCodStatoNotifica() throws DAOException {
		return getString("COD_STATO_NOTIFICA");
	}

	// MEV_39 (Aggiungo il metodo get per il nuovo campo)
	public BigDecimal getIdFascicoloSiepOrigine() throws DAOException {
		return getBigDecimal("RIF_FASC_SIEP_ORIG");
	}

	//
	// METODI SET()
	//

	public void setIdScadenzario(BigDecimal aValore) {
		setBigDecimal("ID_SCADENZARIO_SIEP", aValore);
	}

	public void setCodTipoScadenzario(String aValore) {
		setString("COD_TIPO_SCADENZARIO", aValore);
	}

	public void setDataInizioScadenza(Date aValore) {
		setDate("DATA_INIZIO_SCADENZA", aValore);
	}

	public void setDataFineScadenza(Date aValore) {
		setDate("DATA_FINE_SCADENZA", aValore);
	}

	public void setFlagVisto(String aValore) {
		setString("FLAG_VISTO", aValore);
	}

	public void setDataVisto(Date aValore) {
		setDate("DATA_VISTO", aValore);
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setNotIdNotifica(BigDecimal aValore) {
		setBigDecimal("NOT_ID_NOTIFICA", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setCodStatoNotifica(String aValore) {
		setString("COD_STATO_NOTIFICA", aValore);
	}

	// MEV_39 (Aggiungo il metodo set per il nuovo campo)
	public void setIdFascicoloSiepOrigine(BigDecimal aValore) {
		setBigDecimal("RIF_FASC_SIEP_ORIG", aValore);
	}

	public GenericModel getModel() throws DAOException {

		ScadenzarioModel lScad = new ScadenzarioModel();

		lScad.setIdScadenzario(getIdScadenzario());
		lScad.setCodTipoScadenzario(getCodTipoScadenzario());
		lScad.setDataInizioScadenza(getDataInizioScadenza());
		lScad.setDataFineScadenza(getDataFineScadenza());
		lScad.setFlagVisto(getFlagVisto());
		lScad.setDataVisto(getDataVisto());
		lScad.setCodOperatoreInserimento(getCodOperatoreInserimento());
		lScad.setDataInserimento(getDataInserimento());
		lScad.setCodUfficioInserimento(getCodUfficioInserimento());
		lScad.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
		lScad.setDataAggiornamento(getDataAggiornamento());
		lScad.setCodUfficioAggiornamento(getCodUfficioAggiornamento());
		lScad.setFasSieIdFascicoloSiep(getFasSieIdFascicoloSiep());
		lScad.setNotIdNotifica(getNotIdNotifica());
		lScad.setEveIdEvento(getEveIdEvento());
		lScad.setCodStatoNotifica(getCodStatoNotifica());
		lScad.setIdFascicoloSiepOrigine(getIdFascicoloSiepOrigine());
		return lScad;
	}

	public void setDAOFromModel(ScadenzarioModel aModel) throws DAOException {

		setCodTipoScadenzario(aModel.getCodTipoScadenzario());
		setDataInizioScadenza(aModel.getDataInizioScadenza());
		setDataFineScadenza(aModel.getDataFineScadenza());
		setFlagVisto(aModel.getFlagVisto());
		setDataVisto(aModel.getDataVisto());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setNotIdNotifica(aModel.getNotIdNotifica());
		setEveIdEvento(aModel.getEveIdEvento());
		setCodStatoNotifica(aModel.getCodStatoNotifica());
		setIdFascicoloSiepOrigine(aModel.getIdFascicoloSiepOrigine());
	}

	public void setDAOFromModelForUpdate(ScadenzarioModel aModel) throws DAOException {

		// setIdScadenzario( aModel.getIdScadenzario() );
		setCodTipoScadenzario(aModel.getCodTipoScadenzario());
		setDataInizioScadenza(aModel.getDataInizioScadenza());
		setDataFineScadenza(aModel.getDataFineScadenza());
		setFlagVisto(aModel.getFlagVisto());
		setDataVisto(aModel.getDataVisto());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		// setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
		setCodStatoNotifica(aModel.getCodStatoNotifica());
		setEveIdEvento(aModel.getEveIdEvento());

		setCondizioneUpdate(aModel.getIdScadenzario());
	}

	public void setCondizione(ScadenzarioModel aModel) {

		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_SCADENZARIO_SIEP = " + key);
	}

	public void setCondizioneDelete(BigDecimal key) {
		setCondition(" ID_SCADENZARIO_SIEP = " + key);
	}

	public void setCondizioneForDelete(BigDecimal key) {
		setCondition(" ID_SCADENZARIO_SIEP = " + key + " AND COD_TIPO_SCADENZARIO = '01'");
	}

	public void setCondizioneByIdFascicoloSiepTipoScadenzario(BigDecimal aIdFascicoloSiep,
			String aTipoScadenzario) {
		setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + " AND COD_TIPO_SCADENZARIO = '"
				+ aTipoScadenzario + "'");
	}

	public void setCondizioneByEveIdEventoTipoScadenzario(BigDecimal aIdEvento, String aTipoScadenzario) {
		setCondition(" EVE_ID_EVENTO = " + aIdEvento + " AND COD_TIPO_SCADENZARIO = '" + aTipoScadenzario
				+ "'");
	}

	public void setCondizioneByIdNotifica(BigDecimal aIdNotifica) {
		setCondition(" NOT_ID_NOTIFICA = " + aIdNotifica);
	}

}