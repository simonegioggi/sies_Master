package siap.sius.fascicolo.util;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;

/**
 * <p>
 * Title: FascicoloUtils
 * </p>
 * <p>
 * Description: Classe di utilita' per il package FascicoloSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @author Vincenzo
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class FascicoloUtils {

	/**
	 * Funzione filtraContenutiMA().
	 *
	 * 20170320: aggiunto parametro di passaggio
	 *
	 * @param strCodTipoUfficio
	 * @return String [] Filtro da applicare alla classe Option. Descrizione: La funzione consente di filtrare
	 *         tra gli OggettoProcedimentoUDS (caratterizzati dal codice = "U%", solo quelli che hanno il
	 *         valore RV_HIGH_VALUE (= filtro di DecodificheModel) pari a "S22" escluso U037 ).
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Vincenzo
	 * @version 1.0
	 */
	public String[] filtraContenutiMA(String strCodTipoUfficio) {

		// generazione del filtro sui contenuti per le Misure alternative.
		Collection lOggetti = null;
		if ("UDSM".equals(strCodTipoUfficio))
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), "U004");
		else
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U004");
		String[] lFilter = new String[lOggetti.size() + 1];

		Iterator itx = lOggetti.iterator();
		int i = 0;
		while (itx.hasNext()) {
			DecodificheModel lDecodeOggetto = (DecodificheModel) itx.next();
			if (lDecodeOggetto.getFiltro() != null && lDecodeOggetto.getFiltro().compareTo("S22") == 0
					&& lDecodeOggetto.getCode().compareTo("U037") != 0) { // STUB 14/03/2005
				lFilter[i++] = new String(lDecodeOggetto.getCode());
			} else
				lFilter[i++] = new String("00");
		}
		lFilter[i++] = new String("00");

		// Viene restituito il filtro.
		return lFilter;
	}

	/**
	 * Funzione filtraContenutiMAconU037().
	 *
	 * @return String [] Filtro da applicare alla classe Option. Descrizione: La funzione consente di filtrare
	 *         tra gli OggettoProcedimentoUDS (caratterizzati dal codice = "U%", solo quelli che hanno il
	 *         valore RV_HIGH_VALUE (= filtro di DecodificheModel) pari a "S22", compreso U037 ).
	 *         <p>
	 *         Copyright: Copyright (c) 2003
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Vincenzo
	 * @version 1.0
	 *
	 */
	public String[] filtraContenutiMAconU037() {

		// generazione del filtro sui contenuti per le Misure alternative.
		Collection lOggetti = null;
		lOggetti = DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U004");
		String[] lFilter = new String[lOggetti.size() + 1];

		Iterator itx = lOggetti.iterator();
		int i = 0;
		while (itx.hasNext()) {
			DecodificheModel lDecodeOggetto = (DecodificheModel) itx.next();
			if (lDecodeOggetto.getFiltro() != null && lDecodeOggetto.getFiltro().compareTo("S22") == 0) {
				lFilter[i++] = new String(lDecodeOggetto.getCode());
			} else
				lFilter[i++] = new String("00");
		}
		lFilter[i++] = new String("00");

		// Viene restituito il filtro.
		return lFilter;
	}

	/**
	 * STUB 30/07/2007 Funzione filtraContenutiSS().
	 *
	 * @return String [] Filtro da applicare alla classe Option. Descrizione: La funzione consente di filtrare
	 *         tra gli OggettoProcedimentoUDS (caratterizzati dal codice = "U%", fatta eccezione per U019,
	 *         solo quelli che hanno il valore RV_HIGH_VALUE (= filtro di DecodificheModel) pari a "S12" ).
	 *         <p>
	 *         Copyright: Copyright (c) 2007
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Vincenzo
	 * @version 1.0
	 *
	 */
	public String[] filtraContenutiSS(String strCodTipoUfficio) {

		// generazione del filtro sui contenuti per le Misure alternative.
		Collection lOggetti = null;
		// MEV_66: aggiunto parametro di passaggio
		if ("UDSM".equals(strCodTipoUfficio))
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), "U019");
		else
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U019");
		String[] lFilter = new String[lOggetti.size() + 1];

		Iterator itx = lOggetti.iterator();
		int i = 0;
		while (itx.hasNext()) {
			DecodificheModel lDecodeOggetto = (DecodificheModel) itx.next();
			if (lDecodeOggetto.getFiltro() != null && lDecodeOggetto.getFiltro().compareTo("S12") == 0) {
				lFilter[i++] = new String(lDecodeOggetto.getCode());
			} else
				lFilter[i++] = new String("00");
		}
		lFilter[i++] = new String("00");

		// Viene restituito il filtro.
		return lFilter;
	}

	/**
	 * STUB 30/07/2007 Funzione filtraContenutiMS().
	 *
	 * @return String [] Filtro da applicare alla classe Option. Descrizione: La funzione consente di filtrare
	 *         tra gli OggettoProcedimentoUDS (caratterizzati dal codice = "U%", fatta eccezione per U024,
	 *         solo quelli che hanno il valore RV_HIGH_VALUE (= filtro di DecodificheModel) pari a "S09" ).
	 *         <p>
	 *         Copyright: Copyright (c) 2007
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @author Vincenzo
	 * @version 1.0
	 * @param strCodTipoUfficio
	 *
	 *            MEV63: aggiunto parametro di passaggio per distinzione ufficio minori
	 */
	public String[] filtraContenutiMS(String strCodTipoUfficio) {

		// generazione del filtro sui contenuti per le Misure sicurezza.
		Collection lOggetti = null;
		if ("UDSM".equals(strCodTipoUfficio))
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), "U024");
		else
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U024");
		String[] lFilter = new String[lOggetti.size() + 1];

		Iterator itx = lOggetti.iterator();
		int i = 0;
		while (itx.hasNext()) {
			DecodificheModel lDecodeOggetto = (DecodificheModel) itx.next();
			if (lDecodeOggetto.getFiltro() != null && lDecodeOggetto.getFiltro().compareTo("S09") == 0) {
				lFilter[i++] = new String(lDecodeOggetto.getCode());
			} else
				lFilter[i++] = new String("00");
		}
		lFilter[i++] = new String("00");

		// Viene restituito il filtro.
		return lFilter;
	}

	public boolean IsFascicoloSiusDefinito(String aCodStatoFascicolo) throws F3BException {

		boolean lRet = false;
		if (aCodStatoFascicolo.compareTo("01") == 0 || aCodStatoFascicolo.compareTo("07") == 0
				|| aCodStatoFascicolo.compareTo("05") == 0 || aCodStatoFascicolo.compareTo("99") == 0
				|| aCodStatoFascicolo.compareTo("24") == 0 // MEV_9 si aggiunge anche il 24
			)
			lRet = true;
		return lRet;
	}

}