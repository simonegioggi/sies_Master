package siap.siep.ordineesecuzione.action;

import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe Ation per la Load della funzione di variazione decorrenza pena per 
 * detenuti altra causa nel caso in cui il fine pena Altra Causa venga modificato.
 * In questo caso anche inizio e fine pena questa causa (a decorrenza futura) deve
 * essere aggiornato.
 * 
 * Dalla 3.1 upd02 la funzione è aperta anche per detenuto questa causa
 * 
 * @author 
 *
 */
public class ActLoadVariazioneDecorrenzaScadenza extends ActionSiap implements ICostantiOrdineEsecuzione
{
	public String processRequest() throws Exception
	{
		if (this.isSessionAttributeNullObj("fascicolo"))
		{
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

    isFascicoloSiepDiCompetenza();


		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      return IWebConstants.PG_MESSAGE;
    }

    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile inserire un ordine d'esecuzione!" );
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    this.isEventoNonValidato();

    //Controllo esistenza almeno un avvocato per fascicolo.
    IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();

    try
    {
      lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
    }
    catch(SIEPException e)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  e.getMessage()+ " Impossibile eseguire l'Ordine di Esecuzione." );
      lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (lPos == null || lPos.getPosizioneGiuridica()==null)
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
      lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
    	
    // Ricerca l'ultima pena residua per quel fascicolo
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if(   lPenaResMod == null
       || (   lPos.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
           && !lPos.getPosizioneGiuridica().isLibero()
           && !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
           && lPenaResMod != null && lPenaResMod.getDataInizio()== null 
          )
      )
    { // Pena residua inesistente oppura data inizio = null e 
      if ( lPenaResMod == null)
      {
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
        lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
                            ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      }
      else {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua incongruente. Manca Inizio Pena. Impossibie variare decorrenza/scadenza.");
      }
      
      return IWebConstants.PG_MESSAGE;
    }
    
    Date lDataInizioPena = lPenaResMod.getDataInizio();
    
    String msgTxt = "";
    
    //==========================================================================
    // Verifico la posizione giuridica del condannato. Prosizioni gestite
    // - Detenuto altra causa (tutte) 
    // - Detenuto questa causa
    //   - 03  Espiazione Pena in Regime Carcerario
    //   - 04  Arresti Domiciliari ex art. 656/10
    //==========================================================================
    String lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
    if ( lCodPosGiu.equals("03") || lCodPosGiu.equals("04") )
    { // verifico la data di decorrenza della pena  
      if (lDataInizioPena==null)
        msgTxt = "Non è possibile inserire Variazione Decorrenza/Scadenza, non è stata impostata la Data di Decorrenza Pena.";
    }
    else if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S"))
    {
  	  // CodTipoPosGiuridica per altra causa deve essere: Espiazione Pene Definitiva in Carcere (24)
  	  //if (!(lPos.getAltraCausa().getCodTipoPosGiuridica().equals("24"))) 
  		//  msgTxt = "Non è possibile inserire Variazione Decorrenza/Scadenza, il soggetto non è in Espiazione Pena Definitiva in Carcere per altra causa.";
      // Mod A7RR308
//  	  if (lPos.getAltraCausa().getDataDecorrenza() == null)
//  		  msgTxt = "Non è possibile inserire Variazione Decorrenza/Scadenza, non è stata impostata la Data di Decorrenza Pena per altra causa.";
//  	  else 
      if (lPos.getAltraCausa().getDataScadenza() == null)
  		  msgTxt = "Non è possibile inserire Variazione Decorrenza/Scadenza, non è stata impostata la Data di Scadenza Pena per altra causa.";
	  }
    else {
    	msgTxt = "Non è possibile inserire Variazione Decorrenza/Scadenza. Il soggetto deve essere in Espiazione Pena per altra causa, oppure in Espiazione Pena in Regime Carcerario, oppure in Arresti Domiciliari ex art. 656/10.";
    }

    if (msgTxt.length() > 0) {
    	
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");

      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  msgTxt);
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
    
    setRequestAttribute("posizioneluogoaltra", lPos);



    setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
    setRequestAttribute("penaresidua", lPenaResMod);

    if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S"))
    {
      return PG_LOAD_VARIAZIONE_DECORRENZA_SCADENZA; //restituisce la jsp di VIEW
    }
    else
    {
      return PG_LOAD_VARIAZIONE_DECORRENZA_SCADENZA_QC; //restituisce la jsp di VIEW
    }
  }
}