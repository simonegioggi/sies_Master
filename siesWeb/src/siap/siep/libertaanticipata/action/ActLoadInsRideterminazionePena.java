package siap.siep.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInsRideterminazionePena
 * </p>
 * <p>
 * Description: Classe Action per la Load Inserimento Rideterminazione Pena da Decisioni della Sorveglianza: -
 * Ridimensionamento LA - Scomputo Permesso Questa Action viene invocata dalla form della griglia delle
 * Decisioni della Sorveglianza
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 * @since 5.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInsRideterminazionePena extends ActionSiap implements ICostantiCalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Sezione con i controlli preliminari
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Controllo Fascicolo di Competenza
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// ==========================================================================
		// Recupero la Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero la Posizione Giuridica
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

		// ==========================================================================
		// Recupero la pena residua
		// ==========================================================================
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel lPenaResMod =
		// lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// 03/06/2016 FIX recuperava l'ultima a sistema indipendentemente dallo stato
		// di validazione
		PenaResiduaModel lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		if (lPenaResMod == null || (lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& !lPosLuoAltr.getPosizioneGiuridica().isLibero()
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
				&& lPenaResMod != null && lPenaResMod.getDataInizio() == null)) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena.
			// Impossibile eseguire l'ordine di esecuzione.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if (lPenaResMod == null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?");
			}

			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Recupero il codice motivo
		// ==========================================================================
		// String lCodMotivo = this.getRequestStringParameter("codMotivo");
		String lCodMotivo = "0958";

		// ==========================================================================
		// Recupero il Magistrato a cui è assegnato il fascicolo
		// ==========================================================================
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// ==========================================================================
		// Caricamento delle combo
		// ==========================================================================
		// Oggetti per il caricamento delle combo Tipo Provvedimento
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// Oggetti per il caricamento della combo Autorità Emittente
		Option lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "-" });
		setRequestAttribute("autorita", "" + lAutoritaSORV);

		// Oggetti per il caricamento della combo Oggetto Provvedimento
		Option lOggettoProv = new Option(DecodificheManager.getInstance().getRideterminazionePenaAltroSORV(),
				"-");
		// DA FARE:DEVO FILTRARE SOLO GLI OGGETTI CHE FANNO RIFERIMENTO A SCOMPUTI!!!!!!!
		lOggettoProv.setFilter(new String[] { "0958", "0994", "-" });
		setRequestAttribute("oggettoProvvedimento", "" + lOggettoProv);

		Vector lOgg = new Vector(DecodificheManager.getInstance().getRideterminazionePenaAltroSORV());
		for (int i = 0; i < lOgg.size(); i++) {
			DecodificheModel lDecoMod = (DecodificheModel) lOgg.elementAt(i);
			if (lDecoMod.getCode().equals("0958")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("descrizione " + lDecoMod.getDescription());
				setRequestAttribute("aCodOggetto", lCodMotivo);
				setRequestAttribute("aDescOggetto", lDecoMod.getDescription());
			}
		}

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INS_RIDET_PENA_SORV;
	}

}