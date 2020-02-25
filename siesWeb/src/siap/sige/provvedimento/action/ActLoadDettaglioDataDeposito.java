package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioDataDeposito</p>
* <p>Description: Classe Action per la load dettaglio del Deposito Provvedimento</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class ActLoadDettaglioDataDeposito extends ActionSige implements ICostantiProvvedimentoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    //Bottone di ritorno
    setLinkRitorno();

    DocumentoAllegatoModel lDocAllMod = new DocumentoAllegatoModel();
    // Recupero dell'ID del Documento Allegato dalla request.
    if (!(isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_ALLEGATO) ))
    {
    	String lIdDocAll = getRequestStringParameter(CAMPO_ID_DOCUMENTO_ALLEGATO);

    	// Lettura del Documento Allegato.
    	IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
    	lDocAllMod = lCtrlDA.ExRicercaDocumentoAllegatoByKey(new BigDecimal( lIdDocAll ));
    	if (lDocAllMod == null || lDocAllMod.getIdDocumentoAllegato() == null)
    		throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato");
    }
    // Recupero dell'ID dell'evento correlato al Documento Allegato dalla request.
    else if (!(isRequestParameterNullObj(CAMPO_ID_EVENTO_GENERATO) ))
    {
    	String lIdEveDocAll = getRequestStringParameter(CAMPO_ID_EVENTO_GENERATO);

    	// Lettura del Documento Allegato.
    	IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
    	lDocAllMod = lCtrlDA.ExRicercaDocumentoAllegatoByKeyEvento(new BigDecimal( lIdEveDocAll ) );
    	if (lDocAllMod == null || lDocAllMod.getIdDocumentoAllegato() == null)
    		throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato");
    	
    }
    setRequestAttribute("documentoAllegato", lDocAllMod);

    // Lettura del Provvedimento.
    IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
    ProvvedimentoSigeEventoModel lProvEveMod = lCtrl.ExRicercaProvvedimentoByIdEvento(lDocAllMod.getEveIdEvento());
    if (lProvEveMod == null || lProvEveMod.getProvvedimento().getIdProvvedimentoSige() == null)
      throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Provvedimento");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(">>>>> IdProvvedimento = "+lProvEveMod.getProvvedimento().getIdProvvedimentoSige());
    // Il Provvedimento si mette in sessione.
    setRequestAttribute("ProvvedimentoEvento", lProvEveMod);
    setSessionAttribute("ProvvedimentoEvento", lProvEveMod);
    
    // Lettura delle notifiche.
    INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
    Vector <?>lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento (lProvEveMod.getProvvedimento().getIdEventoGenerato());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(">>>>> Numero notifiche = "+lVect.size());
    setRequestAttribute("notifiche", lVect);

    // Lettura Evento Provvedimento
    IEvento lCtrlEve =  SICOLookupRemote.getEventoRemote();
    EventoModel lEve = lCtrlEve.ExRicercaEventoByKey(lDocAllMod.getEveIdEvento());
    setRequestAttribute("evento", lEve);

    // Modificabilità, Stampabilità e Trasferibile
    String lModificabile  = "NO";
    String lStampabile    = "NO";
    String lTrasferibile  = "NO";

    // Stampabilità
    if (lEve.getFlagDocumentoRegistrato() != null && !lEve.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) {
        if( lDocAllMod.getFlagDocumentoRegistrato() == null  || lDocAllMod.getFlagDocumentoRegistrato().compareTo("N") == 0 ) {
            // Modificabilità e Stampabilità coincidono
            lStampabile   = "SI";
            lModificabile = "SI";
        } else
           lTrasferibile = "SI";
    }
    
    setRequestAttribute("Modificabile",lModificabile);
    setRequestAttribute("Stampabile",lStampabile);
    setRequestAttribute("Trasferibile",lTrasferibile);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return PG_LOAD_DETTAGLIO_DEPOSITO;
  }
}