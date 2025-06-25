<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.model.DecodeModel"%>

<%@ page import="siap.sius.produzioneatti.action.ICostantiProduzioneAtti"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.motivazionedecreto.action.ICostantiMotivazioneDecreto"%>
<%@ page import="siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento"%>

<jsp:useBean id="dataInsFS"         scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioS"   scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"    scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="motivi"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="pageCall"         scope="request" class="java.lang.String"/>

<%
  String lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaParere";
%>

<script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Richiesta parere inammissibilità</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      // Controllo validita' della data richiesta
      var data_emissione=document.LoadInserisciRichiestaParere.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaParere.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaParere.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_emissione))
      {
        alert('Data richiesta non valida');
        return false;
      }

      // Data richiesta minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_emissione,data_sistema) )
      {
        alert('Data richiesta maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Sius <= data richiesta
      if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      {
        alert('Data richiesta minore della data di inserimento del fascicolo SIUS!');
        return false;
      }
      
      // Obbligatorietà Destinatario
	  if(document.LoadInserisciRichiestaParere.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.value == "-"){
	  	alert('Il campo Destinatario è obbligatorio');
	    return false;  
	  }

      return true;
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Richiesta parere </font>
      </td>
<%
	if(pageCall != null && pageCall.equals("Elenco")){
%>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>		
<%
	}
%>
    </tr>
    <tr>
	<br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  	<br>
  	</tr>
    
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaParere">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Data Richiesta <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
		  
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciRichiestaParere','<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
      </tr>

      <tr>
        <td class="l">Destinatario <font class=ob>(*)</font></td>

    		<td class="L">
      		<select title="Destinatario" class=small name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" >
        <%= codTipoUfficioS %>
      </select>
      </tr>

      <tr>
        <td class="l">Sede Procura <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="<%=UtenteConnesso.getUfficioUtente().getDescrComune()%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadInserisciRichiestaParere','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
        

</table>
<div id="comune" style="position: relative; top: 0; left: 0;" >

   <div id="motivazioni" style="position: relative; top: 0; left: 0; " >
   <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l" valign=top> Motivazioni:</td>

        <td class="l" >
  <table>
   <%
    int count = 1;

    while( count <  4 )
    {
      %>
      <tr>
        <td >
          <TEXTAREA title="Motivo n° <%=count%>" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>"  cols=80 rows=3 ></textarea>
        </td>
      </tr>
     <%
     count+=1;
    }
    %>
  </table>
</tr>
</table>
 <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>
</div>
   <div id="motivazioniDecreto" style="position: absolute; top: 0; left: 0; visibility:hidden;  " >
    <table cellspacing=4 cellpadding=4>

      <tr>
      <td class="l" colspan=2>Indicare Motivi di Inammissibilità:</td>
      </tr>
<%
    if (motivi.size() > 0)
    {
      Iterator lMotivCorr = motivi.iterator();
      while (lMotivCorr.hasNext())
      {
        DecodeModel lDecod = (DecodeModel) lMotivCorr.next();
        // Codice 90 va trattato in maniera speciale
        if (lDecod.getCode().compareTo("90") != 0)
        {
          String lDescrizione = lDecod.getDescription();
          lDescrizione = lDescrizione.replace('<', 'x');
          lDescrizione = lDescrizione.replace('>', 'z');
          lDescrizione = lDescrizione.replace('?', '0');
          String pat = "x0z";
          String lSubPat = "x01z";

        // Si separa la descrizione iniziale in tante quante sono separate dai campi1
        // che non devono essere più di 1
          String[] lDescrizioni = lDescrizione.split(pat);
          if (lDescrizioni != null && lDescrizioni.length > 0)
          {
%>
      <tr>
        <td class="l"><input value="<%=lDecod.getCode()%>" type="checkbox" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"></td>

         <td class="l">
<%
         int i = 0; // indice sottostringhe separate da campo1
         for (; i <lDescrizioni.length; i++)
         {
            String lDescrizCorr = lDescrizioni[i];

            if (i == 1 )
            {
             // Individuato campo1, solo 1
%>
          <input value="" type="text" name="<%=ICostantiMotivazioneDecreto.CAMPO_DESCR_MOTIVAZIONE+lDecod.getCode()%>" size=35>
<%
            }
            // Si suddivide ogni stringa in sub-stringhe separate dal secondo campo
            String[] lSubDescrizioni = lDescrizCorr.split(lSubPat);
            int j = 0; // indice sottostringhe separate da campo2
            for (; j <lSubDescrizioni.length; j++)
            {
            if (j == 1 )
            {
             // Individuato campo2, solo 1
%>
          <input value="" type="text" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE+lDecod.getCode()%>" size=35>
<%
            }

%>
        <%=(lSubDescrizioni[j] != null ? lSubDescrizioni[j] : "-")%>
<%
            } // for j
         } // for i
%>

        </td>
      </tr>
<%
      } // endif
    }   // caso 90
   }    // endwhile
  }   // motivi > 0
%>
      <tr>
        <td class="l"><input value="90" type="checkbox" name="<%= ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"></td>
        <td class="l">
          <textarea title="AltraMotivazione90" name=<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE+"90"%> cols=90 rows=5 ></textarea>
        </td>
      </tr>
 <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    </table>
   </div>
</div>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciRichiestaParere");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>", "req","Il campo Sede è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
<%--     frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha"); --%>

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>