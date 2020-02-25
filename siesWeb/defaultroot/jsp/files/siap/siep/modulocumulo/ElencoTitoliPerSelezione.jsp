<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>


<jsp:useBean id="ParentFormName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ParentFormType"  scope="request" class="java.lang.String"/>

<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>


<%
  //==================================================================================
// Form di popup per l'elenco Titoli 
//==================================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Revoca Sospensione/Non Menzione - Elenco dei Titoli Associati </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();
      
      function insertIT (idTitolo) {
        formname = '<%=ParentFormName%>';
      
        div_titolo = window.parent.opener.document.getElementById("idTitolo_");
        div_titolo.style.display = (div_titolo.style.display == "none")? "block" : "none";
        window.parent.close();
      }
      
      
    </script>
  </head>
  
  
<body class="corpo" >
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaTitoliAssociati" >
    
    <input type="HIDDEN" name="ParentFormName" value="<%=ParentFormName%>">
    <input type="HIDDEN" name="ParentFormType" value="<%=ParentFormType%>">
    
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
              <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
          
        <td class="LBG">
          <font class="label">Funzione :</font>
          <%
            if(ParentFormType.equals(ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_APP_BENEFICI) ) {
          %>
          <font class="campo">ELENCO TITOLI &nbsp;</font>
          <% } else { %>    
          <font class="campo">ELENCO PROCEDIMENTI DI INDULTO &nbsp;</font>
          <% } %>    
        </td>   
      </tr>
    </table>
 
  <br>

  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco Titoli in Istruttoria</td>
    </tr>
  </table>  
  
  <table>
    <tr>
      <td class="int" width="8%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="5%"  >Azioni</td>
    </tr>

<%
  Iterator itx = ListaTitoli.iterator();

  while ( itx.hasNext())
  {
    TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
%>
    <tr>
      <td class="L">
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" >
        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato())%>
        <% } else { %>
        &nbsp;
        <% } %>
      </td>
      <td class="C" >
        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
        <% } else { %>
        &nbsp;
        <% } %>
      </td>
      <td class="C" >
        <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>');">
          <img align="middle" src="/images/fileselected.gif" border=0>
        </a>
      </td>
    </tr>
  
<%  
  }
%>
  </form>
  
</body>
</html>
  