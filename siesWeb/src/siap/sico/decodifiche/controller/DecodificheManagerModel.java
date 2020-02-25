package siap.sico.decodifiche.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import f3b.util.F3BException;

/**
 * Classe con le decodifiche senza init iniziale
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecodificheManagerModel extends DecodificheManagerCore {

	private Collection mTipoDefinizione = null; // 8/4/2005
	private Collection mEsitoProvvedimento = null; // 23/07/2005
	private Collection mSedeGabPS = null; // 15/09/2006
	private Collection mAttesaArchiviazione = null;

	private Collection mTipoPrescrizione = null;

	// Dati SIEPE
	private Collection mTipoUfficioSiepe = null; // UEPE 18/08/2006
	// Modifica del 23/02/2016 Nuova Infrastruttura - INIZIO ******
	private Collection mTipoUfficioSiepeMinorili = null;
	// Nuova Infrastruttura - FINE ******
	private Collection mTipoIncaricoSiepe = null;
	private Collection mTipoAttivitaSiepe = null;
	private Collection mTipoRichiestaSiepe = null;
	private Collection mTipoRichiedenteSiepe = null;
	private Collection mEsitoAttivita = null;
	private Collection mStatoRicezioneSiepe = null;
	private Collection mTipoDefinizioneSiepe = null;

	private Collection mEsitoPermessoLicenza = null;
	private Collection mTipoEventoPermessoLicenza = null;
	private Collection mTipoConseguenza = null;

	// Dati SIGE
	private Collection mTipoAttoSige = null;
	private Collection mTipoRichiedenteSige = null;
	private Collection mTipoGiudizioSige = null;
	private Collection mTipiUfficioSige = null;
	private Collection mTipiUfficioSigeAccorpato = null;
	private Collection mRuoloGiudicePopolare = null;
	private Collection mOggettoSige = null;
	private Collection mEsitoTenoreSige = null;
	private Collection mTipoProvvedimentoSige = null;

	private Collection mDestinatarioDepositoSige = null;
	private Collection mEsitoIstanza = null;
	private Collection mStatoNuovaIstanza = null;

	private Collection mForo = null; // 19/03/2010
	private Collection mForoAll = null; // 07/2015

	private Collection mTipoRichiestaMisSic = null; // 10/11/2013
	private Collection mTipoAutoritaMittenteIstanza = null; // 19/04/2010
	private Collection mTipoCuratore = null; // 06/05/2011
	private Collection mPosizioneGiuridica = null;

    private Collection mStatoIstruttoriaCumulo = null; // 04/10/2017
	
	private Collection mContenutiSige = null;

	private Collection<DecodificheModel> mTipoAttiInArchivio = null;

	public Collection getSedeGabPS() throws Exception {
		// Se ancora null, viene letto dal DB
		if (mSedeGabPS == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("SEDE_GAB_POL_SCI");
			mSedeGabPS = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mSedeGabPS;
	}

	public Collection getTipoDefinizione() throws Exception {
		// Se ancora non letto viene letto dal DB
		if (mTipoDefinizione == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_DEFINIZIONE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoDefinizione = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mTipoDefinizione;
	}

	public Collection getEsitoProvvedimento() throws Exception {
		// Se ancora non letto viene letto dal DB
		if (mEsitoProvvedimento == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("ESITO_PROVVEDIMENTO");
			mEsitoProvvedimento = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mEsitoProvvedimento;
	}

	// 18/08/2006 UEPE
	public Collection getTipoUfficioSiepe() throws Exception {
		// Se ancora non letto viene letto dal DB
		if (mTipoUfficioSiepe == null) {
			Vector lVectTipoUfficioSiepe = new Vector();
			lVectTipoUfficioSiepe.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			lVectTipoUfficioSiepe.add(new DecodificheModel("UEPE", "UFFICIO DI ESECUZIONE PENALE ESTERNA",
					"TIPO_UFFICIO_SIEPE", "", "", "", "", "", ""));
			lVectTipoUfficioSiepe.add(new DecodificheModel("UEPESS",
					"UFFICIO DI ESECUZIONE PENALE ESTERNA - SEDE DI SERVIZIO", "TIPO_UFFICIO_SIEPE", "", "",
					"", "", "", ""));

			mTipoUfficioSiepe = lVectTipoUfficioSiepe;
		}
		return this.mTipoUfficioSiepe;
	}

	// 12/05/2015 UEPE
	public Collection getTipoUfficioSiepeMinorili() throws Exception {
		// Se ancora non letto viene letto dal DB
		if (mTipoUfficioSiepeMinorili == null) {
			Vector lVectTipoUfficioSiepe = new Vector();
			lVectTipoUfficioSiepe.add(new DecodificheModel("-", "-", "-", "-", "-", "-", "-", "-", "-"));
			lVectTipoUfficioSiepe.add(new DecodificheModel("UEPE", "UFFICIO DI ESECUZIONE PENALE ESTERNA",
					"TIPO_UFFICIO_SIEPE", "", "", "", "", "", ""));
			lVectTipoUfficioSiepe.add(new DecodificheModel("UEPESS",
					"UFFICIO DI ESECUZIONE PENALE ESTERNA - SEDE DI SERVIZIO", "TIPO_UFFICIO_SIEPE", "", "",
					"", "", "", ""));
			lVectTipoUfficioSiepe.add(new DecodificheModel("USSM", "UFFICIO SERVIZI SOCIALI MINORILI",
					"TIPO_UFFICIO_SIEPE", "", "", "", "", "", ""));
			lVectTipoUfficioSiepe.add(new DecodificheModel("USSMSS",
					"UFFICIO SERVIZI SOCIALI MINORILI - SEDE DI SERVIZIO", "TIPO_UFFICIO_SIEPE", "", "", "",
					"", "", ""));

			mTipoUfficioSiepeMinorili = lVectTipoUfficioSiepe;
		}
		return this.mTipoUfficioSiepeMinorili;
	}

	/**
	 * Restituisce i tipi di Incarichi previsti nel sistema SIEPE.
	 * 
	 * @return Collection di DecodificheModel
	 * @throws Exception
	 */
	public Collection getTipoIncaricoSiepe() throws Exception {
		if (mTipoIncaricoSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("TIPO_INCARICO_SIEPE");
			mTipoIncaricoSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mTipoIncaricoSiepe;
	}

	/**
	 * Restituisce i tipi di Attivita' previsti nel sistema SIEPE.
	 * <p>
	 * 
	 * @return Collection di DecodificheModel
	 * @throws Exception
	 */
	public Collection getTipoAttivitaSiepe() throws Exception {
		if (mTipoAttivitaSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("TIPO_ATTIVITA_SIEPE");
			mTipoAttivitaSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mTipoAttivitaSiepe;
	}

	/**
	 * Restituisce i tipi di richiesta in ambito SIEPE/UEPE.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return Collection di DecodificheModel
	 */
	public Collection getTipoRichiestaSiepe() throws Exception {
		if (mTipoRichiestaSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("TIPO_RICHIESTA_SIEPE");
			mTipoRichiestaSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mTipoRichiestaSiepe;
	}

	/**
	 * Restituisce i tipi di richiesta in ambito SIEPE/UEPE.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return Collection di DecodificheModel
	 */
	public Collection getTipoRichiedenteSiepe() throws Exception {
		if (mTipoRichiedenteSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("TIPO_RICHIEDENTE_SIEPE");
			mTipoRichiedenteSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoRichiedenteSiepe;
	}

	/**
	 * Restituisce gli Esiti per le Attivita' in ambito SIEPE/UEPE.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return Collection di DecodificheModel
	 */
	public Collection getEsitoAttivitaSiepe() throws Exception {
		if (mEsitoAttivita == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("ESITO_ATTIVITA_SIEPE");
			mEsitoAttivita = lDecodifiche.ExRicercaDecodifiche(lModel);
		}

		return mEsitoAttivita;
	}

	/**
	 * Restituisce lo stato ricezione dei messaggi in ambito SIEPE/UEPE.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return Collection di DecodificheModel
	 */
	public Collection getStatoRicezioneSiepe() throws Exception {
		if (mStatoRicezioneSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("STATO_RICEZIONE_SIEPE");
			mStatoRicezioneSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mStatoRicezioneSiepe;
	}

	// 06/06/2007 Un commento non fa male. Nuovo Dominio caricato nel Singleton "SOLO QUANDO SERVE!"
	public Collection getAttesaArchiviazione() throws Exception {
		if (mAttesaArchiviazione == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("ATT_ARC");
			mAttesaArchiviazione = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		}
		return mAttesaArchiviazione;
	}

	/**
	 * Restituisce l'elenco con i tipi di definizione procedimento in ambito SIEPE/UEPE. Inoltre, l'insieme
	 * viene caricato nel signleton solo all'occorrenza.
	 * <p>
	 * 
	 * @return Collection di decodifica model.
	 * @throws Exception
	 *             Propaga erroi di eccezione.
	 */
	public Collection getTipoDefinzioneSiepe() throws Exception {
		if (mTipoDefinizioneSiepe == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_DEFINIZIONE_SIEPE");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoDefinizioneSiepe = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoDefinizioneSiepe;
	}

	/**
	 * Restituisce l'elenco delle prescrizioni con RV_DOMAIN = TIPO_PRESCRIZIONE.
	 * <p>
	 * 
	 * @return Collection di decodifica model.
	 * @throws Exception
	 *             Propaga erroi di eccezione.
	 */
	public Collection getTipoPrescrizione() throws Exception {
		if (mTipoPrescrizione == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_PRESCRIZIONE");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoPrescrizione = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		}
		return mTipoPrescrizione;
	}

	public Collection getEsitoPermessoLicenza() throws Exception {
		if (mEsitoPermessoLicenza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("ESITO_PERMESSO_LICENZA");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mEsitoPermessoLicenza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mEsitoPermessoLicenza;
	}

	public Collection getTipoEventoPermessoLicenza() throws Exception {
		if (mTipoEventoPermessoLicenza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_EVENTO_PERMESSO_LICENZA");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoEventoPermessoLicenza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoEventoPermessoLicenza;
	}

	public Collection getTipoConseguenza() throws Exception {
		if (mTipoConseguenza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_CONSEGUENZA");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoConseguenza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoConseguenza;
	}

	/**
	 * restituisce la codifica del "Tipo Atto Sige" ovvero il Tipo Richiesta Sige.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipoAttoSige() throws Exception {
		if (mTipoAttoSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_ATTO_SIGE");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoAttoSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoAttoSige;
	}

	/**
	 * restituisce la codifica del "Tipo Richiedente Sige" ovvero il tipo del Mittente di una Richiesta SIGE.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipoRichiedenteSige() throws Exception {
		if (mTipoRichiedenteSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_RICHIEDENTE_SIGE");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoRichiedenteSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoRichiedenteSige;
	}

	/**
	 * restituisce la codifica del "Tipo Giudizio Sige".
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipoGiudizioSige() throws Exception {
		if (mTipoGiudizioSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_GIUDIZIO_SIGE");

			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoGiudizioSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoGiudizioSige;
	}

	//

	/**
	 * restituisce la lista dei "Tipi Ufficio Sige".
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipiUfficioSige() throws Exception {
		if (mTipiUfficioSige == null) {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipiUfficioSige = lDecodifiche.ExListaTipiUfficioSige();
		}
		return mTipiUfficioSige;
	}

	/**
	 * restituisce la lista dei "Tipi Ufficio Sige".
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipiUfficioSigeAccorpato() throws Exception {
		if (mTipiUfficioSigeAccorpato == null) {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipiUfficioSigeAccorpato = lDecodifiche.ExListaTipiUfficioSigeAccorpato();
		}
		return mTipiUfficioSigeAccorpato;
	}

	/**
	 * Aggiunge alla lista dei Tipi Ufficio Sige anche il trattino "-".
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getTipiUfficioSigeTrattino() throws Exception {
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setCode("-");
		lDecMod.setDescription("-");
		ArrayList lLista = new ArrayList(getTipiUfficioSige());
		lLista.add(lDecMod);
		return lLista;
	}

	public Collection getRuoloGiudicePopolare() throws Exception {
		if (mRuoloGiudicePopolare == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("RUOLO_GIUDICE_POPOLARE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mRuoloGiudicePopolare = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mRuoloGiudicePopolare;
	}

	public Collection getOggettoSige() throws Exception {
		if (mOggettoSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("OGGETTO_SIGE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mOggettoSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mOggettoSige;
	}

	public Collection getEsitoTenoreSige() throws Exception {
		if (mEsitoTenoreSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("ESITO_TENORE_SIGE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mEsitoTenoreSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mEsitoTenoreSige;
	}

	public Collection getTipoProvvedimentoSige() throws Exception {
		if (mTipoProvvedimentoSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_PROVVEDIMENTO_SIGE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoProvvedimentoSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoProvvedimentoSige;
	}

	public Collection getDestinatarioDepositoSige() throws Exception {
		if (mDestinatarioDepositoSige == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("DESTINATARIO_DEPOSITO_SIGE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mDestinatarioDepositoSige = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mDestinatarioDepositoSige;
	}

	public Collection getEsitoIstanza() throws Exception {
		if (mEsitoIstanza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("ESITO_ISTANZA");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mEsitoIstanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mEsitoIstanza;
	}

	public Collection getStatoNuovaIstanza() throws Exception {
		if (mStatoNuovaIstanza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("STATO_NUOVA_ISTANZA");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mStatoNuovaIstanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mStatoNuovaIstanza;
	}

	/**
	 * 19/03/2010 Restituisce la descrizione di tutti i Fori per gli Avvocati del Distretto.
	 * 
	 * @return mForo
	 * @throws Exception
	 */
	public Collection getForo() throws Exception {

		if (mForo == null) {
			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
			 * Numero MEV : SIES v10
			 * Autore : gioggi
			 * Data : 28/gen/2016
			 * Branch : MEV_SIES v10
			 */
			// Vector lVectForo = new Vector();
			// IAvvocato lCtrlAv = SICOLookupRemote.getAvvocatoRemote();
			// Vector lVect = lCtrlAv.ExRicercaForo();
			// for (int i = 0; i < lVect.size(); i++)
			// lVectForo.add(new DecodificheModel(((AvvocatoModel) lVect.elementAt(i)).getForo(),
			// ((AvvocatoModel) lVect.elementAt(i)).getForo(), "", "", "", "", "", "", ""));
			// mForo = lVectForo;
			// ***** FINE INTERVENTO MEV_SIES v10 *****//

			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("FORO_AVVOCATI");
			lModel.setCodiceAlternativo("VALIDO");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mForo = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		// valore di ritorno
		return mForo;
	}

	/**
	 * Retituisce TUTTI i fori anche quelli soppressi per le funzioni di ricerca
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection getForoAll() throws Exception {
		Vector lVectForo = new Vector();
		if (mForoAll == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("FORO_AVVOCATI");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();

			lVectForo = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
			for (int i = 0; i < lVectForo.size(); i++) {
				DecodificheModel lDecode = (DecodificheModel) lVectForo.elementAt(i);
				if ("SOPPRESSO".equals(lDecode.getCodiceAlternativo())) {
					lDecode.setDescription(lDecode.getDescription() + " (soppresso)");
				} else {
					// nulla
				}

			}
			mForoAll = lVectForo;
		}
		return mForoAll;
	}

	public Collection getTipoAutoritaMittenteIstanza() throws Exception {
		if (mTipoAutoritaMittenteIstanza == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("MITTENTE_ISTANZA");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoAutoritaMittenteIstanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoAutoritaMittenteIstanza;
	}

	public Collection getTipoCuratore() throws Exception {
		if (mTipoCuratore == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("TIPO_CURATORE");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoCuratore = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mTipoCuratore;
	}

	public Collection getRichiesteMisSic() throws F3BException {
		if (mTipoRichiestaMisSic == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lModel.setCodiceAlternativo("U023");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mTipoRichiestaMisSic = lDecodifiche.ExRicercaDecodifiche(lModel);
		}

		return mTipoRichiestaMisSic;
	}

	public Collection getPosizioneGiuridica() throws Exception {
		if (mPosizioneGiuridica == null) {
			// POSIZIONE_GIURIDICA ( tutte le occorrenze )
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("POSIZIONE_GIURIDICA");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mPosizioneGiuridica = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mPosizioneGiuridica;
	}

	/**
	 * Restituisce la lista dei "Contenuti Sige".
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Collection getContenutiSige() throws Exception {
		if (mContenutiSige == null) {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mContenutiSige = lDecodifiche.ExListaContenutiSige();
		}
		return mContenutiSige;
	}

	/**
	 * Aggiunge alla lista dei Contenuti Sige anche il trattino "-".
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Collection getContenutiSigeTrattino() throws Exception {
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setCode("-");
		lDecMod.setDescription("-");
		ArrayList lLista = new ArrayList();
		lLista.add(lDecMod);
		lLista.addAll(getContenutiSige());

		return lLista;
	}

	public Collection<DecodificheModel> getTipoAttiInArchivio() throws Exception {
		if (mTipoAttiInArchivio == null) {
			DecodificheModel lModel = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lModel.setContesto("ATTI_ARCHIVIO");
			mTipoAttiInArchivio = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return this.mTipoAttiInArchivio;
	}

	public void setTipoAttiInArchivio(Collection<DecodificheModel> mTipoAttiInArchivio) {
		this.mTipoAttiInArchivio = mTipoAttiInArchivio;
	}


	public Collection getStatoIstruttoriaCumulo() throws F3BException {
		if (mStatoIstruttoriaCumulo == null) {
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("STATO_ISTRUTTORIA_CUMULO");
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			mStatoIstruttoriaCumulo = lDecodifiche.ExRicercaDecodifiche(lModel);
		}
		return mStatoIstruttoriaCumulo;
	}
}