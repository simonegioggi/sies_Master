<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sius.udienzaprocedimento.model.UdienzaProcedimentoUdiModel" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>


<jsp:useBean id="MovimentiUdienze" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<html>
<% if(modalita.compareTo("action") == 0)
   {
     String lRifFas = "";
     if (fascicoloSiusGP.getFascicoloSiusModel() != null && fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno() != null && fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr() != null)
     {
        lRifFas = " " + fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno() + "/" + fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr();
     }

%>
<body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Movimenti Udienza del Procedimento <%=lRifFas%> </font>
      </td>
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
   </table>
<br>
<% } %>

  <table width="100%" border=1>
<%
  if ( MovimentiUdienze.size() == 0 )
  {
%>
        <td class="int" align="left"> Non ci sono udienze fissate per il procedimento.</td>
<%
  }else {
%>
    <tr>
      <td class="int" width="20%">Data Udienza</td>
      <td class="int" width="40%">Tipo Operazione</td>
      <td class="int" width="20%">Data inserimento</td>
      <td class="int" width="20%">Data Modifica</td>

    </tr>
<%
    Iterator itx = MovimentiUdienze.iterator();
    String tipoOp = "";
    while (itx.hasNext() )
    {
        UdienzaProcedimentoUdiModel udipro = (UdienzaProcedimentoUdiModel) itx.next();
        tipoOp = "";

        if(udipro.getUdienzaProcedimento().getFlagRinviata() != null && udipro.getUdienzaProcedimento().getFlagRinviata().trim().length() > 0)
        {
           tipoOp = udipro.getUdienzaProcedimento().getFlagRinviata();
           if (tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_FISSATA)== 0)
              tipoOp = "Fissazione";
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_PREFISSATA)== 0)
              tipoOp = "Prefissazione";
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_MODIFICATA)== 0)
           {
              if(udipro.getUdienzaProcedimento().getEveIdEvento() != null)
                 tipoOp = "Fissazione e poi Modifica";
              else
                 tipoOp = "Prefissazione e poi Modifica";
           }
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_ANNULLATA)== 0)
              tipoOp = "Annullamento";
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_RINVIATA)== 0)
              tipoOp = "Rinvio";
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.UDIENZA_SEGUITO_RINVIO)== 0)
              tipoOp = "Fissazione a seguito Rinvio";
           else if(tipoOp.compareToIgnoreCase(ICostantiUdienzaProcedimento.NUOVO_RUOLO)== 0)
              tipoOp = "Nuovo Ruolo";
        }

%>
    <tr>
      <td class="L"><font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(udipro.getDataUdienza() ,"dd-MM-yyyy"),"-") %>
      </font></td>
      <td class="L"><font class="campo">
        <%=StringUtils.toStringJSP(tipoOp ,"-") %>
      </font></td>
      <td class="L"><font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(udipro.getUdienzaProcedimento().getDataInserimento() ,"dd-MM-yyyy"),"-") %>
      </font></td>
      <td class="L"><font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(udipro.getUdienzaProcedimento().getDataAggiornamento() ,"dd-MM-yyyy"),"-") %>
      </font></td>
    </tr>

<%
    } // endwhile
  }   // endif
%>

</table>
<% if(modalita.compareTo("action") == 0)
   {
%>
<br>
</body>
<%
   }
%>
</html>