<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="lPenComSanSost"      scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="misurecautelari"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="lAnnotazione"        scope="request" class="java.lang.String"/>
<jsp:useBean id="lSedeUfficio"        scope="request" class="java.lang.String"/>
<jsp:useBean id="residenzaassociata"  scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
if(lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
if(lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if(lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();

PenaComplessivaSanzioneSostitutivaModel lPenaComplessSSMod = lPenComSanSost;
// if(lPenaComplessSSMod == null)
// 	lPenaComplessSSMod = new PenaComplessivaSanzioneSostitutivaModel();

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Trasmissione Atti per l'esecuzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    var desktop;
	function ListaUDS(a_formname, a_fieldname) {
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+
				a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    function ListaUDSMIN(a_formname, a_fieldname, a_typename) {
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+
				a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,
				"Ricerca_UDSMIN", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	}
    <%-- MERGE v10 COLLAUDO: aggiunta funzione di ricerca --%>
    function sceltaLista() {
		if (document.getElementById('<%=MinorMask.ComboMagistratoId%>').value == 'UDSM')
			ListaUDSMIN('LoadInserisciTrasmissioneAttiEsecuzione','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>', document.getElementById('<%=MinorMask.ComboMagistratoId%>').value);
		else
			ListaUDS('LoadInserisciTrasmissioneAttiEsecuzione','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>');
    }

// magistrato competente
  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    var des;
    des = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

   function Verify()
   {
      if (document.LoadInserisciTrasmissioneAttiEsecuzione.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO%>.value == '')
      {
         alert("Il Campo Sede è obbligatorio");
         return false;
      }  
   }
  </script>
  </head>

 <body class="corpo">
	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    			</a>
    		</td>
      		<td class="LBG">
      			<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Trasmissione Atti per l'Esecuzione</font>
     	 	</td>
    	</tr>
  	</table>
 	<br>
   	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>

  	<FORM method="POST" name="LoadInserisciTrasmissioneAttiEsecuzione" action="<%= IWebConstants.PG_MAIN%>">
  		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciTrasmissioneAttiEsecuzione">
  		<table>
    		<tr>
      			<td class="l">Posizione Giuridica </td>
      			<td class="L" colspan=5>
          			<font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%     }
       else
       {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%     }%>
         </font>
         
<%if(lPosizione.isLibero() && residenzaassociata != null && residenzaassociata.getResidenza()!= null){%>   
      Residenza 
          <font class="campo">

         <%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%>

         </font>

 <%} %>           
   </td>
  

</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>
 
<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }

      PenaComplessivaModel lPenCompMod=lPenaComplessSSMod.getPenaComplessiva();
      if(lPenCompMod!=null)
      {
      %>
		  <tr>
		    <td class="L">
          <font class="label">Pena irrogata in sentenza : </font></td>
         <td class="L" colspan="5">
          <%
          if (   (lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
        		 )
          {%>
          <font class="campo">Reclusione</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {%>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenCompMod.getCodTipoPenaDetentiva().equals("04")){%>
              <%if(lPenCompMod.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }

if(lPenaComplessSSMod!=null)
{
SanzioneSostitutivaModel lSanSos = lPenaComplessSSMod.getSanzioneSostitutiva();
if(lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null)
{
%>

<tr>
<td class="L"><font class="label">Sanzione Sostitutiva applicata: </font></td>
<td class="L" colspan="5">
<%
if((lSanSos.getNumAnni()!=null && lSanSos.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumMesi()!=null && lSanSos.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumGiorni()!=null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0))!=0))
{
%>

<font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;</font>
<font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>

<%
}

if(lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Multa&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
<br>
<%
}

if(lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Ammenda&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
}
%>

</td>
</tr>
<%
}
}  

 if(misurecautelari != null && !misurecautelari.isEmpty())
 {
%> 
    <tr>
      <td class="l">Misure Cautelari Computate:</td>
 <%
     MisuraCautelareModel lMisCauMod = null;
     Iterator lItx = misurecautelari.iterator();
     int conta = 0;
     while (lItx.hasNext())
     {
       lMisCauMod = (MisuraCautelareModel) lItx.next();
       if (lMisCauMod.getFlagComputabile().equals("S") && lMisCauMod.getDataFine()!=null )
       {
         conta++;
         
         if (conta ==1){
%>
  <td class="l" colspan="5">
    <font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getDescrTipoMisura())%>&nbsp;</font>
    <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumAnni(), "0")%>&nbsp;</font>
    <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumMesi(), "0")%>&nbsp;</font>
    <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumGiorni(), "0")%></font>
  </td>
<%
         }
         else
         { // devo scrivere un nuovo rigo
%>
</tr>
<tr>
  <td class="l">&nbsp;</td>
  <td class="l" colspan="5">
    <font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getDescrTipoMisura())%>&nbsp;</font>
    <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumAnni(), "0")%>&nbsp;</font>
    <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumMesi(), "0")%>&nbsp;</font>
    <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lMisCauMod.getNumGiorni(), "0")%></font>
  </td>
<%
         } 
       } 
     }
%>   
     </tr>  
     
 <%}    
     
  if(penaresidua.getIdPenaResidua() != null)
    {
%>
<tr>
  <td class="l">Pena da espiare:</td>
<% 
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l" colspan=2>Reclusione
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
         Multa
          <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }

    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
    
      Ammenda
      <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
   
%>
</tr>
<%
     
    }
%>

<%
  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare:</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%}      
%>
  </table>
  <br>
  <table width='100%'>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L">
          <input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input title = "Giorno Data Trasmissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>

      </tr>
   <tr>
     <td class="Titolo" width="100%" colspan=6> Magistrato </td>
   </tr>
   <tr>
     <td class="l">Magistrato
     <td class="L" colspan="3">
       <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciTrasmissioneAttiEsecuzione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
     </td>
     <td>
     </td>
  </tr>      
   <tr>
      <td class="l" colspan="4">
 <%if(lAnnotazione != null && lAnnotazione.equals("S") ) 
 {%>
             <input type="checkbox" checked name="ritrasmissione" value="S"> Ritrasmissione in seguito a restituzione atti    
 
 <%}else{ %>
              <input type="checkbox" name="ritrasmissione" value="S"> Ritrasmissione in seguito a restituzione atti    
 <%} %>
       </td>
    </tr>      
	<tr>
		<td class="l">Destinatario</td>
      	<td class="l" colspan="3">
			<%-- MERGE v10 COLLAUDO: sostituita scritta fissa con combo --%>
			<%--
			<font class="campo">UFFICIO DI SORVEGLIANZA</font>
			<input type="hidden" name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_UFFICIO_%>" value="UDS">
			--%>
			<%=MinorMask.comboMagistrato("true", MinorMask.SorveglianzaUfficio)%>
       	</td>
	</tr>
    <tr>
		<td class="l">Sede</td>
      	<td class="l" colspan="3">
        	<font class="campo">
          		<input Title="Luogo Ufficio Sorveglianza" value="<%=lSedeUfficio %>" name="<%= ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO %>" size=35 type="text">
          		<%-- MERGE v10 COLLAUDO: sostituita ricerca fissa con scelta a seconda del destinatario --%>
          		<a href="Javascript: sceltaLista();">
            		<img src="/images/filefolder.gif" border=0>
          		</a>
        	</font>
      	</td>
	</tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciTrasmissioneAttiEsecuzione");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  
//Controlli Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");

 </script>
</body>
</html>
	