/**
 *
 */
package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.NuovaIstanzaController;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInoltroPM
 * 
 * @author Giselda De Vita
 *
 */
public class ActLoadAssociaRIaFascicoloSIEP extends ActionSiap implements ICostantiNuovaIstanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/* Controllo se un fascicolo e' presente in sessione altrimenti lo faccio selezionare */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.nuovaistanza.action.ActLoadAssociaRIaFascicoloSIEP&"
					+ "TipoFasc=NuovaIstanza";
			return lPage;
		}

		isFascicoloSiepDiCompetenza();

		// ricerca Istanza per il fascicolo in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getChiaveProgr().intValue() < 90000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il procedimento corrente non è un Registro Istanza." + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr() + "<br>Impossibile procedere.");
			setRequestAttribute(IWebConstants.GOTO_PAGE,
					"/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=21120240");

			return IWebConstants.PG_MESSAGE;
		}

		if (lFascMod.getCodStatoFascicolo().compareTo("01") == 0)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione : Il fascicolo indicato è già archiviato! Impossibile procedere.");

		NuovaIstanzaController lIstController = new NuovaIstanzaController();
		Collection<NuovaIstanzaModel> lVect = lIstController
				.ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(lFascMod.getIdFascicoloSiep());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SIZE del registro istanze " + lVect.size());
		if (lVect == null || (lVect != null && lVect.size() == 0)) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessuna Istanza presente legata al procedimento " + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr() + "<br>Impossibile procedere.");
			setRequestAttribute(IWebConstants.GOTO_PAGE,
					"/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=21120240");

			return IWebConstants.PG_MESSAGE;
		}

		this.setRequestAttribute("listaIstanze", lVect);

		// Inserire Eventuali ComboBOX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza());
		setRequestAttribute("oggettoIstanza", "" + lOption);

		if (!isRequestParameterNullObj("AnnoCumulato")) {
			// ========================================================================
			// Tasto Carica - Acquisizione da stesso ufficio o distretto
			// ========================================================================
			FascicoloSiepModel lFasCumulato = new FascicoloSiepModel();
			BigDecimal AnnoCumulato = getRequestBigDecimalParameter("AnnoCumulato");
			BigDecimal NumeroCumulato = getRequestBigDecimalParameter("NumeroCumulato");
			UtenteModel lUtenteMod = new UtenteModel(
					(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

			// IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			// UfficioModel lUffMod = new UfficioModel();
			// lUffMod = lUff.getUfficioByCodTipoUffDescrComune(getRequestStringParameter("AutoritaInt"),
			// getRequestStringParameter("LuogoFas"));

			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasCumulato.setChiaveAnno(AnnoCumulato);
			lFasCumulato.setChiaveProgr(NumeroCumulato);
			lFasCumulato.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio());

			// Perchè due ricerche?
			Vector lFasCumulati = (lFasc.ExRicercaFascicoloOnView(lFasCumulato));
			if (lFasCumulati.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo indicato non esiste! Impossibile procedere.");

			lFasCumulato = (FascicoloSiepModel) lFasCumulati.get(0);
			lFasCumulato = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasCumulato);

			if (lFasCumulato.getCodStatoFascicolo().equals("02"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo indicato non è validato! Impossibile procedere.");

			if (lFasCumulato.getCodStatoFascicolo().equals("01"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione : Il fascicolo indicato risulta archiviato! Impossibile procedere.");

			setRequestAttribute("Cumulato", lFasCumulato);

		}

		// Restituisce SUCCESS
		return PG_LOAD_ASSOCIA_ISTANZA;

	}

}