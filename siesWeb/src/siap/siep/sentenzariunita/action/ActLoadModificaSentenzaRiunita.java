package siap.siep.sentenzariunita.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaSentenzaRiunita</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActLoadModificaSentenzaRiunita extends ActionSiap
		implements ICostantiSentenzaRiunita
{
	public String processRequest() throws F3BException
	{
    // Parse della request
		BigDecimal lId = super.getRequestBigDecimalParameter(CAMPO_ID_SENTENZA_RIUNITA);

		// Riempie il model
		SentenzaRiunitaModel lSmod = new SentenzaRiunitaModel();

		lSmod.setIdSentenzaRiunita(lId);
		lSmod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Chiama il contreller
		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();

		SentenzaRiunitaModel lSmodRet = lCtrl.ExRicercaSentenzaRiunitaByKey(lSmod.getIdSentenzaRiunita());

		// Inserire Eventuali ComboBOX
    // 23/06/2010 Sostituzione Elenco Autorità Emittenti
 		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), lSmodRet.getCodTipoAutoritaEmittente());
  	Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), lSmodRet.getCodTipoAutoritaEmittente());

		// Imposta i provvedimenti Rif.
		setRequestAttribute("autoritaEmi", "" + lOption);

		setRequestAttribute("sentenza", lSmodRet);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCISENTENZARIUNITA; //restituisce la jsp di VIEW
	}
}