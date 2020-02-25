package siap.sico.residenza.action;

/**
* <p>Title: ActLoadModificaResidenza</p>
* <p>Description: Classe Action per la load modifica di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

@SuppressWarnings("rawtypes")
public class ActLoadModificaResidenza extends ActionSiap implements ICostantiResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("<------------ ActLoadModificaResidenza ------------>");
		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Residenza", lId.toString(),
				getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La  " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// riempie il model
		ResidenzaModel lResMod = new ResidenzaModel();
		lResMod.setIdResidenza(lId);

		// chiama il controller
		ResidenzaController lCtrl = new ResidenzaController();

		Vector lVect = lCtrl.ExRicercaResidenza(lResMod);
		if (lVect.size() != 0)
			lResMod = (ResidenzaModel) lVect.get(0);

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), lResMod.getCodStato());
		setRequestAttribute("nazioni", "" + lOption);

		setRequestAttribute("residenza", lResMod);
		setRequestAttribute("modalita", "M");

		// paolo cherubini aggiunta righe elenco residenza 6 agosto 2009
		ResidenzaModel lResModNew = new ResidenzaModel();
		lResModNew.setSogIdSoggetto(lResMod.getSogIdSoggetto());
		lResModNew.setCodTipoResidenza(lResMod.getCodTipoResidenza());
		lVect = lCtrl.ExRicercaResidenza(lResModNew);
		setRequestAttribute("residenze", lVect);

		if (lResMod.getCodTipoResidenza().equals("R"))
			return PG_LOAD_INSERISCIRESIDENZA;
		else
			return PG_LOAD_INSERISCIDOMICILIO;
	}

}