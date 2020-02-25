package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActModificaParteUdienza
 * </p>
 * <p>
 * Description: Classe Azione di modifica della Parte Udienza (Offesa/Civile - Fisica/Giuridica)
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaDifensore extends ActionSiap implements ICostantiPartiUdienza {

	public String processRequest() throws Exception {

		// Codice del tipo parte da inserire (O=Offesa, C=Civile)
		String codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);

		// Identificativo della parte
		String lIdSoggetto = getRequestStringParameter(CAMPO_ID_SOGGETTO);

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);

		// AnagraficaPartiUdienzaModel lAnagraficaParteMod = new AnagraficaPartiUdienzaModel();
		// lAnagraficaParteMod.setIdSoggetto(new BigDecimal(lIdSoggetto));

		IPartiUdienza lCtrlPU = SIGELookupRemote.getPartiUdienzaRemote();
		AnagraficaPartiUdienzaModel lAnagraficaParteMod = lCtrlPU
				.ExRicercaParteUdienzaByKey(new BigDecimal(lIdSoggetto));

		ArrayList lNotifiche = letturaNotifiche();

		// Flag Domiciliato presso Difensore
		ResidenzaModel residenzaMod = null;
		if (lAnagraficaParteMod.getResidenza() != null && !"".equals(lAnagraficaParteMod.getResidenza().toString())) {
			residenzaMod = lAnagraficaParteMod.getResidenza();
			if (!isRequestParameterNullObj(CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE)) {
				residenzaMod.setFlgDomicilioDifensore("S");
			} else {
				residenzaMod.setFlgDomicilioDifensore("N");
			}
		} else {
			residenzaMod = new ResidenzaModel();
			residenzaMod.setIdParteUdienza(new BigDecimal(lIdSoggetto));

			if (residenzaMod.getIdResidenza() != null) {
				residenzaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				residenzaMod.setDataAggiornamento(DateUtils.getSysDate());
				residenzaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			} else {
				residenzaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				residenzaMod.setDataInserimento(DateUtils.getSysDate());
				residenzaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			}

		}

		lAnagraficaParteMod.setResidenza(residenzaMod);

		// Convocazione Udienza
		lAnagraficaParteMod.setFlagConvUdienza(getRequestStringParameter(CAMPO_CONVOCAZIONE_UDIENZA));

		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		lCtrl.ExModificaParteUdienza(lAnagraficaParteMod, lNotifiche, new BigDecimal(lIdEventoUdienza));

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzaparti.action.ActDettaglioParteUdienza");
		lRedir.setParameter(CAMPO_ID_SOGGETTO, lIdSoggetto);
		lRedir.setParameter(CAMPO_COD_TIPO_PART, codTipoParte);

		// Torna alla pagina di dettaglio parti senza aggiungerla allo stack
		lRedir.setParameter(IWebConstants.LINK_RITORNO, "10");

		return lRedir.toString();

	}

	protected ArrayList letturaNotifiche() throws Exception {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Flag S.N.T. (Sistema Notifiche Telematiche)
		// Quando si seleziona il flag non vengono visualizzati
		// i campi "Autorità Destinazione" e "Sede"
		// pertanto in fase di inserimento della Notifica
		// non viene valorizzato il campo AUT_EST_ID_AUTORITA_ESTERNA
		// sulla tabella NOTIFICA
		String destinatarioflagSNT[] = null;
		if (!isRequestParameterNullObj(CAMPO_FLAG_SNT)) {
			destinatarioflagSNT = getRequestStringParameters(CAMPO_FLAG_SNT);
		}
		String lSedi[] = null;
		if (!isRequestParameterNullObj(CAMPO_SEDE)) {
			lSedi = getRequestStringParameters(CAMPO_SEDE);
		}
		String lDestinatari[] = null;
		if (!isRequestParameterNullObj(CAMPO_COD_DESTINATARIO)) {
			lDestinatari = getRequestStringParameters(CAMPO_COD_DESTINATARIO);
		}
		String lAvvocato[] = null;
		if (!isRequestParameterNullObj(CAMPO_COD_AVVOCATO)) {
			lAvvocato = getRequestStringParameters(CAMPO_COD_AVVOCATO);
		}

		// Notifica al Soggetto
		String lDestinatarioSog = "";
		String lSedeSog = "";
		String lIndirizzoSog = "";

		if (!isRequestParameterNullObj(CAMPO_COD_IST_DETENZIONE)) {
			lDestinatarioSog = getRequestStringParameter(CAMPO_COD_IST_DETENZIONE);
		}

		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_DETENZIONE)) {
			lSedeSog = getRequestStringParameter(CAMPO_COD_LUOGO_DETENZIONE);
		}

		if (!isRequestParameterNullObj(CAMPO_INDIRIZZO_DETENZIONE)) {
			lIndirizzoSog = getRequestStringParameter(CAMPO_INDIRIZZO_DETENZIONE);
		}

		// Array per le notifiche.
		ArrayList lNotifiche = new ArrayList();

		// Avvocati
		int lSize = 0;
		if (lDestinatari != null) {
			lSize = lDestinatari.length;
		}

		for (int x = 0; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
				lNotifica.setDataInvio(DateUtils.getSysDate());
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setIdParteUdienza(new BigDecimal(getRequestStringParameter(CAMPO_ID_SOGGETTO)));
				lNotifica.setEveIdEvento(new BigDecimal(
						getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)));
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				}

				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			} else {
				// caso in cui è stato selezionato il flag SNT
				// pertanto non è presente l'Autorità Esterna
				if (destinatarioflagSNT != null) {
					NotificaModel lNotifica = new NotificaModel();
					lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
					lNotifica.setDataInvio(DateUtils.getSysDate());
					lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
					lNotifica.setDataInserimento(DateUtils.getSysDate());
					lNotifica.setCodUfficioInserimento(lCodiceUfficio);
					lNotifica.setCodEsito("-");
					lNotifica.setUffCodUfficio("-");
					lNotifica.setIdParteUdienza(new BigDecimal(getRequestStringParameter(CAMPO_ID_SOGGETTO)));
					lNotifica.setEveIdEvento(new BigDecimal(
							getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)));
					if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
						lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
						BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
						lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
						AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
						lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
						lNotifica.setAvvSige(lAvvSige);
					}

					// Aggiunge il model delle notifiche al vettore.
					lNotifiche.add(lNotifica);
				}
			}

		}

		// Soggetto con autorita' esterna
		if (!Utils.isNullObj(lDestinatarioSog) && !Utils.isNullObj(lSedeSog)) {
			if (!lDestinatarioSog.equals("-") && !lSedeSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedeSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
				lNotifica.setDataInvio(DateUtils.getSysDate());
				lNotifica.setNote(lIndirizzoSog);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setIdParteUdienza(new BigDecimal(getRequestStringParameter(CAMPO_ID_SOGGETTO)));
				lNotifica.setEveIdEvento(new BigDecimal(
						getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)));

				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatarioSog);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.

				lNotifiche.add(lNotifica);
			}
		}

		return lNotifiche;
	}

}