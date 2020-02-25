package siap.sige.udienza.model;

/**
* <p>Title: UdienzaSigeModel</p>
* <p>Description: Classe Model che rappresenta il UdienzaSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.sezione.model.SezioneModel;
import f3b.model.GenericModel;

public class UdienzaSigeModel extends GenericModel 
{
  
  private static final long serialVersionUID = 1132107506511930461L;
  private BigDecimal  mIdUdienzaSige;
  private Date        mDataUdienza;
  private String      mCodGiudice;
  private String      mDescrGiudice;
  private BigDecimal  mColIdCollegio;
  private String      mCodProcuratore;
  private String      mDescrProcuratore;
  private BigDecimal  mCodIdAssistente;
  private String      mDescrIdAssistente;
  private BigDecimal  mNumeroMaxFascicoli;
  private String      mCodOperatoreInserimento;
  private Date        mDataInserimento;
  private String      mCodUfficioInserimento;
  private String      mDescrUfficioInserimento;
  private String      mCodOperatoreAggiornamento;
  private Date        mDataAggiornamento;
  private String      mCodUfficioAggiornamento;
  private String      mDescrUfficioAggiornamento;
  private String      mLuogoUdienza;
  private String      mCodUfficioAppartenenza;
  private String      mDescrUfficioAppartenenza;
  private String      mOraInizio;
  private String      mMinInizio;
  private String      mOraFine;
  private String      mMinFine;
  private BigDecimal  mCodIdSezioneUdienza;
  private BigDecimal  mCodIdAulaUdienza;
  private AulaUdienzaModel aulaUdienzaModel;
  private SezioneModel sezioneModel;
  
  private CollegioModel mCollegio;
  
  // Membro di classe utilizzato per la ricerca range di date.
  private Date[]      mDateUdienze;

  // codice del Magistrato Assegnatario
  private String      mCodMagistratoAss;
  private String      mDescrMagistratoAss;
  
  // intervento per 11.2.1
  private BigDecimal  mNumeroUdienze;
  private String      mlistaIdUdienze;
  //
 
  /*****************************************************************************
   * Costruttore di default che inizializza i campi del model
   * I campi String vengono inizializzati a "", tutti gli altri campi a null
   ****************************************************************************/
  public UdienzaSigeModel ()  {
    this.mIdUdienzaSige              = null;
    this.mDataUdienza                = null;
    this.mCodGiudice                 = null;
    this.mDescrGiudice               = "";
    this.mColIdCollegio              = null;
    this.mCodProcuratore             = "";
    this.mDescrProcuratore           = "";
    this.mCodIdAssistente            = null;
    this.mDescrIdAssistente          = "";
    this.mNumeroMaxFascicoli         = null;
    this.mCodOperatoreInserimento    = "";
    this.mDataInserimento            = null;
    this.mCodUfficioInserimento      = "";
    this.mDescrUfficioInserimento    = "";
    this.mCodOperatoreAggiornamento  = "";
    this.mDataAggiornamento          = null;
    this.mCodUfficioAggiornamento    = "";
    this.mDescrUfficioAggiornamento  = "";
    this.mLuogoUdienza               = "";
    this.mCodUfficioAppartenenza     = "";
    this.mDescrUfficioAppartenenza   = "";
    this.mOraInizio                  = "";
    this.mMinInizio                  = "";
    this.mOraFine                    = "";
    this.mMinFine                    = "";

    this.mCodIdSezioneUdienza            = null;
    this.mCodIdAulaUdienza            = null;

    this.mCollegio                   = null;
    
    this.mDateUdienze                = null;
    this.mDescrMagistratoAss	     = "";
    
  }

  /****************************************************************************** 
   * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto 
   * del model passato in input.
   * <p> 
   * @param aModel 
   ******************************************************************************/ 
  public UdienzaSigeModel ( UdienzaSigeModel aModel )  { 
    this.mIdUdienzaSige              =  aModel.mIdUdienzaSige;
    this.mDataUdienza                =  aModel.mDataUdienza;
    this.mCodGiudice                 =  aModel.mCodGiudice;
    this.mDescrGiudice               =  aModel.mDescrGiudice;
    this.mColIdCollegio              =  aModel.mColIdCollegio;
    this.mCodProcuratore             =  aModel.mCodProcuratore;
    this.mDescrProcuratore           =  aModel.mDescrProcuratore;
    this.mCodIdAssistente            =  aModel.mCodIdAssistente;
    this.mDescrIdAssistente          =  aModel.mDescrIdAssistente;
    this.mNumeroMaxFascicoli         =  aModel.mNumeroMaxFascicoli;
    this.mCodOperatoreInserimento    =  aModel.mCodOperatoreInserimento;
    this.mDataInserimento            =  aModel.mDataInserimento;
    this.mCodUfficioInserimento      =  aModel.mCodUfficioInserimento;
    this.mDescrUfficioInserimento    =  aModel.mDescrUfficioInserimento;
    this.mCodOperatoreAggiornamento  =  aModel.mCodOperatoreAggiornamento;
    this.mDataAggiornamento          =  aModel.mDataAggiornamento;
    this.mCodUfficioAggiornamento    =  aModel.mCodUfficioAggiornamento;
    this.mDescrUfficioAggiornamento  =  aModel.mDescrUfficioAggiornamento;
    this.mLuogoUdienza               =  aModel.mLuogoUdienza;
    this.mCodUfficioAppartenenza     =  aModel.mCodUfficioAppartenenza;
    this.mDescrUfficioAppartenenza   =  aModel.mDescrUfficioAppartenenza;
    this.mOraInizio                  =  aModel.mOraInizio;
    this.mMinInizio                  =  aModel.mMinInizio;
    this.mOraFine                    =  aModel.mOraFine;
    this.mMinFine                    =  aModel.mMinFine;

    this.mCodIdSezioneUdienza                    =  aModel.mCodIdSezioneUdienza;
    this.mCodIdAulaUdienza                    =  aModel.mCodIdAulaUdienza;

    this.mCollegio                   =  aModel.mCollegio;
    this.mlistaIdUdienze			 = aModel.mlistaIdUdienze;
    this.mNumeroUdienze				 = aModel.mNumeroUdienze;
  }

  /***************************************************************************** 
   * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in 
   * input. Utilizzato dai DAO. 
   ****************************************************************************/ 
  public UdienzaSigeModel (
    BigDecimal aIdUdienzaSige,
    Date       aDataUdienza,
    String     aCodGiudice,
    String     aDescrGiudice, 
    BigDecimal aColIdCollegio,
    String     aCodProcuratore,
    String     aDescrProcuratore,
    BigDecimal aCodIdAssistente,
    String     aDescrIdAssistente,
    BigDecimal aNumeroMaxFascicoli,
    String     aCodOperatoreInserimento,
    Date       aDataInserimento,
    String     aCodUfficioInserimento,
    String     aDescrUfficioInserimento,
    String     aCodOperatoreAggiornamento,
    Date       aDataAggiornamento,
    String     aCodUfficioAggiornamento,
    String     aDescrUfficioAggiornamento,
    String     aLuogoUdienza,
    String     aCodUfficioAppartenenza,
    String     aDescrUfficioAppartenenza,
    String     aOraInizio,
    String     aMinInizio,
    String     aOraFine,
    String     aMinFine,
    BigDecimal aCodIdSezioneUdienza,
    BigDecimal aCodIdAulaUdienza)
  {
    this.mIdUdienzaSige               =  aIdUdienzaSige;
    this.mDataUdienza                 =  aDataUdienza;
    this.mCodGiudice                  =  aCodGiudice;
    this.mDescrGiudice                =  aDescrGiudice;
    this.mColIdCollegio               =  aColIdCollegio;
    this.mCodProcuratore              =  aCodProcuratore;
    this.mDescrProcuratore            =  aDescrProcuratore;
    this.mCodIdAssistente             =  aCodIdAssistente;
    this.mDescrIdAssistente           =  aDescrIdAssistente;
    this.mNumeroMaxFascicoli          =  aNumeroMaxFascicoli;
    this.mCodOperatoreInserimento     =  aCodOperatoreInserimento;
    this.mDataInserimento             =  aDataInserimento;
    this.mCodUfficioInserimento       =  aCodUfficioInserimento;
    this.mDescrUfficioInserimento     =  aDescrUfficioInserimento;
    this.mCodOperatoreAggiornamento   =  aCodOperatoreAggiornamento;
    this.mDataAggiornamento           =  aDataAggiornamento;
    this.mCodUfficioAggiornamento     =  aCodUfficioAggiornamento;
    this.mDescrUfficioAggiornamento   =  aDescrUfficioAggiornamento;
    this.mLuogoUdienza                =  aLuogoUdienza;
    this.mCodUfficioAppartenenza      =  aCodUfficioAppartenenza;
    this.mDescrUfficioAppartenenza    =  aDescrUfficioAppartenenza;
    this.mOraInizio                   =  aOraInizio;
    this.mMinInizio                   =  aMinInizio;
    this.mOraFine                     =  aOraFine;
    this.mMinFine                     =  aMinFine;

    this.mCodIdSezioneUdienza             =  aCodIdSezioneUdienza;
    this.mCodIdAulaUdienza             =  aCodIdAulaUdienza;

  }

  //============================================================================ 
  // METODI GET() 
  //============================================================================ 
  public BigDecimal  getIdUdienzaSige()              { return mIdUdienzaSige; } 
  public Date        getDataUdienza()                { return mDataUdienza; } 
  public String      getCodGiudice()                 { return mCodGiudice; }
  public String      getDescrGiudice()               { return mDescrGiudice; }
  public BigDecimal  getColIdCollegio()              { return mColIdCollegio; }
  public String      getCodProcuratore()             { return mCodProcuratore; }
  public String      getDescrProcuratore()           { return mDescrProcuratore; }
  public BigDecimal  getCodIdAssistente()            { return mCodIdAssistente; } 
  public String      getDescrIdAssistente()          { return mDescrIdAssistente; } 
  public BigDecimal  getNumeroMaxFascicoli()         { return mNumeroMaxFascicoli; } 
  public String      getCodOperatoreInserimento()    { return mCodOperatoreInserimento; } 
  public Date        getDataInserimento()            { return mDataInserimento; } 
  public String      getCodUfficioInserimento()      { return mCodUfficioInserimento; } 
  public String      getDescrUfficioInserimento()    { return mDescrUfficioInserimento; } 
  public String      getCodOperatoreAggiornamento()  { return mCodOperatoreAggiornamento; } 
  public Date        getDataAggiornamento()          { return mDataAggiornamento; } 
  public String      getCodUfficioAggiornamento()    { return mCodUfficioAggiornamento; } 
  public String      getDescrUfficioAggiornamento()  { return mDescrUfficioAggiornamento; } 
  public String      getLuogoUdienza()               { return mLuogoUdienza; } 
  public String      getCodUfficioAppartenenza()     { return mCodUfficioAppartenenza; } 
  public String      getDescrUfficioAppartenenza()   { return mDescrUfficioAppartenenza; } 
  public String      getOraInizio()                  { return mOraInizio; } 
  public String      getMinInizio()                  { return mMinInizio; } 
  public String      getOraFine()                    { return mOraFine; } 
  public String      getMinFine()                    { return mMinFine; } 
  
  public CollegioModel  getCollegio()                { return mCollegio; }
  
  public Date[]      getDateUdienze()                { return mDateUdienze; }                   

  public String      getCodMagistratoAss()           { return mCodMagistratoAss; }
  public String      getDescrMagistratoAss()           { return mDescrMagistratoAss; }
  
  // intervento per 11.2.1
  public String      getListaIdUdienze()          { return mlistaIdUdienze; }
  public BigDecimal  getNumeroUdienze()           { return mNumeroUdienze; } 
  //
  
  //============================================================================
  // METODI SET() 
  //============================================================================
  public void  setIdUdienzaSige             (BigDecimal aValore ) { mIdUdienzaSige             = aValore; } 
  public void  setDataUdienza               (Date       aValore ) { mDataUdienza               = aValore; } 
  public void  setCodGiudice                (String     aValore ) { mCodGiudice                = aValore; }
  public void  setDescrGiudice              (String     aValore ) { mDescrGiudice              = aValore; }
  public void  setColIdCollegio             (BigDecimal aValore ) { mColIdCollegio             = aValore; }  
  public void  setCodProcuratore            (String     aValore ) { mCodProcuratore            = aValore; } 
  public void  setDescrProcuratore          (String     aValore ) { mDescrProcuratore          = aValore; } 
  public void  setCodIdAssistente           (BigDecimal aValore ) { mCodIdAssistente           = aValore; } 
  public void  setDescrIdAssistente         (String     aValore ) { mDescrIdAssistente         = aValore; } 
  public void  setNumeroMaxFascicoli        (BigDecimal aValore ) { mNumeroMaxFascicoli        = aValore; } 
  public void  setCodOperatoreInserimento   (String     aValore ) { mCodOperatoreInserimento   = aValore; } 
  public void  setDataInserimento           (Date       aValore ) { mDataInserimento           = aValore; } 
  public void  setCodUfficioInserimento     (String     aValore ) { mCodUfficioInserimento     = aValore; } 
  public void  setDescrUfficioInserimento   (String     aValore ) { mDescrUfficioInserimento   = aValore; } 
  public void  setCodOperatoreAggiornamento (String     aValore ) { mCodOperatoreAggiornamento = aValore; } 
  public void  setDataAggiornamento         (Date       aValore ) { mDataAggiornamento         = aValore; } 
  public void  setCodUfficioAggiornamento   (String     aValore ) { mCodUfficioAggiornamento   = aValore; } 
  public void  setDescrUfficioAggiornamento (String     aValore ) { mDescrUfficioAggiornamento = aValore; } 
  public void  setLuogoUdienza              (String     aValore ) { mLuogoUdienza              = aValore; } 
  public void  setCodUfficioAppartenenza    (String     aValore ) { mCodUfficioAppartenenza    = aValore; } 
  public void  setDescrUfficioAppartenenza  (String     aValore ) { mDescrUfficioAppartenenza  = aValore; } 
  public void  setOraInizio                 (String     aValore ) { mOraInizio                 = aValore; } 
  public void  setMinInizio                 (String     aValore ) { mMinInizio                 = aValore; } 
  public void  setOraFine                   (String     aValore ) { mOraFine                   = aValore; } 
  public void  setMinFine                   (String     aValore ) { mMinFine                   = aValore; }
  
  public void  setCollegio                  (CollegioModel aValore) {mCollegio = aValore; }  
    
  public void  setDateUdienze               (Date[]     aValori) {mDateUdienze               = aValori;}

  public BigDecimal getCodIdSezioneUdienza() {	return mCodIdSezioneUdienza;}
  public void setCodIdSezioneUdienza(BigDecimal codIdSezioneUdienza) {	mCodIdSezioneUdienza = codIdSezioneUdienza;}
  public BigDecimal getCodIdAulaUdienza() {	return mCodIdAulaUdienza;}
  public void setCodIdAulaUdienza(BigDecimal codIdAulaUdienza) {	mCodIdAulaUdienza = codIdAulaUdienza;}

  public void  setCodMagistratoAss          (String     aValore ) { mCodMagistratoAss          = aValore; }
  public void  setDescrMagistratoAss          (String     aValore ) { mDescrMagistratoAss          = aValore; }
  
  
  // intervento per 11.2.1
  public void  setListaIdUdienze  (String  aValore ) { mlistaIdUdienze          = aValore; }
  public void  setNumeroUdienze(BigDecimal numUdi) {mNumeroUdienze = numUdi;}

  //
  

public AulaUdienzaModel getAulaUdienzaModel() {
	return aulaUdienzaModel;
}

public void setAulaUdienzaModel(AulaUdienzaModel aulaModel) {
	this.aulaUdienzaModel = aulaModel;
}

public SezioneModel getSezioneModel() {
	return sezioneModel;
}

public void setSezioneModel(SezioneModel sezioneModel) {
	this.sezioneModel = sezioneModel;
}

public String getDescrizioneAula () {
	String descrizioneAula="";
	if (this.aulaUdienzaModel != null)
		descrizioneAula=aulaUdienzaModel.getDescrizioneAula();
	
	return descrizioneAula;
}

public String getNumeroPiano () {
	String numeroPiano="";
	if (this.aulaUdienzaModel != null) {
		if (aulaUdienzaModel.getNumeroPiano() != null) {
		    numeroPiano=aulaUdienzaModel.getNumeroPiano().toString();
		}
	}
	return numeroPiano;
}

public String getDescrizioneIngresso() {
	String descrizioneIngresso="";
	if (this.aulaUdienzaModel != null)
		descrizioneIngresso=aulaUdienzaModel.getDescrizioneIngresso();

	return descrizioneIngresso;
}

public String getDescrizioneStanza() {
	String descrizioneStanza="";
	if (this.aulaUdienzaModel != null)
		descrizioneStanza=aulaUdienzaModel.getDescrizioneStanza();

	return descrizioneStanza;
}

public String getOrarioInizio () {
	String orario="";
	String oraInizio = (getOraInizio()== null || getOraInizio().equalsIgnoreCase("NULL")?"":getOraInizio());
	String minInizio = (getMinInizio()== null || getMinInizio().equalsIgnoreCase("NULL")?"":getMinInizio());
	if(oraInizio!="" && minInizio!="")
		orario=oraInizio +":"+minInizio;
	return orario;
}

public String getDescrLuogoUdienza () {
	String descr="";
	if (this.mLuogoUdienza != null)
		descr=this.mLuogoUdienza;
	
	return descr;
}




  
  /***************************************************************************** 
   * Metodo toString() che restituisce il contenuto del Model opportunamente 
   * formattato. Utile per il debug. 
   ****************************************************************************/ 
 /*
  public String toString()  { 
    String lStr = new String(); 

    lStr = "UdienzaSigeModel:\n" +
           "[ mIdUdienzaSige             = " + mIdUdienzaSige+" ]\n"+
           "[ mDataUdienza               = " + mDataUdienza+" ]\n"+
           "[ mCodProcuratore            = " + mCodProcuratore+" ]\n"+
           "[ mCodIdAssistente           = " + mCodIdAssistente+" ]\n"+
           "[ mNumeroMaxFascicoli        = " + mNumeroMaxFascicoli+" ]\n"+
           "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento+" ]\n"+
           "[ mDataInserimento           = " + mDataInserimento+" ]\n"+
           "[ mCodUfficioInserimento     = " + mCodUfficioInserimento+" ]\n"+
           "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento+" ]\n"+
           "[ mDataAggiornamento         = " + mDataAggiornamento+" ]\n"+
           "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento+" ]\n"+
           "[ mLuogoUdienza              = " + mLuogoUdienza+" ]\n"+
           "[ mCodUfficioAppartenenza    = " + mCodUfficioAppartenenza+" ]\n"+
           "[ mOraInizio                 = " + mOraInizio+" ]\n"+
           "[ mMinInizio                 = " + mMinInizio+" ]\n"+
           "[ mOraFine                   = " + mOraFine+" ]\n"+
           "[ mMinFine                   = " + mMinFine+" ]";
    return lStr;
  }
  */ 
}