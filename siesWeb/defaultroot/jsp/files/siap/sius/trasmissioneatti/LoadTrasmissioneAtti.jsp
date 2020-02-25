<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.trasmissioneatti.action.ICostantiTrasmissioneAtti" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="tipoUfficioSius" scope="request" class="java.lang.String"/>
<jsp:useBean id="richStampa" scope="request" class="java.lang.String"/>

<html>
  <head>
    <script language="JavaScript1.2">
    </script>
    <title>[S.I.E.S.] - Trasmissione Atti per Competenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>

    <script language="JavaScript">
        var desktop;
        function ListaUffici(a_formname,a_fieldname)
        {
          desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }
    </script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data trasmissione.
      var data_trasm=document.LoadTrasmissioneAtti.<%=ICostantiTrasmissioneAtti.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+document.LoadTrasmissioneAtti.<%=ICostantiTrasmissioneAtti.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+document.LoadTrasmissioneAtti.<%=ICostantiTrasmissioneAtti.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;
      if (! ControllaData(data_trasm))
      {
        alert('Data trasmissione non valida');
        return false;
      }

//Controlliamo che non sia inferiore alla
//data di inserimento del Procedimento e non superiore alla data di sistema.


      // Controllo della data trasmissione <= data di sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

      if (! CompareDate(data_trasm, data_sistema))
      {
        alert('Data trasmissione > della data odierna');
        return false;
      }

      // Controllo della data arrivo in cancelleria <= data di trasmissione
      var data_arrivo='<%=DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd/MM/yyyy")%>'

      if (! CompareDate(data_arrivo, data_trasm ))
      {
        alert('Data trasmissione < Data arrivo in cancelleria ');
        return false;
      }

      // Controllo della data inserimento procedimento <= data di trasmissione
      var data_insproc='<%=DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataInserimento(), "dd/MM/yyyy")%>'

      if (! CompareDate(data_insproc, data_trasm ))
      {
        alert('Data trasmissione < Data inserimento procedimento ');
        return false;
      }
   return true;
    }
    </script>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Trasmissione Atti per Competenza</font>
        </td>
      </tr>
    </table>

  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadTrasmissioneAtti'>

    <jsp:include page="<%=ICostantiTrasmissioneAtti.PG_LOAD_DETTAGLIOPROCEDIMENTOSIUS%>"/>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value='siap.sius.trasmissioneatti.action.ActTrasmissioneAtti'>
    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Data trasmissione </td>
        <td class="L">
          <input type="text" name="<%= ICostantiTrasmissioneAtti.CAMPO_GIORNO_DATA_TRASMISSIONE %>" maxlength="2" size="2" value=<%=DateUtils.getSysDate("dd")%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="<%= ICostantiTrasmissioneAtti.CAMPO_MESE_DATA_TRASMISSIONE %>" maxlength="2" size="2" value=<%=DateUtils.getSysDate("MM")%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="<%= ICostantiTrasmissioneAtti.CAMPO_ANNO_DATA_TRASMISSIONE %>" maxlength="4" size="4" value=<%=DateUtils.getSysDate("yyyy")%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Ufficio Destinatario : </td>
      </tr>

      <tr>
        <td class="l">Tipo </td>
        <td class="L">
          <select title="tipoUfficioSius" class=small name="<%=ICostantiTrasmissioneAtti.CAMPO_COD_TIPO_UFFICIO%>" >
            <%= tipoUfficioSius %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede </td>
        <td class="l">
          <input Title="Sede Ufficio" type="text" maxlength="35" size="35" name="<%=ICostantiTrasmissioneAtti.CAMPO_DESCR_COMUNE_UFFICIO %>">
            <a href="Javascript:ListaUffici('LoadTrasmissioneAtti','<%= ICostantiTrasmissioneAtti.CAMPO_DESCR_COMUNE_UFFICIO %>');">
            <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>

    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Note</td>
          <td class="l"><Textarea Title="Note" name="<%= ICostantiTrasmissioneAtti.CAMPO_NOTE %>" cols=80 rows=5></textarea></td>
        </td>
      </tr>
    </table>

    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.trasmissioneatti.action.ActTrasmissioneAtti" >

  </form>
  </body>
</html>