/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.AVVOCATOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIDECRETO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIDECRETO.DatiFascicoloOrigine;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIDECRETOREVOCATO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIDECRETOUNIFICANTE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DESTINATARIOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.LICENZATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.MOTIVAZIONITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PERIODILIBERTAANTICIPATATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PERIODITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PERMESSOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PRESCRIZIONETYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.TENORETYPE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.curatore.model.CuratoreModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.model.UdienzaModel;

/**
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class DecretoMapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per la mappatura dei dati del decreto
	 * 
	 * @param ddemm
	 * @param fgpmRevoca
	 * @param ddmRevoca
	 * @param isDecretoDiRevoca
	 * @param um
	 * @param isDecretoRevocato
	 * @param ddmRevocato
	 * @param fgpmRevocato
	 * @param isFascicoloOrigineRevocato
	 * @param fgpmOrigineRevocato
	 * @param permessiRev
	 * @param idm
	 * @param licenze
	 * @param revoca
	 * @param licenzePeriodi
	 * @param pasm
	 * @param fascicoloOrigine
	 * @param lFasUnificante
	 * @param lFasUnificato
	 * @param fgpm
	 * @param pamm
	 * @return DATIDECRETO
	 */
	public static DATIDECRETO mapDatiDecreto(DepositoDecretoEventoMotivazioniModel ddemm,
			FascicoloGPModel fgpmRevoca, DepositoDecretoModel ddmRevoca, boolean isDecretoDiRevoca,
			UdienzaModel um, boolean isDecretoRevocato, DepositoDecretoModel ddmRevocato,
			FascicoloGPModel fgpmRevocato, boolean isFascicoloOrigineRevocato,
			FascicoloGPModel fgpmOrigineRevocato, Vector permessiRev, IstitutoDetenzioneModel idm,
			Vector licenze, LicenzaLibAnticipataModel revoca, Vector licenzePeriodi,
			PeriodoAltraSanzioneModel pasm, FascicoloGPModel fascicoloOrigine,
			FascicoloGPModel lFasUnificante, FascicoloGPModel lFasUnificato, FascicoloGPModel fgpm,
			PeriodoAltraMisuraModel pamm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDatiDecreto");

		// instanzio ed inizializzo un oggetto di tipo "DATIDECRETO"
		DATIDECRETO dd = new DATIDECRETO();
		DepositoDecretoModel ddm = (PropertyUtil.isPresent(ddemm)) ? ddemm.getDepositoDecreto() : null;
		EventoModel em = (PropertyUtil.isPresent(ddemm)) ? ddemm.getEvento() : null;
		// aggiungo elementi al tipo "DATIDECRETO"
		if (PropertyUtil.isPresent(ddm)) {
			dd.setDescrTipoDecreto(ddm.getCodTipoDecreto() + "#" + ddm.getDescrTipoDecreto());
			if (ddm.getAnnoS72() != null)
				dd.setAnnoDecreto(ddm.getAnnoS72().toBigInteger());
			else
				dd.setAnnoDecreto(null);
			if (ddm.getNumS72() != null)
				dd.setNumeroDecreto(ddm.getNumS72().toBigInteger());
			else
				dd.setNumeroDecreto(null);
			dd.setDataEmissione(Mapper.creaDataTypeDecreto(ddm.getDataEmissione()));
			dd.setDataDepositoCancelleria(Mapper.creaDataTypeDecreto(ddm.getDataDeposito()));
			// da decodificare A (Annullato), S (Validato), N (Da Validare)
			dd.setStatoProvvedimento(em.getFlagDocumentoRegistrato());
			dd.setLuogoSvolgimentoProva(ddm.getNote());
			if (PropertyUtil.isPresent(um)) {
				dd.setDataUdienza(Mapper.creaDataTypeDecreto(um.getDataUdienza()));
				dd.setLuogoSvolgimentoProva(um.getLuogoUdienza());
			} else
				dd.setDataUdienza(null);

			// valorizzo, se presenti, tutte le altre info del decreto
			if (PropertyUtil.isPresent(ddm.getNote())) {
				dd.setEventualeMotivazione(ddm.getNote());
			}
			if (PropertyUtil.isPresent(ddm.getDescrTdsComp())) {
				dd.setTribSorvCompetente(ddm.getDescrTdsComp());
			}
			if (PropertyUtil.isPresent(ddm.getUfficioCompetente())) {
				dd.setUfficioSorvCompetente(ddm.getUfficioCompetente().getDescrTipoUfficio() + " di "
						+ ddm.getUfficioCompetente().getDescrComune());
			}
			if (PropertyUtil.isPresent(ddm.getDescrTipoControlloEsecuzione())) {
				dd.setDescrTipoControlloEsecuzione(ddm.getDescrTipoControlloEsecuzione());
			}
			if (PropertyUtil.isPresent(ddm.getDescrCommActa())) {
				dd.setDescrCommActa(ddm.getDescrCommActa());
			}
			if (PropertyUtil.isPresent(ddm.getSentenzeRiferimento())) {
				dd.setSentenzaRiferimento(ddm.getSentenzeRiferimento());
			}
			if (PropertyUtil.isPresent(ddm.getLuogoSvolgimentoProva())) {
				dd.setLuogoSvolgimentoProva(ddm.getLuogoSvolgimentoProva());
			}
			if (PropertyUtil.isPresent(ddm.getNote())) {
				dd.setNote(ddm.getNote());
			}
			if (PropertyUtil.isPresent(ddm.getDescrProcuraEsecuzione())) {
				dd.setDescrProcuraEsecuzione(ddm.getDescrProcuraEsecuzione());
			}
			if (PropertyUtil.isPresent(ddm.getStatusPersona())) {
				dd.setStatusPersona(ddm.getStatusPersona());
			}
			if (PropertyUtil.isPresent(ddm.getAnnoProcRevocato())) {
				dd.setAnnoProcRevocato(new BigInteger(ddm.getAnnoProcRevocato()));
			}
			if (PropertyUtil.isPresent(ddm.getAltriDestinatari())) {
				dd.setQuesturaCompEsecuzione(ddm.getAltriDestinatari());
			}
			if (PropertyUtil.isPresent(ddm.getTotOreRaggiungimento())) {
				dd.setTotOreRaggiungimento(ddm.getTotOreRaggiungimento());
			}
			if (PropertyUtil.isPresent(ddm.getNumGiorniRiduzionePena())) {
				dd.setNumeGiorniRiduzionePena(ddm.getNumGiorniRiduzionePena().toString());
			}
			if (PropertyUtil.isPresent(ddm.getSommaRisarcimentoDanni())) {
				dd.setSommaRisarcimentoDanni(new BigDecimal(ddm.getSommaRisarcimentoDanni().toString()));
			}
			if (PropertyUtil.isPresent(ddm.getDataSospensioneSS())) {
				dd.setDataSospensioneSanzSost(Mapper.creaDataTypeDecreto(ddm.getDataSospensioneSS()));
			}
			if (PropertyUtil.isPresent(ddm.getSospensioneAASS())
					|| PropertyUtil.isPresent(ddm.getSospensioneMMSS())
					|| PropertyUtil.isPresent(ddm.getSospensioneGGSS())) {
				dd.setDurataSospensioneSanzSost(Mapper.creaDurataTypeDecreto(ddm.getSospensioneGGSS(),
						ddm.getSospensioneMMSS(), ddm.getSospensioneAASS(), null));
			}
			if (PropertyUtil.isPresent(ddm.getDataScadenzaSospensioneSS())) {
				dd.setDataScadenzaSospSanzSost(Mapper.creaDataTypeDecreto(ddm.getDataScadenzaSospensioneSS()));
			}
			if (PropertyUtil.isPresent(ddm.getFlagRecuperoSS())) {
				dd.setFlagRecuperoSanzSost(ddm.getFlagRecuperoSS());
			}
			if (PropertyUtil.isPresent(ddm.getGiorniRecuperoSS())) {
				dd.setGiorniRecuperoSanzSost(new BigInteger(ddm.getGiorniRecuperoSS().toString()));
			}
			// setto l'elemento codi tipo registro
			if (PropertyUtil.isPresent(fgpm) && PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel())
					&& PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro())) {
				dd.setCodTipoRegistro(fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro());
			} else
				dd.setCodTipoRegistro(null);
			if (PropertyUtil.isPresent(ddm.getNumeroGiorniRevocaLA()))
				dd.setNumeGiorniRevocaLA(ddm.getNumeroGiorniRevocaLA().toBigInteger());
			else
				dd.setNumeGiorniRevocaLA(null);
		}

		if (PropertyUtil.isPresent(idm)) {
			if (PropertyUtil.isPresent(idm.getIdIstitutoDetenzione())) {
				dd.setDescrTipoIstitutoDeten(idm.getDescrTipoIstituto());
				dd.setDescrComuneIstitutoDeten(idm.getDescrComune());
			}
		}
		// valorizzo l'element licenza su DATIDECRETO
		if (PropertyUtil.isPresent(licenze)) {
			if (licenze.size() > 0) {
				// prendo solo il prmo valore così come fatto sulla pagina di sies DettaglioLicenza.jsp
				LicenzaLibAnticipataModel licenzalibanticipata = (LicenzaLibAnticipataModel) licenze.get(0);
				Vector<LicenzaLibAnticipataModel> licVEc = new Vector<LicenzaLibAnticipataModel>();
				licVEc.add(licenzalibanticipata);
				List<LICENZATYPE> lista = DecretoMapper.mapLicenze(licenze);
				if (PropertyUtil.isPresent(lista)) {
					// prendo solo il prmo valore così come fatto sulla pagina di sies DettaglioLicenza.jsp
					LICENZATYPE licenzaT = lista.get(0);
					dd.setLicenza(licenzaT);
				}
			}
		}
		if (PropertyUtil.isPresent(revoca)) {
			if (PropertyUtil.isPresent(revoca.getNumeroGiorni())) {
				dd.setNumeGiorniRevoca(new BigInteger(revoca.getNumeroGiorni().toString()));
			}
			if (PropertyUtil.isPresent(revoca.getNumeroOre())) {
				dd.setNumeroOreRevoca(new BigInteger(revoca.getNumeroOre().toString()));
			}
		}
		// VALORIZZO L'ELEMENT elencoLicenzePeriodiLA su DATIDECRETO
		if (PropertyUtil.isPresent(licenzePeriodi)) {
			List<LICENZATYPE> lpt = DecretoMapper.mapLicenzePeriodi(licenzePeriodi);
			if (lpt != null && !lpt.isEmpty())
				dd.getElencoLicenzePeriodiLA().addAll(lpt);
		}
		// VALORIZZO L'ELEMENT durataSanzsostEspiata e durataSanzSostResidua
		if (PropertyUtil.isPresent(pasm)) {
			dd.setDurataSanzSostEspiata(Mapper.creaDurataTypeDecreto(pasm.getEspiataGG(),
					pasm.getEspiataMM(), pasm.getEspiataAA(), null));
			dd.setDurataSanzSostResidua(Mapper.creaDurataTypeDecreto(pasm.getResiduaGG(),
					pasm.getResiduaMM(), pasm.getResiduaAA(), null));
		}
		// valorizzo i dati dei Periodi Altra Misura
		if (PropertyUtil.isPresent(pamm)) {
			dd.setDurataSanzSostResidua(Mapper.creaDurataTypeDecreto(pamm.getResiduaGG(),
					pamm.getResiduaMM(), pamm.getResiduaAA(), null));
		}
		// dati del fascicoloOrigine
		if (PropertyUtil.isPresent(fascicoloOrigine)) {
			DatiFascicoloOrigine fascOrigin = new DatiFascicoloOrigine();
			fascOrigin = DecretoMapper.mapDatiFascicoloOrigine(fascicoloOrigine);
			dd.setDatiFascicoloOrigine(fascOrigin);
		}
		// DATI DEL DECRETO UNIFICANTE
		if (PropertyUtil.isPresent(lFasUnificante) && PropertyUtil.isPresent(lFasUnificato)) {
			DATIDECRETOUNIFICANTE datiUnificazione = mapDatiDecretoUnificante(lFasUnificante, lFasUnificato);
			dd.setDATIDECRETOUNIFICANTE(datiUnificazione);
		}

		// DECRETO DI REVOCA
		if (isDecretoDiRevoca) {
			DATIDECRETOREVOCATO ddr = new DATIDECRETOREVOCATO();
			ddr.setAnnoDecreto(ddmRevoca.getAnnoS72().toBigInteger());
			ddr.setDataDepositoCancelleria(Mapper.creaDataTypeDecreto(ddmRevoca.getDataDeposito()));
			ddr.setDataEmissione(Mapper.creaDataTypeDecreto(ddmRevoca.getDataEmissione()));
			ddr.setDescrTipoDecreto(ddmRevoca.getDescrTipoDecreto());
			ddr.setNumeroDecreto(ddmRevoca.getNumS72().toBigInteger());
			if (ddmRevoca.getNumeroGiorniRevocaLA() != null)
				ddr.setNumeroGiorniRevocaLA(ddmRevoca.getNumeroGiorniRevocaLA().toBigInteger());
			else
				ddr.setNumeroGiorniRevocaLA(null);
			// da decodificare A (Annullato), S (Validato), N (Da Validare)
			ddr.setStatoProvvedimento(em.getFlagDocumentoRegistrato());
			if (fgpmRevoca.getFascicoloSiusModel().getChiaveAnno() != null)
				ddr.setAnnoProcRevocato(fgpmRevoca.getFascicoloSiusModel().getChiaveAnno().toBigInteger());
			else
				ddr.setAnnoProcRevocato(null);
			if (fgpmRevoca.getFascicoloSiusModel().getChiaveProgr() != null)
				ddr.setNumeroProcRevocato(fgpmRevoca.getFascicoloSiusModel().getChiaveProgr().toBigInteger());
			else
				ddr.setNumeroProcRevocato(null);
			ddr.setDescrOggettoProcedimento(fgpmRevoca.getGeneraleProcedimentoModel()
					.getDescrOggettoProcedimento());
			ddr.setDescrTipoUfficio(fgpmRevoca.getFascicoloSiusModel().getDescrTipoUfficio());
			ddr.setDescrComuneUfficio(fgpmRevoca.getFascicoloSiusModel().getDescrComuneUfficio());
			List<PERMESSOTYPE> lpt = DecretoMapper.mapPermessi(permessiRev);
			if (lpt != null && !lpt.isEmpty())
				ddr.getElencoPermessi().addAll(lpt);
			dd.setDECRETOREVOCATO(ddr);
		} else if (isDecretoRevocato) { // DECRETO REVOCATO
			DATIDECRETOREVOCATO ddr = new DATIDECRETOREVOCATO();
			ddr.setAnnoDecreto(ddmRevocato.getAnnoS72().toBigInteger());
			ddr.setDataDepositoCancelleria(Mapper.creaDataTypeDecreto(ddmRevocato.getDataDeposito()));
			ddr.setDataEmissione(Mapper.creaDataTypeDecreto(ddmRevocato.getDataEmissione()));
			ddr.setDescrTipoDecreto(ddmRevocato.getDescrTipoDecreto());
			ddr.setNumeroDecreto(ddmRevocato.getNumS72().toBigInteger());
			if (ddmRevocato.getNumeroGiorniRevocaLA() != null)
				ddr.setNumeroGiorniRevocaLA(ddmRevocato.getNumeroGiorniRevocaLA().toBigInteger());
			else
				ddr.setNumeroGiorniRevocaLA(null);
			// da decodificare A (Annullato), S (Validato), N (Da Validare)
			ddr.setStatoProvvedimento(em.getFlagDocumentoRegistrato());
			if (isFascicoloOrigineRevocato) {
				if (fgpmOrigineRevocato.getFascicoloSiusModel().getChiaveAnno() != null)
					ddr.setAnnoProcRevocato(fgpmOrigineRevocato.getFascicoloSiusModel().getChiaveAnno()
							.toBigInteger());
				else
					ddr.setAnnoProcRevocato(null);
				if (fgpmOrigineRevocato.getFascicoloSiusModel().getChiaveProgr() != null)
					ddr.setNumeroProcRevocato(fgpmOrigineRevocato.getFascicoloSiusModel().getChiaveProgr()
							.toBigInteger());
				else
					ddr.setNumeroProcRevocato(null);
				ddr.setDescrOggettoProcedimento(fgpmOrigineRevocato.getGeneraleProcedimentoModel()
						.getDescrOggettoProcedimento());
				ddr.setDescrTipoUfficio(fgpmOrigineRevocato.getFascicoloSiusModel().getDescrTipoUfficio());
				ddr.setDescrComuneUfficio(fgpmOrigineRevocato.getFascicoloSiusModel().getDescrComuneUfficio());
				// devo settare anche il fascicolo origine
				if (PropertyUtil.isPresent(fgpmOrigineRevocato)) {
					DatiFascicoloOrigine fascOrigin = new DatiFascicoloOrigine();
					fascOrigin = DecretoMapper.mapDatiFascicoloOrigine(fgpmOrigineRevocato);
					dd.setDatiFascicoloOrigine(fascOrigin);
				}
			} else {
				if (fgpmRevocato.getFascicoloSiusModel().getChiaveAnno() != null)
					ddr.setAnnoProcRevocato(fgpmRevocato.getFascicoloSiusModel().getChiaveAnno()
							.toBigInteger());
				else
					ddr.setAnnoProcRevocato(null);
				if (fgpmRevocato.getFascicoloSiusModel().getChiaveProgr() != null)
					ddr.setNumeroProcRevocato(fgpmRevocato.getFascicoloSiusModel().getChiaveProgr()
							.toBigInteger());
				else
					ddr.setNumeroProcRevocato(null);
				ddr.setDescrOggettoProcedimento(fgpmRevocato.getGeneraleProcedimentoModel()
						.getDescrOggettoProcedimento());
				ddr.setDescrTipoUfficio(fgpmRevocato.getFascicoloSiusModel().getDescrTipoUfficio());
				ddr.setDescrComuneUfficio(fgpmRevocato.getFascicoloSiusModel().getDescrComuneUfficio());
			}
			List<PERMESSOTYPE> lpt = DecretoMapper.mapPermessi(permessiRev);
			if (lpt != null && !lpt.isEmpty())
				ddr.getElencoPermessi().addAll(lpt);
			dd.setDECRETOREVOCATO(ddr);
		}

		// valore di ritorno
		return dd;
	}

	private static DATIDECRETOUNIFICANTE mapDatiDecretoUnificante(FascicoloGPModel lFasUnificante,
			FascicoloGPModel lFasUnificato) {
		DATIDECRETOUNIFICANTE datiUnificazione = new DATIDECRETOUNIFICANTE();
		datiUnificazione
				.setCognomeSoggetto(lFasUnificante.getFascicoloSiusModel().getSoggetto().getCognome());
		datiUnificazione.setNomeSoggetto(lFasUnificante.getFascicoloSiusModel().getSoggetto().getNome());
		datiUnificazione.setDataNascita(Mapper.creaDataTypeDecreto(lFasUnificante.getFascicoloSiusModel()
				.getSoggetto().getDataNascita()));
		if (lFasUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-") == 0) {
			datiUnificazione.setLuogoNascita(lFasUnificante.getFascicoloSiusModel().getSoggetto()
					.getDescrStatoNascita());
		} else {
			datiUnificazione.setLuogoNascita(lFasUnificante.getFascicoloSiusModel().getSoggetto()
					.getDescrComuneNascita()
					+ "  ("
					+ lFasUnificante.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()
					+ ")");
		}

		datiUnificazione.setNumeroSIUS(lFasUnificante.getFascicoloSiusModel().getChiaveAnno() + "/"
				+ lFasUnificante.getFascicoloSiusModel().getChiaveProgr());
		datiUnificazione.setNumeroSIEP(lFasUnificante.getFascicoloSiusModel().getChiaveAnnoSIEP() + "/"
				+ lFasUnificante.getFascicoloSiusModel().getChiaveProgrSIEP());
		datiUnificazione.setDataUnificazione(Mapper.creaDataTypeDecreto(lFasUnificato.getFascicoloSiusModel()
				.getDataDefinizione()));
		datiUnificazione.setFascicoloUnificato(lFasUnificato.getFascicoloSiusModel().getChiaveAnno()
				.toString()
				+ "/" + lFasUnificato.getFascicoloSiusModel().getChiaveProgr().toString());
		int lSize = lFasUnificante.getTenori().length;
		for (int x = 0; x < lSize; x++) {
			String oggetto = lFasUnificante.getTenori()[x].getDescrOggettoTenore();
			datiUnificazione.getElencoOggetti().add(oggetto);
		}

		return datiUnificazione;
	}

	/**
	 * Metodo per la mappatura dei dati delle licenze
	 * 
	 * @param permessi
	 * @return List<LICENZATYPE>
	 */
	private static List<LICENZATYPE> mapLicenze(Vector licenzePeriodi) {
		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapLicenze");

		// instanzio un oggetto di tipo "ArrayList"
		List<LICENZATYPE> lpt = null;
		if (licenzePeriodi != null && !licenzePeriodi.isEmpty()) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<LICENZATYPE>(licenzePeriodi.size());
			for (int i = 0; i < licenzePeriodi.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "LicenzaLibAnticipataModel"
				LicenzaLibAnticipataModel licenzalibanticipata = (LicenzaLibAnticipataModel) licenzePeriodi
						.get(i);
				// instanzio ed inizializzo un oggetto di tipo "LICENZATYPE"
				LICENZATYPE licenzaT = new LICENZATYPE();
				licenzaT.setCodiTipoLicenza(licenzalibanticipata.getCodTipoLicenza());
				licenzaT.setNumeGiorni(licenzalibanticipata.getNumeroGiorni() != null ? new BigInteger(
						licenzalibanticipata.getNumeroGiorni().toString()) : null);
				licenzaT.setNumeMesi(licenzalibanticipata.getNumeroMesi() != null ? new BigInteger(
						licenzalibanticipata.getNumeroMesi().toString()) : null);
				licenzaT.setNumeOre(licenzalibanticipata.getNumeroOre() != null ? new BigInteger(
						licenzalibanticipata.getNumeroOre().toString()) : null);
				licenzaT.setDataInizio(Mapper.creaDataTypeDecreto(licenzalibanticipata.getDataInizio()));
				licenzaT.setDataFine(Mapper.creaDataTypeDecreto(licenzalibanticipata.getDataFine()));
				licenzaT.setOraInizio(licenzalibanticipata.getOraInizio());
				licenzaT.setOraFine(licenzalibanticipata.getOraFine());
				licenzaT.setLuogoSvolgimentoProva(licenzalibanticipata.getLuogoSvolgimentoProva());
				licenzaT.setSommaRisarcDanni(licenzalibanticipata.getSommaRisarcDanni());
				licenzaT.setDescrStatoPermesso(licenzalibanticipata.getDescrStatoPermesso());
				// aggiungo alla lista di ritorno
				lpt.add(licenzaT);
			}
		}
		// valore di ritorno
		return lpt;
	}

	/**
	 * Metodo per la mappatura dei dati del procedimento
	 * 
	 * @param fgpm
	 * @param fspm
	 * @param mrm
	 * @return DATIRIEPILOGOPROCEDIMENTO
	 */
	public static DATIRIEPILOGOPROCEDIMENTO mapDatiRiepilogoProcedimento(FascicoloGPModel fgpm,
			FascicoloSiepModel fspm, MagistratoRelatoreModel mrm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDatiRiepilogoProcedimento");

		// instanzio ed inizializzo un oggetto di tipo "DATIRIEPILOGOPROCEDIMENTO"
		DATIRIEPILOGOPROCEDIMENTO drp = new DATIRIEPILOGOPROCEDIMENTO();
		// dati del soggetto
		SoggettoModel sm = (PropertyUtil.isPresent(fgpm)) ? fgpm.getFascicoloSiusModel().getSoggetto() : null;
		// dati del fascicolo SIUS
		FascicoloSiusModel fssm = (PropertyUtil.isPresent(fgpm)) ? fgpm.getFascicoloSiusModel() : null;
		// dati del magistrato
		MagistratoModel mm = (PropertyUtil.isPresent(mrm)) ? mrm.getMagistrato() : null;
		// dati del procedimento
		GeneraleProcedimentoModel gpm = (PropertyUtil.isPresent(fgpm))
				? fgpm.getGeneraleProcedimentoModel()
				: null;

		if (fspm != null) {
			if (fspm.getChiaveAnno() != null)
				drp.setAnnoFascicoloSIEP(fspm.getChiaveAnno().toBigInteger());
			else
				drp.setAnnoFascicoloSIEP(null);
			if (fspm.getChiaveProgr() != null)
				drp.setNumeroFascicoloSIEP(fspm.getChiaveProgr().toBigInteger());
			else
				drp.setNumeroFascicoloSIEP(null);
			drp.setDataFascicoloSIEP(Mapper.creaDataTypeDecreto(fspm.getDataIscrizione()));
			drp.setUfficioFascicoloSIEP(fspm.getDescrTipoUfficio());
			if (PropertyUtil.isPresent(fspm.getDescrTipoUfficio())
					&& PropertyUtil.isPresent(fspm.getDescrComuneUfficio()))
				drp.setUfficioFascicoloSIEP(fspm.getDescrTipoUfficio() + " " + fspm.getDescrComuneUfficio());
		} else {
			drp.setAnnoFascicoloSIEP(null);
			drp.setNumeroFascicoloSIEP(null);
			drp.setDataFascicoloSIEP(null);
			drp.setUfficioFascicoloSIEP(null);
		}

		if (fssm != null) {
			if (fssm.getChiaveAnno() != null)
				drp.setAnnoProcedimentoSIUS(fssm.getChiaveAnno().toBigInteger());
			else
				drp.setAnnoProcedimentoSIUS(null);
			if (fssm.getChiaveProgr() != null)
				drp.setNumeroProcedimentoSIUS(fssm.getChiaveProgr().toBigInteger());
			else
				drp.setNumeroProcedimentoSIUS(null);
			drp.setCodiceStatoFascicolo(fssm.getCodStatoFascicolo());
			drp.setDescrStatoFascicolo(fssm.getDescrStatoFascicolo());
		} else {
			drp.setAnnoProcedimentoSIUS(null);
			drp.setNumeroProcedimentoSIUS(null);
			drp.setCodiceStatoFascicolo(null);
			drp.setDescrStatoFascicolo(null);
		}

		if (mm != null) {
			drp.setCognomeMagistratoRelatore(mm.getCognome());
			drp.setNomeMagistratoRelatore(mm.getNome());
		} else {
			drp.setCognomeMagistratoRelatore(null);
			drp.setNomeMagistratoRelatore(null);
		}

		if (fgpm != null) {
			if (PropertyUtil.isPresent(fgpm.getUdiPro()))
				drp.setFlagRinviata(fgpm.getUdiPro().getFlagRinviata());
		} else {
			drp.setFlagRinviata(null);
		}

		if (gpm != null) {
			drp.setDataUdienza(Mapper.creaDataTypeDecreto(gpm.getDataCameraConsiglio()));
			drp.setOggettoProcedimentoSIUS(gpm.getDescrOggettoProcedimento());
		} else {
			drp.setDataUdienza(null);
			drp.setOggettoProcedimentoSIUS(null);
		}

		// SOGGETTO
		if (sm != null) {
			drp.setCodiceProvincia(sm.getCodProvinciaNascita());
			drp.setCognomeSoggetto(sm.getCognome());
			drp.setDataNascita(Mapper.creaDataTypeDecreto(sm.getDataNascita()));
			String luogoNascita = sm.getDescrComuneNascita();
			if (!PropertyUtil.isPresent(sm.getCodProvinciaNascita())
					|| "-".equals(sm.getCodProvinciaNascita()))
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
		} else {
			drp.setCodiceProvincia(null);
			drp.setCognomeSoggetto(null);
			drp.setDataNascita(null);
			drp.setLuogoNascita(null);
			drp.setNomeSoggetto(null);
			drp.setSesso(null);
			drp.setEtaPresuntaAnni(null);
			drp.setEtaPresuntaMesi(null);
		}

		// valore di ritorno
		return drp;
	}

	/**
	 * Metodo per la mappatura dei dati dei destinatari
	 * 
	 * @param destinatari
	 * @param csm
	 * @return List<DESTINATARIOTYPE>
	 */
	public static List<DESTINATARIOTYPE> mapDestinatari(Vector destinatari, CuratoreSiusModel csm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDestinatari");

		// instanzio e valorizzo un oggetto di tipo "CuratoreModel"
		CuratoreModel cm = null;
		if (csm != null && csm.getCuratore() != null)
			cm = csm.getCuratore();
		// instanzio un oggetto di tipo "ArrayList"
		List<DESTINATARIOTYPE> ldt = null;
		if (destinatari != null && !destinatari.isEmpty()) {
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
				if (cm != null) {
					dt.setCuratore(cm.getCognome() + " " + cm.getNome());
					dt.setTipoCuratore(cm.getDescrFlagStato());
				} else {
					dt.setCuratore(null);
					dt.setTipoCuratore(null);
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
	 * @return List<TENORETYPE>
	 */
	public static List<TENORETYPE> mapTenori(Vector tenori) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapTenori");

		// instanzio un oggetto di tipo "ArrayList"
		List<TENORETYPE> ltt = null;
		if (tenori != null && !tenori.isEmpty()) {
			// inizializzo l'oggetto di tipo "ArrayList"
			ltt = new ArrayList<TENORETYPE>(tenori.size());
			for (int i = 0; i < tenori.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "TenoreModel"
				TenoreModel tm = (TenoreModel) tenori.get(i);
				// instanzio ed inizializzo un oggetto di tipo "TENORETYPE"
				TENORETYPE tt = new TENORETYPE();
				tt.setDescrEsitoTenore(tm.getDescrEsitoTenore());
				tt.setDescrOggettoTenore(tm.getDescrOggettoTenore());
				tt.setCodOggettoTenore(tm.getCodOggettoTenore());
				// aggiungo alla lista di ritorno
				ltt.add(tt);
			}
		}

		// valore di ritorno
		return ltt;
	}

	/**
	 * Metodo per la mappatura dei dati delle motivazioni del decreto
	 * 
	 * @param motivazioniDecreto
	 * @return List<MOTIVAZIONITYPE>
	 */
	public static List<MOTIVAZIONITYPE> mapMotivazioni(MotivazioneDecretoModel[] motivazioniDecreto) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapMotivazioni");

		// instanzio ed inizializzo un oggetto di tipo "ArrayList"
		List<MOTIVAZIONITYPE> lmt = null;
		if (motivazioniDecreto != null && motivazioniDecreto.length > 0) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lmt = new ArrayList<MOTIVAZIONITYPE>(motivazioniDecreto.length);
			for (int i = 0; i < motivazioniDecreto.length; i++) {
				// instanzio ed valorizzo un oggetto di tipo "MotivazioneDecretoModel"
				MotivazioneDecretoModel mdm = (MotivazioneDecretoModel) motivazioniDecreto[i];
				// instanzio ed inizializzo un oggetto di tipo "MOTIVAZIONITYPE"
				MOTIVAZIONITYPE mt = new MOTIVAZIONITYPE();
				mt.setDescrAltreMotivazioni(mdm.getAltraMotivazione());
				mt.setDescrMotivazioniDecreto(mdm.getDescrMotivazione());
				// aggiungo alla lista di ritorno
				lmt.add(mt);
			}
		}

		// valore di ritorno
		return lmt;
	}

	/**
	 * Metodo per la mappatura dei dati dei permessi
	 * 
	 * @param permessi
	 * @return List<PERMESSOTYPE>
	 */
	public static List<PERMESSOTYPE> mapPermessi(Vector permessi) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapPermessi");

		// instanzio un oggetto di tipo "ArrayList"
		List<PERMESSOTYPE> lpt = null;
		if (permessi != null && !permessi.isEmpty()) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<PERMESSOTYPE>(permessi.size());
			for (int i = 0; i < permessi.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "LicenzaLibAnticipataModel"
				LicenzaLibAnticipataModel pm = (LicenzaLibAnticipataModel) permessi.get(i);
				// instanzio ed inizializzo un oggetto di tipo "PERMESSOTYPE"
				PERMESSOTYPE pt = new PERMESSOTYPE();
				pt.setDescrStatoPermesso(pm.getDescrStatoPermesso());
				pt.setDescrTipoPermesso(pm.getDescrTipoLicenza());
				pt.setDurataPermesso(Mapper.creaDurataTypeDecreto(pm.getNumeroGiorni(), pm.getNumeroMesi(),
						null, pm.getNumeroOre()));
				pt.setFlagScorta(pm.getFlagScorta());
				pt.setLuogoSvolgimentoProva(pm.getLuogoSvolgimentoProva());
				pt.setNote(pm.getAnnotazione());
				// aggiungo alla lista di ritorno
				lpt.add(pt);
			}
		}

		// valore di ritorno
		return lpt;
	}

	/**
	 * Metodo per la mappatura dei dati delle prescrizioni
	 * 
	 * @param prescrizioni
	 * @return List<PRESCRIZIONETYPE>
	 */
	public static List<PRESCRIZIONETYPE> mapPrescrizioni(Vector prescrizioni) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapPrescrizioni");

		// instanzio un oggetto di tipo "ArrayList"
		List<PRESCRIZIONETYPE> lpt = null;
		if (prescrizioni != null && !prescrizioni.isEmpty()) {
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
	 * Metodo per la mappatura dei dati dei difensori
	 * 
	 * @param difensori
	 * @return List<AVVOCATOTYPE>
	 */
	public static List<AVVOCATOTYPE> mapDifensori(Vector difensori) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDifensori");

		// instanzio un oggetto di tipo "ArrayList"
		List<AVVOCATOTYPE> lat = null;
		if (difensori != null && !difensori.isEmpty()) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lat = new ArrayList<AVVOCATOTYPE>(difensori.size());
			for (int i = 0; i < difensori.size(); i++) {
				// instanzio oggetti di tipo "AvvocatoModel" e "AvvocatoSiusModel"
				AvvocatoModel am = null;
				AvvocatoSiusModel asm = null;
				// instanzio ed inizializzo un oggetto di tipo "AVVOCATOTYPE"
				AVVOCATOTYPE at = new AVVOCATOTYPE();
				// verifica dell'instanza dell'oggetto
				if (difensori.get(i) instanceof AvvocatoModel) {
					// valorizzo l'oggetto di tipo "AvvocatoModel"
					am = (AvvocatoModel) difensori.get(i);
					at.setCodiceFiscale(am.getCodiceFiscale());
					at.setCognome(am.getCognome());
					at.setDescrTipo(am.getForo() + "#" + am.getIndirizzo() + "#" + am.getDescrTipo());
					at.setNome(am.getNome());
				} else if (difensori.get(i) instanceof AvvocatoSiusModel) {
					// valorizzo l'oggetto di tipo "AvvocatoSiusModel"
					asm = (AvvocatoSiusModel) difensori.get(i);
					if (PropertyUtil.isPresent(asm.getAvvocato())) {
						at.setCodiceFiscale(asm.getAvvocato().getCodiceFiscale());
						at.setCognome(asm.getAvvocato().getCognome());
						at.setDescrTipo(asm.getAvvocato().getForo() + "#" + asm.getAvvocato().getIndirizzo()
								+ "#" + asm.getAvvocato().getDescrTipo());
						at.setNome(asm.getAvvocato().getNome());
					}
				}

				// aggiungo alla lista di ritorno
				lat.add(at);
			}
		}

		// valore di ritorno
		return lat;
	}

	/**
	 * Metodo per la mappatura dei dati delle licenze
	 * 
	 * @param permessi
	 * @return List<LICENZATYPE>
	 */
	private static List<LICENZATYPE> mapLicenzePeriodi(Vector licenzePeriodi) {
		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapLicenzePeriodi");

		// instanzio un oggetto di tipo "ArrayList"
		List<LICENZATYPE> lpt = null;
		if (licenzePeriodi != null && !licenzePeriodi.isEmpty()) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<LICENZATYPE>(licenzePeriodi.size());
			for (int i = 0; i < licenzePeriodi.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "LicenzaPeriodiLibAnticipataModel"
				LicenzaPeriodiLibAnticipataModel licenzalibanticipata = (LicenzaPeriodiLibAnticipataModel) licenzePeriodi
						.get(i);
				// instanzio ed inizializzo un oggetto di tipo "LICENZATYPE"
				LICENZATYPE licenzaT = new LICENZATYPE();
				licenzaT.setCodiTipoLicenza(licenzalibanticipata.getLicenza().getCodTipoLicenza());
				licenzaT.setNumeGiorni(licenzalibanticipata.getLicenza().getNumeroGiorni() != null
						? new BigInteger(licenzalibanticipata.getLicenza().getNumeroGiorni().toString())
						: null);
				licenzaT.setNumeMesi(licenzalibanticipata.getLicenza().getNumeroMesi() != null
						? new BigInteger(licenzalibanticipata.getLicenza().getNumeroMesi().toString())
						: null);
				licenzaT.setNumeOre(licenzalibanticipata.getLicenza().getNumeroOre() != null
						? new BigInteger(licenzalibanticipata.getLicenza().getNumeroOre().toString())
						: null);
				licenzaT.setDataInizio(Mapper.creaDataTypeDecreto(licenzalibanticipata.getLicenza()
						.getDataInizio()));
				licenzaT.setDataFine(Mapper.creaDataTypeDecreto(licenzalibanticipata.getLicenza()
						.getDataFine()));
				licenzaT.setOraInizio(licenzalibanticipata.getLicenza().getOraInizio());
				licenzaT.setOraFine(licenzalibanticipata.getLicenza().getOraFine());
				licenzaT.setLuogoSvolgimentoProva(licenzalibanticipata.getLicenza()
						.getLuogoSvolgimentoProva());
				licenzaT.setSommaRisarcDanni(licenzalibanticipata.getLicenza().getSommaRisarcDanni());
				licenzaT.setDescrStatoPermesso(licenzalibanticipata.getLicenza().getDescrStatoPermesso());
				// aggiungo alla lista di ritorno
				lpt.add(licenzaT);
			}
		}
		// valore di ritorno
		return lpt;
	}

	/**
	 * Metodo per la mappatura dei dati di revoca liberta' anticipata
	 * 
	 * @param licenzePeriodi
	 * @return DatiRevocaLibertaAnticipata
	 */
	public static DatiRevocaLibertaAnticipata mapDatiRevocaLibertaAnticipata(Vector licenzePeriodi) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDatiRevocaLibertaAnticipata");

		// instanzio ed inizializzo un oggetto di tipo "DatiRevocaLibertaAnticipata"
		DatiRevocaLibertaAnticipata dati = new DatiRevocaLibertaAnticipata();
		// aggiungo elementi all'oggetto di tipo "DatiRevocaLibertaAnticipata"
		dati.setDescrDecisione(null);
		dati.setDescrUfficioMagistratoCompetente(null);
		List<PERIODILIBERTAANTICIPATATYPE> lplat = mapPeriodiLA(licenzePeriodi);
		if (PropertyUtil.isPresent(lplat))
			dati.getLicenzaPeriodiLibertaAnticipata().addAll(lplat);

		// valore di ritorno
		return dati;
	}

	/**
	 * Metodo per la mappatura dei dati dei Periodi di Libertà Anticipata
	 * 
	 * @param periodiLA
	 * @return List<PERIODILIBERTAANTICIPATATYPE>
	 */
	private static List<PERIODILIBERTAANTICIPATATYPE> mapPeriodiLA(Vector periodiLA) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapPeriodiLA");

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
				List<PERIODITYPE> lpt = mapPeriodi(lplam.getPeriodi());
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
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapPeriodi");

		// instanzio un oggetto di tipo "ArrayList"
		List<PERIODITYPE> lpt = null;
		if (plamArray != null && plamArray.length > 0) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lpt = new ArrayList<PERIODITYPE>(plamArray.length);
			for (int i = 0; i < plamArray.length; i++) {
				PeriodoLibAnticipataModel plam = (PeriodoLibAnticipataModel) plamArray[i];
				// instanzio ed inizializzo un oggetto di tipo "PERIODITYPE"
				PERIODITYPE pt = new PERIODITYPE();
				pt.setDataFine(Mapper.creaDataTypeDecreto(plam.getDataFine()));
				pt.setDataInizio(Mapper.creaDataTypeDecreto(plam.getDataInizio()));
				pt.setFlagConcesso(plam.getFlagConcesso());
				// aggiungo alla lista di ritorno
				lpt.add(pt);
			}
		}

		// valore di ritorno
		return lpt;
	}

	/**
	 * Valorizza i campi dell'oggetto DatiFascicoloOrigine
	 * 
	 * @param fgpmOrigine
	 * @return
	 */
	public static DatiFascicoloOrigine mapDatiFascicoloOrigine(FascicoloGPModel fgpmOrigine) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DecretoMapper, metodo: mapDatiFascicoloOrigine");

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
				dati.setDataCameraConsiglio(Mapper.creaDataTypeDecreto(gpm.getDataCameraConsiglio()));
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
				dati.setDataNascita(Mapper.creaDataTypeDecreto(soggetto.getDataNascita()));
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

}