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
 * <p>Title: ActStampaInizioEsecuzione</p>
 * <p>Description: Stampa Inizio esecuzione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaInizioEsecuzione extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
  {     
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     String idevento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
     String codmotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
     UtenteModel lUtenteMod = this.getUtenteConnesso();
     UfficioModel lUff = this.getUfficioUtenteConnesso();

     EventoNotificaModel lEveMod = new EventoNotificaModel();

     lEveMod.getEvento().setIdEvento(new BigDecimal(idevento));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

     lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
     lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
     lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
     lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
     lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
     lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
   
     if(codmotivo.equals("0047"))
    	 lEveMod.setNomeTemplate(TEMPLATE_INIZIO_ESECUZIONE);
     else
    	 lEveMod.setNomeTemplate(TEMPLATE_ESECUZIONE_SENTENZA);
     
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