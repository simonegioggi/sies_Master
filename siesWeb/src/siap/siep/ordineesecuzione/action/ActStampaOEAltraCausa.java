package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
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

public class ActStampaOEAltraCausa extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();

     EventoNotificaModel lEveMod = new EventoNotificaModel();

     lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

     lEveMod.getEvento().setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
     lEveMod.getEvento().setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());
     lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
     lEveMod.getEvento().setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lEveMod.getEvento().setCodOperatoreAggiornamento(lUtenteMod.getUserId());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     // ** COMMENTATO CAUSA REWORK del 17.06.2003      **
     // ** il campo DATA (ALTRA_CAUSA) si trova sulla tabella ALTRA_CAUSA **
/*
		 if (lFascicoloModel.getDataAltraCausa() == null)
			 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_D);
		 else
		   lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_C);
*/
     
     // MEV 10 S3
     // Per i cod_motivo (5534,5535,5536,5537) è stato richiesto dal Referente 
     // (Michele Testa) l'apertura del template SIEP_OE_ARRD.RTF 
     IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
     EventoModel lEveOrd = lCtrlEve.ExRicercaEventoByKey(new BigDecimal(lId));
     String codMotivo = lEveOrd.getCodMotivo();
     if(codMotivo != null && (codMotivo.equals("5534") || codMotivo.equals("5535") ||
    	codMotivo.equals("5536") || codMotivo.equals("5537")) ){
    	 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ARRESTI_DOMICILIARI);
     } else {
	     IAltraCausa lACtrl = SIEPLookupRemote.getAltraCausa();
		 AltraCausaModel  lAltraMod = lACtrl.ExRicercaAltraCausaByFascicolo(lFascicoloModel.getIdFascicoloSiep());
	
		 if (lAltraMod != null)
		 {
			 if ((lAltraMod.getDataScadenza() != null) && (lAltraMod.getDataDecorrenza() != null) )
				 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_D);
			 else
				 lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_C);
		 }
		 else {
		   lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_ALTRA_CAUSA_C);
		 }
     }
	 
     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
 	 ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);		 // setta la risposta nella request

	 //Prepara la pagina di destinazione
     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}
