package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;

/**
 * <p>
 * Title: ActDettaglioAnnotazioneRevocaConversioneSanzSost
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio della revoca/conversione sanzione sostitutiva
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActDettaglioAnnotazioneRevocaConversioneSanzSost extends ActSIESDettaglioProvvedimento implements
		ICostantiPenaPecuniaria {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("motivo", lEveMod.getEvento().getCodMotivo().toString());

		// Scambio sanzione
		// N.B. Il dettaglio in oggetto interviene per 2 tipi di EVENTO, afferenti allo stesso provvedimento
		// di
		// Archiviazione per Revoca Sanzione Sostitutiva in pena detentiva (Art. 66 L. 689/81 per Pene
		// Pecuniarie).
		// In tale provvedimento viene archiviato il Fascicolo di classe VII e creato un nuovo Fascicolo di
		// classe I.
		// Per il fascicolo di classe VII il puntamento a SCAMBIO_SANZIONE è diretto, tramite EVE_ID_EVENTO.
		// Per il fascicolo di classe I il puntamento a SCAMBIO_SANZIONE è indiretto, tramite un doppio
		// puntamento a EVE_ID_EVENTO.
		IScambioSanzione lCtrlSc = SIEPLookupRemote.getScambioSanzionRemote();
		int lFascProg = lFascMod.getChiaveProgr().intValue();
		if (lFascProg > 70000 && lFascProg < 80001) {
			ScambioSanzioneModel scSanzioneMod = lCtrlSc
					.ExRicercaScambioSanzioneByEveIdEventoNoControlValid(lEveMod.getEvento().getEveIdEvento());
			setRequestAttribute("scambiosanzione", scSanzioneMod);
			// Lettura della pena residua del Fascicolo di classe I;
			// Se la pena è scaduta va segnalato all' utente.
			if (Utils.isNullObj(lFascMod.getFasSieIdFascicoloSiep())) {
			} else {
				IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel lUltimaPenaValidataClasseI = lCtrlPenRes
						.ExRicercaPenaResiduaUltimaValidata(lFascMod.getFasSieIdFascicoloSiep());
				if (lUltimaPenaValidataClasseI != null && lUltimaPenaValidataClasseI.getDataFine() != null
						&& lUltimaPenaValidataClasseI.getDataFine().before(DateUtils.getSysDate())) {

					IFascicoloSiep lCtrlFS = SIEPLookupRemote.getFascicoloSiepRemote();
					FascicoloSiepModel lFasModClasseI = new FascicoloSiepModel();
					if (lFasModClasseI != null && lFasModClasseI.getIdFascicoloSiep() != null)
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info(
								">>>>>>>>>>>>>> letto fascicolo Classe I "
										+ lFasModClasseI.getIdFascicoloSiep().toString());
					lFasModClasseI = lCtrlFS.ExRicercaFascicoloByKeyNoError(lFascMod
							.getFasSieIdFascicoloSiep());
					if (lFasModClasseI != null)
						setRequestAttribute("fascicoloClasseI", lFasModClasseI);
				}

			}

		} else {
			EventoNotificaModel lEve2Mod = lCtrl.ExRicercaEventoNotificaByKey(lEveMod.getEvento()
					.getEveIdEvento());
			ScambioSanzioneModel scSanzioneMod = lCtrlSc
					.ExRicercaScambioSanzioneByEveIdEventoNoControlValid(lEve2Mod.getEvento()
							.getEveIdEvento());
			setRequestAttribute("scambiosanzione", scSanzioneMod);
		}

		// Preparazione codice Motivo Provvedimento per Rideterminazione della pena.
		// Codici MOTIVO_PROVVEDIMENTO 1015/1016 abbinati rispettivamente ai codici 1011/1012
		// Revoca/Conversione Sanzione Sostitutiva in pena detentiva (Art. 66 L. 689/81 per Pene Pecuniarie)
		Collection lColTipoProvvedimento = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lColTipoProvvedimento = lDecodifiche.ExRicercaDecodifiche(lModel);
		String lCodMotivo = DecodificheUtils.getCodAlt2byCode(lColTipoProvvedimento, lEveMod.getEvento()
				.getCodMotivo().toString());
		String lDescMotivo = DecodificheUtils.getDescbyCode(lColTipoProvvedimento, lEveMod.getEvento()
				.getCodMotivo().toString());
		setRequestAttribute("motivoRidetPena", lCodMotivo);
		setRequestAttribute("descMotivoRidetPena", lDescMotivo);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Penaresidua
		PenaResiduaModel lPenaResidua = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResidua);

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		if (lSSResiduaModel != null && lSSResiduaModel.getIdSanzioneSostResidua() != null) {
			lPenaResidua.setSanzSostResidua(lSSResiduaModel);
		}

		// Annotazione manuale
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnn.ExRicercaAnnotazioniManualiByIdEvento(lIdEvento);
		setRequestAttribute("annotazione", lAnnMod);

		// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10
		// Ricerca Magistrato
		String lCodmag = "";
		if (lEveMod.getEvento().getCodMagistrato() != null)
			lCodmag = lEveMod.getEvento().getCodMagistrato();

		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = (MagistratoModel) lCtrlM.ExRicercaMagistratoByCod(lCodmag);

		setRequestAttribute("magistrato", lMagi);
		// 01-06-2016 - END Riciclo

		return PG_DETTAGLIO_ANNOTAZIONE_REVOCA_CONV_SANZ_SOST;
	}

}