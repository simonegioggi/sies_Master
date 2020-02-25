<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.lang.String" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius" %>
<%@ page import="siap.sius.scadenzario.model.ScadenzarioSiusModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>

<jsp:useBean id="scadenzarii" scope="request" class="java.util.Vector" />
<jsp:useBean id="tipoScadenzario" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoScadenzario" scope="request" class="java.lang.String" />
<jsp:useBean id="tipo" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="anni" scope="request" class="java.lang.String"/>
<jsp:useBean id="mesi" scope="request" class="java.lang.String"/>
<jsp:useBean id="giorni" scope="request" class="java.lang.String"/>

<%
String lData="";
if (tipoScadenzario.compareTo("70")==0)
  lData="Data Iscrizione";
else
  lData="Data Notifica";
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Scadenzario </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Consultazione Scadenzario di <%=descTipoScadenzario%> - Criterio: <%=titolo%>&nbsp;</font></td>
        <%--td><jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/><td--%>
      </tr>
    </table>
    <br>
    <table cellpadding=2 cellspacing=2>
    <%
    if(tipo.equals("tutti"))
    {
    %>
   <tr>
    <td>&nbsp;<td>
    <td>&nbsp;<td>
    <td align="right"><img src="/images/QuadratinoVerde.gif"></td><td class="campo">In Scadenza</td>
    <td align="right"><img src="/images/QuadratinoRosso.gif"></td><td class="campo">In Scadenza Oggi</td>
    </tr>
    <%
    }
    %>

    <tr>
      <td class="int">N. SIUS</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Data Nascita</td>
      <td class="int">Tipo Provv.</td>
      <td class="int">Data Provv.</td>
      <td class="int"><%=lData%></td>
<%
       if(!tipo.equals("oggi"))
        {
%>
       <td class="int">Data Scadenza</td>
       <td class="int">Giorni Residui</td>
<%
        }%>
        <td class="int">Visto</td>
        <td class="int">Azioni</td>
     </tr>

<%
    Iterator itx = scadenzarii.iterator();
    while ( itx.hasNext())
    {
      ScadenzarioSiusModel lSca = (ScadenzarioSiusModel)itx.next();
      FascicoloSiusModel lFas = lSca.getFascicoloSius();
      SoggettoModel lSog = lFas.getSoggetto();
%>
 <tr>
<%
    if(tipo.equals("tutti"))
    {
      if(lSca.getGiorniResidui().intValue()== 0)
      {
%>

          <td class=crosso><%=lFas.getChiaveAnno() %>/<%=lFas.getChiaveProgr()%></td>
          <td class=crosso><%=lSog.getCognome()%></td>
          <td class=crosso><%=lSog.getNome()%></td>
          <td class=crosso><%=lSog.getDescrComuneNascita()%></td>
          <td class=crosso><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") %></td>
<%
          if (lSca.getEveIdEvento()!= null && lSca.getEvento() != null )
          {%>
            <td class=crosso><%=lSca.getEvento().getDescrTipoProvvedimento()%></td>
            <td class=crosso><%=DateUtils.getDateToString(lSca.getEvento().getDataEmissione(),"dd/MM/yyyy")%></td>
        <%} else {%>
            <td class=crosso>-</td>
            <td class=crosso>-</td>
        <%}
%>
          <td class=crosso><%=DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy") %></td>
          <td class=crosso><%=DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy") %></td>
          <td class=crosso><%=lSca.getGiorniResidui().intValue()%></td>
<%
      }
      else
      {
          if(lSca.getGiorniResidui().intValue()<= 7 && lSca.getGiorniResidui().intValue()> 0)
          {
%>
              <td class=cverde><%=lFas.getChiaveAnno()%>/<%=lFas.getChiaveProgr()%></td>
              <td class=cverde><%=lSog.getCognome()%></td>
              <td class=cverde><%=lSog.getNome()%></td>
              <td class=cverde><%=lSog.getDescrComuneNascita()%></td>
              <td class=cverde><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") %></td>
<%
              if (lSca.getEveIdEvento()!= null && lSca.getEvento() != null )
              {%>
                <td class=cverde><%=lSca.getEvento().getDescrTipoProvvedimento()%></td>
                <td class=cverde><%=DateUtils.getDateToString(lSca.getEvento().getDataEmissione(),"dd/MM/yyyy")%></td>
            <%} else {%>
                <td class=cverde>-</td>
                <td class=cverde>-</td>
            <%}
%>
              <td class=cverde><%=DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy") %></td>
              <td class=cverde><%=DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy") %></td>
              <td class=cverde><%=lSca.getGiorniResidui().intValue()%></td>
<%
          }
          else
          {
%>
            <td class=C><%=lFas.getChiaveAnno()%>/<%=lFas.getChiaveProgr()%></td>
            <td class=C><%=lSog.getCognome()%></td>
            <td class=C><%=lSog.getNome()%></td>
            <td class=C><%=lSog.getDescrComuneNascita()%></td>
            <td class=C><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") %></td>
<%
              if (lSca.getEveIdEvento()!= null && lSca.getEvento() != null )
              {%>
                <td class=C><%=lSca.getEvento().getDescrTipoProvvedimento()%></td>
                <td class=C><%=DateUtils.getDateToString(lSca.getEvento().getDataEmissione(),"dd/MM/yyyy")%></td>
            <%} else {%>
                <td class=C>-</td>
                <td class=C>-</td>
            <%}
%>
            <td class=C><%=DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy") %></td>
            <td class=C><%=DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy") %></td>
            <td class=C><%=lSca.getGiorniResidui().intValue()%></td>
<%
          }

        }
      }// FINE TUTTI
      else
      {
%>
            <td class=C><%=lFas.getChiaveAnno()%>/<%=lFas.getChiaveProgr()%></td>
            <td class=C><%=lSog.getCognome()%></td>
            <td class=C><%=lSog.getNome()%></td>
            <td class=C><%=lSog.getDescrComuneNascita()%></td>
            <td class=C><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy") %></td>
            <%
          	if (lSca.getEveIdEvento()!= null && lSca.getEvento() != null )
          	{%>
            <td class=C><%=lSca.getEvento().getDescrTipoProvvedimento()%></td>
            <td class=C><%=DateUtils.getDateToString(lSca.getEvento().getDataEmissione(),"dd/MM/yyyy")%></td>
        	<%} else {%>
    	        <td class=C>-</td>
	            <td class=C>-</td>
	        <%}
%>
            <td class=C><%=DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy") %></td>

          <% if(!tipo.equals("oggi") )
          {%>
              <td class=C><%=DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy") %></td>
              <td class=C><%=lSca.getGiorniResidui().intValue()%></td>
          <%
         }
       } %>
       <% if( lSca!=null && lSca.getFlagVisto()!=null && lSca.getFlagVisto().equals("S"))
          {%>
           <td class=C><img src="/images/TickRed.gif"> </td>
           <td class=C> &nbsp;</td>
          <%}
          else
          {%>
            <td class=C> &nbsp;</td>
            <td class=C>
              <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.scadenzario.action.ActVistoScadenzarioSius&<%=ICostantiScadenzarioSius.CAMPO_ID_SCADENZARIO%>=<%=lSca.getIdScadenzarioSius()%>&T=<%=tipo%>&A=<%=anni%>&M=<%=mesi%>&G=<%=giorni%>&TS=<%=tipoScadenzario%>&DS=<%=descTipoScadenzario%>  ">
                  <img src="/images/TickRed.gif" width="12" height="12" alt="Visto" border="0">
              </a>
            </td>
          <%}%>
    </tr>
<%
    }
%>
    </table>
  </FORM>
</body>
</html>