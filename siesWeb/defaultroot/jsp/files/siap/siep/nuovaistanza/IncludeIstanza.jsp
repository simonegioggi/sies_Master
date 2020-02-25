<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Date"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>

<jsp:useBean id="nuovaistanza" scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="autorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAvvocato" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoinserimento" scope="request" class="java.lang.String" />

<%
NuovaIstanzaModel lIst = nuovaistanza;
// if(lIst == null)
// 	lIst = new NuovaIstanzaModel();

// Modello fascicolo siep inserito per gestione data irrevocabilità
// Al momento la data viene solo immessa, non letta dal fascicolo
FascicoloSiepModel lFasMod = new FascicoloSiepModel();

AvvocatoModel lAvvPre = lIst.getAvvocatoPresentante();
if(lAvvPre == null)
	lAvvPre = new AvvocatoModel();

AvvocatoModel lAvv = lIst.getAvvocato();
if(lAvv == null)
	lAvv = new AvvocatoModel();

String lFlaP ="checked";
String lFlaD ="";
if("D".equals(lIst.getFlagPresdep()))
{
	lFlaP ="";
	lFlaD ="checked";
	
}

DateUtils.getSysDate();
Date lDataNomina = DateUtils.getSysDate();
if(lIst.getDataNotificaAvvocato() != null)
{
	lDataNomina = lIst.getDataNotificaAvvocato();
}


%>

  <input type="HIDDEN" name="<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>" value="<%=lIst.getIdNuovaIstanza()%>">

  <table width="100%">   
    <tr>
      <td class="titolo">Dati Dell'Istanza</td>
         <td class="titolo" align="center">  
            <font class="titolo"> Pervenuta  </font><input type="radio"  <%=lFlaP %> name="<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>" value="P" onClick ="Javascript:gestioneTipoIstanza()">             
            <font class="titolo"> Depositata  </font><input type="radio" <%=lFlaD %> name="<%=ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>" value="D" onClick ="Javascript:gestioneTipoIstanza()">             
         </td>                          
    </tr>  
  </table>
 <div id="divpervenuta"  style="display:block; position:relative; " >   
   <table width="100%">    
    <tr>
      <td class="l" >Pervenuta in data <font class="ob">(*)</font></td>
      <td class="l" colspan="3"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"dd")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"MM")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_ISTANZA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"yyyy")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_ISTANZA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Mittente</td>
      <td class="l" > 
        <select name="<%=ICostantiNuovaIstanza.CAMPO_COD_AUTORITA_MITTENTE %>">
           <%=autorita%>
        </select>&nbsp;&nbsp;
      <td class="l"> 
        <input type="text" maxlength="100" size="35"  value="<%=StringUtils.toStringJSP(lIst.getDescrMittente()) %>"
               name="<%= ICostantiNuovaIstanza.CAMPO_DESCR_MITTENTE %>" > 
      </td> 
    </tr>
    <tr>
      <td class="l">Sede Mittente</td>
      <td class="l" colspan="3">
        <input title="Sede Mittente"  value="<%=StringUtils.toStringJSP(lIst.getDescrSedeMittente()) %>" type="text" name="<%=ICostantiNuovaIstanza.CAMPO_COD_SEDE_MITTENTE%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciSentenza','<%=ICostantiNuovaIstanza.CAMPO_COD_SEDE_MITTENTE%>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>      
    </tr> 
  </table>

  <table>
    <tr><td>&nbsp;</td>
    </tr> 
  </table>

 </div>
 <div  id="divdepositata"  style="display:none; position:relative; " >   
     <table width="100%">   

    <tr>
      <td class="l">Depositata in data <font class="ob">(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"dd")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_DEPOSITO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"MM")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_DEPOSITO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIst.getDataIstanza(),"yyyy")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_DEPOSITO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <%if(lIst.getDataIstanza()==null) {%>
	      <td class="l">Data Iscrizione</td>
    	  <td class="l">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(),"dd/MM/yyyy HH:mm:ss"))%>
       	</td>       
      <%} else {%>
	      <td class="l"></td>
    	  <td class="l"></td>
      <%}%>
    </tr>
    <tr>
      <td class="l">Soggetto Presentante</td>
      <td class="l" colspan="3"> 
        <input type="text" maxlength="100" size="50" 
               value="<%=StringUtils.toStringJSP(lIst.getSoggPresentante()) %>"
               name="<%= ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE %>" > 
      </td> 
    </tr>
    <tr>
      <td class="l">Identificato con</td>
      <td class="l" colspan="3"> 
        <input type="text" maxlength="100" size="50" 
               value="<%=StringUtils.toStringJSP(lIst.getSoggPresentanteIdentificato()) %>"
               name="<%= ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE_IDENTIFICATO %>"  
              
               > 
      </td> 
    </tr> 
     <tr>
      <td class="l">Presentato da difensore</td>
      <td class="l" colspan="3"> 
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <input type="text" maxlength="38" size="50" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lAvvPre.getNome())%>&nbsp;<%=StringUtils.toStringJSP(lAvvPre.getCognome())%>"
               name="<%= ICostantiNuovaIstanza.CAMPO_NOME_COGNOME_AVVOCATO %>"> 

      <a href="Javascript:ListaAvvocati('LoadInserisciSentenza','parziale');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
      </a>
  <%if (nuovaistanza.getAvvocatoPresentante()== null) {%>
   <input type="hidden" value="" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>">      
  <%} else { %>
   <input type="hidden" value="<%=lAvvPre.getIdAvvocato() %>" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>">      
  <%}%>    
    </td>           
    </tr> 
 
</table>
</div>
<table  width="100%">
    <tr>
      <td class="titolo" colspan="4">Avvocato</td>                        
    </tr> 
  <tr>
    <td class="l" colspan="4">
      <a href="Javascript:ListaAvvocati('LoadInserisciSentenza','completo');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
  </tr>  
  <%if (nuovaistanza.getAvvocato()== null) {%>
   <input type="hidden" value="" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>">
  <%} else { %>
   <input type="hidden" value="<%=lAvv.getIdAvvocato() %>" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>">
  <%}%>    
     <tr>
      <td class="l">Cognome</td>
      <td class="l" > 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lAvv.getNome()) %>" name="<%=ICostantiAvvocato.CAMPO_COGNOME %>"> 
      </td> 
      <td class="l">Nome</td>
      <td class="l" > 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lAvv.getCognome()) %>" name="<%= ICostantiAvvocato.CAMPO_NOME %>"> 
      </td>       
    </tr> 
    <tr>
      <td class="l">Foro di competenza</td>
      <td class="l" colspan="3"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lAvv.getForo()) %>" name="<%= ICostantiAvvocato.CAMPO_FORO %>"> 
      </td>       
    </tr> 
    <tr>
      <td class="titolo" colspan="4">Eventuale nomina del difensore di fiducia</td>                        
    </tr>       
    <tr>
      <td class="l">Tipo di difensore</td>
      <td class="l"> 
        <select name="<%=ICostantiNuovaIstanza.CAMPO_TIPO_AVVOCATO %>">
           <%=tipoAvvocato%>
        </select>         
      </td>   
      <td class="l">Nomina in data</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataNomina,"dd")) %>" 
               name="<%= ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataNomina,"MM")) %>" 
               name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataNomina,"yyyy")) %>" 
               name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>           
    </tr>     
    <tr>
      <td class="titolo" colspan="4">Contenuto</td>                        
    </tr>              
    <tr>
      <td class="l">Contenuto</td>
      <td class="l" colspan="3"> 
        <select name="<%=ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO %>">
           <%=contenuto%>
        </select>         
      </td> 
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="l" colspan="3"> 
        <TEXTAREA cols="80" rows="3" name="<%= ICostantiNuovaIstanza.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lIst.getNote()) %></textarea><%-- maxlength="2000" --%>
      </td> 
    </tr>
    <%-- if(("sentenza".equals(tipoinserimento)) || ("soggetto".equals(tipoinserimento)) || ("nuovo".equals(tipoinserimento)) ) {   %>
    <tr>
      <td class="l" >Data Irrevocabilità </td>
      <td class="l" colspan="3"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIrrevocabilita(),"dd")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIrrevocabilita(),"MM")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIrrevocabilita(),"yyyy")) %>" 
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <% } --%>
  </table>
