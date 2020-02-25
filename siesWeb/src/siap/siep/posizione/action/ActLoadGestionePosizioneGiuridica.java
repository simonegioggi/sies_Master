package siap.siep.posizione.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneGiuridica
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneGiuridica
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadGestionePosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione per aggiungere le Posizioni Giuridiche sulla vecchia maschera
	 * 
	 * @param lFasMod
	 * @throws F3BException
	 */
	protected void settaPosGiuridicheOld(FascicoloSiepModel lFasMod) throws F3BException {

		// Controlla se per il fascicolo selezionato esiste un ordine di esecuzione (o legge simeone)
		// o un Provvedimento di Esecuzione di Pene Concorrenti codice motivo 0222, 0223, 0224,0277
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		boolean lFlagOrdineEsecuzione = lCtrl
				.ExEsisteOrdineEsecuzioneByFascicoloSiep(lFasMod.getIdFascicoloSiep());

		// Ricerca ultima pena residua ++ modifica richiesta 07/07/2004
		IPenaResidua lCtrlPena = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaMod = lCtrlPena
				.ExRicercaPenaResiduaUltimaValidata(lFasMod.getIdFascicoloSiep());

		// Imposta Combo posizione Giuridica
		ArrayList lTipoPosizioniEsecuzione = null;
		ArrayList lTipoPosizioniEsecuzioneBis = null; // 28/07/2010
		if (lFlagOrdineEsecuzione || (lPenaMod != null && lPenaMod.getFlagPenaSospesa() != null)
				|| !this.isRequestParameterNullObj("cambioposizione")) {
			lTipoPosizioniEsecuzione = new ArrayList(
					DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione());
			if (!this.isFascicoloIscritto())
				lTipoPosizioniEsecuzioneBis = new ArrayList(
						DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione());
		} else {
			lTipoPosizioniEsecuzione = new ArrayList(
					DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione());
			// 28/07/2010 Caricamento della combo posizioni giuridiche in base allo stato fascicolo
			// (Iscritto/Altro).
			if (!this.isFascicoloIscritto())
				lTipoPosizioniEsecuzioneBis = new ArrayList(
						DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione());
		}
		if (lTipoPosizioniEsecuzioneBis != null) {
			for (Iterator itx = lTipoPosizioniEsecuzioneBis.iterator(); itx.hasNext();)
				lTipoPosizioniEsecuzione.add(itx.next());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Action Posizioni size : " + lTipoPosizioniEsecuzione.size());
		for (int i = 0; i < lTipoPosizioniEsecuzione.size(); i++) {
			DecodificheModel lPos = new DecodificheModel();
			lPos = (DecodificheModel) lTipoPosizioniEsecuzione.get(i);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Action Posizione Collection " + lPos.getDescription());
		}

		setRequestAttribute("posizioneGiuridicaEsecuzione", lTipoPosizioniEsecuzione);
	}

	/**
	 * Funzione per aggiungere le Posizioni Giuridiche sulla nuova maschera
	 * 
	 * @param lFasMod
	 * @throws F3BException
	 */
	protected void settaPosGiuridicheNew() throws F3BException {

		ArrayList lTipoPosizioniIscrizione = new ArrayList(
				DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione());
		setRequestAttribute("posizioneGiuridicaIscrizione", lTipoPosizioniIscrizione);

	}

	/**
	 * Funzione per aggiungere le Posizioni Giuridiche sulla nuova maschera
	 * 
	 * @param lFasMod
	 * @throws F3BException
	 */
	protected void settaPosGiuridicheCodiceMaschera() throws F3BException {

		ArrayList lTipoPosizioniIscrizione = new ArrayList(
				DecodificheManager.getInstance().getPosizioneGiuridicaIscrizioneCodiceMaschera());
		setRequestAttribute("posizioneGiuridicaIscrizione", lTipoPosizioniIscrizione);

	}

	/**
	 * Funzione per aggiungere il TipoMisura sulla vecchia maschera
	 * 
	 * @param lFasMod
	 * @throws F3BException
	 */
	protected void settaTipoMisuraOld(String lSelPosGiuAltra) throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa(),
				lSelPosGiuAltra);
		setRequestAttribute("tipoPosizioneAltraCausa", "" + lOption);

	}

	/**
	 * Funzione per aggiungere il TipoMisura sulla nuova maschera
	 * 
	 * @param lFasMod
	 * @throws F3BException
	 */
	protected void settaTipoMisuraNew(String lSelPosGiuAltra, String lSelTipoMisCau) throws F3BException {

		Option lOption = null;
		// lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa(),
		// lSelPosGiuAltra);
		// setRequestAttribute("tipoMisuraAltraCausa", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare(), lSelTipoMisCau);
		lOption.setFilter(new String[] { "-", "EA", "EB" });
		setRequestAttribute("tipoMisuraAltraCausa", lOption.toString());
		lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare(), lSelTipoMisCau);
		lOption.setFilter(new String[] { "-", "CF", "CG" });
		setRequestAttribute("tipoMisuraCautelareL2", lOption.toString());
		lOption.setFilter(new String[] { "-", "CH", "CI", "CJ", "CK" });
		setRequestAttribute("tipoMisuraCautelareL3", lOption.toString());

	}

	/**
	 * Azione di Load Inserisci Posizione Giuridica
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws Exception
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel loadPosGiuridica(FascicoloSiepModel lFasMod,
			boolean update) throws Exception {

		String tipoUfficioUtente = "";
		String descComuneUfficioUtente = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			tipoUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodTipoUfficio();
			descComuneUfficioUtente = getUtenteConnesso().getUfficioUtente().getDescrComune();
		}

		// ** Seleziona l'ultima eventuale Posizione Giuridica associata al Fascicolo Siep **
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		if (update) {
			IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			// lPosLuoAltMod =
			// lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFasMod.getIdFascicoloSiep());
			lPosLuoAltMod = lCtrlPos
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
							lFasMod.getIdFascicoloSiep());
		}
		PosizioneGiuridicaModel lPosGiu = lPosLuoAltMod.getPosizioneGiuridica();
		// LuogoDetenzioneModel lLuogoDet = lPosLuoAltMod.getLuogoDetenzione();
		AltraCausaModel lAltraCausa = lPosLuoAltMod.getAltraCausa();
		MisuraCautelareModel lMisuraCautelare = lPosLuoAltMod.getMisuraCautelare();

		// Controllo se per il fascicolo esiste un
		String lSelMask = "-";
		String lSelTipoPosPro = "-";
		String lSelTipoAutCompet = "-";
		if (lPosGiu != null) {
			lSelMask = lPosGiu.getCodMaschera();
			lSelTipoPosPro = lPosGiu.getCodPosizioneProcessuale();
			if (lPosGiu.getAutoritaCompetente() != null && !"".equals(lPosGiu.getAutoritaCompetente())) {
				lSelTipoAutCompet = lPosGiu.getAutoritaCompetente();
			}
		}
		setRequestAttribute("tipoMaschera", lSelMask);

		String lSelPosGiuAltra = "-";
		String lSelTipoAutEmitt = tipoUfficioUtente; // "-";
		if (lAltraCausa != null) {
			lSelPosGiuAltra = lAltraCausa.getCodTipoPosGiuridica();
			lSelTipoAutEmitt = lAltraCausa.getCodAutorita();
		}

		String lSelTipoMisCau = "-";
		String lSelTipoUfficioPM = "-";
		String lSelTipoUfficioRegGen = "-";
		String lSelAutEmittente = tipoUfficioUtente; // "-";
		String lSelAutEmittenteLuogo = descComuneUfficioUtente; // "-";
		String lSelAutCompetente = "-";
		if (lMisuraCautelare != null) {

			if (lMisuraCautelare.getCodTipoMisura() != null)
				lSelTipoMisCau = lMisuraCautelare.getCodTipoMisura();

			if (lMisuraCautelare.getCodiceUfficioPmSede() != null) {
				UfficioModel um = UfficioUtils
						.getUfficioByCodUfficio(lMisuraCautelare.getCodiceUfficioPmSede());
				lSelTipoUfficioPM = um.getCodTipoUfficio();
				setRequestAttribute("ufficioPmSedeDesc", um.getDescrComune());
			}

			if (lMisuraCautelare.getTipoUfficioRegGen() != null)
				lSelTipoUfficioRegGen = lMisuraCautelare.getTipoUfficioRegGen();

			if (lMisuraCautelare.getAutoritaEmittente() != null)
				lSelAutEmittente = lMisuraCautelare.getAutoritaEmittente();

			if (lMisuraCautelare.getAutoritaCompetente() != null)
				lSelAutCompetente = lMisuraCautelare.getAutoritaCompetente();

			if (lMisuraCautelare.getAutoritaEmittenteLuogoDesc() != null)
				lSelAutEmittenteLuogo = lMisuraCautelare.getAutoritaEmittenteLuogoDesc();
		}

		settaPosGiuridicheOld(lFasMod);
		if (lPosGiu != null) {
			// la posizione giuridica esiste ==> modifica(nuova lista posizione giuridiche)
			settaPosGiuridicheCodiceMaschera();
		} else {
			// la posizione giuridica non esiste ==> inserimento(vecchia lista posizione giuridica)
			settaPosGiuridicheNew();
		}

		settaTipoMisuraOld(lSelPosGiuAltra);
		settaTipoMisuraNew(lSelPosGiuAltra, lSelTipoMisCau);

		Option lOption = null;

		// Imposta Stato posizione Processuale
		lOption = new Option(DecodificheManager.getInstance().getPosizioneProcessuale(), lSelTipoPosPro);
		setRequestAttribute("posizioneProcessuale", lOption.toString());

		// Imposta Tipo Ufficio PM
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM(), lSelTipoUfficioPM);
		lOption.setFilter(new String[] { "-", "PM", "PMM", "PGCAP" });
		setRequestAttribute("tipoUfficioPM", lOption.toString());

		// ******************* INIZIO MEV 10 S3 29/10/2015
		// Recupero i dati per caricare la combo per un determinato "tipo ufficio PM"
		UtenteModel utenteConnesso = getUtenteConnesso();
		String codTipoUfficio = "";
		String sedeTipoUfficio = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			codTipoUfficio = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
			sedeTipoUfficio = utenteConnesso.getUfficioUtente().getDescrComune();
		}
		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
		/* UfficioModel lUfficioModel = */lCtrlUfficio.getUfficioByCodTipoUffDescrComune(codTipoUfficio,
				sedeTipoUfficio);
		Option lOptionUffPM = new Option();
		if (lSelTipoUfficioPM != "-") {
			lOptionUffPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM(), lSelTipoUfficioPM);
			lOptionUffPM.setSelected(lSelTipoUfficioPM);
		} else {
			lOptionUffPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
			// default codice ufficio utente
			lOptionUffPM.setSelected(codTipoUfficio);
		}
		// Recupero i dati per caricare la combo per un determinato "tipo ufficio PM"
		// PGCAP= Procura Generale della Repubblica Presso la Corte D'Appello
		// PM =Procura della Repubblica Presso il Tribunale Ordinario
		// PMM =Procura della Repubblica Presso il Tribunale per i Minorenni
		lOptionUffPM.setFilter(new String[] { "PM", "PMM", "PGCAP" });
		setRequestAttribute("tipUffPM", "" + lOptionUffPM);
		// ******************* FINE MEV 10 S3 29/10/2015

		// Imposta Tipo Ufficio Reg. Gen.
		Collection coll = DecodificheManager.getInstance().getTipoUfficioRegGen();
		// String[] nega = { "TRIBSD", "DIBM" };
		// Utils.negativeFilter(coll, nega);
		lOption = new Option(coll, lSelTipoUfficioRegGen);
		setRequestAttribute("tipoUfficioRegGen", lOption.toString());

		// Imposta Tipo Autorita Emittente
		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), lSelTipoAut);
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), lSelTipoAutEmitt);
		setRequestAttribute("autoritaEmittente", lOption.toString());

		// mev_10_step_2_int_16
		// In fase di inserimento prevalorizzato inizialmente con il valore “Gip Presso il Tribunale
		// Ordinario“ (GIP)
		// se l’operatore è della Procura presso il Tribunale Ordinario (PM) oppure se l’operatore è della
		// Procura Generale presso la Corte di Appello(PGCAP).
		// In fase di inserimento prevalorizzato inizialmente con il valore “Gip Presso il Tribunale per i
		// Minorenni“ se l’operatore è della Procura presso il Tribunale per i minorenni(PMM).
		if (lSelAutEmittente.equalsIgnoreCase("PM") || lSelAutEmittente.equalsIgnoreCase("PGCAP"))
			lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "GIP");
		else if (lSelAutEmittente.equalsIgnoreCase("PMM"))
			lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "GIPM");
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
					lSelAutEmittente);
		lOption.setFilter(new String[] { "-", "CAP", "CAPSM", "CAS", "CASAP", "CSS", "DIB", "DIBM", "GIP",
				"GIPM", "GUP", "GUPM", "TRIBSD" });
		setRequestAttribute("autoritaEmittenteCautelare", lOption.toString());
		setRequestAttribute("autoritaEmittenteCautelareLuogo", lSelAutEmittenteLuogo);

		lOption = new Option(DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(),
				lSelTipoAutCompet);
		setRequestAttribute("autoritaCompetente", lOption.toString());

		lOption = new Option(DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(),
				lSelAutCompetente);
		setRequestAttribute("autoritaCompetenteCautelare", lOption.toString());

		// mev_10_step_2_int_16
		// Nell'elenco devono essere visibili e selezionabili le seguenti voci:
		// PGCAP, PM e PMM, prevalorizzato con '-'
		if (lPosLuoAltMod != null && lPosLuoAltMod.getAltraCausa() != null
				&& (lPosLuoAltMod.getAltraCausa().getCodAutorita().equalsIgnoreCase("PGCAP")
						|| lPosLuoAltMod.getAltraCausa().getCodAutorita().equalsIgnoreCase("PM")
						|| lPosLuoAltMod.getAltraCausa().getCodAutorita().equalsIgnoreCase("PMM"))) {
			lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSez1DDTrattino(),
					lPosLuoAltMod.getAltraCausa().getCodAutorita());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSez1DDTrattino());
		}
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSez1DDTrattino());
		setRequestAttribute("autoritaSez1DD", lOption.toString());

		// "EA", "74" // TIPO_MISURA_CAUTELARE / Espiazione pena per Altra Causa in Regime di Detenzione (cod.
		// EA) --> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Regime di Detenzione
		// (cod. 74)
		// "EB", "75" // TIPO_MISURA_CAUTELARE / Espiazione pena per Altra Causa in Misura Sicurezza Detentiva
		// (Internato) (cod. EB) ---> POSIZIONE_GIURIDICA / Libero - Espiazione pena per Altra Causa in Misura
		// Sicurezza Detentiva (Internato) (cod. 75)
		if (lPosLuoAltMod != null && lPosLuoAltMod.getAltraCausa() != null
				&& (lPosLuoAltMod.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("75"))) {
			lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare(), "EB");
			lOption.setFilter(new String[] { "-", "EA", "EB" });
			setRequestAttribute("tipoMisuraAltraCausa", lOption.toString());
		}
		if (lPosLuoAltMod != null && lPosLuoAltMod.getAltraCausa() != null
				&& (lPosLuoAltMod.getAltraCausa().getCodTipoPosGiuridica().equalsIgnoreCase("74"))) {
			lOption.setFilter(new String[] { "-", "EA", "EB" });
			lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare(), "EA");
			setRequestAttribute("tipoMisuraAltraCausa", lOption.toString());
		}

		return lPosLuoAltMod;
	}

}