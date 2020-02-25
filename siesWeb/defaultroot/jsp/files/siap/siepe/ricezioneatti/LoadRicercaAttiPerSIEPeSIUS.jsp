<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siepe.ricezioneatti.action.ICostantiRicezioneAtti"%>

<jsp:useBean id="tipoAtto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="statoRicezione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.P.E.] - R - Ricerca Atti Per Tipo e Date</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  	function init()
  	{
    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>.focus();
  	}
  	function pulisciSIUS()
  	{
    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>.value= '';
    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>.value= '';
  	}
  	function pulisciSIEP()
  	{
    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>.value= '';
    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP%>.value= '';
  	}

    function Verify()
    {
      // Non è possibile specificare solo il numero o solo l'anno SIUS
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length != 0)
         && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIUS");
    		document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>.focus();
        return false;
      }
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length == 0)
           && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length != 0) )
      {
        alert("Valorizzare Numero SIUS");
	    	document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>.focus();
        return false;
      }

      // Non è possibile specificare solo il numero o solo l'anno SIEP
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length != 0)
         && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIEP");
    		document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>.focus();
        return false;
      }
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length == 0)
           && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length != 0) )
      {
        alert("Valorizzare Numero SIEP");
    		document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>.focus();
        return false;
      }

      // Non è possibile specificare solo il Tipo autorità
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>.value.length > 1)
         && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length == 0) )
      {
        alert("Valorizzare la Sede Autorità");
        return false;
      }
      if( (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>.value.length < 2)
           && (document.LoadRicercaAttiPerSIEPeSIUS.<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length < 2) )
      {
        alert("Valorizzare il Tipo Autorità");
        return false;
      }

  		return true;
    }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

	<script language="JavaScript">
      var desktop;
      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
  </script>
	
</head>
  <body class="corpo" onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Atti per Procedimento SIEP/SIUS</font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaAttiPerSIEPeSIUS'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.ricezioneatti.action.ActRicercaAttiPerSIEPeSIUS">

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Anno/Numero SIUS </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIUS"  type="text" name="<%= ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onchange="Javascript:pulisciSIEP();">
          /<input Title="Numero SIUS" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>

      <tr>
        <td class="label" colspan="2">Oppure </td>
      </tr>

      <tr>
        <td class="l">Anno/Numero SIEP </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIEP"  type="text" name="<%= ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onchange="Javascript:pulisciSIUS();">
          /<input Title="Numero SIEP" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Tipo Autorità Mittente <font class=ob>(*)</font></td>
        <td class="L">
          <select title="tipoUfficioSIUS" name="<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>" >
            <%= tipoUfficioSIUS %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Autorità Mittente <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaAttiPerSIEPeSIUS','<%= ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO %>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>

    </table>
    <BR>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Cliccabile" colspan="2">
          Seleziona le caratteristiche dell'atto ricevuto
        </td>
      </tr>
      <tr>
        <td class="l">Tipo Atto</td>
        <td class="l">
          <select title="tipoAtto" name="<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_ATTO%>" >
            <%= tipoAtto %>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Stato Ricezione</td>
        <td class="l">
          <select title="statoRicezione" name="<%=ICostantiRicezioneAtti.CAMPO_STATO_RICEZIONE%>" >
            <%= statoRicezione %>
          </select>
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    </form>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadRicercaAttiPerSIEPeSIUS");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIEP%>","numeric");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIEP%>","numeric");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_PROGR_FASCICOLO_SIUS%>","numeric");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_ANNO_FASCICOLO_SIUS%>","numeric");
    </script>

  </body>
</html>