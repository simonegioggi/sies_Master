package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sige.aula.action.ICostantiAula;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActModificaUdienzaMonocraticaSige
 * </p>
 * <p>
 * Description: Classe Action per la modifica di UdienzaMonocraticaSige
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
public class ActModificaUdienzaMonocraticaSige extends ActionSige
		implements ICostantiUdienzaMonocraticaSige, ICostantiUdienzaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// private UdienzaSigeModel prepareModificaUdienzaMonocraticaFixModel() throws F3BException {
	//
	// IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
	// UdienzaSigeModel lUdiMod = lCtrl
	// .ExRicercaUdienzaSigeById(getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE));
	//
	// lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
	// ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
	// ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));
	//
	// if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)) {
	// String idSez = this.getParameter(CAMPO_SEZ_ID_SEZIONE); // (String)
	// // getRequestAttribute(CAMPO_SEZ_ID_SEZIONE);
	// if (idSez != null && !"-".equals(idSez))
	// lUdiMod.setCodIdSezioneUdienza(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
	// }
	//
	// // if(lUdiMod.getCodIdSezioneUdienza()!=null)
	//
	// lUdiMod.setCodGiudice(getRequestStringParameter(CAMPO_COD_GIUDICE));
	// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
	// lUdiMod.setCodIdAssistente(
	// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
	// lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
	//
	// // codice del giudice
	// if (!isRequestParameterNullObj("CodMagistrato"))
	// lUdiMod.setCodMagistratoAss(getRequestStringParameter("CodMagistrato"));
	//
	// lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
	// lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
	// lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	// lUdiMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());
	//
	// return lUdiMod;
	// }

	private UdienzaSigeModel prepareModificaUdienzaMonocraticaModel() throws F3BException {
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setIdUdienzaSige(getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE));
		lUdiMod.setCodGiudice(getRequestStringParameter(CAMPO_COD_GIUDICE));
		lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA))
			lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));

		// 20090402 - Commentato così come richiesto dall'amministrazione il 20090402 .
		// lUdiMod.setNumeroMaxFascicoli(
		// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_INIZIO))
			lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_INIZIO))
			lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_FINE))
			lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_FINE))
			lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));

		// FIX EMMA:
		// DA QUESTA FORM I DATI RELATIVI A SEZIONE_UDIENZA E AULA_UDIENZA NON VENGONO IMPOSTATI
		// QUINDI IN FASE DI MODIFICA, SE ERANO STATI SETTATI CON IL DECRETO DI FISSAZIONE UDIENZA VENGONO
		// PERSI
		// QUINDI PER SANARE LA COSA ANDREBBERO GESTITI SULLA PAGINA DI MODIFICA (QUELLA DA DENTRO FUNZIONI
		// AMMINISTRATIVE)
		// OPPURE RECUPERATI DA DB FACENDO UNA RICERCA UDIENZA PER ID_UDIENZA

		if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE))
			lUdiMod.setCodIdSezioneUdienza(
					getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE));

		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
			lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());

		return lUdiMod;
	}

	/*****************************************************************************
	 * Azione di Modifica del UdienzaMonocraticaSige
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige");

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================

		UdienzaSigeModel lUdiMod = null;

		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		BigDecimal idUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);
		String codMagPrecedente = "";
		String codMagNuovo = "";
		if (this.getParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null) {
			codMagNuovo = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		} else {
			codMagNuovo = getRequestStringParameter(CAMPO_COD_GIUDICE);
		}

		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
		// devo individuare tutti i fascicoli SIGE che puntanto all'adienza che sto modificando
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(idUdienza, STATO_FASCICOLO, null);
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());

		MagistratoAssegnatarioModel llMagModRet = null;
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			lRedir.setParameter(FORM_DEF_COLLEGIO, getRequestStringParameter(FORM_DEF_COLLEGIO));

			// lUdiMod = prepareModificaUdienzaMonocraticaFixModel();
			if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
					&& getFascicoloSigeEstesoInSessione().getFascicoloSige() != null
					&& getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige() != null
					&& lVect.size() > 0) {

				lMagistrato = prepareModificaAssegnatarioModel(
						getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
				// recupero il precedente magistrato se esiste e lo setto su MagistratoAssegnatarioModel per
				// impostare la data fine
				if (getFascicoloSigeEstesoInSessione().getMagAssegnatario() != null) {
					codMagPrecedente = getFascicoloSigeEstesoInSessione().getMagAssegnatario()
							.getMagCodMagistrato();
					MagistratoModel magMod = new MagistratoModel();
					magMod.setCodMagistrato(codMagPrecedente);
					lMagistrato.setMagistrato(magMod);
				}
				// se sono diversi lo sostituisce, altrimento no
				if (!codMagNuovo.equals(codMagPrecedente) && !codMagNuovo.equals("")) {
					IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
					lMagistrato.getMagistratoAssegnatario().setFlagModifBlocco("P");
					llMagModRet = lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
				}
				// aggiorno eventualmente i campi del procuratore e cancelliere sulla tabella
				// magistrato_assegnatario per id Fascicolo sig
				// NB: IdFascSIGE è settato sul model lMagistrato
				// else {
				// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE)
				// || !isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE)){

				IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
				// SETTAGGIO DELLA MODIFICA PUNTUALE DEL MAGISTRATO ASSEGNATARIO PER FASCICOLI SIG
				lMagistrato.getMagistratoAssegnatario().setFlagModifBlocco("P");
				llMagModRet = lCtrl.ExAggiornaMagistratoAssegnatarioXFascicolo(lMagistrato, "P");
				// }
				// }
			}
		} else {
			// GESTIONE DA FUNZIONI AMMINISTRATIVE

			String cod_magis = null;
			if (this.getParameter(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS) != null) {
				cod_magis = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS);
				setRequestAttribute(ICostantiUdienzaSige.CAMPO_COD_MAG_ASS, cod_magis);
			}

			if (cod_magis != null && !"".equals(cod_magis)) {
				lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(idUdienza, null, "ND", "TUTTI", null,
						cod_magis);
			}

			// qui si entra nel caso di utilizzo della funzione DI MODIFICA da FUNZIONI AMMINISTRATIVE
			// (Funzioni Amministrative » Gestione Udienze » Udienza Monocratica » Ricerca Udienza Monocratica
			// )
			// lUdiMod = prepareModificaUdienzaMonocraticaModel();

			// intervento per ver. 11.2.1 nuova gestione delle udienze monocratiche/collegiali
			// la modifica di un'udienza monocratica consta sempre di una modifica del magistrato assegnatario
			// dei fascicoli
			// associati a quella data udienza, pertanto non si va più in modifica sulla tabella udienza_sige
			// ma si va ad agire sulla tabella magistrato_assegnatario passando per udienza_procedimento_sige

			idUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

			for (Iterator iterator = lVect.iterator(); iterator.hasNext();) {
				ProcedimentixUdienzaModel procedimentixUdienzaModel = (ProcedimentixUdienzaModel) iterator
						.next();
				BigDecimal idFascSige = procedimentixUdienzaModel.getIdFasSIGE();
				String vecchiCodMag = procedimentixUdienzaModel.getMagistrato().getCodMagistrato();
				MagistratoModel magMod = new MagistratoModel();
				magMod.setCodMagistrato(vecchiCodMag);

				lMagistrato = prepareModificaAssegnatarioModel(idFascSige);
				lMagistrato.setMagistrato(magMod);
				// se i codici sono diversi lo sostituisce, altrimento no
				if (!codMagNuovo.equals(vecchiCodMag)) {
					IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
					lMagistrato.getMagistratoAssegnatario().setFlagModifBlocco("B");
					llMagModRet = lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
				}
				// aggiorno eventualmente i campi del procuratore e cancelliere sulla tabella
				// magistrato_assegnatario per id Fascicolo sig
				// NB: IdFascSIGE è settato sul model lMagistrato
				// else {
				// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE)
				// || !isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE)){

				IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
				lMagistrato.getMagistratoAssegnatario().setFlagModifBlocco("B");
				llMagModRet = lCtrl.ExAggiornaMagistratoAssegnatarioXFascicolo(lMagistrato, "B");
				// }
				// }
			}

			// SE LA SIZE è > di ZERO vuol dire che ho agito sulla modifica del magistrato assegnatario,
			// quindi sulla tabella magistrato_assegnatario, quindi nel ricaricare la pagina
			// devo portarmi dietro il codice del magistrato assegnatario appena inserito/modificato
			// e anche i codice del procuratore e del cancelliere
			if (lVect.size() > 0) {

				lRedir = new RedirectTo();
				lRedir.setPage(IWebConstants.PG_MAIN);
				lRedir.setAction(
						"siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaProcedimentoSige");
				lRedir.setParameter(CAMPO_ID_UDIENZA_SIGE, idUdienza.toString());

				// DEVO METTERE NELLA REQUEST IL COD_MAGISTRATO, IL COD_PROCURATORE E COD_ASSISTENTE
				if (llMagModRet != null) {
					if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO))
						lRedir.setParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO,
								llMagModRet.getMagCodMagistrato());
					if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
						lRedir.setParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE,
								llMagModRet.getIdAssistente() != null
										? llMagModRet.getIdAssistente().toString()
										: "");
					if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
						lRedir.setParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE,
								llMagModRet.getCodProcuratore());

					lRedir.setParameter("numProcePerUdienza", Integer.toString(lVect.size()));
				}

				lRedir.setParameter("numProcePerUdienza", Integer.toString(lVect.size()));
				// setta la risposta nella request
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Modifica effettuata con Successo!");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedir);
				return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW

			}
		}

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================

		// SE LA SIZE è ZERO LE MODIFICHE AGISCONO SULLA TABELLA UDIENZA_SIGE PER I CAMPI PROCURATORE E
		// ASSISTENTE
		if (lVect.size() == 0) {
			lUdiMod = prepareModificaUdienzaMonocraticaModel();
			// ===================================================
			// Recupera il controller ed effettua la modifica
			// ===================================================
			IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
			lCtrl.ExModificaUdienzaSige(lUdiMod);
		}

		lRedir.setParameter(CAMPO_ID_UDIENZA_SIGE, idUdienza.toString());
		lRedir.setParameter("numProcePerUdienza", Integer.toString(lVect.size()));
		String lPage = lRedir.toString();

		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lPage;
	}

	private MagistratoAssegnatarioMagistratoModel prepareModificaAssegnatarioModel(BigDecimal idFascicoloSige)
			throws F3BException {

		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();

		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lMagistrato.getMagistratoAssegnatario()
					.setMagCodMagistrato(this.getRequestStringParameter(CAMPO_COD_GIUDICE));
		}

		lMagistrato.getMagistratoAssegnatario().setFasSigeIdFascicoloSige(idFascicoloSige);
		lMagistrato.getMagistratoAssegnatario().setDataInizio(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodRuoloMagistrato("03");
		lMagistrato.getMagistratoAssegnatario().setDataInserimento(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMagistrato.getMagistratoAssegnatario().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lMagistrato.getMagistratoAssegnatario().setIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
			lMagistrato.getMagistratoAssegnatario()
					.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));

		return lMagistrato;
	}

	// private UdienzaSigeModel prepareModificaDatiMonocraticaModel() throws F3BException {
	// UdienzaSigeModel lUdiMod = new UdienzaSigeModel();
	//
	// lUdiMod.setIdUdienzaSige(getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE));
	//
	// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
	// lUdiMod.setCodIdAssistente(
	// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
	// lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
	//
	// lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
	// lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
	// lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	// lUdiMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());
	//
	// return lUdiMod;
	// }

}