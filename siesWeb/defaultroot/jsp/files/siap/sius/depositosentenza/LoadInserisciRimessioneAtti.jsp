<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sius.provvedimento.util.RicercaProvvedimentiUtil"%>

<jsp:useBean id="contenuto"     	 scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"     	 scope="request" class="java.lang.String"/>

<jsp:useBean id="oggetto"       	 scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"    	 scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"   	 scope="request" class="java.lang.String"/>
<jsp:useBean id="tipodecreto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore" scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="avvocato"    		 scope="request" class="java.util.Vector" />
<jsp:useBean id="flagSentenza"   	 scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutorita"  	 scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1" 	 scope="request" class="java.lang.String"/>

<jsp:useBean id="codDettagli"        scope="request" class="java.lang.String"/>

<jsp:useBean id="fascicoloSiusGP"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="TornaQui"           scope="request" class="java.lang.String"/>

<%
/* Estrazione della data udienza  o data iscrizione */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
/* Check sospensione */
String lFascSospeso = "NO";
RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(fascicoloSiusGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());  
if (lRicerca.verificaEsistenzaSospensione())
	lFascSospeso = "SI";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Rimessione Atti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

    <script language="JavaScript">
      var desktop;
      // Chiamata funzione lista Oggetti
      function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
            aLink += "&formname="+a_formname;
            aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }

     // Chiamata funzione lista dei comuni.
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
        
      function  Verifica()
      {
        var ritorno = true;
        var data_minima = '<%=data1%>';
        var fascSospeso = '<%=lFascSospeso%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_emissione=document.LoadInserisciRimessioneAtti.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRimessioneAtti.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE %>.value+'/'+document.LoadInserisciRimessioneAtti.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE %>.value;

        // Controllo sospensione fascicolo
        if (fascSospeso == 'SI' )	{ 
        if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
             return false;
        }

        <%int numAvvocati = avvocato.size();
        if ( numAvvocati == 0)
        { %>
          alert('Avvocato obbligatorio!');
          return false;
        <%}%>
        
        // Controllo magistrato
        <% if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null)	{ %>
            ritorno = false;
        <% } %>
        if (! ritorno)
          alert (" Magistrato Relatore non assegnato");
        else
        
          // Controllo della data di emissione
          if (ritorno && (! ControllaData(data_emissione)))
          {
            alert('Data emissione non valida: '+ data_emissione );
            return false;
          }
          // Controllo data di sistema >= Data Emissione .
          else if( !CompareDate( data_emissione, data_sistema) )
          {
            alert('Data Emissione non può essere superiore alla data odierna!');
            ritorno =  false;
          }
         // Controllo della data deposito <= data camera di consiglio
        //alert("data_camera ->" + data_camera);
         else if ( !CompareDate( data_minima, data_emissione) )
         {
            alert("Data Emissione non può precedere: " + data_minima);
            ritorno =  false;
         }
        return ritorno;
      }
    </script>

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  </head>
<%
    String lAction = null;
    String lDocumento = null;
    String lTitolo = null;
    String lActRet = null;

   
            lAction = new String("siap.sius.depositosentenza.action.ActInserisciRimessioneAtti");
            lDocumento = new String("sentenza");
            lTitolo = new String("Emissione Sentenza Rimessione Atti");
            lActRet = new String("siap.sius.depositosentenza.action.ActLoadRimessioneAtti");
        
%>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
          <font class="campo"><%=lTitolo%></font>
      </td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
      <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
      <jsp:param name="MagRelRitorno" value="<%=lActRet%>"/>
      </jsp:include>
    </tr>
  </table>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="<%=lActRet%>"/>
      </jsp:include>


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRimessioneAtti">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

  <tr>
    <td class="l">Contenuto<font class="ob">(*)</font></td>
    <td class="L"> <%=contenuto%></td>
  </tr>

  <tr>
    <td class="l">Oggetto<font class="ob">(*)</font></td>
    <td class="l">
      <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly><%=descOggetti%></Textarea>
      <a href="Javascript:ListaOggetti('LoadInserisciRimessioneAtti',document.LoadInserisciRimessioneAtti.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciRimessioneAtti.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciRimessioneAtti.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
      &nbsp;
      <a href="Javascript:ListaOggetti('LoadInserisciRimessioneAtti','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciRimessioneAtti.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciRimessioneAtti.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
    </td>
  </tr>
   <tr>
	<td class="l">Motivazioni</td>
    <td class="l"><TEXTAREA title="Motivazioni" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO %>" cols="88" rows="4" ></textarea></td>
  </tr>
  </table>
  <table cellspacing="1" cellpadding="1" > 
   <tr>
    <td class="l" >Dispone la sospensione del procedimento per rimessione degli atti:</td>
   </tr>
   </table>
   <table cellspacing="2" cellpadding="2">
  
	<tr><td class="c"><input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="cortecostituzionale" checked  ></td><td class="l">alla Corte Costituzionale per giudizio di legittimità costituzionale </td></tr>
	<tr><td class="c"><input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="corteeuropea" ></td><td class="l">alla Corte Giustizia Europea per giudizio di legittimità in materia di interpretazione trattati internazionali </td></tr>
	<tr><td class="c"> <input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="altro" ></td><td class="l">a 
		<input name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI %>" value = "" size="88" maxlength="88" > </td></tr>
   </table>
   <table cellspacing="2" cellpadding="2">
   <tr>
    <td class="l" colspan=3 >Autorità Destinatarie Notifica</td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Presidenza Consiglio dei Ministri </td>
        <td class="l"> <input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENZA_CONSIGLIO %>" >
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Corte Giustizia Europea  </td>
        <td class="l"> <input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_CORTE_GIUSTIZIA_EUROPEA %>" >
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Corte Costituzionale </td>
        <td class="l"> <input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_CORTE_COSTITUZIONALE %>" >
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Presidente Giunta Regionale </td>
        <td class="l"> <input title="Regione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENZA_GIUNTA %>"  value="" type="text" maxlength="40" size="40">
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=2>
        Per la notifica al Soggetto:
      </td>
   </tr>
   <tr>
   		<td class="l" colspan=1> </td>
        <td class="l" colspan=2>Autorità Destinazione 
           <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= tipoAutorita %>
          </select>
        </td>
            </tr>
    <tr>
    	<td> </td>
        <td class="l" colspan=2>Sede  
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadInserisciRimessioneAtti', '<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
     </tr>
     <tr>
     		<td> </td>
            <td class="l" colspan=2>Indirizzo         
              <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="60" size="60">
            </td>
     </tr>
 
 <%
        Iterator itxAvv = avvocato.iterator();
        int num_sede = 0;
        while ( itxAvv.hasNext())
        {
         AvvocatoSiusModel lAvv = (AvvocatoSiusModel)itxAvv.next();
%>
 
    <tr>
      <td> </td>
      <td class="l" colspan=2>
        Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%>:
      </td>
   </tr>
   <tr>
   		<td class="l" colspan=1> </td>
        <td class="l" colspan=2>Autorità Destinazione 
           <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_AVVOCATO_DESTINATARIO%>">
            <%= TipiIstituti1 %>
          </select>
        </td>
            </tr>
    <tr>
    	<td> </td>
        <td class="l" colspan=2>Sede  
          <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
             <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE_AVVOCATO%>"
              value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo(),"-")%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadInserisciRimessioneAtti','<%=ICostantiRichiestaAtti.CAMPO_SEDE_AVVOCATO%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
     </tr>
     
    
     <tr>
     		<td> </td>
            <td class="l" colspan=2>Indirizzo         
              <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_INDIRIZZO_AVVOCATO%>" value="" type="text" maxlength="60" size="60">
            </td>
     </tr>
     
      <!-- 
      <tr>
            <td class="l">Notifica Via Fax: </td>
            <td class="L" colspan=3>
             <input title="Notifica Via Fax" name="<//%=ICostantiRichiestaAtti.CAMPO_NOTIFICA_VIA_FAX %>" value="1" type="checkbox"  />
            </td>
     </tr>
      -->
 	 <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSiusModel().getIdAvvocatoFascicoloSius()%>" type="hidden" >
<%
      num_sede++;
     }
%>
      
     
   <tr>
    <td class="l" colspan=3>Uffici Destinatari Comunicazione</td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
         Presidente del Senato </td>
         <td class="l"><input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENTE_SENATO %>" >
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
        	Presidente della Camera dei Deputati </td>
      <td class="l"><input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENTE_CAMERA %>" >
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
        	Presidente Consiglio dei Ministri </td>
        <td class="l"><input type="checkbox" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENTE_CONSIGLIO %>" >
      </td>
   </tr>
   <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciRimessioneAtti");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");
  </script>

  </body>
</html>