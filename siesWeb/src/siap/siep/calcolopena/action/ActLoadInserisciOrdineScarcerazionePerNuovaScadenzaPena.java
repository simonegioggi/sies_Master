package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneReatoModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciOrdineScarcerazionePerNuovaScadenzaPena
 * </p>
 * <p>
 * Description: classe action per la load dell form dell'Ordine di scarcerazione per nuova scadenza pena nel
 * caso di: - Decisioni del GE: Depenalizzazione, Incostituzionalità, Amnistia/Indulto
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciOrdineScarcerazionePerNuovaScadenzaPena extends ActionSiap
		implements ICostantiAnnotazioneManuale {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

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

		// Controllo Esistenza POSIZIONE GIURIDICA
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAlt = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosLuoAlt = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPosLuoAlt == null || lPosLuoAlt.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPosLuoAlt);

		// Controllo Esistenza PENA RESIDUA
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (llPenMod == null || llPenMod.getIdPenaResidua() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile procedere!");

		setRequestAttribute("penaresidua", llPenMod);

		// Ricerca Magistrato Competente
		IMagistratoCompetente lCtrlMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiComp = lCtrlMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("magistratocompetente", lMagiComp);

		// ==========================================================================
		//
		// ==========================================================================
		AnnotazioneOrdinanzaModel lAnnOrdMod = new AnnotazioneOrdinanzaModel();
		Vector lListAnnRea = new Vector();

		IAnnotazioneManuale IAnnCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		String lCodMotivo = null;

		// Se il codice motivo non è legato alla
		// 0161 - Determinazione pena a seguito di applicazione beneficio
		// 0162 - Rideterminazione pena
		// 0163 - Determinazione pena a seguito di revoca beneficio
		if (!getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO).equals("0161")
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO).equals("0162")
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO).equals("0163")) {
			// Recupero l'ultima Ordinanza (03) e annotazione associata
			lAnnOrdMod = IAnnCtrl.ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(lIdFascicolo);

			// ** Se l'Ordinanza esiste viene ricercato il Provvedimento
			// ** corrispondente al particolare CODICE MOTIVO dell'ordinanza trovata.
			// ** Per Ordinanza si intente un evento
			// ** con COD_MOTIVO IN '0122','0212','0213','0210','0211','0121'
			// ** con COD_TIPO_EVENTO '01'
			// ** con COD_TIPO_PROVVEDIMENTO '03'

			// ** Se l'Ordinanza NON esiste vengono cercate le annotazioni
			// ** con i relativi reati associate al Provvedimento.
			// ** Per Provvedimento si intende un evento
			// ** con COD_MOTIVO IN '0122','0212','0213','0210','0211','0121'
			// ** con COD_TIPO_EVENTO '01'
			// ** con COD_TIPO_PROVVEDIMENTO '04'

			if (lAnnOrdMod != null && lAnnOrdMod.getEvento() != null
					&& lAnnOrdMod.getEvento().getCodMotivo() != null) {
				lCodMotivo = lAnnOrdMod.getEvento().getCodMotivo();
			}
		}

		// Annotazioni manuali
		lListAnnRea = IAnnCtrl.ExRicercaUltimeAnnotazioniManualiReatiByIdFascicolo(lIdFascicolo, lCodMotivo);

		if (lListAnnRea == null || lListAnnRea.isEmpty())
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Nessuna Annotazione Manuale trovata. Impossibile procedere!");

		setRequestAttribute("annotazioneordinanza", lAnnOrdMod);
		setRequestAttribute("listaannotazionireati", lListAnnRea);
		// ******************************************************************************

		String lCodTipo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
		setRequestAttribute("codmotivo", lCodTipo);

		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Nel caso di indulto utilizzo una form differente
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListAnnRea = " + lListAnnRea);
		if (lListAnnRea != null && !lListAnnRea.isEmpty()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("XXXXX");
			Iterator lIter = lListAnnRea.iterator();
			AnnotazioneReatoModel lAnnRea = null;
			AnnotazioneManualeModel lAnn = null;

			while (lIter.hasNext()) {
				lAnnRea = (AnnotazioneReatoModel) lIter.next();
				lAnn = lAnnRea.getAnnotazioneManuale();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("codAnno = " + lAnn.getCodTipoAnnotazione());
				if (lAnn.getCodTipoAnnotazione() != null && lAnn.getCodTipoAnnotazione().equals("002") // Indulto
				) {
					return PG_LOAD_INSERISCI_ORDINE_SCARCERAZIONE_PER_NUOVA_SCADENZA_PENA_INDULTO;
				}
			}
		}
		return PG_LOAD_INSERISCI_ORDINE_SCARCERAZIONE_PER_NUOVA_SCADENZA_PENA;
	}
}