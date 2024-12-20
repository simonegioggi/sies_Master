package siap.sius.prescrizione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciPrescrizioneNew
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Prescrizione
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciPrescrizioneNew extends ActionSiap implements ICostantiPrescrizione {

	/**
	 * Azione di Inserimento del Prescrizione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		String lRetPage = null;
		Vector lPrescrizioni = new Vector();

		PrescrizioneModel lPreMod = new PrescrizioneModel();
		lPreMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lPreMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lPreMod.setDataInserimento(DateUtils.getSysDate());

		// preleva dalla request l'ID evento
		lPreMod.setEveIdEve(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		lPreMod.setCodLuogoAffidamento("-");
		lPreMod.setIdCssaCompetente(new BigDecimal("9999"));
		lPreMod.setCodLuogoAutorizzato("-");
		lPreMod.setCodProvinciaAutorizzata("-");
		lPreMod.setCodTipoPrescrizione("-");
		lPreMod.setCodUffMagistratoCompetente("-");

		// Filtro per prescrizioni

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva il cod Oggetto procedimento
		String mCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		String mCodTipoRegistro = lFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro();
		if (mCodOggettoProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");

		// Filtro per prescrizioni
		String FiltroPrescrizione = "";

		// Controllo Sanzioni Sostitutive / Conv. Pene Pecuniarie
		if (mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_AUTO_SANZ_SOSTITUTIVE)
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_SANZ_SOSTITUTIVE)
				// MEV_2023-35 si aggiunge un nuovo codice 
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_PENE_SOSTITUTIVE)
				// MEV_2023-35 - FINE
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE)) {
			FiltroPrescrizione = "ESS";
		}
		// Controllo Applicazione Misure Sicurezza / Dichiarazione Delinquenza Abitale
		if (mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_APPL_MIS_SICUREZZA)
				|| mCodOggettoProc.equals(ICostantiDepositoOrdinanzaPc.OGG_DICH_DELINQ_ABITUALE)
				|| mCodTipoRegistro.equals("S09")) {
			FiltroPrescrizione = "AMS";
		}

		// Cerca le prescrizioni in CG_REF_CODES
		List lElencoPrescrizioni = new ArrayList(DecodificheUtils.getDecodificheFiltrateByCodAltValorizzato(
				DecodificheManager.getInstance().getTipoPrescrizione(), FiltroPrescrizione));

		String codPrescrizione = "";
		String descrizione = "";

		// esegue il ciclo delle prescrizioni trovate
		Iterator itx = lElencoPrescrizioni.iterator();
		while (itx.hasNext()) {
			DecodificheModel lPrescrizioneMod = (DecodificheModel) itx.next();

			// trova il codice della prescrizione
			codPrescrizione = lPrescrizioneMod.getCode();

			// trova la descrizione della prescrizione
			descrizione = lPrescrizioneMod.getDescription() + " ";

			// controlla se il campo check è stato selezionato
			if (isRequestChecked("CAMPO_CK_" + codPrescrizione)) {
				PrescrizioneModel lPreMod1 = new PrescrizioneModel(lPreMod);

				// prepara il caricamento del codice prescrizione nel model
				lPreMod1.setCodTipoPrescrizione(codPrescrizione);

				// Controlla l'esistenza del campo
				if (!isRequestParameterNullObj("CAMPO_TESTO_" + codPrescrizione)) {
					// trova la prima occorrenza di "<?"
					int chkInizio = descrizione.indexOf("<?");
					// imposta l'indice per il campo
					int indiceCampo = -1;

					// Cicla la descrizione finchè esiste una occorrenza di "<?"
					// ed effettua il controllo dei campi
					while (chkInizio != -1) {
						// Aggiorna il valore dell'indice per il campo
						indiceCampo++;

						// Recupera i nomi campo con 4 valori (esempio: "PUCM")
						String controlloQuattro = descrizione.substring(chkInizio + 2, chkInizio + 6);

						// Recupera i nomi campo con 5 valori (esempio: "PUUDS")
						String controlloCinque = descrizione.substring(chkInizio + 2, chkInizio + 7);

						// cerca la successiva occorrenza di "<?" a partire dall'utima trovata
						chkInizio = descrizione.indexOf("<?", chkInizio + 2);

						// controlla che la stringa sia "PUCM"
						if (controlloQuattro.equals("PUCM")) {
							// esegue il controllo del campo comune
							getCodComuneByDescr(getRequestStringParameters(
									"CAMPO_TESTO_" + codPrescrizione)[indiceCampo]);
						}
						// Controllo che la stringa sia "PUUDS"
						if (controlloCinque.equals("PUUDS")) {
							// esegue il controllo per ufficio di sorveglianza
							getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameters(
									"CAMPO_TESTO_" + codPrescrizione)[indiceCampo]);
						}

					}

					// crea l'array dei campi da inserire
					String[] listaCampi = getRequestStringParameters("CAMPO_TESTO_" + codPrescrizione);

					// esegue il ciclo dell'array dei campi
					for (int i = 0; i < listaCampi.length; i++) {
						// controlla che il campo non sia vuoto o null
						if (listaCampi[i] != null && !listaCampi[i].equals("")) {

							// prepara il caricamento dei campi nel model
							if (i == 0) {
								lPreMod1.setDescrPrescrizione1(listaCampi[i]);
							} else if (i == 1) {
								lPreMod1.setDescrPrescrizione2(listaCampi[i]);
							} else if (i == 2) {
								lPreMod1.setDescrPrescrizione3(listaCampi[i]);
							}
						}
					}
				}
				// aggiunge i valori nel model
				lPrescrizioni.add(lPreMod1);
			}
		}

		// esegue l'inserimento nella tabella "PRESCRIZIONE"
		IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		// setta la risposta nella request
		/* PrescrizioneModel lPreModRet = */lCtrl.ExInserisciPrescrizioni(
				(PrescrizioneModel[]) lPrescrizioni.toArray(new PrescrizioneModel[0]), lPreMod.getEveIdEve());

		// Preparazione della pagina di destinazione,
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		// Viene ricavata l'azione successiva di default
		// è il dettaglio dell'Emissione Ordinanza.
		String lNextAction = null;
		if (!isRequestParameterNullObj("nextaction"))
			lNextAction = this.getRequestStringParameter("nextaction");

		if ((lNextAction != null) && (lNextAction.trim().length() > 1))
			lRedirectTo.setAction(lNextAction);
		else
			lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActDettaglioEmissioneOrdinanza&");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lPreMod.getEveIdEve().toString());
		lRetPage = lRedirectTo.toString();

		return lRetPage;
	}

}