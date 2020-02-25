<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Date"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="Provvedimento"   		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="LicenzaPermesso"  		scope="request" class="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		DettaglioScomputoPermessiCumulo		 -->


<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Scomputo Permessi - Titolo Cumulato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script>
    
  function eseguiFunzione(aTipoAzione)
  {
 	   
    if (aTipoAzione=='Inserisci'){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciScomputoPermessiCumulo";
      document.DettaglioScomputoPerm.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.DettaglioScomputoPerm.modalita.value = "I";
      
      document.DettaglioScomputoPerm.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
      document.DettaglioScomputoPerm.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
      document.DettaglioScomputoPerm.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
      
      document.DettaglioScomputoPerm.submit();
    }
    else if (aTipoAzione=='Modifica')
   	{
   	 	lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciScomputoPermessiCumulo";
        document.DettaglioScomputoPerm.modalita.value = "M";
        document.DettaglioScomputoPerm.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaglioScomputoPerm.submit();
   	} 
    else if (aTipoAzione=='Cancella')
    {
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) 
        {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciScomputoPermessiCumulo";
            document.DettaglioScomputoPerm.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.DettaglioScomputoPerm.modalita.value = "C";
	
            document.DettaglioScomputoPerm.submit();
        }
   
    }
    else if (aTipoAzione=='Indietro'){
      lAzione = "siap.siep.modulocumulo.action.ActRicercaScomputoPermessiCumulo";
      document.DettaglioScomputoPerm.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.DettaglioScomputoPerm.submit();
    }
  }
  
  </script>
</head>

<body class="corpo">
<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Scomputo Permessi &nbsp;</font>
      </td>
      <%// if (IstruttoriaCumulo.getFlagStato().equals("A") && 1==1)
      //{ %>
	      <%-- td class="LBG">
	        <a href="javascript:eseguiFunzione('Inserisci')">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
	        <% if (!"C".equals(Provvedimento.getFlagStato()))
	           { %>
	           	<a href="javascript:eseguiFunzione('Modifica')">
	          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
	        	<a href="javascript:eseguiFunzione('Cancella')">
	          	  <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
	        <% } %>
	      </td --%>
    <%// } %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

 <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
 <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioScomputoPerm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">
  
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" 		value = "<%=LicenzaPermesso.getIdLibAnticipataCumulo()%> " >
  <input type="hidden" name="modalita" value="">

<%
//==============================================================================
//         Sezione contenente il Riepilogo dei dati del Provvedimento
//==============================================================================
%>
<table cellspacing="2" cellpadding="2" width="90%">
  <tr>
    <td class="Titolo" colspan=2> Provvedimento</td>
  </tr>

  <tr>
    <td class="l">Tipo Provvedimento &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())%>&nbsp;</font>
    </td>
  </tr>    
  <tr>
    <td class="l">Oggetto &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%></font>
    </td>
  </tr>
<%
 if(Provvedimento.getAnnoProcedimento()!=null)
 { %> 
  <tr>
    <td class="L">
      Anno / Numero SIUS
    </td>
    <td class="L">
       <font class="campo">
           <%=StringUtils.toStringJSP(Provvedimento.getAnnoProcedimento())%>&nbsp;
       </font>
          /
       <font class="campo">
         <%=StringUtils.toStringJSP(Provvedimento.getProgrProcedimento())%>&nbsp;
       </font>
    </td>
  </tr>
<%
 } %>

<%
 if(Provvedimento.getAnnoProvvedimento()!=null)
 { %>    
  <tr>
    <td class="L">
       Anno / Numero <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())%>
    </td>
   <td class="L">
      <font class="campo">
            <%=StringUtils.toStringJSP(Provvedimento.getAnnoProvvedimento())%>&nbsp;
      </font>
      /
      <font class="campo">
            <%=StringUtils.toStringJSP(Provvedimento.getProgrProvvedimento())%>&nbsp;
      </font>
   </td>
  </tr>
<%
 }	%>
 
  <tr>
    <td class="l">
         Autorità emittente
    </td>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(Provvedimento.getDescrUfficioEmittente())%>&nbsp;
      </font>
        di
      <font class="campo">
        <%=StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente())%>&nbsp;
      </font>
    </td>
   </tr>
   <tr>
    <td class="L">
       Data Emissione
    </td>
    <td class="L">
       <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(), "dd-MM-yyyy"))%>&nbsp;
       </font>
    </td>
  </tr>
  <tr>
    <td class="l">Esito &nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrEsito())%></font>
    </td>
  </tr>

<% if(Provvedimento.getNote()!=null && !"".equals(Provvedimento.getNote()) )
   {	%>
     <tr><td></td></tr>	
	 <tr>
      <td class="l">Note &nbsp;</td>
      <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getNote(), "")%></font>
      </td>
     </tr>
<% } %>

 <%	if (LicenzaPermesso !=null && LicenzaPermesso.getIdLibAnticipataCumulo()!=null )
	{
		if(LicenzaPermesso.getNumeroGiorni()!=null )
		{	%>
	<tr><td>&nbsp;</td></tr>	
    <tr>
      <td class="l"><center>Totale giorni scomputati :</center></td>
      <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(LicenzaPermesso.getNumeroGiorni(),"&nbsp;")%></font>
      </td>
    </tr>
 <% 	}
	}	%>

</table>
</FORM>
</body>
</html>