<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siepe.ricezioneatti.action.ICostantiRicezioneAtti"%>

<jsp:useBean id="tipoAtto"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="statoRicezione"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIEPE" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        	scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.P.E.] - R - Ricerca Atti UEPE</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
  	function init()
  	{
    	document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.focus();
  	}
  	/* Commentato, da decommentare per uso di campi Anno, prog, num UEPE
  	function pulisciSIEPE()
  	{
    	document.LoadRicercaAttiUEPE.<%--=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE--%>.value= '';
    	document.LoadRicercaAttiUEPE.<%--=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE--%>.value= '';
  	}
		*/
    function Verify()
    {
      // Non è possibile specificare solo il numero o solo l'anno UEPE
      if( (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.value.length != 0)
         && (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIEPE");
    		document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.focus();
        return false;
      }

      if( (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>.value.length == 0)
           && (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.value.length != 0) )
      {
        alert("Valorizzare Progressivo SIEPE");
	    	document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>.focus();
        return false;
      }

      // Non è possibile specificare solo il numero o solo l'anno SIEPE
      if( (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>.value.length != 0)
         && (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.value.length == 0) )
      {
        alert("Valorizzare Anno UEPE");
    		document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>.focus();
        return false;
      }
      // Non è possibile specificare solo il Tipo autorità
      if( (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>.value.length > 1)
         && (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length == 0) )
      {
        alert("Valorizzare la Sede Autorità");
        return false;
      }
      // Esegue il controllo per la valorizzazione Tipo Autorità
      if( (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>.value.length < 2)
         && (document.LoadRicercaAttiUEPE.<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length < 2) )
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
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Atti SIEPE</font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaAttiUEPE'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.ricezioneatti.action.ActRicercaAttiUEPE">

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">SIEPE (Anno/Progressivo)</td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIEPE"  type="text" name="<%= ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Progressivo SIEPE" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Tipo Autorità Mittente <font class=ob>(*)</font> </td>
        <td class="L">
          <select title="tipoUfficioSIEPE" name="<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_UFFICIO%>" >
            <%= tipoUfficioSIEPE %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Autorità Mittente <font class=ob>(*)</font> </td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaAttiUEPE','<%= ICostantiRicezioneAtti.CAMPO_DESCR_COMUNE_UFFICIO %>');">
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

      var frmvalidator  = new Validator("LoadRicercaAttiUEPE");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_ANNO_SIEPE%>","numeric");

    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    	 frmvalidator.addValidation("<%=ICostantiRicezioneAtti.CAMPO_CHIAVE_PROGR_SIEPE%>","numeric");

    </script>

  </body>
</html>