package siap.siep.decretoordinanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: DecretoOrdinanzaSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DecretoOrdinanzaSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class DecretoOrdinanzaSiepDAO extends TableDAO
{
	public DecretoOrdinanzaSiepDAO (Connection con)
	{
    super(con);
    setTable("DECRETO_ORDINANZA_SIEP");

    setSequenceField("ID_DECRETO_ORDINANZA_SIEP", "DEC_ORD_SEQ");

    setFieldKey("ID_DECRETO_ORDINANZA_SIEP", BIG_DECIMAL);

    setField("ID_DECRETO_ORDINANZA_SIEP", BIG_DECIMAL);
    setField("DATA_RICEZIONE_PROVVEDIMENTO", DATE);
    setField("DATA_EMISSIONE_PROVVEDIMENTO", DATE);
    setField("COD_TIPO_REGISTRO_ORDINANZA", STRING);
    setField("ANNO_REGISTRO", BIG_DECIMAL);
    setField("NUM_REGISTRO", BIG_DECIMAL);
    setField("ANNO_PROVVEDIMENTO", BIG_DECIMAL);
    setField("NUM_PROVVEDIMENTO", BIG_DECIMAL);
    setField("COD_TIPO_PROVVEDIMENTO", STRING);
    setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
    setField("COD_LUOGO_EMITTENTE", STRING);
    setField("DATA_SOSPENSIONE_ESECUZIONE", DATE);
    setField("DATA_DIFFERIMENTO", DATE);
    setField("DATA_RINVIO", DATE);
    setField("DATA_FINE_INTERRUZIONE", DATE);
    setField("DATA_DEPOSITO_ISTANZA", DATE);
    setField("DATA_INTERRUZIONE_PENA", DATE);
    setField("COD_OGGETTO_DECISIONE", STRING);
    setField("MOTIVAZIONI", STRING);
    setField("NOTE", STRING);
    setField("FLAG_SCARCERARE_SCARCERATO", STRING);
    setField("FLAG_PRESENTANTE_ISTANZA", STRING);
    setField("COD_CONTENUTO_DECRETO", STRING);
    setField("COD_OGGETTO_PROCEDIMENTO", STRING);
    setField("FLAG_DATA_INTERRUZIONE_INVALID", STRING);
    setField("PROTOCOLLO", STRING);
    setField("ALTRA_AUTORITA", STRING);
    setField("ALTRO_LUOGO", STRING);
    setField("ID_EVENTO_GENERATO", BIG_DECIMAL);
    setField("FLAG_ELABORATO", STRING);
    setField("DATA_ESPULSIONE", DATE);
    setField("FLAG_DECISIONE_TRIBUNALE", STRING);
    setField("COD_ESITO", STRING);
    setField("NUM_ANNI_RINVIO", BIG_DECIMAL);
    setField("NUM_MESI_RINVIO", BIG_DECIMAL);
    setField("NUM_GIORNI_RINVIO", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("DATA_REVOCA_SOSPENSIONE", DATE);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
// 01-09-2015 - MEV_2 (Misure Sicurezza) STRP_2
    setField("ANNO_REG_GEN", BIG_DECIMAL);
    setField("NUMERO_REG_GEN", BIG_DECIMAL);
    setField("TIPO_REG_GEN", STRING);
	}

  //
  // METODI GET()
  //

  public BigDecimal getIdDecretoOrdinanzaSiep() 		throws DAOException	 { return getBigDecimal("ID_DECRETO_ORDINANZA_SIEP"); }
  public Date 			getDataRicezioneProvvedimento() 		throws DAOException	 { return getDate("DATA_RICEZIONE_PROVVEDIMENTO"); }
  public Date 			getDataEmissioneProvvedimento() 		throws DAOException	 { return getDate("DATA_EMISSIONE_PROVVEDIMENTO"); }
  public String 		getCodTipoRegistroOrdinanza() 		throws DAOException	 { return getString("COD_TIPO_REGISTRO_ORDINANZA"); }
  public BigDecimal getAnnoRegistro() 		throws DAOException	 { return getBigDecimal("ANNO_REGISTRO"); }
  public BigDecimal getNumRegistro() 		throws DAOException	 { return getBigDecimal("NUM_REGISTRO"); }
  public BigDecimal getAnnoProvvedimento() 		throws DAOException	 { return getBigDecimal("ANNO_PROVVEDIMENTO"); }
  public BigDecimal getNumProvvedimento() 		throws DAOException	 { return getBigDecimal("NUM_PROVVEDIMENTO"); }
  public String 		getCodTipoProvvedimento() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
  public String 		getCodTipoAutoritaEmittente() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITTENTE"); }
  public String 		getCodLuogoEmittente() 		throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
  public Date 			getDataSospensioneEsecuzione() 		throws DAOException	 { return getDate("DATA_SOSPENSIONE_ESECUZIONE"); }
  public Date 			getDataDifferimento() 		throws DAOException	 { return getDate("DATA_DIFFERIMENTO"); }
  public Date 			getDataRinvio() 		throws DAOException	 { return getDate("DATA_RINVIO"); }
  public Date 			getDataFineInterruzione() 		throws DAOException	 { return getDate("DATA_FINE_INTERRUZIONE"); }
  public Date 			getDataDepositoIstanza() 		throws DAOException	 { return getDate("DATA_DEPOSITO_ISTANZA"); }
  public Date 			getDataInterruzionePena() 		throws DAOException	 { return getDate("DATA_INTERRUZIONE_PENA"); }
  public String 		getCodOggettoDecisione() 		throws DAOException	 { return getString("COD_OGGETTO_DECISIONE"); }
  public String 		getMotivazioni() 		throws DAOException	 { return getString("MOTIVAZIONI"); }
  public String 		getNote() 		throws DAOException	 { return getString("NOTE"); }
  public String 		getFlagScarcerareScarcerato() 		throws DAOException	 { return getString("FLAG_SCARCERARE_SCARCERATO"); }
  public String 		getFlagPresentanteIstanza() 		throws DAOException	 { return getString("FLAG_PRESENTANTE_ISTANZA"); }
  public String 		getCodContenutoDecreto() 		throws DAOException	 { return getString("COD_CONTENUTO_DECRETO"); }
  public String 		getCodOggettoProcedimento() 		throws DAOException	 { return getString("COD_OGGETTO_PROCEDIMENTO"); }
  public String 		getFlagDataInterruzioneInvalid() 		throws DAOException	 { return getString("FLAG_DATA_INTERRUZIONE_INVALID"); }
  public String 		getProtocollo() 		throws DAOException	 { return getString("PROTOCOLLO"); }
  public String 		getAltraAutorita() 		throws DAOException	 { return getString("ALTRA_AUTORITA"); }
  public String 		getAltroLuogo() 		throws DAOException	 { return getString("ALTRO_LUOGO"); }
  public BigDecimal getIdEventoGenerato() 		throws DAOException	 { return getBigDecimal("ID_EVENTO_GENERATO"); }
  public String 		getFlagElaborato() 		throws DAOException	 { return getString("FLAG_ELABORATO"); }
  public Date 			getDataEspulsione() 		throws DAOException	 { return getDate("DATA_ESPULSIONE"); }
  public String 		getFlagDecisioneTribunale() 		throws DAOException	 { return getString("FLAG_DECISIONE_TRIBUNALE"); }
  public String 		getCodEsito() 		throws DAOException	 { return getString("COD_ESITO"); }
  public BigDecimal getNumAnniRinvio() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RINVIO"); }
  public BigDecimal getNumMesiRinvio() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RINVIO"); }
  public BigDecimal getNumGiorniRinvio() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RINVIO"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public Date 			getDataRevocaSospensione() 		throws DAOException	 { return getDate("DATA_REVOCA_SOSPENSIONE"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  
  public BigDecimal getAnnoRegGen() 		throws DAOException	 { return getBigDecimal("ANNO_REG_GEN"); }
  public BigDecimal getNumeroRegGen()		throws DAOException	 { return getBigDecimal("NUMERO_REG_GEN"); } 
  public String		getTipoRegGen() 		throws DAOException	 { return getString("TIPO_REG_GEN"); }

  //
  // METODI SET()
  //

  public void setIdDecretoOrdinanzaSiep(BigDecimal aValore ) 			  { setBigDecimal("ID_DECRETO_ORDINANZA_SIEP", aValore); }
  public void setDataRicezioneProvvedimento(Date aValore ) 			    { setDate("DATA_RICEZIONE_PROVVEDIMENTO", aValore); }
  public void setDataEmissioneProvvedimento(Date aValore ) 			    { setDate("DATA_EMISSIONE_PROVVEDIMENTO", aValore); }
  public void setCodTipoRegistroOrdinanza(String aValore ) 			    { setString("COD_TIPO_REGISTRO_ORDINANZA", aValore); }
  public void setAnnoRegistro(BigDecimal aValore ) 			            { setBigDecimal("ANNO_REGISTRO", aValore); }
  public void setNumRegistro(BigDecimal aValore ) 			            { setBigDecimal("NUM_REGISTRO", aValore); }
  public void setAnnoProvvedimento(BigDecimal aValore ) 			      { setBigDecimal("ANNO_PROVVEDIMENTO", aValore); }
  public void setNumProvvedimento(BigDecimal aValore ) 			        { setBigDecimal("NUM_PROVVEDIMENTO", aValore); }
  public void setCodTipoProvvedimento(String aValore ) 			        { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
  public void setCodTipoAutoritaEmittente(String aValore ) 			    { setString("COD_TIPO_AUTORITA_EMITTENTE", aValore); }
  public void setCodLuogoEmittente(String aValore ) 			          { setString("COD_LUOGO_EMITTENTE", aValore); }
  public void setDataSospensioneEsecuzione(Date aValore ) 			    { setDate("DATA_SOSPENSIONE_ESECUZIONE", aValore); }
  public void setDataDifferimento(Date aValore ) 			              { setDate("DATA_DIFFERIMENTO", aValore); }
  public void setDataRinvio(Date aValore ) 			                    { setDate("DATA_RINVIO", aValore); }
  public void setDataFineInterruzione(Date aValore ) 			          { setDate("DATA_FINE_INTERRUZIONE", aValore); }
  public void setDataDepositoIstanza(Date aValore ) 			          { setDate("DATA_DEPOSITO_ISTANZA", aValore); }
  public void setDataInterruzionePena(Date aValore ) 			          { setDate("DATA_INTERRUZIONE_PENA", aValore); }
  public void setCodOggettoDecisione(String aValore ) 			        { setString("COD_OGGETTO_DECISIONE", aValore); }
  public void setMotivazioni(String aValore ) 			                { setString("MOTIVAZIONI", aValore); }
  public void setNote(String aValore ) 			                        { setString("NOTE", aValore); }
  public void setFlagScarcerareScarcerato(String aValore ) 			    { setString("FLAG_SCARCERARE_SCARCERATO", aValore); }
  public void setFlagPresentanteIstanza(String aValore ) 			      { setString("FLAG_PRESENTANTE_ISTANZA", aValore); }
  public void setCodContenutoDecreto(String aValore ) 			        { setString("COD_CONTENUTO_DECRETO", aValore); }
  public void setCodOggettoProcedimento(String aValore ) 			      { setString("COD_OGGETTO_PROCEDIMENTO", aValore); }
  public void setFlagDataInterruzioneInvalid(String aValore ) 			{ setString("FLAG_DATA_INTERRUZIONE_INVALID", aValore); }
  public void setProtocollo(String aValore ) 			                  { setString("PROTOCOLLO", aValore); }
  public void setAltraAutorita(String aValore ) 			              { setString("ALTRA_AUTORITA", aValore); }
  public void setAltroLuogo(String aValore ) 			                  { setString("ALTRO_LUOGO", aValore); }
  public void setIdEventoGenerato(BigDecimal aValore ) 			        { setBigDecimal("ID_EVENTO_GENERATO", aValore); }
  public void setFlagElaborato(String aValore ) 			              { setString("FLAG_ELABORATO", aValore); }
  public void setDataEspulsione(Date aValore ) 			                { setDate("DATA_ESPULSIONE", aValore); }
  public void setFlagDecisioneTribunale(String aValore ) 			      { setString("FLAG_DECISIONE_TRIBUNALE", aValore); }
  public void setCodEsito(String aValore ) 			                    { setString("COD_ESITO", aValore); }
  public void setNumAnniRinvio(BigDecimal aValore ) 			          { setBigDecimal("NUM_ANNI_RINVIO", aValore); }
  public void setNumMesiRinvio(BigDecimal aValore ) 			          { setBigDecimal("NUM_MESI_RINVIO", aValore); }
  public void setNumGiorniRinvio(BigDecimal aValore ) 			        { setBigDecimal("NUM_GIORNI_RINVIO", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			              { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 			  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			            { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setDataRevocaSospensione(Date aValore ) 			        { setDate("DATA_REVOCA_SOSPENSIONE", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 			  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  
  public void setAnnoRegGen(BigDecimal aValore )						{ setBigDecimal("ANNO_REG_GEN", aValore); }
  public void setNumeroRegGen(BigDecimal aValore )						{ setBigDecimal("NUMERO_REG_GEN", aValore); }
  public void setTipoRegGen(String aValore )							{ setString("TIPO_REG_GEN", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new DecretoOrdinanzaSiepModel(
                                           getIdDecretoOrdinanzaSiep() ,
                                           getDataRicezioneProvvedimento() ,
                                           getDataEmissioneProvvedimento() ,
                                           getCodTipoRegistroOrdinanza() ,
                                           "",
                                           getAnnoRegistro() ,
                                           getNumRegistro() ,
                                           getAnnoProvvedimento() ,
                                           getNumProvvedimento() ,
                                           getCodTipoProvvedimento() ,
                                           "",
								                           getCodTipoAutoritaEmittente() ,
                                           "",
                                           getCodLuogoEmittente() ,
                                           "",
                                           getDataSospensioneEsecuzione() ,
                                           getDataDifferimento() ,
                                           getDataRinvio() ,
                                           getDataFineInterruzione() ,
                                           getDataDepositoIstanza() ,
                                           getDataInterruzionePena() ,
                                           getCodOggettoDecisione() ,
                                           "",
                                           getMotivazioni() ,
                                           getNote() ,
                                           getFlagScarcerareScarcerato() ,
                                           getFlagPresentanteIstanza() ,
                                           getCodContenutoDecreto() ,
                                           "",
                                           getCodOggettoProcedimento() ,
                                           "",
                                           getFlagDataInterruzioneInvalid() ,
                                           getProtocollo() ,
                                           getAltraAutorita() ,
                                           getAltroLuogo() ,
                                           getIdEventoGenerato() ,
                                           getFlagElaborato() ,
                                           getDataEspulsione() ,
                                           getFlagDecisioneTribunale() ,
                                           getCodEsito() ,
                                           "",
                                           getNumAnniRinvio() ,
                                           getNumMesiRinvio() ,
                                           getNumGiorniRinvio() ,
                                           getCodOperatoreInserimento() ,
                                           getCodUfficioInserimento() ,
                                           "",
                                           getDataInserimento() ,
                                           getCodOperatoreAggiornamento() ,
                                           getCodUfficioAggiornamento() ,
                                           "",
                                           getDataAggiornamento() ,
                                           getDataRevocaSospensione() ,
								                           getFasSieIdFascicoloSiep(),
								           getAnnoRegGen(),
								           getNumeroRegGen(),
								           getTipoRegGen()
								                          );
  }

  public void setDAOFromModel(DecretoOrdinanzaSiepModel aModel) throws DAOException
  {
    setIdDecretoOrdinanzaSiep( aModel.getIdDecretoOrdinanzaSiep() );
    setDataRicezioneProvvedimento( aModel.getDataRicezioneProvvedimento() );
    setDataEmissioneProvvedimento( aModel.getDataEmissioneProvvedimento() );
    setCodTipoRegistroOrdinanza( aModel.getCodTipoRegistroOrdinanza() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setNumRegistro( aModel.getNumRegistro() );
    setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
    setNumProvvedimento( aModel.getNumProvvedimento() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setDataSospensioneEsecuzione( aModel.getDataSospensioneEsecuzione() );
    setDataDifferimento( aModel.getDataDifferimento() );
    setDataRinvio( aModel.getDataRinvio() );
    setDataFineInterruzione( aModel.getDataFineInterruzione() );
    setDataDepositoIstanza( aModel.getDataDepositoIstanza() );
    setDataInterruzionePena( aModel.getDataInterruzionePena() );
    setCodOggettoDecisione( aModel.getCodOggettoDecisione() );
    setMotivazioni( aModel.getMotivazioni() );
    setNote( aModel.getNote() );
    setFlagScarcerareScarcerato( aModel.getFlagScarcerareScarcerato() );
    setFlagPresentanteIstanza( aModel.getFlagPresentanteIstanza() );
    setCodContenutoDecreto( aModel.getCodContenutoDecreto() );
    setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
    setFlagDataInterruzioneInvalid( aModel.getFlagDataInterruzioneInvalid() );
    setProtocollo( aModel.getProtocollo() );
    setAltraAutorita( aModel.getAltraAutorita() );
    setAltroLuogo( aModel.getAltroLuogo() );
    setIdEventoGenerato( aModel.getIdEventoGenerato() );
    setFlagElaborato( aModel.getFlagElaborato() );
    setDataEspulsione( aModel.getDataEspulsione() );
    setFlagDecisioneTribunale( aModel.getFlagDecisioneTribunale() );
    setCodEsito( aModel.getCodEsito() );
    setNumAnniRinvio( aModel.getNumAnniRinvio() );
    setNumMesiRinvio( aModel.getNumMesiRinvio() );
    setNumGiorniRinvio( aModel.getNumGiorniRinvio() );

    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    setDataRevocaSospensione( aModel.getDataRevocaSospensione() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    
    setAnnoRegGen( aModel.getAnnoRegGen() );
    setNumeroRegGen( aModel.getNumeroRegGen() );
    setTipoRegGen(aModel.getTipoRegGen() );
  }

  public void setDAOFromModelForUpdate(DecretoOrdinanzaSiepModel aModel) throws DAOException
  {
    //setIdDecretoOrdinanzaSiep( aModel.getIdDecretoOrdinanzaSiep() );
    setDataRicezioneProvvedimento( aModel.getDataRicezioneProvvedimento() );
    setDataEmissioneProvvedimento( aModel.getDataEmissioneProvvedimento() );
    setCodTipoRegistroOrdinanza( aModel.getCodTipoRegistroOrdinanza() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setNumRegistro( aModel.getNumRegistro() );
    setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
    setNumProvvedimento( aModel.getNumProvvedimento() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setDataSospensioneEsecuzione( aModel.getDataSospensioneEsecuzione() );
    setDataDifferimento( aModel.getDataDifferimento() );
    setDataRinvio( aModel.getDataRinvio() );
    setDataFineInterruzione( aModel.getDataFineInterruzione() );
    setDataDepositoIstanza( aModel.getDataDepositoIstanza() );
    setDataInterruzionePena( aModel.getDataInterruzionePena() );
    setCodOggettoDecisione( aModel.getCodOggettoDecisione() );
    setMotivazioni( aModel.getMotivazioni() );
    setNote( aModel.getNote() );
    setFlagScarcerareScarcerato( aModel.getFlagScarcerareScarcerato() );
    setFlagPresentanteIstanza( aModel.getFlagPresentanteIstanza() );
    setCodContenutoDecreto( aModel.getCodContenutoDecreto() );
    setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
    setFlagDataInterruzioneInvalid( aModel.getFlagDataInterruzioneInvalid() );
    setProtocollo( aModel.getProtocollo() );
    setAltraAutorita( aModel.getAltraAutorita() );
    setAltroLuogo( aModel.getAltroLuogo() );
    setIdEventoGenerato( aModel.getIdEventoGenerato() );
    setFlagElaborato( aModel.getFlagElaborato() );
    setDataEspulsione( aModel.getDataEspulsione() );
    setFlagDecisioneTribunale( aModel.getFlagDecisioneTribunale() );
    setCodEsito( aModel.getCodEsito() );
    setNumAnniRinvio( aModel.getNumAnniRinvio() );
    setNumMesiRinvio( aModel.getNumMesiRinvio() );
    setNumGiorniRinvio( aModel.getNumGiorniRinvio() );
    setDataRevocaSospensione( aModel.getDataRevocaSospensione() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );

    setCondizioneUpdate(aModel.getIdDecretoOrdinanzaSiep());
  }

  public void setDAOFromModelForUpdateSospensione(DecretoOrdinanzaSiepModel aModel) throws DAOException
  {
    //setIdDecretoOrdinanzaSiep( aModel.getIdDecretoOrdinanzaSiep() );
    setDataRicezioneProvvedimento( aModel.getDataRicezioneProvvedimento() );
    setDataEmissioneProvvedimento( aModel.getDataEmissioneProvvedimento() );
    setCodTipoRegistroOrdinanza( aModel.getCodTipoRegistroOrdinanza() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setNumRegistro( aModel.getNumRegistro() );
    setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
    setNumProvvedimento( aModel.getNumProvvedimento() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setDataSospensioneEsecuzione( aModel.getDataSospensioneEsecuzione() );
    //setDataDifferimento( aModel.getDataDifferimento() );
    //setDataRinvio( aModel.getDataRinvio() );
    //setDataFineInterruzione( aModel.getDataFineInterruzione() );
    //setDataDepositoIstanza( aModel.getDataDepositoIstanza() );
    //setDataInterruzionePena( aModel.getDataInterruzionePena() );
    setCodOggettoDecisione( aModel.getCodOggettoDecisione() );
    setMotivazioni( aModel.getMotivazioni() );
    //setNote( aModel.getNote() );
    setFlagScarcerareScarcerato( aModel.getFlagScarcerareScarcerato() );
    //setFlagPresentanteIstanza( aModel.getFlagPresentanteIstanza() );
    //setCodContenutoDecreto( aModel.getCodContenutoDecreto() );
    setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
    //setFlagDataInterruzioneInvalid( aModel.getFlagDataInterruzioneInvalid() );
    //setProtocollo( aModel.getProtocollo() );
    //setAltraAutorita( aModel.getAltraAutorita() );
    //setAltroLuogo( aModel.getAltroLuogo() );
    //setIdEventoGenerato( aModel.getIdEventoGenerato() );
    //setFlagElaborato( aModel.getFlagElaborato() );
    //setDataEspulsione( aModel.getDataEspulsione() );
    //setFlagDecisioneTribunale( aModel.getFlagDecisioneTribunale() );
    setCodEsito( aModel.getCodEsito() );
    //setNumAnniRinvio( aModel.getNumAnniRinvio() );
    //setNumMesiRinvio( aModel.getNumMesiRinvio() );
    //setNumGiorniRinvio( aModel.getNumGiorniRinvio() );

    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    //setDataRevocaSospensione( aModel.getDataRevocaSospensione() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

    setCondizioneUpdate(aModel.getIdDecretoOrdinanzaSiep());
  }

  public void setCondizione(DecretoOrdinanzaSiepModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_DECRETO_ORDINANZA_SIEP = " + key );
  }

	public void setCondizioneByIdDecretoOrdinanzaFlagNonElaborato(BigDecimal key)
  {
    setCondition(" ID_DECRETO_ORDINANZA_SIEP = " + key +" AND (FLAG_ELABORATO='N' OR FLAG_ELABORATO IS NULL)");
  }

  public void setCondizioneByIdFascicoloSiepFlagNonElaborato(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo +" AND (FLAG_ELABORATO='N' OR FLAG_ELABORATO IS NULL)");
  }
}
