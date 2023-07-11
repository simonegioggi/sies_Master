package siap.siep.sanzionesostitutiva.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;


/**
 * MEV-2023-33
 * Model aggregato per i risultati della funzione di ricera Stato PagementoBollettini
 * 
 * @author d.fiorletta
 */
public class RicercaStatoPagamentiModel extends GenericModel {
	private BigDecimal mIdFascicoloSiep;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String     mChiaveUfficio;
	private Date       mDataIscrizione;
	private String     mCognome;
	private String     mNome;
	
	private String     mTipoRateizzazione;
	private BigDecimal mImportoDaPagare;
	private BigDecimal mImportoPagato;
	private Date       mDataUltimaScadenza;
	
	// GETTER
	public BigDecimal getIdFascicoloSiep()    { return mIdFascicoloSiep;  }
	public BigDecimal getChiaveAnno()         { return mChiaveAnno; }
	public BigDecimal getChiaveProgr()        { return mChiaveProgr;  }
	public String     getChiaveUfficio()      { return mChiaveUfficio;  }
	public Date       getDataIscrizione()     { return mDataIscrizione; }
	public String     getCognome()            { return mCognome;  }
	public String     getNome()               { return mNome; }
	public String     getTipoRateizzazione()  { return mTipoRateizzazione; }
	public BigDecimal getImportoDaPagare()    { return mImportoDaPagare;  }
	public BigDecimal getImportoPagato()      { return mImportoPagato;  }
	public Date       getDataUltimaScadenza() { return mDataUltimaScadenza; } 
	
	// SETTER
	public void setIdFascicoloSiep    (BigDecimal mIdFascicoloSiep) {this.mIdFascicoloSiep = mIdFascicoloSiep;}
	public void setChiaveAnno         (BigDecimal mChiaveAnno) {this.mChiaveAnno = mChiaveAnno;}
	public void setChiaveProgr        (BigDecimal mChiaveProgr) {this.mChiaveProgr = mChiaveProgr;}
	public void setChiaveUfficio      (String     mChiaveUfficio) {this.mChiaveUfficio = mChiaveUfficio;}
	public void setDataIscrizione     (Date       mDataIscrizione) {this.mDataIscrizione = mDataIscrizione;}
	public void setCognome            (String     mCognome) {this.mCognome = mCognome;}
	public void setNome               (String     mNome) {this.mNome = mNome;}
	public void setTipoRateizzazione  (String     mTipoRateizzazione)  { this.mTipoRateizzazione = mTipoRateizzazione; }
	public void setImportoDaPagare    (BigDecimal mImportoDaPagare) {this.mImportoDaPagare = mImportoDaPagare;}
	public void setImportoPagato      (BigDecimal mImportoPagato) {this.mImportoPagato = mImportoPagato;}
	public void setDataUltimaScadenza (Date       mDataUltimaScadenza) {this.mDataUltimaScadenza = mDataUltimaScadenza;}

	
}
