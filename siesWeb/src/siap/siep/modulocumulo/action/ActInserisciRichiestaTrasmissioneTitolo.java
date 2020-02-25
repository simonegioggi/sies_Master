package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.action.ICostantiCompetenza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: Action per l'inserimento della Richiesta ad altro ufficio della 
 *        trasmissione atti ai fini dell'assorbimento in cumulo
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRichiestaTrasmissioneTitolo extends ActionSiap implements ICostantiModuloCumulo 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception 
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoNotificaModel lEveNot = new EventoNotificaModel();

    // setto il tipo provvedimento

    lEveNot.getEvento().setCodTipoEvento        ("01"); //
    lEveNot.getEvento().setCodTipoProvvedimento (getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
    lEveNot.getEvento().setCodMotivo            (getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

    lEveNot.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    
    // Data Emissione
    Date lDataEmissione = getRequestDateParameter (ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                   ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                   ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
    lEveNot.getEvento().setDataEmissione(lDataEmissione);
    
    // Data Trasmissione
    Date lDataTrasmissione = getRequestDateParameter (ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
                                                      ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
                                                      ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
    lEveNot.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

    // Magistratodel firmatario
    lEveNot.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

    UfficioModel lUff = this.getUfficioUtenteConnesso();

    
    lEveNot.getEvento().setCodLuogoEmittente   (lUff.getCodComune());
    lEveNot.getEvento().setCodUfficioEmittente (lUff.getCodUfficio());
    lEveNot.getEvento().setAnnoProtocollo      (new BigDecimal(DateUtils.getSysDate("yyyy")));
    
    lEveNot.getEvento().setFlagStampaSiep ("S");
    lEveNot.getEvento().setFlagVideoSiep  ("S");
    lEveNot.getEvento().setFlagDocumentoRegistrato("N");
    
    lEveNot.getEvento().setCodEsito                   ("-");
    lEveNot.getEvento().setCodLuogoDestinatario       ("-");
    lEveNot.getEvento().setCodTipoUfficioDestinatario ("-");

    lEveNot.getEvento().setCodOperatoreInserimento (getCodUtenteConnesso());
    lEveNot.getEvento().setCodUfficioInserimento   (lUff.getCodUfficio());
    lEveNot.getEvento().setDataInserimento         (DateUtils.getSysDate());
    

    // Imposto il campo contenuto nella tabella CampoNote
    if (   !this.isRequestParameterNullObj("camponote")
        && !getRequestStringParameter("camponote").equals("")
       ) 
    {
      ArrayList <CampoNotaModel>lCampoNote = new ArrayList <CampoNotaModel>();
      
      CampoNotaModel lCampMod = new CampoNotaModel();
      
      lCampMod.setDescr (getRequestStringParameter("camponote"));

      lCampMod.setCodOperatoreInserimento (getCodUtenteConnesso());
      lCampMod.setCodUfficioInserimento   (lUff.getCodUfficio());
      lCampMod.setDataInserimento         (DateUtils.getSysDate());
      
      lCampoNote.add(lCampMod);

      lEveNot.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
    }



    //==========================================================================
    // Dati del provvedimento richiesto (tabella COMPETENZA)
    //==========================================================================
    CompetenzaModel lComp = new CompetenzaModel();   
  
    if (   !this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO)
        && !"".equals(getRequestStringParameter(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO))
       ) 
    {        
      // fascicolo e sentenza provenienti da ricerca nel distretto
      IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel findedFasc = lFasCtrl.ExRicercaFascicoloByKey(getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO));
      
      ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
      SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(findedFasc.getSenIdSentenza());

      // Estremi del titolo richiesto (sentenza/decreto penale)
      lComp.setCodTipoProvvedimento        (aSent.getCodTipoProvvedimento());
      lComp.setAnnoSentenza                (aSent.getAnnoSentenza());
      lComp.setNumeroSentenza              (aSent.getNumeroSentenza());     
      lComp.setDataProvvedimento           (aSent.getDataProvvedimento());
      lComp.setDataIrrevocabilita          (findedFasc.getDataIrrevocabilita()); // dal fascicolo
      lComp.setCodTipoAutoritaEmittente    (aSent.getCodTipoAutoritaEmittente());
      lComp.setCodLuogoEmittente           (aSent.getCodLuogoEmittente());
      lComp.setNumSezioneAutoritaEmittente (aSent.getNumSezioneAutoritaEmittente());      
      lComp.setSenIdSentenza               (aSent.getIdSentenza());
      
      // Estremi del procedimento richiesto (fascicolo)
      lComp.setFasSieIdFascicoloSiep (findedFasc.getIdFascicoloSiep());
      lComp.setChiaveAnno            (findedFasc.getChiaveAnno());
      lComp.setChiaveProgr           (findedFasc.getChiaveProgr());
      lComp.setChiaveUfficio         (findedFasc.getChiaveUfficio());
      
      if (findedFasc.getChiaveProgrOrig()!=null) {
        lComp.setFlagAccorpato("S");
        lComp.setChiaveProgrOrigine (findedFasc.getChiaveProgrOrig());
        
        UfficioAccorpatoModel lUffOrig = getUfficioAccorpatoByCodAccorpanteProgr (findedFasc.getChiaveUfficio(), findedFasc.getChiaveProgr());
        
        lComp.setChiaveUfficioOrigine ( lUffOrig.getCodUfficio() );
      }
      else {
        lComp.setFlagAccorpato("N");        
      }
      
      
      
      // Ufficio Competente
      IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
      UfficioModel mUffComp = lCtrlUff.ExRicercaUfficioByCod(findedFasc.getChiaveUfficio());
      
      lComp.setCodTipoAutoritaComp    (mUffComp.getCodTipoUfficio());
      lComp.setCodLuogoAutoritaComp   (mUffComp.getCodComune());
      lComp.setCodUfficioAutoritaComp (findedFasc.getChiaveUfficio());

    }
    else
    {
      // fascicolo e sentenza inputati manualmente in questo caso non tutti i 
      // dati sono necessariamente presenti. In particolare i dati del procedimento
      // (fascicolo) non è detto che siano presenti, anno e numero non sono obbligatori
      
      // Estremi del titolo da assorbire (sentenza)
      lComp.setCodTipoProvvedimento (getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO));
      lComp.setAnnoSentenza         (getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_ANNO_SENTENZA));
      lComp.setNumeroSentenza       (getRequestStringParameter(ICostantiCompetenza.CAMPO_NUMERO_SENTENZA)); 
      
      lComp.setDataProvvedimento ( getRequestDateParameter ( ICostantiCompetenza.CAMPO_ANNO_DATA_PROVVEDIMENTO, 
                                                             ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO, 
                                                             ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
                                                           );
      lComp.setDataIrrevocabilita ( getRequestDateParameter ( ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA, 
                                                              ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA, 
                                                              ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA)
                                                            );
      
      lComp.setCodTipoAutoritaEmittente (getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
      ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA)));
      lComp.setCodLuogoEmittente(lComModAutEmi.getCodComune());
      lComp.setNumSezioneAutoritaEmittente (getRequestStringParameter(ICostantiCompetenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
            
      // Estremi del procedimento (fascicolo): Anno/Numero/Ufficio
      // n.b. non obbligatori l'utente potrebbe non conoscere quale sia il procedimento
      //      di esecuzione (anno/numero) ma solo gli estremi del titolo 
      lComp.setChiaveAnno  (getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_ANNO));
      lComp.setChiaveProgr (getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_PROGR));
      
      //
      String lCodiceUffEsec = getCodUfficioByCodTipoUfficioDescrComune(
                          getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO), 
                          getRequestStringParameter(ICostantiCompetenza.CAMPO_SEDE_UFFICIO));
      lComp.setChiaveUfficio (lCodiceUffEsec);
      
      if (lComp.getChiaveProgr()!=null){
        if (   !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)
            && !"0".equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) 
            && !"".equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) 
           )
        { 
          BigDecimal lIncrementoAccorpato = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
          BigDecimal lChiaveProgrAccorpato = lComp.getChiaveProgr().add(lIncrementoAccorpato);
          
          lComp.setChiaveProgr(lChiaveProgrAccorpato);
          
          lComp.setFlagAccorpato("S");
          lComp.setChiaveProgrOrigine (getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_PROGR));
          
          UfficioModel lUffOrig = getUfficioAccorpatoByCodAccorpanteIncrement (lComp.getChiaveUfficio(), ""+lIncrementoAccorpato);
          lComp.setChiaveUfficioOrigine (lUffOrig.getCodUfficio());
        }
      }   

      
      // UFFICIO COMPETENZA: sono IO
      lComp.setCodTipoAutoritaComp    (getUfficioUtenteConnesso().getCodTipoUfficio()); //not null
      lComp.setCodLuogoAutoritaComp   (getUfficioUtenteConnesso().getCodComune());      //not null
      lComp.setCodUfficioAutoritaComp (getUfficioUtenteConnesso().getCodUfficio());     

    }      
    
    //==========================================================================
    // Preparo le notifiche
    //==========================================================================
    ArrayList <NotificaModel> lNotifiche = new ArrayList <NotificaModel>();

    // Notifica all'ufficio a cui si richiedono gli atti
    NotificaModel lNotUffComp = new NotificaModel();
    
    lNotUffComp.setCodTipoNotifica ("N");
    lNotUffComp.setDataInvio (lDataTrasmissione);
    lNotUffComp.setCodEsito("-");   
    
    //
    lNotUffComp.setUffCodUfficio (lComp.getChiaveUfficio());
    
    lNotUffComp.setCodOperatoreInserimento (getCodUtenteConnesso());
    lNotUffComp.setDataInserimento         (DateUtils.getSysDate());
    lNotUffComp.setCodUfficioInserimento   (lUff.getCodUfficio());
  
    lNotifiche.add(lNotUffComp);    
    
    
    // ALTRO DESTINATARIO
    if (   !isRequestParameterNullObj("AltroDestinatario")
        && !getRequestStringParameter("AltroDestinatario").equals("-")) 
    {
      NotificaModel lNotAltroDestinatario = new NotificaModel();
      
      lNotAltroDestinatario.setCodTipoNotifica ("E");  // ???? Perchè E dovrebbe essere N o C
      lNotAltroDestinatario.setDataInvio (lDataTrasmissione);
      lNotAltroDestinatario.setCodEsito  ("-");
      
      lNotAltroDestinatario.setCodOperatoreInserimento (getCodUtenteConnesso());
      lNotAltroDestinatario.setDataInserimento         (DateUtils.getSysDate());
      lNotAltroDestinatario.setCodUfficioInserimento   (lUff.getCodUfficio());
      
      
      AutoritaEsternaModel lAut = new AutoritaEsternaModel();
      lAut.setCodTipoAutorita (getRequestStringParameter("AltroDestinatario"));

      ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(getRequestStringParameter("SedeAltroDestinatario")));

      lAut.setCodSede (lComMod.getCodComune());
      
      lAut.setCodOperatoreInserimento (getCodUtenteConnesso());
      lAut.setCodUfficioInserimento   (lUff.getCodUfficio());
      lAut.setDataInserimento         (DateUtils.getSysDate());

      //Setto l'Autorita Esterna per la notifica corrente
      lNotAltroDestinatario.setAutoritaEsterna(lAut);

      lNotifiche.add(lNotAltroDestinatario);
    }
    

    
    // Inserisco l'array di Notifiche nell'Evento
    lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lComp = "+lComp);
    
    //==========================================================================
    // Inserimento Evento
    //==========================================================================  
    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();   
    EventoNotificaModel lRetModel = lCompCtrl.ExInserisciRichiestaTrasmissioneAtti(lEveNot,lComp);
    

    // ==================

    String lPage = IWebConstants.PG_MAIN+ "?"+ IWebConstants.ACTION_FIELD
      + "=siap.siep.modulocumulo.action.ActDettaglioRichiestaTrasmissioneTitolo&"
      + ICostantiEvento.CAMPO_ID_EVENTO + "="+ lRetModel.getEvento().getIdEvento();

    
    return lPage;
  }
}