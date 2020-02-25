package siap.siep.archiviazione.action;

/**
 * <p>Title: ActLoadInserisciPenaEspiata</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
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
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciPenaEspiata extends ActionSiap implements ICostantiArchiviazione
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isFascicoloSiepDiCompetenza();

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
     IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

     PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

     if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null)
     {
       RedirectTo lRedirigi = new RedirectTo();
       lRedirigi.setPage(IWebConstants.PG_MAIN);
       setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
       lRedirigi.setAction( "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
                            ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
       setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

       return IWebConstants.PG_MESSAGE;
     }

     setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);

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
    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

    PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
    if (lPenaResidua == null)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
      lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
                           ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    setRequestAttribute("penaresidua", lPenaResidua);

/******************************************************************************/
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

    Option lOptionAutorita = null;
    lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutorita", "" + lOptionAutorita);


    Option lOption = new Option(DecodificheManager.getInstance().getMotivoFineEspiazione());
    setRequestAttribute("oggettodefinzione", "" + lOption);

/*
//sede giudiziaria
    if(lFascMod.getSoggetto().getDescComuneNascitaEstero() != null &&
       !lFascMod.getSoggetto().getDescComuneNascitaEstero().equals(""))
    {
       ComuneModel lComMod = new ComuneModel();
       lComMod.setDescrizione("ROMA");
       IComune lCtrl = SICOLookupRemote.getComuneRemote();
       lComMod = lCtrl.ExGetCodiceComune(lComMod);

       setRequestAttribute("codicecomune", "" +lComMod.getCodComune());
       setRequestAttribute("sedegiudiziaria", lComMod.getDescrizione());
    }
    else
    {
      String lCodCas = lFascMod.getSoggetto().getCodComuneCasellario();
      ISedeGiudiziaria lCtrl = SIEPLookupRemote.getSedeGiudiziariaRemote();
      SedeGiudiziariaModel lSedGiuMod = lCtrl.ExRicercaSedeGiudiziariaByKey(lCodCas);

      setRequestAttribute("sedegiudiziaria", "" + lSedGiuMod.getDescrizione());
      setRequestAttribute("codicecomune", "" + lSedGiuMod.getCodComune());
    }
*/
    return PG_LOAD_INSERISCI_PENA_ESPIATA;
  }
}
