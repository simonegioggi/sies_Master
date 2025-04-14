package siap.sige.udienzaparti.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.action.ICostantiAvvocato;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciAssegnaAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Avvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActLoadInserisciAssegnaAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		if (!isRequestParameterNullObj("numeroDifensori")
				&& getRequestStringParameter("numeroDifensori").equals("2")) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione: Sono già assegnati due difensori!");
		}

		// Identificativo della parte
		String lIdSoggetto = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);

		IPartiUdienza lCtrlPU = SIGELookupRemote.getPartiUdienzaRemote();

		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrlPU
				.ExRicercaParteUdienzaByKey(new BigDecimal(lIdSoggetto));

		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" tipoDifensore ");

		if (!isRequestParameterNullObj(CAMPO_ID_AVVOCATO)) {
			String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

			AvvocatoModel lAvv = lCtrl.ExRicercaAvvocatoByKey(new BigDecimal(lAvvId));
			setRequestAttribute("avvocato", lAvv);
		}

		if (!isRequestParameterNullObj("tipoDifensore") && (getRequestStringParameter("tipoDifensore")
				.equalsIgnoreCase("D'UFFICIO")
				|| getRequestStringParameter("tipoDifensore").equalsIgnoreCase("DELLA FASE DI GIUDIZIO"))) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"I difensori possono essere due solo se entrambi sono di fiducia!");
		}

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

		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));

		gestioneRitorno();

		UfficioModel lUffUte = getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		setRequestAttribute("comune", lDescrComune);

		lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(),
				Option.NO_BLANK_ITEM);
		setRequestAttribute("foro", "" + lOption);

		// MEV_21 Nuova gestione Combo per Stato di Nascita
		lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazione", "" + lOption);

		// MEV_21 Nuova gestione Combo per Stato Difensore
		lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
		setRequestAttribute("statoAvv", "" + lOption);

		return ICostantiPartiUdienza.PG_LOAD_INSERISCIAVVOCATO; // restituisce la jsp di VIEW
	}

}