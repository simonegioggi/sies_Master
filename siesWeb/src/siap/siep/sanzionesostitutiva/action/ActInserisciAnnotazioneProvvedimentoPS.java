package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActInserisciAnnotazioneProvvedimentoPS</p>
 * <p>Description: Annotazione in SIEP di un provvedimento SIUS di pena sostitutiva (Riforma Cartabia). Lo stato del procedimento non viene variato.</p>
 * @author SG
 * @since 20261124-MEV_2026-124_SG
 */
public class ActInserisciAnnotazioneProvvedimentoPS extends ActionSiap implements ICostantiSanzioneSostitutiva {

	/** Azione di dettaglio a cui reindirizzare; sovrascrivibile dalle sottoclassi. */
	protected String getActionDettaglio() {
		return "siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneProvvedimentoPS";
	}

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEventoSius = getRequestBigDecimalParameter(CAMPO_ID_SANZIONE_SOSTITUTIVA);
		if (lIdEventoSius == null || lIdEventoSius.toString().equals("")) {
			throw new F3BException(F3BException.USER_MESSAGE, "Selezionare un provvedimento dalla lista");
		}

		IScambioSanzione lCtrl = SIEPLookupRemote.getScambioSanzionRemote();
		ScambioSanzioneModel lProvv = lCtrl.ExRicercaProvvedimentoPenaSostitutivaByIdEvento(lIdEventoSius);
		if (lProvv == null || lProvv.getIdScambioSanzione() == null) {
			throw new F3BException(F3BException.USER_MESSAGE, "Provvedimento SIUS non trovato");
		}

		// Stato del procedimento: viene mantenuto quello corrente
		IStatoProcedimento lStatoCtrl = SIEPLookupRemote.getStatoProcedimentoRemote();
		Vector lStati = lStatoCtrl.ExRicercaStatoProcedimentoByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
		if (lStati != null && lStati.size() > 0) {
			StatoProcedimentoModel lCorrente = (StatoProcedimentoModel) lStati.get(0);
			lStatoProcMod.setCodStatoProcedimento(lCorrente.getCodStatoProcedimento());
		}
		lStatoProcMod.setProgressivo(new BigDecimal(1));
		lStatoProcMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lStatoProcMod.setData(DateUtils.getSysDate());
		lStatoProcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lStatoProcMod.setDataInserimento(DateUtils.getSysDate());
		lStatoProcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = SIEPLookupRemote.getPosizioneGiuridicaRemote()
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
						lFascMod.getIdFascicoloSiep());

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		EventoModel lEveMod = new EventoModel();
		lEveMod.setEveIdEvento(lProvv.getEveIdEvento());
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("04");
		lEveMod.setCodMotivo(lProvv.getCodTipoSanzione());
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodEsito("-");
		lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setCodMagistrato("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");

		EventoModel lRetModel = lCtrl.ExInserisciEventoAggiornaStatoProcedimento(lEveMod,
				lPos.getPosizioneGiuridica(), lPenaResMod, lStatoProcMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + getActionDettaglio() + "&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getIdEvento();
	}
}
