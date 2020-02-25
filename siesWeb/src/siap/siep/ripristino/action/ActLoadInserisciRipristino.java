package siap.siep.ripristino.action;

/**
 * <p>Title: ActLoadInserisciSospensione</p>
 * <p>Description: Azione Load del Calcolo Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadInserisciRipristino extends ActionSiap
                                        implements ICostantiRipristino
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//Controllo Validazione Fascicolo
    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    this.isFascicoloSiepDiCompetenza();

//Controllo Fascicolo definito
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    this.isEventoNonValidato();

/******************************* Posizione Giuridica **********************************/
    IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

    if (lPosizione == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

    setRequestAttribute("posizione", lPosizione);

/******************************* Pena Complessiva *****************************/
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

    String lFlagErgastolo = "N";
    // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
    if(  lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "")
    {
      if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
      {
        lFlagErgastolo = "S";
      }
      else
      if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
      {
       lFlagErgastolo = "D";
      }
    }


    setRequestAttribute("flagergastolo", lFlagErgastolo);
/******************************* Fine Pena Complessiva ************************/

/***********************************  Pena Residua  ***************************/

    // Cerca la PENA_RESIDUA a partire da un evento particolare
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01");

//  lEveMod.setCodTipoProvvedimento("04");
   // String[] lMotivi = {"0267", "0270"};

   // Cambiato il codice Tipo Provvedimento.
   // Comunque si mantiene la compatibilità con i vecchi codici. Luigi 11-10-2005
    String[] lTipoProv = {"04","12", "04", "25"};
    String[] lMotivi = {"0267","0267", "0270", "0270"};

    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);

    lEveMod = lCtrlEvento.ExRicercaEventoUnicoTipoProvTipoMot(lEveMod,lTipoProv,  lMotivi);
  //  lEveMod = lCtrlEvento.ExRicercaEventoPerMotivo(lMotivi, lEveMod);

    if (lEveMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "La pena non è interrotta. Impossibile eseguire la richiesta.");

    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

    PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaByIdEvento(lEveMod.getIdEvento());

    if( lPenaResidua == null )
      throw new SIEPException(SIEPException.USER_MESSAGE, "La pena non è interrotta. Impossibile eseguire la richiesta.");

    setRequestAttribute("penaresidua", lPenaResidua);

    // ** NOTA : mi aspetto un solo record di sopensione per pena residua
    ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
    SospensioneModel lSospensione = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

    if(  lSospensione == null )
      throw new SIEPException(SIEPException.USER_MESSAGE, "Dati dell'interruzione non presenti. Impossibile eseguire la richiesta.");

    setRequestAttribute("sospensione", lSospensione);
    return PG_LOAD_INSERISCI_RIPRISTINO;  //restituisce la jsp di VIEW
  }
}
