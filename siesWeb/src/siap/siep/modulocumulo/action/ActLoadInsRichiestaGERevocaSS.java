package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load Inserimento della Richieste del PM al GE di revoca Sanzione Sostitutiva Cumulo
 *
 * @author IntersistemiItalia S.p.A.
 *
 */
public class ActLoadInsRichiestaGERevocaSS extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		// Recupera dati legati all'ISTRUTTORIA_CUMULO
		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();

		String lPage = "";
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		// RichiestePmInCumuloModel lRichiestaModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			this.getElencoTitoliReato(lIstrCumulo);
			lPage = PG_INS_RICH_GE_REV_SAN_SOST;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal aIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);

			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			RichiestePmInCumuloModel lRicMod = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);

			setRequestAttribute("RichiestaAlGE", lRicMod);

			// Ricerca dei Titoli e Sanzioni Sostitutive collegati alla Richiesta (tramite tabella di
			// Relazione RICHPM_TITOLO_CUM)
			Vector<TitoloCumulatoModel> lVecTitoli = new Vector<>();
			lVecTitoli = lCtrlRich.ExRicercaTitoli_e_SSCumByRichiestaGE(aIdRich);

			// siesLogger.debug("--XX-- i Titoli collegati alla Richiesta Revoca Sanzione Sost sono:
			// "+lVecTitoli.size());
			setRequestAttribute("VectorTitoli", lVecTitoli);

			lPage = PG_INS_RICH_GE_REV_SAN_SOST;
		} else {
			// Rilanciare Eccezione - Operazione non supportata
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
		}

		setRequestAttribute("modalita", lModalita);

		return lPage;

	} // Chiude processRequest()

	/**
	 * Recupera l'elenco dei Titoli con relativi reati da caricare nella form in hidden
	 * 
	 * @param aIstruttoriaModel
	 * @throws F3BException
	 */
	private void getElencoTitoliReato(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {
		// ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		Vector<TitoloCumulatoModel> lVecTitoli = null;

		// Lista degli ID_Titoli Selezionati dall'Utente
		String[] lIdTitoliSelezionati = null;

		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		/*
		 * Vector<String> lCodTipiSS = new Vector<String>(); lCodTipiSS.add("S"); // Semidetenzione
		 * lCodTipiSS.add("L"); // Liberta' Controllata lCodTipiSS.add("P"); // Pena Pecuniaria
		 * lCodTipiSS.add("E"); // Espulsione
		 */
		// La Query trova Titolo_Cumulato_Model in join con Sanzione_Sost_Cumulo e il Procedimento_Cumulato
		// Aggregato
		lVecTitoli = new Vector<>(
				lIstrCtrl.ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy(
						aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lIdTitoliSelezionati));

		siesLogger.debug("--XX-- Size Lista = " + lVecTitoli.size());

		setRequestAttribute("VectorTitoli", lVecTitoli);

	} // Chiude getElencoTitoliReato()

} // Chiude Classe
