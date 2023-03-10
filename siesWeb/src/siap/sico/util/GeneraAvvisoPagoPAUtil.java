/**
 *
 */
package siap.sico.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;

import f3b.log.LogF3B;
import f3b.security.SecurityException;
import f3b.util.DateUtils;
import f3b.util.Utils;
import it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento;
import it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;

/**
 * Classe di utilità per il ws di generazione avviso pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class GeneraAvvisoPagoPAUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public static RichiestaPagamentoTelematico caricaDatiRichiestaPagamentoTelematico(UfficioModel ufm) {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiRichiestaPagamentoTelematico");

		RichiestaPagamentoTelematico rpt = new RichiestaPagamentoTelematico();
		rpt.setAutenticazioneSoggetto("OTH");
		rpt.setCodiceDistretto(/*ufm.getCodDistretto()*/"GLTO");
		rpt.setCodiceUfficio(ufm.getCodUfficio());
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtils.getSysDate());
		rpt.setDataScadenza(c); // OBBLIGATORIA, altrimenti 30 giorni in automatico
		return rpt;
	}

	public static DatiVersamento caricaDatiVersamento(SoggettoModel sm, DettaglioPenaComplessivaModel dpcm)
			throws SecurityException {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiVersamento");

		DatiVersamento dv = new DatiVersamento();
		dv.setBicAddebito(null);
		DatiSingoloVersamento[] dsv = caricaDatiSingoloVersamento(sm, dpcm);
		dv.setDatiSingoloVersamento(dsv);
		for (int i = 0; i < dsv.length; i++)
			dv.setDatiSingoloVersamento(i, dsv[i]); // da 1 a 5 occorrenze
		// IbanAddebito: da non valorizzare nel caso in cui il file debba essere usato in generaAvviso()
		dv.setIbanAddebito(null);
		dv.setImportoTotale(new BigDecimal(16.00)); // se DatiMarcaBolloDigitale è valorizzato
		return dv;
	}

	private static DatiSingoloVersamento[] caricaDatiSingoloVersamento(SoggettoModel sm,
			DettaglioPenaComplessivaModel dpcm) throws SecurityException {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiSingoloVersamento");

		DatiSingoloVersamento[] dsv = new DatiSingoloVersamento[5]; // da 1 a 5 occorrenze
		dsv[0] = new DatiSingoloVersamento();
		String causale = "Pagamenti in favore Amministrazione";
		// se DatiMarcaBolloDigitale è valorizzato allora l'importo è di 16.00
		DatiMarcaBolloDigitale dmbd = caricaDatiMarcaBolloDigitale(sm);
		dsv[0].setDatiMarcaBolloDigitale(dmbd);
		dsv[0].setDatiSpecificiRiscossione("PENPE"); // valore fisso
		// Il valore dell'importo deve contenere obbligatoriamente le due cifre decimali con
		// separatore il '.' --> 12345678.90
		dsv[0].setImporto(calcolaImporto(dpcm));
		dsv[0].setCausale("/" + dsv[0].getImporto() + "/TXT/" + causale); // MAX 100 chars
		return dsv;
	}

	private static BigDecimal calcolaImporto(DettaglioPenaComplessivaModel dpcm) {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".calcolaImporto");

		BigDecimal importo = null;
		if (!Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva())) {
			if (!Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva())) {
				importo = !Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva()
						.getSanzioneSostitutiva().getSanzionePecuniariaMulta())
								? dpcm.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva()
										.getSanzionePecuniariaMulta()
								: dpcm.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva()
										.getSanzionePecuniariaAmmenda();
			} else if (!Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva())) {
				importo = !Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva())
						? dpcm.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getImportoMulta()
						: dpcm.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva()
								.getImportoAmmenda();
			}
		}

		return importo;
	}

	private static DatiMarcaBolloDigitale caricaDatiMarcaBolloDigitale(SoggettoModel sm)
			throws SecurityException {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiMarcaBolloDigitale");

		DatiMarcaBolloDigitale dmbd = new DatiMarcaBolloDigitale();
		// contiene l'impronta informatica (digest), rappresentata in "base 64 binary", del documento
		// informatico o della segnatura di procollo cui è associata la marca da bollo digitale.
		// algoritmo di hash da utilizzare è SHA-256
		MessageDigest md = null;
		String hd = "MarcaBolloDigitale";
		String hdCripted = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			hdCripted = new String(Base64.encode(md.digest(hd.getBytes())));
		} catch (Exception ex) {
			throw new SecurityException("Errore durante il crypting del digest");
		}
		dmbd.setHashDocumento(hdCripted);
		dmbd.setProvinciaResidenza(sm.getCodProvinciaNascita());
		dmbd.setTipoBollo("01");
		return dmbd;
	}

	public static AnagraficaSoggetto caricaDatiAnagraficaSoggetto(SoggettoModel sm) {

		// info per il log
		siesLogger.debug(GeneraAvvisoPagoPAUtil.class.getName() + ".caricaDatiAnagraficaSoggetto");

		AnagraficaSoggetto as = new AnagraficaSoggetto();
		as.setCap(null);
		as.setCivico(null);
		as.setCodiceIdentificativoUnivoco(sm.getCodFiscale()); // C.F. or P.I.
		as.setEmail(null);
		as.setIndirizzo(null);
		as.setLocalita(null);
		as.setNaturaGiuridica("F"); // F or G
		as.setNazione(/*sm.getCodStatoNascita()*/null);
		as.setNominativo(sm.getCognome() + " " + sm.getNome()); // MAX 70 chars
		as.setProvincia(sm.getCodProvinciaNascita());
		as.setRegione(sm.getCodComuneNascita());
		return as;
	}

}