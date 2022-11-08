<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.regesies.regereato.model.RegeReatoModel"%>
<%@ page import="siap.regesies.regereato.action.ICostantiRegeReato"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="regereato" scope="request" class="siap.regesies.regereato.model.RegeReatoModel"/>
<jsp:useBean id="TipiReato" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Rege Reato </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>
<body class="corpo">
        <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
         <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
           <font class="campo">Modifica Rege Reato</font>
         </td>
</tr>
</table>
<br>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaRegeReato">
<%
  RegeReatoModel lReato = regereato;
  String lAzione = "siap.regesies.regereato.action.ActModificaRegeReato";

  //Se il reato è quello principale
  if(lReato.getProgrCircostanza() == 1)
  {
%>
    <table cellspacing=2 cellpadding=2>
      <td class="l">Numero Reato</td>
      <td class="c">
      <%if (lReato.getProgrNumeroManuale()!=null)
      {%>
        <input size=10 maxlength=10 value="<%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_PROGR_NUMERO_MANUALE %>">
     <%} else {%>
        <input size=10 maxlength=10 value="<%=lReato.getProgrReato()%>" type="text" name="<%= ICostantiRegeReato.CAMPO_PROGR_NUMERO_MANUALE %>">
    <%}%>
        </td>
    </table>
<%
  }
%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.qualificante</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
    <tr>
      <td class="l">
        <select name="<%= ICostantiRegeReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="l">
        <input size=4 maxlength=4 title="Anno" value="<%=StringUtils.intZerotoString(lReato.getAnnoFonte()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_ANNO_FONTE %>">
      </td>
      <td class="l">
        <input size=6 maxlength=6 title="Numero" value="<%=StringUtils.toStringJSP(lReato.getNumeroFonte()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="l">
        <input size=5 maxlength=5 title="Articolo" value="<%=StringUtils.toStringJSP(lReato.getArticolo()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_ARTICOLO %>">
      </td>
      <td class="l">
      <select name="<%=ICostantiRegeReato.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
      </td>
      <td class="l">
        <strong>C</strong><input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(lReato.getComma()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_COMMA %>">
      </td>
      <td class="l">
        <strong>L</strong><input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(lReato.getLettera()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_LETTERA %>">
      </td>
      <td class="l">
        <strong>N</strong><input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(lReato.getNumero()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_NUMERO %>">
      </td>
    </tr>
  </table>
<%
  //Se il reato è quello principale
  if(lReato.getProgrCircostanza() == 1)
  {
%>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo" colspan=4>Rege Reato</td></tr>
		<tr>
				<td class="l" width=20%>Tipo Reato</td>
				<td class="l">
        <select name="<%= ICostantiRegeReato.CAMPO_COD_TIPO_REATO %>"  >
          <%=TipiReato%>
        </select>
        </td>
		</tr>
    <tr>
				<td class="l">Luogo Reato</td>
				<td class="l"><input size=50 maxlength=300 value="<%=StringUtils.toStringJSP(lReato.getDescLuogo()) %>" type="text" name="<%= ICostantiRegeReato.CAMPO_DESC_LUOGO %>"  ></td>
		</tr>
    <tr>
				<td class="l">Periodo Consumazione</td>
				<td class="l">
        <select name="<%= ICostantiRegeReato.CAMPO_COD_PERIODO_CONSUMAZIONE %>"  >
          <%=PeriodoConsumazione%>
        </select>
        </td>
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">&lt;Data1&gt;</td>
      <td class="l">
<%
        String lStrGGInizio = StringUtils.intZerotoString( lReato.getGiornoInizio());
        if (lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.intZerotoString( lReato.getMeseInizio());
        if (lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.intZerotoString( lReato.getAnnoInizio());
%>
        <input size=2 maxlength=2 value="<%=lStrGGInizio%>" type="text" size="2" maxlength="2" name="<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=2 maxlength=2 value="<%=lStrMMInizio%>" type="text" size="2" maxlength="2" name="<%=ICostantiRegeReato.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=4 maxlength=4 value="<%=lStrAAInizio%>" type="text" size="4" maxlength="4" name="<%=ICostantiRegeReato.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
      <td class="l">&lt;Data2&gt;</td>
      <td class="l">
<%
        String lStrGGFine = StringUtils.intZerotoString( lReato.getGiornoFine());
        if (lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.intZerotoString( lReato.getMeseFine());
        if (lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.intZerotoString( lReato.getAnnoFine());
%>
        <input size=2 maxlength=2 value="<%=lStrGGFine%>" type="text" size="2" maxlength="2" name="<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=2 maxlength=2 value="<%=lStrMMFine%>" type="text" size="2" maxlength="2" name="<%=ICostantiRegeReato.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=4 maxlength=4 value="<%=lStrAAFine%>" type="text" size="4" maxlength="4" name="<%=ICostantiRegeReato.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
				<td class="l">Note</td>
				<td class="l"><TextArea cols=80 rows=5 name="<%= ICostantiRegeReato.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lReato.getNote()) %></textarea></td>
		</tr>
  </table>
<%
  }
%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>
      </td>
		</tr>
  </table>


      <input type="HIDDEN" value="<%=lReato.getIdFile()%>" name="<%=ICostantiRegeReato.CAMPO_ID_FILE%>">
      <input type="HIDDEN" value="<%=lReato.getProgrReato()%>" name="<%=ICostantiRegeReato.CAMPO_PROGR_REATO%>" >
      <input type="HIDDEN" value="<%=lReato.getProgrCircostanza()%>" name="<%=ICostantiRegeReato.CAMPO_PROGR_CIRCOSTANZA%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaRegeReato");

  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_FONTE%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_FONTE%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ARTICOLO%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_NUMERO_FONTE%>", "alphanumeric");

  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_COMMA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_LETTERA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_NUMERO%>", "alphanumeric");

<%
  //Se il reato è quello principale
  if(lReato.getProgrCircostanza() == 1)
  {
%>
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_INIZIO%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_INIZIO%>","lt=2099");

    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_GIORNO_DATA_FINE%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_MESE_DATA_FINE%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_FINE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiRegeReato.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
  }
%>
 </script>

 </body>
</html>