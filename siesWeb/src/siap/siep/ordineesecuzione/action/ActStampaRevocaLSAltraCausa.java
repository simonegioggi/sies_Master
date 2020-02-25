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

public class ActStampaRevocaLSAltraCausa
    extends ActionSiap
    implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");
    String istanza = this.getRequestStringParameter("istanza");
    String finePena = this.getRequestStringParameter("finePena");
    if (istanza.equals("S") && (finePena.equals("N")))
    {
      lEveMod.setNomeTemplate(TEMPLATE_REVOCA_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA_ISTANZA);
    }
    else if (istanza.equals("N") && (finePena.equals("N")))
    {
      lEveMod.setNomeTemplate(TEMPLATE_REVOCA_ORDINE_ESECUZIONE_SIMEONE_ALTRA_CAUSA);
    }

    if (istanza.equals("S") && finePena.equals("S"))
    {
      lEveMod.setNomeTemplate(TEMPLATE_REVOCA_SIMEONE_ALTRA_CAUSA_ISTANZA_FINE_PENA);
    }
    else if (istanza.equals("N") && finePena.equals("S"))
    {
      lEveMod.setNomeTemplate(TEMPLATE_REVOCA_SIMEONE_ALTRA_CAUSA_FINE_PENA);
    }


    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

//Prepara la pagina di destinazione
//if (lReport != null)
    setRequestAttribute("report", lReport);
    setRequestAttribute("fc", getRequestStringParameter("fc"));

    return IWebConstants.PG_DOWNLOAD;
  }
}