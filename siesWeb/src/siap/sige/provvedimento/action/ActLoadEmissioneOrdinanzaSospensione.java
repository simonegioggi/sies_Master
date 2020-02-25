package siap.sige.provvedimento.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sige.SIGEException;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanzaSospensione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ActLoadEmissioneOrdinanzaSospensione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadEmissioneOrdinanzaSospensione extends ActRicercaFSigePuntuale implements
		ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneOrdinanzaSospensione: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della pagina di view in caso di assenza provvedimento definitorio.
		String lRetPage = PG_LOAD_EMISSIONE_ORDINANZA_SOSPENSIONE;

		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se si proviene dal menu'.
			super.processRequest();
		}

		if (this.isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Fascicolo Sige non in sessione");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		// Verifica esistenza di un' ordinanza depositata il fascicolo SIGE selezionato.
		ProvvedimentoSigeEventoModel lProvvedimento = lProvCtrl
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(lFasEsteso.getFascicoloSige()
						.getIdFascicoloSige());

		if (lProvvedimento == null
				|| lProvvedimento.getProvvedimento() == null
				|| lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() == null
				|| lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
						.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA) != 0
				|| lProvvedimento.getProvvedimento().getChiaveProgr() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Impossibile emettere Ordinanza di Sospensione! Non ci sono Ordinanze depositate per il fascicolo!");

		setRequestAttribute("provvDaSospendere", lProvvedimento);
 		   
 		// Combo per l'Ufficio Competente.
 		 	setComboUfficioCompetente();

		// In caso di esistenza di Ordinanza di Sospensione non annullata, oppure;
		// In caso di esistenza di Ordinanza di Sospensione svalidata, si accede al suo Dettaglio.
		// Altrimenti si può emettere un'altra Ordinanza di Sospensione.
		if (lProvvedimento != null
				&& lProvvedimento.getProvvedimento() != null
				&& lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige() != null
				&& (lProvvedimento.getProvvedimento().getCodTipoProvvedimentoSige()
						.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE) == 0)
				&& lProvvedimento.getEventoNotifica().getEvento() != null
				&& (lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null || (lProvvedimento
						.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && lProvvedimento
						.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != "A"))) {
			// Prepara la pagina di destinazione, il Dettaglio Ordinanza di Sospensione.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);

			lPage.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanzaSospensione");

			lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, ""
					+ lProvvedimento.getProvvedimento().getIdProvvedimentoSige());
			lRetPage = lPage.toString();
		} else {
			// Solo la prima volta vengono messi i Tenori in sessione
			if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
				// Ricerca tenori attivi
				TenoreSigeModel lTenore = new TenoreSigeModel();
				// lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
				// Vanno considerati solo i tenori del provvedimento da sospendere
				// quindi elimino la condizione sul fascicolo ed aggiungo quella sul provverdimento
				lTenore.setProvIdProvvedimentoSige(lProvvedimento.getProvvedimento().getIdProvvedimentoSige());
				ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
				Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

				// 24/05/2010 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta
				// SIGE.
				if (lTenori.size() == 0 && lFasEsteso.getRichiestaSige() != null)
					lTenori = lTenCtrl.ExRicercaTenoreEstesoByRichiesta(lFasEsteso.getRichiestaSige()
							.getIdRichiestaSige());

				setSessionAttribute("tenori", lTenori);
			}

			// Avvocati attuali assegnati al fascicolo SIGE.
			FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
			Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige()
					.getIdFascicoloSige());
			if (lAvvocati.size() > 0)
				setRequestAttribute("avvocato", lAvvocati);

			// Magistrato Assegnatario
			MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
			setRequestAttribute("magistratoassegnatario", lMagAss);

			// Combo per la definizione del tipo Giudizio.
			setComboTipoGiudizio();

			// Combo per l'Ufficio Competente.
			setComboUfficioCompetente();

			// In caso di Udienza gia fissata si visualizzano i dati del collegio.
			// Eventuale lettura del collegio.
			CollegioModel lColMod = null;
			if (lFasEsteso.getUdienzaProcedimento() != null
					&& lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
				ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
				lColMod = lCtrl.ExRicercaCollegioByIdUdienzaSige(lFasEsteso.getUdienzaProcedimento()
						.getUdiIdUdienzaSige());
			}
			setRequestAttribute("collegio", lColMod);

			// Carica Tipo Destinatario in base al Tipo Ufficio.
			String strTipoDest = lFasSigeUtils.leggiTipoDestinatario(this.getUfficioUtenteConnesso()
					.getCodTipoUfficio());
			setRequestAttribute("TipoDest", strTipoDest);

			// Preparazione delle COMBO per i Destinatari
			// Preleva elenco degli altri destinatari.
			Option lOptionAut = new Option();
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);

			// Ricerca LUOGO DETENZIONE
			IFasSigeDetenzione lDetenzioneCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
			FasSigeDetenzioneModel lDetenzione = lDetenzioneCtrl
					.ExRicercaUltimaDetenzioneFascicolo(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			setRequestAttribute("detenzione", lDetenzione);

			// LISTA UFFICI SOGGETTO
			// Option lOptionSog = new Option();
			// if (lDetenzione != null
			// && lDetenzione.getLuogoDetenzione() != null
			// && lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
			// lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), lDetenzione
			// .getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto(), 75);
			// else
			// lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

			// LISTA UFFICI
			Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
			// 30//11/2018 aggiungo autorità A2 su richiesta Nunzia (email del 29/11/2018 - Documento1.docx)
			String[] lStringFilter = new String[]{ "-", "22", "A2" };
			Option lOptionAvv = new Option(lTipoIstituto, "-", 75);			
			lOptionAvv.setFilter(lStringFilter);
			
			 setRequestAttribute("tipoAutoSogg", "");
			 setRequestAttribute("decrTipoAutoSogg", "");
			 setRequestAttribute("tipoAuto", "");						 
			 setRequestAttribute("decrTipoAuto", "");			
			 // imposto la sede della notifica al soggetto
		    setRequestAttribute("DescrLuogoDetenzione", "");
		    // imposto la sede della notifica altri destinatari
		       setRequestAttribute("sedeAuto", "");

			setRequestAttribute("luogodet", lDetenzione);
			setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
			setRequestAttribute("tipoAutorita", lOptionAut.toString());

			// Si richiama il lock
			lockApplicativo("Emissione_Provvedimento");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadEmissioneOrdinanzaSospensione: -> page: " + lRetPage);
		return lRetPage; // restituisce la jsp di VIEW
	}

}