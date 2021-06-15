package siap.sico.utenzaAdn.controller;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public interface IUtenzaAdn {

	BigDecimal inserisciUtenzaAdn(String userId) throws F3BException;

	UtenzaAdnModel verificaEsistenzaUtenzaAdn(String userId) throws F3BException;

}