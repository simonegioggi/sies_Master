package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaRevoca
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio del provvedimento di Richiesta Revoca Beneficio della
 * sospensione condizionale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioRichiestaRevoca extends ActSIESDettaglioProvvedimento
		implements ICostantiPenaSospesa {

	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		Iterator itxOggetto = lColMotivo.iterator();
		while (itxOggetto.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
			if (lDecMod.getCode().equals(lEveMod.getEvento().getCodMotivo())) {
				setRequestAttribute("articolo", lDecMod.getFiltro());
				setRequestAttribute("motivazione", lDecMod.getDescription());
				break;
			}
		}

		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMan = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdEvento(lIdEvento);
		setRequestAttribute("AnnotazioneMan", lAnnMan);

		Vector lReati = new Vector();
		IPenaSospesa lCtrlPSosp = SIEPLookupRemote.getPenaSospesaRemote();
		if (lAnnMan != null) {
			lReati = lCtrlPSosp.ExRicercaReatiByAnnotazioneMan(lAnnMan.getIdAnnotazioneManuale());
		}
		setRequestAttribute("reati", lReati);
		setRequestAttribute("stringareati", getPopupReati(lReati));

		PenaComplessivaSanzioneSostitutivaModel dettaglioPenaComplessiva = new PenaComplessivaSanzioneSostitutivaModel();
		if (lAnnMan != null) {
			dettaglioPenaComplessiva = lCtrlPSosp
					.ExRicercaPenaComplessivaByAnnotazioneMan(lAnnMan.getIdAnnotazioneManuale());
		}
		setRequestAttribute("penacomplessiva", dettaglioPenaComplessiva);

		lModel = new DecodificheModel();
		lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
		Collection Uffi_Emi = lDecodifiche.ExRicercaDecodifiche(lModel);
		String strDescrUfficio = "";
		itxOggetto = Uffi_Emi.iterator();
		while (itxOggetto.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
			if (lAnnMan != null) {
				if (lDecMod.getCode().equals(lAnnMan.getCodTipoUfficioSiep()))
					strDescrUfficio += lDecMod.getDescription();
			}
		}
		setRequestAttribute("strDescrUfficio", strDescrUfficio);

		IComune comctrl = SICOLookupRemote.getComuneRemote();
		ComuneModel comunemod = comctrl.ExRicercaComuneByKey(lAnnMan.getCodLuogoUfficioSiep());
		setRequestAttribute("strDescrComune", comunemod.getDescrizione());

		if (!this.isRequestParameterNullObj("modifica")
				&& lEveMod.getEvento().getFlagDocumentoRegistrato() != null
				&& !lEveMod.getEvento().getFlagDocumentoRegistrato().equals("N")) {
			setRequestAttribute("modifica", "dettaglio");
		}

		return IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/DettaglioRichiestaRevoca.jsp";
	}

	/**
	 * 
	 * @param inVect
	 * @return
	 */
	private Vector getPopupReati(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = (ReatoModel) itx.next();

			if (lReato.getProgrCircostanza().intValue() == 1) {

				if (strReato.length() != 0) {

					outVect.addElement(strReato);
					strReato = "";
				}
			}

			lFlagAnnoNumero = false;
			if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("")
					&& lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("")) {
				lFlagAnnoNumero = true;
			}

			if (lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += lReato.getDescrFonte() + " ";
				if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals(""))
					strReato += lReato.getAnnoFonte();
				if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
					strReato += "/" + lReato.getNumeroFonte();
			}

			if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
				strReato += " art." + lReato.getArticolo();
			if (lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("")
					&& !lReato.getDescrSottonumerazione().equals("-"))
				strReato += " " + lReato.getDescrSottonumerazione();

			if (!lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += " " + lReato.getDescrFonte();
			}

			if (lReato.getComma() != null && !lReato.getComma().equals(""))
				strReato += " c. " + lReato.getComma();
			// ***********************************************************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			if (lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("")
					&& !lReato.getDescrCommaQualificante().equals("-"))
				strReato += " " + lReato.getDescrCommaQualificante();
			// ***********************************************************************************
			if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
				strReato += " l. " + lReato.getLettera();
			if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
				strReato += " n. " + lReato.getNumero();

			strReato += ", ";
		}

		outVect.addElement(strReato);

		return outVect;
	}

}