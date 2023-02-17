package siap.sico.evento.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.evento.model.EventoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EventoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Evento
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
public class EventoDAO extends SIAPTableDAO {

	public EventoDAO(Connection con) {
    super(con);
    setTable("EVENTO");

    //Settare la Sequence e i campi chiave
    this.setSequenceField("ID_EVENTO","EVE_SEQ");
    this.setFieldKey("ID_EVENTO", BIG_DECIMAL);

    setField("ID_EVENTO", BIG_DECIMAL);
    setField("COD_TIPO_EVENTO", STRING);
    setField("COD_TIPO_PROVVEDIMENTO", STRING);
    setField("COD_MOTIVO", STRING);
    setField("COD_UFFICIO_EMITTENTE", STRING);
    setField("COD_LUOGO_EMITTENTE", STRING);
    setField("COGNOME_SOGGETTO_PRESENTANTE", STRING);
    setField("NOME_SOGGETTO_PRESENTANTE", STRING);
    setField("DATA_EMISSIONE", DATE);
    setField("COD_ESITO", STRING);
    setField("FLAG_PIU_MENO", STRING);
    setField("DATA_TRASMISSIONE_ATTI", DATE);
    setField("DATA_RICEZIONE_ATTI", DATE);
    setField("COD_UFFICIO_DESTINATARIO", STRING);
    setField("COD_LUOGO_DESTINATARIO", STRING);
    setField("ANNO_PROTOCOLLO", BIG_DECIMAL);
    setField("PROGR_PROTOCOLLO", BIG_DECIMAL);
    setField("DOC_BLOB", TBLOB);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("TEN_ID_TENORE", BIG_DECIMAL);
    // setField("FAS_SIU_SOG_ID_SOGGETTO", BIG_DECIMAL);
    setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
    setField("COD_MAGISTRATO", STRING);
    setField("COD_TIPO_UFFICIO_DESTINATARIO", STRING);
    setField("FAS_SIU_ID_FASCICOLO_SIUS_DEST", BIG_DECIMAL);
    // STUB:2003-07-09 PM Aggiunto pro tempore
    setField("TEM_ID_TEMPLATE", STRING);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("FLAG_STAMPA_SIEP", STRING);
    setField("FLAG_STAMPA_SIUS", STRING);
    setField("FLAG_VIDEO_SIEP", STRING);
    setField("FLAG_VIDEO_SIUS", STRING);
    setField("DEC_ID_DECRETO_ORDINANZA_SIEP", BIG_DECIMAL);
    setField("PEN_ACC_ID_PENA_ACCESSORIA", BIG_DECIMAL);
    setField("EVE_ID_EVENTO_REVOCA", BIG_DECIMAL);
    setField("ANN_ID_ANNOTAZIONE_MANUALE", BIG_DECIMAL);
    setField("PEN_ID_PENA_RESIDUA", BIG_DECIMAL);
    setField("DATA_ESPULSIONE_SANZ_SOST",DATE);
    setField("DATA_RICHIESTA",DATE);
    setField("KEY_ESEC_NSC", BIG_DECIMAL);
    setField("TIPOLOGIA_INVIO_ATTI", STRING);
    setField("DESCRIZIONE_INVIO_ATTI", STRING);
    setField("DATA_INVIO_ATTI", DATE);
    
    setField("ISTR_ID_ISTRUTTORIA_CUMULO", BIG_DECIMAL);
    setField("ESTREMI_SOGG_RICH_ISTR", STRING);
    
    // MEV_9 (D.lgs. 123/2018)
    setField("DATA_RESTITUZIONE_AI", DATE);
   }


  //
  // METODI GET()
  //

	public BigDecimal getIdEvento() throws DAOException {
		return getBigDecimal("ID_EVENTO");
	}

	public String getCodTipoEvento() throws DAOException {
		return getString("COD_TIPO_EVENTO");
	}

	public String getCodTipoProvvedimento() throws DAOException {
		return getString("COD_TIPO_PROVVEDIMENTO");
	}

	public String getCodMotivo() throws DAOException {
		return getString("COD_MOTIVO");
	}

	public String getCodUfficioEmittente() throws DAOException {
		return getString("COD_UFFICIO_EMITTENTE");
	}

	public String getCodLuogoEmittente() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE");
	}

	public String getCognomeSoggettoPresentante() throws DAOException {
		return getString("COGNOME_SOGGETTO_PRESENTANTE");
	}

	public String getNomeSoggettoPresentante() throws DAOException {
		return getString("NOME_SOGGETTO_PRESENTANTE");
	}

	public Date getDataEmissione() throws DAOException {
		return getDate("DATA_EMISSIONE");
	}

	public String getCodEsito() throws DAOException {
		return getString("COD_ESITO");
	}

	public String getFlagPiuMeno() throws DAOException {
		return getString("FLAG_PIU_MENO");
	}

	public Date getDataTrasmissioneAtti() throws DAOException {
		return getDate("DATA_TRASMISSIONE_ATTI");
	}

	public Date getDataRicezioneAtti() throws DAOException {
		return getDate("DATA_RICEZIONE_ATTI");
	}

	public String getCodUfficioDestinatario() throws DAOException {
		return getString("COD_UFFICIO_DESTINATARIO");
	}

	public String getCodLuogoDestinatario() throws DAOException {
		return getString("COD_LUOGO_DESTINATARIO");
	}

	public BigDecimal getAnnoProtocollo() throws DAOException {
		return getBigDecimal("ANNO_PROTOCOLLO");
	}

	public BigDecimal getProgrProtocollo() throws DAOException {
		return getBigDecimal("PROGR_PROTOCOLLO");
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

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public BigDecimal getTenIdTenore() throws DAOException {
		return getBigDecimal("TEN_ID_TENORE");
	}

	// public BigDecimal getFasSiuSogIdSoggetto() throws DAOException { return
	// getBigDecimal("FAS_SIU_SOG_ID_SOGGETTO"); }
	public ByteArrayOutputStream getDocBlob() throws DAOException {
		return getBlob("DOC_BLOB");
	}

	public String getFlagDocumentoRegistrato() throws DAOException {
		return getString("FLAG_DOCUMENTO_REGISTRATO");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public String getCodTipoUfficioDestinatario() throws DAOException {
		return getString("COD_TIPO_UFFICIO_DESTINATARIO");
	}

	public BigDecimal getFasSiuIdFascicoloSiusDest() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST");
	}

  // STUB:2003-07-09 PM Aggiunto pro tempore
	public String getTemIdTemplate() throws DAOException {
		return getString("TEM_ID_TEMPLATE");
	}

	public String getFlagStampaSiep() throws DAOException {
		return getString("FLAG_STAMPA_SIEP");
	}

	public String getFlagStampaSius() throws DAOException {
		return getString("FLAG_STAMPA_SIUS");
	}

	public String getFlagVideoSiep() throws DAOException {
		return getString("FLAG_VIDEO_SIEP");
	}

	public String getFlagVideoSius() throws DAOException {
		return getString("FLAG_VIDEO_SIUS");
	}

	public BigDecimal getDecIdDecretoOrdinanzaSiep() throws DAOException {
		return getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP");
	}

	public BigDecimal getPenAccIdPenaAccessoria() throws DAOException {
		return getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public BigDecimal getEveIdEventoRevoca() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO_REVOCA");
	}

	public BigDecimal getAnnIdAnnotazioneManuale() throws DAOException {
		return getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE");
	}

	public BigDecimal getPenIdPenaResidua() throws DAOException {
		return getBigDecimal("PEN_ID_PENA_RESIDUA");
	}

	public Date getDataEspulsioneSanzSost() throws DAOException {
		return getDate("DATA_ESPULSIONE_SANZ_SOST");
	}

	public Date getDataRichiesta() throws DAOException {
		return getDate("DATA_RICHIESTA");
	}

	public BigDecimal getKeyEsecNsc() throws DAOException {
		return getBigDecimal("KEY_ESEC_NSC");
	}

	public String getCodTipologiaInvioAtti() throws DAOException {
		return getString("TIPOLOGIA_INVIO_ATTI");
	}

	public String getDescrizioneTipologiaInvioAtti() throws DAOException {
		return getString("DESCRIZIONE_INVIO_ATTI");
	}
  
  public BigDecimal getIstruIdIstruttoriaCumulo()   throws DAOException     { return getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"); }
  public String     getEstremiSoggRichIstr()        throws DAOException     { return getString("ESTREMI_SOGG_RICH_ISTR"); }

	public Date getDataInvioAtti() throws DAOException {
		return getDate("DATA_INVIO_ATTI");
	}
  
	// MEV_9 (D.lgs. 123/2018)
	public Date getDataRestituzioneAi() throws DAOException {
		return getDate("DATA_RESTITUZIONE_AI ");
	}
	
  //
  // METODI SET()
  //

	public void setIdEvento(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO", aValore);
	}

	public void setCodTipoEvento(String aValore) {
		setString("COD_TIPO_EVENTO", aValore);
	}

	public void setCodTipoProvvedimento(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO", aValore);
	}

	public void setCodMotivo(String aValore) {
		setString("COD_MOTIVO", aValore);
	}

	public void setCodUfficioEmittente(String aValore) {
		setString("COD_UFFICIO_EMITTENTE", aValore);
	}

	public void setCodLuogoEmittente(String aValore) {
		setString("COD_LUOGO_EMITTENTE", aValore);
	}

	public void setCognomeSoggettoPresentante(String aValore) {
		setString("COGNOME_SOGGETTO_PRESENTANTE", aValore);
	}

	public void setNomeSoggettoPresentante(String aValore) {
		setString("NOME_SOGGETTO_PRESENTANTE", aValore);
	}

	public void setDataEmissione(Date aValore) {
		setDate("DATA_EMISSIONE", aValore);
	}

	public void setCodEsito(String aValore) {
		setString("COD_ESITO", aValore);
	}

	public void setFlagPiuMeno(String aValore) {
		setString("FLAG_PIU_MENO", aValore);
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		setDate("DATA_TRASMISSIONE_ATTI", aValore);
	}

	public void setDataRicezioneAtti(Date aValore) {
		setDate("DATA_RICEZIONE_ATTI", aValore);
	}

	public void setCodUfficioDestinatario(String aValore) {
		setString("COD_UFFICIO_DESTINATARIO", aValore);
	}

	public void setCodLuogoDestinatario(String aValore) {
		setString("COD_LUOGO_DESTINATARIO", aValore);
	}

	public void setAnnoProtocollo(BigDecimal aValore) {
		setBigDecimal("ANNO_PROTOCOLLO", aValore);
	}

	public void setProgrProtocollo(BigDecimal aValore) {
		setBigDecimal("PROGR_PROTOCOLLO", aValore);
	}

	public void setDocBlob(ByteArrayInputStream aValore) {
		setBlob("DOC_BLOB", aValore);
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setTenIdTenore(BigDecimal aValore) {
		setBigDecimal("TEN_ID_TENORE", aValore);
	}

	public void setFlagDocumentoRegistrato(String aValore) {
		setString("FLAG_DOCUMENTO_REGISTRATO", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setCodTipoUfficioDestinatario(String aValore) {
		setString("COD_TIPO_UFFICIO_DESTINATARIO", aValore);
	}

	public void setFasSiuIdFascicoloSiusDest(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST", aValore);
	}

  // STUB:2003-07-09 PM Aggiunto pro tempore
	public void setTemIdTemplate(String aValore) {
		setString("TEM_ID_TEMPLATE", aValore);
	}

	public void setFlagStampaSiep(String aValore) {
		setString("FLAG_STAMPA_SIEP", aValore);
	}

	public void setFlagStampaSius(String aValore) {
		setString("FLAG_STAMPA_SIUS", aValore);
	}

	public void setFlagVideoSiep(String aValore) {
		setString("FLAG_VIDEO_SIEP", aValore);
	}

	public void setFlagVideoSius(String aValore) {
		setString("FLAG_VIDEO_SIUS", aValore);
	}

	public void setDecIdDecretoOrdinanzaSiep(BigDecimal aValore) {
		setBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP", aValore);
	}

	public void setPenAccIdPenaAccessoria(BigDecimal aValore) {
		setBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setEveIdEventoRevoca(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO_REVOCA", aValore);
	}

	public void setAnnIdAnnotazioneManuale(BigDecimal aValore) {
		setBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE", aValore);
	}

	public void setPenIdPenaResidua(BigDecimal aValore) {
		setBigDecimal("PEN_ID_PENA_RESIDUA", aValore);
	}

	public void setDataEspulsioneSanzSost(Date aValore) {
		setDate("DATA_ESPULSIONE_SANZ_SOST", aValore);
	}

	public void setDataRichiesta(Date aValore) {
		setDate("DATA_RICHIESTA", aValore);
	}

	public void setKeyEsecNsc(BigDecimal aValore) {
		setBigDecimal("KEY_ESEC_NSC", aValore);
	}

	public void setCodTipologiaInvioAtti(String aValore) {
		setString("TIPOLOGIA_INVIO_ATTI", aValore);
	}

	public void setDescrizioneTipologiaInvioAtti(String aValore) {
		setString("DESCRIZIONE_INVIO_ATTI", aValore);
	}

	public void setDataInvioAtti(Date aValore) {
		setDate("DATA_INVIO_ATTI", aValore);
	}
  
	public void setIstruIdIstruttoriaCumulo(BigDecimal aValore) {
		setBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO", aValore);
	}
	
	public void setEstremiSoggRichIstr (String aValore)           { 
		setString("ESTREMI_SOGG_RICH_ISTR", aValore); 
	}  
  
	// MEV_9 (D.lgs. 123/2018)
	public void setDataRestituzioneAi (Date aValore) throws DAOException {
		setDate("DATA_RESTITUZIONE_AI",aValore);
	}
  
  
	public GenericModel getModel() throws DAOException {
    EventoModel lEveMod = new EventoModel();

    lEveMod.setIdEvento(getIdEvento());
    lEveMod.setCodTipoEvento(getCodTipoEvento());
    lEveMod.setCodTipoProvvedimento(getCodTipoProvvedimento());
    lEveMod.setCodMotivo(getCodMotivo());
    lEveMod.setCodUfficioEmittente(getCodUfficioEmittente());
    lEveMod.setCodLuogoEmittente(getCodLuogoEmittente());
    lEveMod.setCognomeSoggettoPresentante(getCognomeSoggettoPresentante());
    lEveMod.setNomeSoggettoPresentante(getNomeSoggettoPresentante());
    lEveMod.setDataEmissione(getDataEmissione());
    lEveMod.setCodEsito(getCodEsito());
    lEveMod.setFlagPiuMeno(getFlagPiuMeno());
    lEveMod.setDataTrasmissioneAtti(getDataTrasmissioneAtti());
    lEveMod.setDataRicezioneAtti(getDataRicezioneAtti());
    lEveMod.setCodUfficioDestinatario(getCodUfficioDestinatario());
    lEveMod.setCodLuogoDestinatario(getCodLuogoDestinatario());
    lEveMod.setAnnoProtocollo(getAnnoProtocollo());
    lEveMod.setProgrProtocollo(getProgrProtocollo());
    //setDocBlob( )
    lEveMod.setCodOperatoreInserimento(getCodOperatoreInserimento());
    lEveMod.setDataInserimento(getDataInserimento());
    lEveMod.setCodUfficioInserimento(getCodUfficioInserimento());
    lEveMod.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
    lEveMod.setDataAggiornamento(getDataAggiornamento());
    lEveMod.setCodUfficioAggiornamento(getCodUfficioAggiornamento());
    lEveMod.setFasSieIdFascicoloSiep(getFasSieIdFascicoloSiep());
    lEveMod.setFasSiuIdFascicoloSius(getFasSiuIdFascicoloSius());
    lEveMod.setTenIdTenore(getTenIdTenore());
    lEveMod.setFlagDocumentoRegistrato(getFlagDocumentoRegistrato());
    lEveMod.setCodMagistrato(getCodMagistrato());
    lEveMod.setCodTipoUfficioDestinatario(getCodTipoUfficioDestinatario());
    lEveMod.setFasSiuIdFascicoloSiusDest(getFasSiuIdFascicoloSiusDest());
    lEveMod.setTemIdTemplate(getTemIdTemplate());
    lEveMod.setFlagStampaSiep(getFlagStampaSiep());
    lEveMod.setFlagStampaSius(getFlagStampaSius());
    lEveMod.setFlagVideoSiep(getFlagVideoSiep());
    lEveMod.setFlagVideoSius(getFlagVideoSius());
    lEveMod.setDecIdDecretoOrdinanzaSiep(getDecIdDecretoOrdinanzaSiep());
    lEveMod.setPenAccIdPenaAccessoria(getPenAccIdPenaAccessoria());
    lEveMod.setEveIdEvento(getEveIdEvento());
    lEveMod.setEveIdEventoRevoca(getEveIdEventoRevoca());
    lEveMod.setAnnIdAnnotazioneManuale(getAnnIdAnnotazioneManuale());
    lEveMod.setPenIdPenaResidua(getPenIdPenaResidua());
    lEveMod.setDataEspulsioneSanzSost(getDataEspulsioneSanzSost());
    lEveMod.setDataRichiesta(getDataRichiesta());
    lEveMod.setKeyEsecNsc(getKeyEsecNsc());
    
    lEveMod.setCodTipologiaInvioAtti (getCodTipologiaInvioAtti()); 
    lEveMod.setDescrizioneInvioAtti (getDescrizioneTipologiaInvioAtti()); 
    lEveMod.setDataInvioAtti (this.getDataInvioAtti()); 
	
	lEveMod.setIstruidIstruttoriaCumulo(getIstruIdIstruttoriaCumulo());
	lEveMod.setEstremiSoggRichIstr(getEstremiSoggRichIstr());
	
	// MEV_9 (D.lgs. 123/2018)
	lEveMod.setDataRestituzioneAi(getDataRestituzioneAi());
	
    return lEveMod;
  }

	public void setDAOFromModel(EventoModel aModel) throws DAOException {
    setIdEvento( aModel.getIdEvento() );
    setCodTipoEvento( aModel.getCodTipoEvento() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    this.setCodTipoUfficioDestinatario( aModel.getCodTipoUfficioDestinatario() );
    setCodMotivo( aModel.getCodMotivo() );
    setCodUfficioEmittente( aModel.getCodUfficioEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setCognomeSoggettoPresentante( aModel.getCognomeSoggettoPresentante() );
    setNomeSoggettoPresentante( aModel.getNomeSoggettoPresentante() );
    setDataEmissione( aModel.getDataEmissione() );
    setCodEsito( aModel.getCodEsito() );
    setFlagPiuMeno( aModel.getFlagPiuMeno() );
    setDataTrasmissioneAtti( aModel.getDataTrasmissioneAtti() );
    setDataRicezioneAtti( aModel.getDataRicezioneAtti() );
    setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
    setCodLuogoDestinatario( aModel.getCodLuogoDestinatario() );
    setAnnoProtocollo( aModel.getAnnoProtocollo() );
    setProgrProtocollo( aModel.getProgrProtocollo() );
    setDocBlob( aModel.getDocBlobIn() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setTenIdTenore( aModel.getTenIdTenore() );
    setDocBlob( aModel.getDocBlobIn());
    setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
    setFasSiuIdFascicoloSiusDest( aModel.getFasSiuIdFascicoloSiusDest() );
    setCodMagistrato( aModel.getCodMagistrato() );
    // STUB:2003-07-09 PM Aggiunto pro tempore
    setTemIdTemplate( aModel.getTemIdTemplate() );
    setFlagStampaSiep (aModel.getFlagStampaSiep());
    setFlagStampaSius (aModel.getFlagStampaSius());
    setFlagVideoSiep (aModel.getFlagVideoSiep());
    setFlagVideoSius (aModel.getFlagVideoSius());
    setDecIdDecretoOrdinanzaSiep( aModel.getDecIdDecretoOrdinanzaSiep() );
    setPenAccIdPenaAccessoria( aModel.getPenAccIdPenaAccessoria() );
    setEveIdEvento (aModel.getEveIdEvento());
    setEveIdEventoRevoca(aModel.getEveIdEventoRevoca());
    setAnnIdAnnotazioneManuale(aModel.getAnnIdAnnotazioneManuale());
    setPenIdPenaResidua(aModel.getPenIdPenaResidua());
    setDataEspulsioneSanzSost(aModel.getDataEspulsioneSanzSost());
    setDataRichiesta(aModel.getDataRichiesta());
    setKeyEsecNsc(aModel.getKeyEsecNsc());
    setIstruIdIstruttoriaCumulo(aModel.getIstruIdIstruttoriaCumulo());
    setEstremiSoggRichIstr(aModel.getEstremiSoggRichIstr());

    this.setDataInvioAtti(aModel.getDataInvioAtti());
    this.setCodTipologiaInvioAtti(aModel.getCodTipologiaInvioAtti());
    this.setDescrizioneTipologiaInvioAtti(aModel.getDescrizioneInvioAtti());
    
    // MEV_9 (D.lgs. 123/2018)
    setDataRestituzioneAi(aModel.getDataRestituzioneAi());
  }

	public void setDAOFromModelForUpdate(EventoModel aModel) throws DAOException {
    setCodTipoEvento( aModel.getCodTipoEvento() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setCodMotivo( aModel.getCodMotivo() );
    setCodUfficioEmittente( aModel.getCodUfficioEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setCognomeSoggettoPresentante( aModel.getCognomeSoggettoPresentante() );
    setNomeSoggettoPresentante( aModel.getNomeSoggettoPresentante() );
    setDataEmissione( aModel.getDataEmissione() );
    setCodEsito( aModel.getCodEsito() );
    setFlagPiuMeno( aModel.getFlagPiuMeno() );
    setDataTrasmissioneAtti( aModel.getDataTrasmissioneAtti() );
    setDataRicezioneAtti( aModel.getDataRicezioneAtti() );
    setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
    setCodLuogoDestinatario( aModel.getCodLuogoDestinatario() );
    setDocBlob( aModel.getDocBlobIn() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setTenIdTenore( aModel.getTenIdTenore() );
    setDocBlob( aModel.getDocBlobIn());
    setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
    setCodMagistrato( aModel.getCodMagistrato());
    // STUB:2003-07-09 PM Aggiunto pro tempore
    setTemIdTemplate( aModel.getTemIdTemplate() );
    setFlagStampaSiep( aModel.getFlagStampaSiep());
    setFlagStampaSius( aModel.getFlagStampaSius());
    setFlagVideoSiep( aModel.getFlagVideoSiep());
    setFlagVideoSius( aModel.getFlagVideoSius());
    setDecIdDecretoOrdinanzaSiep( aModel.getDecIdDecretoOrdinanzaSiep() );
    setPenAccIdPenaAccessoria( aModel.getPenAccIdPenaAccessoria() );
    setEveIdEvento (aModel.getEveIdEvento());
    setEveIdEventoRevoca(aModel.getEveIdEventoRevoca());
    setAnnIdAnnotazioneManuale(aModel.getAnnIdAnnotazioneManuale());
    setPenIdPenaResidua(aModel.getPenIdPenaResidua());
    setDataEspulsioneSanzSost(aModel.getDataEspulsioneSanzSost());
    setDataRichiesta(aModel.getDataRichiesta());
    setKeyEsecNsc(aModel.getKeyEsecNsc());
    
    this.setDataInvioAtti(aModel.getDataInvioAtti());
    this.setCodTipologiaInvioAtti(aModel.getCodTipologiaInvioAtti());
    this.setDescrizioneTipologiaInvioAtti(aModel.getDescrizioneInvioAtti());

    setIstruIdIstruttoriaCumulo(aModel.getIstruIdIstruttoriaCumulo());
    setEstremiSoggRichIstr(aModel.getEstremiSoggRichIstr());
	
    selCondizioneUpdate(aModel.getIdEvento());
    
    // MEV_9 (D.lgs. 123/2018)
    //setDataRestituzioneAi(aModel.getDataRestituzioneAi());
  }

	public void setDAOFromModelForUpdateBlob(EventoModel aModel) throws DAOException {
    setDocBlob( aModel.getDocBlobIn() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
  }

  // Update della data di ricezione atti
	public void setDAOFromModelForUpdateDataRicezione(EventoModel aModel) throws DAOException {
    setDataRicezioneAtti(aModel.getDataRicezioneAtti());
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );

    selCondizioneUpdate(aModel.getIdEvento());
  }

  // Update dell'Ufficio Destinatario  // 06/12/2010
	public void setDAOFromModelForUpdateUfficioDestinatario(EventoModel aModel) throws DAOException {
    setCodUfficioDestinatario(aModel.getCodUfficioDestinatario());
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );

    selCondizioneUpdate(aModel.getIdEvento());
  }

  // Update del FAS_SIE_ID_FASCICOLO_SIEP.
	public void setDAOFromModelForUpdateIdFascicoloSius(EventoModel aModel) throws DAOException {
    setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );

    selCondizioneUpdateXIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
  }
  /**
   * Il metodo prepara l'Update da effettuare sulla tabella EVENTO per annullare tutti i riferimenti
   * attraverso l'EVE_ID_EVENTO al record il cui ID viene passato attraverso il model.
	 * 
   * @param aModel
   * @throws DAOException
   */
	public void setDAOFromModelForResetRifEveIdEvento(EventoModel aModel) throws DAOException {
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setEveIdEvento(null);

    setCondition(" EVE_ID_EVENTO = " + aModel.getIdEvento());
  }

 /**
  * Il metodo prepara l'Update da effettuare sulla tabella EVENTO per annullare tutti i riferimenti
  * attraverso l'EVE_ID_EVENTO_REVOCA al record il cui ID viene passato attraverso il model.
	 * 
  * @param aModel
  * @throws DAOException
  * @param aModel
  * @throws DAOException
  */
	public void setDAOFromModelForResetRifEveIdEventoRevoca(EventoModel aModel) throws DAOException {
   setDataAggiornamento( aModel.getDataAggiornamento() );
   setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
   setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
   setEveIdEventoRevoca(null);

   setCondition(" EVE_ID_EVENTO_REVOCA = " + aModel.getIdEvento());
 }

 /**
	 * Il metodo annulla i riferimenti in tabella ad un Evento con chiave IdEvento, riferimenti tramite
	 * EVE_ID_EVENTO e EVE_ID_EVENTO_REVOCA.
	 * 
  * @param aModel
  * @throws DAOException
  */
	public void updateDAOFromModelForResetRifEve(EventoModel aModel) throws DAOException {
    // Cancellazione  riferimenti tramite EVE_ID_EVENTO
    setDAOFromModelForResetRifEveIdEvento(aModel);
    update();
    stop();

    // Cancellazione  riferimenti tramite EVE_ID_EVENTO_REVOCA
    setDAOFromModelForResetRifEveIdEventoRevoca(aModel);
    update();
    stop();
  }

	public void selCondizione(EventoModel aModel) {
    setCondition(" ID_EVENTO = " + aModel.getIdEvento());
  }

	public void selCondizioneUpdateXIdFascicoloSius(BigDecimal key) {
    setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + key);
  }

	public void selCondizioneUpdate(BigDecimal key) {
    setCondition(" ID_EVENTO = " + key);
  }

	public void selCondizioneEveIdEvento(BigDecimal key) {
    setCondition(" EVE_ID_EVENTO = " + key);
  }

	public void selCondizioneAnnIdAnnotazioneManualeNonValidati(BigDecimal key) {
		setCondition(" ANN_ID_ANNOTAZIONE_MANUALE = " + key
				+ " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)");
  }

// Per il cambiamento del codice Tipo Provvedimento in molti provvedimenti è necessario
	// prevedere una funzione di ricerca compatibile con i dati vecchi, ovvero che cerchi sia i codici nuovi
	// che il vecchi.
// Luigi 12-10-2005
	public void selCondizioneRicerca(BigDecimal aIdFascicolo, String aTipoEvento, String[] aTipoProv,
			String[] aCodMotiv, String aFlagRegistrato) {
    String lCondizione = new String();

		if (aIdFascicolo != null) {
      lCondizione += " FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
    }

		if (aTipoEvento != null) {
      if (aTipoEvento.compareTo("") != 0)
        lCondizione += " AND COD_TIPO_EVENTO ='" +aTipoEvento+ "'";
    }
		if (aTipoProv != null && aTipoProv.length > 0 && aCodMotiv != null && aCodMotiv.length > 0
				&& aTipoProv.length == aCodMotiv.length) {
      lCondizione += " AND ( ";
			for (int i = 0; i < aTipoProv.length; i++) {
        if ( i > 0)
          lCondizione += " OR ";
				lCondizione += "(COD_TIPO_PROVVEDIMENTO = '" + aTipoProv[i] + "' AND COD_MOTIVO = '"
						+ aCodMotiv[i] + "' )";
      }
      lCondizione += " )";
    }
		if (aFlagRegistrato != null) {
			if (aFlagRegistrato.compareTo("S") == 0) {
        lCondizione += " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			} else {
        if (aFlagRegistrato.compareTo("N") == 0)
          lCondizione += " AND (FLAG_DOCUMENTO_REGISTRATO = 'N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
      }
    }

    setCondition(lCondizione);
    setOrder("DATA_INSERIMENTO");
  }

//	private String inCodiceMotivo(String[] aCodMotivi) {
//		String lCondizione = "";
//
//		if (aCodMotivi.length > 0) {
//			lCondizione += " AND COD_MOTIVO IN (";
//			for (int i = 0; i < aCodMotivi.length; i++) {
//				lCondizione += "'" + aCodMotivi[i] + "'";
//				if (aCodMotivi.length > 1 && i < aCodMotivi.length - 1)
//					lCondizione += ",";
//			}
//			lCondizione += ")";
//		}
//
//		return lCondizione;
//	}

  /**
   * 22/01/2008 Il metodo prepara l'Update da effettuare sulla tabella EVENTO per valorizzare i campi
	 * PEN_ID_PENA_RESIDUA e ANN_ID_ANNOTAZIONE_MANUALE; l'EVENTO era stato inserito senza di essi per evitare
	 * eccezioni di integrità referenziale.
	 * 
   * @param aModel
   * @throws DAOException
   */
	public void setDAOFromModelForValoriIntegritaRef(EventoModel aModel) throws DAOException {
    setAnnIdAnnotazioneManuale( aModel.getAnnIdAnnotazioneManuale() );
    setPenIdPenaResidua( aModel.getPenIdPenaResidua() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setEveIdEventoRevoca( aModel.getEveIdEventoRevoca() );

    setCondition(" EVE_ID_EVENTO = " + aModel.getIdEvento());
  }
  
	public void setDAOFromModelForUpdateKeyNsc(EventoModel aModel) throws DAOException {
        setKeyEsecNsc(aModel.getKeyEsecNsc());
  }
}
