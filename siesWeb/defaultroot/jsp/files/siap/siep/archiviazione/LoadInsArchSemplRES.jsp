<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>


<%
  //============================================================================
  // Form per l'Archiviazione semplificata dei Procedimenti iscritti RES
  //============================================================================
%>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Definizione Procedimento Semplificata RES</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;
      function Verify()
      {
        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }

        return true;
      }  

    </script>
  </head>
  
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
          <font  class="label">Funzione :&nbsp;</font>
          <font class="campo">Archiviazione procedimento migrato</font>
        </td>
      </tr>
    </table>

    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciArchSemplificataRES">

      <table width="100%">
        <tr>
          <td class="l" >Data Definizione </td>
          <td class="l">
            <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" 
                   value="<%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" 
                   value="<%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"MM")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" 
                   value="<%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"yyyy")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        <tr>
          <td class="l">Note Procedimento</td>
          <td class="l">
            <textarea cols="40" rows="5" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(fascicolo.getNote()).trim()%></textarea>
          </td>
        </tr>
      </table>
      
      <table>
        <tr>
          <td class="lNoBord" colspan="2">
            <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
          </td>
        </tr>
      </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>  
</body>
</html>