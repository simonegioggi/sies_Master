package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.GeneraAvvisoPagoPAUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: ActLoadGeneraAvvisoPagoPA 
 * Description: Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActLoadGeneraAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	// info per il log dedicato
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	private static String codUtente = null;
	private static String codUfficio = null;

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// info utente ufficio collegato
		codUtente = getCodUtenteConnesso();
		codUfficio = getCodUfficioUtenteConnesso();

		// BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// siesLogger.debug("ID_EVENTO = " + idEvento);
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		// Ricerca lo stato dei pagamenti per id fascicolo
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		boolean isElencoEmpty = elencoStatoPagamenti.isEmpty();
		// Ricerca i pagamenti per idFascicolo
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo, "");
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			EventoModel em = listaRichiestaBollettini.firstElement().getEvento();
			setRequestAttribute("evento", em);
			// MEV_2023-33: controllo notifica al condannato
			IEvento ie = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(em.getIdEvento());
			NotificaModel[] nms = enm.getNotifiche();
			String dataNotificaCondannato = "";
			for (int i = 0; i < nms.length; i++) {
				// NOTIFICA AL CONDANNATO
				if (nms[i].getAvvIdAvvocatoFascicoloSiep() == null
						&& nms[i].getIdCivilmenteObbligato() == null)
					dataNotificaCondannato = DateUtils.getDateToString(nms[i].getDataAvvenutaNotifica(), "dd/MM/yyyy");
			}
			setRequestAttribute("dataNotificaCondannato", dataNotificaCondannato);

			if (isElencoEmpty) {
				int progressivoRata = 1;
				// dalle rateizzazioni creo i bollettini
				Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
				while (iter.hasNext()) {
					RateizzazionePPModel rata = iter.next();
					for (int i = 0; i < rata.getNumeroRate().intValue(); i++) {
						// MEV_2023-33: cambiata firma del metodo con la data Emissione OEIP
						BollettinoPagopaModel bpm = GeneraAvvisoPagoPAUtil.popolaBollettino(rata, codUtente,
								codUfficio, "PN", progressivoRata, em.getDataEmissione());
						ibp.ExInserisciBollettinoPagopa(bpm);
						progressivoRata++;
					}
				}
				elencoStatoPagamenti = ibp.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
			}
		}

		// poi li imposto nella pagina
		setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);
		// MEV_2023-33: aggiunte impostazioni di attributo
		boolean isUnico = !elencoStatoPagamenti.isEmpty() && elencoStatoPagamenti.size() == 1
				&& "U".equals(elencoStatoPagamenti.get(0).getTipoRateizzazione());
		setRequestAttribute("isRateale", !isUnico);
		boolean isSoloPrimaRata = false;
		if (!isUnico) {
			Iterator<BollettinoPagopaModel> itx = elencoStatoPagamenti.iterator();
			int contaIUV = 0;
			while (itx.hasNext()) {
				BollettinoPagopaModel bpm = itx.next();
				if (Utils.isPresent(bpm.getIuv()))
					contaIUV++;
			}
			if (contaIUV == 1)
				isSoloPrimaRata = true;
			BollettinoPagopaModel primaRata = elencoStatoPagamenti.get(0);
			BollettinoPagopaModel rataSuccessiva = elencoStatoPagamenti.get(1);
			if (!Utils.isNullObj(primaRata.getDataGenerazioneBollettino())
					&& !Utils.isNullObj(rataSuccessiva.getDataGenerazioneBollettino())
					&& !DateUtils.isEqualsLocalDateTime(primaRata.getDataGenerazioneBollettino(),
							rataSuccessiva.getDataGenerazioneBollettino()))
				isSoloPrimaRata = true;
			else if (contaIUV != 1)
				isSoloPrimaRata = false;
		}
		setRequestAttribute("isSoloPrimaRata", isSoloPrimaRata);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_GENERA_AVVISO_PAGOPA;
	}

}