<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCumulo" scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffrecrediti"     scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo"     scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="sedeCumulo"     scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="EsitoTrasmissione"		scope="request"  class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>
<jsp:useBean id="StessoUfficio"     	scope="request" class="java.lang.String"/>
<%-- MEV_66: aggiunti useBean per gestione combo tds ed uds --%>
<jsp:useBean id="ufficioMdS"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioTdS"   			scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  Boolean lAssorbimentoCum = false;
  if(EsitoTrasmissione!=null && EsitoTrasmissione.getIdEsitoTrasmissione()!=null)
  {
	  lAssorbimentoCum = true;
  }
  
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Definizione Procedimento - Perdità di competenza</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
	function Verify() {
        //DATA EMISSIONE
        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value.length==1)
          document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value='0'+document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value;
        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value.length==1)
          document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value='0'+document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value+'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value+'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>.value;

        if (!ControllaData(data_to_verify)) {
          alert('Data Provvediemnto di cumulo non valida');
          document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.focus();

          return false;
        }

      	if (!document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled) {
        	if (document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>[document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.selectedIndex].value == '-') {
          alert("Il Campo Ufficio che ha emesso il cumulo è obbligatorio");
          return false;
        }
      }  
      	if (!document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled) {
        	if(document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE %>.value == "") {
          alert("Il Campo Sede Ufficio che ha emesso il cumulo è obbligatorio");
          return false;
        }
      }  

        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>.value == "") {
          alert("Anno Procedimento SIEP è obbligatorio");
          return false;
        }

        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>.value == "") {
          alert("Numero Procedimento SIEP è obbligatorio");
          return false;
        }


        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify)) {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }

        if (document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == "") {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if (document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        <%-- MEV_66: aggiunti controllo di sicurezza --%>
        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked) {
	        if (document.f.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value != '') {
	  			if (document.f.ufficioTds[document.f.ufficioTds.selectedIndex].value == '-') {
			        alert("Il campo Tribunale di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
			        document.f.ufficioTds.focus();
			        return false;
	      		}
	    	}
	
			if (document.f.ufficioTds[document.f.ufficioTds.selectedIndex].value != '-') {
	  			if (document.f.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value == '') {
			        alert("Il campo Sede Tribunale di Sorveglianza e' obbligatorio, se il campo Tribunale di Sorveglianza e' valorizzato!");
			        document.f.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.focus();
			        return false;
	      		}
      }

			if (document.f.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value != '') {
	  			if (document.f.ufficioMdS[document.f.ufficioMdS.selectedIndex].value == '-') {
			        alert("Il campo Magistrato di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
			        document.f.ufficioMdS.focus();
			        return false;
	      		}
	    	}

			if (document.f.ufficioMdS[document.f.ufficioMdS.selectedIndex].value != '-') {
	  			if (document.f.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value == '') {
			        alert("Il campo Sede Magistrato di Sorveglianza e' obbligatorio, se il campo Magistrato di Sorveglianza e' valorizzato!");
			        document.f.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.focus();
			        return false;
	      		}
	    	}
		}
		<%-- FINE MEV_66 --%>

		return true;
	}

//funzioni
         var desktop;
	function ListaComuniUff(a_formname,a_fieldname,codTipoUfficio) {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

	function ListaComuni(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

	function ListaUDS(a_formname,a_fieldname) {
		<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
  		var codTipoSede = document.f.ufficioMdS[document.f.ufficioMdS.selectedIndex].value;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+codTipoSede, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

	function ListaComuniTds(formname,fieldname) {
		<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
  		var codTipoSede = document.f.ufficioTds[document.f.ufficioTds.selectedIndex].value;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

	function ListaMagistrati(a_formname) {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

	function radio() {
       var nodedestinatari =document.getElementById('divdestinatari');
       var nodebottone = document.getElementById('divbottone');
       var nodebottoneconferma = document.getElementById('divbottoneconferma');
       var nodeufficio = document.getElementById('divufficio');
        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked) {
          nodebottoneconferma.style.display='block';
          nodebottone.style.display='none';
          nodeufficio.style.display='none';
          document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled=true;
          document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled=true;
          nodedestinatari.style.display='none';
        } else if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked) {
          nodebottone.style.display='block';
          nodebottoneconferma.style.display='none';
          nodeufficio.style.display='block';
          document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled=false;
          document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled=false;
          nodedestinatari.style.display='block';

        }

   }
	
	function radioIni()
    {
		var lStesso = '<%=StessoUfficio%>';
		//alert('radioIni'+lStesso);
		
		if(lStesso == "SI")
		{	
       		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked = true;
       		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked = false;
        }
       else if(lStesso == "NO")
       {
      	 	document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked = false;
     		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked = true;
       }

		radio();
    }

	function Altro() {
       document.f.tipobottone.value='altro';
       	if (Verify()) {
        document.f.I.disabled=true;
        document.f.A.disabled=true;
        document.f.submit();
       }

   }


    </script>
  </head>
  <body class="corpo" onload="radioIni();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Perdita di competenza</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciAssorCumulo">
    <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <input type="HIDDEN" name="tipobottone" value="">

    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }
%>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
    if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 </td>
<%            }
        }
      }
}
%>

     </tr>
   <tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
    </tr>
  </table>
  <br>
  <table style="width: 100%;">
    <tr>
      <td colspan=5 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
     <td class="l" colspan="5">
         Assorbimento cumulo stesso ufficio &nbsp;<input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="0019" onclick="radio();" checked>
         &nbsp; Assorbimento cumulo altro ufficio  &nbsp; <input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="0022" onclick="radio();">
      </td>
    </tr>
    <tr>
      
<%  if(lAssorbimentoCum)
	{	%>
	  <td class="l" width="22%">Data Assorbimento in cumulo <font class=ob>(*)</font></td>	
      <td class="l" colspan="4">
        <input type="text" Title="Giorno Cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"dd"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"MM"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"yyyy"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%	}
	else
	{	%>
	  <td class="l" width="22%">Data Provvedimento di cumulo <font class=ob>(*)</font></td>	
	  <td class="l" colspan="4">
        <input type="text" Title="Giorno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"dd"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"MM"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"yyyy"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%	} %>
	      
    </tr>
</table>
<div id="divufficio" style="width: 100%; display:none; position:relative; ">
<table style="width: 100%;">   
    <tr>
      <td class="l" width="22%">Ufficio che ha emesso il cumulo <font class=ob>(*)</font></td>
      <td class="L" colspan="4">
        <select  Title="Ufficio"  name="<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>">
         <%=autoritaCumulo%>
         </select>
      </td>
    </tr>
    <tr>
      <td class="l" width="22%">Sede <font class=ob>(*)</font></td>
     <td class="L" colspan="4">
          <input title="Sede Ufficio"  value="<%=sedeCumulo%>" type="text" name="<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuniUff('f','<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>',document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>[document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0"></a>
      </td>
    </tr>
</table>
</div>
<table style="width: 100%;">    
    <tr>
      <td class="l" width="22%">Numero Procedimento SIEP <font class=ob>(*)</font></td>
      <td class="l" colspan="4">
 <% if(lAssorbimentoCum)
	{	%>     
         <input Title="Anno Cumulante" value="<%=StringUtils.toStringJSP(EsitoTrasmissione.getChiaveAnno())%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>" type="text" size="4" maxlength="4"> /
         <input Title="Numero Cumulante" value="<%=StringUtils.toStringJSP(EsitoTrasmissione.getChiaveProgr())%>" name="<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>" type="text" size="6" maxlength="6">
<%	}
 	else
 	{	%>
 		<input Title="Anno Unione" value="<%=StringUtils.toStringJSP(fascicolo.getAnnoFascicoloUnione())%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>" type="text" size="4" maxlength="4"> /
         <input Title="Numero Unione" value="<%=StringUtils.toStringJSP(fascicolo.getNumFascicoloUnione())%>" name="<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>" type="text" size="6" maxlength="6">
 <%	} %>		      
      </td>
    </tr>
    <tr>
      <td class="l" width="22%">Data Definizione <font class=ob>(*)</font></td>
      <td class="l" colspan="4">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
       <td rowspan=2 class="l" width="22%">Motivazione</td>
       <td rowspan=2 class="L" colspan="4">
          <textarea title="Motivazione" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=4></textarea>
       </td>
    </tr>
</table>
<table style="width: 100%;">
    <tr>
      <td class="titolo" style="width: 100%;" colspan=2>Magistrato firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario <font class=ob>(*)</font></td>
      <td class="L">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border="0">
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
</table>
<div id="divdestinatari" style="width: 100%; display:none; position:relative;">
<table style="width: 100%;">
  <tr>
    <td colspan=4 class="titolo">Destinatari</td>
  </tr>

  <tr>
      <td class="l" width="22%">Ufficio recupero crediti presso</td>
      <td class="L">
        <select  Title="Ufficio recupero crediti" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          <%=uffrecrediti%>
         </select>
      </td>
     <td class="l">di</td>
     <td class="L">
          <input title="Sede Ufficio recupero crediti"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="25">
           <a href="Javascript:ListaComuniUff('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>',document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>[document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
  </tr>

  <tr>
      <td class="l" width="22%">Autorità di polizia</td>
      <td class="L" colspan="3">
        <select  Title="Autorita" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
         <%=codiceAutorita%>
         </select>
      </td>
    </tr>
    <tr>
     <td class="l" width="22%">Sede</td>
     <td class="L">
          <input title="Sede Autorita"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
         <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      </td>
    </tr>
    <tr>
		<%-- MEV_66: aggiunte combo per gestione tds ed uds --%>
      <td class="l" width="22%">Magistrato di Sorveglianza</td>
      <td class="L" colspan="3">
      		<select title="Magistrato di Sorveglianza" name="ufficioMdS"><%=ufficioMdS%></select>
   			&nbsp;Sede&nbsp;
       		<input title="Sede Magistrato di Sorveglianza" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="35">
       		<a href="Javascript:ListaUDS('f','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
       			<img src="/images/filefolder.gif" border="0">
       		</a>
      </td>
    </tr>
    <tr>
       <td class="L" width="22%">Tribunale di Sorveglianza</td>
       <td class="L" colspan="3">
       		<select title="Tribunale di Sorveglianza" name="ufficioTds"><%=ufficioTdS%></select>
   			&nbsp;Sede&nbsp;
         <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuniTds('f','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
         		<img src="/images/filefolder.gif" border="0">
         	</a>
       </td>
    </tr>
    <tr>
      <td class="l" width="22%">Altra Autorità</td>
      <td class="L" colspan="3">
        <select  Title="Autorita" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
         <%=codiceAutorita%>
         </select>
      </td>
    </tr>
    <tr>
     <td class="l" width="22%">Sede</td>
     <td class="L">
          <input title="Sede Autorita"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
          		<img src="/images/filefolder.gif" border="0">
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"  cols=30 ></textarea>
       </td>
    </tr>

    <tr>
      <td class="l" width="22%">Altra Autorità </td>
       <td class="l" colspan="3">
       <font class="campo">
        <input Title="Altra Autorità" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  size=70 type="text">
        </font>
       </td>
    </tr>

</table>
</div>
<div id="divbottoneconferma" style="display:none; position:relative;">
<table style="width: 100%;">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</div>
<div id="divbottone" style="display:none; position:relative; ">
<table style="width: 100%;">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>

      <td class="lNoBord" colspan="2">
        <br><INPUT onclick="Javascript:Altro();" class="bottone" type="button" name="A" value="Altro Destinatario">
      </td>
    </tr>
  </table>
</div>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","lt=2099");

//data emissione
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","maxlen=4","La lunghezza massima per l'Anno provvedimento cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","minlen=4","La lunghezza minima per l'Anno provvedimento cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","numeric");


//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>