package siap.sius.titoloesecutivo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.siep.util.SIEPLookupRemote;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.fascicolo.controller.IFascicoloSiep;import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
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
 * Title: ActLoadModificaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per la LoadModificaTitoloEsecutivo
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

public class ActLoadModificaTitoloEsecutivo extends ActionSius implements ICostantiTitoloEsecutivo,
		ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// STUB 20080122 Commentata poichè non utilizzata
		// Recupero dell'utente dalla sessione.
		// UtenteModel lUtenteMod =
		// (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Lettura del procedimento SIUS.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		BigDecimal aIdFascicoloSius = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS);
		lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(aIdFascicoloSius);

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByKey(aIdFascicoloSius);

		// STUB 20080122 - Commentata poichè cambiata logica. Vedi nuova implemntazione
		/*
		 * // Se il procedimento è definito non viene consentita l'operazione.
		 * if((lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01")== 0 ) ||
		 * (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05")== 0 ) ||
		 * (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10")== 0 )) throw new
		 * SIUSException( SIUSException.USER_MESSAGE,
		 * "La ridefinizione del Titolo Esecutivo Principale non è consentita per i procedimenti definiti!" );
		 */

		// TODO carmela modifica del 11/10/2013 Mev "Revisione Misure di Sicurezza SIUS"
		// Modifica del 11/10/2013 Mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo

		// Se il procedimento è definito non viene consentita l'operazione.
		// if( lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo(COD_DEFINITO)== 0 ||
		// lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo(COD_UNIFICATO)== 0 )
		// throw new SIUSException( SIUSException.USER_MESSAGE,
		// "La ridefinizione del Titolo Esecutivo Principale non è consentita per i procedimenti definiti!" );

		// Se il procedimento non fa riferimento a un titolo esecutivo non viene consentita l'operazione.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il procedimento non fa riferimento ad un procedimento SIEP! Ridefinizione non consentita.");

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

		setRequestAttribute("modalita", "M");
		setRequestAttribute("LuogoUtenteConnesso", super.getUfficioUtenteConnesso().getDescrComune());

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