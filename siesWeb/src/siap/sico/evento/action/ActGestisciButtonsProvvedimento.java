package siap.sico.evento.action;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.siep.dettaglioprovvedimento.controller.DettaglioProvvedimentoManager;
import siap.util.SIESSwitch;
import f3b.log.LogF3B;

/**
 * <p>Title ActGestisciButtonsProvvedimento</p>
 * <p>Description Classe helper per la JSP di ricerca dei provvedimenti. A seconda del codice MOtivo viene
 * smistata la corretta Action corrispondente</p>
 * <p>Copyright Copyright (c) 2002</p>
 */
public class ActGestisciButtonsProvvedimento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public boolean isDettaglioVisualizzabile(String aTipoProvv, String aMotivo, String aTemplate)
	{
		boolean lIsVisibile = false;


		if(  (!"02".equals(aTipoProvv)||("02".equals(aTipoProvv)&& ("0282".equals(aMotivo) )) )
				&& !"03".equals(aTipoProvv)
				&& !"*".equals(aTemplate))
			lIsVisibile = true;


		return lIsVisibile;

	}


	/**
	 * Prende l'Action dettaglio relativa al COdice Motivo Passato
	 * @param lMotivoProvvedimento - Codice Motivo del Provvedimento
	 * @return
	 */
	public String getActionDettaglioProvvedimento(String lMotivoProvvedimento,
			String lTipoEvento)
	{

		String lAction = "";

		/**
		 * Test sul Rework del dettaglio
		 */
		if (SIESSwitch.isReworkDettaglio())
		{

			/**
			 * Chiamaata al DettaglioProvvedimentoManager
			 */
			lAction = DettaglioProvvedimentoManager.getInstance().getActionDettaglio(lTipoEvento, "", lMotivoProvvedimento,"");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("REWORK dettaglio --> Action di Dettaglio restituita = " + lAction);

			if (!lAction.equals("siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico"))
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("REWORK --> Action di Dettaglio restituita = " + lAction);
				return lAction;
			}
		} //Fine REWORK!!!!

		// Esecuzione Pena accessoria
		if (lTipoEvento != null && lTipoEvento.equals("16"))
			lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioEsecuzionePA";
		else

			// Richiesta al G.E.
			if (lTipoEvento != null && lTipoEvento.equals("17"))
				lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE";
			else

				// Comunicazione per Pena accessoria
				if (lTipoEvento != null && lTipoEvento.equals("18"))
					lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione";
				else
					if (lMotivoProvvedimento.equals("0370") || lMotivoProvvedimento.equals("0371") || //affidamento
							lMotivoProvvedimento.equals("0226") || lMotivoProvvedimento.equals("0227") || //affidamento
							lMotivoProvvedimento.equals("0244") || lMotivoProvvedimento.equals("0245") || //affidamento
							lMotivoProvvedimento.equals("0373") || //semilibertà
							lMotivoProvvedimento.equals("0372") || lMotivoProvvedimento.equals("0228") || //detenzione domiciliare
							lMotivoProvvedimento.equals("0229") || //detenzione domiciliare
							lMotivoProvvedimento.equals("2245")) //L.207/2003
						lAction = "siap.siep.misuraalternativa.action.ActDettaglioConcessione";
					else
					{
						if (lMotivoProvvedimento.equals("0087") || lMotivoProvvedimento.equals("0089")
								|| lMotivoProvvedimento.equals("0016") || lMotivoProvvedimento.equals("0091")
								|| lMotivoProvvedimento.equals("0088"))
							lAction = "siap.siep.misuraalternativa.action.ActDettaglioRevocaMA";
						else
						{
//							2145,2146,2147,2149,2150,2151,2148 ActDettaglioMASospProvv
							if (lMotivoProvvedimento.equals("2145") || lMotivoProvvedimento.equals("2146") ||
									lMotivoProvvedimento.equals("2147") || lMotivoProvvedimento.equals("2149") ||
									lMotivoProvvedimento.equals("2150") || lMotivoProvvedimento.equals("2151") ||
									lMotivoProvvedimento.equals("2148") || lMotivoProvvedimento.equals("2293") ||
									lMotivoProvvedimento.equals("0388") || lMotivoProvvedimento.equals("0389") ||
									lMotivoProvvedimento.equals("0390") || lMotivoProvvedimento.equals("0383") ||
									lMotivoProvvedimento.equals("0384") || lMotivoProvvedimento.equals("0385") ||
									lMotivoProvvedimento.equals("0386") || lMotivoProvvedimento.equals("0387") ||
									lMotivoProvvedimento.equals("0380") || lMotivoProvvedimento.equals("0455") ||
									lMotivoProvvedimento.equals("0456") || lMotivoProvvedimento.equals("0457"))
								lAction = "siap.siep.misuraalternativa.action.ActDettaglioMASospProvv";
							else
							{
								if (lMotivoProvvedimento.equals("0057"))
									lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOELibero";
								else
								{
									if (lMotivoProvvedimento.equals("0058"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC";

									if (lMotivoProvvedimento.equals("0059"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0060"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0061"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSLibero";

									if (lMotivoProvvedimento.equals("0246"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni";

									if (lMotivoProvvedimento.equals("0062"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa";

									if (lMotivoProvvedimento.equals("0063"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0078") || lMotivoProvvedimento.equals("0079")
											|| lMotivoProvvedimento.equals("0080"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioRevocaLSAltrePosizioni";

									if (lMotivoProvvedimento.equals("0104"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSLiberoIstanzaProdotta";

									if (lMotivoProvvedimento.equals("0117"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa";

									if (lMotivoProvvedimento.equals("0130") || lMotivoProvvedimento.equals("0131")
											|| lMotivoProvvedimento.equals("0132") || lMotivoProvvedimento.equals("0133")
											|| lMotivoProvvedimento.equals("0134"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioEmissioneProvvedimento";

//									REVOCA viene gestita nell'azione ActDettaglioRevocaMAAffInProva
									if (lMotivoProvvedimento.equals("0014") || lMotivoProvvedimento.equals("0015") ||
											lMotivoProvvedimento.equals("0086") || lMotivoProvvedimento.equals("0196"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioRevocaMAAffInProva";

//									ripristino viene gestita nell'azione ActDettaglioMARipristino
									if (lMotivoProvvedimento.equals("0304") || lMotivoProvvedimento.equals("0305") ||
											lMotivoProvvedimento.equals("0306") || lMotivoProvvedimento.equals("0307") ||
											lMotivoProvvedimento.equals("0308") || lMotivoProvvedimento.equals("0309") ||
											lMotivoProvvedimento.equals("0310") || lMotivoProvvedimento.equals("0410") ||
											lMotivoProvvedimento.equals("0311"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMARipristino";

//									perdita efficacia
									if (lMotivoProvvedimento.equals("2160") || lMotivoProvvedimento.equals("2161")
											|| lMotivoProvvedimento.equals("2162") || lMotivoProvvedimento.equals("2163")
											|| lMotivoProvvedimento.equals("2164") || lMotivoProvvedimento.equals("2165")
											|| lMotivoProvvedimento.equals("2166") || lMotivoProvvedimento.equals("2289")
											|| lMotivoProvvedimento.equals("2167"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAPerditaEfficacia";

//									prosecuzione provvisoria senza cumulo
									if (lMotivoProvvedimento.equals("0375") || lMotivoProvvedimento.equals("0374")
											|| lMotivoProvvedimento.equals("0376") || lMotivoProvvedimento.equals("0382")
											|| lMotivoProvvedimento.equals("0377") || lMotivoProvvedimento.equals("0379")
											|| lMotivoProvvedimento.equals("0378")
											|| lMotivoProvvedimento.equals("0381"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione";

									if (lMotivoProvvedimento.equals("0391") //affidamento
											|| lMotivoProvvedimento.equals("0235") //affidamento
											|| lMotivoProvvedimento.equals("0236") //affidamento
											|| lMotivoProvvedimento.equals("0392") //detenzione
											|| lMotivoProvvedimento.equals("0237") //detenzione
											|| lMotivoProvvedimento.equals("0238") //detenzione
											|| lMotivoProvvedimento.equals("0239") //detenzione
											|| lMotivoProvvedimento.equals("0240") //detenzione
											|| lMotivoProvvedimento.equals("0393") //semilibertà
											|| lMotivoProvvedimento.equals("0450") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0241") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0242") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0451") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0452") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0453") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0458") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0459") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0454") //semilibertà cumulo
									)
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAEstensioneDefinitiva";

//									rigetto misura alternativa
									//if (lMotivoProvvedimento.equals("9000") || lMotivoProvvedimento.equals("9001"))
									if (lMotivoProvvedimento.equals("0217") || lMotivoProvvedimento.equals("0218"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMARigetto";

//									detenzione domiciliare a termine
									if (lMotivoProvvedimento.equals("0011") || lMotivoProvvedimento.equals("0197")
											|| lMotivoProvvedimento.equals("2340"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMADetDomTemp";

//									ammisione provisoria a detenzione domiciliare
									if (lMotivoProvvedimento.equals("2005") || lMotivoProvvedimento.equals("2006"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria";

//									Archiviazione per fine espiazione
									if (lMotivoProvvedimento.equals("0096") || lMotivoProvvedimento.equals("0097")
											|| lMotivoProvvedimento.equals("0098") || lMotivoProvvedimento.equals("0099")
											|| lMotivoProvvedimento.equals("0214") || lMotivoProvvedimento.equals("0215")
											|| lMotivoProvvedimento.equals("0216"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioPenaEspiata";

//									Archiviazione per provvedimento altra autorità
									if (lMotivoProvvedimento.equals("0411") || lMotivoProvvedimento.equals("0412")
											|| lMotivoProvvedimento.equals("0413") || lMotivoProvvedimento.equals("0414")
											|| lMotivoProvvedimento.equals("0415") || lMotivoProvvedimento.equals("0416")
											|| lMotivoProvvedimento.equals("0417") || lMotivoProvvedimento.equals("0418")
											|| lMotivoProvvedimento.equals("0419") || lMotivoProvvedimento.equals("0420")
											|| lMotivoProvvedimento.equals("0421") || lMotivoProvvedimento.equals("0422")
											|| lMotivoProvvedimento.equals("0423") || lMotivoProvvedimento.equals("0424")
											|| lMotivoProvvedimento.equals("0425") || lMotivoProvvedimento.equals("0426"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita";

//									Archiviazione per perdità di competenza
									if (lMotivoProvvedimento.equals("0019") || lMotivoProvvedimento.equals("0022"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioAssorCumulo";

//									Archiviazione per Non Luogo a Provvedere
									if (lMotivoProvvedimento.equals("0006") || lMotivoProvvedimento.equals("0007")
											|| lMotivoProvvedimento.equals("0008") || lMotivoProvvedimento.equals("0009")
											|| lMotivoProvvedimento.equals("0119") || lMotivoProvvedimento.equals("0120")
											|| lMotivoProvvedimento.equals("0353"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere";

									if (lMotivoProvvedimento.equals("0221") || lMotivoProvvedimento.equals("0274")
											|| lMotivoProvvedimento.equals("0354") || lMotivoProvvedimento.equals("0355"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioDifferimentoOE";


									if (lMotivoProvvedimento.equals("0263") || lMotivoProvvedimento.equals("0241"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv";

									/*              if (lMotivoProvvedimento.equals("0263"))
                              lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneArt47";
                            if (lMotivoProvvedimento.equals("0264"))
                              lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneArt91";
									 */
									if (lMotivoProvvedimento.equals("0265"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensionePena";

									if (lMotivoProvvedimento.equals("0210"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteDepenIncost";

									if (lMotivoProvvedimento.equals("0082"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione";

									if (lMotivoProvvedimento.equals("0297"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichAccDataReato";

									if (lMotivoProvvedimento.equals("0289"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichDetPenAboReato";

									if (lMotivoProvvedimento.equals("0287") || lMotivoProvvedimento.equals("0288")
											|| lMotivoProvvedimento.equals("0290") || lMotivoProvvedimento.equals("0291")
											|| lMotivoProvvedimento.equals("0292") || lMotivoProvvedimento.equals("0294")
											|| lMotivoProvvedimento.equals("0296"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteConCodice";

									if (lMotivoProvvedimento.equals("0293") || lMotivoProvvedimento.equals("0288")
											|| lMotivoProvvedimento.equals("0290") || lMotivoProvvedimento.equals("0295"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteDepenIncost";

									if (lMotivoProvvedimento.equals("0289"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichDetPenAboReato";

									if (lMotivoProvvedimento.equals("0210"))
										lAction = "siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniDepen";

									if (lMotivoProvvedimento.equals("0367"))
										lAction = "siap.siep.richiesta.action.ActDettaglioOrdineScarcerazioneProvv";

									if (lMotivoProvvedimento.equals("0121"))
										lAction = "siap.siep.calcolopena.action.ActDettaglioComputoCustodiaCautelare";

									if (lMotivoProvvedimento.equals("0212"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAltroTitolo";

									/* if (lMotivoProvvedimento.equals("0213"))
              lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniSenzaTitolo";
									 */
									if (lMotivoProvvedimento.equals("0266") || lMotivoProvvedimento.equals("0267")
											|| lMotivoProvvedimento.equals("0268") || lMotivoProvvedimento.equals("0269")
											|| lMotivoProvvedimento.equals("0270"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioInterruzione";

									if (lMotivoProvvedimento.equals("0276"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioNotificheDifferimento";

									if (lMotivoProvvedimento.equals("0081"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipata";

									if (lMotivoProvvedimento.equals("0083"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipataMA";

									if (lMotivoProvvedimento.equals("0282"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioDecretoSospensione";

									if (lMotivoProvvedimento.equals("0277")
											|| lMotivoProvvedimento.equals("0222")
											|| lMotivoProvvedimento.equals("0223")
											|| lMotivoProvvedimento.equals("0224"))
										lAction = "siap.siep.cumulo.action.ActDettaglioCumuloStampa";

									if (lMotivoProvvedimento.equals("0272"))
										lAction = "siap.siep.ripristino.action.ActDettaglioProvvedimentoRipristino";

									if (lMotivoProvvedimento.equals("0339") || lMotivoProvvedimento.equals("0340"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiestaGenerica";

									if (lMotivoProvvedimento.equals("0284"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAmnistiaIndulto";

									if (lMotivoProvvedimento.equals("0285"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniDepenalizzazione";

									if (lMotivoProvvedimento.equals("0286"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniIncostituzionalita";

									/*
                             if (lMotivoProvvedimento.equals("2130") ||lMotivoProvvedimento.equals("0076") )
                               lAction = "siap.sico.libertaanticipata.action.ActDettaglioLiberazioneAnticipata";
									 */

									if (lMotivoProvvedimento.equals("2000"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensione";

									if (lMotivoProvvedimento.equals("0902") || lMotivoProvvedimento.equals("0901") || lMotivoProvvedimento.equals("0903") || lMotivoProvvedimento.equals("0900")
											|| lMotivoProvvedimento.equals("0920") || lMotivoProvvedimento.equals("0921")
							             // Aggiunti i 2 nuovi codice MOTIVO_PROVVEDIMENTO  Luigi 20-08-2010
											|| lMotivoProvvedimento.compareTo("0947")==0 || lMotivoProvvedimento.compareTo("0952")==0 )
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneEsecPenaDispPm";

									if (lMotivoProvvedimento.equals("0158") || lMotivoProvvedimento.equals("0159") || lMotivoProvvedimento.equals("0160") || lMotivoProvvedimento.equals("0174") ||
											lMotivoProvvedimento.equals("0175"))
										lAction = "siap.siep.calcolopena.action.ActDettaglioOrdineScarcerazionePerNuovaScadenzaPena";

									if (lMotivoProvvedimento.equals("0913") || lMotivoProvvedimento.equals("0914") || lMotivoProvvedimento.equals("0915") ||
											lMotivoProvvedimento.equals("0916") || lMotivoProvvedimento.equals("0917") || lMotivoProvvedimento.equals("0918") ||
											lMotivoProvvedimento.equals("0919"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioRidetPena";

									if (lMotivoProvvedimento.equals("0300"))
										lAction = "siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioniAN";

									if (lMotivoProvvedimento.equals("0298") || lMotivoProvvedimento.equals("0299") || lMotivoProvvedimento.equals("0301"))
										lAction = "siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioni";

									if (lMotivoProvvedimento.equals("0922"))
										lAction = "siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLAErgastolo";

									if (lMotivoProvvedimento.equals("0923"))
										lAction = "siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLALibero";

									//ANNOTAZIONE PROVVEDIMENTO DI CUMULO
									if (lMotivoProvvedimento.equals("0356") || lMotivoProvvedimento.equals("0357"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioAnnProvCumulo";

									//RICHIESTE GENERICHE
									if (lMotivoProvvedimento.equals("0351") || lMotivoProvvedimento.equals("0336") || lMotivoProvvedimento.equals("0337") || //parere
											lMotivoProvvedimento.equals("0338") || //visto
											lMotivoProvvedimento.equals("0342") || //ricorso
											lMotivoProvvedimento.equals("0339") || lMotivoProvvedimento.equals("0340") || //trasmissione
											lMotivoProvvedimento.equals("0320") || lMotivoProvvedimento.equals("0321") || //richiesta
											lMotivoProvvedimento.equals("0322") || lMotivoProvvedimento.equals("0323") || //richiesta
											lMotivoProvvedimento.equals("0324") || lMotivoProvvedimento.equals("0325") || //richiesta
											lMotivoProvvedimento.equals("0344") || lMotivoProvvedimento.equals("0326") || //richiesta
											lMotivoProvvedimento.equals("0327") || lMotivoProvvedimento.equals("0328") || //richiesta
											lMotivoProvvedimento.equals("0329") || lMotivoProvvedimento.equals("0330") || //richiesta
											lMotivoProvvedimento.equals("0331") || lMotivoProvvedimento.equals("0332") || //richiesta
											lMotivoProvvedimento.equals("0333") || lMotivoProvvedimento.equals("0345") || //richiesta
											lMotivoProvvedimento.equals("0334") || lMotivoProvvedimento.equals("0335") || //richiesta
											lMotivoProvvedimento.equals("0346") || lMotivoProvvedimento.equals("0343") || //richiesta
											lMotivoProvvedimento.equals("0347") || lMotivoProvvedimento.equals("0348") || //richiesta
											lMotivoProvvedimento.equals("0349") || lMotivoProvvedimento.equals("0350") || //richiesta
											lMotivoProvvedimento.equals("0341") || lMotivoProvvedimento.equals("0352")) //comunicazioni
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiestaGenerica";

								}
							}
//							}
						}
					} //fine di tutti gli else per evitare di far eseguire tutti gli if...
		Logger.getRootLogger().info("*** REWORK --> Action di Dettaglio restituita NORMALMENTE = " + lAction);

		return lAction;
	}

//	MA Affidamento in PRova Concessione 0370, 0371
//	siap.siep.action.ActDettaglioMAAffidamentoinProva

//	0005,0009,0010,0013 ActDettaglioMSDetenzioneD0miciliare

//	0004 ActDettaglioMASemiliberta

//	0014,0015,0086,0087,0089,0016,0091 ActDettaglioRevocaMA

//	2145,2146,2147,2149,2150,2151,2148 ActDettaglioMASospProvv

//	Ripristino ----?

	/**
	 * restituisce l'aCtion di Stampa
	 * @param lMotivoProvvedimento
	 * @return
	 */
	public String getActionStampaProvvedimento(String lMotivoProvvedimento)
	{
		String lAction = "";
		if (lMotivoProvvedimento.equals("0370") || lMotivoProvvedimento.equals("0002") ||
				lMotivoProvvedimento.equals("0003"))
			lAction = "siap.siep.misuraalternativa.action.ActStampaMAAffidamentoInProva";
		else
		{
//			0004 ActDettaglioMASemiliberta
			if (lMotivoProvvedimento.equals("0004"))
				lAction = "siap.siep.misuraalternativa.action.ActStampaMASemiliberta";
			else
			{
//				0005,0009,0010,0013 ActDettaglioMSDetenzioneDomiciliare
				if (lMotivoProvvedimento.equals("0005") || lMotivoProvvedimento.equals("0009") ||
						lMotivoProvvedimento.equals("0010") || lMotivoProvvedimento.equals("0013"))
					lAction = "siap.siep.misuraalternativa.action.ActStampaMADetenzioneDomiciliare";
				else
				{
//					0014,0015,0086,0087,0089,0016,0091 ActDettaglioRevocaMA
					if (lMotivoProvvedimento.equals("0014") || lMotivoProvvedimento.equals("0015") ||
							lMotivoProvvedimento.equals("0086") || lMotivoProvvedimento.equals("0087") ||
							lMotivoProvvedimento.equals("0089") || lMotivoProvvedimento.equals("0016") ||
							lMotivoProvvedimento.equals("0091"))
						lAction = "siap.siep.misuraalternativa.action.ActStampaRevocaMA";
					else
					{
//						2145,2146,2147,2149,2150,2151,2148 ActDettaglioMASospProvv
						if (lMotivoProvvedimento.equals("2145") || lMotivoProvvedimento.equals("2146") ||
								lMotivoProvvedimento.equals("2147") || lMotivoProvvedimento.equals("2149") ||
								lMotivoProvvedimento.equals("2150") || lMotivoProvvedimento.equals("2151") ||
								lMotivoProvvedimento.equals("2148"))
							lAction = "siap.siep.misuraalternativa.action.ActStampaMASospProvv";
						else
						{
							if (lMotivoProvvedimento.equals("0116") || lMotivoProvvedimento.equals("0167") ||
									lMotivoProvvedimento.equals("0167") || lMotivoProvvedimento.equals("0169") ||
									lMotivoProvvedimento.equals("0171") || lMotivoProvvedimento.equals("0168") ||
									lMotivoProvvedimento.equals("0170") || lMotivoProvvedimento.equals("01782"))
								lAction = "siap.siep.misuraalternativa.action.ActStampaMARipristino";
							else
							{
								if (lMotivoProvvedimento.equals("0057"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaOECondannatoLibero";
								if (lMotivoProvvedimento.equals("0058"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaOEDetenutoQC";
								if (lMotivoProvvedimento.equals("0059"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaOEArrestiDomiciliari";
								if (lMotivoProvvedimento.equals("0060"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaOEArrestiDomiciliari";
								if (lMotivoProvvedimento.equals("0061"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaLSLibero";
								if (lMotivoProvvedimento.equals("0062"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaLSAltraCausa";
								if (lMotivoProvvedimento.equals("0063"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaLSArrestiDomiciliari";
								if (lMotivoProvvedimento.equals("0078"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaRevocaLSLibero";
								if (lMotivoProvvedimento.equals("0079"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaRevocaLSAltraCausa";
								if (lMotivoProvvedimento.equals("0104"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaLSLiberoIstanzaProdotta";
								if (lMotivoProvvedimento.equals("0117"))
									lAction = "siap.siep.ordineesecuzione.action.ActStampaLSAltraCausa";
							}
						}
					}
				}
			}
		}

		Logger.getRootLogger().debug("*** REWORK --> Action di Dettaglio restituita NORMALMENTE = " + lAction);
		return lAction;
	}

	/**
	 * Restituisce il Dettagliko di un provveidmento a partire da un aserie di parametri
	 * Passati
	 * @param lMotivoProvvedimento - Motivo Provvedimento
	 * @param lTipoEvento - Tipo Evento
	 * @param lTipoProvvedimento - Tipo Provveidmento
	 * @param lTemIdTemplate - IdTemplate
	 * @return
	 */
	public String getActionDettaglioProvvedimento(
			String lMotivoProvvedimento,
			String lTipoEvento,
			String lTipoProvvedimento,
			String lTemIdTemplate,
			String lValidato)
	{

		String lAction = "";
		if ((lTipoProvvedimento.equals("02") || lTipoProvvedimento.equals("03"))
				&& (lTemIdTemplate != null && lTemIdTemplate.equals("*")))
			return "siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico";

		/**
		 * Test sul Rework del dettaglio
		 */
		if (SIESSwitch.isReworkDettaglio())
		{
			/**
			 * Chiamaata al DettaglioProvvedimentoManager
			 */
			lAction = DettaglioProvvedimentoManager.getInstance().getActionDettaglio(lTipoEvento, lTipoProvvedimento, lMotivoProvvedimento, lValidato);

			if (!lAction.equals("siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico"))
			{
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				//siesLogger.info("REWORK --> Action di Dettaglio restituita = " + lAction);
				return lAction;
			}
		} //Fine REWORK

		// Esecuzione Pena accessoria
		if (lTipoEvento != null && lTipoEvento.equals("16"))
			lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioEsecuzionePA";
		else

			// Richiesta al G.E.
			if (lTipoEvento != null && lTipoEvento.equals("17"))
				lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE";
			else

				// Comunicazione per Pena accessoria
				if (lTipoEvento != null && lTipoEvento.equals("18"))
					lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione";
				else
					if (lMotivoProvvedimento.equals("0370") || lMotivoProvvedimento.equals("0371") || //affidamento
							lMotivoProvvedimento.equals("0226") || lMotivoProvvedimento.equals("0227") || //affidamento
							lMotivoProvvedimento.equals("0244") || lMotivoProvvedimento.equals("0245") || //affidamento
							lMotivoProvvedimento.equals("0373") || //semilibertà
							lMotivoProvvedimento.equals("0372") || lMotivoProvvedimento.equals("0228") || //detenzione domiciliare
							lMotivoProvvedimento.equals("0229") || //detenzione domiciliare
							lMotivoProvvedimento.equals("2245")) //L.207/2003
						lAction = "siap.siep.misuraalternativa.action.ActDettaglioConcessione";
					else
					{
						if (lMotivoProvvedimento.equals("0087") || lMotivoProvvedimento.equals("0089")
								|| lMotivoProvvedimento.equals("0016") || lMotivoProvvedimento.equals("0091")
								|| lMotivoProvvedimento.equals("0088"))
							lAction = "siap.siep.misuraalternativa.action.ActDettaglioRevocaMA";
						else
						{
//							2145,2146,2147,2149,2150,2151,2148 ActDettaglioMASospProvv
							if (lMotivoProvvedimento.equals("2145") || lMotivoProvvedimento.equals("2146") ||
									lMotivoProvvedimento.equals("2147") || lMotivoProvvedimento.equals("2149") ||
									lMotivoProvvedimento.equals("2150") || lMotivoProvvedimento.equals("2151") ||
									lMotivoProvvedimento.equals("2148") || lMotivoProvvedimento.equals("2293") ||
									lMotivoProvvedimento.equals("0388") || lMotivoProvvedimento.equals("0389") ||
									lMotivoProvvedimento.equals("0390") || lMotivoProvvedimento.equals("0383") ||
									lMotivoProvvedimento.equals("0384") || lMotivoProvvedimento.equals("0385") ||
									lMotivoProvvedimento.equals("0386") || lMotivoProvvedimento.equals("0387") ||
									lMotivoProvvedimento.equals("0380") || lMotivoProvvedimento.equals("0455") ||
									lMotivoProvvedimento.equals("0456") || lMotivoProvvedimento.equals("0457"))
								lAction = "siap.siep.misuraalternativa.action.ActDettaglioMASospProvv";
							else
							{
								if (lMotivoProvvedimento.equals("0057"))
									lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOELibero";
								else
								{
									if (lMotivoProvvedimento.equals("0058"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC";

									if (lMotivoProvvedimento.equals("0059"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0060"))
										lAction = "siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0061"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSLibero";

									if (lMotivoProvvedimento.equals("0246"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni";

									if (lMotivoProvvedimento.equals("0062"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa";

									if (lMotivoProvvedimento.equals("0063"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSArrestiDomiciliari";

									if (lMotivoProvvedimento.equals("0078") || lMotivoProvvedimento.equals("0079")
											|| lMotivoProvvedimento.equals("0080"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioRevocaLSAltrePosizioni";

									if (lMotivoProvvedimento.equals("0104"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSLiberoIstanzaProdotta";

									if (lMotivoProvvedimento.equals("0117"))
										lAction = "siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa";

									if (lMotivoProvvedimento.equals("0130") || lMotivoProvvedimento.equals("0131")
											|| lMotivoProvvedimento.equals("0132") || lMotivoProvvedimento.equals("0133")
											|| lMotivoProvvedimento.equals("0134"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioEmissioneProvvedimento";

//									REVOCA viene gestita nell'azione ActDettaglioRevocaMAAffInProva
									if (lMotivoProvvedimento.equals("0014") || lMotivoProvvedimento.equals("0015") ||
											lMotivoProvvedimento.equals("0086") || lMotivoProvvedimento.equals("0196"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioRevocaMAAffInProva";

//									ripristino viene gestita nell'azione ActDettaglioMARipristino
									if (lMotivoProvvedimento.equals("0304") || lMotivoProvvedimento.equals("0305") ||
											lMotivoProvvedimento.equals("0306") || lMotivoProvvedimento.equals("0307") ||
											lMotivoProvvedimento.equals("0308") || lMotivoProvvedimento.equals("0309") ||
											lMotivoProvvedimento.equals("0310") || lMotivoProvvedimento.equals("0410") ||
											lMotivoProvvedimento.equals("0311"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMARipristino";

//									perdita efficacia
									if (lMotivoProvvedimento.equals("2160") || lMotivoProvvedimento.equals("2161")
											|| lMotivoProvvedimento.equals("2162") || lMotivoProvvedimento.equals("2163")
											|| lMotivoProvvedimento.equals("2164") || lMotivoProvvedimento.equals("2165")
											|| lMotivoProvvedimento.equals("2166") || lMotivoProvvedimento.equals("2289")
											|| lMotivoProvvedimento.equals("2167"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAPerditaEfficacia";

//									prosecuzione provvisoria senza cumulo
									if (lMotivoProvvedimento.equals("0375") || lMotivoProvvedimento.equals("0374")
											|| lMotivoProvvedimento.equals("0376") || lMotivoProvvedimento.equals("0382")
											|| lMotivoProvvedimento.equals("0377") || lMotivoProvvedimento.equals("0379")
											|| lMotivoProvvedimento.equals("0378")
											|| lMotivoProvvedimento.equals("0381"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione";

									if (lMotivoProvvedimento.equals("0391") //affidamento
											|| lMotivoProvvedimento.equals("0235") //affidamento
											|| lMotivoProvvedimento.equals("0236") //affidamento
											|| lMotivoProvvedimento.equals("0392") //detenzione
											|| lMotivoProvvedimento.equals("0237") //detenzione
											|| lMotivoProvvedimento.equals("0238") //detenzione
											|| lMotivoProvvedimento.equals("0239") //detenzione
											|| lMotivoProvvedimento.equals("0240") //detenzione
											|| lMotivoProvvedimento.equals("0393") //semilibertà
											|| lMotivoProvvedimento.equals("0450") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0241") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0242") //affidamento cumulo
											|| lMotivoProvvedimento.equals("0451") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0452") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0453") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0458") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0459") //detenzione cumulo
											|| lMotivoProvvedimento.equals("0454") //semilibertà cumulo
									)
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAEstensioneDefinitiva";

//									rigetto misura alternativa
									//if (lMotivoProvvedimento.equals("9000") || lMotivoProvvedimento.equals("9001"))
									if (lMotivoProvvedimento.equals("0217") || lMotivoProvvedimento.equals("0218"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMARigetto";

//									detenzione domiciliare a termine
									if (lMotivoProvvedimento.equals("0011") || lMotivoProvvedimento.equals("0197")
											|| lMotivoProvvedimento.equals("2340"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMADetDomTemp";

//									ammisione provisoria a detenzione domiciliare
									if (lMotivoProvvedimento.equals("2005") || lMotivoProvvedimento.equals("2006"))
										lAction = "siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria";

//									Archiviazione per fine espiazione
									if (lMotivoProvvedimento.equals("0096") || lMotivoProvvedimento.equals("0097")
											|| lMotivoProvvedimento.equals("0098") || lMotivoProvvedimento.equals("0099")
											|| lMotivoProvvedimento.equals("0214") || lMotivoProvvedimento.equals("0215")
											|| lMotivoProvvedimento.equals("0216"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioPenaEspiata";

//									Archiviazione per provvedimento altra autorità
									if (lMotivoProvvedimento.equals("0411") || lMotivoProvvedimento.equals("0412")
											|| lMotivoProvvedimento.equals("0413") || lMotivoProvvedimento.equals("0414")
											|| lMotivoProvvedimento.equals("0415") || lMotivoProvvedimento.equals("0416")
											|| lMotivoProvvedimento.equals("0417") || lMotivoProvvedimento.equals("0418")
											|| lMotivoProvvedimento.equals("0419") || lMotivoProvvedimento.equals("0420")
											|| lMotivoProvvedimento.equals("0421") || lMotivoProvvedimento.equals("0422")
											|| lMotivoProvvedimento.equals("0423") || lMotivoProvvedimento.equals("0424")
											|| lMotivoProvvedimento.equals("0425") || lMotivoProvvedimento.equals("0426"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita";

//									Archiviazione per perdità di competenza
									if (lMotivoProvvedimento.equals("0019") || lMotivoProvvedimento.equals("0022"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioAssorCumulo";

//									Archiviazione per Non Luogo a Provvedere
									if (lMotivoProvvedimento.equals("0006") || lMotivoProvvedimento.equals("0007")
											|| lMotivoProvvedimento.equals("0008") || lMotivoProvvedimento.equals("0009")
											|| lMotivoProvvedimento.equals("0119") || lMotivoProvvedimento.equals("0120")
											|| lMotivoProvvedimento.equals("0353"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere";

									if (lMotivoProvvedimento.equals("0221") || lMotivoProvvedimento.equals("0274")
											|| lMotivoProvvedimento.equals("0354") || lMotivoProvvedimento.equals("0355"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioDifferimentoOE";

									if (lMotivoProvvedimento.equals("0263") || lMotivoProvvedimento.equals("0241"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv";

									if (lMotivoProvvedimento.equals("0265"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensionePena";

									if (lMotivoProvvedimento.equals("0210"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteDepenIncost";

									if (lMotivoProvvedimento.equals("0082"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione";

									if (lMotivoProvvedimento.equals("0297"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichAccDataReato";

									if (lMotivoProvvedimento.equals("0289"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichDetPenAboReato";

									if (lMotivoProvvedimento.equals("0287") || lMotivoProvvedimento.equals("0288")
											|| lMotivoProvvedimento.equals("0290") || lMotivoProvvedimento.equals("0291")
											|| lMotivoProvvedimento.equals("0292") || lMotivoProvvedimento.equals("0294")
											|| lMotivoProvvedimento.equals("0296"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteConCodice";

									if (lMotivoProvvedimento.equals("0293") || lMotivoProvvedimento.equals("0288")
											|| lMotivoProvvedimento.equals("0290") || lMotivoProvvedimento.equals("0295"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiesteDepenIncost";

									if (lMotivoProvvedimento.equals("0289"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichDetPenAboReato";

									if (lMotivoProvvedimento.equals("0210"))
										lAction = "siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniDepen";

									if (lMotivoProvvedimento.equals("0367"))
										lAction = "siap.siep.richiesta.action.ActDettaglioOrdineScarcerazioneProvv";

									if (lMotivoProvvedimento.equals("0121"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniStessoTitolo";

									if (lMotivoProvvedimento.equals("0212"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAltroTitolo";
									/*
            if (lMotivoProvvedimento.equals("0213"))
              lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniSenzaTitolo";
									 */
									if (lMotivoProvvedimento.equals("0266") || lMotivoProvvedimento.equals("0267")
											|| lMotivoProvvedimento.equals("0268") || lMotivoProvvedimento.equals("0269")
											|| lMotivoProvvedimento.equals("0270"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioInterruzione";

									if (lMotivoProvvedimento.equals("0276"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioNotificheDifferimento";

									if (lMotivoProvvedimento.equals("0081"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipata";

									if (lMotivoProvvedimento.equals("0083"))
										lAction = "siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipataMA";

									if (lMotivoProvvedimento.equals("0282"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioDecretoSospensione";

									if (lMotivoProvvedimento.equals("0277")
											|| lMotivoProvvedimento.equals("0222")
											|| lMotivoProvvedimento.equals("0223")
											|| lMotivoProvvedimento.equals("0224"))
										lAction = "siap.siep.cumulo.action.ActDettaglioCumuloStampa";

									if (lMotivoProvvedimento.equals("0272"))
										lAction = "siap.siep.ripristino.action.ActDettaglioProvvedimentoRipristino";

									if (lMotivoProvvedimento.equals("0339") || lMotivoProvvedimento.equals("0340"))
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiestaGenerica";

									if (lMotivoProvvedimento.equals("0284"))//Per l'indulto questo dettalgio non funziona
										//lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAmnistiaIndulto";
										lAction = "siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico";


									if (lMotivoProvvedimento.equals("0285"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniDepenalizzazione";

									if (lMotivoProvvedimento.equals("0286"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniIncostituzionalita";

									if (lMotivoProvvedimento.equals("2000"))
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensione";

									if (lMotivoProvvedimento.equals("0902") || lMotivoProvvedimento.equals("0901") || lMotivoProvvedimento.equals("0903") || lMotivoProvvedimento.equals("0900")
											|| lMotivoProvvedimento.equals("0920") || lMotivoProvvedimento.equals("0921")
								             // Aggiunti i 2 nuovi codice MOTIVO_PROVVEDIMENTO  Luigi 20-08-2010
											|| lMotivoProvvedimento.compareTo("0947")==0 || lMotivoProvvedimento.compareTo("0952")==0 )
										lAction = "siap.siep.sospensione.action.ActLoadDettaglioSospensioneEsecPenaDispPm";

									if (lMotivoProvvedimento.equals("0158") || lMotivoProvvedimento.equals("0159") || lMotivoProvvedimento.equals("0160") || lMotivoProvvedimento.equals("0174") ||
											lMotivoProvvedimento.equals("0175"))
										lAction = "siap.siep.calcolopena.action.ActDettaglioOrdineScarcerazionePerNuovaScadenzaPena";

									if (lMotivoProvvedimento.equals("0913") || lMotivoProvvedimento.equals("0914") || lMotivoProvvedimento.equals("0915") ||
											lMotivoProvvedimento.equals("0916") || lMotivoProvvedimento.equals("0917") || lMotivoProvvedimento.equals("0918") ||
											lMotivoProvvedimento.equals("0919"))
										lAction = "siap.siep.calcolopena.action.ActLoadDettaglioRidetPena";

									if (lMotivoProvvedimento.equals("0300"))
										lAction = "siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioniAN";

									if (lMotivoProvvedimento.equals("0298") || lMotivoProvvedimento.equals("0299") || lMotivoProvvedimento.equals("0301"))
										lAction = "siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioni";

									if (lMotivoProvvedimento.equals("0922"))
										lAction = "siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLAErgastolo";

									if (lMotivoProvvedimento.equals("0923"))
										lAction = "siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLALibero";

									//ANNOTAZIONE PROVVEDIMENTO DI CUMULO
									if (lMotivoProvvedimento.equals("0356") || lMotivoProvvedimento.equals("0357"))
										lAction = "siap.siep.archiviazione.action.ActLoadDettaglioAnnProvCumulo";

									//RICHIESTE GENERICHE
									if (lMotivoProvvedimento.equals("0351") || lMotivoProvvedimento.equals("0336") || lMotivoProvvedimento.equals("0337") || //parere
											lMotivoProvvedimento.equals("0338") || //visto
											lMotivoProvvedimento.equals("0342") || //ricorso
											lMotivoProvvedimento.equals("0339") || lMotivoProvvedimento.equals("0340") || //trasmissione
											lMotivoProvvedimento.equals("0320") || lMotivoProvvedimento.equals("0321") || //richiesta
											lMotivoProvvedimento.equals("0322") || lMotivoProvvedimento.equals("0323") || //richiesta
											lMotivoProvvedimento.equals("0324") || lMotivoProvvedimento.equals("0325") || //richiesta
											lMotivoProvvedimento.equals("0344") || lMotivoProvvedimento.equals("0326") || //richiesta
											lMotivoProvvedimento.equals("0327") || lMotivoProvvedimento.equals("0328") || //richiesta
											lMotivoProvvedimento.equals("0329") || lMotivoProvvedimento.equals("0330") || //richiesta
											lMotivoProvvedimento.equals("0331") || lMotivoProvvedimento.equals("0332") || //richiesta
											lMotivoProvvedimento.equals("0333") || lMotivoProvvedimento.equals("0345") || //richiesta
											lMotivoProvvedimento.equals("0334") || lMotivoProvvedimento.equals("0335") || //richiesta
											lMotivoProvvedimento.equals("0346") || lMotivoProvvedimento.equals("0343") || //richiesta
											lMotivoProvvedimento.equals("0347") || lMotivoProvvedimento.equals("0348") || //richiesta
											lMotivoProvvedimento.equals("0349") || lMotivoProvvedimento.equals("0350") || //richiesta
											lMotivoProvvedimento.equals("0341") || lMotivoProvvedimento.equals("0352")) //comunicazioni
										lAction = "siap.siep.richiesta.action.ActDettaglioRichiestaGenerica";

								}
							}
//							}
						}
					} //fine di tutti gli else per evitare di far eseguire tutti gli if...

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("*** NO *** REWORK --> " + lAction);

		return lAction;
	}

	/**
	 * Metodo che restituisce l'action di upload per il tipo di evento passato
	 * 
	 * @param EventoModel - Evento Model
	 * @return
	 */
	public String getActionDettaglioUpload(EventoModel aEvento)
	{

		String lAction = "";

		/**
		 * Chiamaata al DettaglioProvvedimentoManager
		 */
		lAction = DettaglioProvvedimentoManager.getInstance().getActionUpload(aEvento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("Action di UPLOAD restituita = " + lAction);

	
		return lAction;
	}
}