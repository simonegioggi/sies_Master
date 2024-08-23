package siap.sige.provvedimento.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * ActRicercaDepositoOrdinanza - Classe Action per la Ricerca delle Ordinanze Emesse per un Procedimento SIGE
 *
 * @version 1.0
 */
public class ActRicercaDepositoOrdinanza extends ActRicercaFSigePuntuale
		implements ICostantiProvvedimentoSige {

	public String processRequest() throws Exception {

		String lRetPage = null;

		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			super.processRequest();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Predisposizione ritorno
		setLinkRitorno();

		// Per uno stesso Procedimento SIGE ci possono essere + Ordinanze da depositare.
		// Si cercano tutte le Ordinanze validate del fascicolo.
		try {
			ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
			lProvSige.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			lProvSige.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA);
			IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel lProvSigeEvento = new ProvvedimentoSigeEventoModel();

			// Vector lVect = lCtrl.ExRicercaProvvedimentoDaDepositare( lProvSige );
			Vector<ProvvedimentoSigeEventoModel> lVect = lCtrl.ExRicercaOrdinanzeDaDepositare(lProvSige);

			if (lVect.size() > 1) {
				// Più ordinanze
				setRequestAttribute("ordinanze", lVect);
				lRetPage = PG_ELENCO_DEPOSITO_ORDINANZE;
				lProvSigeEvento = lVect.get(0);
			} else if (lVect.size() == 1) {
				// Unico provvedimento: se validato, si va al dettaglio.
				lProvSigeEvento = lVect.get(0);
				if (lProvSigeEvento.getProvvedimento().getChiaveProgr() != null) {
					RedirectTo lPage = new RedirectTo();
					lPage.setPage(IWebConstants.PG_MAIN);
					lPage.setAction("siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito");
					// &" + CAMPO_ID_DOCUMENTO_ALLEGATO + "=" + lDocAllMod.getIdDocumentoAllegato();
					lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO,
							lProvSigeEvento.getProvvedimento().getIdEventoGenerato().toString());
					this.setRequestAttribute("ordinanza", lVect.get(0));
					lRetPage = lPage.toString();
				} else {
					// Si passa al deposito dell' ordinanza
					RedirectTo lPage = new RedirectTo();
					lPage.setPage(IWebConstants.PG_MAIN);
					lPage.setAction("siap.sige.provvedimento.action.ActLoadInserisciDataDeposito");
					lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE,
							lProvSigeEvento.getProvvedimento().getIdProvvedimentoSige().toString());
					lPage.setParameter("IdEvento",
							lProvSigeEvento.getProvvedimento().getIdEventoGenerato().toString());
					this.setRequestAttribute("ordinanza", lVect.get(0));
					lRetPage = lPage.toString();
				}
			} else {
				// Nessun elemento trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessuna Ordinanza emessa per il procedimento ");
			}

			// Modificabilità, Stampabilità e Trasferibile
			// String lModificabile = "NO";
			// String lStampabile = "NO";
			// String lTrasferibile = "NO";
			String lModificabile = "SI";
			String lStampabile = "SI";
			String lTrasferibile = "SI";
			/*
			 * if( IsFascicoloSigeModificabile()) { // Stampabilità if(
			 * lProvSigeEventoDocAllMod.getFlagDocumentoRegistrato() == null ||
			 * lDocAllMod.getFlagDocumentoRegistrato().compareTo("N") == 0 ) { // Modificabilità e
			 * Stampabilità coincidono lStampabile = "SI"; lModificabile = "SI"; } else lTrasferibile = "SI";
			 * }
			 */

			setRequestAttribute("Modificabile", lModificabile);
			setRequestAttribute("Stampabile", lStampabile);
			setRequestAttribute("Trasferibile", lTrasferibile);
		} catch (F3BException fex) {
			if (fex.getErrorCode() == F3BException.USER_MESSAGE)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è stata trovata nessuna ordinanza validata !");
			else
				throw (fex);
		} catch (Exception ex) {
			throw (ex);
		}

		// restituisce la jsp di VIEW
		return lRetPage;
	}

}