package siap.siep.jms.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage; // STUB 07/03/2005
import siap.sico.lock.model.LockModel;
import siap.sico.jms.model.PresaInCaricoModel;
import siap.sico.web.ActionSiap;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActPresaInCaricoFascicoloSiep
 * </p>
 * <p>
 * Description: Azione di presa in Carico di un fascicolo ricercato all'interno di un'altra BDI
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActPresaInCaricoFascicoloSiep extends ActionSiap implements ICostantiJMS {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
	public String processRequest() throws Exception {
		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricofascicolo",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questo procedimento SIEP è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		if (lMess.getCodBdiDestinataria().equals(lMess.getCodBdiMittente())) {
			// setRequestAttribute("IDEvento", lEveId.toString());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento richiesto è di un ufficio di questa BDI. Il trasferimento è completato!");

			return IWebConstants.PG_MESSAGE;

		}

		/* UfficioModel lBDI = */this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());		
		
		MessaggioModel lMessReturn = null;
		IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
		
		// MEV_2024-DNA - Si passano al controller anche le informazione su data utente e ufficio 
		//                che precede alla presa in carico
		PresaInCaricoModel lPresaIncaricoModel = new PresaInCaricoModel();
		lPresaIncaricoModel.setDataPresaInCarico (DateUtils.getSysDate());
		lPresaIncaricoModel.setCodOperatorePresaInCarico (getCodUtenteConnesso());
		lPresaIncaricoModel.setCodUfficioPresaInCarico (getCodUfficioUtenteConnesso());
		
		//lMessReturn = lPres.ExInserisciFascicoloSiep(lMess);
		siesLogger.debug(lPresaIncaricoModel);
		lMessReturn = lPres.ExInserisciFascicoloSiep (lMess, lPresaIncaricoModel);
		// MEV_2024-DNA - FINE

		// STUB 07/03/2005 Recupero del Fascicolo per il dettaglio Fascicolo SIEP.
		ParserMessage lParser = null;

		if (lMess.getCodEsito().equals(ICostantiJMS.POSITIVO)
				|| lMess.getCodEsito().equals(ICostantiJMS.TROVATO)) {
			lParser = new ParserMessage(lMess.getTreeModel());

			if (lParser.getDettaglioFascicoloSiep() != null
					&& lParser.getDettaglioFascicoloSiep().getFascicoloSiep() != null) {
				this.setRequestAttribute("dettagliofascicolo", lParser.getDettaglioFascicoloSiep());
			}
		}

		this.setRequestAttribute("Messaggio", lMessReturn);

		return f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/jms/RapportoTrasferimentoFascicolo.jsp";
	}

}