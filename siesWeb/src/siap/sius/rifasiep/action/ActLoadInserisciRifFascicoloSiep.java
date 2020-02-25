package siap.sius.rifasiep.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
//import siap.sico.utente.model.UtenteModel;
//import siap.sico.security.action.ICostantiSecurity;
//import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRifFasicoloSiep
 * </p>
 * <p>
 * Description: Classe Action per la LoadInserisciRifFasicoloSiep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciRifFascicoloSiep extends ActionSius
		implements ICostantiRifFascicoloSiep, ICostantiFascicoloSius {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
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

		// Si esegue controllo lo stato del fascicolo.
		// Da come si evince non è possibile utilizzare la funzione se lo stato è :
		// COD_DEFINITO = 01
		// COD_UNIFICATO = 05
		if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals(COD_DEFINITO)
				|| lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals(COD_UNIFICATO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"L'inserimento non è consentito per i procedimenti definiti!");

		/*
		 * STUB : 20080123 - Commentato per nuova eliminazione controllo su stato 10 eseguita
		 * reimplementazione ( vedi sopra ). // Se il procedimento è definito non viene consentita
		 * l'operazione. if( lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("02")!= 0 )
		 * throw new SIUSException( SIUSException.USER_MESSAGE,
		 * "L'inserimento non è consentito per i procedimenti definiti!" );
		 */

		// Si Mette in sessione il fascicolo SIUS.
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		// Lettura del procedimento SIEP.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();
			lFasMod.setIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			IFascicoloSiep lCtrlSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasMod = lCtrlSiep
					.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			setSessionAttribute("fascicolo", lFasMod);
		}
		//// else
		//// throw new SIUSException( SIUSException.USER_MESSAGE, "Il procedimento Sius manca del riferimento
		//// dell'Esecuzione!." );

		// Si Imposta l'Autorita Competente.
		// Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"GP", "PM", "PMM", "PGCAP", "TRIBSD", "CAPSM" } ); //solo le
		// Autorità competenti.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(), "-");
		lOption.setSelected("PM");
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		// Si Imposta il Tipo Provvedimento (con Rv_Abbreviation = "C").
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentoCumulo());
		lOption.setValueBlankItem("-");
		lOption.setAddBlankItem(true);
		setRequestAttribute("TipoProvvedimenti", "" + lOption);

		// Si Imposta l'Autorita Emittente.
		// lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente());
		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioCumuloSentenzaDecreto(), "-");
		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), "-");
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCumuloUfficioLoginRifSiep(), "-");
		setRequestAttribute("AutoritaEmittente", "" + lOption);

		// Si Imposta il Tipo Provvedimento per M.SIC (Titoli non presenti su SIAP)
		// (con Rv_Abbreviation = "C" and Rv_Low_Value="03").
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentoMSic());
		lOption.setValueBlankItem("-");
		lOption.setAddBlankItem(true);
		setRequestAttribute("TipoProvvedimentiMSic", "" + lOption);

		// Si Imposta l'Autorita Emittente per M.SIC
		// lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente());
		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioCumuloSentenzaDecreto(), "-");
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioCumuloRifMSic(), "-");
		setRequestAttribute("AutoritaEmittenteMSic", "" + lOption);

		// Imposta la Combo contenente la Tipologia di Numerazione (Es. Mis. Sic. oppure Es. Pene Pec.).
		lOption = new Option(DecodificheManager.getInstance().getTipologiaNumerazione());
		setRequestAttribute("tipologiaNumerazione", "" + lOption);

		setRequestAttribute("modalita", "I");
		setRequestAttribute("LuogoUtenteConnesso", this.getUfficioUtenteConnesso().getDescrComune());

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_INSERISCIRIFASIEP; // restituisce la jsp di VIEW
	}

}