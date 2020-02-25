package siap.siep.cumulo.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.beneficio.model.BeneficioPenaAccessoriaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.action.ICostantiPenaAccessoria;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci Pene Accessorie legate al Fascicolo Cumulante
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		this.setLinkRitorno();

		// Controllo fascicolo di SESSIONE
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isFascicoloSiepDiCompetenza();

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("PosizioneGiuridicaLuogoAltra", lPos);

		// ==========================================================================
		// Carica la Pena Residua NON validata inserita più di recente, dovrebbe
		// essere quella calcolata in fase di inserimento dei dati del cumulo.
		// Attenzione!! ciò non è necessariamente vero, se è stato fatto solo il primo
		// calcolo della pena (sul cumulante)
		// MAC - Va verificato che sia presente una pena cumulo associata al record
		// cumulo non validato (quelli di appoggio, il primo inserito)
		// ==========================================================================
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		// Option lOption = new Option( DecodificheManager.getInstance().getTipoPeneAccessorie());
		Option lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);
		lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoriaNoAltre", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente());
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// STUB 24/02/2006 Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
				DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });
		/* boolean lBool = ( */lColl.addAll(lColl2)/* ) */;
		lOption = new Option(lColl, "-");
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// STUB 23/03/2006 Costruzione opzioni "Tenore Ordinanza"
		Collection lColTenoreOrdinanza = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TENORE_ORDINANZA_PA");
		lColTenoreOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTenoreOrdinanza = new Option(lColTenoreOrdinanza, "0", 50);
		setRequestAttribute("tenoreOrdinanza", "" + lOptionTenoreOrdinanza);

		// Costruzione combo "Fonte Reato" e "Sottonumerazione"
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		// Lettura Pene Accessorie per selezionare la form pertinente.
		Vector lVect = new Vector();
		Vector lPenBenVec = new Vector();
		try {
			IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			lVect = lCtrl.ExRicercaPenaAccessoria(lPenMod);

			BeneficioPenaAccessoriaModel lBenPenMod = null;
			if (lVect != null) {
				Iterator itx = lVect.iterator();
				while (itx.hasNext()) {
					lBenPenMod = new BeneficioPenaAccessoriaModel();
					PenaAccessoriaModel lPenModel = (PenaAccessoriaModel) itx.next();
					lBenPenMod.setPenaAccessoria(lPenModel);

					if (lPenModel != null && lPenModel.getBenIdBeneficio() != null) {
						IBeneficio lCtrlBen = SIEPLookupRemote.getBeneficioRemote();
						BeneficioModel lBenMod = lCtrlBen
								.ExRicercaBeneficioByKey(lPenModel.getBenIdBeneficio());
						lBenPenMod.setBeneficio(lBenMod);
					}
					lPenBenVec.add(lBenPenMod);
				}
			}

		} catch (F3BException e) {
			setRequestAttribute("modalita", "I");
			return siap.siep.cumulo.action.ICostantiCumulo.PG_LOAD_INSERISCIPENAACCESSORIA;
		}
		setRequestAttribute("peneaccessorie", lVect);
		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);

		// Lettura degli eventi di Pena Accessoria del fascicolo.
		Vector lEventi = new Vector();
		try {
			IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			lVect = lCtrl.ExRicercaPenaAccessoria(lPenMod);

			// Si Invoca il controller per gli eventi di Esecuzione Pena Accessoria del fascicolo.
			IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
			String[] lTipoEvento = { "16", "17", "18" };
			lEventi = lCtrl2.ExRicercaEventoNotificaByFascicoloSiep(lPenMod.getFasSieIdFascicoloSiep(),
					lTipoEvento);
		} catch (F3BException e) {
			setRequestAttribute("modalita", "P");
			setRequestAttribute("eveCorrelati", lEventi);
			return siap.siep.cumulo.action.ICostantiCumulo.PG_LOAD_DETTAGLIO_APPLICAZIONE_BENEFICI;
		}
		setRequestAttribute("eveCorrelati", lEventi);

		if (lVect.size() == 1) {
			PenaAccessoriaModel lPenAcc = (PenaAccessoriaModel) lVect.firstElement();
			lOptionTenoreOrdinanza = new Option(lColTenoreOrdinanza, lPenAcc.getFlagCondonata(), 50);
		}
		setRequestAttribute("modalita", "P");
		return siap.siep.cumulo.action.ICostantiCumulo.PG_LOAD_DETTAGLIO_APPLICAZIONE_BENEFICI;
	}

}