package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.action.ICostantiTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActCancellaTitolo extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO);
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		// Controllo di sicurezza
		IstruttoriaCumuloModel lIstruttoriaModel = getDatiIstruttoria();
		if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"L'istruttoria non risulta in stato Aperta, impossibile rimuovere un Titolo dall'istruttoria");
		}

		// ==========================================================================
		// ==========================================================================
		ITitoloCumulato lCtrlTitoloCum = SIEPLookupRemote.getTitoloCumulatoRemote();
		String lTipo = "";
		BigDecimal lIdMess = null;

		// prima della cancellazione controllo il TIPO_ISCRIZIONE e se esiste salvo l'Id del Messaggio di
		// Trasferimento Atti per comp.
		TitoloCumulatoModel lTitolo = lCtrlTitoloCum.ExRicercaTitoloCumulatoById(lIdTitolo);
		if (lTitolo.getTipoIscrizione() != null)
			lTipo = lTitolo.getTipoIscrizione();

		if (lTitolo.getMessIdMessaggio() != null)
			lIdMess = lTitolo.getMessIdMessaggio();

		try {
			lCtrlTitoloCum.ExCancellaTitoloDaIstruttoriaById(lIdTitolo, lTipo);
		} catch (F3BException e) {
			siesLogger.debug("e.toString() = " + e.toString());

			// String lCod = "";
			// String lTab = "";
			// String lindex = "";
			//
			// int lORA = e.toString().indexOf("ORA-");
			// lCod = e.toString().substring(lORA).substring(0,9); // estraggo il codice Errore (ES.
			// "ORA-nnnnn")
			//
			// int lRICH = e.toString().indexOf("RICHPM");
			// lTab = e.toString().substring(lRICH); // estraggo da RICHPM in poi
			//
			// int lParent = lTab.indexOf(")");
			// lindex = lTab.substring(0, lParent); // isolo il Nome della tabella di Relazione ( ES.
			// RICHPM_nomeTab_CUM_FK)

			int lFKViolation = e.toString().indexOf("ORA-02292");
			String lMessagErr = "";
			if (lFKViolation >= 0) {
				lMessagErr = "Impossibile eliminare il titolo dall'istruttoria. Verificare che il titolo non sia coinvolto in richieste del PM. Eliminare la Richiesta prima di cancellare il Titolo.";
				// lMessagErr="ERRORE: Integrità violata in Tab. "+lindex+". Eliminare la Richiesta prima di
				// cancellare il Titolo.";
			} else {
				lMessagErr = e.toString();
			}

			throw new F3BException(F3BException.USER_MESSAGE, lMessagErr);

		}

		// ==========================================================================
		// Viene restituita la pagina con l'elenco dei Titoli in Istruttoria
		// ==========================================================================
		String lPage = "";
		if (lTipo.equals("01")) // Titolo iscritto da Presa in Carico
		{
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessa = lCrtl.ExRicercaMessaggioByKey(lIdMess);

			setRequestAttribute(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO,
					"" + lIdIstruttoriaCumulo);
			setRequestAttribute("IdMess", "" + lIdMess);
			setRequestAttribute("messaggiodiarrivoatti", lMessa);
			setRequestAttribute(IWebConstants.GOTO_PAGE,
					"siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti");

			lPage = ICostantiIstruttoriaCumulo.PG_MESSAGGIO_CONFERMA_RESTITUZIONE;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti";
			lPage += "&" + CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + lIdIstruttoriaCumulo.toString();
		}

		return lPage;
	}

}