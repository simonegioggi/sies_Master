package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.Date;
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
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActInserisciUdienzaCollegiale
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di UdienzaCollegialeSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActInserisciUdienzaCollegiale extends ActUdienzaCollegiale
		implements ICostantiUdienzaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Inserimento del UdienzaCollegialeSige
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		String popUp = "";
		if (!isRequestParameterNullObj("PopUp")) {
			popUp = getRequestStringParameter("PopUp");
		}
		setRequestAttribute("PopUp", popUp);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");

		// 20170914: [SG] aggiunta gestione associazione udienze già presenti
		IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();
		String sDataUdienza = DateUtils.getDateToString(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA,
				CAMPO_MESE_DATA_UDIENZA, CAMPO_GIORNO_DATA_UDIENZA), "dd/MM/yyyy");
		String codMagis = "";
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
				&& Utils.isPresent(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)))
			codMagis = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		else {
			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			if (lMagAss != null)
				codMagis = lMagAss.getMagCodMagistrato();
		}
		BigDecimal idSezione = null;
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
				// && !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE))
				&& getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE) != null)
			idSezione = getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE);
		else if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE + "_CBX")
				// && !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX"))
				&& getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX") != null)
			idSezione = getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX");
		String idProcuratore = "";
		BigDecimal idAssistente = null;
		if (!isRequestParameterNullObj(CAMPO_COD_ID_ASSISTENTE)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE)))
			idAssistente = getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE);
		if (!isRequestParameterNullObj(CAMPO_COD_PROCURATORE)
				&& Utils.isPresent(getRequestStringParameter(CAMPO_COD_PROCURATORE)))
			idProcuratore = getRequestStringParameter(CAMPO_COD_PROCURATORE);

		if (Utils.isPresent(sDataUdienza)) {
			Vector udienze = lCtrlUdi.ExRicercaUdienzaCollegialeSige(codMagis, sDataUdienza, idSezione,
					idAssistente, idProcuratore, "I", getCodUfficioUtenteConnesso());
			if (udienze.size() > 0) {
				lRedir.setPage(IWebConstants.PG_MAIN);
				lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
				lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, udienze.get(0).toString());
				lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
				lRedir.setParameter("PopUp", popUp);
				String lParamValue = this.getParameter("TornaQui");
				lRedir.setParameter("TornaQui", (lParamValue != null) ? lParamValue : "");
				lRedir.setParameter(ICostantiUdienzaSige.CAMPO_NUMERO_UDIENZE_MAGRISTRATO,
						"" + udienze.size());
				return lRedir.toString();
			}
		}
		// FINE 20170914: [SG]

		UdienzaSigeModel lUdiMod = null;
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// QUI SI ENTRA QUANDO USO LA POP-UP PER INSERIRE L'UDIENZA COLLEGIALE (QUINDI NON DA FUZNIONI DI
			// SUPPORTO)
			lRedir.setParameter(FORM_DEF_COLLEGIO, getRequestStringParameter(FORM_DEF_COLLEGIO));
			lUdiMod = preperareInsertCollegioUdienzaCollegiale();
		} else {
			// QUI SI ENTRA PER INSERIMENTO UDIENZA COLLEGIALE DA FUNZIONI DI SUPPORTO
			// CHE PERO' PER LA VERSIONE 11.2.1 è STATA DISABILITATA
			lUdiMod = insertUdienzaCollegiale();
		}

		// 20171129: [EC] aggiunta condizione nel controllo
		if (idSezione != null)
			lUdiMod.setCodIdSezioneUdienza(idSezione);

		UdienzaSigeModel lUdiRetMod = lCtrlUdi.ExInserisciUdienzaSige(lUdiMod, null);

		// 20171013: [SG] aggiorno la tabella collegio_magistrato col collegamento all'udienza sige
		ICollegio iCollegio = SIGELookupRemote.getCollegioRemote();
		iCollegio.ExAggiornaCollegioMagistrati(lUdiRetMod, lColMod.getCollegioMagistrati(), "I");

		lRedir.setParameter(CAMPO_ID_UDIENZA_SIGE, lUdiRetMod.getIdUdienzaSige().toString());
		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return lPage;
	}

	private CollegioModel insertCollegio() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".insertCollegio");

		// 20170908: [SG] aggiunta condizione nel controllo
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE)))
			lColMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		lColMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// lColMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
		// CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
		// lColMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
		// CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lColMod.setDataInizioValidita(DateUtils.getSysDate());

		lColMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lColMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lColMod.setDataInserimento(DateUtils.getSysDate());

		// I metodi seguenti di lettura sono ereditati dalla ActionCollegio
		// Prevedono che nell'istanza lColMod sia valorizzato il codice collegio.

		// Legge e Popola i dati afferenti ai Magistrati.
		letturaDatiMagistrati("I");

		// Legge e Popola i dati afferenti ai Giudici Popolari.
		letturaDatiGiudiciPopolari();

		// Legge e Popola i dati afferenti agli Esperti.
		letturaDatiEsperti();

		// 20170908: [SG] non tutti gli uffici hanno le sezioni!
		boolean test = false;
		Option o = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()));
		String[] s = o.getCodes();
		if (s == null || s.length == 0)
			test = true;
		if (isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE + "_CBX") && !test)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Sezioni non presenti sul Sistema. Provvedere all'inserimento delle sezioni per l'ufficio connesso");

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE + "_CBX")
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX")) && !test) {
			BigDecimal idSezione = super.getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE + "_CBX");
			lColMod.setSezIdSezione(idSezione);
		}

		// Chiama il controller.
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();

		// 20170913: [SG] aggiunti parametri di passaggio
		Date dataUdienza = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA);
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

		// [EC] Recupero il tipoGiudizio per distinguere le MONOCRATICHE e le COLLEGIALI
		String tipoGiudizio = "";
		try {
			// [EC] Recupero il tipoGiudizio per distinguere le MONOCRATICHE e le COLLEGIALI
			tipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);

		} catch (Exception e) {
			siesLogger.info("NESSUNA SEZIONE, O CAMPO_COD_TIPO_GIUDIZIO IN REQUEST");
		}

		// 20171005: [SG] cambiata firma del metodo
		String ris = lCtrl.ExRicercaMaxCodCollegio(lColMod, dataUdienza, codMagis, tipoGiudizio, "I");
		int max = 0;
		BigDecimal idCollegio = null;
		// String operazione = "";
		if (ris.contains("#")) {
			// 20171122: [EC] allineo il codice per una modifica al metodo ExRicercaMaxCodCollegio che ora
			// restituisce una
			// stringa così composta codCollegio#idCollegio#(flagCodicePresente)
			String[] st = ris.split("#");
			max = new Integer(st[0]).intValue();
			idCollegio = new BigDecimal(st[1]);
			// operazione = st[2];
			lColMod.setIdCollegio(idCollegio);
		} else {
			if (Utils.isPresent(ris))
				max = new Integer(ris).intValue();
		}
		// determina il cod collegio
		// 20171005: [SG] se esiste un collegio lo aggancio, altrimenti lo inserisco ex novo
		lColMod.setCodCollegio("" + max);

		// Esegue l'insert.
		CollegioModel lColModRet = lCtrl.ExInserisciCollegio(lColMod);

		return lColModRet;
	}

	private UdienzaSigeModel preperareInsertCollegioUdienzaCollegiale() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".insertCollegioUdienzaCollegiale");

		CollegioModel lColModRet = insertCollegio();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setColIdCollegio(lColModRet.getIdCollegio());
		lUdiMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA));

		if (!isRequestParameterNullObj(CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE));
		if (!isRequestParameterNullObj(CAMPO_COD_PROCURATORE))
			lUdiMod.setCodProcuratore(getRequestStringParameter(CAMPO_COD_PROCURATORE));

		// aggiungo per nuova gestione udienze collegiali per 11.2.1
		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
			lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA))
			lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));
		lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));
		// fine intervento per 11.2.1

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());

		return lUdiMod;
	}

	private UdienzaSigeModel insertUdienzaCollegiale() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".insertUdienzaCollegiale");

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setColIdCollegio(getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO));

		lUdiMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA));
		if (!isRequestParameterNullObj(CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE));
		lUdiMod.setCodProcuratore(getRequestStringParameter(CAMPO_COD_PROCURATORE));
		lUdiMod.setLuogoUdienza(getRequestStringParameter(CAMPO_LUOGO_UDIENZA));

		// 20090402 - Commentato così come richiesto dall'amministrazione.
		// lUdiMod.setNumeroMaxFascicoli(
		// getRequestBigDecimalParameter(CAMPO_NUMERO_MAX_FASCICOLI));

		lUdiMod.setOraInizio(getRequestStringParameter(CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(CAMPO_MIN_FINE));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());

		return lUdiMod;
	}

}