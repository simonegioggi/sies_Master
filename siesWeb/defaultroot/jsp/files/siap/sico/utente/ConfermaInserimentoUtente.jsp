<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.utente.model.UtenteViewModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>

<jsp:useBean id="utente"  scope="request" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="utenti"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="titolo"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tit"     scope="request" class="java.lang.String"/>
<jsp:useBean id="profilo" scope="request" class="siap.sico.profilo.model.ProfiloModel"/>

<%
//==============================================================================
// Form per la visualizzazione di Utenti Omonimi in caso di Inserimento o 
// Modifica utente
//==============================================================================
%>
  <%
  String Dprf = profilo.getDescrizione();
  String GFV="";
  String MFV="";
  String AFV="";
  String DFV="";
  if (utente.getDataFineValidita()!=null)
  {
    GFV=DateUtils.getDayToString(utente.getDataFineValidita());
    MFV=DateUtils.getMonthToString(utente.getDataFineValidita());
    AFV=DateUtils.getYearToString(utente.getDataFineValidita());
    DFV=DateUtils.getDateToString(utente.getDataFineValidita(),"dd/MM/yyyy");
  }else
    DFV="-";
  %>
  
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Utente </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Conferma <%= titolo %> Utente</font>
      </td>
      <td>
   		<!-- BOTTONE DI RITORNO -->
   		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>   
	  </td>
    </tr>
  </table> 
  <br>
<div style="width: 100%;">
<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name=f>
  <input type="hidden" value="siap.sico.utente.action.Act<%=titolo%>Utente" name="<%=IWebConstants.ACTION_FIELD%>">

  <input type="hidden" name="cod_prf" value="<%=utente.getUserProfile().getProfileId()%>">
  <input type="hidden" name="<%=ICostantiUtente.CAMPO_GIORNO_DATA_FINE_VALIDITA%>" value="<%= GFV %>">
  <input type="hidden" name="<%=ICostantiUtente.CAMPO_MESE_DATA_FINE_VALIDITA%>" value="<%= MFV %>">
  <input type="hidden" name="<%=ICostantiUtente.CAMPO_ANNO_DATA_FINE_VALIDITA%>" value="<%= AFV %>">
  <input type="hidden" name="flag" value="si">
  
  <table cellpadding="2" cellspacing="2">
    <!-- ==================================================================  -->
    <!--               LISTA UTENTI OMONIMI SULL'UFFICIO                     -->
    <!-- ==================================================================  -->
    <tr>
      <td class="LBG" colspan="3"><font class="campo">Lista Utenti Omonimi</font></td>
      <td colspan="6">&nbsp;</td>
    </tr>
    <tr>
      <td class="int">Codice Utente</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Telefono</td>
      <td class="int">Fax</td>
      <td class="int">E-mail</td>
      <td class="int">Ufficio</td>
      <td class="int">Profilo</td>
      <td class="int">Data Fine Validità</td>
    </tr>

    <%
    UtenteViewModel ut=new UtenteViewModel();
    for (int i=0;i<utenti.size();i++)
    {
      ut = (UtenteViewModel)utenti.get(i);
    %>
    <tr>
      <td class="l"><font class="campoSmall"><%=ut.getUserId() %></font></td>
      <td class="l"><font class="campoSmall"><%=ut.getCognome() %></font></td>
      <td class="l"><font class="campoSmall"><%=ut.getNome() %></font></td>
      <td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(ut.getUtTelefono(),"-") %></font></td>
      <td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(ut.getUtFax(),"-") %></font></td>
      <td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(ut.getUtEmail(),"-")%></font></td>
      <td class="l"><%=ut.getDescTipoUfficio()%> DI <font color=navy> <%=ut.getComuneUfficio() %></font></td>
      <td class="l"><%=ut.getDescProfilo()%></td>
      <td class="c"><font class="campoSmall"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ut.getDataFineValidita(),"dd-MM-yyyy"),"-")%></font></td>
    </tr>
    <% } %>
    
    <tr>
      <td colspan="6">&nbsp;<br><br></td>
    </tr>
    
    
    <!-- ==================================================================  -->
    <!--                       UTENTE DA MODIFICARE                          -->
    <!-- ==================================================================  -->
    <tr>
      <td class="LBG" colspan="3"><font class="campo">Utente da <%= tit %></font></td>
      <td colspan="6">&nbsp;</td>
    </tr>
    <tr>
      <td class="int" width="12%">Codice Utente</td>
      <td class="int" width="12%">Cognome</td>
      <td class="int" width="12%">Nome</td>
      <td class="int" width="8%">Telefono</td>
      <td class="int" width="8%">Fax</td>
      <td class="int" width="8%">E-mail</td>
      <td class="int">Ufficio</td>
      <td class="int">Profilo</td>
      <td class="int">Data Fine Validità</td>
    </tr>
    <tr>
      <td class="l">
        <input name="<%=ICostantiUtente.CAMPO_COD_UTENTE%>" VALUE="<%=utente.getUserId() %>" type="hidden">
        <font class="campoSmall">
          <%=utente.getUserId() %>
        </font>
      </td>
      <td class="l">
        <input name="<%=ICostantiUtente.CAMPO_COGNOME%>" type="Hidden" value="<%=utente.getCognome() %>">
        <font class="campoSmall">
          <%=utente.getCognome() %>
        </font>
      </td>
      <td class="l">
        <input type="Hidden" name="<%=ICostantiUtente.CAMPO_NOME%>" value="<%=utente.getNome() %>">
        <font class="campoSmall">
          <%=utente.getNome() %>
        </font>
      </td>
      <td class="c">
        <input type="Hidden" name="<%=ICostantiUtente.CAMPO_TELEFONO%>" value="<%=utente.getTelefono() %>">
        <font class="campoSmall">
          <%=StringUtils.toStringJSP(utente.getTelefono(),"-") %>&nbsp;
        </font>
      </td>
      <td class="c">
        <input type="Hidden" name="<%=ICostantiUtente.CAMPO_FAX%>" value="<%=utente.getFax() %>">
        <font class="campoSmall">
          <%=StringUtils.toStringJSP(utente.getFax(),"-") %>&nbsp;
        </font>
      </td>
      <td class="c">
        <input type="Hidden" name="<%=ICostantiUtente.CAMPO_E_MAIL%>" value="<%=utente.getEmail() %>">
        <font class="campoSmall">
          <%=StringUtils.toStringJSP(utente.getEmail(),"-") %>&nbsp;
        </font>
      </td>
      <td class="l">
        <input type="Hidden" name="cod_uff" value="<%=utente.getUfficioUtente().getCodUfficio() %>">
        <%=ut.getDescTipoUfficio()+" DI <font color=navy>"+ut.getComuneUfficio() %>
      </td>
      
      <td class="l">
        <%=Dprf%>
      </td>
      <td class="c"><%= DFV %></td>
    </tr>
    
    <tr>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--
      	<td colspan="2">
        	<input type="button" value="Torna alla lista Utenti Attivi" class="bottone" onclick="Javascript:document.location.href='<%= IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.utente.action.ActListaUtenti"%>';">
      	</td>
      	--%>
      	<td>
      		<input type="submit" class=bottone name="go" value="<%= titolo %>">
      	</td>
      	<td colspan="6">&nbsp;</td>
	</tr>
</table>

    </form>
</div>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");
</script>
</body>
</html>