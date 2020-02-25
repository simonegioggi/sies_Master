package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaEmissioneProvvedimento extends ActionSiap implements ICostantiOrdineEsecuzione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);

     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();

     //String lCodMotivo = this.getRequestStringParameter("codmotivo");
     EventoModel lEventoMod = new EventoModel();

     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     lEventoMod = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

     if(lEventoMod == null)
       throw new F3BException(F3BException.USER_MESSAGE, "Evento non Registrato");

   //ricerca posizione giuridica
    PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

   //ricerca pena residua
   PenaResiduaModel lPenaResMod = new PenaResiduaModel();
   IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
   lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

   //ricerca template
     TemplateModel lTempMod = new TemplateModel();
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     String lIdTempalte = "";
     if(lFascicoloModel.getFlagAltraCausa()!= null && lFascicoloModel.getFlagAltraCausa().equals("S"))
     {
       String lFlagTempalte = "";
       if(lPenaResMod != null && lPenaResMod.getDataInizio() != null)
       {
         lFlagTempalte = "1";
       }else
       {
         lFlagTempalte = "0";
       }
       lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoMod.getCodTipoEvento(),lEventoMod.getCodTipoProvvedimento(),lEventoMod.getCodMotivo(),lFlagTempalte);
       lIdTempalte = lTempMod.getIdTemplate();
     }else
     {
        if((lPos!=null && lPos.getCodPosizioneGiuridica()!=null) &&
          (lPos.getCodPosizioneGiuridica().equals("07") || lPos.getCodPosizioneGiuridica().equals("10") ||
           lPos.getCodPosizioneGiuridica().equals("01") || lPos.getCodPosizioneGiuridica().equals("03") ||
           lPos.getCodPosizioneGiuridica().equals("02") ||lPos.getCodPosizioneGiuridica().equals("04") ||
           lPos.getCodPosizioneGiuridica().equals("16") ||lPos.getCodPosizioneGiuridica().equals("20") ||
           lPos.getCodPosizioneGiuridica().equals("46") ||lPos.getCodPosizioneGiuridica().equals("47")
           ))
         {
           lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoMod.getCodTipoEvento(),lEventoMod.getCodTipoProvvedimento(),lEventoMod.getCodMotivo(),null);
           lIdTempalte = lTempMod.getIdTemplate();
         }else
         {
           lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null,null,"0000","Z");
           lIdTempalte = lTempMod.getIdTemplate();
         }
      }

     EventoNotificaModel lEveMod = new EventoNotificaModel();

     //lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

     lEventoMod.setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
     lEventoMod.setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());
     lEventoMod.setDataAggiornamento(DateUtils.getSysDate());
     lEventoMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lEventoMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
     lEventoMod.setFlagDocumentoRegistrato("N");
     lEveMod.setEvento(lEventoMod);
     lEveMod.setNomeTemplate(lIdTempalte);

     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Model EventoNotifica ma evento = "+lEveMod.getEvento().toString());
 		// ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod);		 // setta la risposta nella request

      IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
      ByteArrayOutputStream lReport = lCtrlAnn.ExStampaDocumentoXAnnotazioni(lEveMod, lUtenteMod); // setta la risposta nella request

    //Prepara la pagina di destinazione
     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}