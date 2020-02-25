<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />

<jsp:useBean id="residenza"  scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Reato - </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
   Disabilita();
  }

 function Disabilita()
  {
    document.Abbandona.A.disabled = true;

<% if(residenza.getCodTipoResidenza().equals("R"))
  { %>

   if (aForm==null)
    aForm=document.getElementById("Domicilio");
    document.Domicilio.D.disabled = true;

<%}else{%>
  if (aForm==null)
    aForm=document.getElementById("Sentenza");

    document.Sentenza.S.disabled = true;
<%}%>

   aForm.submit();
  }

</script>
<%}%>
  </head>
  <body class="corpo">
  <form name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font>
       <% if(residenza.getCodTipoResidenza().equals("R"))
          { %>
            <font class="campo">Dettaglio Residenza</font>
       <% }
          else
          { %>
            <font class="campo">Dettaglio Domicilio</font>
       <% } %>
        </td>

      </tr>
    </table>
  </form>
  <jsp:include page="/jsp/files/siap/sico/soggetto/DettaglioSoggettoAssociato.jsp"/>
  <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l"><font class="label">Indirizzo</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(residenza.getIndirizzo())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">CAP</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(residenza.getCap())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Luogo</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(residenza.getDescrComune())%>&nbsp;</font></td>
      </tr>
       <tr>
        <td class="l"><font class="label">Comune Estero</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(residenza.getDescComuneEstero())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Stato</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(residenza.getDescrStato())%>&nbsp;</font></td>
      </tr>

    <br>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>

<tr>
<td class="lNoBord" colspan="2">
<% if(residenza.getCodTipoResidenza().equals("R"))
  { %>
<FORM method="POST" name="Domicilio" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActLoadInserisciDomicilio&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=residenza.getSogIdSoggetto()%>">
      <br><INPUT class="bottone" type="submit" name="D" value="Prosegui" onclick="Javascript:Disabilita();">
  </FORM>
 <% }
    else
    { %>
<FORM method="POST" name="Sentenza" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadInserisciSentenza&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=residenza.getSogIdSoggetto()%>">
      <br><INPUT class="bottone" type="button" name="S" value="Prosegui" onclick="Javascript:Disabilita();">
  </FORM>
 <%}%>

</td>


<td class="lNoBord" colspan="2">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActLoadDettaglioResidenza&<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>=<%=residenza.getIdResidenza()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:Verify();">
 </FORM>

</td>
</tr>

<%}%>
  </table>
  
<%if(lTipoFunzione.equals("") || lTipoFunzione.equals("ritornodettaglio")) 
{%>
	<br>
	  <jsp:include page="/jsp/files/siap/sico/residenza/IncludeElencoResidenze.jsp"/>
	<br>  
<%}%>

  </body>
</html>