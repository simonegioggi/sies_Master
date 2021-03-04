package siap.sico.utenzaAdn.controller;

import java.math.BigDecimal;
import java.util.List;

import f3b.util.F3BException;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public interface IAssocUtenteSiesAdn {

	List<AssocUtenteSiesAdnModel> verificaAssociazioneSiesAdn(String userId) throws F3BException;

	AssocUtenteSiesAdnModel inserisciAssociazioneSiesAdn(String userId, BigDecimal id) throws F3BException;

}