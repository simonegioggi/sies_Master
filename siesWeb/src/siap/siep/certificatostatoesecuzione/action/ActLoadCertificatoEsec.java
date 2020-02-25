package siap.siep.certificatostatoesecuzione.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.CertificatoStatoEsecController;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * 
 * Title: ActLoadCertificaEsec Description:
 *
 * @version 1.0
 */
public class ActLoadCertificatoEsec extends ActionSiap implements ICostantiCertificatoStatoEsec {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);

			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");

			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Vedo se l'utente in sessione è un utente di un ufficio
		// competente per il fascicolo
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

		// AMBROS 07/11 -->
		UfficioModel UffMod = new UfficioModel();
		UffMod = lUtenteMod.getUfficioUtente();

		String uffCompetenza = "1";
		if (!lUffUtente.equals(lFascMod.getCodUfficioInserimento()))
			uffCompetenza = "0";

		if (UffMod.isUfficioDiCompetenza(lFascMod.getCodUfficioInserimento()))
			uffCompetenza = "1";
		// --> END AMBROS

		// passo il flag alla jsp
		setRequestAttribute("UfficioCompetenza", uffCompetenza);
		// this.isFascicoloSiepDiCompetenza();

		// Trovo i Certificati già inseriti
		// chiama il controller
		CertificatoStatoEsecController lCtrl = new CertificatoStatoEsecController();

		// Instanzia il vettore
		// se non ci sono Certificati già inseriti accedo comunque alla pagina
		Vector lVect;
		try {
			lVect = lCtrl.ExRicercaCertificatoStatoEsecByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		} catch (F3BException ex) {
			lVect = null;
		}

		// setta la risposta nella request
		setRequestAttribute("ElencoCertificati", lVect);

		/*
		 * if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) { RedirectTo
		 * lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno() +
		 * "/" + lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
		 * lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 *
		 * return IWebConstants.PG_MESSAGE; }
		 */

		return PG_LOAD_STAMPA_CERTIFICATO;
	}
}