package siap.sius.prescrizione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciPrescrizione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Prescrizione
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
public class ActInserisciPrescrizione extends ActionSiap implements ICostantiPrescrizione {

	/**
	 * Azione di Inserimento del Prescrizione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String lRetPage = null;
		Vector lPrescrizioni = new Vector();

		PrescrizioneModel lPreMod = new PrescrizioneModel();
		lPreMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lPreMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lPreMod.setDataInserimento(DateUtils.getSysDate());
		// Le prescrizioni sono ora collegate all'evento Luigi 12-12-2003
		lPreMod.setEveIdEve(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		// lPreMod.setDepOpidDepositoOrdinanzaPc(getRequestBigDecimalParameter(CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC));

		lPreMod.setCodLuogoAffidamento("-");
		lPreMod.setIdCssaCompetente(new BigDecimal("9999"));
		lPreMod.setCodLuogoAutorizzato("-");
		lPreMod.setCodProvinciaAutorizzata("-");
		lPreMod.setCodTipoPrescrizione("-");
		lPreMod.setCodUffMagistratoCompetente("-");

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_01)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_01));
			lPreMod1.setCodLuogoAffidamento(getCodComuneByDescr(
					getRequestStringParameters(CAMPO_COD_LUOGO_AFFIDAMENTO)[0]).getCodComune());
			lPreMod1.setCodUffMagistratoCompetente(getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameters(CAMPO_COD_UFF_MAGISTRATO_COMPETENTE)[0]));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_02)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_02));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_03)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_03));
			lPreMod1.setCodLuogoAutorizzato(getCodComuneByDescr(
					getRequestStringParameter(CAMPO_COD_LUOGO_AUTORIZZATO)).getCodComune());
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_04)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_04));
			lPreMod1.setCodLuogoAffidamento(getCodComuneByDescr(
					getRequestStringParameters(CAMPO_COD_LUOGO_AFFIDAMENTO)[1]).getCodComune());
			lPreMod1.setCodUffMagistratoCompetente(getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameters(CAMPO_COD_UFF_MAGISTRATO_COMPETENTE)[1]));
			lPreMod1.setDescrComunitaTerapeutica(getRequestStringParameters(CAMPO_DESCR_COMUNITA_TERAPEUTICA)[0]);
			lPrescrizioni.add(lPreMod1);

		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_05)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_05));
			// STUB 20/10/2003 Passaggio da CodComuneCssaCompetente a IdCssaCompetente
			// lPreMod1.setCodComuneCssaCompetente(getCodComuneByDescr(getRequestStringParameters(CAMPO_COD_COMUNE_CSSA_COMPETENTE
			// )[0]).getCodComune());
			lPreMod1.setDescrComuneCssaCompetente(getRequestStringParameter(CAMPO_ID_CSSA_COMPETENTE));
			lPreMod1.setIdCssaCompetente(getIdCSSAByDescrComune(getRequestStringParameter(CAMPO_ID_CSSA_COMPETENTE)));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_06)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_06));
			// STUB 20/10/2003 Passaggio da CodComuneCssaCompetente a IdCssaCompetente
			// lPreMod1.setCodComuneCssaCompetente(getCodComuneByDescr(getRequestStringParameters(CAMPO_COD_COMUNE_CSSA_COMPETENTE)[1]).getCodComune());
			lPreMod1.setDescrComuneCssaCompetente(getRequestStringParameter(CAMPO_ID_CSSA_COMPETENTE));
			lPreMod1.setIdCssaCompetente(getIdCSSAByDescrComune(getRequestStringParameter(CAMPO_ID_CSSA_COMPETENTE)));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_07)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_07));
			lPreMod1.setDescrMansioneLavorativa(getRequestStringParameter(CAMPO_DESCR_MANSIONE_LAVORATIVA));
			lPreMod1.setDescrLuogoLavoro(getRequestStringParameter(CAMPO_DESCR_LUOGO_LAVORO));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_08)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_08));
			lPreMod1.setCodProvinciaAutorizzata(getCodComuneByDescr(
					getRequestStringParameter(CAMPO_COD_PROVINCIA_AUTORIZZATA)).getCodProvincia());
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_09)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_09));
			lPreMod1.setDescrComunitaTerapeutica(getRequestStringParameters(CAMPO_DESCR_COMUNITA_TERAPEUTICA)[1]);
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_10)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_10));
			lPreMod1.setOraUscitaAbitazione(getRequestStringParameter(CAMPO_ORA_USCITA_ABITAZIONE));
			lPreMod1.setOraRientroAbitazione(getRequestStringParameter(CAMPO_ORA_RIENTRO_ABITAZIONE));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_11)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_11));
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato
		if (isRequestChecked(CAMPO_CK_12)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_12));
			lPreMod1.setNumVolteControllo(getRequestBigDecimalParameter(CAMPO_NUM_VOLTE_CONTROLLO));
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_13)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_13));
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_14)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_14));
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_15)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_15));
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_16)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_16));
			lPrescrizioni.add(lPreMod1);
		}

		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_90)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_90));
			lPreMod1.setDescrAltraPrescrizione(getRequestStringParameters(CAMPO_DESCR_ALTRA_PRESCRIZIONE)[0]);
			lPrescrizioni.add(lPreMod1);
		}
		// Controlla se check è stato selezionato.
		if (isRequestChecked(CAMPO_CK_91)) {
			PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);
			lPreMod1.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_CK_91));
			lPreMod1.setDescrAltraPrescrizione(getRequestStringParameters(CAMPO_DESCR_ALTRA_PRESCRIZIONE)[1]);
			lPrescrizioni.add(lPreMod1);
		}

		IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		/*PrescrizioneModel lPreModRet = */lCtrl.ExInserisciPrescrizioni(
				(PrescrizioneModel[]) lPrescrizioni.toArray(new PrescrizioneModel[0]), lPreMod.getEveIdEve()); // setta
																												// la
																												// risposta
																												// nella
																												// request

		// Prepara la pagina di destinazione
		// STUB 22/10/2003 Aggiunto il parametro Id_Evento, da passare alla ActDettaglioEmissioneOrdinanza

		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.sius.depositoordinanzapc.action.ActDettaglioEmissioneOrdinanza&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+
		// lPreMod.getEveIdEve();

		// Preparazione della pagina di destinazione,

		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		// Viene ricavata l'azione successiva di default
		// è il dettaglio dell'Emissione Ordinanza.
		String lNextAction = null;
		if (!isRequestParameterNullObj("nextaction"))
			lNextAction = this.getRequestStringParameter("nextaction");

		if ((lNextAction != null) && (lNextAction.trim().length() > 1))
			lRedirectTo.setAction(lNextAction);
		else
			lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActDettaglioEmissioneOrdinanza&");

		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lPreMod.getEveIdEve().toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

}