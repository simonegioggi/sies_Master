package siap.sige.provvedimento.action;

import java.util.Vector;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaDepositoDecreto
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca dei Decreti Emessi per un Procedimento SIGE.
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaDepositoDecreto extends ActRicercaFSigePuntuale implements ICostantiProvvedimentoSige,
		ICostantiProvvedimento {

	public String processRequest() throws Exception {

		String lRetPage = null;

		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			super.processRequest();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Predisposizione ritorno
		setLinkRitorno();

		// Per uno stesso Procedimento SIGE ci possono essere + Decreti da depositare.
		// Si cercano tutte i Decreti validati del fascicolo.
		try {
			ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
			lProvSige.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			lProvSige.setCodTipoProvvedimento(ICostantiProvvedimento.COD_DECRETO);
			IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel lProvSigeEvento = new ProvvedimentoSigeEventoModel();

			// Vector lVect = lCtrl.ExRicercaProvvedimentoDaDepositare( lProvSige );
			Vector<ProvvedimentoSigeEventoModel> lVect = lCtrl.ExRicercaDecretiDaDepositare(lProvSige);

			if (lVect.size() > 1) {
				// Più provvedimenti
				setRequestAttribute("decreti", lVect);
				lRetPage = PG_ELENCO_DEPOSITO_DECRETI;
				lProvSigeEvento = (ProvvedimentoSigeEventoModel) lVect.get(0);
			} else if (lVect.size() == 1) {
				// Unico provvedimento: se validato, si va al dettaglio.
				lProvSigeEvento = (ProvvedimentoSigeEventoModel) lVect.get(0);
				if (lProvSigeEvento.getProvvedimento().getChiaveProgr() != null) {
					RedirectTo lPage = new RedirectTo();
					lPage.setPage(IWebConstants.PG_MAIN);
					lPage.setAction("siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito");
					// &" + CAMPO_ID_DOCUMENTO_ALLEGATO + "=" + lDocAllMod.getIdDocumentoAllegato();
					lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO, lProvSigeEvento
							.getProvvedimento().getIdEventoGenerato().toString());
					this.setRequestAttribute("decreto", (ProvvedimentoSigeEventoModel) lVect.get(0));
					lRetPage = lPage.toString();

				} else {
					// Si passa al deposito del decreto
					RedirectTo lPage = new RedirectTo();
					lPage.setPage(IWebConstants.PG_MAIN);
					lPage.setAction("siap.sige.provvedimento.action.ActLoadInserisciDataDeposito");
					lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE,
							lProvSigeEvento.getProvvedimento().getIdProvvedimentoSige().toString());
					lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ELENCO_DECRETI, "true");
					this.setRequestAttribute("decreto", (ProvvedimentoSigeEventoModel) lVect.get(0));
					lRetPage = lPage.toString();
				}
			} else {
				// Nessun elemento trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Decreto emesso per il procedimento ");
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
						"Non è stato trovato nessun Decreto validato !");
			else
				throw (fex);
		} catch (Exception ex) {
			throw (ex);
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

//	private boolean isDecretoFissazioneUdienza() {
//		return false;
//	}

}