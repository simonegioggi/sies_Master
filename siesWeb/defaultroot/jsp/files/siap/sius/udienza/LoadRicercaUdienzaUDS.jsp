<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>

<jsp:useBean id="elencoPresidenti"     scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoProcuratori"     scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoAssistenti"     scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto riferimento al codice tipo ufficio --%>
<jsp:useBean id="codTipoUfficio" scope="request" class="java.lang.String"/>

<html>

<head>
  <title> [S.I.E.S.] - Ricerca Udienza UDS - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  function Verify()
        {
                var dataUdienzaInizio = document.f.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                            document.f.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
                            document.f.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;

               /* alert(dataUdienzaInizio);*/
                var dataUdienzaFine   = document.f.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>.value +'/'+
                            document.f.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>.value + '/' +
                            document.f.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>.value;
              /*  alert(dataUdienzaFine); */


                if (!ControllaData( dataUdienzaInizio )  && dataUdienzaInizio.length > 2 )
                {
                  alert('Data inizio Udienza  non valida');
                  return false;
                }

                if (!ControllaData( dataUdienzaFine ) && dataUdienzaFine.length > 2 )
                {
                  alert('Data Udienza fine non valida');
                  return false;
                }
       return true;
         }
  </script>
</head>

<body class="corpo">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.udienza.action.ActRicercaUdienzaUDS">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Udienza UDS</font></td>
    </tr>
  </table>

  <br>

  <table cellpadding=2 cellspacing=2>
      <tr>
      <td class="l">N.ro Udienza</td>
      <td class="l">
        <select title="collegio" name="<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>" >
           <option value = "" >
           <option value = "1" > 1
           <option value = "2" > 2
           <option value = "3" > 3
           <option value = "4" > 4
           <option value = "5" > 5
           <option value = "6" > 6
           <option value = "7" > 7
           <option value = "8" > 8
           <option value = "9" > 9
           <option value = "10" > 10
           <option value = "11" > 11
           <option value = "12" > 12
           <option value = "13" > 13
           <option value = "14" > 14
           <option value = "15" > 15
        </select>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">
          Dalla data udienza
        </font>
      </td>
      <td class="l">
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input value="" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="L">
        <font class="label">
          Alla data udienza
        </font>
      </td>
      <td class="l">
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input value="" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Magistrato</td>
      <td class="l">
        <select title="Presidente" name="<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>"   >
          <%= elencoPresidenti %>
        </select>
      </td>
    </tr>


    <tr>
    	<%-- MEV10-s3: cambiata etichetta (ex Procuratore Repubblica) in funzione dell'utenza collegata --%>
    	<% if ("UDSM".equals(codTipoUfficio)) { %>
      		<td class="l">Procuratore della Repubblica presso il Tribunale dei Minorenni</td>
      	<% } else { %>
      		<td class="l">Procuratore della Repubblica</td>
      	<% } %>
     	<td class="l">
	        <select title="ProcuratoreG" name="<%=ICostantiUdienza.CAMPO_COD_PG%>"  >
	          	<%= elencoProcuratori %>
	        </select>
      	</td>
    </tr>

    <tr>
      <td class="l">Assistente</td>
      <td class="l">
        <select title="Assistente" name="<%=ICostantiUdienza.CAMPO_COD_ID_ASSISTENTE%>"   >
          <%= elencoAssistenti %>
        </select>
      </td>
    </tr>

      <!--
         Gestione radio buttons per tipo di ordinamento :
         -> D-C ( Ordinamento per Data Udienza e Collegio ) default
         -> C-D ( Ordinamento per Collegio e Data Udienza )
       -->
      <tr>
        <td class="l">
            Ordina udienze per data e collegio &nbsp;
        </td>
        <td class="l">
          <input type=radio name="<%=ICostantiUdienza.CAMPO_TIPOORDINAMENTO%>" value="D-C" checked ></td>
        </td>
      </tr>

      <tr>
        <td class="l">
            Ordina udienze per collegio e data &nbsp;
        </td>
        <td class="l">
          <input type=radio name="<%=ICostantiUdienza.CAMPO_TIPOORDINAMENTO%>" value="C-D"></td>
        </td>
      </tr>

    <tr>
      <td colspan="2">
        <br><br>
          <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

  </table>
  </form>
    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
</body>
</html>