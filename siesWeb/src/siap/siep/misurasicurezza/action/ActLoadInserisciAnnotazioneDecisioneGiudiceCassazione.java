package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciAnnotazioneDecisioneGiudiceCassazione
 * </p>
 * <p>
 * Description: Classe Action per la load iscrizione Annotazione dei provvedimenti
 * </p>
 * <p>
 * del Giudice su faascicoli di Misure di Sicurezza Provvisorie o Fuori sentenza
 * </p>
 */
public class ActLoadInserisciAnnotazioneDecisioneGiudiceCassazione extends ActionSiap implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Funzione di esclusiva competenza dei proc. di classe IV.
		if (lFascMod.getChiaveProgr().intValue() < 40000 || lFascMod.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimenti di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		this.isEventoNonValidato();

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
		// return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");
		if (lPenaResMod != null)
			setRequestAttribute("penaresidua", lPenaResMod);

		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		MisuraSicurezzaModel lMisSicMod = null;
		if (lListMis.size() > 0)
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
		else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento privo di Misura di Sicurezza, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);

		// carico il tipo provvedimento
		Option lOptionTipProv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTipProv.setFilter(new String[] { "-", "01", "02", "03" }); // SENTENZA, DECRETO o ORDINANZA
		lOptionTipProv.setSelected("-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionTipProv);

		// AUTORITA' EMITTENTE
		Option lOptionEmi = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionEmi.setFilter(new String[] { "-", "CSS", "CAP", "CAS", "CASAP", "GIP", "GIPM", "GUP", "GUPM",
				"CAPSM", "DIB", "RIE", "DIBM" });
		setRequestAttribute("autoritaEmittente", "" + lOptionEmi);

		// TIPOLOGIA OGGETTO_PROVVEDIMENTO
		Collection lContenuto = (Collection) DecodificheManager.getInstance().getOggettoProcedimentoMS();
		setRequestAttribute("contenuto", lContenuto);

		// MOTIVO PROVVEDIMENTO
		Collection lOggetto = (Collection) DecodificheManager.getInstance().getMotivoProvvedimento();
		setRequestAttribute("oggetto", lOggetto);

		// ESITO TENORE / ESITO PROVVEDIMENTO
		Collection lEsito = (Collection) DecodificheManager.getInstance().getEsitoTenore();
		setRequestAttribute("esito", lEsito);

		// NATURA MISURA SICUREZZA
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// TIPO MISURA SICUREZZA
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		return PG_LOAD_INS_ANNOTAZIONE_DECISIONE_GIUDICE_CASSAZIONE;
	}

}