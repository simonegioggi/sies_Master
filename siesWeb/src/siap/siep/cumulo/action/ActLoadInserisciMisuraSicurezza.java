package siap.siep.cumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraSicurezza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		preparaForm(lFascMod);

		return siap.siep.cumulo.action.ICostantiCumulo.PG_LOAD_INSERISCIMISURASICUREZZA; // restituisce la jsp
																							// di VIEW

	}

	@SuppressWarnings("rawtypes")
	protected void preparaForm(FascicoloSiepModel lFascMod) throws F3BException {

		// ComboBOX X Natura Misura Sicurezza
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("PosizioneGiuridicaLuogoAltra", lPos);

		// ==========================================================================
		// Carica la Pena Residua NON validata inserita più di recente, dovrebbe
		// essere quella calcolata in fase di inserimento dei dati del cumulo.
		// Attenzione!! ciò non è necessariamente vero, se è stato fatto solo il primo
		// calcolo della pena (sul cumulante)
		// MAC - Va verificato che sia presente una pena cumulo associata al record
		// cumulo non validato (quelli di appoggio, il primo inserito)
		// ==========================================================================
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

	}

}