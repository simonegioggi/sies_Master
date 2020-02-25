package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActRevoca
 * </p>
 * <p>
 * Description: Classe Action Padre delle classi LoadInserisciRevoca
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
@SuppressWarnings("rawtypes")
public class ActRevoca extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Effettua i controlli in comune alle classi di LoadInserisci, recupera e carica sulla request i dati da
	 * visualizzare. n.b. In assenza della pena residua validata o meno retituisce una pagina di errore
	 *
	 * @return La pagina di visualizzazione di un messaggio di errore o "" in caso tutto OK
	 * @throws F3BException
	 */
	protected String getRevoca() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		// magistrato
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMADetDom());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		setRequestAttribute("tipoRevoca", "DETENZIONE");

		Option lOptionTipoUff = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("tipoUfficioSIUS", "" + lOptionTipoUff);

		// Combo Tipo Provvedimenti SIUS
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getDescrComune();

		setRequestAttribute("distretto", StrCodiceDistrettoUtente);

		return "";
	}

	/**
	 * revoca con calcolo
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected String getRevocaConCalcolo() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Controllo Esistenza pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		if (!this.isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)
				&& this.getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null) {
			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResMod);

			Object lAttrSess = getSessionAttribute("REMApenaresidua");

			PenaResiduaModel lPenModel = null;
			if (lAttrSess instanceof PenaResiduaModel) {
				lPenModel = (PenaResiduaModel) lAttrSess;
			} else {
				CalcoloPenaModel lCalcoloPenaModel = (CalcoloPenaModel) lAttrSess;
				lPenModel = lCalcoloPenaModel.getPenaResiduaRicalcolata();
			}

			setRequestAttribute("nuovapenaresidua", lPenModel);
		} else {
			// rimuovo la pena dalla sessione
			this.removeSessionAttribute("REMApenaresidua");

			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
			if ((lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
					&& lPos.getPosizioneGiuridica().isLibero())
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Non risulta nessun provvedimento per questo fascicolo. Impossibile eseguire l'operazione.");
			else if ((lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
					&& !lPos.getPosizioneGiuridica().isLibero()) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente o non Validata. Eseguire Calcolo della pena?");
				lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
						+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
			setRequestAttribute("penaresidua", lPenaResMod);
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// Posizione giuridica precedente
		PosizioneGiuridicaModel lPosPreMod = new PosizioneGiuridicaModel();
		lPosPreMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneprecedente", lPosPreMod);

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// STUB 14/12/2005 Si imposta il Tipo Ufficio SIUS.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		// Combo Tipo Provvedimenti SIUS
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getDescrComune();

		setRequestAttribute("distretto", StrCodiceDistrettoUtente);

		return "";
	}

}