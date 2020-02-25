package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciAssorCumulo</p>
* <p>Description: Classe Action per l'inserimento di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciAssorCumulo extends ActArchiviazione implements ICostantiArchiviazione

{
  public String processRequest() throws F3BException
 	{
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//evento notifica da passare al metodo di inserimento
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();

//evento
    EventoModel lEveMod = new EventoModel();

    //commentato il 15-04-2005 -- viviana -- dario altrimenti nn si vede nello stato esecuzione,elenco prov, ecc.
    //lEveMod.setCodTipoEvento("15");        // Tipo Evento = Definizione Procedimento
    lEveMod.setCodTipoEvento("01");
    // STUB 17/10/2005 REWORK STATO ESECUZIONE
    //lEveMod.setCodTipoProvvedimento("22"); // Tipo Provvedimento = Assorbimento in cumulo
    lEveMod.setCodTipoProvvedimento("25");
    lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
    lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE));
    lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
    lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
    lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodUfficioDestinatario("-");
    lEveMod.setCodMagistrato(this.calcolaMagistrato());
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
    lArcMod.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,CAMPO_MESE_DATA_DEFINIZIONE,CAMPO_GIORNO_DATA_DEFINIZIONE));
    lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lArcMod.setDataInserimento(DateUtils.getSysDate());
    lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lArcMod.setNote(getRequestStringParameter(CAMPO_NOTE));

//notifica
    // solo se Assorbimento cumulo altro ufficio setto le notifiche
    if(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE).equals("0022"))
    {
      lEveNotMod.setNotifiche(this.loadNotifiche());
    }

//FASCICOLO
    FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

    lFascicolo.setIdFascicoloSiep(lIdFascicolo);
    lFascicolo.setAnnoFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE));
    lFascicolo.setNumFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE));
    lFascicolo.setDataUnione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE,ICostantiFascicoloSiep.CAMPO_MESE_UNIONE,ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE));

    String lUff = null; 
    if(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE).equals("0022"))
    {
     String lUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE);
     String lSedeUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE);
     lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio,lSedeUfficio);
    }
    else
    {
     lUff = this.getCodUfficioUtenteConnesso();
    }	
    	

    lFascicolo.setCodUfficioUnione(lUff);
    lFascicolo.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lFascicolo.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lFascicolo.setDataAggiornamento(DateUtils.getSysDate());

//inserimento
    IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
    ArchiviazioneModel lArcModRes = lCtrl.ExInserisciEventoNotificaArchiviazione(lEveNotMod,lArcMod,lFascicolo);

    String lPage = null;

    if(!this.isRequestParameterNullObj("tipobottone") && this.getRequestStringParameter("tipobottone") != null
       && this.getRequestStringParameter("tipobottone").equals("altro"))
    {

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.archiviazione.action.ActLoadDettaglioAssorCumuloAltro&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lArcModRes.getEveIdEvento();
    }
    else
    {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.archiviazione.action.ActLoadDettaglioAssorCumulo&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lArcModRes.getEveIdEvento();
    }

    return lPage;
  }
}