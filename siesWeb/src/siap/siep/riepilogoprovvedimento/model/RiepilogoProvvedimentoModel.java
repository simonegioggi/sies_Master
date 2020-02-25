package siap.siep.riepilogoprovvedimento.model;

/**
* <p>Title: RiepilogoProvvedimentoModel</p>
* <p>Description: Classe Model che rappresenta il RiepilogoProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RiepilogoProvvedimentoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1227356717519181700L;
	private BigDecimal mIdRiepilogoProvvedimento;
	private BigDecimal mNumRes;
	private BigDecimal mNumProgressivoRes;
	private BigDecimal mNumProtocolloRes;
	private String mFlagErgastolo;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private BigDecimal mNumAnniPresofferto;
	private BigDecimal mNumMesiPresofferto;
	private BigDecimal mNumGiorniPresofferto;
	private BigDecimal mNumAnniInterruzione;
	private BigDecimal mNumMesiInterruzione;
	private BigDecimal mNumGiorniInterruzione;
	private BigDecimal mNumGiorniLibAnticipata;
	private BigDecimal mNumAnniReclusioneBenefici;
	private BigDecimal mNumMesiReclusioneBenefici;
	private BigDecimal mNumGiorniReclusioneBenefici;
	private BigDecimal mImportoMultaBenefici;
	private BigDecimal mNumAnniArrestoBenefici;
	private BigDecimal mNumMesiArrestoBenefici;
	private BigDecimal mNumGiorniArrestoBenefici;
	private BigDecimal mImportoAmmendaBenefici;
	private BigDecimal mNumAnniAumentiPenaReclus;
	private BigDecimal mNumMesiAumentiPenaReclus;
	private BigDecimal mNumGiorniAumentiPenaReclus;
	private BigDecimal mImportoMultaAumentiPena;
	private BigDecimal mNumAnniAumentiPenaArres;
	private BigDecimal mNumMesiAumentiPenaArres;
	private BigDecimal mNumGiorniAumentiPenaArres;
	private BigDecimal mImportoAmmendaAumentiPena;
	private String mDiesAQuo;
	private Date mDataInizioPena;
	private Date mDataFinePena;
	private Date mDataFineReclusione;
	private Date mDataFinePrecedente;
	private Date mDataFineDetDomiciliare;
	private BigDecimal mNumAnniPenaResiduaReclus;
	private BigDecimal mNumMesiPenaResiduaReclus;
	private BigDecimal mNumGiorniPenaResiduaReclus;
	private BigDecimal mImportoMultaResidua;
	private BigDecimal mNumAnniPenaResiduaArres;
	private BigDecimal mNumMesiPenaResiduaArres;
	private BigDecimal mNumGiorniPenaResiduaArres;
	private BigDecimal mImportoAmmendaResidua;
	private BigDecimal mNumAnniFungibilita;
	private BigDecimal mNumMesiFungibilita;
	private BigDecimal mNumGiorniFungibilita;
	private BigDecimal mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public RiepilogoProvvedimentoModel() {
		this.mIdRiepilogoProvvedimento = null;
		this.mNumRes = null;
		this.mNumProgressivoRes = null;
		this.mNumProtocolloRes = null;
		this.mFlagErgastolo = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mNumAnniPresofferto = null;
		this.mNumMesiPresofferto = null;
		this.mNumGiorniPresofferto = null;
		this.mNumAnniInterruzione = null;
		this.mNumMesiInterruzione = null;
		this.mNumGiorniInterruzione = null;
		this.mNumGiorniLibAnticipata = null;
		this.mNumAnniReclusioneBenefici = null;
		this.mNumMesiReclusioneBenefici = null;
		this.mNumGiorniReclusioneBenefici = null;
		this.mImportoMultaBenefici = null;
		this.mNumAnniArrestoBenefici = null;
		this.mNumMesiArrestoBenefici = null;
		this.mNumGiorniArrestoBenefici = null;
		this.mImportoAmmendaBenefici = null;
		this.mNumAnniAumentiPenaReclus = null;
		this.mNumMesiAumentiPenaReclus = null;
		this.mNumGiorniAumentiPenaReclus = null;
		this.mImportoMultaAumentiPena = null;
		this.mNumAnniAumentiPenaArres = null;
		this.mNumMesiAumentiPenaArres = null;
		this.mNumGiorniAumentiPenaArres = null;
		this.mImportoAmmendaAumentiPena = null;
		this.mDiesAQuo = "";
		this.mDataInizioPena = null;
		this.mDataFinePena = null;
		this.mDataFineReclusione = null;
		this.mDataFinePrecedente = null;
		this.mDataFineDetDomiciliare = null;
		this.mNumAnniPenaResiduaReclus = null;
		this.mNumMesiPenaResiduaReclus = null;
		this.mNumGiorniPenaResiduaReclus = null;
		this.mImportoMultaResidua = null;
		this.mNumAnniPenaResiduaArres = null;
		this.mNumMesiPenaResiduaArres = null;
		this.mNumGiorniPenaResiduaArres = null;
		this.mImportoAmmendaResidua = null;
		this.mNumAnniFungibilita = null;
		this.mNumMesiFungibilita = null;
		this.mNumGiorniFungibilita = null;
		this.mCodTipoProvvedimento = null;
		this.mDescrTipoProvvedimento = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public RiepilogoProvvedimentoModel(RiepilogoProvvedimentoModel aModel) {
		this.mIdRiepilogoProvvedimento = aModel.mIdRiepilogoProvvedimento;
		this.mNumRes = aModel.mNumRes;
		this.mNumProgressivoRes = aModel.mNumProgressivoRes;
		this.mNumProtocolloRes = aModel.mNumProtocolloRes;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mNumAnniPresofferto = aModel.mNumAnniPresofferto;
		this.mNumMesiPresofferto = aModel.mNumMesiPresofferto;
		this.mNumGiorniPresofferto = aModel.mNumGiorniPresofferto;
		this.mNumAnniInterruzione = aModel.mNumAnniInterruzione;
		this.mNumMesiInterruzione = aModel.mNumMesiInterruzione;
		this.mNumGiorniInterruzione = aModel.mNumGiorniInterruzione;
		this.mNumGiorniLibAnticipata = aModel.mNumGiorniLibAnticipata;
		this.mNumAnniReclusioneBenefici = aModel.mNumAnniReclusioneBenefici;
		this.mNumMesiReclusioneBenefici = aModel.mNumMesiReclusioneBenefici;
		this.mNumGiorniReclusioneBenefici = aModel.mNumGiorniReclusioneBenefici;
		this.mImportoMultaBenefici = aModel.mImportoMultaBenefici;
		this.mNumAnniArrestoBenefici = aModel.mNumAnniArrestoBenefici;
		this.mNumMesiArrestoBenefici = aModel.mNumMesiArrestoBenefici;
		this.mNumGiorniArrestoBenefici = aModel.mNumGiorniArrestoBenefici;
		this.mImportoAmmendaBenefici = aModel.mImportoAmmendaBenefici;
		this.mNumAnniAumentiPenaReclus = aModel.mNumAnniAumentiPenaReclus;
		this.mNumMesiAumentiPenaReclus = aModel.mNumMesiAumentiPenaReclus;
		this.mNumGiorniAumentiPenaReclus = aModel.mNumGiorniAumentiPenaReclus;
		this.mImportoMultaAumentiPena = aModel.mImportoMultaAumentiPena;
		this.mNumAnniAumentiPenaArres = aModel.mNumAnniAumentiPenaArres;
		this.mNumMesiAumentiPenaArres = aModel.mNumMesiAumentiPenaArres;
		this.mNumGiorniAumentiPenaArres = aModel.mNumGiorniAumentiPenaArres;
		this.mImportoAmmendaAumentiPena = aModel.mImportoAmmendaAumentiPena;
		this.mDiesAQuo = aModel.mDiesAQuo;
		this.mDataInizioPena = aModel.mDataInizioPena;
		this.mDataFinePena = aModel.mDataFinePena;
		this.mDataFineReclusione = aModel.mDataFineReclusione;
		this.mDataFinePrecedente = aModel.mDataFinePrecedente;
		this.mDataFineDetDomiciliare = aModel.mDataFineDetDomiciliare;
		this.mNumAnniPenaResiduaReclus = aModel.mNumAnniPenaResiduaReclus;
		this.mNumMesiPenaResiduaReclus = aModel.mNumMesiPenaResiduaReclus;
		this.mNumGiorniPenaResiduaReclus = aModel.mNumGiorniPenaResiduaReclus;
		this.mImportoMultaResidua = aModel.mImportoMultaResidua;
		this.mNumAnniPenaResiduaArres = aModel.mNumAnniPenaResiduaArres;
		this.mNumMesiPenaResiduaArres = aModel.mNumMesiPenaResiduaArres;
		this.mNumGiorniPenaResiduaArres = aModel.mNumGiorniPenaResiduaArres;
		this.mImportoAmmendaResidua = aModel.mImportoAmmendaResidua;
		this.mNumAnniFungibilita = aModel.mNumAnniFungibilita;
		this.mNumMesiFungibilita = aModel.mNumMesiFungibilita;
		this.mNumGiorniFungibilita = aModel.mNumGiorniFungibilita;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public RiepilogoProvvedimentoModel(BigDecimal aIdRiepilogoProvvedimento, BigDecimal aNumRes,
			BigDecimal aNumProgressivoRes, BigDecimal aNumProtocolloRes, String aFlagErgastolo,
			BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione, BigDecimal aNumGiorniReclusione,
			BigDecimal aImportoMulta, BigDecimal aNumAnniArresto, BigDecimal aNumMesiArresto,
			BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda, BigDecimal aNumAnniPresofferto,
			BigDecimal aNumMesiPresofferto, BigDecimal aNumGiorniPresofferto, BigDecimal aNumAnniInterruzione,
			BigDecimal aNumMesiInterruzione, BigDecimal aNumGiorniInterruzione,
			BigDecimal aNumGiorniLibAnticipata, BigDecimal aNumAnniReclusioneBenefici,
			BigDecimal aNumMesiReclusioneBenefici, BigDecimal aNumGiorniReclusioneBenefici,
			BigDecimal aImportoMultaBenefici, BigDecimal aNumAnniArrestoBenefici,
			BigDecimal aNumMesiArrestoBenefici, BigDecimal aNumGiorniArrestoBenefici,
			BigDecimal aImportoAmmendaBenefici, BigDecimal aNumAnniAumentiPenaReclus,
			BigDecimal aNumMesiAumentiPenaReclus, BigDecimal aNumGiorniAumentiPenaReclus,
			BigDecimal aImportoMultaAumentiPena, BigDecimal aNumAnniAumentiPenaArres,
			BigDecimal aNumMesiAumentiPenaArres, BigDecimal aNumGiorniAumentiPenaArres,
			BigDecimal aImportoAmmendaAumentiPena, String aDiesAQuo, Date aDataInizioPena, Date aDataFinePena,
			Date aDataFineReclusione, Date aDataFinePrecedente, Date aDataFineDetDomiciliare,
			BigDecimal aNumAnniPenaResiduaReclus, BigDecimal aNumMesiPenaResiduaReclus,
			BigDecimal aNumGiorniPenaResiduaReclus, BigDecimal aImportoMultaResidua,
			BigDecimal aNumAnniPenaResiduaArres, BigDecimal aNumMesiPenaResiduaArres,
			BigDecimal aNumGiorniPenaResiduaArres, BigDecimal aImportoAmmendaResidua,
			BigDecimal aNumAnniFungibilita, BigDecimal aNumMesiFungibilita, BigDecimal aNumGiorniFungibilita,
			BigDecimal aCodTipoProvvedimento, String aDescrTipoProvvedimento, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			BigDecimal aEveIdEvento, BigDecimal aFasSieIdFascicoloSiep) {
		this.mIdRiepilogoProvvedimento = aIdRiepilogoProvvedimento;
		this.mNumRes = aNumRes;
		this.mNumProgressivoRes = aNumProgressivoRes;
		this.mNumProtocolloRes = aNumProtocolloRes;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mNumAnniPresofferto = aNumAnniPresofferto;
		this.mNumMesiPresofferto = aNumMesiPresofferto;
		this.mNumGiorniPresofferto = aNumGiorniPresofferto;
		this.mNumAnniInterruzione = aNumAnniInterruzione;
		this.mNumMesiInterruzione = aNumMesiInterruzione;
		this.mNumGiorniInterruzione = aNumGiorniInterruzione;
		this.mNumGiorniLibAnticipata = aNumGiorniLibAnticipata;
		this.mNumAnniReclusioneBenefici = aNumAnniReclusioneBenefici;
		this.mNumMesiReclusioneBenefici = aNumMesiReclusioneBenefici;
		this.mNumGiorniReclusioneBenefici = aNumGiorniReclusioneBenefici;
		this.mImportoMultaBenefici = aImportoMultaBenefici;
		this.mNumAnniArrestoBenefici = aNumAnniArrestoBenefici;
		this.mNumMesiArrestoBenefici = aNumMesiArrestoBenefici;
		this.mNumGiorniArrestoBenefici = aNumGiorniArrestoBenefici;
		this.mImportoAmmendaBenefici = aImportoAmmendaBenefici;
		this.mNumAnniAumentiPenaReclus = aNumAnniAumentiPenaReclus;
		this.mNumMesiAumentiPenaReclus = aNumMesiAumentiPenaReclus;
		this.mNumGiorniAumentiPenaReclus = aNumGiorniAumentiPenaReclus;
		this.mImportoMultaAumentiPena = aImportoMultaAumentiPena;
		this.mNumAnniAumentiPenaArres = aNumAnniAumentiPenaArres;
		this.mNumMesiAumentiPenaArres = aNumMesiAumentiPenaArres;
		this.mNumGiorniAumentiPenaArres = aNumGiorniAumentiPenaArres;
		this.mImportoAmmendaAumentiPena = aImportoAmmendaAumentiPena;
		this.mDiesAQuo = aDiesAQuo;
		this.mDataInizioPena = aDataInizioPena;
		this.mDataFinePena = aDataFinePena;
		this.mDataFineReclusione = aDataFineReclusione;
		this.mDataFinePrecedente = aDataFinePrecedente;
		this.mDataFineDetDomiciliare = aDataFineDetDomiciliare;
		this.mNumAnniPenaResiduaReclus = aNumAnniPenaResiduaReclus;
		this.mNumMesiPenaResiduaReclus = aNumMesiPenaResiduaReclus;
		this.mNumGiorniPenaResiduaReclus = aNumGiorniPenaResiduaReclus;
		this.mImportoMultaResidua = aImportoMultaResidua;
		this.mNumAnniPenaResiduaArres = aNumAnniPenaResiduaArres;
		this.mNumMesiPenaResiduaArres = aNumMesiPenaResiduaArres;
		this.mNumGiorniPenaResiduaArres = aNumGiorniPenaResiduaArres;
		this.mImportoAmmendaResidua = aImportoAmmendaResidua;
		this.mNumAnniFungibilita = aNumAnniFungibilita;
		this.mNumMesiFungibilita = aNumMesiFungibilita;
		this.mNumGiorniFungibilita = aNumGiorniFungibilita;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRiepilogoProvvedimento() {
		return mIdRiepilogoProvvedimento;
	}

	public BigDecimal getNumRes() {
		return mNumRes;
	}

	public BigDecimal getNumProgressivoRes() {
		return mNumProgressivoRes;
	}

	public BigDecimal getNumProtocolloRes() {
		return mNumProtocolloRes;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public BigDecimal getNumAnniPresofferto() {
		return mNumAnniPresofferto;
	}

	public BigDecimal getNumMesiPresofferto() {
		return mNumMesiPresofferto;
	}

	public BigDecimal getNumGiorniPresofferto() {
		return mNumGiorniPresofferto;
	}

	public BigDecimal getNumAnniInterruzione() {
		return mNumAnniInterruzione;
	}

	public BigDecimal getNumMesiInterruzione() {
		return mNumMesiInterruzione;
	}

	public BigDecimal getNumGiorniInterruzione() {
		return mNumGiorniInterruzione;
	}

	public BigDecimal getNumGiorniLibAnticipata() {
		return mNumGiorniLibAnticipata;
	}

	public BigDecimal getNumAnniReclusioneBenefici() {
		return mNumAnniReclusioneBenefici;
	}

	public BigDecimal getNumMesiReclusioneBenefici() {
		return mNumMesiReclusioneBenefici;
	}

	public BigDecimal getNumGiorniReclusioneBenefici() {
		return mNumGiorniReclusioneBenefici;
	}

	public BigDecimal getImportoMultaBenefici() {
		return mImportoMultaBenefici;
	}

	public BigDecimal getNumAnniArrestoBenefici() {
		return mNumAnniArrestoBenefici;
	}

	public BigDecimal getNumMesiArrestoBenefici() {
		return mNumMesiArrestoBenefici;
	}

	public BigDecimal getNumGiorniArrestoBenefici() {
		return mNumGiorniArrestoBenefici;
	}

	public BigDecimal getImportoAmmendaBenefici() {
		return mImportoAmmendaBenefici;
	}

	public BigDecimal getNumAnniAumentiPenaReclus() {
		return mNumAnniAumentiPenaReclus;
	}

	public BigDecimal getNumMesiAumentiPenaReclus() {
		return mNumMesiAumentiPenaReclus;
	}

	public BigDecimal getNumGiorniAumentiPenaReclus() {
		return mNumGiorniAumentiPenaReclus;
	}

	public BigDecimal getImportoMultaAumentiPena() {
		return mImportoMultaAumentiPena;
	}

	public BigDecimal getNumAnniAumentiPenaArres() {
		return mNumAnniAumentiPenaArres;
	}

	public BigDecimal getNumMesiAumentiPenaArres() {
		return mNumMesiAumentiPenaArres;
	}

	public BigDecimal getNumGiorniAumentiPenaArres() {
		return mNumGiorniAumentiPenaArres;
	}

	public BigDecimal getImportoAmmendaAumentiPena() {
		return mImportoAmmendaAumentiPena;
	}

	public String getDiesAQuo() {
		return mDiesAQuo;
	}

	public Date getDataInizioPena() {
		return mDataInizioPena;
	}

	public Date getDataFinePena() {
		return mDataFinePena;
	}

	public Date getDataFineReclusione() {
		return mDataFineReclusione;
	}

	public Date getDataFinePrecedente() {
		return mDataFinePrecedente;
	}

	public Date getDataFineDetDomiciliare() {
		return mDataFineDetDomiciliare;
	}

	public BigDecimal getNumAnniPenaResiduaReclus() {
		return mNumAnniPenaResiduaReclus;
	}

	public BigDecimal getNumMesiPenaResiduaReclus() {
		return mNumMesiPenaResiduaReclus;
	}

	public BigDecimal getNumGiorniPenaResiduaReclus() {
		return mNumGiorniPenaResiduaReclus;
	}

	public BigDecimal getImportoMultaResidua() {
		return mImportoMultaResidua;
	}

	public BigDecimal getNumAnniPenaResiduaArres() {
		return mNumAnniPenaResiduaArres;
	}

	public BigDecimal getNumMesiPenaResiduaArres() {
		return mNumMesiPenaResiduaArres;
	}

	public BigDecimal getNumGiorniPenaResiduaArres() {
		return mNumGiorniPenaResiduaArres;
	}

	public BigDecimal getImportoAmmendaResidua() {
		return mImportoAmmendaResidua;
	}

	public BigDecimal getNumAnniFungibilita() {
		return mNumAnniFungibilita;
	}

	public BigDecimal getNumMesiFungibilita() {
		return mNumMesiFungibilita;
	}

	public BigDecimal getNumGiorniFungibilita() {
		return mNumGiorniFungibilita;
	}

	public BigDecimal getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	//
	// METODI SET()
	//

	public void setIdRiepilogoProvvedimento(BigDecimal aValore) {
		mIdRiepilogoProvvedimento = aValore;
	}

	public void setNumRes(BigDecimal aValore) {
		mNumRes = aValore;
	}

	public void setNumProgressivoRes(BigDecimal aValore) {
		mNumProgressivoRes = aValore;
	}

	public void setNumProtocolloRes(BigDecimal aValore) {
		mNumProtocolloRes = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setNumAnniPresofferto(BigDecimal aValore) {
		mNumAnniPresofferto = aValore;
	}

	public void setNumMesiPresofferto(BigDecimal aValore) {
		mNumMesiPresofferto = aValore;
	}

	public void setNumGiorniPresofferto(BigDecimal aValore) {
		mNumGiorniPresofferto = aValore;
	}

	public void setNumAnniInterruzione(BigDecimal aValore) {
		mNumAnniInterruzione = aValore;
	}

	public void setNumMesiInterruzione(BigDecimal aValore) {
		mNumMesiInterruzione = aValore;
	}

	public void setNumGiorniInterruzione(BigDecimal aValore) {
		mNumGiorniInterruzione = aValore;
	}

	public void setNumGiorniLibAnticipata(BigDecimal aValore) {
		mNumGiorniLibAnticipata = aValore;
	}

	public void setNumAnniReclusioneBenefici(BigDecimal aValore) {
		mNumAnniReclusioneBenefici = aValore;
	}

	public void setNumMesiReclusioneBenefici(BigDecimal aValore) {
		mNumMesiReclusioneBenefici = aValore;
	}

	public void setNumGiorniReclusioneBenefici(BigDecimal aValore) {
		mNumGiorniReclusioneBenefici = aValore;
	}

	public void setImportoMultaBenefici(BigDecimal aValore) {
		mImportoMultaBenefici = aValore;
	}

	public void setNumAnniArrestoBenefici(BigDecimal aValore) {
		mNumAnniArrestoBenefici = aValore;
	}

	public void setNumMesiArrestoBenefici(BigDecimal aValore) {
		mNumMesiArrestoBenefici = aValore;
	}

	public void setNumGiorniArrestoBenefici(BigDecimal aValore) {
		mNumGiorniArrestoBenefici = aValore;
	}

	public void setImportoAmmendaBenefici(BigDecimal aValore) {
		mImportoAmmendaBenefici = aValore;
	}

	public void setNumAnniAumentiPenaReclus(BigDecimal aValore) {
		mNumAnniAumentiPenaReclus = aValore;
	}

	public void setNumMesiAumentiPenaReclus(BigDecimal aValore) {
		mNumMesiAumentiPenaReclus = aValore;
	}

	public void setNumGiorniAumentiPenaReclus(BigDecimal aValore) {
		mNumGiorniAumentiPenaReclus = aValore;
	}

	public void setImportoMultaAumentiPena(BigDecimal aValore) {
		mImportoMultaAumentiPena = aValore;
	}

	public void setNumAnniAumentiPenaArres(BigDecimal aValore) {
		mNumAnniAumentiPenaArres = aValore;
	}

	public void setNumMesiAumentiPenaArres(BigDecimal aValore) {
		mNumMesiAumentiPenaArres = aValore;
	}

	public void setNumGiorniAumentiPenaArres(BigDecimal aValore) {
		mNumGiorniAumentiPenaArres = aValore;
	}

	public void setImportoAmmendaAumentiPena(BigDecimal aValore) {
		mImportoAmmendaAumentiPena = aValore;
	}

	public void setDiesAQuo(String aValore) {
		mDiesAQuo = aValore;
	}

	public void setDataInizioPena(Date aValore) {
		mDataInizioPena = aValore;
	}

	public void setDataFinePena(Date aValore) {
		mDataFinePena = aValore;
	}

	public void setDataFineReclusione(Date aValore) {
		mDataFineReclusione = aValore;
	}

	public void setDataFinePrecedente(Date aValore) {
		mDataFinePrecedente = aValore;
	}

	public void setDataFineDetDomiciliare(Date aValore) {
		mDataFineDetDomiciliare = aValore;
	}

	public void setNumAnniPenaResiduaReclus(BigDecimal aValore) {
		mNumAnniPenaResiduaReclus = aValore;
	}

	public void setNumMesiPenaResiduaReclus(BigDecimal aValore) {
		mNumMesiPenaResiduaReclus = aValore;
	}

	public void setNumGiorniPenaResiduaReclus(BigDecimal aValore) {
		mNumGiorniPenaResiduaReclus = aValore;
	}

	public void setImportoMultaResidua(BigDecimal aValore) {
		mImportoMultaResidua = aValore;
	}

	public void setNumAnniPenaResiduaArres(BigDecimal aValore) {
		mNumAnniPenaResiduaArres = aValore;
	}

	public void setNumMesiPenaResiduaArres(BigDecimal aValore) {
		mNumMesiPenaResiduaArres = aValore;
	}

	public void setNumGiorniPenaResiduaArres(BigDecimal aValore) {
		mNumGiorniPenaResiduaArres = aValore;
	}

	public void setImportoAmmendaResidua(BigDecimal aValore) {
		mImportoAmmendaResidua = aValore;
	}

	public void setNumAnniFungibilita(BigDecimal aValore) {
		mNumAnniFungibilita = aValore;
	}

	public void setNumMesiFungibilita(BigDecimal aValore) {
		mNumMesiFungibilita = aValore;
	}

	public void setNumGiorniFungibilita(BigDecimal aValore) {
		mNumGiorniFungibilita = aValore;
	}

	public void setCodTipoProvvedimento(BigDecimal aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRiepilogoProvvedimento + " - " + mNumRes + " - " + mNumProgressivoRes + " - "
				+ mNumProtocolloRes + " - " + mFlagErgastolo + " - " + mNumAnniReclusione + " - "
				+ mNumMesiReclusione + " - " + mNumGiorniReclusione + " - " + mImportoMulta + " - "
				+ mNumAnniArresto + " - " + mNumMesiArresto + " - " + mNumGiorniArresto + " - "
				+ mImportoAmmenda + " - " + mNumAnniPresofferto + " - " + mNumMesiPresofferto + " - "
				+ mNumGiorniPresofferto + " - " + mNumAnniInterruzione + " - " + mNumMesiInterruzione + " - "
				+ mNumGiorniInterruzione + " - " + mNumGiorniLibAnticipata + " - "
				+ mNumAnniReclusioneBenefici + " - " + mNumMesiReclusioneBenefici + " - "
				+ mNumGiorniReclusioneBenefici + " - " + mImportoMultaBenefici + " - "
				+ mNumAnniArrestoBenefici + " - " + mNumMesiArrestoBenefici + " - "
				+ mNumGiorniArrestoBenefici + " - " + mImportoAmmendaBenefici + " - "
				+ mNumAnniAumentiPenaReclus + " - " + mNumMesiAumentiPenaReclus + " - "
				+ mNumGiorniAumentiPenaReclus + " - " + mImportoMultaAumentiPena + " - "
				+ mNumAnniAumentiPenaArres + " - " + mNumMesiAumentiPenaArres + " - "
				+ mNumGiorniAumentiPenaArres + " - " + mImportoAmmendaAumentiPena + " - " + mDiesAQuo + " - "
				+ mDataInizioPena + " - " + mDataFinePena + " - " + mDataFineReclusione + " - "
				+ mDataFinePrecedente + " - " + mDataFineDetDomiciliare + " - " + mNumAnniPenaResiduaReclus
				+ " - " + mNumMesiPenaResiduaReclus + " - " + mNumGiorniPenaResiduaReclus + " - "
				+ mImportoMultaResidua + " - " + mNumAnniPenaResiduaArres + " - " + mNumMesiPenaResiduaArres
				+ " - " + mNumGiorniPenaResiduaArres + " - " + mImportoAmmendaResidua + " - "
				+ mNumAnniFungibilita + " - " + mNumMesiFungibilita + " - " + mNumGiorniFungibilita + " - "
				+ mCodTipoProvvedimento + " - " + mDescrTipoProvvedimento + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mEveIdEvento + " - " + mFasSieIdFascicoloSiep;

		return lStr;
	}
}
