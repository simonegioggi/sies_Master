package siap.sige.udienzaparti.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import siap.sico.residenza.model.ResidenzaModel;
import f3b.model.GenericModel;

/**
* <p>Title: AnagraficaPartiUdienzaModel</p>
* <p>Description: Classe Model che rappresenta le parti (Offese, Civili) associate ad una Udienza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class AnagraficaPartiUdienzaModel extends GenericModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5638082986199979409L;
	private BigDecimal  mIdSoggetto;
	private String	  	mCodTipoPart;
	private String	  	mCodParte;
	private String	  	mCodFiscale;
	private String	  	mCognome;
	private String	  	mNome;
	private String	  	mDenominazione;
	private Date 		mDataNascita;
	private String	  	mCodComuneNascita;
	private String	  	mCodStatoNascita;
	private String	  	mDescComuneNascitaEstero;
	private String	  	mSesso;
	private String	  	mRagSociale;
	private String	  	mCodProvincia;
	private String	  	mIndSedeLegale;
	private String	  	mIndSedeOperativa;
	private String	  	mFlagConvUdienza;
	private String	  	mCodOperatoreInserimento;
	private Date 		mDataInserimento;
	private String	  	mCodUfficioInserimento;
	private String	  	mCodOperatoreAggiornamento;
	private Date 		mDataAggiornamento;
	private String	  	mCodUfficioAggiornamento;
	private String	  	mCodFiscaleRap;
	
	private String	  	mDescComuneNascita;
	private String      mDescrStatoNascita;
	private String      mCodProvinciaNascita;
	private String      mDescrProvinciaNascita;
	private String	  	mDescrProvincia;
	
	private ResidenzaModel mResidenza;
	private List <PartiUdienzaDifensoreModel> mDifensori;
	private String	  	mIndirizzoResidenza;
	
	//COSTRUTTORE DI DEFAULT
	public AnagraficaPartiUdienzaModel ()
	{

		this.mIdSoggetto = null;
		this.mCodTipoPart = "";
		this.mCodParte = "";
		this.mCodFiscale = "";
		this.mCognome = "";
		this.mNome = "";
		this.mDenominazione = "";
		this.mDataNascita = null;
		this.mCodComuneNascita = "";
		this.mCodStatoNascita = "";
		this.mDescComuneNascitaEstero = "";
		this.mSesso = "";
		this.mRagSociale = "";
		this.mCodProvincia = "";
		this.mIndSedeLegale = "";
		this.mIndSedeOperativa = "";
		this.mFlagConvUdienza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mCodFiscaleRap = "";
		
		this.mDescComuneNascita = "";
		this.mDescrStatoNascita = "";
	    this.mCodProvinciaNascita = "";
	    this.mDescrProvinciaNascita = "";
	    this.mDescrProvincia = "";

		this.mDifensori = null;
		this.mResidenza = null;
		this.mIndirizzoResidenza = "";
	}

	//COSTRUTTORE DI COPIA
	public AnagraficaPartiUdienzaModel ( AnagraficaPartiUdienzaModel aModel )
	{
		this.mIdSoggetto = aModel.mIdSoggetto;
		this.mCodTipoPart = aModel.mCodTipoPart;
		this.mCodParte = aModel.mCodParte;
		this.mCodFiscale = aModel.mCodFiscale;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mDenominazione = aModel.mDenominazione;
		this.mDataNascita = aModel.mDataNascita;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescComuneNascitaEstero = aModel.mDescComuneNascitaEstero;
		this.mSesso = aModel.mSesso;
		this.mRagSociale = aModel.mRagSociale;
		this.mCodProvincia = aModel.mCodProvincia;
		this.mIndSedeLegale = aModel.mIndSedeLegale;
		this.mIndSedeOperativa = aModel.mIndSedeOperativa;
		this.mFlagConvUdienza = aModel.mFlagConvUdienza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mCodFiscaleRap = aModel.mCodFiscaleRap;

	    this.mCodProvinciaNascita = aModel.mCodProvinciaNascita;
	    this.mDescrProvinciaNascita = aModel.mDescrProvinciaNascita;
		this.mDescComuneNascita = aModel.mDescComuneNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		this.mDifensori = aModel.mDifensori;
		this.mResidenza = aModel.mResidenza;
		this.mDescrProvincia = aModel.mDescrProvincia;
	}

	//COSTRUTTORE MODEL
  	public AnagraficaPartiUdienzaModel ( 
						  			BigDecimal  aIdSoggetto,
						  			String	  	aCodTipoPart,
						  			String	  	aCodParte,
						  			String	  	aCodFiscale,
						  			String	  	aCognome,
						  			String	  	aNome,
						  			String	  	aDenominazione,
						  			Date 		aDataNascita,
						  			String	  	aCodComuneNascita,
						  			String	  	aCodStatoNascita,
						  			String	    aDescrStatoNascita,
						  			String	  	aDescComuneNascitaEstero,
						  			String	    aCodProvinciaNascita,
			                        String	    aDescrProvinciaNascita,
						  			String	  	aSesso,
						  			String	  	aRagSociale,
						  			String	  	aCodProvincia,
						  			String	  	aIndSedeLegale,
						  			String	  	aIndSedeOperativa,
						  			String	  	aFlagConvUdienza,
						  			String	  	aCodOperatoreInserimento,
						  			Date 		aDataInserimento,
						  			String	  	aCodUfficioInserimento,
						  			String	  	aCodOperatoreAggiornamento,
						  			Date 		aDataAggiornamento,
						  			String	  	aCodUfficioAggiornamento,
						  			String	  	aCodFiscaleRap )
  	{
		this.mIdSoggetto = aIdSoggetto;
		this.mCodTipoPart = aCodTipoPart;
		this.mCodParte = aCodParte;
		this.mCodFiscale = aCodFiscale;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mDenominazione = aDenominazione;
		this.mDataNascita = aDataNascita;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescComuneNascitaEstero = aDescComuneNascitaEstero;
		this.mSesso = aSesso;
		this.mRagSociale = aRagSociale;
		this.mCodProvincia = aCodProvincia;
		this.mIndSedeLegale = aIndSedeLegale;
		this.mIndSedeOperativa = aIndSedeOperativa;
		this.mFlagConvUdienza = aFlagConvUdienza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mCodFiscaleRap = aCodFiscaleRap;
	    this.mCodProvinciaNascita = aCodProvinciaNascita;
	    this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		
	    if(aDescrStatoNascita!=null)
	    	this.mDescrStatoNascita = aDescrStatoNascita.toUpperCase();

  	}

  	//
  	// METODI GET()
  	//
  	public BigDecimal getIdSoggetto() 			      { return mIdSoggetto; }
  	public String 	  getCodTipoPart() 		          { return mCodTipoPart; }
  	public String 	  getCodParte()  			      { return mCodParte; }
  	public String 	  getCodFiscale() 		          { return mCodFiscale; }
  	public String 	  getCognome() 			          { return mCognome; }
  	public String 	  getNome() 			          {  return mNome; }
  	public String 	  getDenominazione()		      { return mDenominazione; }
  	public Date 	  getDataNascita() 			      { return mDataNascita; }
  	public String 	  getCodComuneNascita()		      { return mCodComuneNascita; }
  	public String 	  getCodStatoNascita() 		      { return mCodStatoNascita; }
  	public String 	  getDescComuneNascitaEstero()    { return mDescComuneNascitaEstero; }
  	public String 	  getSesso() 					  { return mSesso; }
  	public String 	  getRagSociale()				  { return mRagSociale; }
  	public String 	  getCodProvincia()			      { return mCodProvincia; }
  	public String 	  getIndSedeLegale() 			  { return mIndSedeLegale; }
  	public String 	  getIndSedeOperativa() 		  { return mIndSedeOperativa; }
  	public String 	  getFlagConvUdienza() 		      { return mFlagConvUdienza; }
	public String 	  getCodOperatoreInserimento()    { return mCodOperatoreInserimento; }
	public Date 	  getDataInserimento()		      { return mDataInserimento; }
	public String 	  getCodUfficioInserimento() 	  { return mCodUfficioInserimento; }
	public String 	  getCodOperatoreAggiornamento()  { return mCodOperatoreAggiornamento; }
	public Date 	  getDataAggiornamento()       	  { return mDataAggiornamento; }
	public String 	  getCodUfficioAggiornamento()    { return mCodUfficioAggiornamento; }
	public String 	  getCodFiscaleRap() 		      { return mCodFiscaleRap; }
	
	public String     getCodProvinciaNascita()        { return mCodProvinciaNascita; }
	public String     getDescrProvinciaNascita()      { return mDescrProvinciaNascita; }
	public String 	  getDescComuneNascita()          { return mDescComuneNascita; }
	public String     getDescrStatoNascita() 		  { return mDescrStatoNascita; }
	public String     getDescrProvincia() 		      { return mDescrProvincia; }

	public List<PartiUdienzaDifensoreModel>       getDifensori()                  { return mDifensori; }
	public ResidenzaModel getResidenza()              { return mResidenza; }
	public String     getIndirizzoResidenza() 		  { return mIndirizzoResidenza; }

	
	//
	// METODI SET()
	//
  	public void   setIdSoggetto(BigDecimal aValore) 			{ mIdSoggetto = aValore; }
  	public void   setCodTipoPart(String aValore) 		        { mCodTipoPart = aValore; }
  	public void   setCodParte(String aValore)  			        { mCodParte = aValore; }
  	public void   setCodFiscale(String aValore) 		        { mCodFiscale = aValore; }
  	public void   setCognome(String aValore) 			        { mCognome = aValore; }
  	public void   setNome(String aValore) 			            { mNome = aValore; }
  	public void   setDenominazione(String aValore)		        { mDenominazione = aValore; }
  	public void   setDataNascita(Date aValore) 			        { mDataNascita = aValore; }
  	public void   setCodComuneNascita(String aValore)		    { mCodComuneNascita = aValore; }
  	public void   setCodStatoNascita(String aValore) 		    { mCodStatoNascita = aValore; }
  	public void   setDescComuneNascitaEstero(String aValore)    { mDescComuneNascitaEstero = aValore; }
  	public void   setSesso(String aValore) 					    { mSesso = aValore; }
  	public void   setRagSociale(String aValore)				    { mRagSociale = aValore; }
  	public void   setCodProvincia(String aValore)			    { mCodProvincia = aValore; }
  	public void   setIndSedeLegale(String aValore) 			    { mIndSedeLegale = aValore; }
  	public void   setIndSedeOperativa(String aValore) 		    { mIndSedeOperativa = aValore; }
  	public void   setFlagConvUdienza(String aValore) 		    { mFlagConvUdienza = aValore; }
	public void   setCodOperatoreInserimento(String aValore)    { mCodOperatoreInserimento = aValore; }
	public void   setDataInserimento(Date aValore)		   	    { mDataInserimento = aValore; }
	public void   setCodUfficioInserimento(String aValore) 	    { mCodUfficioInserimento = aValore; }
	public void   setCodOperatoreAggiornamento(String aValore)  { mCodOperatoreAggiornamento = aValore; }
	public void   setDataAggiornamento(Date aValore)            { mDataAggiornamento = aValore; }
	public void   setCodUfficioAggiornamento(String aValore)    { mCodUfficioAggiornamento = aValore; }
	public void   setCodFiscaleRap(String aValore) 		        { mCodFiscaleRap = aValore; }

    public void   setCodProvinciaNascita(String aValore ) 	    { mCodProvinciaNascita = aValore; }
	public void   setDescrProvinciaNascita(String aValore )     { mDescrProvinciaNascita = aValore; }
	public void   setDescComuneNascita(String aValore)          { mDescComuneNascita = aValore; }
	public void   setDescrProvincia(String aValore)             { mDescrProvincia = aValore; }
	public void   setDifensori(List<PartiUdienzaDifensoreModel> aValore)                    { mDifensori = aValore; }
	public void   setResidenza(ResidenzaModel aValore)          { mResidenza = aValore; }
	
	public void   setDescrStatoNascita(String aValore ) 	    { if (aValore != null) mDescrStatoNascita = aValore.toUpperCase();  }
	
	public void   setIndirizzoResidenza(String aValore )        { mIndirizzoResidenza = aValore; }
	
	public String getDescrTipoPart() { 
		String descTipoPart = "";
		if(mCodTipoPart != null && mCodTipoPart.equals("O")){
			descTipoPart = "Offesa";
		} else {
			descTipoPart = "Civile";
		}
		
		return descTipoPart; 
	}

	public String getDescrParte() { 
		String descParte = "";
		if(mCodParte != null && mCodParte.equals("G")){
			descParte = "Giuridica";
		} else {
			descParte = "Fisica";
		}
		
		return descParte; 
	}

}