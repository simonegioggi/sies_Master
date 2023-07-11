<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina --%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva" %>


<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Ricerca Stato Pagamenti</title>
  
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"> </script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

  <script language="JavaScript">
    function Verify () {
      //alert("Verify");
      
      // Validazione campi intervallo procedimenti
      // Non è possibile specificare solo il numero o solo l'anno  (verificare)
      if (   document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0
          && document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0 )
      {
        alert("Valorizzare Anno inizio ricerca");
        return false;
      }
      
      /*
      if (   document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0
          && document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0 )
      {
        alert("Valorizzare Numero inizio ricerca");
        return false;
      }*/
      
      if (   document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0
          && document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0 )
      {
        alert("Valorizzare Anno di fine ricerca");
        return false;
      }
      
      /*
      if (   document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0
          && document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0 )
      {
        alert("Valorizzare Numero di fine ricerca");
        return false;
      }
      */
      
      
      // validazione delle date
      var data_inizio = document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value
                  +'/'+ document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value
                  +'/'+ document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
      var data_fine = document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value
                +'/'+ document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value
                +'/'+ document.RicercaStatoPagamenti.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
      
      if (!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data Iscrizione iniziale non valida');
        return false;
      }      
      
      if (!ControllaDataPassaVuota(data_fine))
      {
        alert('Data Iscrizione Finale non valida');
        return false;
      }  

      if (   data_inizio != '//' && data_fine != '//' 
          && !CompareDate(data_inizio, data_fine) 
         )
      {
        alert("La Data di fine non puo' essere inferiore alla data di inizio");
        return false;
      }      
      
      // Selezionare almeno un criterio di ricerca
      
      
      return true;
    }
  </script>
</head>

<body class="corpo">

  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Procedimenti Pene Pecuniarie In Base a Stato Pagamenti</font>
      </td>
    </tr>
  </table>

  <br>
  <%  if (!fascicoloNotInSession.equals("S")) { %>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <% } %>
  <br>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaStatoPagamenti" >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActRicercaStatoPagamenti">
  
    <table width="96%">
      <tr>
        <td class="Titolo">Intervallo Estremi Procedimento</td>
      </tr>
    </table>

    <table width="96%">
      <tr>
        <td class="c" width="50%" >
          Anno/Numero Iniziale:&nbsp;&nbsp;
          <input type="text" size="4" maxlength="4" value="" 
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>"
                 onFocus="javascript:textboxSelect(this)" 
                 onBlur="javascript:value=FillYear(value)"
                 onkeypress="return TicTabNumField(this,event)" />
          /
          <input type="text" size="14" maxlength="14" value="" 
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>"
                 onFocus="javascript:textboxSelect(this)" 
                 onkeypress="return TicTabNumField(this,event)" />
          Anno/Numero Finale:&nbsp;&nbsp;
          <input type="text" size="4" maxlength="4" value="" 
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>"
                 onFocus="javascript:textboxSelect(this)" 
                 onBlur="javascript:value=FillYear(value)"
                 onkeypress="return TicTabNumField(this,event)" />
          /
          <input type="text" size="14" maxlength="14" value="" 
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>"
                 onFocus="javascript:textboxSelect(this)" 
                 onkeypress="return TicTabNumField(this,event)" />
        </td>
      </tr>
    </table>

    <table width="96%">
      <tr>
        <td class="Titolo">Intervallo Date Iscrizione</td>
      </tr>
    </table>
    
    <table width="96%">
      <tr>
        <td class="c" width="50%" >
          Data Iscrizione Iniziale:&nbsp;&nbsp;
          <font class="l">
            <input type="text" title="Giorno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </font>
          Data Iscrizione Finale:&nbsp;&nbsp;
          <font class="l">
            <input type="text" title="Giorno Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </font>
        </td>
      </tr>
    </table>
    
    <table width="96%">
      <tr>
        <td class="Titolo">Tipologia Statistica</td>
      </tr>
    </table>

    <table width="96%">
      <tr>
        <td class="l" width="30%">
          <font class="label">Procedimenti con pena pecuniaria totalmente pagata</font>
        </td>
        <td class="l">
          <input type="radio" checked="checked"
                 value="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_INTERAMENTE_PAGATO%>"
                 name="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_STATO_PAGAMENTO%>" />
        </td>
      </tr>
      <tr>
        <td class="l">
          <font class="label">Procedimenti con pagamento rateizzato con rate non pagate</font>
        </td>
        <td class="l">
          <input type="radio" 
                 value="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_RETEIZZATO_NON_PAGATO%>"
                 name="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_STATO_PAGAMENTO%>" />
        </td>
      </tr>
      <tr>
        <td class="l">
          <font class="label">Procedimenti con pagamento in unica soluzione non pagata</font>
        </td>
        <td class="l">
          <input type="radio" 
                 value="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_UNICA_RATA_NON_PAGATO%>"
                 name="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_STATO_PAGAMENTO%>" />
        </td>
      </tr>
    </table>    
    <br>
    <table>
      <tr>
        <td class="l" >
          <input type="submit" class="bottone"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
    
  </form>  
</body>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("RicercaStatoPagamenti");
    frmvalidator.setAddnlValidationFunction("Verify"); 
  </script>  
</html>