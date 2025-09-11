package siap.siep.sentenzariunita.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;

/**
 * SentenzaRiunitaDAO - Classe DAO che rappresenta la tabella SentenzaRiunita
 *
 * @version 1.0
 */
public class SentenzaRiunitaDAO extends SIAPTableDAO {

	public SentenzaRiunitaDAO(Connection con) {

		super(con);
		setTable("SENTENZA_RIUNITA");

		// Settare la Sequence e i campi chiave
		setFieldKey("ID_SENTENZA_RIUNITA", BIG_DECIMAL);
		setSequenceField("ID_SENTENZA_RIUNITA", "SEN_RIU_SEQ");

		setField("ID_SENTENZA_RIUNITA", BIG_DECIMAL);
		setField("DATA_SENTENZA", DATE);
		setField("ANNO_SENTENZA", BIG_DECIMAL);
		setField("NUMERO_SENTENZA", STRING);
		setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
		setField("COD_AUTORITA_EMITTENTE", STRING);
		setField("COD_LUOGO_EMITTENTE", STRING);
		setField("SEZIONE_AUTORITA_EMITTENTE", STRING);
		setField("ANNO_REGE_PM", BIG_DECIMAL);
		setField("NUMERO_REGE_PM", STRING);
		setField("ANNO_REGE_GIP", BIG_DECIMAL);
		setField("NUMERO_REGE_GIP", STRING);
		setField("ANNO_REGE_DIB", BIG_DECIMAL);
		setField("NUMERO_REGE_DIB", STRING);
		setField("ANNO_REGE_CAS", BIG_DECIMAL);
		setField("NUMERO_REGE_CAS", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("SEN_ID_SENTENZA", BIG_DECIMAL);

		setField("COD_SEDE_NOTIZIA_REATO", STRING);

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSentenzaRiunita() throws DAOException {
		return getBigDecimal("ID_SENTENZA_RIUNITA");
	}

	public Date getDataSentenza() throws DAOException {
		return getDate("DATA_SENTENZA");
	}

	public BigDecimal getAnnoSentenza() throws DAOException {
		return getBigDecimal("ANNO_SENTENZA");
	}

	public String getNumeroSentenza() throws DAOException {
		return getString("NUMERO_SENTENZA");
	}

	public String getCodTipoAutoritaEmittente() throws DAOException {
		return getString("COD_TIPO_AUTORITA_EMITTENTE");
	}

	public String getCodAutoritaEmittente() throws DAOException {
		return getString("COD_AUTORITA_EMITTENTE");
	}

	public String getCodLuogoEmittente() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE");
	}

	public String getSezioneAutoritaEmittente() throws DAOException {
		return getString("SEZIONE_AUTORITA_EMITTENTE");
	}

	public BigDecimal getAnnoRegePm() throws DAOException {
		return getBigDecimal("ANNO_REGE_PM");
	}

	public String getNumeroRegePm() throws DAOException {
		return getString("NUMERO_REGE_PM");
	}

	public BigDecimal getAnnoRegeGip() throws DAOException {
		return getBigDecimal("ANNO_REGE_GIP");
	}

	public String getNumeroRegeGip() throws DAOException {
		return getString("NUMERO_REGE_GIP");
	}

	public BigDecimal getAnnoRegeDib() throws DAOException {
		return getBigDecimal("ANNO_REGE_DIB");
	}

	public String getNumeroRegeDib() throws DAOException {
		return getString("NUMERO_REGE_DIB");
	}

	public BigDecimal getAnnoRegeCas() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAS");
	}

	public String getNumeroRegeCas() throws DAOException {
		return getString("NUMERO_REGE_CAS");
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

	public BigDecimal getSenIdSentenza() throws DAOException {
		return getBigDecimal("SEN_ID_SENTENZA");
	}

	public String getCodSedeNotiziaReato() throws DAOException {
		return getString("COD_SEDE_NOTIZIA_REATO");
	}

	//
	// METODI SET()
	//

	public void setIdSentenzaRiunita(BigDecimal aValore) {
		setBigDecimal("ID_SENTENZA_RIUNITA", aValore);
	}

	public void setDataSentenza(Date aValore) {
		setDate("DATA_SENTENZA", aValore);
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		setBigDecimal("ANNO_SENTENZA", aValore);
	}

	public void setNumeroSentenza(String aValore) {
		setString("NUMERO_SENTENZA", aValore);
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		setString("COD_TIPO_AUTORITA_EMITTENTE", aValore);
	}

	public void setCodAutoritaEmittente(String aValore) {
		setString("COD_AUTORITA_EMITTENTE", aValore);
	}

	public void setCodLuogoEmittente(String aValore) {
		setString("COD_LUOGO_EMITTENTE", aValore);
	}

	public void setSezioneAutoritaEmittente(String aValore) {
		setString("SEZIONE_AUTORITA_EMITTENTE", aValore);
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_PM", aValore);
	}

	public void setNumeroRegePm(String aValore) {
		setString("NUMERO_REGE_PM", aValore);
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_GIP", aValore);
	}

	public void setNumeroRegeGip(String aValore) {
		setString("NUMERO_REGE_GIP", aValore);
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_DIB", aValore);
	}

	public void setNumeroRegeDib(String aValore) {
		setString("NUMERO_REGE_DIB", aValore);
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAS", aValore);
	}

	public void setNumeroRegeCas(String aValore) {
		setString("NUMERO_REGE_CAS", aValore);
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

	public void setSenIdSentenza(BigDecimal aValore) {
		setBigDecimal("SEN_ID_SENTENZA", aValore);
	}

	public void setCodSedeNotiziaReato(String aValore) {
		setString("COD_SEDE_NOTIZIA_REATO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new SentenzaRiunitaModel(getIdSentenzaRiunita(), getDataSentenza(), getAnnoSentenza(),
				getNumeroSentenza(), getCodTipoAutoritaEmittente(), "", getCodAutoritaEmittente(), "",
				getCodLuogoEmittente(), "", getSezioneAutoritaEmittente(), getAnnoRegePm(), getNumeroRegePm(),
				getAnnoRegeGip(), getNumeroRegeGip(), getAnnoRegeDib(), getNumeroRegeDib(), getAnnoRegeCas(),
				getNumeroRegeCas(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getSenIdSentenza(), getCodSedeNotiziaReato(), "");
	}

	public void setDAOFromModel(SentenzaRiunitaModel aModel) throws DAOException {

		setIdSentenzaRiunita(aModel.getIdSentenzaRiunita());
		setDataSentenza(aModel.getDataSentenza());
		setAnnoSentenza(aModel.getAnnoSentenza());
		setNumeroSentenza(aModel.getNumeroSentenza());
		setCodTipoAutoritaEmittente(aModel.getCodTipoAutoritaEmittente());
		setCodAutoritaEmittente(aModel.getCodAutoritaEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setSezioneAutoritaEmittente(aModel.getSezioneAutoritaEmittente());
		setAnnoRegePm(aModel.getAnnoRegePm());
		setNumeroRegePm(aModel.getNumeroRegePm());
		setAnnoRegeGip(aModel.getAnnoRegeGip());
		setNumeroRegeGip(aModel.getNumeroRegeGip());
		setAnnoRegeDib(aModel.getAnnoRegeDib());
		setNumeroRegeDib(aModel.getNumeroRegeDib());
		setAnnoRegeCas(aModel.getAnnoRegeCas());
		setNumeroRegeCas(aModel.getNumeroRegeCas());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setSenIdSentenza(aModel.getSenIdSentenza());

		setCodSedeNotiziaReato(aModel.getCodSedeNotiziaReato());
	}

	public void setDAOFromModelForUpdate(SentenzaRiunitaModel aModel) throws DAOException {

		setDataSentenza(aModel.getDataSentenza());
		setAnnoSentenza(aModel.getAnnoSentenza());
		setNumeroSentenza(aModel.getNumeroSentenza());
		setCodTipoAutoritaEmittente(aModel.getCodTipoAutoritaEmittente());
		setCodAutoritaEmittente(aModel.getCodAutoritaEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setSezioneAutoritaEmittente(aModel.getSezioneAutoritaEmittente());
		setAnnoRegePm(aModel.getAnnoRegePm());
		setNumeroRegePm(aModel.getNumeroRegePm());
		setAnnoRegeGip(aModel.getAnnoRegeGip());
		setNumeroRegeGip(aModel.getNumeroRegeGip());
		setAnnoRegeDib(aModel.getAnnoRegeDib());
		setNumeroRegeDib(aModel.getNumeroRegeDib());
		setAnnoRegeCas(aModel.getAnnoRegeCas());
		setNumeroRegeCas(aModel.getNumeroRegeCas());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());

		setCodSedeNotiziaReato(aModel.getCodSedeNotiziaReato());

		selCondizioneUpdate(aModel.getIdSentenzaRiunita());
	}

	public void selCondizione(SentenzaRiunitaModel aModel) {

		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void selCondizioneUpdate(BigDecimal key) {

		setCondition(" ID_SENTENZA_RIUNITA = " + key);
	}

}