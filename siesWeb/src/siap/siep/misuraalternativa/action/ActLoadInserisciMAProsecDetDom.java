package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Nuova gestione della Prosecuzione della Misura a seguito delle modifiche introdotte del DL 146/2013.
 * 
 * @author d.fiorletta
 * @since 04/02/2014 DL 146/2013
 */

public class ActLoadInserisciMAProsecDetDom extends ActProsecuzione51Bis {
	/**
	 * Questa Action viene invocata due volte: - dalla grigia delle funzioni della misura - dopo l'inserimento
	 * del provvedimento della Sorveglianza
	 */
	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {
		String lRitorno = getProsecuzione();
		if (!lRitorno.equals(""))
			return lRitorno;

		// Collection <DecodificheModel> lOggettiDecisione = new Vector <DecodificheModel>();
		// lOggettiDecisione.add(new DecodificheModel("-","-","","","","","","",""));
		// lOggettiDecisione.addAll(DecodificheManager.getInstance().getMotivoProvvedimentoProsecMADetDomMDS51Bis());
		//
		// Option lOption = new Option(lOggettiDecisione);
		// setRequestAttribute("motivoProvv", "" + lOption);
		// ==========================================================================
		Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
				.getMotivoProvvedimentoProsecMADetDomMDS51Bis();
		Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
				.getMotivoProvvedimentoProsecMADetDom_TDS_51Bis();

		setRequestAttribute("oggettiTDS", lOggettoTDS);
		setRequestAttribute("oggettiMDS", lOggettoMDS51bis);
		// ==========================================================================

		// Combo con i motivi concessione delle MA AFF per la sezione in cui si
		// indicano gli estremi dell'ordinanza di concessione della misura da prorogare
		Collection<DecodificheModel> lOggettiDecisioneAT = new Vector<DecodificheModel>();
		lOggettiDecisioneAT.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lOggettiDecisioneAT.addAll(DecodificheManager.getInstance().getMotivoProvvedimentoMADDom());
		Option lOptionAT = new Option(lOggettiDecisioneAT);
		setRequestAttribute("comboTipoMisuraAT", "" + lOptionAT);

		setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_INSERISCI_MA_PROSECUZIONE_51_BIS;
	}

}