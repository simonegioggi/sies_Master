package siap.sige.tenore.action;

/**
* <p>Title: ActLoadDettaglioOggettiProv</p>
* <p>Description: Classe Action per la load dettaglio di TenoreSige</p>
* La classe  apre la finestra per la Gestione degli Oggetti Sige.
* Non viene effettuata la ricerca degli oggetti perchè quelli visualizzati
* dalla finestra saranno quelli presenti in session.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.datiprovsige.controller.IDatiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

@SuppressWarnings("rawtypes")
public class ActLoadDettaglioOggettoProv extends ActionSige implements ICostantiTenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	Collection mDati = null;

	public String processRequest() throws Exception {
		Option lEsiti = null;
		TenoreSigeModel lTenore = null;
		String lPage = PG_INSERIMENTO_TENORE_ESITO_SIGE;
		String lTipoEsito = "unico";

		if (this.isRequestParameterNullObj(CAMPO_ID_TENORE_SIGE))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Manca ID");

		setLinkRitorno();

		// Ricerca Tenori
		ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		BigDecimal idTenore = getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE);
		Vector<TenoreSigeEstesoModel> lTenori = lCtrl.ExRicercaTenoreEstesoById(idTenore);
		// 20190521: [EC] gestione esiti distinti per reato/sentenza e per oggetto
		if (!isRequestParameterNullEmptyObj("isTitoliEsecutivi")
				&& !super.getRequestStringParameter("isTitoliEsecutivi").equalsIgnoreCase("false")) {
			BigDecimal idProvvedimento = super.getRequestBigDecimalParameter("idProvvedimento");
			setRequestAttribute("isTitoliEsecutivi", "true");
			setRequestAttribute("idProvvedimento", idProvvedimento.toString());
			lTenori = lCtrl.ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento(
					super.getRequestStringParameter("codOggettoSige"), idProvvedimento);
		}

		// Ricerca esiti per lo specifico oggetto
		if (lTenori != null && lTenori.size() > 0) {
			// Estrazione del Codice Oggetto
			TenoreSigeEstesoModel lTenoeEsteso = this.getSelTenore(lTenori, idTenore);
			lTenore = lTenoeEsteso.getTenoreSige();

			if (lTenore.getCodEsitoSige() != null && lTenore.getCodEsitoSige().length() > 1) {
				// Dettaglio
				lPage = PG_DETTAGLIO_TENORE_ESITO_SIGE;

				// Tipo di esito : Unico o differenziato
				// 15/03/2011 Gestione Esito tampone "0000" per Esito diversificato per SENTENZA_REATO (Stesso
				// TENORE_SIGE)
				if (lTenore.getCodEsitoSige().length() > 1
						&& lTenore.getCodEsitoSige().trim().compareTo("0000") != 0)
					lTipoEsito = "unico";
				else
					lTipoEsito = "differenziato";

				setRequestAttribute("titolo", "Dettaglio Oggetto");

				// Ricerca Dati Provvedimento
				IDatiProvvedimentoSige lDatiProvCtrl = SIGELookupRemote.getDatiProvvedimentoSigeRemote();
				Vector lDatiProv = lDatiProvCtrl.ExRicercaDatiProvvedimentoSigeByIdTenore(
						getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));
				setRequestAttribute("dati_prov", lDatiProv);

				try {
					// Ricerca eventuale Annotazione Manuale
					BigDecimal idSentenza = super.getRequestBigDecimalParameter("idSenSentenza");
					ricercaAnnotazioneManuale(idSentenza, lTenore.getIdTenoreSige());
				} catch (F3BException e) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Errore nella ricerca della Annotazione Manuale");
				}
			} else {
				// Si richiama il lock
				lockApplicativo("Modifica_Oggetti");

				// Inserimento
				lEsiti = getEsito(lTenoeEsteso.getTenoreSige().getCodOggettoSige());
				setRequestAttribute("esiti", lEsiti);
				setRequestAttribute("titolo", "Definizione Oggetto");
				setRequestAttribute("dati", mDati);
			}
			setRequestAttribute("tenori", lTenori);

			setRequestAttribute("tipo_esito", lTipoEsito);

		}
		setRequestAttribute("id_tenore", getRequestStringParameter(CAMPO_ID_TENORE_SIGE));

		String isTitoliEsecutivi = "false";
		if (!super.isRequestParameterNullObj("isTitoliEsecutivi"))
			isTitoliEsecutivi = super.getRequestStringParameter("isTitoliEsecutivi");

		super.setRequestAttribute("isTitoliEsecutivi", isTitoliEsecutivi);

		// Passa alla request il parametro che esprime la possibilità di inserire/modificare/cancellare
		// Oggetti
		setModificabileOggettiAtto();

		return lPage;
	}

	// Preleva gli esiti dalla CG_REF_CODES e costruisce la Option per la combo
	// e ricava i Dati Provvedimento Sige per i check.
	protected Option getEsito(String codiceOggetti) throws Exception {
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lColl = lDecodifiche.ExRicercaEsitiByOggettoSige(codiceOggetti);
		Option lOption = new Option(lColl, false);

		// Ricerca Dati Provvedimento SIGE
		mDati = lDecodifiche.ExRicercaDatiProvvSigeByOggetto(codiceOggetti);

		// Se si tratta di una richiesta di INDULTO inserisco qui la ricerca delle richieste al GE
		if (isIndultoAmnistia(codiceOggetti)) {

			String lCodTipoBeneficio;

			// Flag Indulto viene usato per passare il tipo Beneficio
			// Indulto : 002 ; Amnistia : 003
			if (codiceOggetti.equalsIgnoreCase(APPLICAZIONE_INDULTO)
					|| codiceOggetti.equalsIgnoreCase(APPLICAZIONE_INDULTO_CONDIZIONATO))
				lCodTipoBeneficio = "002";
			else
				lCodTipoBeneficio = "003";

			setRequestAttribute(FLAG_INDULTO, lCodTipoBeneficio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FLAG_INDULTO -> " + lCodTipoBeneficio);

			// Se esiste il Fascicolo SIEP di riferimento si ricercano le Richieste al GE relative ad esso.
			if (getFascicoloSigeEstesoInSessione().getFascicoloSiep() != null)
				ricercaRichiesteAlGE(
						getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep(),
						lCodTipoBeneficio);
		}

		// Solo per debug
		if (mDati != null) {
			Iterator itxDati = mDati.iterator();
			for (int i = 0; itxDati.hasNext(); i++) {
				DecodificheModel lDato = (DecodificheModel) itxDati.next();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Dati Provv Sige [" + i + "] " + lDato.getDescription());
			}
		}
		return lOption;
	}

	/**
	 * Recupero tutte le richieste al GE di Aministia/Indulto validate e legate al Fascicolo SIEP individuato
	 * dall'ID. nella form (solo nel caso di amnistia/indulto
	 * 
	 * @param aIdFascicoloSiep
	 * @throws F3BException
	 */

	protected void ricercaRichiesteAlGE(BigDecimal aIdFascicoloSiep, String aCodTipoBeneficio)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaRichiesteAlGE");

		if (aIdFascicoloSiep != null) {
			AnnotazioneManualeModel lAnnPerRicerca = new AnnotazioneManualeModel();
			lAnnPerRicerca.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
			lAnnPerRicerca.setCodTipoAnnotazione(aCodTipoBeneficio); // ricerca sia 002 che 003
			lAnnPerRicerca.setFlagValidato("S");
			// Ricerca
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			Vector lListaRichieste = IAnn.ExRicercaRichieste(lAnnPerRicerca);
			setRequestAttribute("RichiesteAlGE", lListaRichieste);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num RichiesteAlGE -> " + lListaRichieste.size());
		}
	}

	protected boolean isIndultoAmnistia(String aCodOggetto) {
		boolean lRet = false;
		if (aCodOggetto.equalsIgnoreCase(APPLICAZIONE_INDULTO)
				|| aCodOggetto.equalsIgnoreCase(APPLICAZIONE_INDULTO_CONDIZIONATO)
				|| aCodOggetto.equalsIgnoreCase(APPLICAZIONE_AMNISTIA)
				|| aCodOggetto.equalsIgnoreCase(APPLICAZIONE_AMNISTIA_CONDIZIONATA))
			lRet = true;
		return lRet;
	}

	/**
	 * Nel caso di oggetto relativiad INDULTO o AMNISTIA esiste una ANNOTAZIONE MANUALE legata all'EVENTO. In
	 * questi casi viene effettuata la ricerca e l'Annotazione viene passata nella request oltre al
	 * FLAG_INDULTO che viene valorizzato al tipo di BENEFICIO (Indulto/Amnistia).
	 * 
	 * @param aTenore
	 * @return
	 * @throws F3BException
	 */

	protected AnnotazioneManualeModel ricercaAnnotazioneManuale(TenoreSigeModel aTenore) throws F3BException {
		AnnotazioneManualeModel lAnnMod = null;

		if (isIndultoAmnistia(aTenore.getCodOggettoSige())) {
			// Ricerca del Provvedimento attraverso l'ID TENORE_SIGE
			IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeModel lProvv = lCtrl
					.ExRicercaProvedimentoByIdTenore(getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE));

			// Ricerca dell?Annotazione Manuale attraverso l'Id Evento
			IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
			Vector lListAnnMan = lCtrlAnnMan
					.ExRicercaAnnotazioneManualeByIdEvento(lProvv.getIdEventoGenerato());
			if (lListAnnMan != null && lListAnnMan.size() > 0) {
				lAnnMod = (AnnotazioneManualeModel) lListAnnMan.get(0);
				setRequestAttribute("AnnotazioneManuale", lAnnMod);
				setRequestAttribute(FLAG_INDULTO, lAnnMod.getCodTipoAnnotazione());
			}
		}
		return lAnnMod;

	}

	protected AnnotazioneManualeModel ricercaAnnotazioneManuale(BigDecimal idSentenza,
			BigDecimal idTenoreSige) throws F3BException {
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		// AnnotazioneManualeModel annotazione =
		// lCtrlAnnMan.ExRicercaAnnotazioneManualeByIdSentenza(idSentenza);
		AnnotazioneManualeModel annotazione = lCtrlAnnMan
				.ExRicercaAnnotazioneManualeByIdSentenzaIdTenoreSige(idSentenza, idTenoreSige);

		setRequestAttribute("AnnotazioneManuale", annotazione);

		if (annotazione != null)
			setRequestAttribute(FLAG_INDULTO, annotazione.getCodTipoAnnotazione());

		return annotazione;
	}

	private TenoreSigeEstesoModel getSelTenore(Vector<TenoreSigeEstesoModel> lTenori, BigDecimal idTenore) {
		TenoreSigeEstesoModel sel = null;
		for (TenoreSigeEstesoModel tenore : lTenori) {
			if (tenore.getTenoreSige().getIdTenoreSige().compareTo(idTenore) == 0) {
				sel = tenore;
				break;
			}

		}
		return sel;
	}

}