package siap.sius.titoloesecutivo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.siep.util.SIEPLookupRemote;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.fascicolo.controller.IFascicoloSiep;import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
//import siap.sico.utente.model.UtenteModel;
//import siap.sico.security.action.ICostantiSecurity;
//import siap.sico.web.ActionSiap;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDeassegnaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per la LoadDeassegnaTitoloEsecutivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull S.p.A.
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

		// Recupero dell'utente dalla sessione.
		// UtenteModel lUtenteMod =
		// (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Lettura del procedimento SIUS.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		BigDecimal aIdFascicoloSius = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS);
		lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(aIdFascicoloSius);

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByKey(aIdFascicoloSius);

		// Se il procedimento è definito non viene consentita l'operazione.
		if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo(COD_DEFINITO) == 0
				|| lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo(COD_UNIFICATO) == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"La deassegnazione del Titolo Esecutivo Principale "
							+ "non è consentita per i procedimenti definiti!");

		// Se il procedimento non fa già riferimento a un titolo esecutivo non viene consentita l'operazione.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il procedimento non fa riferimento ad un procedimento SIEP !");

		// Si Mette in sessione il fascicolo SIUS.
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

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
		// Riempie la combo delle nazioni
		lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
				.getNazioni(), "-"), "039");
		setRequestAttribute("nazioni", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + lOption);

		// 28/04/2010 lOption = new Option( DecodificheManager.getInstance().getNazionalita(),"I");
		// setRequestAttribute("nazionalita", "" + lOption );
		lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
				.getStatoCittadinanza(), "-"), "039");
		setRequestAttribute("StatoCittadinanza", "" + lOption);

		// Flag Data Nascita Presunta
		lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), "N");
		setRequestAttribute("dataNascitaPresunta", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_DEASSEGNA_TITOLOESECUTIVO; // restituisce la jsp di VIEW
	}

}