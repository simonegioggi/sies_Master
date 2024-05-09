package siap.siep.pagoPaBatch.model;

import java.math.BigDecimal;
import f3b.model.GenericModel;

public class BollettinoBatchPagopaModel extends GenericModel {
	private static final long serialVersionUID = -694397429790571450L;

	private BigDecimal mFkIdInvocazionePagopa;
	private BigDecimal mFkIdBollettinoPagopa;
	private BigDecimal mFkIdBatchPagopa;
	private String mStatoPagopa;
	
	public BigDecimal getFkIdInvocazionePagopa() { return mFkIdInvocazionePagopa;}
	public BigDecimal getFkIdBollettinoPagopa()  { return mFkIdBollettinoPagopa;}
	public BigDecimal getFkIdBatchPagopa()       { return mFkIdBatchPagopa;}
	public String     getStatoPagopa()           { return mStatoPagopa;  }
	
	public void setFkIdInvocazionePagopa (BigDecimal aValore) { this.mFkIdInvocazionePagopa = aValore; }
	public void setFkIdBollettinoPagopa  (BigDecimal aValore) { this.mFkIdBollettinoPagopa = aValore;  }
	public void setFkIdBatchPagopa       (BigDecimal aValore) { this.mFkIdBatchPagopa = aValore;  }
	public void setStatoPagopa           (String     aValore) { this.mStatoPagopa = aValore;  }

	
}
