package siap.sico.ufficio.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UfficioAccorpatoUtils {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UfficioAccorpatoModel getUfficioAccorpatoByCodAccorpanteProgr(String aCodUfficioAccorpante,
			BigDecimal aChiaveProgr) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aCodUfficioAccorpante = " + aCodUfficioAccorpante);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aChiaveProgr = " + aChiaveProgr);

		UfficioAccorpatoModel lAccorpato = null;

		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lListaAccorpati = lUffCtrl.ListaUfficiAccorpati("PM", aCodUfficioAccorpante);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaAccorpati.size() = " + lListaAccorpati.size());
		if (lListaAccorpati != null && lListaAccorpati.size() > 0) {
			// L'ufficio prevede accorpati di tipo PM

			// Riordino la lista per INCR_PROGRESSIVO asc
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinamento...");
			Vector lAccorpatiOrd = new Vector();
			Iterator<UfficioAccorpatoModel> lIterAccorpati = lListaAccorpati.iterator();
			while (lIterAccorpati.hasNext()) {
				UfficioAccorpatoModel lUffAccorpato = lIterAccorpati.next();
				BigDecimal lIncrement = new BigDecimal(lUffAccorpato.getIncrProgressivo());

				if (lAccorpatiOrd.size() == 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("add");
					lAccorpatiOrd.add(lUffAccorpato);
				} else {
					boolean inserito = false;
					for (int i = 0; i < lAccorpatiOrd.size(); i++) {
						BigDecimal lIncr = new BigDecimal(
								((UfficioAccorpatoModel) lAccorpatiOrd.elementAt(i)).getIncrProgressivo());
						if (lIncrement.compareTo(lIncr) == -1) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("add (i): " + i);
							lAccorpatiOrd.add(i, lUffAccorpato);
							inserito = true;
							break;
						}
					}

					if (!inserito) {
						// se non inserito lo aggiungo alla fine
						lAccorpatiOrd.add(lUffAccorpato);
					}
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAccorpatiOrd.size() = " + lAccorpatiOrd.size());

			// Determino il min increment
			BigDecimal lMinIncrement = new BigDecimal(
					((UfficioAccorpatoModel) lAccorpatiOrd.elementAt(0)).getIncrProgressivo());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMinIncrement = " + lMinIncrement);

			if (aChiaveProgr.compareTo(lMinIncrement) == 1) {
				// Il fascicolo è accorpato in quanto ha un progr > del min increment previsto per
				// l'ufficio. Devo stabilire

				// Inizializzo con il primo ufficio
				// lAccorpato = (UfficioAccorpatoModel) lAccorpatiOrd.elementAt(0);

				for (int i = 0; i < lAccorpatiOrd.size(); i++) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("i = " + i);

					UfficioAccorpatoModel lUffAccorpato = (UfficioAccorpatoModel) lAccorpatiOrd.elementAt(i);

					BigDecimal lIncrement = new BigDecimal(lUffAccorpato.getIncrProgressivo());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("aChiaveProgr = " + aChiaveProgr);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lIncrement = " + lIncrement);

					if (aChiaveProgr.compareTo(lIncrement) == -1) {
						// Chiave fascicolo < increment. Esco, l'accorpante era il precedente
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("aChiaveProgr.compareTo(lIncrement)==-1");
						break;
					} else {
						lAccorpato = lUffAccorpato;
					}
				}
			}
		} else {
			// L'ufficio non è accorpante
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAccorpato = " + lAccorpato);
		return lAccorpato;
	}

	public UfficioModel getUfficioAccorpatoByCodAccorpanteIncrement(String aCodUfficioAccorpante,
			String aIncremento) throws F3BException {
		UfficioModel lUfficio = new UfficioModel();

		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		lUfficio = lUff.getUfficioAccorpatoByAccorpanteIncrement(aCodUfficioAccorpante, aIncremento);

		return lUfficio;
	}

	/**
	 * 
	 * @param aUffAcc
	 * @param aProgrNew
	 * @return
	 */
	public BigDecimal getProgOrigineByUffAccProgr(UfficioAccorpatoModel aUffAcc, BigDecimal aProgrNew) {
		BigDecimal lProgrOrigine = null;

		lProgrOrigine = aProgrNew.subtract(new BigDecimal(aUffAcc.getIncrProgressivo()));

		return lProgrOrigine;
	}

}