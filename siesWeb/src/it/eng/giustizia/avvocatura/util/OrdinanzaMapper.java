/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.CONVERSIONEPENEPECUNIARIETYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATIORDINANZA;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DESTINATARIOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.ESITITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.MISURASICUREZZATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiIndultino;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiLicenza;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRevoca;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRicoveri;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiScomputo;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.PERIODILIBERTAANTICIPATATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.PERIODITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.PRESCRIZIONETYPE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class OrdinanzaMapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per la mappatura dei dati dell'ordinanza
	 * 
	 * @param oetpm
	 * @param isOrdinanzaRevocata
	 * @param oetpmRev
	 * @param fgpm
	 * @return DATIORDINANZA
	 */
	public static DATIORDINANZA mapDatiOrdinanza(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			PeriodoAltraMisuraModel pamm, MisuraSicurezzaModel msm, Vector licenzePeriodi,
			boolean isOrdinanzaRevocata, OrdinanzaEventoTenoriPrescrizioniModel oetpmRev,
			FascicoloGPModel fgpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiOrdinanza");

		// instanzio ed inizializzo un oggetto di tipo "DATIORDINANZA"
		DATIORDINANZA dr = new DATIORDINANZA();
		DepositoOrdinanzaPcModel dopm = oetpm.getOrdinanza();
		EventoModel em = oetpm.getEvento();
		// aggiungo elementi al tipo "DATIORDINANZA"
		String descrTipoOrdinanza = dopm.getDescrTipoOrdinanza();
		if (descrTipoOrdinanza != null)
			descrTipoOrdinanza = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getTipoOrdinanza(), dopm.getCodTipoOrdinanza());
		dr.setTipoOrdinanza(dopm.getCodTipoOrdinanza() + "#" + descrTipoOrdinanza);
		if (dopm.getAnnoS3() != null)
			dr.setAnnoOrdinanza(dopm.getAnnoS3().toBigInteger());
		else
			dr.setAnnoOrdinanza(null);
		if (dopm.getNumS3() != null)
			dr.setNumeroOrdinanza(dopm.getNumS3().toBigInteger());
		else
			dr.setNumeroOrdinanza(null);
		dr.setDataEmissione(Mapper.creaDataTypeOrdinanza(em.getDataEmissione()));
		dr.setDataDepositoCancelleria(Mapper.creaDataTypeOrdinanza(dopm.getDataDeposito()));
		// da decodificare A (Annullato), S (Validato), N (Da Validare)
		dr.setStatoProvvedimento(em.getFlagDocumentoRegistrato());

		if (PropertyUtil.isPresent(em) && PropertyUtil.isPresent(em.getEveIdEventoRevoca())) {
			// lo stato del provv. diventa Revocato 'R'
			dr.setStatoProvvedimento("R");
		}
		dr.setTotaleGiorniLibertaAnticipata(dopm.getNumGiorniLibanticipata() != null ? dopm
				.getNumGiorniLibanticipata().toBigInteger() : null);
		dr.setDescrDecisione(dopm.getUlterioreDescrizione());
		dr.setTotaleGiorniRiduzionePena(dopm.getNumGiorniRiduzionePena() != null ? dopm
				.getNumGiorniRiduzionePena().toBigInteger() : null);
		dr.setSommaRisarcimento(dopm.getSommaRisarcimento());
		dr.setDescrUfficioMagistratoCompetente(dopm.getDescrUfficioMagistratoComp());
		dr.setUfficioSorveglianzaCompetente(dopm.getCodUfficioMagistratoComp());
		dr.setDispositivo(dopm.getCodNaturaProvvedimento());
		
		if (oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RICHIESTA_OTTEMPERANZA) == 0 ) {
			if ("S".equals(dopm.getFlagNominaComActa())) {
				dr.setFlagNominaCommissarioActa("SI");
			}else dr.setFlagNominaCommissarioActa("NO");
			dr.setDescrCommissarioActa(dopm.getDescrCommActa());
		}
		
		if (oetpm.getOrdinanza().getCodTipoOrdinanza()
				.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0
				|| oetpm.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
				|| oetpm.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(ICostantiDepositoOrdinanzaPc.ORD_SOSPENSIONE_ESECUZIONE_MS) == 0) {
			if (oetpm.getEvento().getCodMotivo() != null
					&& (oetpm.getEvento().getCodMotivo().equals("2670")
							|| oetpm.getEvento().getCodMotivo().equals("2440")
							|| oetpm.getEvento().getCodMotivo().equals("2441") || oetpm.getEvento()
							.getCodMotivo().equals("2442"))
					&& oetpm.getOrdinanza().getDataFineMisura() != null) {
				dr.setDataCessazioneMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza()
						.getDataFineMisura()));
			}

			if (oetpm.getEvento().getCodMotivo() != null
					&& (oetpm.getEvento().getCodMotivo().equals("2610") || oetpm.getEvento().getCodMotivo()
							.equals("2611"))) {
				// valorizzo la data rinvio
				if (oetpm.getOrdinanza().getDataInizioPeriodo() != null) {
					dr.setDataInizioRinvio(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza()
							.getDataInizioPeriodo()));
				}
				// valorizzo data fine rinvio
				if (oetpm.getOrdinanza().getDataFineMisura() != null) {
					dr.setDataFineRinvio(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza()
							.getDataFineMisura()));
				}
				if ((oetpm.getOrdinanza().getSospensioneAASS() != null && oetpm.getOrdinanza()
						.getSospensioneAASS().intValue() > 0)
						|| (oetpm.getOrdinanza().getSospensioneMMSS() != null && oetpm.getOrdinanza()
								.getSospensioneMMSS().intValue() > 0)
						|| (oetpm.getOrdinanza().getSospensioneGGSS() != null && oetpm.getOrdinanza()
								.getSospensioneGGSS().intValue() > 0)) {

					dr.setDurataSospensione(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
							.getSospensioneGGSS(), oetpm.getOrdinanza().getSospensioneMMSS(), oetpm
							.getOrdinanza().getSospensioneAASS()));
				}
			}
			// controlli per valorizzae la data di sospensione
			if (oetpm.getEvento().getCodMotivo() != null
					&& (oetpm.getEvento().getCodMotivo().equals("2410")
							|| oetpm.getEvento().getCodMotivo().equals("2411") || oetpm.getEvento()
							.getCodMotivo().equals("2412"))) {
				if (oetpm.getOrdinanza().getDataSospensioneSS() != null) {
					dr.setDataSospensioneOrdinanza(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza()
							.getDataSospensioneSS()));
				}
			}

			// setto l'element durata altra misura
			if (pamm != null && pamm.getIdPeriodoAltraMisura() != null) {
				dr.setDurataResiduaAltraMisura(Mapper.creaDurataTypeOrdinanza(pamm.getResiduaGG(),
						pamm.getResiduaMM(), pamm.getResiduaAA()));
			}
			// setto l'element DescrUfficioMagistratoComp
			if (dopm.getDescrUfficioMagistratoComp() != null
					&& !dopm.getDescrUfficioMagistratoComp().equals("-")) {
				dr.setDescrUfficioMagistratoCompetente(dopm.getDescrUfficioMagistratoComp());
			}
			// setto l'element DescrUffTdsConcessoRiduzione
			if (dopm.getDescrUffTdsConcessoRiduzione() != null
					&& !dopm.getDescrUffTdsConcessoRiduzione().equals("-")) {
				dr.setUfficioConcessioneRiduzione(dopm.getDescrUffTdsConcessoRiduzione());
			}
			// setto l'elemento motivoRichiesta
			if (PropertyUtil.isPresent(dopm.getCodNaturaProvvedimento())) {
				dr.setMotivoRichiesta(dopm.getCodNaturaProvvedimento());
			}
			// setto l'elemento ulteriore descrizione ordinanza
			if (PropertyUtil.isPresent(dopm.getUlterioreDescrizione())) {
				dr.setUlterioreDescrizioneOrdinanza(dopm.getUlterioreDescrizione());
			}
			// setto l'elemento codi tipo registro 
			if (PropertyUtil.isPresent(fgpm)
					&& PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel())
					&& PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro())) {
				dr.setCodTipoRegistro(fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro());
			} else
				dr.setCodTipoRegistro(null);
		}

		// motivazioni ordinanza
		if ((dopm.getCodTipoOrdinanza() != null)
				&& (dopm.getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI) == 0)
				&& (dopm.getCodNaturaProvvedimento() != null)) {
			dr.setMotivazioni(dopm.getCodNaturaProvvedimento());
		}
		// setto l'element dataDecorrenzaMisura
		if (dopm.getCodTipoOrdinanza() != null
				&& dopm.getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0) {
			if (msm != null && msm.getDataDecorrenza() != null) {
				dr.setDataDecorrenzaMisuraSicurezza(Mapper.creaDataTypeOrdinanza(msm.getDataDecorrenza()));
			}
		}

		TenoreModel[] tenori = oetpm.getTenori();
		if (tenori != null && tenori.length > 0) {

			if (dopm.getCodTipoOrdinanza() != null
					&& dopm.getCodTipoOrdinanza().equals(
							ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA)
					&& ("0133".equals(oetpm.getTenori()[0].getCodEsitoTenore())
							|| "0134".equals(oetpm.getTenori()[0].getCodEsitoTenore()) || "0053".equals(oetpm
							.getTenori()[0].getCodEsitoTenore()))) {
				dr.setDataDecorrenzaMisuraSicurezza(Mapper.creaDataTypeOrdinanza(msm.getDataDecorrenza()));
				dr.setDurataMisuraSicurezza(Mapper.creaDurataTypeOrdinanza(msm.getNumGiorni(),
						msm.getNumMesi(), msm.getNumAnni()));
			}

			for (int x = 0; x < tenori.length; x++) {

				// Totale giorni per L.A. SPECIALE
				if (tenori[x].getCodOggettoTenore().equals("2131") || // L.A. Speciale
						tenori[x].getCodOggettoTenore().equals("1013") || // Reclamo su L.A. Speciale
						tenori[x].getCodOggettoTenore().equals("0620") || // Revoca su L.A. Speciale TDS
						tenori[x].getCodOggettoTenore().equals("2136")) // Revoca su L.A. Speciale UDS
				{
					int GiorniLS = 0;
					Iterator Itrx = licenzePeriodi.iterator();
					while (Itrx.hasNext()) {
						LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx
								.next();
						if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
							if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LS")) {
								GiorniLS = GiorniLS + lLicMod.getLicenza().getNumeroGiorni().intValue();
							}
						}
					}
					if (GiorniLS > 0) {
						BigInteger ggLS = new BigInteger(Integer.toString(GiorniLS));
						dr.setTotaleGiorniLibertaAnticipataSpeciale(ggLS);
					}
				}
				// Totale giorni per L.A. INTEGRAZIONE
				else if (tenori[x].getCodOggettoTenore().equals("2132") || // Ordinanza L.A. Integrazione
						tenori[x].getCodOggettoTenore().equals("1014") || // Ordinanza Reclamo L.A.
																			// Integrazione
						tenori[x].getCodOggettoTenore().equals("0621") || // Ordinanza Revoca L.A.
																			// Integrazione TDS
						tenori[x].getCodOggettoTenore().equals("2137")) // Ordinanza Revoca L.A. Integrazione
																		// UDS
				{
					int GiorniLI = 0;
					Iterator Itrx = licenzePeriodi.iterator();
					while (Itrx.hasNext()) {
						LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx
								.next();
						if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
							if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LI")) {
								GiorniLI = GiorniLI + lLicMod.getLicenza().getNumeroGiorni().intValue();
							}
							if (GiorniLI > 0) {
								BigInteger ggLI = new BigInteger(Integer.toString(GiorniLI));
								dr.setTotaleGiorniLibertaAnticipataIntegrazione(ggLI);
							}
						}
					}
				}// CHIUSURA ELSE LI
					// Totale giorni per L.A.normale
				else if (tenori[x].getCodOggettoTenore().equals("2130") || // Ordinanza L.A.
						tenori[x].getCodOggettoTenore().equals("0113") || // Ordinanza Reclamo L.A.
						tenori[x].getCodOggettoTenore().equals("0028") || // Ordinanza revoca L.A. TDS
						tenori[x].getCodOggettoTenore().equals("2135")) // Ordinanza Revoca L.A. UDS
				{
					int GiorniLA = 0;
					Iterator Itrx = licenzePeriodi.iterator();
					while (Itrx.hasNext()) {
						LicenzaPeriodiLibAnticipataModel lLicMod = (LicenzaPeriodiLibAnticipataModel) Itrx
								.next();
						if (lLicMod.getLicenza().getDescrStatoPermesso() != null) {
							if (lLicMod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LA")) {
								GiorniLA = GiorniLA + lLicMod.getLicenza().getNumeroGiorni().intValue();
							}
						} else {
							GiorniLA = GiorniLA + lLicMod.getLicenza().getNumeroGiorni().intValue();
						}
					}
					if (GiorniLA > 0) {
						BigInteger ggLA = new BigInteger(Integer.toString(GiorniLA));
						dr.setTotaleGiorniLibertaAnticipataNormale(ggLA);
					}
				}

				if ((dopm != null)
						&& (dopm.getCodTipoOrdinanza() != null)
						&& (dopm.getCodTipoOrdinanza()
								.compareTo(ICostantiDepositoOrdinanzaPc.RIMESSIONE_ATTI)) == 0) {
					dr.setOggettoProcedimento(dopm.getOggettoProcedimento());
				}
			} // chiusura for su tenori

		}

		if (isOrdinanzaRevocata) {
			// instanzio ed inizializzo un oggetto di tipo "DATIORDINANZA"
			DATIORDINANZA dor = new DATIORDINANZA();
			DepositoOrdinanzaPcModel dopmRev = oetpmRev.getOrdinanza();
			EventoModel emRev = oetpmRev.getEvento();
			dor.setAnnoOrdinanza(dopmRev.getAnnoS3().toBigInteger());
			dor.setDataDepositoCancelleria(Mapper.creaDataTypeOrdinanza(dopmRev.getDataDeposito()));
			dor.setDataEmissione(Mapper.creaDataTypeOrdinanza(emRev.getDataEmissione()));
			dor.setTipoOrdinanza(dopmRev.getDescrTipoOrdinanza());
			dor.setNumeroOrdinanza(dopmRev.getNumS3().toBigInteger());
			// da decodificare A (Annullato), S (Validato), N (Da Validare)
			dor.setStatoProvvedimento(emRev.getFlagDocumentoRegistrato());
			if (PropertyUtil.isPresent(em) && PropertyUtil.isPresent(em.getEveIdEventoRevoca())) {
				// lo stato del provv. diventa Revocato 'R'
				dor.setStatoProvvedimento("R");
			}
		}

		// valore di ritorno
		return dr;
	}

	/**
	 * Metodo per la mappatura dei dati del procedimento
	 * 
	 * @param fgpm
	 * @param fspm
	 * @param mrm
	 * @param isOrdinanzaRevocata
	 * @param fgpmRev
	 * @return DATIRIEPILOGOPROCEDIMENTO
	 */
	public static DATIRIEPILOGOPROCEDIMENTO mapDatiRiepilogoProcedimento(FascicoloGPModel fgpm,
			FascicoloSiepModel fspm, MagistratoRelatoreModel mrm, boolean isOrdinanzaRevocata,
			FascicoloGPModel fgpmRev) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRiepilogoProcedimento");

		// instanzio ed inizializzo un oggetto di tipo "DATIRIEPILOGOPROCEDIMENTO"
		DATIRIEPILOGOPROCEDIMENTO drp = new DATIRIEPILOGOPROCEDIMENTO();
		// dati del soggetto
		SoggettoModel sm = fgpm.getFascicoloSiusModel().getSoggetto();
		// dati del fascicolo SIUS
		FascicoloSiusModel fssm = fgpm.getFascicoloSiusModel();
		// dati del magistrato
		MagistratoModel mm = mrm.getMagistrato();
		// dati del procedimento
		GeneraleProcedimentoModel gpm = fgpm.getGeneraleProcedimentoModel();

		if (fspm != null) {
			if (fspm.getChiaveAnno() != null)
				drp.setAnnoFascicoloSIEP(fspm.getChiaveAnno().toBigInteger());
			if (fspm.getChiaveProgr() != null)
				drp.setNumeroFascicoloSIEP(fspm.getChiaveProgr().toBigInteger());
			String descrTipoUfficio = fspm.getDescrTipoUfficio();
			String descrComuneUfficio = fspm.getDescrComuneUfficio();
			String ufficiofascicoloSIEP = descrTipoUfficio
					+ ((PropertyUtil.isPresent(descrComuneUfficio)) ? " di " + descrComuneUfficio : "");
			drp.setUfficioFascicoloSIEP(ufficiofascicoloSIEP);
			drp.setDataFascicoloSIEP(Mapper.creaDataTypeOrdinanza(fspm.getDataIscrizione()));
		} else {
			drp.setAnnoFascicoloSIEP(null);
			drp.setNumeroFascicoloSIEP(null);
			drp.setUfficioFascicoloSIEP(null);
			drp.setDataFascicoloSIEP(null);
		}

		// SOGGETTO
		drp.setCodiceProvincia(sm.getCodProvinciaNascita());
		drp.setCognomeSoggetto(sm.getCognome());
		drp.setDataNascita(Mapper.creaDataTypeOrdinanza(sm.getDataNascita()));
		String luogoNascita = sm.getDescrComuneNascita();
		if (!PropertyUtil.isPresent(sm.getCodProvinciaNascita()) || "-".equals(sm.getCodProvinciaNascita()))
			luogoNascita = sm.getDescrStatoNascita();
		drp.setLuogoNascita(luogoNascita);
		drp.setNomeSoggetto(sm.getNome());
		drp.setSesso(sm.getSesso());
		if (sm.getEtaPresuntaAnni() != null)
			drp.setEtaPresuntaAnni(sm.getEtaPresuntaAnni().toBigInteger());
		else
			drp.setEtaPresuntaAnni(null);
		if (sm.getEtaPresuntaMesi() != null)
			drp.setEtaPresuntaMesi(sm.getEtaPresuntaMesi().toBigInteger());
		else
			drp.setEtaPresuntaMesi(null);

		if (fssm.getChiaveProgr() != null)
			drp.setNumeroProcedimentoSIUS(fssm.getChiaveProgr().toBigInteger());
		else
			drp.setNumeroProcedimentoSIUS(null);
		if (fssm.getChiaveAnno() != null)
			drp.setAnnoProcedimentoSIUS(fssm.getChiaveAnno().toBigInteger());
		else
			drp.setAnnoProcedimentoSIUS(null);

		drp.setCognomeMagistratoRelatore(mm.getCognome());
		drp.setNomeMagistratoRelatore(mm.getNome());

		drp.setDataUdienza(Mapper.creaDataTypeOrdinanza(gpm.getDataCameraConsiglio()));
		drp.setOggettoProcedimentoSIUS(gpm.getDescrOggettoProcedimento());
        drp.setCodiceStatoFascicolo(fssm.getCodStatoFascicolo());
        drp.setDescrStatoFascicolo(fssm.getDescrStatoFascicolo());        
        if(PropertyUtil.isPresent(fgpm.getUdiPro())){
        	drp.setFlagRinviata(fgpm.getUdiPro().getFlagRinviata());
        } 
		// if (isOrdinanzaRevocata) {
		// // dati del soggetto
		// SoggettoModel smRev = fgpmRev.getFascicoloSiusModel().getSoggetto();
		// // dati del fascicolo SIUS
		// FascicoloSiusModel fssmRev = fgpmRev.getFascicoloSiusModel();
		// // dati del procedimento
		// GeneraleProcedimentoModel gpmRev = fgpmRev.getGeneraleProcedimentoModel();
		// // SOGGETTO
		// drp.setCodiceProvincia(smRev.getCodProvinciaNascita());
		// drp.setCognomeSoggetto(smRev.getCognome());
		// drp.setDataNascita(Mapper.creaDataTypeOrdinanza(smRev.getDataNascita()));
		// String luogoNascitaRev = smRev.getDescrComuneNascita();
		// if (!PropertyUtil.isPresent(smRev.getCodProvinciaNascita()) ||
		// "-".equals(smRev.getCodProvinciaNascita()))
		// luogoNascitaRev = smRev.getDescrStatoNascita();
		// drp.setLuogoNascita(luogoNascitaRev);
		// drp.setNomeSoggetto(smRev.getNome());
		// drp.setSesso(smRev.getSesso());
		// if (smRev.getEtaPresuntaAnni() != null)
		// drp.setEtaPresuntaAnni(smRev.getEtaPresuntaAnni().toBigInteger());
		// else
		// drp.setEtaPresuntaAnni(null);
		// if (smRev.getEtaPresuntaMesi() != null)
		// drp.setEtaPresuntaMesi(smRev.getEtaPresuntaMesi().toBigInteger());
		// else
		// drp.setEtaPresuntaMesi(null);
		//
		// if (fssmRev.getChiaveProgr() != null)
		// drp.setNumeroProcedimentoSIUS(fssmRev.getChiaveProgr().toBigInteger());
		// else
		// drp.setNumeroProcedimentoSIUS(null);
		// if (fssmRev.getChiaveAnno() != null)
		// drp.setAnnoProcedimentoSIUS(fssmRev.getChiaveAnno().toBigInteger());
		// else
		// drp.setAnnoProcedimentoSIUS(null);
		//
		// drp.setDataUdienza(Mapper.creaDataTypeOrdinanza(gpmRev.getDataCameraConsiglio()));
		// drp.setOggettoProcedimentoSIUS(gpmRev.getDescrOggettoProcedimento());
		// }

		// valore di ritorno
		return drp;
	}

	/**
	 * Metodo per la mappatura dei dati dei destinatari
	 * 
	 * @param destinatari
	 * @return List<DESTINATARIOTYPE>
	 */
	public static List<DESTINATARIOTYPE> mapDestinatari(Vector destinatari) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDestinatari");

		// instanzio un oggetto di tipo "ArrayList"
		List<DESTINATARIOTYPE> ldt = null;
		if (PropertyUtil.isPresent(destinatari)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			ldt = new ArrayList<DESTINATARIOTYPE>(destinatari.size());
			for (int i = 0; i < destinatari.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "NotificaModel"
				NotificaModel nm = (NotificaModel) destinatari.get(i);
				// instanzio ed inizializzo un oggetto di tipo "DESTINATARIOTYPE"
				DESTINATARIOTYPE dt = new DESTINATARIOTYPE();
				if (nm.getAvvSiep() != null && nm.getAvvSiep().getAvvocato() != null)
					dt.setAvvocatoSIEP(nm.getAvvSiep().getAvvocato().getCognome() + " "
							+ nm.getAvvSius().getAvvocato().getNome());
				else
					dt.setAvvocatoSIEP(null);
				if (nm.getAvvSius() != null && nm.getAvvSius().getAvvocato() != null)
					dt.setAvvocatoSIUS(nm.getAvvSius().getAvvocato().getCognome() + " "
							+ nm.getAvvSius().getAvvocato().getNome());
				else
					dt.setAvvocatoSIUS(null);
				if (nm.getCSSA() != null) {
					dt.setComuneCSSA(nm.getCSSA().getComune());
					dt.setIndirizzoCSSA(nm.getCSSA().getIndirizzo());
				} else {
					dt.setComuneCSSA(null);
					dt.setIndirizzoCSSA(null);
				}
				if (nm.getIstitutoDetenzione() != null) {
					dt.setDescrComuneIstitutoDeten(nm.getIstitutoDetenzione().getDescrComune());
					dt.setDescrTipoIstitutoDeten(nm.getIstitutoDetenzione().getDescrTipoIstituto());
				} else {
					dt.setDescrComuneIstitutoDeten(null);
					dt.setDescrTipoIstitutoDeten(null);
				}
				if (nm.getUfficio() != null) {
					dt.setDescrComuneUfficio(nm.getUfficio().getDescrComune());
					dt.setDescrTipoUfficio(nm.getUfficio().getDescrTipoUfficio());
				} else {
					dt.setDescrComuneUfficio(null);
					dt.setDescrTipoUfficio(null);
				}
				if (nm.getAutoritaEsterna() != null) {
					dt.setDescrSedeAutoritaEsterna(nm.getAutoritaEsterna().getDescrSede());
					dt.setDescrTipoAutoritaEsterna(nm.getAutoritaEsterna().getDescrTipoAutorita());
				} else {
					dt.setDescrSedeAutoritaEsterna(null);
					dt.setDescrTipoAutoritaEsterna(null);
				}
				dt.setNote(nm.getNote());

				// aggiungo alla lista di ritorno
				ldt.add(dt);
			}
		}

		// valore di ritorno
		return ldt;
	}

	/**
	 * Metodo per la mappatura dei dati dei tenori
	 * 
	 * @param tenori
	 * @param dopm
	 * @return List<ESITITYPE>
	 */
	public static List<ESITITYPE> mapEsiti(TenoreModel[] tenori, DepositoOrdinanzaPcModel dopm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapEsiti");

		// instanzio un oggetto di tipo "ArrayList"
		List<ESITITYPE> let = null;
		if (tenori != null && tenori.length > 0) {
			// inizializzo l'oggetto di tipo "ArrayList"
			let = new ArrayList<ESITITYPE>(tenori.length);
			for (int i = 0; i < tenori.length; i++) {
				// instanzio ed valorizzo un oggetto di tipo "TenoreModel"
				TenoreModel tm = (TenoreModel) tenori[i];
				// instanzio ed inizializzo un oggetto di tipo "TENORETYPE"
				ESITITYPE et = new ESITITYPE();
				et.setDescrEsitoTenore(tm.getDescrEsitoTenore());
				et.setCodiceEsitoTenore(tm.getCodEsitoTenore());
				et.setDescrOggettoTenore(tm.getDescrOggettoTenore());
				et.setCodiceOggettoTenore(tm.getCodOggettoTenore());
				et.setOggettoProcedimento(dopm.getOggettoProcedimento());
				// aggiungo alla lista di ritorno
				let.add(et);
			}
		}

		// valore di ritorno
		return let;
	}

	/**
	 * Metodo per la mappatura dei dati delle prescrizioni
	 * 
	 * @param prescrizioni
	 * @return List<PRESCRIZIONETYPE>
	 */
	public static List<PRESCRIZIONETYPE> mapPrescrizioni(Vector prescrizioni) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapPrescrizioni");

		// instanzio un oggetto di tipo "ArrayList"
		List<PRESCRIZIONETYPE> lpt = null;
		if (PropertyUtil.isPresent(prescrizioni)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<PRESCRIZIONETYPE>(prescrizioni.size());
			for (int i = 0; i < prescrizioni.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "PrescrizioneModel"
				PrescrizioneModel pm = (PrescrizioneModel) prescrizioni.get(i);
				// instanzio ed inizializzo un oggetto di tipo "PRESCRIZIONETYPE"
				PRESCRIZIONETYPE pt = new PRESCRIZIONETYPE();
				pt.setDescrAltraPrescrizione(pm.getDescrAltraPrescrizione());
				pt.setDescrTipoPrescrizione(pm.getDescrTipoPrescrizione());
				// aggiungo alla lista di ritorno
				lpt.add(pt);
			}
		}

		// valore di ritorno
		return lpt;
	}

	/**
	 * Metodo per la mappatura dei dati dei Periodi di Libertà Anticipata
	 * 
	 * @param periodiLA
	 * @return List<PERIODILIBERTAANTICIPATATYPE>
	 */
	public static List<PERIODILIBERTAANTICIPATATYPE> mapPeriodiLA(Vector periodiLA) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapPeriodiLA");

		// instanzio un oggetto di tipo "ArrayList"
		List<PERIODILIBERTAANTICIPATATYPE> lplat = null;
		if (PropertyUtil.isPresent(periodiLA)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lplat = new ArrayList<PERIODILIBERTAANTICIPATATYPE>(periodiLA.size());
			for (int i = 0; i < periodiLA.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "LicenzaPeriodiLibAnticipataModel"
				LicenzaPeriodiLibAnticipataModel lplam = (LicenzaPeriodiLibAnticipataModel) periodiLA.get(i);
				// instanzio ed inizializzo un oggetto di tipo "PERIODILIBERTAANTICIPATATYPE"
				PERIODILIBERTAANTICIPATATYPE plat = new PERIODILIBERTAANTICIPATATYPE();
				plat.setDescrStatoPermesso(lplam.getLicenza().getDescrStatoPermesso());
				plat.setFlagConcesso(lplam.getLicenza().getFlagConcesso());
				plat.setFlagScorta(lplam.getLicenza().getFlagScorta());
				plat.setCodiTipoLicenza(lplam.getLicenza().getCodTipoLicenza());
				if (lplam.getLicenza().getNumeroGiorni() != null)
					plat.setNumeroGiorniRiduzione(lplam.getLicenza().getNumeroGiorni().toString());
				else
					plat.setNumeroGiorniRiduzione(null);
				plat.setSommaRisarcimentoDanni(lplam.getLicenza().getSommaRisarcDanni());
				List<PERIODITYPE> lpt = OrdinanzaMapper.mapPeriodi(lplam.getPeriodi());
				if (PropertyUtil.isPresent(lpt))
					plat.getElencoPeriodi().addAll(lpt);
				// aggiungo alla lista di ritorno
				lplat.add(plat);
			}
		}

		// valore di ritorno
		return lplat;
	}

	/**
	 * Metodo per la mappatura dei dati dei Periodi di Libertà Anticipata
	 * 
	 * @param plamArray
	 * @return List<PERIODITYPE>
	 */
	private static List<PERIODITYPE> mapPeriodi(PeriodoLibAnticipataModel[] plamArray) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapPeriodi");

		// instanzio un oggetto di tipo "ArrayList"
		List<PERIODITYPE> lpt = null;
		if (plamArray != null && plamArray.length > 0) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<PERIODITYPE>(plamArray.length);
			for (int i = 0; i < plamArray.length; i++) {
				PeriodoLibAnticipataModel plam = (PeriodoLibAnticipataModel) plamArray[i];
				// instanzio ed inizializzo un oggetto di tipo "PERIODITYPE"
				PERIODITYPE pt = new PERIODITYPE();
				pt.setDataFine(Mapper.creaDataTypeOrdinanza(plam.getDataFine()));
				pt.setDataInizio(Mapper.creaDataTypeOrdinanza(plam.getDataInizio()));
				pt.setFlagConcesso(plam.getFlagConcesso());
				// aggiungo alla lista di ritorno
				lpt.add(pt);
			}
		}

		// valore di ritorno
		return lpt;
	}

	/**
	 * Metodo per la mappatura dei dati delle Misure di Sicurezza
	 * 
	 * @param misureSicurezza
	 * @return List<MISURASICUREZZATYPE>
	 */
	public static List<MISURASICUREZZATYPE> mapMisureSicurezza(Vector misureSicurezza) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapMisureSicurezza");

		// instanzio un oggetto di tipo "ArrayList"
		List<MISURASICUREZZATYPE> lmst = null;
		if (PropertyUtil.isPresent(misureSicurezza)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lmst = new ArrayList<MISURASICUREZZATYPE>(misureSicurezza.size());
			for (int i = 0; i < misureSicurezza.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "MisuraSicurezzaModel"
				MisuraSicurezzaModel msm = (MisuraSicurezzaModel) misureSicurezza.get(i);
				// instanzio ed inizializzo un oggetto di tipo "MISURASICUREZZATYPE"
				MISURASICUREZZATYPE mst = new MISURASICUREZZATYPE();
				//@emma 09072018 intervento post COLLAUDO 11.2
				// correzione MEV AVVOCATURA post COLLAUDO (inserisco controllo per msm.getEvento() != null)
				if (msm.getEvento() != null) {
					mst.setCodiceEsitoEvento(msm.getEvento().getCodEsito());
				}
				else{
					mst.setCodiceEsitoEvento("");
				}
				mst.setDescrNatura(msm.getDescrNatura());
				mst.setDescrTipoMisura(msm.getDescrTipo());
				mst.setDurataMisura(Mapper.creaDurataTypeOrdinanza(msm.getNumGiorni(), msm.getNumMesi(),
						msm.getNumAnni()));
				// aggiungo alla lista di ritorno
				lmst.add(mst);
			}
		}

		// valore di ritorno
		return lmst;
	}

	// DettaglioOrdinanzaConcessioneRinvioEP.jsp
	public static DatiConcessioneRinvio mapDatiConcessioneRinvio(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiConcessioneRinvio");

		// instanzio ed inizializzo un oggetto di tipo "DatiConcessioneRinvio"
		DatiConcessioneRinvio dati = new DatiConcessioneRinvio();
		// aggiungo elementi all'oggetto di tipo "DatiConcessioneRinvio"
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDataTrasmissione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataTrasmissione()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrTipoUfficio(codTipoUfficio);
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaConversioneSS.jsp
	public static DatiConversioneSanzSost mapDatiConversioneSanzSost(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, PeriodoAltraSanzioneModel pasm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiConversioneSanzSost");

		// instanzio ed inizializzo un oggetto di tipo "DatiConversioneSanzSost"
		DatiConversioneSanzSost dati = new DatiConversioneSanzSost();
		// aggiungo elementi all'oggetto di tipo "DatiConversioneSanzSost"
		dati.setDurataArrestoRev(Mapper.creaDurataTypeOrdinanza(
				oetpm.getOrdinanza().getNumGiorniArrestoRev(), oetpm.getOrdinanza().getNumMesiArrestoRev(),
				oetpm.getOrdinanza().getNumAnniArrestoRev()));
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));
		if (pasm != null) {
			dati.setDurataSanzSostEspiata(Mapper.creaDurataTypeOrdinanza(pasm.getEspiataGG(),
					pasm.getEspiataMM(), pasm.getEspiataAA()));
			dati.setDurataSanzSostiResiduaEspiare(Mapper.creaDurataTypeOrdinanza(pasm.getResiduaGG(),
					pasm.getResiduaMM(), pasm.getResiduaAA()));
		} else {
			dati.setDurataSanzSostEspiata(null);
			dati.setDurataSanzSostiResiduaEspiare(null);
		}

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaEsecPressoDomicilio.jsp
	public static DatiEsecuzDomicilio mapDatiEsecuzDomicilio(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiEsecuzDomicilio");

		// instanzio ed inizializzo un oggetto di tipo "DatiEsecuzDomicilio"
		DatiEsecuzDomicilio dati = new DatiEsecuzDomicilio();
		// aggiungo elementi all'oggetto di tipo "DatiEsecuzDomicilio"
		dati.setAutoritaVigilante(oetpm.getOrdinanza().getAutoritaVigilante());
		dati.setCodTipoUfficio(codTipoUfficio);
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDescrComuneCssaComp(oetpm.getOrdinanza().getDescrComuneCssaComp());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setFlagEsistenzaReatoOstativo(oetpm.getOrdinanza().getFlagEsistenzaReatoostativo());
		dati.setFlagEspiazioneReatoOstativo(oetpm.getOrdinanza().getFlagEspiazioneReatoostativo());
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaEstinzionePena.jsp
	public static DatiEstinzionePena mapDatiEstinzionePena(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiEstinzionePena");

		// instanzio ed inizializzo un oggetto di tipo "DatiEstinzionePena"
		DatiEstinzionePena dati = new DatiEstinzionePena();
		// aggiungo elementi all'oggetto di tipo "DatiEstinzionePena"
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaEPLC.jsp
	public static DatiEstinzionePenaLibCondizionale mapDatiEstinzionePenaLibCondizionale(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiEstinzionePenaLibCondizionale");

		// instanzio ed inizializzo un oggetto di tipo "DatiEstinzionePenaLibCondizionale"
		DatiEstinzionePenaLibCondizionale dati = new DatiEstinzionePenaLibCondizionale();
		// aggiungo elementi all'oggetto di tipo "DatiEstinzionePenaLibCondizionale"
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaDeclaratoriaEstinsioneSS.jsp
	public static DatiEstinzioneSanzSost mapDatiEstinzioneSanzSost(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiEstinzioneSanzSost");

		// instanzio ed inizializzo un oggetto di tipo "DatiEstinzioneSanzSost"
		DatiEstinzioneSanzSost dati = new DatiEstinzioneSanzSost();
		// aggiungo elementi all'oggetto di tipo "DatiEstinzioneSanzSost"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setDescrUffTdsConcessoRiduzione(oetpm.getOrdinanza().getDescrUffTdsConcessoRiduzione());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaIndultino.jsp
	public static DatiIndultino mapDatiIndultino(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiIndultino");

		// instanzio ed inizializzo un oggetto di tipo "DatiIndultino"
		DatiIndultino dati = new DatiIndultino();
		// aggiungo elementi all'oggetto di tipo "DatiIndultino"
		dati.setAutoritaVigilante(oetpm.getOrdinanza().getAutoritaVigilante());
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDescrComuneCssaComp(oetpm.getOrdinanza().getDescrComuneCssaComp());
		dati.setDescrUfficioMagistratoComp(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setFlagEsistenzaReatoOstativo(oetpm.getOrdinanza().getFlagEsistenzaReatoostativo());
		dati.setFlagEspiazioneReatoOstativo(oetpm.getOrdinanza().getFlagEspiazioneReatoostativo());
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaLibAnt.jsp
	public static DatiLibertaAnticipata mapDatiLibertaAnticipata(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, Vector licenzePeriodi) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiLibertaAnticipata");

		// instanzio ed inizializzo un oggetto di tipo "DatiLibertaAnticipata"
		DatiLibertaAnticipata dati = new DatiLibertaAnticipata();
		// aggiungo elementi all'oggetto di tipo "DatiLibertaAnticipata"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		List<PERIODILIBERTAANTICIPATATYPE> lplat = OrdinanzaMapper.mapPeriodiLA(licenzePeriodi);
		if (PropertyUtil.isPresent(lplat))
			dati.getLicenzaPeriodiLibertaAnticipata().addAll(lplat);

		// valore di ritorno
		return dati;
	}

	// DettaglioLicenza.jsp
	public static DatiLicenza mapDatiLicenza(Vector licenze) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiLicenza");

		// instanzio un oggetto di tipo "DatiLicenza"
		DatiLicenza dati = null;
		if (PropertyUtil.isPresent(licenze)) {
			// inizializzo l'oggetto di tipo "DatiLicenza"
			dati = new DatiLicenza();
			// prendo il primo elemento dalla lista
			LicenzaLibAnticipataModel llam = (LicenzaLibAnticipataModel) licenze.firstElement();
			// aggiungo elementi all'oggetto di tipo "DatiLicenza"
			dati.setDataFineLicenza(Mapper.creaDataTypeOrdinanza(llam.getDataInizio()));
			dati.setDataInizioLicenza(Mapper.creaDataTypeOrdinanza(llam.getDataFine()));
			dati.setLuogoSvolgimentoProva(llam.getLuogoSvolgimentoProva());
			if (llam.getNumeroGiorni() != null)
				dati.setNumeroGiorniLicenza(llam.getNumeroGiorni().toString());
			else
				dati.setNumeroGiorniLicenza(null);
			if (llam.getNumeroMesi() != null)
				dati.setNumeroMesiLicenza(llam.getNumeroMesi().toString());
			else
				dati.setNumeroMesiLicenza(null);
			if (llam.getNumeroOre() != null)
				dati.setNumeroOreLicenza(llam.getNumeroOre().toString());
			else
				dati.setNumeroOreLicenza(null);
			dati.setOraFineLicenza(llam.getOraFine());
			dati.setOraInizioLicenza(llam.getOraInizio());
		}

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaMA.jsp
	public static DatiMisuraAlternativa mapDatiMisuraAlternativa(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiMisuraAlternativa");

		// instanzio ed inizializzo un oggetto di tipo "DatiMisuraAlternativa"
		DatiMisuraAlternativa dati = new DatiMisuraAlternativa();
		// aggiungo elementi all'oggetto di tipo "DatiMisuraAlternativa"
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDescrComuneCssaComp(oetpm.getOrdinanza().getDescrComuneCssaComp());
		dati.setDescrComuneUssmComp(oetpm.getOrdinanza().getDescrComuneUssmComp());
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());
		dati.setServizioTerapeuticoComp(oetpm.getOrdinanza().getServizioTerapeuticoComp());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaModificaPermanenteSS.jsp
	public static DatiModificaPermanSanziSost mapDatiModificaPermanSanziSost(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiModificaPermanSanziSost");

		// instanzio ed inizializzo un oggetto di tipo "DatiModificaPermanSanziSost"
		DatiModificaPermanSanziSost dati = new DatiModificaPermanSanziSost();
		// aggiungo elementi all'oggetto di tipo "DatiModificaPermanSanziSost"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaReclamata.jsp
	public static DatiOrdinanzaReclamata mapDatiOrdinanzaReclamata(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiOrdinanzaReclamata");

		// instanzio ed inizializzo un oggetto di tipo "DatiOrdinanzaReclamata"
		DatiOrdinanzaReclamata dati = new DatiOrdinanzaReclamata();
		// aggiungo elementi all'oggetto di tipo "DatiOrdinanzaReclamata"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setCodiTipoOrdinanza(oetpm.getOrdinanza().getCodTipoOrdinanza());
		dati.setDataTrasmissione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataTrasmissione()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaSospEsecOr.jsp
	public static DatiOrdinanzaSospesa mapDatiOrdinanzaSospesa(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiOrdinanzaSospesa");

		// instanzio ed inizializzo un oggetto di tipo "DatiOrdinanzaSospesa"
		DatiOrdinanzaSospesa dati = new DatiOrdinanzaSospesa();
		// aggiungo elementi all'oggetto di tipo "DatiOrdinanzaSospesa"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setCodiTipoUfficio(codTipoUfficio);
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDescrUffTdsConcessoRiduzione(oetpm.getOrdinanza().getDescrUffTdsConcessoRiduzione());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaProrogaDD.jsp
	public static DatiProrogaDetenDomic mapDatiProrogaDetenDomic(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiProrogaDetenDomic");

		// instanzio ed inizializzo un oggetto di tipo "DatiProrogaDetenDomic"
		DatiProrogaDetenDomic dati = new DatiProrogaDetenDomic();
		// aggiungo elementi all'oggetto di tipo "DatiProrogaDetenDomic"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaProrogaDS.jsp
	public static DatiProrogaDetenDomicSpeciale mapDatiProrogaDetenDomicSpeciale(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiProrogaDetenDomicSpeciale");

		// instanzio ed inizializzo un oggetto di tipo "DatiProrogaDetenDomicSpeciale"
		DatiProrogaDetenDomicSpeciale dati = new DatiProrogaDetenDomicSpeciale();
		// aggiungo elementi all'oggetto di tipo "DatiProrogaDetenDomicSpeciale"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaReclamiCEDU.jsp
	public static DatiReclamiCEDU mapDatiReclamiCEDU(Vector licenzePeriodi) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiReclamiCEDU");

		// instanzio ed inizializzo un oggetto di tipo "DatiReclamiCEDU"
		DatiReclamiCEDU dati = new DatiReclamiCEDU();
		// aggiungo elementi all'oggetto di tipo "DatiReclamiCEDU"
		List<PERIODILIBERTAANTICIPATATYPE> lplat = OrdinanzaMapper.mapPeriodiLA(licenzePeriodi);
		if (PropertyUtil.isPresent(lplat))
			dati.getLicenzaPeriodiLibertaAnticipata().addAll(lplat);

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaReclamoPermesso.jsp + DettaglioPermesso.jsp
	public static DatiReclamoPermesso mapDatiReclamoPermesso(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			Vector licenze) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiReclamoPermesso");

		// instanzio ed inizializzo un oggetto di tipo "DatiReclamoPermesso"
		DatiReclamoPermesso dati = new DatiReclamoPermesso();
		// aggiungo elementi all'oggetto di tipo "DatiReclamoPermesso"
		dati.setDataTrasmissione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataTrasmissione()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		if (PropertyUtil.isPresent(licenze)) {
			// prendo il primo elemento dalla lista
			LicenzaLibAnticipataModel llam = (LicenzaLibAnticipataModel) licenze.firstElement();
			// aggiungo elementi all'oggetto di tipo "DatiReclamoPermesso"
			if (llam.getNumeroGiorni() != null)
				dati.setGiorniPermesso(llam.getNumeroGiorni().toString());
			else
				dati.setGiorniPermesso(null);
			if (llam.getNumeroOre() != null)
				dati.setOrePermesso(llam.getNumeroOre().toString());
			else
				dati.setOrePermesso(null);
		}

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaRevoca.jsp
	public static DatiRevoca mapDatiRevoca(OrdinanzaEventoTenoriPrescrizioniModel oetpmRev) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRevoca");

		// instanzio ed inizializzo un oggetto di tipo "DatiRevoca"
		DatiRevoca dati = new DatiRevoca();
		if (oetpmRev != null) {
			// aggiungo elementi all'oggetto di tipo "DatiRevoca"
			if (oetpmRev.getOrdinanza().getAnnoS3() != null)
				dati.setAnnoOrdinanza(oetpmRev.getOrdinanza().getAnnoS3().toBigInteger());
			else
				dati.setAnnoOrdinanza(null);
			dati.setDataDepositoCancelleria(Mapper.creaDataTypeOrdinanza(oetpmRev.getOrdinanza()
					.getDataDeposito()));
			dati.setDataEmissione(Mapper.creaDataTypeOrdinanza(oetpmRev.getEvento().getDataEmissione()));
			if (oetpmRev.getOrdinanza().getNumS3() != null)
				dati.setNumeroOrdinanza(oetpmRev.getOrdinanza().getNumS3().toBigInteger());
			else
				dati.setNumeroOrdinanza(null);
			dati.setTipoOrdinanza(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getTipoOrdinanza(), oetpmRev.getOrdinanza().getCodTipoOrdinanza()));
		}

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaReclamata.jsp + DettaglioRevocaLibanticipata.jsp
	public static DatiRevocaLiberazioneAnticipata mapDatiRevocaLiberazioneAnticipata(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRevocaLiberazioneAnticipata");

		// instanzio ed inizializzo un oggetto di tipo "DatiRevocaLiberazioneAnticipata"
		DatiRevocaLiberazioneAnticipata dati = new DatiRevocaLiberazioneAnticipata();
		// aggiungo elementi all'oggetto di tipo "DatiRevocaLiberazioneAnticipata"
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaReclamata.jsp + DettaglioLibanticipata.jsp
	public static DatiRevocaLibertaAnticipata mapDatiRevocaLibertaAnticipata(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, Vector licenzePeriodi) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRevocaLibertaAnticipata");

		// instanzio ed inizializzo un oggetto di tipo "DatiRevocaLibertaAnticipata"
		DatiRevocaLibertaAnticipata dati = new DatiRevocaLibertaAnticipata();
		// aggiungo elementi all'oggetto di tipo "DatiRevocaLibertaAnticipata"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		List<PERIODILIBERTAANTICIPATATYPE> lplat = OrdinanzaMapper.mapPeriodiLA(licenzePeriodi);
		if (PropertyUtil.isPresent(lplat))
			dati.getLicenzaPeriodiLibertaAnticipata().addAll(lplat);

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaRevocaMA.jsp
	public static DatiRevocaMisuraAlternativa mapDatiRevocaMisuraAlternativa(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, String codTipoUfficio, UfficioModel um) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRevocaMisuraAlternativa");

		// instanzio ed inizializzo un oggetto di tipo "DatiRevocaMisuraAlternativa"
		DatiRevocaMisuraAlternativa dati = new DatiRevocaMisuraAlternativa();
		// aggiungo elementi all'oggetto di tipo "DatiRevocaMisuraAlternativa"
		dati.setDataDecorrenza(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataDecorrenza()));
		dati.setDataEmissione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDataTrasmissione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataTrasmissione()));
		dati.setDescrizioneDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		if (um != null) {
			String dtu = "";
			if ("TDS".equals(um.getCodTipoUfficio())) {
				if ("TDSM".equals(codTipoUfficio))
					dtu = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza emittente";
				else
					dtu = "Tribunale Sorveglianza emittente";
			} else if ("UDS".equals(um.getCodTipoUfficio())) {
				if ("UDSM".equals(codTipoUfficio))
					dtu = "Ufficio di Sorveglianza presso il Tribunale per Minorenni emittente";
				else
					dtu = "Ufficio Sorveglianza emittente";
			}
			dati.setDescrTipoUfficio(dtu);
		} else
			dati.setDescrTipoUfficio(null);
		dati.setDescrUfficioMagistratoComp(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setDescrUffTdsConcessoRiduzione(oetpm.getOrdinanza().getDescrUffTdsConcessoRiduzione());
		dati.setDurataArresto(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza().getNumGiorniArrestoRev(),
				oetpm.getOrdinanza().getNumMesiArrestoRev(), oetpm.getOrdinanza().getNumAnniArrestoRev()));
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaRicoveri.jsp
	public static DatiRicoveri mapDatiRicoveri(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRicoveri");

		// instanzio ed inizializzo un oggetto di tipo "DatiRicoveri"
		DatiRicoveri dati = new DatiRicoveri();
		// aggiungo elementi all'oggetto di tipo "DatiRicoveri"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaRicoveroOPGOssPsiche.jsp
	public static DatiRicoveriOssPsich mapDatiRicoveriOssPsich(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRicoveriOssPsich");

		// instanzio ed inizializzo un oggetto di tipo "DatiRicoveriOssPsich"
		DatiRicoveriOssPsich dati = new DatiRicoveriOssPsich();
		// aggiungo elementi all'oggetto di tipo "DatiRicoveriOssPsich"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());

		// valore di ritorno
		return dati;
	}

	// DettaglioRimessioneAtti.jsp
	public static List<DESTINATARIOTYPE> mapDestinatariRimessionAtti(Vector notifiche) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDestinatariRimessionAtti");

		// instanzio un oggetto di tipo "ArrayList"
		List<DESTINATARIOTYPE> ldt = null;
		if (PropertyUtil.isPresent(notifiche)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			ldt = new ArrayList<DESTINATARIOTYPE>(notifiche.size());
			for (int i = 0; i < notifiche.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "NotificaModel"
				NotificaModel nm = (NotificaModel) notifiche.get(i);
				// instanzio ed inizializzo un oggetto di tipo "DESTINATARIOTYPE"
				DESTINATARIOTYPE dt = new DESTINATARIOTYPE();
				if (nm.getAvvSiep() != null && nm.getAvvSiep().getAvvocato() != null)
					dt.setAvvocatoSIEP(nm.getAvvSiep().getAvvocato().getDescrTipo() + " " +
							nm.getAvvSiep().getAvvocato().getCognome() + " "
							+ nm.getAvvSius().getAvvocato().getNome());
				else
					dt.setAvvocatoSIEP(null);
				if (nm.getAvvSius() != null && nm.getAvvSius().getAvvocato() != null)
					dt.setAvvocatoSIUS(nm.getAvvSius().getAvvocato().getCognome() + " "
							+ nm.getAvvSius().getAvvocato().getNome());
				else
					dt.setAvvocatoSIUS(null);
				if (nm.getCSSA() != null) {
					dt.setComuneCSSA(nm.getCSSA().getComune());
					dt.setIndirizzoCSSA(nm.getCSSA().getIndirizzo());
				} else {
					dt.setComuneCSSA(null);
					dt.setIndirizzoCSSA(null);
				}
				if (nm.getIstitutoDetenzione() != null) {
					dt.setDescrComuneIstitutoDeten(nm.getIstitutoDetenzione().getDescrComune());
					dt.setDescrTipoIstitutoDeten(nm.getIstitutoDetenzione().getDescrTipoIstituto());
				} else {
					dt.setDescrComuneIstitutoDeten(null);
					dt.setDescrTipoIstitutoDeten(null);
				}
				if (nm.getUfficio() != null) {
					dt.setDescrComuneUfficio(nm.getUfficio().getDescrComune());
					dt.setDescrTipoUfficio(nm.getUfficio().getDescrTipoUfficio());
				} else {
					dt.setDescrComuneUfficio(null);
					dt.setDescrTipoUfficio(null);
				}
				if (nm.getAutoritaEsterna() != null) {
					dt.setDescrSedeAutoritaEsterna(nm.getAutoritaEsterna().getDescrSede());
					dt.setDescrTipoAutoritaEsterna(nm.getAutoritaEsterna().getDescrTipoAutorita());
				} else {
					dt.setDescrSedeAutoritaEsterna(null);
					dt.setDescrTipoAutoritaEsterna(null);
				}
				
				if(PropertyUtil.isPresent(nm.getSogIdSoggetto())){
					dt.setIdSoggetto(nm.getSogIdSoggetto().toString());
				}
				
				dt.setNote(nm.getNote());

				// aggiungo alla lista di ritorno
				ldt.add(dt);
			}
		}

		// valore di ritorno
		return ldt;
	}

	// DettaglioOrdinanzaRinvioSS.jsp
	public static DatiRinvioSanzSost mapDatiRinvioSanzSost(OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiRinvioSanzSost");

		// instanzio ed inizializzo un oggetto di tipo "DatiRinvioSanzSost"
		DatiRinvioSanzSost dati = new DatiRinvioSanzSost();
		// aggiungo elementi all'oggetto di tipo "DatiRinvioSanzSost"
		dati.setDataFineMisura(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataFineMisura()));
		dati.setDataInizioPeriodo(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataInizioPeriodo()));
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setDurataSospensione(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza().getSospensioneGGSS(),
				oetpm.getOrdinanza().getSospensioneMMSS(), oetpm.getOrdinanza().getSospensioneAASS()));

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaApplicazioneSS.jsp
	public static DatiSanzioneSostitutiva mapDatiSanzioneSostitutiva(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiSanzioneSostitutiva");

		// instanzio ed inizializzo un oggetto di tipo "DatiSanzioneSostitutiva"
		DatiSanzioneSostitutiva dati = new DatiSanzioneSostitutiva();
		// aggiungo elementi all'oggetto di tipo "DatiSanzioneSostitutiva"
		dati.setDescrDecisione(oetpm.getOrdinanza().getUlterioreDescrizione());
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		String duc = "";
		if (oetpm.getOrdinanza().getCodUfficioMagistratoComp() != null
				&& oetpm.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 1) {
			UfficioModel um;
			try {
				um = UfficioUtils.getUfficioByCodUfficio(oetpm.getOrdinanza().getCodUfficioMagistratoComp());
				if (um != null)
					duc = um.getDescrTipoUfficio() + " di " + um.getDescrComune();
			} catch (F3BException e) {
				avvocaturaLogger.error("Errore nella ricerca Ufficio: ", e);
				e.printStackTrace();
			}
		}
		dati.setDesUfficioCompetente(duc);
		dati.setDescrUfficioMagistratoCompetente(oetpm.getOrdinanza().getDescrUfficioMagistratoComp());
		dati.setDurataDetenzioneDomiciliare(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza()
				.getNumGiorniDetenzioneDom(), oetpm.getOrdinanza().getNumMesiDetenzioneDom(), oetpm
				.getOrdinanza().getNumAnniDetenzioneDom()));

		// valore di ritorno
		return dati;
	}

	// DettaglioScomputo.jsp
	public static DatiScomputo mapDatiScomputo(Vector licenze) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiScomputo");

		// instanzio un oggetto di tipo "DatiScomputo"
		DatiScomputo dati = null;
		if (PropertyUtil.isPresent(licenze)) {
			// inizializzo l'oggetto di tipo "DatiScomputo"
			dati = new DatiScomputo();
			// prendo il primo elemento dalla lista
			LicenzaLibAnticipataModel llam = (LicenzaLibAnticipataModel) licenze.firstElement();
			// aggiungo elementi all'oggetto di tipo "DatiScomputo"
			if (llam.getNumeroGiorni() != null)
				dati.setNumeroGiorni(llam.getNumeroGiorni().toBigInteger());
			else
				dati.setNumeroGiorni(null);
		}

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaSopravvenienzaNT.jsp
	public static DatiSopravvenienzaNuovoTitolo mapDatiSopravvenienzaNuovoTitolo(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiSopravvenienzaNuovoTitolo");

		// instanzio ed inizializzo un oggetto di tipo "DatiSopravvenienzaNuovoTitolo"
		DatiSopravvenienzaNuovoTitolo dati = new DatiSopravvenienzaNuovoTitolo();
		// aggiungo elementi all'oggetto di tipo "DatiSopravvenienzaNuovoTitolo"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setCodiTipoUfficio(codTipoUfficio);
		dati.setDescrUffTdsConcessoRiduzione(oetpm.getOrdinanza().getDescrUffTdsConcessoRiduzione());
		dati.setLuogoSvolgimentoProva(oetpm.getOrdinanza().getLuogoSvolgimentoProva());
		dati.setServizioTerapeuticoComp(oetpm.getOrdinanza().getServizioTerapeuticoComp());

		// valore di ritorno
		return dati;
	}

	// DettaglioOrdinanzaSospensioneEsecuzioneSanzioniSostitutive.jsp
	public static DatiSospEsecSanzSost mapDatiSospEsecSanzSost(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			String codTipoUfficio, PeriodoAltraSanzioneModel pasm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiSospEsecSanzSost");

		// instanzio ed inizializzo un oggetto di tipo "DatiSospEsecSanzSost"
		DatiSospEsecSanzSost dati = new DatiSospEsecSanzSost();
		// aggiungo elementi all'oggetto di tipo "DatiSospEsecSanzSost"
		dati.setCodiNaturaProvvedimento(oetpm.getOrdinanza().getCodNaturaProvvedimento());
		dati.setCodTipoUfficio(codTipoUfficio);
		dati.setDataScadenzaSospensioneSS(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza()
				.getDataScadenzaSospensioneSS()));
		dati.setDataSospensione(Mapper.creaDataTypeOrdinanza(oetpm.getOrdinanza().getDataSospensioneSS()));
		dati.setDescrUffTdsConcessoRiduzione(oetpm.getOrdinanza().getDescrUffTdsConcessoRiduzione());
	
		if (pasm != null && pasm.getIdPeriodoAltraSanzione() != null) {
			dati.setDurataSanzSostEspiata(Mapper.creaDurataTypeOrdinanza(pasm.getEspiataGG(),
					pasm.getEspiataMM(), pasm.getEspiataAA()));
			dati.setDurataSanzSostiResiduaEspiare(Mapper.creaDurataTypeOrdinanza(pasm.getResiduaGG(),
					pasm.getResiduaMM(), pasm.getResiduaAA()));
		} 
		dati.setDurataSospensione(Mapper.creaDurataTypeOrdinanza(oetpm.getOrdinanza().getSospensioneGGSS(),
				oetpm.getOrdinanza().getSospensioneMMSS(), oetpm.getOrdinanza().getSospensioneAASS()));
		
		if(oetpm.getOrdinanza().getFlagRecuperoSS()!= null && oetpm.getOrdinanza().getFlagRecuperoSS().equals("S")){		
			dati.setGiorniRecuperoSS(oetpm.getOrdinanza().getGiorniRecuperoSS().toString());}		

		// valore di ritorno
		return dati;
	}

	public static List<CONVERSIONEPENEPECUNIARIETYPE> mapDatiConversionePenePecuniarie(
			OrdinanzaEventoTenoriPrescrizioniModel oetpm, Vector richiesteConversioni) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiConversionePenePecuniarie");

		// instanzio un oggetto di tipo "ArrayList"
		List<CONVERSIONEPENEPECUNIARIETYPE> lcppt = null;
		TenoreModel[] tenori = oetpm.getTenori();
		if (PropertyUtil.isPresent(richiesteConversioni)) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lcppt = new ArrayList<CONVERSIONEPENEPECUNIARIETYPE>(richiesteConversioni.size());
			for (int i = 0; i < richiesteConversioni.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "RichiestaConversioneEstesaModel"
				RichiestaConversioneEstesaModel rcem = (RichiestaConversioneEstesaModel) richiesteConversioni
						.get(i);
				CONVERSIONEPENEPECUNIARIETYPE cppt = new CONVERSIONEPENEPECUNIARIETYPE();
				if (PropertyUtil.isPresent(rcem.getFasSiep()) && rcem.getFasSiep().getChiaveAnno() != null)
					cppt.setAnnoFascSIEP(rcem.getFasSiep().getChiaveAnno().toBigInteger());
				else
					cppt.setAnnoFascSIEP(null);
				if (PropertyUtil.isPresent(rcem.getFasSiep()) && rcem.getFasSiep().getChiaveProgr() != null)
					cppt.setNumeroFascSIEP(rcem.getFasSiep().getChiaveProgr().toBigInteger());
				else
					cppt.setNumeroFascSIEP(null);
				cppt.setImportoAmmenda(rcem.getRichiestaConversione().getImportoAmmenda());
				cppt.setImportoMulta(rcem.getRichiestaConversione().getImportoMulta());
				cppt.setDurataEsito(Mapper.creaDurataTypeOrdinanza(rcem.getRichiestaConversione()
						.getDurataEsitoGiorni(), rcem.getRichiestaConversione().getDurataEsitoMesi(), rcem
						.getRichiestaConversione().getDurataEsitoAnni()));
				cppt.setCodTipoSanzione(rcem.getRichiestaConversione().getCodTipoSanzione());
				if (rcem.getRichiestaConversione().getNumeroRate() != null)
					cppt.setNumeroRate(rcem.getRichiestaConversione().getNumeroRate().intValue());
				else
					cppt.setNumeroRate(0);
				cppt.setDataInizioPagamento(Mapper.creaDataTypeOrdinanza(rcem.getRichiestaConversione()
						.getDataInizioPagamento()));
				if (rcem.getRichiestaConversione().getNumeroGiorniInizioPagamento() != null)
					cppt.setNumeroGiorniInizioPagamento(rcem.getRichiestaConversione()
							.getNumeroGiorniInizioPagamento().toString());
				else
					cppt.setNumeroGiorniInizioPagamento(null);
				cppt.setValoreRata(rcem.getRichiestaConversione().getValoreRata());
				cppt.setValoreUltimaRata(rcem.getRichiestaConversione().getValoreUltimaRata());

				if (tenori != null && tenori.length > 0) {
					cppt.setCodOggettoTenore(tenori[0].getCodOggettoTenore());
					cppt.setDescrOggettoTenore(tenori[0].getDescrOggettoTenore());
				} else {
					cppt.setCodOggettoTenore(null);
					cppt.setDescrOggettoTenore(null);
				}
				// aggiungo elementi all'arrayList
				lcppt.add(cppt);
			}
		}

		// valore di ritorno
		return lcppt;
	}

	/**
	 * Valorizza i campi dell'oggetto DatiFascicoloOrigine
	 * 
	 * @param fgpmOrigine
	 * @return
	 */
	public static DatiFascicoloOrigine mapDatiFascioloOrigine(FascicoloGPModel fgpmOrigine) {
		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiFascioloOrigine");

		// instanzio un oggetto di tipo "DatiFascicoloOrigine"
		DatiFascicoloOrigine dati = null;
		if (PropertyUtil.isPresent(fgpmOrigine)) {
			// inizializzo l'oggetto di tipo "DatiFascicoloOrigine"
			dati = new DatiFascicoloOrigine();
			FascicoloSiusModel fascicoloOrigine = fgpmOrigine.getFascicoloSiusModel();
			// dati del soggetto
			SoggettoModel soggetto = fgpmOrigine.getFascicoloSiusModel().getSoggetto();
			// dati del procedimento
			GeneraleProcedimentoModel gpm = fgpmOrigine.getGeneraleProcedimentoModel();

			if (PropertyUtil.isPresent(fascicoloOrigine)) {
				if (PropertyUtil.isPresent(fascicoloOrigine.getChiaveAnno()))
					dati.setAnnoProcedimentoSIUS(fascicoloOrigine.getChiaveAnno().toBigInteger());
				else
					dati.setAnnoProcedimentoSIUS(null);
				if (PropertyUtil.isPresent(fascicoloOrigine.getChiaveProgr()))
					dati.setNumeroProcedimentoSIUS(fascicoloOrigine.getChiaveProgr().toBigInteger());
				else
					dati.setNumeroProcedimentoSIUS(null);
				if (PropertyUtil.isPresent(fascicoloOrigine.getDescrComuneUfficio()))
					dati.setDescrComuneUfficio(fascicoloOrigine.getDescrComuneUfficio());
				else
					dati.setDescrComuneUfficio(null);
				if (PropertyUtil.isPresent(fascicoloOrigine.getDescrTipoUfficio()))
					dati.setDescrTipoUfficio(fascicoloOrigine.getDescrTipoUfficio());
				else
					dati.setDescrTipoUfficio(null);
				if (PropertyUtil.isPresent(fascicoloOrigine.getCodStatoFascicolo()))
					dati.setCodiceStatoFascicolo(fascicoloOrigine.getCodStatoFascicolo());
				else
					dati.setCodiceStatoFascicolo(null);
				if (PropertyUtil.isPresent(fascicoloOrigine.getDescrStatoFascicolo()))
					dati.setDescrStatoFascicolo(fascicoloOrigine.getDescrStatoFascicolo());
				else
					dati.setDescrStatoFascicolo(null);
			}

			if (PropertyUtil.isPresent(gpm)) {
				if (PropertyUtil.isPresent(gpm.getDescrOggettoProcedimento()))
					dati.setOggettoProcedimentoSIUS(gpm.getDescrOggettoProcedimento());
				else
					dati.setOggettoProcedimentoSIUS(null);
				dati.setDataCameraConsiglio(Mapper.creaDataTypeOrdinanza(gpm.getDataCameraConsiglio()));
			}

			if (PropertyUtil.isPresent(soggetto)) {
				if (PropertyUtil.isPresent(soggetto.getNome()))
					dati.setNomeSoggetto(soggetto.getNome());
				else
					dati.setNomeSoggetto(null);
				if (PropertyUtil.isPresent(soggetto.getCognome()))
					dati.setCognomeSoggetto(soggetto.getCognome());
				else
					dati.setCognomeSoggetto(null);
				if (PropertyUtil.isPresent(soggetto.getSesso()))
					dati.setSesso(soggetto.getSesso());
				else
					dati.setSesso(null);
				dati.setDataNascita(Mapper.creaDataTypeOrdinanza(soggetto.getDataNascita()));
				if (PropertyUtil.isPresent(soggetto.getDescrComuneNascita()))
					dati.setDescrComuneNascita(soggetto.getDescrComuneNascita());
				else
					dati.setDescrComuneNascita(null);
				if (PropertyUtil.isPresent(soggetto.getCodProvinciaNascita()))
					dati.setCodiceProvincia(soggetto.getCodProvinciaNascita());
				else
					dati.setCodiceProvincia(null);
				if (PropertyUtil.isPresent(soggetto.getDescrStatoNascita()))
					dati.setDescrStatoNascita(soggetto.getDescrStatoNascita());
				else
					dati.setDescrStatoNascita(null);
			}
			if (PropertyUtil.isPresent(fgpmOrigine.getUdiPro())
					&& PropertyUtil.isPresent(fgpmOrigine.getUdiPro().getFlagRinviata()))
				dati.setFlagRinviata(fgpmOrigine.getUdiPro().getFlagRinviata());
			else
				dati.setFlagRinviata(null);
		}
		return dati;
	}

	public static MISURASICUREZZATYPE mapDatiMisuraSicurezza(EsecuzioneMisuraSicurezzaModel emsm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: OrdinanzaMapper, metodo: mapDatiMisuraSicurezza");

		// instanzio un oggetto di tipo "MISURASICUREZZATYPE"
		MISURASICUREZZATYPE dati = null;
		if (emsm != null) {
			// inizializzo l'oggetto di tipo "MISURASICUREZZATYPE"
			dati = new MISURASICUREZZATYPE();
			// aggiungo elementi all'oggetto di tipo "MISURASICUREZZATYPE"
			dati.setCodiceEsitoEvento(null);
			dati.setDescrNatura(null);
			dati.setDescrTipoMisura(emsm.getDescrTipoMisura());
			dati.setDurataMisura(Mapper.creaDurataTypeOrdinanza(emsm.getNumGiorniMisura(),
					emsm.getNumMesiMisura(), emsm.getNumAnniMisura()));
		}
		// valore di ritorno
		return dati;
	}

}