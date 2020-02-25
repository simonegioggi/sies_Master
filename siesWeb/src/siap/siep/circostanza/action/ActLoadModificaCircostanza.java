package siap.siep.circostanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaCircostanza</p>
* <p>Description: Classe Action per la load modifica di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaCircostanza extends ActionSiap implements ICostantiCircostanza
{
	public String processRequest() throws Exception
	{

      boolean proceed=true;
      FascicoloSiepModel lFascMod=new FascicoloSiepModel();

      lFascMod.setIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
      IFascicoloSiep lFCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      lFascMod=lFCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());

      if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
      {
        proceed=false;
        // setta la risposta nella request
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
      }
      if (lFascMod.getFlagValidato().equalsIgnoreCase("S"))
      {
        proceed=false;
        // setta la risposta nella request
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è stato validato! Impossibile Aggiungere Circostanze Ai Capi di Imputazione");
      }

      if (proceed)
      {
    	  // Preparazione della form di Modifica
    	  return elaborazione();
      }
      else
      {
        //Prepara la "pagina" di destinAction
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage( IWebConstants.PG_MAIN );
        lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
        setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

        return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
      }
	}
	 
	 protected String elaborazione() throws Exception
	 {
         
   	  //Controllo che non si stia lavorando su una entità in modifica ad altri
         LockModel lck =
         lockIfNotLocked("circostanza", getRequestStringParameter(CAMPO_ID_CIRCOSTANZA), getCodUtenteConnesso());
         if (lck != null)
         {
           setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La "+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
           return IWebConstants.PG_MESSAGE;
         }

       CircostanzaModel lMod = new CircostanzaModel();

       lMod.setIdCircostanza(getRequestBigDecimalParameter(ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA));

       ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();

       lMod = lCtrl.ExRicercaCircostanzaByKey(lMod.getIdCircostanza());

       setRequestAttribute("circostanza", lMod);

       Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato(), lMod.getCodFonte() );
       setRequestAttribute("TipiFontiReato", "" + lOption );

       lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione(), lMod.getCodSottonumerazione() );
       setRequestAttribute("TipiSottonumerazione", "" + lOption );

       lOption  = new Option( DecodificheManager.getInstance().getBilanciamentoCircostanze(), lMod.getCodBilanciamentoCircostanze() );
       setRequestAttribute("BilanciamentoCircostanze", "" + lOption );

       //**************************************************************************************************
       //Federica - a9-rr-078
       //aggiunto campo Comma-Qualificante 
       lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCommaQualificante());
       setRequestAttribute("TipiCommaQualificante", "" + lOption);
       //**************************************************************************************************
       
      setRequestAttribute("modalita", "M");

       return PG_LOAD_MODIFICACIRCOSTANZA;  //restituisce la jsp di VIEW
	 }
	 
	 
}