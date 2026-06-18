<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>


<jsp:useBean id="aVecchioMagistrato" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="aNuovoMagistrato"   scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<jsp:useBean id="aListaProcedimenti" scope="request" class="java.util.Vector" />

<jsp:useBean id="aDataCompetenza"    scope="request" class="java.lang.String" />

<%
//==============================================================================
// Form per la visualizzazione del risultato della ricerca dei procedimenti 
// attivi per un certo magistrato
//
//==============================================================================
%>
<html>
  <head>
    <title>[S.I.E.S.] - Elenco Procedimenti Modificati</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script>
      function RicariaListaProcedimenti()
      {
        document.EsitoModificaProcedimentiMagistratoCompetente.submit();
      }
    </script>
    
</head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Esito Cambio Magistrato Competente</font>
      </td>
      
      <td class="LBG">
        <a href="javascript:RicariaListaProcedimenti()">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"
               title="Ricarica lista procedimenti">
        </a>
      </td>
      
    </tr>
  </table>

  <br><br>
  
  <%
  //============================================================================
  // Form per ricaricare l'elenco dei procedimenti assegnati al magistrato
  //============================================================================
  %>
  <form method="POST" name="EsitoModificaProcedimentiMagistratoCompetente" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActRicercaProcedimentiAssegnati">
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>" value="<%=aVecchioMagistrato.getCodMagistrato()%>" >
  <table style="width: 95%;">
    <%
    //========================================================================
    // Elenco dei fascicoli per i quali è stato Aggiornato il magistrato
    //========================================================================
    %>
    <tr>
      <td colspan="100%">
        <table cellspacing="2" cellpadding="2" align="center" width="95%">
          <tr>
            <td class="int" colspan="4"> Totale Procedimenti Aggiornati: <%=aListaProcedimenti.size()%></td>
          </tr>
          <tr>
            <td class="int">Numero SIEP</td>
            <td class="int">Competenza trasferita dal Magistrato</td>
            <td class="int">al Magistrato</td>
            <td class="int">In data</td>
          </tr>
          
          <%
          for (int i=0; i<aListaProcedimenti.size();i++)
          {
            String lAnnoNumero = (String) aListaProcedimenti.elementAt(i);
            
            %>
            <tr>
              <td class="c"><font class="label"><%=StringUtils.toStringJSP(lAnnoNumero)%></font></td>
              <td class="c"><font class="label"><%=StringUtils.toStringJSP(aVecchioMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aVecchioMagistrato.getNome())%></font></td>
              <td class="c"><font class="label"><%=StringUtils.toStringJSP(aNuovoMagistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(aNuovoMagistrato.getNome())%></font></td>
              <td class="c"><font class="label"><%=aDataCompetenza%></font></td>
            </tr>
            <%
          }
          %>
        </table>
      </td>
    </tr>
</table>
</form>
</body>
</html>