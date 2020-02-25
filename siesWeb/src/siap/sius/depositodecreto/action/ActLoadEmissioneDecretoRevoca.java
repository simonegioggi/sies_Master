package siap.sius.depositodecreto.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActLoadEmissioneDecretoRevoca
 * </p>
 * <p>
 * Description: Classe Action per il caricamento della form di Inserimento di un Decreto di Revoca. La classe
 * costituisce una specializzazione della ActLoadEmissioneDecreto
 * </p>
 * La azione fa uso della classe padre per implementare la funzione di Emissione Decreto Generica, però oltre
 * a quella effettua un controllo sul tipo di decreto in maniera di consentire l'emissione solo della Revoca.
 *
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadEmissioneDecretoRevoca extends ActLoadEmissioneDecreto
		implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		String lRetPage = PG_LOAD_EMISSIONE_DECRETO; // pagina di view

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneDecretoRevoca: inizio");

		lRetPage = super.processRequest();

		// Se ritorna la pagina di warning, interrompe il flusso
		// per ritornare il messaggio di warning.
		if (lRetPage.equals(PG_WARNING))
			return lRetPage;

		if (mCodOggettoProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");

		String lCodTipoDec = generaTipoDecreto(mCodOggettoProc);

		if (lCodTipoDec == null || !lCodTipoDec.equalsIgnoreCase(REVOCA_DECRETO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il Procedimento selezionato ha un contenuto diverso da  - REVOCA PROVVEDIMENTO -");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneDecretoRevoca: fine");

		return lRetPage; // restituisce la jsp di VIEW
	}

	/**
	 * Funzione per la determinazione del tipo decreto associato al contenuto.
	 * 
	 * @param lCodContenuto
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String generaTipoDecreto(String lCodContenuto) {

		// Generazione automatica in base al contenuto
		Collection lOggetti = DecodificheManager.getInstance().getOggettoProcedimento();
		String lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Decreto = " + lCodTipoDec);
		return lCodTipoDec;
	}

}