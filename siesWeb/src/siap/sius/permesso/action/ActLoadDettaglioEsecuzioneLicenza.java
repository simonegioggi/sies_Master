package siap.sius.permesso.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * ActLoadDettaglioEsecuzioneLicenza - Classe Action per la load Dettaglio Esecuzione Permesso
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioEsecuzioneLicenza extends ActRicercaFSPuntuale
		implements ICostantiDepositoDecreto, ICostantiPermesso, ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		String lRetPage = PG_DETTAGLIO_ESECUZIONE_PERMESSOLICENZA;

		BigDecimal lIDFasSius = null;
		// Se uno dei campi di ricerca sono null prende l'id del fascicolo dalla sessione.
		if (super.isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO)
				|| super.isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR))
			lIDFasSius = super.getFascicoloSiusModelInSessione().getIdFascicoloSius();
		else {
			// altrimenti prosegue con la ricerca del fascicolo attraverso le due chiavi ANNO/PROGR
			super.processRequest();
			lIDFasSius = super.getFascicoloSiusModelInSessione().getIdFascicoloSius();
		}

		setLinkRitorno();

		IPermesso lCtrlPerm = SIUSLookupRemote.getPermessoRemote();
		DepositoDecretoMotivazioniLicenzaModel lPermDepDecr = lCtrlPerm
				.ExRicercaLicenzaDepositata(lIDFasSius);

		if (lPermDepDecr == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Per il Procedimento indicato non risulta depositato un decreto "
							+ "di concessione licenza.\n" + "Operazione non consentita.");

		String lCodUfficioIns = lPermDepDecr.getLicenza().getCodUfficioInserimento();

		if (lCodUfficioIns.equals(super.getCodUfficioUtenteConnesso())) {
			super.setRequestAttribute("Modificabile", "SI");
			super.setRequestAttribute("Inseribile", "SI");
		} else {
			super.setRequestAttribute("Modificabile", "NO");
			super.setRequestAttribute("Inseribile", "NO");
		}

		Collection lColl = new ArrayList();
		IEventoPermessoLicenza lCtrlEvPerLic = SIUSLookupRemote.getEventoPermessoLicenzaRemote();
		lColl = lCtrlEvPerLic.ExRicercaEventoPermessoLicenzaByKeyLicLib(
				lPermDepDecr.getLicenza().getIdLicenzaLibanticipata());

		setRequestAttribute("permessoDepDecr", lPermDepDecr);
		setRequestAttribute("eventiPermessoLicenza", lColl);

		setRequestAttribute(ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA,
				lPermDepDecr.getLicenza().getIdLicenzaLibanticipata().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		// restituisce la jsp di VIEW
		return lRetPage;
	}

}