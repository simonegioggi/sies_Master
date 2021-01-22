package siap.siep.modulocumulo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.model.EventoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneCumuloUtils {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Codici delle sosp PM: Comma 5, DL78, L199
	public static List<String> aCodSospC5Provv = new ArrayList<String>(Arrays.asList("0061", "0104", "0063",
			"0105", "0117", "5506", "5507", "5508", "5511", "5512", "5510", "5513"));
	public static List<String> aCodSospC5VVR = new ArrayList<String>(Arrays.asList("0313"));
	public static List<String> aCodSospC5DecIRR = new ArrayList<String>(Arrays.asList("0282"));

	// Nota che il codice 0993 è generico per tutte le istanza. Per quelle del
	// comma 5 va testato il valore NUOVA_ISTANZA.COD_CONTENUTO
	public static List<String> aCodSospIstanza = new ArrayList<String>(Arrays.asList("0993"));

	public static List<String> aCodSospC5Revoca = new ArrayList<String>(
			Arrays.asList("0078", "0079", "0080", "5514", "5515", "5516", "5519", "5520", "5518", "5521"));
	public static List<String> aCodSospC5RevocaSorv = new ArrayList<String>(
			Arrays.asList("9000", "9001", "9002"));

	public static List<String> aCodSospPM78 = new ArrayList<String>(
			Arrays.asList("1022", "1023", "5522", "5523", "5524", "1024"));

	// 199/2010
	public static List<String> aCodSospPM199_Conc = new ArrayList<String>(
			Arrays.asList("0499", "0364", "5527", "5528", "5504", "0365", "0498"));
	public static List<String> aCodSospPM199_Rev = new ArrayList<String>(
			Arrays.asList("0495", "0496", "0497", "5530", "5531", "5532"));
	public static List<String> aCodSospPM199_Sorv = new ArrayList<String>(
			Arrays.asList("9000", "9001", "9002"));
	// "0608"

	// Codici dei Presofferti
	public static List<String> aCodPresofferto = new ArrayList<String>(Arrays.asList("0121"));
	public static List<String> aCodFungibilita = new ArrayList<String>(Arrays.asList("0212", "0213"));

	// Espiazione Pegressa PM
	public static List<String> aCodEspPregressa = new ArrayList<String>(
			Arrays.asList("0900", "0901", "0902", "0903", "0937", "0267", "0270", "0675"));

	// Codici dei Pagamento PP
	public static List<String> aCodPagamentoPP = new ArrayList<String>(Arrays.asList("1007"));

	// MEV70 Codice dell'annotazione Ordinanza/Sentenza Revoca Pena Sosoesa
	public static List<String> aCodRevPenSosp = new ArrayList<String>(
			Arrays.asList("1100", "1101", "1102", "1103", "1104", "1105", "1106", "1107", "1108"));

	// Codici Rideterminazione Pena PM Altro //22/02/2019 MEV70
	public static List<String> aCodRidPenaPMAltroDufficio = new ArrayList<String>(
			Arrays.asList("0948", "0951", "0950", "0949", "1000"));
	public static List<String> aCodRidPenaPMAltroAltAut = new ArrayList<String>(
			Arrays.asList("0953", "0955", "1005"));
	public static List<String> aCodRidPenaPMAltroGE = new ArrayList<String>(
			Arrays.asList("0959", "0956", "0987", "0988", "1006"));
	public static List<String> aCodRidPenaPMAltroSorv = new ArrayList<String>(
			Arrays.asList("0954", "0958", "0957", "0994", "0999"));

	// Codici Rideterminazione Pena Per Revoca MA /02/04/2019 MEV70
	public static List<String> aCodRidPenaPerRevocaMA = new ArrayList<String>(
			Arrays.asList("0015", "0014", "0086", "0316", "2640", "0196"));

	// Codici di Sospensione / Differimento della Pena /16/04/2019 MEV70
	public static List<String> aCodSospDiff = new ArrayList<String>(Arrays.asList("2000", "2001", "2480",
			"2010", "0030", "0031", "0032", "0033", "2011", "0201", "0202"));

	// 09/05/2019 MEV70 Codice delle Ordinanze del GE Revoca Sospensione condizionale della pena.
	public static List<String> aCodOrdGERevSospCondPena = new ArrayList<String>(Arrays.asList("0818"));

	// Codice delle Amnistia/Indulto
	public static List<String> aCodAmnistiaIndulto = new ArrayList<String>(Arrays.asList("0284"));

	// Codice delle Depenalizzazioni
	public static List<String> aCodDepenalizzazione = new ArrayList<String>(Arrays.asList("0285"));

	// Codice delle Incostituzionalità
	public static List<String> aCodIncostituzionalita = new ArrayList<String>(Arrays.asList("0286"));

	// Codice delle Sospensioni GE
	// public static List<String> aCodSospensioneGE = new
	// ArrayList<String>(Arrays.asList("0800","0801","0802","0803","0804","0805","0806","0807","0808","0809","0840"));
	// Codice 0840 soppresso come da richiesta GDL del 04/10/2018
	public static List<String> aCodSospensioneGE = new ArrayList<String>(
			Arrays.asList("0800", "0801", "0802", "0803", "0804", "0805", "0806", "0807", "0808", "0809"));

	// Codice delle Interruzioni GE
	// public static List<String> aCodInterruzioneGE = new
	// ArrayList<String>(Arrays.asList("0266","0267","0268","0269","0270","0366"));
	// Codici 0266 e 0366 soppressi come da richiesta GDL del 04/10/2018
	public static List<String> aCodInterruzioneGE = new ArrayList<String>(
			Arrays.asList("0267", "0268", "0269", "0270"));

	// Codice delle Concessioni Misure Alternative
	public static List<String> aCodConcMisAltSORV = new ArrayList<String>(
			Arrays.asList("0002", "0003", "0001", "2008", "2006", "0013", "0005", "0010", "0012", "2005",
					"0610", "2630", "2245", "2008", "2006", "2005", "0025", "0011"));

	// Codice delle Revoche Misure Alternative
	public static List<String> aCodRevocaMisAltSORV = new ArrayList<String>(
			Arrays.asList("0086", "0014", "0015", "0089", "0087", "0016", "0088", "2270", "0091", "0196",
					"0316", "2640", "0232", "2744", "2747", "2757", "2746"));

	// Codice delle Sospensioni Misure Alternative
	public static List<String> aCodSospMisAltSORV = new ArrayList<String>(
			Arrays.asList("2146", "2147", "2145", "2151", "2153", "2149", "2150", "2152", "2293", "2291",
					"2148", "2280", "2297", "2741", "2742", "2743", "2756"));

	// Codice delle Liberazioni Anticipate (L.A)
	public static List<String> aCodLibAnticipata = new ArrayList<String>(Arrays.asList("0076", "2130", "2131",
			"2132", "0028", "0620", "0621", "2135", "2136", "2137", "0113"));

	// 25/07/2018 Codici di L.A. ed Esiti in Esclusione
	public static List<String> aCodLibAnticipataConc = new ArrayList<String>(
			Arrays.asList("0076", "2130", "2131", "2132", "0028", "0620", "0621", "0113", "1013", "1014"));
	public static List<String> aCodEsiLibAntipataConc = new ArrayList<String>(
			Arrays.asList("0002", "0003", "0004", "0005", "0029", "0034", "0021", "0022"));
	public static List<String> aCodLibAnticipataConcBis = new ArrayList<String>(
			Arrays.asList("2135", "2136", "2137"));
	public static List<String> aCodEsiLibAntipataConcBis = new ArrayList<String>(Arrays.asList("0023"));
	// 25/07/2018 Valori di FLAG_CONCESSO L.A. di Concessione.
	public static List<String> aFlagConc = new ArrayList<String>(Arrays.asList("C", "S"));

	// 27/07/2018 Esiti di M.A. in Esclusione per la Revoca (non concessi).
	public static List<String> aCodEsiMisAltEsclusi = new ArrayList<String>(Arrays.asList("0002", "0003",
			"0004", "0005", "0174", "0010", "0176", "0018", "0023", "0021", "0022", "0007"));

	// Rimedi Eisarcitori (cod 2790) - Reclamo Rimedi Risarcitori (cod 9027)
	public static List<String> aCodRimediRisarcitori = new ArrayList<String>(Arrays.asList("2790", "9027"));

	// Codici delle Sospensioni Esecuzione della pena.
	public static List<String> aCodSospEsecuzioneSORV = new ArrayList<String>(
			Arrays.asList("2000", "2001", "2480"));

	// Codici dei Differimento pena.
	public static List<String> aCodDifferimentoSORV = new ArrayList<String>(
			Arrays.asList("0203", "0204", "0205", "0206", "0207", "0208", "2010", "2011", "0030", "0031",
					"0032", "0033", "0031", "0201", "0202"));

	// Scomputo Permessi: Esclusione Computo Permesso (cod 2250) - Reclamo Avverso Scomputo Periodo Permesso
	// (cod 0039)
	public static List<String> aCodScomputoPermessi = new ArrayList<String>(Arrays.asList("0039", "2250"));

	// Codici delle Espulsioni.
	public static List<String> aCodEspulsioneSORV = new ArrayList<String>(Arrays.asList("2140", "0029"));

	/**
	 *
	 * @param aEvento
	 * @return
	 */
	public static boolean isPresofferto(EventoModel aEvento) {
		if (aCodPresofferto.contains(aEvento.getCodMotivo()))
			return true;
		else
			return false;
	}

	public static boolean isFungibilita(EventoModel aEvento) {
		if (aCodFungibilita.contains(aEvento.getCodMotivo()))
			return true;
		else
			return false;
	}

	/**
	 *
	 * @param aEvento
	 * @return
	 */
	public static boolean isSospensionePM(EventoModel aEvento) {
		if (aCodSospPM78.contains(aEvento.getCodMotivo()))
			return true;
		else if (aCodSospPM199_Conc.contains(aEvento.getCodMotivo()))
			return true;
		else if (aCodSospPM199_Rev.contains(aEvento.getCodMotivo()))
			return true;
		else if (aCodSospPM199_Sorv.contains(aEvento.getCodMotivo()))
			return true;
		else
			return false;
	}

	/**
	 *
	 * @param aEvento
	 * @return
	 */
	public static boolean isPagamentoPP(EventoModel aEvento) {
		if (aCodPagamentoPP.contains(aEvento.getCodMotivo()))
			return true;
		else
			return false;
	}

	// 22/05/2019 MEV70
	public static boolean isRevocaPenaSospesa(String aCodMotivo) {
		if (aCodRevPenSosp.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospensioneC5Provv(String aCodMotivo) {
		if (aCodSospC5Provv.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospensioneC5VVR(String aCodMotivo) {
		if (aCodSospC5VVR.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospensioneC5DecIrr(String aCodMotivo) {
		siesLogger.debug("aCodMotivo = " + aCodMotivo);
		if (aCodSospC5DecIRR.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospensioneC5Istanza(String aCodMotivo) {
		if (aCodSospIstanza.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospC5Revoca(String aCodMotivo) {
		if (aCodSospC5Revoca.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	public static boolean isSospC5RevocaSorv(String aCodMotivo, String aTipoSosp) {
		if (aCodSospC5RevocaSorv.contains(aCodMotivo) && "C5".equals(aTipoSosp))
			return true;
		else
			return false;
	}

	/**
	 * Ritorna true se il cod Motivo appartiene a quelli previsti per le sospensioni del PM 78
	 *
	 * @param aCodMotivo
	 * @return
	 */
	public static boolean isSospensionePM78(String aCodMotivo) {
		if (aCodSospPM78.contains(aCodMotivo))
			return true;
		else
			return false;
	}

	/**
	 *
	 * @param aCodMotivo
	 * @return
	 */
	public static boolean isSospensionePM199(String aCodMotivo, String aTipoSosp) {
		if (aCodSospPM199_Conc.contains(aCodMotivo))
			return true;
		else if (aCodSospPM199_Rev.contains(aCodMotivo))
			return true;
		else if (aCodSospPM199_Sorv.contains(aCodMotivo) && "199".equals(aTipoSosp))
			return true;
		else
			return false;
	}

	public static boolean isSospensionePM199_Conc(String aCodMotivo) {
		boolean isSospensionePM = false;

		if (aCodSospPM199_Conc.contains(aCodMotivo))
			return true;

		return isSospensionePM;
	}

	public static boolean isSospensionePM199_Rev(String aCodMotivo) {
		boolean isSospensionePM = false;

		if (aCodSospPM199_Rev.contains(aCodMotivo))
			return true;

		return isSospensionePM;
	}

	public static boolean isEspiazionePregressaPM(String aCodMotivo) {
		boolean isEspiazionePregressaPM = false;

		if (aCodEspPregressa.contains(aCodMotivo))
			return true;

		return isEspiazionePregressaPM;
	}

	public static boolean isSospensionePM199_Sorv(String aCodMotivo, String aTipoSosp) {
		boolean isSospensionePM = false;

		if (aCodSospPM199_Sorv.contains(aCodMotivo) && "199".equals(aTipoSosp))
			return true;

		return isSospensionePM;
	}

	public static boolean isAmnistiaIndulto(EventoModel aEvento) {
		boolean isAmnistiaIndulto = false;

		if (aCodAmnistiaIndulto.contains(aEvento.getCodMotivo())
				&& "03".equals(aEvento.getCodTipoProvvedimento()))
			return true;

		return isAmnistiaIndulto;
	}

	public static boolean isDepenalizzazione(EventoModel aEvento) {
		boolean isDepenalizzazione = false;

		if (aCodDepenalizzazione.contains(aEvento.getCodMotivo())
				&& "03".equals(aEvento.getCodTipoProvvedimento()))
			return true;

		return isDepenalizzazione;
	}

	public static boolean isIncostituzionalita(EventoModel aEvento) {
		boolean isIncostituzionalita = false;

		if (aCodIncostituzionalita.contains(aEvento.getCodMotivo())
				&& "03".equals(aEvento.getCodTipoProvvedimento()))
			return true;

		return isIncostituzionalita;
	}

	public static boolean isSospensioneGE(EventoModel aEvento) {
		boolean isSospensioneGE = false;

		if (aCodSospensioneGE.contains(aEvento.getCodMotivo()))
			return true;

		return isSospensioneGE;
	}

	public static boolean isInterruzioneGE(EventoModel aEvento) {
		boolean isInterruzioneGE = false;

		if (aCodInterruzioneGE.contains(aEvento.getCodMotivo()))
			return true;

		return isInterruzioneGE;
	}

	public static boolean isConcMisAlternativa(EventoModel aEvento) {
		boolean isConcMisuraAlternativa = false;

		if (aCodConcMisAltSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| ("03".equals(aEvento.getCodTipoProvvedimento()))))
			return true;

		return isConcMisuraAlternativa;
	}

	public static boolean isRevocaMisAlternativa(EventoModel aEvento) {
		boolean isRevocaMisuraAlternativa = false;

		if (aCodRevocaMisAltSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| ("03".equals(aEvento.getCodTipoProvvedimento()))))
			return true;

		return isRevocaMisuraAlternativa;
	}

	public static boolean isSospMisAlternativa(EventoModel aEvento) {
		boolean isSospMisuraAlternativa = false;

		if (aCodSospMisAltSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| "03".equals(aEvento.getCodTipoProvvedimento())))
			return true;

		return isSospMisuraAlternativa;
	}

	public static boolean isLiberazioneAnticipata(EventoModel aEvento) {
		boolean isLiberazioneAnticipata = false;

		if (aCodLibAnticipata.contains(aEvento.getCodMotivo()))
			return true;

		return isLiberazioneAnticipata;
	}

	public static boolean isRimediRisarcitori(EventoModel aEvento) {
		boolean isRimediRisarcitori = false;

		if (aCodRimediRisarcitori.contains(aEvento.getCodMotivo()))
			return true;

		return isRimediRisarcitori;
	}

	public static boolean isScomputoPermessi(EventoModel aEvento) {
		boolean isScomputoPermessi = false;

		if (aCodScomputoPermessi.contains(aEvento.getCodMotivo()))
			return true;

		return isScomputoPermessi;
	}

	public static boolean isSospEsecuzione(EventoModel aEvento) {
		boolean isSospEsecuzione = false;

		if (aCodSospEsecuzioneSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| ("03".equals(aEvento.getCodTipoProvvedimento()))))
			return true;

		return isSospEsecuzione;
	}

	public static boolean isDifferimento(EventoModel aEvento) {
		boolean isDifferimento = false;

		if (aCodDifferimentoSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| ("03".equals(aEvento.getCodTipoProvvedimento()))))
			return true;

		return isDifferimento;
	}

	public static boolean isEspulsione(EventoModel aEvento) {
		boolean isEspulsione = false;

		if (aCodEspulsioneSORV.contains(aEvento.getCodMotivo())
				&& ("02".equals(aEvento.getCodTipoProvvedimento())
						|| ("03".equals(aEvento.getCodTipoProvvedimento()))))
			return true;

		return isEspulsione;
	}

	// 22/02/2019 MEV70
	public static boolean isRidPenaPMAltroDufficio(String aCodMotivo) {
		boolean isRidPenaPMAltroDufficio = false;

		if (aCodRidPenaPMAltroDufficio.contains(aCodMotivo))
			return true;

		return isRidPenaPMAltroDufficio;
	}

	public static boolean isRidPenaPMAltroAltAut(String aCodMotivo) {
		boolean isRidPenaPMAltroAltAut = false;

		if (aCodRidPenaPMAltroAltAut.contains(aCodMotivo))
			return true;

		return isRidPenaPMAltroAltAut;
	}

	public static boolean isRidPenaPMAltroGE(String aCodMotivo) {
		boolean isRidPenaPMAltroGE = false;

		if (aCodRidPenaPMAltroGE.contains(aCodMotivo))
			return true;

		return isRidPenaPMAltroGE;
	}

	public static boolean isRidPenaPMAltroSorv(String aCodMotivo) {
		boolean isRidPenaPMAltroSorv = false;

		if (aCodRidPenaPMAltroSorv.contains(aCodMotivo))
			return true;

		return isRidPenaPMAltroSorv;
	}

	// 02/04/2019 MEV70
	public static boolean isRidPenaRevocaMA(String aCodMotivo) {
		boolean isRidPenaRevocaMA = false;

		if (aCodRidPenaPerRevocaMA.contains(aCodMotivo))
			return true;

		return isRidPenaRevocaMA;
	}

	// 16/04/2019 MEV70
	public static boolean isSospDiff(String aCodMotivo) {
		boolean isSospDiff = false;

		if (aCodSospDiff.contains(aCodMotivo))
			return true;

		return isSospDiff;
	}

	// 09/05/2019 MEV70
	public static boolean isRevocaSospCondPenaGE(String aCodMotivo) {
		boolean isRevocaSospCondPenaGE = false;

		if (aCodOrdGERevSospCondPena.contains(aCodMotivo))
			return true;

		return isRevocaSospCondPenaGE;
	}

	/**
	 * Restituisce la Action per la visualizzazione del dettaglio
	 *
	 * @param aStatoEsecModel
	 * @return
	 */
	public static String getActionDettaglio(StatoEsecTitoloCumulatoModel aStatoEsecModel) {
		if (aCodPresofferto.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioPresoffertoCumulo";
		} else if (aCodFungibilita.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActLoadDettaglioFungibilitaCumulo";
		} else if (isSospensioneC5Provv(aStatoEsecModel.getCodMotivo())
				|| isSospensioneC5VVR(aStatoEsecModel.getCodMotivo())
				|| isSospensioneC5DecIrr(aStatoEsecModel.getCodMotivo())
				|| isSospensioneC5Istanza(aStatoEsecModel.getCodMotivo())
				|| isSospC5Revoca(aStatoEsecModel.getCodMotivo())
				|| isSospC5RevocaSorv(aStatoEsecModel.getCodMotivo(), aStatoEsecModel.getFlagTipoSosp())
				|| isSospensionePM78(aStatoEsecModel.getCodMotivo())
				|| isSospensionePM199(aStatoEsecModel.getCodMotivo(), aStatoEsecModel.getFlagTipoSosp())) {
			return "siap.siep.modulocumulo.action.ActDettaglioDecretiSospPM";
		} else if (aCodEspPregressa.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioEspiazionePregressa";
		} else if (aCodPagamentoPP.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioPagamentoPP";
		} else if (aCodAmnistiaIndulto.contains(aStatoEsecModel.getCodMotivo())
				&& "03".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioAmnistiaIndultoCumulo";
		} else if (aCodDepenalizzazione.contains(aStatoEsecModel.getCodMotivo())
				&& "03".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioDepenalizzazioneCumulo";
		} else if (aCodIncostituzionalita.contains(aStatoEsecModel.getCodMotivo())
				&& "03".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioIncostituzionalitaCumulo";
		} else if (aCodSospensioneGE.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioSospensioneCumulo";
		} else if (aCodInterruzioneGE.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioInterruzioneCumulo";
		}
		//
		else if (("0610".equals(aStatoEsecModel.getCodMotivo())
				|| "2630".equals(aStatoEsecModel.getCodMotivo())
				|| "2640".equals(aStatoEsecModel.getCodMotivo()))
				&& (!"02".equals(aStatoEsecModel.getCodTipoProvvedimento())
						&& !"03".equals(aStatoEsecModel.getCodTipoProvvedimento()))) {
			// Dettaglio generico
			return "siap.siep.modulocumulo.action.ActLoadDettaglioStatoEsecTitoloCumulato";
		}
		//
		else if (aCodConcMisAltSORV.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioConcMisuraAlternativaCumulo";
		} else if (aCodLibAnticipata.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioLiberazioneAnticipataCumulo";
		} else if (aCodRimediRisarcitori.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioRimediRisarcitoriDL201492Cumulo";
		} else if (aCodScomputoPermessi.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioScomputoPermessiCumulo";
		} else if (aCodEspulsioneSORV.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioEspulsioneCumulo";
		} else if (aCodSospEsecuzioneSORV.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioSospEsecuzionePenaCumulo";
		} else if (aCodRevocaMisAltSORV.contains(aStatoEsecModel.getCodMotivo())) {
			// INIZIO: Ticket#202012020116 - Errore non segnalato ma emerso in fase di analisi del ticket
			// Il provvedimento di esecuzuone del PM avendo lo stesso codice del provv sorveglianza agganciava
			// lo stesso dettaglio ma andava in errore non avendo i dati richiesti
			if (   "02".equals(aStatoEsecModel.getCodTipoProvvedimento())
				|| "03".equals(aStatoEsecModel.getCodTipoProvvedimento())
			   ) {
				return "siap.siep.modulocumulo.action.ActDettaglioRevocaMisuraAlternativaCumulo";
		    }
			// FIne Ticket
		} else if (aCodDifferimentoSORV.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioDifferimentoPenaCumulo";
		} else if (aCodSospMisAltSORV.contains(aStatoEsecModel.getCodMotivo())) {
			return "siap.siep.modulocumulo.action.ActDettaglioSospMisuraAlternativaCumulo";
		} else if (aCodRidPenaPMAltroDufficio.contains(aStatoEsecModel.getCodMotivo())
				&& "25".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo";
		} else if (aCodRidPenaPMAltroAltAut.contains(aStatoEsecModel.getCodMotivo())
				&& "25".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo";
		} else if (aCodRidPenaPMAltroGE.contains(aStatoEsecModel.getCodMotivo())
				&& "25".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo";
		} else if (aCodRidPenaPMAltroSorv.contains(aStatoEsecModel.getCodMotivo())
				&& "25".equals(aStatoEsecModel.getCodTipoProvvedimento())) {
			return "siap.siep.modulocumulo.action.ActDettaglioRidetPenaPMAltroCumulo";
		} else if (aCodOrdGERevSospCondPena.contains(aStatoEsecModel.getCodMotivo())) // 10/05/2019 MEV70
		{
			return "siap.siep.modulocumulo.action.ActDettaglioAnnotazioneRevocaBeneficioCumulo";
		}

		// Dettaglio generico
		return "siap.siep.modulocumulo.action.ActLoadDettaglioStatoEsecTitoloCumulato";
		// return null;
	}

	public static Collection getCodiciMisureDetentive() {
		Collection lCodMisureDetentiveCollection = new Vector();

		DecodificheModel lDec = new DecodificheModel();
		lDec.setCode("-");
		lDec.setDescription("-");
		lCodMisureDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CA");
		lDec.setDescription("Custodia cautelare in carcere");
		lCodMisureDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CD");
		lDec.setDescription("Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria");
		lCodMisureDetentiveCollection.add(lDec);

		return lCodMisureDetentiveCollection;

	}

	public static Collection getCodiciMisureNonDetentive() {
		Collection lCodMisureNonDetentiveCollection = new Vector();

		DecodificheModel lDec = new DecodificheModel();
		lDec.setCode("-");
		lDec.setDescription("-");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("AD");
		lDec.setDescription("Custodia cautelare in Arresti domiciliari");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CB");
		lDec.setDescription("Custodia Cautelare in Regime di Permanenza in Casa");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CC");
		lDec.setDescription("Custodia Cautelare in Collocamento in Comunita'");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CE");
		lDec.setDescription("Custodia cautelare in camera di sicurezza");
		lCodMisureNonDetentiveCollection.add(lDec);

		lDec = new DecodificheModel();
		lDec.setCode("CL");
		lDec.setDescription("Computo periodo di messa alla prova");
		lCodMisureNonDetentiveCollection.add(lDec);

		return lCodMisureNonDetentiveCollection;
	}

}