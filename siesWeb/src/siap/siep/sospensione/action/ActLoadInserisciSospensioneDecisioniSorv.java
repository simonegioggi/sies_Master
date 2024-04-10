package siap.siep.sospensione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * ActLoadInserisciSospensioneDecisioniSorv - Classe Action per la load inserimento di Sospensione Decisioni
 * Sorveglianza
 *
 * @version 1.0
 */
public class ActLoadInserisciSospensioneDecisioniSorv extends ActionSiap implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		isFascicoloSiepDiCompetenza();

		if (isFascicoloNonValidato() || isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		/******************************* Posizione Giuridica **********************************/
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		/******************************* Pena Complessiva *****************************/
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03"))
				lFlagErgastolo = "S";
			else if (lPenComMod.getCodTipoPenaDetentiva().equals("04"))
				lFlagErgastolo = "D";
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/
		/*********************************** Pena Residua ***************************/

		// Controllo Esistenza pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)
				&& getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null) {
			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResMod);

			PenaResiduaModel lPenModel = (PenaResiduaModel) getSessionAttribute("SOSPpenaresidua");
			setRequestAttribute("nuovapenaresidua", lPenModel);

			if (!isSessionAttributeNullObj("SOSPENSIONE")) {
				SospensioneModel lSospModel = (SospensioneModel) getSessionAttribute("SOSPENSIONE");
				setRequestAttribute("sospensione", lSospModel);
			}
		} else {

			isEventoNonValidato();
			// rimuovo la pena dalla sessione
			removeSessionAttribute("SOSPpenaresidua");
			// rimuovo la sospensione dalla sessione
			removeSessionAttribute("SOSPENSIONE");

			if (lPosizione.getCodPosizioneGiuridica() != null
					&& (!lPosizione.getCodPosizioneGiuridica().equals("07")
							&& !lPosizione.getCodPosizioneGiuridica().equals("16")
							&& !lPosizione.getCodPosizioneGiuridica().equals("17")
							&& !lPosizione.getCodPosizioneGiuridica().equals("46")
							&& !lPosizione.getCodPosizioneGiuridica().equals("47"))) // LIBERO
			{
				lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
			} else {
				lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
			}

			// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
			// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
			String lErrore = null;
			String lAzioneChiamante = null;

			if (lPenaResMod == null) {
				// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
				lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
				lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
			} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("16")
					&& !lPosizione.getCodPosizioneGiuridica().equals("17")
					&& !lPosizione.getCodPosizioneGiuridica().equals("46")
					&& !lPosizione.getCodPosizioneGiuridica().equals("47")) // non è libero
					&& (lPenaResMod.getDataInizio() == null // non ha le date
							|| lPenaResMod.getDataFine() == null)
					&& lFlagErgastolo.equals("N")) {
				lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
				lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
			}

			if (lErrore != null) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
				lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
						+ getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("penaresidua", lPenaResMod);
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// Se posizione = 46 cerca i dati della SOSPENSIONE
		if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null
				&& lPosizione.getCodPosizioneGiuridica().equals("46")) // LIBERO IN SOSPENSIONE
		{
			/******************************* SOSPENSIONE ***********************************/
			// Se è libero in sospensione mi aspetto una pena sospesa validata e sospesa
			// con il relativo record di sospensione, se non è l'ultima la cerco
			if (lPenaResMod.getFlagPenaSospesa() == null || (lPenaResMod.getFlagPenaSospesa() != null
					&& lPenaResMod.getFlagPenaSospesa().equals("N"))) {
				PenaResiduaModel lPenaResiduaSospesaValidata = lPenResCtrl
						.ExRicercaPenaResiduaUltimaValidataSospesa(lIdFascicolo);

				if (lPenaResiduaSospesaValidata == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"La posizione giuridica è LIBERO IN SOSPENSIONE ma non esiste una pena residua validata e sospesa. Impossibile eseguire la richiesta.");
			}

		}

		// misura alternativa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();

		MisuraAlternativaModel lMisSospesa = null;
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS)) {
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lMisSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

			if (lMisSospesa != null) {
				UfficioModel lUffMod = new UfficioModel();
				lUffMod = lCtrlUffEmi.getUfficioByKey(lMisSospesa.getChiaveUfficioFascicoloSius());

				setRequestAttribute("UfficioEmittente", lUffMod);
			}
		}

		setRequestAttribute("misuraalternativa", lMisSospesa);

		// tipo provvedimento
		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Autorità esterna
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutoritaE);

		// STUB 14/12/2005 Si imposta il Tipo Ufficio SIUS.
		Option lOptionSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("tipoUfficioSIUS", "" + lOptionSIUS);

		// OGGETTO DECISIONE
		Option lOptionOggetto = new Option(DecodificheManager.getInstance().getOggettoDecisione());
		setRequestAttribute("motivoProvv", "" + lOptionOggetto);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_SOSPENSIONE_DECISIONI_SORVEGLIANZA;
	}

}