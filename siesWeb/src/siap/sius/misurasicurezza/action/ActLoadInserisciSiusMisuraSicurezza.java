package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciSiusMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraSicurezza
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
public class ActLoadInserisciSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza {

	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		this.setRequestAttribute("idfascicolo", lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() + "");

		/*
		 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato Numero MEV : SIES v10
		 * Autore : gioggi Data : 27/gen/2016 Branch : MEV_SIES v10
		 */
		// Se sono già stati emessi provvedimenti o il fascicolo non è in stato 'Iscritto' non sono consentite
		// modifiche alle misure
		// 18/02/2015 Correzione anomalia in fase di inserimenento Mis.Sic.
		// RicercaProvvedimentiUtil lRicerca = new
		// RicercaProvvedimentiUtil(lFasGPMod.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());

		// 15/06/2015
		// RicercaProvvedimentiUtil lRicerca = new
		// RicercaProvvedimentiUtil(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		// boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		// if ( lEsistenzaDoc ||
		// lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("02") != 0 )
		// throw new
		// SIUSException(SIUSException.USER_MESSAGE,"Impossibile inserire nuove misure per questo fascicolo!");
		// ***** FINE INTERVENTO MEV_SIES v10 *****//

		// parametro passato solo nel caso di iscrizione guidata
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		// TODO carmela verificare
		// Imposta l'elenco Riferimento Titoli Esecutivi associati al Procedimento SIUS.
		BigDecimal idFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		ITitoloEsecutivo lRifTitoloEsec = SIUSLookupRemote.getTitoloEsecutivoRemote();
		Option lOption = null;
		lOption = new Option(lRifTitoloEsec.ExRicercaTitoloEsecutivoByIdFascicoloSius(idFascicoloSius));
		setRequestAttribute("riferimentoTitoloEsecutivo", "" + lOption);

		preparaForm();

		return PG_LOAD_INSERISCI_SIUS_MISURASICUREZZA; // restituisce la jsp di VIEW
	}

	@SuppressWarnings("rawtypes")
	protected void preparaForm() throws F3BException {

		// Inserire Eventuali ComboBOX
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());

		if (super.getFiltroMinorenni().equalsIgnoreCase("true")) {
			lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezzaMinorenni());
			// MEV10-s3: aggiunta nuova gestione liste per tipo natura
			String codNatura = this.getParameter("CodNatura");
			if (codNatura != null) {
				if ("02".equals(codNatura))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniNonDetentiva());
				else if ("01".equals(codNatura))
					lOptionT = new Option(DecodificheManager.getInstance().getTipoMSMinorenniDetentiva());
				setRequestAttribute("codNatura", codNatura);
			}
			String fasSieIdFascicoloSiepRif = this.getParameter("FasSieIdFascicoloSiepRif");
			if (fasSieIdFascicoloSiepRif != null)
				setRequestAttribute("fasSieIdFascicoloSiepRif", fasSieIdFascicoloSiepRif);
			String numAnni = this.getParameter("NumAnni");
			if (numAnni != null)
				setRequestAttribute("numAnni", numAnni);
			String numMesi = this.getParameter("NumMesi");
			if (numMesi != null)
				setRequestAttribute("numMesi", numMesi);
			String numGiorni = this.getParameter("NumGiorni");
			if (numGiorni != null)
				setRequestAttribute("numGiorni", numGiorni);
			FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			setRequestAttribute("codTipoUfficio", lFasGPMod.getFascicoloSiusModel().getCodTipoUfficio());
		}

		// variabile che verrà passata alla jsp
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
		setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

		// MERGE v10: sovrascrivo il "tipoMisuraSicurezza" poichè nella pagina ci aspettiamo un vettore
		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = null;
		if (super.getFiltroMinorenni().equalsIgnoreCase("true")) {
			lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezzaMinorenni();
		} else {
			lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		}
		
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
	}

}