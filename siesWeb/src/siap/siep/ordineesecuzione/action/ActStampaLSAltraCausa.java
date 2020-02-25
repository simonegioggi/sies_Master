package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
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

public class ActStampaLSAltraCausa extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);

     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();
  	 UfficioModel lUff = this.getUfficioUtenteConnesso();

     EventoNotificaModel lEveMod = new EventoNotificaModel();

     lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

     lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
     lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

     lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
     lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
     lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     PosizioneGiuridicaModel lPos = null;
     IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
     lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
     
     AltraCausaModel altraCausa = null;
     IAltraCausa lAltraCausa = SIEPLookupRemote.getAltraCausa();
     if (lPos != null && lPos.getAltCauIdAltraCausa() != null){
    	 altraCausa = lAltraCausa.ExRicercaAltraCausaIstitutoByKey(lPos.getAltCauIdAltraCausa());
     }
     
     if(altraCausa != null && altraCausa.getCodTipoPosGiuridica() != null &&
    	!altraCausa.getCodTipoPosGiuridica().equals("") && 
    	(altraCausa.getCodTipoPosGiuridica().equals("78") ||
    	 altraCausa.getCodTipoPosGiuridica().equals("79") ||
    	 altraCausa.getCodTipoPosGiuridica().equals("80") ||
    	 altraCausa.getCodTipoPosGiuridica().equals("81"))
    	){
    	 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA_MIS);
     } else {
    	 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA);
     }

     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
 		 ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);		 // setta la risposta nella request

		  //Prepara la pagina di destinazione
      //if (lReport != null)
     setRequestAttribute("report", lReport);
     setRequestAttribute("fc", getRequestStringParameter("fc"));

     return IWebConstants.PG_DOWNLOAD;
  }
}
