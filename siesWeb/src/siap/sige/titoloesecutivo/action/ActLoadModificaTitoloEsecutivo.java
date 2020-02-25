package siap.sige.titoloesecutivo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadModificaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per la LoadModificaTitoloEsecutivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadModificaTitoloEsecutivo extends ActionSige implements ICostantiTitoloEsecutivo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();

		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Se il procedimento non fa riferimento a un titolo esecutivo non viene consentita l'operazione.
		if (lFasSigeEsteso.getFascicoloSiep() == null) {
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Il procedimento non fa riferimento ad un procedimento SIEP! Ridefinizione non consentita.");
		}

		// Si Imposta l'Autorita Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "GP", "PM", "PMM", "PGCAP", "TRIBSD", "CAPSM", "TDS", "UDS" }); // solo
																											// le
																											// Autorità
																											// competenti.
		lOption.setSelected("PM");
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		setRequestAttribute("LuogoUtenteConnesso", super.getUfficioUtenteConnesso().getDescrComune());

		setRequestAttribute("modalita", "M");

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_ASSEGNA_TITOLOESECUTIVO; // restituisce la jsp di VIEW
	}

}