<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="modalita"          scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"        scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="codiceAutoritaE"   scope="request" class="java.lang.String"/>

<%
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>
<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaAvvocati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function Verify()
    {
      if (document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        return false;
      }

      if(document.LoadInserisciDecretoSospensione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
	      alert("Il Cognome del Magistrato è obbligatorio");
        return false;
      }
  
      if(document.LoadInserisciDecretoSospensione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
  	    alert("Il Nome del Magistrato è obbligatorio");
        return false;
      }
  	}

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

      //========================================================================
      // Funzione per il caricamento della lista degli Ordini di Esecuzione
      //========================================================================
      function ListaOrdiniEsecuzione(a_formname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActListaOE&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Ordini_Esecuzione", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
      }


      //========================================================================
      // Funzione Visualizzare/Nascondere la sezione (div) per la richiesta di
      // restituzione Ordine di Esecuzione Simeone
      //========================================================================
      function VisualizzaOE()
      {
        var nodeOE = document.getElementById('divOrdineEsecuzione');

        if(document.LoadInserisciDecretoSospensione.<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>.checked == true)
        {
           nodeOE.style.display='block';

        }
        else
        {
           nodeOE.style.display='none';
           
           // Se viene deselezionato il checkbox vengono sbiancati i campi relativi
           document.LoadInserisciDecretoSospensione.<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>.value='';
           document.LoadInserisciDecretoSospensione.descrMotivoOE.value='';
           document.LoadInserisciDecretoSospensione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>.value='-';
           document.LoadInserisciDecretoSospensione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>.value='';
           document.LoadInserisciDecretoSospensione.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>.value='';           
        }
      }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
     <font class="campo">Decreto di Irreperibilità</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" name="LoadInserisciDecretoSospensione" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciDecretoSospensione">
 		<table>
      <tr>
        <td class="l">Decreto di sospensione emesso in data</td>
        <td class="L">
         <font class="campo">
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
         </font>
       </td>
        <td class="l">Decreto di irreperibilità emesso in data</td>
        <td class="L">
           <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
           <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
           <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
      </tr>
     </table>
     <table width="100%">
     <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
     <tr>
     <td class="l">Magistrato</td>
     <td class="L" colspan="3">
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
       <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <a href="Javascript:ListaMagistrati('LoadInserisciDecretoSospensione');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="Titolo" colspan=6>Destinatari</td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table width="100%">
          <tr>
            <td class="l">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
        <table width="100%">
          <tr><td class="l">Autorità delegata alla notifica </td>
          <td class="L" colspan="3">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsterna%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciDecretoSospensione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
        <td class="l">Indirizzo</td>
        <td  class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=30></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
</table>
<table>
  <tr>
    <td class="l">
    	Contestuale Richiesta Restituzione Decreto di Sospensione 
    </td>
    <td class="l">
	    <input type="checkbox" name="<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>" onclick="VisualizzaOE();">
    </td>
  </tr>
</table>
<%
//==============================================================================
// Sezione della restituzione dell'ordine di esecuzione
//==============================================================================
%>
<div id="divOrdineEsecuzione" style="display:none; position:relative;">
	<table style="width: 95%;  border: 0;">
	  <tr>
	    <td class="Titolo" colspan="4">Restituzione Decreto Sospensione</td>
	  </tr>
		<tr>
		<td colspan="4">
			<table>
			  <tr>
			    <td class="l">
			      <a href="Javascript:ListaOrdiniEsecuzione('LoadInserisciDecretoSospensione');">
			        Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
			      </a>
			    </td>
			  </tr>
			  <tr>
			    <td class="l"> Data Emissione </td>
			    <td class="l">
			      <input Title="Data Emissione" name="<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>" type="text" size="10" maxlength="10" READONLY >
			    </td>
			    <td class="l"> Oggetto </td>
			    <td class="l" >
			      <input Title="Oggetto" name="descrMotivoOE" type="text" size="50" maxlength="50" READONLY>
			    </td>
			  </tr>
			</table>
		</td>
	</tr>
  <tr>
    <td class="Titolo" colspan="4">Autorità per la Restituzione</td>
  </tr>
  <tr>
    <td class="l">Destinatario per esecuzione </td>
    <td class="L" colspan="3">
      <select Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>">
        <%=codiceAutoritaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>" maxlength="35" size="35" READONLY>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>"  cols="30" READONLY></textarea>
    </td>
  </tr>
</table>
</div>
<div id="divConferma" style="display:block; position:relative;">
	<table style="width: 95%;">
	  <tr>
	    <td class="lNoBord" colspan="2">
	      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
	    </td>
	  </tr>
	</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciDecretoSospensione");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
</script>
</body>
</html>