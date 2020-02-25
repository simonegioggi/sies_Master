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
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 *
 * @author d.fiorletta
 *
 */
public class ActProsecuzione51Bis extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	/**
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	protected String getProsecuzione() throws F3BException {
		// ==========================================================================
		// Controlli preliminari
		// ==========================================================================
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

		String lCodPosGiuAltraCausa = "";
		if (lPos.getAltraCausa() != null && !lPos.getAltraCausa().toString().equals("")) {
			lCodPosGiuAltraCausa = lPos.getAltraCausa().getCodTipoPosGiuridica().trim();

			if (lCodPosGiuAltraCausa.equals("74") || lCodPosGiuAltraCausa.equals("75")
					|| lCodPosGiuAltraCausa.equals("76") || lCodPosGiuAltraCausa.equals("77")
					|| lCodPosGiuAltraCausa.equals("78") || lCodPosGiuAltraCausa.equals("79")
					|| lCodPosGiuAltraCausa.equals("80") || lCodPosGiuAltraCausa.equals("81")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Posizione Giuridica non gestita per il provvedimento selezionato");
			}
		}

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Controllo Esistenza pena residua
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = null;

		// Con Cumulo/Senza Cumulo
		if ("S".equals(lFascMod.getFlagCumulante()))
			setRequestAttribute("isConCumulo", "S");
		else
			setRequestAttribute("isConCumulo", "N");

		// ==========================================================================
		//
		// ==========================================================================
		if (isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS)) {
			// Provengo dalla griglia delle funzione delle Misure. Devo caricare la
			// form per l'inserimento del provvedimento SIUS
			this.removeSessionAttribute("MAPenaResiduaRideterminata");

			setRequestAttribute("isFaseOrdinanza", "S");

			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
			lPenaResMod = lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

			// FIXME DL146/2013 verificare come comportarsi se non presente la pena residua validata
			// n.b. se non presente vuol dire che non ci sono provvedimenti quindi può essere
			// solo il caso di Libero-Detenuto altra causa e sto inserendo la prosecuzione senza cumulo.

			// if ( (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null ) &&
			// lPos.getPosizioneGiuridica().isLibero())
			// {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Non risulta nessun provvedimento per
			// questo fascicolo. Impossibile procedere.");
			// }
			// else if ( (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null) &&
			// !lPos.getPosizioneGiuridica().isLibero())
			// {
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente o non
			// Validata. Eseguire Calcolo della pena?");
			// lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
			// ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			//
			// return IWebConstants.PG_MESSAGE;
			// }
			if (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
				lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
						+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("penaresidua", lPenaResMod);

			// Combo uffici SIUS
			Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
			setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

			// Combo Tipo Provvedimenti SIUS
			Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
			lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
			lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
			lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
			Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
			lOptionTipoProvvSorv.setSelected("-");
			setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		} else {
			// ========================================================================
			// Provengo dalla Action di Inserimento dopo la registrazione del
			// provvedimento (decreto/ordinanza) SIUS e devo ricaricare la
			// form con i dati del decreto/ordinanza e i campi per il caricamento
			// del provvedimento di Esecuzione, quindi con i destinatari
			// ========================================================================
			setRequestAttribute("isFaseOrdinanza", "N");

			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResMod);
			if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
				setRequestAttribute("dataeditabile", "S");

			// Passo sulla request la Pena Residua Rideterminata messa precedentemente in sessione
			PenaResiduaModel lPenModel = (PenaResiduaModel) getSessionAttribute("MAPenaResiduaRideterminata");
			setRequestAttribute("nuovapenaresidua", lPenModel);

			// Misura alternativa
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMAProsecuzione = lMisAltCtrl
					.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
			setRequestAttribute("misuraalternativa", lMAProsecuzione);

			// Ufficio Sorveglianza emittente (Ordinanza)
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = lCtrlUffEmi
					.getUfficioByKey(lMAProsecuzione.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffMod);

			// Magistrato firmatario
			IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
			MagistratoCompetenteMagistratoModel lMagMod = lMagComp
					.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
			if (lMagMod != null)
				setRequestAttribute("magistratocompetente", lMagMod);

			// Destinatari
			IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
			Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);

			// UNEP (per la notifica agli avvocati)
			Option lOptionUNEP = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22"); // UNEP
			setRequestAttribute("autoritaEsternaAvv", "" + lOptionUNEP);

			// Autorità esterna C
			Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);
		}

		return "";
	}
}
