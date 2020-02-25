<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.regesies.regenotiziareato.action.ICostantiRegeNotiziaReato" %>
<%@ page import="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel" %>

<%@ page import="siap.regesies.action.ICostantiRegeSies" %>

<jsp:useBean id="regenotiziareato" scope="request" class="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel" />
<jsp:useBean id="Acquisizione" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio notizia reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function Verify()
      {
      var d1=document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_FATTO%>.value;

      if(d1 != '//')
      {
	      if (! ControllaData(d1))
	      {
	        alert('Data Fatto non valida');
	        return false;
	      }
      }
      
      //Data Acquisizione
      var d2=document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>.value;

      if(d1 != '//')
      {      
	      if (! ControllaData(d2))
	      {
	        alert('Data Acquisizione non valida');
	        return false;
	      }
	  }
	  
      //Data Pervenimento
      var d3=document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadModificaRegeNotiziaReato.<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
      
      if(d1 != '//')
      {      
	      if (! ControllaData(d3))
	      {
	        alert('Data Pervenimento non valida');
	        return false;
	      }
	  }
	  
      return true;
      }
    </script>

  </head>
<body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Modifica Rege Notizie di Reato</font></td>
        </td>
          <%RegeNotiziaReatoModel lRegeNotiziaReato = regenotiziareato;
          String lAzione = "siap.regesies.regenotiziareato.action.ActModificaRegeNotiziaReato";%>
        <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeNotiziaReato.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lRegeNotiziaReato.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeNotiziaReato.CAMPO_PROGR_NOTIZIA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lRegeNotiziaReato.getProgrNotizia()%>" />
          </jsp:include>
        </td>
    </tr>
  </table>
</form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/></td>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaRegeNotiziaReato">


<table  cellspacing=2 cellpadding=2>

    <tr>
        <td class="l">Data Pervenimento</td>

        <td class="l"><input title="Giorno data pervenimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataPervenimento(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  >
        <input title="Mese data pervenimento"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataPervenimento(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO %>"  >
        <input title="Anno data pervenimento"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataPervenimento(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO %>"  ></td>
    </tr>
    <tr>
        <td class="l">Acquisizione Diretta</td>
        <td class="l"><select name="<%=ICostantiRegeNotiziaReato.CAMPO_ACQUISIZIONE_DIRETTA %>">
        <%=Acquisizione %>
      </select>        </td>
    </tr>
    <tr>
        <td class="l">Data Fatto</td>
        <td class="l"><input title="Giorno data fatto" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataFatto(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_FATTO %>"  >
        <input size="2" title="Mese data fatto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataFatto(),"MM")) %>" type="text" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_FATTO %>"  >
        <input size="4" title="Anno data fatto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataFatto(),"yyyy")) %>" type="text" maxlength="4" name="<%= ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_FATTO %>"  ></td>
    </tr>
    <tr>
        <td class="l">Descrizione Fonte</td>

        <td class="l"><input size="35" title="Descrizione Fonte"  maxlength="30" value="<%=StringUtils.toStringJSP(lRegeNotiziaReato.getDescrizioneFonte(),"") %>" type="text" name="<%= ICostantiRegeNotiziaReato.CAMPO_DESCRIZIONE_FONTE %>"  ></td>
    <tr>
        <td class="l">Comune Fonte</td>
        <%
        String lComune = lRegeNotiziaReato.getDescrComuneFonte();
        if(lComune.equals("-"))
            lComune="";
        %>
        <td class="l"><input size="35" title="Comune Fonte"  maxlength="35" value="<%=lComune%>" type="text" name="<%= ICostantiRegeNotiziaReato.CAMPO_COD_COMUNE_FONTE %>"  >
    <a href="Javascript:ListaComuni('LoadModificaRegeNotiziaReato','<%=ICostantiRegeNotiziaReato.CAMPO_COD_COMUNE_FONTE%>');">
        <img src="/images/filefolder.gif" border=0>
      </a></td>
      </tr>

    <tr>
        <td class="l">Num. Reg. Autorita</td>
        <td class="l"><input title="Num. Reg. Autorita"   value="<%=StringUtils.toStringJSP(lRegeNotiziaReato.getNumRegAutorita(),"")%>" type="text" name="<%= ICostantiRegeNotiziaReato.CAMPO_NUM_REG_AUTORITA %>"  ></td>
    </tr>
    <tr>
        <td class="l">Luogo Provenienza</td>
        <td class="l"><input  title="Luogo Provenienza"  size="35" maxlength="100" value="<%=StringUtils.toStringJSP(lRegeNotiziaReato.getLuogoProvenienza()) %>" type="text" name="<%= ICostantiRegeNotiziaReato.CAMPO_LUOGO_PROVENIENZA %>"  ></td>
    </tr>
    <tr>
        <td class="l">Data Acquisizione</td>

        <td class="l">
        <input  title="Giorno Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataAcquisizione(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE %>"  >
        <input  title="Giorno Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataAcquisizione(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE %>"  >
        <input  title="Giorno Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lRegeNotiziaReato.getDataAcquisizione(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE %>"  ></td>
    </tr>
    <tr>
        <td class="l">Numero Ricevuta</td>
        <td class="l"><input  title="Numero Ricevuta" size="35" maxlength="100" value="<%=StringUtils.toStringJSP(lRegeNotiziaReato.getNumeroRicevuta()) %>" type="text" name="<%= ICostantiRegeNotiziaReato.CAMPO_NUMERO_RICEVUTA %>"  ></td>
    </tr>


</table>
 <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>


      <input type="HIDDEN" value="<%=lRegeNotiziaReato.getIdFile()%>" name="<%=ICostantiRegeNotiziaReato.CAMPO_ID_FILE%>">
      <input type="HIDDEN" value="<%=lRegeNotiziaReato.getProgrNotizia()%>" name="<%=ICostantiRegeNotiziaReato.CAMPO_PROGR_NOTIZIA%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">

      </td>
    </tr>
  </table>
  </form>
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaRegeNotiziaReato");

  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>", "minlength=2");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>", "minlength=2");

  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_FATTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_FATTO%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_FATTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_FATTO%>", "minlength=2");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>", "minlength=2");

  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>", "minlength=2");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>", "minlength=2");

  frmvalidator.addValidation("<%=ICostantiRegeNotiziaReato.CAMPO_COD_COMUNE_FONTE%>", "alphanumeric");

  frmvalidator.setAddnlValidationFunction("Verify");

 </script>
  </body>
</html>