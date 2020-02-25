package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
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
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaMACessazione</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMACessazione
    extends ActionSiap
    implements ICostantiMisuraAlternativa
{

  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosMod = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());


    String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lIdEvento));
    String lMotivo = lEventoModel.getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lIdEvento));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    //==========================================================================
    // Recupero la MA
    //==========================================================================
    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisALtModel = null;
    lMisALtModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());
    
    String flagTemplate = null;
    if(lPosMod != null && lPosMod.getCodPosizioneGiuridica()!= null)
    {
     if(   lPosMod.getCodPosizioneGiuridica().equals("31") || lPosMod.getCodPosizioneGiuridica().equals("33") // 51 ter
        || lPosMod.getCodPosizioneGiuridica().equals("36") || lPosMod.getCodPosizioneGiuridica().equals("38") // 51 bis
       )
     {
       flagTemplate = "2"; // IN SOSPENSIONE (tutte le misure)
     }
     else if(lPosMod.getCodPosizioneGiuridica().equals("12") || lPosMod.getCodPosizioneGiuridica().equals("14"))
     {
        flagTemplate = "3"; // IN MISURA
        if (lMisALtModel!=null && "SORV".equals(lMisALtModel.getCodTipoUfficioScarcerazione()) && lPosMod.getCodPosizioneGiuridica().equals("12"))
          flagTemplate = "5"; // IN MISURA ESEGUE SORVEGLIANZA (soggetto già detenuto) solo DETENZIONE DOMICILIARE
     }
     else if(lPosMod.getCodPosizioneGiuridica().equals("29"))
     {
        flagTemplate = "4"; // 29 - Detenzione Domiciliare Provvisoria
     }    
     else if(lPosMod.getCodPosizioneGiuridica().equals("07") || lPosMod.getCodPosizioneGiuridica().equals("10"))
     {
        flagTemplate = "0"; // Libero
     }
    }

    if(flagTemplate != null && lMotivo!= null && !lMotivo.equals("0000"))
    {
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     TemplateModel lTemMod = new TemplateModel();
     lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01","03",lMotivo,flagTemplate);
     lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    }else
     {
       lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
     }

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

//Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}