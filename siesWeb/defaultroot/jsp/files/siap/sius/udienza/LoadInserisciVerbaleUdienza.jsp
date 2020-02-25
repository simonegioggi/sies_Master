<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>

<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>


<jsp:useBean id="avvocato"    scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato"  scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="esperto"     scope="request" class="siap.sius.esperto.model.EspertoModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">

    // Funzione dei controlli formali della form
    function Verifica()
    {
       if (document.LoadInserisciVerbaleUdienza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value == ""
          && document.LoadInserisciVerbaleUdienza.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value == "" )
          {
          alert('Il Magistrato Relatore è obbligatorio!');
          return false;
          }

       return true;
    }
  </script >

</head>
<%
      String lAzione = new String();
      lAzione = "siap.sius.udienza.action.ActInserisciVerbaleUdienza";
%>

<body class="corpo"
  >

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Inserimento Verbale Udienza</font>
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciVerbaleUdienza">
    <table width="100%" cellspacing=2 cellpadding=2>

    <tr>
      <td class="Titolo" colspan=3 width="100%"> Magistrato Relatore </td>
    </tr>

	<tr>
		<td class="L" colspan=3>
        	<%=StringUtils.toStringJSP(magistrato.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(magistrato.getNome())%>
      	</td>
	</tr>
    <tr>
      <td class="L" colspan=3>
        <%=StringUtils.toStringJSP(esperto.getCognome())%>
        <%=StringUtils.toStringJSP(esperto.getNome())%>
      </td>
    </tr>

    <tr>
      <td class="L" colspan=3>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.magistratorelatore.action.ActLoadInserisciMagistratoRelatore&acdest=siap.sius.udienza.action.ActLoadInserisciVerbaleUdienza">
          Assegnazione/Cambio Magistrato Relatore
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    </table >

      <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="pippo"/>
      </jsp:include>

    <table cellspacing=2 cellpadding=2 width="100%">

    <tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"                 value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>"        value="">
    <input type="HIDDEN" name="<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>"          value="<%=StringUtils.toStringJSP(esperto.getIdEsperto() )%>">
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"   value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" >

  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciVerbaleUdienza");
  </script>

  </body>
</html>