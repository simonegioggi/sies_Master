package siap.siep.cumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciCumulo
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Cumulo
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
public class ActInserisciCumulo extends ActionSiap implements ICostantiCumulo {

	/**
	 * Azione di Inserimento dei dati dei titoli da cumulare. Inserisce i dati del CUMULO che contiene i
	 * riferimenti al titolo esecutivo da cumulare: Sentenza, Decreto, 'Cumulo' (Cumulo di Cumulo). Se il
	 * titolo è di altra BDI inserisce anche SENTENZA e FASCICOLO_SIEP (n.b. in classe 7 dal 11/01/2006) Nel
	 * caso di 'cumulo di cumulo' (13) il fascicolo siep non viene inserito, ma registrato solo il
	 * provvedimento di cumulo sulla tabella Sentenza con COD_TIPO_PROVVEDIMENTO = 13 (cumulo)
	 *
	 * @return PG_MESSAGE con esito inserimento
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasSessione = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
		SoggettoModel lSogMod = lFasSessione.getSoggetto();
		IUfficio lUffCtl = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = new UfficioModel();
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		SentenzaModel lSenMod = new SentenzaModel();
		FascicoloSiepModel lFASCUMULATO = null;
		CumuloModel lCumMod = new CumuloModel();

		// ==========================================================================
		// Inserimento titolo di altra BDI. Creo il FASCICOLO e la SENTENZA
		// ==========================================================================
		if (getRequestStringParameter("lFasCumulato").equals("")) {
			if (!getRequestStringParameter("Autorita").equals("-")
					&& !getRequestStringParameter("Luogo").equals("")) {
				lUffMod = lUffCtl.getUfficioByCodTipoUffDescrComune(getRequestStringParameter("Autorita"),
						getRequestStringParameter("Luogo").toUpperCase());
			}

			/*
			 * 30-05-05 -- DARIO --LUCIANA -- MODIFICA RICHIESTA DAL CLIENTE
			 * if(!getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("13")) {
			 * String lCodiceDistrettoDig = lUffMod.getCodDistretto();
			 * if(getCodDistrettoUtenteConnesso().compareTo(lCodiceDistrettoDig) == 0) { RedirectTo lRedirigi
			 * = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
			 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,
			 * "Il Distretto deve essere diverso dal Distretto di appartenenza " ); lRedirigi.setAction(
			 * "siap.siep.cumulo.action.ActLoadRichiestaFascicoliCumulo");
			 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi); return IWebConstants.PG_MESSAGE;
			 * } }
			 */

			// SENTENZA
			lSenMod.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO));
			if (!getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("13")) {
				lSenMod.setDataProvvedimento(
						getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO,
								ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO,
								ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO));
				lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_SENTENZA));
				// STUB 11/01/2006 Aggiunta di 700.000 al Numero Sentenza.
				// lSenMod.setNumeroSentenza(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA).toUpperCase());
				String NumeroSentenza = "700000"
						.substring(0,
								6 - getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA)
										.length())
						.concat(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA)
								.toUpperCase());
				lSenMod.setNumeroSentenza(NumeroSentenza);

			} else { // Se CUMULO (13)
				lSenMod.setDataProvvedimento(
						getRequestDateParameter("annodataprov", "mesedataprov", "giornodataprov"));
				lSenMod.setAnnoSentenza(getRequestBigDecimalParameter("annosentenza"));
				// STUB 11/01/2006 Aggiunta di 700.000 al Numero Sentenza.
				// lSenMod.setNumeroSentenza(getRequestStringParameter("numerosentenza").toUpperCase());
				String NumeroSentenza = "700000"
						.substring(0, 6 - getRequestStringParameter("numerosentenza").length())
						.concat(getRequestStringParameter("numerosentenza").toUpperCase());
				lSenMod.setNumeroSentenza(NumeroSentenza);
			}

			// STUB 11/01/2006 Aggiunta di 700.000 al Numero Reg.Gen.
			String NumeroRegistro = "700000".substring(0, 6 - getRequestStringParameter("NRG").length())
					.concat(getRequestStringParameter("NRG"));
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
				lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
				// lSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
				lSenMod.setNumeroRegeGip(NumeroRegistro);
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
				lSenMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
				// lSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
				lSenMod.setNumeroRegeDib(NumeroRegistro);
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
				lSenMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
				// lSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
				lSenMod.setNumeroRegeCas(NumeroRegistro);
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
				lSenMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
				// lSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
				lSenMod.setNumeroRegeCap(NumeroRegistro);
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
				lSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
				// lSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
				lSenMod.setNumeroRegeCasap(NumeroRegistro);
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gup")) {
				lSenMod.setAnnoRegeGup(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeGup(NumeroRegistro);
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("capsm")) {
				lSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCapsm(NumeroRegistro);
			}

			// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
			// lSenMod.setDataArrivoAtto(DateUtils.getSysDate());

			ComuneModel lComMod = new ComuneModel();
			if (!getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("13")) { // Sentenza/Decreto
				// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
				// lSenMod.setDataIrrevocabilita(getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA,
				// ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA,
				// ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA));
				lComMod = getCodComuneByDescr(
						getRequestStringParameter(ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE));
				lSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter("AutoEmi"));
			} else { // 13 - CUMULO
				// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
				// lSenMod.setDataIrrevocabilita(DateUtils.getSysDate());
				lComMod = getCodComuneByDescr(getRequestStringParameter("luogoemi"));
				lSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter("AutoEmi1"));
			}

			lSenMod.setCodLuogoEmittente(lComMod.getCodComune());
			lSenMod.setNumSezioneAutoritaEmittente(
					getRequestStringParameter(ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
			lSenMod.setCodBilanciamentoCircostanze("-");
			lSenMod.setCodTipoProvvRif("-");
			lSenMod.setCodTipoAutoritaProvvRif("-");
			lSenMod.setCodTipoDecisioneCassazione("-");
			lSenMod.setCodLuogoProvvRif("-");
			lSenMod.setCodTipoDecisioneCassazione("-");
			// Inserita comunque da questo ufficio
			lSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lSenMod.setDataInserimento(DateUtils.getSysDate());
			lSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			// campi nuovi
			lSenMod.setCodTipoProvvedimentoRif("-");
			lSenMod.setCodTipoProvvedimentoAltro("-");
			lSenMod.setCodSedeNotiziaReato("-");

			if (!this.isRequestParameterNullObj("Note")) {
				lSenMod.setNote(getRequestStringParameter("Note"));
			}

			// Dati del fascicolo solo se trattasi di Decreto o Sentenza. In questo
			// caso creo il fascicolo in classe 7
			if (!getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("13")) {
				// FASCICOLO
				lFasMod.setChiaveAnno(getRequestBigDecimalParameter("ASiep"));
				// STUB 11/01/2006 Aggiunta di 700.000 al Numero SIEP.
				// lFasMod.setChiaveProgr(getRequestBigDecimalParameter("NSiep"));
				lFasMod.setChiaveProgr(getRequestBigDecimalParameter("NSiep").add(new BigDecimal("700000")));

				lFasMod.setChiaveUfficio(lUffMod.getCodUfficio());

				if (!getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("13")) { // Sentenza/Decreto
					// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
					lFasMod.setDataIrrevocabilita(
							getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA,
									ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA,
									ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA));
				}

				// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
				// lFasMod.setDataIrrevocabilita(lSenMod.getDataIrrevocabilita());
				lFasMod.setFlagCumulato("N");
				lFasMod.setCodStatoFascicolo("03");
				lFasMod.setSoggetto(lSogMod);
				lFasMod.setCodTipoPosLibero("-");
				lFasMod.setCodMotivoArchiviazione("-");
				lFasMod.setSogIdSoggetto(lSogMod.getIdSoggetto());
				// Inserita comunque da questo ufficio
				lFasMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lFasMod.setDataInserimento(DateUtils.getSysDate());
				lFasMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

				lFASCUMULATO = new FascicoloSiepModel(lFasMod);
			}

		} else {
			// Inserimento Fascicolo stessa BDI
			IFascicoloSiep lFasCtl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFASCUMULATO = new FascicoloSiepModel(
					lFasCtl.ExRicercaFascicoloByKey(getRequestBigDecimalParameter("lFasCumulato")));
			lCumMod.setSenIdSentenza(lFASCUMULATO.getSenIdSentenza());
		}

		// NOT CUMULO
		if (lFASCUMULATO != null) {
			lCumMod.setChiaveAnnoFasCumulato(lFASCUMULATO.getChiaveAnno());
			lCumMod.setChiaveProgrFasCumulato(lFASCUMULATO.getChiaveProgr());
		}

		String LuogoUfficio = "";
		String CodTipoUfficio = "";
		if (isRequestParameterNullObj("Luogo")) {
			lUffMod = new UfficioModel();
			lUffMod = lUffCtl.getUfficioByKey(lFASCUMULATO.getChiaveUfficio());
			LuogoUfficio = lUffMod.getCodComune();
			CodTipoUfficio = lUffMod.getCodTipoUfficio();
		} else {
			LuogoUfficio = lUffMod.getCodComune();
			CodTipoUfficio = lUffMod.getCodTipoUfficio();
		}

		lCumMod.setCodLuogoUfficioFasCumulato(LuogoUfficio);
		lCumMod.setCodTipoUfficioFasCumulato(CodTipoUfficio);
		if (lFASCUMULATO != null && lFASCUMULATO.getIdFascicoloSiep() != null) { // Se Stessa BDI
			lCumMod.setIdFascicoloSiepCumulato(lFASCUMULATO.getIdFascicoloSiep());
		}
		lCumMod.setFasSieIdFascicoloSiep(lFasSessione.getIdFascicoloSiep());
		lCumMod.setDataInserimento(DateUtils.getSysDate());
		lCumMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lCumMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCumMod.setFlagTipoStampa("-");
		// lCumMod.getIstrIdIstruttoriaCumulo();
		// lCumMod.setIstrIdIstruttoriaCumulo(lIdIstruttoriaCorrente);

		ICumulo lCumCtl = SIEPLookupRemote.getCumuloRemote();
		// Ambrosino // passo come ultimo valore null
		// lCumCtl.ExInserisciCumulo(lCumMod, lFasMod, lSenMod, lCumMod.getIstrIdIstruttoriaCumulo());
		lCumCtl.ExInserisciCumulo(lCumMod, lFasMod, lSenMod, null);

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Inserimento Fascicolo Cumulato Avvenuto Correttamente!");
		lRedirigi.setAction("siap.siep.cumulo.action.ActLoadRichiestaFascicoliCumulo");
		// lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;

	}

}