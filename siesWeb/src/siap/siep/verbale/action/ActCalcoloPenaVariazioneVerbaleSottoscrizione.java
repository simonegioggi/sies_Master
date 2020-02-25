package siap.siep.verbale.action;

/**
* <p>Title: ActCalcoloPenaVariazioneVerbaleSottoscrizione</p>
* <p>Description: Classe Action per il calcolo delle pena per la variazione data inizio misura</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActCalcoloPenaVariazioneVerbaleSottoscrizione extends ActionSiap implements ICostantiVerbale
{

  /**
  * Azione di Inserimento Aggiornamento del PenaResidua nel caso di Varizione della data del Verbale
  * Sottoscrizione agli Obblighi di condannato Libero(). In questo caso va 
  * ricalcolato il quantum di pena e soprattutto le date di decorrenza a partire
  * dalla data variata  di firma del Verbale.
  * @return Nome della pagina JSP da visualizzare del risultato al termine dell'elaborazione
  * @throws F3BException
  */
	
  public String processRequest() throws Exception
  {
	  
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    PenaResiduaModel lPenModFin = new PenaResiduaModel();
    MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
    // n.b. questa Action NON viene invocata dalle jsp ma dalla ActLoadDettaglioVerbaleSottoscrizione
    		
    //AMBROSINO 08/2010 Per il calcolo della Data Fine Pena  prendo la NUOVA DATA INIZIO MISURA (misura alternativa)
   //					e non più la data inizio misura del Verbale, perchè è stata VARIATA  

    	lMisMod = (MisuraAlternativaModel)getRequestAttribute("misuraalternativa");

    String vedoDataIntermedia = "N";
    Date lDataFinePenaPres = null;
    Date lDataInizio = null;

    lDataInizio = lMisMod.getDataInizioMisura();
     	
    // Recupero l'ultima pena residua a sistema (validata o meno)
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    PenaResiduaModel lPenaModel = new PenaResiduaModel();

    //========================================================================
    // Recupero i quantum di pena Validati che concorrono alla calcolo della
    // pena 
    //========================================================================

    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(), null);

// ***********************************NO*ERGASTOLO********************************************/
    //===========================================================================
    // Il ricalcolo viene effettuato solo se non Ergastolo, altrimenti viene 
    // semplicemente impostato il fine pena a 31/12/9999
    //===========================================================================
    if(lPenMod.getFlagErgastolo().equals("N"))
    {
		     lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio, null,"all");
		     lDataFinePenaPres = lPenaModel.getDataFine();
		     if(lPenaModel != null && (lPenaModel.getDataFineReclusione() != null ||
		                               lPenaModel.getDataInizioArresto() != null))
		     {
		    	 vedoDataIntermedia = "S";
		     }
   }
   else  //inizio ergastolo
   {
		     lPenaModel = new PenaResiduaModel(lPenMod);
		     lDataFinePenaPres = DateUtils.getDate(9999,12,31);
		     lPenaModel.setDataFine(lDataFinePenaPres);
   } //fine ergastolo

    lPenaModel.setDataInizio(lDataInizio);
    lPenaModel.setDataFinePresunta(lDataFinePenaPres);

    int lTotGiorni = lCalcoloPenaModel.getLiberazioneAnticipata();
    int lTotGiorniRD = lCalcoloPenaModel.getRimediRisarcitori();
    
    
    		lPenaModel.setEveIdEvento(lMisMod.getEveIdEvento());
    
     lPenaModel.setFlagValidato(lPenMod.getFlagValidato());
     lPenaModel.setIdPenaResidua(lPenMod.getIdPenaResidua()); // A8RR004
     //------
     lPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
     lPenaModel.setFlagErgastolo(lPenMod.getFlagErgastolo());
     lPenaModel.setDiesAQuo("S");

     // Se l'ultima pena residua era Validata
     if(lPenMod.getFlagValidato().equals("S"))
     {
      lPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lPenaModel.setDataInserimento(DateUtils.getSysDate());

      lPenaModel.setCodUfficioAggiornamento(null);
      lPenaModel.setCodOperatoreAggiornamento(null);
      lPenaModel.setDataAggiornamento(null);
    }

    if(lPenMod.getFlagValidato().equals("N"))
    {
      lPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
      lPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
      lPenaModel.setDataAggiornamento(DateUtils.getSysDate());
    }

    //==========================================================================
    // Aggiorna la pena_residua e le LA (da N a E)
    //==========================================================================
    IVerbale IVerCtrl = SIEPLookupRemote.getVerbaleRemote();
    lPenModFin = IVerCtrl.ExAggiornaPenaVerbale(lPenaModel);

//prepara i dati per la jsp
    setRequestAttribute("penaresidua", lPenModFin);
    setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);

// AMBROSINO - Se model "verbale" esiste , lo passo al dettaglio successivo, altrimenti passo un model vuoto    
    VerbaleModel lVerM = new VerbaleModel();
    if (!this.isRequestAttributeNullObj("verbale") && getRequestAttribute("verbale") != null)
    {
    	lVerM = (VerbaleModel)getRequestAttribute("verbale");
    }	
    setRequestAttribute("verbale", lVerM);
    
    setRequestAttribute("cssa",getRequestAttribute("cssa"));
    setRequestAttribute("istitutodetenzione", getRequestAttribute("istitutodetenzione"));
    setRequestAttribute("lTotGiorniConcessi",""+lTotGiorni);
    setRequestAttribute("lTotGiorniRDConcessi",""+lTotGiorniRD);
    
    
    setRequestAttribute("misuraalternativa", lMisMod);
    setRequestAttribute("evento", getRequestAttribute("evento"));
    setRequestAttribute("camponota", getRequestAttribute("camponota"));
    
    return PG_LOAD_DETTAGLIO_PENA_RESIDUA_VARIAZIONE_VERBALE_SOTTOSCRIZIONE;
  }
}