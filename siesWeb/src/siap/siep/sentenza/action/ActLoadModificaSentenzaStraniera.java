package siap.siep.sentenza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSentenza</p>
 * <p>Description: Classe Action che effettua la load dell'inserisci Sentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadModificaSentenzaStraniera extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    BigDecimal lId = super.getRequestBigDecimalParameter(CAMPO_ID_SENTENZA);

    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();

    SentenzaModel lSmodRet = lCtrl.ExRicercaSentenzaByKey(lId);

    // 23/06/2010 Sostituzione Elenco Autorità Emittenti
 		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    setRequestAttribute("autoritaEmi", "" + lOption );

    setRequestAttribute( "sentenza", lSmodRet );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    return PG_LOAD_INSERISCISENTENZASTRANIERA; //restituisce la jsp di VIEW
  }
}
