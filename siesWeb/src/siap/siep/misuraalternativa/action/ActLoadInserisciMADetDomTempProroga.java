package siap.siep.misuraalternativa.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.SIAPException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciMADetDomTempProroga
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Concessione Detenzione Domiciliare
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

public class ActLoadInserisciMADetDomTempProroga extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
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
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		// posizione Precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		String lFlagAffi = "N";

		if (lPosPre != null && lPosPre.getCodPosizioneGiuridica() != null && lPos != null
				&& lPos.getPosizioneGiuridica() != null
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null && lPosPre.isLibero()
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("12")) {
			lFlagAffi = "S";
		}
		setRequestAttribute("lFlagAffi", lFlagAffi);

		// verifica se è stata fatta una concessione o una proroga
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl
				.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		if (lMisAlModConcessa == null || lMisAlModConcessa.getIdMisuraAlternativa() == null
				|| lMisAlModConcessa.getCodTipoDecisione() == null
				|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
				|| lMisAlModConcessa.getCodNaturaDecisione() == null
				|| !lMisAlModConcessa.getCodNaturaDecisione().equals("DD")
				|| lMisAlModConcessa.getCodTipoMisura() == null
				|| (!lMisAlModConcessa.getCodTipoMisura().equals("0011")
						&& !lMisAlModConcessa.getCodTipoMisura().equals("0197"))) {
			throw new F3BException(SIAPException.USER_MESSAGE,
					"Non è possibile effettuare la proroga di una detenzione a termine non concessa.");
		}

		// ricerca magistrato competente
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

		// Autorità esterna N
		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

		// Autorità esterna C
		Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

		// setto il campo codice motivo
		Option lOption = new Option(
				DecodificheManager.getInstance().getMotivoProvvedimentoMADetDomTempProroga());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		setRequestAttribute("tipoMisura", PROROGA);
		setRequestAttribute("azione", "siap.siep.misuraalternativa.action.ActInserisciMADetDomTempProroga");
		setRequestAttribute("tipmis", "PROROGA CONCESSIONE DETENZIONE DOMICILIARE A TERMINE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_DET_DOM_TEMP;
	}
}