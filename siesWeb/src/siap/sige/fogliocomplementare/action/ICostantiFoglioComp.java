package siap.sige.fogliocomplementare.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiFoglioComp</p>
* <p>Description: Classe di costanti di Foglio Complementare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface ICostantiFoglioComp
{

	 public static final String CAMPO_COD_MOTIVO_NON_INVIO        = "CodMotivoNonInvio";
	 public static final String CAMPO_DESCR_MOTIVO_NON_INVIO      = "DescrMotivoNonInvio";
	 public static final String CAMPO_GIORNO_DATA_INS_MANUALE     = "GiornoDataInsManuale";
	 public static final String CAMPO_MESE_DATA_INS_MANUALE       = "MeseDataInsManuale";
	 public static final String CAMPO_ANNO_DATA_INS_MANUALE 	  = "AnnoDataInsManuale";
	 public static final String CAMPO_GIORNO_DATA_TRASMISSIONE    = "GiornoDataTrasmissione";
	 public static final String CAMPO_MESE_DATA_TRASMISSIONE      = "MeseDataTrasmissione";
	 public static final String CAMPO_ANNO_DATA_TRASMISSIONE      = "AnnoDataTrasmissione";
	 public static final String CAMPO_ID_DOCUMENTO_ALLEGATO       = "IdDocumentoAllegato";
	 public static final String CAMPO_MOTIVO_ANNULLAMENTO         = "MotivoAnnullamento";
	 
	 public static final String PG_ELENCOPROVVEDIMENTICFC      = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/ElencoProvvedimentiCFC.jsp";
	 public static final String PG_ELENCOPROVVEDIMENTICFC_TASTOFUNZIONE= IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/ElencoProvvedimentiCFCTastoFunzione.jsp";
	 
	 public static final String PG_LOADDETTAGLIOCOMPFOGLIOCOMP = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadDettaglioCompFoglioComp.jsp";
	 public static final String PG_LISTA_CFC_ANNULLATI	       = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/ListaDocAllNulli.jsp";
	 public static final String PG_LOADINSERISCICOMPFOGLIOCOMP = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadInserisciCompFoglioComp.jsp";
	 public static final String PG_LOADINSERISCICOMPFOGLIOCOMP_TASTO_FUNZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadInserisciCompFoglioCompTastoFunzione.jsp";
	 public static final String PG_LOADINSERISCICOMPFOGLIOCOMP_TASTOFUNZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadInserisciCompFoglioCompTastoFunzione.jsp";
	 public static final String PG_LOADANNULLAMENTOFOGLIOCOMP  = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadAnnullamentoFoglioComp.jsp";
	 public static final String PG_LOAMOTIVIDANNULLAMENTOFOGLIOCOMP  = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadMotiviAnnullamentoFC.jsp";
	 public static final String PG_LOADDETTAGLIOCOMPFOGLIOCOMPTASTOFUNZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/LoadDettaglioCompFoglioCompTastoFunzione.jsp";
	 
	 public static final String PG_BUTTONS_CFC  = IWebConstants.ROOT_DIR + "files/siap/sige/fogliocomplementare/buttonsCFC.jsp";
	  
}