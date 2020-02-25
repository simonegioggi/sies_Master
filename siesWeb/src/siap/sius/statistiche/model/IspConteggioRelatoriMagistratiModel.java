package siap.sius.statistiche.model;

import f3b.util.F3BException;

/**
* <p>Title: IspConteggioRelatoriModel</p>
* <p>Description: Classe Model che rappresenta il IspConteggioRelatori</p>
* Il model rappresenta i conteggi statistici effettuati dalla funzione Statistica di "Estrazione Oggetti SIUS",
* e viene utilizzato per generare il report.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.magistrato.model.MagistratoModel;

/**
 * Classe rappresentativa dei conteggi statistici relativi a provvedimenti di uno specifico oggetto.
 * 
 * @author Lesposito
 *
 */
public class IspConteggioRelatoriMagistratiModel extends IspConteggioRelatoriModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 591600782862166258L;
	MagistratoModel mMagistrato = null;

	// COSTRUTTORE DI DEFAULT
	public IspConteggioRelatoriMagistratiModel() {
		super();
		mMagistrato = new MagistratoModel();
	}

	// COSTRUTTORE DI COPIA
	public IspConteggioRelatoriMagistratiModel(IspConteggioRelatoriMagistratiModel aModel) {
		super(aModel);
		mMagistrato = aModel.mMagistrato;
	}

	//
	// METODI GET()
	//
	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	//
	// METODI SET()
	//
	public void setMagistrato(MagistratoModel aModel) {
		mMagistrato = aModel;
	}

	/**
	 * Somma tra due model. E' possibile sommare solo due IspConteggioRelatoriModel con lo stesso contenuto.
	 * La funzione viene utilizzata per ottenere la Statistica Aggregata a partire da quella Dettagliata.
	 */
	public IspConteggioRelatoriMagistratiModel add(IspConteggioRelatoriMagistratiModel aModel)
			throws F3BException {
		// Model risultato della somma

		IspConteggioRelatoriMagistratiModel lAppoggioMod = new IspConteggioRelatoriMagistratiModel(this);
		/*
		 * if(aModel != null) { // Si sommano solo dati relativi allo stesso CONTENUTO if
		 * (aModel.getDescContenutoStatis().compareTo(mDescContenutoStatis) == 0) {
		 * lAppoggioMod.setNumPendentiInizio(mNumPendentiInizio.add(aModel.getNumPendentiInizio()));
		 * lAppoggioMod.setNumSopravvenuti(mNumSopravvenuti.add(aModel.getNumSopravvenuti()));
		 * lAppoggioMod.setNumDefEsito1(mNumDefEsito1.add(aModel.getNumDefEsito1()));
		 * lAppoggioMod.setNumDefEsito2(mNumDefEsito2.add(aModel.getNumDefEsito2()));
		 * lAppoggioMod.setNumDefEsito3(mNumDefEsito3.add(aModel.getNumDefEsito3()));
		 * lAppoggioMod.setNumDefEsito4(mNumDefEsito4.add(aModel.getNumDefEsito4()));
		 * lAppoggioMod.setNumDefEsito5(mNumDefEsito5.add(aModel.getNumDefEsito5()));
		 * lAppoggioMod.setNumDefEsito6(mNumDefEsito6.add(aModel.getNumDefEsito6()));
		 * lAppoggioMod.setNumDefIscErr(mNumDefIscErr.add(aModel.getNumDefIscErr()));
		 * lAppoggioMod.setNumPendentiFine(mNumPendentiFine.add(aModel.getNumPendentiFine()));
		 * lAppoggioMod.setNumCancellati(mNumCancellati.add(aModel.getNumCancellati()));
		 * lAppoggioMod.setNumUnificati(mNumUnificati.add(aModel.getNumUnificati())); } else throw new
		 * F3BException(F3BException.USER_MESSAGE,
		 * "Impossibile sommare 2 IspConteggioRelatoriModel con contenuto diverso !");
		 * 
		 * }
		 */
		return lAppoggioMod;
	}

}
