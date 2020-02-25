package siap.siep.penasospesa.action;

import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciAnnotazioneRevoca
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Annotazione Revoca Beneficio ex art.168 c.p. - 674
 * c.p.p.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciAnnotazioneRevoca extends ActionSiap implements ICostantiPenaSospesa {

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
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.penasospesa.action.ActLoadInserisciAnnotazioneRevoca";

			return lPage;
		}
		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Controllo Validazione Fascicolo
		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		// Controllo evento non validato paolo cherubini 01/07/2011
		this.isEventoNonValidato();

		// Controllo presenza Benefici - 18/04/2019 MEV70.
		if (this.assenzaBeneficio())
			return IWebConstants.PG_MESSAGE;

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
	 * La funzione prepara i dati necessari alla form di input. Separata dalla processRequest() per poter
	 * essere ereditata.
	 *
	 * @return
	 * @throws F3BException
	 */
	protected String preparazioneForm() throws F3BException {

		// Collection lColMotivo = DecodificheManager.getInstance().getMotivoProvvedimento();

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		setRequestAttribute("oggetto", lColMotivo);

		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		// Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
		// DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });

		lOption = new Option(lColl, "-");
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		setRequestAttribute("modalita", "I");
		// setRequestAttribute("ChiaveFascicolo",getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

		return PG_LOAD_INSERISCIANNOTAZIONEREVOCA; // restituisce la jsp di VIEW
	}

	/**
	 * Si effettua una ricerca dei Benefici Concessi e di tipo Sospensione Condizionale per impedire
	 * l'inserimento della Annotazione Revoca.
	 *
	 * @return
	 * @throws F3BException
	 */
	protected boolean assenzaBeneficio() throws F3BException {

		// String lCodSospCond = "-";
		BeneficioModel lBenMod = new BeneficioModel();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lBenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// Beneficio concesso
		lBenMod.setCodNaturaBeneficio("C");
		// Sospensione Condizionale
		lBenMod.setCodTipoBeneficio("01");

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lBeneficii = lCtrl.ExRicercaBeneficio(lBenMod);

		if (lBeneficii.size() == 0) {
			// throw new F3BException(F3BException.USER_MESSAGE,
			// "Nel Procedimento non è presente il beneficio della Sospensione Condizionale della pena.
			// Provvedere all'inserimento.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nel Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è presente il beneficio della Sospensione Condizionale della pena. Provvedere all'inserimento.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		} else {
			setRequestAttribute("benefici", lBeneficii);
			return false;
		}
	}

}