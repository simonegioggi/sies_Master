package siap.siep.penaaccessoria.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;

/**
 * PenaAccessoriaDAO - Classe DAO che rappresenta la tabella PenaAccessoria
 *
 * @version 1.0
 */
public class PenaAccessoriaDAO extends SIAPTableDAO {

	public PenaAccessoriaDAO(Connection con) {

		super(con);
		setTable("PENA_ACCESSORIA");

		setSequenceField("ID_PENA_ACCESSORIA", "PEN_ACC_SEQ");

		setFieldKey("ID_PENA_ACCESSORIA", BIG_DECIMAL);

		setField("COD_TIPO_PENA_ACCESSORIA", STRING);
		setField("DURATA", STRING);
		setField("NUM_ANNI", BIG_DECIMAL);
		setField("NUM_MESI", BIG_DECIMAL);
		setField("NUM_GIORNI", BIG_DECIMAL);
		setField("FLAG_CONDONATA", STRING);
		setField("DATA_DPR", DATE);
		setField("NUM_DPR", STRING);
		setField("FLAG_DICHIARAZIONE_FALSITA", STRING);
		setField("FLAG_REVOCA_CONDONO", STRING);
		setField("DATA_SENTENZA_REVOCA", DATE);
		setField("ANNO_SENTENZA_REVOCA", BIG_DECIMAL);
		setField("NUMERO_SENTENZA_REVOCA", STRING);
		setField("COD_TIPO_UFFICIO_SENTENZA_REVO", STRING);
		setField("COD_LUOGO_SENTENZA_REVOCA", STRING);
		setField("ANNO_REGE_PM_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_PM_REVOCA", STRING);
		setField("ANNO_REGE_GIP_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_GIP_REVOCA", STRING);
		setField("ANNO_REGE_DIB_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_DIB_REVOCA", STRING);
		setField("ANNO_REGE_CAS_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_CAS_REVOCA", STRING);
		setField("ANNO_REGE_CAP_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_CAP_REVOCA", STRING);
		setField("ANNO_REGE_CASAP_REVOCA", BIG_DECIMAL);
		setField("NUMERO_REGE_CASAP_REVOCA", STRING);
		setField("NOTE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("DATA_INIZIO_VALIDITA", DATE);
		setField("DATA_FINE_VALIDITA", DATE);
		setField("ANNO_ORDINANZA_GE", BIG_DECIMAL);
		setField("NUMERO_ORDINANZA_GE", BIG_DECIMAL);
		setField("DATA_ORDINANZA_GE", DATE);
		setField("ESTREMI_CONDONO", STRING);
		setField("ID_PENA_ACCESSORIA_ORIGINE", BIG_DECIMAL);
		setField("COD_NUOVO_TIPO_PENA_ACCESSORIA", STRING);
		setField("COD_TIPO_UFFICIO_ORDINANZA_GE", STRING);
		setField("COD_LUOGO_UFFICIO_ORDINANZA_GE", STRING);
		setField("DATA_ORDINANZA_PA", DATE);
		setField("ANNO_ORDINANZA_PA", BIG_DECIMAL);
		setField("NUMERO_ORDINANZA_PA", BIG_DECIMAL);
		setField("COD_TIPO_UFFICIO_ORDINANZA_PA", STRING);
		setField("COD_LUOGO_UFFICIO_ORDINANZA_PA", STRING);
		setField("DESCR_ALTRE_PA", STRING); // 04/04/2006
		setField("COD_FONTE_GE", STRING); // 04/04/2006
		setField("ANNO_FONTE_GE", STRING); // 04/04/2006
		setField("NUMERO_FONTE_GE", STRING); // 04/04/2006
		setField("COD_SOTTONUMERAZIONE_GE", STRING); // 04/04/2006
		setField("COMMA_GE", STRING); // 04/04/2006
		setField("LETTERA_GE", STRING); // 04/04/2006
		setField("NUMERO_GE", STRING); // 04/04/2006
		setField("ARTICOLO_GE", STRING); // 04/04/2006

		setField("BEN_ID_BENEFICIO", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdPenaAccessoria() throws DAOException {
		return getBigDecimal("ID_PENA_ACCESSORIA");
	}

	public String getCodTipoPenaAccessoria() throws DAOException {
		return getString("COD_TIPO_PENA_ACCESSORIA");
	}

	public String getDurata() throws DAOException {
		return getString("DURATA");
	}

	public String getDescrDurata() throws DAOException {
		return getString("DESCRDURATA");
	}

	public BigDecimal getNumAnni() throws DAOException {
		return getBigDecimal("NUM_ANNI");
	}

	public BigDecimal getNumMesi() throws DAOException {
		return getBigDecimal("NUM_MESI");
	}

	public BigDecimal getNumGiorni() throws DAOException {
		return getBigDecimal("NUM_GIORNI");
	}

	public String getFlagCondonata() throws DAOException {
		return getString("FLAG_CONDONATA");
	}

	public Date getDataDpr() throws DAOException {
		return getDate("DATA_DPR");
	}

	public String getNumDpr() throws DAOException {
		return getString("NUM_DPR");
	}

	public String getFlagDichiarazioneFalsita() throws DAOException {
		return getString("FLAG_DICHIARAZIONE_FALSITA");
	}

	public String getFlagRevocaCondono() throws DAOException {
		return getString("FLAG_REVOCA_CONDONO");
	}

	public Date getDataSentenzaRevoca() throws DAOException {
		return getDate("DATA_SENTENZA_REVOCA");
	}

	public BigDecimal getAnnoSentenzaRevoca() throws DAOException {
		return getBigDecimal("ANNO_SENTENZA_REVOCA");
	}

	public String getNumeroSentenzaRevoca() throws DAOException {
		return getString("NUMERO_SENTENZA_REVOCA");
	}

	public String getCodTipoUfficioSentenzaRevo() throws DAOException {
		return getString("COD_TIPO_UFFICIO_SENTENZA_REVO");
	}

	public String getCodLuogoSentenzaRevoca() throws DAOException {
		return getString("COD_LUOGO_SENTENZA_REVOCA");
	}

	public BigDecimal getAnnoRegePmRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_PM_REVOCA");
	}

	public String getNumeroRegePmRevoca() throws DAOException {
		return getString("NUMERO_REGE_PM_REVOCA");
	}

	public BigDecimal getAnnoRegeGipRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_GIP_REVOCA");
	}

	public String getNumeroRegeGipRevoca() throws DAOException {
		return getString("NUMERO_REGE_GIP_REVOCA");
	}

	public BigDecimal getAnnoRegeDibRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_DIB_REVOCA");
	}

	public String getNumeroRegeDibRevoca() throws DAOException {
		return getString("NUMERO_REGE_DIB_REVOCA");
	}

	public BigDecimal getAnnoRegeCasRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAS_REVOCA");
	}

	public String getNumeroRegeCasRevoca() throws DAOException {
		return getString("NUMERO_REGE_CAS_REVOCA");
	}

	public BigDecimal getAnnoRegeCapRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAP_REVOCA");
	}

	public String getNumeroRegeCapRevoca() throws DAOException {
		return getString("NUMERO_REGE_CAP_REVOCA");
	}

	public BigDecimal getAnnoRegeCasapRevoca() throws DAOException {
		return getBigDecimal("ANNO_REGE_CASAP_REVOCA");
	}

	public String getNumeroRegeCasapRevoca() throws DAOException {
		return getString("NUMERO_REGE_CASAP_REVOCA");
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

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public Date getDataInizioValidita() throws DAOException {
		return getDate("DATA_INIZIO_VALIDITA");
	}

	public Date getDataFineValidita() throws DAOException {
		return getDate("DATA_FINE_VALIDITA");
	}

	public BigDecimal getAnnoOrdinanzaGE() throws DAOException {
		return getBigDecimal("ANNO_ORDINANZA_GE");
	}

	public BigDecimal getNumeroOrdinanzaGE() throws DAOException {
		return getBigDecimal("NUMERO_ORDINANZA_GE");
	}

	public Date getDataOrdinanzaGE() throws DAOException {
		return getDate("DATA_ORDINANZA_GE");
	}

	public String getEstremiCondono() throws DAOException {
		return getString("ESTREMI_CONDONO");
	}

	public BigDecimal getIdPenaAccessoriaOrigine() throws DAOException {
		return getBigDecimal("ID_PENA_ACCESSORIA_ORIGINE");
	}

	public String getCodNuovoTipoPenaAccessoria() throws DAOException {
		return getString("COD_NUOVO_TIPO_PENA_ACCESSORIA");
	}

	public String getCodTipoUfficioOrdinanzaGE() throws DAOException {
		return getString("COD_TIPO_UFFICIO_ORDINANZA_GE");
	}

	public String getCodLuogoUfficioOrdinanzaGE() throws DAOException {
		return getString("COD_LUOGO_UFFICIO_ORDINANZA_GE");
	}

	public Date getDataOrdinanzaPA() throws DAOException {
		return getDate("DATA_ORDINANZA_PA");
	}

	public BigDecimal getAnnoOrdinanzaPA() throws DAOException {
		return getBigDecimal("ANNO_ORDINANZA_PA");
	}

	public BigDecimal getNumeroOrdinanzaPA() throws DAOException {
		return getBigDecimal("NUMERO_ORDINANZA_PA");
	}

	public String getCodTipoUfficioOrdinanzaPA() throws DAOException {
		return getString("COD_TIPO_UFFICIO_ORDINANZA_PA");
	}

	public String getCodLuogoUfficioOrdinanzaPA() throws DAOException {
		return getString("COD_LUOGO_UFFICIO_ORDINANZA_PA");
	}

	public String getDescrAltrePA() throws DAOException {
		return getString("DESCR_ALTRE_PA");
	} // 04/04/2006

	public String getCodFonteGE() throws DAOException {
		return getString("COD_FONTE_GE");
	} // 04/04/2006

	public String getDescrFonteGE() throws DAOException {
		return getString("DESCR_FONTE_GE");
	} // 04/04/2006

	public String getAnnoFonteGE() throws DAOException {
		return getString("ANNO_FONTE_GE");
	} // 04/04/2006

	public String getNumeroFonteGE() throws DAOException {
		return getString("NUMERO_FONTE_GE");
	} // 04/04/2006

	public String getCodSottonumerazioneGE() throws DAOException {
		return getString("COD_SOTTONUMERAZIONE_GE");
	} // 04/04/2006

	public String getDescrSottonumerazioneGE() throws DAOException {
		return getString("DESCR_SOTTONUMERAZIONE_GE");
	} // 04/04/2006

	public String getCommaGE() throws DAOException {
		return getString("COMMA_GE");
	} // 04/04/2006

	public String getLetteraGE() throws DAOException {
		return getString("LETTERA_GE");
	} // 04/04/2006

	public String getNumeroGE() throws DAOException {
		return getString("NUMERO_GE");
	} // 04/04/2006

	public String getArticoloGE() throws DAOException {
		return getString("ARTICOLO_GE");
	} // 04/04/2006

	public BigDecimal getBenIdBeneficio() throws DAOException {
		return getBigDecimal("BEN_ID_BENEFICIO");
	}

	//
	// METODI SET()
	//
	public void setIdPenaAccessoria(BigDecimal aValore) {
		setBigDecimal("ID_PENA_ACCESSORIA", aValore);
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		setString("COD_TIPO_PENA_ACCESSORIA", aValore);
	}

	public void setDurata(String aValore) {
		setString("DURATA", aValore);
	}

	public void setDescrDurata(String aValore) {
		setString("DESCRDURATA", aValore);
	}

	public void setNumAnni(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI", aValore);
	}

	public void setNumMesi(BigDecimal aValore) {
		setBigDecimal("NUM_MESI", aValore);
	}

	public void setNumGiorni(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI", aValore);
	}

	public void setFlagCondonata(String aValore) {
		setString("FLAG_CONDONATA", aValore);
	}

	public void setDataDpr(Date aValore) {
		setDate("DATA_DPR", aValore);
	}

	public void setNumDpr(String aValore) {
		setString("NUM_DPR", aValore);
	}

	public void setFlagDichiarazioneFalsita(String aValore) {
		setString("FLAG_DICHIARAZIONE_FALSITA", aValore);
	}

	public void setFlagRevocaCondono(String aValore) {
		setString("FLAG_REVOCA_CONDONO", aValore);
	}

	public void setDataSentenzaRevoca(Date aValore) {
		setDate("DATA_SENTENZA_REVOCA", aValore);
	}

	public void setAnnoSentenzaRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_SENTENZA_REVOCA", aValore);
	}

	public void setNumeroSentenzaRevoca(String aValore) {
		setString("NUMERO_SENTENZA_REVOCA", aValore);
	}

	public void setCodTipoUfficioSentenzaRevo(String aValore) {
		setString("COD_TIPO_UFFICIO_SENTENZA_REVO", aValore);
	}

	public void setCodLuogoSentenzaRevoca(String aValore) {
		setString("COD_LUOGO_SENTENZA_REVOCA", aValore);
	}

	public void setAnnoRegePmRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_PM_REVOCA", aValore);
	}

	public void setNumeroRegePmRevoca(String aValore) {
		setString("NUMERO_REGE_PM_REVOCA", aValore);
	}

	public void setAnnoRegeGipRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_GIP_REVOCA", aValore);
	}

	public void setNumeroRegeGipRevoca(String aValore) {
		setString("NUMERO_REGE_GIP_REVOCA", aValore);
	}

	public void setAnnoRegeDibRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_DIB_REVOCA", aValore);
	}

	public void setNumeroRegeDibRevoca(String aValore) {
		setString("NUMERO_REGE_DIB_REVOCA", aValore);
	}

	public void setAnnoRegeCasRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAS_REVOCA", aValore);
	}

	public void setNumeroRegeCasRevoca(String aValore) {
		setString("NUMERO_REGE_CAS_REVOCA", aValore);
	}

	public void setAnnoRegeCapRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAP_REVOCA", aValore);
	}

	public void setNumeroRegeCapRevoca(String aValore) {
		setString("NUMERO_REGE_CAP_REVOCA", aValore);
	}

	public void setAnnoRegeCasapRevoca(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CASAP_REVOCA", aValore);
	}

	public void setNumeroRegeCasapRevoca(String aValore) {
		setString("NUMERO_REGE_CASAP_REVOCA", aValore);
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setDataInizioValidita(Date aValore) {
		setDate("DATA_INIZIO_VALIDITA", aValore);
	}

	public void setDataFineValidita(Date aValore) {
		setDate("DATA_FINE_VALIDITA", aValore);
	}

	public void setAnnoOrdinanzaGE(BigDecimal aValore) {
		setBigDecimal("ANNO_ORDINANZA_GE", aValore);
	}

	public void setNumeroOrdinanzaGE(BigDecimal aValore) {
		setBigDecimal("NUMERO_ORDINANZA_GE", aValore);
	}

	public void setDataOrdinanzaGE(Date aValore) {
		setDate("DATA_ORDINANZA_GE", aValore);
	}

	public void setEstremiCondono(String aValore) {
		setString("ESTREMI_CONDONO", aValore);
	}

	public void setIdPenaAccessoriaOrigine(BigDecimal aValore) {
		setBigDecimal("ID_PENA_ACCESSORIA_ORIGINE", aValore);
	}

	public void setCodNuovoTipoPenaAccessoria(String aValore) {
		setString("COD_NUOVO_TIPO_PENA_ACCESSORIA", aValore);
	}

	public void setCodTipoUfficioOrdinanza_GE(String aValore) {
		setString("COD_TIPO_UFFICIO_ORDINANZA_GE", aValore);
	}

	public void setCodLuogoUfficioOrdinanza_GE(String aValore) {
		setString("COD_LUOGO_UFFICIO_ORDINANZA_GE", aValore);
	}

	public void setDataOrdinanzaPA(Date aValore) {
		setDate("DATA_ORDINANZA_PA", aValore);
	}

	public void setAnnoOrdinanzaPA(BigDecimal aValore) {
		setBigDecimal("ANNO_ORDINANZA_PA", aValore);
	}

	public void setNumeroOrdinanzaPA(BigDecimal aValore) {
		setBigDecimal("NUMERO_ORDINANZA_PA", aValore);
	}

	public void setCodTipoUfficioOrdinanzaPA(String aValore) {
		setString("COD_TIPO_UFFICIO_ORDINANZA_PA", aValore);
	}

	public void setCodLuogoUfficioOrdinanzaPA(String aValore) {
		setString("COD_LUOGO_UFFICIO_ORDINANZA_PA", aValore);
	}

	public void setDescrAltrePA(String aValore) {
		setString("DESCR_ALTRE_PA", aValore);
	} // 04/04/2006

	public void setCodFonteGE(String aValore) {
		setString("COD_FONTE_GE", aValore);
	} // 04/04/2006

	public void setDescrFonteGE(String aValore) {
		setString("DESCR_FONTE_GE", aValore);
	} // 04/04/2006

	public void setAnnoFonteGE(String aValore) {
		setString("ANNO_FONTE_GE", aValore);
	} // 04/04/2006

	public void setNumeroFonteGE(String aValore) {
		setString("NUMERO_FONTE_GE", aValore);
	} // 04/04/2006

	public void setCodSottonumerazioneGE(String aValore) {
		setString("COD_SOTTONUMERAZIONE_GE", aValore);
	} // 04/04/2006

	public void setCommaGE(String aValore) {
		setString("COMMA_GE", aValore);
	} // 04/04/2006

	public void setLetteraGE(String aValore) {
		setString("LETTERA_GE", aValore);
	} // 04/04/2006

	public void setNumeroGE(String aValore) {
		setString("NUMERO_GE", aValore);
	} // 04/04/2006

	public void setArticoloGE(String aValore) {
		setString("ARTICOLO_GE", aValore);
	} // 04/04/2006

	public void setBenIdBeneficio(BigDecimal aValore) {
		setBigDecimal("BEN_ID_BENEFICIO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new PenaAccessoriaModel(getIdPenaAccessoria(), getCodTipoPenaAccessoria(), "", getDurata(), "",
				getNumAnni(), getNumMesi(), getNumGiorni(), getFlagCondonata(), getDataDpr(), getNumDpr(),
				getFlagDichiarazioneFalsita(), getFlagRevocaCondono(), getDataSentenzaRevoca(),
				getAnnoSentenzaRevoca(), getNumeroSentenzaRevoca(), getCodTipoUfficioSentenzaRevo(), "",
				getCodLuogoSentenzaRevoca(), "", getAnnoRegePmRevoca(), getNumeroRegePmRevoca(),
				getAnnoRegeGipRevoca(), getNumeroRegeGipRevoca(), getAnnoRegeDibRevoca(),
				getNumeroRegeDibRevoca(), getAnnoRegeCasRevoca(), getNumeroRegeCasRevoca(),
				getAnnoRegeCapRevoca(), getNumeroRegeCapRevoca(), getAnnoRegeCasapRevoca(),
				getNumeroRegeCasapRevoca(), getNote(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getFasSieIdFascicoloSiep(), getDataInizioValidita(),
				getDataFineValidita(), getAnnoOrdinanzaGE(), getNumeroOrdinanzaGE(), getDataOrdinanzaGE(),
				getEstremiCondono(), getIdPenaAccessoriaOrigine(), getCodNuovoTipoPenaAccessoria(), "", null,
				getCodTipoUfficioOrdinanzaGE(), "", getCodLuogoUfficioOrdinanzaGE(), "", getDataOrdinanzaPA(),
				getAnnoOrdinanzaPA(), getNumeroOrdinanzaPA(), getCodTipoUfficioOrdinanzaPA(), "",
				getCodLuogoUfficioOrdinanzaPA(), "", getDescrAltrePA(), getCodFonteGE(), "", getAnnoFonteGE(),
				getNumeroFonteGE(), getCodSottonumerazioneGE(), "", getCommaGE(), getLetteraGE(),
				getNumeroGE(), getArticoloGE(), getBenIdBeneficio());
	}

	public void setDAOFromModel(PenaAccessoriaModel aModel) throws DAOException {

		setIdPenaAccessoria(aModel.getIdPenaAccessoria());
		setCodTipoPenaAccessoria(aModel.getCodTipoPenaAccessoria());
		setDurata(aModel.getDurata());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		setFlagCondonata(aModel.getFlagCondonata());
		setDataDpr(aModel.getDataDpr());
		setNumDpr(aModel.getNumDpr());
		setFlagDichiarazioneFalsita(aModel.getFlagDichiarazioneFalsita());
		setFlagRevocaCondono(aModel.getFlagRevocaCondono());
		setDataSentenzaRevoca(aModel.getDataSentenzaRevoca());
		setAnnoSentenzaRevoca(aModel.getAnnoSentenzaRevoca());
		setNumeroSentenzaRevoca(aModel.getNumeroSentenzaRevoca());
		setCodTipoUfficioSentenzaRevo(aModel.getCodTipoUfficioSentenzaRevo());
		setCodLuogoSentenzaRevoca(aModel.getCodLuogoSentenzaRevoca());
		setAnnoRegePmRevoca(aModel.getAnnoRegePmRevoca());
		setNumeroRegePmRevoca(aModel.getNumeroRegePmRevoca());
		setAnnoRegeGipRevoca(aModel.getAnnoRegeGipRevoca());
		setNumeroRegeGipRevoca(aModel.getNumeroRegeGipRevoca());
		setAnnoRegeDibRevoca(aModel.getAnnoRegeDibRevoca());
		setNumeroRegeDibRevoca(aModel.getNumeroRegeDibRevoca());
		setAnnoRegeCasRevoca(aModel.getAnnoRegeCasRevoca());
		setNumeroRegeCasRevoca(aModel.getNumeroRegeCasRevoca());
		setAnnoRegeCapRevoca(aModel.getAnnoRegeCapRevoca());
		setNumeroRegeCapRevoca(aModel.getNumeroRegeCapRevoca());
		setAnnoRegeCasapRevoca(aModel.getAnnoRegeCasapRevoca());
		setNumeroRegeCasapRevoca(aModel.getNumeroRegeCasapRevoca());
		setNote(aModel.getNote());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setDataInizioValidita(aModel.getDataInizioValidita());
		setDataFineValidita(aModel.getDataFineValidita());
		setAnnoOrdinanzaGE(aModel.getAnnoOrdinanzaGE());
		setNumeroOrdinanzaGE(aModel.getNumeroOrdinanzaGE());
		setDataOrdinanzaGE(aModel.getDataOrdinanzaGE());
		setEstremiCondono(aModel.getEstremiCondono());
		setIdPenaAccessoriaOrigine(aModel.getIdPenaAccessoriaOrigine());
		setCodNuovoTipoPenaAccessoria(aModel.getCodNuovoTipoPenaAccessoria());
		setCodTipoUfficioOrdinanza_GE(aModel.getCodTipoUfficioOrdinanzaGE());
		setCodLuogoUfficioOrdinanza_GE(aModel.getCodLuogoUfficioOrdinanzaGE());
		setDataOrdinanzaPA(aModel.getDataOrdinanzaPA());
		setAnnoOrdinanzaPA(aModel.getAnnoOrdinanzaPA());
		setNumeroOrdinanzaPA(aModel.getNumeroOrdinanzaPA());
		setCodTipoUfficioOrdinanzaPA(aModel.getCodTipoUfficioOrdinanzaPA());
		setCodLuogoUfficioOrdinanzaPA(aModel.getCodLuogoUfficioOrdinanzaPA());
		setDescrAltrePA(aModel.getDescrAltrePA()); // 04/04/2006
		setCodFonteGE(aModel.getCodFonteGE()); // 04/04/2006
		setAnnoFonteGE(aModel.getAnnoFonteGE()); // 04/04/2006
		setNumeroFonteGE(aModel.getNumeroFonteGE()); // 04/04/2006
		setCodSottonumerazioneGE(aModel.getCodSottonumerazioneGE()); // 04/04/2006
		setCommaGE(aModel.getCommaGE()); // 04/04/2006
		setLetteraGE(aModel.getLetteraGE()); // 04/04/2006
		setNumeroGE(aModel.getNumeroGE()); // 04/04/2006
		setArticoloGE(aModel.getArticoloGE()); // 04/04/2006

		setBenIdBeneficio(aModel.getBenIdBeneficio());
	}

	public void setDAOFromModelForUpdate(PenaAccessoriaModel aModel) throws DAOException {

		// setIdPenaAccessoria( aModel.getIdPenaAccessoria() );
		setCodTipoPenaAccessoria(aModel.getCodTipoPenaAccessoria());
		setDurata(aModel.getDurata());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		setFlagCondonata(aModel.getFlagCondonata());
		setDataDpr(aModel.getDataDpr());
		setNumDpr(aModel.getNumDpr());
		setFlagDichiarazioneFalsita(aModel.getFlagDichiarazioneFalsita());
		setFlagRevocaCondono(aModel.getFlagRevocaCondono());
		setDataSentenzaRevoca(aModel.getDataSentenzaRevoca());
		setAnnoSentenzaRevoca(aModel.getAnnoSentenzaRevoca());
		setNumeroSentenzaRevoca(aModel.getNumeroSentenzaRevoca());
		setCodTipoUfficioSentenzaRevo(aModel.getCodTipoUfficioSentenzaRevo());
		setCodLuogoSentenzaRevoca(aModel.getCodLuogoSentenzaRevoca());
		setAnnoRegePmRevoca(aModel.getAnnoRegePmRevoca());
		setNumeroRegePmRevoca(aModel.getNumeroRegePmRevoca());
		setAnnoRegeGipRevoca(aModel.getAnnoRegeGipRevoca());
		setNumeroRegeGipRevoca(aModel.getNumeroRegeGipRevoca());
		setAnnoRegeDibRevoca(aModel.getAnnoRegeDibRevoca());
		setNumeroRegeDibRevoca(aModel.getNumeroRegeDibRevoca());
		setAnnoRegeCasRevoca(aModel.getAnnoRegeCasRevoca());
		setNumeroRegeCasRevoca(aModel.getNumeroRegeCasRevoca());
		setAnnoRegeCapRevoca(aModel.getAnnoRegeCapRevoca());
		setNumeroRegeCapRevoca(aModel.getNumeroRegeCapRevoca());
		setAnnoRegeCasapRevoca(aModel.getAnnoRegeCasapRevoca());
		setNumeroRegeCasapRevoca(aModel.getNumeroRegeCasapRevoca());
		setNote(aModel.getNote());
		// setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		// setDataInserimento( aModel.getDataInserimento() );
		// setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setDataInizioValidita(aModel.getDataInizioValidita());
		setDataFineValidita(aModel.getDataFineValidita());
		setAnnoOrdinanzaGE(aModel.getAnnoOrdinanzaGE());
		setNumeroOrdinanzaGE(aModel.getNumeroOrdinanzaGE());
		setDataOrdinanzaGE(aModel.getDataOrdinanzaGE());
		setEstremiCondono(aModel.getEstremiCondono());
		setIdPenaAccessoriaOrigine(aModel.getIdPenaAccessoriaOrigine());
		setCodNuovoTipoPenaAccessoria(aModel.getCodNuovoTipoPenaAccessoria());
		setCodTipoUfficioOrdinanza_GE(aModel.getCodTipoUfficioOrdinanzaGE());
		setCodLuogoUfficioOrdinanza_GE(aModel.getCodLuogoUfficioOrdinanzaGE());
		setDataOrdinanzaPA(aModel.getDataOrdinanzaPA());
		setAnnoOrdinanzaPA(aModel.getAnnoOrdinanzaPA());
		setNumeroOrdinanzaPA(aModel.getNumeroOrdinanzaPA());
		setCodTipoUfficioOrdinanzaPA(aModel.getCodTipoUfficioOrdinanzaPA());
		setCodLuogoUfficioOrdinanzaPA(aModel.getCodLuogoUfficioOrdinanzaPA());
		setDescrAltrePA(aModel.getDescrAltrePA()); // 04/04/2006
		setCodFonteGE(aModel.getCodFonteGE()); // 04/04/2006
		setAnnoFonteGE(aModel.getAnnoFonteGE()); // 04/04/2006
		setNumeroFonteGE(aModel.getNumeroFonteGE()); // 04/04/2006
		setCodSottonumerazioneGE(aModel.getCodSottonumerazioneGE()); // 04/04/2006
		setCommaGE(aModel.getCommaGE()); // 04/04/2006
		setLetteraGE(aModel.getLetteraGE()); // 04/04/2006
		setNumeroGE(aModel.getNumeroGE()); // 04/04/2006
		setArticoloGE(aModel.getArticoloGE()); // 04/04/2006
		setBenIdBeneficio(aModel.getBenIdBeneficio());
	}

	public void setDAOFromModelForUpdateOrdinanzaPA(PenaAccessoriaModel aModel) throws DAOException {

		if (aModel.getDescrAltrePA().trim() != "")
			setDescrAltrePA(aModel.getDescrAltrePA());

		if (aModel.getFlagCondonata().compareTo("-") != 0) {
			setFlagCondonata(aModel.getFlagCondonata());
			setDataOrdinanzaPA(aModel.getDataOrdinanzaPA());
			setAnnoOrdinanzaPA(aModel.getAnnoOrdinanzaPA());
			setNumeroOrdinanzaPA(aModel.getNumeroOrdinanzaPA());
			setCodTipoUfficioOrdinanzaPA(aModel.getCodTipoUfficioOrdinanzaPA());
			setCodLuogoUfficioOrdinanzaPA(aModel.getCodLuogoUfficioOrdinanzaPA());
			setCodFonteGE(aModel.getCodFonteGE());
			setAnnoFonteGE(aModel.getAnnoFonteGE());
			setNumeroFonteGE(aModel.getNumeroFonteGE());
			setArticoloGE(aModel.getArticoloGE());
			setCodSottonumerazioneGE(aModel.getCodSottonumerazioneGE());
			setCommaGE(aModel.getCommaGE());
			setLetteraGE(aModel.getLetteraGE());
			setNumeroGE(aModel.getNumeroGE());
		}
		if (aModel.getCodNuovoTipoPenaAccessoria().compareTo("-") != 0) {
			setCodNuovoTipoPenaAccessoria(aModel.getCodNuovoTipoPenaAccessoria());
			// setDurata( aModel.getDurata() );
			// setNumAnni( aModel.getNumAnni() );
			// setNumMesi( aModel.getNumMesi() );
			// setNumGiorni( aModel.getNumGiorni() );

			if ((aModel.getFlagCondonata().compareTo("S") == 0)
					|| (aModel.getFlagCondonata().compareTo("T") == 0)) {
				setDataFineValidita(aModel.getDataAggiornamento());
			}
		}

		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}

	public void setDAOFromModelForUpdateCodNuovoTipoPA(PenaAccessoriaModel aModel) throws DAOException {

		setCodNuovoTipoPenaAccessoria(aModel.getCodNuovoTipoPenaAccessoria());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}

	public void selCondizione(PenaAccessoriaModel aModel) {

		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void selCondizioneUpdateBenIdBeneficioFascSiep(BigDecimal keyBen, BigDecimal keyFasc) {

		String lCondizioni = " BEN_ID_BENEFICIO = " + keyBen;
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + keyFasc;
		setCondition(lCondizioni);
	}

	public void selCondizioneUpdate(BigDecimal key) {

		String lCondizioni = " ID_PENA_ACCESSORIA = " + key;

		setCondition(lCondizioni);
	}

	public void selCondizioneFascSiep(BigDecimal key) {

		String lCondizioni = " FAS_SIE_ID_FASCICOLO_SIEP = " + key;

		setCondition(lCondizioni);
	}

}