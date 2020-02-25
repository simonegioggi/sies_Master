package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActDettaglioidimensionamentoLA
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio dei periodi di liberazione anticipata
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
public class ActDettaglioRidimensionamentoLA extends ActSIESDettaglioProvvedimento implements
		ICostantiLibertaAnticipata {

	/**
	 * Azione di Dettaglio del Ridimensionamento Liberazione Anticipata
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
					.ExRicercaLicenzaLibanticipataByKey(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_LIC_ID_LICENZA_LIBANTICIPATA));

			mIdEvento = lLicModel.getEveIdEvento();
		}

		// RICERCA PERIODI LICENZA
		Vector lLicenzePeriodi = lCtrlLib.ExRicercaLicenzeLibanticipataByEve(mIdEvento);
		// int lTotGiorni = 0;

		if (!lLicenzePeriodi.isEmpty()) {
			LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel) lLicenzePeriodi
					.firstElement();
			String lCodUffEmi = lLicenzaPeriodiModel.getLicenza().getCodUfficioEmittente();

			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);

			lLicenzaPeriodiModel.getLicenza().setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
			// lTotGiorni = lLicenzaPeriodiModel.getLicenza().getNumeroGiorni().intValue();
		}

		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// PENA RESIDUA
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = lPenResCtrl.ExRicercaPenaResiduaByIdEvento(mIdEvento);
		PenaResiduaModel lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);

		setRequestAttribute("penaresidua", lPenRes);
		setRequestAttribute("penaresiduavalidata", lUltimaPenResVal);
    // Fungibilità
    IFungibilita lCtrlFung = SIEPLookupRemote.getFungibilitaRemote();
    FungibilitaModel lFungCalcolata = lCtrlFung.ExRicercaFungibilitaByKeyEvento (mIdEvento);
    setRequestAttribute("fungibilita", lFungCalcolata);

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

		// ============================================
		// Ricerca evento di computo inserito
		// ============================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(mIdEvento);

		setRequestAttribute("evento", lEveComputo);
		if (lEveComputo.getCodMotivo().equalsIgnoreCase("0995")) {
			setRequestAttribute(ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA,
					ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM);
		} else {
			setRequestAttribute(ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA,
					ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA);
		}

		// ============================================
		// Recupero il campo nota
		// ============================================
		ICampoNota lCtrlCampoNota = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCampoNotaModel = lCtrlCampoNota.ExRicercaCampoNotaByIdEvento(mIdEvento);

		setRequestAttribute("aCampoNota", lCampoNotaModel);

		// ============================================
		// Ricerca Magistrato
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveComputo.getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// return PG_LOAD_DETTAGLIO_LIBANTICIPATA;
		return IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/DettaglioRidetPenaRidimLA.jsp";
	}

}