package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioEsitoEspulsione</p>
 * <p>Description: Classe Action per la load dettaglio di Richiesta Esito Espulsione</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActDettaglioEsitoEspulsione extends ActSIESDettaglioProvvedimento implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

// id dell'evento inserito
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

//Pena Residua
    PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("penaresidua", llPenMod);

//ricerca posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("posizioneluogoaltra", lPos);

//ricerca evento notifica
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
    this.setRequestAttribute("eventonotifica", lEveMod);

//Ricerca Magistrato
    IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

    setRequestAttribute("magistrato", lMagMod);

//notifiche
   for(int i=0;i<lEveMod.getNotifiche().length;i++)
   {
     NotificaModel lNotMod = new NotificaModel();
     lNotMod = lEveMod.getNotifiche()[i];

     if(lNotMod != null && lNotMod.getAutEstIdAutoritaEsterna() != null)
     {
       setRequestAttribute("autoritaEsterna", lNotMod.getAutoritaEsterna());
       setRequestAttribute("noteautoritaEsterna", lNotMod.getNote());
     }
   }
    return PG_DETTAGLIO_ESITO_ESPULSIONE;
  }
}