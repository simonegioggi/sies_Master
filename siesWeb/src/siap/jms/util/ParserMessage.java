package siap.jms.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istruttoriacumulo.model.DatiCumuloPerTrasferimentoModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel; // STUB 15/04/2005
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel; // SIEPE 19/02/2007
import siap.siepe.attivita.model.AttivitaModel; // SIEPE 23/10/2006
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel; // SIEPE 23/10/2006
import siap.siepe.relazione.model.RelazioneModel; // SIEPE 23/10/2006
import siap.siepe.richiesta.model.RichiestaModel; // SIEPE 23/10/2006
import siap.sius.avvocato.model.AvvocatoSiusModel; // STUB 07/04/2005
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel; // STUB 12/04/2005
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloGPTPModel; // 25/02/2008
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel; // STUB 15/04/2005
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.model.UdienzaModel;
import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: ParserMessage
 * </p>
 * <p>
 * Description: Classe che realizza l'analisi del TreeModel e che ne ricava gli oggetti Model contenuti al
 * livello 1
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParserMessage {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	SoggettoModel mSoggetto;
	FascicoloSiepModel mFascicolo;
	SentenzaModel mSentenza;
	EventoNotificaModel mEvento;
	IstanzaModel mIstanza;
	NuovaIstanzaModel mNuovaIstanza;
	FascicoloGPModel mFascicoloGPSius;
	DettaglioFascicoloModel mDettaglioFascicoloSiep;
	List mSoggettoArray = null;

	List mTenori = null;
	List mPrescrizioni = null;
	UdienzaModel mUdienza;
	// STUB 11/12/2003 Aggiunti Esperti, Magistrati, Assistente Giudiziario.
	AssistenteGiudiziarioModel mAssGiudiziario;
	List mEsperti = null;
	List mMagistrati = null;

	// STUB 21/03/2005
	List mLicenze = null;

	// STUB 07/04/2005
	List mAvvSius = null;

	MisuraAlternativaModel mMisuraAlternativa;
	MisuraSicurezzaModel mMisuraSicurezza;
	DepositoOrdinanzaPcModel mDepositoOrdinanza;
	DepositoSentenzaModel mDepositoSentenza;
	ImpugnazioneModel mImpugnazione;
	ResidenzaAssociataModel mResidenzaAssociata;
	DepositoDecretoModel mDepositoDecreto;
	List mMotivazioniDecreto = null;

	// STUB 12/04/2005.
	DocumentoAllegatoModel mDocumentoAllegato;

	// STUB 15/04/2005
	List mRifasiep = null;
	LuogoDetenzioneModel mLuogoDetenzione;

	// SIEPE 23/10/2006
	FascicoloSiepeEstesoModel mFasSiepeEst = null;
	AttivitaModel mAttivita = null;
	RichiestaModel mRichiesta = null;
	RelazioneModel mRelazione = null;
	AssistenteSocialeModel mAssistenteSociale = null;

	FascicoloGPTPModel mFascicoloGPTPSius = null;

	// MEV 26 Cumulo Step2
	DatiCumuloPerTrasferimentoModel mDatiCumuloPerTrasferimento = null;

	protected ParserMessage() {
	}

	public ParserMessage(TreeModel aTree) throws Exception {
		// Parser del TreeModel
		iterateObjectModel(aTree);
	}

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public EventoNotificaModel getEvento() {
		return mEvento;
	}

	public FascicoloSiepModel getFascicolo() {
		return mFascicolo;
	}

	public IstanzaModel getIstanza() {
		return mIstanza;
	}

	public NuovaIstanzaModel getNuovaIstanza() {
		return mNuovaIstanza;
	}

	public FascicoloGPModel getFascicoloGPSius() {
		return mFascicoloGPSius;
	}

	public List getTenori() {
		return mTenori;
	}

	public List getPrescrizioni() {
		return mPrescrizioni;
	}

	public ResidenzaAssociataModel getResidenzaAssociata() {
		return mResidenzaAssociata;
	}

	public UdienzaModel getUdienza() {
		return mUdienza;
	}

	public MisuraAlternativaModel getMisuraAlternativa() {
		return mMisuraAlternativa;
	}

	public MisuraSicurezzaModel getMisuraSicurezza() {
		return mMisuraSicurezza;
	}

	public DepositoOrdinanzaPcModel getDepositoOrdinanzaPc() {
		return mDepositoOrdinanza;
	}

	public DepositoSentenzaModel getDepositoSentenza() {
		return mDepositoSentenza;
	}

	public ImpugnazioneModel getImpugnazione() {
		return mImpugnazione;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return mDepositoDecreto;
	}

	public List getMotivazioniDecreto() {
		return mMotivazioniDecreto;
	}

	public DettaglioFascicoloModel getDettaglioFascicoloSiep() {
		return mDettaglioFascicoloSiep;
	}

	public List getSoggettoArray() {
		return mSoggettoArray;
	}

	// STUB 11/12/2003 Aggiunti per Trasmissione Ordinanza.
	public AssistenteGiudiziarioModel getAssGiudiziario() {
		return mAssGiudiziario;
	}

	public List getEsperti() {
		return mEsperti;
	}

	public List getMagistrati() {
		return mMagistrati;
	}

	// STUB 21/03/2005 Aggiunta per Trasmissione Ordinanza.
	public List getLicenze() {
		return mLicenze;
	}

	// STUB 07/04/2005 Aggiunta per Trasmissione Avvocati SIUS.
	public List getAvvSius() {
		return mAvvSius;
	}

	// STUB 12/04/2005 Aggiunto Documento Allegato.
	public DocumentoAllegatoModel getDocumentoAllegato() {
		return mDocumentoAllegato;
	}

	// STUB 15/04/2005 Aggiunto Luogo Detenzione e Rifasiep.
	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mLuogoDetenzione;
	}

	public List getRifasiep() {
		return mRifasiep;
	}

	// STUB 23/10/2006 Fascicolo SIEPE Esteso.
	public FascicoloSiepeEstesoModel getFascicoloSiepeEsteso() {
		return mFasSiepeEst;
	}

	public AttivitaModel getAttivita() {
		return mAttivita;
	}

	public RichiestaModel getRichiesta() {
		return mRichiesta;
	}

	public RelazioneModel getRelazione() {
		return mRelazione;
	}

	public AssistenteSocialeModel getAssistenteSociale() {
		return mAssistenteSociale;
	}

	public FascicoloGPTPModel getFascicoloGPTPSius() {
		return mFascicoloGPTPSius;
	}

	// MEV 26 Cumulo Step2
	public DatiCumuloPerTrasferimentoModel getDatiCumuloPerTrasferimento() {
		return mDatiCumuloPerTrasferimento;
	}

	/**
	 * Metodo che itera sui nodi di PRIMO LIVELLO del TreeModel e li estrae valorizzando i campi recuperabili
	 * tramite i metodi Get.
	 * 
	 * @param aTreeModel
	 */
	private void iterateObjectModel(TreeModel aTreeModel) throws Exception {
		try {
			if (aTreeModel != null) {
				int nChildCount = aTreeModel.getChildCount();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Chiamato Parser Model Message. Child di primo livello = " + nChildCount);

				for (int i = 0; i < nChildCount; i++) {
					TreeModel lTreeChild = (TreeModel) aTreeModel.getChildAt(i);

					Object lObj = lTreeChild.getModel();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Tipo Nodo: " + lObj.getClass().getName());

					if (lObj instanceof SoggettoModel) {
						mSoggetto = (SoggettoModel) lObj;
						if (mSoggettoArray == null)
							mSoggettoArray = new ArrayList();

						mSoggettoArray.add(lObj);
					}
					if (lObj instanceof FascicoloSiepModel)
						mFascicolo = (FascicoloSiepModel) lObj;
					if (lObj instanceof EventoNotificaModel)
						mEvento = (EventoNotificaModel) lObj;
					if (lObj instanceof SentenzaModel)
						mSentenza = (SentenzaModel) lObj;
					if (lObj instanceof IstanzaModel)
						mIstanza = (IstanzaModel) lObj;
					if (lObj instanceof NuovaIstanzaModel)
						mNuovaIstanza = (NuovaIstanzaModel) lObj;
					if (lObj instanceof FascicoloGPModel)
						mFascicoloGPSius = (FascicoloGPModel) lObj;
					if (lObj instanceof TenoreModel) {
						if (mTenori == null)
							mTenori = new ArrayList();

						mTenori.add(lObj);
					}
					if (lObj instanceof PrescrizioneModel) {
						if (mPrescrizioni == null)
							mPrescrizioni = new ArrayList();

						mPrescrizioni.add(lObj);
					}
					if (lObj instanceof UdienzaModel)
						mUdienza = (UdienzaModel) lObj;
					if (lObj instanceof MisuraAlternativaModel)
						mMisuraAlternativa = (MisuraAlternativaModel) lObj;
					if (lObj instanceof MisuraSicurezzaModel)
						mMisuraSicurezza = (MisuraSicurezzaModel) lObj;
					if (lObj instanceof DepositoOrdinanzaPcModel)
						mDepositoOrdinanza = (DepositoOrdinanzaPcModel) lObj;
					if (lObj instanceof ImpugnazioneModel)
						mImpugnazione = (ImpugnazioneModel) lObj;
					if (lObj instanceof ResidenzaAssociataModel)
						mResidenzaAssociata = (ResidenzaAssociataModel) lObj;
					if (lObj instanceof DepositoDecretoModel)
						mDepositoDecreto = (DepositoDecretoModel) lObj;
					if (lObj instanceof MotivazioneDecretoModel) {
						if (mMotivazioniDecreto == null)
							mMotivazioniDecreto = new ArrayList();

						mMotivazioniDecreto.add(lObj);
					}
					// STUB 11/12/2003 Aggiunti per Trasmissione Ordinanza.
					if (lObj instanceof AssistenteGiudiziarioModel)
						mAssGiudiziario = (AssistenteGiudiziarioModel) lObj;
					if (lObj instanceof EspertoModel) {
						if (mEsperti == null)
							mEsperti = new ArrayList();

						mEsperti.add(lObj);
					}

					if (lObj instanceof MagistratoModel) {
						if (mMagistrati == null)
							mMagistrati = new ArrayList();

						mMagistrati.add(lObj);
					}

					if (lObj instanceof DettaglioFascicoloModel) {
						mDettaglioFascicoloSiep = (DettaglioFascicoloModel) lObj;
						
						// MEV-2026_1 - Aggiunte loggature (da eliminare)
						siesLogger.debug("MEV-2026_1: toString() = "+mDettaglioFascicoloSiep.toString());
						siesLogger.debug("MEV-2026_1: getAvvocati() = "+mDettaglioFascicoloSiep.getAvvocati());
						if (mDettaglioFascicoloSiep.getAvvocati()!=null)
						    siesLogger.debug("MEV-2026_1: getAvvocati().size() = "+mDettaglioFascicoloSiep.getAvvocati().size());
                        siesLogger.debug("MEV-2026_1: getStoricoCalcoliPenaDL92DB() = "+mDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB());
                        if (mDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB()!=null)
                            siesLogger.debug("MEV-2026_1: getStoricoCalcoliPenaDL92DB().size() = "+mDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB().size());
                        // MEV-2026_1 -FINE 
					}

					// STUB 21/03/2005 Aggiunta per Trasmissione Ordinanza.
					if (lObj instanceof LicenzaPeriodiLibAnticipataModel) {
						if (mLicenze == null)
							mLicenze = new ArrayList();

						mLicenze.add(lObj);
					}

					// STUB 07/04/2005 Aggiunta per Trasmissione Dati Sorveglianza.
					if (lObj instanceof AvvocatoSiusModel) {
						if (mAvvSius == null)
							mAvvSius = new ArrayList();

						mAvvSius.add(lObj);
					}

					// STUB 12/04/2005 Aggiunta per Trasmissione DocumentoAllegatoModel.
					if (lObj instanceof DocumentoAllegatoModel)
						mDocumentoAllegato = (DocumentoAllegatoModel) lObj;

					// STUB 15/04/2005 Aggiunta per Trasmissione LuogoDetenzione.
					if (lObj instanceof LuogoDetenzioneModel)
						mLuogoDetenzione = (LuogoDetenzioneModel) lObj;

					// STUB 15/04/2005 Aggiunta per Trasmissione RiferimentoFascicoloSiepModel.
					if (lObj instanceof RiferimentoFascicoloSiepModel) {
						if (mRifasiep == null)
							mRifasiep = new ArrayList();

						mRifasiep.add(lObj);
					}
					// STUB 23/10/2006 Aggiunta per SIEPE.
					if (lObj instanceof FascicoloSiepeEstesoModel)
						mFasSiepeEst = (FascicoloSiepeEstesoModel) lObj;
					if (lObj instanceof AttivitaModel)
						mAttivita = (AttivitaModel) lObj;
					if (lObj instanceof RichiestaModel)
						mRichiesta = (RichiestaModel) lObj;
					if (lObj instanceof RelazioneModel)
						mRelazione = (RelazioneModel) lObj;
					if (lObj instanceof AssistenteSocialeModel)
						mAssistenteSociale = (AssistenteSocialeModel) lObj;

					if (lObj instanceof FascicoloGPTPModel)
						mFascicoloGPTPSius = (FascicoloGPTPModel) lObj;

					// MEV 26 CUMULO Step2
					if (lObj instanceof DatiCumuloPerTrasferimentoModel) {
						mDatiCumuloPerTrasferimento = (DatiCumuloPerTrasferimentoModel) lObj;
					}
					// END MEV 26
				} // fine for sui figli
			}

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "");
			ex.printStackTrace();
			throw ex;
		}
	}

	
	/**
	 * Metodo di TEST per il traverse completo della struttura
	 * @param aTreeModel
	 * @param level
	 * @throws Exception
	 */
  public static void iterateFullObjectModel(TreeModel aTreeModel, int level) throws Exception {
      String lIdent = "  ";
      for (int i=0; i<level; i++) 
        lIdent+=lIdent;
      
      siesLogger.debug(lIdent+"level = " + level);
      try {
        if (aTreeModel != null) {
          int nChildCount = aTreeModel.getChildCount();

          for (int i = 0; i < nChildCount; i++) {
            TreeModel lTreeChild = (TreeModel) aTreeModel.getChildAt(i);

            Object lObj = lTreeChild.getModel();
            siesLogger.debug(lIdent+"Tipo Nodo: " + lObj.getClass().getName());

            if (lObj instanceof DettaglioFascicoloModel) {
                DettaglioFascicoloModel lDettaglioFascicoloSiep = (DettaglioFascicoloModel) lObj;

                siesLogger.debug("MEV-2026_1: toString() = "+lDettaglioFascicoloSiep.toString());
                siesLogger.debug("MEV-2026_1: getAvvocati() = "+lDettaglioFascicoloSiep.getAvvocati());
                
                if (lDettaglioFascicoloSiep.getAvvocati()!=null)
                    siesLogger.debug("MEV-2026_1: getAvvocati().size() = "+lDettaglioFascicoloSiep.getAvvocati().size());
                
                siesLogger.debug("MEV-2026_1: getStoricoCalcoliPenaDL92DB() = "+lDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB());
                if (lDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB()!=null)
                    siesLogger.debug("MEV-2026_1: getStoricoCalcoliPenaDL92DB().size() = "+lDettaglioFascicoloSiep.getStoricoCalcoliPenaDL92DB().size());
               
            }            
            
            ParserMessage.iterateFullObjectModel (lTreeChild,level+1);
          }
        }

      } catch (Exception ex) {
        siesLogger.error("Err Level "+level, ex);
        throw ex;
      }
    }
}