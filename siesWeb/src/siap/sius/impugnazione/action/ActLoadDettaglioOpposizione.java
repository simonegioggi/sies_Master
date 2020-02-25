package siap.sius.impugnazione.action;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadDettaglioOpposizione</p>
* <p>Description: Classe Action per la load dettaglio di Opposizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioOpposizione extends ActionSius implements ICostantiImpugnazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /***
   * Action invocata a seguito dell'inserimento (ActInserisciOpposizione) o dalla
   * liste delle opposizioni per provvedimento
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );

    this.setLinkRitorno();

    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    
    IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    ImpugnazioneModel lImpMod = null;
    
    lImpMod = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));

    setRequestAttribute("impugnazione", lImpMod);

    // Recupero dell'Evento.
    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    EventoModel lEveMod = null;
    lEveMod = lCtrlEv.ExRicercaEventoByKey( getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    setRequestAttribute("provvedimento", lEveMod);
    
    //===================================
    // Recupero dell'Ordinanza/Decreto.
    //===================================
    String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";    
    if (lEveMod.getCodTipoProvvedimento().compareTo("03")==0)
    {
      DepositoOrdinanzaPcModel lDOMod = null;
      IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());
      
      aNumOrdDec    = lDOMod.getAnnoS3().toString()+"/"+lDOMod.getNumS3().toString();
      aDataOrdDec   = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
      aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");

    }
    else if (lEveMod.getCodTipoProvvedimento().compareTo("02")==0)
    {
      DepositoDecretoModel lDDMod = null;
      IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
      lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());

      aNumOrdDec    = lDDMod.getAnnoS72().toString()+"/"+lDDMod.getNumS72().toString();
      aDataOrdDec   = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
      aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
    }

    setRequestAttribute("numOrdDec", aNumOrdDec );
    setRequestAttribute("dataOrdDec", aDataOrdDec );
    setRequestAttribute("dataDeposito", aDataDeposito );

    
    // Caricamento combo template.
    Option lOptTemplate = null;
    if(strCodTipoUfficio.equals("TDSM") || strCodTipoUfficio.equals("UDSM")){
    	lOptTemplate = UtilTemplate.listaTemplateByCodProvv("15","3");
    } else {
    	lOptTemplate = UtilTemplate.listaTemplateByCodProvv("15","1");
    }
    
    setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
    
  
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return PG_LOAD_DETTAGLIO_OPPOSIZIONE;
  }
}