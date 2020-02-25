package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

/**
 * <p>
 * Title: ActInserisciImpugnazione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Impugnazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciEsitoImpugnazioneSige extends ActionSiap
		implements ICostantiImpugnazioneSige, ICostantiFascicoloSige {

	/**
	 * Azione di Inserimento dell' Impugnazione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	private FascicoloSigeEstesoModel lFasEst;

	public String processRequest() throws F3BException {

		lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		BigDecimal idFascicolo = lFasEst.getFascicoloSige().getIdFascicoloSige();
		BigDecimal idImpugnazione = super.getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
		IImpugnazioneSige iCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = iCtrl.ExRicercaImpugnazioneByKey(idImpugnazione);
		String codiceDecisione = super.getRequestStringParameter(CAMPO_COD_TENORE_DECISIONE);
		impugnazione.setAnnotazione(getRequestStringParameter(ICostantiImpugnazioneSige.CAMPO_NOTE));
		impugnazione.setDataDecisione(getRequestDateParameter(CAMPO_ANNO_DATA_DECISIONE,
				CAMPO_MESE_DATA_DECISIONE, CAMPO_GIORNO_DATA_DECISIONE));

		if (!super.isRequestParameterNullEmptyObj(CAMPO_ANNO_DATA_RESTITUZIONE_ATTI))
			impugnazione.setDataRestituzioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE_ATTI,
					CAMPO_MESE_DATA_RESTITUZIONE_ATTI, CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI));

		impugnazione.setCodTenoreDecisione(codiceDecisione);
		impugnazione.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		impugnazione.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		impugnazione.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		impugnazione.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());

		if (impugnazione != null && impugnazione.getCodTipoImpugnazione() != null
				&& impugnazione.getCodTipoImpugnazione().equals("04")) {
			impugnazione.setNotifiche(this.getNotificheOpposizione(impugnazione));
		} else {
			impugnazione.setNotifiche(this.getNotifiche(impugnazione));
		}

		EventoModel evento = this.getEvento(impugnazione);

		if (impugnazione.getIdProvvedimentoGenerato() == null)
			impugnazione = iCtrl.ExImpostaEsitoImpugnazione(impugnazione, evento, idFascicolo);
		else {
			impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().setEvento(evento);
			iCtrl.ExAggiornaEsitoImpugnazione(impugnazione, idFascicolo);
		}

		String redirect = this.getPage(codiceDecisione, impugnazione);
		String esito = impugnazione.getCodTenoreDecisione();

		// Modifica del 11/11/2016
		// Se l'esito precedente ha aggiornato lo stato del fascicolo
		// impostandolo uguale a 14 o 15 o 16 e l'esito attuale è diverso
		// da 02-10-12, bisogna aggiornare nuovamente lo stato del fascicolo
		// e settarlo uguale a 07 (Emesso Provvedimento)

		// @emma 20082018 intervento post COLLAUDO 11.2 (RECUPERO L'ULTIMO STATO FASCICOLO DAL DB perchè
		// l'oggetto in sessione non è sempre aggiornato)
		IFascicoloSige ctrlFasc = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeModel fSigeAggiornato = ctrlFasc.ExRicercaFascicoloSigeByKey(idFascicolo);
		lFasEst.setFascicoloSige(fSigeAggiornato);
		setSessionAttribute("FascicoloSigeEsteso", lFasEst);

		if (lFasEst != null && lFasEst.getFascicoloSige() != null
				&& lFasEst.getFascicoloSige().getCodStatoFascicolo() != null
				&& (lFasEst.getFascicoloSige().getCodStatoFascicolo().equals("14")
						|| lFasEst.getFascicoloSige().getCodStatoFascicolo().equals("15")
						|| lFasEst.getFascicoloSige().getCodStatoFascicolo().equals("16")
						// @emma 26072018 intervento post COLLAUDO 11.2 (anche per lo stato 'ricorso' e
						// 'Converte in Ricorso in Cassazione' occorre aggiornare lo stato fascicolo se
						// l'esito è uno di quelli sotto)
						|| lFasEst.getFascicoloSige().getCodStatoFascicolo().equals("18")
						|| lFasEst.getFascicoloSige().getCodStatoFascicolo().equals("19"))
				&& (esito.equals(COD_ESITO_ANNULLA_SENZA_RINVIO)
						// @emma 21082018 intervento post COLLAUDO 11.2, per esito
						// COD_ESITO_ANNULLA_PARZIALMENTE lo stato del fascicolo non deve essere aggiornato ad
						// EmessoProvvedimento(vedi doc di nunzia esiti procedimento.docx)
						/* || esito.equals(COD_ESITO_ANNULLA_PARZIALMENTE) */
						// @emma 21082018 intervento post COLLAUDO 11.2, per esito
						// DICHIARA_INAMISSIBILE_IL_RICORSO lo stato del fascicolo non deve essere aggiornato
						// ad EmessoProvvedimento(vedi doc di nunzia esiti procedimento.docx)
						/* || esito.equals(COD_ESITO_DICHIARA_INAMISSIBILE_IL_RICORSO) */
						|| esito.equals(COD_ESITO_RIGETTA)
						// @emma 21082018 intervento post COLLAUDO 11.2, per esito COD_ESITO_RETTIFICA lo
						// stato del fascicolo non deve essere aggiornato ad EmessoProvvedimento(vedi doc di
						// nunzia esiti procedimento.docx)
						/* || esito.equals(COD_ESITO_RETTIFICA) */
						// @emma 21082018 intervento post COLLAUDO 11.2, per esito COD_ESITO_ACCOGLIE lo stato
						// del fascicolo non deve essere aggiornato ad EmessoProvvedimento(vedi doc di nunzia
						// esiti procedimento.docx)
						/* || esito.equals(COD_ESITO_ACCOGLIE) */
						// @emma 21082018 intervento post COLLAUDO 11.2, per esito DICHIARA_NDP_NLP lo stato
						// del fascicolo non deve essere aggiornato ad EmessoProvvedimento(vedi doc di nunzia
						// esiti procedimento.docx)
						/* || esito.equals(COD_ESITO_DICHIARA_NDP_NLP) */

						|| esito.equals(COD_ESITO_DICHIARA_INCOPETENZA)
						|| esito.equals(COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE)
						|| esito.equals(COD_ESITO_DICHIARA_INAMISSIBILE))) {
			// aggiornaStatoFascicolo (COD_EMESSO_PROVVEDIMENTO, null);
			aggiornaStatoFascicolo(COD_EMESSO_PROVVEDIMENTO, impugnazione.getDataDecisione());
		}

		if (esito.equals(COD_ESITO_ANNULLA_CON_RINVIO)) {
			aggiornaStatoFascicolo(COD_ACCOGLIE_FISSA_UDIENZA, impugnazione.getDataDecisione());
		}

		if (esito.equals(COD_ESITO_ACCOGLIE_FISSA_UDIENZA)) {
			redirect = handleAccoglieEFissaUdienza(impugnazione);
		}

		if (esito.equals(COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE)) {
			redirect = handleConverteRicorsoCassazione(impugnazione, idFascicolo);
		}

		if (esito.equals(COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE)) {
			aggiornaStatoFascicolo(COD_RICORSO_CONVERTITO_OPPOSIZIONE, null);
		}

		// 13/11/2018 intervento post COLLAUDO 11.2 (email del 20/9/2018 di Alfieri) - vedi allegato Ricorso-
		// esiti.docx
		if (esito.equals(COD_ESITO_ACCOGLIE)) {
			aggiornaStatoFascicolo(null, impugnazione.getDataDecisione());
		}
		// 27/11/2018 intervento post COLLAUDO 11.2 (by Nunzia. se metto esito COD_ESITO_DICHIARA_INAMISSIBILE
		// lo stato deve essere opposizione )
		if (esito.equals(COD_ESITO_DICHIARA_INAMISSIBILE)) {
			aggiornaStatoFascicolo(COD_OPPOSIZIONE, impugnazione.getDataDecisione());
		}

		return redirect;
	}

	private String getPage(String codiceDecisione, ImpugnazioneSigeModel impugnazione) {

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige");
		lPage.setParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE,
				"" + impugnazione.getIdImpugnazioneSige());
		lPage.setParameter(IWebConstants.LINK_RITORNO, "10");
		return lPage.toString();
	}

	private EventoModel getEvento(ImpugnazioneSigeModel impugnazione) throws F3BException {

		EventoModel evento = new EventoModel();
		if (impugnazione.getProvvedimentoSigeGenerato() != null) {
			evento = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
			evento.setCodEsito(impugnazione.getCodTenoreDecisione());
			evento.setDataAggiornamento(new Date());
			evento.setCodOperatoreAggiornamento(super.getUtenteConnesso().getUserId());
			evento.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
			return evento;
		}

		evento.setCodTipoEvento("13");
		evento.setCodTipoProvvedimento("15");
		evento.setCodEsito("-");
		evento.setCodMotivo("-");
		evento.setCodTipoUfficioDestinatario("-");
		evento.setCodLuogoDestinatario("-");
		evento.setCodLuogoEmittente("-");
		evento.setCodTipoUfficioEmittente("-");
		evento.setCodUfficioDestinatario(super.getCodUfficioUtenteConnesso());
		evento.setCodUfficioEmittente(super.getCodUfficioUtenteConnesso());
		evento.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
		evento.setCodOperatoreInserimento(super.getUtenteConnesso().getUserId());
		return evento;
	}

	private String handleAccoglieEFissaUdienza(ImpugnazioneSigeModel impugnazione) throws F3BException {

		FascicoloSigeEstesoModel fascicoloEsteso = (FascicoloSigeEstesoModel) super.getSessionAttribute(
				"FascicoloSigeEsteso");
		FascicoloSigeModel fascicolo = fascicoloEsteso.getFascicoloSige();
		this.aggiornaStatoFascicolo(COD_ACCOGLIE_FISSA_UDIENZA, null);
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.udienza.action.ActLoadInserisciFissazioneUdienza&" + CAMPO_CHIAVE_ANNO
				+ "=" + fascicolo.getChiaveAnno().toString() + "&" + CAMPO_CHIAVE_PROGR + "="
				+ fascicolo.getChiaveProgr() + "&inserisci=Si&provenienza=EsitoImpugnazione");
		return lRedirigi.toString();
	}

	private String handleConverteRicorsoCassazione(ImpugnazioneSigeModel impugnazione, BigDecimal idFascicolo)
			throws F3BException {

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.impugnazione.action.ActLoadModificaRicorsoDaOpposizione&"
				+ ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE + "="
				+ impugnazione.getIdImpugnazioneSige());
		return lRedirigi.toString();
	}

	private void aggiornaStatoFascicolo(String stato, Date dataDefinizione) throws F3BException {

		FascicoloSigeEstesoModel fascicoloEsteso = (FascicoloSigeEstesoModel) super.getSessionAttribute(
				"FascicoloSigeEsteso");
		FascicoloSigeModel fascicolo = fascicoloEsteso.getFascicoloSige();
		IFascicoloSige ctrlFasc = SIGELookupRemote.getFascicoloSigeRemote();
		if (stato != null)
			fascicolo.setCodStatoFascicolo(stato);
		if (dataDefinizione != null)
			fascicolo.setDataDefinizione(dataDefinizione);

		fascicolo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		fascicolo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		fascicolo.setDataAggiornamento(DateUtils.getSysDate());

		RichiestaSigeModel richiesta = fascicoloEsteso.getRichiestaSige();
		richiesta.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		richiesta.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		richiesta.setDataAggiornamento(DateUtils.getSysDate());
		ctrlFasc.ExModificaFascicoloSige(fascicolo, richiesta);
		fascicoloEsteso.setFascicoloSige(fascicolo);
		setSessionAttribute("FascicoloSigeEsteso", fascicoloEsteso);
	}

	private Vector<NotificaModel> getNotifiche(ImpugnazioneSigeModel impugnazione) throws F3BException {

		Vector<NotificaModel> notifiche = new Vector<>();
		if (super.isRequestParameterNullObj(CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO))
			return notifiche;

		FascicoloSigeEstesoModel fascicoloEsteso = (FascicoloSigeEstesoModel) super.getSessionAttribute(
				"FascicoloSigeEsteso");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		EventoModel eventoNotifica = null;
		if (impugnazione.getProvvedimentoSigeGenerato() != null
				&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento() != null)
			eventoNotifica = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();

		String codDestinatarioPubblicoMinistero = super.getRequestStringParameter(
				CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO);
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		if (!codDestinatarioPubblicoMinistero.equals("-") && eventoNotifica != null) {
			NotificaModel notificaPm = new NotificaModel();
			notificaPm.setEveIdEvento(eventoNotifica.getIdEvento());
			notificaPm.setCodTipoNotifica("N");
			notificaPm.setDataInvio(impugnazione.getDataDecisione());
			notificaPm.setCodOperatoreInserimento(lCodiceOperatore);
			notificaPm.setDataInserimento(DateUtils.getSysDate());
			notificaPm.setCodUfficioInserimento(lCodiceUfficio);
			notificaPm.setCodEsito("-");
			notificaPm.setUffCodUfficio("-");
			notificaPm.setSogIdSoggetto(fascicoloEsteso.getFascicoloSige().getSogIdSoggetto());
			String descrComuneSedeUfficio = super.getRequestStringParameter(
					CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO);
			String codiceUfficioDestinatario = getCodUfficioByCodTipoUfficioDescrComune(
					codDestinatarioPubblicoMinistero, descrComuneSedeUfficio);
			notificaPm.setUffCodUfficio(codiceUfficioDestinatario);
			notifiche.add(notificaPm);
		}

		String codDestinatarioUfficioRecuperoCredito = super.getRequestStringParameter(
				CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI);
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		if (!codDestinatarioUfficioRecuperoCredito.equals("-") && eventoNotifica != null) {
			NotificaModel notificaRecupero = new NotificaModel();
			notificaRecupero.setEveIdEvento(eventoNotifica.getIdEvento());
			notificaRecupero.setCodTipoNotifica("N");
			notificaRecupero.setDataInvio(impugnazione.getDataDecisione());
			notificaRecupero.setCodOperatoreInserimento(lCodiceOperatore);
			notificaRecupero.setDataInserimento(DateUtils.getSysDate());
			notificaRecupero.setCodUfficioInserimento(lCodiceUfficio);
			notificaRecupero.setCodEsito("-");
			notificaRecupero.setUffCodUfficio("-");
			notificaRecupero.setSogIdSoggetto(fascicoloEsteso.getFascicoloSige().getSogIdSoggetto());

			AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
			lAutorita.setCodTipoAutorita(codDestinatarioUfficioRecuperoCredito);
			lAutorita.setCodSede(super.getCodComuneByDescrFlagVal(
					super.getRequestStringParameter(CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI))
							.getCodComune());
			lAutorita.setDescrSede(
					super.getRequestStringParameter(CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI));
			lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
			lAutorita.setCodUfficioInserimento(lCodiceUfficio);
			lAutorita.setDataInserimento(DateUtils.getSysDate());
			notificaRecupero.setAutoritaEsterna(lAutorita);

			notifiche.add(notificaRecupero);
		}
		return notifiche;
	}

	private Vector<NotificaModel> getNotificheOpposizione(ImpugnazioneSigeModel impugnazione)
			throws F3BException {

		// Vector per le notifiche.
		Vector<NotificaModel> lNotifiche = new Vector<>();
		// EventoModel
		// eventoNotifica=impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		BigDecimal idEvento = null;
		if (impugnazione.getProvvedimentoSigeGenerato() != null && impugnazione.getProvvedimentoSigeGenerato()
				.getProvvedimento().getIdEventoGenerato() != null) {
			idEvento = impugnazione.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato();
		}

		// if (super.isRequestParameterNullObj(CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO))
		// return notifiche;

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		String lSediSog = null;
		String lDestinatariSog = null;
		lSediSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE);
		lDestinatariSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE);

		String[] destinatarioflagSNT = getParameterValues("flagSNT");
		HashSet<Integer> hs = new HashSet<>();
		if (destinatarioflagSNT != null && destinatarioflagSNT.length > 0) {
			for (String flagId : destinatarioflagSNT) {
				hs.add(Integer.valueOf(flagId));
			}
		}

		// Codice Tipo Ufficio dell'utente connesso
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lCodiceUfficioPG = null;
		String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();

		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienzaSige.CAMPO_COD_AVVOCATO);
		String lTipoNotifica = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_TIPONOTIFICA);
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);

		// Soggetto con autorita' esterna
		if (!Utils.isNullObj(lDestinatariSog) && !Utils.isNullObj(lSediSog)) {
			if (!lDestinatariSog.equals("-") && !lSediSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSediSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
				lNotifica.setDataInvio(impugnazione.getDataDecisione());
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setSogIdSoggetto(lFasEst.getFascicoloSige().getSogIdSoggetto());
				lNotifica.setNote(lNote[0]);
				lNotifica.setEveIdEvento(idEvento);
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatariSog);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		} // end if (!Utils.isNullObj(lDestinatariSog) && !Utils.isNullObj(lSediSog)) {

		// Notifica alla "Procura Generale della Repubblica presso la Corte di Appello" nel caso del Tribunale
		// di Sorveglianza
		// oppure notifica alla "Procura della Repubblica presso il Tribunale Ordinario" nel caso del
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE)) {
			NotificaModel lNot = null;
			if (lCodTipoUfficio.equalsIgnoreCase("CASAP") || lCodTipoUfficio.equalsIgnoreCase("CAP"))
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", lDescrComune);
			else if (lCodTipoUfficio.equalsIgnoreCase("TRIBSD")) {
				lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);
				IComune lCtrl = SICOLookupRemote.getComuneRemote();
				lDescrComune = lCtrl.ExRicercaComuneByKey(lCodComune).getDescrizione();
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);
			} else
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);

			// Procura Generale dell'ufficio di riferimento dell'utente connesso
			lNot = new NotificaModel();
			lNot.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICACOMUNICAZIONE);
			lNot.setDataInvio(impugnazione.getDataDecisione());
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);
			lNot.setUffCodUfficio(lCodiceUfficioPG);
			lNot.setEveIdEvento(idEvento);
			lNotifiche.add(lNot);
		}

		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			if ((!lDestinatari[x].equals("-") && !lSedi[x].equals("")) || (hs.contains(x))) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				lNotifica.setDataInvio(impugnazione.getDataDecisione());
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setEveIdEvento(idEvento);
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					// Avvocati
					lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				} else {
					// Altro Destinatario
					lNotifica.setCodTipoNotifica(lTipoNotifica);
					lNotifica.setNote(lNote[1]);
				}

				// verifica se non sia stata impostata la notifica telematica
				if (!hs.contains(x)) {
					// Crea Model Autorità Esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
					lAutorita.setCodTipoAutorita(lDestinatari[x]);
					lAutorita.setCodSede(lCodComuneSede);
					lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
					lAutorita.setCodUfficioInserimento(lCodiceUfficio);
					lAutorita.setDataInserimento(DateUtils.getSysDate());

					// Aggiunge il model Autorità Esterna alla Notifica
					lNotifica.setAutoritaEsterna(lAutorita);
				}

				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		} // end for (int x = 0; x < lSize; x++) {

		return lNotifiche;
	}

}