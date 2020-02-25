<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.esperto.model.EspertoModel"%>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>

<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="acdest"      scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato"  scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="esperto"     scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="utente"     scope="request" class="siap.sico.utente.model.UtenteModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Magistrato Relatore</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    function CambiaTipo()
    {
       if (document.LoadInserisciMagistratoRelatore.tipo[1].checked)
        {
        document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value="";
        document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_NOME%>.value="";
        document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = "";
        }
       if (document.LoadInserisciMagistratoRelatore.tipo[0].checked)
        {
        document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_COGNOME%>.value="";
        document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_NOME%>.value="";
        document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>.value ="";
        }
     }
  </script>

  <script language="JavaScript">
    // Chiamata liste
    function chiamaLista(a_formname)
    {
      var desktop;
      // Elenco esperti.
      if (document.LoadInserisciMagistratoRelatore.tipo[1].checked)
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.esperto.action.ActLoadRicercaEspertoLista&formname="+a_formname, "Ricerca_Esperto","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      // Elenco magistrati.
      if (document.LoadInserisciMagistratoRelatore.tipo[0].checked)
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    function chiamaListaSingola(a_formname)
    {
      var desktop;
      // Elenco magistrati.
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
  </script>

  <script language="JavaScript">
    // Funzione dei controlli formali della form
    function Verifica()
    {
      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value == ""
          && document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value == "" )
          {
          alert('Scegliere il Magistrato Relatore selezionando un magistrato o un esperto dalla lista!');
          return false;}

      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != CodMag
          && document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != "")
        {
        document.LoadInserisciMagistratoRelatore.magistratoMod.value='YES';
        }

      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value != IdEsp
          && document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value != "")
        {
        document.LoadInserisciMagistratoRelatore.espertoMod.value='YES';
        }

      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value == IdEsp
          && document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value == CodMag)
          {
          if (document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_ID_ESPERTO %>.value != ""
              || document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != "")
              {
              alert('Il Magistrato Relatore selezionato è uguale a quello già assegnato!');
              return false;
              }
          }
      return true;
    }
  </script >

  </head>

  <body class="corpo">
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;

      <%
        String lAzione = new String();
        if( modalita.equals("I") )
			  {
          lAzione = "siap.sius.magistratorelatore.action.ActInserisciMagistratoRelatore";
      %>
		    <font class="campo">Assegnazione/Cambio Magistrato Relatore</font>
      <%
        }
      //  else if( modalita.equals("M") )
      //  {
      %>

      <%
       // }
      %>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </td>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
  <FORM method="POST" name="LoadInserisciMagistratoRelatore" action="<%= IWebConstants.PG_MAIN%>">

    <table cellspacing=2 cellpadding=2 width="100%">
    <tr><td>&nbsp;</td></tr>

    <tr>
<% if(utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0 || utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDSM") == 0 )
   {%>
      <td class="l"><input type="radio" name="tipo" value="Magistrato" onClick="javascript:CambiaTipo();">Magistrato</td>
<% }else{%>
      <td class="l">Magistrato</td>
<% }%>
      <td class="L" >
        <input readonly  title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome())%>"  type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
        <input readonly  title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getNome())%>"     type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"    maxlength="35" size="25">
      </td>
    </tr>

<% if(utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0 || utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDSM") == 0 )
   {%>
    <tr>
      <td class="l"><input type="radio" name="tipo" value="Esperto" onClick="javascript:CambiaTipo();">Esperto</td>
      <td class="L" >
        <input readonly  title="Cognome Esperto" value="<%=StringUtils.toStringJSP(esperto.getCognome())%>"  type="text" name="<%=ICostantiEsperto.CAMPO_COGNOME%>" maxlength="35" size="25">
        <input readonly  title="Nome Esperto"    value="<%=StringUtils.toStringJSP(esperto.getNome())%>"     type="text" name="<%=ICostantiEsperto.CAMPO_NOME%>"    maxlength="35" size="25">
      </td>
    </tr>
<% }%>

<% if(utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0 || utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDSM") == 0 )
   {%>
    <tr>
      <td class="L" >
        <a href="Javascript:chiamaLista('LoadInserisciMagistratoRelatore');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
<% }else{%>
    <tr>
      <td class="L" >
        <a href="Javascript:chiamaListaSingola('LoadInserisciMagistratoRelatore');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
<% }%>

    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
      </td>
    </tr>
  </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"                 value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>"          value="<%=StringUtils.toStringJSP(esperto.getIdEsperto() )%>">
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"   value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" >
    <input type="HIDDEN" name="magistratoMod"  value="NO">
    <input type="HIDDEN" name="espertoMod"     value="NO">
    <input type="HIDDEN" name="acdest"         value="<%=acdest%>">
  </form>

  <script language="JavaScript">
      //Codice eseguito sempre
      var CodMag  = "<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>";
      var IdEsp   = "<%=StringUtils.toStringJSP(esperto.getIdEsperto() )%>";

<% if(utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0 || utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDSM") == 0 )
{%>

      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiEsperto.CAMPO_COGNOME%>.value != "")
        document.LoadInserisciMagistratoRelatore.tipo[1].checked = true;

      if (document.LoadInserisciMagistratoRelatore.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value != "")
        document.LoadInserisciMagistratoRelatore.tipo[0].checked = true;
<%}%>
  </script>
  </body>
  </html>