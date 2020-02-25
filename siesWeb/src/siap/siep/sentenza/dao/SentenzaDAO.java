package siap.siep.sentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.sentenza.model.SentenzaModel;

/**
 * <p>
 * Title: SentenzaDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Sentenza
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
public class SentenzaDAO extends SIAPTableDAO {

	public SentenzaDAO(Connection con) {
		super(con);
		setTable("SENTENZA");

		// Settare la Sequence e i campi chiave

		setSequenceField("ID_SENTENZA", "SEN_SEQ");
		setFieldKey("ID_SENTENZA", BIG_DECIMAL);

		setField("ID_SENTENZA", BIG_DECIMAL);
		setField("COD_TIPO_PROVVEDIMENTO", STRING);
		setField("ANNO_REGE_PM", BIG_DECIMAL);
		setField("NUMERO_REGE_PM", STRING);
		setField("DATA_ARRIVO_ATTO", DATE);
		setField("DATA_PROVVEDIMENTO", DATE);
		setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
		setField("COD_LUOGO_EMITTENTE", STRING);
		setField("NUM_SEZIONE_AUTORITA_EMITTENTE", STRING);
		setField("ANNO_SENTENZA", BIG_DECIMAL);
		setField("NUMERO_SENTENZA", STRING);
		setField("DATA_IRREVOCABILITA", DATE);
		setField("DATA_ISCRIZIONE", DATE);

		setField("FLAG_SENTENZA_APPLICAZ_PENA", STRING);
		setField("COD_TIPO_PROVV_RIF", STRING);
		setField("DATA_PROVV_RIF", DATE);
		setField("COD_TIPO_AUTORITA_PROVV_RIF", STRING);
		setField("ANNO_PROVV_RIF", BIG_DECIMAL);
		setField("NUMERO_PROVV_RIF", STRING);

		setField("ANNO_PROVVEDIMENTO", BIG_DECIMAL);
		setField("NUMERO_PROVVEDIMENTO", STRING);

		setField("COD_LUOGO_PROVV_RIF", STRING);
		setField("NUM_SEZIONE_AUTORITA_PROVV_RIF", STRING);
		setField("COD_TIPO_DECISIONE_CASSAZIONE", STRING);
		setField("NOTE1_DECISIONE_CASSAZIONE", STRING);
		setField("NOTE2_DECISIONE_CASSAZIONE", STRING);
		setField("ANNO_SENTENZA_CASSAZIONE", BIG_DECIMAL);
		setField("NUMERO_SENTENZA_CASSAZIONE", STRING);
		setField("ANNO_RACCOLTA_GENERALE", BIG_DECIMAL);
		setField("NUMERO_RACCOLTA_GENERALE", STRING);
		setField("FLAG_ALTRE_SENTENZE", STRING);
		setField("DESCR_ALTRE_SENTENZE", STRING);
		setField("ANNO_REGISTRO_35", BIG_DECIMAL);
		setField("NUM_REGISTRO_35", STRING);
		setField("NOTE", STRING);
		setField("DESCR_NUM_CAMPIONE_PENALE", STRING);
		setField("ANNO_REGE_GIP", BIG_DECIMAL);
		setField("NUMERO_REGE_GIP", STRING);
		setField("ANNO_REGE_DIB", BIG_DECIMAL);
		setField("NUMERO_REGE_DIB", STRING);
		setField("ANNO_REGE_CAS", BIG_DECIMAL);
		setField("NUMERO_REGE_CAS", STRING);
		setField("ANNO_REGE_CAP", BIG_DECIMAL);
		setField("NUMERO_REGE_CAP", STRING);
		setField("ANNO_REGE_CASAP", BIG_DECIMAL);
		setField("NUMERO_REGE_CASAP", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("COD_BILANCIAMENTO_CIRCOSTANZE", STRING);
		setField("FLAG_GIUDIZIO_ABBREVIATO", STRING);
		setField("COD_TIPO_RITO", STRING);

		setField("COD_TIPO_PROVVEDIMENTO_RIF", STRING);
		setField("COD_TIPO_PROVVEDIMENTO_ALTRO", STRING);
		setField("COD_SEDE_NOTIZIA_REATO", STRING);

		setField("FLAG_VISIBILITA", STRING);
		// MEV_66: aggiunte quattro nuove proprietà
		setField("ANNO_REGE_GUP", BIG_DECIMAL);
		setField("NUMERO_REGE_GUP", STRING);
		setField("ANNO_REGE_CAPSM", BIG_DECIMAL);
		setField("NUMERO_REGE_CAPSM", STRING);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSentenza() throws DAOException {
		return getBigDecimal("ID_SENTENZA");
	}

	public String getCodTipoProvvedimento() throws DAOException {
		return getString("COD_TIPO_PROVVEDIMENTO");
	}

	public BigDecimal getAnnoRegePm() throws DAOException {
		return getBigDecimal("ANNO_REGE_PM");
	}

	public String getNumeroRegePm() throws DAOException {
		return getString("NUMERO_REGE_PM");
	}

	// public Date getDataArrivoAtto() throws DAOException { return getDate("DATA_ARRIVO_ATTO"); }
	public Date getDataProvvedimento() throws DAOException {
		return getDate("DATA_PROVVEDIMENTO");
	}

	public String getCodTipoAutoritaEmittente() throws DAOException {
		return getString("COD_TIPO_AUTORITA_EMITTENTE");
	}

	public String getCodLuogoEmittente() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE");
	}

	public String getNumSezioneAutoritaEmittente() throws DAOException {
		return getString("NUM_SEZIONE_AUTORITA_EMITTENTE");
	}

	public BigDecimal getAnnoSentenza() throws DAOException {
		return getBigDecimal("ANNO_SENTENZA");
	}

	public String getNumeroSentenza() throws DAOException {
		return getString("NUMERO_SENTENZA");
	}

	public Date getDataIrrevocabilita() throws DAOException {
		return getDate("DATA_IRREVOCABILITA");
	}

	public String getFlagSentenzaApplicazPena() throws DAOException {
		return getString("FLAG_SENTENZA_APPLICAZ_PENA");
	}

	public String getCodTipoProvvRif() throws DAOException {
		return getString("COD_TIPO_PROVV_RIF");
	}

	public Date getDataProvvRif() throws DAOException {
		return getDate("DATA_PROVV_RIF");
	}

	public String getCodTipoAutoritaProvvRif() throws DAOException {
		return getString("COD_TIPO_AUTORITA_PROVV_RIF");
	}

	public BigDecimal getAnnoProvvRif() throws DAOException {
		return getBigDecimal("ANNO_PROVV_RIF");
	}

	public String getNumeroProvvRif() throws DAOException {
		return getString("NUMERO_PROVV_RIF");
	}

	public BigDecimal getAnnoProvvedimento() throws DAOException {
		return getBigDecimal("ANNO_PROVVEDIMENTO");
	}

	public String getNumeroProvvedimento() throws DAOException {
		return getString("NUMERO_PROVVEDIMENTO");
	}

	public String getCodLuogoProvvRif() throws DAOException {
		return getString("COD_LUOGO_PROVV_RIF");
	}

	public String getNumSezioneAutoritaProvvRif() throws DAOException {
		return getString("NUM_SEZIONE_AUTORITA_PROVV_RIF");
	}

	public String getCodTipoDecisioneCassazione() throws DAOException {
		return getString("COD_TIPO_DECISIONE_CASSAZIONE");
	}

	public String getNote1DecisioneCassazione() throws DAOException {
		return getString("NOTE1_DECISIONE_CASSAZIONE");
	}

	public String getNote2DecisioneCassazione() throws DAOException {
		return getString("NOTE2_DECISIONE_CASSAZIONE");
	}

	public BigDecimal getAnnoSentenzaCassazione() throws DAOException {
		return getBigDecimal("ANNO_SENTENZA_CASSAZIONE");
	}

	public String getNumeroSentenzaCassazione() throws DAOException {
		return getString("NUMERO_SENTENZA_CASSAZIONE");
	}

	public BigDecimal getAnnoRaccoltaGenerale() throws DAOException {
		return getBigDecimal("ANNO_RACCOLTA_GENERALE");
	}

	public String getNumeroRaccoltaGenerale() throws DAOException {
		return getString("NUMERO_RACCOLTA_GENERALE");
	}

	public String getFlagAltreSentenze() throws DAOException {
		return getString("FLAG_ALTRE_SENTENZE");
	}

	public String getDescrAltreSentenze() throws DAOException {
		return getString("DESCR_ALTRE_SENTENZE");
	}

	public BigDecimal getAnnoRegistro35() throws DAOException {
		return getBigDecimal("ANNO_REGISTRO_35");
	}

	public String getNumRegistro35() throws DAOException {
		return getString("NUM_REGISTRO_35");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public String getDescrNumCampionePenale() throws DAOException {
		return getString("DESCR_NUM_CAMPIONE_PENALE");
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

	public BigDecimal getAnnoRegeCap() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAP");
	}

	public String getNumeroRegeCap() throws DAOException {
		return getString("NUMERO_REGE_CAP");
	}

	public BigDecimal getAnnoRegeCasap() throws DAOException {
		return getBigDecimal("ANNO_REGE_CASAP");
	}

	public String getNumeroRegeCasap() throws DAOException {
		return getString("NUMERO_REGE_CASAP");
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

	public Date getDataIscrizione() throws DAOException {
		return getDate("DATA_ISCRIZIONE");
	}

	public String getCodSedeNotiziaReato() throws DAOException {
		return getString("COD_SEDE_NOTIZIA_REATO");
	}

	public String getFlagVisibilita() throws DAOException {
		return getString("FLAG_VISIBILITA");
	}

	// MEV_66: aggiunte quattro nuove proprietà
	public BigDecimal getAnnoRegeGup() throws DAOException {
		return getBigDecimal("ANNO_REGE_GUP");
	}

	public String getNumeroRegeGup() throws DAOException {
		return getString("NUMERO_REGE_GUP");
	}

	public BigDecimal getAnnoRegeCapsm() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAPSM");
	}

	public String getNumeroRegeCapsm() throws DAOException {
		return getString("NUMERO_REGE_CAPSM");
	}

	//
	// METODI SET()
	//

	public void setIdSentenza(BigDecimal aValore) {
		setBigDecimal("ID_SENTENZA", aValore);
	}

	public void setCodTipoProvvedimento(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO", aValore);
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_PM", aValore);
	}

	public void setNumeroRegePm(String aValore) {
		setString("NUMERO_REGE_PM", aValore);
	}

	public void setDataArrivoAtto(Date aValore) {
		setDate("DATA_ARRIVO_ATTO", aValore);
	}

	public void setDataProvvedimento(Date aValore) {
		setDate("DATA_PROVVEDIMENTO", aValore);
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		setString("COD_TIPO_AUTORITA_EMITTENTE", aValore);
	}

	public void setCodLuogoEmittente(String aValore) {
		setString("COD_LUOGO_EMITTENTE", aValore);
	}

	public void setNumSezioneAutoritaEmittente(String aValore) {
		setString("NUM_SEZIONE_AUTORITA_EMITTENTE", aValore);
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		setBigDecimal("ANNO_SENTENZA", aValore);
	}

	public void setNumeroSentenza(String aValore) {
		setString("NUMERO_SENTENZA", aValore);
	}

	public void setDataIrrevocabilita(Date aValore) {
		setDate("DATA_IRREVOCABILITA", aValore);
	}

	public void setFlagSentenzaApplicazPena(String aValore) {
		setString("FLAG_SENTENZA_APPLICAZ_PENA", aValore);
	}

	public void setCodTipoProvvRif(String aValore) {
		setString("COD_TIPO_PROVV_RIF", aValore);
	}

	public void setDataProvvRif(Date aValore) {
		setDate("DATA_PROVV_RIF", aValore);
	}

	public void setCodTipoAutoritaProvvRif(String aValore) {
		setString("COD_TIPO_AUTORITA_PROVV_RIF", aValore);
	}

	public void setAnnoProvvRif(BigDecimal aValore) {
		setBigDecimal("ANNO_PROVV_RIF", aValore);
	}

	public void setNumeroProvvRif(String aValore) {
		setString("NUMERO_PROVV_RIF", aValore);
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		setBigDecimal("ANNO_PROVVEDIMENTO", aValore);
	}

	public void setNumeroProvvedimento(String aValore) {
		setString("NUMERO_PROVVEDIMENTO", aValore);
	}

	public void setCodLuogoProvvRif(String aValore) {
		setString("COD_LUOGO_PROVV_RIF", aValore);
	}

	public void setNumSezioneAutoritaProvvRif(String aValore) {
		setString("NUM_SEZIONE_AUTORITA_PROVV_RIF", aValore);
	}

	public void setCodTipoDecisioneCassazione(String aValore) {
		setString("COD_TIPO_DECISIONE_CASSAZIONE", aValore);
	}

	public void setNote1DecisioneCassazione(String aValore) {
		setString("NOTE1_DECISIONE_CASSAZIONE", aValore);
	}

	public void setNote2DecisioneCassazione(String aValore) {
		setString("NOTE2_DECISIONE_CASSAZIONE", aValore);
	}

	public void setAnnoSentenzaCassazione(BigDecimal aValore) {
		setBigDecimal("ANNO_SENTENZA_CASSAZIONE", aValore);
	}

	public void setNumeroSentenzaCassazione(String aValore) {
		setString("NUMERO_SENTENZA_CASSAZIONE", aValore);
	}

	public void setAnnoRaccoltaGenerale(BigDecimal aValore) {
		setBigDecimal("ANNO_RACCOLTA_GENERALE", aValore);
	}

	public void setNumeroRaccoltaGenerale(String aValore) {
		setString("NUMERO_RACCOLTA_GENERALE", aValore);
	}

	public void setFlagAltreSentenze(String aValore) {
		setString("FLAG_ALTRE_SENTENZE", aValore);
	}

	public void setDescrAltreSentenze(String aValore) {
		setString("DESCR_ALTRE_SENTENZE", aValore);
	}

	public void setAnnoRegistro35(BigDecimal aValore) {
		setBigDecimal("ANNO_REGISTRO_35", aValore);
	}

	public void setNumRegistro35(String aValore) {
		setString("NUM_REGISTRO_35", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setDescrNumCampionePenale(String aValore) {
		setString("DESCR_NUM_CAMPIONE_PENALE", aValore);
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

	public void setAnnoRegeCap(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAP", aValore);
	}

	public void setNumeroRegeCap(String aValore) {
		setString("NUMERO_REGE_CAP", aValore);
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CASAP", aValore);
	}

	public void setNumeroRegeCasap(String aValore) {
		setString("NUMERO_REGE_CASAP", aValore);
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

	public void setDataIscrizione(Date aValore) {
		setDate("DATA_ISCRIZIONE", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setCodBilanciamentoCircostanze(String aValore) {
		setString("COD_BILANCIAMENTO_CIRCOSTANZE", aValore);
	}

	public void setFlagGiudizioAbbreviato(String aValore) {
		setString("FLAG_GIUDIZIO_ABBREVIATO", aValore);
	}

	public void setCodTipoRito(String aValore) {
		setString("COD_TIPO_RITO", aValore);
	}

	public void setCodTipoProvvedimentoRif(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO_RIF", aValore);
	}

	public void setCodTipoProvvedimentoAltro(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO_ALTRO", aValore);
	}

	public void setCodSedeNotiziaReato(String aValore) {
		setString("COD_SEDE_NOTIZIA_REATO", aValore);
	}

	public void setFlagVisibilita(String aValore) {
		setString("FLAG_VISIBILITA", aValore);
	}

	// MEV_66: aggiunte quattro nuove proprietà
	public void setAnnoRegeGup(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_GUP", aValore);
	}

	public void setNumeroRegeGup(String aValore) {
		setString("NUMERO_REGE_GUP", aValore);
	}

	public void setAnnoRegeCapsm(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAPSM", aValore);
	}

	public void setNumeroRegeCapsm(String aValore) {
		setString("NUMERO_REGE_CAPSM", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new SentenzaModel();
	}

	public void setDAOFromModel(SentenzaModel aModel) throws DAOException {
		setIdSentenza(aModel.getIdSentenza());
		setCodTipoProvvedimento(aModel.getCodTipoProvvedimento());
		setAnnoRegePm(aModel.getAnnoRegePm());
		setNumeroRegePm(aModel.getNumeroRegePm());
		// setDataArrivoAtto( aModel.getDataArrivoAtto() );
		setDataProvvedimento(aModel.getDataProvvedimento());
		setCodTipoAutoritaEmittente(aModel.getCodTipoAutoritaEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setNumSezioneAutoritaEmittente(aModel.getNumSezioneAutoritaEmittente());
		setAnnoSentenza(aModel.getAnnoSentenza());
		setNumeroSentenza(aModel.getNumeroSentenza());
		// setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
		// setFlagSentenzaApplicazPena( aModel.getFlagSentenzaApplicazPena() );
		setCodTipoProvvRif(aModel.getCodTipoProvvRif());
		setDataProvvRif(aModel.getDataProvvRif());
		setCodTipoAutoritaProvvRif(aModel.getCodTipoAutoritaProvvRif());
		setAnnoProvvRif(aModel.getAnnoProvvRif());
		setNumeroProvvRif(aModel.getNumeroProvvRif());

		setAnnoProvvedimento(aModel.getAnnoProvvedimento());
		setNumeroProvvedimento(aModel.getNumeroProvvedimento());

		setCodLuogoProvvRif(aModel.getCodLuogoProvvRif());
		setNumSezioneAutoritaProvvRif(aModel.getNumSezioneAutoritaProvvRif());
		setCodTipoDecisioneCassazione(aModel.getCodTipoDecisioneCassazione());
		setNote1DecisioneCassazione(aModel.getNote1DecisioneCassazione());
		setNote2DecisioneCassazione(aModel.getNote2DecisioneCassazione());
		setAnnoSentenzaCassazione(aModel.getAnnoSentenzaCassazione());
		setNumeroSentenzaCassazione(aModel.getNumeroSentenzaCassazione());
		setAnnoRaccoltaGenerale(aModel.getAnnoRaccoltaGenerale());
		setNumeroRaccoltaGenerale(aModel.getNumeroRaccoltaGenerale());
		setFlagAltreSentenze(aModel.getFlagAltreSentenze());
		setDescrAltreSentenze(aModel.getDescrAltreSentenze());
		setAnnoRegistro35(aModel.getAnnoRegistro35());
		setNumRegistro35(aModel.getNumRegistro35());
		setNote(aModel.getNote());
		// setDescrNumCampionePenale( aModel.getDescrNumCampionePenale() );
		setAnnoRegeGip(aModel.getAnnoRegeGip());
		setNumeroRegeGip(aModel.getNumeroRegeGip());
		setAnnoRegeDib(aModel.getAnnoRegeDib());
		setNumeroRegeDib(aModel.getNumeroRegeDib());
		setAnnoRegeCas(aModel.getAnnoRegeCas());
		setNumeroRegeCas(aModel.getNumeroRegeCas());
		setAnnoRegeCap(aModel.getAnnoRegeCap());
		setNumeroRegeCap(aModel.getNumeroRegeCap());
		setAnnoRegeCasap(aModel.getAnnoRegeCasap());
		setNumeroRegeCasap(aModel.getNumeroRegeCasap());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodBilanciamentoCircostanze(aModel.getCodBilanciamentoCircostanze());
		setFlagGiudizioAbbreviato(aModel.getFlagGiudizioAbbreviato());
		setCodTipoRito(aModel.getCodTipoRito());
		setDataIscrizione(aModel.getDataIscrizione());
		setCodTipoProvvedimentoRif(aModel.getCodTipoProvvedimentoRif());
		setCodTipoProvvedimentoAltro(aModel.getCodTipoProvvedimentoAltro());
		setCodSedeNotiziaReato(aModel.getCodSedeNotiziaReato());
		setFlagVisibilita(aModel.getFlagVisibilita());
		// MEV_66: aggiunte quattro nuove proprietà
		setAnnoRegeGup(aModel.getAnnoRegeGup());
		setNumeroRegeGup(aModel.getNumeroRegeGup());
		setAnnoRegeCapsm(aModel.getAnnoRegeCapsm());
		setNumeroRegeCapsm(aModel.getNumeroRegeCapsm());
	}

	public void setDAOFromModelForUpdate(SentenzaModel aModel) throws DAOException {
		setIdSentenza(aModel.getIdSentenza());
		setCodTipoProvvedimento(aModel.getCodTipoProvvedimento());
		setAnnoRegePm(aModel.getAnnoRegePm());
		setNumeroRegePm(aModel.getNumeroRegePm());
		// setDataArrivoAtto( aModel.getDataArrivoAtto() );
		setDataProvvedimento(aModel.getDataProvvedimento());
		setCodTipoAutoritaEmittente(aModel.getCodTipoAutoritaEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setNumSezioneAutoritaEmittente(aModel.getNumSezioneAutoritaEmittente());
		setAnnoSentenza(aModel.getAnnoSentenza());
		setNumeroSentenza(aModel.getNumeroSentenza());
		// setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
		// setFlagSentenzaApplicazPena( aModel.getFlagSentenzaApplicazPena() );
		setCodTipoProvvRif(aModel.getCodTipoProvvRif());
		setDataProvvRif(aModel.getDataProvvRif());
		setCodTipoAutoritaProvvRif(aModel.getCodTipoAutoritaProvvRif());
		setAnnoProvvRif(aModel.getAnnoProvvRif());
		setNumeroProvvRif(aModel.getNumeroProvvRif());

		setAnnoProvvedimento(aModel.getAnnoProvvedimento());
		setNumeroProvvedimento(aModel.getNumeroProvvedimento());

		setCodLuogoProvvRif(aModel.getCodLuogoProvvRif());
		setNumSezioneAutoritaProvvRif(aModel.getNumSezioneAutoritaProvvRif());
		setCodTipoDecisioneCassazione(aModel.getCodTipoDecisioneCassazione());
		setNote1DecisioneCassazione(aModel.getNote1DecisioneCassazione());
		setNote2DecisioneCassazione(aModel.getNote2DecisioneCassazione());
		setAnnoSentenzaCassazione(aModel.getAnnoSentenzaCassazione());
		setNumeroSentenzaCassazione(aModel.getNumeroSentenzaCassazione());
		setAnnoRaccoltaGenerale(aModel.getAnnoRaccoltaGenerale());
		setNumeroRaccoltaGenerale(aModel.getNumeroRaccoltaGenerale());
		setFlagAltreSentenze(aModel.getFlagAltreSentenze());
		setDescrAltreSentenze(aModel.getDescrAltreSentenze());
		setAnnoRegistro35(aModel.getAnnoRegistro35());
		setNumRegistro35(aModel.getNumRegistro35());
		setNote(aModel.getNote());
		// setDescrNumCampionePenale( aModel.getDescrNumCampionePenale() );
		setAnnoRegeGip(aModel.getAnnoRegeGip());
		setNumeroRegeGip(aModel.getNumeroRegeGip());
		setAnnoRegeDib(aModel.getAnnoRegeDib());
		setNumeroRegeDib(aModel.getNumeroRegeDib());
		setAnnoRegeCas(aModel.getAnnoRegeCas());
		setNumeroRegeCas(aModel.getNumeroRegeCas());
		setAnnoRegeCap(aModel.getAnnoRegeCap());
		setNumeroRegeCap(aModel.getNumeroRegeCap());
		setAnnoRegeCasap(aModel.getAnnoRegeCasap());
		setNumeroRegeCasap(aModel.getNumeroRegeCasap());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodBilanciamentoCircostanze(aModel.getCodBilanciamentoCircostanze());
		setFlagGiudizioAbbreviato(aModel.getFlagGiudizioAbbreviato());
		setCodTipoRito(aModel.getCodTipoRito());
		setDataIscrizione(aModel.getDataIscrizione());
		setCodTipoProvvedimentoRif(aModel.getCodTipoProvvedimentoRif());
		setCodTipoProvvedimentoAltro(aModel.getCodTipoProvvedimentoAltro());
		setCodSedeNotiziaReato(aModel.getCodSedeNotiziaReato());
		setFlagVisibilita(aModel.getFlagVisibilita());
		// MEV_66: aggiunte quattro nuove proprietà
		setAnnoRegeGup(aModel.getAnnoRegeGup());
		setNumeroRegeGup(aModel.getNumeroRegeGup());
		setAnnoRegeCapsm(aModel.getAnnoRegeCapsm());
		setNumeroRegeCapsm(aModel.getNumeroRegeCapsm());
	}

	public void selCondizione(SentenzaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	/**
	 * Condizione per update
	 *
	 * @param key
	 *            chiave per update
	 */
	public void selCondizioneUpdate(BigDecimal key) {
		String lCondizioni = " ID_SENTENZA = " + key;

		setCondition(lCondizioni);
	}

}