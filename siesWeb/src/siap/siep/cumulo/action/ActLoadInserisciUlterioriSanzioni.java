package siap.siep.cumulo.action;

import java.util.Hashtable;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciUlterioriSanzioni.j
 * </p>
 * <p>
 * Description: Classe Action per la load delle Ulteriori Sanzioni Cumulo
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

public class ActLoadInserisciUlterioriSanzioni extends ActionSiap implements ICostantiCumulo {

	/**
	 * Verifica se presenti già a sistema di ULTERIORI_SANZIONI_CUMULO per il cumulo corrente (collegate a un
	 * record cumulo non validato) e carica la pagina di visualizzazione/inserimento/modifica
	 * 
	 * @return jsp di visualizzazione/inserimento
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// Ricerca cumulo per FasSieIdFascicoloSiep
		CumuloModel lCumuloMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		lCumuloMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		Vector lCum = iCum.ExRicercaCumulo(lCumuloMod);
		if (lCum.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Recupero i record CUMULO non ancora validati ordinati per data_inserimento
		// ascendente
		CumuloModel lCumMod = new CumuloModel();
		Vector cumuli = iCum
				.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());
		if (cumuli.size() > 0) { // Recpero il primo CUMULO inserito (non ancora validato)
			lCumMod = ((CumuloModel) (cumuli).get(0));
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione : Il Provvedimento di esecuzione pene concorrenti risulta già validato.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		}

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		if (lPG == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");
		}

		setRequestAttribute("PosizioneGiuridica", lPG);

		Option lOption = new Option(DecodificheManager.getInstance().getFlagErgastolo());
		setRequestAttribute("FlagErgastolo", "" + lOption);

		// ==========================================================================
		// Ricerca se presenti record ULTERIORI_SANZIONI_CUMULO sul primo CUMULO
		// non validato
		// ==========================================================================
		IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
		Vector lUltMod = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(
				lFascMod.getIdFascicoloSiep(), lCumMod.getIdCumulo());
		Hashtable lTable = new Hashtable();

		// n.b. codici dell'RV_DOMAIN = 'TIPO_ULTERIORE_SANZIONE'
		for (int i = 0; i < lUltMod.size(); i++) {
			UlterioreSanzioneCumuloModel lUltCumMod = (UlterioreSanzioneCumuloModel) lUltMod.get(i);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("01")) // 01 - Sanzione Sostitutiva :
																		// Semidetenzione
				lTable.put("01", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("02")) // 02 - Sanzione Sostitutiva : Liberta'
																		// Controllata
				lTable.put("02", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("03")) // 03 - Sanzione Sostitutiva :
																		// Espulsione
				lTable.put("03", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("04")) // 04 - Sanzione Sostitutiva : Pena
																		// Pecuniaria
				lTable.put("04", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("05")) // 05 - Pena Pecuniaria : Lavoro
																		// Sostitutivo
				lTable.put("05", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("06")) // 06 - Pena Militare : Reclusione
				lTable.put("06", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("07")) // 07 - Giudice di Pace : Permanenza
																		// Domiciliare
				lTable.put("07", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("08")) // 08 - Giudice di Pace : Lavoro
																		// Sostitutivo
				lTable.put("08", lUltCumMod);

			if (lUltCumMod.getCodTipoUlterioreSanzione().equals("09")) // 09 - Giudice di Pace : Lavoro
																		// Pubblica Utilita'
				lTable.put("09", lUltCumMod);

		}

		setRequestAttribute("lTable", lTable);
		setRequestAttribute("ulterioriSanzione", lUltMod);
		setSessionAttribute("cumulowiz", "");// ???

		String provenienza = "";
		String idPenaResidua = "";
		if (!isRequestParameterNullObj("Provenienza")) {
			provenienza = getRequestStringParameter("Provenienza");
		}
		if (!isRequestParameterNullObj("IdPenaResidua")) {
			idPenaResidua = getRequestStringParameter("IdPenaResidua");
		}
		setRequestAttribute("IdPenaResidua", idPenaResidua);

		if (provenienza != null && !provenienza.equals("")) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					"siap.siep.cumulo.action.ActLoadInserisciUlterioriSanzioni");
		} else {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					"siap.siep.cumulo.action.ActLoadDettaglioApplicazioneBenefici");
		}

		return PG_LOAD_INSERIMENTO_ULTERIORI_SANZIONI;

	}

}