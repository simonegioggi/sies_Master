package siap.siep.annotazionemanuale.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AnnotazioneManualeDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella AnnotazioneManuale
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
public class AnnotazioneManualeDAO extends TableDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public AnnotazioneManualeDAO(Connection con) {
		super(con);
		setTable("ANNOTAZIONE_MANUALE");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_ANNOTAZIONE_MANUALE", "ANN_MAN_SEQ");
		setField("ID_ANNOTAZIONE_MANUALE", BIG_DECIMAL);
		setField("COD_TIPO_ANNOTAZIONE", STRING);
		setField("FLAG_PIU_MENO", STRING);
		setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
		setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
		setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
		setField("IMPORTO_MULTA", BIG_DECIMAL);
		setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
		setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
		setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
		setField("IMPORTO_AMMENDA", BIG_DECIMAL);
		setField("DATA_RECLUSIONE_DA", DATE);
		setField("DATA_RECLUSIONE_A", DATE);
		setField("DATA_ARRESTO_DA", DATE);
		setField("DATA_ARRESTO_A", DATE);
		setField("DATA_RICEZIONE_DOC", DATE);
		setField("MOTIVAZIONI", STRING);
		setField("NOTE_RECLUSIONE", STRING);
		setField("ANNO_GE", BIG_DECIMAL);
		setField("NUMERO_GE", STRING);
		setField("ANNO_REGE", BIG_DECIMAL);
		setField("NUMERO_REGE", STRING);
		setField("ANNO_MC", BIG_DECIMAL);
		setField("NUMERO_MC", STRING);
		setField("ANNO_CDA", BIG_DECIMAL);
		setField("NUMERO_CDA", STRING);
		setField("ANNO_CC", BIG_DECIMAL);
		setField("NUMERO_CC", STRING);
		setField("ANNO_SIEP", BIG_DECIMAL);
		setField("NUMERO_SIEP", STRING);
		setField("COD_TIPO_UFFICIO_SIEP", STRING);
		setField("COD_LUOGO_UFFICIO_SIEP", STRING);
		setField("DATA_ISCRIZIONE_SIEP", DATE);
		setField("COD_FONTE", STRING);
		setField("ANNO_FONTE", BIG_DECIMAL);
		setField("NUMERO_FONTE", STRING);
		setField("COD_SOTTONUMERAZIONE", STRING);
		setField("COMMA", STRING);
		setField("LETTERA", STRING);
		setField("NUMERO", STRING);
		setField("ARTICOLO", STRING);
		setField("COD_CAUSALE_COMPUTO", STRING);
		setField("COD_DPR", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("REA_ID_REATO", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);

		setField("FLAG_VALIDATO", STRING);
		setField("FLAG_CONFORME", STRING);
		setField("FLAG_APP_PROVVISORIA", STRING);
		setField("PEN_RES_ID_PENA_RESIDUA", BIG_DECIMAL);
		setField("FUN_ID_FUNGIBILITA", BIG_DECIMAL);
		setField("DATA_RICHIESTA", DATE);
		setField("DATA_CC", DATE);
		setField("DATA_GE", DATE);
		setField("ANNO_SENTENZA_SIAP", BIG_DECIMAL);
		setField("NUMERO_SENTENZA_SIAP", STRING);
		setField("DATA_SENTENZA_SIAP", DATE);
		setField("ANNO_ID_ANNOTAZIONE_MANUALE", BIG_DECIMAL);

		setField("FLAG_COMPUTABILE", STRING);
		setField("FLG_BENEFICIO_DETRATTO", STRING);
		setField("SEN_ID_SENTENZA", BIG_DECIMAL);
		setField("TEN_ID_TENORE_SIGE", BIG_DECIMAL);

		setField("FLAG_SEL_QUANTUM", STRING);

		// 07/2015 - MEV 29 punto 11 - Anno e Numero Procedimento SIGE
		setField("ANNO_SIGE", BIG_DECIMAL);
		setField("NUMERO_SIGE", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAnnotazioneManuale() throws DAOException {
		return getBigDecimal("ID_ANNOTAZIONE_MANUALE");
	}

	public String getCodTipoAnnotazione() throws DAOException {
		return getString("COD_TIPO_ANNOTAZIONE");
	}

	public String getFlagPiuMeno() throws DAOException {
		return getString("FLAG_PIU_MENO");
	}

	public BigDecimal getNumAnniReclusione() throws DAOException {
		return getBigDecimal("NUM_ANNI_RECLUSIONE");
	}

	public BigDecimal getNumMesiReclusione() throws DAOException {
		return getBigDecimal("NUM_MESI_RECLUSIONE");
	}

	public BigDecimal getNumGiorniReclusione() throws DAOException {
		return getBigDecimal("NUM_GIORNI_RECLUSIONE");
	}

	public BigDecimal getImportoMulta() throws DAOException {
		return getBigDecimal("IMPORTO_MULTA");
	}

	public BigDecimal getNumAnniArresto() throws DAOException {
		return getBigDecimal("NUM_ANNI_ARRESTO");
	}

	public BigDecimal getNumMesiArresto() throws DAOException {
		return getBigDecimal("NUM_MESI_ARRESTO");
	}

	public BigDecimal getNumGiorniArresto() throws DAOException {
		return getBigDecimal("NUM_GIORNI_ARRESTO");
	}

	public BigDecimal getImportoAmmenda() throws DAOException {
		return getBigDecimal("IMPORTO_AMMENDA");
	}

	public Date getDataArrestoDa() throws DAOException {
		return getDate("DATA_ARRESTO_DA");
	}

	public Date getDataArrestoA() throws DAOException {
		return getDate("DATA_ARRESTO_A");
	}

	public Date getDataReclusioneDa() throws DAOException {
		return getDate("DATA_RECLUSIONE_DA");
	}

	public Date getDataReclusioneA() throws DAOException {
		return getDate("DATA_RECLUSIONE_A");
	}

	public Date getDataRicezioneDoc() throws DAOException {
		return getDate("DATA_RICEZIONE_DOC");
	}

	public String getMotivazioni() throws DAOException {
		return getString("MOTIVAZIONI");
	}

	public String getNoteReclusione() throws DAOException {
		return getString("NOTE_RECLUSIONE");
	}

	public BigDecimal getAnnoGe() throws DAOException {
		return getBigDecimal("ANNO_GE");
	}

	public String getNumeroGe() throws DAOException {
		return getString("NUMERO_GE");
	}

	public BigDecimal getAnnoRege() throws DAOException {
		return getBigDecimal("ANNO_REGE");
	}

	public String getNumeroRege() throws DAOException {
		return getString("NUMERO_REGE");
	}

	public BigDecimal getAnnoMc() throws DAOException {
		return getBigDecimal("ANNO_MC");
	}

	public String getNumeroMc() throws DAOException {
		return getString("NUMERO_MC");
	}

	public BigDecimal getAnnoCda() throws DAOException {
		return getBigDecimal("ANNO_CDA");
	}

	public String getNumeroCda() throws DAOException {
		return getString("NUMERO_CDA");
	}

	public BigDecimal getAnnoCc() throws DAOException {
		return getBigDecimal("ANNO_CC");
	}

	public String getNumeroCc() throws DAOException {
		return getString("NUMERO_CC");
	}

	public BigDecimal getAnnoSiep() throws DAOException {
		return getBigDecimal("ANNO_SIEP");
	}

	public String getNumeroSiep() throws DAOException {
		return getString("NUMERO_SIEP");
	}

	public String getCodTipoUfficioSiep() throws DAOException {
		return getString("COD_TIPO_UFFICIO_SIEP");
	}

	public String getCodLuogoUfficioSiep() throws DAOException {
		return getString("COD_LUOGO_UFFICIO_SIEP");
	}

	public Date getDataIscrizioneSiep() throws DAOException {
		return getDate("DATA_ISCRIZIONE_SIEP");
	}

	public String getCodFonte() throws DAOException {
		return getString("COD_FONTE");
	}

	public BigDecimal getAnnoFonte() throws DAOException {
		return getBigDecimal("ANNO_FONTE");
	}

	public String getNumeroFonte() throws DAOException {
		return getString("NUMERO_FONTE");
	}

	public String getCodSottonumerazione() throws DAOException {
		return getString("COD_SOTTONUMERAZIONE");
	}

	public String getComma() throws DAOException {
		return getString("COMMA");
	}

	public String getLettera() throws DAOException {
		return getString("LETTERA");
	}

	public String getNumero() throws DAOException {
		return getString("NUMERO");
	}

	public String getArticolo() throws DAOException {
		return getString("ARTICOLO");
	}

	public String getCodCausaleComputo() throws DAOException {
		return getString("COD_CAUSALE_COMPUTO");
	}

	public String getCodDpr() throws DAOException {
		return getString("COD_DPR");
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

	public BigDecimal getReaIdReato() throws DAOException {
		return getBigDecimal("REA_ID_REATO");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public String getFlagValidato() throws DAOException {
		return getString("FLAG_VALIDATO");
	}

	public String getFlagConforme() throws DAOException {
		return getString("FLAG_CONFORME");
	}

	public String getFlagAppProvvisoria() throws DAOException {
		return getString("FLAG_APP_PROVVISORIA");
	}

	public BigDecimal getPenResIdPenaResidua() throws DAOException {
		return getBigDecimal("PEN_RES_ID_PENA_RESIDUA");
	}

	public BigDecimal getFunIdFungibilita() throws DAOException {
		return getBigDecimal("FUN_ID_FUNGIBILITA");
	}

	public Date getDataRichiesta() throws DAOException {
		return getDate("DATA_RICHIESTA");
	}

	public Date getDataCC() throws DAOException {
		return getDate("DATA_CC");
	}

	public Date getDataGE() throws DAOException {
		return getDate("DATA_GE");
	}

	public BigDecimal getAnnoSentenzaSiap() throws DAOException {
		return getBigDecimal("ANNO_SENTENZA_SIAP");
	}

	public String getNumeroSentenzaSiap() throws DAOException {
		return getString("NUMERO_SENTENZA_SIAP");
	}

	public Date getDataSentenzaSiap() throws DAOException {
		return getDate("DATA_SENTENZA_SIAP");
	}

	public BigDecimal getAnnoIdAnnotazioneManuale() throws DAOException {
		return getBigDecimal("ANNO_ID_ANNOTAZIONE_MANUALE");
	}

	public String getFlagComputabile() throws DAOException {
		return getString("FLAG_COMPUTABILE");
	}

	public String getFlagBeneficioDetratto() throws DAOException {
		return getString("FLG_BENEFICIO_DETRATTO");
	}

	public BigDecimal getSenIdSentenza() throws DAOException {
		return getBigDecimal("SEN_ID_SENTENZA");
	}

	public BigDecimal getTenIdTenoreSige() throws DAOException {
		return getBigDecimal("TEN_ID_TENORE_SIGE");
	}

	public String getFlagSelQuantum() throws DAOException {
		return getString("FLAG_SEL_QUANTUM");
	}

	public BigDecimal getAnnoSige() throws DAOException {
		return getBigDecimal("ANNO_SIGE");
	}

	public BigDecimal getNumeroSige() throws DAOException {
		return getBigDecimal("NUMERO_SIGE");
	}

	//
	// METODI SET()
	//
	public void setIdAnnotazioneManuale(BigDecimal aValore) {
		setBigDecimal("ID_ANNOTAZIONE_MANUALE", aValore);
	}

	public void setCodTipoAnnotazione(String aValore) {
		setString("COD_TIPO_ANNOTAZIONE", aValore);
	}

	public void setFlagPiuMeno(String aValore) {
		setString("FLAG_PIU_MENO", aValore);
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_RECLUSIONE", aValore);
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_RECLUSIONE", aValore);
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore);
	}

	public void setImportoMulta(BigDecimal aValore) {
		setBigDecimal("IMPORTO_MULTA", aValore);
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_ARRESTO", aValore);
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_ARRESTO", aValore);
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_ARRESTO", aValore);
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		setBigDecimal("IMPORTO_AMMENDA", aValore);
	}

	public void setDataArrestoDa(Date aValore) {
		setDate("DATA_ARRESTO_DA", aValore);
	}

	public void setDataArrestoA(Date aValore) {
		setDate("DATA_ARRESTO_A", aValore);
	}

	public void setDataReclusioneDa(Date aValore) {
		setDate("DATA_RECLUSIONE_DA", aValore);
	}

	public void setDataReclusioneA(Date aValore) {
		setDate("DATA_RECLUSIONE_A", aValore);
	}

	public void setDataRicezioneDoc(Date aValore) {
		setDate("DATA_RICEZIONE_DOC", aValore);
	}

	public void setMotivazioni(String aValore) {
		setString("MOTIVAZIONI", aValore);
	}

	public void setNoteReclusione(String aValore) {
		setString("NOTE_RECLUSIONE", aValore);
	}

	public void setAnnoGe(BigDecimal aValore) {
		setBigDecimal("ANNO_GE", aValore);
	}

	public void setNumeroGe(String aValore) {
		setString("NUMERO_GE", aValore);
	}

	public void setAnnoRege(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE", aValore);
	}

	public void setNumeroRege(String aValore) {
		setString("NUMERO_REGE", aValore);
	}

	public void setAnnoMc(BigDecimal aValore) {
		setBigDecimal("ANNO_MC", aValore);
	}

	public void setNumeroMc(String aValore) {
		setString("NUMERO_MC", aValore);
	}

	public void setAnnoCda(BigDecimal aValore) {
		setBigDecimal("ANNO_CDA", aValore);
	}

	public void setNumeroCda(String aValore) {
		setString("NUMERO_CDA", aValore);
	}

	public void setAnnoCc(BigDecimal aValore) {
		setBigDecimal("ANNO_CC", aValore);
	}

	public void setNumeroCc(String aValore) {
		setString("NUMERO_CC", aValore);
	}

	public void setAnnoSiep(BigDecimal aValore) {
		setBigDecimal("ANNO_SIEP", aValore);
	}

	public void setNumeroSiep(String aValore) {
		setString("NUMERO_SIEP", aValore);
	}

	public void setCodTipoUfficioSiep(String aValore) {
		setString("COD_TIPO_UFFICIO_SIEP", aValore);
	}

	public void setCodLuogoUfficioSiep(String aValore) {
		setString("COD_LUOGO_UFFICIO_SIEP", aValore);
	}

	public void setDataIscrizioneSiep(Date aValore) {
		setDate("DATA_ISCRIZIONE_SIEP", aValore);
	}

	public void setCodFonte(String aValore) {
		setString("COD_FONTE", aValore);
	}

	public void setAnnoFonte(BigDecimal aValore) {
		setBigDecimal("ANNO_FONTE", aValore);
	}

	public void setNumeroFonte(String aValore) {
		setString("NUMERO_FONTE", aValore);
	}

	public void setCodSottonumerazione(String aValore) {
		setString("COD_SOTTONUMERAZIONE", aValore);
	}

	public void setComma(String aValore) {
		setString("COMMA", aValore);
	}

	public void setLettera(String aValore) {
		setString("LETTERA", aValore);
	}

	public void setNumero(String aValore) {
		setString("NUMERO", aValore);
	}

	public void setArticolo(String aValore) {
		setString("ARTICOLO", aValore);
	}

	public void setCodCausaleComputo(String aValore) {
		setString("COD_CAUSALE_COMPUTO", aValore);
	}

	public void setCodDpr(String aValore) {
		setString("COD_DPR", aValore);
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

	public void setReaIdReato(BigDecimal aValore) {
		setBigDecimal("REA_ID_REATO", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setFlagValidato(String aValore) {
		setString("FLAG_VALIDATO", aValore);
	}

	public void setFlagConforme(String aValore) {
		setString("FLAG_CONFORME", aValore);
	}

	public void setFlagAppProvvisoria(String aValore) {
		setString("FLAG_APP_PROVVISORIA", aValore);
	}

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		setBigDecimal("PEN_RES_ID_PENA_RESIDUA", aValore);
	}

	public void setFunIdFungibilita(BigDecimal aValore) {
		setBigDecimal("FUN_ID_FUNGIBILITA", aValore);
	}

	public void setDataRichiesta(Date aValore) {
		setDate("DATA_RICHIESTA", aValore);
	}

	public void setDataCC(Date aValore) {
		setDate("DATA_CC", aValore);
	}

	public void setDataGE(Date aValore) {
		setDate("DATA_GE", aValore);
	}

	public void setAnnoSentenzaSiap(BigDecimal aValore) {
		setBigDecimal("ANNO_SENTENZA_SIAP", aValore);
	}

	public void setNumeroSentenzaSiap(String aValore) {
		setString("NUMERO_SENTENZA_SIAP", aValore);
	}

	public void setDataSentenzaSiap(Date aValore) {
		setDate("DATA_SENTENZA_SIAP", aValore);
	}

	public void setAnnoIdAnnotazioneManuale(BigDecimal aValore) {
		setBigDecimal("ANNO_ID_ANNOTAZIONE_MANUALE", aValore);
	}

	public void setFlagComputabile(String aValore) {
		setString("FLAG_COMPUTABILE", aValore);
	}

	public void setFlagBeneficioDetratto(String aValore) {
		setString("FLG_BENEFICIO_DETRATTO", aValore);
	}

	public void setSenIdSentenza(BigDecimal aSenIdSentenza) {
		setBigDecimal("SEN_ID_SENTENZA", aSenIdSentenza);
	}

	public void setTenIdTenoreSige(BigDecimal aTenIdTenoreSige) {
		setBigDecimal("TEN_ID_TENORE_SIGE", aTenIdTenoreSige);
	}

	public void setFlagSelQuantum(String aFlagSelQuantum) {
		super.setString("FLAG_SEL_QUANTUM", aFlagSelQuantum);
	}

	public void setAnnoSige(BigDecimal aValore) {
		setBigDecimal("ANNO_SIGE", aValore);
	}

	public void setNumeroSige(BigDecimal aValore) {
		setBigDecimal("NUMERO_SIGE", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new AnnotazioneManualeModel(getIdAnnotazioneManuale(), getCodTipoAnnotazione(), "",
				getFlagPiuMeno(), getNumAnniReclusione(), getNumMesiReclusione(), getNumGiorniReclusione(),
				getImportoMulta(), getNumAnniArresto(), getNumMesiArresto(), getNumGiorniArresto(),
				getImportoAmmenda(), getDataArrestoDa(), getDataReclusioneA(), getDataReclusioneDa(),
				getDataArrestoA(), getDataRicezioneDoc(), getMotivazioni(), getNoteReclusione(), getAnnoGe(),
				getNumeroGe(), getAnnoRege(), getNumeroRege(), getAnnoMc(), getNumeroMc(), getAnnoCda(),
				getNumeroCda(), getAnnoCc(), getNumeroCc(), getAnnoSiep(), getNumeroSiep(),
				getCodTipoUfficioSiep(), "", getCodLuogoUfficioSiep(), "", getDataIscrizioneSiep(),
				getCodFonte(), "", getAnnoFonte(), getNumeroFonte(), getCodSottonumerazione(), "",
				getComma(), getLettera(), getNumero(), getArticolo(), getCodCausaleComputo(), "",
				getCodDpr(), "", getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getFasSieIdFascicoloSiep(), getReaIdReato(),
				getEveIdEvento(), getFlagValidato(), getFlagConforme(), getFlagAppProvvisoria(),
				getPenResIdPenaResidua(), getFunIdFungibilita(), getDataRichiesta(), getDataCC(),
				getDataGE(), getAnnoSentenzaSiap(), getNumeroSentenzaSiap(), getDataSentenzaSiap(),
				getAnnoIdAnnotazioneManuale(), getFlagComputabile(), getFlagBeneficioDetratto(), getSenIdSentenza(),
				getTenIdTenoreSige(), getFlagSelQuantum(), getAnnoSige(), getNumeroSige());
	}

	public void setDAOFromModel(AnnotazioneManualeModel aModel) throws DAOException {
		setIdAnnotazioneManuale(aModel.getIdAnnotazioneManuale());
		setCodTipoAnnotazione(aModel.getCodTipoAnnotazione());
		setFlagPiuMeno(aModel.getFlagPiuMeno());
		setNumAnniReclusione(aModel.getNumAnniReclusione());
		setNumMesiReclusione(aModel.getNumMesiReclusione());
		setNumGiorniReclusione(aModel.getNumGiorniReclusione());
		setImportoMulta(aModel.getImportoMulta());
		setNumAnniArresto(aModel.getNumAnniArresto());
		setNumMesiArresto(aModel.getNumMesiArresto());
		setNumGiorniArresto(aModel.getNumGiorniArresto());
		setImportoAmmenda(aModel.getImportoAmmenda());
		setDataArrestoDa(aModel.getDataArrestoDa());
		setDataArrestoA(aModel.getDataArrestoA());
		setDataReclusioneDa(aModel.getDataReclusioneDa());
		setDataReclusioneA(aModel.getDataReclusioneA());
		setDataRicezioneDoc(aModel.getDataRicezioneDoc());
		setMotivazioni(aModel.getMotivazioni());
		setNoteReclusione(aModel.getNoteReclusione());
		setAnnoGe(aModel.getAnnoGe()); // Anno Ordinanza SIGE
		setNumeroGe(aModel.getNumeroGe()); // Numero Ordinanza SIGE
		setAnnoRege(aModel.getAnnoRege());
		setNumeroRege(aModel.getNumeroRege());
		setAnnoMc(aModel.getAnnoMc());
		setNumeroMc(aModel.getNumeroMc());
		setAnnoCda(aModel.getAnnoCda());
		setNumeroCda(aModel.getNumeroCda());
		setAnnoCc(aModel.getAnnoCc());
		setNumeroCc(aModel.getNumeroCc());
		setAnnoSiep(aModel.getAnnoSiep());
		setNumeroSiep(aModel.getNumeroSiep());
		setCodTipoUfficioSiep(aModel.getCodTipoUfficioSiep());
		setCodLuogoUfficioSiep(aModel.getCodLuogoUfficioSiep());
		setDataIscrizioneSiep(aModel.getDataIscrizioneSiep());
		setCodFonte(aModel.getCodFonte());
		setAnnoFonte(aModel.getAnnoFonte());
		setNumeroFonte(aModel.getNumeroFonte());
		setCodSottonumerazione(aModel.getCodSottonumerazione());
		setComma(aModel.getComma());
		setLettera(aModel.getLettera());
		setNumero(aModel.getNumero());
		setArticolo(aModel.getArticolo());
		setCodCausaleComputo(aModel.getCodCausaleComputo());
		setCodDpr(aModel.getCodDpr());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setReaIdReato(aModel.getReaIdReato());
		setEveIdEvento(aModel.getEveIdEvento());
		setFlagValidato(aModel.getFlagValidato());
		setFlagConforme(aModel.getFlagConforme());
		setFlagAppProvvisoria(aModel.getFlagAppProvvisoria());
		setPenResIdPenaResidua(aModel.getPenResIdPenaResidua());
		setFunIdFungibilita(aModel.getFunIdFungibilita());
		setDataRichiesta(aModel.getDataRichiesta());
		setDataCC(aModel.getDataCC());
		setDataGE(aModel.getDataGE());
		setAnnoSentenzaSiap(aModel.getAnnoSentenzaSiap());
		setNumeroSentenzaSiap(aModel.getNumeroSentenzaSiap());
		setDataSentenzaSiap(aModel.getDataSentenzaSiap());
		setAnnoIdAnnotazioneManuale(aModel.getAnnoIdAnnotazioneManuale());
		setFlagComputabile(aModel.getFlagComputabile());
		setFlagBeneficioDetratto(aModel.getFlagBeneficioDetratto());
		setSenIdSentenza(aModel.getSenIdSentenza());
		this.setTenIdTenoreSige(aModel.getTenIdTenoreSige());
		this.setFlagSelQuantum(aModel.getFlagSelQuantum());

		// 07-2015 - MEV29 punto 11 - Anno e Numero Procedimento SIGE
		setAnnoSige(aModel.getChiaveAnnoSige());
		setNumeroSige(aModel.getChiaveNumeroSige());
	}

	public void setDAOFromModelForUpdate(AnnotazioneManualeModel aModel) throws DAOException {
		// setIdAnnotazioneManuale( aModel.getIdAnnotazioneManuale() );
		setCodTipoAnnotazione(aModel.getCodTipoAnnotazione());
		setFlagPiuMeno(aModel.getFlagPiuMeno());
		setNumAnniReclusione(aModel.getNumAnniReclusione());
		setNumMesiReclusione(aModel.getNumMesiReclusione());
		setNumGiorniReclusione(aModel.getNumGiorniReclusione());
		setImportoMulta(aModel.getImportoMulta());
		setNumAnniArresto(aModel.getNumAnniArresto());
		setNumMesiArresto(aModel.getNumMesiArresto());
		setNumGiorniArresto(aModel.getNumGiorniArresto());
		setImportoAmmenda(aModel.getImportoAmmenda());
		setDataArrestoDa(aModel.getDataArrestoDa());
		setDataArrestoA(aModel.getDataArrestoA());
		setDataReclusioneDa(aModel.getDataReclusioneDa());
		setDataReclusioneA(aModel.getDataReclusioneA());
		setDataRicezioneDoc(aModel.getDataRicezioneDoc());
		setMotivazioni(aModel.getMotivazioni());
		setNoteReclusione(aModel.getNoteReclusione());
		setAnnoGe(aModel.getAnnoGe());
		setNumeroGe(aModel.getNumeroGe());
		setAnnoRege(aModel.getAnnoRege());
		setNumeroRege(aModel.getNumeroRege());
		setAnnoMc(aModel.getAnnoMc());
		setNumeroMc(aModel.getNumeroMc());
		setAnnoCda(aModel.getAnnoCda());
		setNumeroCda(aModel.getNumeroCda());
		setAnnoCc(aModel.getAnnoCc());
		setNumeroCc(aModel.getNumeroCc());
		setAnnoSiep(aModel.getAnnoSiep());
		setNumeroSiep(aModel.getNumeroSiep());
		setCodTipoUfficioSiep(aModel.getCodTipoUfficioSiep());
		setCodLuogoUfficioSiep(aModel.getCodLuogoUfficioSiep());
		setDataIscrizioneSiep(aModel.getDataIscrizioneSiep());
		setCodFonte(aModel.getCodFonte());
		setAnnoFonte(aModel.getAnnoFonte());
		setNumeroFonte(aModel.getNumeroFonte());
		setCodSottonumerazione(aModel.getCodSottonumerazione());
		setComma(aModel.getComma());
		setLettera(aModel.getLettera());
		setNumero(aModel.getNumero());
		setArticolo(aModel.getArticolo());
		setCodCausaleComputo(aModel.getCodCausaleComputo());
		setCodDpr(aModel.getCodDpr());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setReaIdReato(aModel.getReaIdReato());
		setEveIdEvento(aModel.getEveIdEvento());
		setFlagValidato(aModel.getFlagValidato());
		setFlagConforme(aModel.getFlagConforme());
		setFlagAppProvvisoria(aModel.getFlagAppProvvisoria());
		setPenResIdPenaResidua(aModel.getPenResIdPenaResidua());
		setFunIdFungibilita(aModel.getFunIdFungibilita());
		setDataRichiesta(aModel.getDataRichiesta());
		setDataCC(aModel.getDataCC());
		setDataGE(aModel.getDataGE());
		setAnnoSentenzaSiap(aModel.getAnnoSentenzaSiap());
		setNumeroSentenzaSiap(aModel.getNumeroSentenzaSiap());
		setDataSentenzaSiap(aModel.getDataSentenzaSiap());

		setCondizioneUpdate(aModel.getIdAnnotazioneManuale());
		setAnnoIdAnnotazioneManuale(aModel.getAnnoIdAnnotazioneManuale());
		setFlagComputabile(aModel.getFlagComputabile());
		setFlagBeneficioDetratto(aModel.getFlagBeneficioDetratto());
		setSenIdSentenza(aModel.getSenIdSentenza());
		this.setTenIdTenoreSige(aModel.getTenIdTenoreSige());
		this.setFlagSelQuantum(aModel.getFlagSelQuantum());

		// 07-2015 - MEV29 punto 11 - Anno e Numero Procedimento SIGE
		setAnnoSige(aModel.getChiaveAnnoSige());
		setNumeroSige(aModel.getChiaveNumeroSige());
	}

	public void setCondizione(AnnotazioneManualeModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_ANNOTAZIONE_MANUALE = " + key);
	}

	/**
	 * Imposta la condizione di update, selezionando le richieste che puntano la decisione passata in input.
	 * 
	 * @param aIdDecisione
	 */
	public void setCondizioneLinkDecisione(BigDecimal aIdDecisione) {
		setCondition(" ANNO_ID_ANNOTAZIONE_MANUALE = " + aIdDecisione);
	}

	/*
	 * public void setCondizioneConSenzaRichiestaByIdFascicolo(BigDecimal aKey) { String lSql = "";
	 * 
	 * lSql += " FAS_SIE_ID_FASCICOLO_SIEP="+aKey; lSql +=
	 * " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )"; //AMNISTIA-INDULTO lSql +=
	 * " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A' )"; lSql += " AND FLAG_VALIDATO='N'";
	 * 
	 * setCondition(lSql); }
	 */

	/**
	 * Imposta la condizione di update per le annotazioni con richiesta (A/R) non validate del tipo di quelle
	 * passate in input
	 * 
	 * @param aIdFascicolo
	 * @param aTipoAnnotazione
	 */
	public void setCondizioneConSenzaRichiestaByIdFascicoloTipoAnnotazione(BigDecimal aIdFascicolo,
			String aTipoAnnotazione) {
		String lSql = "";

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;

		if ("002".equals(aTipoAnnotazione) || "003".equals(aTipoAnnotazione)) {
			lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )"; // AMNISTIA-INDULTO
		} else {
			lSql += " AND COD_TIPO_ANNOTAZIONE='" + aTipoAnnotazione + "'"; // ALTRE ANNOTAZIONI
		}

		lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A' )";
		lSql += " AND FLAG_VALIDATO='N'";

		setCondition(lSql);
	}

	/**
	 * Imposta la condizione di where per l'update delle annotazioni (non richieste) non ancora validate del
	 * tipo passato in input
	 * 
	 * @param aIdFascicolo
	 * @param aCodTipo
	 */
	public void setCondizioneCodTipoNonValidataByIdFascicolo(BigDecimal aIdFascicolo, String aCodTipo) {
		String lSql = "";

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		// lSql += " AND COD_TIPO_ANNOTAZIONE='"+aCodTipo+"'";
		if ("003".equals(aCodTipo) || "002".equals(aCodTipo)) // Nel Caso di Amnistia o Indulto prende anche
																// l'altro direttamente
			lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )";
		else
			lSql += " AND COD_TIPO_ANNOTAZIONE='" + aCodTipo + "'";
		lSql += " AND FLAG_APP_PROVVISORIA='-'"; // NON RICHIESTE
		lSql += " AND FLAG_VALIDATO='N'";

		setCondition(lSql);
	}

	public void setCondizioneLinkEvento(BigDecimal aIdEvento) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("setCondizioneLinkEvento: " + aIdEvento);
		setCondition(" EVE_ID_EVENTO = " + aIdEvento);
	}

	public void setCondizioneSenIdSentenzaTenIdTenore(BigDecimal aIdSentenza, BigDecimal aIdTenore) {
		setCondition(" SEN_ID_SENTENZA = " + aIdSentenza + " and TEN_ID_TENORE_SIGE = " + aIdTenore);
	}

	/**
	 * Condizione di update usata per individuare le Annotazioni linkate ad altre attraverso EVE_ID_EVENTO di
	 * queste.
	 * 
	 * @param aIdEvento
	 */
	public void setCondizioneUpdateLinkAnnotazionebyIdEvento(BigDecimal aIdEvento) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("setCondizioneUpdateLinkAnnotazionebyIdEvento: " + aIdEvento);
		setCondition(" ANNO_ID_ANNOTAZIONE_MANUALE IN ( SELECT ID_ANNOTAZIONE_MANUALE FROM ANNOTAZIONE_MANUALE WHERE EVE_ID_EVENTO = "
				+ aIdEvento + " )");
	}

}