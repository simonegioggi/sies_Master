<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.provvedimentopm.model.ProvvedimentoModel"%>
<%@ page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>

<jsp:useBean id="provvedimento" scope="request" class="siap.siep.provvedimentopm.model.ProvvedimentoModel"/>
<jsp:useBean id="modalita"      scope="request" class="java.lang.String"/>
<jsp:useBean id="posizione"     scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - GestioneProvvedimento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
	  {
		  if (document.f.<%=ICostantiProvvedimento.CAMPO_DATA_GG_EMISSIONE%>.value.length==1)
			  document.f.<%=ICostantiProvvedimento.CAMPO_DATA_GG_EMISSIONE%>.value='0'+document.f.<%=ICostantiProvvedimento.CAMPO_DATA_GG_EMISSIONE%>.value;
		  if (document.f.<%=ICostantiProvvedimento.CAMPO_DATA_MM_EMISSIONE%>.value.length==1)
			  document.f.<%=ICostantiProvvedimento.CAMPO_DATA_MM_EMISSIONE%>.value='0'+document.f.<%=ICostantiProvvedimento.CAMPO_DATA_MM_EMISSIONE%>.value;

		  var data_to_verify = document.f.<%=ICostantiProvvedimento.CAMPO_DATA_GG_EMISSIONE%>.value+'/'+document.f.<%=ICostantiProvvedimento.CAMPO_DATA_MM_EMISSIONE%>.value+'/'+document.f.<%=ICostantiProvvedimento.CAMPO_DATA_AAAA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }
	  }
  </script>

  </head>

  <body class="corpo">

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;

      <%
			  ProvvedimentoModel lProvvedimento = new ProvvedimentoModel();

        String lAzione = new String();

        if( modalita.equals("I") )
			  {
          lProvvedimento = new ProvvedimentoModel(provvedimento);
          lAzione = "siap.siep.provvedimentopm.action.ActInserisciProvvedimento";
      %>
			    <font class="campo">Inserimento Ordine di Esecuzione</font>
      <%
        }
        else if( modalita.equals("M") )
        {
          lProvvedimento = new ProvvedimentoModel(provvedimento);
			    lAzione = "siap.siep.provvedimentopm.action.ActModificaProvvedimento";
      %>
          <font class="campo">Modifica di Ordine di Esecuzione</font>
      <%
        }
      %>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.provvedimentopm.action.ActInserisciProvvedimento">
    <table>

      <tr>
          <td class="l">Posizione Giuridica</td>
          <td class="l">
            <font class="campo"><%=posizione.getDescrPosizioneGiuridica() %></font>
          </td>
      </tr>

      <tr>
          <td class="l">Destinatario</td>
          <td class="l">
          <select name="<%=ICostantiProvvedimento.AUTORITA_DESTINATARIO%>">
            <option value="Cancelleria" />Cancelleria
            <option value="Casellario Giudiziale" />Casellario Giudiziale
            <option value="Ministero Della Giustizia" />Ministero Della Giustizia
            <option value="Casa Circondariale" />Casa Circondariale
            <option value="Comando Stazione CARABINIERI "  />Comando Stazione CARABINIERI
            <option value="Questura" />Questura
          </select>
          </td>
      </tr>

      <tr>
          <td class="l">Sede Destinatario</td>
          <td class="l"><input type="text" name="<%= ICostantiProvvedimento.AUTORITA_SEDE %>" size = "35" ></td>
      </tr>

      <tr>
        <td class="l">Data Emissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimento.CAMPO_DATA_GG_EMISSIONE %>"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimento.CAMPO_DATA_MM_EMISSIONE %>"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiProvvedimento.CAMPO_DATA_AAAA_EMISSIONE %>"  >
        </td>
      </tr>

      <tr>
        <td class="l">Note</td>
        <td class="L">
          <TEXTAREA title="note" name="<%= ICostantiProvvedimento.CAMPO_NOTE %>"  cols=40 rows=5 >
          </textarea>
        </td>
		  </tr>

      <tr>
        <td class="lNoBord" colspan="2">
          <br><br>
          <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
        </td>
      </tr>

  </table>
	</form>
	</body>
</html>