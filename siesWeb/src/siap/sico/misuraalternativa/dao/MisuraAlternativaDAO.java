package siap.sico.misuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;

/**
 * Title: MisuraAlternativaDAO
 * Description: Classe DAO che rappresenta la tabella MisuraAlternativa
 *
 * @version 1.0
 */

public class MisuraAlternativaDAO extends TableDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MisuraAlternativaDAO(Connection con) {

		super(con);
		setTable("MISURA_ALTERNATIVA");

		// Settare la Sequence e i campi chiave
		this.setSequenceField("ID_MISURA_ALTERNATIVA", "MIS_ALT_SEQ");
		this.setFieldKey("ID_MISURA_ALTERNATIVA", BIG_DECIMAL);

		setField("ID_MISURA_ALTERNATIVA", BIG_DECIMAL);
		setField("COD_TIPO_DECISIONE", STRING);
		setField("COD_NATURA_DECISIONE", STRING);
		setField("COD_TIPO_MISURA", STRING);
		setField("DATA_DECISIONE", DATE);
		setField("COD_MAGISTRATO", STRING);
		setField("COD_UFFICIO_SORVEGLIANZA", STRING);
		setField("CSS_ID_CSSA", BIG_DECIMAL);
		setField("DESCR_LUOGO_PROVA", STRING);
		setField("NUM_ANNI_MISURA", BIG_DECIMAL);
		setField("NUM_MESI_MISURA", BIG_DECIMAL);
		setField("NUM_GIORNI_MISURA", BIG_DECIMAL);
		setField("DATA_INIZIO_MISURA", DATE);
		setField("DATA_FINE_MISURA", DATE);
		setField("CHIAVE_ANNO_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO_FASCICOLO_SIUS", STRING);
		setField("CHIAVE_PROGR_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("ANNO_REGISTRO", BIG_DECIMAL);
		setField("NUMERO_REGISTRO", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("NOTE", STRING);
		setField("DATA_SCARCERAZIONE", DATE);
		setField("DATA_INGRESSO_ISTITUTO", DATE);
		setField("COD_TIPO_UFFICIO_SCARCERAZIONE", STRING);
		setField("FLAG_UFFICIO_INSERIMENTO", STRING);

		setField("DATA_INIZIO_REVOCA", DATE);
		setField("NUM_ANNI_REVOCA_ARRESTO", BIG_DECIMAL);
		setField("NUM_MESI_REVOCA_ARRESTO", BIG_DECIMAL);
		setField("NUM_GIORNI_REVOCA_ARRESTO", BIG_DECIMAL);
		setField("NUM_ANNI_REVOCA_RECLUSIONE", BIG_DECIMAL);
		setField("NUM_MESI_REVOCA_RECLUSIONE", BIG_DECIMAL);
		setField("NUM_GIORNI_REVOCA_RECLUSIONE", BIG_DECIMAL);
		setField("FLAG_PERIODO_ESPIATO", STRING);

		setField("ANNO_ALTRO_TITOLO", BIG_DECIMAL);
		setField("NUM_ALTRO_TITOLO", STRING);
		setField("DATA_ALTRO_TITOLO", DATE);
		setField("COD_LUOGO_ALTRO_TITOLO", STRING);
		setField("COD_AUTORITA_ALTRO_TITOLO", STRING);

		setField("DATA_SCADENZA_PROROGA", DATE);
		setField("FLAG_DECISIONE_TRIBUNALE", STRING);
		setField("COD_TDS_COMPETENTE", STRING);
		setField("FLAG_SITUAZIONE", STRING);

		// DL 146/2013
		setField("COD_TIPO_DECISIONE_MA_AT", STRING);
		setField("COD_TIPO_MISURA_MA_AT", STRING);
		setField("DATA_DECISIONE_MA_AT", DATE);
		setField("CHIAVE_ANNO_FAS_SIUS_MA_AT", BIG_DECIMAL);
		setField("CHIAVE_PROGR_FAS_SIUS_MA_AT", BIG_DECIMAL);
		setField("CHIAVE_UFF_FAS_SIUS_MA_AT", STRING);
		setField("ANNO_REGISTRO_MA_AT", BIG_DECIMAL);
		setField("NUMERO_REGISTRO_MA_AT", BIG_DECIMAL);

		setField("FL_FORMA_MISURA", BIG_DECIMAL);
		setField("DESCRIZIONE_COMUNITA", STRING);

		// MEV_2019-09
		setField("DATA_ESECUTIVITA", DATE);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdMisuraAlternativa() throws DAOException {
		return getBigDecimal("ID_MISURA_ALTERNATIVA");
	}

	public String getCodTipoDecisione() throws DAOException {
		return getString("COD_TIPO_DECISIONE");
	}

	public String getCodNaturaDecisione() throws DAOException {
		return getString("COD_NATURA_DECISIONE");
	}

	public String getCodTipoMisura() throws DAOException {
		return getString("COD_TIPO_MISURA");
	}

	public Date getDataDecisione() throws DAOException {
		return getDate("DATA_DECISIONE");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public String getCodUfficioSorveglianza() throws DAOException {
		return getString("COD_UFFICIO_SORVEGLIANZA");
	}

	public BigDecimal getCssIdCssa() throws DAOException {
		return getBigDecimal("CSS_ID_CSSA");
	}

	public String getDescrLuogoProva() throws DAOException {
		return getString("DESCR_LUOGO_PROVA");
	}

	public BigDecimal getNumAnniMisura() throws DAOException {
		return getBigDecimal("NUM_ANNI_MISURA");
	}

	public BigDecimal getNumMesiMisura() throws DAOException {
		return getBigDecimal("NUM_MESI_MISURA");
	}

	public BigDecimal getNumGiorniMisura() throws DAOException {
		return getBigDecimal("NUM_GIORNI_MISURA");
	}

	public Date getDataInizioMisura() throws DAOException {
		return getDate("DATA_INIZIO_MISURA");
	}

	public Date getDataFineMisura() throws DAOException {
		return getDate("DATA_FINE_MISURA");
	}

	public BigDecimal getChiaveAnnoFascicoloSius() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS");
	}

	public String getChiaveUfficioFascicoloSius() throws DAOException {
		return getString("CHIAVE_UFFICIO_FASCICOLO_SIUS");
	}

	public BigDecimal getChiaveProgrFascicoloSius() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS");
	}

	public BigDecimal getAnnoRegistro() throws DAOException {
		return getBigDecimal("ANNO_REGISTRO");
	}

	public BigDecimal getNumeroRegistro() throws DAOException {
		return getBigDecimal("NUMERO_REGISTRO");
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

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public Date getDataScarcerazione() throws DAOException {
		return getDate("DATA_SCARCERAZIONE");
	}

	public Date getDataIngressoIstituto() throws DAOException {
		return getDate("DATA_INGRESSO_ISTITUTO");
	}

	public String getCodTipoUfficioScarcerazione() throws DAOException {
		return getString("COD_TIPO_UFFICIO_SCARCERAZIONE");
	}

	public String getFlagUfficioInserimento() throws DAOException {
		return getString("FLAG_UFFICIO_INSERIMENTO");
	}

	public Date getDataInizioRevoca() throws DAOException {
		return getDate("DATA_INIZIO_REVOCA");
	}

	public BigDecimal getNumAnniRevocaArresto() throws DAOException {
		return getBigDecimal("NUM_ANNI_REVOCA_ARRESTO");
	}

	public BigDecimal getNumMesiRevocaArresto() throws DAOException {
		return getBigDecimal("NUM_MESI_REVOCA_ARRESTO");
	}

	public BigDecimal getNumGiorniRevocaArresto() throws DAOException {
		return getBigDecimal("NUM_GIORNI_REVOCA_ARRESTO");
	}

	public BigDecimal getNumAnniRevocaReclusione() throws DAOException {
		return getBigDecimal("NUM_ANNI_REVOCA_RECLUSIONE");
	}

	public BigDecimal getNumMesiRevocaReclusione() throws DAOException {
		return getBigDecimal("NUM_MESI_REVOCA_RECLUSIONE");
	}

	public BigDecimal getNumGiorniRevocaReclusione() throws DAOException {
		return getBigDecimal("NUM_GIORNI_REVOCA_RECLUSIONE");
	}

	public String getFlagPeriodoEspiato() throws DAOException {
		return getString("FLAG_PERIODO_ESPIATO");
	}

	public BigDecimal getAnnoAltroTitolo() throws DAOException {
		return getBigDecimal("ANNO_ALTRO_TITOLO");
	}

	public String getNumAltroTitolo() throws DAOException {
		return getString("NUM_ALTRO_TITOLO");
	}

	public Date getDataAltroTitolo() throws DAOException {
		return getDate("DATA_ALTRO_TITOLO");
	}

	public String getCodLuogoAltroTitolo() throws DAOException {
		return getString("COD_LUOGO_ALTRO_TITOLO");
	}

	public String getCodAutoritaAltroTitolo() throws DAOException {
		return getString("COD_AUTORITA_ALTRO_TITOLO");
	}

	public Date getDataScadenzaProroga() throws DAOException {
		return getDate("DATA_SCADENZA_PROROGA");
	}

	public String getFlagDecisioneTribunale() throws DAOException {
		return getString("FLAG_DECISIONE_TRIBUNALE");
	}

	public String getCodTdsCompetente() throws DAOException {
		return getString("COD_TDS_COMPETENTE");
	}

	public String getFlagSituazione() throws DAOException {
		return getString("FLAG_SITUAZIONE");
	}

	public String getCodTipoDecisioneMaAt() throws DAOException {
		return getString("COD_TIPO_DECISIONE_MA_AT");
	}

	public String getCodTipoMisuraMaAt() throws DAOException {
		return getString("COD_TIPO_MISURA_MA_AT");
	}

	public Date getDataDecisioneMaAt() throws DAOException {
		return getDate("DATA_DECISIONE_MA_AT");
	}

	public BigDecimal getChiaveAnnoFascicoloSiusMaAt() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO_FAS_SIUS_MA_AT");
	}

	public BigDecimal getChiaveProgrFascicoloSiusMaAt() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR_FAS_SIUS_MA_AT");
	}

	public String getChiaveUfficioFascicoloSiusMaAt() throws DAOException {
		return getString("CHIAVE_UFF_FAS_SIUS_MA_AT");
	}

	public BigDecimal getAnnoRegistroMaAt() throws DAOException {
		return getBigDecimal("ANNO_REGISTRO_MA_AT");
	}

	public BigDecimal getNumeroRegistroMaAt() throws DAOException {
		return getBigDecimal("NUMERO_REGISTRO_MA_AT");
	}

	public BigDecimal getFlFormaMisura() throws DAOException {
		return getBigDecimal("FL_FORMA_MISURA");
	}

	public String getDescrizioneComunita() throws DAOException {
		return getString("DESCRIZIONE_COMUNITA");
	}

	// MEV_2019-09
	public Date getDataEsecutivita() throws DAOException {
		return getDate("DATA_ESECUTIVITA");
	}

	//
	// METODI SET()
	//
	public void setIdMisuraAlternativa(BigDecimal aValore) {
		setBigDecimal("ID_MISURA_ALTERNATIVA", aValore);
	}

	public void setCodTipoDecisione(String aValore) {
		setString("COD_TIPO_DECISIONE", aValore);
	}

	public void setCodNaturaDecisione(String aValore) {
		setString("COD_NATURA_DECISIONE", aValore);
	}

	public void setCodTipoMisura(String aValore) {
		setString("COD_TIPO_MISURA", aValore);
	}

	public void setDataDecisione(Date aValore) {
		setDate("DATA_DECISIONE", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setCodUfficioSorveglianza(String aValore) {
		setString("COD_UFFICIO_SORVEGLIANZA", aValore);
	}

	public void setCssIdCssa(BigDecimal aValore) {
		setBigDecimal("CSS_ID_CSSA", aValore);
	}

	public void setDescrLuogoProva(String aValore) {
		setString("DESCR_LUOGO_PROVA", aValore);
	}

	public void setNumAnniMisura(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_MISURA", aValore);
	}

	public void setNumMesiMisura(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_MISURA", aValore);
	}

	public void setNumGiorniMisura(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_MISURA", aValore);
	}

	public void setDataInizioMisura(Date aValore) {
		setDate("DATA_INIZIO_MISURA", aValore);
	}

	public void setDataFineMisura(Date aValore) {
		setDate("DATA_FINE_MISURA", aValore);
	}

	public void setChiaveAnnoFascicoloSius(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS", aValore);
	}

	public void setChiaveUfficioFascicoloSius(String aValore) {
		setString("CHIAVE_UFFICIO_FASCICOLO_SIUS", aValore);
	}

	public void setChiaveProgrFascicoloSius(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS", aValore);
	}

	public void setAnnoRegistro(BigDecimal aValore) {
		setBigDecimal("ANNO_REGISTRO", aValore);
	}

	public void setNumeroRegistro(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGISTRO", aValore);
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

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setDataScarcerazione(Date aValore) {
		setDate("DATA_SCARCERAZIONE", aValore);
	}

	public void setDataIngressoIstituto(Date aValore) {
		setDate("DATA_INGRESSO_ISTITUTO", aValore);
	}

	public void setCodTipoUfficioScarcerazione(String aValore) {
		setString("COD_TIPO_UFFICIO_SCARCERAZIONE", aValore);
	}

	public void setFlagUfficioInserimento(String aValore) {
		setString("FLAG_UFFICIO_INSERIMENTO", aValore);
	}

	public void setNumAnniRevocaReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_REVOCA_RECLUSIONE", aValore);
	}

	public void setNumMesiRevocaReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_REVOCA_RECLUSIONE", aValore);
	}

	public void setNumGiorniRevocaReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_REVOCA_RECLUSIONE", aValore);
	}

	public void setNumAnniRevocaArresto(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_REVOCA_ARRESTO", aValore);
	}

	public void setNumMesiRevocaArresto(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_REVOCA_ARRESTO", aValore);
	}

	public void setNumGiorniRevocaArresto(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_REVOCA_ARRESTO", aValore);
	}

	public void setDataInizioRevoca(Date aValore) {
		setDate("DATA_INIZIO_REVOCA", aValore);
	}

	public void setFlagPeriodoEspiato(String aValore) {
		setString("FLAG_PERIODO_ESPIATO", aValore);
	}

	public void setAnnoAltroTitolo(BigDecimal aValore) {
		setBigDecimal("ANNO_ALTRO_TITOLO", aValore);
	}

	public void setNumAltroTitolo(String aValore) {
		setString("NUM_ALTRO_TITOLO", aValore);
	}

	public void setDataAltroTitolo(Date aValore) {
		setDate("DATA_ALTRO_TITOLO", aValore);
	}

	public void setCodLuogoAltroTitolo(String aValore) {
		setString("COD_LUOGO_ALTRO_TITOLO", aValore);
	}

	public void setCodAutoritaAltroTitolo(String aValore) {
		setString("COD_AUTORITA_ALTRO_TITOLO", aValore);
	}

	public void setDataScadenzaProroga(Date aValore) {
		setDate("DATA_SCADENZA_PROROGA", aValore);
	}

	public void setFlagDecisioneTribunale(String aValore) {
		setString("FLAG_DECISIONE_TRIBUNALE", aValore);
	}

	public void setCodTdsCompetente(String aValore) {
		setString("COD_TDS_COMPETENTE", aValore);
	}

	public void setFlagSituazione(String aValore) {
		setString("FLAG_SITUAZIONE", aValore);
	}

	public void setCodTipoDecisioneMaAt(String aValore) {
		setString("COD_TIPO_DECISIONE_MA_AT", aValore);
	}

	public void setCodTipoMisuraMaAt(String aValore) {
		setString("COD_TIPO_MISURA_MA_AT", aValore);
	}

	public void setDataDecisioneMaAt(Date aValore) {
		setDate("DATA_DECISIONE_MA_AT", aValore);
	}

	public void setChiaveAnnoFascicoloSiusMaAt(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO_FAS_SIUS_MA_AT", aValore);
	}

	public void setChiaveProgrFascicoloSiusMaAt(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR_FAS_SIUS_MA_AT", aValore);
	}

	public void setChiaveUfficioFascicoloSiusMaAt(String aValore) {
		setString("CHIAVE_UFF_FAS_SIUS_MA_AT", aValore);
	}

	public void setAnnoRegistroMaAt(BigDecimal aValore) {
		setBigDecimal("ANNO_REGISTRO_MA_AT", aValore);
	}

	public void setNumeroRegistroMaAt(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGISTRO_MA_AT", aValore);
	}

	public void setFlFormaMisura(BigDecimal aValore) {
		setBigDecimal("FL_FORMA_MISURA", aValore);
	}

	public void setDescrizioneComunita(String aValore) {
		setString("DESCRIZIONE_COMUNITA", aValore);
	}

	// MEV_2019-09
	public void setDataEsecutivita(Date aValore) {
		setDate("DATA_ESECUTIVITA", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new MisuraAlternativaModel(getIdMisuraAlternativa(), getCodTipoDecisione(), "",
				getCodNaturaDecisione(), "", getCodTipoMisura(), "", getDataDecisione(), getCodMagistrato(),
				"", getCodUfficioSorveglianza(), "", getCssIdCssa(), getDescrLuogoProva(), getNumAnniMisura(),
				getNumMesiMisura(), getNumGiorniMisura(), getDataInizioMisura(), getDataFineMisura(),
				getChiaveAnnoFascicoloSius(), getChiaveUfficioFascicoloSius(), getChiaveProgrFascicoloSius(),
				getAnnoRegistro(), getNumeroRegistro(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getFasSieIdFascicoloSiep(), getEveIdEvento(), getNote(),
				getDataScarcerazione(), getDataIngressoIstituto(), getCodTipoUfficioScarcerazione(),
				getFlagUfficioInserimento(), getNumAnniRevocaReclusione(), getNumMesiRevocaReclusione(),
				getNumGiorniRevocaReclusione(), getNumAnniRevocaArresto(), getNumMesiRevocaArresto(),
				getNumGiorniRevocaArresto(), getDataInizioRevoca(), getFlagPeriodoEspiato(),
				getAnnoAltroTitolo(), getNumAltroTitolo(), getDataAltroTitolo(), getCodLuogoAltroTitolo(),
				getCodAutoritaAltroTitolo(), getDataScadenzaProroga(), getFlagDecisioneTribunale(),
				getCodTdsCompetente(), getFlagSituazione(), "", "", getCodTipoDecisioneMaAt(), "",
				getCodTipoMisuraMaAt(), "", getDataDecisioneMaAt(), getChiaveAnnoFascicoloSiusMaAt(),
				getChiaveProgrFascicoloSiusMaAt(), getChiaveUfficioFascicoloSiusMaAt(), "",
				getAnnoRegistroMaAt(), getNumeroRegistroMaAt(), getFlFormaMisura(),
				getDescrizioneComunita(),
				// MEV_2019-09
				getDataEsecutivita());
	}

	public void setDAOFromModel(MisuraAlternativaModel aModel) throws DAOException {

		setIdMisuraAlternativa(aModel.getIdMisuraAlternativa());
		setCodTipoDecisione(aModel.getCodTipoDecisione());
		setCodNaturaDecisione(aModel.getCodNaturaDecisione());
		setCodTipoMisura(aModel.getCodTipoMisura());
		setDataDecisione(aModel.getDataDecisione());
		setCodMagistrato(aModel.getCodMagistrato());
		setCodUfficioSorveglianza(aModel.getCodUfficioSorveglianza());
		setCssIdCssa(aModel.getCssIdCssa());
		setDescrLuogoProva(aModel.getDescrLuogoProva());
		setNumAnniMisura(aModel.getNumAnniMisura());
		setNumMesiMisura(aModel.getNumMesiMisura());
		setNumGiorniMisura(aModel.getNumGiorniMisura());
		setDataInizioMisura(aModel.getDataInizioMisura());
		setDataFineMisura(aModel.getDataFineMisura());
		setChiaveAnnoFascicoloSius(aModel.getChiaveAnnoFascicoloSius());
		setChiaveUfficioFascicoloSius(aModel.getChiaveUfficioFascicoloSius());
		setChiaveProgrFascicoloSius(aModel.getChiaveProgrFascicoloSius());
		setAnnoRegistro(aModel.getAnnoRegistro());
		setNumeroRegistro(aModel.getNumeroRegistro());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setEveIdEvento(aModel.getEveIdEvento());
		setNote(aModel.getNote());
		setDataScarcerazione(aModel.getDataScarcerazione());
		setDataIngressoIstituto(aModel.getDataIngressoIstituto());
		setCodTipoUfficioScarcerazione(aModel.getCodTipoUfficioScarcerazione());
		setFlagUfficioInserimento(aModel.getFlagUfficioInserimento());
		setDataInizioRevoca(aModel.getDataInizioRevoca());
		setNumAnniRevocaArresto(aModel.getNumAnniRevocaArresto());
		setNumMesiRevocaArresto(aModel.getNumMesiRevocaArresto());
		setNumGiorniRevocaArresto(aModel.getNumGiorniRevocaArresto());
		setNumAnniRevocaReclusione(aModel.getNumAnniRevocaReclusione());
		setNumMesiRevocaReclusione(aModel.getNumMesiRevocaReclusione());
		setNumGiorniRevocaReclusione(aModel.getNumGiorniRevocaReclusione());
		setFlagPeriodoEspiato(aModel.getFlagPeriodoEspiato());
		setAnnoAltroTitolo(aModel.getAnnoAltroTitolo());
		setNumAltroTitolo(aModel.getNumAltroTitolo());
		setDataAltroTitolo(aModel.getDataAltroTitolo());
		setCodLuogoAltroTitolo(aModel.getCodLuogoAltroTitolo());
		setCodAutoritaAltroTitolo(aModel.getCodAutoritaAltroTitolo());
		setDataScadenzaProroga(aModel.getDataScadenzaProroga());
		setFlagDecisioneTribunale(aModel.getFlagDecisioneTribunale());
		setCodTdsCompetente(aModel.getCodTdsCompetente());
		setFlagSituazione(aModel.getFlagSituazione());
		setCodTipoDecisioneMaAt(aModel.getCodTipoDecisioneMaAt());
		setCodTipoMisuraMaAt(aModel.getCodTipoMisuraMaAt());
		setDataDecisioneMaAt(aModel.getDataDecisioneMaAt());
		setChiaveAnnoFascicoloSiusMaAt(aModel.getChiaveAnnoFascicoloSiusMaAt());
		setChiaveProgrFascicoloSiusMaAt(aModel.getChiaveProgrFascicoloSiusMaAt());
		setChiaveUfficioFascicoloSiusMaAt(aModel.getChiaveUfficioFascicoloSiusMaAt());
		setAnnoRegistroMaAt(aModel.getAnnoRegistroMaAt());
		setNumeroRegistroMaAt(aModel.getNumeroRegistroMaAt());
		setFlFormaMisura(aModel.getFlFormaMisura());
		setDescrizioneComunita(aModel.getDescrizioneComunita());
		// MEV_2019-09
		setDataEsecutivita(aModel.getDataEsecutivita());
	}

	public void setDAOFromModelForUpdate(MisuraAlternativaModel aModel) throws DAOException {

		setIdMisuraAlternativa(aModel.getIdMisuraAlternativa());
		setCodTipoDecisione(aModel.getCodTipoDecisione());
		setCodNaturaDecisione(aModel.getCodNaturaDecisione());
		setCodTipoMisura(aModel.getCodTipoMisura());
		setDataDecisione(aModel.getDataDecisione());
		setCodMagistrato(aModel.getCodMagistrato());
		setCodUfficioSorveglianza(aModel.getCodUfficioSorveglianza());
		setCssIdCssa(aModel.getCssIdCssa());
		setDescrLuogoProva(aModel.getDescrLuogoProva());
		setNumAnniMisura(aModel.getNumAnniMisura());
		setNumMesiMisura(aModel.getNumMesiMisura());
		setNumGiorniMisura(aModel.getNumGiorniMisura());
		setDataInizioMisura(aModel.getDataInizioMisura());
		setDataFineMisura(aModel.getDataFineMisura());
		setChiaveAnnoFascicoloSius(aModel.getChiaveAnnoFascicoloSius());
		setChiaveUfficioFascicoloSius(aModel.getChiaveUfficioFascicoloSius());
		setChiaveProgrFascicoloSius(aModel.getChiaveProgrFascicoloSius());
		setAnnoRegistro(aModel.getAnnoRegistro());
		setNumeroRegistro(aModel.getNumeroRegistro());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setEveIdEvento(aModel.getEveIdEvento());
		setNote(aModel.getNote());
		setDataScarcerazione(aModel.getDataScarcerazione());
		setDataIngressoIstituto(aModel.getDataIngressoIstituto());
		setCodTipoUfficioScarcerazione(aModel.getCodTipoUfficioScarcerazione());
		setFlagUfficioInserimento(aModel.getFlagUfficioInserimento());
		setDataInizioRevoca(aModel.getDataInizioRevoca());
		setNumAnniRevocaArresto(aModel.getNumAnniRevocaArresto());
		setNumMesiRevocaArresto(aModel.getNumMesiRevocaArresto());
		setNumGiorniRevocaArresto(aModel.getNumGiorniRevocaArresto());
		setNumAnniRevocaReclusione(aModel.getNumAnniRevocaReclusione());
		setNumMesiRevocaReclusione(aModel.getNumMesiRevocaReclusione());
		setNumGiorniRevocaReclusione(aModel.getNumGiorniRevocaReclusione());
		setFlagPeriodoEspiato(aModel.getFlagPeriodoEspiato());
		setAnnoAltroTitolo(aModel.getAnnoAltroTitolo());
		setNumAltroTitolo(aModel.getNumAltroTitolo());
		setDataAltroTitolo(aModel.getDataAltroTitolo());
		setCodLuogoAltroTitolo(aModel.getCodLuogoAltroTitolo());
		setCodAutoritaAltroTitolo(aModel.getCodAutoritaAltroTitolo());
		setDataScadenzaProroga(aModel.getDataScadenzaProroga());
		setFlagDecisioneTribunale(aModel.getFlagDecisioneTribunale());
		setCodTdsCompetente(aModel.getCodTdsCompetente());
		setFlagSituazione(aModel.getFlagSituazione());
		setCondizioneUpdate(aModel.getIdMisuraAlternativa());
		setCodTipoDecisioneMaAt(aModel.getCodTipoDecisioneMaAt());
		setCodTipoMisuraMaAt(aModel.getCodTipoMisuraMaAt());
		setDataDecisioneMaAt(aModel.getDataDecisioneMaAt());
		setChiaveAnnoFascicoloSiusMaAt(aModel.getChiaveAnnoFascicoloSiusMaAt());
		setChiaveProgrFascicoloSiusMaAt(aModel.getChiaveProgrFascicoloSiusMaAt());
		setChiaveUfficioFascicoloSiusMaAt(aModel.getChiaveUfficioFascicoloSiusMaAt());
		setAnnoRegistroMaAt(aModel.getAnnoRegistroMaAt());
		setNumeroRegistroMaAt(aModel.getNumeroRegistroMaAt());
		// MEV_2019-09
		setDataEsecutivita(aModel.getDataEsecutivita());
	}

	public void setCondizione(MisuraAlternativaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_MISURA_ALTERNATIVA = " + key);
	}

	public void setCondizioneByIdEvento(BigDecimal key) {
		setCondition(" EVE_ID_EVENTO = " + key);
	}

	public void setCondizioneIdFascicolo(BigDecimal aIdFascicoloSiep) {
		setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep);
	}

	/**
	 * La funzione controlla se esiste almeno un record in tabelle MISURA_ALTERNATIVA legata all'Evento
	 * specificato dal suo ID.
	 *
	 * @param aIdEvento
	 * @return
	 * @throws DAOException
	 */

	public boolean esisteMisuraAlternativaPerEvento(BigDecimal aIdEvento) throws DAOException {

		boolean lRet = false;
		setCondizioneByIdEvento(aIdEvento);
		start();
		if (next())
			lRet = true;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Esiste Misura Alternativa per evento -> " + aIdEvento + " ? " + lRet);

		return lRet;
	}

}