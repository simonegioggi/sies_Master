package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * @author Caporizzo classe che gestisce l'eliminazione dell'esito di un opposione/ricorso
 *
 */
public class ActEliminaEsitoImpugnazioneSige extends ActionSige
		implements ICostantiImpugnazioneSige, ICostantiFascicoloSige {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private FascicoloSigeEstesoModel lFasEst;

	public String processRequest() throws Exception {

		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();
		lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		BigDecimal idFascicolo = lFasEst.getFascicoloSige().getIdFascicoloSige();

		IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = lCtrl
				.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		impugnazione.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		impugnazione.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		impugnazione.setDataAggiornamento(DateUtils.getSysDate());
		impugnazione.setFlagValidazioneEsito("N");
		impugnazione.setCodTenoreDecisione("");
		impugnazione.setDataDecisione(null);
		lCtrl.ExModificaImpugnazione(impugnazione);

		// devo modificare anche il cod_esito sulla tabella EVENTO per idEventoGenerato
		/* EventoModel evento = */getEvento(impugnazione);

		if (impugnazione.getIdProvvedimentoGenerato() != null) {
			impugnazione = lCtrl
					.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
			lCtrl.ExEliminaEsitoImpugnazione(impugnazione, idFascicolo);
		}

		impugnazione = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		setRequestAttribute("impugnazione", impugnazione);
		String ritorno = super.ritornoDopoCancellazione("L'esito impugnazione e' stata Cancellata.", null);
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return ritorno;
	}

	/**
	 * @param impugnazione
	 * @return
	 * @throws F3BException
	 */
	private EventoModel getEvento(ImpugnazioneSigeModel impugnazione) throws F3BException {

		EventoModel evento = new EventoModel();
		if (impugnazione.getProvvedimentoSigeGenerato() != null) {
			evento = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
			evento.setCodEsito(impugnazione.getCodTenoreDecisione());
			evento.setDataAggiornamento(new Date());
			evento.setCodOperatoreAggiornamento(super.getUtenteConnesso().getUserId());
			evento.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		}
		return evento;
	}

}