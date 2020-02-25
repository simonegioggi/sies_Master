<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.siepe.espertoattivita.model.EspertoAttivitaEspertoModel" %>
<%@ page import="siap.siepe.espertoattivita.action.ICostantiEspertoAttivita" %>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>

<jsp:useBean id="esperti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="attivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>

<%
String azione = "siap.siepe.espertoattivita.action.ActInserisciEsperto";
String azione2 = "siap.siepe.espertoattivita.action.ActChiusuraEsperto";

%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Esperti X Attività </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ICostantiEspertoAttivita.JS_ESPERTO_ATTIVITA%>"></script>
  </head>

  <script language="JavaScript">
     // Caricamento della data di Inizio Attività
     var dataIniAtt = "<%=DateUtils.getDateToString(attivita.getDataInizio(),"dd/MM/yyyy")%>";

    // Definizione degli array contenenti i dati degli esperti già assegnati
     var NumTotale = <%=esperti.size()%>;   /* numero complessivo esperti  */
     var lIdEsperto = new Array(NumTotale);   /* Array degli esperti  */
     var lDataIniEsperto = new Array(NumTotale);   /* Array data inizio esperti  */
     var lDataFineEsperto = new Array(NumTotale);   /* Array data fine esperti  */

   // Valorizzazione degli array esperti
     <%
     for (int i=0; i < esperti.size(); i++)
     {
        EspertoAttivitaEspertoModel esperto = (EspertoAttivitaEspertoModel) esperti.get(i);
     %>
         lIdEsperto[<%=i%>] = "<%=esperto.getEspIdEsperto()%>";
         lDataIniEsperto[<%=i%>] = "<%=DateUtils.getDateToString(esperto.getDataInizio(),"dd/MM/yyyy")%>";
         lDataFineEsperto[<%=i%>] = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(esperto.getDataFine(),"dd/MM/yyyy"),"-")%>";
<%    } %>
  </script>

  <script language="JavaScript">
  // Funzione di verifica dei dati immessi nel caso di inserimento Esperto.
  function VerificaInserimento()
  {
     var ritorno = false;
     var trovato = false;
     // ID Esperto immesso
     var id = document.InserisciEsperto.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>.value;

     if (id.length < 1)
     {
        alert( " Esperto non selezionato");
        ritorno = false;
      }
      else
      {
        // Controllo sulla data inizio esperto e suo confronto con la data inizio attività
         var gg0 = FillDM(document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_INIZIO%>.value);
         var mm0 = FillDM(document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_INIZIO%>.value);
         var aa0 = document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_INIZIO%>.value;
         var dataIniEsperto = gg0 + "/" + mm0 + "/" + aa0;

        ritorno = controlloDateInserimento(dataIniAtt, dataIniEsperto);

        //alert( " si chiama cercaEsperto");
        // Controllo dell'esistenza dell'Esperto
        if(ritorno)
          trovato = cercaEsperto(id, dataIniEsperto);
        if (trovato)
        {
           alert("L'esperto selezionato è già presente nell'elenco ed attivo nella data prescelta");
           ritorno = false;
        }
      }
      return ritorno;
   }

// Funzione di verifica dei dati immessi nel caso di chiusura Esperto.
  function VerificaChiusura()
  {
     var ritorno = false;

    // ID Esperto immesso
     var id = document.ChiusuraEsperto.<%=ICostantiEspertoAttivita.CAMPO_ESP_ID_ESPERTO%>.value;
 //    alert("id Esperto ->" + id);
     if (id.length < 1)
     {
        alert( " Esperto non selezionato");
        ritorno = false;
      }
      else
      {
        // Controllo sulla data fine e confronto con la data inizio
         var gg0 = FillDM(document.ChiusuraEsperto.<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_FINE%>.value);
         var mm0 = FillDM(document.ChiusuraEsperto.<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_FINE%>.value);
         var aa0 = document.ChiusuraEsperto.<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_FINE%>.value;

         var dataFine = gg0 + "/" + mm0 + "/" + aa0;
         var dataInizio = document.ChiusuraEsperto.DataInizio.value;

         ritorno = controlloDateChiusura(dataInizio, dataFine);
      }
      return ritorno;
  }


</script>

  <script language="JavaScript" >
   function initDataInizio()
   {
     document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_INIZIO%>.value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"dd"))%>";
     document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_INIZIO%>.value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"MM"))%>";
     document.InserisciEsperto.<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_INIZIO%>.value = "<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"yyyy"))%>";
   }
  </script>

      <script language="JavaScript">
      var desktop;
      // Esegue la chiamata all'elenco degli esperti.
      function elencoEsperti(a_formname)
    	{
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.esperto.action.ActLoadRicercaEspertoLista&formname="+a_formname, "Ricerca_Esperto","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>
      <script language="JavaScript">

      function annullaEsperto(idEsperto, cognome, nome, dataInizio)
      {
        document.ChiusuraEsperto.DataInizio.value = dataInizio ;
        document.ChiusuraEsperto.<%=ICostantiEsperto.CAMPO_COGNOME%>.value =cognome;
        document.ChiusuraEsperto.<%=ICostantiEsperto.CAMPO_NOME%>.value = nome;
        document.ChiusuraEsperto.<%=ICostantiEspertoAttivita.CAMPO_ESP_ID_ESPERTO%>.value = idEsperto;
        visualizzaChiusura();
      }

      function visualizzaChiusura()
      {
        var node;
        node=document.getElementById("inserimento");
        node.style.visibility='hidden';

        node=document.getElementById("chiusura");
        node.style.visibility='visible';
      }

      function visualizzaInserimento()
      {
        var node;
        node=document.getElementById("chiusura");
        node.style.visibility='hidden';

        node=document.getElementById("inserimento");
        node.style.visibility='visible';
      }

      function resetInserimento()
      {
         document.InserisciEsperto.<%=ICostantiEsperto.CAMPO_NOME%>.value = "";
         document.InserisciEsperto.<%=ICostantiEsperto.CAMPO_COGNOME%>.value = "";
         document.InserisciEsperto.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>.value = "";
         initDataInizio();
      }
      function setInserimento()
      {
        resetInserimento();
        visualizzaInserimento();
      }


    </script>



  <body class="corpo">

  <table width=85%>
    <tr> <td class="Titolo" > Elenco Esperti Assegnati </td> </tr>
</table>
<% if (esperti.size() > 0) { %>
<table width=85%>
      <tr>
        <td class="int" width=20%>Nome</td>
        <td class="int" width=20%>Cognome</td>
        <td class="int" width=20%>Data inizio</td>
        <td class="int" width=20%>Data fine</td>
        <td class="int" width=10%>Azioni</td>
        <td class="int" width=5%>
                <a href="Javascript:setInserimento();">
                  <img src="/images/new24.gif" width="12" height="12" alt="Ripristino Inserimento Nuovo Esperto" border="0">
                </a>
</td>
      </tr>
<%
  Iterator itx = esperti.iterator();
  while ( itx.hasNext())
  {
    EspertoAttivitaEspertoModel esperto = (EspertoAttivitaEspertoModel)itx.next();
%>
    <tr>
    	<td class="l"><%=esperto.getNome()%></td>
    	<td class="l"><%=esperto.getCognome()%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(esperto.getDataInizio(),"dd-MM-yyyy"),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(esperto.getDataFine(),"dd-MM-yyyy"),"-")%></td>
      <td class="c">
     <!-- BOTTONE DI ANNULLAMENTO -->
      <% if (esperto.getDataFine() == null) { %>
                <a href="Javascript:annullaEsperto('<%=esperto.getEspIdEsperto()%>','<%=esperto.getCognome()%>','<%=esperto.getNome()%>','<%=DateUtils.getDateToString(esperto.getDataInizio(),"dd/MM/yyyy")%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Chiusura" border="0">
                </a>
      <% } %>
     </td>
    </tr>
<%
  }
%>
   </table>
<% } else { %>
<table>
    <tr><td class="int" > Non ci sono Esperti assegnati all'Attività </td></tr>
</table>
<% } %>
  <br>
<div id="comune" style="position: relative; top: 0; left: 0;" >
  <div id="inserimento" style="position: relative; top: 0; left: 0; " >

 <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciEsperto">
 <table width=85%>
    <tr> <td class="Titolo" > Inserimento Nuovo Esperto </td>
    </tr>
</table>
 <table cellspacing="2" cellpadding="2" width=85%>
    <tr>
      <td class="L" >
      <input readonly  title="Nome Esperto"    value=""     type="text" name="<%=ICostantiEsperto.CAMPO_NOME%>"    maxlength="35" size="25" >
      </td>
      <td class="L" >
        <input readonly  title="Cognome Esperto" value=""  type="text" name="<%=ICostantiEsperto.CAMPO_COGNOME%>" maxlength="35" size="25">
       </td>
         <td class="L" >
             <a href="JavaScript:elencoEsperti('InserisciEsperto');">
      	Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
            </a>
         </td>
</tr>
   <tr>
      <td class="l">Data inizio (*)
      (gg/mm/aa)
      </td>
      <td>
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>" >
  <input type="HIDDEN" name="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" value="<%=attivita.getIdAttivita()%>" >
  <input type="HIDDEN" name="<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>"     value="" >
</form>

</div>
  <div id="chiusura" style="position: absolute; top: 0; left: 0; visibility:hidden; " >
 <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ChiusuraEsperto">
 <table width=85%>
    <tr> <td class="Titolo" > Chiusura Esperto </td>
    </tr>
</table>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="L" >
      <input readonly  title="Nome Esperto"    value=""     type="text" name="<%=ICostantiEsperto.CAMPO_NOME%>"    maxlength="35" size="25" >
      </td>
      <td class="L" >
        <input readonly  title="Cognome Esperto" value=""  type="text" name="<%=ICostantiEsperto.CAMPO_COGNOME%>" maxlength="35" size="25">
       </td>
    </tr>
   <tr>
      <td class="l">Data inizio
      </td>
      <td>
      <input readonly  title="Data Inizio"    value=""     type="text" name="DataInizio"    maxlength="35" size="25" >
      </td>
</tr>
<tr>
      <td class="l">Data fine (*)
      (gg/mm/aa)
      </td>
      <td>
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
      <td>


      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione2%>" >
  <input type="HIDDEN" name="<%=ICostantiEspertoAttivita.CAMPO_ATT_ID_ATTIVITA%>" value="<%=attivita.getIdAttivita()%>" >
  <input type="HIDDEN" name="<%=ICostantiEspertoAttivita.CAMPO_ESP_ID_ESPERTO%>"     value="" >
</form>
</div>
</div>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciEsperto");
    frmvalidator.setAddnlValidationFunction("VerificaInserimento");
    frmvalidator.addValidation("<%=ICostantiEspertoAttivita.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEspertoAttivita.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEspertoAttivita.CAMPO_ANNO_DATA_INIZIO%>","numeric");
  </script>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("ChiusuraEsperto");
    frmvalidator.setAddnlValidationFunction("VerificaChiusura");
  </script>


  </body>
</html>