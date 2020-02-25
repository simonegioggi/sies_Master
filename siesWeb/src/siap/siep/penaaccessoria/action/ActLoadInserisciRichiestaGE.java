package siap.siep.penaaccessoria.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRichiestaGE
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Richiesta al GE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciRichiestaGE extends ActionSiap implements ICostantiPenaAccessoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Gestione ritorno
		this.setLinkRitorno();

		// Controllo fascicolo di SESSIONE
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isFascicoloSiepDiCompetenza(); // 10/03/2008

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

		// Option lOption = new Option( DecodificheManager.getInstance().getTipoPeneAccessorie());
		Option lOption = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
				DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });
		/* boolean lBool = ( */lColl.addAll(lColl2)/* ) */;
		lOption = new Option(lColl, "-");
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// Costruzione opzioni "Tipo Richieste al GE"
		Collection lColTipoRichiestaGE = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_RICHIESTA_GE");
		lColTipoRichiestaGE = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTipoRichiestaGE = new Option(lColTipoRichiestaGE, "0", 50);
		String[] lStringFilter = { "-", "04", "01" };
		lOptionTipoRichiestaGE.setFilter(lStringFilter);
		setRequestAttribute("tipoRichiestaGE", "" + lOptionTipoRichiestaGE);

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
			return PG_PRELOAD_RICHIESTAGE;
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

			// Si Invoca il controller per gli eventi di Richiesta al GE di Pena Accessoria del fascicolo.
			IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
			String[] lTipoEvento = { "16", "17", "18" };
			lEventi = lCtrl2.ExRicercaEventoNotificaByFascicoloSiep(lPenMod.getFasSieIdFascicoloSiep(),
					lTipoEvento);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Warning Pene accessorie - Richiesta GE : " + e.getMessage());
			// setRequestAttribute("modalita", "G");
			// setRequestAttribute("eveCorrelati", lEventi);
			// return PG_RICERCAPENAACCESSORIA;
		}
		setRequestAttribute("eveCorrelati", lEventi);

		String lAggiungi = "yes";
		if (isRequestParameterNullObj("Aggiungi"))
			lAggiungi = "no";
		if ((lAggiungi.compareTo("yes") == 0)) {
			setRequestAttribute("modalita", "I");
			return PG_PRELOAD_RICHIESTAGE;
		} else {
			setRequestAttribute("modalita", "G");
			return PG_RICERCAPENAACCESSORIA;
		}
	}

}