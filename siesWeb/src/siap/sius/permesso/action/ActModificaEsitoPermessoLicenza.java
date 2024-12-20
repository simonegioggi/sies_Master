package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

/**
 * Title: ActModificaEsitoPermessoLicenza Description: Classe Action per la Modifica Esito Permesso
 *
 * @version 1.0
 */
public class ActModificaEsitoPermessoLicenza extends ActionSius
		implements ICostantiPermesso, ICostantiLicenzaLibanticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		ILicenzaPeriodiLibAnticipata lCtrlLicPerLibAnt = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

		// Recupero del record da modificare.
		LicenzaLibAnticipataModel lLic = lCtrlLicPerLibAnt.ExRicercaLicenzaLibanticipataByKey(
				getRequestBigDecimalParameter(CAMPO_ID_LICENZA_LIBANTICIPATA));

		// Imposta i parametri da modificare.
		lLic.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		lLic.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		lLic.setDataAggiornamento(DateUtils.getSysDate());
		lLic.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));
		lLic.setNumeroGiorniNoFruiti(getRequestBigDecimalParameter(CAMPO_NUMERO_GIORNI_NO_FRUITI));
		lLic.setNumeroOreNoFruite(getRequestBigDecimalParameter(CAMPO_NUMERO_ORE_NO_FRUITE));
		lLic.setDataAnnotazioneEsito(getRequestDateParameter(CAMPO_ANNO_DATA_ANNOTAZIONE_ESITO,
				CAMPO_MESE_DATA_ANNOTAZIONE_ESITO, CAMPO_GIORNO_DATA_ANNOTAZIONE_ESITO));

		// Si esegue la chiamata del metodo del controller preposto alla modifica.
		lCtrlLicPerLibAnt.ExModificaLicenzaLibanticipata(lLic);

		// Prepara la view di ritorno.
		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);

		// 20110524 - PM : Inclusa condizione LI (Licenza Internato) e PI (Permesso Internato)
		if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA)
				|| lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA_INTERNATO)
				// MEV_2023-35: aggiunti codici
				|| lLic.getCodTipoLicenza()
						.equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA_PENE_SOSTITUTIVE)
				|| lLic.getCodTipoLicenza()
						.equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_LICENZA)
				|| lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.REVOCA_LICENZA))
			lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzioneLicenza");
		else if (lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO)
				|| lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_INTERNATO)
				// MEV_2023-35: aggiunti codici
				|| lLic.getCodTipoLicenza().equalsIgnoreCase(ICostantiLicenzaLibanticipata.REVOCA_PERMESSO)
				|| lLic.getCodTipoLicenza()
						.equalsIgnoreCase(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_PERMESSO))
			lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEsecuzionePermesso");

		lRedir.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO,
				"" + super.getFascicoloSiusModelInSessione().getChiaveAnno());

		lRedir.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR,
				"" + super.getFascicoloSiusModelInSessione().getChiaveProgr());

		String lRetPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		// restituisce la jsp di VIEW
		return lRetPage;
	}

}