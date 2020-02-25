package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di PenaAccessoria
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
public class ActInserisciPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {

	/**
	 * Azione di Inserimento del PenaAccessoria
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
		BigDecimal lIdFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		mPenMod = letturaDatiPenaAccesoria(lIdFascicoloSiep);

		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();

		// PenaAccessoriaModel lPenModRit = new PenaAccessoriaModel();
		mPenMod = lCtrl.ExInserisciPenaAccessoria(mPenMod);

		// Serve ???
		setRequestAttribute("penaaccessoria", mPenMod);

		// Eventuale inserimento della Pena Accessoria Sostitutiva.
		mPenModNew = letturaDatiPenaAccesoriaSostitutiva(lIdFascicoloSiep);
		if (mPenModNew != null)
			mPenModNew = lCtrl.ExInserisciPenaAccessoria(mPenModNew);

		// Prepara la pagina di destinazione.
		return paginaDestinazione("siap.siep.penaaccessoria.action.ActLoadDettaglioPenaAccessoria");

	}

	/**
	 * La funzione valorizza la Pena Accessoria da inserire.
	 * 
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws Exception
	 */
	protected PenaAccessoriaModel letturaDatiPenaAccesoria(BigDecimal aIdFascicoloSiep) throws Exception {

		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();

		lPenMod.setCodTipoPenaAccessoria(getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));

		// Codice Nuovo Tipo Pena Accessoria
		mCodTipoPenaAccessoriaNuovo = "-";

		if (isRequestParameterNullObj(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO))
			mCodTipoPenaAccessoriaNuovo = "-";
		else
			mCodTipoPenaAccessoriaNuovo = getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO)
					.toUpperCase();

		lPenMod.setCodNuovoTipoPenaAccessoria(mCodTipoPenaAccessoriaNuovo);
		lPenMod.setDurata(getRequestStringParameter(CAMPO_DURATA));
		lPenMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lPenMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lPenMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));

		// 05/04/2006 Aggiunta campi per richiesta al giudice dell'esecuzione.
		lPenMod.setDescrAltrePA(getRequestStringParameter(CAMPO_DESCR_ALTRE_PA));
		lPenMod.setCodFonteGE(getRequestStringParameter("CodFonte"));
		lPenMod.setAnnoFonteGE(getRequestStringParameter("AnnoFonte"));
		lPenMod.setNumeroFonteGE(getRequestStringParameter("NumeroFonte"));
		lPenMod.setArticoloGE(getRequestStringParameter("Articolo"));
		lPenMod.setCodSottonumerazioneGE(getRequestStringParameter("CodSottonumerazione"));
		lPenMod.setCommaGE(getRequestStringParameter("Comma"));
		lPenMod.setLetteraGE(getRequestStringParameter("Lettera"));
		lPenMod.setNumeroGE(getRequestStringParameter("Numero"));

		lPenMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINEVALIDITA,
				CAMPO_MESE_DATA_FINEVALIDITA, CAMPO_GIORNO_DATA_FINEVALIDITA));
		if (getRequestStringParameter(CAMPO_ANNO_DATA_FINEVALIDITA).length() < 4
				&& mCodTipoPenaAccessoriaNuovo.compareTo("-") != 0)
			lPenMod.setDataFineValidita(DateUtils.getSysDate());

		// STUB 24/02/2006 Nuovi campi x Ordinanza del GE.
		lPenMod.setDataOrdinanzaGE(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_GE,
				CAMPO_MESE_DATA_ORDINANZA_GE, CAMPO_GIORNO_DATA_ORDINANZA_GE));
		lPenMod.setAnnoOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_GE));
		lPenMod.setNumeroOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_GE));
		String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ORDINANZA_GE);
		lPenMod.setCodTipoUfficioOrdinanzaGE(lCodTipoUfficio);
		String lDescrComune = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_GE).toUpperCase();
		if (lDescrComune != null && !lDescrComune.equals("")) {
			String lCodComune = (getCodComuneByDescrFlagVal(lDescrComune)).getCodComune();
			lPenMod.setCodLuogoUfficioOrdinanzaGE(lCodComune);
		} else
			lPenMod.setCodLuogoUfficioOrdinanzaGE("-");

		// STUB 23/03/2006 Nuovi campi x Ordinanza Condono/Revoca/Sostituzione.
		lPenMod.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
				CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
		lPenMod.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
		lPenMod.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
		String lCodTipoUfficioPA = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA);
		lPenMod.setCodTipoUfficioOrdinanzaPA(lCodTipoUfficioPA);
		String lDescrComunePA = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA).toUpperCase();
		if (lDescrComunePA != null && !lDescrComunePA.equals("")) {
			String lCodComunePA = (getCodComuneByDescrFlagVal(lDescrComunePA)).getCodComune();
			lPenMod.setCodLuogoUfficioOrdinanzaPA(lCodComunePA);
		} else {
			lPenMod.setCodLuogoUfficioOrdinanzaPA("-");
		}

		lPenMod.setFlagCondonata(getRequestStringParameter(CAMPO_FLAG_CONDONATA));

		if (isRequestChecked(CAMPO_FLAG_REVOCA_CONDONO))
			lPenMod.setFlagRevocaCondono("S");
		else
			lPenMod.setFlagRevocaCondono("N");

		lPenMod.setDataSentenzaRevoca(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA_REVOCA,
				CAMPO_MESE_DATA_SENTENZA_REVOCA, CAMPO_GIORNO_DATA_SENTENZA_REVOCA));
		lPenMod.setAnnoSentenzaRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_REVOCA));
		lPenMod.setNumeroSentenzaRevoca(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_REVOCA));

		lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO);
		lPenMod.setCodTipoUfficioSentenzaRevo(lCodTipoUfficio);
		lDescrComune = getRequestStringParameter(CAMPO_COD_LUOGO_SENTENZA_REVOCA).toUpperCase();
		if (lDescrComune != null && !lDescrComune.equals("")) {
			String lCodComune = (getCodComuneByDescrFlagVal(lDescrComune)).getCodComune();
			lPenMod.setCodLuogoSentenzaRevoca(lCodComune);
		} else
			lPenMod.setCodLuogoSentenzaRevoca("-");

		lPenMod.setAnnoRegePmRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM_REVOCA));
		lPenMod.setNumeroRegePmRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_PM_REVOCA));
		lPenMod.setAnnoRegeGipRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_GIP_REVOCA));
		lPenMod.setNumeroRegeGipRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_GIP_REVOCA));
		lPenMod.setAnnoRegeDibRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_DIB_REVOCA));
		lPenMod.setNumeroRegeDibRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_DIB_REVOCA));
		lPenMod.setAnnoRegeCasRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CAS_REVOCA));
		lPenMod.setNumeroRegeCasRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_CAS_REVOCA));
		lPenMod.setAnnoRegeCapRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CAP_REVOCA));
		lPenMod.setNumeroRegeCapRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_CAP_REVOCA));
		lPenMod.setAnnoRegeCasapRevoca(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CASAP_REVOCA));
		lPenMod.setNumeroRegeCasapRevoca(getRequestStringParameter(CAMPO_NUMERO_REGE_CASAP_REVOCA));

		if (isRequestChecked(CAMPO_FLAG_DICHIARAZIONE_FALSITA))
			lPenMod.setFlagDichiarazioneFalsita("S");
		else
			lPenMod.setFlagDichiarazioneFalsita("N");

		lPenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		lPenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lPenMod.setDataInserimento(DateUtils.getSysDate());
		lPenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lPenMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

		return lPenMod;
	}

	/**
	 * La funzione valorizza l'eventuale Pena Accessoria Sostitutiva leggendo i dati nella form di input.
	 * 
	 * @param aIdFascicoloSiep
	 * @return PenaAccessoriaModel
	 * @throws Exception
	 */
	protected PenaAccessoriaModel letturaDatiPenaAccesoriaSostitutiva(BigDecimal aIdFascicoloSiep)
			throws Exception {

		PenaAccessoriaModel lPenModNew = null;

		// Eventuale inserimento della Pena Accessoria Sostitutiva.
		if (mCodTipoPenaAccessoriaNuovo.compareTo("-") != 0) {
			lPenModNew = new PenaAccessoriaModel();
			lPenModNew.setIdPenaAccessoriaOrigine(mPenMod.getIdPenaAccessoria());
			lPenModNew.setCodTipoPenaAccessoria(mCodTipoPenaAccessoriaNuovo.trim());
			lPenModNew.setDataOrdinanzaGE(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
					CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
			lPenModNew.setAnnoOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
			lPenModNew.setNumeroOrdinanzaGE(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
			lPenModNew.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenModNew.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenModNew.setDataInserimento(DateUtils.getSysDate());
			lPenModNew.setFasSieIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep());
			lPenModNew.setFlagRevocaCondono("N");
			lPenModNew.setFlagDichiarazioneFalsita("N");
			lPenModNew.setFlagCondonata("-");

			// Se Sostituzione e Condono, si aggiungono i campi per richiesta al giudice dell'esecuzione.
			if (!isRequestParameterNullObj(CAMPO_FLAG_CONDONATA)
					&& getRequestStringParameter(CAMPO_FLAG_CONDONATA).compareTo("T") == 0) {
				lPenModNew.setDescrAltrePA(getRequestStringParameter(CAMPO_DESCR_ALTRE_PA));
				lPenModNew.setCodFonteGE(getRequestStringParameter("CodFonte"));
				lPenModNew.setAnnoFonteGE(getRequestStringParameter("AnnoFonte"));
				lPenModNew.setNumeroFonteGE(getRequestStringParameter("NumeroFonte"));
				lPenModNew.setArticoloGE(getRequestStringParameter("Articolo"));
				lPenModNew.setCodSottonumerazioneGE(getRequestStringParameter("CodSottonumerazione"));
				lPenModNew.setCommaGE(getRequestStringParameter("Comma"));
				lPenModNew.setLetteraGE(getRequestStringParameter("Lettera"));
				lPenModNew.setNumeroGE(getRequestStringParameter("Numero"));
			}
			String lCodTipoUfficioGE = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA);
			lPenModNew.setCodTipoUfficioOrdinanzaGE(lCodTipoUfficioGE);
			String lDescrComuneGE = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA)
					.toUpperCase();
			if (lDescrComuneGE != null && !lDescrComuneGE.equals("")) {
				String lCodComuneGE = (getCodComuneByDescrFlagVal(lDescrComuneGE)).getCodComune();
				lPenModNew.setCodLuogoUfficioOrdinanzaGE(lCodComuneGE);
			} else
				lPenModNew.setCodLuogoUfficioOrdinanzaGE("-");

			// In caso di sostituzione e Condono occorre valorizzare anche i dati dell'ordinanza PA.
			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_COMUNICAZIONE)
					&& getRequestStringParameter(CAMPO_COD_TIPO_COMUNICAZIONE).compareTo("03") == 0) {
				lPenModNew.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
						CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
				lPenModNew.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
				lPenModNew.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
				lPenModNew.setDataOrdinanzaPA(getRequestDateParameter(CAMPO_ANNO_DATA_ORDINANZA_PA,
						CAMPO_MESE_DATA_ORDINANZA_PA, CAMPO_GIORNO_DATA_ORDINANZA_PA));
				lPenModNew.setAnnoOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_ANNO_ORDINANZA_PA));
				lPenModNew.setNumeroOrdinanzaPA(getRequestBigDecimalParameter(CAMPO_NUMERO_ORDINANZA_PA));
				lPenModNew.setCodTipoUfficioOrdinanzaPA(mPenMod.getCodTipoUfficioOrdinanzaPA());
				lPenModNew.setCodLuogoUfficioOrdinanzaPA(mPenMod.getCodLuogoUfficioOrdinanzaPA());
				lPenModNew.setFlagCondonata("C");
			}
		}
		return lPenModNew;
	}

	protected String paginaDestinazione(String aNomeAction) {

		// Prepara la pagina di destinazione.
		if (mCodTipoPenaAccessoriaNuovo.compareTo("-") != 0) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Inserita Pena Accessoria Sostitutiva : Completare le modifiche necessarie.");
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(aNomeAction);
			lRedirigi.setParameter(CAMPO_ID_PENA_ACCESSORIA, "" + mPenMod.getIdPenaAccessoria());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		} else {
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction(aNomeAction);
			lPage.setParameter(CAMPO_ID_PENA_ACCESSORIA, "" + mPenMod.getIdPenaAccessoria());
			lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

			return lPage.toString();
		}
	}

}