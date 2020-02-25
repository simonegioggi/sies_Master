package siap.siepe.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.util.UtilTemplate;
import siap.sico.web.ActionSiap;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRichiesta
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Richiesta
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
public class ActLoadDettaglioRichiesta extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		String lId = getRequestStringParameter(CAMPO_ID_RICHIESTA);

		// Chiama il controller e riempie il model
		IRichiesta lCtrl = SIEPELookupRemote.getRichiestaRemote();
		RichiestaModel lRicMod = lCtrl.ExRicercaRichiestaByKey(new BigDecimal(lId));

		// Chiama il Controller per il recupero delle Relazioni
		IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote();
		Vector lRelazioni = lCtrlRel.ExRicercaRelazioniByRichiesta(new BigDecimal(lId));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Numero di Relazioni collegate ->" + lRelazioni.size());

		// setLinkRitorno(); // Imposta come link di ritorno

		ritorno();

		String lStampabile = "SI";
		String lModificabile = "SI";
		String lTrasferibile = "NO";
		String lUpload = "SI";
		String lValidata = "NO";

		// Esegue il controllo di validità
		if (lRicMod.getFlagDocumentoRegistrato() != null
				&& lRicMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
			lModificabile = "NO";
			lTrasferibile = "SI";
			lUpload = "NO";
			lValidata = "SI";
		} else
			gestioneTemplate(lRicMod.getCodTipoRichiesta());

		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Trasferibile", lTrasferibile);
		setRequestAttribute("Upload", lUpload);
		setRequestAttribute("Validata", lValidata);
		setRequestAttribute("relazioni", lRelazioni);
		setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei
													// dati di sintesi SIEPE
		setRequestAttribute("richiesta", lRicMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Richiesta : " + lRicMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_DETTAGLIORICHIESTA;
	}

	/**
	 * Metodo, che gestisce il setLink di ritorno. E' stato creato tale metodo, poichè questa classe Azione,
	 * può essere ereditata e quindi è possibile che si possa desiderare che il ritorno sia gestito dalla
	 * classe figlia.
	 * <p>
	 * 
	 * @throws Exception
	 *             Propaga l'errore di eccezione.
	 */
	public void ritorno() throws Exception {
		setLinkRitorno();
	}

	// Funzione per la costruzione della combo con i template di stampa previsti la Richiesta
	private void gestioneTemplate(String aCodRichiesta) throws Exception {

		Option lOptTemplate = null;
		// La lista dei Template nella combo viene scelta in base al codice Attività.
		lOptTemplate = UtilTemplate.listaTemplateByRichiesta(aCodRichiesta);
		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		/*
		 * // template di default String[] lSelected = lOptTemplate.getSelecteds(); if (lSelected != null &&
		 * lSelected.length > 0) { setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE,
		 * lSelected[0]); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
		 * posto di LogF3B.getLogger() siesLogger.debug("TemplateDiDefault -> " + lSelected[0]); }
		 */
		return;
	}

}