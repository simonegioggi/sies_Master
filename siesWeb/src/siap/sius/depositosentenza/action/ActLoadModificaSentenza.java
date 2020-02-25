package siap.sius.depositosentenza.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaSentenza
 * </p>
 * <p>
 * Description: Classe Action devoluta alla preparazione della Form di modifica di una Sentenza.
 * </p>
 * <p>
 * Poichè i dati da visualizzare sono gli stessi utilizzati per la visualizzazione del Dettaglio, la classe è
 * ottenuta come specializzazione della ActDettaglioEmissioneSentenza in modo da poter utilizzare le stesse
 * funzioni per ricavare i dati.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadModificaSentenza extends ActDettaglioEmissioneSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// Si utilizza la jsp di Dettaglio
		String lRetPage = super.processRequest();

		// Si richiama il lock
		lockApplicativo("Sentenza");

		TenoreModel[] lTenori = super.mSenEveTenPreMod.getTenori();
		if (lTenori != null) {
			// Preparazione delle combo Esiti, una per ogni oggetto
			int lNumOggetti = lTenori.length;
			String[] lEsiti = new String[lNumOggetti];
			for (int i = 0; i < lNumOggetti; i++)
				// poichè il cod esito è l'HIGH VALUE nel dominio ESITO_TENORI occorre ritradurlo (!)
				lEsiti[i] = getEsiti(lTenori[i].getCodOggettoTenore(), lTenori[i].getCodEsitoTenore());
			// trasferimento lista esiti
			setRequestAttribute("esiti", lEsiti);
		}

		// LISTA UFFICI per notifica all'avvocato
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "22" };
		Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
		lOptionAvv.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOptionAvv);

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl
				.ExRicercaAvvocatiByFascicoloNoError(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("avvocato", lAvvocato);

		setRequestAttribute("modalita", "M");
		return lRetPage;
	}

	// Preleva l'Elenco di Esiti corrispondente ad un dato oggetto.
	private String getEsiti(String codiceOggetto, String acodAltEsitoSelezionato) throws Exception {

		if (acodAltEsitoSelezionato != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> " + acodAltEsitoSelezionato);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito Alternativo selezionato -> null ");

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetto);
		String lCodEsitoSelezionato = DecodificheUtils.getCodebyCodAlt(lColl, acodAltEsitoSelezionato);
		Option lOption = new Option(lColl, lCodEsitoSelezionato);
		if (lCodEsitoSelezionato != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito selezionato -> " + lCodEsitoSelezionato);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codice esito selezionato -> null ");

		return lOption.toString();
	}

}