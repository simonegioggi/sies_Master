package siap.siep.ordinescarcerazione.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActLoadInserisciOSFuturaMemoria
 * </p>
 * <p>
 * Description: Load Inserisci Ordine Scarcerazione Futura Memoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciOSFuturaMemoria extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lControl = controllaFascicolo();
		if (lControl != null)
			return lControl;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		controllaPenaResiduaAvvocato(lFascMod.getIdFascicoloSiep());

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena. "
					+ "Impossibile eseguire l'Ordine di Scarcerazione.");

		this.isEventoNonValidato();

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvVect = null;
		try {
			lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					e.getMessage() + "Impossibile eseguire l'Ordine di Scarcerazione.");
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl
				.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPos);

		if ((lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10")
				|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07"))
				&& lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile effettuare l'Ordine di Scarcerazione per un Condannato Libero.");

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// posizione giuridica
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		// Riempimento ComboBoX
		Option lOption = null;
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().compareTo("N") == 0) {
			IPosizioneGiuridica lPosCtrlControl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPosMod = lPosCtrlControl
					.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

			if (lPosMod == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " non à stata associata una Posizione Giuridica.");

			setRequestAttribute("posizione", lPosMod);
			setRequestAttribute("detenutoAltraCausa", "");
			if (lPosMod != null)
				lDataInizioPena = lPosMod.getDataInizio();
		} else {
			setRequestAttribute("detenutoAltraCausa", "SI");
			// ** COMMENTATO CAUSA REWORK del 17.06.2003 **
			// ** il campo DATA (ALTRA_CAUSA) si trova sulla tabella ALTRA_CAUSA **
			// ** -- lDataInizioPena = lFascMod.getDataAltraCausa(); --
			if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			IAltraCausa lCtrlAltraCausa = SIEPLookupRemote.getAltraCausa();

			AltraCausaModel lAltrMod = lCtrlAltraCausa
					.ExRicercaAltraCausaByFascicolo(lFascMod.getIdFascicoloSiep());
			this.setRequestAttribute("altracausaposizionegiuridica", lAltrMod);
			setRequestAttribute("posizione", null);
		}

		// modifica relativa al tipo istituto
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) { // Altra Causa
			// modifica relativa al tipo istituto
			if (lPos.getAltraCausa().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			IAltraCausa lCtrlAltraCausa = SIEPLookupRemote.getAltraCausa();

			AltraCausaModel lAltrMod = lCtrlAltraCausa
					.ExRicercaAltraCausaByFascicolo(lFascMod.getIdFascicoloSiep());
			this.setRequestAttribute("altracausaposizionegiuridica", lAltrMod);
		}
		/*
		 * else {//Non è Altra Causa //modifica relativa al tipo istituto if
		 * (lPos.getLuogoDetenzione().getIstitutoDetenzione() != null) lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),
		 * lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto()); }--NOn viene messa da
		 * nessuna parte....
		 */

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("14")) { // Semilibertà
			setRequestAttribute("autoritaEsternaC", "" + lOption);
		}

		// fine modifica relativa al tipo istituto

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);
		setRequestAttribute("avvocati", lAvvVect);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);
		lDataInizioPena = lPenaResMod.getDataInizio();
		// lDataFinePenaM = lPenaResMod.getDataFine();
		lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		return PG_INSERISCI_ORDINE_SCARCERAZIONE; // restituisce la jsp di VIEW
	}

}