package siap.sius.provvedimento.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositosentenza.action.ICostantiDepositoSentenza;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: RicercaProvvedimentiUtil.
 * </p>
 * <p>
 * Description: La classe raggruppa funzioni di utilità usati per la ricerca di Provvedimenti emessi per un
 * Procedimento di Sorveglianza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author Luigi
 * @version 1.0
 */
public class RicercaProvvedimentiUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// ID GENERALE PROCEDIMENTO
	BigDecimal mIdGenProc = null;

	// Tipi di decreti da escludere dalla ricerca perchè di tipo non definitorio.
	String[] mTipiDecretoDaEscludere = { ICostantiDepositoDecreto.CITAZIONE,
			ICostantiDepositoDecreto.IRREPERIBILITA,
			// MEV_9: aggiunta casistica per nuova tipologia
			ICostantiDepositoDecreto.DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA };
	// Tipi di Ordinanza da escludere. Nessuna perchè sono tutte di tipo declaratorio.
	// Il tipo di Ordinanza da escludere è RU ( Rinvio Udienza ) poichè non è di tipo definitorio.
	String[] mTipiOrdinanzaDaEscludere = { ICostantiDepositoOrdinanzaPc.RINVIO_UDIENZA,
			ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI,
			// MEV_9: aggiunta casistica per nuova tipologia (CONFERMA_DECISIONE_MAGISTRATO_RELATORE???)
			ICostantiDepositoOrdinanzaPc.MISURA_ALTERNATIVA_AMMISSIONE_DL_123_2018 };
	// Il tipo di Ordinanza da escludere opzionalmente è SO ( Sospenziome ) poichè non è di tipo definitorio
	String[] mTipiOrdinanzaDaEscludereSospesa = { ICostantiDepositoOrdinanzaPc.RINVIO_UDIENZA,
			// MEV_9: aggiunta casistica per nuova tipologia (CONFERMA_DECISIONE_MAGISTRATO_RELATORE???)
			ICostantiDepositoOrdinanzaPc.MISURA_ALTERNATIVA_AMMISSIONE_DL_123_2018 };
	// Il tipo di Sentenza da escludere è RU ( Rinvio Udienza ) poichè non è di tipo definitorio
	String[] mTipiSentenzaDaEscludere = { ICostantiDepositoSentenza.RINVIO_UDIENZA,
			ICostantiDepositoSentenza.RIMESSIONE_ATTI };
	// Il tipo di Sentenza da escludere opzionalmente è RU ( Rinvio udienza ) poichè non è di tipo
	// definitorio.
	String[] mTipiSentenzaDaEscludereSospesa = { ICostantiDepositoSentenza.RINVIO_UDIENZA };

	// Costruttore semplice
	public RicercaProvvedimentiUtil() {
	}

	// Costruttore con inizializzazione ID Generale Procedimento
	public RicercaProvvedimentiUtil(BigDecimal aIdGenProc) throws Exception {

		mIdGenProc = aIdGenProc;
	}

	/**
	 * La funzione controlla l'esistenza di un decreto, di un'ordinanza oppure di una Sentenza di tipo
	 * Declaratorio.
	 *
	 * @param aIdGenProc
	 * @return
	 * @throws Exception
	 */
	public boolean verificaEsistenzaProv() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaProv(): inizio");

		boolean retValue = false;

		// Controllo Decreti
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		if (lDepDecrCtrl.ExEsisteDepositoDecretoByGenProcEccettoTipi(mIdGenProc, mTipiDecretoDaEscludere)) {
			retValue = true;
		}

		// Controllo Ordinanze
		IDepositoOrdinanzaPc lDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		if (lDepOrdCtrl.ExEsisteDepositoOrdinanzaByGenProcEccettoTipi(mIdGenProc,
				mTipiOrdinanzaDaEscludere)) {
			retValue = true;
		}

		// Controllo Sentenza
		IDepositoSentenza lDepSenCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
		// MERGE v10: modificato parametro di passaggio
		if (lDepSenCtrl.ExEsisteDepositoSentenzaByGenProcEccettoTipi(mIdGenProc, mTipiSentenzaDaEscludere)) {
			retValue = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ritorno->" + retValue);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaProv(): fine");

		return retValue;
	}

	/**
	 * La funzione controlla l'esistenza di un'ordinanza di tipo Rimessione Atti.
	 *
	 * @param aIdGenProc
	 * @return
	 * @throws Exception
	 */
	public boolean verificaEsistenzaSospensione() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaSospensione(): inizio");

		boolean retValue = false;

		// Controllo Ordinanze
		IDepositoOrdinanzaPc lDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		if (!lDepOrdCtrl.ExEsisteDepositoOrdinanzaByGenProcEccettoTipi(mIdGenProc, mTipiOrdinanzaDaEscludere)
				&& lDepOrdCtrl.ExEsisteDepositoOrdinanzaByGenProcEccettoTipi(mIdGenProc,
						mTipiOrdinanzaDaEscludereSospesa))
			retValue = true;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ritorno->" + retValue);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaSospensione(): fine");

		return retValue;
	}

	/**
	 * La funzione controlla l'esistenza di un'ordinanza di tipo Rimessione Atti.
	 *
	 * @param aIdGenProc
	 * @return
	 * @throws Exception
	 */
	public boolean verificaEsistenzaSospensioneSentenza() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaSospensioneSentenza(): inizio");

		boolean retValue = false;

		// Controllo Sentenza
		IDepositoSentenza lDepSenCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
		if (!lDepSenCtrl.ExEsisteDepositoSentenzaByGenProcEccettoTipi(mIdGenProc, mTipiSentenzaDaEscludere)
				&& lDepSenCtrl.ExEsisteDepositoSentenzaByGenProcEccettoTipi(mIdGenProc,
						mTipiSentenzaDaEscludereSospesa)) {
			retValue = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ritorno->" + retValue);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".verificaEsistenzaSospensioneSentenza(): fine");

		return retValue;
	}

}