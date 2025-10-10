package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * 
 * @since MEV_2019-09-SIEP
 */

public class ActLoadDettaglioSospensioneDecisioniSorv678 extends ActMisuraAlternativa implements
		ICostantiSospensione 
{
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		PenaResiduaModel lPenaResMod = super.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("nuovapenaresidua", lPenaResMod);

		PenaResiduaModel lPenValidata = super.getPenaResiduaPrecedenteValidata(lIdEvento,lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenValidata);

		// ricerca sospensione
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospMod = new SospensioneModel();
		if (lPenaResMod != null && lPenaResMod.getIdPenaResidua() != null)
			lSospMod = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenaResMod.getIdPenaResidua());
		else if (lPenValidata != null && lPenValidata.getIdPenaResidua() != null)
			lSospMod = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenValidata.getIdPenaResidua());

		setRequestAttribute("sospensione", lSospMod);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = super
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = super.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisura = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento()
				.getEveIdEvento());
		setRequestAttribute("misuraalternativa", lMisura);

		// Autorità esterna C
		AutoritaEsternaModel lAutC = null;
		String NoteAutC = null;

		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			NoteAutC = ((NotificaModel) lTable.get("AutC")).getNote();
			setRequestAttribute("NoteAutC", NoteAutC);
			setRequestAttribute("autoritaEsternaC", lAutC);
		}

		// Cssa
		String lCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
		}

		// Ufficio TDS
		String UffTDS = null;
		// MEV10-s3: aggiunta variabile e set di attributo nella richiesta
		String descrTipoUfficioTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			descrTipoUfficioTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrTipoUfficio();
			setRequestAttribute("UffTDS", UffTDS);
			setRequestAttribute("descrTipoUfficioTDS", descrTipoUfficioTDS);
		}

		// Ufficio UDS
		String UffUDS = null;
		// MEV10-s3: aggiunte variabili e set di attributi nella richiesta
		String descrTipoUfficioUDS = null;
		String codTipoUfficioUDS = null;
		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			descrTipoUfficioUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrTipoUfficio();
			codTipoUfficioUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getCodTipoUfficio();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("descrTipoUfficioUDS", descrTipoUfficioUDS);
			setRequestAttribute("codTipoUfficioUDS", codTipoUfficioUDS);
		}

		// istituto
		IstitutoDetenzioneModel lIstMod = null;
		if (lTable.get("lNotIstituto") != null) {
			lIstMod = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione();
			setRequestAttribute("lIstMod", lIstMod);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisura.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		return PG_LOAD_DETTAGLIO_SOSP_DECISIONI_SORVEGLIANZA_678;
	}
}