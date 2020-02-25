package siap.sige.titoloesecutivo.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDeassegnaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per la LoadDeassegnaTitoloEsecutivo
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDeassegnaTitoloEsecutivo extends ActionSius implements ICostantiTitoloEsecutivo,
		ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIGE dalla sessione.
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Se il procedimento è definito non viene consentita l'operazione.
		if (lFasSigeEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo(COD_DEFINITO) == 0
				|| lFasSigeEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo(COD_UNIFICATO) == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"La deassegnazione del Titolo Esecutivo Principale "
							+ "non è consentita per i procedimenti definiti!");
		// Se il procedimento non fa già riferimento a un titolo esecutivo non viene consentita l'operazione.
		if (lFasSigeEsteso.getFascicoloSiep() == null
				|| lFasSigeEsteso.getFascicoloSiep().getIdFascicoloSiep() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il procedimento non fa riferimento ad un procedimento SIEP !");

		// Si Mette in sessione il fascicolo SIGE.
		setSessionAttribute("fascicoloSigeEsteso", lFasSigeEsteso);

		// Si Imposta l'Autorita Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "GP", "PM", "PMM", "PGCAP", "TRIBSD", "CAPSM", "TDS", "UDS" }); // solo
																											// le
																											// Autorità
																											// competenti.
		lOption.setSelected("PM");
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		setRequestAttribute("LuogoUtenteConnesso", super.getUfficioUtenteConnesso().getDescrComune());
		setRequestAttribute("modalita", "I");

		// Inizializzazione Combo relative al Soggetto
		SoggettoModel lSoggetto = lFasSigeEsteso.getSoggetto();
		String codStatoNazione = "039";
		if (lSoggetto != null && lSoggetto.getCodStatoNascita() != null) {
			codStatoNazione = lSoggetto.getCodStatoNascita();
		}

		// Riempie la combo delle nazioni
		lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
				.getNazioni(), "-"), codStatoNazione);
		setRequestAttribute("nazioni", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + lOption);

		String codStatoCittadinanza = "039";
		if (lSoggetto != null && lSoggetto.getNazionalita() != null) {
			codStatoCittadinanza = lSoggetto.getNazionalita();
		}

		// lOption = new Option(
		// DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getStatoCittadinanza(),"-"),
		// codStatoCittadinanza);
		lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(), codStatoCittadinanza);
		setRequestAttribute("StatoCittadinanza", "" + lOption);

		// Flag Data Nascita Presunta
		lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), "N");
		setRequestAttribute("dataNascitaPresunta", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_DEASSEGNA_TITOLOESECUTIVO_SIGE; // restituisce la jsp di VIEW
	}
}