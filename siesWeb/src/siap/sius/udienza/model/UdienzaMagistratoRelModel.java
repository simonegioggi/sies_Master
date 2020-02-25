package siap.sius.udienza.model;

import java.util.ArrayList;
import java.util.Date;

import f3b.model.GenericModel;
/**
* <p>Title: UdienzaMagistratoRelModel</p>
* <p>Description: Classe Model che rappresenta l'aggregato
 * Procedimenti per Magistrato Relatore per Udienza.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaMagistratoRelModel extends UdienzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7014023966039484995L;

	private ArrayList mMagistrati = null;

	/**
	 *
	 * <p>
	 * Title: MagRelSintModel
	 * </p>
	 * <p>
	 * Description: classe rappresentante il Magistrato Relatore
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2004
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @author not attributable
	 * @version 1.0
	 */
	public class MagRelSintModel extends GenericModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8534097427282852185L;

		private String mCognome = "";
		private String mNome = "";
		private String mCodMagistrato = null;
		private BigDecimal mIdEsperto = null;
		private ArrayList mProcedimenti = null;

		public class ProcedimentoSintModel {
			private BigDecimal mIdFascicoloSius = null;
			private String mFlagRinvio = "";

			public BigDecimal getIdFascicolo() {
				return mIdFascicoloSius;
			}

			public void setIdFascicolo(BigDecimal aValue) {
				mIdFascicoloSius = aValue;
			}

			public String getFlagRinvio() {
				return mFlagRinvio;
			}

			public void setFlagRinvio(String aValue) {
				mFlagRinvio = aValue;
			}
		}

		// costruttore
		public MagRelSintModel() {
			mProcedimenti = new ArrayList();
			// mProcedimenti.add(new ProcedimentoSintModel());
		}

		//
		// METODI GET()
		//
		public BigDecimal getIdEsperto() {
			return mIdEsperto;
		}

		public String getCodMagistrato() {
			return mCodMagistrato;
		}

		/**
		 * Restituisce il tipo di Magistrato Relatore. Il Magistrato relatore può essere un Magistrato oppure
		 * un Esperto con funzione di Magiostrato Relatore.
		 * 
		 * @return
		 */
		public String getTipoMagistratoRelatore() {
			String lRet = "";
			if (mCodMagistrato != null)
				lRet = "Magistrato";
			else if (mIdEsperto != null)
				lRet = "Esperto";
			return lRet;
		}

		public String getCognome() {
			return mCognome;
		}

		public String getNome() {
			return mNome;
		}

		/**
		 * Ritorna il numero di Procedimenti assegnati al mMagistrato Relatore
		 * 
		 * @return int
		 */
		public int getNumProcedimenti() {
			int i = 0;
			if (mProcedimenti != null)
				i = mProcedimenti.size();
			return i;
		}

		/**
		 * Ritorna il numero di Procedimenti assegnati al mMagistrato Relatore con il flag_rinviato uguale al
		 * parametro aFlag.
		 * 
		 * @param String
		 *            aFlag
		 * @return int Numero Procedimenti
		 */
		public int getNumProcedimenti(String aFlag) {
			int i = 0;
			if (mProcedimenti != null)
				for (int j = 0; j < mProcedimenti.size(); j++)
					if (getProcedimento(i).getFlagRinvio().compareTo(aFlag) == 0)
						i++;
			return i;
		}

		/**
		 * Ritorna il Numero di Procedimenti per cui la Fissazione dell'Udienza proviene da un Rinvio.
		 * 
		 * @return
		 */
		public int getNumProcedimentiDaRinvio() {
			return getNumProcedimenti("S");
		}

		/**
		 * Ritorna il Numero di Procedimenti per cui la Fissazione dell'Udienza è di tipo "Prefissata".
		 * 
		 * @return
		 */
		public int getNumProcedimentiPrefissati() {
			return getNumProcedimenti("P");
		}

		/**
		 * Ritorna il Procedimento di indice i.
		 * 
		 * @param i
		 * @return ProcedimentoModel
		 */
		public ProcedimentoSintModel getProcedimento(int i) {
			ProcedimentoSintModel lRetModel = null;
			if (i < mProcedimenti.size()) {
				lRetModel = (ProcedimentoSintModel) (mProcedimenti.get(i));
			}
			return lRetModel;
		}

		public ProcedimentoSintModel getUltimoProcedimento() {
			return getProcedimento(mProcedimenti.size() - 1);
		}

		//
		// SET
		//
		public void setCognome(String aValue) {
			mCognome = aValue;
		}

		public void setNome(String aValue) {
			mNome = aValue;
		}

		public void setCodMagistrato(String aValue) {
			mCodMagistrato = aValue;
		}

		public void setIdEsperto(BigDecimal aValue) {
			mIdEsperto = aValue;
		}

		public void setProcedimenti(ArrayList aValore) {
			mProcedimenti = aValore;
		}

		public void addProcedimento(ProcedimentoSintModel aProcModel) {
			mProcedimenti.add(aProcModel);
		}

		public void addProcedimento() {
			mProcedimenti.add(new ProcedimentoSintModel());
		}

		public boolean equals(MagRelSintModel aMagModel) {
			boolean lRet = false;
			if (mCodMagistrato != null && aMagModel.getCodMagistrato() != null
					&& mCodMagistrato.equalsIgnoreCase(aMagModel.getCodMagistrato()))
				lRet = true;
			else if (mIdEsperto != null && aMagModel.getIdEsperto() != null
					&& mIdEsperto.equals(aMagModel.getIdEsperto()))
				lRet = true;
			return lRet;
		}
	}

	// COSTRUTTORE DI DEFAULT
	public UdienzaMagistratoRelModel() {
		super();
		mMagistrati = new ArrayList();
		// addMagistrato();
	}

	// ALTRI COSTRUTTORI
	public UdienzaMagistratoRelModel(UdienzaModel aModel) {
		super(aModel);
		mMagistrati = new ArrayList();

	}

	// Costruttore di copia
	public UdienzaMagistratoRelModel(UdienzaMagistratoRelModel aModel) {
		super((UdienzaModel) aModel);
		mMagistrati = aModel.mMagistrati;
	}

	//
	// METODI GET()
	//

	/**
	 * Ritorna il numero di Magistrati Relatori previsti per l'Udienza.
	 * 
	 * @return int
	 */
	public int getNumMagistrati() {
		int i = 0;
		if (mMagistrati != null)
			i = mMagistrati.size();
		return i;
	}

	/**
	 * Ritorna il MagistratoRelatore di indice i.
	 * 
	 * @param i
	 * @return MagRelModel
	 */
	public MagRelSintModel getMagistrato(int i) {
		MagRelSintModel lRetModel = null;
		if (i < mMagistrati.size()) {
			lRetModel = (MagRelSintModel) (mMagistrati.get(i));
		}

		return lRetModel;
	}

	/**
	 * Ritorna il numero di Procedimenti Fissati per l'Udienza
	 * 
	 * @return int
	 */
	public int getNumProcedimenti() {
		int j = 0;
		for (int i = 0; i < mMagistrati.size(); i++)
			j += getMagistrato(i).getNumProcedimenti();
		return j;
	}

	/**
	 * Ritorna il numero di Procedimenti Fissati per l'Udienza e provenienti da Rinvio.
	 * 
	 * @return int
	 */
	public int getNumProcedimentiDaRinvio() {
		int j = 0;
		for (int i = 0; i < mMagistrati.size(); i++)
			j += getMagistrato(i).getNumProcedimentiDaRinvio();
		return j;
	}

	/**
	 * Ritorna il numero di Procedimenti PreFissati all'Udienza.
	 * 
	 * @return int
	 */
	public int getNumProcedimentiPrefissati() {
		int j = 0;
		for (int i = 0; i < mMagistrati.size(); i++)
			j += getMagistrato(i).getNumProcedimentiPrefissati();
		return j;
	}

	// Ridefinite per renderle visibili nell'XML
	// generato attraverso la funzione ParserModelTree().
	public BigDecimal getIdUdienza() {
		return super.getIdUdienza();
	}

	public Date getDataUdienza() {
		return super.getDataUdienza();
	}

	public BigDecimal getNumCollegio() {
		return super.getNumCollegio();
	}
	//
	// METODI SET()
	//

	public void setMagistrati(ArrayList aValore) {
		mMagistrati = aValore;
	}

	public void addMagistrato() {
		mMagistrati.add(new MagRelSintModel());
	}

	public void addMagistrato(MagRelSintModel aMagModel) {
		mMagistrati.add(aMagModel);
	}

	public MagRelSintModel getUltimoMagistrato() {
		return getMagistrato(mMagistrati.size() - 1);
	}

	public String toString() {
		String lStr = new String();

		lStr = super.toString() + " - " + getNumMagistrati();
		return lStr;
	}

}