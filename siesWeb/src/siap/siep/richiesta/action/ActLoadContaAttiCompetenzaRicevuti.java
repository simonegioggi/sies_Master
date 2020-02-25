package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadContaAttiCompetenzaRicevuti
 * </p>
 * <p>
 * Description: Classe Action utilizzata UNICAMENTE per invocare il conteggio degli atti ricevuti per
 * competenza per assorbimento in cumulo
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadContaAttiCompetenzaRicevuti extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws Exception {

		MessaggioModel lMessaggio = new MessaggioModel();
		lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio(ICostantiJMS.RICHIESTA);

		lMessaggio.setFlagVisto("N"); // non ancora presi in carico
		lMessaggio.setCodUfficioMittente("");

		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);

		// Filtro sulla data di trasmissione
		Date lDataInizio = null;
		Date lDataFine = null;

		// le Date di INIZIO e FINE RICERCA si impostano a 2 mesi precedenti, proprio come
		// viene fatto nella 'ActLoadRicercaAttiCompetenzaRicevuti' che mostra anche l'elenco degli atti
		lDataInizio = DateUtils.getEnneMonthBefore(DateUtils.getSysDate(), 2);
		lDataFine = DateUtils.getSysDate();

		// Ricerca Messaggi
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		BigDecimal lCountRisultati = null;
		lCountRisultati = lCrtl.ExCountMessaggiRicevutiPaged(lMessaggio, lListaTipoOperazione, lDataInizio,
				lDataFine, 0);

		if (lCountRisultati != null)
			setRequestAttribute("contaAttiRicevuti", lCountRisultati + "");
		else
			setRequestAttribute("contaAttiRicevuti", "0");

		return ICostantiMessaggio.PG_CONTA_MESSAGGI_RICEVUTI_TRASMISSIONE_COMPETENZA;

	}

}