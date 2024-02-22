package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaEventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActListaDocumentiSius extends ActionSiap implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		String lNaturaMA = getRequestStringParameter(CAMPO_NATURA_MA);

		String lTipoMA = "";
		if (!isRequestParameterNullObj(CAMPO_TIPO_MA))
			lTipoMA = getRequestStringParameter(CAMPO_TIPO_MA);

		String[] lTipoDecisone = getCodTipoDecisione(lNaturaMA, lTipoMA); // Decreto/Ordinanza
		String[] lNaturaDecisione = getCodNaturaDecisione(lNaturaMA);
		String[] lTipoMisura = getCodTipoMisura(lNaturaMA, lTipoMA);

		if (isRequestParameterNullObj("RICERCA_PER_SOGGETTO")) { // Ricerca MA per Fascicolo corrente
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();

			// List <MisuraAlternativaEventoModel>
			List lListaEventiOrdinanze = lCtrlMisura.ExRicercaMisureAlternativeEventiOrderDesc(
					lIdFascicoloSiep, lTipoDecisone, lNaturaDecisione, lTipoMisura);

			setRequestAttribute("documentiSius", lListaEventiOrdinanze);

			return PG_LISTA_DOCUMENTI_SIUS;
		} else {
			// new 05/2014 la ricerca viene effettuata per tutti i fascicoli presenti
			// sul distretto (BDI) legati a soggetti che abbiano lo stesso Nome e Cognome
			// del soggetto del fasciolo corrente.
			// Per ora utilizzata nella Prosecuzione 51bis per caricare in maschera
			// i dati dell'ordinanza/decreto di concessione della MA che si intende
			// proseguire in genere concessa su altro titolo per cui la ricerca viene
			// effettuata sull'intera BDI
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lNaturaMA = " + lNaturaMA);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lTipoMA   = " + lTipoMA);

			// Recupero l'elenco dei fascicoli legati a soggetti con stesso Nome e cognome
			SoggettoModel lSoggCorrente = (SoggettoModel) getSessionAttribute("soggetto");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lSoggCorrente = " + lSoggCorrente);

			SoggettoModel lSoggetto = new SoggettoModel();
			lSoggetto.setNome(lSoggCorrente.getNome());
			lSoggetto.setCognome(lSoggCorrente.getCognome());

			Vector<FascicoloSiepModel> lListaFascicoli = null;
			IFascicoloSiep lCtrlFascicolo = SIEPLookupRemote.getFascicoloSiepRemote();
			lListaFascicoli = lCtrlFascicolo.ExRicercaFascicoliPerSoggetto(lSoggetto);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fascioli trovati: " + lListaFascicoli.size());

			// Per ogni fascicolo recupero le MA
			List<MisuraAlternativaEventoModel> lListaEventiOrdinanze = new ArrayList<>();

			for (int i = 0; i < lListaFascicoli.size(); i++) {
				FascicoloSiepModel lFascicolo = lListaFascicoli.elementAt(i);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lFascicolo = [" + lFascicolo.getIdFascicoloSiep() + "], " + "[ "
						+ lFascicolo.getChiaveAnno() + "/" + lFascicolo.getChiaveProgr() + "] " + "[ "
						+ lFascicolo.getDescrTipoUfficio() + " di " + lFascicolo.getDescrComuneUfficio()
						+ "]");

				IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
				List<MisuraAlternativaEventoModel> lListaPerFasciolo = lCtrlMisura
						.ExRicercaMisureAlternativeEventiOrderDesc(lFascicolo.getIdFascicoloSiep(),
								lTipoDecisone, lNaturaDecisione, lTipoMisura);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Misure trovate = " + lListaPerFasciolo.size());
				for (int j = 0; j < lListaPerFasciolo.size(); j++) {
					lListaPerFasciolo.get(j).setFascicoloSiep(lFascicolo);
				}

				lListaEventiOrdinanze.addAll(lListaPerFasciolo);

			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Totale Misure Trovate = " + lListaEventiOrdinanze.size());
			setRequestAttribute("documentiFascicoliSius", lListaEventiOrdinanze);

			return PG_LISTA_DOCUMENTI_SIUS_PER_SOGGETTO;
		}
	}

	/**
	 * Ritorna la lista dei codici motivo
	 *
	 * @param aNaturaMA
	 * @param aTipoMA
	 * @return
	 */
	private String[] getCodTipoMisura(String aNaturaMA, String aTipoMA) throws F3BException {

		String[] lCodMotivi = new String[0];

		if (aTipoMA.equals(AFFIDAMENTO_IN_PROVA)) {
			if (aNaturaMA.equals(CONCESSIONE)) {
				// MEV_9-SIEP: si differenzia per PM e PMM
				if (isUfficioMinorenni())
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoMAffPMinor());
				else
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoMAffP());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoSospProvvMAffP());
			} else if (aNaturaMA.equals(PERDITA_EFFICACIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAPreEffAffPro());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRipristinoMAffP());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMAffP());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMAffP());
			} else if (aNaturaMA.equals(REVOCA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAffP());
			} else if (aNaturaMA.equals(CESSAZIONE)) {
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMAffP();
				Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAffPTDS51bis());

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoTDS51BisSuReclamo);
				lCodMotivi = estraiCodiciDecodifiche(lOggetti);
			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMAffPMDS51bis();

				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
			} else if (aNaturaMA.equals(AMMISSIONE_PROVVISORIA)) {
				// MEV_9 si differenzia per PM e PMM
				if (isUfficioMinorenni())
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffiPmm());
				else
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
				// MEV_9 - FINE
			} else if (aNaturaMA.equals(PROSECUZIONE_51BIS) || aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) {
				// Prosecuzione MDS+TDS
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMAAffProMDS51Bis();
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMAAffPro_TDS_51Bis();

				Collection lOggetti = new Vector(lOggettoMDS51bis);
				lOggetti.addAll(lOggettoTDS);
				lCodMotivi = estraiCodiciDecodifiche(lOggetti);
			}
		} else if (aTipoMA.equals(DETENZIONE_DOMICILIARE)) {
			if (aNaturaMA.equals(CONCESSIONE)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMADDom());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoSospProvvMADetDom());
			} else if (aNaturaMA.equals(PERDITA_EFFICACIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAPreEffDetDom());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRipristinoDetDom());
			} else if (aNaturaMA.equals(REVOCA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMADetDom());
			} else if (aNaturaMA.equals(CESSAZIONE)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDom());

				Collection<DecodificheModel> lOggettoTDS = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDom());
				Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDomTDS51bis());
				// Collection <DecodificheModel> lOggettoMDS51bis = new
				// Vector(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDomMDS51bis());

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoTDS51BisSuReclamo);
				// lOggetti.addAll(lOggettoMDS51bis);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);
			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMADetDomMDS51bis();

				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMADetDom());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMADetDom());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMADetDom());
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMADetDom());
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMADetDom());
			} else if (aNaturaMA.equals(AMMISSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom());
				// MEV_9 si differenzia per PM e PMM
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
				if (isUfficioMinorenni())
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDomPmm());
				else
					lCodMotivi = estraiCodiciDecodifiche(
							DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom());
				// MEV_9 - FINE
			} else if (aNaturaMA.equals(PROSECUZIONE_51BIS) || aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoProsecMADetDomMDS51Bis());
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMADetDomMDS51Bis();
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMADetDom_TDS_51Bis();

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoMDS51bis);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);
			}
		} else if (aTipoMA.equals(SEMILIBERTA)) {
			if (aNaturaMA.equals(CONCESSIONE)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMASemiL());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoSospProvvMASemiL());
			} else if (aNaturaMA.equals(PERDITA_EFFICACIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAPreEffSemli());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRipristinoSemi());
			} else if (aNaturaMA.equals(REVOCA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMASemiL());
			} else if (aNaturaMA.equals(CESSAZIONE)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiL());

				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMASemiL();
				Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiLTDS51bis());
				// Collection <DecodificheModel> lOggettoMDS51bis =
				// DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiLMDS51bis();

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoTDS51BisSuReclamo);
				// lOggetti.addAll(lOggettoMDS51bis);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);

			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMASemiLMDS51bis();

				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMASem());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMASem());
			} else if (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMASem());
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMASem());
			} else if (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMASem());
			} else if (aNaturaMA.equals(PROSECUZIONE_51BIS) || aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoProsecMASemMDS51Bis());
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMASemMDS51Bis();
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMASem_TDS_51Bis();

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoMDS51bis);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);

			}
		} else if (aTipoMA.equals(INDULTINO)) {
			if (aNaturaMA.equals(CONCESSIONE)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAConIndultino());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMASospIndultino());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAIndultino());
			} else if (aNaturaMA.equals(PERDITA_EFFICACIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAPerEfficIndultino());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMARipristinoIndultino());
			} else if (aNaturaMA.equals(REVOCA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAIndultino());
			} else if (aNaturaMA.equals(CESSAZIONE)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAIndultino());
			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMAIndultinoMDS51bis();

				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			}
		}
		// 27/09/2010 Espiazione Pena presso Domicilio
		else if (aTipoMA.equals(ESP_PRESSO_DOM)) {
			if (aNaturaMA.equals(CONCESSIONE)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoMAConIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAEspPressoDom());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoMASospIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMASospEspPressoDom());
			} else if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMADetDom());
			} else if (aNaturaMA.equals(PERDITA_EFFICACIA)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoMAPerEfficIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMAPerEfficEspPressoDom());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoMARipristinoIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMARipristinoEspPressoDom());
			} else if (aNaturaMA.equals(REVOCA)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAIndultino());
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAEspPressoDom());
			} else if (aNaturaMA.equals(CESSAZIONE)) {
				Collection<DecodificheModel> lOggettoTDS = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAEspPressoDom());
				Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(DecodificheManager
						.getInstance().getMotivoProvvedimentoCessazioneMAEspPressoDomTDS51bis());

				lOggettoTDS.addAll(lOggettoTDS51BisSuReclamo);

				lCodMotivi = estraiCodiciDecodifiche(lOggettoTDS);
			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMAEspPressoDomMDS51bis();

				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			} else if (aNaturaMA.equals(PROSECUZIONE_51BIS) || aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) {
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis();
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMAEsecPreDom_TDS_51Bis();

				Collection lOggetti = new Vector(lOggettoMDS51bis);
				lOggetti.addAll(lOggettoTDS);
				lCodMotivi = estraiCodiciDecodifiche(lOggetti);

				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoProsecMAEsecPreDomMDS51Bis());
			}
		} else if (aTipoMA.equals(RIGETTO)) {
			lCodMotivi = estraiCodiciDecodifiche(
					DecodificheManager.getInstance().getMotivoProvvedimentoRigettoMA());
		} else if (aTipoMA.equals(DETENZIONE_DOMICILIARE_TERMINE)) {
			if (aNaturaMA.equals(DIFFERIMENTO_PENA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMADetDomTemp());
			} else if (aNaturaMA.equals(PROROGA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoMADetDomTempProroga());
			} else if (aNaturaMA.equals(PROROGA_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(DecodificheManager.getInstance()
						.getMotivoProvvedimentoMADetDomTempProrogaProvvisoria());
			} else if (aNaturaMA.equals(PROSECUZIONE_51BIS) || aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) {
				// lCodMotivi =
				// estraiCodiciDecodifiche(DecodificheManager.getInstance().getMotivoProvvedimentoProsecMADetDomTerMDS51Bis());
				Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMADetDomTerMDS51Bis();
				Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
						.getMotivoProvvedimentoProsecMADetDomTer_TDS_51Bis();

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoMDS51bis);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);

			} else if (aNaturaMA.equals(CESSAZIONE)) {
				Collection<DecodificheModel> lOggettoTDS = new Vector(
						DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMADetDomTerm());
				Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(DecodificheManager
						.getInstance().getMotivoProvvedimentoCessazioneMADetDomTermTDS51bis());

				Collection lOggetti = new Vector(lOggettoTDS);
				lOggetti.addAll(lOggettoTDS51BisSuReclamo);

				lCodMotivi = estraiCodiciDecodifiche(lOggetti);
			} else if (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
				Collection<DecodificheModel> lOggettoMDS51bis = new Vector(DecodificheManager.getInstance()
						.getMotivoProvvedimentoCessazioneMADetDomTermMDS51bis());
				lCodMotivi = estraiCodiciDecodifiche(lOggettoMDS51bis);
			}
		} else if (aTipoMA.equals(DIFFERIMENTO_PENA_PROV)) { // Sempre concessione
			lCodMotivi = estraiCodiciDecodifiche(
					DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiffProvv());
		} else if (aTipoMA.equals(DIFFERIMENTO_PENA_DEF)) { // Sempre concessione
			lCodMotivi = estraiCodiciDecodifiche(
					DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiffDef());
		} else if (aTipoMA.equals(ESPULSIONE)) { // concessione
			if (aNaturaMA.equals(CONCESSIONE)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoEspulsione());
			} else if (aNaturaMA.equals(ACCOGLIMENTO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoAccoglieOpEspulsione());
			} else if (aNaturaMA.equals(RIGETTO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRigettoOpEspulsione());
			}
		}
		// MERGE v10 COLLAUDO: aggiunta casistica per gestire gli arresti domiciliari
		else if (aTipoMA.equals(ARRESTI_DOMICILIARI)) {
			if (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoSospProvvArrestiDom());
			} else if (aNaturaMA.equals(RIPRISTINO)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRipristinoArrestiDom());
			} else if (aNaturaMA.equals(REVOCA)) {
				lCodMotivi = estraiCodiciDecodifiche(
						DecodificheManager.getInstance().getMotivoProvvedimentoRevocaArrestiDom());
			}
		}

		if (aNaturaMA.equals(CONCESSIONE_SOSPENSIONE)) {
			lCodMotivi = estraiCodiciDecodifiche(DecodificheManager.getInstance().getOggettoDecisione());
		}

		return lCodMotivi;
	}

	/**
	 *
	 * @param aNaturaMA
	 * @param aTipoMA
	 * @return
	 */
	private String[] getCodTipoDecisione(String aNaturaMA, String aTipoMA) {

		String[] lCodTipoDecisione = null;

		if ((aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(RIPRISTINO) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(REVOCA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				// || (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(RIPRISTINO) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(REVOCA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				// MERGE v10 COLLAUDO: aggiunte casistiche per gestire gli arresti domiciliari
				|| (aNaturaMA.equals(RIPRISTINO) && aTipoMA.equals(ARRESTI_DOMICILIARI))
				|| (aNaturaMA.equals(REVOCA) && aTipoMA.equals(ARRESTI_DOMICILIARI))
				// || (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(RIPRISTINO) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(REVOCA) && aTipoMA.equals(SEMILIBERTA))
				// || (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(RIPRISTINO) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(REVOCA) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(INDULTINO)) || (aNaturaMA.equals(RIGETTO))
				|| (aNaturaMA.equals(DIFFERIMENTO_PENA) && aTipoMA.equals(DETENZIONE_DOMICILIARE_TERMINE))
				|| (aNaturaMA.equals(PROROGA) && aTipoMA.equals(DETENZIONE_DOMICILIARE_TERMINE))
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(DIFFERIMENTO_PENA_DEF)) // Aggiunto da
																							// diego
																							// 30/08/2006
				|| (aNaturaMA.equals(ACCOGLIMENTO) && aTipoMA.equals(ESPULSIONE))) {
			lCodTipoDecisione = new String[1];
			lCodTipoDecisione[0] = "03";
		} else if ((aNaturaMA.equals(SOSPENSIONE_PROVVISORIA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(PERDITA_EFFICACIA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				// MERGE v10 COLLAUDO: aggiunte casistiche per gestire gli arresti domiciliari
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA) && aTipoMA.equals(ARRESTI_DOMICILIARI))
				|| (aNaturaMA.equals(PERDITA_EFFICACIA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)
						&& aTipoMA.equals(DETENZIONE_DOMICILIARE))
				// MEV_09|| (aNaturaMA.equals(AMMISSIONE_PROVVISORIA) &&
				// aTipoMA.equals(DETENZIONE_DOMICILIARE))
				// || (aNaturaMA.equals(AMMISSIONE_PROVVISORIA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(PERDITA_EFFICACIA) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(PERDITA_EFFICACIA) && aTipoMA.equals(INDULTINO))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO)
						&& aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(PROROGA_PROVVISORIA) && aTipoMA.equals(DETENZIONE_DOMICILIARE_TERMINE))
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(DIFFERIMENTO_PENA_PROV)) // Aggiunto da
																								// diego
																								// 30/08/2006
				|| (aNaturaMA.equals(CONCESSIONE) && aTipoMA.equals(ESPULSIONE))) {
			lCodTipoDecisione = new String[1];
			lCodTipoDecisione[0] = "02";
		} else if (aNaturaMA.equals(CONCESSIONE_SOSPENSIONE)
				// 22/01/2014 DL 146 Può essere concessa anche con ordinanza
				|| (aNaturaMA.equals(AMMISSIONE_PROVVISORIA) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				// MEV_9 anche per la deetenzione domiciliare - ammissione provvisoria si prevedono sia
				// decreti che ordinanze
				|| (aNaturaMA.equals(AMMISSIONE_PROVVISORIA) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(PROSECUZIONE_51BIS)) // DL 146/20113
				|| (aNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)) // DL 146/20113
				|| (aNaturaMA.equals(CESSAZIONE_51BIS_MDS)) // DL 146/20113
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(AFFIDAMENTO_IN_PROVA))
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(DETENZIONE_DOMICILIARE))
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(SEMILIBERTA))
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(DETENZIONE_DOMICILIARE_TERMINE))
				|| (aNaturaMA.equals(CESSAZIONE) && aTipoMA.equals(ESP_PRESSO_DOM))) {
			lCodTipoDecisione = new String[2];
			lCodTipoDecisione[0] = "02";
			lCodTipoDecisione[1] = "03";
		}

		return lCodTipoDecisione;
	}

	/**
	 * In funzione della natura decisione indicate, restituisce l'insieme dei codici Natura decisione previsti
	 * sulla tabella MISURA_ALTERNATIVA.
	 *
	 * @param lNaturaMA
	 * @return
	 */
	private String[] getCodNaturaDecisione(String lNaturaMA) {

		String[] lCodNatura = new String[1];

		if (lNaturaMA.equals(CONCESSIONE) || lNaturaMA.equals(CONCESSIONE_SOSPENSIONE)) {
			lCodNatura[0] = "CO";
		} else if (lNaturaMA.equals(SOSPENSIONE_PROVVISORIA)
				|| lNaturaMA.equals(SOSPENSIONE_PROVVISORIA_51_BIS)) {
			lCodNatura[0] = "SP";
		} else if (lNaturaMA.equals(PERDITA_EFFICACIA)) {
			lCodNatura[0] = "PE";
		} else if (lNaturaMA.equals(RIPRISTINO)) {
			lCodNatura = new String[2];
			lCodNatura[0] = "RI";
			lCodNatura[1] = "RG";
		} else if (lNaturaMA.equals(ESTENSIONE_DEFINITIVA)) {
			lCodNatura = new String[2];
			lCodNatura[0] = "ED";
			lCodNatura[1] = "RI";
		} else if (lNaturaMA.equals(ESTENSIONE_DEFINITIVA_CUMULO)) {
			lCodNatura = new String[2];
			lCodNatura[0] = "EC";
			lCodNatura[1] = "RI";
		} else if (lNaturaMA.equals(REVOCA)) {
			lCodNatura[0] = "RE";
		} else if (lNaturaMA.equals(CESSAZIONE) || lNaturaMA.equals(CESSAZIONE_51BIS_MDS)) {
			lCodNatura[0] = "CE";
		} else if (lNaturaMA.equals(AMMISSIONE_PROVVISORIA)) {
			// richiesta umberto modifica dario data:03-05-07
			// lCodNatura[0] = "AP";
			lCodNatura[0] = "CO";
		} else if (lNaturaMA.equals(RIGETTO)) {
			lCodNatura[0] = "RG";
		} else if (lNaturaMA.equals(DIFFERIMENTO_PENA)) {
			// 23/06/2011 lCodNatura[0] = "DD";
			lCodNatura[0] = "CO";
		} else if (lNaturaMA.equals(PROROGA)) {
			lCodNatura[0] = "DD";
		} else if (lNaturaMA.equals(PROROGA_PROVVISORIA)) {
			lCodNatura[0] = "DD";
		} else if (lNaturaMA.equals(PROSECUZIONE_PROVVISORIA)) {
			lCodNatura[0] = "PP";
		} else if (lNaturaMA.equals(PROSECUZIONE_PROVVISORIA_CUMULO)) {
			lCodNatura[0] = "PC";
		} else if (lNaturaMA.equals(ACCOGLIMENTO)) {
			lCodNatura[0] = "AC";
		} else if (lNaturaMA.equals(PROSECUZIONE_51BIS)) {
			lCodNatura = new String[2];
			lCodNatura[0] = "ED"; // ED sia per MDS che TDS
			lCodNatura[1] = "EC"; // EC solo per MDS iscritte SIEP
		}
		// else if(lNaturaMA.equals(PROSECUZIONE_51BIS_CUMULO)){
		// lCodNatura[0] = "PC"; //FIXME DL 146/2013 verificare la natura decisione della Sorveglianza ED?
		// Estensione Definitva
		// }

		return lCodNatura;
	}

	private String[] estraiCodiciDecodifiche(Collection aCollDecodifiche) {

		List lListaCodici = new ArrayList();
		Iterator iter = aCollDecodifiche.iterator();
		while (iter.hasNext()) {
			DecodificheModel item = (DecodificheModel) iter.next();
			lListaCodici.add(item.getCode());
		}
		return (String[]) lListaCodici.toArray((new String[0]));
	}

}