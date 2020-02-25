<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>
<jsp:useBean id="tiporicerca"              scope="request" class="java.lang.String"/>
<html>

<head>
  <title> [S.I.E.S.] - Ricerca Udienze Magistrati Procedimenti Procedimento- </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>


 <script language="JavaScript">
  function Verify()
	{
          var ritorno = true;
          var dataUdienzaInizio = document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                      document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
                      document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;

          var dataUdienzaFine = document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>.value +'/'+
                      document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>.value + '/' +
                      document.LoadRicercaUdiMagProc.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>.value;


          if(dataUdienzaInizio.length <= 2 )
          {
            alert('Occorre inserire Data Iniziale');
            ritorno = false;
          }
          else if (!ControllaData( dataUdienzaInizio ) )
          {
            alert('Data Iniziale non valida');
            ritorno = false;
          }
          else if(dataUdienzaFine.length <= 2 )
          {
            alert('Occorre inserire Data Finale');
            ritorno = false;
          }
          else if (!ControllaData( dataUdienzaFine ) )
          {
            alert('Data Finale non valida');
            ritorno = false;
          }
          return ritorno;
	 }

  </script>
</head>

<body class="corpo" onLoad="document.forms['LoadRicercaUdiMagProc'].elements['<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>'].focus()">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaUdiMagProc">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.udienzaprocedimento.action.ActRicercaUdienzeMagistratiProcedimenti">
  <input type="HIDDEN" name="tiporicerca" value="<%=tiporicerca%>">

 <table>
    <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG ><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca numero procedimenti per magistrato relatore e per udienza
    </font></td>
    </tr>
  </table>
   <br>
  <table cellpadding=2 cellspacing=2 class="l">
   <tr> <td>
        <font class="label">
  Indicare intervallo date udienza:
        </font>
   </td></tr>
    <tr>
      <td class="l">
        <font class="label">
          Dalla data:
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>"   maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>"   maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
             </font>
      </td>
      <td class="l">
        <font class="label">
          Alla data:
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)"onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>"   maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input  value="" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>"   maxlength="4" size="4" onBlur="javascript:value=FillYear(value)" >
        </font>
      </td>
    </tr>
  </table>
   <br>
  <table>
   <tr>
      <td class="l">
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="Conferma" value="Conferma">
      </td>
   </tr>
  </table>
</form>
    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadRicercaUdiMagProc");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA_FINE%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA_FINE%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA_FINE%>","lt=3000");
    </script>
</body>
</html>