package siap.sico.libertaanticipata.action;

/**
* <p>Title: ActConfermaFungibilitaLibAnt</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

public class ActConfermaFungibilitaLibAnt extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    isFascicoloSiepDiCompetenza();

    //==========================================================================
    // L'id dell'evento di Ordinanza selezionato da lista o inserito
    //==========================================================================
    BigDecimal lEveIdEventoOrdinanza = getRequestBigDecimalParameter("lEveIdEventoOrdinanza");

    Date dataFinePena = null;
    boolean lCancellaFungibilita = false;
    boolean lModificaDataFinePena = false;

    //==========================================================================
    // Nuova gestione della fungibilità
    //==========================================================================
    BigDecimal lIdFungibilita = null;
    if ( !isRequestParameterNullObj("flagFungibilita") )
    {
      String flagFungibilita = getRequestStringParameter("flagFungibilita");
      lIdFungibilita = getRequestBigDecimalParameter("IdFungibilita");

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("flagFungibilita = "+flagFungibilita);

      if ( flagFungibilita.equals("1") )
      {
        // Data Fine Pena = Sysdate
        // conferma Fungibilità calcolata.
        lCancellaFungibilita = false;
        lModificaDataFinePena = true;

        //dataFinePena = getRequestDateParameter("AnnoDataFineCalcolata","MeseDataFineCalcolata","GiornoDataFineCalcolata");
        //dataFinePena = DateUtils.getDate( DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy");
        // Mod Data fine pena = data recuperata dalla form
        dataFinePena = getRequestDateParameter("AnnoDataSysCalcoli","MeseDataSysCalcoli","GiornoDataSysCalcoli");
      }
      else if ( flagFungibilita.equals("2") )
      {
        // Data Fine Pena = Sysdate
        // cancella la Fungibilità
        lCancellaFungibilita = true;
        lModificaDataFinePena = true;

        //dataFinePena =  DateUtils.getDate( DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy");
        dataFinePena = getRequestDateParameter("AnnoDataSysCalcoli","MeseDataSysCalcoli","GiornoDataSysCalcoli");
      }
      else if ( flagFungibilita.equals("3") )
      {
        // Data Fine Pena = Data Fine Calcolata
        // cancella la Fungibilità
        lCancellaFungibilita = true;

        dataFinePena = getRequestDateParameter("AnnoDataFineCalcolata","MeseDataFineCalcolata","GiornoDataFineCalcolata");
      }

      setRequestAttribute("IdFungibilita", lIdFungibilita);

      if (   !isRequestParameterNullObj("IdPenaResidua")
          && !getRequestStringParameter("IdPenaResidua").equals("")
          && !getRequestStringParameter("IdPenaResidua").equals("null"))
      {
        BigDecimal lIdPenaResidua = getRequestBigDecimalParameter("IdPenaResidua");

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("IdPenaResidua = "+flagFungibilita);

        IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
        PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lIdPenaResidua);

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("PenaResidua = "+lPenaResMod);

        if (lPenaResMod == null)
          throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Residua non presente. Impossibile procedere.");

        if(lModificaDataFinePena && lCancellaFungibilita)
        {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("Modifica Data Fine e Cancella Fungibilita");

          lPenaResMod.setDataFine(dataFinePena);

          ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
          lCtrlLib.ExCancellaFungibilitaLicenzeLibanticipata(lPenaResMod, lIdFungibilita);
        }
        else if( !lModificaDataFinePena && lCancellaFungibilita )
        {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("NON Modifica Data Fine e Cancella Fungibilita");

          IFungibilita IFung = SIEPLookupRemote.getFungibilitaRemote();
          FungibilitaModel lFunModel = new FungibilitaModel();
          lFunModel.setIdFungibilita(lIdFungibilita);
          IFung.ExCancellaFungibilita(lFunModel);
        }
        else if( lModificaDataFinePena && !lCancellaFungibilita )
        {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("Modifica Data Fine e NON Cancella Fungibilita");

          lPenaResMod.setDataFine(dataFinePena);
          
          lPenResCtrl.ExModificaPenaResidua(lPenaResMod);       
        }
        else
        {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("Nessuna Modifica");
        }
      }
    }

    /******************************* Posizione Giuridica **********************/
    IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
    if (lPosizione == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");
    /**************************************************************************/

    //==========================================================================
    // Pagina di ritorno
    //==========================================================================
    String lPage;

    boolean lIsDl92 = false;
    
    if (!isRequestParameterNullObj("isDL92")){
      lIsDl92 = true;
    }
    
    if (lIsDl92) {
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.libertaanticipata.action.ActLoadInserisciOSRimediRisarcitori&"+ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO+"="+lEveIdEventoOrdinanza+"&"+ICostantiFungibilita.CAMPO_ID_FUNGIBILITA+"="+lIdFungibilita;
    } else {
      if(lPosizione.isMisAlt() || lPosizione.getCodPosizioneGiuridica().equals("04")) // Arresti domiciliari ex art.656/10
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipataMA&lAzioneOS=libant&"+ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO+"="+lEveIdEventoOrdinanza;
      else
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipata&lAzioneOS=libant&"+ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO+"="+lEveIdEventoOrdinanza;
    }
    return lPage;
  }
}