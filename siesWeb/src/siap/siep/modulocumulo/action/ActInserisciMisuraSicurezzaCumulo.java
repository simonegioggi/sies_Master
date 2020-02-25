package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per l'inserimento di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciMisuraSicurezzaCumulo extends ActionModuloCumulo
		implements ICostantiMisuraSicurezzaCumulo {

	/*****************************************************************************
	 * Azione di Inserimento del MisuraSicurezzaCumulo
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		MisuraSicurezzaCumuloModel lMisMod = new MisuraSicurezzaCumuloModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// ==========================================================================
		lMisMod.setCodNatura(getRequestStringParameter(CAMPO_COD_NATURA));
		lMisMod.setCodTipo(getRequestStringParameter(CAMPO_COD_TIPO));
		lMisMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lMisMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lMisMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		lMisMod.setMotivoModifica(
				getRequestStringParameter(ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA));

		if (!isRequestParameterNullObj(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO)
				&& getRequestStringParameter(
						ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO) != null) {
			lMisMod.setAnnoFascicoloSiepIV(getRequestBigDecimalParameter(
					ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO));
			lMisMod.setNumeroFascicoloSiepIV(getRequestBigDecimalParameter(
					ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO));
		}

		if (!isRequestParameterNullObj(ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO)
				&& !getRequestStringParameter(
						ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO).equals("-")) {
			String lCodiceuf = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(
							ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO),
					getRequestStringParameter(
							ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO));

			ComuneModel lCom = getCodComuneByDescr(getRequestStringParameter(
					ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO));
			lMisMod.setCodLuogoEmittenteIV(lCom.getCodComune());
			lMisMod.setCodAutoritaEmittenteIV(lCodiceuf);
		}

		lMisMod.setFlagStatoMisura(getRequestStringParameter(CAMPO_COD_STATO_MISURA));
		lMisMod.setFlagStato("I"); // Inserita manualmente
		lMisMod.setFlagDatiFinali("S"); // di default tutte le Misure fanno parte del provv. di
										// 'Dati_Finali_Cumulo'

		lMisMod.setTitIdTitoloCumulato(lIdTitolo);
		lMisMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		lMisMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMisMod.setDataInserimento(DateUtils.getSysDate());
		lMisMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		MisuraSicurezzaCumuloModel lMisRetMod = new MisuraSicurezzaCumuloModel();
		lMisRetMod = lCtrl.ExInserisciMisuraSicurezzaCumulo(lMisMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioMisuraSicurezzaCumulo";
		lPage += "&" + CAMPO_ID_MISURA_SICUREZZA_CUMULO + "="
				+ lMisRetMod.getIdMisuraSicurezzaCumulo().toString();

		return lPage;
	}

}