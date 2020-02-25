package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
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

public class ActStampaOEDetenutoQC extends ActionSiap
                                   implements ICostantiOrdineEsecuzione
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
     lEveMod.setNomeTemplate(TEMPLATE_ORDINE_ESECUZIONE_CONDANNATO_DETENUTO_QC);

     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);		 // setta la risposta nella request

		 //Prepara la pagina di destinazione
     //if (lReport != null)
     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}
