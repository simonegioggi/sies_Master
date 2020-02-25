<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.presaincarico.action.ICostantiPresaincarico"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>


<jsp:useBean id="tipo_provvedimento"  scope="request" class="java.lang.String"/>
<jsp:useBean id="function_name" scope="request" class="java.lang.String"/>
<jsp:useBean id="action_name" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Presa in Carico - Ricerca Provvedimenti SIUS</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    function Verify()
    {

      var data_inizio=document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;
      var data_fine=document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length != 0)
           && (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIEP");
        return false;
      }
      if( (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length == 0)
           && (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length != 0) )
      {
        alert("Valorizzare Progressivo SIEP");
        return false;
      }
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length != 0)
         && (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIUS");
        return false;
      }
      if( (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length == 0)
           && (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length != 0) )
      {
        alert("Valorizzare Progressivo SIUS");
        return false;
      }

      if( (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length == 0)
           && (document.LoadRicercaProvSius.<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>.value.length != 0) )
      {
        alert("Il campo Sede Ufficio è obbligatorio");
        return false;
      }
      if(ControllaData(data_inizio) && ControllaData(data_fine))
         if(!CompareDate(data_inizio,data_fine))
         {
           alert('La Data di termine non può precedere la data di inizio');
           return false;
         }

    return true;

    }
  </script>

  <script language="JavaScript">
      var desktop;
      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"> <%=function_name%> </font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaProvSius'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action_name%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE%>" value="<%=tipo_provvedimento%>">

   <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="label">Ufficio di provenienza del Provvedimento: </td>
      </tr>
    </table>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSIUS" class=small name="<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>" >
            <%= tipoUfficioSIUS %>
           <option value="" selected> TUTTI </option>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Ufficio </font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaProvSius','<%= ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO %>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>
   <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Numero SIEP(Anno/Progressivo) </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIEP "  type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP %>" maxlength="4" size="4">
          /<input Title="Numero SIEP " type="text" name="<%= ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP %>" maxlength="6" size="6">
        </td>
      </tr>
      <tr>
        <td class="l">Numero SIUS (Anno/Progressivo) </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIUS"  type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS %>" maxlength="4" size="4">
          /<input Title="Numero SIUS" type="text" name="<%= ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS %>" maxlength="6" size="6">
        </td>
      </tr>
    </table>
   <br>
    <table cellspacing=2 cellpadding=2>

        <tr>
          <td class="l">Dalla data di trasmissione (gg-mm-aaaa) </td>
          <td class="l" colspan ='2'>
            <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
             -
            <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
             -
            <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
        <tr>
          <td class="l">Alla data di trasmissione &nbsp; (gg-mm-aaaa) </td>
          <td class="l" colspan ='2'>
            <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
             -
            <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
             -
            <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
    </table>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Se le date di Trasmissione non sono valorizzate, il sistema estrae gli atti relativi all'ultimo mese.
        </td>
      </tr>
    </table>

    <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Visualizza anche gli atti già presi in carico&nbsp;
        </td>
        <td class="l">
          <input type=checkbox name="<%=ICostantiPresaincarico.CAMPO_INCLUDE_INCARICO%>" value=1></td>
        </td>
        <td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      </tr>

    </table>
    <BR>

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
    var frmvalidator  = new Validator("LoadRicercaProvSius");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>","maxlen=6","La lunghezza massima per il Numero Fascicolo SIEP è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>","numeric");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo SIEP è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","minlen=4","La lunghezza minima per l'Anno Fascicolo SIEP è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","numeric");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>","maxlen=6","La lunghezza massima per il Numero Fascicolo SIUS è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>","numeric");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo SIUS è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","minlen=4","La lunghezza minima per l'Anno Fascicolo SIUS è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","numeric");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiPresaincarico.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=3000");

  </script>
  </body>
</html>