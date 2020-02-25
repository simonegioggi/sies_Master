package siap.sico.evento.action;

/**
* <p>Title: ActDettaglioDocumento</p>
* <p>Description: Classe Action per la load dettaglio generica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioDocumento extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EVENTO
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// ARCHIVIAZIONE
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneByIdEvento(lIdEvento);

		if (lEveMod != null && lEveMod.getEvento() != null
				&& lEveMod.getEvento().getCodTipoProvvedimento() != null
				&& (lEveMod.getEvento().getCodTipoProvvedimento().equals("20") ||
				// Nuovo codice. Luigi 17-10-2005
						lEveMod.getEvento().getCodTipoProvvedimento().equals("25")
						|| lEveMod.getEvento().getCodTipoProvvedimento().equals("21"))) {
			lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lIdEvento);
		} else {
			lArcMod = lCtrlArc.ExRicercaArchiviazioneByIdEvento(lIdEvento);
		}

		setRequestAttribute("archiviazione", lArcMod);

		// misura alternativa
		// prima l'evento ordinanza o decreto che serve per cercare la misura alternativa
		EventoModel lEveModSorv = new EventoModel();
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			lEveModSorv = lCtrl.ExRicercaEventoByKey(lEveMod.getEvento().getEveIdEvento());

			MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
			if (lEveModSorv != null && lEveModSorv.getIdEvento() != null) {
				IMisuraAlternativa lCrtlMis = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisMod = lCrtlMis.ExRicercaMisuraAlternativaByIdEvento(lEveModSorv.getIdEvento());
				setRequestAttribute("misuraalternativa", lMisMod);
			}

			// Sede Ufficio di Sorveglianza
			if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				UfficioModel lUffEmiMod = new UfficioModel();
				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisMod.getChiaveUfficioFascicoloSius());
				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			}
		}

		if (lEveModSorv != null && "03".equalsIgnoreCase(lEveModSorv.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep
					.ExRicercaDepositoOrdinanzaPcByEvento(lEveModSorv.getIdEvento());
			setRequestAttribute("DepositoOrdinanzaPc", lDepOrdMod);
		} else if (lEveModSorv != null && "02".equalsIgnoreCase(lEveModSorv.getCodTipoProvvedimento())) {
			// RICERCA DEPOSITO_DECRETO
			IDepositoDecreto lCtrlDec = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDepDecMod = lCtrlDec
					.ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto(lEveModSorv.getIdEvento());
			setRequestAttribute("DepositoDecreto", lDepDecMod);
		}

		// RICERCA PERIODI LICENZA
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lLicenzePeriodi = lCtrlLib.ExRicercaLicenzeLibanticipataByEve(lIdEvento);

		if (!lLicenzePeriodi.isEmpty()) {
			LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel) lLicenzePeriodi
					.firstElement();
			String lCodUffEmi = lLicenzaPeriodiModel.getLicenza().getCodUfficioEmittente();

			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);

			lLicenzaPeriodiModel.getLicenza().setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		}

		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(lIdEvento);

		setRequestAttribute("decretoordinanza", lDecOrd);
		String lFlagDec = (lDecOrd == null ? "N" : "S");
		setRequestAttribute("flagdecretoordinanza", lFlagDec);

		// ANNOTAZIONI MANUALI

		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();

		AnnotazioneManualeModel lAnnGE = null;
		AnnotazioneManualeModel lAnnGEConTipoAnn = null;
		EventoModel lEve04 = new EventoModel();

		lAnnGE = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdEvento(lIdEvento);
		if (lAnnGE != null) {
			AnnotazioneOrdinanzaModel lAnnOrdMod = new AnnotazioneOrdinanzaModel(lAnnGE, lEveMod.getEvento());

			setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdMod);

			lEve04.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
			lEve04.setDataInserimento(lEveMod.getEvento().getDataInserimento());
			lEve04.setCodTipoProvvedimento("04");
			lEve04 = lCtrl.ExRicercaEventoByDataInserimentoUguale(lEve04);

			if (lEve04 != null && lEve04.getIdEvento() != null) {
				lAnnGEConTipoAnn = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdEvento(lEve04.getIdEvento());

				AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
				if (lAnnMod != null && lAnnMod.getIdAnnotazioneManuale() != null) {
					lAnnMod.setFasSieIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
					lAnnMod.setCodTipoAnnotazione(lAnnGEConTipoAnn.getCodTipoAnnotazione());
					lAnnMod.setFlagAppProvvisoria("-"); // NON RICHIESTE
					lAnnMod.setFlagValidato("S");

					Vector lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioneManualeGenerico(lAnnMod);
					setRequestAttribute("ListaAnnotazioni", lListAnnMan);
				}
			}
		}

		// POSIZIONE GIURIDICA LEGATA ALL'EVENTO
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento(lIdEvento);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO LEGATO ALL'EVENTO
		MagistratoModel lMag = lEveMod.getMagistrato();

		setRequestAttribute("magistrato", lMag);

		// PENA RESIDUA LEGATA ALL'EVENTO
		IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrlp.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		setRequestAttribute("penaresidua", llPenMod);

		// NOTIFICHE
		List lListAutorita = new ArrayList();
		List IListIstituti = new ArrayList();
		List lListUffici = new ArrayList();
		List lListCssa = new ArrayList();
		List lListAvvocatiSiep = new ArrayList();
		List lListAvvocatiSius = new ArrayList();

		NotificaModel[] lNotifiche = lEveMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			// Autorita Esterne
			if (lNotifiche[i].getAutEstIdAutoritaEsterna() != null) {
				lListAutorita.add(lNotifiche[i]);
			}

			// Istituto
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
					&& !lNotifiche[i].getIstDetIdIstitutoDetenzione().equals("")) {
				IListIstituti.add(lNotifiche[i]);
			}

			// Uffici
			if (lNotifiche[i].getUffCodUfficio() != null) {
				lListUffici.add(lNotifiche[i]);
			}

			// Cssa
			if (lNotifiche[i].getCssIdCssa() != null) {
				lListCssa.add(lNotifiche[i]);
			}

			// Avvocati Siep
			if (lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() != null) {
				lListAvvocatiSiep.add(lNotifiche[i]);
			}

			// Avvocati Sius
			if (lNotifiche[i].getAvvIdAvvocatoFascicoloSius() != null) {
				lListAvvocatiSius.add(lNotifiche[i]);
			}
		}

		setRequestAttribute("listaAutorita", lListAutorita);
		setRequestAttribute("listaIstituti", IListIstituti);
		setRequestAttribute("listaUffici", lListUffici);
		setRequestAttribute("listaCssa", lListCssa);
		setRequestAttribute("listaAvvSiep", lListAvvocatiSiep);
		setRequestAttribute("listaAvvSius", lListAvvocatiSius);

		return PG_LOAD_DETTAGLIO_DOCUMENTO;
	}

}