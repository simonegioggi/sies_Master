package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciAnnProvCumulo</p>
* <p>Description: Classe Action per l'inserimento di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciAnnProvCumulo extends ActArchiviazione implements ICostantiArchiviazione

{
  public String processRequest() throws F3BException
 	{
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    String lUfficio = null;
    String lSedeUfficio = null;
    if(!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE))
      lUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE);
    else
      lUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE_STESSA);

    if(!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE))
      lSedeUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE);
    else
      lSedeUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE_STESSA);

    String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio,lSedeUfficio);

    ComuneModel lCumMod = this.getCodComuneByDescr(lSedeUfficio);

//evento notifica da passare al metodo di inserimento
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();

//evento
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01");
    lEveMod.setCodTipoProvvedimento("25");
    lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
    lEveMod.setDataEmissione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE,ICostantiFascicoloSiep.CAMPO_MESE_UNIONE,ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE));
    lEveMod.setCodUfficioEmittente(lUff);
    lEveMod.setCodLuogoEmittente(lCumMod.getCodComune());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodUfficioDestinatario("-");
    lEveMod.setCodMagistrato("-");
    lEveMod.setCodEsito("-");
    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lEveMod.setFlagVideoSiep("S");
    lEveMod.setFlagStampaSiep("S");

    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lEveMod.setDataInserimento(DateUtils.getSysDate());

    //setto l'evento dentro l'eventonotificaModel
    lEveNotMod.setEvento(lEveMod);

//archiviazione
    ArchiviazioneModel lArcMod = new ArchiviazioneModel();
    lArcMod.setCodTipoProvvedimento("22");
    lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
    lArcMod.setDataDefinizione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE,ICostantiFascicoloSiep.CAMPO_MESE_UNIONE,ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE));
    lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lArcMod.setDataInserimento(DateUtils.getSysDate());
    lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lArcMod.setNote(getRequestStringParameter(CAMPO_NOTE));

//FASCICOLO
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

    lFascicolo.setIdFascicoloSiep(lIdFascicolo);
    lFascicolo.setAnnoFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE));
    lFascicolo.setNumFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE));
    lFascicolo.setDataUnione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE,ICostantiFascicoloSiep.CAMPO_MESE_UNIONE,ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE));
    lFascicolo.setCodUfficioUnione(lUff);
    lFascicolo.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lFascicolo.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lFascicolo.setDataAggiornamento(DateUtils.getSysDate());

//inserimento
    IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
    ArchiviazioneModel lArcModRes = lCtrl.ExInserisciEventoNotificaArchiviazione(lEveNotMod,lArcMod,lFascicolo);

    String lPage = null;

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.archiviazione.action.ActLoadDettaglioAnnProvCumulo&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lArcModRes.getEveIdEvento();


    return lPage;
  }
}