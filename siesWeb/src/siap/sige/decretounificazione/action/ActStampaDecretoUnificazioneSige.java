package siap.sige.decretounificazione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.SIGEException;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActStampaDecretoUnificazioneSige
 * </p>
 * <p>
 * Description: Classe Azione per richiesta stampa del Decreto di Unificazione di 2 Procedimenti SIGE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActStampaDecretoUnificazioneSige extends ActionSiap implements ICostantiDecretoUnificazioneSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		// String lCodiceOperatore = getCodUtenteConnesso();
		// String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		// String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		// String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();
		// UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave dell'evento come parametro
		BigDecimal lKeyProvvedimento = super.getRequestBigDecimalParameter(
				ICostantiDecretoUnificazioneSige.CAMPO_ID_DECRETO_UNIFICAZIONE);

		// Preleva Il ProvvedimentoSigeEventoModel da cui il ProvvedimentoSigeModel
		IProvvedimentoSige lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvEveMod = lProvvCtrl.ExRicercaProvvedimentoById(lKeyProvvedimento);
		ProvvedimentoSigeModel lProvv = lProvvEveMod.getProvvedimento();

		// Preleva l'evento generato
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(lProvv.getIdEventoGenerato());

		// Preleva Il FascicoloSigeEstesoModel
		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeModel lFasMod = lFasCtrl.ExRicercaFascicoloSigeByKey(lProvv.getFasIdFascicoloSige());

		// Crea il ByteArrayOutputStream
		// String lCodTemplate = "";
		// if (!isRequestParameterNullObj(ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE))
		// lCodTemplate = getRequestStringParameter(ICostantiImpugnazioneSige.CAMPO_COD_TEMPLATE);

		IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
		// ByteArrayOutputStream lReport =
		// lCtrl.ExStampaDecretoUnificazioneSige(lProvv.getFasIdFascicoloSige(), lKeyProvvedimento,
		// lCodTemplate, lUfficio.getCodUfficio(), super.getUtenteConnesso() );
		ByteArrayOutputStream lReport = lCtrl.ExStampaDecretoUnificazioneSige(lEveMod,
				lProvv.getFasIdFascicoloSige(), lFasMod.getFasSigIdFascicoloSige(),
				getCodUfficioUtenteConnesso(), super.getUtenteConnesso());

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}