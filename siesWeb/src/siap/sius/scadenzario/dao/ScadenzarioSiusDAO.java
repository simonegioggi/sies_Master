package siap.sius.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * ScadenzarioSiusDAO - Classe DAO che rappresenta la tabella ScadenzarioSius
 *
 * @version 1.0
 */
public class ScadenzarioSiusDAO extends TableDAO {

	public ScadenzarioSiusDAO(Connection con) {

		super(con);
		setTable("SCADENZARIO_SIUS");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_SCADENZARIO_SIUS", "SCA_SIU_SEQ");
		setFieldKey("ID_SCADENZARIO_SIUS", BIG_DECIMAL);
		setField("ID_SCADENZARIO_SIUS", BIG_DECIMAL);
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
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdScadenzarioSius() throws DAOException {
		return getBigDecimal("ID_SCADENZARIO_SIUS");
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

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	//
	// METODI SET()
	//
	public void setIdScadenzarioSius(BigDecimal aValore) {
		setBigDecimal("ID_SCADENZARIO_SIUS", aValore);
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		// MEV_2026-1: aggiunti 'null' in coda
		return new ScadenzarioSiusModel(getIdScadenzarioSius(), getCodTipoScadenzario(), "",
				getDataInizioScadenza(), getDataFineScadenza(), getFlagVisto(), getDataVisto(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(), "",
				null, getFasSiuIdFascicoloSius(), null, getEveIdEvento(), null, null, null, null, null, null,
				null);
	}

	public void setDAOFromModel(ScadenzarioSiusModel aModel) throws DAOException {

		setIdScadenzarioSius(aModel.getIdScadenzarioSius());
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
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setEveIdEvento(aModel.getEveIdEvento());
	}

	public void setDAOFromModelForUpdate(ScadenzarioSiusModel aModel) throws DAOException {

		setIdScadenzarioSius(aModel.getIdScadenzarioSius());
		setCodTipoScadenzario(aModel.getCodTipoScadenzario());
		setDataInizioScadenza(aModel.getDataInizioScadenza());
		setDataFineScadenza(aModel.getDataFineScadenza());
		setFlagVisto(aModel.getFlagVisto());
		setDataVisto(aModel.getDataVisto());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setEveIdEvento(aModel.getEveIdEvento());
		setCondizioneUpdate(aModel.getIdScadenzarioSius());
	}

	public void setDAOFromModelForUpdateVisto(ScadenzarioSiusModel aModel) throws DAOException {

		setIdScadenzarioSius(aModel.getIdScadenzarioSius());
		setFlagVisto(aModel.getFlagVisto());
		setDataVisto(aModel.getDataVisto());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCondizioneUpdate(aModel.getIdScadenzarioSius());
	}

	public void setCondizione(ScadenzarioSiusModel aModel) {

		String lCondizioni = new String();
		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {

		setCondition(" ID_SCADENZARIO_SIUS = " + key);
	}

	public void setCondizioneDeleteByTipo(BigDecimal idFasSius, String aTipo) {

		setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + idFasSius + " AND COD_TIPO_SCADENZARIO = " + aTipo);
	}

	public void setCondizioniByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo) {

		String lStatement = new String();
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "'";
		lStatement += " AND COD_TIPO_SCADENZARIO = " + aTipo;
		// 31/10/2007 Aggiunta la setCondition mancante e modificato l'output del metodo.
		setCondition(lStatement);
	}

}