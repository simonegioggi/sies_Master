package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.depositoordinanzapc.util.RicercaProvvedimentiCollegati;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanzaUDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Ordinanza Revoca Misura Alternativa
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciOrdinanzaReclamoPermesso extends ActionSiap implements
		ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = null;
		BigDecimal lIdFasOrigine = null;
		DepositoDecretoModel lDepDecMod = null;
		OrdinanzaEventoTenoriPrescrizioniModel lDatiOrdinanza = null;
		String lPage = PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_PERMESSO;

		// lettura tipo di ordinanza
		String lCodTipoDec = getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE);

		// switch tipo di ordinanza
		if (lCodTipoDec.compareTo("00") == 0) {
			// Lettura contenuto
			String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);

			// Generazione automatica in base al contenuto
			Collection lOggetti = DecodificheManager.getInstance().getOggettoProcedimento();
			lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Tipo Decreto: " + lCodTipoDec);

		// Valorizzazione pagina jsp di inserimento
		if (lCodTipoDec.compareTo(RECLAMO_SCOMPUTO) == 0)
			lPage = PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_SCOMPUTO;

		else if (lCodTipoDec.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0) // 15/04/2014 Nuova
																					// ordinanza L.A. -
																					// Decreto 2013/146 -
			lPage = PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_LIBERAZIONE_ANTICIPATA; // Nuova form per Gestione
																				// tipo oggetti Reclamo L.A.

		else if (lCodTipoDec.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0) { // 15/07/2014 Nuova Revoca
																					// L.A. - Decreto 2013/146
			// lCodTipoDec.compareTo(ICostantiDepositoDecreto.GENERICO) == 0 )
			lPage = PG_LOAD_INSERISCI_ORDINANZA_REVOCA_LA; // Nuova form per Gestione tipo oggetti Revoca L.A.
			setRequestAttribute("revoca", "SI");
		}
		// TODO carmela (verificare)
		else if (lCodTipoDec.compareTo(RECLAMO_REVOCA_LICENZA_PERMESSO) == 0)
			lPage = PG_LOAD_INSERISCI_ORDINANZA_RECLAMO_SCOMPUTO;

		// Si preleva dalla sessione il fascicolo GPModel.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");

		lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("ActLoadInserisciOrdinanzaReclamoPermesso: Inizio ricerca del decreto di riferimento");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ID del Fascicolo corrente ->"
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// Se esiste il Fascicolo SIUS Origine si effettua la ricerca dei provvedimenti di riferimento.
		// Il Fascicolo Origine rappresenta il procedimento emesso dal Magistrato di Sorveglianza e trasferito
		// al TdS.

		lIdFasOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
		if (lIdFasOrigine != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Fascicolo Origine ->" + lIdFasOrigine);
			RicercaProvvedimentiCollegati lRicerca = new RicercaProvvedimentiCollegati(lIdFasOrigine);
			if (lCodTipoDec.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
					|| lCodTipoDec.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
				// Ricerca dell'Ordinanza di Liberazione Anticipata Reclamata
				lDatiOrdinanza = lRicerca.RicercaOrdinanza(LIBERAZIONE_ANTICIPATA);
				if (lDatiOrdinanza.getOrdinanza() != null)
					setRequestAttribute("datiOrdinanza", lDatiOrdinanza);
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Ordinanza di Liberazione Anticipata non trovata");
			} else {
				// Ricerca del Decreto Permesso
				lDepDecMod = lRicerca.RicercaDecreto(ICostantiDepositoDecreto.PERMESSO);
				if (lDepDecMod != null)
					setRequestAttribute("decreto", lDepDecMod);
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Deposito Decreto non trovato");
			}
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Il fascicolo (id ->" + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius()
					+ ") non ha un Fascicolo Origine di riferimento");

		setRequestAttribute("reclamo", "SI");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciOrdinanzaReclamoPermesso: Fine ricerca del decreto di riferimento");

		return lPage;
	}

}