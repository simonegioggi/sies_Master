package siap.siep.sospensione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaDifferimento</p>
 * <p>Description: Classe Action per la Stampa dei documento legati ai Diferimenti </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActStampaDifferimento extends ActLoadInserisciDifferimentoMaster
                                implements ICostantiSospensione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel  lUtenteMod = this.getUtenteConnesso();  
    UfficioModel lUff       = this.getUfficioUtenteConnesso(); 

    String lId = getRequestStringParameter("IdEvento");

    //==========================================================================
    // Recupero l'evento per il quale produrre la Stampa
    //==========================================================================
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

    //==========================================================================
    // Recupero i dati della Misura Alternativa e Posizione Giuridica
    // per poter determinare il flagTemplate  
    //==========================================================================
    BigDecimal lIdEventoProvvSorv = lEventoModel.getEveIdEvento();

    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisAltDiff = null;
    lMisAltDiff = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoProvvSorv);
    
    //==========================================================================
    // Posizione giuridica
    //==========================================================================
    PosizioneGiuridicaModel lPos = null;
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
    
    //==========================================================================
    // Determino il flag template. 
    // Il flag Template mi differenzia tra documenti che hanno la stesso
    // COD_TIPO_EVENTO, COD_TIPO_PROVVEDIMENTO, COD_MOTIVO, ma che dipendono da
    // un altro parametro quale per esempio la posizione giuridica.
    //==========================================================================
    String flagTemplate = null;
    flagTemplate = getFlagTemplate(lMisAltDiff, lPos);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("flagTemplate = " +flagTemplate);
    
    //==========================================================================
    // Recupero i dati del TEMPLATE previsto per l'evento (TIPO_EVENTO, 
    // TIPO_PROVVEDIMENTO, MOTIVO) e il flag template
    //==========================================================================
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    try {
      lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate( lEventoModel.getCodTipoEvento(), 
                                                                                 lEventoModel.getCodTipoProvvedimento(), 
                                                                                 lEventoModel.getCodMotivo(), 
                                                                                 flagTemplate);
    } 
    catch (F3BException e) 
    {
      if (e.getErrorCode()==F3BException.USER_MESSAGE){
        // nessun elemento trovato
        // provvedimento generico a causa di posizione giuridica nn gestita
        // in questo caso viene inserito un provvedimento (04) al posto di 
        // 09 o 12 per cui non è prevista una stampa
        lTemMod.setIdTemplate("SIEP_VUOTO");
      }
      else {
        throw e; // eccezione di altra natura, la rilancio
      }
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Template Model = "+lTemMod);

//------------------------------------------------------------------------------    
    //==========================================================================
    // Genero il model da passare alla funzione di stampa
    //==========================================================================
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente   (lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente (lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento        (DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento  (lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");
    
    // Imposto l'id del template d autilizzare 
    lEveMod.setNomeTemplate( lTemMod.getIdTemplate());
    
    //==========================================================================
    // Genero la stampa
    //==========================================================================
    ISospensione lCtrSosp = SIEPLookupRemote.getSospensioneRemote();
    ByteArrayOutputStream lReport = lCtrSosp.ExStampaDocumentoDifferimento(lEveMod, lUtenteMod); 
    
    // setta la risposta nella request
    setRequestAttribute("report", lReport);

    // Richiama la pagina per effettuare l'Upload Del Documento Generato
    return IWebConstants.PG_DOWNLOAD;
    //return IWebConstants.ROOT_DIR + "/PippoPippo.jsp";
  }
  
  /*****************************************************************************
   * Restituisce il flagTemplate in funzione del tipo Provvedimento (differimento
   * provvisorio, definitivo, revoca, rigetto), della posizione giuridica e
   * del flag Da scarcerare/già scarcerato   
   * @param aTipoProvvedimento
   * @param aMisAltDiff
   * @param aPos
   * @return
   ************************************************************************** */
  private String getFlagTemplate (MisuraAlternativaModel aMisAltDiff,
                                  PosizioneGiuridicaModel aPos){
    String flagTemplate = "";

    String tipoProvvedimento = getTipoProvvedimento(aMisAltDiff);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("aMisAltDiff = "+aMisAltDiff);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("aPos = "+aPos);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("tipoProvvedimento = "+tipoProvvedimento);
    
    
    if ( tipoProvvedimento.equals(DIFFERIMENTO_PROV) ) {
      // Prevede tre casi
      // - Libero
      // - Detenuto, Arresti Domiciliari, Misura Alternativa - DA SCARCERARE
      // - Detenuto, Arresti Domiciliari, Misura Alternativa - GIA' SCARCERATO
      if (   aMisAltDiff.getCodTipoUfficioScarcerazione()==null 
          || aMisAltDiff.getCodTipoUfficioScarcerazione().equals("") 
          || aMisAltDiff.getCodTipoUfficioScarcerazione().equals("-") ) {
        flagTemplate = "0" ; //libero
      }
      else if ( aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV") ) {
        // Già Scarcerato
        flagTemplate = "1" ; // Detenuto, Arresti Domiciliari, Misura Alternativa
      }
      else if ( aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC") ) {
        // Da Scarcerare
        flagTemplate = "0" ; // Detenuto, Arresti Domiciliari, Misura Alternativa
      }
    }
    else if ( tipoProvvedimento.equals(DIFFERIMENTO_DEF) ) {
      if ( aPos.isLibero() ) {
        // Libero
        flagTemplate = "0" ; //libero
      }
      else if (   aPos.getCodPosizioneGiuridica().equals("03")
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC")) {
        flagTemplate = "1" ; // Detenuto da Scarcerare
      }
      else if (   aPos.getCodPosizioneGiuridica().equals("03")
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV") ) {
        flagTemplate = "2" ; // Detenuto già Scarcerato 
      }
      else if (   isMisAlt(aPos)
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC")) {
        flagTemplate = "3" ; // In Misura Alternativa da Scarcerare
      }
      else if (   isMisAlt(aPos)
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV")) {
        flagTemplate = "4" ; // In Misura Alternativa già Scarcerato 
      }
      else if (   aPos.getCodPosizioneGiuridica().equals("04")
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC")) {
        flagTemplate = "5" ; // Arresti Domiciliari da Scarcerare
      }
      else if (   aPos.getCodPosizioneGiuridica().equals("04")
               && aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV")) {
        flagTemplate = "6" ; // Arresti Domiciliari già Scarcerato 
      }
    }
    else if ( tipoProvvedimento.equals(DIFFERIMENTO_RIGETTO) ) {
      flagTemplate = "0" ;
    }
    else if ( tipoProvvedimento.equals(DIFFERIMENTO_REVOCA) ) {
      flagTemplate = "0" ;
    }
    
    return flagTemplate ;
  }
}