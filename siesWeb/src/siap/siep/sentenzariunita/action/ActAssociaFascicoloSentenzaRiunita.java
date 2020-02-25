package siap.siep.sentenzariunita.action;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAssociaFascicoloSentenzaRiunita
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della relazione tra Fascicolo e SentenzaRiunita
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAssociaFascicoloSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della relazione tra Fascicolo e SentenzaRiunita
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasc = new FascicoloSiepModel(
				(FascicoloSiepModel) getSessionAttribute("fascicolo"));
//		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

//		SentenzaRiunitaModel lSenRiu = new SentenzaRiunitaModel();
//		SentenzaRiunitaFascSiepModel lSenFascMod = new SentenzaRiunitaFascSiepModel();

		int numRighe = this.getRequestIntParameter("numsenriu");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("TOTALE SEN.RIUNITE= " + numRighe);
		// Id della Sentenza

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("VALORE DI FLAGCOLLEGATO=" + this.getParameterValues("flagcollegato"));

		String[] senriuniteSelezionate = null;
		HashSet sentenzeScelte = new HashSet();

		if (!isRequestParameterNullObj("flagcollegato")) {
			senriuniteSelezionate = this.getParameterValues("flagcollegato");
			for (int x = 0; x < senriuniteSelezionate.length; x++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("CONTENUTO DEI CHECK!!!" + senriuniteSelezionate[x].toString());
				sentenzeScelte.add(senriuniteSelezionate[x]);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("DOPO if e FOR dentro");

		String idSenRiu = "";
		Vector idDaAssociare = new Vector();
		Vector idDaDissociare = new Vector();
		for (int i = 0; i < numRighe; i++) {
			idSenRiu = this.getRequestStringParameter("chiaveSenRiu" + i);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("CONTENUTO DI idSenRiu=" + idSenRiu);
			if (sentenzeScelte.contains(String.valueOf(i))) {
				if (this.getRequestStringParameter("associato" + i).compareTo("checked") != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("RECORD NUOVO da ASSOCIARE");
					idDaAssociare.addElement(idSenRiu);
				}
			} else {
				if (this.getRequestStringParameter("associato" + i).compareTo("checked") == 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("RECORD VECCHIO da DE-ASSOCIARE");
					idSenRiu = this.getRequestStringParameter("chiaveSenRiuFasc" + i);
					idDaDissociare.addElement(idSenRiu);
				}
			}
		}

		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();

		Iterator itx = idDaAssociare.iterator();
//		BigDecimal idSenRiuFasc = null;
		SentenzaRiunitaFascSiepModel lSentenzaRiunitaFasSiep = new SentenzaRiunitaFascSiepModel();
		lSentenzaRiunitaFasSiep.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());

		while (itx.hasNext()) {
			idSenRiu = itx.next().toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("CONTENUTO DI idSenRiu per settare il model=" + idSenRiu);
			lSentenzaRiunitaFasSiep.setSenRiuIdSentenzaRiunita(new BigDecimal(idSenRiu));
			lCtrl.ExAggiornaSentenzaRiunitaFascicolo(lSentenzaRiunitaFasSiep, 1);
		}

		Iterator itx2 = idDaDissociare.iterator();
		while (itx2.hasNext()) {
			idSenRiu = itx2.next().toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("2 CONTENUTO DI idSenRiu per settare il model=" + idSenRiu);
			lSentenzaRiunitaFasSiep.setIdSentenzariunitaFascSiep(new BigDecimal(idSenRiu));
			lCtrl.ExAggiornaSentenzaRiunitaFascicolo(lSentenzaRiunitaFasSiep, 2);
		}
		// setRequestAttribute("sentenzariunita", lSenRet);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasc.getIdFascicoloSiep().toString();

		return lPage;
	}

}