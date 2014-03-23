package com.orders.search;

import com.orders.facade.ProductFacade;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.orders.entity.Product;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;

@ManagedBean(name="indexerController")
@SessionScoped
public class IndexerController {

    @EJB
    private ProductFacade productFacade;

    public void startIndexing() {
        try {
            FSDirectory FSD =  FSDirectory.open(new File(".//Index"));  //индекс будем хранить в директории ./Index
            RussianAnalyzer analyzer = new  RussianAnalyzer(Version.LUCENE_31);  //какой используем анализатор

            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31,analyzer); //наш конфиг
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE); //содаем всегда новый индекс
            IndexWriter writer = new IndexWriter(FSD, iwc); //создаем объект IndexWriter - или по другому индекс

            for(Product product: productFacade.findAll()){
                Document doc = new Document(); //создаем документ
                doc.add(new Field("id","1",Field.Store.YES,Field.Index.NOT_ANALYZED)); //добавляем 1-е поле в документ
                doc.add(new Field("recid",product.getRecid().toString()
                        ,Field.Store.YES,Field.Index.NOT_ANALYZED)); //добавляем 2-е поле в документ
                doc.add(new Field("name",product.getName()
                        ,Field.Store.YES,Field.Index.ANALYZED)); //добавляем 2-е поле в документ
                writer.addDocument(doc); //добавляем документ в индекс

            }

            writer.optimize(); //оптимизируем индекс
            writer.close();  //все закрываем
            FSD.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMessage("Индексация завершена");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
