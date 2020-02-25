package siap.siep.misurasicurezza.action;

/**
 * <p>Title: ActLoadIscrizioneProcApplicazioneMisuraProvvisoria	</p>
 * <p>Description: Classe Action per la load Iscrizione Procedimento	</p>
 * <p> di Applicazione Misura Sicurezza Provvisoria o</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Intersistemi Italia s.p.a.</p>
 * @version 8.2
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadIscrizioneProcApplicazioneMisuraProvvisoria extends ActionSiap implements
		ICostantiSoggetto {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare in tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.removeSessionAttribute("fascicolo");
		this.removeSessionAttribute("soggetto");
		this.removeSessionAttribute("penaresidua");

		// indica se inserisco istanza o sentenza+istanza o soggetto+sentenza+istanza
		String lTipoInserimento = "nuovo";
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))// precarica soggetto
		{
			setRequestAttribute("idsoggetto", ""
					+ getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
			lTipoInserimento = "soggetto";

			ISoggetto lCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel lSog = lCtrl
					.ExRicercaSoggettoByKey(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
			if (lSog != null) {
				setSessionAttribute("soggetto", lSog);
				setRequestAttribute("soggetto", lSog);
			} else {
				throw new SIEPException(SIEPException.USER_MESSAGE, "Soggetto non individuato");
			}
		} else// NON PRECARICA NULLA ->
		{
			lTipoInserimento = "nuovo";
		}

		setRequestAttribute("tipoinserimento", lTipoInserimento);

		{
			// ========================================================================
			// New d.f. 14/04/2015
			// Devo controllare se per l'anno corrente sono stati iscritti procedimenti
			// di classe IV. In caso negativo devo obbligare l'utente a indicare
			// manualmente il numero di procedimento che rappresenterà l'inizio della
			// numerazione automatica per i fascicoli telematici dell'anno corrente.
			// Infatti sono stati già iscritti sicuramente fascicoli cartacei che
			// andranno eventualmente caricati manualmente. La numerazione automatica
			// vale solo per i nuovi e non puù sovrapporsi a quella cartecea già assegnata
			// dall'ufficio
			//
			// Verificare se subordinare il controllo al 2015
			// ========================================================================
			BigDecimal lAnnoCorrente = new BigDecimal(DateUtils.getSysDate("yyyy"));

			if (lAnnoCorrente.intValue() == 2015) {
				// n.b. controllo solo per il 2015, anno di avvio delle Misure Sicurezza
				IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
				boolean lEsisteFascicoloClasseIVAnnoCorrente = lCtrlMS.ExEsistonoFascicoliClasseIVAnno(
						getUfficioUtenteConnesso(), lAnnoCorrente);

				// Se non esiste devo forzare la numerazione manuale
				if (!lEsisteFascicoloClasseIVAnnoCorrente) {
					setRequestAttribute("NumerazioneManuale", "S");
					setRequestAttribute("EsisteFascicoloClasseIVAnnoCorrente", "N");
					setRequestAttribute("AnnoCorrente", DateUtils.getSysDate("yyyy"));

				}
			}
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- ActLoadIscrizioneProcApplicazioneMisuraProvvisoria - NumerazioneManualeMisureFS = "+getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));
		setRequestAttribute("NumerazioneManualeMisureProvvFS",
				getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));

		// Riempie la combo delle nazioni
		Option lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
				.getNazioni(), "-"), "039");
		setRequestAttribute("nazioni", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(), "-");
		setRequestAttribute("StatoCittadinanza", "" + lOption);

		// Flag Data Nascita Presunta
		lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), "N");
		setRequestAttribute("dataNascitaPresunta", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
		setRequestAttribute("tipoRito1", "" + lOption);

		// Tipo Provvedimento Sorv : Decreto / Ordinanza
		Option lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv);

		// autorita: Uffici della Cognizione
		Option lAutoOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
		setRequestAttribute("autoritaEmi", "" + lAutoOption);

		// ComboBOX X Natura Misura Sicurezza
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// Restituisce la pagina di Inserimento dei Dati
		return ICostantiMisuraSicurezza.PG_LOAD_ISCR_PROC_APPLICAZIONE_MIS_SIC_PROVVISORIA;
	}

}