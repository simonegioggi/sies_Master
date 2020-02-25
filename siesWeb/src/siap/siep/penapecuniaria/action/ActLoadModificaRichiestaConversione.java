package siap.siep.penapecuniaria.action;


/**
* <p>Title: ActLoadModificaRichiestaConversione</p>
* 
* <p>Description: Classe Action per la load Modifica di RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadModificaRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria
{ 
 /**
  * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare in tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  */
  public String processRequest()  throws F3BException 
  {
  	IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
  	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
  	lRicMod.setIdRichiestaConversione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
  	lRicMod=lCtrl.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione() );
  	setRequestAttribute("richiestaconversione", lRicMod);

  	// 29/09/2015 Nuova gestione Richieste di conversione: si è slegata la richiesta di conversione all'evento corrente di R.C.
  	// Per stabilire se le Richieste di Conversione contemplano provvedimenti della Sorveglianza, 
  	// bisogna cercare Eventi collegati tramite FAS_SIU_ID_FASCICOLO_SIUS, che abbiano COD_TIPO_PROVVEDIMENTO = 02 /03, COD_MOTIVO = 2470 / 2471 e FLAG_DOCUMENTO_REGISTRATO <> "A".
  	String lIdRicConv = getRequestStringParameter (CAMPO_ID_RICHIESTA_CONVERSIONE);
  	String[] lCodTipoProvv = {"02", "03"};
    String[] lCodMotivo    = {"2470", "2471"};
    String[] lCodEsito     = {"0601", "0602", "0603", "0604"};
    EventoModel eveMod = new EventoModel();
    eveMod = lCtrl.ExRicercaEventoSorvDiRichiestaConversione(lIdRicConv, eveMod, lCodTipoProvv, lCodMotivo, lCodEsito) ;
  	///// 26/11/2014 Per le Richieste di conversione con provvedimenti della Sorveglianza, la modifica non è consentita.
  	///IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
  	///EventoModel eveMod  = lCtrlEve.ExRicercaEventoByKey(lRicMod.getEveIdEvento());
  	if  (eveMod!=null && 
  		(eveMod.getCodTipoProvvedimento().compareTo("02")==0  ||
  		 eveMod.getCodTipoProvvedimento().compareTo("03")==0 	) )
		throw new SIEPException(SIEPException.USER_MESSAGE, "Non è consentito modificare le Richieste di conversione in presenza di provvedimenti della Sorveglianza!");

  	// autorità per la conversione.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita() , lRicMod.getCodTipoAutoritaEmittente());
    lOption.setFilter( new String[] {"-","36", "99", "57","37","98", "38"});

    setRequestAttribute("autoritaConv", "" + lOption ); 

  	// Parametri multa e ammenda di Sanzione pecuniaria residua prelevati dalla pena complessiva.
	IPenaComplessiva lCtrlPC = SIEPLookupRemote.getPenaComplessivaRemote();
	PenaComplessivaModel lPCMod = new PenaComplessivaModel();
	//lPCMod = lCtrlPC.ExRicercaPenaComplessivaByIdFascicolo(eveMod.getFasSieIdFascicoloSiep());
	lPCMod = lCtrlPC.ExRicercaPenaComplessivaByIdFascicolo(lRicMod.getFasSieIdFascicoloSiep());
	setRequestAttribute("multaResidua",lPCMod.getImportoMulta().toString());
	setRequestAttribute("ammendaResidua",lPCMod.getImportoAmmenda().toString());
    
    // Imposta la Modalità a Modifica.
    setRequestAttribute("modalita", "M");
    
    return PG_LOAD_MODIFICA_RICHIESTA_CONVERSIONE;	  
  }
}