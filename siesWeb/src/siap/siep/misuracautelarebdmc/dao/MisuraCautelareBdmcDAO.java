package siap.siep.misuracautelarebdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: MisuraCautelareBdmcDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella MisuraCautelareBdmc
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
public class MisuraCautelareBdmcDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public MisuraCautelareBdmcDAO(Connection con) {
		super(con);
		setTable("MISURA_CAUTELARE_BDMC");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_MISURA_CAUTELARE_BDMC", "SEQ_MIS_CAUT_BDMC");

		// setField("ID_MISURA_CAUTELARE", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("PEN_RES_ID_PENA_RESIDUA", BIG_DECIMAL);
		setField("ID_PREN", BIG_DECIMAL);
		setField("PROG_PERI_PRES", BIG_DECIMAL);
		setField("FLAG_CARICAMENTO", STRING);
		setField("FLAG_STATO", STRING);
		setField("COD_TIPO_MISURA", STRING);
		setField("DATA_INIZIO", DATE);
		setField("DATA_FINE", DATE);
		setField("NUM_ANNI", BIG_DECIMAL);
		setField("NUM_MESI", BIG_DECIMAL);
		setField("NUM_GIORNI", BIG_DECIMAL);
		setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
		setField("ALTRO_LUOGO_DETENZIONE", STRING);
		setField("FLAG_COMPUTABILE", STRING);
		setField("COD_MOTIVO_NON_COMPUTABILE", STRING);
		setField("COD_TIPO_UFFICIO_RIFER", STRING);
		setField("COD_LUOGO_UFFICIO_RIFER", STRING);
		setField("DATA_COMPUTO", DATE);
		setField("ANNO_FASC_SIEP", BIG_DECIMAL);
		setField("NUME_FASC_SIEP", BIG_DECIMAL);
		setField("NOTE", STRING);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("COD_UFFICIO_BDMC", STRING);
		setField("ANNO_RGNR", BIG_DECIMAL);
		setField("NUME_RGNR", BIG_DECIMAL);
		setField("COD_UFFICIO_RGNR", STRING);
		setField("ANNO_REGE_GIP", BIG_DECIMAL);
		setField("NUMERO_REGE_GIP", BIG_DECIMAL);
		setField("COD_UFFICIO_GIP", STRING);
		setField("ANNO_REGE_DIB", BIG_DECIMAL);
		setField("NUMERO_REGE_DIB", BIG_DECIMAL);
		setField("COD_UFFICIO_DIB", STRING);
		setField("ANNO_REGE_CAS", BIG_DECIMAL);
		setField("NUMERO_REGE_CAS", BIG_DECIMAL);
		setField("COD_UFFICIO_CAS", STRING);
		setField("ANNO_REGE_CAP", BIG_DECIMAL);
		setField("NUMERO_REGE_CAP", BIG_DECIMAL);
		setField("COD_UFFICIO_CAP", STRING);
		setField("ANNO_REGE_CASAP", BIG_DECIMAL);
		setField("NUMERO_REGE_CASAP", BIG_DECIMAL);
		setField("COD_UFFICIO_CASAP", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("ID_MISURA_CAUTELARE", BIG_DECIMAL);
		setField("ID_ANNOTAZIONE_MANUALE", BIG_DECIMAL);
		setField("STATO_TRASMISSIONE_ISC", STRING);
		setField("STATO_TRASMISSIONE_VAL", STRING);
		setField("DATA_INIZIO_USATA", DATE);
		setField("DATA_FINE_USATA", DATE);
		setField("ID_PROVV_BDMC", BIG_DECIMAL);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdMisuraCautelareBdmc() throws DAOException {
		return getBigDecimal("ID_MISURA_CAUTELARE_BDMC");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getSogIdSoggetto() throws DAOException {
		return getBigDecimal("SOG_ID_SOGGETTO");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public BigDecimal getPenResIdPenaResidua() throws DAOException {
		return getBigDecimal("PEN_RES_ID_PENA_RESIDUA");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public BigDecimal getProgPeriPres() throws DAOException {
		return getBigDecimal("PROG_PERI_PRES");
	}

	public String getFlagCaricamento() throws DAOException {
		return getString("FLAG_CARICAMENTO");
	}

	public String getFlagStato() throws DAOException {
		return getString("FLAG_STATO");
	}

	public String getCodTipoMisura() throws DAOException {
		return getString("COD_TIPO_MISURA");
	}

	public Date getDataInizio() throws DAOException {
		return getDate("DATA_INIZIO");
	}

	public Date getDataFine() throws DAOException {
		return getDate("DATA_FINE");
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

	public String getIstDetIdIstitutoDetenzione() throws DAOException {
		return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
	}

	public String getAltroLuogoDetenzione() throws DAOException {
		return getString("ALTRO_LUOGO_DETENZIONE");
	}

	public String getFlagComputabile() throws DAOException {
		return getString("FLAG_COMPUTABILE");
	}

	public String getCodMotivoNonComputabile() throws DAOException {
		return getString("COD_MOTIVO_NON_COMPUTABILE");
	}

	public String getCodTipoUfficioRifer() throws DAOException {
		return getString("COD_TIPO_UFFICIO_RIFER");
	}

	public String getCodLuogoUfficioRifer() throws DAOException {
		return getString("COD_LUOGO_UFFICIO_RIFER");
	}

	public Date getDataComputo() throws DAOException {
		return getDate("DATA_COMPUTO");
	}

	public BigDecimal getAnnoFascSiep() throws DAOException {
		return getBigDecimal("ANNO_FASC_SIEP");
	}

	public BigDecimal getNumeFascSiep() throws DAOException {
		return getBigDecimal("NUME_FASC_SIEP");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
	}

	public BigDecimal getNumeFascBdmc() throws DAOException {
		return getBigDecimal("NUME_FASC_BDMC");
	}

	public String getCodUfficioBdmc() throws DAOException {
		return getString("COD_UFFICIO_BDMC");
	}

	public BigDecimal getAnnoRgnr() throws DAOException {
		return getBigDecimal("ANNO_RGNR");
	}

	public BigDecimal getNumeRgnr() throws DAOException {
		return getBigDecimal("NUME_RGNR");
	}

	public String getCodUfficioRgnr() throws DAOException {
		return getString("COD_UFFICIO_RGNR");
	}

	public BigDecimal getAnnoRegeGip() throws DAOException {
		return getBigDecimal("ANNO_REGE_GIP");
	}

	public BigDecimal getNumeroRegeGip() throws DAOException {
		return getBigDecimal("NUMERO_REGE_GIP");
	}

	public String getCodUfficioGip() throws DAOException {
		return getString("COD_UFFICIO_GIP");
	}

	public BigDecimal getAnnoRegeDib() throws DAOException {
		return getBigDecimal("ANNO_REGE_DIB");
	}

	public BigDecimal getNumeroRegeDib() throws DAOException {
		return getBigDecimal("NUMERO_REGE_DIB");
	}

	public String getCodUfficioDib() throws DAOException {
		return getString("COD_UFFICIO_DIB");
	}

	public BigDecimal getAnnoRegeCas() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAS");
	}

	public BigDecimal getNumeroRegeCas() throws DAOException {
		return getBigDecimal("NUMERO_REGE_CAS");
	}

	public String getCodUfficioCas() throws DAOException {
		return getString("COD_UFFICIO_CAS");
	}

	public BigDecimal getAnnoRegeCap() throws DAOException {
		return getBigDecimal("ANNO_REGE_CAP");
	}

	public BigDecimal getNumeroRegeCap() throws DAOException {
		return getBigDecimal("NUMERO_REGE_CAP");
	}

	public String getCodUfficioCap() throws DAOException {
		return getString("COD_UFFICIO_CAP");
	}

	public BigDecimal getAnnoRegeCasap() throws DAOException {
		return getBigDecimal("ANNO_REGE_CASAP");
	}

	public BigDecimal getNumeroRegeCasap() throws DAOException {
		return getBigDecimal("NUMERO_REGE_CASAP");
	}

	public String getCodUfficioCasap() throws DAOException {
		return getString("COD_UFFICIO_CASAP");
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

	public BigDecimal getIdMisuraCautelare() throws DAOException {
		return getBigDecimal("ID_MISURA_CAUTELARE");
	}

	public BigDecimal getIdAnnotazioneManuale() throws DAOException {
		return getBigDecimal("ID_ANNOTAZIONE_MANUALE");
	}

	public BigDecimal getIdProvvBdmc() throws DAOException {
		return getBigDecimal("ID_PROVV_BDMC");
	}

	public String getStatoTrasmissioneIsc() throws DAOException {
		return getString("STATO_TRASMISSIONE_ISC");
	}

	public String getStatoTrasmissioneVal() throws DAOException {
		return getString("STATO_TRASMISSIONE_VAL");
	}

	public Date getDataInizioUsata() throws DAOException {
		return getDate("DATA_INIZIO_USATA");
	}

	public Date getDataFineUsata() throws DAOException {
		return getDate("DATA_FINE_USATA");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdMisuraCautelareBdmc(BigDecimal aValore) {
		setBigDecimal("ID_MISURA_CAUTELARE_BDMC", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		setBigDecimal("SOG_ID_SOGGETTO", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		setBigDecimal("PEN_RES_ID_PENA_RESIDUA", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setProgPeriPres(BigDecimal aValore) {
		setBigDecimal("PROG_PERI_PRES", aValore);
	}

	public void setFlagCaricamento(String aValore) {
		setString("FLAG_CARICAMENTO", aValore);
	}

	public void setFlagStato(String aValore) {
		setString("FLAG_STATO", aValore);
	}

	public void setCodTipoMisura(String aValore) {
		setString("COD_TIPO_MISURA", aValore);
	}

	public void setDataInizio(Date aValore) {
		setDate("DATA_INIZIO", aValore);
	}

	public void setDataFine(Date aValore) {
		setDate("DATA_FINE", aValore);
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

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
	}

	public void setAltroLuogoDetenzione(String aValore) {
		setString("ALTRO_LUOGO_DETENZIONE", aValore);
	}

	public void setFlagComputabile(String aValore) {
		setString("FLAG_COMPUTABILE", aValore);
	}

	public void setCodMotivoNonComputabile(String aValore) {
		setString("COD_MOTIVO_NON_COMPUTABILE", aValore);
	}

	public void setCodTipoUfficioRifer(String aValore) {
		setString("COD_TIPO_UFFICIO_RIFER", aValore);
	}

	public void setCodLuogoUfficioRifer(String aValore) {
		setString("COD_LUOGO_UFFICIO_RIFER", aValore);
	}

	public void setDataComputo(Date aValore) {
		setDate("DATA_COMPUTO", aValore);
	}

	public void setAnnoFascSiep(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_SIEP", aValore);
	}

	public void setNumeFascSiep(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_SIEP", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_BDMC", aValore);
	}

	public void setCodUfficioBdmc(String aValore) {
		setString("COD_UFFICIO_BDMC", aValore);
	}

	public void setAnnoRgnr(BigDecimal aValore) {
		setBigDecimal("ANNO_RGNR", aValore);
	}

	public void setNumeRgnr(BigDecimal aValore) {
		setBigDecimal("NUME_RGNR", aValore);
	}

	public void setCodUfficioRgnr(String aValore) {
		setString("COD_UFFICIO_RGNR", aValore);
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_GIP", aValore);
	}

	public void setNumeroRegeGip(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGE_GIP", aValore);
	}

	public void setCodUfficioGip(String aValore) {
		setString("COD_UFFICIO_GIP", aValore);
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_DIB", aValore);
	}

	public void setNumeroRegeDib(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGE_DIB", aValore);
	}

	public void setCodUfficioDib(String aValore) {
		setString("COD_UFFICIO_DIB", aValore);
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAS", aValore);
	}

	public void setNumeroRegeCas(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGE_CAS", aValore);
	}

	public void setCodUfficioCas(String aValore) {
		setString("COD_UFFICIO_CAS", aValore);
	}

	public void setAnnoRegeCap(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CAP", aValore);
	}

	public void setNumeroRegeCap(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGE_CAP", aValore);
	}

	public void setCodUfficioCap(String aValore) {
		setString("COD_UFFICIO_CAP", aValore);
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		setBigDecimal("ANNO_REGE_CASAP", aValore);
	}

	public void setNumeroRegeCasap(BigDecimal aValore) {
		setBigDecimal("NUMERO_REGE_CASAP", aValore);
	}

	public void setCodUfficioCasap(String aValore) {
		setString("COD_UFFICIO_CASAP", aValore);
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

	public void setIdMisuraCautelare(BigDecimal aValore) {
		setBigDecimal("ID_MISURA_CAUTELARE", aValore);
	}

	public void setIdAnnotazioneManuale(BigDecimal aValore) {
		setBigDecimal("ID_ANNOTAZIONE_MANUALE", aValore);
	}

	public void setIdProvvBdmc(BigDecimal aValore) {
		setBigDecimal("ID_PROVV_BDMC", aValore);
	}

	public void setStatoTrasmissioneIsc(String aValore) {
		setString("STATO_TRASMISSIONE_ISC", aValore);
	}

	public void setStatoTrasmissioneVal(String aValore) {
		setString("STATO_TRASMISSIONE_VAL", aValore);
	}

	public void setDataInizioUsata(Date aValore) {
		setDate("DATA_INIZIO_USATA", aValore);
	}

	public void setDataFineUsata(Date aValore) {
		setDate("DATA_FINE_USATA", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new MisuraCautelareBdmcModel(getIdMisuraCautelareBdmc(), getFasSieIdFascicoloSiep(),
				getSogIdSoggetto(), getEveIdEvento(), getPenResIdPenaResidua(), getIdPren(),
				getProgPeriPres(), getFlagCaricamento(), getFlagStato(), getCodTipoMisura(), "",
				getDataInizio(), getDataFine(), getNumAnni(), getNumMesi(), getNumGiorni(),
				getIstDetIdIstitutoDetenzione(), getAltroLuogoDetenzione(), getFlagComputabile(),
				getCodMotivoNonComputabile(), "", getCodTipoUfficioRifer(), "", getCodLuogoUfficioRifer(),
				"", getDataComputo(), getAnnoFascSiep(), getNumeFascSiep(), getNote(), getAnnoFascBdmc(),
				getNumeFascBdmc(), getCodUfficioBdmc(), "", getAnnoRgnr(), getNumeRgnr(),
				getCodUfficioRgnr(), "", getAnnoRegeGip(), getNumeroRegeGip(), getCodUfficioGip(), "",
				getAnnoRegeDib(), getNumeroRegeDib(), getCodUfficioDib(), "", getAnnoRegeCas(),
				getNumeroRegeCas(), getCodUfficioCas(), "", getAnnoRegeCap(), getNumeroRegeCap(),
				getCodUfficioCap(), "", getAnnoRegeCasap(), getNumeroRegeCasap(), getCodUfficioCasap(), "",
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(),
				getIdMisuraCautelare(), getIdAnnotazioneManuale(), getIdProvvBdmc(),
				getStatoTrasmissioneIsc(), getStatoTrasmissioneVal(), getDataInizioUsata(),
				getDataFineUsata());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(MisuraCautelareBdmcModel aModel) throws DAOException {
		setIdMisuraCautelareBdmc(aModel.getIdMisuraCautelareBdmc());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setSogIdSoggetto(aModel.getSogIdSoggetto());
		setEveIdEvento(aModel.getEveIdEvento());
		setPenResIdPenaResidua(aModel.getPenResIdPenaResidua());
		setIdPren(aModel.getIdPren());
		setProgPeriPres(aModel.getProgPeriPres());
		setFlagCaricamento(aModel.getFlagCaricamento());
		setFlagStato(aModel.getFlagStato());
		setCodTipoMisura(aModel.getCodTipoMisura());
		setDataInizio(aModel.getDataInizio());
		setDataFine(aModel.getDataFine());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setAltroLuogoDetenzione(aModel.getAltroLuogoDetenzione());
		setFlagComputabile(aModel.getFlagComputabile());
		setCodMotivoNonComputabile(aModel.getCodMotivoNonComputabile());
		setCodTipoUfficioRifer(aModel.getCodTipoUfficioRifer());
		setCodLuogoUfficioRifer(aModel.getCodLuogoUfficioRifer());
		setDataComputo(aModel.getDataComputo());
		setAnnoFascSiep(aModel.getAnnoFascSiep());
		setNumeFascSiep(aModel.getNumeFascSiep());
		setNote(aModel.getNote());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setNumeFascBdmc(aModel.getNumeFascBdmc());
		setCodUfficioBdmc(aModel.getCodUfficioBdmc());
		setAnnoRgnr(aModel.getAnnoRgnr());
		setNumeRgnr(aModel.getNumeRgnr());
		setCodUfficioRgnr(aModel.getCodUfficioRgnr());
		setAnnoRegeGip(aModel.getAnnoRegeGip());
		setNumeroRegeGip(aModel.getNumeroRegeGip());
		setCodUfficioGip(aModel.getCodUfficioGip());
		setAnnoRegeDib(aModel.getAnnoRegeDib());
		setNumeroRegeDib(aModel.getNumeroRegeDib());
		setCodUfficioDib(aModel.getCodUfficioDib());
		setAnnoRegeCas(aModel.getAnnoRegeCas());
		setNumeroRegeCas(aModel.getNumeroRegeCas());
		setCodUfficioCas(aModel.getCodUfficioCas());
		setAnnoRegeCap(aModel.getAnnoRegeCap());
		setNumeroRegeCap(aModel.getNumeroRegeCap());
		setCodUfficioCap(aModel.getCodUfficioCap());
		setAnnoRegeCasap(aModel.getAnnoRegeCasap());
		setNumeroRegeCasap(aModel.getNumeroRegeCasap());
		setCodUfficioCasap(aModel.getCodUfficioCasap());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setIdMisuraCautelare(aModel.getIdMisuraCautelare());
		setIdAnnotazioneManuale(aModel.getIdAnnotazioneManuale());
		setIdProvvBdmc(aModel.getIdProvvBdmc());
		setStatoTrasmissioneIsc(aModel.getStatoTrasmissioneIsc());
		setStatoTrasmissioneVal(aModel.getStatoTrasmissioneVal());
		setDataInizioUsata(aModel.getDataInizioUsata());
		setDataFineUsata(aModel.getDataFineUsata());

	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(MisuraCautelareBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdMisuraCautelareBdmc() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aModel.getIdMisuraCautelareBdmc() + "";
		}
		if (aModel.getIdMisuraCautelare() != null) {
			lCondizioni += " and ID_MISURA_CAUTELARE = " + aModel.getIdMisuraCautelare() + "";
		}
		if (aModel.getIdAnnotazioneManuale() != null) {
			lCondizioni += " and ID_ANNOTAZIONE_MANUALE = " + aModel.getIdAnnotazioneManuale() + "";
		}

		if (aModel.getStatoTrasmissioneIsc() != null) {
			lCondizioni += " and STATO_TRASMISSIONE_ISC = '" + aModel.getStatoTrasmissioneIsc() + "'";
		}
		if (aModel.getStatoTrasmissioneVal() != null) {
			lCondizioni += " and STATO_TRASMISSIONE_VAL = '" + aModel.getStatoTrasmissioneVal() + "'";
		}
		if (aModel.getDataInizioUsata() != null) {
			lCondizioni += " and to_char(DATA_INIZIO_USATA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizioUsata(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFineUsata() != null) {
			lCondizioni += " and to_char(DATA_FINE_USATA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFineUsata(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getSogIdSoggetto() != null) {
			lCondizioni += " and SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		if (aModel.getPenResIdPenaResidua() != null) {
			lCondizioni += " and PEN_RES_ID_PENA_RESIDUA = " + aModel.getPenResIdPenaResidua() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PRES = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getFlagCaricamento() != null && aModel.getFlagCaricamento().length() > 0) {
			lCondizioni += " and FLAG_CARICAMENTO = '" + aModel.getFlagCaricamento() + "' ";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) {
			lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' ";
		}
		if (aModel.getDataInizio() != null) {
			lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizio(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNumAnni() != null) {
			lCondizioni += " and NUM_ANNI = " + aModel.getNumAnni() + "";
		}
		if (aModel.getNumMesi() != null) {
			lCondizioni += " and NUM_MESI = " + aModel.getNumMesi() + "";
		}
		if (aModel.getNumGiorni() != null) {
			lCondizioni += " and NUM_GIORNI = " + aModel.getNumGiorni() + "";
		}
		if (aModel.getIstDetIdIstitutoDetenzione() != null
				&& aModel.getIstDetIdIstitutoDetenzione().length() > 0) {
			lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione()
					+ "' ";
		}
		if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) {
			lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' ";
		}
		if (aModel.getFlagComputabile() != null && aModel.getFlagComputabile().length() > 0) {
			lCondizioni += " and FLAG_COMPUTABILE = '" + aModel.getFlagComputabile() + "' ";
		}
		if (aModel.getCodMotivoNonComputabile() != null && aModel.getCodMotivoNonComputabile().length() > 0) {
			lCondizioni += " and COD_MOTIVO_NON_COMPUTABILE = '" + aModel.getCodMotivoNonComputabile() + "' ";
		}
		if (aModel.getCodTipoUfficioRifer() != null && aModel.getCodTipoUfficioRifer().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_RIFER = '" + aModel.getCodTipoUfficioRifer() + "' ";
		}
		if (aModel.getCodLuogoUfficioRifer() != null && aModel.getCodLuogoUfficioRifer().length() > 0) {
			lCondizioni += " and COD_LUOGO_UFFICIO_RIFER = '" + aModel.getCodLuogoUfficioRifer() + "' ";
		}
		if (aModel.getDataComputo() != null) {
			lCondizioni += " and to_char(DATA_COMPUTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataComputo(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoFascSiep() != null) {
			lCondizioni += " and ANNO_FASC_SIEP = " + aModel.getAnnoFascSiep() + "";
		}
		if (aModel.getNumeFascSiep() != null) {
			lCondizioni += " and NUME_FASC_SIEP = " + aModel.getNumeFascSiep() + "";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodUfficioBdmc() != null && aModel.getCodUfficioBdmc().length() > 0) {
			lCondizioni += " and COD_UFFICIO_BDMC = '" + aModel.getCodUfficioBdmc() + "' ";
		}
		if (aModel.getAnnoRgnr() != null) {
			lCondizioni += " and ANNO_RGNR = " + aModel.getAnnoRgnr() + "";
		}
		if (aModel.getNumeRgnr() != null) {
			lCondizioni += " and NUME_RGNR = " + aModel.getNumeRgnr() + "";
		}
		if (aModel.getCodUfficioRgnr() != null && aModel.getCodUfficioRgnr().length() > 0) {
			lCondizioni += " and COD_UFFICIO_RGNR = '" + aModel.getCodUfficioRgnr() + "' ";
		}
		if (aModel.getAnnoRegeGip() != null) {
			lCondizioni += " and ANNO_REGE_GIP = " + aModel.getAnnoRegeGip() + "";
		}
		if (aModel.getNumeroRegeGip() != null) {
			lCondizioni += " and NUMERO_REGE_GIP = " + aModel.getNumeroRegeGip() + "";
		}
		if (aModel.getCodUfficioGip() != null && aModel.getCodUfficioGip().length() > 0) {
			lCondizioni += " and COD_UFFICIO_GIP = '" + aModel.getCodUfficioGip() + "' ";
		}
		if (aModel.getAnnoRegeDib() != null) {
			lCondizioni += " and ANNO_REGE_DIB = " + aModel.getAnnoRegeDib() + "";
		}
		if (aModel.getNumeroRegeDib() != null) {
			lCondizioni += " and NUMERO_REGE_DIB = " + aModel.getNumeroRegeDib() + "";
		}
		if (aModel.getCodUfficioDib() != null && aModel.getCodUfficioDib().length() > 0) {
			lCondizioni += " and COD_UFFICIO_DIB = '" + aModel.getCodUfficioDib() + "' ";
		}
		if (aModel.getAnnoRegeCas() != null) {
			lCondizioni += " and ANNO_REGE_CAS = " + aModel.getAnnoRegeCas() + "";
		}
		if (aModel.getNumeroRegeCas() != null) {
			lCondizioni += " and NUMERO_REGE_CAS = " + aModel.getNumeroRegeCas() + "";
		}
		if (aModel.getCodUfficioCas() != null && aModel.getCodUfficioCas().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CAS = '" + aModel.getCodUfficioCas() + "' ";
		}
		if (aModel.getAnnoRegeCap() != null) {
			lCondizioni += " and ANNO_REGE_CAP = " + aModel.getAnnoRegeCap() + "";
		}
		if (aModel.getNumeroRegeCap() != null) {
			lCondizioni += " and NUMERO_REGE_CAP = " + aModel.getNumeroRegeCap() + "";
		}
		if (aModel.getCodUfficioCap() != null && aModel.getCodUfficioCap().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CAP = '" + aModel.getCodUfficioCap() + "' ";
		}
		if (aModel.getAnnoRegeCasap() != null) {
			lCondizioni += " and ANNO_REGE_CASAP = " + aModel.getAnnoRegeCasap() + "";
		}
		if (aModel.getNumeroRegeCasap() != null) {
			lCondizioni += " and NUMERO_REGE_CASAP = " + aModel.getNumeroRegeCasap() + "";
		}
		if (aModel.getCodUfficioCasap() != null && aModel.getCodUfficioCasap().length() > 0) {
			lCondizioni += " and COD_UFFICIO_CASAP = '" + aModel.getCodUfficioCasap() + "' ";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aIdMisuraCautelare) {
		String lCondizioni = new String();

		lCondizioni += " and ID_MISURA_CAUTELARE_BDMC = " + aIdMisuraCautelare;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di order by per la ricerca
	 * 
	 *****************************************************************************/
	public void setOrderBy() {
		String orderBy = "";
		// =======================================================================
		// Lasciare orderBy="" se non si vuole scegliere un ordinamento,
		// altrimenti elencare i campi separati da virgola
		// n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente
		// =======================================================================

		setOrder(orderBy);
	}

}