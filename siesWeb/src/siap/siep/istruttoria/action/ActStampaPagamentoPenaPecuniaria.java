package siap.siep.istruttoria.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaPagamentoPenaPecuniaria</p>
 * <p>Description: Stampa un Estratto della Sentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaPagamentoPenaPecuniaria extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
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
     lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     lEveMod.setNomeTemplate(TEMPLATE_PAGAMENTO_PENA_PECUNIARIA);
     lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
     lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
     //-------
     IIstruttoria lCtrlIstruttoria = SIEPLookupRemote.getIstruttoriaRemote();
    //rtf
     ByteArrayOutputStream lReport = lCtrlIstruttoria.ExStampaIstruttoria(lEveMod, lUtenteMod);	 // setta la risposta nella request

      //Prepara la pagina di destinazione

     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}