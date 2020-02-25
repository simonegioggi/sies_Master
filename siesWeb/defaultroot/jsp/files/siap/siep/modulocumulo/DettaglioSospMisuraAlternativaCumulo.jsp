<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="UfficioEmittenteProvv" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<jsp:useBean id="oggettodecisione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="tipologiadecisione" scope="request" class="java.util.ArrayList"/>

<%
Vector <ComputiCumuloModel> lListaComputi = Provvedimento.getListaComputi();
ComputiCumuloModel lComputo = new ComputiCumuloModel();

if (lListaComputi!=null) {

	Iterator itxComputi = lListaComputi.iterator();
	int ind = 0;
	while ( itxComputi.hasNext()) 
	{
		lComputo = (ComputiCumuloModel) itxComputi.next();
		ind++;
	}
	
    lComputo.setDescrOggettoDecisione ( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoInterruzione(),lComputo.getCodOggettoDecisione() ) );  
}

%>

<html>
<head>
  <title> Dettaglio Concessione Misura Alternativa </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>  
 
  <script language="JavaScript">

    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaSospMisureAlternativeCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }

    function showDivForOggetto()
    {
      var codOggetto = '<%= (Provvedimento.getCodMotivo())%>' ;
      var arrayEmpty = [ "2297","-"];
      var arrayDataIngresso = [ "2146","2147","2145","2151","2153", "2149","2150","2152", "2293", "2291", "2148", "2280" ];


      if ($.inArray(codOggetto, arrayDataIngresso)>-1) {
        $('#divDataIngresso').show();
        $('#divDataIngresso textarea').prop('disabled',false);
      }
      else {
        $('#divDataIngresso').hide();
        $('#divDataIngresso textarea').prop('disabled',true);
      }

      if ($.inArray(codOggetto, arrayEmpty)>-1) {
           $('#divEmpty').show();
           $('#divEmpty text').prop('disabled',false);
      }
      else {
        $('#divEmpty').hide();
        $('#divEmpty text').prop('disabled',true);
      }
    }
    
  </script>
</head>

<body class="corpo" onLoad="showDivForOggetto();" >

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Sospensione Misura Alternativa &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">


  	<table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
		
		<tr>
	      <td class="titolo" colspan="4">Dati del Provvedimento</td>
		</tr>
	     
		<tr>
	      <td class="l" width="210px">Data emissione provvedimento </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataEmissioneProvv(),"dd-MM-yyyy"))%></font>&nbsp;
	      </td>
	      <td class="l">Tipo provvedimento </td>
	      <td class="l" >
			<font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento() )%> </font>&nbsp;
	      </td>		      
	    </tr>

		<tr>
	      <td class="l">Anno / Numero Provvedimento </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoProvv() )%> &nbsp;/ <%=StringUtils.toStringJSP(lComputo.getProgrProvv() )%></font>&nbsp;
	      </td>
	      
	      <td class="l">Anno / Numero SIUS </td>
	      <td class="l" >
	         <font class="campo"><%=StringUtils.toStringJSP(lComputo.getAnnoProc() )%> &nbsp;/ <%=StringUtils.toStringJSP(lComputo.getProgrProc() )%></font>&nbsp;
	      </td>
	    </tr>

	    <tr> 
	      <td class="l">Autorità Emittente </td>
	      <td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrUfficioEmittente())+ " " + StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente())%> </font>&nbsp;
	      </td>
	    </tr>
	     
	    <tr> 
	      <td class="l">Oggetto Procedimento</td>
	      <td class="l"colspan = "3">
	         <font class="campo"><%=StringUtils.toStringJSP( Provvedimento.getDescrMotivo() ) %></font>&nbsp;
		  </td>
	    </tr>


		<div id="blank" style="display:block;">
		  <table width="95%" align="center" >
		  <tr>
		   </tr>
		  </table>
		</div>  

		<div id="divDataIngresso" style="display:block;">
		  <table width="95%" align="center" >
		  	<tr>
		      <td class="l"  width="210">Data ingresso in istituto </td>
		      <td class="l" colspan = "3">
		         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataIngressoIstituto(),"dd-MM-yyyy"))%></font>
		      </td>
	    	</tr>
		  </table>
		</div>

	</table>

<br>

</form>
</body>
</html>