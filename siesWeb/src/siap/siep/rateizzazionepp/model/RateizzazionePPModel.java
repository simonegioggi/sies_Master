package siap.siep.rateizzazionepp.model;

/**
* <p>Title: RateizzazionePPModel</p>
* <p>Description: Classe Model che rappresenta la tabella RETEIZZAZIONE_PP</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RateizzazionePPModel extends GenericModel {
  private static final long serialVersionUID = 7652960898545828432L;
  
  private BigDecimal mIdRateizzazionePP;
  private BigDecimal mImportoDaPagare;
  private BigDecimal mImportoRata;
  private BigDecimal mNumeroRate;
  private String mTipoRateizzazione;
  private BigDecimal mScadenzaGiorni;
  
  private BigDecimal mFasSieIdFascicoloSiep;
  //private BigDecimal mEveIdEvento; 
  
  private String mCodOperatoreInserimento;
  private Date   mDataInserimento;
  private String mCodUfficioInserimento;
  private String mDescrUfficioInserimento;
  
  private String mCodOperatoreAggiornamento;
  private Date   mDataAggiornamento;
  private String mCodUfficioAggiornamento;
  private String mDescrUfficioAggiornamento;
  
  private String mDescrTipoRateizzazione;


  /*****************************************************************************
   * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
   * tutti gli altri campi a null
   ****************************************************************************/
  public RateizzazionePPModel() {
    this.mIdRateizzazionePP = null;
    this.mImportoDaPagare = null;
    this.mImportoRata = null;
    this.mNumeroRate = null;
    this.mTipoRateizzazione = "";
    this.mScadenzaGiorni = null;
    
    this.mFasSieIdFascicoloSiep = null;
    //this.mEveIdEvento = null;
    
    this.mCodOperatoreInserimento = "";
    this.mDataInserimento = null;
    this.mCodUfficioInserimento = "";
    this.mDescrUfficioInserimento = "";
    this.mCodOperatoreAggiornamento = "";
    this.mDataAggiornamento = null;
    this.mCodUfficioAggiornamento = "";
    this.mDescrUfficioAggiornamento = "";
    
    this.mDescrTipoRateizzazione = "";
    
  }

  /*****************************************************************************
   * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
   * input
   * 
   * @param aModel
   ****************************************************************************/
  public RateizzazionePPModel(RateizzazionePPModel aModel) {
    this.mIdRateizzazionePP = aModel.mIdRateizzazionePP;    
    this.mImportoDaPagare = aModel.mImportoDaPagare;
    this.mImportoRata = aModel.mImportoRata;
    this.mNumeroRate = aModel.mNumeroRate;
    this.mTipoRateizzazione = aModel.mTipoRateizzazione;
    this.mScadenzaGiorni = aModel.mScadenzaGiorni;
    
    this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
    //this.mEveIdEvento = aModel.mEveIdEvento;
    
    this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
    this.mDataInserimento = aModel.mDataInserimento;
    this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
    this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
    this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
    this.mDataAggiornamento = aModel.mDataAggiornamento;
    this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
    this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
    
    this.mDescrTipoRateizzazione = aModel.mDescrTipoRateizzazione;
  }

  /*****************************************************************************
   * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
   ****************************************************************************/

  // ============================================================================
  // METODI GET()
  // ============================================================================
  public BigDecimal getIdRateizzazionePP()         { return mIdRateizzazionePP;}
  public BigDecimal getImportoDaPagare()           { return mImportoDaPagare;}
  public BigDecimal getImportoRata()               { return mImportoRata;}
  public BigDecimal getNumeroRate()                { return mNumeroRate;}
  public String     getTipoRateizzazione()         { return mTipoRateizzazione;}
  public BigDecimal getScadenzaGiorni()            { return mScadenzaGiorni;}
  
  public BigDecimal getFasSieIdFascicoloSiep()     { return mFasSieIdFascicoloSiep;}
  //public BigDecimal getEveIdEvento()               { return mEveIdEvento;}
  
  public String     getCodOperatoreInserimento()   { return mCodOperatoreInserimento;}
  public Date       getDataInserimento()           { return mDataInserimento;}
  public String     getCodUfficioInserimento()     { return mCodUfficioInserimento;}
  public String     getDescrUfficioInserimento()   { return mDescrUfficioInserimento;}
  public String     getCodOperatoreAggiornamento() { return mCodOperatoreAggiornamento;}
  public Date       getDataAggiornamento()         { return mDataAggiornamento;}
  public String     getCodUfficioAggiornamento()   { return mCodUfficioAggiornamento;}
  public String     getDescrUfficioAggiornamento() { return mDescrUfficioAggiornamento;}
  public String     getDescrTipoRateizzazione()    { return mDescrTipoRateizzazione;}
  
  // ============================================================================
  // METODI SET()
  // ============================================================================
  public void setIdRateizzazionePP (BigDecimal mIdRateizzazionePP) {  this.mIdRateizzazionePP = mIdRateizzazionePP;}
  public void setImportoDaPagare (BigDecimal mImportoDaPagare) {  this.mImportoDaPagare = mImportoDaPagare;}
  public void setImportoRata (BigDecimal mImportoRata) {  this.mImportoRata = mImportoRata;}
  public void setNumeroRate (BigDecimal mNumeroRate) {  this.mNumeroRate = mNumeroRate;}
  public void setTipoRateizzazione (String mTipoRateizzazione) {  this.mTipoRateizzazione = mTipoRateizzazione;}
  public void setScadenzaGiorni (BigDecimal mScadenzaGiorni) {  this.mScadenzaGiorni = mScadenzaGiorni;}
  
  public void setFasSieIdFascicoloSiep (BigDecimal mFasSieIdFascicoloSiep) {  this.mFasSieIdFascicoloSiep = mFasSieIdFascicoloSiep;}
  //public void setEveIdEvento (BigDecimal mEveIdEvento) {  this.mEveIdEvento = mEveIdEvento;}
  
  public void setCodOperatoreInserimento (String mCodOperatoreInserimento) {  this.mCodOperatoreInserimento = mCodOperatoreInserimento;}
  public void setDataInserimento (Date mDataInserimento) {  this.mDataInserimento = mDataInserimento; }
  public void setCodUfficioInserimento (String mCodUfficioInserimento) {  this.mCodUfficioInserimento = mCodUfficioInserimento;}
  public void setDescrUfficioInserimento (String mDescrUfficioInserimento) {  this.mDescrUfficioInserimento = mDescrUfficioInserimento;}
  public void setCodOperatoreAggiornamento (String mCodOperatoreAggiornamento) {  this.mCodOperatoreAggiornamento = mCodOperatoreAggiornamento;}
  public void setDataAggiornamento (Date mDataAggiornamento) {  this.mDataAggiornamento = mDataAggiornamento;}
  public void setCodUfficioAggiornamento (String mCodUfficioAggiornamento) {  this.mCodUfficioAggiornamento = mCodUfficioAggiornamento;}
  public void setDescrUfficioAggiornamento( String mDescrUfficioAggiornamento) {  this.mDescrUfficioAggiornamento = mDescrUfficioAggiornamento;}
  public void setDescrTipoRateizzazione (String mDescrTipoRateizzazione) {  this.mDescrTipoRateizzazione = mDescrTipoRateizzazione;}
  
  /*****************************************************************************
   * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
   ****************************************************************************/
  @Override
  public String toString() {
    String lStr = new String();

    lStr = "RateizzazionePPModel:\n" 
        + "[ mIdRateizzazionePP         = " + mIdRateizzazionePP+ " ]\n"        
        + "[ mImportoDaPagare           = " + mImportoDaPagare + " ]\n"
        + "[ mImportoRata               = " + mImportoRata + " ]\n"
        + "[ mNumeroRate                = " + mNumeroRate + " ]\n"
        + "[ mTipoRateizzazione         = " + mTipoRateizzazione + " ]\n"
        + "[ mDescrTipoRateizzazione    = " + mDescrTipoRateizzazione + " ]\n"
        + "[ mScadenzaGiorni            = " + mScadenzaGiorni + " ]\n"        
        + "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
//        + "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
        + "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
        + "[ mDataInserimento           = " + mDataInserimento + " ]\n"
        + "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
        + "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
        + "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
        + "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";    

    return lStr;
  }

}
