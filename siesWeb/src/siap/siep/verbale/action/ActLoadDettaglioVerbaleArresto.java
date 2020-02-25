package siap.siep.verbale.action;

import java.math.BigDecimal;

import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioVerbale</p>
 * <p>Description: Classe Action per la load dettaglio di Verbale Arresto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioVerbaleArresto extends ActSIESDettaglioProvvedimento implements ICostantiVerbale
{
  public String processRequest() throws F3BException
  {

    BigDecimal lIdVerbale = null;
    BigDecimal lIdEvento = null; //Id dell'evento collegato al verbale
    VerbaleModel lVerMod = new VerbaleModel();
    IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();

//    if (this.isRequestAttributeNullObj(CAMPO_ID_VERBALE))
    if (this.isRequestParameterNullObj(CAMPO_ID_VERBALE))
    { //Siamo nel caso del dettalgio dell'evento...
      //Esiste solo l'id dell'evento, ricerco il verbale da quello
      lIdEvento = getRequestBigDecimalParameter(siap.sico.evento.action.ICostantiEvento.CAMPO_ID_EVENTO);
      lVerMod = lCtrl.ExRicercaVerbaleByCodTipoIdEvento(lIdEvento,"01");
    }
    else
    { //Siamo nel caso del dettaglio del verbale appena inserito
      lIdVerbale = getRequestBigDecimalParameter(CAMPO_ID_VERBALE);
      lVerMod = lCtrl.ExRicercaVerbaleByKey(lIdVerbale);
      lIdEvento = lVerMod.getEveIdEvento();
    }

    BigDecimal lIdFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

    /*REWORK DETTAGLIO
        LuogoDetenzioneModel lLuoDetMod = new LuogoDetenzioneModel();
        ILuogoDetenzione lCtrlDet = SIEPLookupRemote.getLuogoDetenzioneRemote();
        lLuoDetMod = lCtrlDet.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lIdFasc);
     */

    //model Istituto Detenzione
    IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
    if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null && !lVerMod.getIstDetIdIstitutoDetenzione().equals("-"))
    {
      IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
      lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerMod.getIstDetIdIstitutoDetenzione());
    }
    setRequestAttribute("istitutodetenzione", lIstMod);

    //preparo il model di pena residua
    PenaResiduaModel lPenMod = new PenaResiduaModel();

    /*REWORK
         IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
         //lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFasc);
         lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lIdFasc);
     */

    /*Qui il rework del dettaglio non ha effetto... Purtroppo non riesco ad arrivare alla
     LA senza il fascicolo siep*/
    lPenMod = this.getPenaResidua(lIdEvento, lIdFasc);
    //==========================================================================
    // CERCA IL TOTALE GIORNI LIB ANTICIPATA
    //==========================================================================
    // N.B. cerca quelli con FLAG_ELABORATO E perchè già conteggiati nel calcolo nell'inserimento
//    ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
//    int lTotGiorni = lCtrlLib.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(lIdFasc);
    // n.b. nel nuovo calcolo della pena i giorni di LA non sono quelli con flag
    //      elaborato, ma quelli inseriti non periodo di competenza
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFasc, lIdEvento);
    int lTotGiorni = lCalcoloPenaModel.getLiberazioneAnticipata();
      
    setRequestAttribute("totalegiornilibanticipata", new BigDecimal(lTotGiorni));
  //---REWORK  setRequestAttribute("luogodetenzione", lLuoDetMod);
    setRequestAttribute("verbale", lVerMod);
    setRequestAttribute("penaresidua", lPenMod);

    if (!this.isRequestAttributeNullObj("vedoDataIntermedia"))
    {
      setRequestAttribute("vedoDataIntermedia", this.getRequestAttribute("vedoDataIntermedia"));
    }
    else
    {
      setRequestAttribute("DettaglioDaElenco", "SI");
    }
    return PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_ARRESTO;

  }
}