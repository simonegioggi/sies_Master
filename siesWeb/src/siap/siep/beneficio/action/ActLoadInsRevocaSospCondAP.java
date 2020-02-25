package siap.siep.beneficio.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
//import per le combo
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInsRevocaSospCondAP</p>
* <p>Description: Classe Action per la load inserisci Revoca di Beneficio (Sospensione Condizionale)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInsRevocaSospCondAP extends ActionSiap implements ICostantiBeneficio
{
  public String processRequest() throws Exception
  {
    boolean proceed = true;
    
    FascicoloSiepModel lFascMod = new FascicoloSiepModel();
    lFascMod.setIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );

    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFascMod=lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
    
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile Aggiungere o Revocare Benefici");
    }
    if (lFascMod.getFlagValidato().equalsIgnoreCase("S"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è stato validato! Impossibile Aggiungere o Revocare Benefici");
    }

    if (proceed)
    {
    	Option lOption  = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    	setRequestAttribute("CodTipoAutoritaEmittente", "" + lOption );
    	
    	Option lOptionProv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), "01");
    	lOptionProv.setFilter( new String[] {"01", "02"} );
    	setRequestAttribute("CodTipoProvvedimento", "" + lOptionProv );    	

 //     setRequestAttribute("concesso_revocato","R");

    	if(! isRequestParameterNullObj("ComingFromInsert"))
    	{
    		setRequestAttribute("ComingFromInsert", "YES");
    	}

    	setRequestAttribute("modalita", "I");
    	setRequestAttribute("fascicolo", lFascMod);

    	return PG_LOAD_INSERISCI_REVOCA_SOSPCOND;  //restituisce la jsp di VIEW
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
}