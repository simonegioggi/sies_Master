package siap.siep.calcolopena.action;

/**
* <p>Title: ActInserisciPenaValidata</p>
* <p>Description: Classe Action per l'aggiornamento della Pena residua dopo il 
* primo calcolo della pena se pena in decorrenza o a decorrenza futura e presenza
* del fine pena manuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActInserisciPenaValidata extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Aggiornamento della Pena Residua e dello scadenzario fine pena
   * nel caso di primo calcolo della pena 
   * - Aggiorna la Pena Residua con il fine pena manuale 
   * - Valida la Pena Residua  (!!!non più!!!)
   * - Aggiorna lo scadenzario fine pena (!!! non più!!!)
   * @return la jsp di visualizzazione del dettaglio
   * @throws F3BException
   */
  public String processRequest() throws Exception
  {
    PenaResiduaModel lPenRes=new PenaResiduaModel();
    BigDecimal lFascID = ((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();
    
    //==========================================================================
    // Se non Libero per questa causa, oppure libero ma detenuto altra causa
    // aggiorno i dati della pena
    //==========================================================================
    if (  !(   getRequestStringParameter("FlagAltraCausa").equals("N") 
            && (   getRequestStringParameter("CodPosizioneGiuridica").equals("07")
                || getRequestStringParameter("CodPosizioneGiuridica").equals("10")
               )
            )
       )
    {
      //=======================================================================
      // Se detenuto altra causa, o presente fine pena manuale
      //=======================================================================
       if (!getRequestStringParameter("GPV").equals("") || getRequestStringParameter("FlagAltraCausa").equals("S"))
       {
         if (!getRequestStringParameter("GPV").equals(""))
           lPenRes.setDataFine(getRequestDateParameter("APV","MPV","GPV"));
         
         lPenRes.setNumAnniReclusione(getRequestBigDecimalParameter("Arec"));
         lPenRes.setNumMesiReclusione(getRequestBigDecimalParameter("Mrec"));
         lPenRes.setNumGiorniReclusione(getRequestBigDecimalParameter("Grec"));
         lPenRes.setImportoMulta(getRequestBigDecimalParameter("Multa"));

         lPenRes.setNumAnniArresto(getRequestBigDecimalParameter("Aarr"));
         lPenRes.setNumMesiArresto(getRequestBigDecimalParameter("Marr"));
         lPenRes.setNumGiorniArresto(getRequestBigDecimalParameter("Garr"));
         lPenRes.setImportoAmmenda(getRequestBigDecimalParameter("Ammenda"));
         
         lPenRes.setIdPenaResidua(new BigDecimal(getRequestStringParameter("IdPenaResidua")));
         // mod 03/04/2007 la pena non viene più validata sul primo calcolo ma 
         // viene fatto solo l'update del fine pena manuale per consentire di 
         // modificare sempre il calcolo fino al primo Provvedimnento che rende
         // esecutiva la pena agganciandola e validandola
//         lPenRes.setFlagValidato("S");
         lPenRes.setFlagValidato("N"); 
         lPenRes.setDiesAQuo("S");
         lPenRes.setFlagErgastolo("N");
         
         if(!isRequestParameterNullObj("Gdatainiziopena"))
         {
           lPenRes.setDataInizio(getRequestDateParameter("Adatainiziopena","Mdatainiziopena","Gdatainiziopena"));
         }
         
         if(!isRequestParameterNullObj("Gdatafinepenapresunta"))
         {
           lPenRes.setDataFinePresunta(getRequestDateParameter(
               "Adatafinepenapresunta", "Mdatafinepenapresunta",
               "Gdatafinepenapresunta"));
         }
         if(!isRequestParameterNullObj("Gdatafinereclusione"))
         {
           lPenRes.setDataFineReclusione(getRequestDateParameter(
               "Adatafinereclusione", "Mdatafinereclusione",
               "Gdatafinereclusione"));
         }
         if(!isRequestParameterNullObj("Gdatainizioarresto"))
         {
           lPenRes.setDataInizioArresto(getRequestDateParameter(
               "Adatainizioarresto", "Mdatainizioarresto", "Gdatainizioarresto"));
         }
         
         //=====================================================================
         // Se detenuto altra causa il fine pena è quello calcolato 
         //=====================================================================
         if (getRequestStringParameter("FlagAltraCausa").equals("S") && !isRequestParameterNullObj("Gdatainiziopena") && getRequestStringParameter("GPV").equals(""))
         {
           lPenRes.setDataFine(getRequestDateParameter("Adatafinepenapresunta","Mdatafinepenapresunta","Gdatafinepenapresunta"));
         }
         else
         { // altrimenti è quello manuale
           lPenRes.setDataFine(getRequestDateParameter("APV", "MPV", "GPV"));
         }
         
         lPenRes.setFasSieIdFascicoloSiep(lFascID);
         
         lPenRes.setCodOperatoreAggiornamento (getCodUtenteConnesso());
         lPenRes.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
         lPenRes.setDataAggiornamento         (DateUtils.getSysDate());
         
         //=======================
         // Sanzioni sostitutive
         //=======================
         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
         siesLogger.debug("Verifico se recuperare le sanzione sostitutive");
         
         
         //=====================================================================
         // Aggiorno la pena residua (fine pena e validazione)
         //=====================================================================
         IPenaResidua lPR=SIEPLookupRemote.getPenaResiduaRemote();
         lPenRes=lPR.ExModificaPenaResidua(lPenRes);

         
         //=====================================================================
         // Verifico se presente Sanzione Sostitutiva
         //=====================================================================
         ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
         SanzioneSostResiduaModel lSSModel = lSSCtrl.getSSByIdPenaResidua(lPenRes.getIdPenaResidua());
         
         setRequestAttribute("aSSResidua",lSSModel);
         
         //=====================================================================
         // AGGIORNA I RECORD DI LIB. ANTICIPATA DA N o null AD 'E' in quanto
         // computati sul fine pena. Infatti tale action viene invocata solo
         // se la pena è in decorrenza (o a decorrenza futura) quindi le LA 
         // presenti sono state tutte computate. Il problema è che una volta
         // flaggate a E non è più possibile tornare in dietro perchè con il 
         // semplice flag non è possibile sapere a seguito di quale operazione
         // tale flag è stato modificato.
         //=====================================================================
         // Mod 24/04/2009 4.0upd02: le LA non vengono più fleggate sul primo 
         // calcolo in quanto sicuramente non computate
         //ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
         //lCtrlLib.ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep(lFascID, "N", "E");
         //=====================================================================
         // Scadenzario Fine Pena
         //=====================================================================
//         ScadenzarioModel lScaMod=new ScadenzarioModel();
//
//         lScaMod.setFasSieIdFascicoloSiep(lFascID);
//         lScaMod.setDataInizioScadenza(lPenRes.getDataInizio());
//         lScaMod.setDataFineScadenza(lPenRes.getDataFine());
//         lScaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
//         lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
//         lScaMod.setDataInserimento(DateUtils.getSysDate());
//
//         IScadenzario lSC = SIEPLookupRemote.getScadenzarioRemote();
//         lSC.ExRicercaInserisciAggScadenzarioIdFascicoloCorrente(lScaMod);
       }
       
       //======================================================================= 
       // Prepara la pagina di destinazione
       //======================================================================= 
       CalendarModel lCalModArresto =new CalendarModel();
       lCalModArresto.setNumAnni(getRequestBigDecimalParameter("Aarr"));
       lCalModArresto.setNumMesi(getRequestBigDecimalParameter("Marr"));
       lCalModArresto.setNumGiorni(getRequestBigDecimalParameter("Garr"));
       lCalModArresto.setImportoAmmenda(getRequestBigDecimalParameter("Ammenda").doubleValue());
       
       CalendarModel lCalModReclusione =new CalendarModel();
       lCalModReclusione.setNumAnni(getRequestBigDecimalParameter("Arec"));
       lCalModReclusione.setNumMesi(getRequestBigDecimalParameter("Mrec"));
       lCalModReclusione.setNumGiorni(getRequestBigDecimalParameter("Grec"));
       lCalModReclusione.setImportoMulta(getRequestBigDecimalParameter("Multa").doubleValue());

       if (lPenRes.getDataInizio()!=null)
         setRequestAttribute("datainiziopena",DateUtils.getDateToString(lPenRes.getDataInizio(),"dd-MM-yyyy"));
       if (lPenRes.getDataFinePresunta()!=null)
         setRequestAttribute("datafinepenapresunta",DateUtils.getDateToString(lPenRes.getDataFinePresunta(),"dd-MM-yyyy"));
       if (lPenRes.getDataFineReclusione()!=null)
         setRequestAttribute("datafinereclusione",DateUtils.getDateToString(lPenRes.getDataFineReclusione(),"dd-MM-yyyy"));
       if (lPenRes.getDataInizioArresto()!=null)
         setRequestAttribute("datainizioarresto",DateUtils.getDateToString(lPenRes.getDataInizioArresto(),"dd-MM-yyyy"));
       if (lPenRes.getDataFine()!=null)
         setRequestAttribute("datafinepenavalidata",DateUtils.getDateToString(lPenRes.getDataFine(),"dd-MM-yyyy"));
       setRequestAttribute("arresto",lCalModArresto);
       setRequestAttribute("reclusione",lCalModReclusione);

    }

    if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
    {
      setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
    }

    if (!isRequestParameterNullObj("EsisteTrasmissioneAtti"))
    {
      setRequestAttribute("EsisteTrasmissioneAtti", getRequestStringParameter("EsisteTrasmissioneAtti"));
    }

    String lPage=new String(f3b.web.IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidata.jsp");
    return lPage;
  }
}