package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la load di inserimento di un difensore alla parte
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActLoadInserisciAvvocato extends ActionSiap implements ICostantiPartiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String idSoggetto = "";

		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV);

		// recupero le informazioni della parte interessata
		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrl
				.ExRicercaParteUdienzaByKey(new BigDecimal(idSoggetto));

		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);
		setRequestAttribute("idEventoUdienza", lIdEventoUdienza);

		// Gestione pulsante di ritorno.
		setLinkRitorno();

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					this.getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		Vector lVect = null;

		try {
			lVect = new Vector();
			lVect = lCtrl.ExRicercaDifensoreByIdSoggetto(new BigDecimal(idSoggetto));
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Elemento trovato");
		}

		if (lVect.size() > 0) {
			setRequestAttribute("modalita", "NoPop");
			setRequestAttribute("avvocato", lVect);

			return PG_ASSEGNA_INSERISCI_AVVOCATO; // restituisce la jsp di VIEW
		} else {
			Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
			String[] lFilter = { "-", "01", "02" };
			lOption.setFilter(lFilter);
			setRequestAttribute("tipoAvvocato", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("autoritaEsterna", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
			setRequestAttribute("autoritaEsternaDif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
			setRequestAttribute("motivoDesignazione", "" + lOption);

			setRequestAttribute("modalita", "I");

			UfficioModel lUffUte = this.getUfficioUtenteConnesso();
			String lDescrComune = lUffUte.getDescrComune();
			this.setRequestAttribute("comune", lDescrComune);

			lOption = new Option(DecodificheManager.getInstance().getForo(),
					lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
			setRequestAttribute("foro", "" + lOption);

			// MEV_21 Nuova gestione Combo per Stato di Nascita
			lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
			setRequestAttribute("nazione", "" + lOption);

			// MEV_21 Nuova gestione Combo per Stato Difensore
			lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
			setRequestAttribute("statoAvv", "" + lOption);

			return PG_LOAD_INSERISCIAVVOCATO; // restituisce la jsp di VIEW
		}
	}

}