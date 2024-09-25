package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
//01-06-2016 - Riciclo dopo Primo Collaudo per V.10
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.scambiosanzione.action.ICostantiScambioSanzione;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

//01-06-2016 - END Riciclo 

/**
 * <p>
 * Title: ActInserisciAnnotazioneRevocaConversioneSanzSost
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di una Annotazione Revoca/Conversione Sanzione Sostitutiva su
 * Pene Pecuniarie. viene variato lo stato di esecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi S.p.A
 * </p>
 * 
 * @version 1.0.
 */
public class ActInserisciAnnotazioneRevocaConversioneSanzSost extends ActMisuraAlternativa implements
		ICostantiPenaPecuniaria, ICostantiSanzioneSostitutiva {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idScSanzione = getRequestBigDecimalParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA);
		String lPage = null;
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String GRec = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_GIORNI_RECLUSIONE);
		String MRec = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_MESI_RECLUSIONE);
		String ARec = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_ANNI_RECLUSIONE);

		String GArr = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_GIORNI_ARRESTO);
		String MArr = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_MESI_ARRESTO);
		String AArr = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NUM_ANNI_ARRESTO);

		IScambioSanzione lCtrl = SIEPLookupRemote.getScambioSanzionRemote();
		ScambioSanzioneModel scSanzioneMod = null;
		if (idScSanzione != null && !idScSanzione.toString().equals(""))
			scSanzioneMod = lCtrl.ExRicercaScambioSanzioneById(idScSanzione);

		EventoNotificaModel lEveUffMod = null;
		DepositoOrdinanzaPcModel lDepOrdMod = null;
		DepositoDecretoModel lDepDecMod = null;
		TenoreModel lTenMod = null;

		// Se non è presente la sanzione sostitutiva la inserisco
		if (scSanzioneMod == null) {
			String lTipoProvvedimento = null;
			if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO)) {
				lTipoProvvedimento = getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO);
			}
			String lMotivoProvvedimento = null;
			if (!this.isRequestParameterNullObj(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE)) {
				lMotivoProvvedimento = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE);
			}

			// INSERISCO EVENTO
			lEveUffMod = new EventoNotificaModel();
			lEveUffMod.getEvento().setCodMotivo(
					getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE)); // (Codici
																									// 1011 /
																									// 1012
																									// Revoca/Conversione
																									// Sanzione
																									// Sostitutiva
																									// in pena
																									// detentiva
																									// (Art.
																									// 66 L.
																									// 689/81
																									// per
																									// Pene
																									// Pecuniarie)).
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(
					ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE);
			lEveUffMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveUffMod.getEvento(),
					lTipoProvvedimento, lCodiceUffEmi, lComModAutEmi, lDataEmisTras));
			lEveUffMod.getEvento().setFlagDocumentoRegistrato("N");

			// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10
			lEveUffMod.getEvento().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			// 01-06-2016 - END Riciclo

			// Setto il deposito ordinanza/decreto

			if ("03".equals(lTipoProvvedimento)) {
				lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdMod
						.setAnnoS3(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO));
				lDepOrdMod
						.setNumS3(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO));

				lDepOrdMod.setDataUdienza(getRequestDateParameter(
						ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE));
				lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
				lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
				lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
				lDepOrdMod.setCodUfficioMagistratoComp(lCodiceUffEmi);

			} else if ("02".equals(lTipoProvvedimento)) {
				lDepDecMod = new DepositoDecretoModel();
				lDepDecMod
						.setAnnoS72(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO));
				lDepDecMod
						.setNumS72(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO));
				lDepDecMod.setDataEmissione(getRequestDateParameter(
						ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE));
				lDepDecMod.setCodUfficioInserimento(lCodiceUfficio);
				lDepDecMod.setCodOperatoreInserimento(lCodiceOperatore);
				lDepDecMod.setDataInserimento(DateUtils.getSysDate());
				lDepDecMod.setCodUfficioCompetente(lCodiceUffEmi);
			}

			// Setto il tenore
			lTenMod = new TenoreModel();
			lTenMod.setCodEsitoTenore("0211");

			lTenMod.setData(getRequestDateParameter(ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE));

			lTenMod.setCodOggettoTenore(lMotivoProvvedimento);
			lTenMod.setProgrTenore(new BigDecimal(1));
			lTenMod.setCodUfficioInserimento(lCodiceUfficio);
			lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
			lTenMod.setDataInserimento(DateUtils.getSysDate());

			// Setto ScambioSanzione
//			String lNatura = null;
			scSanzioneMod = new ScambioSanzioneModel();

			scSanzioneMod.setCodTipoDecisione(lTipoProvvedimento);
			scSanzioneMod.setCodNaturaSanzione("CS");
			scSanzioneMod.setCodTipoSanzione(lMotivoProvvedimento);

			scSanzioneMod
					.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO));
			scSanzioneMod
					.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO));
			scSanzioneMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			scSanzioneMod.setCodUfficioInserimento(lCodiceUfficio);
			scSanzioneMod.setCodOperatoreInserimento(lCodiceOperatore);
			scSanzioneMod.setDataInserimento(DateUtils.getSysDate());

			scSanzioneMod.setCodUfficioEmittente(lCodiceUffEmi);
			scSanzioneMod.setCodUfficioSorveglianza(lCodiceUffEmi);
			scSanzioneMod.setDataEmissione(getRequestDateParameter(
					ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE));

			if (!ARec.equals(""))
				scSanzioneMod.setNumAnniReclusione(new BigDecimal(ARec));
			if (!MRec.equals(""))
				scSanzioneMod.setNumMesiReclusione(new BigDecimal(MRec));
			if (!GRec.equals(""))
				scSanzioneMod.setNumGiorniReclusione(new BigDecimal(GRec));

			if (!AArr.equals(""))
				scSanzioneMod.setNumAnniArresto(new BigDecimal(AArr));
			if (!MArr.equals(""))
				scSanzioneMod.setNumMesiArresto(new BigDecimal(MArr));
			if (!GArr.equals(""))
				scSanzioneMod.setNumGiorniArresto(new BigDecimal(GArr));

		}

		// ========= modifica dello stato del procedimento ==========
		// preparo il model da passare al controller per l'aggiornamento
		StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

		// imposto i valori nel model
		lStatoProcMod.setProgressivo(new BigDecimal(1));
		lStatoProcMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lStatoProcMod.setData(DateUtils.getSysDate());
		lStatoProcMod.setCodStatoProcedimento("0235");
		lStatoProcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lStatoProcMod.setDataInserimento(DateUtils.getSysDate());
		lStatoProcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		// luogo detenzione
		LuogoDetenzioneModel lLuogoDetenzione = new LuogoDetenzioneModel();

		lLuogoDetenzione.setIstDetIdIstitutoDetenzione("-");
		lLuogoDetenzione.setDataInizioDetenzione(DateUtils.getSysDate());
		lLuogoDetenzione.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lLuogoDetenzione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLuogoDetenzione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLuogoDetenzione.setDataInserimento(DateUtils.getSysDate());

		// Gestione Flag 'Detenuto Per Altra Causa' sulla tabella FASCICOLO_SIEP
		if (isRequestChecked(ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA))
			lFascMod.setFlagAltraCausa("S");
		else
			lFascMod.setFlagAltraCausa("N");

		lFascMod.setCodTipoPosLibero("-");

		AltraCausaModel lAltraCausa = null;
		if (!this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA)) {
			lAltraCausa = new AltraCausaModel();

			lAltraCausa
					.setCodTipoPosGiuridica(getRequestStringParameter(ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA));

			lAltraCausa.setCodAutorita("-");
			lAltraCausa.setCodLuogo("-");

			lAltraCausa.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAltraCausa.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAltraCausa.setDataInserimento(DateUtils.getSysDate());
			lAltraCausa.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		}

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		EventoModel lEveMod = new EventoModel();

		lEveMod.setEveIdEvento(scSanzioneMod.getEveIdEvento());
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");

		// Codici MOTIVO_PROVVEDIMENTO 1011 / 1012 Revoca/Conversione Sanzione Sostitutiva in pena detentiva
		// (Art. 66 L. 689/81 per Pene Pecuniarie)
		// Abbinati rispettivamente ai codici 0261 / 0262 provenienti dalla sorveglianza.
		String lCodMotivoSorv = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE);
		Collection lColTipoProvvedimento = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lColTipoProvvedimento = lDecodifiche.ExRicercaDecodifiche(lModel);
		String lCodMotivo = DecodificheUtils.getCodAlt2byCode(lColTipoProvvedimento, lCodMotivoSorv);
		lEveMod.setCodMotivo(lCodMotivo);

		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodEsito("-");
		lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		Date lDataRicezione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);
		lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEveMod.setDataRicezioneAtti(lDataRicezione);
		lEveMod.setFlagDocumentoRegistrato("N");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");

		// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10
		// lEveMod.setCodMagistrato("-");
		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		// 01-06-2016 - END Riciclo

		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");

		// Annotazione Manuale
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		lAnnMod.setFasSieIdFascicoloSiep(scSanzioneMod.getFasSieIdFascicoloSiep());
		lAnnMod.setCodTipoAnnotazione("015"); // sanzioni sostitutive
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setFlagPiuMeno("-");

		lAnnMod.setFlagValidato("S");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");

		lAnnMod.setCodDpr("-");
		lAnnMod.setFlagConforme("-");

		if (!ARec.equals(""))
			lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
		if (!MRec.equals(""))
			lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
		if (!GRec.equals(""))
			lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

		if (!AArr.equals(""))
			lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
		if (!MArr.equals(""))
			lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
		if (!GArr.equals(""))
			lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		EventoModel lRetModel = lCtrl.ExInserisciRevocaConversione(lEveUffMod, lDepOrdMod, lDepDecMod,
				lTenMod, scSanzioneMod, lLuogoDetenzione, lAltraCausa, lEveMod, lPos.getPosizioneGiuridica(),
				lFascMod, lPenaResMod, lStatoProcMod,
				this.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA),
				lAnnMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penapecuniaria.action.ActDettaglioAnnotazioneRevocaConversioneSanzSost&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento();

		return lPage;
	}

}