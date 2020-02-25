<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.unificazione.action.ICostantiUnificazione"%>

<jsp:useBean id="tipoUfficioSIUS" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Unificazione Soggetti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    var desktop;
    // Lista TDS/UDS
    function ListaTDS_UDS(a_formname,a_fieldname,a_fieldcode)
    {
      if (a_fieldcode == 'CodTipoUfficioDaUnif')
      {
        var valore = document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF%>.value;
        var i = document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF%>.selectedIndex;
      }else{
        var valore = document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE%>.value;
        var i = document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE%>.selectedIndex;
      }
      if ( i == 1 )
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    }
  </script>

  <script language="JavaScript">
  function Verify()
  {
    // Controllo di uguaglianza tra i 2 soggetti.
    var anno_da_unif=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_ANNO_DA_UNIF%>.value;
    var anno_unificante=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE%>.value;
    var numero_da_unif=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF%>.value;
    var numero_unificante=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE%>.value;
    var TipoUff_da_unif=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF%>.value;
    var TipoUff_unificante=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE%>.value;
    var SedeUff_da_unif=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF%>.value;
    var SedeUff_unificante=document.LoadUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_UNIFICANTE%>.value;

    // Controllo uguaglianza procedimenti "Da Unificare" e "Unificante".
    if ( anno_da_unif == anno_unificante        &&
         numero_da_unif == numero_unificante    &&
         TipoUff_da_unif == TipoUff_unificante  &&
         SedeUff_da_unif == SedeUff_unificante )
    {
      alert('Impossibile Unificare un Soggetto con se stesso!');
      return false;
    }
    return true;
  }
  </script>

</head>

<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Unificazione  Soggetti</font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadUnificazioneSoggetti'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.unificazione.action.ActLoadPreUnificazioneSoggetti">
    <table cellspacing=2 cellpadding=2 width=60%>

      <tr>
        <td class="LBG" colspan="2" >
          <font class="label">Estremi del Procedimento relativo al Soggetto da Unificare </font><font class=ob>(*)</font>&nbsp;
        </td>
      </tr>

      <tr>
        <td class="l">Anno e Numero </td>

        <td class="l">
          <input Title="AnnoDaUnif" type="text" name="<%= ICostantiUnificazione.CAMPO_ANNO_DA_UNIF %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" maxlength="4" size="4">
          /<input Title="NumeroDaUnif" type="text" name="<%= ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF %>" maxlength="6" size="6">
        </td>
      </tr>

      <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSIUS" class=small name="<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF%>" >
            <%= tipoUfficioSIUS %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede </font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaTDS_UDS('LoadUnificazioneSoggetti','<%= ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF %>', '<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_DA_UNIF%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>

    </table>
    <br>

    <table cellspacing=2 cellpadding=2 width=60%>
      <tr>
        <td class="LBG" colspan="2" >
          <font class="label">Estremi del Procedimento relativo al Soggetto Unificante </font><font class=ob>(*)</font>&nbsp;
        </td>
      </tr>

      <tr>
        <td class="l">Anno e Numero </td>

        <td class="l">
          <input Title="AnnoUnificante" type="text" name="<%= ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" maxlength="4" size="4">
          /<input Title="NumeroUnificante" type="text" name="<%= ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE %>" maxlength="6" size="6">
        </td>
      </tr>

      <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSIUS" class=small name="<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE%>" >
            <%= tipoUfficioSIUS %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede </font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_UNIFICANTE%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaTDS_UDS('LoadUnificazioneSoggetti','<%= ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_UNIFICANTE %>', '<%=ICostantiUnificazione.CAMPO_COD_TIPO_UFFICIO_UNIFICANTE%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" name="Ricerca" value="Conferma">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadUnificazioneSoggetti");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_DA_UNIF%>","req","Il campo Anno Procedimento del Soggetto da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_DA_UNIF%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_DA_UNIF%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF%>","req","Il campo Numero Procedimento del Soggetto da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF%>","req","Il campo Sede Ufficio del Procedimento riferito al Soggetto Da Unificare è obbligatorio");

    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE%>","req","Il campo Anno Procedimento del Soggetto Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE%>","req","Il campo Numero Procedimento del Soggetto Unificante è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiUnificazione.CAMPO_DESCR_COMUNE_UFFICIO_DA_UNIF%>","req","Il campo Sede Ufficio del Procedimento riferito al Soggetto Unificante è obbligatorio");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
  </body>
</html>