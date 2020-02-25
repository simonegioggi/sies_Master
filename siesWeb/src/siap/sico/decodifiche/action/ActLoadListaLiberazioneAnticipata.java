package siap.sico.decodifiche.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * Action per il caricamento della popup con l'elenco di: - LA concesse (flaglicenza non passato) (decisioni
 * della sorveglianza - Liberazione Anticipata) - Scomputi permesso (flaglicenza = Scomputo) (decisioni della
 * sorveglianza - Scomputo Permessi) - Ridimensionamento LA (flaglicenza = RidimensionamentoLA)
 * (Rideterminazione Pena - Ridimensionamento LA) - LA Revocate (new 07/2014) (flaglicenza = RevocaLA)
 * (decisioni della sorveglianza - Revoca Liberazione Anticipata)
 * 
 * Viene richiamata quindi dalla funzione di concessione LA
 * 
 * @author
 */
@SuppressWarnings("rawtypes")
public class ActLoadListaLiberazioneAnticipata extends ActionSiap implements ICostantiDecodifiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// String strFormname = getRequestStringParameter("formname");
		BigDecimal a_fieldname = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		String strFlagLicenza = "";
		if (!this.isRequestParameterNullObj("flaglicenza"))
			strFlagLicenza = getRequestStringParameter("flaglicenza");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("strFlagLicenza = " + strFlagLicenza);

		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel lModelLib = new LicenzaLibAnticipataModel();

		List lLicenze = null;

		if (strFlagLicenza.equals("Scomputo")) {
			// Decisioni della sorveglianza - Scomputo Permessi
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Chiamo ExRicercaLicenzaScomputiConcessiDepositatiByIdFascicoloSIEP per SCOMPUTI ");
			lLicenze = lCtrlLib.ExRicercaLicenzaScomputiConcessiDepositatiByIdFascicoloSIEP(a_fieldname,
					null);
			setRequestAttribute("licenze", lLicenze);
		} else if (strFlagLicenza.equals("RidimensionamentoLA")) {
			// Ridetermnazione Pena - Ridimensionamento LA
			Vector lPeriodi = null;
			lPeriodi = lCtrlLib.ExRicercaLAPeriodiConcessiDepositatiByIdFascSIEP(a_fieldname, null);
			setRequestAttribute("licenze", lPeriodi);
		} else if (strFlagLicenza.equals("RevocaLA")) {
			// Decisioni della sorveglianza - Revoca Liberazione Anticipata
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Chiamo ExRicercaLicenzaLibanticipataRevocateDepositateByIdFascicoloSIEP per LICENZE");
			Vector lPeriodi = null;
			lPeriodi = lCtrlLib.ExRicercaLAPeriodiRevocatiDepositatiByIdFascSIEP(a_fieldname, null);
			setRequestAttribute("licenze", lPeriodi);
			// FIXME REVOCA LA prevedere nuovo metodo
			// Vector <LicenzaPeriodiLibAnticipataModel>
		} else {
			// Decisioni della sorveglianza - Liberazione Anticipata (concessione)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Chiamo ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP per LICENZE");
			lLicenze = lCtrlLib.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(a_fieldname,
					null);
			setRequestAttribute("licenze", lLicenze);
		}

		// Return pagina di visualizzazione
		if (strFlagLicenza.equals("Scomputo"))
			return ROOT_DIR + "files/siap/sico/decodifiche/ListaPermessi.jsp";
		else if (strFlagLicenza.equals("RidimensionamentoLA"))
			return ROOT_DIR + "files/siap/sico/decodifiche/ListaLARidim.jsp";
		else if (strFlagLicenza.equals("RevocaLA"))
			return ROOT_DIR + "files/siap/sico/decodifiche/ListaLARevoca.jsp";
		else
			return PG_LISTA_LIBERAZIONE_ANTICIPATA;
	}

}