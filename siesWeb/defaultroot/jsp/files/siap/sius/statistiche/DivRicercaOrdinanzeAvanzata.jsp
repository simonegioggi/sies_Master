<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="f3b.util.DateUtils"%>
  
 <jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>
 <jsp:useBean id="tipoRicorso" scope="request" class="java.lang.String"/>
  <jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>
 <jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
 <jsp:useBean id="esperto" scope="request" class="java.lang.String"/>
 
 <% 
 // Il default è ricerca x Estremi Ordinanza
 String testo1 = "Estremi Ordinanza";
 String testoDate = "Date Deposito";
 String valoreDate = ICostantiStatistiche.DATA_DEPOSITO_INTERVALLO;
 String testo2 = "Tutte";
 String testo3 = "Validate";
 String testo4 = "Non Validate";
 String testo5 = "Annullate";
 boolean isImpugnazione = false;
 boolean isFoglioComplementare = false;
 
    if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO))
    {
      // Ricerca x Estremi Decreto
      testo1 = "Estremi Decreto";
      testo2 = "Tutti";
      testo3 = "Validati";
      testo4 = "Non Validati";
      testo5 = "Annullati";
    }
    else if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_IMPUGNAZIONE))
    {
      // Ricerca x Estremi Impugnazione
      testo1 = "Estremi Ricorso/Impugnazione";
      testoDate = "Date Arrivo in Cancelleria";
      valoreDate = ICostantiStatistiche.DATA_ARRIVO_CANCELLERIA_INTERVALLO;
      isImpugnazione = true;
      testo2 = "Tutti";
      testo3 = "Validati";
      testo4 = "Non Validati";
      testo5 = "Annullati";     
    }
    else if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE))
    {
      // Ricerca x Estremi Impugnazione
      testo1 = "Estremi Foglio Complementare";
      testoDate = "Date Emissione";
      valoreDate = ICostantiStatistiche.DATA_EMISSIONE_INTERVALLO;
      isFoglioComplementare = true;
      testo2 = "Tutti";
      testo3 = "Validati";
      testo4 = "Non Validati";
      testo5 = "Annullati";     
    }  
 %>
  
  <script language="JavaScript" src="<%=ICostantiStatistiche.RICERCA_ORDINANZA_JS%>"></script>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaAvanzata" >
    <table  width=96%>
      <tr> <td class="Titolo" colspan=4>Indicare</td></tr>
    </table>
   
     <table width=96% >
     <%if (modalitaRicerca != null && 
         (modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO) || 
          modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_ORDINANZA))) {%>
      <tr>
      <td class="c" width="20%" >Relatore: </td>
      <td class="c" width="60%" >
            Magistrato <input id="radioMagistrato" type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_RELATORE%>" value="Magistrato"  onClick="VisualizzaElencoMagistrati();"  checked >&nbsp;
      <%if (tipoUfficio.compareTo("TDS")==0) { %>
      Esperto <input id="radioEsperto" type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_RELATORE%>" value="Esperto" onClick="VisualizzaElencoEsperti();" ></td>
      <% } %>
      </tr>
      <tr>
      <td class="c" id="labElencoMagistrati">Magistrato </td>
      <td class="L" id="ElencoMagistrati">
        <select title="magistrato" class=small name="<%=ICostantiStatistiche.CB_LISTA_MAGISTRATI%>" >
          <%= magistrato %>
        </select>
      </td>
      </tr>
      <tr>
      <td class="c" id="labElencoEsperti">Esperto </td>
      <td class="L" id="ElencoEsperti">
        <select title="esperto" class=small name="<%=ICostantiStatistiche.CB_LISTA_ESPERTI%>" >
          <%= esperto %>
        </select>
      </td>
      </tr>
      
     <%} %> 
     
      <tr>
      <td class="c" width="20%" >Intervallo: </td>
      <td class="c" width="60%" >
            <%=testo1%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_INTERVALLO%>" value="<%=ICostantiStatistiche.ESTREMI_PROVVEDIMENTO_INTERVALLO %>"  onClick="VisualizzaEstremiOrdinanza();"  checked >&nbsp;
      <%=testoDate%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_INTERVALLO%>" value="<%=valoreDate%>" onClick="VisualizzaDateDeposito();" ></td>
      </tr>
      <tr>
      <td class="c" width="20%" >Stato Validazione: </td>
       <td class="c" width="60%" >
            <%=testo2%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.TUTTI%>"   checked> 
            <%if (isImpugnazione || isFoglioComplementare) {%>
      Non Annullati <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.NON_ANNULLATI%>"  >&nbsp;     
      <%}else  { %>      
      <%=testo3%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.VALIDATI %>"  >&nbsp;
      <%=testo4%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.NON_VALIDATI %>"  >&nbsp;
      <%} %>
      <%=testo5%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.ANNULLATI %>"  ></td>
     </tr>
     
     
     <%
     if(   modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_ORDINANZA)
        || modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO)
       ) 
     {%>
       <tr>
       <%if(modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_ORDINANZA)) {%>
        <td class="c" width="20%" >Solo Ordinanze con: </td>
       <%} else if (modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO)) { %>
        <td class="c" width="20%" >Solo Decreti con: </td>
       <%}%>
        <td class="c" width="20%" >
          Controllo tramite mezzi elettronici 
          <input value="E" type="checkbox" name="<%=ICostantiStatistiche.CAMPO_TIPI_CONTROLLI_ESECUZIONE%>" > 
          &nbsp;&nbsp;&nbsp;
          Controllo tramite altri strumenti tecnici 
          <input value="T" type="checkbox" name="<%=ICostantiStatistiche.CAMPO_TIPI_CONTROLLI_ESECUZIONE%>" > 
        </td>
       </tr>     
     <% } %>     
      
<%if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO)) {%>  
    <tr>
      <td class="c" width="20%" >Tipo: </td>
      <td class="c" width="60%" >
            Tutti <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.TUTTI_DECRETI%>"   checked> 
            Inammissibilità <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.INAMMISSIBILITA %>"  >
            Incompetenza <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.INCOMPETENZA %>"  >
        NDP/NLP <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.NDPNLP %>"  >
        Revoca <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.REVOCA %>"  >
        Altri <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_DECRETO%>" value="<%=ICostantiStatistiche.ALTRI_DECRETI %>"  ></td>
     </tr>
<%} %>     
<%if (isImpugnazione) {%>  
    <tr>
        <td class="c" width="20%" >Tipo ricorso/impugnazione: </td>
        <td class="L" width="60%" >&nbsp;&nbsp;
            <select title="tipoRicorso" class=small name="<%=ICostantiStatistiche.CAMPO_TIPO_RICORSO%>" >
            <%=tipoRicorso%>
            <option value = "" selected />-
          </select>
          </td>
          </tr>
<%} %>     
     
 
      </table>
    <div id="contenitore" style="position: relative; top: 0; left: 0;  " >         
    <div id="EstremiOrdinanza" style="position:relative;  top: 0; left: 0; " >  
      <table  width=95%>
       <tr> <td class="Titolo" colspan=4>Intervallo <%=testo1%></td></tr>
      <tr>
        <td class="c" >
          <font class="label">Anno/Numero Iniziale (*) </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Ordinanza Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">/
          <input type="text" title="Numero Ordinanza Iniziale" name="<%=ICostantiStatistiche.CAMPO_NUM_INI %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="c" >
          <font class="label">Anno/Numero Finale (*) </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Ordinanza Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">/
          <input type="text" title="Numero Ordinanza Finale" name="<%=ICostantiStatistiche.CAMPO_NUM_FINE %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        </tr>      
         </table>
   </div>     
      <div id="DateDeposito" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
       <table width=96%>
        <tr>
    <td class="Titolo"  colspan ="4" >Intervallo <%=testoDate%> </td>
        </tr>
        <tr>
          <td class="c" width="10%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="c" width="10%"  >
            <font class="label">Data Finale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Finale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>

      </div> 
   </div>
        <br>
        <table>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="Validazione();">
         </td>
        </tr>
    </table>
    <br>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaProcXOrdinanze">
    <input type="HIDDEN" name="<%=ICostantiStatistiche.CAMPO_MODALITA_RICERCA%>" value="<%=modalitaRicerca%>">
  </FORM>
 
 
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("RicercaAvanzata");

  // Viene usata questa funzione per definire i campi da controllare che cambiano dalle opzioni scelte.
     function Validazione()
     {
     // alert ("validazione");
      
      if (isEstremiOrdinanza())
      {
        frmvalidator.clearAllValidations();
        // alert ("isEstremiOrdinanza");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI %>","req","Il campo Anno Inizio è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI%>","minlen=4","La lunghezza del campo Anno Inizio deve essere di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_INI %>","req","Il campo Numero Inizio  è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_INI%>","numeric");
    
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE %>","req","Il campo Anno Fine è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>","minlen=4","La lunghezza del campo Anno Fine deve essere di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_FINE %>","req","Il campo Numero Fine  è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_FINE%>","numeric");     
        
          frmvalidator.setAddnlValidationFunction("VerificaBase");      
      }
      else
      {
        frmvalidator.clearAllValidations();
          frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>","req","Il campo Giorno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","req","Il campo Giorno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");

        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","req","Il campo Mese è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","req","Il campo Mese è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");

        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","req","Il campo Anno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","req","Il campo Anno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
        
          frmvalidator.setAddnlValidationFunction("VerificaDateDeposito");
        }
      
     }
  </script>
   <script language="JavaScript" type="text/javascript">
     function VerificaDateDeposito()
  {
    var ritorno = true;
  var data_iniziale = document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>.value + '/' + document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>.value + '/'+ document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>.value;
      
  var data_finale = document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>.value + '/' + document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>.value + '/'+ document.RicercaAvanzata.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>.value;
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     if (! ControllaData(data_iniziale))
     {
        alert('Data iniziale non valida!');
        ritorno =  false;
     }
     else if (! ControllaData(data_finale))
     {
      alert('Data finale non valida!');
        ritorno =  false;
     }
     // Controllo data finale >= Data iniziale .
     else if( !CompareDate( data_iniziale, data_finale ) )
     {
        alert('Data finale < Data Iniziale!');
        ritorno =  false;
     }
     else if( !CompareDate( data_iniziale, data_sistema ) )
     {
        alert('Data iniziale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
     else if( !CompareDate( data_finale, data_sistema ) )
     {
      alert('Data finale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
              
     return ritorno;
  }
 
  
   </script>
   
   
   