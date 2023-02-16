<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>

<jsp:useBean id="contenuto"          scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"       scope="request" class="java.lang.String"/>

<jsp:useBean id="oggetto"            scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"         scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipodecreto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore" scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="avvocato"           scope="request" class="java.util.Vector" />
<jsp:useBean id="flagOrdinanza"      scope="request" class="java.lang.String"/>

<jsp:useBean id="codDettagli"        scope="request" class="java.lang.String"/>
<jsp:useBean id="fascSospeso"        scope="request" class="java.lang.String"/>
<!--jsp:useBean id="lFascicolo"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/-->

<jsp:useBean id="fascicoloSiusGP"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="TornaQui"           scope="request" class="java.lang.String"/>

<jsp:useBean id="isOrdProvvisoria"   scope="request" class="java.lang.String"/>

<%
/* Estrazione della data udienza  o data iscrizione */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto/Ordinanza/Sentenza SIUS</title>
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

      function  Verifica()
      {
        var ritorno = true;
        var data_minima = '<%=data1%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_emissione=document.LoadInserisciEmissioneDecreto.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEmissioneDecreto.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE %>.value+'/'+document.LoadInserisciEmissioneDecreto.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE %>.value;

        // Controllo sospensione fascicolo
        <% if (fascSospeso.compareTo("SI") == 0)	{ %>
        if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
             return false;
    <% } %>
    
        // Controllo magistrato
        <% if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null)	{ %>
            ritorno = false;
        <% } %>
        if (! ritorno)
          alert (" Magistrato Relatore non assegnato");
        else
        // Controllo avvocato solo per TDS

        <%
        if ( UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0)
        {
          boolean controlloAvvocato = true;

          // Si tratta di un decreto
          if( flagOrdinanza == null  || flagOrdinanza.compareTo("ordinanza") != 0)
          {
        	  // Controllo non va fatto per decreto con contenuto C027. Luigi 2-5-2007
        	  // 19/07/2007 Controllo disabilitato anche per contenuto C018.
        	  if (codContenuto.equalsIgnoreCase("C027") ||
        	      codContenuto.equalsIgnoreCase("C018") )
        		  controlloAvvocato = false;
        	  else
        		  controlloAvvocato = true;
          }
          // Si tratta di un'ordinanza
          else
          {
              // Controllo non va fatto per l'Emissione Ordinanza se
              // l'Udienza è di tipo Prefissata. Luigi 13-09-2005
        	  if (fascicoloSiusGP.getUdiPro()  != null && fascicoloSiusGP.getUdiPro().getFlagRinviata() != null && ! fascicoloSiusGP.getUdiPro().getFlagRinviata().equalsIgnoreCase("P"))
            	  controlloAvvocato = true;
        	  else
        		  controlloAvvocato = false;
          } // endif Decreto/Ordinanza
       	  if (controlloAvvocato)
          {
          int numAvvocati = avvocato.size();
          if ( numAvvocati == 0)
          { %>
            ritorno = false;
            alert('Avvocato obbligatorio!');
          <%}
          }
        }%>

          // Controllo della data di emissione
          if (ritorno && (! ControllaData(data_emissione)))
          {
            alert('Data emissione non valida: '+ data_emissione );
            return false;
          }
          // Controllo data di sistema >= Data Emissione .
          else if(ritorno && (!CompareDate( data_emissione, data_sistema)) )
          {
            alert('Data Emissione non può essere superiore alla data odierna!');
            ritorno =  false;
          }
         // Controllo della data deposito <= data camera di consiglio
         else if (ritorno && (!CompareDate( data_minima, data_emissione)) )
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

    if (flagOrdinanza != null && flagOrdinanza.compareTo("ordinanza") == 0)
    {
        lAction = new String("siap.sius.depositoordinanzapc.action.ActInserisciEmissioneOrdinanzaUDS");
        lDocumento = new String("ordinanza");
        if ("SI".equals(isOrdProvvisoria))
        	lTitolo = new String("Emissione Ordinanza Applicazione Provvisoria Misura Alternativa");
        else
        	lTitolo = new String("Emissione Ordinanza");
        lActRet = new String("siap.sius.depositoordinanzapc.action.ActLoadEmissioneOrdinanzaUDS");
    }
    else if(flagOrdinanza != null && flagOrdinanza.compareTo("sentenza") == 0)
    {
        lAction = new String("siap.sius.depositosentenza.action.ActInserisciEmissioneSentenzaTDS");
        lDocumento = new String("sentenza");
        lTitolo = new String("Emissione Sentenza");
        lActRet = new String("siap.sius.depositosentenza.action.ActLoadEmissioneSentenzaTDS");
    }
    else
    {
        lAction = new String("siap.sius.depositodecreto.action.ActInserisciEmissioneDecreto");
        lDocumento = new String("decreto");
        lTitolo = new String("Emissione Decreto");
        lActRet = new String("siap.sius.depositodecreto.action.ActLoadEmissioneDecreto");
    }
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


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciEmissioneDecreto">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
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
      <a href="Javascript:ListaOggetti('LoadInserisciEmissioneDecreto',document.LoadInserisciEmissioneDecreto.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciEmissioneDecreto.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciEmissioneDecreto.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
      &nbsp;
      <a href="Javascript:ListaOggetti('LoadInserisciEmissioneDecreto','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciEmissioneDecreto.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciEmissioneDecreto.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
    </td>
  </tr>

  </table>
  
  <input type="HIDDEN" name="<%=ICostantiMagistratoRelatore.CAMPO_MAG_COD_MAGISTRATO%>" value="<%= magistratorelatore.getMagCodMagistrato()%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
  <input type="HIDDEN" name="isOrdProvvisoria" value="<%=isOrdProvvisoria%>">




  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciEmissioneDecreto");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");
  </script>

  </body>
</html>