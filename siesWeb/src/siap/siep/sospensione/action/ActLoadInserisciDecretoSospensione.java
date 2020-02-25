package siap.siep.sospensione.action;

import java.util.Vector;

//import siap.siep.verbale.controller.IVerbale;
//import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciDecretoSospensione
 * </p>
 * <p>
 * Description: Classe Action per la load inserimento del Decreto di Sospensione
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

public class ActLoadInserisciDecretoSospensione extends ActionSiap implements ICostantiSospensione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

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

		this.isEventoNonValidato();

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		// String[] motivo ={"0061","0062","0063","0104","0105","0117","0000"};
		// Modifica del 05/11/2015 richiesta da Michele
		// MEV 10_S3 gestite nuove posizioni giuridiche
		// 17/04/2019  MEV70 Aggiunta nuovi Codici. 
    	//String[] motivo ={"0061","0062","0063","0104","0105","0117","0000","5506","5507","5508","5509","5510","5511","5512","5513"};
    	String[] motivo ={"0061","0062","0063","0104","0105","0117","0000","5506","5507","5508","5509","5510","5511","5512","5513","0635","0637","0642","0661","0666"};
    	lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloCodiceMotivo(lFascMod.getIdFascicoloSiep(),motivo);

    	if (lEveNot == null || lEveNot.getEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");

		// Controllo presenza avvenuta notifica al condannato
		NotificaModel[] lNotificheOE = lEveNot.getNotifiche();
		for (int i = 0; i < lNotificheOE.length; i++) {
			NotificaModel lNot = lNotificheOE[i];
			if (lNot != null && "E".equals(lNot.getCodTipoNotifica())
					&& lNot.getDataAvvenutaNotifica() != null) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"E' stata notificata la notifica al condannato del Decreto di Sospensione. Impossibile procedere.");
			}
		}

		setRequestAttribute("eventonotifica", lEveNot);

		/*
		 * Commentato poichè l'evento verbale non viene correttamente legato all'evento giusto! EventoModel
		 * lEveMod = new EventoModel(); lEveMod =
		 * lCtrl.ExRicercaEventoByEveIdEvento(lEveNot.getEvento().getIdEvento());
		 * 
		 * IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote(); VerbaleModel lVermod = new VerbaleModel();
		 * if( lEveVerbale != null && lEveVerbale.getIdEvento()!= null ) { lVermod =
		 * lCtrlVer.ExRicercaVerbaleByCodTipoIdEvento( lEveVerbale.getIdEvento(), "02" ); }
		 * 
		 * if( lVermod == null || lVermod.getIdVerbale() == null ) { throw new
		 * SIEPException(SIEPException.USER_MESSAGE,
		 * "Impossibile eseguire il decreto di irreperibilità, inserire il Verbale Vane Ricerche"); }
		 */

		// Controllo esistenza Verbale Vane Ricerche
		EventoModel lEveVerbale = new EventoModel();
		lEveVerbale.setCodTipoEvento("07");
		lEveVerbale.setCodTipoProvvedimento("17");
		lEveVerbale.setCodMotivo("0313");
		lEveVerbale.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveVerbale.setFlagDocumentoRegistrato("S");

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveVerbale = lCtrlEve.ExRicercaUltimoTipoEventoByIdFascicolo(lEveVerbale);

		if (lEveVerbale == null || lEveVerbale.getIdEvento() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile eseguire il decreto di irreperibilità, inserire il Verbale Vane Ricerche");
		}

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE, e.getMessage()
					+ "Impossibile eseguire il decreto di irreperibilità, inserire almeno un avvocato.");
		}

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsterna", "" + lOption);

		// MAGISTRATO
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// AVVOCATI
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// ========================
		// Autorità Esterna
		// ========================
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		return PG_LOAD_INSERISCI_DECRETO_SOSPENSIONE; // restituisce la jsp di VIEW
	}
}