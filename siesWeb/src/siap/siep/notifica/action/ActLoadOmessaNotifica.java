package siap.siep.notifica.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadOmessaNotifica</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadOmessaNotifica extends ActionSiap implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
     {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
       setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
                          lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
       return IWebConstants.PG_MESSAGE;
     }

    this.isFascicoloSiepDiCompetenza();

    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
       setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
                          lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
       return IWebConstants.PG_MESSAGE;
    }


    EventoNotificaModel lEveNot = new EventoNotificaModel();
    IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
    lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(),"LS");

    if(lEveNot == null || lEveNot.getEvento() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");

    if(lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");

    EventoModel lEveMod = new EventoModel();
    lEveMod = lCtrl.ExRicercaEventoByEveIdEvento(lEveNot.getEvento().getIdEvento());

    IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
    VerbaleModel lVermod = new VerbaleModel();
    if( lEveMod != null && lEveMod.getIdEvento()!= null )
    {
      lVermod = lCtrlVer.ExRicercaVerbaleByCodTipoIdEvento( lEveMod.getIdEvento(), "02" );
    }

    NotificaModel lNotifica = null;
    for(int i=0;i<lEveNot.getNotifiche().length;i++)
    {
      NotificaModel lNotMod = new NotificaModel();
      lNotMod =lEveNot.getNotifiche()[i];
      if(lNotMod.getCodTipoNotifica().equals("E"))
      {
        lNotifica = new NotificaModel(lNotMod);
      }
    }

    setRequestAttribute("evento",lEveNot.getEvento());
    setRequestAttribute("notifica", lNotifica);
    setRequestAttribute("verbale", lVermod);

    Option lOptionAutorita = null;
    Option lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());

    if(lNotifica != null && lNotifica.getAutoritaEsterna() != null &&
       lNotifica.getAutoritaEsterna().getCodTipoAutorita() != null && !lNotifica.getAutoritaEsterna().getCodTipoAutorita().equals("22"))
    {
      lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),lNotifica.getAutoritaEsterna().getCodTipoAutorita());

    }
    else
    {
      lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
    }

    setRequestAttribute("tipoAutoritaAltra", "" + lOptionAutoritaAltra );
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );

    return PG_LOAD_OMESSA_NOTIFICA;  //restituisce la jsp di VIEW
  }
}