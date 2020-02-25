package siap.siep.sospensione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioDecretoSospensione</p>
 * <p>Description: Classe Action per la load dettaglio di Sospensione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioDecretoSospensione extends ActSIESDettaglioProvvedimento implements ICostantiSospensione,ICostantiEvento
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
    setRequestAttribute("eventonotifica", lEveNotMod);


    EventoNotificaModel lEveNot = new EventoNotificaModel();
    IEventoSimeone lCtrlSimeone = SICOLookupRemote.getEventoSimeoneRemote();
    String[] motivo ={"0061","0062","0063","0104","0105","0117","0000"};

    lEveNot = lCtrlSimeone.ExRicercaEventoNotificaByIdFascicoloCodiceMotivo(lFascMod.getIdFascicoloSiep(),motivo);
    setRequestAttribute("eventonotificasimeone", lEveNot);

    NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
    for (int i = 0; i < lNotifiche.length; i++)
    {
      if (lNotifiche[i].getAutoritaEsterna() != null && lNotifiche[i].getCodTipoNotifica().equals("N"))
      {
        setRequestAttribute("autoritaN", lNotifiche[i].getAutoritaEsterna());
      }
    }

    //Magistrato
    IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
    setRequestAttribute("magistratocompetente", lMagi);
    
    return PG_LOAD_DETTAGLIO_DECRETO_SOSPENSIONE;
  }
}