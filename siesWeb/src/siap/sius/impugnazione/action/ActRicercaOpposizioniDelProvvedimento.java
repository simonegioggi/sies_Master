package siap.sius.impugnazione.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
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

/**
* <p>Title: ActRicercaOpposizioniDelProvvedimento</p>
* <p>Description: Classe Action per la load dell'Elenco Opposizioni per il singolo Provvedimento</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaOpposizioniDelProvvedimento extends ActionSius implements ICostantiImpugnazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );


    this.setLinkRitorno();

    //=======================
    // Recupero l'Evento.
    //=======================
    EventoModel lEveMod = null;
    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrlEv.ExRicercaEventoByKey( getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    setRequestAttribute("provvedimento", lEveMod);
    
    //==============================
    // Recupero le Opposizioni (04)
    //==============================
    IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
    Vector <ImpugnazioneModel> lImpugnazioni = lCtrlImp.ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(lEveMod.getIdEvento(),lEveMod.getCodTipoProvvedimento(), new String[]{"04"}, null  );
    setRequestAttribute("impugnazioni", lImpugnazioni);


    //==========================================================================
    // Recupero dell'Ordinanza/Decreto per visualizzare i dati iin jsp

    String aNumOrdDec = "", aDataOrdDec = "", aDataDeposito = "";
    if (lEveMod.getCodTipoProvvedimento().compareTo("03")==0)  {
      IDepositoOrdinanzaPc lCtrlDO = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      DepositoOrdinanzaPcModel lDOMod = null;
      lDOMod = lCtrlDO.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());
      
      aNumOrdDec = lDOMod.getAnnoS3().toString()+"/"+lDOMod.getNumS3().toString();
      aDataOrdDec = DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy");
      aDataDeposito = DateUtils.getDateToString(lDOMod.getDataDeposito(), "dd-MM-yyyy");
    }
    else if (lEveMod.getCodTipoProvvedimento().compareTo("02")==0) {
      IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
      DepositoDecretoModel lDDMod = null;
      lDDMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
      
      aNumOrdDec = lDDMod.getAnnoS72().toString()+"/"+lDDMod.getNumS72().toString();
      aDataOrdDec = DateUtils.getDateToString(lDDMod.getDataEmissione(), "dd-MM-yyyy");
      aDataDeposito = DateUtils.getDateToString(lDDMod.getDataDeposito(), "dd-MM-yyyy");
    }   
   
    setRequestAttribute("numOrdDec", aNumOrdDec );
    setRequestAttribute("dataOrdDec", aDataOrdDec );
    setRequestAttribute("dataDeposito", aDataDeposito );


    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return PG_ELENCO_OPPOSIZIONI;
  }

}