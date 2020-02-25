package siap.jms.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.model.UdienzaModel;
import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: ParserMessageRec
 * </p>
 * <p>
 * Description: Classe che realizza l'analisi del TreeModel e che ne ricava gli oggetti Model contenuti a piu'
 * livelli
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParserMessageRec extends ParserMessage {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	List mResidenza = null;
	LuogoDetenzioneModel mLuogoDetenzione;
	IstitutoDetenzioneModel mIstitutoDetenzione;
	List mAvvocato = null;
	List mAvvocatoSius = null;

	protected ParserMessageRec() {
	}

	public ParserMessageRec(TreeModel aTree) throws Exception {
		// Parser del TreeModel
		iterateObjectModel(aTree);
	}

	public List getResidenza() {
		return mResidenza;
	}

	public List getAvvocato() {
		return mAvvocato;
	}

	public List getAvvocatoSius() {
		return mAvvocatoSius;
	}

	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mLuogoDetenzione;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	/**
	 * Itera sul TreeModel e crea i Model struttura del Tree.
	 * 
	 * @param aTreeModel
	 *            TreeModel da convertire
	 * @return Element
	 */
	private void iterateObjectModel(TreeModel aTreeModel) throws Exception {
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Parser Model Rec: inizio");
			if (aTreeModel != null) {
				int nChildCount = aTreeModel.getChildCount();

				for (int i = 0; i < nChildCount; i++) {
					TreeModel lTreeChild = (TreeModel) aTreeModel.getChildAt(i);

					if (lTreeChild != null && lTreeChild.getChildCount() > 0)
						iterateObjectModel((TreeModel) lTreeChild);

					Object lObj = lTreeChild.getModel();

					if (lObj instanceof SoggettoModel)
						mSoggetto = (SoggettoModel) lObj;
					if (lObj instanceof FascicoloSiepModel)
						mFascicolo = (FascicoloSiepModel) lObj;
					if (lObj instanceof EventoNotificaModel)
						mEvento = (EventoNotificaModel) lObj;
					if (lObj instanceof SentenzaModel)
						mSentenza = (SentenzaModel) lObj;
					if (lObj instanceof IstanzaModel)
						mIstanza = (IstanzaModel) lObj;
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
					if (lObj instanceof DettaglioFascicoloModel)
						mDettaglioFascicoloSiep = (DettaglioFascicoloModel) lObj;
					if (lObj instanceof ResidenzaModel) {
						if (mResidenza == null)
							mResidenza = new ArrayList();

						mResidenza.add(lObj);
					}
					if (lObj instanceof AvvocatoSiusModel) {
						if (mAvvocatoSius == null)
							mAvvocatoSius = new ArrayList();

						mAvvocatoSius.add(lObj);
					}
					if (lObj instanceof AvvocatoModel) {
						if (mAvvocato == null)
							mAvvocato = new ArrayList();
						mAvvocato.add(lObj);
					}
					if (lObj instanceof LuogoDetenzioneModel)
						mLuogoDetenzione = (LuogoDetenzioneModel) lObj;
					if (lObj instanceof IstitutoDetenzioneModel)
						mIstitutoDetenzione = (IstitutoDetenzioneModel) lObj;
					if (lObj instanceof IstitutoDetenzioneModel)
						mIstitutoDetenzione = (IstitutoDetenzioneModel) lObj;

				} // fine for sui figli
			}
		} // endif aTreeModel
		catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(" Parser Rec. Exception ", ex);
			throw ex;
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Parser Model Rec: fine");
		}
	}

}