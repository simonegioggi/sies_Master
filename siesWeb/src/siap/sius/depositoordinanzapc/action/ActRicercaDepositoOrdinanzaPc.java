package siap.sius.depositoordinanzapc.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaDepositoOrdinanzaPc
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di DepositoOrdinanzaPc
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
public class ActRicercaDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel();
		lDepMod.setIdDepositoOrdinanzaPc(getRequestBigDecimalParameter(CAMPO_ID_DEPOSITO_ORDINANZA_PC));
		lDepMod.setAnnoS3(getRequestBigDecimalParameter(CAMPO_ANNO_S3));
		lDepMod.setNumS3(getRequestBigDecimalParameter(CAMPO_NUM_S3));
		lDepMod.setOggettoProcedimento(getRequestStringParameter(CAMPO_OGGETTO_PROCEDIMENTO));
		lDepMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA));
		lDepMod.setDataCameraConsiglio(getRequestDateParameter(CAMPO_ANNO_DATA_CAMERA_CONSIGLIO,
				CAMPO_MESE_DATA_CAMERA_CONSIGLIO, CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO));
		lDepMod.setDataDeposito(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO, CAMPO_MESE_DATA_DEPOSITO,
				CAMPO_GIORNO_DATA_DEPOSITO));
		lDepMod.setCodNaturaProvvedimento(getRequestStringParameter(CAMPO_COD_NATURA_PROVVEDIMENTO));
		lDepMod.setIdCssaComp(getRequestBigDecimalParameter(CAMPO_ID_CSSA_COMP));
		lDepMod.setCodUfficioMagistratoComp(getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP));
		lDepMod.setLuogoSvolgimentoProva(getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
		lDepMod.setServizioTerapeuticoComp(getRequestStringParameter(CAMPO_SERVIZIO_TERAPEUTICO_COMP));
		lDepMod.setNumGiorniDetenzioneDom(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_DETENZIONE_DOM));
		lDepMod.setNumMesiDetenzioneDom(getRequestBigDecimalParameter(CAMPO_NUM_MESI_DETENZIONE_DOM));
		lDepMod.setNumAnniDetenzioneDom(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_DETENZIONE_DOM));
		lDepMod.setNumGiorniPermessoAccordati(
				getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI));
		lDepMod.setNumGiorniRiduzionePena(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RIDUZIONE_PENA));
		lDepMod.setNumGiorniRiduzioneUsufruiti(
				getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RIDUZIONE_USUFRUITI));
		lDepMod.setCodUffTdsConcessoRiduzione(
				getRequestStringParameter(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE));
		lDepMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lDepMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lDepMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lDepMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lDepMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lDepMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lDepMod.setGenPridGeneraleProcedimento(
				getRequestBigDecimalParameter(CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO));

		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		Vector lVect = lCtrl.ExRicercaDepositoOrdinanzaPc(lDepMod);
		setRequestAttribute("depositoordinanzapc", lVect);

		return PG_RICERCADEPOSITOORDINANZAPC;
	}

}