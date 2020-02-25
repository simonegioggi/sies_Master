package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadComunicazioniEsecSorv extends ActionModuloCumulo implements ICostantiModuloCumulo {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiFinaliCumulo = super.getDatiFinaliCumuloAggregato();

		if (lDatiFinaliCumulo.getProvvedimentoCumulo() == null
				|| lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Nessuna Provvedimento di cumulo associato all'istruttoria corrente. Impossibile inserire le comunicazioni");
		} else if ("A".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			// throw new SIEPException (SIEPException.USER_MESSAGE, "Il Provvedimento di Cumulo risulta
			// annullato. Impossibile inserire le comunicazioni");
		} else if (!"S".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Provvedimento di cumulo non risulta validato. Impossibile inserire le comunicazioni");
		}

		// Se esiste Evento non validato di comunicazione alle procure (01-12-0670)
		// per stessa istruttoria apri diettamente il dettaglio
		/// isEventoNonValidatoPerCumulo(); //????

		setRequestAttribute("ProvvedimentoCumulo", lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento());

		// ==============================================================================================================================
		// 29/03/2019 Recupero dei soli provvedimenti di Comunicazione Cumulo (COD_MOTIVO = 0670) collegati a
		// Provv. di Cumulo Corrente
		// A tale scopo si utilizza l'ultimo elemento dell'array CodMotivo per contenere l'EVE_ID_EVENTO da
		// filtrare.
		// ==============================================================================================================================
		IEventoSimeone lCtrlS = SICOLookupRemote.getEventoSimeoneRemote();
		String[] lTipProvv = null;
		String[] lTipEvento = null;

		// 29/03/2019 Va recuperato la chiave (ID_EVENTO) del provvedimento di Cumulo corrente.
		BigDecimal lIdEventoCumulo = null;
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			lIdEventoCumulo = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// 08/04/2019 Se provengo da cancellazione, l'IdEventoCumulo ha un diverso nome parametro.
		if (!isRequestParameterNullObj("idEventoCumulo"))
			lIdEventoCumulo = getRequestBigDecimalParameter("idEventoCumulo");

		String[] lCodMotivo = { "0670", "include", StringUtils.getParteIntera(lIdEventoCumulo) };
		// siesLogger.debug (" --XX-- ultimo lCodMotivo = "+ lCodMotivo[lCodMotivo.length-1]);

		Vector eventiComunicazioniCumulo = new Vector();
		try {
			eventiComunicazioniCumulo = lCtrlS.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(
					lIstruttoriaModel.getFasSieIdFascicoloSiep(), getUfficioUtenteConnesso(), lTipEvento,
					lTipProvv, lCodMotivo, 1, "EM");
		} catch (F3BException fex) {
			// Se non trova eventi rilancia una eccezione che non può essere bloccante
			if (fex.getErrorCode() != F3BException.USER_MESSAGE)
				throw fex;
		}
		setRequestAttribute("eventi", eventiComunicazioniCumulo);
		// siesLogger.debug (" --XX-- size di eventi comunicazioni = "+ eventiComunicazioniCumulo.size());

		EventoModel lEveMod = lCtrlS
				.ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
						lIstruttoriaModel.getFasSieIdFascicoloSiep(), lTipEvento, lTipProvv, lCodMotivo,
						"EM");
		if (lEveMod == null || lEveMod.getIdEvento() == null) {
			lEveMod = new EventoModel();
		}
		setRequestAttribute("eventocancellareannullare", lEveMod);
		siesLogger.debug(" --XX-- size di evento eventocancellareannullare  = " + lEveMod.toString());

		// ====================================================================================================================
		// 27/08/2018 Recupero gli uffici destinatari di comunicazioni già inviate per il provvedimento di
		// cumulo/istruttoria
		// ====================================================================================================================
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		String[] lTipoEvento = { "01" };
		Vector eventiComunicazioni = new Vector();
		try {
			eventiComunicazioni = lCtrl.ExRicercaEventoNotificaByFascicoloSiep(
					lIstruttoriaModel.getFasSieIdFascicoloSiep(), lTipoEvento);
		} catch (F3BException fex) {
			// Se non trova eventi rilancia una eccezione che non può essere bloccante
			if (fex.getErrorCode() != F3BException.USER_MESSAGE)
				throw fex;
		}

		// Carico in un ArrayList gli Uffici già destinatari di comunicazione.
		ArrayList<String> lIdUfficiDestCom = new ArrayList<>();
		ArrayList<BigDecimal> lIdTitoliDestCom = new ArrayList<>();
		// Seleziono solo gli eventi di tipo comunicazione collegati all'istruttoria
		if (!eventiComunicazioni.isEmpty()) {
			Iterator itx = eventiComunicazioni.iterator();
			while (itx.hasNext()) {
				EventoModel lEvento = (EventoModel) itx.next();
				if (lEvento.getCodTipoProvvedimento().equals("12") && lEvento.getCodMotivo().equals("0670")
						&& lEvento.getIstruIdIstruttoriaCumulo()
								.equals(lIstruttoriaModel.getIdIstruttoriaCumulo())
						&& (lEvento.getFlagDocumentoRegistrato() == null
								// non validati
								|| (lEvento.getFlagDocumentoRegistrato() != null
										// oppure validati non annullati
										&& lEvento.getFlagDocumentoRegistrato().compareTo("A") != 0))) {
					EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKey(lEvento.getIdEvento());
					if (lEveNot.getNotifiche() != null) {
						NotificaModel[] lNotifiche = lEveNot.getNotifiche();

						for (int j = 0; j < lNotifiche.length; j++) {
							NotificaModel lNotMod = lNotifiche[j];
							if (lNotMod.getUffCodUfficio() != null
									&& !lIdUfficiDestCom.contains(lNotMod.getUffCodUfficio()))
								lIdUfficiDestCom.add(lNotMod.getUffCodUfficio());

							if (lNotMod.getCurIdCuratore() != null)
								lIdTitoliDestCom.add(lNotMod.getCurIdCuratore());
						}
					}
				}
			}
		}

		setRequestAttribute("ufficiDestinatariComunicazioni", lIdUfficiDestCom);
		setRequestAttribute("titoliGiaComunicati", lIdTitoliDestCom);

		String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();

		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector<TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
				lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);
		Vector<TitoloCumulatoModel> lListaOrdinata = getListaOrdinata(lListaTitoli);

		setRequestAttribute("ListaTitoli", lListaOrdinata);
		// setRequestAttribute("ListaTitoli", lListaTitoli);
		setRequestAttribute("Provvedimento", lDatiFinaliCumulo.getProvvedimentoCumulo());

		// Carica gli Uffici della sorveglianza destinatari di Notifiche.
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffModel = null;
		Vector<UfficioModel> lUffNotifiche = new Vector<>();
		String[] lUfficiSorv = new String[] { "UDS", "TDS", "UDSM", "TDSM" };

		if (lDatiFinaliCumulo.getProvvedimentoCumulo().getNotifiche() != null) {
			for (int i = 0; i < lDatiFinaliCumulo.getProvvedimentoCumulo().getNotifiche().length; i++) {
				if (lDatiFinaliCumulo.getProvvedimentoCumulo().getNotifiche()[i].getUffCodUfficio() != null) {
					lUffModel = lUffCtrl.ExRicercaUfficioByCod(
							lDatiFinaliCumulo.getProvvedimentoCumulo().getNotifiche()[i].getUffCodUfficio());
					if (Arrays.asList(lUfficiSorv).contains(lUffModel.getCodTipoUfficio()))
						lUffNotifiche.add(lUffModel);
				}
			}
		} else {
			// throw new SIEPException (SIEPException.USER_MESSAGE, "Nessuna notifica presente sul
			// provvedimento. Impossibile inserire comunicazioni");
		}

		setRequestAttribute("UfficiSorveglianza", lUffNotifiche);

		setRequestAttribute("nextAction", "siap.siep.modulocumulo.action.ActLoadComunicazioniEsecSorv");

		return PG_GRIGLIA_COMUNICAZIONI_PROCURE;
	}

	/**
	 * Ordina la lista per stessa procura Elimina i titoli dell'ufficio corrente
	 */
	private Vector<TitoloCumulatoModel> getListaOrdinata(Vector<TitoloCumulatoModel> aListaTitoli)
			throws F3BException {
		Vector<TitoloCumulatoModel> lListaOrdinata = new Vector<>();

		String lCodUffCumulante = getCodUfficioUtenteConnesso();

		// Lista Procure (distinct)
		ArrayList<String> lListaProcure = new ArrayList<>();

		// - Scorro la lista delle procure
		siesLogger.debug("Carico la lista delle procure (distinc) a cui inviare i dati ");
		for (TitoloCumulatoModel lTitolo : aListaTitoli) {
			ProcedimentoCumulatoModel lProcCum = lTitolo.getProcedimentoCumulato();

			if (lProcCum == null) {
				siesLogger.debug("Assente procedimento cumulato salto il titolo ");
				continue; // Salto
			} else if (lCodUffCumulante.equals(lProcCum.getCodUfficioFasCumulato())) {
				siesLogger.debug("Titolo stesso uffico procedimento cumulato salto il titolo ");
				continue; // Salto
			}

			if (!lListaProcure.contains(lProcCum.getCodUfficioFasCumulato())) {
				siesLogger.debug("Aggiungo " + lProcCum.getDescrUfficioFasCumulato());
				lListaProcure.add(lProcCum.getCodUfficioFasCumulato());
			}
		}
		siesLogger.debug("Totale uffici a cui mandare le comunicazioni = " + lListaProcure.size());

		siesLogger.debug("Ordino la lista");
		for (int i = 0; i < lListaProcure.size(); i++) {
			String lCodUfficio = lListaProcure.get(i);
			siesLogger.debug("lCodUfficio = " + lCodUfficio);

			// Scorro la lista dei titoli
			for (TitoloCumulatoModel lTitolo : aListaTitoli) {
				ProcedimentoCumulatoModel lProcCum = lTitolo.getProcedimentoCumulato();

				if (lProcCum == null) {
					siesLogger.debug("Assente procedimento cumulato salto il titolo ");
					continue; // Salto
				} else if (lCodUffCumulante.equals(lProcCum.getCodUfficioFasCumulato())) {
					siesLogger.debug("Titolo stesso uffico procedimento cumulato salto il titolo ");
					continue; // Salto
				}

				if (lCodUfficio.equals(lProcCum.getCodUfficioFasCumulato())) {
					siesLogger.debug("Aggiungo il titolo " + lTitolo.getAnnoSentenza() + "/"
							+ lTitolo.getNumeroSentenza());
					lListaOrdinata.add(lTitolo);
				}
			}
		}

		return lListaOrdinata;
	}

}