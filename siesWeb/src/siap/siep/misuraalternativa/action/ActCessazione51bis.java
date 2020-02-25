package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActCessazione51bis extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Gestisce la chiamata proveniente dalla griglia delle funzioni e prepara la form di inserimento dei dati
	 * dell'ordinanza
	 * 
	 * @throws Exception
	 */
	public String loadInserisciProvvedimetoSorveglianza() throws F3BException {
		setRequestAttribute("isInsProvvSorv", "S");

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
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// rimuovo la pena dalla sessione
		this.removeSessionAttribute("penaresidua51bisMDS");

		// Controllo Esistenza pena residua validata per quel fascicolo
		// FIXME verificare se è il caso di prendere solo quella validata
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = null;
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null) {
			// if ( lPos.getPosizioneGiuridica().isLibero()) {
			// throw new SIEPException(SIEPException.USER_MESSAGE,
			// "Non risulta nessun provvedimento per questo fascicolo. Impossibile eseguire l'operazione.");
			// }
			// else
			// {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente o non Validata. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
			// }
		}

		setRequestAttribute("penaresidua", lPenaResMod);

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// ==========================================================================
		// Combo tipo Ufficio Sorveglianza
		// ==========================================================================
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		lOption.setFilter("UDS");
		lOption.setSelected("UDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		// ==========================================================================
		// Combo Tipo Provvedimenti SIUS
		// ==========================================================================
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<DecodificheModel>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		return null;
	}

	/**
	 * Gestisce la chiamata proveniente dalla action di Inserimento dopo la insert della
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String loadInserisciProvvedimetoEsecuzione() throws F3BException {
		setRequestAttribute("isInsProvvSorv", "N");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Recupero il provvedimenti SIUS di Cessazione
		// ==========================================================================
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMACessazione = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		setRequestAttribute("misuraalternativa", lMACessazione);

		UfficioModel lUffEmittenteMod = getUfficioByCodUfficio(lMACessazione.getChiaveUfficioFascicoloSius());
		setRequestAttribute("UfficioEmittente", lUffEmittenteMod);
		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// ==========================================================================
		// Recupero la Pena Residua in Corso
		// ==========================================================================
		if (!this.isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)
				&& this.getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null) {
			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));

			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenaResCorrenteMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResCorrenteMod);
		}

		// ==========================================================================
		// Recupero la Pena Residua Rideterminata (se presente) e la passo sulla
		// request per la visualizzazione ed eventuale modifica fine pena
		// ==========================================================================
		if (!isSessionAttributeNullObj("penaresidua51bisMDS")) {
			CalcoloPenaModel lCalcoloPenaModel = (CalcoloPenaModel) getSessionAttribute("penaresidua51bisMDS");
			PenaResiduaModel lPenModel = lCalcoloPenaModel.getPenaResiduaRicalcolata();
			setRequestAttribute("nuovapenaresidua", lPenModel);
			setRequestAttribute("isPenaRideterminata", "S");
		}

		// ===================================================
		// Magistrato
		// ===================================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ===================================================
		// Destinatari
		// ===================================================
		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		return null;
	}

}