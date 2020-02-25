package siap.regesies.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeModel
 * </p>
 * <p>
 * Description: Classe che generalizza i model del sistema rege-sies
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class RegeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 247168613584353641L;

	protected BigDecimal toBigDecimal(int aValore) {

		BigDecimal lBigDec = null;
		if (aValore > 0)
			lBigDec = new BigDecimal(aValore);

		return lBigDec;
	}

}