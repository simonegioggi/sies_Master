<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaGE"        scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoloRichiesta"      scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="TitoloRiferimento"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="articolo"           scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazione"      scope="request" class="java.lang.String"/>

<% 

RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
  lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}

BeneficioCumuloModel lBenMod1 = null;
BeneficioCumuloModel lBenMod2 = null;
String lBenCod1="";
String lBenCod2="";
boolean isConProvvedimento = false; 

if(TitoloRichiesta.getBeneficiCumulo()!=null && TitoloRichiesta.getBeneficiCumulo().size()>0)
{	
	List ListaBen = TitoloRichiesta.getBeneficiCumulo();
  	lBenMod1 = (BeneficioCumuloModel)ListaBen.get(0);
  	lBenCod1 = lBenMod1.getCodTipoBeneficio();
  	if(ListaBen.size() > 1)
  	{
    	lBenMod2 = (BeneficioCumuloModel)ListaBen.get(1);
    	lBenCod2 = lBenMod2.getCodTipoBeneficio();
  	}
} 

//Preparo i model con i Benefici concessi con Provvedimento
StatoEsecTitoloCumulatoModel lStatEsecMod = null;
ComputiCumuloModel lCompMod = null;
String lCodProv = "";

if(TitoloRichiesta.getStatoEsecTitoloCumulato()!=null)
{
	isConProvvedimento = true;
	lStatEsecMod = (StatoEsecTitoloCumulatoModel) TitoloRichiesta.getStatoEsecTitoloCumulato();
	
	if(lStatEsecMod!=null && lStatEsecMod.getIdStatoEsecTitoloCumulato()!=null) {
		if(lStatEsecMod.getListaComputi()!=null && lStatEsecMod.getListaComputi().size()>0) 
		{
			lCompMod = (ComputiCumuloModel) lStatEsecMod.getListaComputi().get(0);
			lBenCod1 = lCompMod.getCodTipoAnnotazione();
		}
	}

}

%>

<html>
<head>
  <title> Gestione Richieste al Ge - Revoca Benefici</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettRichGE.submit();
    }
    
    function vaiaDecisione()
    {
      var action = document.f.comboActionRev[document.f.comboActionRev.selectedIndex].value ;
      
      if( action.indexOf('Cancella') >= 0)
      {
          if ( !window.confirm("Si vuole procedere alla cancellazione della decisione?") ) 
          {
              return;
          }
          else
          {
            document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
              document.DettRichGE.modalita.value="C";
              document.DettRichGE.submit();
          }
      }
      else
      {   
        document.DettRichGE.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.DettRichGE.modalita.value="I";
        document.DettRichGE.submit();
      }    
    }
    
  </script>
</head>

<body class="corpo">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richiesta Revoca Benefici&nbsp;</font>
      </td>
      
       <td class="LBG">
         <select name="comboActionRev" >
           <option value="siap.siep.modulocumulo.action.ActLoadInsDecisioneDelGERevocaBeneficioCumulo">Inserimento/Modifica Decisione del G.E.</option>
 <% if (RichiestaGE.getDecisioneGeSorvCum()!= null &&
    RichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null ) { %>
      <option value="siap.siep.modulocumulo.action.ActCancellaDecisioneGeSorvDellaRichiesta">Cancella Decisione del G.E. </option>
<%  } %>                                   
         </select>
         <a href="javascript:vaiaDecisione()">
           <img align="middle" src="/images/vedi24.gif" alt="Vai" width="24" height="24" border="0">
         </a>                    
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
</FORM>

  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
      <br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
  
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettRichGE">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo()%>">
  <input type="hidden" name="modalita" value="">
  <input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaBenefici">


<table width="95%" align="center">
    <tr><td colspan="2" class="Titolonocap" style="text-align:left" >Beneficio da Revocare:</td></tr>
</table> 
<%
if(!lBenCod2.equals(""))
{
  // Sospensione    %>

  <table width="95%" align="center" id="tabBen_S">
      <tr>
        <td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      </tr>
      <tr>   
        <td class="l">Natura Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
      </tr>
      <tr>  
        <td class="l">Durata sospensione</td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
  </table>

<%
//  Non Menzione
%>
  <table width="95%" align="center" id="tabBen_NM">
    <tr>
      <td class="l" width="20%">Tipo Beneficio</td>
        <td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
      <%--  
      <td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod2.getDescrTipoBeneficio(),"") %></font></td>
      <td class="l" colspan="2"><img src="/images/V.gif"> </td>
      --%>
    </tr>
  </table>
<%   
 }  
 else
 {
  if(lBenCod1.equals("01") )
  {
  // Sospensione    %>
  <table width="95%" align="center" id="tabBen_S">
      <tr>
        <td class="l" width="20%">Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrSottotipoBeneficio(), "") %></font></td>
      </tr>
      <tr>  
        <td class="l">Durata sospensione</td>
        <td class="l"><font class="campo">Anni:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getNumAnniSospensione(), "-" ) %></font></td>
      </tr>
  </table>
<%  }
  else if(lBenCod1.equals("02") )
  { 
    // Non Menzione %>
  <table width="95%" align="center" id="tabBen_NM">
    <tr>
      <td class="l" width="20%">Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      <%-- 
      <td class="l" width="150px"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %></font></td>
      <td class="l" colspan="2"><img src="/images/V.gif"> </td>
      --%>
    </tr>
  </table>
<%  }
  else if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003") )
  { 
    // Amnistia/ Indulto dati con provvedimento
    if(isConProvvedimento) { %>

    <table width="95%" align="center" id="tabBen_I">
      <tr>
        <td class="l" width="20%" >Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lCompMod.getDescrTipoAnnotazione(),"") %> </font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lCompMod.getDescDpr() ,"") %></font></td>
       </tr>
      <tr>  
        <td class="l">Concesso Nella misura di</td>
        <td class="l" colspan="2">
      
      <% if( !lCompMod.isQuantumReclusioneZero() ){ %>
      <font class="campo">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniReclusione(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiReclusione(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniReclusione(), "0") %>&nbsp;
          </font>&nbsp;
      <% } %>
    
  
      <% if( lCompMod.getImportoMulta() != null && lCompMod.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ) {%>
      <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoMulta(), "-") %>&nbsp;&euro;</font>                
      <% } %>
    

      <% if ( !lCompMod.isQuantumArrestoZero()) {  %>
      <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lCompMod.getNumAnniArresto(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lCompMod.getNumMesiArresto(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lCompMod.getNumGiorniArresto(), "0") %>&nbsp;
          </font>&nbsp;
      <% } %>
    

    <% if( lCompMod.getImportoAmmenda() != null && lCompMod.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
      <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lCompMod.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>                
    <% } %> 

      </td>
    </tr>
  </table>
    
<%  } else {  // Amnistia/ Indulto dati in Sentenza %>

	<table width="95%" align="center" id="tabBen_I">
      <tr>
        <td class="l" width="20%" >Beneficio</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrTipoBeneficio(),"") %> </font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lBenMod1.getDescrDpr() ,"") %></font></td>
       </tr>
      <tr>  
        <td class="l">Concesso Nella misura di</td>
        <td class="l" colspan="2">
      
      <% if( !lBenMod1.isQuantumReclusioneZero() ){ %>
      <font class="campo">Reclusione&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniReclusione(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiReclusione(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniReclusione(), "0") %>&nbsp;
          </font>&nbsp;
      <% } %>
    
  
      <% if( lBenMod1.getImportoMulta() != null && lBenMod1.getImportoMulta().compareTo(BigDecimal.ZERO) > 0  ) {%>
      <font class="campoLow">Multa:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoMulta(), "-") %>&nbsp;&euro;</font>                
      <% } %>
    

      <% if ( !lBenMod1.isQuantumArrestoZero()) {  %>
      <font class="campoLow">Arresto&nbsp;&nbsp;Anni: <%=StringUtils.toStringJSP(lBenMod1.getNumAnniArresto(), "0") %>&nbsp;
                              mesi: <%=StringUtils.toStringJSP(lBenMod1.getNumMesiArresto(), "0") %>&nbsp;
                              Giorni: <%=StringUtils.toStringJSP(lBenMod1.getNumGiorniArresto(), "0") %>&nbsp;
          </font>&nbsp;
      <% } %>
    

    <% if( lBenMod1.getImportoAmmenda() != null && lBenMod1.getImportoAmmenda().compareTo(BigDecimal.ZERO) > 0 ) { %>
      <font class="campoLow">Ammenda:&nbsp;<%=StringUtils.toStringJSP(lBenMod1.getImportoAmmenda(), "-") %>&nbsp;&euro;</font>                
    <% } %> 

      </td>
    </tr>
  </table>

<%  }
   }    
 } %> 

<!--        TITOLO  SU  CUI  è  CONCESSO  IL  BENEFICIO      -->        
<%  
  String AnnoNumero ="";
  AnnoNumero = TitoloRichiesta.getAnnoSentenza() +"/"+TitoloRichiesta.getNumeroSentenza();
 %> 
  <table width="95%" align="center">
    <tr><td class="Titolonocap" style="text-align:left" >Concesso sul Titolo </td></tr>
    <tr>
        <td class="l" >
          <font class="label"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumero%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRichiesta.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRichiesta.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
  </table>

<!--                  DATI  DELLA   RICHIESTA   DI  REVOCA                 -->
  <br>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap" >Dati della richiesta di Revoca</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta :</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>
    <%--
    <tr>
      <td class="l" width="200px">Beneficio</td>
      <td class="l" colspan="3" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
      
      <%  if(RichiestaGE.getCodDpr()!=null ) { %>
          <td class="l">DPR</td>
          <td class="l" >
            <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrDpr() )%></font>
          </td>
      <%  } else  { %>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
      <%  } %>
    </tr>
    --%>

<%  if(RichiestaGE.getFlagAppProvvisoria()!=null) { %>
      <tr>
        <td class="l">Anticipazione degli effetti :</td>
        
        <% if("A".equals(RichiestaGE.getFlagAppProvvisoria() )) { %>
        <td class="l" colspan="3"><img src="/images/V.gif"> </td>
        <% } else if("R".equals(RichiestaGE.getFlagAppProvvisoria() )) {  %>
        <td class="l" colspan="3"><font class="label" style="color:red" > NO </font></td>
        <% }  %>

      </tr>
<%  } %>

<%
  if(!articolo.equals("") && !motivazione.equals("") )
  { // Sosp/Cond
  %>
    <tr>
      <td class="l" >Articolo :</td>
      <td class="l">
      <font class="campo">
        <%=articolo%>
        &nbsp;</font>
      </td>
    </tr>
    <tr>
      <td class="l">Motivazione :</td>
      <td class="l">
      <font class="campo">
        <%=motivazione%>   
        &nbsp;</font>     
      </td>
    </tr>
<%  } %>

<%
if(lBenCod2.equals(""))
{
  if(lBenCod1.equals("03") || lBenCod1.equals("04") || lBenCod1.equals("002") || lBenCod1.equals("003") )
  { // Amnistia/Indulto visualizza i quantum di revoca richiesti
  %>

    <tr>
      <td class="l" colspan="1">Reclusione :  </td>
      <td class="l" colspan="3">&nbsp;
        <font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
        <font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniReclusioneR(), " - ") %></font>&nbsp;
        <font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiReclusioneR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniReclusioneR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp; <font class="label"> Multa : </font>&nbsp;
        
        <% if(RichiestaGE.getImportoMultaR()!=null && RichiestaGE.getImportoMultaR().compareTo(BigDecimal.ZERO) > 0 ){ %>
           <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoMultaR()) %></font>&nbsp;&euro;&nbsp;
        <% } else { %>
           <font class="campo">&nbsp; - &nbsp; </font>
        <%  }  %>

      </td>
    </tr>
    
    <tr>
      <td class="l" colspan="1">Arresto :  </td>
      <td class="l" colspan="3">&nbsp;
        <font class="campo" style="color:red"><%=StringUtils.toStringJSP(RichiestaGE.getFlagPiuMenoR(),"")%></font>&nbsp;&nbsp;
        <font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumAnniArrestoR(), " - " ) %></font>&nbsp;
        <font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumMesiArrestoR(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getNumGiorniArrestoR(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp; <font class="label"> Ammenda : </font>&nbsp;
        <% if(RichiestaGE.getImportoAmmendaR()!=null && RichiestaGE.getImportoAmmendaR().compareTo(BigDecimal.ZERO) > 0 ){ %>
           <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getImportoAmmendaR()) %></font>&nbsp;&euro;&nbsp;
        <% } else { %>
            <font class="campo">&nbsp; - &nbsp; </font>
        <%  }  %>
      </td>
    </tr>
<%
  }
 }  
%>
    
<%  if(RichiestaGE.getMotivazioni()!=null && !RichiestaGE.getMotivazioni().equals("") )
  { %>
  <tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getMotivazioni(),"&nbsp;" )%></font>
      </td>
    </tr>     
<%  } %>      
  </table>

<!--                  DATI  INVIO   RICHIESTA                  -->  
<%  if(lRicInv!=null && lRicInv.getIdRichiesteInviateCum()!=null )
  { %>  
  <table width="95%" align="center" style="display:block">
    <tr><td> </td></tr>
    <tr>
      <td class="l" colspan="1" width="200px">Inviata a : </td>
      <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrUfficioDest(),"")%></font>
         di <font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrLuogoDest(),"")%></font>
      </td>
      <td class="l" colspan="1">in data: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRicInv.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
    </tr>
<%    if( !("null").equals(lRicInv.getContenuto()) )
    {   %>
    <tr>
      <td class="l" colspan="1" width="200px">Contenuto </td>
        <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"")%></font></td>
      </tr>
 <%   } %>           
  </table>
<%  } %>

<!--                  DATI  TITOLO  RIFERIMENTO                  -->  
<%  
  String AnnoNumeroRev ="";
  // MEV70 solo se il Titolo di riferimento è valorizato.
  if (TitoloRiferimento.getAnnoSentenza()!=null) {
  AnnoNumeroRev = TitoloRiferimento.getAnnoSentenza() +"/"+TitoloRiferimento.getNumeroSentenza();
 %> 
  <table width="95%" align="center">
    <tr><td colspan="8" class="Titolonocap" style="text-align:left" >In Relazione al Titolo </td></tr>
    <tr>
        <td class="l" colspan="8">
          <font class="label"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumeroRev%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRiferimento.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(TitoloRiferimento.getDescrLuogoEmittente(), "") %></font>

          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(TitoloRiferimento.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>
  </table>
<% }%>
<!--              DATI  DELLA   DECISIONE   DEL   G.E.                 -->
<%
if(RichiestaGE.getDecisioneGeSorvCum()!=null && RichiestaGE.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum()!=null)  
{ 
  ProvvedimentoGeSorvCumModel DecisioneGE = (ProvvedimentoGeSorvCumModel)RichiestaGE.getDecisioneGeSorvCum();
  %>  
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della Decisione del Giudice dell'Esecuzione</td></tr>
    <tr>
      <td class="l" width="200px">Anno/Numero Procedimento SIGE</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getAnnoProvv() )%></font>
        &nbsp;/&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumeroProvv() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Ufficio Emittente </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getDescrUfficioEmittente() )%></font>
        &nbsp;di&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getDescrLuogoEmittente() )%></font>
      </td>
    </tr>
    <tr>
      <td class="l" width="200px">Data Emissione </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(DecisioneGE.getDataD(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>   
    <tr>
      <td class="l" width="200px">Revoca :</td>
      <td class="l" colspan="3">
      
<%  String lsolo="SI";
  if("S".equals(DecisioneGE.getBenSospCond()) )
  { 
    lsolo = "NO"; %>     
        <font class="campo">Sospensione Condizionale della Pena </font>
<%  } %> 

<%  if("N".equals(DecisioneGE.getBenNonMenzione()) )
  { 
    if("NO".equals(lsolo) )
    { %>     
        ,&nbsp;<font class="campo">Non Menzione </font>&nbsp;
<%    }
    else  
    { %>
    <font class="campo">Non Menzione </font>&nbsp;
<%    }
  } %> 

<%  if("I".equals(DecisioneGE.getBenIndulto()))
  { %>     
        <font class="campo">Indulto </font>&nbsp;
<%  } %>

<%  if("A".equals(DecisioneGE.getBenIndulto()))
  { %>     
        <font class="campo">Amnistia </font>&nbsp;
<% } %>        
      </td>
    </tr>  

    <tr>
      <td class="l">Esito :</td>
<%
//==============================================================================
// ESITO: se getFlagPiuMenoD()<>null allora trattasi di decisione su richiesta 
//        revoca Indulto. 
//        Altrimenti su revoca Sospensione condizionale/non menzione
//==============================================================================
%>
 <% if("C".equals(DecisioneGE.getFlagConforme() ))
  { 
    if( "-".equals(DecisioneGE.getFlagPiuMenoD() ))
    { %>     
          <td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%    }
    else if( "+".equals(DecisioneGE.getFlagPiuMenoD() ))
    { %>
      <td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%    }
    else
    { %>              
      <td class="l" colspan="3"><font class="label"> In Conformità alla richiesta del P.M., REVOCA </font></td>
<%   }
  }
  else if("D".equals(DecisioneGE.getFlagConforme() ))
  { 
    if( "-".equals(DecisioneGE.getFlagPiuMenoD() ))
    { %>     
          <td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., DIMINUISCE la Pena </font></td>
<%    }
    else if( "+".equals(DecisioneGE.getFlagPiuMenoD() ))
    { %>
      <td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M., AUMENTA la Pena </font></td>
<%    }
    else
    { %>              
      <td class="l" colspan="3"><font class="label"> In Difformità alla richiesta del P.M. </font></td>
<%    }
    }
  else if("I".equals(DecisioneGE.getFlagConforme() ))
  { %>     
    <td class="l" colspan="3"><font class="label" style="color:red" > dichiara INAMMISSIBILE la richiesta del P.M. </font></td>
<%  }
  else if("R".equals(DecisioneGE.getFlagConforme() ))
  {  %>
    <td class="l" colspan="3"><font class="label" style="color:red" > RIGETTA la richiesta del P.M. </font></td>
<%  } %>
          
    </tr>

<%  if("I".equals(DecisioneGE.getBenIndulto()) )
  { %>
    <tr>
      <td class="l" colspan="1">Reclusione :  </td>
      <td class="l" colspan="3">&nbsp;
        <font class="campo" style="color:red"><%=StringUtils.toStringJSP(DecisioneGE.getFlagPiuMenoD(),"")%></font>&nbsp;&nbsp;
        <font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumAnniReclusioneD(), " - ") %></font>&nbsp;
        <font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumMesiReclusioneD(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumGiorniReclusioneD(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp; <font class="label"> Multa : </font>&nbsp;
         <%         if(DecisioneGE.getImportoMultaD()!=null && DecisioneGE.getImportoMultaD().compareTo(BigDecimal.ZERO) > 0 )
                  { %>
                    <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getImportoMultaD()) %></font>&nbsp;&euro;&nbsp;
              <%    }
                  else
                  { %>
                    <font class="campo">&nbsp; - &nbsp; </font>
                <%  }  %>       

      </td>
    </tr>
    <tr>
      <td class="l" colspan="1">Arresto :  </td>
      <td class="l" colspan="3">&nbsp;
        <font class="campo" style="color:red"><%=StringUtils.toStringJSP(DecisioneGE.getFlagPiuMenoD(),"")%></font>&nbsp;&nbsp;
        <font class="label"> Anni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumAnniArrestoD(), " - " ) %></font>&nbsp;
        <font class="label"> Mesi </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumMesiArrestoD(), " - ") %></font>&nbsp;
        <font class="label"> Giorni </font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getNumGiorniArrestoD(), " - ") %></font>&nbsp;
         &nbsp;&nbsp;&nbsp; <font class="label"> Ammenda : </font>&nbsp;
         <%         if(DecisioneGE.getImportoAmmendaD()!=null && DecisioneGE.getImportoAmmendaD().compareTo(BigDecimal.ZERO) > 0 )
                  { %>
                    <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getImportoAmmendaD()) %></font>&nbsp;&euro;&nbsp;
              <%    }
                  else
                  { %>
                    <font class="campo">&nbsp; - &nbsp; </font>
                <%  }  %>       

      </td>
    </tr>
<%  } %>


    <tr>
      <td class="l" colspan="1">Note :  </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DecisioneGE.getMotivazioniD(),"&nbsp;" )%></font>
      </td>
    </tr>

  </table>   
<%
  } // Chiude if Decisione  %>

</FORM>

</body>
</html>
