package siap.siep.provvedimentogenerico.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: ActInserisciProvvedimentoGenerico
 * </p>
 * <p>
 * Description: Classe Action per l'inserisci provvedimento generico derivato da Decisioni della Sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciProvvedimentoGenerico extends ActProvvedimentoGenerico {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		// String lTipoRegistro = getRequestStringParameter("tiporegistro");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String lTipoProvvedimento = getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO);
		// Controllo sull'esistenza dell'ufficio per quel comune
		String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_AUTORITA),
				getRequestStringParameter(CAMPO_SEDE_AUTORITA));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_AUTORITA)));

		// Evento
		EventoModel lEveMod = new EventoModel();
		lEveMod.setCodTipoEvento("01"); // Provvedimento
		lEveMod.setCodTipoProvvedimento(lTipoProvvedimento);
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_OGGETTO));
		lEveMod.setCodUfficioEmittente(lCodiceUffEmittente);
		lEveMod.setCodLuogoEmittente(lComMod.getCodComune());
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
				CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagDocumentoRegistrato("N");
		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// Dario -- Viviana 22-09-2006 -- Per permettere di individuare l'evento di provvedimento generico
		lEveMod.setTemIdTemplate("*");

		// setto il campo note
		CampoNotaModel lCamNot = new CampoNotaModel();
		lCamNot.setCodOperatoreInserimento(lCodiceOperatore);
		lCamNot.setCodUfficioInserimento(lCodiceUfficio);
		lCamNot.setDataInserimento(DateUtils.getSysDate());
		lCamNot.setFasSieIdFascicoloSiep(lIdFascicolo);
		if (getRequestStringParameter(CAMPO_NOTE) != null
				&& !getRequestStringParameter(CAMPO_NOTE).equals(""))
			lCamNot.setDescr(getRequestStringParameter(CAMPO_NOTE));
		else
			lCamNot.setDescr("-");

		lCamNot.setProgressivo(new BigDecimal(1));

		// DEPOSITO DECRETO O DEPOSITO ORDINANZA DIPENDE DAL TIPO PROVVEDIMENTO
		DepositoDecretoModel lDepDecMod = null;
		DepositoOrdinanzaPcModel lDepOrdMod = null;

		if (lTipoProvvedimento.equals("03")) {
			// setto il deposito ordinanza
			lDepOrdMod = new DepositoOrdinanzaPcModel();
			lDepOrdMod.setAnnoS3(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
			lDepOrdMod.setNumS3(getRequestBigDecimalParameter(CAMPO_NUMERO_PROVVEDIMENTO));
			lDepOrdMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));

			lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
			lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
			lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
		} else if (lTipoProvvedimento.equals("02")) {
			// setto il deposito decreto
			lDepDecMod = new DepositoDecretoModel();
			lDepDecMod.setAnnoS72(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
			lDepDecMod.setNumS72(getRequestBigDecimalParameter(CAMPO_NUMERO_PROVVEDIMENTO));
			lDepDecMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
			lDepDecMod.setCodUfficioInserimento(lCodiceUfficio);
			lDepDecMod.setCodOperatoreInserimento(lCodiceOperatore);
			lDepDecMod.setDataInserimento(DateUtils.getSysDate());
		}

		// setto il tenore
		TenoreModel lTenMod = new TenoreModel();
		lTenMod.setCodEsitoTenore(getRequestStringParameter(CAMPO_ESITO));
		lTenMod.setData(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lTenMod.setCodOggettoTenore(getRequestStringParameter(CAMPO_OGGETTO));
		lTenMod.setProgrTenore(new BigDecimal(1));
		lTenMod.setCodUfficioInserimento(lCodiceUfficio);
		lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
		lTenMod.setDataInserimento(DateUtils.getSysDate());

		// inserimento
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		EventoModel lEventoMod = lCtrl.ExInserisciProvvedimentoGenerico(lEveMod, lCamNot, lDepDecMod,
				lDepOrdMod, lTenMod);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEventoMod.getIdEvento().toString();

		return lPage;
	}

}