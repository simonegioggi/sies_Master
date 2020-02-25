package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActDettaglioLiberazioneAnticipata</p>
 * <p>Description: Classe Action per il dettaglio dei periodi di liberazione anticipata</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

public class ActDettaglioLiberazioneAnticipata extends ActionSiap implements ICostantiLibertaAnticipata {
	/**
	 * Azione di Dettaglio del Liberazione Anticipata
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		LicenzaLibAnticipataModel lLicModel = null;
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// RICERCA POSIZIONE GURIDICA
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		BigDecimal mIdEvento = null;
		setRequestAttribute("posizioneluogoaltra", lPos);
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			mIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		} else {
			lLicModel = lCtrlLib
					.ExRicercaLicenzaLibanticipataByKey(getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA));

			mIdEvento = lLicModel.getEveIdEvento();
		}


		// RICERCA DEPOSITO_ORDINANZA_PC
		IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(mIdEvento);

		setRequestAttribute("DepositoOrdinanzaPc", lDepOrdMod);

		// RICERCA PERIODI LICENZA
		Vector lLicenzePeriodi = lCtrlLib.ExRicercaLicenzeLibanticipataByEve(mIdEvento);

		if (!lLicenzePeriodi.isEmpty()) {
			LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel) lLicenzePeriodi
					.firstElement();
			String lCodUffEmi = lLicenzaPeriodiModel.getLicenza().getCodUfficioEmittente();

			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);

			lLicenzaPeriodiModel.getLicenza().setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		}

		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// PENA RESIDUA
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		setRequestAttribute("penaresidua", lPenaResidua);

		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		// PENA COMPLESSIVA
		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null
				&& lPenComMod.getCodTipoPenaDetentiva() != ""
				&& (lPenComMod.getCodTipoPenaDetentiva().equals("03") || lPenComMod.getCodTipoPenaDetentiva()
						.equals("04"))) {
			lFlagErgastolo = "S";
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_DETTAGLIO_LIBANTICIPATA;
	}

}