package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sige.aula.action.ICostantiAula;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * Title: ActModificaUdienzaCollegiale
 * Description: Classe Action per la modifica di UdienzaCollegialeSige
 *
 * @version 1.0
 */
public class ActModificaUdienzaCollegiale extends ActUdienzaCollegiale
		implements ICostantiUdienzaCollegiale, ICostantiUdienzaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private CollegioModel updateCollegio() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// devo individuare tutti i fascicoli SIGE che puntanto all'adienza che sto modificando
		BigDecimal idUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);
		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(idUdienza, STATO_FASCICOLO, null);
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());

		// intervento per 11.2.1
		if (lVect.size() == 0) {
			String codMagis = "";
			if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
					&& Utils.isPresent(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)))
				codMagis = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
			else {
				MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
				codMagis = lMagAss.getMagCodMagistrato();
			}
			// intervento er 11.2.1 - imposto sul collegio model il codMagis
			lColMod.setMagCodMagistrato(codMagis);
		}

		siesLogger.debug(getClass().getName() + ".updateCollegio");
		BigDecimal idCollegio = getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO);

		lColMod.setIdCollegio(idCollegio);
		lColMod.setCodCollegio(getRequestStringParameter(CAMPO_COD_COLLEGIO));

		// 20170908: [SG] aggiunta condizione nel controllo
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE)))
			lColMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		lColMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		lColMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lColMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lColMod.setDataAggiornamento(DateUtils.getSysDate());

		// lColMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
		// CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
		// lColMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
		// CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lColMod.setDataInizioValidita(DateUtils.getSysDate());

		letturaDatiMagistrati("M");

		letturaDatiGiudiciPopolari();

		letturaDatiEsperti();

		// 20170908: [SG] non tutti gli uffici hanno le sezioni!
		boolean test = false;
		Option o = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()));
		String[] s = o.getCodes();
		if (s == null || s.length == 0)
			test = true;
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE + "_CBX")
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX")) && !test) {
			BigDecimal idSezione = super.getRequestBigDecimalParameter(
					ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE + "_CBX");
			lColMod.setSezIdSezione(idSezione);
		}

		// chiama il controller
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColModRet = null;
		// 20170913: [SG] aggiunti parametri di passaggio

		String codMagis = "";
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
				&& Utils.isPresent(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO))) {
			// SE ENTRO QUI E' PERCHè HO IMPOSTATO SOLO IL PRESIDENTE DEL COLLEGIO
			codMagis = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
			// IMPOSTO TALE VALORE SULL'ENTIA COLLEGIO
			// lColMod.setMagCodMagistrato(codMagis);
		} else {
			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			codMagis = lMagAss.getMagCodMagistrato();
		}

		// Date dataUdienza = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
		// CAMPO_GIORNO_DATA_UDIENZA);
		// [EC] Recupero il tipoGiudizio per distinguere le MONOCRATICHE e le COLLEGIALI
		String tipoGiudizio = "";
		try {
			// [EC] Recupero il tipoGiudizio per distinguere le MONOCRATICHE e le COLLEGIALI
			tipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);

		} catch (Exception e) {
			siesLogger.info("NESSUNA SEZIONE, O CAMPO_COD_TIPO_GIUDIZIO IN REQUEST");
		}
		// 20171005: [SG] cambiata firma del metodo
		String ris = lCtrl.ExRicercaMaxCodCollegio(lColMod, null, codMagis, tipoGiudizio, "M");
		// int max = 0;
		BigDecimal idCollegioN = null;
		// String operazione = "";
		if (ris.contains("#")) {
			// 20171122: [EC] allineo il codice per una modifica al metodo ExRicercaMaxCodCollegio che ora
			// restituisce una
			// stringa così composta codCollegio#idCollegio#(flagCodicePresente)
			String[] st = ris.split("#");
			// max = new Integer(st[0]).intValue();
			idCollegioN = new BigDecimal(st[1]);
			// operazione = st[2];
			lColMod.setIdCollegio(idCollegioN);
		}
		// AGGIUNGO PER INTERVENTO PER PROBLEMATICA ORDINANZE DI RINVIO UDIENZE SU ESERCIZION 11.2.1
		if (idCollegio == null && idCollegioN != null) {
			idCollegio = idCollegioN;
		}
		// else {
		// if (Utils.isPresent(ris))
		// max = new Integer(ris).intValue();
		// }
		if (idCollegio != null && idCollegioN != null && idCollegioN.compareTo(new BigDecimal(0)) != 0) {
			lColModRet = lCtrl.ExModificaCollegio(lColMod);
		}
		// else {
		// // lColMod.setCodCollegio("" + (max + 1));
		// // 20171005: [SG] se esiste un collegio lo aggancio, altrimenti lo inserisco ex novo
		// lColMod.setCodCollegio("" + max);
		// lColMod.setDataInserimento(DateUtils.getSysDate());
		// lColMod.setDataAggiornamento(null);
		// lColMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lColMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// lColMod.setCodOperatoreAggiornamento(null);
		// lColMod.setCodUfficioAggiornamento(null);
		// lColModRet = lCtrl.ExInserisciCollegio(lColMod);
		// }

		return lColModRet;
	}

	private UdienzaSigeModel updateCollegioUdienzaCollegiale() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".updateCollegioUdienzaCollegiale");

		CollegioModel lColModRet = updateCollegio();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl
				.ExRicercaUdienzaSigeById(getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE));

		lUdiMod.setColIdCollegio(lColModRet.getIdCollegio());

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA))
			lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
					ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
					ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
			lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));

		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
			lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA))
			lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_INIZIO))
			lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_INIZIO))
			lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_FINE))
			lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_FINE))
			lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());

		// 20171005: [SG] aggiunta gestione id sezione
		BigDecimal idSezione = null;
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE + "_CBX")
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX")))
			idSezione = getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX");
		else if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE)))
			idSezione = getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE);
		lUdiMod.setCodIdSezioneUdienza(idSezione);

		return lUdiMod;
	}

	private UdienzaSigeModel updateUdienzaCollegiale() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".updateUdienzaCollegiale");

		// aggiungo per 11.2.1
		CollegioModel lColModRet = updateCollegio();

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		lUdiMod = lCtrl.ExRicercaUdienzaSigeById(getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE));

		// lUdiMod.setColIdCollegio(getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO));
		if (lColModRet != null && lColModRet.getIdCollegio() != null)
			lUdiMod.setColIdCollegio(lColModRet.getIdCollegio());

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA))
			lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
					ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
					ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA))
			lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));

		// 20090402 - Commentato così come richiesto dall'amministrazione.
		// lUdiMod.setNumeroMaxFascicoli(
		// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI));

		// FIX EMMA
		// IN MODIFICA DI UN UDIENZA COLLEGIALE, SULLA TABELLA udienza_sige DEVE ESSERE SETTATO ANCHE
		// IL CAMPO SEZIONE_UDIENZA (SE PRESENTE) CHE è UNA PROPRIETA DEL COLLEGIO
		// QUINDI CON L'IDENTIFICATIVO DEL COLLEGIO DOVREI RECUPERARE IL COLLEGIO E LEGGERE LA SEZIONE AD ESSO
		// LEGATA

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_INIZIO))
			lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_INIZIO))
			lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ORA_FINE))
			lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_MIN_FINE))
			lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));

		if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE))
			lUdiMod.setCodIdSezioneUdienza(
					getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE));

		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
			lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));

		lUdiMod.setDataAggiornamento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());

		return lUdiMod;
	}

	/*****************************************************************************
	 * Azione di Modifica del UdienzaCollegialeSige
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
		lRedir.setParameter("ReturnModifica", "S");

		// Modifica del 08/03/2017
		String popUp = "";
		if (!isRequestParameterNullObj("PopUp")) {
			popUp = getRequestStringParameter("PopUp");
		}
		setRequestAttribute("PopUp", popUp);

		// 20170914: [SG] aggiunta gestione associazione udienze già presenti
		IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();
		BigDecimal idUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

		// String sDataUdienza = DateUtils.getDateToString(
		// getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
		// CAMPO_GIORNO_DATA_UDIENZA), "dd/MM/yyyy");
		// String codMagis = "";
		// if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
		// && Utils.isPresent(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)))
		// codMagis = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		// else {
		// MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
		// codMagis = lMagAss.getMagCodMagistrato();
		// }

		// String idProcuratore = "";
		// BigDecimal idAssistente = null;
		// if (!isRequestParameterNullObj(CAMPO_COD_ID_ASSISTENTE)
		// && !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE)))
		// idAssistente = getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE);
		// if (!isRequestParameterNullObj(CAMPO_COD_PROCURATORE)
		// && Utils.isPresent(getRequestStringParameter(CAMPO_COD_PROCURATORE)))
		// idProcuratore = getRequestStringParameter(CAMPO_COD_PROCURATORE);

		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
		// devo individuare tutti i fascicoli SIGE che puntanto all'adienza che sto modificando
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(idUdienza, STATO_FASCICOLO, null);
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());

		// if (Utils.isPresent(sDataUdienza)) {
		// Vector udienze = lCtrlUdi.ExRicercaUdienzaCollegialeSige(codMagis, sDataUdienza, idSezione,
		// idAssistente, idProcuratore, "M", getCodUfficioUtenteConnesso());
		// if (udienze.size() > 0) {
		// lRedir.setPage(IWebConstants.PG_MAIN);
		// lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
		// lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, udienze.get(0).toString());
		// lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
		// lRedir.setParameter("PopUp", popUp);
		// String lParamValue = this.getParameter("TornaQui");
		// lRedir.setParameter("TornaQui", (lParamValue != null) ? lParamValue : "");
		// lRedir.setParameter(ICostantiUdienzaSige.CAMPO_NUMERO_UDIENZE_MAGRISTRATO,
		// "" + udienze.size());
		// return lRedir.toString();
		// }
		// }
		// FINE 20170914: [SG]

		UdienzaSigeModel lUdiMod = null;
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			lRedir.setParameter(FORM_DEF_COLLEGIO, getRequestStringParameter(FORM_DEF_COLLEGIO));
			// intervento per 11.2.1 se il numero di procedimenti collegati all'udienza è >1
			// non si può procedere con la modifica dell'udienza collegiale

			if (lVect.size() > 1) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione! Udienza Collegiale non modificabile. Esistono procedimenti Collegati!");
			}
			lUdiMod = updateCollegioUdienzaCollegiale();
		} else {
			lUdiMod = updateUdienzaCollegiale();
		}

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		lCtrlUdi.ExModificaUdienzaSige(lUdiMod);

		// 20171013: [SG] aggiorno la tabella collegio_magistrato col collegamento all'udienza sige
		ICollegio iCollegio = SIGELookupRemote.getCollegioRemote();
		iCollegio.ExAggiornaCollegioMagistrati(lUdiMod, lColMod.getCollegioMagistrati(), "M");

		// intervento 11.2.1 nel caso provengo da funzioni amministrative devo andare in aggiornamento del
		// magistrato assegnatario del fascicolo selected, con il magAssSelected
		if (isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			Map<String, String> mapFascMagAss = letturaDatiMagistratiAssegnatari();
			// ---Aggiungere in SIGELookupRemote il metodo getMagistratoAssegnatarioRemote()
			IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			if (!mapFascMagAss.isEmpty()) {
				Iterator<?> it = mapFascMagAss.entrySet().iterator();

				// Verifica con il metodo hasNext() che nella hashmap
				// ci siano altri elementi su cui ciclare
				while (it.hasNext()) {
					// Utilizza il nuovo elemento (coppia chiave-valore)
					// dell'hashmap
					Map.Entry entry = (Map.Entry) it.next();

					// Stampa a schermo la coppia chiave-valore;
					// System.out.println("Key = " + entry.getKey());
					// System.out.println("Value = " + entry.getValue());
					siesLogger.debug("Key = " + entry.getKey());
					siesLogger.debug("Value = " + entry.getValue());

					MagistratoAssegnatarioModel lMagistratoCorrente = lCtrl
							.ExRicercaEstesaMagAssCorrenteXFascicolo(
									new BigDecimal(entry.getKey().toString()));

					MagistratoModel magMod = new MagistratoModel();
					magMod.setCodMagistrato(lMagistratoCorrente.getMagCodMagistrato());

					MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
					// devo recuperare il vecchio codMagistrato
					lMagistrato.setMagistrato(magMod);

					lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(entry.getValue().toString());
					lMagistrato.getMagistratoAssegnatario()
							.setFasSigeIdFascicoloSige(new BigDecimal(entry.getKey().toString()));
					lMagistrato.getMagistratoAssegnatario().setDataInizio(DateUtils.getSysDate());
					lMagistrato.getMagistratoAssegnatario().setCodRuoloMagistrato("03");
					lMagistrato.getMagistratoAssegnatario().setDataInserimento(DateUtils.getSysDate());
					lMagistrato.getMagistratoAssegnatario()
							.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lMagistrato.getMagistratoAssegnatario()
							.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

					/* MagistratoAssegnatarioModel llMagModRet = */lCtrl
							.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
				}
			}
		}

		lRedir.setParameter(CAMPO_ID_UDIENZA_SIGE, lUdiMod.getIdUdienzaSige().toString());
		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return lPage;
	}

}