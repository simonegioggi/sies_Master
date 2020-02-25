package siap.siep.nuovaistanza.action;

/**
* <p>Title: ActRicercaNuovaIstanza</p>
* <p>Description: Classe Action per la ricerca di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile s.r.l.</p>
* @version 5.0
*/

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String tipoRicerca = "";
		BigDecimal annoRegistroIstanza = null;
		BigDecimal progRegistroIstanza = null;
		BigDecimal annoRegistroIstanzaIniziale = null;
		BigDecimal progRegistroIstanzaIniziale = null;
		BigDecimal annoRegistroIstanzaFinale = new BigDecimal(9999);
		BigDecimal progRegistroIstanzaFinale = new BigDecimal(REGISTRO_ISTANZA_MAX);

		NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();
		FascicoloSiepModel lFascSiep = new FascicoloSiepModel();
		SoggettoModel soggIstanzaModel = new SoggettoModel();

		// lNuoMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		lNuoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		if ((!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO))
				&& ((getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO)) != null)
				&& (!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO))
				&& ((getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO)) != null)) {
			annoRegistroIstanza = getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO);
			progRegistroIstanza = getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO);
			if ((progRegistroIstanza.intValue() < REGISTRO_ISTANZA_MIN)
					|| (progRegistroIstanza.intValue() > REGISTRO_ISTANZA_MAX)) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Numero registro istanza non corretto!");
				return IWebConstants.PG_MESSAGE;
			}

			tipoRicerca = ICostantiNuovaIstanza.TIPO_RICERCA_SINGOLA_ISTANZA;
		}
		if (tipoRicerca.length() < 1) {
			if ((!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_INIZIALE))
					&& (getRequestBigDecimalParameter(
							ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_INIZIALE) != null)
					&& (!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE))
					&& (getRequestBigDecimalParameter(
							ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE) != null)) {
				annoRegistroIstanzaIniziale = getRequestBigDecimalParameter(
						ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_INIZIALE);
				progRegistroIstanzaIniziale = getRequestBigDecimalParameter(
						ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE);
				if ((progRegistroIstanzaIniziale.intValue() < REGISTRO_ISTANZA_MIN)
						|| (progRegistroIstanzaIniziale.intValue() > REGISTRO_ISTANZA_MAX)) {
					this.setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Numero registro istanza iniziale non corretto!");
					return IWebConstants.PG_MESSAGE;
				}
				if ((!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_FINALE))
						&& (getRequestBigDecimalParameter(
								ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_FINALE) != null)
						&& (!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_FINALE))
						&& (getRequestBigDecimalParameter(
								ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_FINALE) != null)) {
					annoRegistroIstanzaFinale = getRequestBigDecimalParameter(
							ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_FINALE);
					progRegistroIstanzaFinale = getRequestBigDecimalParameter(
							ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_FINALE);
					if ((progRegistroIstanzaFinale.intValue() < REGISTRO_ISTANZA_MIN)
							|| (progRegistroIstanzaFinale.intValue() > REGISTRO_ISTANZA_MAX)) {
						this.setRequestAttribute(IWebConstants.MESSAGE_TEXT,
								"Numero registro istanza finale non corretto!");
						return IWebConstants.PG_MESSAGE;
					}
				}
				tipoRicerca = ICostantiNuovaIstanza.TIPO_RICERCA_INTERVALLO_ISTANZE;
			}
		}
		if (tipoRicerca.length() < 1) {
			if ((!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME))
					&& (getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).length() > 0)) {
				// SoggettoModel soggIstanzaModel = new SoggettoModel();
				soggIstanzaModel.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
				if ((!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME))
						&& (getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).length() > 0))
					soggIstanzaModel.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
				if ((!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA))
						&& (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA))
						&& (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA))) {
					soggIstanzaModel
							.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
									ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA,
									ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));
				}
				if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)) {
					// soggIstanzaModel.setCodComuneNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA));
					ComuneModel comuneMod = new ComuneModel();
					comuneMod.setDescrizione(
							(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA))
									.toUpperCase());
					IComune lCtrlCom = SICOLookupRemote.getComuneRemote();
					soggIstanzaModel.setCodComuneNascita(
							(lCtrlCom.ExGetCodiceComuneValidita(comuneMod)).getCodComune());
				}
				tipoRicerca = ICostantiNuovaIstanza.TIPO_RICERCA_SOGGETTO_ISTANZA;
			}
		}
		if (!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_ATTIVITA_ISTANZA)
				&& (getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_ATTIVITA_ISTANZA)
						.compareTo("-") != 0)) {
			if (tipoRicerca.length() < 1)
				tipoRicerca = ICostantiNuovaIstanza.TIPO_RICERCA_ATTIVITA_ISTANZA;
			lNuoMod.setCodStatoIstanza(
					getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_ATTIVITA_ISTANZA));
		}
		if ((!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_TIPOLOGIA_ISTANZA))
				&& (getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_TIPOLOGIA_ISTANZA)
						.compareTo("-") != 0)) {
			lNuoMod.setFlagPresdep(getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_TIPOLOGIA_ISTANZA));
		}

		if (tipoRicerca.length() < 1) {
			this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserire un filtro di ricerca!");
			return IWebConstants.PG_MESSAGE;
		}

		IFascicoloSiep lCtrlFs = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascSiep.setChiaveUfficio(getCodUfficioUtenteConnesso());

		if (tipoRicerca.compareTo(TIPO_RICERCA_SINGOLA_ISTANZA) == 0) {
			// Ricavare l'id del fascicolo SIEP
			lFascSiep.setChiaveAnno(annoRegistroIstanza);
			lFascSiep.setChiaveProgr(progRegistroIstanza);

			lFascSiep = lCtrlFs.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascSiep);

			if (lFascSiep == null || lFascSiep.getIdFascicoloSiep() == null) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna Istanza presente");
				return IWebConstants.PG_MESSAGE;
			}

			lNuoMod.setFasSieIdFascicoloSiep(lFascSiep.getIdFascicoloSiep());
			lNuoMod.setCodStatoIstanza(null); // Per la ricerca puntuale
			lNuoMod.setFlagPresdep(null); // non vanno considerati altri filtri
			// setSessionAttribute("fascicolo", lFascSiep);
		}

		if (tipoRicerca.compareTo(TIPO_RICERCA_ATTIVITA_ISTANZA) == 0) {
			// Non fare nulla
		}

		String lReturnPage = null;

		String lPagina = "1";
		String strCountRisultati;

		// ============================================================================
		// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
		// ============================================================================
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca paginata
		// =========================================================

		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		Vector lVect = new Vector();

		if ((tipoRicerca.compareTo(TIPO_RICERCA_SINGOLA_ISTANZA) == 0)
				|| (tipoRicerca.compareTo(TIPO_RICERCA_ATTIVITA_ISTANZA) == 0)) {
			lVect = lCtrl.ExRicercaNuovaIstanzaPaged(lNuoMod, Integer.parseInt(lPagina));
		}

		if (tipoRicerca.compareTo(TIPO_RICERCA_INTERVALLO_ISTANZE) == 0) {
			// Ricavare gli id dei fascicoli SIEP
			lVect = lCtrl.ExRicercaNuoveIstanzeByAnnoProgrPaged(lNuoMod,
					annoRegistroIstanzaIniziale.intValue(), progRegistroIstanzaIniziale.intValue(),
					annoRegistroIstanzaFinale.intValue(), progRegistroIstanzaFinale.intValue(),
					Integer.parseInt(lPagina));
		}

		if (tipoRicerca.compareTo(TIPO_RICERCA_SOGGETTO_ISTANZA) == 0) {
			// Ricavare gli id dei fascicoli SIEP
			lVect = lCtrl.ExRicercaNuoveIstanzeBySoggettoPaged(lNuoMod, soggIstanzaModel,
					Integer.parseInt(lPagina));
		}

		if (lVect == null || lVect.size() == 0) {
			this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna Istanza presente");
			return IWebConstants.PG_MESSAGE;
		}

		// ======================================================================
		// Recupero il numero di record totali della ricerca utilizzato per
		// calcolare il numero totale di pagine necessarie a visualizzare i dati
		// ======================================================================
		if (isRequestParameterNullObj("CountRisultati")) {
			if (tipoRicerca.compareTo(TIPO_RICERCA_INTERVALLO_ISTANZE) == 0)
				strCountRisultati = lCtrl.ExGetCountNuovaIstanzaByAnnoProgr(lNuoMod,
						annoRegistroIstanzaIniziale.intValue(), progRegistroIstanzaIniziale.intValue(),
						annoRegistroIstanzaFinale.intValue(), progRegistroIstanzaFinale.intValue())
						.toString();
			else if (tipoRicerca.compareTo(TIPO_RICERCA_SOGGETTO_ISTANZA) == 0)
				strCountRisultati = lCtrl.ExGetCountNuovaIstanzaBySoggetto(lNuoMod, soggIstanzaModel)
						.toString();
			else
				strCountRisultati = lCtrl.ExGetCountNuovaIstanza(lNuoMod).toString();

		} else
			strCountRisultati = getRequestStringParameter("CountRisultati");

		BigDecimal CountRisultati = new BigDecimal(strCountRisultati);

		if (Integer.parseInt(strCountRisultati) == 1) {
			lNuoMod = new NuovaIstanzaModel((NuovaIstanzaModel) lVect.firstElement());
			lFascSiep = lCtrlFs.ExRicercaFascicoloByKey(lNuoMod.getFasSieIdFascicoloSiep());

			// 14/03/2011 Lettura dell'Avvocato.
			if (lNuoMod.getAvvIdAvvocato() != null) {
				AvvocatoModel lAvvMod = new AvvocatoModel();
				lAvvMod.setIdAvvocato(lNuoMod.getAvvIdAvvocato());

				IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
				AvvocatoModel lAvv = lCtrlAvv.ExRicercaAvvocatoByKey(lNuoMod.getAvvIdAvvocato());
				lNuoMod.setAvvocato(lAvv);
			}

			setRequestAttribute("nuovaistanza", lNuoMod);
			setSessionAttribute("fascicolo", lFascSiep);
			setFunctionsAvailableToRequest("siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza");
			lReturnPage = PG_LOAD_DETTAGLIONUOVAISTANZA;
		} else {

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("nuovaistanza", lVect);

			// Nel caso di più istanze devo passare anche il vettore dei relativi fascicoli siep
			Vector lVectFasSiep = new Vector();
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				FascicoloSiepModel lFasSiepNuoIsta = new FascicoloSiepModel();

				NuovaIstanzaModel lNuoIsta = (NuovaIstanzaModel) itx.next();
				lFasSiepNuoIsta = lCtrlFs.ExRicercaFascicoloByKey(lNuoIsta.getFasSieIdFascicoloSiep());
				lVectFasSiep.addElement(lFasSiepNuoIsta);
			}
			setRequestAttribute("fascicolosiep", lVectFasSiep);

			lReturnPage = PG_RICERCANUOVAISTANZA;
		}

		return lReturnPage;

	}
}