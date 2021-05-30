# ProiectDiscord  
https://discord.com/developers/applications/831466160020127764/bot  
  
//Add Bot to Server  
https://discord.com/api/oauth2/authorize?client_id=831466160020127764&permissions=8&scope=bot  
  
//Dont forget to check the JavaDoc too  
Technologii folosite: JDA, comunicare cu baza de date(cu interfata Dao), folosirea HashMap si Lista, lucru cu URL-uri (doar cereri GET), tratare exceptii, creare JavaDoc

Functia de baza a aplicatiei:  
 -user-ul cand da o comanda pentru a vedea un feed Rss (ex: ~javapapers) //programul va genera o lista de Article pentru url   
 -user-ul va vedea informatiile din article (de pe pagina 0 initial) intr-un EmbedBuilder cu 4 emoji-ul (<- ; -> ; X ; 🔄)  
 -cu sagetile stanga/dreapta de la emoji user-ul poate schimba pagina (se ia alt article din lista si se transforma intr-un EmbedBuilder si suprascrie vechiul mesaj)  
 -cu X se sterge mesajul  
 -cu 🔄 se face refresh (se da o noua cerere pentru a citi feed-ul rss, si se reinoiesc datele salvate)  
 (//Toate EmbedBuilder-le Articole au in footer adresa Url la feed si index-ul la care se afla articolul in lista sa)  
 (//Cand se da pagina, nu se va da alta cerere de Get la adresa Url, decat daca botul a fost inchis si redeschis intre timp)  
 (//se pot da mai multe pagini din diferite liste de articole in acelasi timp)    
 <br> Explicatii clase si metode:    
datatypes:  
<p align="center">
  ---EmbedBuilder: datatype special de la JDA pentru a afisa mesaje speciale in discord, perfect pentru a afisa un Article--- 
</p>
<p align="center">
  ---Article : datatype care contine urmatoarele variabile String: title, link, description, imageURL---  
</p>
clase importante:
<p align="center">
  ------Commands : aici se analizeaza comenzile de la user si se trateaza potrivit ------  
</p>
<p align="center">
  ------GuildMessageReactionAdd/GuildMessageReactionRemove : interactiunea butoanelor (emoji-urilor)------  
<p align="center">
------RssReader : clasa in care se afla metodele de citire si parsare a feed-ului Rss------ 
</p>  
  metode:    
readRSSFeed/readRedditFeed: trimite o cerere de get la un Url specificat si inputul primit este pus intr-un buffered reader si parsat, returnand o lista de Article  
  <p align="center"><br>
------ArticleManager : clasa care se ocupa cu administrarea listelor de Article obtinute din RssReader-----  
 </p>
-HashMap adressArticles: key-ul este adresa Url de unde se ia feed-ul rss,iar valoarea este lista de Article //folosit ca sa crestem eficienta,sa nu fie nevoie de cereri   constante la get, astfel user-ul poate vedea mai multe liste de Article fara pierderi mari;  
    
metode:  
-getPage(urlAdress,page,createNew) : se ocupa de management-ul HashMap-ului: 
    ->daca adresa Url nu este gasita in hashmap, se apeleaza metoda potrivita din RssReader si rezultatul este adaugat in HashMap  
    ->daca adresa este gasita, se ia valoarea din HashMap si nu mai nevoie sa citim feed-ul din nou  
    ->createNew este boolean daca se vrea reinoita valoarea din hashmap pentru o cheie (folosita in createArticle/button-ul de refresh)  
    ->la final apeleaza si returneaza valoarea din rssPage  
-rssPage(urlAdress,page,articles) : aici se construieste si se returneaza Embed Builder-ul cu datele din Article-ul gasit in lista articles la locatia page, este apelata doar din getPage()  

