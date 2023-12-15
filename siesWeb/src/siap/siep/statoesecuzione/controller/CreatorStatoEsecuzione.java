/**
 * 
 */
package siap.siep.statoesecuzione.controller;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import f3b.log.LogF3B;

/**
 * @author Giselda De Vita CreatorStatoEsecuzione - Creator dello Stato Esecuzione, Capisce il tipo di
 *         provvedimento e lo istanzia.
 */
public class CreatorStatoEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneElement create(String aTipoEvento, StatoEsecuzioneElement lStatElement) {
		StatoEsecuzioneElement lStatoEsec = null;

		if (aTipoEvento.equals("MAC"))
			lStatoEsec = new StatoEsecuzioneMAC(lStatElement);
		if (aTipoEvento.equals("PROVV"))
			lStatoEsec = new StatoEsecuzioneProvv(lStatElement);
		if (aTipoEvento.equals("MAR"))
			lStatoEsec = new StatoEsecuzioneMAR(lStatElement);
		if (aTipoEvento.equals("IST"))
			lStatoEsec = new StatoEsecuzioneIst(lStatElement);
		if (aTipoEvento.equals("SIUS"))
			lStatoEsec = new StatoEsecuzioneSius(lStatElement);
		if (aTipoEvento.equals("VERB"))
			lStatoEsec = new StatoEsecuzioneVerbale(lStatElement);
		if (aTipoEvento.equals("INTERR"))
			lStatoEsec = new StatoEsecuzioneInterruzione(lStatElement);
		if (aTipoEvento.equals("LA"))
			lStatoEsec = new StatoEsecuzioneLA(lStatElement);
		if (aTipoEvento.equals("REFERTO"))
			lStatoEsec = new StatoEsecuzioneReferto(lStatElement);
		if (aTipoEvento.equals("ARCH"))
			lStatoEsec = new StatoEsecuzioneArch(lStatElement);
		if (aTipoEvento.equals("RICH"))
			lStatoEsec = new StatoEsecuzioneRich(lStatElement);
		if (aTipoEvento.equals("DIFF"))
			lStatoEsec = new StatoEsecuzioneDifferimento(lStatElement);
		if (aTipoEvento.equals("SIMEONE"))
			lStatoEsec = new StatoEsecuzioneSimeone(lStatElement);
		if (aTipoEvento.equals("INDULTO"))
			lStatoEsec = new StatoEsecuzioneIndulto(lStatElement);
		if (aTipoEvento.equals("RIDETERM"))
			lStatoEsec = new StatoEsecuzioneRideterminazione(lStatElement);
		// MEV_2023-33
    if (aTipoEvento.equals("PENEPECUNIARIE"))
      lStatoEsec = new StatoEsecuzionePP(lStatElement);

		return lStatoEsec;
	}

	/**
	 * Metodo per la decisione di quale evento fare il Dispatch. Dal codice del provvedimento e dal codice
	 * motivo viene deciso a quale tipo di famiglia appartiene il Provvedimento in questione.
	 * 
	 * @param aEvento
	 * @return Tipo Evento
	 */
	public String getTipoStatoEsecuzioneDaCreare(EventoModel aEvento) {
		String lFamigliaEvento = "PROVV";

		try {
			if (aEvento.getCodMotivo().startsWith("C") || aEvento.getCodMotivo().equals("0993")) // aggiunta
																									// nuova
																									// istanza
																									// Paolo
																									// c.
																									// 17/09/2010
				return "IST";

			if (aEvento.getCodTipoProvvedimento().equals("02")
					|| aEvento.getCodTipoProvvedimento().equals("03"))
				return "SIUS";

			if (aEvento.getCodTipoProvvedimento().equals("19") && aEvento.getCodMotivo().equals("0315"))
				return "REFERTO";

			int lIntMotivo = Integer.parseInt(aEvento.getCodMotivo());

			if (lIntMotivo == 61 || lIntMotivo == 63 || lIntMotivo == 117 || lIntMotivo == 104)
				return "SIMEONE";
			
			// MEV_2023-33
	    if (lIntMotivo == 622 || lIntMotivo == 1307 || lIntMotivo == 1308 || lIntMotivo == 1309)
	      return "PENEPECUNIARIE";
			
			if (isIndulto(lIntMotivo))
				return "INDULTO";

			if (aEvento.getCodTipoProvvedimento().equals("11")
					|| aEvento.getCodTipoProvvedimento().equals("29")
					|| aEvento.getCodTipoProvvedimento().equals("30")
 				  || (lIntMotivo > 319 && lIntMotivo < 353))
				return "RICH";

			if (aEvento.getCodTipoProvvedimento().equals("16")
					|| aEvento.getCodTipoProvvedimento().equals("17")
					|| aEvento.getCodTipoProvvedimento().equals("18")
					|| aEvento.getCodTipoProvvedimento().equals("27")
					|| aEvento.getCodTipoProvvedimento().equals("26"))
				return "VERB";

			// Aggiungere i Vari tipi di eventi a che famiglia appartengono

			// if (aEvento.getCodTipoProvvedimento().equals("25") ||
			if (aEvento.getCodTipoProvvedimento().equals("25") &&
			// aEvento.getCodTipoProvvedimento().equals("12")||
					aEvento.getCodMotivo().equals("0267"))
				/*
				 * || (aEvento.getCodTipoProvvedimento().equals("04") ))&&
				 * (aEvento.getCodMotivo().equals("0268") || aEvento.getCodMotivo().equals("0269") ||
				 * aEvento.getCodMotivo().equals("0270"))
				 */
				return "INTERR";

			if (!aEvento.getCodTipoProvvedimento().equals("-")) {

				if (isMAC(Integer.parseInt(aEvento.getCodTipoProvvedimento()), lIntMotivo))
					return "MAC";

				if (isMAR(Integer.parseInt(aEvento.getCodTipoProvvedimento()), lIntMotivo))
					return "MAR";
			}

			if (isEventoConLiberazioneAnticipata(aEvento))
				return "LA";

			if (isArchiviazione(lIntMotivo))
				return "ARCH";

			if (isRideterminazione(aEvento.getCodMotivo()))
				return "RIDETERM";

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore durante getTipoStatoEsecuzioneDaCreare" + ex, ex);
		}

		return lFamigliaEvento;
	}

	/**
	 * Metodo che verifica se il tipo di provvedimento è di tipo MA Concessione
	 * 
	 * @param aTipoProvv
	 * @param aCodMotivo
	 * @return
	 */
	private boolean isMAC(int aTipoProvv, int aCodMotivo) {
		boolean lReturn = false;

		if (aTipoProvv == 6
				&& (aCodMotivo == 228 || aCodMotivo == 229 || aCodMotivo == 372 || aCodMotivo == 373
						|| (aCodMotivo >= 2145 && aCodMotivo <= 2151) || aCodMotivo == 2245
						|| aCodMotivo == 373 || aCodMotivo == 372 || aCodMotivo == 2293 || aCodMotivo == 11))
			lReturn = true;

		if (aTipoProvv == 9
				&& ((aCodMotivo >= 226 && aCodMotivo <= 229) || (aCodMotivo >= 371 && aCodMotivo <= 373)
						|| (aCodMotivo >= 2145 && aCodMotivo <= 2151) || aCodMotivo == 2005
						|| aCodMotivo == 2006 || aCodMotivo == 2245 || aCodMotivo == 11))
			lReturn = true;

		if (aTipoProvv == 12
				&& ((aCodMotivo >= 226 && aCodMotivo <= 229) || (aCodMotivo >= 235 && aCodMotivo <= 243)
						|| (aCodMotivo >= 304 && aCodMotivo <= 311)
						|| (aCodMotivo >= 371 && aCodMotivo <= 393)
						|| (aCodMotivo >= 450 && aCodMotivo <= 459)
						|| (aCodMotivo >= 450 && aCodMotivo <= 459)
						|| (aCodMotivo >= 2145 && aCodMotivo <= 2151)
						|| (aCodMotivo >= 2160 && aCodMotivo <= 2167) || aCodMotivo == 197
						|| aCodMotivo == 11 || aCodMotivo == 2005 || aCodMotivo == 2006 || aCodMotivo == 2289
						|| aCodMotivo == 2293 || aCodMotivo == 410 || aCodMotivo == 11))
			lReturn = true;

		/*
		 * Questo è un referto di Scarcerazione!!! if(aTipoProvv == 19 && aCodMotivo==315) lReturn = true;
		 */

		/* il 26 - 0244 pare essere un verbale... */
		if (aTipoProvv == 26 && (aCodMotivo == 244 || aCodMotivo == 245 || aCodMotivo == 370))
			lReturn = true;

		return lReturn;
	}

	/**
	 * Metodo che verifica se il tipo di provvedimento è di tipo MA Revoca
	 * 
	 * @param aTipoProvv
	 * @param aCodMotivo
	 * @return
	 */
	private boolean isMAR(int aTipoProvv, int aCodMotivo) {
		boolean lReturn = false;

		if (aTipoProvv == 6
				&& (aCodMotivo == 16 || aCodMotivo == 87 || aCodMotivo == 88 || aCodMotivo == 89
						|| aCodMotivo == 91 || aCodMotivo == 15 || aCodMotivo == 86 || aCodMotivo == 196 || aCodMotivo == 14))
			lReturn = true;

		return lReturn;
	}

	/**
	 * Questo metodo controlla se è l'evento corrente puo' avere la Liberazione Anticipata
	 * 
	 * @param lEve
	 * @return
	 */
	private boolean isEventoConLiberazioneAnticipata(EventoModel lEve) {
		if (lEve != null && lEve.getCodTipoEvento() != null && lEve.getCodTipoProvvedimento() != null
				&& lEve.getCodMotivo() != null) {
			boolean aRet1 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("03") && lEve.getCodMotivo().equals("2130");

			boolean aRet4 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("03") && lEve.getCodMotivo().equals("0076");

			boolean aRet2 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("09") && lEve.getCodMotivo().equals("0081");

			boolean aRet3 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("09") && lEve.getCodMotivo().equals("0083");

			// TEST DL92
			boolean aRet5 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("09")
					&& (lEve.getCodMotivo().equals("5491") || lEve.getCodMotivo().equals("5492")
							// inizio ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori 
							|| lEve.getCodMotivo().equals("9254") || lEve.getCodMotivo().equals("9154")
							);

			// DL92 - Comunicazioni
			boolean aRet6 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("12") && (lEve.getCodMotivo().equals("5493") // DL92
																											// -
																											// Condannato
																											// Libero
							|| lEve.getCodMotivo().equals("5494") // DL92 - Condannato in Ergastolo
					|| lEve.getCodMotivo().equals("5495") // DL92 - Condannato già Scarcerato
					//  ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori 
					|| lEve.getCodMotivo().equals("9032")
					|| lEve.getCodMotivo().equals("9033")
					|| lEve.getCodMotivo().equals("9034"));

			// MOD MEV29 per visualizzare i periodi anche per le comunicazioni LA
			boolean aRet7 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("12") && (lEve.getCodMotivo().equals("0922") // Condannato
																											// Libero
					|| lEve.getCodMotivo().equals("0923")); // Condannato in Ergastolo

			// 0998 ridimensionamento + Revoca

			return aRet1 || aRet2 || aRet3 || aRet4 || aRet5 || aRet6 || aRet7;
		} else
			return false;
	}

	/**
	 * Verifico se il motivo passato è un'archiviazione
	 * 
	 * @param aMotivo
	 * @return true se il motivo è un'archiviazione
	 */
	private boolean isArchiviazione(int aIntMotivo) {
		/*
		 * Archiviazione RES da 400 a 409
		 */

		/*
		 * Non luogo a provvedere 0006, 0007, 0008, 0009, 0120, 0353, 0478
		 */

		/*
		 * Fine espiazione 0097, 0096, 0098, 0099, 0473, 0479, 0480, 0481
		 */

		/*
		 * Fine espiazione 0019, 0022
		 */

		/*
		 * Altra Autorità 0411, 0412, 0413, 0414, 0415, 0416, 0417, 0418, 0419, 0420, 0476, 0477, 0421, 0422,
		 * 0424, 0425, 0426,
		 */

		/*
		 * Attesa Archiviazione 0482, 0483, 0484, 0485, 0486, 0487
		 */

		if (aIntMotivo > 399 && aIntMotivo < 410)
			return true;

		if ((aIntMotivo > 5 && aIntMotivo < 10) || (aIntMotivo > 96 && aIntMotivo < 100)
				|| (aIntMotivo == 19) || (aIntMotivo == 22) || (aIntMotivo == 120) || (aIntMotivo == 353)
				|| (aIntMotivo > 410 && aIntMotivo < 423) || (aIntMotivo > 423 && aIntMotivo < 427)
				|| (aIntMotivo == 473) || (aIntMotivo > 475 && aIntMotivo < 489) || (aIntMotivo == 356)
				|| (aIntMotivo == 357))

			return true;

		return false;
	}

	/**
	 * Verifico se il motivo passato è un'archiviazione
	 * 
	 * @param aMotivo
	 * @return true se il motivo è un'archiviazione
	 */
//	private boolean isDifferimento(int aIntMotivo) {
//		/*
//		 * Differimento da 0428 a 0435 da 0030 a 0033 da 2010 2011 da 410
//		 */
//
//		if ((aIntMotivo > 427 && aIntMotivo < 436) || (aIntMotivo > 29 && aIntMotivo < 34)
//				|| (aIntMotivo == 2010 || aIntMotivo == 2011))
//			return true;
//
//		/*
//		 * if( (aIntMotivo > 5 && aIntMotivo < 10) || (aIntMotivo > 96 && aIntMotivo < 100) ||
//		 * (aIntMotivo==19) || (aIntMotivo==22) || (aIntMotivo==120) || (aIntMotivo==353) || (aIntMotivo > 410
//		 * && aIntMotivo < 423) || (aIntMotivo > 423 && aIntMotivo < 427) || (aIntMotivo==473) || (aIntMotivo
//		 * > 475 && aIntMotivo < 489) || (aIntMotivo == 356) || (aIntMotivo == 357))
//		 */
//
//		return false;
//
//	}

	/**
	 * Verifico se il motivo passato è un'archiviazione
	 * 
	 * @param aMotivo
	 * @return true se il motivo è un'archiviazione
	 */
	private boolean isIndulto(int aIntMotivo) {
		/*
		 * Decisioni del GE Aministia/Indulto - Incostituzionalità - Depenalizzazione da 0285 a 0286 da 0284
		 */

		if ((aIntMotivo == 285) || (aIntMotivo == 286) || (aIntMotivo == 284))
			return true;

		// 0287, 0288, 0289, 0289, 0290, 0291, 0292, 0293, 0293, 0294, 0295, 0295, 0296, 0297, 0298, 0299,
		// 0367

		if ((aIntMotivo >= 287 && aIntMotivo <= 299) || (aIntMotivo == 367))
			return true;

		return false;
	}

	/**
	 * Verifico se il motivo passato è Rideterminazione Pena
	 * 
	 * @param aMotivo
	 * @return true se il motivo è Rideterminazione Pena
	 */
	private boolean isRideterminazione(String aCodMotivo) {
		/*
		 * Rideterminazione Pena da 0212 0213 0121 Provvedimento di computo da 0913 a 0919 rideterminazione
		 * pena altro vecchi codici
		 */
		int aIntMotivo = Integer.parseInt(aCodMotivo);

		if ((aIntMotivo == 121) || (aIntMotivo == 212) || (aIntMotivo == 213)
				|| (aIntMotivo >= 913 && aIntMotivo <= 919))
			return true;

		// MEV29 - i codice della rideterminazione pena altro (da 0913 a 0919)
		// sono cambiati da tempo ma non sono stati rimapati in questo metodo.
		// Si aggiunge il controllo
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Controllo aCodMotivo = "+aCodMotivo);
		if (DecodificheUtils.containsCode(DecodificheManager.getInstance()
				.getRideterminazionePenaAltroDufficio(), aCodMotivo))
			return true; // D'ufficio
		if (DecodificheUtils.containsCode(DecodificheManager.getInstance()
				.getRideterminazionePenaAltroAUfficio(), aCodMotivo))
			return true; // Altro Ufficio - Altra Autorita
		if (DecodificheUtils.containsCode(DecodificheManager.getInstance().getRideterminazionePenaAltroGE(),
				aCodMotivo))
			return true; // Altro Ufficio - Giudice esecuzione
		if (DecodificheUtils.containsCode(
				DecodificheManager.getInstance().getRideterminazionePenaAltroSORV(), aCodMotivo))
			return true; // Altro Ufficio - Sorveglianza

		// OE di rideterminazione! Per rideterminazione pena Computi.
		// Attenzione! Che le Annotazioni manuali sono legate al provvedimento
		// di computo e non all'ordine di esecuzione
		if ((aIntMotivo == 130) || (aIntMotivo == 132) || (aIntMotivo == 131) || (aIntMotivo == 134))
			return true;

		return false;
	}

}