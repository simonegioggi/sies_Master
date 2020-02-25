package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciPenaAccessoriaCumulo
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di PenaAccessoria Cumulo
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
public class ActInserisciPenaAccessoriaCumulo extends ActionModuloCumulo implements
		ICostantiPenaAccessoriaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Azione di Inserimento Manuale di Pena Accessoria in un titolo Cumulato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */

	// Pena Accessoria da inserire
	protected PenaAccessoriaModel mPenMod = null;

	// Codice Nuovo Tipo Pena Accessoria
	protected String mCodTipoPenaAccessoriaNuovo = "-";

	// Ulteriore Pena Accessoria da inserire (Pena Accessoria Sostitutiva).
	protected PenaAccessoriaModel mPenModNew = null;

	public String processRequest() throws Exception {

		// Lettura ID Fascicolo Siep da session
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		String lModo = getRequestStringParameter("modalita");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- ActInserisciPenaAccessoriaCumulo - Inizio - Modo = " + lModo);

		PenaAccessoriaCumuloModel PenaCumMod = null;

		if (lModo.compareTo("M") == 0) {
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA_CUMULO);
			IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
			PenaAccessoriaCumuloModel lPena = lCtrl.ExRicercaPenaAccessoriaCumuloByKey(lId);
			if (lPena != null && lPena.getIdPenaAccessoriaCumulo() != null)
				PenaCumMod = new PenaAccessoriaCumuloModel(lPena);
		} else {
			PenaCumMod = new PenaAccessoriaCumuloModel();
			PenaCumMod.setFlagDatiFinali("S"); // Per default le PA sono caricati in Dati Finali Cumulo
		}

		PenaCumMod.setCodTipoPenaAccessoria(getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));

		PenaCumMod.setDurata(getRequestStringParameter(CAMPO_DURATA));
		PenaCumMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		PenaCumMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		PenaCumMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		PenaCumMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		if (!isRequestParameterNullObj(CAMPO_DESCR_ALTRE_PA)
				&& !getRequestStringParameter(CAMPO_DESCR_ALTRE_PA).equals("")) {
			PenaCumMod.setDescrAltrePA(getRequestStringParameter(CAMPO_DESCR_ALTRE_PA));
		}

		PenaCumMod
				.setMotivoModifica(getRequestStringParameter(ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA));
		PenaCumMod.setTitIdTitoloCumulato(lIdTitolo);

		if (lModo.compareTo("I") == 0) {
			PenaCumMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			PenaCumMod.setDataInserimento(DateUtils.getSysDate());
			PenaCumMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			PenaCumMod.setFlagStato("I");
		} else if (lModo.compareTo("M") == 0) {
			PenaCumMod
					.setIdPenaAccessoriaCumulo(getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA_CUMULO));
			PenaCumMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			PenaCumMod.setDataAggiornamento(DateUtils.getSysDate());
			PenaCumMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

			if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
				PenaCumMod.setFlagStato("M");
		}

		// STUB 24/02/2006 Nuovi campi x Ordinanza del GE.
		/*
		 * PenaCumMod.setDataOrdinanzaGE( getRequestDateParameter( CAMPO_ANNO_DATA_ORDINANZA_GE,
		 * CAMPO_MESE_DATA_ORDINANZA_GE, CAMPO_GIORNO_DATA_ORDINANZA_GE) ); PenaCumMod.setAnnoOrdinanzaGE(
		 * getRequestBigDecimalParameter( CAMPO_ANNO_ORDINANZA_GE) ); PenaCumMod.setNumeroOrdinanzaGE(
		 * getRequestBigDecimalParameter( CAMPO_NUMERO_ORDINANZA_GE) ); String lCodTipoUfficio =
		 * getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_ORDINANZA_GE);
		 * PenaCumMod.setCodTipoUfficioOrdinanzaGE( lCodTipoUfficio ); String lDescrComune =
		 * getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_GE).toUpperCase(); if(lDescrComune !=
		 * null && !lDescrComune.equals("")) { String lCodComune = (getCodComuneByDescrFlagVal( lDescrComune
		 * )).getCodComune(); PenaCumMod.setCodLuogoUfficioOrdinanzaGE( lCodComune ); } else
		 * PenaCumMod.setCodLuogoUfficioOrdinanzaGE( "-" );
		 */

		IPenaAccessoriaCumulo lCtrlI = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
		PenaAccessoriaCumuloModel lPenRetMod = new PenaAccessoriaCumuloModel();
		if (lModo.compareTo("I") == 0)
			lPenRetMod = lCtrlI.ExInserisciPenaAccessoriaCumulo(PenaCumMod);
		else if (lModo.compareTo("M") == 0)
			lPenRetMod = lCtrlI.ExModificaPenaAccessoriaCumulo(PenaCumMod);

		// Prepara la pagina di destinazione.
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActDettaglioPenaAccessoriaCumulo&"
				+ CAMPO_ID_PENA_ACCESSORIA_CUMULO + "=" + lPenRetMod.getIdPenaAccessoriaCumulo().toString();
		// return paginaDestinazione("siap.siep.modulocumulo.action.ActDettaglioPenaAccessoriaCumulo");
		return lPage;

	} // CHIUDE ProcessRequest();

} // CHIUDE Classe ActInserisciPenaAccessoriaCumulo()