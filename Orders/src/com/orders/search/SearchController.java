package com.orders.search;

import com.orders.controllers.ProposalController;
import com.orders.facade.ProductFacade;
import com.orders.facade.ProposalFacade;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;
import org.orders.entity.Product;
import org.orders.entity.Proposal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="searchController")
@SessionScoped
public class SearchController {
    private static Logger _log = Logger.getLogger(SearchController.class.getName());

    private String searchString;
    private List<Proposal> searchproposals;
    private List<Product> productList;

    @EJB
    private ProposalFacade proposalFacade;
    @EJB
    private ProductFacade productFacade;

    @ManagedProperty("#{proposalController}")
    ProposalController proposalController;

    @PostConstruct
    public void init(){
        searchproposals = new ArrayList<Proposal>();
        productList = new ArrayList<Product>();
    }
    public List<String> complete(String query) throws IOException{
        _log.info("Автозаполнение началось:" + query);
        List<String> suggestions = new ArrayList<String>();
        /*productList = productFacade.findAll();
        for(Product p : productList) {
            if(p.getName().startsWith(query))
                suggestions.add(p.getName());
        }*/
        try {
            Directory directory = FSDirectory.open(new File(".//Index")); //где находится индекс
            IndexSearcher is = new IndexSearcher(directory); //объект поиска
            QueryParser parser = new QueryParser(Version.LUCENE_31, "name", new RussianAnalyzer(Version.LUCENE_31));//поле поиска + анализатор
            Query q = parser.parse(query); //что ищем
            _log.info("Запрос состоит из: " + q.toString());
            TopDocs results =is.search(q, null, 10); //включаем поиск ограничиваемся 10 документами, results содержит ...

            for (ScoreDoc hits:results.scoreDocs) { // получаем подсказки
                Document doc = is.doc(hits.doc); //получаем документ по спец сылке doc

                suggestions.add(doc.get("name"));

            }

            directory.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



        return suggestions;
    }

    public void analyze(String text) throws IOException {
        List<String> searchlst = new ArrayList<String>();

        proposalController.getProposalList().clear();
        String query = "";
        System.out.println("Analzying \"" + text + "\"");

            Analyzer analyzer = new RussianAnalyzer(Version.LUCENE_31);
            System.out.println("\t" + analyzer.getClass().getName() + ":");
            System.out.print("\t\t");
            TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

        while (true) {
                if (!stream.incrementToken()) break;

                AttributeSource token = stream.cloneAttributes();
                CharTermAttribute term =(CharTermAttribute) token.addAttribute(CharTermAttribute.class);
                System.out.print("[" + term.toString() + "] "); //2
                searchlst.add(term.toString());

            }

        int i = 0;
        for(String param : searchlst){

            if(i< searchlst.size()-1){
                query += param + " AND ";
            }else{
                query += param;
            }
            i++;
        }

            _log.info("Запрос для поиска:" + query);
            startSearch(query);
            System.out.println("\n");

    }
    public void startSearch(String searchString) throws IOException {

        /*analyze(searchString);*/

        try {
            Directory directory = FSDirectory.open(new File(".//Index")); //где находится индекс
            IndexSearcher is = new IndexSearcher(directory); //объект поиска
            QueryParser parser = new QueryParser(Version.LUCENE_31, "name", new RussianAnalyzer(Version.LUCENE_31));//поле поиска + анализатор
           /* String str1 = "фотоаппарат";
            String str2 = "телевизор";
            String str3 = "SONY";
            String total = "(" + str1 + " OR " + str2 + ")" + " AND " + str3;
            System.out.println(total);*/
            Query query = parser.parse(searchString); //что ищем
            TopDocs results =is.search(query, null, 10); //включаем поиск ограничиваемся 10 документами, results содержит ...
            System.out.println("getMaxScore()="+results.getMaxScore()+" totalHits="+results.totalHits); // MaxScore - наилучший результат(приоритет), totalHits - количество найденных документов

            /*proposalController.getProposalList().clear();*/

            for (ScoreDoc hits:results.scoreDocs) { // получаем подсказки
                Document doc = is.doc(hits.doc); //получаем документ по спец сылке doc

                    for(Proposal proposal: proposalFacade.findPropolsalsByProduct(Long.valueOf(doc.get("recid")))){

                        proposalController.getProposalList().add(proposal);
                        _log.info("Предложение найдено:" + proposal.getRecid().toString() + ",Товар: " + doc.get("recid") + ", " + doc.get("name"));
                    }

                /*System.out.println("doc="+hits.doc+" score="+hits.score);//выводим спец сылку doc + приоритет
                addMessage(doc.get("id") + " | " + doc.get("recid") + " | " + doc.get("name"));//выводим поля найденного документа*/
            }

            directory.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        addMessage("Поиск выполнен");
    }


    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ProposalController getProposalController() {
        return proposalController;
    }

    public void setProposalController(ProposalController proposalController) {
        this.proposalController = proposalController;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
