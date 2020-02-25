package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciVerbaleUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Verbale Udienza.
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
public class ActInserisciVerbaleUdienza extends ActionSiap implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();

		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dal FascicoloGPModel
		FascicoloSiusModel lFasSius = lFascicoloGPModel.getFascicoloSiusModel();
		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		BigDecimal lIdFasSius = lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius();
		if (Utils.isPresent(lIdFasSius)) {
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			MagistratoRelatoreModel mrm = imr.ExRicercaEstesaMagRelByFascicolo(lIdFasSius);
			if (mrm != null && mrm.getMagistrato() != null) {
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = new MagistratoModel();
				mm.setCodMagistrato(mrm.getMagistrato().getCodMagistrato());
				mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
				mm.setCognome(mrm.getMagistrato().getCognome());
				mm.setNome(mrm.getMagistrato().getNome());
				Vector v = im.ExRicercaMagistrato(mm);
				if (!v.isEmpty()) {
					MagistratoModel mag = (MagistratoModel) v.get(0);
					if (mag.getDataFineValidita() != null
							&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
									|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
				}
			}
		}

		// Preleva la data udienza.
		Date lDataUdienza = lFascicoloGPModel.getGeneraleProcedimentoModel().getDataCameraConsiglio();
		/*
		 * // AVVOCATO BigDecimal lIdAvvocato =
		 * getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO); if (lIdAvvocato != null) {
		 * AvvocatoModel lAvvMod = new AvvocatoModel(); AvvocatoFascicoloSiusModel lAvvFascMod=new
		 * AvvocatoFascicoloSiusModel(); lAvvMod.setIdAvvocato(lIdAvvocato);
		 *
		 * lAvvFascMod.setCodTipoAvvocato("01");
		 * lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		 * lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		 * lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		 * lAvvFascMod.setFasSiuIdFascicoloSius(lIdFasSius);
		 * lAvvFascMod.setCodUfficioInserimento(lCodiceUfficio);
		 *
		 * // AvvocatoSiusModel lAvvSiusMod = new AvvocatoSiusModel();
		 *
		 * IAvvocato lCtrl3 = SIUSLookupRemote.getAvvocatoRemote(); AvvocatoSiusModel lAvvSiusMod =
		 * lCtrl3.ExInserisciAvvocato(lAvvMod,lAvvFascMod); // setta la risposta nella request
		 * setRequestAttribute("avvocato",lVect); }
		 */
		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.
		EventoModel lEve = new EventoModel();
		lEve.setCodTipoEvento("07");
		lEve.setDataEmissione(lDataUdienza);
		lEve.setCodMotivo("0700");
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setCodLuogoEmittente(lCodComune);
		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.setCodEsito("-");
		lEve.setCodTipoProvvedimento("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setFasSiuIdFascicoloSius(lFasSius.getIdFascicoloSius());
		// lEve.setTemIdTemplate();

		// Chiamata al Controller
		IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
		lEve = lCtrl2.ExInserisciEvento(lEve);

		// Prepara la pagina di redirezione.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.udienza.action.ActLoadDettaglioVerbaleUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEve.getIdEvento());

		return lPage.toString();
	}

}