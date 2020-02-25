function ControllaData(Data)
                {
                        if (Data.length!=10) return false;

                        day=Data.substring(0,2);
                        month=Data.substring(3,5);
                        year=Data.substring(6,10);
                        var Miadata = new Date(year, month-1, day);
                        var rgiorno=Miadata.getDate();
                        var rmese=Miadata.getMonth();
                        var ranno=Miadata.getYear();
                        return (((day==rgiorno) && (day>0)) && ((month-1==rmese) && (month>0)) && year>0);
}

function ControllaDataPassaVuota(Data)
                {
                        if (Data.length==2) return true;
                        if (Data.length!=10) return false;

                        day=Data.substring(0,2);
                        month=Data.substring(3,5);
                        year=Data.substring(6,10);
                        var Miadata = new Date(year, month-1, day);
                        var rgiorno=Miadata.getDate();
                        var rmese=Miadata.getMonth();
                        var ranno=Miadata.getYear();
                        return (((day==rgiorno) && (day>0)) && ((month-1==rmese) && (month>0)) && year>0);
}

function DifferenzaDateInGiorni(data1, data2)
{
  //le date DEVONO ESSERE formalmente corrette
  //La funzione ritorna la differenza, in giorni, delle date
  day   = data1.substring(0,2);
  month = data1.substring(3,5);
  year  = data1.substring(6,10);
  var dateStart = new Date(year, month-1, day);
  day   = data2.substring(0,2);
  month = data2.substring(3,5);
  year  = data2.substring(6,10);
  var dateEnd = new Date(year, month-1, day);
  
  var diff = dateEnd.getTime() - dateStart.getTime();
  var days = diff / (1000 * 60 * 60 * 24);
  return days;
}

function CompareDate(Data1,Data2)
{
  /*N.B. : qui si assume  che le date in input siano formalmente corrette
    Bisogna quindi passare per la ControllaData in anticipo!!!
    La funzione torna true se la prima data e' <= della seconda, false otherwise*/
  day=Data1.substring(0,2);
  month=Data1.substring(3,5);
  year=Data1.substring(6,10);
  var Miadata1 = new Date(year, month-1, day);
  day=Data2.substring(0,2);
  month=Data2.substring(3,5);
  year=Data2.substring(6,10);
  var Miadata2 = new Date(year, month-1, day);
  if (Miadata1<=Miadata2)
    return true;
  return false;
}

function CompareDateStrettamente (Data1,Data2)
{
  /*N.B. : qui si assume  che le date in input siano formalmente corrette
    Bisogna quindi passare per la ControllaData in anticipo!!!
    La funzione torna true se la prima data e' > della seconda, false otherwise*/
  day=Data1.substring(0,2);
  month=Data1.substring(3,5);
  year=Data1.substring(6,10);
  var Miadata1 = new Date(year, month-1, day);
  day=Data2.substring(0,2);
  month=Data2.substring(3,5);
  year=Data2.substring(6,10);
  var Miadata2 = new Date(year, month-1, day);
  if (Miadata1>Miadata2 || Miadata1<Miadata2)
    return true;
  return false;
}

function DateComparison()
{

       ArrivalDate = new Date(document.frmsample.txtArivalDate.value)
       DepDate = new Date(document.frmsample.txtDepDate.value)

       yyyyArrival = ArrivalDate.getFullYear();
       yyyyDep=DepDate.getFullYear();

       mmFrom=ArrivalDate.getMonth();
       mmTo=DepDate.getMonth();
       mmFrom+=1;
       mmTo+=1;

       ddFrom=ArrivalDate.getDate();
       ddTo=DepDate.getDate();

       if (yyyyTo < yyyyFrom)
          {
              return(false);
          }
       else if(mmTo<mmFrom && yyyyTo==yyyyFrom)
          {
              return(false);
          }

       else if(ddTo<ddFrom && yyyyTo==yyyyFrom && mmTo==mmFrom)
          {
              return(false);
          }
    return(true);
}

// Funzione di Filling del parametro "objValue" in cui viene impostato il contenuto dei campi GG e MM delle forms SIAP.
// Consente di rimpiazzare ad es. '8' con '08', e inoltra un alert se si inserisce un dato non numerico.
function FillDM(objValue)
{
    if (typeof (objValue) == "undefined" || objValue.length == 0)
        return objValue;

    var charpos = objValue.search("[^0-9]");
    if (charpos >= 0)
    {
      //alert("Il Campo deve essere numerico");
      return objValue;
    }
    else if (objValue.length==1)
      return ('0'+objValue);
    else
      return objValue;
}

// Funzione di Filling del parametro "objValue" in cui viene impostato il contenuto del campo ANNO delle forms SIES.
// Consente di rimpiazzare ad es. '04' con '2004'.
function FillYear(objValue)
{
    if (typeof (objValue) == "undefined" || objValue.length == 0)
        return objValue;

    var charpos = objValue.search("[A-Z,a-z]");
    if (charpos >= 0)
    {
      return objValue;
    }
    else if (objValue.length==2)
    {
      if (objValue > 50)
        return ('19'+objValue);
      else
        return ('20'+objValue);
    }
    else return objValue;
}

// Funzione utile per realizzare lo Skip automatico del campo. Realizzata per i campi GG, MM e AAAA che compongono le date delle forms SIES.
// Attivata con l'aggiunta di : "ONKEYUP=SkipField('CurrentFieldName','NextFieldName')" all'interno del TAG di <input>..
var CurrentNode;
var NextNode;
function SkipField(CurrentFieldName, NextFieldName)
{
  CurrentNode=document.getElementById(CurrentFieldName);
  NextNode=document.getElementById(NextFieldName);
  if (CurrentNode.value.length == CurrentNode.size)
    NextNode.focus();
}

// Questa funzione setta il focus all'oggetto successivo quando si raggiunge la dimensione
//massima del campo. NB:Successivo oggetto di all[]!!! Permette solo l'inserimento di caratteri numerici
// Attivata con l'aggiunta di : "ONKEYPRESS=return TicTabNumField(this,event)" all'interno del TAG di <input>.
function TicTabNumField(oToSkip,e){
    
    var self = this;
    
    if (!e) var e = window.event;
    if (e.keyCode == 8 || e.keyCode == 9 || e.keyCode == 46 || e.keyCode == 13)
        return true;
    if (( e.keyCode > 47 && e.keyCode < 58)){
       var nNodo = 0
       nNodo= oToSkip.sourceIndex+1
       window.setTimeout(function(){self.TicTabNumFieldChild(oToSkip,nNodo);}, 0);
    }else{
       return false;
    }
}

function TicTabNumFieldChild(CurrentNode,sID){
       
      if (!isNaN(sID)){
      	
       //if (CurrentNode.value.length == CurrentNode.size && document.all[sID].type != 'hidden') {
 			if (CurrentNode.value.length == CurrentNode.size && document.all[sID].type == 'text') {
		   	   document.all[sID].focus();
         	}
      }
}

// This function set the focus to the textbox so that when a user types, it will replace the selected text
// Attivata con l'aggiunta di : "ONFOCUS=textboxSelect(this)" all'interno del TAG di <input>.
function textboxSelect (oTextbox, iStart, iEnd) {
   switch(arguments.length) {
       case 1:
           oTextbox.select();
           break;

       case 2:
           iEnd = oTextbox.value.length;
           /* falls through */

       case 3:
           if (isIE) {
               var oRange = oTextbox.createTextRange();
               oRange.moveStart("character", iStart);
               oRange.moveEnd("character", -oTextbox.value.length + iEnd);
               oRange.select();
           } else if (isMoz){
               oTextbox.setSelectionRange(iStart, iEnd);
           }
   }
   oTextbox.focus();
}

