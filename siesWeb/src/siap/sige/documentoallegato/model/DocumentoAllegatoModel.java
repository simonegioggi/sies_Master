package siap.sige.documentoallegato.model;

/**
* <p>Title: DocumentoAllegatoModel</p>
* <p>Description: Classe Model che rappresenta il DocumentoAllegato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * Model rappresentativo dell'entità omonima nel Modello Dati del DB.
 * Viene usato per contenere stampe aggiuntive collegate a Provvedimenti 
 * SIUS come ad esempio la Stampa del Deposito o il Foglio Complementare.
 * @author Lesposito
 *
 */
public class DocumentoAllegatoModel extends GenericModel
{
  
	private static final long serialVersionUID = 3988536196701415066L;
private BigDecimal	        mIdDocumentoAllegato;
  private Date	                mDataEmissione;
  private String	            mCodTipoDocumento;
  private String	            mDescrTipoDocumento;
  private BigDecimal	        mNumeroProgressivo;
  private String	            mFlagDocumentoRegistrato;
  //private Blob	              mDocBlob;
  private ByteArrayInputStream  mDocBlobIn;
  private ByteArrayOutputStream mDocBlobOut;
  private String    	        mCodOperatoreInserimento;
  private Date	                mDataInserimento;
  private String	            mCodUfficioInserimento;
  private String	            mDescrUfficioInserimento;
  private String	            mCodOperatoreAggiornamento;
  private Date	                mDataAggiornamento;
  private String	            mCodUfficioAggiornamento;
  private String	            mDescrUfficioAggiornamento;
  private BigDecimal            mEveIdEvento;
  private String	            mTemIdTemplate;
  private BigDecimal            mAnnoFoglioComplementare;
  private BigDecimal            mProgrFoglioComplementare;
  private Date	                mDataTrasmissione;
  private byte[]                mDocPerTrasferimento;  
  private Date	                mDataAnnullamento;
  private String                mMotivoAnnullamento;
  private String	            mComuneSedeGiudiziaria;

  private String	            mCodMotivazioneNonInvio;
  private String	            mDescrMotivazioneNonInvio;
  private String	            mDescrizioneNonInvio;
  private Date	                mDataUltInvio;
  private Date	                mDataInsMan;

  //COSTRUTTORE DI DEFAULT
  public DocumentoAllegatoModel ()
  {
    this.mIdDocumentoAllegato = null;
    this.mDataEmissione = null;
    this.mCodTipoDocumento = "";
    this.mDescrTipoDocumento = "";
    this.mNumeroProgressivo = null;
    this.mFlagDocumentoRegistrato = "";
    //this.mDocBlob = null;
    this.mCodOperatoreInserimento = "";
    this.mDataInserimento = null;
    this.mCodUfficioInserimento = "";
    this.mDescrUfficioInserimento = "";
    this.mCodOperatoreAggiornamento = "";
    this.mDataAggiornamento = null;
    this.mCodUfficioAggiornamento = "";
    this.mDescrUfficioAggiornamento = "";
    this.mEveIdEvento = null;
    this.mTemIdTemplate = "";
    this.mAnnoFoglioComplementare = null;
    this.mProgrFoglioComplementare = null;
    this.mDataTrasmissione = null;
    this.mDataAnnullamento = null;
    this.mMotivoAnnullamento = "";
    this.mComuneSedeGiudiziaria = "";
    this.mCodMotivazioneNonInvio = "";
    this.mDescrMotivazioneNonInvio = "";
    this.mDescrizioneNonInvio = "";
    this.mDataUltInvio = null;
    this.mDataInsMan = null;
  }

  //COSTRUTTORE DI COPIA
  public DocumentoAllegatoModel ( DocumentoAllegatoModel aModel )
  {
    this.mIdDocumentoAllegato = aModel.mIdDocumentoAllegato;
    this.mDataEmissione = aModel.mDataEmissione;
    this.mCodTipoDocumento = aModel.mCodTipoDocumento;
    this.mDescrTipoDocumento = aModel.mDescrTipoDocumento;
    this.mNumeroProgressivo = aModel.mNumeroProgressivo;
    this.mFlagDocumentoRegistrato = aModel.mFlagDocumentoRegistrato;
    //this.mDocBlob = aModel.mDocBlob;
    this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
    this.mDataInserimento = aModel.mDataInserimento;
    this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
    this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
    this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
    this.mDataAggiornamento = aModel.mDataAggiornamento;
    this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
    this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
    this.mEveIdEvento = aModel.mEveIdEvento;
    this.mTemIdTemplate = aModel.mTemIdTemplate;
    this.mAnnoFoglioComplementare = aModel.mAnnoFoglioComplementare;
    this.mProgrFoglioComplementare = aModel.mProgrFoglioComplementare;
    this.mDataTrasmissione = aModel.mDataTrasmissione;
    this.mDocPerTrasferimento = aModel.mDocPerTrasferimento;  // STUB 12/04/2005
    this.mDataAnnullamento = aModel.mDataAnnullamento;
    this.mMotivoAnnullamento = aModel.mMotivoAnnullamento;
	this.mComuneSedeGiudiziaria = aModel.mComuneSedeGiudiziaria;
    this.mCodMotivazioneNonInvio = aModel.mCodMotivazioneNonInvio;
    this.mDescrMotivazioneNonInvio = aModel.mDescrMotivazioneNonInvio;
    this.mDescrizioneNonInvio = aModel.mDescrizioneNonInvio;
    this.mDataUltInvio = aModel.mDataUltInvio;
    this.mDataInsMan = aModel.mDataInsMan;
	
  }

  //COSTRUTTORE MODEL
	public DocumentoAllegatoModel (
				   BigDecimal aIdDocumentoAllegato,
				   Date	      aDataEmissione,
				   String	  aCodTipoDocumento,
				   String	  aDescrTipoDocumento,
				   BigDecimal aNumeroProgressivo,
				   String	  aFlagDocumentoRegistrato,
				   //Blob	 aDocBlob,
				   String	  aCodOperatoreInserimento,
				   Date	      aDataInserimento,
				   String	  aCodUfficioInserimento,
				   String	  aDescrUfficioInserimento,
				   String	  aCodOperatoreAggiornamento,
                   Date	      aDataAggiornamento,
				   String	  aCodUfficioAggiornamento,
				   String	  aDescrUfficioAggiornamento,
				   BigDecimal aEveIdEvento,
				   String	  aTemIdTemplate,
				   BigDecimal aAnnoFoglioComplementare,
				   BigDecimal aProgrFoglioComplementare,
                   Date       aDataTrasmissione,
                   Date       aDataAnnullamento,
                   String     mMotivoAnnullamento,
                   String	  aComuneSedeGiudiziaria,
                   String	  aCodMotivazioneNonInvio,
                   String     aDescrMotivazioneNonInvio,
                   String	  aDescrizioneNonInvio,
                   Date       aDataUltInvio,
                   Date       aDataInsMan )

  {
    this.mIdDocumentoAllegato = aIdDocumentoAllegato;
    this.mDataEmissione = aDataEmissione;
    this.mCodTipoDocumento = aCodTipoDocumento;
    this.mDescrTipoDocumento = aDescrTipoDocumento;
    this.mNumeroProgressivo = aNumeroProgressivo;
    this.mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
    //this.mDocBlob = aDocBlob;
    this.mCodOperatoreInserimento = aCodOperatoreInserimento;
    this.mDataInserimento = aDataInserimento;
    this.mCodUfficioInserimento = aCodUfficioInserimento;
    this.mDescrUfficioInserimento = aDescrUfficioInserimento;
    this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
    this.mDataAggiornamento = aDataAggiornamento;
    this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
    this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
    this.mEveIdEvento = aEveIdEvento;
    this.mTemIdTemplate = aTemIdTemplate;
    this.mAnnoFoglioComplementare = aAnnoFoglioComplementare;
    this.mProgrFoglioComplementare = aProgrFoglioComplementare;
    this.mDataTrasmissione = aDataTrasmissione;
    this.mDataAnnullamento = aDataAnnullamento;
    this.mMotivoAnnullamento = mMotivoAnnullamento;
    this.mComuneSedeGiudiziaria = aComuneSedeGiudiziaria;
    this.mCodMotivazioneNonInvio = aCodMotivazioneNonInvio;
    this.mDescrMotivazioneNonInvio = aDescrMotivazioneNonInvio;
    this.mDescrizioneNonInvio = aDescrizioneNonInvio;
    this.mDataUltInvio = aDataUltInvio;
    this.mDataInsMan = aDataInsMan;
  }

  //
  // METODI GET()
  //
  public BigDecimal  getIdDocumentoAllegato() 	    { return mIdDocumentoAllegato; }
  public Date 		 getDataEmissione() 		    { return mDataEmissione; }
  public String 	 getCodTipoDocumento() 		    { return mCodTipoDocumento; }
  public String 	 getDescrTipoDocumento() 	    { return mDescrTipoDocumento; }
  public BigDecimal  getNumeroProgressivo()         { return mNumeroProgressivo; }
  public String 	 getFlagDocumentoRegistrato()   { return mFlagDocumentoRegistrato; }
  //public Blob      getDocBlob() 			        { return mDocBlob; }
  public ByteArrayInputStream getDocBlobIn()        { return mDocBlobIn;}
  public ByteArrayOutputStream getDocBlobOut()      { return mDocBlobOut;}
  public String 	 getCodOperatoreInserimento()   { return mCodOperatoreInserimento; }
  public Date 		 getDataInserimento() 		    { return mDataInserimento; }
  public String 	 getCodUfficioInserimento()     { return mCodUfficioInserimento; }
  public String 	 getDescrUfficioInserimento()   { return mDescrUfficioInserimento; }
  public String 	 getCodOperatoreAggiornamento() { return mCodOperatoreAggiornamento; }
  public Date 		 getDataAggiornamento() 	    { return mDataAggiornamento; }
  public String 	 getCodUfficioAggiornamento()   { return mCodUfficioAggiornamento; }
  public String 	 getDescrUfficioAggiornamento() { return mDescrUfficioAggiornamento; }
  public BigDecimal  getEveIdEvento() 		        { return mEveIdEvento; }
  public String 	 getTemIdTemplate() 		    { return mTemIdTemplate; }
  public BigDecimal  getAnnoFoglioComplementare() 	{ return mAnnoFoglioComplementare; }
  public BigDecimal  getProgrFoglioComplementare() 	{ return mProgrFoglioComplementare; }
  public Date 		 getDataTrasmissione() 	        { return mDataTrasmissione; }
  public byte[]      getDocPerTrasferimento()       { return mDocPerTrasferimento; }  // STUB 12/04/2005
  public Date 		 getDataAnnullamento()          { return this.mDataAnnullamento;}
  public String 	 getMotivoAnnullamento()        { return this.mMotivoAnnullamento; }
  public String 	 getComuneSedeGiudiziaria() 	{ return mComuneSedeGiudiziaria; }
  public String 	 getCodMotivazioneNonInvio() 	{ return mCodMotivazioneNonInvio; }
  public String 	 getDescrMotivazioneNonInvio() 	{ return mDescrMotivazioneNonInvio; }
  public String 	 getDescrizioneNonInvio() 	    { return mDescrizioneNonInvio; }
  public Date 		 getDataUltInvio() 	            { return mDataUltInvio; }
  public Date 		 getDataInsMan() 	            { return mDataInsMan; }


  //
  // METODI SET()
  //
  public void  	 setIdDocumentoAllegato(BigDecimal aValore ) 	  { mIdDocumentoAllegato = aValore; }
  public void  	 setDataEmissione(Date aValore ) 			      { mDataEmissione = aValore; }
  public void  	 setCodTipoDocumento(String aValore ) 			  { mCodTipoDocumento = aValore; }
  public void  	 setDescrTipoDocumento(String aValore ) 		  { mDescrTipoDocumento = aValore; }
  public void  	 setNumeroProgressivo(BigDecimal aValore ) 		  { mNumeroProgressivo = aValore; }
  public void  	 setFlagDocumentoRegistrato(String aValore ) 	  { mFlagDocumentoRegistrato = aValore; }
  //public void    setDocBlob(Blob aValore ) 			          { mDocBlob = aValore; }
  public void    setDocBlobIn( ByteArrayInputStream aValore )     { mDocBlobIn = aValore;}
  public void    setDocBlobOut( ByteArrayOutputStream aValore )   { mDocBlobOut = aValore;}
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { mCodOperatoreInserimento = aValore; }
  public void  	 setDataInserimento(Date aValore ) 			      { mDataInserimento = aValore; }
  public void  	 setCodUfficioInserimento(String aValore ) 		  { mCodUfficioInserimento = aValore; }
  public void  	 setDescrUfficioInserimento(String aValore ) 	  { mDescrUfficioInserimento = aValore; }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { mCodOperatoreAggiornamento = aValore; }
  public void  	 setDataAggiornamento(Date aValore ) 			  { mDataAggiornamento = aValore; }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { mCodUfficioAggiornamento = aValore; }
  public void  	 setDescrUfficioAggiornamento(String aValore ) 	  { mDescrUfficioAggiornamento = aValore; }
  public void  	 setEveIdEvento(BigDecimal aValore ) 			  { mEveIdEvento = aValore; }
  public void  	 setTemIdTemplate(String aValore ) 			      { mTemIdTemplate = aValore; }
  public void  	 setAnnoFoglioComplementare(BigDecimal aValore )  { mAnnoFoglioComplementare = aValore; }
  public void  	 setProgrFoglioComplementare(BigDecimal aValore ) { mProgrFoglioComplementare = aValore; }
  public void  	 setDataTrasmissione(Date aValore ) 			  { mDataTrasmissione = aValore; }
  public void    setDocPerTrasferimento(byte[] aValore)           { mDocPerTrasferimento = aValore;}  // STUB 12/04/2005
  public void    setDataAnnullamento(Date aValore)                { mDataAnnullamento = aValore;}
  public void    setMotivoAnnullamento(String aValore)            { mMotivoAnnullamento = aValore;}
  public void  	 setComuneSedeGiudiziaria(String aValore ) 		  { mComuneSedeGiudiziaria = aValore;} 
  public void  	 setCodMotivazioneNonInvio(String aValore ) 	  { mCodMotivazioneNonInvio = aValore;}
  public void  	 setDescrMotivazioneNonInvio(String aValore ) 	  { mDescrMotivazioneNonInvio = aValore;}
  public void  	 setDescrizioneNonInvio(String aValore ) 	      { mDescrizioneNonInvio = aValore;}
  public void    setDataUltInvio(Date aValore)                    { mDataUltInvio = aValore;}
  public void    setDataInsMan(Date aValore)                      { mDataInsMan = aValore;}
  
}
