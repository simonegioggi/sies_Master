package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaMADetDomSpecAmmiPeriodo</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMADetDomSpecAmmiPeriodo
    extends ActionSiap
    implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);
    String lMotivo = lEventoModel.getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(lIdEvento);
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    // Cerca la MA per ID_EVENTO del PROVVEDIMENTO
    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

    // A secondo del COD_MOTIVO e dell'ufficio che ha registrato la MA (PROC, SORV)
    // chiama il TEMPLATE corrispondente
    if (lMotivo.equals("0012"))
    {
      if (   lMisAlMod != null
          && lMisAlMod.getCodTipoUfficioScarcerazione() != null
          && lMisAlMod.getCodTipoUfficioScarcerazione().equals("SORV"))//** TDS **
      {
        lEveMod.setNomeTemplate(TEMPLATE_DDS_MOTIVO_0012_BIS);
      }
      else
      {
        lEveMod.setNomeTemplate(TEMPLATE_DDS_MOTIVO_0012);
      }
    }
    else
    {
      lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
    }

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}