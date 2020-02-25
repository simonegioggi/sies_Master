package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciAnnotazioneDesignazioneIstituto
 * </p>
 * <p>
 * Description: form per inserimento Annotazione
 * </p>
 * <p>
 * della designazione Istituto da parte del DAP
 * </p>
 * <p>
 * per applicazione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 */
public class ActInserisciAnnotazioneDesignazioneIstituto extends ActionSiap implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		String lCodPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		String lPage = "";

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String lDataOd = DateUtils.getDateToString(DateUtils.getSysDate(), "dd/MM/yyyy");
		Date lDataEmissione = DateUtils.getDate(lDataOd, "dd/MM/yyyy");

		Date lDataRicezione = getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO);
		Date lDataDesignazione = getRequestDateParameter(
				ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

//		String IdMis = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);

		EventoModel lEveMod = new EventoModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");
		lEveMod.setCodMotivo("1130");
		// (non ?) menzionabile per Certificato Stato Esecuzione
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");

		lEveMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveMod.setDataEmissione(lDataEmissione);
		lEveMod.setDataRicezioneAtti(lDataRicezione); // Data pervenimento

		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);

		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);

		lEveMod.setCodEsito("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");
		// ----------
		VerbaleModel lVerMod = new VerbaleModel();

		lVerMod.setCodTipoVerbale("-");
		lVerMod.setDataPervenimento(lDataRicezione);
		lVerMod.setDataEmissione(lDataDesignazione);

		if (!this.isRequestParameterNullObj(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lVerMod.setIstDetIdIstitutoDetenzione(this
					.getRequestStringParameter(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE));
		} else {
			lVerMod.setIstDetIdIstitutoDetenzione("-");
		}

		// if(!this.isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST)
		// &&
		// !this.isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST))
		if (!this
				.isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_TIPO_AUTORITA_DESIGNAZIONE_IST)
				&& !this.isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_TIPO_AUTORITA_DESIGNAZIONE_IST)) {
			// ComuneModel lComMod =
			// this.getCodComuneByDescr(getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST));
			ComuneModel lComMod = this.getCodComuneByDescr("ROMA");

			lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
			lVerMod.setCodTipoUfficioFirmatario(this
					.getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_TIPO_AUTORITA_DESIGNAZIONE_IST));
		} else {
			lVerMod.setCodTipoUfficioFirmatario("-");
			lVerMod.setCodLuogoUfficioFirmatario("-");
		}

		lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lVerMod.setDataInserimento(DateUtils.getSysDate());

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO) != null
				&& !getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO).equals("")) {
			lVerMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));
		}
		//
		// -----------
		//
		// Pena residua
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);

		// ---> INSERIMENTO EVENTO di tipo ANNOTAZIONE

		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();

		BigDecimal lIdEve = lCtrl.ExInserisciEventoVerbale(lEveMod, lVerMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioDesignazioneIstituto&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEve;

		return lPage;
	} // Chiude processRequest

} // Chiude Action