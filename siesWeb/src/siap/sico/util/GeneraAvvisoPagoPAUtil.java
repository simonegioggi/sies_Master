package siap.sico.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.SecurityException;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento;
import it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe di utilità per il ws di generazione avviso pagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class GeneraAvvisoPagoPAUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public static RichiestaPagamentoTelematico caricaDatiRichiestaPagamentoTelematico(UfficioModel ufm)
			throws F3BException {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiRichiestaPagamentoTelematico");

		RichiestaPagamentoTelematico rpt = new RichiestaPagamentoTelematico();
		rpt.setAutenticazioneSoggetto("OTH");
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		String[] codici = ibp.ExRicercaCodiciUfficiProduzione(ufm.getCodUfficio());
		rpt.setCodiceUfficio(codici[0]);
		// "GLTO" esempio di codice distretto
		rpt.setCodiceDistretto(codici[1]);
		// DataScadenza OBBLIGATORIA, altrimenti 30 giorni in automatico
		// la imposto nel metodo chiamante
		// rpt.setDataScadenza(null);
		return rpt;
	}

	public static DatiVersamento caricaDatiVersamento(SoggettoModel sm, BollettinoPagopaModel bpm)
			throws SecurityException {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiVersamento");

		DatiVersamento dv = new DatiVersamento();
		dv.setBicAddebito(null);
		// DatiSingoloVersamento[] dsv = caricaDatiSingoloVersamento(sm, bpm);
		DatiSingoloVersamento[] dsvs = new DatiSingoloVersamento[1];
		// dati singolo versamento
		DatiSingoloVersamento dsv = new DatiSingoloVersamento();
		String importo = (Utils.isNullObj(bpm.getImportoRata())) ? "" : bpm.getImportoRata().toString();
		dsv.setImporto(new BigDecimal(importo));
		String causale = "Sanzione pecuniaria"; // ex "Pagamenti in favore Amministrazione"
		dsv.setCausale("/" + importo + "/TXT/" + causale);
		dsv.setDatiSpecificiRiscossione("PENPE");
		dsv.setDatiMarcaBolloDigitale(null);
		dsvs[0] = dsv;
		dv.setDatiSingoloVersamento(dsvs);
		// for (int i = 0; i < dsv.length; i++)
		// dv.setDatiSingoloVersamento(i, dsv[i]); // da 1 a 5 occorrenze
		// IbanAddebito: da non valorizzare nel caso in cui il file debba essere usato in generaAvviso()
		dv.setImportoTotale(dsvs[0].getImporto());
		dv.setIbanAddebito(null);
		return dv;
	}

	// private static DatiSingoloVersamento[] caricaDatiSingoloVersamento(SoggettoModel sm,
	// BollettinoPagopaModel bpm) throws SecurityException {
	//
	// // info per il log
	// siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiSingoloVersamento");
	//
	// DatiSingoloVersamento[] dsv = new DatiSingoloVersamento[1]; // da 1 a 5 occorrenze???
	// dsv[0] = new DatiSingoloVersamento();
	// String causale = "Sanzione pecuniaria";
	// // se DatiMarcaBolloDigitale è valorizzato allora l'importo è di 16.00
	// // DatiMarcaBolloDigitale dmbd = caricaDatiMarcaBolloDigitale(sm);
	// dsv[0].setDatiMarcaBolloDigitale(null);
	// dsv[0].setDatiSpecificiRiscossione("PENPE"); // valore fisso
	// // Il valore dell'importo deve contenere obbligatoriamente le due cifre decimali con
	// // separatore il '.' --> 12345678.90 nel DB abbiamo 10000,00 deve essere 10000.00
	// String importo = (Utils.isNullObj(bpm.getImportoRata())) ? "" : bpm.getImportoRata().toString();
	// if (Utils.isPresent(importo)) {
	// if (importo.contains(","))
	// importo = importo.replace(",", ".");
	// else
	// importo += ".00";
	// }
	// dsv[0].setImporto(new BigDecimal(importo));
	// dsv[0].setCausale("/" + importo + "/TXT/" + causale); // MAX 100 chars
	// return dsv;
	// }

	// private static DatiMarcaBolloDigitale caricaDatiMarcaBolloDigitale(SoggettoModel sm)
	// throws SecurityException {
	//
	// // info per il log
	// siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiMarcaBolloDigitale");
	//
	// DatiMarcaBolloDigitale dmbd = new DatiMarcaBolloDigitale();
	// // contiene l'impronta informatica (digest), rappresentata in "base 64 binary", del documento
	// // informatico o della segnatura di procollo cui è associata la marca da bollo digitale.
	// // algoritmo di hash da utilizzare è SHA-256
	// MessageDigest md = null;
	// String hd = "MarcaBolloDigitale";
	// String hdCripted = null;
	// try {
	// md = MessageDigest.getInstance("SHA-256");
	// hdCripted = new String(Base64.encode(md.digest(hd.getBytes())));
	// } catch (Exception ex) {
	// throw new SecurityException("Errore durante il crypting del digest");
	// }
	// dmbd.setHashDocumento(hdCripted);
	// dmbd.setProvinciaResidenza(sm.getCodProvinciaNascita());
	// dmbd.setTipoBollo("01");
	// return dmbd;
	// }

	public static AnagraficaSoggetto caricaDatiAnagraficaSoggetto(SoggettoModel sm) {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiAnagraficaSoggetto");

		AnagraficaSoggetto as = new AnagraficaSoggetto();
		as.setCap(null);
		as.setCivico(null);
		// C.F. or P.I. (OBBLIGATORIO)
		as.setCodiceIdentificativoUnivoco(sm.getCodFiscale());
		as.setEmail(null);
		as.setIndirizzo(null);
		as.setLocalita(null);
		as.setNaturaGiuridica("F"); // F or G
		as.setNazione(null); // sm.getCodStatoNascita() questo non lo accetta il WS
		as.setNominativo(sm.getCognome() + " " + sm.getNome()); // MAX 70 chars
		as.setProvincia(sm.getCodProvinciaNascita());
		as.setRegione(sm.getCodComuneNascita());
		return as;
	}

	// MEV_2023-33: cambiata firma del metodo con la data Emissione OEIP
	public static BollettinoPagopaModel popolaBollettino(RateizzazionePPModel rata, String codUtente,
			String codUfficio, String statoPagamento, int cont, Date dataEmissioneOI) {

		BollettinoPagopaModel bpm = new BollettinoPagopaModel();
		bpm.setCodOperatoreInserimento(codUtente);
		bpm.setCodUfficioInserimento(codUfficio);
		bpm.setDataInserimento(DateUtils.getSysDate());
		// bpm.setDataAvvPagamento(null);
		// bpm.setDataScadenza(null);
		// bpm.setDataScadenzaRich(DateUtils.getDate("31/12/2049", "dd/MM/yyyy"));
		if ("R".equals(rata.getTipoRateizzazione()))
			bpm.setDataScadenzaRich(
					DateUtils.moveDateTo(dataEmissioneOI, java.util.Calendar.DAY_OF_MONTH, 60));
		else
			bpm.setDataScadenzaRich(
					DateUtils.moveDateTo(dataEmissioneOI, java.util.Calendar.DAY_OF_MONTH, 120));
		bpm.setFasSieIdFascicolSiep(rata.getFasSieIdFascicoloSiep());
		// bpm.setImportoPagato(null);
		// bpm.setIuv(null);
		bpm.setNumeroRate(rata.getNumeroRate().intValue());
		bpm.setProgRata(cont);
		bpm.setRatIdRateizzazionePP(rata.getIdRateizzazionePP());
		bpm.setStatoPagamento(statoPagamento);
		bpm.setTipoRateizzazione(rata.getTipoRateizzazione());
		// MEV_2023-33: metto sempre importo rata poichè si può inserire un importo diverso dal totale da pagare
		// if ("U".equals(rata.getTipoRateizzazione()))
		// bpm.setImportoRata(rata.getImportoDaPagare());
		// else
		bpm.setImportoRata(rata.getImportoRata());

		// valore di ritorno
		return bpm;
	}

}