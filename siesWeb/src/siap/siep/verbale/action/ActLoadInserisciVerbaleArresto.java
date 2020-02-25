package siap.siep.verbale.action;


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;




/**
* <p>Title: ActLoadInserisciVerbale</p>
* <p>Description: Classe Action per la load inserisci di Verbale Arresto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciVerbaleArresto extends ActionSiap implements ICostantiVerbale
{
	public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    if (isFascicoloArchiviatoDefinito())
     return IWebConstants.PG_MESSAGE;

    this.isEventoNonValidato();

/************************************MODIFICA*****************************************/
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes=SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascMod.getIdFascicoloSiep());

    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenMod))
      return IWebConstants.PG_MESSAGE;

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    String lPosizione = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();


    //REGOLE PER DIFFERENZIARE L'USO DELLA FUNZIONE
    String lMessaggio = null;

    if("20".equals(lPosizione)) //Evaso
    {
      lMessaggio = "Attenzione : Occorre registrare il ripristino dell'esecuzione dalla funzione interruzioni ripristino esecuzione.";
    }
    else if(  "29".equals(lPosizione)  //detenzione domiciliare provvisoria
           || "30".equals(lPosizione)) //ESTRADATO
    {
      lMessaggio = "Attenzione : Occorre utilizzare la funzione ripristino detenzione.";
    }

    if(lMessaggio != null)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
                         ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMessaggio);
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      
      return IWebConstants.PG_MESSAGE;
    }


    //Controllo Evento Ordine Esecuzione
    if(   lPosizione != null && (lPos.getPosizioneGiuridica().isLibero())
      || (lFascMod.getFlagAltraCausa()!=null && lFascMod.getFlagAltraCausa().equals("S")))
    {
      IVerbale lEve = SIEPLookupRemote.getVerbaleRemote();

      lEve.ExRicercaEventoVerbale(lFascMod.getIdFascicoloSiep(), "Nessun Documento registrato in relazione al verbale d'arresto");
    }
    else
    {
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Non è possibile emettere Verbale di Arresto per questa Posizione Giuridica" );
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

/************************************MODIFICA******************************************/

   // Inserire Eventuali ComboBOX
   // Option lOption = new Option( DecodificheManager.getInstance().get???());

       // Imposta Tipo Istituto
//modifica relativa al tipo istituto
    // Option lOption = null;
     //lOption = new Option( DecodificheManager.getInstance().getTipoIstituto());

    //=======================================
    // Caricamento Combo
    //=======================================
    Option lOptionAutorita = null;
 
    lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());

    String[] lFilter = new String[10];
    lFilter[0] = "-";
    lFilter[1] = "92"; // Carabinieri
    lFilter[2] = "93"; // Polizia di Stato
    lFilter[3] = "32"; // Guardia di Finanza
    lFilter[4] = "79"; // Polizia Penitenziaria
    lFilter[5] = "94"; // Polizia Municipale
    
    lFilter[6] = "20"; // Questura
    lFilter[7] = "19"; // Commissariato di P.S.
    lFilter[8] = "28"; // Carabinieri - Comando Stazione
    lFilter[9] = "58"; // Carabinieri - Nucleo Operativo
    
    lOptionAutorita.setFilter(lFilter);
    
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );
    //modifica relativa al tipo istituto
  //  setRequestAttribute("tipoIstituto", "" + lOption );

   // Imposta Modalità.
   setRequestAttribute("modalita", "I");

     return PG_LOAD_INSERISCIVERBALE_ARRESTO;  //restituisce la jsp di VIEW

    }

}