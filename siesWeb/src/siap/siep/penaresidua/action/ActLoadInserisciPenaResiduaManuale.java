package siap.siep.penaresidua.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaResidua</p>
* <p>Description: Classe Action per la load inserisci di PenaResiduaManuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciPenaResiduaManuale extends ActionSiap
                                                implements ICostantiPenaResidua
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   public String processRequest() throws F3BException
 {

   if (this.isSessionAttributeNullObj("fascicolo"))
   {
     return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
   }
   FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

   //===========================================================================
   // La pena residua manuale può essere inserita solo per fascicoli migrati
   //===========================================================================
//   if(!lFascMod.getCodOperatoreInserimento().startsWith("res"))
//   {
//     throw new SIEPException(SIEPException.USER_MESSAGE,"Non si può chiedere l'inserimento di pena residua per un fascicolo non migrato");
//   }

   if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                         "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
     lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&" +
                        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
     setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

     return IWebConstants.PG_MESSAGE;
   }

   this.isFascicoloSiepDiCompetenza();


//   if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
//   {
//      RedirectTo lRedirigi = new RedirectTo();
//      lRedirigi.setPage(IWebConstants.PG_MAIN);
//      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
//                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
//      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
//                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
//      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
//
//      return IWebConstants.PG_MESSAGE;
//   }

   this.isEventoNonValidato(); 
   
   //===========================================================================
   // Ricerca posizione giuridica corrente
   //===========================================================================
   PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
   IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
   lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

   if (lPG == null)
   {
     throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");
   }
   setRequestAttribute("PosizioneGiuridica", lPG);

   Option lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
   setRequestAttribute("valute", ""+lOption );

   setRequestAttribute("modalita", "I");
   
   
   //===========================================================================
   // Se provengo dal dettaglio della pena (F5) devo precaricare la maschera
   // con i dati passati dal dettaglio che sono legati all'ultima pena validata
   //===========================================================================
   if (!isRequestParameterNullObj("FromF5"))
   {
     PenaResiduaModel lPenMod = new PenaResiduaModel();
     
     lPenMod.setDataInizio         (getRequestDateParameter(CAMPO_ANNO_DATA_DECORRENZA_PENA, CAMPO_MESE_DATA_DECORRENZA_PENA, CAMPO_GIORNO_DATA_DECORRENZA_PENA));
     lPenMod.setDataFineReclusione (getRequestDateParameter(CAMPO_ANNO_DATA_FINE_RECLUSIONE, CAMPO_MESE_DATA_FINE_RECLUSIONE, CAMPO_GIORNO_DATA_FINE_RECLUSIONE));
     lPenMod.setDataInizioArresto  (getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ARRESTO, CAMPO_MESE_DATA_INIZIO_ARRESTO, CAMPO_GIORNO_DATA_INIZIO_ARRESTO));
     lPenMod.setDataFinePresunta   (getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
     lPenMod.setDataFine           (getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));

     // Quantum Reclusione 
     lPenMod.setNumAnniReclusione   (getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
     lPenMod.setNumMesiReclusione   (getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
     lPenMod.setNumGiorniReclusione (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));
     
     // Multa
     if(  (getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA)!=null && !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA)).equals(""))
        ||(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)!=null && !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)).equals("")) )
     {
       lPenMod.setImportoMulta(new BigDecimal(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA) + "." +
                                              getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)));
     }
     
     
     // Quantum Arresto e Ammenda
     lPenMod.setNumAnniArresto   (getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO));
     lPenMod.setNumMesiArresto   (getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO));
     lPenMod.setNumGiorniArresto (getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO));
     
     if(  (getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)!=null && !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)).equals(""))
        ||(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)!=null && !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)).equals("")) )
     {
        lPenMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)+"."+
                                                 getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)));
     }
    
     String lLibAnt = "";
     if (!isRequestParameterNullObj("LibAntGiorni")){
       lLibAnt = getRequestStringParameter("LibAntGiorni");
     }
     
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Pena da F5 = "+lPenMod);
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("LibAntGiorni = "+lLibAnt);
       
     setRequestAttribute("PenaModelDaF5", lPenMod );
     setRequestAttribute("LibAntGiorni", lLibAnt );
     setRequestAttribute("caricaDati", "S");
   }

   return PG_LOAD_INSERISCIPENARESIDUA_MANUALE_RES;
 }
}