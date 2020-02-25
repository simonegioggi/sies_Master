package siap.siep.penasospesa.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRichiesta
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Richiesta, questa viene specializzata per i vari tipi
 * di richiesta.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author Luigi
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciRichiesta extends ActionSiap implements ICostantiPenaSospesa {
	protected String mNomeAction;
	protected String mNomeJsp;

	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		/* Controllo se un fascicolo e' presente in sessione altrimenti lo faccio selezionare */
		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + mNomeAction;
			return lPage;
		}
		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Controllo Validazione Fascicolo
		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		// Controllo evento non Fascicolo paolo cherubini 01/07/2011
		this.isEventoNonValidato();

		// paolo cherubini 30/06/2011 menu fruibile solo per pene sospese classe III
		FascicoloSiepModel lFasMod = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));

		// AMBROS 14/11/2013 Se Fascicolo di Ufficio Accorpato, il controllo è su CHIAVE_PROGR_ORIG --->
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

		UfficioModel UffMod = new UfficioModel();
		UffMod = lUtenteMod.getUfficioUtente();

		if (lUffUtente.equals(lFasMod.getCodUfficioInserimento())) {
			int NumFasc = lFasMod.getChiaveProgr().intValue();
			if (NumFasc < 30000 || NumFasc > 40000) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFasMod.getChiaveAnno()
						+ "/" + lFasMod.getChiaveProgr() + " non è di classe III. Impossibile procedere!");
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
		} else {
			if (UffMod.isUfficioDiCompetenza(lFasMod.getCodUfficioInserimento())) {
				int NumFascOrig = lFasMod.getChiaveProgrOrig().intValue();
				if (NumFascOrig < 30000 || NumFascOrig > 40000) {
					RedirectTo lRedirigi = new RedirectTo();
					lRedirigi.setPage(IWebConstants.PG_MAIN);
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Il Procedimento N." + lFasMod.getChiaveAnno() + "/" + lFasMod.getChiaveProgr()
									+ " di Ufficio accorpato non è di classe III. Impossibile procedere!");
					lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
							+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
					return IWebConstants.PG_MESSAGE;
				}
			}
		}

		// ---> END AMBROS

		return preparazioneForm();

	}

	/**
	 * La funzione prepara i dati necessari alla form di input.
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected String preparazioneForm() throws F3BException {
		setRequestAttribute("modalita", "I");

		return mNomeJsp; // restituisce la jsp di VIEW
	}

	/**
	 * Si effettua una ricerca dei Benefici Concessi e di tipo Sospensione Condizionale per valorizzare il
	 * Tipo di obbligo in maniera conforme al primo beneficio nella lista.
	 * 
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	protected String ricercaBeneficio() throws F3BException {

		String lCodSospCond = "-";
		BeneficioModel lBenMod = new BeneficioModel();
		lBenMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		// Beneficio concesso
		lBenMod.setCodNaturaBeneficio("C");
		// Sospensione Condizionale
		lBenMod.setCodTipoBeneficio("01");

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lBeneficii = lCtrl.ExRicercaBeneficio(lBenMod);

		if (lBeneficii.size() != 0) {
			// Si seleziona il codice dal primo Beneficio nella lista
			BeneficioModel lBeneficio = (BeneficioModel) lBeneficii.get(0);
			lCodSospCond = lBeneficio.getCodTipoSospSubordinata();
			// setta la risposta nella request
			setRequestAttribute("benefici", lBeneficii);

		}
		return lCodSospCond;
	}

}